package com.shatteredpixel.shatteredpixeldungeon.items.jokings;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Bomb;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

// 击杀久住、久住跑路之后会掉落的物品之一：伪装成丰收号角的炸弹
// 【塞满黑火药的丰收号角】

public class HornOfPlentyBomb extends Bomb {

    {
        image = ItemSpriteSheet.ARTIFACT_HORN1;   // 贴图借用丰收号角
        stackable = false;                        // 不可堆叠
    }

    @Override
    public String desc() {
        return Messages.get(this, "desc", 4 + Dungeon.scalingDepth(), 12 + 3*Dungeon.scalingDepth()) + "\n\n" + Messages.get(Bomb.class, "desc_burning");
    }
}
