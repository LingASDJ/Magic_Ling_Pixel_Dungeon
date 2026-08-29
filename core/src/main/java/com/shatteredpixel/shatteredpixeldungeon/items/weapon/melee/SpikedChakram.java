package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.utils.Random;

//尖刺轮盘
//二阶，力量需求12
//初始2-15，成长1-3
//当你拥有护盾时，获得1-3+等级点额外伤害与护甲。
//当车轮完美，当车舵完蛋。

public class SpikedChakram extends MeleeWeapon{
    {
        image = ItemSpriteSheet.STICK_CIRCLE;

        hitSound = Assets.Sounds.HIT_CRUSH;
        hitSoundPitch = 1f;

        tier = 2;
    }

    @Override
    public int max(int lvl) {
        return  15 + lvl*3;
    }
    @Override
    public int min(int lvl) {
        return  2 + lvl;
    }

    // 额外伤害:提高武器提供的伤害掷点上下限 这里是增加一个小伤害值,数学上在平均值和最大最小值上是等价的
    @Override
    public int damageRoll(Char owner) {
        int dmg = super.damageRoll(owner);
        if (owner.shielding() > 0){
            dmg += Random.IntRange(1, 3 + buffedLvl());   // 1~3 + 强化等级
        }
        return dmg;
    }

    // 额外护甲:提高武器提供的防御掷点上下限
    @Override
    public int defenseFactor(Char owner) {
        int def = super.defenseFactor(owner);   // 普通近战武器默认 0
        if (owner.shielding() > 0){
            def += Random.IntRange(1, 3 + buffedLvl());
        }
        return def;
    }

    @Override
    public String statsInfo() {
        // 实时显示护盾加成区间
        return Messages.get(this, "stats_desc", 1, 3 + buffedLvl());
    }
}
