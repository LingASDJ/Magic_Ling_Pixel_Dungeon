package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bleeding;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Light;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Scorpio;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DwarfSoliderSprite;
import com.watabou.utils.Random;

public class DwarfFuze extends Scorpio {
    {
        spriteClass = DwarfSoliderSprite.DwarfFuzeSprite.class;

        HP = HT =50;
        defenseSkill = 24;
        viewDistance = Light.DISTANCE;

        EXP = 14;
        maxLvl = -27;

        loot = Generator.Category.ARMOR;
        lootChance = 0.5f;

        properties.add(Char.Property.MINIBOSS);
    }
    @Override
    public int drRoll() {
        return super.drRoll() + Random.NormalIntRange(0, 5);
    }
    @Override
    protected boolean getCloser( int target ) {
        return super.getCloser( target );
    }
    @Override
    public int damageRoll() {
        return Random.NormalIntRange(10, 30 );
    }
    @Override
    public int attackProc(Char enemy, int damage ) {
        damage = super.attackProc( enemy, damage );
        if (Random.Int( 4,20 ) < 9) {
            Buff.affect( enemy, Bleeding.class ).set((damage/2f));
        }
        return damage;
    }
}
