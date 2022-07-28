/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2022 Evan Debenham
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
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.BloodBat;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;

public class BlueBatSprite extends MobSprite {

    public void zap( int cell ) {

        turnTo( ch.pos , cell );
        play( zap );

        MagicMissile.boltFromChar( parent,
                MagicMissile.FROST,
                this,
                cell,
                new Callback() {
                    @Override
                    public void call() {
                        ((BloodBat)ch).onZapComplete();
                    }
                } );
        Sample.INSTANCE.play( Assets.Sounds.ZAP );
    }

    public BlueBatSprite() {
        super();

        texture( Assets.Sprites.BBAT );

        TextureFilm frames = new TextureFilm( texture, 15, 15 );

        idle = new Animation( 8, true );
        idle.frames( frames, 0, 1 );

        run = new Animation( 12, true );
        run.frames( frames, 0, 1 );

        attack = new Animation( 12, false );
        attack.frames( frames, 2, 3, 0, 1 );

        die = new Animation( 12, false );
        die.frames( frames, 4, 5, 6 );

        play( idle );
    }

    public static class BatEDSprite extends MobSprite {

        public BatEDSprite() {
            super();

            texture(Assets.Sprites.BATEX);

            TextureFilm frames = new TextureFilm(texture, 15, 15);

            idle = new Animation(8, true);
            idle.frames(frames, 0, 1);

            run = new Animation(12, true);
            run.frames(frames, 0, 1);

            attack = new Animation(12, false);
            attack.frames(frames, 2, 3, 0, 1);

            die = new Animation(12, false);
            die.frames(frames, 4, 5, 6);

            play(idle);
        }
    }
}
