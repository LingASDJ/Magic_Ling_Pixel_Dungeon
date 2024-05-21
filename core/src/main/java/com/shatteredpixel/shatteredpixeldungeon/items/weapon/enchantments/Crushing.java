package com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BlastParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SmokeParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Bomb;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Random;

public class Crushing extends Weapon.Enchantment {

    private static ItemSprite.Glowing ORANGE = new ItemSprite.Glowing( 0xcc7770 );

    @Override
    public int proc(Weapon weapon, Char attacker, Char defender, int damage ) {
        // lvl 0 - 33%
        // lvl 1 - 50%
        // lvl 2 - 60%
        int level = Math.max( 0, weapon.level() );

        if (Random.Int( level + 12 ) >= 5) {

            if (Random.Int( 4 ) == 0) {
                Buff.prolong( defender, Cripple.class, Random.Float( 1f, 1f + level/2f ) );
            }
            defender.damage( Random.Int((int) (1 * procChanceMultiplier(attacker)), (int) (level + 2 * 1 * procChanceMultiplier(attacker))), this );

            defender.sprite.emitter().burst(BlastParticle.FACTORY, 30 );
            defender.sprite.emitter().burst(SmokeParticle.FACTORY, 4 );

            String[] TXT_RANDOM = {
                    Messages.get(Crushing.class,"kill",defender.name(),attacker.name()),
                    Messages.get(Crushing.class,"kill1",defender.name(),attacker.name()),
                    Messages.get(Crushing.class,"kill3",defender.name(),attacker.name()),
                    Messages.get(Crushing.class,"kill4",defender.name(),attacker.name()),
                    Messages.get(Crushing.class,"kill5",defender.name(),attacker.name()),
                    Messages.get(Crushing.class,"kill6",defender.name(),attacker.name())
            };

            if (Random.Int( 4 ) == 0) {
                damage += level;
                GLog.n(TXT_RANDOM[Random.Int(TXT_RANDOM.length)]);
                new Bomb().explodeHeros(defender.pos);
            }
        }



        return damage;

    }

    @Override
    public ItemSprite.Glowing glowing() {
        return ORANGE;
    }
}