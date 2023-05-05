package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.MagicFlameParticle;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Bundle;


/**
 * 魔法火把
 */
public class MagicTorch extends MeleeWeapon {

    /**
     * 重写父类的属性
     * @param tier 等级
     * @param image 图片
     * @param ACC 命中率
     */ {
        image = ItemSpriteSheet.MAGIC_TORCH;
        tier = 3;
        ACC = 0.8f;
    }

    /**
     * 重写父类的方法
     *
     * @param lvl 等级
     * @return 最小伤害
     */
    @Override
    public int max(int lvl) {
        return 5 * (tier) +    //base
                lvl * (tier + 2);   //level scaling
    }

    /**
     * 重写父类的方法
     *
     * @param lvl 等级
     * @return 最大伤害
     */
    @Override
    public int STRReq(int lvl) {
        return (6 + tier * 3) - (int) (Math.sqrt(8 * lvl + 1) - 1) / 2;
        //19 base strength req, up from 18
    }

    /**
     * 重写父类的方法
     *
     * @param hero   英雄
     * @param action 动作
     */
    @Override
    public void execute(Hero hero, String action) {
        super.execute(hero, action);
        //当英雄使用魔法火把时，给英雄添加一个光环buff，光环buff会对周围的敌人造成伤害，
        // 伤害值为英雄的魔法火把的最大伤害值，持续时间为5回合，每回合造成一次伤害，伤害值为英雄的魔法火把的最大伤害值，光环buff的名称为“光环”
        switch (action) {
            case AC_EQUIP:
                Buff.affect(hero, MagicLight.class).set((100), 1);
                break;
            case AC_UNEQUIP:
                doUnequip(hero, true);
                Buff.detach(hero, MagicLight.class);
                break;
            case AC_THROW:
                super.doThrow(hero);
                Buff.detach(hero, MagicLight.class);
                break;
        }
    }

    /**
     * 重写父类的proc方法
     *
     * @param attacker 攻击者
     * @param defender 防御者
     * @param damage   伤害
     */
    @Override
    public int proc(Char attacker, Char defender, int damage) {
        /**
         * @param defender 防御者
         * @param Burning.class 伤害类型
         * @param hero 伤害来源
         * @param 4f + level() / 5f 伤害值
         */
        Buff.affect(defender, Burning.class).reignite(hero, 4f + level() / 5f);

        return super.proc(attacker, defender, damage);
    }


    public static class MagicLight extends FlavourBuff {
        /**
         * @param target 目标来源
         * @param type 类型
         * @param DISTANCE 照明范围
         * @param act 停止回滚
         */
        {
            type = buffType.POSITIVE;
        }

        @Override
        public void detach() {
            target.viewDistance = Dungeon.level.viewDistance;
            Dungeon.observe();
            super.detach();
        }

        public static int level = 0;
        private int interval = 1;
        public static final int DISTANCE	= 4;
        @Override
        public boolean attachTo( Char target ) {
            if (super.attachTo( target )) {
                if (Dungeon.level != null) {
                    target.viewDistance = Math.max( Dungeon.level.viewDistance, DISTANCE );
                    Dungeon.observe();
                }
                return true;
            } else {
                return false;
            }
        }
        MagicTorch item = Dungeon.hero.belongings.getItem(MagicTorch.class);
        @Override
        public boolean act() {
            if (target.isAlive()) {

                spend(interval);
                if (level <= 0 || hero.belongings.weapon != item) {
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
            return 1f;
        }

        @Override
        public String toString() {
            return Messages.get(this, "name");
        }

        @Override
        public void fx(boolean on) {
            if (on) target.sprite.add(CharSprite.State.ILLUMINATED);
            else target.sprite.remove(CharSprite.State.ILLUMINATED);
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

        @Override
        public void tintIcon(Image icon) {
            icon.hardlight(0x808080);
        }

        @Override
        public int icon() {
            return BuffIndicator.LIGHT;
        }

    }

    @Override
    public Emitter emitter() {
        Emitter emitter = new Emitter();
        emitter.pos(12.5f, 3);
        emitter.fillTarget = false;
        emitter.pour(StaffParticleFactory, 0.1f);
        return emitter;
    }


    private final Emitter.Factory StaffParticleFactory = new Emitter.Factory() {
        /**
         * @param emitter 目标来源
         * @param index 特效来源
         * @param x,y 位置
         */
        @Override
        public void emit( Emitter emitter, int index, float x, float y ) {
            ((MagicFlameParticle)emitter.recycle( MagicFlameParticle.class )).reset( x, y+3 );
        }
        @Override
        public boolean lightMode() {
            return true;
        }
    };


}
