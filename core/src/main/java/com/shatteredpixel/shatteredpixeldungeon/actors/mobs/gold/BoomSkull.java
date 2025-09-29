package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.gold;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;
import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.level;
import static com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfBlastWave.throwChar;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BlastParticle;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class BoomSkull extends GoldMob {
    {
        HP = HT = 15;

        spriteClass = SkullSprite.class;

        defenseSkill = 0;

        viewDistance = 30;

        flying = true;

    }

    @Override
    protected boolean act() {
        beckon(hero.pos);
        return super.act();
    }

    @Override
    public boolean attack(Char enemy, float dmgMulti, float dmgBonus, float accMulti,DamageType type) {
        die(Hero.class);
        return super.attack(enemy, dmgMulti, dmgBonus, accMulti, type);
    }

    @Override
    public void die(Object cause) {
        super.die(cause);
        int cell;

        for (int i : PathFinder.NEIGHBOURS9){
            cell = pos + i;
            Char ch = Char.findChar(cell);
            if (ch!=null){
                if (ch.alignment == Alignment.ALLY){
                    //friends receive 0 damage
                } else {
                    ch.damage (Math.round(damageRoll()) - ch.drRoll(),this);
                    Ballistica trajectory = new Ballistica(ch.pos, ch.pos + i, Ballistica.MAGIC_BOLT);
                    throwChar(ch, trajectory, 4, false, true, getClass());
                };
                if (ch.equals(enemy)){
                    Buff.affect(ch, Vertigo.class, 3);
                }
            }
            if (level.heroFOV[cell]) {
                CellEmitter.center(cell).burst(BlastParticle.FACTORY, 5);
            }
        }
    }

    @Override
    public int attackSkill(Char target) {
        return 1000;
    }

    @Override
    public int damageRoll() {
        return Random.Int(hero.lvl*2 + 4, hero.lvl*3 + 5);
    }
}
