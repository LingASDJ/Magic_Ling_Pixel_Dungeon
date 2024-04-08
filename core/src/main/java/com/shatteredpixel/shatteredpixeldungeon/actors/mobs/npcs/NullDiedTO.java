package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs;

import static com.shatteredpixel.shatteredpixeldungeon.BGMPlayer.playBGM;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.EnergyParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SnowParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Firebomb;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.FrostBomb;
import com.shatteredpixel.shatteredpixeldungeon.items.food.LightFood;
import com.shatteredpixel.shatteredpixeldungeon.items.food.MeatPie;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Pasty;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfFrost;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHaste;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfLiquidFlame;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfIdentify;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRemoveCurse;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ShopkKingSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndKingShop;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndQuest;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndRushShop;
import com.watabou.noosa.Game;
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
    public String name(){
        if (Statistics.bossRushMode) {
            return Messages.get(this, "namex");
        } else {
            return Messages.get(this, "name");
        }

    }

    @Override
    public String description(){
        if (Statistics.bossRushMode) {
            return Messages.get(this, "descx");
        } else {
            return Messages.get(this, "desc");
        }

    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        first = bundle.getBoolean(FIRST);
    }

    public void flee() {
        destroy();
        Dungeon.level.seal();
       playBGM(Assets.BGM_FRBOSS, true);
        GameScene.flash(0x8000FFFF);
        sprite.killAndErase();
        CellEmitter.get( pos ).burst(SnowParticle.FACTORY, 6 );
        CellEmitter.get( pos ).burst(EnergyParticle.FACTORY, 6 );
    }

    public void ReloadShop() {
        shop1 = Generator.random(Generator.Category.SCROLL );
        shop1.windowsBuy = true;
        shop2 = Generator.random(Generator.Category.STONE);
        shop2.windowsBuy = true;
        shop3 = Generator.random(Generator.Category.WAND);
        shop3.windowsBuy = true;
        shop4 = Generator.random(Generator.Category.ARTIFACT);
        shop4.windowsBuy = true;
        shop6 = Generator.random(Generator.Category.ARMOR );
        shop6.windowsBuy = true;
        shop5 = Generator.random(Generator.Category.RING );
        shop5.windowsBuy = true;
        shop7 = Generator.random( Generator.Category.WEP_T1 );
        shop7.windowsBuy = true;
        shop8 = Generator.random( Generator.Category.WEP_T2 );
        shop8.windowsBuy = true;
        shop9 = Generator.random( Generator.Category.WEP_T3 );
        shop9.windowsBuy = true;
        shop10 = Generator.random( Generator.Category.WEP_T4 );
        shop10.windowsBuy = true;
        shop11 = Generator.random( Generator.Category.WEP_T5 );
        shop11.windowsBuy = true;
        shop12 = Generator.random( Generator.Category.WEP_T6 );
        shop12.windowsBuy = true;
        shop13 = new PotionOfFrost().quantity(1).identify();
        shop13.windowsBuy = true;
        shop14 = new PotionOfLiquidFlame().quantity(1).identify();
        shop14.windowsBuy = true;
        shop15 = new MeatPie();
        shop15.windowsBuy = true;
        shop16 = new LightFood();
        shop16.windowsBuy = true;
        shop17 = new ScrollOfIdentify();
        shop17.windowsBuy = true;
        shop18 = new PotionOfLiquidFlame();
        shop18.windowsBuy = true;

        shop19 = new PotionOfHaste();
        shop19.windowsBuy = true;
        shop20 = new ScrollOfRemoveCurse();
        shop20.windowsBuy = true;
        shop21 = new Pasty().quantity(1).identify();
        shop21.windowsBuy = true;
        shop22 = new ScrollOfRemoveCurse().quantity(1).identify();
        shop22.windowsBuy = true;
        shop23 = new Firebomb().quantity(1);
        shop23.windowsBuy = true;
        shop24 = new FrostBomb().quantity(1);
        shop24.windowsBuy = true;
    }


    protected boolean act() {
        sprite.turnTo( pos, Dungeon.hero.pos );
        spend( TICK );
        throwItem();
        return NullDiedTO.super.act();
    }

    public Item shop1;
    public Item shop2;
    public Item shop3;
    public Item shop4;
    public Item shop5;
    public Item shop6;

    public Item shop7;
    public Item shop8;
    public Item shop9;
    public Item shop10;
    public Item shop11;
    public Item shop12;

    public Item shop13;
    public Item shop14;
    public Item shop15;
    public Item shop16;
    public Item shop17;
    public Item shop18;

    public Item shop19;
    public Item shop20;
    public Item shop21;
    public Item shop22;
    public Item shop23;
    public Item shop24;

    @Override
    public boolean interact(Char c) {
        sprite.turnTo( pos, Dungeon.hero.pos );
        if(first){
            WndQuest.chating(this,chat);
            first=false;
        }else {
            if(Statistics.bossRushMode && Dungeon.depth == 27){
                GLog.n(Messages.get(this,"leave"));
            } else {
                Game.runOnRenderThread(new Callback() {
                    @Override
                    public void call() {
                        if(Statistics.bossRushMode){
                            GameScene.show(new WndRushShop());
                        } else {
                            GameScene.show(new WndKingShop());
                        }

                    }
                });
            }


        }
        return true;
    }

    {
        spriteClass = ShopkKingSprite.class;

        chat = new ArrayList<String>() {
            {
                if(Statistics.bossRushMode && Dungeon.depth==1) {
                    add(Messages.get(NullDiedTO.class, "talk_a"));
                    add(Messages.get(NullDiedTO.class, "talk_b"));
                    add(Messages.get(NullDiedTO.class, "talk_c"));
                    add(Messages.get(NullDiedTO.class, "talk_d"));
                    add(Messages.get(NullDiedTO.class, "talk_e"));
                    add(Messages.get(NullDiedTO.class, "talk_f"));
                } else if(Statistics.bossRushMode && Dungeon.depth==17) {
                    add(Messages.get(NullDiedTO.class, "talk_g"));
                    add(Messages.get(NullDiedTO.class, "talk_h"));
                } else if(Statistics.bossRushMode && Dungeon.depth==27) {
                    add(Messages.get(NullDiedTO.class, "talk_i"));
                    add(Messages.get(NullDiedTO.class, "talk_j"));
                } else if(Statistics.bossRushMode && Dungeon.depth==28) {
                    add(Messages.get(NullDiedTO.class, "talk_k"));
                    add(Messages.get(NullDiedTO.class, "talk_l"));
                    add(Messages.get(NullDiedTO.class, "talk_m"));
                    add(Messages.get(NullDiedTO.class, "talk_n"));
                    add(Messages.get(NullDiedTO.class, "talk_o"));
                } else {
                    add(Messages.get(NullDiedTO.class, "talk_x"));
                }

            }
        };

    }
}
