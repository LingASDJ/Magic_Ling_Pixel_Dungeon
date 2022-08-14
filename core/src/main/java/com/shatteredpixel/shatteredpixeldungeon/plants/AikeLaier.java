package com.shatteredpixel.shatteredpixeldungeon.plants;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ChampionEnemy;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ChampionHero;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Healing;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
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
                switch (Random.Int(4)) {
                    case 0:
                    default:
                        Buff.affect(ch, ChampionEnemy.Blazing.class);
                        break;
                    case 1:
                        Buff.affect(ch, ChampionEnemy.Projecting.class);
                        break;
                    case 2:
                        Buff.affect(ch, ChampionEnemy.Blessed.class);
                        break;
                    case 3:
                        Buff.affect(ch, ChampionEnemy.Halo.class);
                        break;
                }
            } else if (ch instanceof Hero){
                GLog.i( Messages.get(this, "refreshed") );
                PotionOfHealing.cure(ch);
                Buff.affect(ch, ChampionHero.Light.class, ChampionHero.DURATION/5);
                Buff.affect(ch, Healing.class).setHeal(10, 5f, 6);
            }
        }
    }

    public static class Seed extends Plant.Seed {
        {
            image = ItemSpriteSheet.SEED_AIKELAIER;

            plantClass = AikeLaier.class;
        }
    }
}
