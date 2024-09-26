package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Levitation;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NTNPC;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.plot.PianoLePlot;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.PianoLeSprite;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.watabou.noosa.Game;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class PianoLe extends NTNPC {

    {
        spriteClass = PianoLeSprite.class;
        properties.add(Property.IMMOVABLE);
        properties.add(Property.UNKNOWN);
        maxLvl = -1;

    }


    @Override
    public String defenseVerb() {
        return def_verb();
    }
    private String def_verb(){
        if(Statistics.amuletObtained){
            return Messages.get(this, "def_verb_1");
        } else {
            return Messages.get(this, "def_verb_2");
        }

    }

    private boolean first=true;
    private boolean secnod=true;
    private boolean rd=true;
    private boolean td=true;
    private boolean sd=true;

    private static final String FIRST = "first";
    private static final String SECNOD = "secnod";
    private static final String RD = "rd";
    private static final String TD = "td";
    private static final String SD = "sd";


    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(FIRST, first);
        bundle.put(SECNOD, secnod);
        bundle.put(RD, rd);
        bundle.put(TD, td);
        bundle.put(SD, sd);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        first = bundle.getBoolean(FIRST);
        secnod = bundle.getBoolean(SECNOD);
        rd = bundle.getBoolean(RD);
        td = bundle.getBoolean(TD);
        sd = bundle.getBoolean(SD);
    }

    @Override
    public boolean interact(Char c) {
        sprite.turnTo(pos, Dungeon.hero.pos);
        PianoLePlot pianoLePlot = new PianoLePlot();
        PianoLePlot.PianoLePlotSecond B_plot = new PianoLePlot.PianoLePlotSecond();
        PianoLePlot.End C_plot = new PianoLePlot.End();

        if(first){
            Game.runOnRenderThread(new Callback() {
                @Override
                public void call() {
                    GameScene.show(new WndDialog(pianoLePlot,false));
                }
            });
            first=false;
        } else if(secnod){
            Game.runOnRenderThread(new Callback() {
                @Override
                public void call() {
                    GameScene.show(new WndDialog(B_plot,false));
                }
            });
            secnod=false;
        } else if(rd && Random.NormalIntRange(0,99)<=25) {
            Game.runOnRenderThread(new Callback() {
                @Override
                public void call() {
                    GameScene.show(new WndOptions(new PianoLeSprite(),
                            Messages.titleCase(Messages.get(PianoLe.class, "name")),
                            Messages.get(PianoLe.class, "quest_start_prompt"),
                            Messages.get(PianoLe.class, "enter_yes"),
                            Messages.get(PianoLe.class, "enter_no")) {
                        @Override
                        protected void onSelect(int index) {
                            if (index == 0) {
                                Buff.affect( hero, Levitation.class, 100f );
                                rd = false;
                            }
                        }
                    });
                }

            });
        } else if(!rd && td && Random.NormalIntRange(0,99)<=25) {
            Game.runOnRenderThread(new Callback() {
                @Override
                public void call() {
                    GameScene.show(new WndOptions(new PianoLeSprite(),
                            Messages.titleCase(Messages.get(PianoLe.class, "name")),
                            Messages.get(PianoLe.class, "quest_start_prompt_2"),
                            Messages.get(PianoLe.class, "enter_yes"),
                            Messages.get(PianoLe.class, "enter_no")) {
                        @Override
                        protected void onSelect(int index) {
                            if (index == 0) {
                                Buff.affect( hero, Levitation.class, 1000f );
                                td = false;
                            }
                        }
                    });
                }

            });
        } else if(!rd && !td && sd && Random.NormalIntRange(0,99)<=25) {
            Game.runOnRenderThread(new Callback() {
                @Override
                public void call() {
                    GameScene.show(new WndOptions(new PianoLeSprite(),
                            Messages.titleCase(Messages.get(PianoLe.class, "name")),
                            Messages.get(PianoLe.class, "quest_start_prompt_3"),
                            Messages.get(PianoLe.class, "enter_yes"),
                            Messages.get(PianoLe.class, "enter_no")) {
                        @Override
                        protected void onSelect(int index) {
                            if (index == 0) {
                                Buff.affect( hero, Levitation.class, 10000f );
                                sd = false;
                                yell(Messages.get(PianoLe.class, "not_magic"));
                            }
                        }
                    });
                }

            });
        } else {
            Game.runOnRenderThread(new Callback() {
                @Override
                public void call() {
                    GameScene.show(new WndDialog(C_plot,false));
                }
            });
        }



        return true;
    }

}
