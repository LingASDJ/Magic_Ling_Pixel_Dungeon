package com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class RedBlock extends MissileWeapon {

    {
        image = ItemSpriteSheet.RED_BLOOD;
        hitSound = Assets.Sounds.HIT_STAB;
        hitSoundPitch = 0.8f;

        DLY = 2f;

        tier = 3;
        baseUses = 8;
        sticky = false;
    }

    @Override
    public int min(int lvl) {
        return 8 + (level() * 2);
    }

    @Override
    public int max(int lvl) {
        return 18 + (level() * 6) ;
    }

    @Override
    public int proc(Char attacker, Char defender, int damage ) {
        Buff.affect( defender, Vertigo.class,3+level()/4f);

        return super.proc( attacker, defender, damage );
    }

}
