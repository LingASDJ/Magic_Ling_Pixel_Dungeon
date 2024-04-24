package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.notsync;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.PaswordBadges;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.lb.RivalSprite;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NTNPC;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.plot.FayiNaPlot;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.ClearHStal;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.DragonHeart;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ClearElemtGuardGirlSprites;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndQuest;
import com.watabou.noosa.Game;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.List;

public class FayiNa extends NTNPC {

    private boolean first = true;
    private boolean secnod = true;
    private boolean rd = true;
    private boolean sd = true;
    private boolean kd = true;

    private int progress = 1;

    private static final String FIRST = "first";
    private static final String SECNOD = "secnod";
    private static final String RD = "rd";
    private static final String SD = "sd";
    private static final String KD = "kd";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(FIRST, first);
        bundle.put(SECNOD, secnod);
        bundle.put(RD, rd);
        bundle.put(SD, sd);
        bundle.put(KD, kd);
        bundle.put("progress", progress);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        first = bundle.getBoolean(FIRST);
        secnod = bundle.getBoolean(SECNOD);
        rd = bundle.getBoolean(RD);
        sd = bundle.getBoolean(SD);
        kd = bundle.getBoolean(KD);
        progress = bundle.getInt("progress");
    }

    {
        spriteClass = ClearElemtGuardGirlSprites.class;
    }


    private static String[] TXT_RANDOM = {
            Messages.get(FayiNa.class,"roll1"),
            Messages.get(FayiNa.class,"roll2"),
            Messages.get(FayiNa.class,"roll3"),
            Messages.get(FayiNa.class,"roll4"),
            Messages.get(FayiNa.class,"roll5"),
            Messages.get(FayiNa.class,"roll6", Dungeon.hero.name()),
            Messages.get(FayiNa.class,"roll7"),
            Messages.get(FayiNa.class,"roll8"),
    };

    @Override
    public boolean interact(Char c) {

        sprite.turnTo(pos, hero.pos);
        FayiNaPlot plot = new FayiNaPlot();
        FayiNaPlot.FayiNaPlotEND plot2 = new FayiNaPlot.FayiNaPlotEND();

        PaswordBadges.loadGlobal();
        List<PaswordBadges.Badge> passwordbadges = PaswordBadges.filtered(true);

        if (passwordbadges.contains(PaswordBadges.Badge.SWORDDREAM)) {
            yell(TXT_RANDOM[Random.Int(TXT_RANDOM.length)]);
        } else {
            if (first) {
                Game.runOnRenderThread(new Callback() {
                    @Override
                    public void call() {
                        GameScene.show(new WndDialog(plot,false));
                    }
                });
                //Dungeon.level.drop( ( Generator.randomUsingDefaults( Generator.Category.FOOD ) ), hero.pos );
                first=false;
            } else if(secnod) {
                Game.runOnRenderThread(new Callback() {
                    @Override
                    public void call() {
                        GameScene.show(new WndOptions(new ClearElemtGuardGirlSprites(),
                                Messages.titleCase(Messages.get(FayiNa.class, "name")),
                                Messages.get(FayiNa.class, "info"),
                                Messages.get(FayiNa.class, "take_firedragon_heart")) {
                            @Override
                            protected void onSelect(int index) {
                                if (index == 0) {
                                    ArrayList<DragonHeart> clearCryStals = hero.belongings.getAllItems(DragonHeart.class);
                                    if(clearCryStals != null){
                                        for (DragonHeart w : clearCryStals.toArray(new DragonHeart[0])){
                                            w.detachAll(hero.belongings.backpack);
                                        }
                                        tell(Messages.get(FayiNa.class, "message3",hero.name()));
                                        secnod = false;
                                    }
                                }
                            }
                        });
                    }
                });
            } else if(rd) {
                Game.runOnRenderThread(new Callback() {
                    @Override
                    public void call() {
                        GameScene.show(new WndOptions(new ClearElemtGuardGirlSprites(),
                                Messages.titleCase(Messages.get(FayiNa.class, "name")),
                                Messages.get(FayiNa.class, "info_2"),
                                Messages.get(FayiNa.class, "take_cry"),
                                Messages.get(FayiNa.class, "look")) {
                            @Override
                            protected void onSelect(int index) {
                                if (index == 0) {
                                    tell(Messages.get(FayiNa.class, "message4a"));
                                } else if (index == 1){
                                    ArrayList<ClearHStal> clearCryStals = hero.belongings.getAllItems(ClearHStal.class);
                                    if(clearCryStals != null) {
                                        for (ClearHStal w : clearCryStals.toArray(new ClearHStal[0])){
                                            w.detachAll(hero.belongings.backpack);
                                        }
                                        tell(Messages.get(FayiNa.class, "message4b"));
                                        rd = false;
                                    }

                                }
                            }
                        });
                    }
                });
            } else if(sd){
                Game.runOnRenderThread(new Callback() {
                    @Override
                    public void call() {
                        GameScene.show(new WndOptions(new RivalSprite(),
                                Messages.titleCase(Messages.get(ClearElemtGuard.class, "myname",hero.name())),
                                Messages.get(FayiNa.class, "info_3"),
                                Messages.get(FayiNa.class, "i")) {
                            @Override
                            protected void onSelect(int index) {
                                if (index == 0) {
                                    tell(Messages.get(FayiNa.class, "message6",hero.name()));
                                    sd = false;
                                }
                            }
                        });
                    }
                });
            } else if(kd) {
                Game.runOnRenderThread(new Callback() {
                    @Override
                    public void call() {
                        GameScene.show(new WndDialog(plot2,false));
                    }
                });
                //Dungeon.level.drop( ( Generator.randomUsingDefaults( Generator.Category.FOOD ) ), hero.pos );
                kd=false;
            }
        }

        return true;
    }

    public static void tell(String text) {
        Game.runOnRenderThread(new Callback() {
                                   @Override
                                   public void call() {
                                       GameScene.show(new WndQuest(new FayiNa(), text));
                                   }
                               }
        );
    }

}
