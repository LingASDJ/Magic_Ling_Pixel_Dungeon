package com.shatteredpixel.shatteredpixeldungeon.custom.utils.plot;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.GamesInProgress;
import com.shatteredpixel.shatteredpixeldungeon.Rankings;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Levitation;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.TrueInvisibiity;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.huntress.SpiritHawk;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.BloodBat;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.hollow.DeathRong;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.hollow.ZeroBoat;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.pets.Pets;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.Script;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.DriedRose;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.RankingsScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialog;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.tweeners.Delayer;

public class BoatPlot extends Plot {

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

        if(diagulewindow!=null && process < 5) {
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
            }
            diagulewindow.update();
            process ++;
        } else if(diagulewindow != null && diaglogic && !alt_diaglogic && process <=8){
            switch (process) {
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
                    process_to_Select();
                    break;
            }
            diagulewindow.update();
            process ++;
        } else if(diagulewindow != null && alt_diaglogic){
            if(branch_logic){
                switch (process) {
                    case 9:
                        process_to_9A_1();
                        break;
                    case 10:
                        process_to_9A_2();
                        break;
                    case 11:
                        process_to_9A_3();
                        break;
                    case 12:
                        process_to_9A_4();
                        break;
                    case 13:
                        process_to_9A_5();
                        break;
                    case 14:
                        process_to_9A_6();
                        break;
                    case 15:
                        process_to_9A_7();
                        break;
                    case 16:
                        process_to_9A_8();
                        break;
                    case 17:
                        process_to_9A_9();
                        break;
                    case 18:
                        process_to_9A_10();
                        break;
                    case 19:
                        process_to_9A_11();
                        break;
                }
            } else {
                switch (process) {
                    case 9:
                        process_to_9B_1();
                        break;
                    case 10:
                        process_to_9B_2();
                        break;
                    case 11:
                        process_to_9B_3();
                        break;
                }
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
        int maxprocess;
        if(alt_diaglogic && branch_logic){
            maxprocess = 19;
        } else {
            maxprocess = 11;
        }
        return process > maxprocess;
    }



    @Override
    public void skip() {

    }

    private void process_to_1()
    {
        diagulewindow.hideSkip();
        diagulewindow.hideAll();
        diagulewindow.setMainAvatar(new Image(Assets.Splashes.KARONG_1));
        diagulewindow.setLeftName(Messages.get(DeathRong.class,"name"));
        diagulewindow.changeText(Messages.get(DeathRong.class,"message1",hero.name()));
    }

    private void process_to_2()
    {
        diagulewindow.setMainAvatar(new Image(Assets.Splashes.KARONG_2));
        diagulewindow.changeText(Messages.get(DeathRong.class,"message2"));
    }

    private void process_to_3()
    {
        diagulewindow.setMainAvatar(new Image(Assets.Splashes.KARONG_3));
        diagulewindow.changeText(Messages.get(DeathRong.class,"message3"));
    }

    private void process_to_4()
    {
        diagulewindow.setMainAvatar(new Image(Assets.Splashes.KARONG_0));
        diagulewindow.changeText(Messages.get(DeathRong.class,"message4"));
        //实验性
        RedButton dialogButton = new RedButton(Messages.get(DeathRong.class,"choice1")){
            @Override
            protected void onClick() {
                diaglogic = true;
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
        diagulewindow.setMainAvatar(new Image(Assets.Splashes.KARONG_0));
        diagulewindow.changeText(Messages.get(DeathRong.class,"message5"));
    }

    private void process_to_6()
    {
        diagulewindow.setMainAvatar(new Image(Assets.Splashes.KARONG_0));
        diagulewindow.changeText(Messages.get(DeathRong.class,"message6"));
    }

    private void process_to_7()
    {
        diagulewindow.setMainAvatar(new Image(Assets.Splashes.KARONG_3));
        diagulewindow.changeText(Messages.get(DeathRong.class,"message7"));
    }


    RedButton Select_B_Button;
    RedButton Select_A_Button;
    /** 分支选项 */
    private void process_to_Select()
    {
        diagulewindow.setMainAvatar(new Image(Assets.Splashes.KARONG_3));

        Select_A_Button = new RedButton(Messages.get(DeathRong.class,"button2a")){
            @Override
            protected void onClick() {
                if(Dungeon.gold>=6660000){
                   alt_diaglogic = true;
                   branch_logic = true;
                } else {
                   alt_diaglogic = true;
                }
                destroy();
                Select_B_Button.destroy();
                process_to_9A_1();
                diagulewindow.update();
            }
        };
        Select_A_Button.setRect(diagulewindow.thirdAvatar.x - diagulewindow.rightname.width(),diagulewindow.chrome.y-30,20,16);
        diagulewindow.add(Select_A_Button);
        diagulewindow.changeText(Messages.get(DeathRong.class,"message8"));

        Select_B_Button = new RedButton(Messages.get(DeathRong.class,"button2b")){
            @Override
            protected void onClick() {
                alt_diaglogic = true;
                destroy();
                Select_A_Button.destroy();
                //process_to_9B();
                diagulewindow.update();
            }
        };
        Select_B_Button.setRect(diagulewindow.thirdAvatar.x + diagulewindow.rightname.width(),diagulewindow.chrome.y-30,20,16);
        diagulewindow.add(Select_B_Button);
    }

    /** 选择A分支 */
    private void process_to_9A_1()
    {
        diagulewindow.setMainAvatar(new Image(Assets.Splashes.KARONG_3));
        diagulewindow.changeText(Messages.get(DeathRong.class,"message9a_1"));
    }

    private void process_to_9A_2()
    {
        diagulewindow.setMainAvatar(new Image(Assets.Splashes.KARONG_4));
        diagulewindow.changeText(Messages.get(DeathRong.class,"message9a_2"));
    }

    private void process_to_9A_3()
    {
        diagulewindow.setMainAvatar(new Image(Assets.Splashes.KARONG_4));
        diagulewindow.changeText(Messages.get(DeathRong.class,"message9a_3"));
    }

    private void process_to_9A_4()
    {
        Dungeon.gold -= 6660000;
        diagulewindow.setMainAvatar(new Image(Assets.Splashes.KARONG_5));
        diagulewindow.changeText(Messages.get(DeathRong.class,"message9a_4"));
    }

    public void moves(int step) {
        ScrollOfTeleportation.appear(hero, step);
        Buff.detach( hero, Levitation.class );
        Buff.detach( hero, TrueInvisibiity.class );
    }

    private void process_to_9A_5()
    {
        diagulewindow.setLeftName(Script.Name(Script.Character.NOBODY));
        diagulewindow.hideMainAvatar();
        diagulewindow.changeText(Messages.get(DeathRong.class,"message9a_5"));
        int dest = 921;

        for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
            if (mob instanceof ZeroBoat) {
                hero.sprite.jump(1357, dest, () -> {
                    Camera.main.shake(2, 0.5f);
                    hero.spendAndNext(1);
                    int doorPos = 918;

                    for (Mob mob1 : Dungeon.level.mobs.toArray(new Mob[0])){
                        if (	mob1 instanceof Pets ||
                                mob1 instanceof DriedRose.GhostHero ||
                                mob1 instanceof BloodBat ||
                                mob1 instanceof SpiritHawk.HawkAlly) {
                            ScrollOfTeleportation.appear(mob1, doorPos);
                        }
                    }
                });
                mob.sprite.jump(1357, dest, () -> {
                    mob.move(dest);
                    Camera.main.shake(2, 0.5f);
                    hero.spendAndNext(1);
                });

            }
        }
    }

    private void process_to_9A_6()
    {
        diagulewindow.changeText(Messages.get(DeathRong.class,"message9a_6"));
    }

    private void process_to_9A_7() {
        diagulewindow.changeText(Messages.get(DeathRong.class, "message9a_7"));
    }

    private void process_to_9A_8() {
        diagulewindow.changeText(Messages.get(DeathRong.class, "message9a_8"));
    }

    private void process_to_9A_9() {
        diagulewindow.changeText(Messages.get(DeathRong.class, "message9a_9"));
    }

    private void process_to_9A_10() {
        diagulewindow.changeText(Messages.get(DeathRong.class, "message9a_10"));
    }

    public static class DiedBoat{}
    /** 选择A分支 GAME-OVER*/
    private void process_to_9A_11() {
        GameScene.fadeToBlack(2f,6f);
        diagulewindow.changeText(Messages.get(DeathRong.class, "message9a_11"));
        //2s延迟后，恢复闲置状态
        GameScene.scene.add(new Delayer(3f){
            @Override
            protected void onComplete() {
                Dungeon.fail( DeathRong.class);
                Rankings.INSTANCE.submit(false, DiedBoat.class);
                Game.switchScene( RankingsScene.class );
                Dungeon.deleteGame( GamesInProgress.curSlot, true );
            }
        });
        rong.first = false;
    }


    /** 选择B分支 */
    private void process_to_9B_1()
    {
        diagulewindow.setMainAvatar(new Image(Assets.Splashes.KARONG_5));
        diagulewindow.changeText(Messages.get(DeathRong.class, "message9b_1"));
    }

    private void process_to_9B_2()
    {
        diagulewindow.setMainAvatar(new Image(Assets.Splashes.KARONG_0));
        diagulewindow.changeText(Messages.get(DeathRong.class, "message9b_2"));
    }

    private void process_to_9B_3()
    {
        diagulewindow.setMainAvatar(new Image(Assets.Splashes.KARONG_6));
        diagulewindow.changeText(Messages.get(DeathRong.class, "message9b_3"));
        for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
            if (mob instanceof ZeroBoat) {
                ((ZeroBoat) mob).rd = true;
            }
        }
        rong.first = false;
    }

}

