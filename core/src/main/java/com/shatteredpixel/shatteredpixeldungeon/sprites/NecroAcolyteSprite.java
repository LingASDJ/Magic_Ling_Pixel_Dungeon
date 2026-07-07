/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2024 Evan Debenham
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
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.tumulus.NecroAcolyte;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;

public class NecroAcolyteSprite extends MobSprite {

    private Animation charging; // 新增循环蓄力动画，实现召唤持续播放
    private Emitter summoningParticles;

    public NecroAcolyteSprite(){
        super();

        texture( Assets.Sprites.NECRO_APPREN );
        TextureFilm film = new TextureFilm( texture, 18, 17 );

        idle = new Animation( 11, true );
        idle.frames( film, 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,2,3,3,3,3,3,3,3,3,3,3,4);

        run = new Animation( 11, true );
        run.frames( film, 5,6,7,8,9,10,11,12 );

        // 施法起手动画：单次不循环
        zap = new Animation( 11, false );
        zap.frames( film, 13,14,15,16,17 );

        // 循环蓄力动画：true 开启循环，持续召唤时重复播放
        charging = new Animation( 11, true );
        charging.frames( film, 13,14,15,16,17 );

        die = new Animation( 11, false );
        die.frames( film, 18,19,20,21,22,23 );

        // 攻击动画复用zap，普攻只播放一次，不进蓄力循环
        attack = zap.clone();

        idle();
    }

    @Override
    public void link(Char ch) {
        super.link(ch);
        if (ch instanceof NecroAcolyte && ((NecroAcolyte) ch).summoning){
            zap(((NecroAcolyte) ch).summoningPos);
        }
    }

    @Override
    public void update() {
        super.update();
        if (summoningParticles != null && ((NecroAcolyte) ch).summoningPos != -1){
            summoningParticles.visible = Dungeon.level.heroFOV[((NecroAcolyte) ch).summoningPos];
        }
    }

    @Override
    public void die() {
        super.die();
        if (summoningParticles != null){
            summoningParticles.on = false;
            summoningParticles = null;
        }
    }

    @Override
    public void kill() {
        super.kill();
        if (summoningParticles != null){
            summoningParticles.on = false;
            summoningParticles = null;
        }
    }

    public void cancelSummoning(){
        if (summoningParticles != null){
            summoningParticles.on = false;
            summoningParticles = null;
        }
    }

    public void finishSummoning(){
        if (summoningParticles != null) {
            if (summoningParticles.visible) {
                Sample.INSTANCE.play(Assets.Sounds.BONES);
                summoningParticles.burst(Speck.factory(Speck.BLUE_LIGHT), 5);
            } else {
                summoningParticles.on = false;
            }
            summoningParticles = null;
        }
        idle();
    }

    public void charge(){
        play(charging);
    }

    @Override
    public void zap(int cell) {
        super.zap(cell);
        if (ch instanceof NecroAcolyte && ((NecroAcolyte) ch).summoning){
            if (summoningParticles != null){
                summoningParticles.on = false;
            }
            summoningParticles = CellEmitter.get(((NecroAcolyte) ch).summoningPos);
            summoningParticles.pour(Speck.factory(Speck.BLUE_LIGHT), 0.2f);
            summoningParticles.visible = Dungeon.level.heroFOV[((NecroAcolyte) ch).summoningPos];
            if (visible || summoningParticles.visible ) Sample.INSTANCE.play( Assets.Sounds.CHARGEUP, 1f, 0.8f );
        }
    }

    @Override
    public void onComplete(Animation anim) {
        super.onComplete(anim);
        if (anim == zap){
            if (ch instanceof NecroAcolyte){
                // 正在召唤 → 切循环蓄力动画，实现持续施法效果
                if (((NecroAcolyte) ch).summoning){
                    charge();
                } else {
                    // 普通攻击降头，播放一次直接切待机
                    idle();
                }
            } else {
                idle();
            }
        }
    }
}