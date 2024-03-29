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
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.MagicGirlDead;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BlastParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SnowParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SparkParticle;
import com.watabou.noosa.Camera;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Callback;

public class MagicGirlSprite extends MobSprite {

    private Animation charge;
    private Animation slam;

    private Emitter superchargeSparks;

    public MagicGirlSprite() {
        super();

        texture( Assets.Sprites.MGAS );

        updateChargeState(false);
    }

    public void updateChargeState( boolean enraged ){
        if (superchargeSparks != null) superchargeSparks.on = enraged;

        int c = 0;

        TextureFilm frames = new TextureFilm( texture, 12, 16 );

        idle = new Animation( 4, true );
        idle.frames( frames, c+0, c+1 );

        run = new Animation(  6, true );
        run.frames( frames, c+0, c+1 );

        attack = new Animation( 5, false );
        attack.frames( frames, c+3, c+4 );

        //unaffected by enrage state

        if (charge == null) {
            charge = new Animation(4, true);
            charge.frames(frames, 0, 10);

            slam = attack.clone();

            zap = new Animation(15, false);
            zap.frames(frames, 6, 7, 7, 6);

            die = new Animation(20, false);
            die.frames(frames, 3,4);
        }

        if (curAnim != charge) play(idle);
    }

    @Override
    public void play( Animation anim ) {
        if (anim == die) {
            emitter().burst( SnowParticle.FACTORY, 8 );
        }
        super.play( anim );
    }

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
                        ((MagicGirlDead)ch).onZapComplete();
                    }
                } );
        Sample.INSTANCE.play( Assets.Sounds.GAS );
    }

    public void charge(){
        play( charge );
    }

    public void slam( int cell ){
        turnTo( ch.pos , cell );
        play( slam );
        Sample.INSTANCE.play( Assets.Sounds.ROCKS );
        Camera.main.shake( 3, 0.7f );
    }

    @Override
    public void onComplete( Animation anim ) {

        if (anim == zap || anim == slam){
            idle();
        }

        if (anim == slam){
            ((MagicGirlDead)ch).onSlamComplete();
        }

        super.onComplete( anim );

        if (anim == die) {
            Sample.INSTANCE.play(Assets.Sounds.BLAST);
            emitter().burst( BlastParticle.FACTORY, 100 );
            killAndErase();
        }
    }

    @Override
    public void place(int cell) {
        if (parent != null) parent.bringToFront(this);
        super.place(cell);
    }

    @Override
    public void link(Char ch) {
        super.link(ch);

        superchargeSparks = emitter();
        superchargeSparks.autoKill = false;
        superchargeSparks.pour(SparkParticle.STATIC, 0.05f);
        superchargeSparks.on = false;

        if (ch instanceof MagicGirlDead && ((MagicGirlDead) ch).isSupercharged()){
            updateChargeState(true);
        }
    }

    @Override
    public void update() {
        super.update();

        if (superchargeSparks != null){
            superchargeSparks.visible = visible;
        }
    }

    @Override
    public void die() {
        super.die();
        if (superchargeSparks != null){
            superchargeSparks.on = false;
        }
    }

    @Override
    public void kill() {
        super.kill();
        if (superchargeSparks != null){
            superchargeSparks.killAndErase();
        }
    }

    @Override
    public int blood() {
        return 0xFFFFFF88;
    }
}
