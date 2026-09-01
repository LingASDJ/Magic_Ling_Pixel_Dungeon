package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

//门板
//五阶，力量需求18
//初始4-20，成长1-4
//初始护甲2-3，成长1-3
//这把武器的攻击会额外附带你最大生命值（20+2*等级）%的真实伤害。
//沉重的武器，你似乎可以把全身的力量都倾注入一击之上。
public class DoorPlank extends MeleeWeapon{
    {
        image = ItemSpriteSheet.SKIN_5;

        hitSound = Assets.Sounds.HIT_CRUSH;
        hitSoundPitch = 1f;

        tier = 5;
    }

    @Override
    public int max(int lvl) { return 20 + lvl * 4; }

    @Override
    public int min(int lvl) { return 4 + lvl; }

    @Override
    public int proc(Char attacker, Char defender, int damage) {
        defender.damage(Math.round((0.2f + 0.02f * buffedLvl()) * attacker.HT), attacker, Char.DamageType.REAL);
        return super.proc(attacker, defender, damage);
    }
}
