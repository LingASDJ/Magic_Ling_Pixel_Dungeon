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

package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.watabou.noosa.TextureFilm;

public class FireBallMobSprite extends MobSprite {

    public FireBallMobSprite() {
        super();

        texture( Assets.Sprites.ASDWX );

        TextureFilm frames = new TextureFilm( texture, 10, 14 );

        idle = new Animation( 14, true );
        idle.frames( frames, 0, 1, 2, 3,0, 1, 2, 3,0, 1, 2 ,0, 1, 2, 3 );

        run = new Animation( 6, true );
        run.frames( frames, 0, 1, 0, 1, 0,1 );

        attack = new Animation( 15, false );
        attack.frames( frames, 2, 3,2, 3,2,3 );

        die = new Animation( 10, false );
        die.frames( frames,  4,5,6,7);

        play( idle );
    }

    public void link(Char var1) {
        super.link(var1);
        this.add(State.HALOMETHANEBURNING);
    }
}
