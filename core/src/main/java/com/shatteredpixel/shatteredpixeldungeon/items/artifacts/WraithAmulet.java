package com.shatteredpixel.shatteredpixeldungeon.items.artifacts;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ShadowParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.QuickSlotButton;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;

public class WraithAmulet extends Artifact {

    {
        image = ItemSpriteSheet.WRALIPS;
        cooldown = 0;
        charge = Math.min(level()+3, 10);
        partialCharge = 0;
        chargeCap = Math.min(level()+3, 10);
        level = 0;
        levelCap = 10;
        defaultAction = AC_GHOST;
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
        return levelKnown ? Math.round((level()*8)/(float)levelCap): 0;
    }


    @Override
    public void execute(Hero hero, String action) {
        super.execute(hero, action);
        if(action.equals(AC_GHOST)){
            if (cooldown > 0) {
                GLog.i(Messages.get(this,"cooddown"));
            } else if(useableBasic()) {
                if(this.isEquipped(Dungeon.hero)){
                    if(this.charge > 0) {
                        exp += 5;
                        Buff.affect(Dungeon.hero, Invisibility.class, Invisibility.DURATION);
                        GLog.i(Messages.get(this,"ghost"));
                        cooldown = 12 - (level / 2);
                        charge--;
                    } else {
                        GLog.i(Messages.get(this,"nochareup"));
                    }
                } else {
                    GLog.i(Messages.get(this,"noequip"));
                }
            } else {
                GLog.i(Messages.get(this,"whoareyou"));
            }
        } else if (action.equals(AC_ASSASSINATE)) {
            if(this.charge >= 5){
                GameScene.selectCell(porter);
                charge-=5;
            } else {
                GLog.i(Messages.get(this,"nochareup"));
            }
        }
    }

    protected boolean useableBasic(){
            return true;
    }

    protected boolean useable(){
            return true;
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        if (isEquipped(hero))
            actions.add(AC_GHOST);
        if (isEquipped(hero) && charge >= 7)
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
            if (charge < chargeCap && !cursed && useableBasic()) {
                partialCharge += 1 / (150f - (chargeCap - charge) * 15f);

                if (partialCharge >= 1) {
                    partialCharge--;
                    charge++;

                    if (charge == chargeCap) {
                        partialCharge = 0;
                    }
                }
            } else if(cursed){
                if(Random.Int(40) == 0){
                    GLog.i(Messages.get(this,"cursed"));
                }
            }

            if(exp > level * 50){
                exp = 0;
                if(level < levelCap){
                    //I must add Complete WraithAmulet
                    //Ok,Ling will Complete WraithAmulet
                    upgrade();
                    exp += level * 50;
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
        return Messages.get(this, "desc");
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
            HashSet<Mob> victim = new HashSet<Mob>();
            if (target != null ) {

                if (target == curUser.pos) {
                    GLog.i(Messages.get(this,"select"));
                    return;
                }

                QuickSlotButton.target(Actor.findChar(target));
                if(Actor.findChar(target) != null){
                    if(Level.distancex(Dungeon.hero.pos, target) == 1) {
                        final WraithAmulet amulet = (WraithAmulet) Item.curItem;
                        amulet.charge--;
                        amulet.exp += 10;
                        Objects.requireNonNull(Actor.findChar(target)).damage(Objects.requireNonNull(Actor.findChar(target)).HT, WraithAmulet.class);
                        Dungeon.hero.pos = target;
                        Dungeon.hero.sprite.emitter().start(ShadowParticle.UP, 0.05f, 10);
                        ScrollOfTeleportation.appear(Dungeon.hero, target);
                        Dungeon.observe();
                        GLog.i(Messages.get(this,"killmobs"));
                    }
                    } else {
                    GLog.i(Messages.get(this,"far"));
                    }
                } else {
                    GLog.i(Messages.get(this,"notthere"));
                }
        }
    };

}

