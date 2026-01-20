package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.Game;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.tweeners.PosTweener;
import com.watabou.utils.Callback;
import com.watabou.utils.PointF;

public class SwordLingSprites extends CharSprite{

    public PosTweener tweener;
    private Callback callback;

    public float time;

    public SwordLingSprites() {
        super();

        time = 1f;

        texture(Assets.Sprites.SWORDLING);

        TextureFilm frames = new TextureFilm(texture,24,24);

        idle = new Animation(20,true);
        idle.frames(frames,0,1,2,3,4);

        play(idle);
    }

    public void place(CharSprite from,int to,Callback callback) {
        this.callback = callback;
        point(from.center());

        PointF pointF = new PointF(from.x + from.width() / 2 - width() / 2,from.y + from.height() / 2 - height() / 2);
        PointF pointF1 = worldToCamera(to);
        PointF d = PointF.diff(pointF1,pointF);
        angle = 90 - (float)(Math.atan2(d.x,d.y) / 3.1415926 * 180);
        speed.set(d).normalize().scale(90f);
        angularSpeed = 0;

        tweener = new PosTweener(this,pointF1,1f);
        tweener.listener = this;

        Game.scene().add(this);
    }

    @Override
    public void update() {
        super.update();

        if ((time -= Game.elapsed) < 0) {
            callback.call();
            killAndErase();
        }
    }
}
