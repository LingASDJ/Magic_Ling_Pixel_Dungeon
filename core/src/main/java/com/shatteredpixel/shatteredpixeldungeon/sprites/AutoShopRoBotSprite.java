package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ElmoParticle;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;

public class AutoShopRoBotSprite extends MobSprite {

  public AutoShopRoBotSprite() {
    super();

    texture(Assets.Sprites.KEEPERKINGBOT);
    TextureFilm film = new TextureFilm(texture, 16, 16);

    idle = new Animation(10, true);
    idle.frames(film, 0);
    die = new Animation(20, false);
    die.frames(film, 0);

    run = idle.clone();

    attack = idle.clone();

    idle();
  }

  @Override
  public void die() {
    super.die();

    remove(State.SHIELDED);
    emitter().start(ElmoParticle.FACTORY, 0.03f, 60);

    if (visible) {
      Sample.INSTANCE.play(Assets.Sounds.BURNING);
    }
  }
}
