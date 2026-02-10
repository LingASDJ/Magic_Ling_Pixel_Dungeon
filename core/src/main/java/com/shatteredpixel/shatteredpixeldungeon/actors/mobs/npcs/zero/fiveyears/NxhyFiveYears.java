package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.fiveyears;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Adrenaline;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FireImbue;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Haste;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Healing;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.FiveYearsNPC;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.plot.fiveyears.FiveYearsThreePlot;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.FlameParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.Elixir;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MagicTorch;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.NxhySprite;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialog;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;

public class NxhyFiveYears extends FiveYearsNPC {

    {
        spriteClass = NxhySprite.class;
        plot1 = new FiveYearsThreePlot.NxhyFiveYearsPlot();
    }

    @Override
    public boolean interact(Char c) {
        sprite.turnTo( pos, c.pos );
        if(first){
            Game.runOnRenderThread(() -> GameScene.show(new WndDialog(plot1,false)));
            first = false;
        }
        return true;
    }

    public static class MolotovBlazeBrew extends Elixir {

        {
            image = ItemSpriteSheet.MOTO_BREW;
        }
        @Override
        public void apply(Hero hero) {
            Buff.affect(hero, Adrenaline.class,50f);
            Buff.affect(hero, FireImbue.class).set(FireImbue.DURATION);
            Buff.affect(hero, Haste.class, 15f);
            Buff.affect(hero, Vertigo.class,10f);
            Buff.affect(hero, Healing.class).setHeal((int) (0.1f * hero.HT/2 + 5), 0.2f, 0);
            Sample.INSTANCE.play( Assets.Sounds.BURNING );
            hero.sprite.emitter().burst(FlameParticle.FACTORY, 10);
        }
        @Override
        public Emitter emitter() {
            Emitter emitter = new Emitter();
            emitter.pos(5.5f, 0);
            emitter.fillTarget = false;
            emitter.pour(MagicTorch.StaffParticleFactory, 0.1f);
            return emitter;
        }

    }

}
