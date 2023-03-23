package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.lb;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AllyBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SmokeParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.items.LostBackpack;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.utils.BArray;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class BlackSoul extends Mob {
    public int gold;
    {
        spriteClass = ShadowSprite.class;

        //与英雄成长阶级相同
        HP = HT = (20 + 5*(hero.lvl-1) + hero.HTBoost)/2;

        immunities.add(AllyBuff.class);
    }
        @Override
        protected boolean act() {
            int oldPos = pos;
            boolean result = super.act();
            //partially simulates how the hero switches to idle animation
            if ((pos == target || oldPos == pos) && sprite.looping()){
                sprite.idle();
            }
            return result;
        }

        @Override
        public int attackSkill(Char target) {
            return defenseSkill+5; //equal to base hero attack skill
        }

    @Override
    public void die( Object cause ) {

        super.die( cause );
        if (gold > 0) {
        	Dungeon.level.drop( new Gold(gold), pos ).sprite.drop();
        }
        Dungeon.level.drop( new LostBackpack(), pos).sprite.drop( pos );
    }

        @Override
        public int damageRoll() {
            int damage = Random.NormalIntRange(1, 5);
            int heroDamage = hero.damageRoll();
            heroDamage /= hero.attackDelay(); //normalize hero damage based on atk speed
            heroDamage = Math.round(0.075f * hero.pointsInTalent(Talent.SHADOW_BLADE) * heroDamage);
            if (heroDamage > 0){
                damage += heroDamage;
            }
            return damage;
        }

        @Override
        public int attackProc( Char enemy, int damage ) {
            damage = super.attackProc( enemy, damage );
            if (Random.Int(4) < hero.pointsInTalent(Talent.SHADOW_BLADE)
                    && hero.belongings.weapon() != null){
                return hero.belongings.weapon().proc( this, enemy, damage );
            } else {
                return damage;
            }
        }

        @Override
        public int drRoll() {
            int dr = super.drRoll();
            int heroRoll = hero.drRoll();
            heroRoll = Math.round(0.15f * hero.pointsInTalent(Talent.CLONED_ARMOR) * heroRoll);
            if (heroRoll > 0){
                dr += heroRoll;
            }
            return dr;
        }

        @Override
        public int defenseProc(Char enemy, int damage) {
            damage = super.defenseProc(enemy, damage);
            if (Random.Int(4) < hero.pointsInTalent(Talent.CLONED_ARMOR)
                    && hero.belongings.armor() != null){
                return hero.belongings.armor().proc( enemy, this, damage );
            } else {
                return damage;
            }
        }

        @Override
        public boolean canInteract(Char c) {
            if (super.canInteract(c)){
                return true;
            } else if (Dungeon.level.distance(pos, c.pos) <= hero.pointsInTalent(Talent.PERFECT_COPY)) {
                return true;
            } else {
                return false;
            }
        }

        @Override
        public boolean interact(Char c) {
            if (!hero.hasTalent(Talent.PERFECT_COPY)){
                return super.interact(c);
            }

            //some checks from super.interact
            if (!Dungeon.level.passable[pos] && !c.flying){
                return true;
            }

            if (properties().contains(Property.LARGE) && !Dungeon.level.openSpace[c.pos]
                    || c.properties().contains(Property.LARGE) && !Dungeon.level.openSpace[pos]){
                return true;
            }

            int curPos = pos;

            //warp instantly with the clone
            PathFinder.buildDistanceMap(c.pos, BArray.or(Dungeon.level.passable, Dungeon.level.avoid, null));
            if (PathFinder.distance[pos] == Integer.MAX_VALUE){
                return true;
            }
            appear(this, hero.pos);
            appear(hero, curPos);
            Dungeon.observe();
            GameScene.updateFog();
            return true;
        }

        private static void appear( Char ch, int pos ) {

            ch.sprite.interruptMotion();

            if (Dungeon.level.heroFOV[pos] || Dungeon.level.heroFOV[ch.pos]){
                Sample.INSTANCE.play(Assets.Sounds.PUFF);
            }

            ch.move( pos );
            if (ch.pos == pos) ch.sprite.place( pos );

            if (Dungeon.level.heroFOV[pos] || ch == hero ) {
                ch.sprite.emitter().burst(SmokeParticle.FACTORY, 10);
            }
        }

        private static final String DEF_SKILL = "def_skill";

        @Override
        public void storeInBundle(Bundle bundle) {
            super.storeInBundle(bundle);
            bundle.put(DEF_SKILL, defenseSkill);
        }

        @Override
        public void restoreFromBundle(Bundle bundle) {
            super.restoreFromBundle(bundle);
            defenseSkill = bundle.getInt(DEF_SKILL);
        }
    }

