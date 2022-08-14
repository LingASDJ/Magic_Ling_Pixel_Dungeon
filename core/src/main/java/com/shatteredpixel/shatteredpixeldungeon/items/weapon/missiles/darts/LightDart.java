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
                switch (Random.Int(7)) {
                    case 0:
                    default:
                        Buff.affect(defender, ChampionEnemy.Blazing.class);
                        break;
                    case 1:
                        Buff.affect(defender, ChampionEnemy.Projecting.class);
                        break;
                    case 2:
                        Buff.affect(defender, ChampionEnemy.Blessed.class);
                        break;
                    case 3:
                        Buff.affect(defender, ChampionEnemy.Halo.class);
                        break;
                }
            } else if (defender instanceof Hero){
                GLog.i( Messages.get(AikeLaier.class, "refreshed") );
                Buff.affect(defender, ChampionHero.Light.class, ChampionHero.DURATION/2);
            }
        }
        return super.proc( attacker, defender, damage );
    }

}
