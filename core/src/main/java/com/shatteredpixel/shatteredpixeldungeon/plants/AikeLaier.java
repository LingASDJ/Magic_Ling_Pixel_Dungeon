package com.shatteredpixel.shatteredpixeldungeon.plants;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ChampionEnemy;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ChampionHero;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Healing;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class AikeLaier extends Plant {

    {
        image = 17;
        seedClass = AikeLaier.Seed.class;
    }

    @Override
    public void activate( Char ch ) {

        if (ch != null) {
            if (ch instanceof Mob) {
                Class<? extends ChampionEnemy> championClass;
                switch (Random.Int(4)) {
                    case 0:
                    default:
                        championClass = ChampionEnemy.Blazing.class;
                        break;
                    case 1:
                        championClass = ChampionEnemy.Projecting.class;
                        break;
                    case 2:
                        championClass = ChampionEnemy.Blessed.class;
                        break;
                    case 3:
                        championClass = ChampionEnemy.Halo.class;
                        break;
                }
                Buff.affect(ch, championClass);

                //我方(友方)怪物获得的精英强化改为限时效果
                if (ch.alignment == Char.Alignment.ALLY) {
                    ChampionTimeLimit limit = Buff.affect(ch, ChampionTimeLimit.class, 500);
                    limit.championClass = championClass;
                }
            } else if (ch instanceof Hero){
                GLog.i( Messages.get(this, "refreshed") );
                PotionOfHealing.cure(ch);
                Buff.affect(ch, ChampionHero.Light.class, ChampionHero.DURATION/5);
                Buff.affect(ch, Healing.class).setHeal((int)Math.ceil(ch.HT * 0.1f), 5f, 6);
            }
        }
    }


    //限时移除友方怪物身上的精英强化
    public static class ChampionTimeLimit extends FlavourBuff {
        public Class<? extends ChampionEnemy> championClass;

        @Override
        public boolean act() {
            if (championClass != null) {
                Buff.detach(target, championClass);
            }
            super.act();
            return true;
        }

        private static final String CHAMPION_CLASS = "championClass";

        @Override
        public void storeInBundle(Bundle bundle) {
            super.storeInBundle(bundle);
            bundle.put(CHAMPION_CLASS, championClass);
        }

        @Override
        public void restoreFromBundle(Bundle bundle) {
            super.restoreFromBundle(bundle);
            championClass = bundle.getClass(CHAMPION_CLASS);
        }
    }
    public static class Seed extends Plant.Seed {
        {
            image = ItemSpriteSheet.SEED_AIKELAIER;

            plantClass = AikeLaier.class;
        }
    }
}
