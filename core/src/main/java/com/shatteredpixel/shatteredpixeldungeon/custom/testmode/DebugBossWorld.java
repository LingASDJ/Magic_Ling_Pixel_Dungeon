package com.shatteredpixel.shatteredpixeldungeon.custom.testmode;

import com.shatteredpixel.shatteredpixeldungeon.Chrome;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.DM300;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.DwarfKing;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Goo;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.GreenStingCV;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.MagicGirlDead;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.NewDM720;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.SlimeKing;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Tengu;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.YogDzewa;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.YogReal;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.DiamondKnight;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.DwarfGeneral;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.DwarfMaster;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.FireDragon;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.FireMagicDied;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.Qliphoth;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.SakaFishBoss;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.bossrush.SkyGoo;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.hollow.DeadDogCerberus;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.hollow.Morphs;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.notsync.CrivusStarFruits;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.dragon.PirahaKing;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.lb.BlackSoul;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.lb.RivalSprite;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.spical.SkyDead;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TimekeepersHourglass;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Swiftthistle;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.InterlevelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CrivusStarFruitsSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DM300Sprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DM720Sprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DeadDogCerberusSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DiedMonkSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DimandKingSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DwarfGeneralSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.FireDragonSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.FireMagicGirlSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.FistSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GooSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GreenSltingSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.KingSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MagicGirlSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MorpheusSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.PirahaKingSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.QliphothSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SakaFishBossSprites;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SkyDeadSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SlimeKingMobSprites;
import com.shatteredpixel.shatteredpixeldungeon.sprites.TenguSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.IconButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.StyledButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.utils.Bundle;

import java.util.ArrayList;

public class DebugBossWorld extends TestItem {
    {
        image = ItemSpriteSheet.STORYBOOKS;
        defaultAction = AC_SET;
    }

    public int chosen;

    private static final String AC_PLACE = "place";
    private static final String AC_SET = "set";

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.add(AC_PLACE);
        actions.add(AC_SET);
        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {
        super.execute(hero, action);
        if (action.equals(AC_PLACE)) {
            if(Dungeon.branch != 0){
                TimekeepersHourglass.timeFreeze timeFreeze = Dungeon.hero.buff(TimekeepersHourglass.timeFreeze.class);
                if (timeFreeze != null) timeFreeze.disarmPresses();
                Swiftthistle.TimeBubble timeBubble = Dungeon.hero.buff(Swiftthistle.TimeBubble.class);
                if (timeBubble != null) timeBubble.disarmPresses();
                InterlevelScene.mode = InterlevelScene.Mode.ASCEND;
                InterlevelScene.curTransition = new LevelTransition();
                InterlevelScene.curTransition.destDepth = chosen;
                InterlevelScene.curTransition.destType = LevelTransition.Type.BRANCH_EXIT;
                InterlevelScene.curTransition.destBranch = 0;
                InterlevelScene.curTransition.type = LevelTransition.Type.BRANCH_EXIT;
                InterlevelScene.curTransition.centerCell  = -1;
                Game.switchScene( InterlevelScene.class );
            } else {
                GLog.n(Messages.get(this, "wrong_branch"));
            }
        }

        if (action.equals(AC_SET)) {
            GameScene.show(new SettingsWindow());
        }
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put("chosen", chosen);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        chosen = bundle.getInt("chosen");
    }

    public StyledButton selectDepthButton;

    private class SettingsWindow extends Window {

        public SettingsWindow() {
            DebugBossButton DebugBossButton;
            int x;
            int y;

            for (int i = 0;i < 24;i++) {
                DebugBossButton = new DebugBossButton(i);
                x = i % 5 * 22 + 2;
                y = i / 5 * 22 + 2;

                if(i == 22) {
                    DebugBossButton.setRect(x, y+3,   20, 20);
                } else if(i == 14) {
                    DebugBossButton.setRect(x+2, y,   20, 20);
                } else if(i == 13) {
                    DebugBossButton.setRect(x, y - 5, 20, 20);
                } else if(i == 10) {
                    DebugBossButton.setRect(x + 2, y - 5, 20, 20);
                } else {
                    DebugBossButton.setRect(x,y,20,20);
                }

                add(DebugBossButton);
            }

            selectDepthButton = new DepthGoBossButton(Chrome.Type.GREY_BUTTON_TR, Messages.get(this, "depth",depthname()),6){
                @Override
                public void update() {
                    super.update();
                    text(Messages.get(SettingsWindow.class, "depth",depthname()));
                }
            };
            selectDepthButton.icon(Icons.get(Icons.ENTER));
            add(selectDepthButton);
            selectDepthButton.setRect(0, 112, 110, 16);

            resize(110,114 + (int)selectDepthButton.height());
        }
    }

