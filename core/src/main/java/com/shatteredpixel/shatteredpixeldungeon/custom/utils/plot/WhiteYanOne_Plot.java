package com.shatteredpixel.shatteredpixeldungeon.custom.utils.plot;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.WhiteYan;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.WhiteYanBossSprite;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class WhiteYanOne_Plot extends Plot {

    private final static int maxprocess = 4;

    {
        process = 1;
    }

    protected String getPlotName() {
        return SEWER_NAME;
    }

    @Override
    public void reachProcess(WndDialog wndDialog) {
        diagulewindow = wndDialog;

        while (this.process < needed_process) {
            this.process();
        }
    }

    @Override
    public void process() {
        if (diagulewindow != null) {
            switch (process) {
                default:
                case 1:
                    process_to_1();
                    break;
                case 2:
                    process_to_2();
                    break;
                case 3:
                    process_to_3();
                    break;
                case 4:
                    process_to_4();
                    break;
            }
            diagulewindow.update();
            process++;
        }
    }

    @Override
    public void initial(WndDialog wndDialog) {
        diagulewindow = wndDialog;
        process = 2;
        process_to_1();
    }

    @Override
    public boolean end() {
        return process > maxprocess;
    }

    @Override
    public void skip() {
    }

    private void process_to_1() {
        diagulewindow.hideAll();
        Dungeon.hero.interrupt();
        diagulewindow.setMainAvatar(new Image(Assets.Splashes.WY_WXHAT));
        diagulewindow.setLeftName(Messages.get(WhiteYan.class, "name"));
        diagulewindow.changeText(Messages.get(WhiteYan.class, "sessage1"));
    }

    private void process_to_2() {
        diagulewindow.changeText(Messages.get(WhiteYan.class, "sessage2"));
    }

    private void process_to_3() {
        diagulewindow.changeText("Select Your Talk");

        Game.runOnRenderThread(new Callback() {
            @Override
            public void call() {
                GameScene.show(new WndOptions(new WhiteYanBossSprite(),
                        Messages.titleCase(Messages.get(WhiteYan.class, "name")),
                        Messages.get(WhiteYan.class, "quest_start_prompt"),
                        Messages.get(WhiteYan.class, "enter_yes"),
                        Messages.get(WhiteYan.class, "enter_no")) {
                    @Override
                    public void onBackPressed() {

                    }
                    @Override
                    protected void onSelect(int index) {
                        if (index == 0) {
                            diagulewindow.setMainAvatar(new Image(Assets.Splashes.WY_NOLAN));
                            diagulewindow.changeText(Messages.get(WhiteYan.class, "sessage3"));
                            hide();
                        } else {
                            diagulewindow.setMainAvatar(new Image(Assets.Splashes.WY_HAPPY));
                            diagulewindow.changeText(Messages.get(WhiteYan.class, "sessage4"));
                            hide();
                        }
                    }
                });
            }

        });
    }

    private void process_to_4() {
        diagulewindow.changeText((Messages.get(WhiteYan.class, "d_message3")));
    }

    public static class WhiteYanHotelPlotEND extends Plot {


        private final static int maxprocess = 1;

        {
            process = 1;
        }

        protected String getPlotName() {
            return SEWER_NAME;
        }

        @Override
        public void reachProcess(WndDialog wndDialog) {
            diagulewindow = wndDialog;

            while (this.process < needed_process) {
                this.process();
            }
        }

        @Override
        public void process() {
            if (diagulewindow != null) {
                switch (process) {
                    default:
                    case 1:
                        process_to_1();
                        break;
                }
                diagulewindow.update();
                process++;
            }
        }

        @Override
        public void initial(WndDialog wndDialog) {
            diagulewindow = wndDialog;
            process = 2;
            process_to_1();
        }

        @Override
        public boolean end() {
            return process > maxprocess;
        }

        @Override
        public void skip() {
        }

        private void process_to_1() {
            diagulewindow.hideAll();
            Dungeon.hero.interrupt();
            diagulewindow.setMainAvatar(new Image(Assets.Splashes.BZMR));
            diagulewindow.setLeftName(Messages.get(WhiteYan.class, "name"));

            switch (Random.Int(3)){
                case 0:
                    diagulewindow.changeText((Messages.get(WhiteYan.class, "e_message1")));
                    break;
                case 1:
                    diagulewindow.changeText((Messages.get(WhiteYan.class, "e_message2")));
                    break;
                case 2:
                    diagulewindow.changeText((Messages.get(WhiteYan.class, "e_message3")));
                    break;
            }
        }


    }
}
