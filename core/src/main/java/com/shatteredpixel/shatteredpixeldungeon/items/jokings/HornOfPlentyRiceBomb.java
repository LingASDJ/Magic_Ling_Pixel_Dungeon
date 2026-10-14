package com.shatteredpixel.shatteredpixeldungeon.items.jokings;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.RiceMagic;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Bomb;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Food;
import com.shatteredpixel.shatteredpixeldungeon.items.food.MeatPie;
import com.shatteredpixel.shatteredpixeldungeon.items.food.MysteryMeat;
import com.shatteredpixel.shatteredpixeldungeon.items.food.SmallRation;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.spdtomlpd.TheDrip;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.utils.Random;

// 击杀久住、久住跑路之后会掉落的物品之一：伪装成丰收号角的炸弹
// 但是是米饭炸弹，炸弹击杀敌人后会掉落随机食物（除了全肉大饼）
// 【塞满黑火药的丰收号角】
public class HornOfPlentyRiceBomb extends Bomb {

    {
        image = ItemSpriteSheet.ARTIFACT_HORN1;   // 贴图借用丰收号角
        stackable = false;                        // 不可堆叠
    }

    @Override
    public String desc() {
        return Messages.get(this, "desc", 4 + Dungeon.scalingDepth(), 12 + 3*Dungeon.scalingDepth()) + "\n\n" + Messages.get(Bomb.class, fuse == null ? "desc_fuse" : "desc_burning");
    }

    @Override
    public void explode(int cell) {
        super.explode(cell);
        for (Char ch : affectedChars) {
            if (ch != null && ch.alignment == Char.Alignment.ENEMY) {
                if(!ch.isAlive())
                    // Dungeon.level.drop( Generator.random( Generator.Category.FOOD ), ch.pos ).sprite.drop();
                    Dungeon.level.drop( theDrop(ch), ch.pos ).sprite.drop();
                else
                    Buff.affect(ch, RiceMagic.class);
            }
        }
    }

    private Item theDrop(Char ch) {
        Item thedrop = new Item();
        if(Char.hasProp(ch, Char.Property.BOSS))
            thedrop = new MeatPie();
        else
        {
            int GeneratorRandom = Random.Int(3);
            switch (GeneratorRandom) {
                case 0:
                {
                    thedrop = new MysteryMeat();
                    break;
                }
                case 1:
                {
                    thedrop = new Food();
                    break;
                }
                case 2:
                {
                    thedrop = new SmallRation();
                    break;
                }
            }
        }
        return thedrop;
    }
}
