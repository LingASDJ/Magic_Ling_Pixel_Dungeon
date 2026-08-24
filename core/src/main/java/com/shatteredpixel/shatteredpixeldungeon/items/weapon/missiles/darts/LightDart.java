package com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.darts;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ChampionEnemy;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ChampionHero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.AikeLaier;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Random;

public class LightDart extends TippedDart {

    {
        image = ItemSpriteSheet.LIGT_DART;
    }

    @Override
    public int proc( Char attacker, Char defender, int damage ) {
        if (defender != null) {
            if (defender instanceof Mob) {
                Class<? extends ChampionEnemy> championClass;
                switch (Random.Int(7)) {
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
                Buff.affect(defender, championClass);

                //我方(友方)怪物获得的精英强化改为限时效果
                if (defender.alignment == Char.Alignment.ALLY) {
                    AikeLaier.ChampionTimeLimit limit = Buff.affect(defender, AikeLaier.ChampionTimeLimit.class, 500);
                    limit.championClass = championClass;
                }
            } else if (defender instanceof Hero){
                GLog.i( Messages.get(AikeLaier.class, "refreshed") );
                Buff.affect(defender, ChampionHero.Light.class, ChampionHero.DURATION/2);
            }
        }
        return super.proc( attacker, defender, damage );
    }

}
