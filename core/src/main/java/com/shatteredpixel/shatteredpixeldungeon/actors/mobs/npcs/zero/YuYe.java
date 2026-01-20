package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero;

/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2022 Evan Debenham
 *
 * Mund Pixel Dungeon
 * Copyright (C) 2018-2023 Thliey Pen
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

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NTNPC;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfBlink;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.YuyeSprite;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndQuest;

public class YuYe extends NTNPC {

    {
        spriteClass = YuyeSprite.class;
    }

    @Override
    public boolean interact(Char c) {
        if (c != hero) return true;

        GameScene.show( new WndQuest( YuYe.this, Messages.get(YuYe.class, "hello") ) );
        Dungeon.level.drop(new StoneOfBlink(), hero.pos).sprite.drop();
        Dungeon.gold -= 500;
        destroy();
        CellEmitter.get( pos ).burst( Speck.factory( Speck.WOOL ), 10 );

        return true;
    }
}