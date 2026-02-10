package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.fiveyears;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Adrenaline;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bleeding;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Drowsy;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Haste;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Healing;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Poison;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Slow;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vulnerable;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Weakness;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.FiveYearsNPC;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.plot.fiveyears.FiveYearsThreePlot;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Food;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.WhiteYanSprite;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialog;
import com.watabou.noosa.Game;

public class WhiteYanNewYears extends FiveYearsNPC {

    {
        spriteClass = WhiteYanSprite.class;
        plot1 = new FiveYearsThreePlot.WhiteYanFiveYearsPlot();
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

    public static class FlowerCake extends Food {

        {
            image = ItemSpriteSheet.Flower_Cake;
            energy = Hunger.HUNGRY;
        }

        public static void cure( Char ch ) {
            Buff.detach( ch, Poison.class );
            Buff.detach( ch, Cripple.class );
            Buff.detach( ch, Weakness.class );
            Buff.detach( ch, Vulnerable.class );
            Buff.detach( ch, Bleeding.class );
            Buff.detach( ch, Blindness.class );
            Buff.detach( ch, Drowsy.class );
            Buff.detach( ch, Slow.class );
            Buff.detach( ch, Vertigo.class);
        }

        @Override
        protected void satisfy(Hero hero) {
            super.satisfy( hero );
            Buff.affect(hero, Healing.class).setHeal((int) (0.25f * hero.HT/2 ), 0.25f, 0);
            cure( hero );
            Buff.affect(hero, Haste.class, 8f);
            Buff.affect(hero, Adrenaline.class, 4f);
        }

    }

}
