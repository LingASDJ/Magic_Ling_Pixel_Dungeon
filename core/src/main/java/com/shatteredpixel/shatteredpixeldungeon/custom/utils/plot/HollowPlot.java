package com.shatteredpixel.shatteredpixeldungeon.custom.utils.plot;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;
import static com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene.scene;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.Morphs;
import com.shatteredpixel.shatteredpixeldungeon.effects.BannerSprites;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.Banner;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialog;
import com.watabou.noosa.Image;

public class HollowPlot extends Plot {

    private final static int maxprocess = 8;

    {
        process = 1 ;
    }

    protected String getPlotName() {
        return SEWER_NAME;
    }

    @Override
    public void reachProcess(WndDialog wndDialog) {
        diagulewindow = wndDialog;

        while(this.process < needed_process )
        {
            this.process();
        }
    }

    @Override
    public void process() {
        if(diagulewindow!=null) {
            switch (process) {
                default:
                case 1:
                    process_to_1();//Mostly process to 1 is made directly when creating,it might not be used,just in case
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
                case 6:
                    process_to_6();
                    break;
                case 7:
                    process_to_7();
                    break;
                case 8:
                    process_to_8();
                    break;
//                case 10:
//                    process_to_10();
//                    break;
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
        return process > maxprocess;
    }

    @Override
    public void skip() {
        diagulewindow.cancel();
        WndDialog.settedPlot = null;
    }

    private void process_to_1()
    {
        diagulewindow.hideAll();
        diagulewindow.setMainColor(Window.CBLACK);
        diagulewindow.setMainAvatar(new Image(Assets.Splashes.MOSRDX));
        diagulewindow.setLeftName(Messages.get(Morphs.class,"unknown"));
        diagulewindow.changeText(Messages.get(Morphs.class,"message1",hero.name()));
    }

    private void process_to_2()
    {
        diagulewindow.changeText(Messages.get(Morphs.class,"message2"));
    }

    private void process_to_3()
    {
        diagulewindow.changeText(Messages.get(Morphs.class,"message3"));
    }

    private void process_to_4()
    {
        diagulewindow.changeText(Messages.get(Morphs.class,"message4"));

    }

    private void process_to_5()
    {
        diagulewindow.setSecondColor(Window.CBLACK);
        diagulewindow.setSecondAvatar(new Image(Assets.Splashes.GHOSTJ));
        diagulewindow.changeText(Messages.get(Morphs.class,"message5"));
    }

    private void process_to_6()
    {
        diagulewindow.hideMainAvatar();
        diagulewindow.changeText(Messages.get(Morphs.class,"message6"));
    }

    private void process_to_7()
    {
        diagulewindow.hideSecondAvatar();
        diagulewindow.changeText(Messages.get(Morphs.class,"message7",hero.name()));
    }

    private void process_to_8()
    {
        diagulewindow.changeText(Messages.get(Morphs.class,"message8"));
        Banner mapnameSlain = new Banner( BannerSprites.get( BannerSprites.Type.NULL ) );

        mapnameSlain.texture("interfaces/mapname/hollow.png");
        mapnameSlain.show(Window.CBLACK, 0.6f, 3f);
        scene.showLogo(mapnameSlain);
    }

//    private void process_to_4()
//    {
//        diagulewindow.setMainAvatar(Script.Portrait(Script.Character.CHEN));
//        diagulewindow.setLeftName(Script.Name(Script.Character.CHEN));
//        diagulewindow.changeText(Messages.get(this, "txt1"));
//    }

//    private void process_to_5()
//    {
//        diagulewindow.darkenMainAvatar();
//
//        diagulewindow.setSecondAvatar(Script.Portrait(Script.Character.RED));
//        diagulewindow.setRightName(Script.Name(Script.Character.RED));
//        diagulewindow.changeText(Messages.get(this, "txt2"));
//    }

}

