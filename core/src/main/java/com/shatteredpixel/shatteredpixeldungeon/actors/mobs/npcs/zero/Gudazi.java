package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.GodNPC;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.HealLanFire;
import com.shatteredpixel.shatteredpixeldungeon.android.AndroidGameRecords;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.plot.GudaziPlot;
import com.shatteredpixel.shatteredpixeldungeon.items.Torch;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfNoWater;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfBlink;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GudaziSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.watabou.noosa.Game;
import com.watabou.noosa.tweeners.Delayer;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class Gudazi extends GodNPC {

    {
        spriteClass = GudaziSprite.class;
        properties.add(Property.UNKNOWN);
    }


    private boolean first=true;
    private boolean secnod=true;
    private boolean rd=true;

    private static final String FIRST = "first";
    private static final String SECNOD = "secnod";
    private static final String RD = "rd";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(FIRST, first);
        bundle.put(SECNOD, secnod);
        bundle.put(RD, rd);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        first = bundle.getBoolean(FIRST);
        secnod = bundle.getBoolean(SECNOD);
        rd = bundle.getBoolean(RD);
    }

    private String def_verb(){
        //格挡动画
        ((GudaziSprite)sprite).What_Up();
        //1s延迟后，恢复闲置状态
        GameScene.scene.add(new Delayer(1f){
            @Override
            protected void onComplete() {
                sprite.idle();
            }
        });
        if(Random.Int(100)>=50){
            return Messages.get(this, "def_verb_1");
        } else {
            return Messages.get(this, "def_verb_2");
        }

    }

    public String defenseVerb() {
        return def_verb();
    }

    @Override
    public boolean act() {

        if (Statistics.gdzHelpDungeon == 4 && Dungeon.depth == 21){
            destroy();
            sprite.die();
            Statistics.gdzHelpDungeon++;
            GLog.n(Messages.get(this,"dead"));
        }

        return super.act();
    }

    @Override
    public boolean interact(Char c) {

        sprite.turnTo(pos, hero.pos);
        GudaziPlot plot = new GudaziPlot();
        GudaziPlot.GudaziRDPlot rdplot = new GudaziPlot.GudaziRDPlot();
        if(Dungeon.depth == 21 && Statistics.gdzHelpDungeon >= 5){
            HealLanFire healLanFire = new HealLanFire();
            healLanFire.pos = pos;
            GameScene.add(healLanFire);
            destroy();
            sprite.die();
        } else if(Dungeon.depth == 21 && Statistics.gdzHelpDungeon == 4){
            Game.runOnRenderThread(new Callback() {
                @Override
                public void call() {
                    GameScene.show(new WndDialog(rdplot,false));
                }
            });
        } else if(Dungeon.depth == 4 && Statistics.gdzHelpDungeon == 1){
            Dungeon.level.drop(new Torch(),hero.pos);
            Dungeon.level.drop(new Torch(),hero.pos);
            yell(Messages.get(this,"level4"));
            destroy();
            sprite.die();
            Statistics.gdzHelpDungeon++;
        } else if(Dungeon.depth == 9 && Statistics.gdzHelpDungeon == 2){
            Dungeon.level.drop(new StoneOfBlink(),hero.pos);
            Dungeon.level.drop(new StoneOfBlink(),hero.pos);
            yell(Messages.get(this,"level9"));
            destroy();
            sprite.die();
            Statistics.gdzHelpDungeon++;
        } else if(Dungeon.depth == 14 && Statistics.gdzHelpDungeon == 3){
            yell(Messages.get(this,"level14"));
            Dungeon.level.drop(new PotionOfNoWater(),hero.pos);
            Dungeon.level.drop(new PotionOfHealing(),hero.pos);
            Dungeon.level.drop(new PotionOfNoWater(),hero.pos);
            destroy();
            sprite.die();
            Statistics.gdzHelpDungeon++;
        } else if (first) {
            Game.runOnRenderThread(new Callback() {
                @Override
                public void call() {
                    GameScene.show(new WndDialog(plot,false));
                }
            });
            first=false;
        } else if(secnod) {
            if(Dungeon.gold >= 300 && secnod){
                Game.runOnRenderThread(new Callback() {
                    @Override
                    public void call() {
                        GameScene.show(new WndOptions(new GudaziSprite(),
                                Messages.titleCase(Messages.get(Gudazi.class, "name")),
                                Messages.get(Gudazi.class, "quest_start_prompt"),
                                Messages.get(Gudazi.class, "enter_yes"),
                                Messages.get(Gudazi.class, "enter_no")) {
                            @Override
                            protected void onSelect(int index) {
                                if (index == 0) {
                                    Dungeon.gold -= 300;
                                    AndroidGameRecords.GudaziGoldCount(300);
                                    Statistics.gdzHelpDungeon++;
                                    secnod = false;
                                    yell(Messages.get(Gudazi.class, "good"));
                                } else {
                                    yell(Messages.get(Gudazi.class, "not"));
                                    secnod = false;
                                }
                            }
                        });
                    }

                });
            } else if(!Statistics.amuletObtained) {

            } else {

            }

        }
        return true;
    }
}