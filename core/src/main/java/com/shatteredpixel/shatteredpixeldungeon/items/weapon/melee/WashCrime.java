package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Wraith;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Chains;
import com.shatteredpixel.shatteredpixeldungeon.effects.Effects;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.WraithSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class WashCrime extends MeleeWeapon {

    private int killmobs;
    private int spawnmobs;

    @Override
    public int min(int lvl) {
        return  2 + (lvl) + Math.min(killmobs / 2, 26);//level scaling
    }

    @Override
    public int max(int lvl) {
        return  25 + (lvl*6) + Math.min(killmobs / 2, 26);    //level scaling
    }

    {
        image = ItemSpriteSheet.WASH_CRIME;
        hitSound = Assets.Sounds.HIT;
        hitSoundPitch = 1.1f;

        tier = 5;
        RCH = 3;    //lots of extra reach
        DLY = 1.76f;
    }

    @Override
    public int STRReq(int lvl) {
        int req = (9 + tier * 2) - (int)(Math.sqrt(8 * lvl + 1) - 1)/2;
        if (masteryPotionBonus){
            req -= 2;
        }
        return req;
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        killmobs = bundle.getInt("lvl");
        spawnmobs = bundle.getInt("vlv");
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put("lvl", killmobs);
        bundle.put("vlv",spawnmobs);
    }

    @Override
    public int proc(Char attacker, Char defender, int damage ) {
        DLY = DLY < 0.3f ? 0.3f : 2.17f - killmobs*0.0067f; //Slow Speed
        if(killmobs > 100 ){
            killmobs=100;
        }
        // 击杀判定推迟到结算之后，小有代价：之后的if (killmobs>= 2) 实际上要到第三次击杀才会触发
        final Char target = defender;
        Actor.add(new Actor() {
            {
                actPriority = VFX_PRIO;
            }

            @Override
            protected boolean act() {
                if (!target.isAlive() && killmobs < 100) {
                    killmobs++;
                }
                Actor.remove(this);
                return true;
            }
        });

        if (killmobs>= 2) {
            for (int i : PathFinder.NEIGHBOURS9){

                if (!Dungeon.level.solid[attacker.pos + i]
                        && !Dungeon.level.pit[attacker.pos + i]
                        && Actor.findChar(attacker.pos + i) == null
                        && attacker == Dungeon.hero && spawnmobs < 1 && this.level>1 ) {

                    WashCrime.DiedGhost Mob = new WashCrime.DiedGhost();
                    Mob.pos = defender.pos + i;
                    Mob.aggro(defender);
                    GameScene.add(Mob);
                    Dungeon.level.occupyCell(Mob);

                    spawnmobs++;

                    CellEmitter.get(Mob.pos).burst(Speck.factory(Speck.EVOKE), 4);
                    break;
                } else if(!Dungeon.level.solid[attacker.pos + i]
                        && !Dungeon.level.pit[attacker.pos + i]
                        && Actor.findChar(attacker.pos + i) == null
                        && attacker == Dungeon.hero && spawnmobs<2 && this.level>6 ){
                    WashCrime.DiedGhost Mob = new WashCrime.DiedGhost();
                    Mob.pos = defender.pos + i;
                    Mob.aggro(defender);
                    GameScene.add(Mob);
                    Dungeon.level.occupyCell(Mob);

                    spawnmobs++;

                    CellEmitter.get(Mob.pos).burst(Speck.factory(Speck.EVOKE), 4);
                } else if(!Dungeon.level.solid[attacker.pos + i]) {
                    spawnmobs = 0;
                    return super.proc( attacker, defender, damage );
                }
            }


            if (Random.NormalIntRange(1, 4) == 4) {
                Buff.prolong(defender, Cripple.class, 8f);
            } else {
                return super.proc(attacker, defender, damage);
            }

        }

        return super.proc(attacker, defender, damage);
    }

    public class DiedGhost extends Wraith {

        {
            state = WANDERING;
            spriteClass = WraithSprite.class;
            alignment = Alignment.ALLY;
            WANDERING = new Wandering();
            properties.add(Property.UNDEAD);
        }

        public DiedGhost() {

            HP = HT = 5 + killmobs/10;
            defenseSkill = 4 + killmobs/10;
        }

        @Override
        public int drRoll() {
            return Random.Int(3+killmobs/10, 6+killmobs/10);
        }

        public void die(Object cause) {
            super.die(cause);
            spawnmobs=0;
        }

        private class Wandering extends Mob.Wandering{

            @Override
            public boolean act(boolean enemyInFOV, boolean justAlerted) {
                if (!enemyInFOV){
                    destroy();
                    sprite.die();
                    return true;
                } else {
                    return super.act(true, justAlerted);
                }
            }

        }
    }

    @Override
    protected void duelistAbility(Hero hero, Integer target) {

        ArrayList<Char> targets = new ArrayList<>();
        Char closest = null;

        hero.belongings.abilityWeapon = this;
        for (Char ch : Actor.chars()){
            if (ch.alignment == Char.Alignment.ENEMY
                    && !hero.isCharmedBy(ch)
                    && Dungeon.level.heroFOV[ch.pos]
                    && hero.canAttack(ch)){
                targets.add(ch);
                if (closest == null || Dungeon.level.trueDistance(hero.pos, closest.pos) > Dungeon.level.trueDistance(hero.pos, ch.pos)){
                    closest = ch;
                }
            }
        }
        hero.belongings.abilityWeapon = null;

        if (targets.isEmpty()) {
            GLog.w(Messages.get(this, "ability_no_target"));
            return;
        }

        throwSound();
        Char finalClosest = closest;
        hero.sprite.attack(hero.pos, new Callback() {
            @Override
            public void call() {
                beforeAbilityUsed(hero, finalClosest);
                for (Char ch : targets) {
                    //ability does no extra damage
                    hero.attack(ch, 1, 0, Char.INFINITE_ACCURACY);
                    if (!ch.isAlive()){
                        hero.sprite.parent.add(new Chains(hero.sprite.center(), ch.sprite.destinationCenter(),
                                Effects.Type.RED_CHAIN,
                                new Callback() {
                                    public void call() {
                                        onAbilityKill(hero, ch);
                                    }
                                }));
                    }
                }
                Invisibility.dispel();
                hero.spendAndNext(hero.attackDelay());
                afterAbilityUsed(hero);
            }
        });
    }

    @Override
    public String abilityInfo() {
        if (levelKnown){
            return Messages.get(this, "ability_desc", augment.damageFactor(min()), augment.damageFactor(max()));
        } else {
            return Messages.get(this, "typical_ability_desc", min(0), max(0));
        }
    }

    public String upgradeAbilityStat(int level){
        return augment.damageFactor(min(level)) + "-" + augment.damageFactor(max(level));
    }

}
