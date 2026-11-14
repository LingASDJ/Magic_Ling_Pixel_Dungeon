package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.tumulus.LichKing;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;

public class LichKingSprite extends MobSprite {

    private Emitter chargeParticles;

    public LichKingSprite() {
        super();

        texture( Assets.Sprites.LICH );

        TextureFilm frames = new TextureFilm( texture, 41, 32 );

        idle = new Animation( 5, true );
        idle.frames( frames, 0,1,2,3,4,5 );

        run = new Animation( 9, true );
        run.frames( frames, 6,7,8,9,10,11);

        attack = new Animation( 20, false );
        attack.frames( frames, 12,13,14,15,16 );

        die = new Animation( 11, false );
        die.frames( frames, 22,23,24,25,26,27,28 );

        zap= new Animation( 20, false );
        zap.frames( frames, 17,18,19,20,21);

        play( idle );
    }

    @Override
    public void link(Char ch) {
        super.link(ch);

        if (chargeParticles != null) {
            chargeParticles.killAndErase();
        }
        chargeParticles = centerEmitter();
        if (chargeParticles != null) {
            chargeParticles.autoKill = false;
            chargeParticles.pour(MagicMissile.MagicParticle.ATTRACTING, 0.05f);
            chargeParticles.on = ch instanceof LichKing && ((LichKing) ch).isCharging();
        }
    }

    @Override
    public void update() {
        super.update();
        if (chargeParticles != null) {
            chargeParticles.pos(center());
            chargeParticles.visible = visible;
        }
    }

    @Override
    public void die() {
        super.die();
        endCharge();
    }

    @Override
    public void kill() {
        super.kill();
        if (chargeParticles != null) {
            chargeParticles.killAndErase();
            chargeParticles = null;
        }
    }

    /** 轰杀蓄力：转向目标并开启聚能粒子。 */
    public void charge( int targetPos ) {
        if (ch != null && targetPos >= 0) {
            turnTo(ch.pos, targetPos);
        }
        if (chargeParticles != null) {
            chargeParticles.on = true;
        }
        if (visible) {
            Sample.INSTANCE.play(Assets.Sounds.CHARGEUP);
        }
    }

    /** 结束蓄力（发射或死亡时调用）。 */
    public void endCharge() {
        if (chargeParticles != null) {
            chargeParticles.on = false;
        }
    }

}
