/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2021 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.shatteredpixel.shatteredpixeldungeon.items.potions;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.HalomethaneFire;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.ElixirOfDragonKing;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfDragonKingBreath;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;

public class PotionOfLiquidFlameX extends Potion {

    {
        icon = ItemSpriteSheet.Icons.POTION_BLUE;
    }

    @Override
    public void shatter( int cell ) {

        if (Dungeon.level.heroFOV[cell]) {
            identify();

            splash( cell );
            Sample.INSTANCE.play( Assets.Sounds.SHATTER );
            Sample.INSTANCE.play( Assets.Sounds.BURNING );
        }

        for (int offset : PathFinder.NEIGHBOURS9){
            if (!Dungeon.level.solid[cell+offset]) {

                GameScene.add(Blob.seed(cell + offset, 7, HalomethaneFire.class));

            }
        }
    }

    /**
     * 龙王药水配方内部类
     * 继承自SimpleRecipe，定义了制作龙王药水的配方
     */
    public static class Recipe extends com.shatteredpixel.shatteredpixeldungeon.items.Recipe.SimpleRecipe {

        // 初始化代码块，设置配方所需材料
        {
            // 需要的材料：龙王呼吸药水
            inputs =  new Class[]{PotionOfDragonKingBreath.class};
            // 材料数量：1瓶
            inQuantity = new int[]{1};

            cost = 8;

            // 产品：龙王药水
            output = ElixirOfDragonKing.class;
            // 产品数量：1瓶
            outQuantity = 1;
        }

    }

    @Override
    public int value() {
        return isKnown() ? 30 * quantity : super.value();
    }
}
