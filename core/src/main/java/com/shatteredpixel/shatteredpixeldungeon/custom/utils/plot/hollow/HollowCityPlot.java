package com.shatteredpixel.shatteredpixeldungeon.custom.utils.plot.hollow;

import static com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene.scene;

import com.shatteredpixel.shatteredpixeldungeon.custom.utils.plot.Plot;
import com.shatteredpixel.shatteredpixeldungeon.effects.BannerSprites;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.Banner;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialog;

public class HollowCityPlot extends Plot {

    private final static int maxprocess = 4;

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
        diagulewindow.changeText(Messages.get(HollowCityPlot.class,"message1"));
        diagulewindow.setLeftName("???");
    }

    private void process_to_2()
    {
        diagulewindow.changeText(Messages.get(HollowCityPlot.class,"message2"));
    }

    private void process_to_3()
    {
        diagulewindow.setLeftName(Messages.get(HollowCityPlot.class,"name"));
        diagulewindow.changeText(Messages.get(HollowCityPlot.class,"message3"));
    }

    private void HollowLogo(){
        Banner mapnameSlain = new Banner( BannerSprites.get( BannerSprites.Type.NULL ) );
        mapnameSlain.texture( "interfaces/mapname/hollow.png" );
        mapnameSlain.show( Window.CYELLOW, 0.6f, 3f );
        scene.showLogo( mapnameSlain );
    }

    private void process_to_4()
    {
        diagulewindow.changeText(Messages.get(HollowCityPlot.class,"message4"));
    }
}
