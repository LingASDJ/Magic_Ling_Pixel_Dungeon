package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.EnergyParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SnowParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Bomb;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Firebomb;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.FrostBomb;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Food;
import com.shatteredpixel.shatteredpixeldungeon.items.food.MeatPie;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Pasty;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.Potion;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHaste;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.Scroll;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfIdentify;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRemoveCurse;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ShopkKingSprite;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndKingShop;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndQuest;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Music;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;

import java.util.ArrayList;

public class NullDiedTO extends NTNPC {

    private static final String FIRST = "first";
    private boolean first=true;
    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(FIRST, first);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        first = bundle.getBoolean(FIRST);
    }

    public void flee() {
        destroy();
        Dungeon.level.seal();
        Music.INSTANCE.play(Assets.BGM_FRBOSS, true);
        GameScene.flash(0x8000FFFF);
        sprite.killAndErase();
        CellEmitter.get( pos ).burst(SnowParticle.FACTORY, 6 );
        CellEmitter.get( pos ).burst(EnergyParticle.FACTORY, 6 );
    }


    protected boolean act() {
        sprite.turnTo( pos, Dungeon.hero.pos );
        spend( TICK );

        shop6 = Generator.randomUsingDefaults( Generator.Category.ARMOR );
        shop5 = Generator.randomUsingDefaults( Generator.Category.RING );
        shop4 = new MeatPie();
        shop3 = new Pasty();
        shop2 = new ScrollOfIdentify();
        shop1 = new PotionOfHealing();

        shop7 = new PotionOfHaste();
        shop8 = new ScrollOfRemoveCurse();
        shop9 = Generator.randomUsingDefaults( Generator.Category.WEP_T5 );
        shop10 = Generator.randomUsingDefaults( Generator.Category.WEP_T3 );
        shop11 = (Bomb) new Firebomb().quantity(1);
        shop12 = (Bomb) new FrostBomb().quantity(1);
        throwItem();
        return NullDiedTO.super.act();
    }

    public static Potion shop1;
    public static Scroll shop2;
    public static Food shop3;
    public static Food shop4;
    public static Item shop5;
    public static Item shop6;

    public static Potion shop7;
    public static Scroll shop8;
    public static Item shop9;
    public static Item shop10;
    public static Bomb shop11;
    public static Bomb shop12;

    @Override
    public boolean interact(Char c) {
        sprite.turnTo( pos, Dungeon.hero.pos );
        if(first){
            WndQuest.chating(this,chat);
            first=false;
        }else {
            Game.runOnRenderThread(new Callback() {
                                       @Override
                                       public void call() {
                                           GameScene.show(new WndKingShop());
                                       }
            });

        }
        return true;
    }

    {
        spriteClass = ShopkKingSprite.class;

        chat = new ArrayList<String>() {
            {
                add(Messages.get(NullDiedTO.class, "talk_x"));
            }
        };

    }
}
