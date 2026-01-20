package com.shatteredpixel.shatteredpixeldungeon.custom.utils.plot.hollow;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.status.SliceDeadBless;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.hollow.DeathRong;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.hollow.SliceGirl;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.plot.Plot;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SliceGirlSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialog;
import com.watabou.noosa.Image;
import com.watabou.noosa.tweeners.AlphaTweener;
import com.watabou.utils.Callback;

public class SlicePlot extends Plot {

    {
        process = 1 ;
    }
    private final static int maxprocess = 6;
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

    boolean diaglogic = false;
    boolean alt_diaglogic = false;

    boolean branch_logic = false;

    public DeathRong rong;

    @Override
    public void process() {

        for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])){
            if (mob instanceof DeathRong) {
                rong = (DeathRong) mob;
            }
        }

        if(diagulewindow!=null && process < 3) {
            switch (process) {
                case 1:
                    process_to_1();
                    break;
                case 2:
                    process_to_2();
                    break;
            }
            diagulewindow.update();
            process ++;
        } else if(diagulewindow != null && diaglogic && !alt_diaglogic && process <5){
            switch (process) {
                case 3:
                    process_to_3();
                    break;
                case 4:
                    process_to_4();
                    break;
            }
            diagulewindow.update();
            process ++;
        } else if(alt_diaglogic && diaglogic && diagulewindow != null) {
            switch (process) {
                case 5:
                    process_to_5();
                    break;
                case 6:
                    process_to_6();
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

    }

    private void process_to_1()
    {
        diagulewindow.hideSkip();
        diagulewindow.hideAll();
        diagulewindow.setMainAvatar(new Image(Assets.Splashes.Silence_0));
        diagulewindow.setLeftName("???");
        diagulewindow.changeText(Messages.get(SliceGirl.class,"message1",hero.name()));
    }

    private void process_to_2()
    {
        diagulewindow.setMainAvatar(new Image(Assets.Splashes.Silence_1));
        diagulewindow.changeText(Messages.get(SliceGirl.class,"message2"));
        RedButton dialogButton = new RedButton(Messages.get(SliceGirl.class,"choice1")){
            @Override
            protected void onClick() {
                diaglogic = true;
                destroy();
                process_to_3();
                diagulewindow.update();
            }
        };
        dialogButton.setRect(diagulewindow.thirdAvatar.x - diagulewindow.rightname.width(),diagulewindow.chrome.y-30,60,16);
        diagulewindow.add(dialogButton);
    }

    private void process_to_3()
    {
        diagulewindow.changeText(Messages.get(SliceGirl.class,"message3"));
    }

    private void process_to_4()
    {
        diagulewindow.changeText(Messages.get(SliceGirl.class,"message4"));
        RedButton dialogButton = new RedButton(Messages.get(SliceGirl.class,"choice2")){
            @Override
            protected void onClick() {
                alt_diaglogic = true;
                destroy();
                process_to_5();
                diagulewindow.update();
            }
        };
        dialogButton.setRect(diagulewindow.thirdAvatar.x - diagulewindow.rightname.width(),diagulewindow.chrome.y-30,60,16);
        diagulewindow.add(dialogButton);
    }


    private void process_to_5()
    {
        diagulewindow.setMainAvatar(new Image(Assets.Splashes.Silence_2));
        diagulewindow.changeText(Messages.get(SliceGirl.class,"message5"));
    }

    private void process_to_6() {
        diagulewindow.setLeftName(Messages.get(SliceGirl.class,"name"));
        diagulewindow.setMainAvatar(new Image(Assets.Splashes.Silence_3));
        diagulewindow.changeText(Messages.get(SliceGirl.class, "message6"));
        Buff.affect(hero, SliceDeadBless.class).set( 100, 1 );
        hero.busy();
        for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])){
            if (mob instanceof SliceGirl) {
                ((SliceGirlSprite)mob.sprite).leapPrep(Dungeon.level.exit()+6);
                mob.sprite.parent.add( new AlphaTweener( mob.sprite, 0, 3f ) {
                    @Override
                    protected void onComplete() {
                       mob.sprite.killAndErase();
                        hero.spendAndNext(1);
                        hero.sprite.operate(hero.pos);
                    }
                } );
                mob.sprite.jump(Dungeon.level.exit(), Dungeon.level.exit()+6,0,3f, new Callback() {
                    @Override
                    public void call() {
                        mob.die(true);
                        mob.move(Dungeon.level.exit()+6);
                        Dungeon.level.occupyCell(hero);
                        Dungeon.observe();
                        GameScene.updateFog();
                        Statistics.godGirl = true;
                    }
                });
            }
        }
    }

}

