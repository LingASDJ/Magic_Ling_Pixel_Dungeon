package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero;

import static com.shatteredpixel.shatteredpixeldungeon.BGMPlayer.playBGM;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Conducts;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NTNPC;
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
import com.watabou.noosa.Game;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;

import java.util.ArrayList;

public class WaloKe extends NTNPC {

    private static final String FIRST = "first";
    private boolean first=true;
    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(FIRST, first);
    }
    @Override
    public String name(){
        if (Dungeon.isDLC(Conducts.Conduct.BOSSRUSH)) {
            return Messages.get(this, "namex");
        } else {
            return Messages.get(this, "name");
        }

    }

    @Override
    public String description(){
        if (Dungeon.isDLC(Conducts.Conduct.BOSSRUSH)) {
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


    protected boolean act() {
        sprite.turnTo( pos, Dungeon.hero.pos );
        spend( TICK );

        shop1 = Generator.randomUsingDefaults( Generator.Category.SCROLL );
        shop2 = Generator.randomUsingDefaults( Generator.Category.STONE);
        shop3 = Generator.randomUsingDefaults( Generator.Category.WAND);
        shop4 = Generator.randomUsingDefaults( Generator.Category.ARTIFACT);
        shop6 = Generator.randomUsingDefaults( Generator.Category.ARMOR );
        shop5 = Generator.randomUsingDefaults( Generator.Category.RING );

        shop7 =  Generator.randomUsingDefaults( Generator.Category.WEP_T1 );
        shop8 =  Generator.randomUsingDefaults( Generator.Category.WEP_T2 );
        shop9 =  Generator.randomUsingDefaults( Generator.Category.WEP_T3 );
        shop10 = Generator.randomUsingDefaults( Generator.Category.WEP_T4 );
        shop11 = Generator.randomUsingDefaults( Generator.Category.WEP_T5 );
        shop12 = Generator.randomUsingDefaults( Generator.Category.WEP_T6 );

        shop13 = new PotionOfFrost().quantity(1).identify();
        shop14 = new PotionOfLiquidFlame().quantity(1).identify();
        shop15 = new MeatPie();
        shop16 = new LightFood();
        shop17 = new ScrollOfIdentify();
        shop18 = new PotionOfLiquidFlame();

        shop19 = new PotionOfHaste();
        shop20 = new ScrollOfRemoveCurse();
        shop21 = new Pasty().quantity(1).identify();
        shop22 = new ScrollOfRemoveCurse().quantity(1).identify();
        shop23 = new Firebomb().quantity(1);
        shop24 = new FrostBomb().quantity(1);


        throwItem();
        return WaloKe.super.act();
    }

    public static Item shop1;
    public static Item shop2;
    public static Item shop3;
    public static Item shop4;
    public static Item shop5;
    public static Item shop6;

    public static Item shop7;
    public static Item shop8;
    public static Item shop9;
    public static Item shop10;
    public static Item shop11;
    public static Item shop12;

    public static Item shop13;
    public static Item shop14;
    public static Item shop15;
    public static Item shop16;
    public static Item shop17;
    public static Item shop18;

    public static Item shop19;
    public static Item shop20;
    public static Item shop21;
    public static Item shop22;
    public static Item shop23;
    public static Item shop24;

    @Override
    public boolean interact(Char c) {
        sprite.turnTo( pos, Dungeon.hero.pos );
        if(first){
            WndQuest.chating(this,chat);
            first=false;
        }else {
            if(Dungeon.isDLC(Conducts.Conduct.BOSSRUSH) && Dungeon.depth == 27){
                GLog.n(Messages.get(this,"leave"));
            } else {
                Game.runOnRenderThread(new Callback() {
                    @Override
                    public void call() {
                        GameScene.show(new WndKingShop());
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
                if(Dungeon.isDLC(Conducts.Conduct.BOSSRUSH) && Dungeon.depth==0) {
                    add(Messages.get(WaloKe.class, "talk_a"));
                    add(Messages.get(WaloKe.class, "talk_b"));
                    add(Messages.get(WaloKe.class, "talk_c"));
                    add(Messages.get(WaloKe.class, "talk_d"));
                    add(Messages.get(WaloKe.class, "talk_e"));
                    add(Messages.get(WaloKe.class, "talk_f"));
                } else if(Dungeon.isDLC(Conducts.Conduct.BOSSRUSH) && Dungeon.depth==17) {
                    add(Messages.get(WaloKe.class, "talk_g"));
                    add(Messages.get(WaloKe.class, "talk_h"));
                } else if(Dungeon.isDLC(Conducts.Conduct.BOSSRUSH) && Dungeon.depth==27) {
                    add(Messages.get(WaloKe.class, "talk_i"));
                    add(Messages.get(WaloKe.class, "talk_j"));
                } else if(Dungeon.isDLC(Conducts.Conduct.BOSSRUSH) && Dungeon.depth==28) {
                    add(Messages.get(WaloKe.class, "talk_k"));
                    add(Messages.get(WaloKe.class, "talk_l"));
                    add(Messages.get(WaloKe.class, "talk_m"));
                    add(Messages.get(WaloKe.class, "talk_n"));
                    add(Messages.get(WaloKe.class, "talk_o"));
                } else {
                    add(Messages.get(WaloKe.class, "talk_x"));
                }

            }
        };

    }
}

