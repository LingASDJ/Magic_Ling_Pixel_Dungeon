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

package com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.HaloFireImBlue;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.FlameParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfDragonKingBreath;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;

/**
 * 龙王药水类，继承自Elixir类
 * 提供龙王火焰光环效果
 */
public class ElixirOfDragonKing extends Elixir {

    // 初始化代码块，设置药水的图像为龙王火焰光环
    {
        image = ItemSpriteSheet.DRAGONKINGHALOFIRE;
    }

    /**
     * 药水效果应用方法
     * @param hero 使用药水的英雄角色
     */
    @Override
    public void apply(Hero hero) {
        // 为英雄添加蓝色火焰光环效果，持续时间为HaloFireImBlue.DURATION
        Buff.affect(hero, HaloFireImBlue.class).set(HaloFireImBlue.DURATION);
        // 播放燃烧音效
        Sample.INSTANCE.play( Assets.Sounds.BURNING );
        // 生成10个火焰粒子效果
        hero.sprite.emitter().burst(FlameParticle.FACTORY, 10);
    }

    /**
     * 获取药水的溅射颜色
     * @return 返回火焰红色(0xFFFF002A)
     */
    @Override
    protected int splashColor() {
        return 0xFFFF002A;
    }

    /**
     * 计算药水的价值
     * @return 返回药水的总价值(基础价值50 + 材料价值40)乘以数量
     */
    @Override
    public int value() {
        //prices of ingredients
        return quantity * (50 + 40);
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

            // 制作成本：15金币
            cost = 15;

            // 产品：龙王药水
            output = ElixirOfDragonKing.class;
            // 产品数量：1瓶
            outQuantity = 1;
        }

    }
}

