package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.particles.Emitter;

public class CrivusFruitsSprite extends MobSprite {

  private Emitter cloud;

  public CrivusFruitsSprite() {
    super();

    perspectiveRaise = 0.2f;

    texture(Assets.Sprites.CFAS);

    TextureFilm frames = new TextureFilm(texture, 16, 16);

    idle = new MovieClip.Animation(1, true);
    idle.frames(frames, 0);

    run = new MovieClip.Animation(1, true);
    run.frames(frames, 0);

    attack = new MovieClip.Animation(1, false);
    attack.frames(frames, 0);

    die = new MovieClip.Animation(8, false);
    die.frames(frames, 1, 2, 3, 4, 5, 6, 6, 6);

    play(idle);
  }

  @Override
  public void link(Char ch) {
    super.link(ch);

    renderShadow = false;

    if (cloud == null) {
      cloud = emitter();
      cloud.pour(Speck.factory(Speck.DIED), 0.7f);
    }
  }

  @Override
  public void turnTo(int from, int to) {
    // do nothing
  }

  @Override
  public void update() {

    super.update();

    if (cloud != null) {
      cloud.visible = visible;
    }
  }

  @Override
  public void die() {
    super.die();

    if (cloud != null) {
      cloud.on = false;
    }
  }

  @Override
  public int blood() {
    return 0xFF88CC44;
  }
}
