package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AutoRandomBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ElmoParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Cake;
import com.shatteredpixel.shatteredpixeldungeon.items.food.ChargrilledMeat;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Food;
import com.shatteredpixel.shatteredpixeldungeon.items.food.LightFood;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Pasty;
import com.shatteredpixel.shatteredpixeldungeon.items.food.SmallRation;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Switch;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.AutoShopRoBotSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndAutoShop;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndQuest;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Music;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;

public class AutoShopReBot extends NPC {

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
        sprite.killAndErase();
        CellEmitter.get( pos ).burst( ElmoParticle.FACTORY, 6 );
    }
    private void tell(String text) {
        Game.runOnRenderThread(new Callback() {
                                   @Override
                                   public void call() {
                                       GameScene.show(new WndQuest(new AutoShopReBot(), text));
                                   }
                               }
        );
    }
    @Override
    public boolean interact(Char c) {
        sprite.turnTo( pos, Dungeon.hero.pos );

        if(Dungeon.hero.buff(AutoRandomBuff.class) == null) {
            tell(Messages.get(WndAutoShop.class, "maxbuy"));
            for (Buff buff : hero.buffs()) {
                if (buff instanceof AutoRandomBuff) {
                    buff.detach();
                }
            }
        } else {
            Game.runOnRenderThread(new Callback() {
                                       @Override
                                       public void call() {
                                           GameScene.show(new WndAutoShop());
                                       }
                                   }
            );

        }
        return true;
    }
    private boolean seenBefore = false;
    protected boolean act() {
        if (!seenBefore && Dungeon.level.heroFOV[pos]) {
            GLog.p(Messages.get(this, "greetings", Dungeon.hero.name()));
            seenBefore = true;
        } else if (seenBefore && !Dungeon.level.heroFOV[pos] && Dungeon.depth == 0) {
            Music.INSTANCE.play(Assets.TOWN, true);
            seenBefore = false;
        } else if (seenBefore && !Dungeon.level.heroFOV[pos] && Dungeon.depth == 12) {
            Music.INSTANCE.play(Assets.BGM_2,true);
            seenBefore = false;
        }
        throwItem();

        sprite.turnTo( pos, Dungeon.hero.pos );
        spend( TICK );

        shop6 = new ChargrilledMeat();
        shop5 = new Pasty();
        shop4 = new Switch();
        shop3 = new Cake();
        shop2 = new SmallRation();
        shop1 = new LightFood();
        ate = new AutoShopReBot();
        throwItem();
        return super.act();
    }

    public static Food shop1;
    public static Food shop2;
    public static Food shop3;
    public static Food shop4;
    public static Food shop5;
    public static Food shop6;
    public static AutoShopReBot ate;

    {
        spriteClass = AutoShopRoBotSprite.class;
        HT=HP=30;
        flying = true;
    }
}

