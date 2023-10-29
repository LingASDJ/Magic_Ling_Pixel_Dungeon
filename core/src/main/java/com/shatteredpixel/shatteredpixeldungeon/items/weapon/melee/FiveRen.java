package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Corrosion;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

public class FiveRen extends MeleeWeapon {

    {
        image = ItemSpriteSheet.FIVEREN;
        hitSound = Assets.Sounds.HIT_STAB;
        hitSoundPitch = 0.9f;

        tier = 5;
        DLY = 1.5f; //0.67x speed
        RCH = 2;    //extra reach
    }

    @Override
    public int proc(Char attacker, Char defender, int damage) {
        switch (Random.Int(7)) {
            case 0:case 1:case 2:case 3:case 4:
            default:
                return super.proc(attacker, defender, damage);
            case 5:case 6:case 7:
                if (Random.Int(10)==1) {
                    Buff.affect(defender, Corrosion.class).set(5f, Dungeon.depth/3);
                    if (Dungeon.level.heroFOV[defender.pos]) {
                        defender.sprite.centerEmitter().start(Speck.factory(Speck.HEART), 0.2f, 5);
                        Sample.INSTANCE.play(Assets.Sounds.CHARMS);
                    }
                }
                return super.proc(attacker, defender, damage);
        }
    }

    @Override
    public int max(int lvl) {
        return  Math.round(6.67f*(tier+1)) +    //20 base, up from 15
                lvl*Math.round(1.33f*(tier+1)); //+4 per level, up from +3
    }

}

