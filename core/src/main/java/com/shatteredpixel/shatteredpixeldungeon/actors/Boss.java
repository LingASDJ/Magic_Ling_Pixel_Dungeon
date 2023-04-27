package com.shatteredpixel.shatteredpixeldungeon.actors;

import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRetribution;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfPsionicBlast;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Grim;
import com.watabou.utils.Random;

public class Boss extends Mob {

        protected static float baseMin; //最小伤害
        protected static float baseMax; //最大伤害
        protected static float baseAcc; //命中率
        protected static float baseEva; //闪避率
        protected static float baseHT; //生命值
        protected static float baseMinDef; //最小防御
        protected static float baseMaxDef; //最大防御

        protected void initProperty() {
            properties.add(Property.BOSS); //添加BOSS属性
            immunities.add(Grim.class); //添加Grim类
            immunities.add(ScrollOfPsionicBlast.class); //添加ScrollOfPsionicBlast类
            immunities.add(ScrollOfRetribution.class); //添加ScrollOfRetribution类
        }

        protected void initBaseStatus(float min, float max, float acc, float eva, float ht, float mid, float mad) {
            baseMin = min; //最小伤害
            baseMax = max; //最大伤害
            baseAcc = acc; //命中率
            baseEva = eva; //闪避率
            baseHT = ht; //生命值
            baseMinDef = mid; //最小防御
            baseMaxDef = mad; //最大防御
        }

        protected void initStatus(int exp) {
            defenseSkill = Math.round(baseEva); //闪避率
            EXP = exp; //经验值
            HP = HT = Math.round(baseHT); //生命值
        }

        @Override
        public int damageRoll() {
            return Math.round(Random.NormalFloat( baseMin, baseMax )); //随机伤害
        }

        @Override
        public int attackSkill( Char target ) {
            return Math.round(baseAcc); //命中率
        }

        @Override
        public int drRoll() {
            return Math.round(Random.NormalFloat(baseMinDef, baseMaxDef)); //随机防御
        }
}
