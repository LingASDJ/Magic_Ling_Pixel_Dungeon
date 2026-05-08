package com.shatteredpixel.shatteredpixeldungeon.custom.utils.plot;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Rat;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.hollow.MorphsNPC;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.hollow.Typhon;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.UnsignedInvitationLetter;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MissileSprite;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialog;
import com.watabou.noosa.Image;

public class TyphonPlot extends Plot {

    private final static int maxprocess = 16;

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
                case 9:
                    process_to_9();
                    break;
                case 10:
                    process_to_10();
                    break;
                case 11:
                    process_to_11();
                    break;
                case 12:
                    process_to_12();
                    break;
                case 13:
                    process_to_13();
                    break;
                case 14:
                    process_to_14();
                    break;
                case 15:
                    process_to_15();
                    break;
                case 16:
                    process_to_16();
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
        diagulewindow.setMainAvatar(new Image(Assets.Splashes.TYPHON));
        diagulewindow.setLeftName(Messages.get(Typhon.class, "name"));
        diagulewindow.changeText(Messages.get(TyphonPlot.class, "message1", hero.name()));
    }

    private void process_to_2() {
        diagulewindow.setLeftName(Messages.get(Typhon.class, "name"));
        diagulewindow.changeText(Messages.get(TyphonPlot.class, "message2"));
    }

    private void process_to_3() {
        diagulewindow.changeText(Messages.get(TyphonPlot.class, "message3"));
    }

    private void process_to_4() {
        diagulewindow.changeText(Messages.get(TyphonPlot.class, "message4"));

    }

    private void process_to_5() {
        diagulewindow.hideAll();
        diagulewindow.setLeftName(hero.name());
        diagulewindow.changeText(Messages.get(TyphonPlot.class, "message5", hero.name()));
    }

    private void process_to_6() {
        diagulewindow.setLeftName(Messages.get(Typhon.class, "name"));
        diagulewindow.setMainAvatar(new Image(Assets.Splashes.TYPHON));
        diagulewindow.changeText(Messages.get(TyphonPlot.class, "message6"));
    }

    private void process_to_7() {
        MorphsNPC typhonn = new MorphsNPC();
        typhonn.pos = 358;
        GameScene.add(typhonn);
        diagulewindow.setLeftName(Messages.get(MorphsNPC.class, "name"));
        diagulewindow.setMainAvatar(new Image(Assets.Splashes.MOSRDX_1));
        diagulewindow.changeText(Messages.get(TyphonPlot.class, "message7", hero.name()));
    }

    private void process_to_8() {
        diagulewindow.setLeftName(Messages.get(Typhon.class, "name"));
        diagulewindow.setMainAvatar(new Image(Assets.Splashes.TYPHON));
        diagulewindow.changeText(Messages.get(TyphonPlot.class, "message8", hero.name()));
    }

    private void process_to_9() {
        diagulewindow.setLeftName(Messages.get(MorphsNPC.class, "namex"));
        diagulewindow.setMainAvatar(new Image(Assets.Splashes.MOSRDX_2));
        diagulewindow.changeText(Messages.get(TyphonPlot.class, "message9", hero.name()));
    }

    private void process_to_10() {
        diagulewindow.setMainAvatar(new Image(Assets.Splashes.TYPHON));
        diagulewindow.setLeftName(Messages.get(Typhon.class, "name"));
        diagulewindow.changeText(Messages.get(TyphonPlot.class, "message10", hero.name()));
    }

    private void process_to_11() {
        diagulewindow.setLeftName(Messages.get(MorphsNPC.class, "namex"));
        diagulewindow.setMainAvatar(new Image(Assets.Splashes.MOSRDX_0));
        diagulewindow.changeText(Messages.get(TyphonPlot.class, "message11", hero.name()));
    }

    private void process_to_12() {
        diagulewindow.setLeftName(Messages.get(Typhon.class, "name"));
        diagulewindow.setMainAvatar(new Image(Assets.Splashes.TYPHON));
        diagulewindow.changeText(Messages.get(TyphonPlot.class, "message12", hero.name()));
    }

    private void process_to_13() {
        diagulewindow.setLeftName(Messages.get(MorphsNPC.class, "namex"));
        diagulewindow.setMainAvatar(new Image(Assets.Splashes.MOSRDX_0));
        diagulewindow.changeText(Messages.get(TyphonPlot.class, "message13", hero.name()));

       Rat rat = new Rat();
       rat.flying = true;
       rat.pos = 1;
       GameScene.add(rat);
        for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
            if (mob instanceof Rat) {
                MagicMissile.boltFromChar(mob.sprite.parent,
                        MagicMissile.HEART_ANMY,
                        new MissileSprite(),
                        356,
                        () -> {
                            Dungeon.level.drop(new UnsignedInvitationLetter(),356);
                            mob.die(null);
                        });
            }
        }


    }

    private void process_to_14() {
        hero.busy();
        for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
            if (mob instanceof MorphsNPC) {
                mob.die(true);
            }
        }
        diagulewindow.setMainAvatar(new Image(Assets.Splashes.MOSRDX_3));
        diagulewindow.changeText(Messages.get(TyphonPlot.class, "message14", hero.name()));
    }

    private void process_to_15() {
        diagulewindow.setLeftName(Messages.get(Typhon.class, "name"));
        diagulewindow.setMainAvatar(new Image(Assets.Splashes.TYPHON));
        diagulewindow.changeText(Messages.get(TyphonPlot.class, "message15", hero.name()));
    }

    private void process_to_16() {
        diagulewindow.changeText(Messages.get(TyphonPlot.class, "message16", hero.name()));
        for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
            if (mob instanceof Typhon) {
                mob.die(null);
            }
        }
        hero.spendAndNext(1);
        hero.sprite.operate(hero.pos);
    }

}






