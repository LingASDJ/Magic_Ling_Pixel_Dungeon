package com.shatteredpixel.shatteredpixeldungeon;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.depth;
import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.nlf.calendar.Lunar;
import com.nlf.calendar.Solar;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ChampionHero;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ClearBleesdGoodBuff.BlessBossRushLow;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.status.QuestGold;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.WaloKe;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TimekeepersHourglass;
import com.shatteredpixel.shatteredpixeldungeon.items.props.Prop;
import com.shatteredpixel.shatteredpixeldungeon.levels.RegularLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Swiftthistle;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.InterlevelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ShopkKingSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.Holiday;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndError;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.watabou.noosa.Game;
import com.watabou.utils.Callback;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class GameRules {

    /**
     * BossRush模式
     */
    public static void BossRush() {
        Game.runOnRenderThread(new Callback() {
            @Override
            public void call() {
                GameScene.show(new WndOptions(new ShopkKingSprite(),
                        Messages.titleCase(Messages.get(WaloKe.class, "name")),
                        Messages.get(WaloKe.class, "quest_start_prompt"),
                        Messages.get(WaloKe.class, "easy"),
                        Messages.get(WaloKe.class, "normal"),
                        Messages.get(WaloKe.class, "hard"),
                        Messages.get(WaloKe.class, "hell")) {
                    @Override
                    protected void onSelect(int index) {
                        if (index == 0) {
                            TimekeepersHourglass.timeFreeze timeFreeze = hero.buff(TimekeepersHourglass.timeFreeze.class);
                            if (timeFreeze != null) timeFreeze.disarmPresses();
                            Swiftthistle.TimeBubble timeBubble = hero.buff(Swiftthistle.TimeBubble.class);
                            if (timeBubble != null) timeBubble.disarmPresses();
                            InterlevelScene.mode = InterlevelScene.Mode.DESCEND;
                            InterlevelScene.curTransition = new LevelTransition();
                            InterlevelScene.curTransition.destDepth = depth+1;
                            InterlevelScene.curTransition.destType = LevelTransition.Type.REGULAR_EXIT;
                            InterlevelScene.curTransition.destBranch = 0;
                            InterlevelScene.curTransition.type = LevelTransition.Type.REGULAR_EXIT;
                            InterlevelScene.curTransition.centerCell = -1;
                            Game.switchScene(InterlevelScene.class);
                            Buff.affect(hero, BlessBossRushLow.class, ChampionHero.DURATION*123456f);
                            Statistics.difficultyDLCEXLevel = 1;
                            Statistics.bossRushMode = true;
                            Dungeon.gold = 0;
                            Dungeon.rushgold = 16;
                        } else if (index == 1) {
                            TimekeepersHourglass.timeFreeze timeFreeze = hero.buff(TimekeepersHourglass.timeFreeze.class);
                            if (timeFreeze != null) timeFreeze.disarmPresses();
                            Swiftthistle.TimeBubble timeBubble = hero.buff(Swiftthistle.TimeBubble.class);
                            if (timeBubble != null) timeBubble.disarmPresses();
                            InterlevelScene.mode = InterlevelScene.Mode.DESCEND;
                            InterlevelScene.curTransition = new LevelTransition();
                            InterlevelScene.curTransition.destDepth = depth + 1;
                            InterlevelScene.curTransition.destType = LevelTransition.Type.REGULAR_EXIT;
                            InterlevelScene.curTransition.destBranch = 0;
                            InterlevelScene.curTransition.type = LevelTransition.Type.REGULAR_EXIT;
                            InterlevelScene.curTransition.centerCell = -1;
                            Game.switchScene(InterlevelScene.class);
                            Statistics.difficultyDLCEXLevel = 2;
                            Statistics.bossRushMode = true;
                            Dungeon.gold = 0;
                            Dungeon.rushgold = 16;
                        } else if (index == 2) {
                            TimekeepersHourglass.timeFreeze timeFreeze = hero.buff(TimekeepersHourglass.timeFreeze.class);
                            if (timeFreeze != null) timeFreeze.disarmPresses();
                            Swiftthistle.TimeBubble timeBubble = hero.buff(Swiftthistle.TimeBubble.class);
                            if (timeBubble != null) timeBubble.disarmPresses();
                            InterlevelScene.mode = InterlevelScene.Mode.DESCEND;
                            InterlevelScene.curTransition = new LevelTransition();
                            InterlevelScene.curTransition.destDepth = depth + 1;
                            InterlevelScene.curTransition.destType = LevelTransition.Type.REGULAR_EXIT;
                            InterlevelScene.curTransition.destBranch = 0;
                            InterlevelScene.curTransition.type = LevelTransition.Type.REGULAR_EXIT;
                            InterlevelScene.curTransition.centerCell = -1;
                            Game.switchScene(InterlevelScene.class);
                            Statistics.difficultyDLCEXLevel = 3;
                            Statistics.bossRushMode = true;
                            Dungeon.gold = 0;
                            Dungeon.rushgold = 16;
                        } else if (index == 3) {
                            PaswordBadges.loadGlobal();
                            List<PaswordBadges.Badge> passwordbadges = PaswordBadges.filtered(true);
                            if (passwordbadges.contains(PaswordBadges.Badge.BRCLER)) {
                                TimekeepersHourglass.timeFreeze timeFreeze = hero.buff(TimekeepersHourglass.timeFreeze.class);
                                if (timeFreeze != null) timeFreeze.disarmPresses();
                                Swiftthistle.TimeBubble timeBubble = hero.buff(Swiftthistle.TimeBubble.class);
                                if (timeBubble != null) timeBubble.disarmPresses();
                                InterlevelScene.mode = InterlevelScene.Mode.DESCEND;
                                InterlevelScene.curTransition = new LevelTransition();
                                InterlevelScene.curTransition.destDepth = depth + 1;
                                InterlevelScene.curTransition.destType = LevelTransition.Type.REGULAR_EXIT;
                                InterlevelScene.curTransition.destBranch = 0;
                                InterlevelScene.curTransition.type = LevelTransition.Type.REGULAR_EXIT;
                                InterlevelScene.curTransition.centerCell = -1;
                                Game.switchScene(InterlevelScene.class);
                                Statistics.difficultyDLCEXLevel = 4;
                                Statistics.bossRushMode = true;
                                Dungeon.gold = 0;
                                Dungeon.rushgold = 16;
                            } else {
                                Game.scene().add( new WndError( Messages.get(WaloKe.class, "br_no_clear") ) );
                            }
                        }
                    }
                });
            }

        });
    }

    /**
     * RandMode模式
     */
    public static void RandMode() {
        Game.runOnRenderThread(new Callback() {
            @Override
            public void call() {
                GameScene.show(new WndOptions(new ShopkKingSprite(),
                        Messages.titleCase(Messages.get(WaloKe.class, "name")),
                        Messages.get(WaloKe.class, "quest_start2_prompt"),
                        Messages.get(WaloKe.class, "randmode")) {
                    @Override
                    protected void onSelect(int index) {
                        if (index == 0) {
                            TimekeepersHourglass.timeFreeze timeFreeze = Dungeon.hero.buff(TimekeepersHourglass.timeFreeze.class);
                            if (timeFreeze != null) timeFreeze.disarmPresses();
                            Swiftthistle.TimeBubble timeBubble = Dungeon.hero.buff(Swiftthistle.TimeBubble.class);
                            if (timeBubble != null) timeBubble.disarmPresses();
                            InterlevelScene.mode = InterlevelScene.Mode.DESCEND;
                            InterlevelScene.curTransition = new LevelTransition();
                            InterlevelScene.curTransition.destDepth = depth+1;
                            InterlevelScene.curTransition.destType = LevelTransition.Type.REGULAR_ENTRANCE;
                            InterlevelScene.curTransition.destBranch = 0;
                            InterlevelScene.curTransition.type = LevelTransition.Type.REGULAR_ENTRANCE;
                            InterlevelScene.curTransition.centerCell = -1;
                            Game.switchScene(InterlevelScene.class);
                            Statistics.RandMode = true;
                            Buff.affect(hero, QuestGold.class).set((100), 1);
                        }
                    }
                });
            }

        });
    }

    public static String BannersRules() {
        final Calendar calendar = Calendar.getInstance();
        String banners;
        int month = calendar.get(Calendar.MONTH);
        Solar date = Solar.fromDate(calendar.getTime());
        Lunar lunar = date.getLunar();

        if(SPDSettings.isCustomBanner()){
            switch (SPDSettings.CustomBanner_Text()) {
                case "复活": case "RH":
                    banners = Assets.Interfaces.BANNERS_EA;
                    break;
                case "圣诞": case "CS":
                    banners = Assets.Interfaces.BANNERS_SD;
                    break;
                case "端午": case "DBF":
                    banners = Assets.Interfaces.BANNERS_DW;
                    break;
                case "元宵": case "LR":
                    banners = Assets.Interfaces.BANNERS_YX;
                    break;
                case "七夕": case "QX":
                    banners = Assets.Interfaces.BANNERS_QX;
                    break;
                case "生日": case "BR":
                    banners = Assets.Interfaces.BANNERS_BD;
                    break;
                case "清明": case "QF":
                    banners = Assets.Interfaces.BANNERS_QM;
                    break;
                case "万圣": case "HW":
                    banners = Assets.Interfaces.BANNERS_HL;
                    break;
                case "春节": case "SF":
                    banners = Assets.Interfaces.BANNERS_CJ;
                    break;
                case "夏季": case "SS":
                    banners = Assets.Interfaces.BANNERS_SM;
                    break;
                case "冬季": case "WR":
                    banners = Assets.Interfaces.BANNERS_WT;
                    break;
                case "秋季": case "AT":
                    banners = Assets.Interfaces.BANNERS_AT;
                    break;
                case "春季": case "SR":
                    banners = Assets.Interfaces.BANNERS_SR;
                    break;
                case "劳动": case "LD":
                    banners = Assets.Interfaces.BANNERS_LD;
                    break;
                case "中元": case "ZY":
                    banners = Assets.Interfaces.BANNERS_ZY;
                    break;
                case "国庆": case "GQ":
                    banners = Assets.Interfaces.BANNERS_GQ;
                    break;
                case "重阳": case "CY":
                    banners = Assets.Interfaces.BANNERS_CY;
                    break;
                case "中秋": case "ZQ":
                    banners = Assets.Interfaces.BANNERS_ZQ;
                    break;
               case "碎生": case "BX":
                    banners = Assets.Interfaces.BANNERS_BX;
                    break;
                default:
                    banners = Assets.Interfaces.BANNERS;
                    break;
            }
        } else {
            if(Holiday.getCurrentHoliday()  == Holiday.SHATTEREDPD_BIRTHDAY){
                banners = Assets.Interfaces.BANNERS_SP;
            } else if (RegularLevel.chinaHoliday == RegularLevel.ChinaHoliday.YX) {
                if (!SPDSettings.ClassUI()) {
                    banners = Assets.Interfaces.BANNERS_YX;
                } else {
                    banners = Assets.Interfaces.BANNERS_BD;
                }
            } else if (RegularLevel.chinaHoliday == RegularLevel.ChinaHoliday.CJ) {
                banners = Assets.Interfaces.BANNERS_CJ;
            } else if (RegularLevel.chinaHoliday == RegularLevel.ChinaHoliday.QMJ) {
                banners = Assets.Interfaces.BANNERS_QM;
            } else if (RegularLevel.chinaHoliday == RegularLevel.ChinaHoliday.DWJ) {
                banners = Assets.Interfaces.BANNERS_DW;
            } else if (RegularLevel.chinaHoliday == RegularLevel.ChinaHoliday.ZYJ) {
                banners = Assets.Interfaces.BANNERS_ZY;
            } else if (RegularLevel.chinaHoliday == RegularLevel.ChinaHoliday.QXJ) {
                banners = Assets.Interfaces.BANNERS_QX;
            } else if (RegularLevel.chinaHoliday == RegularLevel.ChinaHoliday.GQJ) {
                banners = Assets.Interfaces.BANNERS_GQ;
            } else if (RegularLevel.chinaHoliday == RegularLevel.ChinaHoliday.ZQJ) {
                banners = Assets.Interfaces.BANNERS_ZQ;
            } else if (RegularLevel.chinaHoliday == RegularLevel.ChinaHoliday.CYJ) {
                banners = Assets.Interfaces.BANNERS_CY;
            } else if (RegularLevel.holiday == RegularLevel.WestHoliday.XMAS) {
                banners = Assets.Interfaces.BANNERS_SD;
            } else  if (4 == month) {
                //四季主题
               banners = Assets.Interfaces.BANNERS_LD;
            } else if (month == 2 || month == 3 ) { // 春季：3, 4, 5月
                banners = Assets.Interfaces.BANNERS_SR;
            } else if (month == 5 || month == 6 || month == 7) { // 夏季：6, 7, 8月
                banners = Assets.Interfaces.BANNERS_SM;
            } else if (month == 8 || month == 9 || month == 10) { // 秋季：9, 10, 11月
                banners = Assets.Interfaces.BANNERS_AT;
            } else { // 冬季：12, 1, 2月
                banners = Assets.Interfaces.BANNERS_WT;
            }
        }

        return banners;
    }

    public static void PropsScore() {
        ArrayList<Prop> AllProps = hero.belongings.getAllItems(Prop.class);
        for (Prop w : AllProps.toArray(new Prop[0])){
            if(w.kind == 0){
                Statistics.goodMultiplier += 0.2f;
            } else {
                Statistics.badMultiplier += 0.4f;
            }
        }
    }
}
