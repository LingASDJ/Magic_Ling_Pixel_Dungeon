package com.shatteredpixel.shatteredpixeldungeon.items.artifacts;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LockedFloor;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mimic;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NPC;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ShadowParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfEnergy;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.QuickSlotButton;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class WraithAmulet extends Artifact {

    {
        image = ItemSpriteSheet.WRALIPS;
        cooldown = 0;
        levelCap = 10;
        charge = Math.min(level()+1, 10);
        partialCharge = 0;
        charge = Math.min(level()+1, 10);
        level = 0;
        defaultAction = AC_GHOST;
    }

    @Override
    public boolean doEquip(Hero hero) {
        if (super.doEquip(hero)){
            if (cursed) {
                Buff.affect(hero, CursedAmulet.class).set( (100), 1 );
            }
            return true;
        } else {
            return false;
        }
    }

    private static final String AC_GHOST = "ghost";
    private static final String AC_ASSASSINATE = "darkkill";

    @Override
    public Item upgrade(){
        chargeCap++;
        return super.upgrade();
    }

    @Override
    public int visiblyUpgraded() {
        return levelKnown ? Math.round((level()*10)/(float)levelCap): 0;
    }


    @Override
    public void execute(Hero hero, String action) {
        super.execute(hero, action);
        if(action.equals(AC_GHOST) && !cursed){
            if (cooldown > 0) {
                GLog.w(Messages.get(this,"cooddown"));
            } else if(useableBasic()) {
                if(this.isEquipped(Dungeon.hero)){
                    if(this.charge > 0) {
                        exp += level()>5 ? 20 : 40;
                        Buff.affect(Dungeon.hero, Invisibility.class, Invisibility.DURATION/2);
                        GLog.p(Messages.get(this,"ghost"));
                        cooldown = 40 - (level);
                        charge--;
                    } else {
                        GLog.i(Messages.get(this,"nochareup"));
                    }
                } else {
                    GLog.i(Messages.get(this,"noequip"));
                }
            } else {
                GLog.n(Messages.get(this,"whoareyou"));
            }
        } else if (action.equals(AC_ASSASSINATE) && !cursed) {
            if (cooldown > 0) {
                GLog.w(Messages.get(this, "cooddown"));
            } else if(this.charge >= 5){
                GameScene.selectCell(porter);
            } else {
                GLog.i(Messages.get(this,"nochareup"));
            }
        } else if(cursed) {
            GLog.i(Messages.get(this,"must_nocursed"));
        }
    }

    protected boolean useableBasic(){
            return true;
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        if (isEquipped(hero) && !cursed)
            actions.add(AC_GHOST);
        if (isEquipped(hero) && charge >= 6 && !cursed)
            actions.add(AC_ASSASSINATE);
        return actions;
    }

    @Override
    protected ArtifactBuff passiveBuff() {
        return new WraithRecharge();
    }

    public class WraithRecharge extends ArtifactBuff{
        @Override
        public boolean act() {
            LockedFloor lock = target.buff(LockedFloor.class);


            if (charge < chargeCap && !cursed && useableBasic() && (lock == null || lock.regenOn())) {
                partialCharge += 1 / (150f - (chargeCap - charge) * 15f);
                float chargeGain = 1 / (90f - (chargeCap - charge)*3f);
                chargeGain *= RingOfEnergy.artifactChargeMultiplier(target);
                partialCharge += chargeGain;
                if (partialCharge >= 1) {
                    partialCharge--;
                    charge++;

                    if (charge == chargeCap) {
                        partialCharge = 0;
                    }
                }
            } else if(cursed){
                int level = level() == 0 ? 1 : level();
                hero.sprite.showStatus(CharSprite.NEGATIVE, Messages.get(this,"cursed"));
                hero.damage(Random.Int(4*level, 6*level), this);
                if (!hero.isAlive()) {
                    Dungeon.fail(getClass());
                    GLog.n(Messages.get(this, "ondeath"));
                }
                spend(90f);
            }

            if(exp > level * 50){
                exp = 0;
                if(level < levelCap){
                    //I must add Complete WraithAmulet
                    //Ok,Ling will Complete WraithAmulet
                    upgrade();
                    exp += level * 38;
                    GLog.p(Messages.get(this,"ghoststong"));
                }
            }

            if (cooldown > 0)
                cooldown--;

            updateQuickslot();
            spend(TICK);
            return true;
        }
    }


    @Override
    public String desc() {
        String result = Messages.get(this, "desc");

        if (isEquipped(Dungeon.hero)) {
            if (cursed) {
                Buff.affect(hero, CursedAmulet.class).set( (100), 1 );
                result += "\n\n" + Messages.get(this, "cursed");
            }
        }

        return result;
    }
    public int getCharge(){
        return this.charge;
    }
    protected static CellSelector.Listener porter = new CellSelector.Listener() {
        @Override
        public String prompt() {
            return Messages.get(this,"selectkill");
        }
        @Override
        public void onSelect(Integer target) {

            if (target != null ) {

                if (target == curUser.pos) {
                    GLog.i(Messages.get(this, "select"));
                    return;
                }

                QuickSlotButton.target(Actor.findChar(target));
                Char enemy = Actor.findChar(target);
                if (!(((Statistics.boss_enhance & 0x2) != 0 || Statistics.mimicking) && Dungeon.depth==10)) {
                    if (hero.rooted || Dungeon.level.distance(hero.pos, target) < 3) {
                        if(enemy != null && !(enemy instanceof NPC)){
                            final WraithAmulet amulet = (WraithAmulet) Item.curItem;
                            amulet.exp += 40;
                            hero.pos = target;
                            if (enemy.properties().contains(Char.Property.BOSS)) {
                                enemy.damage(enemy.HT / 4, WraithAmulet.class);
                                GLog.i(Messages.get(this, "killboss"));
                                enemy.sprite.showStatus(CharSprite.NEGATIVE, Messages.get(this, "koss"));
                            } else if (enemy.properties().contains(Char.Property.MIMIC) && enemy.alignment == Char.Alignment.NEUTRAL){
                                for (Mob m: Dungeon.level.mobs){
                                    if(m.state == m.PASSIVE && (m instanceof Mimic)){
                                        GLog.i(Messages.get(this, "notthere2"));
                                        return;
                                    }
                                }
                            } else {
                                enemy.die(true);
                                GLog.i(Messages.get(this, "killmobs"));
                                enemy.sprite.showStatus(CharSprite.NEGATIVE, Messages.get(this,"died"));
                                ScrollOfTeleportation.appear(hero, target);
                                Dungeon.observe();
                                hero.sprite.emitter().start(ShadowParticle.UP, 0.05f, 10);
                                amulet.cooldown = 150 / (amulet.level() / 2);
                                amulet.charge -= 6;
                            }
                        } else {
                            GLog.w(Messages.get(this, "notnpc"));
                        }
                    } else if(Dungeon.level.distance(hero.pos, target) > 3 && enemy !=null) {
                        GLog.w(Messages.get(this, "far"));
                    } else if (hero.rooted) {
                        GLog.n(Messages.get(this, "rooted"));
                    } else if(((Statistics.boss_enhance & 0x2) != 0 || Statistics.mimicking) && Dungeon.depth==10){
                        GLog.n(Messages.get(this, "gold"));
                    } else {
                        GLog.w(Messages.get(this, "notthere"));
                    }
                }
            }
        }
    };

    public static class CursedAmulet extends Buff {

        {
            type = buffType.POSITIVE;
        }

        public static int level = 0;
        private int interval = 1;

        @Override
        public boolean act() {
            if (target.isAlive()) {

                spend(interval);
                if (level <= 0) {
                    detach();
                }

            }

            return true;
        }

        public int level() {
            return level;
        }

        public void set( int value, int time ) {
            //decide whether to override, preferring high value + low interval
            if (Math.sqrt(interval)*level <= Math.sqrt(time)*value) {
                level = value;
                interval = time;
                spend(time - cooldown() - 1);
            }
        }

        @Override
        public float iconFadePercent() {
            if (target instanceof Hero){
                float max = ((Hero) target).lvl;
                return Math.max(0, (max-level)/max);
            }
            return 0;
        }

        @Override
        public String toString() {
            return Messages.get(this, "name");
        }

        @Override
        public String desc() {
            return Messages.get(this, "desc", level, dispTurns(visualcooldown()));
        }

        private static final String LEVEL	    = "level";
        private static final String INTERVAL    = "interval";

        @Override
        public void storeInBundle( Bundle bundle ) {
            super.storeInBundle( bundle );
            bundle.put( INTERVAL, interval );
            bundle.put( LEVEL, level );
        }

        @Override
        public void restoreFromBundle( Bundle bundle ) {
            super.restoreFromBundle( bundle );
            interval = bundle.getInt( INTERVAL );
            level = bundle.getInt( LEVEL );
        }

//    @Override
//    public void tintIcon(Image icon) {
//        icon.hardlight(0x990000);
//    }

        @Override
        public int icon() {
            return BuffIndicator.NONE;
        }


    }

}

