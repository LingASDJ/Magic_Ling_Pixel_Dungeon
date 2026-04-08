package com.shatteredpixel.shatteredpixeldungeon.custom.utils.plot.fiveyears;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.PaswordBadges;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.normal.SliceDream;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.Script;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.plot.Plot;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.UnlessFlower;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialog;
import com.watabou.noosa.Image;

public class SliceGirlPlot extends Plot {

    {
        process = 1;
    }

    boolean alt_diaglogic = false;

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
    int maxprocess;
    @Override
    public void process() {
        if(diagulewindow!=null && process < maxprocess) {
            switch (process) {
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
                case 5:
                    process_to_5();
                    break;
            }
            diagulewindow.update();
            process ++;
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

        if(alt_diaglogic){
            maxprocess = 6;
        } else {
            maxprocess = 5;
        }

        return process > maxprocess;
    }

    @Override
    public void skip() {

    }

    private void process_to_1() {
        diagulewindow.removeSkip();
        diagulewindow.hideAll();
        hero.interrupt();
        diagulewindow.setMainAvatar(new Image(Assets.Splashes.Silence_0));
        diagulewindow.setLeftName(Messages.get(SliceDream.class, "name"));
        diagulewindow.changeText(Messages.get(SliceDream.class, "messages1",hero.name()));
    }

    private void process_to_2() {
        diagulewindow.hideAll();
        hero.interrupt();
        diagulewindow.setLeftName(Script.Name(Script.Character.NOBODY));
        diagulewindow.changeText(Messages.get(SliceDream.class, "messages2"));
    }

    private void process_to_3() {
        diagulewindow.setLeftName(Messages.get(SliceDream.class, "name"));
        diagulewindow.setMainAvatar(new Image(Assets.Splashes.Silence_1));
        diagulewindow.changeText(Messages.get(SliceDream.class, "messages3",hero.name()));
    }


    RedButton Select_B_Button;
    RedButton Select_A_Button;

    private void process_to_4() {
        diagulewindow.setMainAvatar(new Image(Assets.Splashes.Silence_3));
        diagulewindow.changeText(Messages.get(SliceDream.class, "messages4"));

        Select_A_Button = new RedButton(Messages.get(SliceDream.class,"A")){
            @Override
            protected void onClick() {
                destroy();
                Select_A_Button.destroy();
                diagulewindow.setMainAvatar(new Image(Assets.Splashes.Silence_4));
                diagulewindow.changeText(Messages.get(SliceDream.class, "messages5",hero.name()));
            }
        };
        Select_A_Button.setRect(diagulewindow.thirdAvatar.x - diagulewindow.rightname.width()-60,diagulewindow.chrome.y-30,50,16);
        diagulewindow.add(Select_A_Button);

        Select_B_Button = new RedButton(Messages.get(SliceDream.class,"B")){
            @Override
            protected void onClick() {
                alt_diaglogic = true;
                destroy();
                Select_A_Button.destroy();
                process_to_5();
                process = 5;
                Dungeon.level.drop(new UnlessFlower(),hero.pos).sprite.drop();
            }
        };
        Select_B_Button.setRect(diagulewindow.thirdAvatar.x + diagulewindow.rightname.width()-30,diagulewindow.chrome.y-30,50,16);
        diagulewindow.add(Select_B_Button);
    }


    private void process_to_5() {
        diagulewindow.setLeftName(Messages.get(SliceDream.class, "name"));
        diagulewindow.setMainAvatar(new Image(Assets.Splashes.Silence_1));
        diagulewindow.changeText(Messages.get(SliceDream.class, "messages6",hero.name()));
        process++;
    }


    public static class TND extends Plot {
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
            diagulewindow.cancel();
            WndDialog.settedPlot = null;
        }

        private void process_to_1() {
            diagulewindow.hideAll();
            hero.interrupt();
            diagulewindow.setMainAvatar(new Image(Assets.Splashes.Silence_2));
            diagulewindow.setLeftName(Messages.get(SliceDream.class, "name"));
            diagulewindow.changeText(Messages.get(SliceDream.class, "messages7"));
        }

    }

    public static class END extends Plot {
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
            diagulewindow.cancel();
            WndDialog.settedPlot = null;
        }

        private void process_to_1() {
            diagulewindow.hideAll();
            hero.interrupt();
            diagulewindow.setMainAvatar(new Image(Assets.Splashes.Silence_0));
            diagulewindow.setLeftName(Messages.get(SliceDream.class, "name"));
            diagulewindow.changeText(Messages.get(SliceDream.class, "messages8"));
            PaswordBadges.FlowerHome(Challenges.activeChallenges());
            SPDSettings.RecordFlowerChallengs(Challenges.activeChallenges());
        }

    }

    public static class ENDLOOP extends Plot {
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
            diagulewindow.cancel();
            WndDialog.settedPlot = null;
        }

        private void process_to_1() {
            diagulewindow.hideAll();
            hero.interrupt();
            diagulewindow.setLeftName(Messages.get(SliceDream.class, "name"));
            diagulewindow.changeText(Messages.get(SliceDream.class, "messages9"));
        }

    }
    
}
