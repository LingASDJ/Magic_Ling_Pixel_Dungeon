package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;


import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;


/*
* 钻石制成的奇特大剑，但因为剧烈碰撞而破碎了。
* 剑身残留着钻石宝箱王的魔力，因此对其他魔力有着莫名的亲和性。
* 相比于其他武器，这把武器会随着等级的提升增加更多伤害。
* 这把武器攻击速度极慢。
*/

public class SDBSword extends MeleeWeapon {

    {
        tier = 4;
        DLY = 1.2f;

        if (this.level() >= 6) {
            image = ItemSpriteSheet.HHBlade;
        } else {
            image = ItemSpriteSheet.SDBlade;
        }
        ACC = 3f; //20% boost to accuracy
        DLY = 2f; //3x speed

    }

    public int image() {
        if (this.level() >= 6) {
            image = ItemSpriteSheet.HHBlade;
        } else {
            image = ItemSpriteSheet.SDBlade;
        }
        return image;
    }

    public int min() {
        return 6 + level();
    }

    public int max() {
        return 20 + level() * 9;
    }

}