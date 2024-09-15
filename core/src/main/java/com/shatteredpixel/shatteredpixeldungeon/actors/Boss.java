package com.shatteredpixel.shatteredpixeldungeon.actors;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Conducts;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ChampionEnemy;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Corruption;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.IceCyanBlueSquareCoin;
import com.shatteredpixel.shatteredpixeldungeon.items.KingGold;
import com.shatteredpixel.shatteredpixeldungeon.items.KingsCrown;
import com.shatteredpixel.shatteredpixeldungeon.items.TengusMask;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRetribution;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfPsionicBlast;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Grim;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Calendar;

abstract public class Boss extends Mob {
        {
            if(Statistics.difficultyDLCEXLevel==4){
                immunities.add(Terror.class);
            }
        }
        protected boolean SprintableModeBoolean = (Statistics.bossRushMode || Dungeon.isDLC(Conducts.Conduct.DEV));

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
            immunities.add(Corruption.class);
        }

    /**
     *
     * @param min 最小伤害
     * @param max 最大伤害
     * @param acc 命中率
     * @param eva 闪避率
     * @param ht 生命值
     * @param mid 最小防御
     * @param mad 最大防御
     */
        protected void initBaseStatus(float min, float max, float acc, float eva, float ht, float mid, float mad) {
            baseMin = min;
            baseMax = max;
            baseAcc = acc;
            baseEva = eva;
            baseHT = ht;
            baseMinDef = mid;
            baseMaxDef = mad;
        }

    public void die( Object cause ) {
        super.die(cause);


            ArrayList<IceCyanBlueSquareCoin> ice = hero.belongings.getAllItems(IceCyanBlueSquareCoin.class);
            if(ice != null){
                for (IceCyanBlueSquareCoin w : ice.toArray(new IceCyanBlueSquareCoin[0])){
                    w.detachAll(hero.belongings.backpack);
                    if(SPDSettings.Cheating()){
                        //盗版蓝币只有正版的十分之一
                        SPDSettings.iceCoin(w.quantity/10);
                    } else {
                        SPDSettings.iceCoin(w.quantity);
                    }

                }
            }

            if(Statistics.RandMode && Dungeon.depth == 10){
                Dungeon.level.drop(new TengusMask(),pos);
            }

            if(Statistics.RandMode && Dungeon.depth == 20){
                Dungeon.level.drop(new KingsCrown(),pos);
            }

            if(Statistics.bossRushMode){
                Dungeon.level.drop(new KingGold(Random.NormalIntRange(3+Dungeon.depth/5,5+Dungeon.depth/5)),pos);
            }

            final Calendar calendar = Calendar.getInstance();
            boolean holiday = false;

            if (calendar.get(Calendar.MONTH) == Calendar.MAY) {
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                if (dayOfMonth >= 2 && dayOfMonth <= 20)
                    holiday = true;
            }

            if(!Statistics.bossRushMode){
                if(Challenges.activeChallenges()>9){

                    if(holiday){
                        Dungeon.level.drop(new IceCyanBlueSquareCoin(((5*(Dungeon.depth/5)) * (Challenges.activeChallenges() / 5)) * 2),pos);
                    } else {
                        Dungeon.level.drop(new IceCyanBlueSquareCoin(((5*(Dungeon.depth/5)) * (Challenges.activeChallenges() / 5))),pos);
                    }

                } else {

                    if(holiday){
                        Dungeon.level.drop(new IceCyanBlueSquareCoin(5*(Dungeon.depth/5) * 2),pos);
                    } else {
                        Dungeon.level.drop(new IceCyanBlueSquareCoin(5*(Dungeon.depth/5)),pos);
                    }
                }
            }

    }

    @Override
    public float attackDelay() {
        if (Statistics.gameNight) {
            return 0.75f;
        } else {
            return 1f;
        }
    }

        protected void initStatus(int exp) {
            defenseSkill = Math.round(baseEva); //闪避率
            EXP = exp; //经验值
            HP = HT = Math.round(baseHT); //生命值


        }

        @Override
        public int damageRoll() {
            if(Statistics.gameNight){
                return Math.round(Random.NormalFloat( baseMin*1.25f, baseMax*1.25f )); //随机伤害
            } else {
                return Math.round(Random.NormalFloat( baseMin, baseMax )); //随机伤害
            }

        }

        @Override
        public int attackSkill( Char target ) {
            return Math.round(baseAcc); //命中率
        }

        @Override
        public int drRoll() {

            return Math.round(Random.NormalFloat(baseMinDef, baseMaxDef)); //随机防御
        }
    private boolean first=false;

    private static final String FIRST = "first";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(FIRST, first);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        first = bundle.getBoolean(FIRST);
    }

    private void RollCS(){
        Class<?extends ChampionEnemy> buffCls;
        switch (Random.Int(9)){
            case 0: default:    buffCls = ChampionEnemy.Blazing.class;      break;
            case 1:             buffCls = ChampionEnemy.Projecting.class;   break;
            case 2:             buffCls = ChampionEnemy.AntiMagic.class;    break;
            case 3:             buffCls = ChampionEnemy.Giant.class;        break;
            case 4:             buffCls = ChampionEnemy.Blessed.class;      break;
            case 5:             buffCls = ChampionEnemy.Growing.class;      break;
            case 6:             buffCls = ChampionEnemy.Halo.class;      	break;
            case 7:             buffCls = ChampionEnemy.DelayMob.class;     break;
        }
        Buff.affect(this, buffCls);
        this.state = this.WANDERING;
    }

    private void RollEX(){
        Class<?extends ChampionEnemy> buffCls2;
        switch (Random.Int(5)){
            case 0: default:    buffCls2 = ChampionEnemy.Middle.class;      break;
            case 1:             buffCls2 = ChampionEnemy.Bomber.class;      break;
            case 2:             buffCls2 = ChampionEnemy.Sider.class;       break;
            case 3:             buffCls2 = ChampionEnemy.LongSider.class;   break;
            case 4:             buffCls2 = ChampionEnemy.Big.class;         break;
        }
        Buff.affect(this, buffCls2);
        this.state = this.WANDERING;
    }



    @Override
    public void notice() {
        super.notice();
        if (Statistics.bossRushMode && !(Dungeon.depth == 2 || Dungeon.depth == 4 || Dungeon.depth == 24 || Dungeon.depth == 27)){
            if(!first){
                if(Statistics.difficultyDLCEXLevel >= 3){
                    RollEX();
                    RollCS();
                } else if (Statistics.difficultyDLCEXLevel == 2){
                    RollCS();
                }
                first = true;
            }
        } else if (Statistics.bossRushMode && Statistics.difficultyDLCEXLevel == 3) {
            Buff.affect(this, ChampionEnemy.Halo.class);
            Buff.affect(this, ChampionEnemy.Sider.class);
        } else if (Statistics.bossRushMode && Statistics.difficultyDLCEXLevel == 2) {
            Buff.affect(this, ChampionEnemy.Halo.class);
        }
    }


}
