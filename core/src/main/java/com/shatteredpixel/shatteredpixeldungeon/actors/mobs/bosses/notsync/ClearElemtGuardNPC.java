package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.notsync;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.lb.RivalSprite;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NTNPC;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.plot.ClearElemtPlot;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.LingJing;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ClearGuardSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.watabou.noosa.Game;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;

public class ClearElemtGuardNPC extends NTNPC {

    private boolean first=true;
    private boolean secnod=true;
    private boolean rd=true;
    private boolean sd=true;

    private int progress = 1;

    private static final String FIRST = "first";
    private static final String SECNOD = "secnod";
    private static final String RD = "rd";
    private static final String SD = "sd";


    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(FIRST, first);
        bundle.put(SECNOD, secnod);
        bundle.put(RD, rd);
        bundle.put(SD, sd);
        bundle.put("progress", progress);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        first = bundle.getBoolean(FIRST);
        secnod = bundle.getBoolean(SECNOD);
        rd = bundle.getBoolean(RD);
        sd = bundle.getBoolean(SD);
        progress = bundle.getInt("progress");
    }

    {
        spriteClass = ClearGuardSprite.ClearNPCGuardSprite.class;
    }

    @Override
    public boolean interact(Char c) {

        sprite.turnTo(pos, hero.pos);
        ClearElemtPlot plot = new ClearElemtPlot();
        if (first) {
            Game.runOnRenderThread(new Callback() {
                @Override
                public void call() {
                    GameScene.show(new WndDialog(plot,false));
                }
            });
            first=false;
        } else if(secnod) {
            Game.runOnRenderThread(new Callback() {
                @Override
                public void call() {
                    GameScene.show(new WndOptions(new ClearGuardSprite.ClearNPCGuardSprite(),
                            Messages.titleCase(Messages.get(ClearElemtGuard.class, "name")),
                            Messages.get(ClearElemtGuard.class, "quest_start_prompt"),
                            Messages.get(ClearElemtGuard.class, "enter_yes"),
                            Messages.get(ClearElemtGuard.class, "enter_no")) {
                        @Override
                        protected void onSelect(int index) {
                            if (index == 0) {
                                die(true);
                                EXP += 30;
                                GLog.w(Messages.get(ClearElemtGuard.class, "sad_a"));
                            } else if(index == 1){
                                GLog.w(Messages.get(ClearElemtGuard.class, "pro_a"));
                                secnod = false;
                            }
                        }
                    });
                }

            });
        } else if(rd){
            ClearElemtPlot.SE plot2 = new ClearElemtPlot.SE();
            if (rd) {
                Game.runOnRenderThread(new Callback() {
                    @Override
                    public void call() {
                        GameScene.show(new WndDialog(plot2, false));
                    }
                });
                rd = false;
            }
        } else if(sd) {
            Game.runOnRenderThread(new Callback() {
                @Override
                public void call() {
                    GameScene.show(new WndOptions(new ClearGuardSprite.ClearNPCGuardSprite(),
                            Messages.titleCase(Messages.get(ClearElemtGuard.class, "name")),
                            Messages.get(ClearElemtGuard.class, "quest_start_prompt2"),
                            Messages.get(ClearElemtGuard.class, "enter_yes2"),
                            Messages.get(ClearElemtGuard.class, "enter_no2")) {
                        @Override
                        protected void onSelect(int index) {
                            if (index == 0) {
                                die(true);
                                GLog.w(Messages.get(ClearElemtGuard.class, "sad_b"));
                                Dungeon.level.drop( ( Generator.randomUsingDefaults( Generator.Category.ARTIFACT ) ), hero.pos );
                            } else if(index == 1){
                                sd = false;
                                GLog.w(Messages.get(ClearElemtGuard.class, "pro_b"));
                            }
                        }
                    });
                }

            });
        } else {
            switch (progress){
                case 1:
                    Game.runOnRenderThread(new Callback() {
                        @Override
                        public void call() {
                            GameScene.show(new WndOptions(new RivalSprite(),
                                    Messages.titleCase(Messages.get(ClearElemtGuard.class, "myname",hero.name())),
                                    Messages.get(ClearElemtGuard.class, "quest_start_prompt3"),
                                    Messages.get(ClearElemtGuard.class, "enter")) {
                                @Override
                                protected void onSelect(int index) {
                                    if (index == 0) {
                                        yell(Messages.get(ClearElemtGuard.class, "pro_c"));
                                        progress++;
                                    }
                                }
                            });
                        }

                    });
                break;
                case 2:
                    Game.runOnRenderThread(new Callback() {
                        @Override
                        public void call() {
                            GameScene.show(new WndOptions(new RivalSprite(),
                                    Messages.titleCase(Messages.get(ClearElemtGuard.class, "myname",hero.name())),
                                    Messages.get(ClearElemtGuard.class, "quest_start_prompt4"),
                                    Messages.get(ClearElemtGuard.class, "enter")) {
                                @Override
                                protected void onSelect(int index) {
                                    if (index == 0) {
                                        yell(Messages.get(ClearElemtGuard.class, "pro_d"));
                                        progress++;
                                    }
                                }
                            });
                        }
                    });
                break;
                case 3:
                    Game.runOnRenderThread(new Callback() {
                        @Override
                        public void call() {
                            GameScene.show(new WndOptions(new RivalSprite(),
                                    Messages.titleCase(Messages.get(ClearElemtGuard.class, "myname",hero.name())),
                                    Messages.get(ClearElemtGuard.class, "quest_start_prompt5"),
                                    Messages.get(ClearElemtGuard.class, "enter")) {
                                @Override
                                protected void onSelect(int index) {
                                    if (index == 0) {
                                        yell(Messages.get(ClearElemtGuard.class, "pro_e"));
                                        progress++;
                                    }
                                }
                            });
                        }
                    });
                break;
                case 4:
                    Game.runOnRenderThread(new Callback() {
                        @Override
                        public void call() {
                            GameScene.show(new WndOptions(new RivalSprite(),
                                    Messages.titleCase(Messages.get(ClearElemtGuard.class, "myname",hero.name())),
                                    Messages.get(ClearElemtGuard.class, "quest_start_prompt6"),
                                    Messages.get(ClearElemtGuard.class, "enter")) {
                                @Override
                                protected void onSelect(int index) {
                                    if (index == 0) {
                                        yell(Messages.get(ClearElemtGuard.class, "pro_f"));
                                        progress++;
                                    }
                                }
                            });
                        }
                    });
                break;
                case 5:
                    Game.runOnRenderThread(new Callback() {
                        @Override
                        public void call() {
                            GameScene.show(new WndOptions(new RivalSprite(),
                                    Messages.titleCase(Messages.get(ClearElemtGuard.class, "myname",hero.name())),
                                    Messages.get(ClearElemtGuard.class, "quest_start_prompt7"),
                                    Messages.get(ClearElemtGuard.class, "enter")) {
                                @Override
                                protected void onSelect(int index) {
                                    if (index == 0) {
                                        yell(Messages.get(ClearElemtGuard.class, "pro_g"));
                                        progress++;
                                    }
                                }
                            });
                        }
                    });
                    break;
                case 6:
                    Game.runOnRenderThread(new Callback() {
                        @Override
                        public void call() {
                            GameScene.show(new WndOptions(new RivalSprite(),
                                    Messages.titleCase(Messages.get(ClearElemtGuard.class, "myname",hero.name())),
                                    Messages.get(ClearElemtGuard.class, "quest_start_prompt8"),
                                    Messages.get(ClearElemtGuard.class, "enter")) {
                                @Override
                                protected void onSelect(int index) {
                                    if (index == 0) {
                                       yell(Messages.get(ClearElemtGuard.class, "pro_h"));
                                        progress++;
                                    }
                                }
                            });
                        }
                    });
                 break;
                case 7:
                    Game.runOnRenderThread(new Callback() {
                        @Override
                        public void call() {
                            GameScene.show(new WndOptions(new ClearGuardSprite.ClearNPCGuardSprite(),
                                    Messages.titleCase(Messages.get(ClearElemtGuard.class, "name")),
                                    Messages.get(ClearElemtGuard.class, "quest_start_prompt9"),
                                    Messages.get(ClearElemtGuard.class, "yesgo")) {
                                @Override
                                protected void onSelect(int index) {
                                    if (index == 0) {
                                        progress++;
                                    }
                                }
                            });
                        }
                    });
                 break;
                case 8:
                    Game.runOnRenderThread(new Callback() {
                        @Override
                        public void call() {
                            GameScene.show(new WndOptions(new ClearGuardSprite.ClearNPCGuardSprite(),
                                    Messages.titleCase(Messages.get(ClearElemtGuard.class, "name")),
                                    Messages.get(ClearElemtGuard.class, "quest_start_prompt10"),
                                    Messages.get(ClearElemtGuard.class, "myname",hero.name())) {
                                @Override
                                protected void onSelect(int index) {
                                    if (index == 0) {
                                        progress++;
                                        yell( Messages.get(ClearElemtGuard.class, "thanks",hero.name()));
                                    }
                                }
                            });
                        }
                    });
                break;
                case 9:
                    Game.runOnRenderThread(new Callback() {
                        @Override
                        public void call() {
                            GameScene.show(new WndOptions(new ClearGuardSprite.ClearNPCGuardSprite(),
                                    Messages.titleCase(Messages.get(ClearElemtGuard.class, "name")),
                                    Messages.get(ClearElemtGuard.class, "quest_start_prompt11"),
                                    Messages.get(ClearElemtGuard.class, "let")) {
                                @Override
                                protected void onSelect(int index) {
                                    if (index == 0) {
                                        progress++;
                                        Statistics.unLockedFireDargon = true;
                                        Dungeon.level.drop(new LingJing(),hero.pos);
                                        yell( Messages.get(ClearElemtGuard.class, "thanks2",hero.name()));
                                    }
                                }
                            });
                        }
                    });
                break;
            }
        }
        return true;
    }


    @Override
    public void die( Object cause ) {
        super.die( cause );


        if(SPDSettings.KillDragon()){
            Mob mob = new FayiNa();
            mob.pos = pos;
            GameScene.add(mob);
        }

    }

    @Override
    protected boolean act() {
        if (Statistics.GameKillFireDargon) {
           die(true);
        }
        return super.act();
    }

}
