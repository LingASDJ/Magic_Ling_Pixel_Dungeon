package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.lb;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AllyBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Degrade;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SmokeParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.items.LostBackpack;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.BArray;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class BlackSoul extends Mob implements Callback {
    public int gold;
    {
        spriteClass = ShadowSprite.class;

        //与英雄成长阶级相同
        HP = HT = (40 + 5*(hero.lvl-1) + hero.HTBoost)/2;

        immunities.add(AllyBuff.class);

        properties.add(Property.ABYSS);
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
            return hero.defenseSkill(target)+5; //equal to base hero attack skill
        }


    @Override
    protected boolean canAttack( Char enemy ) {
        if(Dungeon.hero.lvl >= 15) {
            return new Ballistica(pos, enemy.pos, MagicMissile.WARD).collisionPos == enemy.pos;
        } else {
            return super.canAttack(enemy);
        }
    }

    @Override
    public void die( Object cause ) {

        super.die( cause );
            if (gold > 0) {
                Dungeon.level.drop( new Gold(gold), pos ).sprite.drop();
            }
            Dungeon.level.drop( new LostBackpack(), pos).sprite.drop( pos );

    }

    public void onZapComplete() {
        zap();
        next();
    }

        @Override
        public int damageRoll() {
            return hero.damageRoll()/2;
        }

        @Override
        public int attackProc( Char enemy, int damage ) {
            damage = super.attackProc( enemy, damage );
            if(Dungeon.hero.lvl == 15) {
                zap();
            }
            if (Random.Int(4) < hero.pointsInTalent(Talent.SHADOW_BLADE)
                    && hero.belongings.weapon() != null){
                return hero.belongings.weapon().proc( this, enemy, damage );
            } else {
                return damage;
            }
        }

    public static class DarkBolt{}
    public static int level = 1;
    private static final float TIME_TO_ZAP	= 2f;

    protected boolean doAttack( Char enemy ) {

        if (Dungeon.level.adjacent( pos, enemy.pos )) {

            return super.doAttack( enemy );

        } else {

            if (sprite != null && (sprite.visible || enemy.sprite.visible)) {
                sprite.zap( enemy.pos );
                return false;
            } else {
                zap();
                return true;
            }
        }
    }

    @Override
    public void call() {
        next();
    }
    private void zap() {
        spend( TIME_TO_ZAP );

        if (hit( this, enemy, true )) {

            if (enemy == Dungeon.hero && Random.Int( 2 ) == 0) {
                Buff.prolong( enemy, Blindness.class, Degrade.DURATION );
                Sample.INSTANCE.play( Assets.Sounds.DEBUFF );
            }

            int dmg = hero.damageRoll()/2;
            enemy.damage( dmg, new DarkBolt() );

            if (enemy == Dungeon.hero && !enemy.isAlive()) {
                Dungeon.fail( getClass() );
                GLog.n( Messages.get(this, "died_kill") );
            }
        } else {
            enemy.sprite.showStatus( CharSprite.NEUTRAL,  enemy.defenseVerb() );
        }
    }

        @Override
        public int drRoll() {
            return hero.drRoll()/2;
        }

        @Override
        public int defenseProc(Char enemy, int damage) {
            return hero.defenseProc( enemy, damage );
        }

        @Override
        public boolean canInteract(Char c) {
            if (super.canInteract(c)){
                return true;
            } else return Dungeon.level.distance(pos, c.pos) <= hero.pointsInTalent(Talent.PERFECT_COPY);
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

