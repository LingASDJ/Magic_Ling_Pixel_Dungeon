package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.extra;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NTNPC;
import com.shatteredpixel.shatteredpixeldungeon.sprites.PeachGodStateSprite;

public class PeachGodState extends NTNPC {
    {
        //TODO 完善祈愿阶段性变化 如果一次10连就直接渲染最终效果
        spriteClass = PeachGodStateSprite.class;
    }

    @Override
    public boolean interact(Char c) {

        sprite.turnTo(pos, hero.pos);

        //TODO 完善抽卡逻辑
        return true;
    }

}