    private String depthname() {
        switch (chosen){
            case 1:
                return Messages.get(Qliphoth.class,"name");
            case 2:
                return Messages.get(CrivusStarFruits.class,"name");
            case 3:
                return Messages.get(FireDragon.class,"name");
            case 4:
                return Messages.get(Tengu.class,"name");
            case 5:
                return Messages.get(BlackSoul.class,"name");
            case 6:
                return Messages.get(DiamondKnight.class,"name");
            case 7:
                return Messages.get(DM300.class,"name");
            case 8:
                return Messages.get(NewDM720.class,"name");
            case 9:
                return Messages.get(MagicGirlDead.class,"name");
            case 10:
                return Messages.get(DwarfGeneral.class,"name");
            case 11:
                return Messages.get(DwarfMaster.class,"name");
            case 12:
                return Messages.get(DwarfKing.class,"name");
            case 13:
                return Messages.get(PirahaKing.class,"name");
            case 14:
                return Messages.get(SakaFishBoss.class,"name");
            case 15:
                return Messages.get(YogDzewa.class,"name");
            case 16:
                return Messages.get(YogReal.class,"name");
            case 17:
                return Messages.get(FireMagicDied.class,"name");
            case 18:
                return Messages.get(DeadDogCerberus.class,"name");
            case 19:
                return Messages.get(Morphs.class,"name");
            case 20:
                return Messages.get(SkyGoo.class,"name");
            case 21:
                return Messages.get(GreenStingCV.class,"name");
            case 22:
                return Messages.get(SkyDead.class,"name");
            case 23:
                return Messages.get(SlimeKing.class,"name");
            default:
                return Messages.get(Goo.class,"name");
        }
    }

    private class DepthGoBossButton extends StyledButton {

        public DepthGoBossButton(Chrome.Type type, String label, int size){
            super(type, label, size);
        }

        @Override
        protected void onClick() {
            TimekeepersHourglass.timeFreeze timeFreeze = Dungeon.hero.buff(TimekeepersHourglass.timeFreeze.class);
            if (timeFreeze != null) timeFreeze.disarmPresses();
            Swiftthistle.TimeBubble timeBubble = Dungeon.hero.buff(Swiftthistle.TimeBubble.class);
            if (timeBubble != null) timeBubble.disarmPresses();
            InterlevelScene.mode = InterlevelScene.Mode.DESCEND;
            InterlevelScene.curTransition = new LevelTransition();
            InterlevelScene.curTransition.destDepth = chosen;
            InterlevelScene.curTransition.destType = LevelTransition.Type.BRANCH_ENTRANCE;
            InterlevelScene.curTransition.destBranch = 12;
            InterlevelScene.curTransition.type = LevelTransition.Type.BRANCH_ENTRANCE;
            InterlevelScene.curTransition.centerCell  = -1;
            Game.switchScene( InterlevelScene.class );
        }
    }

    private class DebugBossButton extends IconButton {

        public int DebugBoss = -1;

        public DebugBossButton(int DebugBoss) {
            this.DebugBoss = DebugBoss;
            switch (DebugBoss) {
                case 0: case 20:
                    icon(new Image(new GooSprite()));
                    break;
                case 1:
                    icon(new Image(new QliphothSprite()));
                    icon.scale.set(0.75f);
                    break;
                case 2:
                    icon(new Image(new CrivusStarFruitsSprite()));
                    icon.scale.set(0.75f);
                    break;
                case 3:
                    icon(new Image(new FireDragonSprite()));
                    icon.scale.set(0.75f);
                    break;
                case 4:
                    icon(new TenguSprite());
                    break;
                case 5:
                    icon(new Image(new RivalSprite()));
                    break;
                case 6:
                    icon(new Image(new DimandKingSprite()));
                    icon.scale.set(0.75f);
                    break;
                case 7:
                    icon(new Image(new DM300Sprite()));
                    icon.scale.set(0.75f);
                    break;
                case 8:
                    icon(new Image(new DM720Sprite()));
                    icon.scale.set(0.75f);
                    break;
                case 9:
                    icon(new Image(new MagicGirlSprite()));
                    break;
                case 10:
                    icon(new Image(new DwarfGeneralSprite()));
                    icon.scale.set(0.75f);
                    break;
                case 11:
                    icon(new Image(new DiedMonkSprite()));
                    break;
                case 12:
                    icon(new Image(new KingSprite()));
                    break;
                case 13:
                    icon(new Image(new PirahaKingSprite()));
                    icon.scale.set(0.75f);
                    break;
                case 14:
                    icon(new Image(new SakaFishBossSprites()));
                    icon.scale.set(0.65f);
                    break;
                case 15:
                    icon(new FistSprite.Burning());
                    icon.scale.set(0.75f);
                    break;
                case 16:
                    icon(new FistSprite.HaloFist());
                    icon.scale.set(0.75f);
                    break;
                case 17:
                    icon(new Image(new FireMagicGirlSprite()));
                    icon.scale.set(0.75f);
                    break;
                case 18:
                    icon(new Image(new DeadDogCerberusSprite()));
                    icon.scale.set(0.65f);
                    break;
                case 19:
                    icon(new Image(new MorpheusSprite()));
                    icon.scale.set(0.65f);
                    break;
                case 21:
                    icon(new Image(new GreenSltingSprite()));
                    break;
                case 22:
                    icon(new Image(new SkyDeadSprite()));
                    icon.scale.set(0.85f);
                    break;
                case 23:
                    icon(new Image(new SlimeKingMobSprites()));
                    break;
            }
        }

        @Override
        public void onClick() {
            chosen = DebugBoss;
        }
    }
}