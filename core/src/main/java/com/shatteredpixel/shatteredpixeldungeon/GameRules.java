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

import java.util.Map;
public class GameRules {

    /**
     * BossRush模式
     */
    public static void BossRush() {
        // 每日挑战禁用特殊模式（Boss Rush / 随机怪物 Rush）。
        if (Dungeon.daily || Dungeon.dailyReplay) {
            return;
        }
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
                            if (Badges.isUnlocked(Badges.Badge.BOSSRUSH)) {
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
        // 每日挑战禁用特殊模式（Boss Rush / 随机怪物 Rush）。
        if (Dungeon.daily || Dungeon.dailyReplay) {
            return;
        }
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
        Calendar calendar = Calendar.getInstance();
        Solar solar = Solar.fromDate(calendar.getTime());
        Lunar lunar = solar.getLunar();

        // 1. 自定义横幅
        if (SPDSettings.isCustomBanner()) {
            return getCustomBanner(SPDSettings.CustomBanner_Text());
        }

        // 2. 碎 PD 生日（原逻辑保留）
        if (Holiday.getCurrentHoliday() == Holiday.SHATTEREDPD_BIRTHDAY
                && RegularLevel.chinaHoliday == null) {
            return Assets.Interfaces.BANNERS_SP;
        }

        // 3. 原有中外节日
        String holidayBanner = getHolidayBanner();
        if (holidayBanner != null) {
            return holidayBanner;
        }

        // 4. 劳动节：公历 5 月 1 日（原特殊规则保留）
        if (solar.getMonth() == 5 && solar.getDay() == 1) {
            return Assets.Interfaces.BANNERS_LD;
        }

        // 5. 按二十四节气“四立”划分四季
        return getSeasonBannerByJieQi(lunar);
    }

    /**
     * 用四立节气判定四季横幅（按整天比较）
     */
    private static String getSeasonBannerByJieQi(Lunar lunar) {
        Map<String, Solar> table = lunar.getJieQiTable();
        String today = lunar.getSolar().toYmd();

        Solar liChun = pick(table, "立春", "LI_CHUN");
        Solar liXia  = table.get("立夏");
        Solar liQiu  = table.get("立秋");
        Solar liDong = table.get("立冬");

        if (inRange(today, liChun, liXia)) {   // 立春 ~ 立夏前
            return Assets.Interfaces.BANNERS_SR;
        }
        if (inRange(today, liXia, liQiu)) {    // 立夏 ~ 立秋前
            return Assets.Interfaces.BANNERS_SM;
        }
        if (inRange(today, liQiu, liDong)) {    // 立秋 ~ 立冬前
            return Assets.Interfaces.BANNERS_AT;
        }
        return Assets.Interfaces.BANNERS_WT;    // 立冬 ~ 次年立春前
    }

    /** 取节气，兼容中文名与英文别名两种 key */
    private static Solar pick(Map<String, Solar> table, String zhKey, String enKey) {
        Solar s = table.get(zhKey);
        return s != null ? s : table.get(enKey);
    }

    /** [start, end) 日期区间判断，边界为 null 时视为不限制 */
    private static boolean inRange(String today, Solar start, Solar end) {
        if (start != null && today.compareTo(start.toYmd()) < 0) {
            return false;
        }
        if (end != null && today.compareTo(end.toYmd()) >= 0) {
            return false;
        }
        return true;
    }

    /** 自定义横幅映射（原 switch 逻辑平移为 Map） */
    private static String getCustomBanner(String key) {
        Map<String, String> map = Map.ofEntries(
                Map.entry("复活", Assets.Interfaces.BANNERS_EA),
                Map.entry("RH", Assets.Interfaces.BANNERS_EA),
                Map.entry("圣诞", Assets.Interfaces.BANNERS_SD),
                Map.entry("CS", Assets.Interfaces.BANNERS_SD),
                Map.entry("端午", Assets.Interfaces.BANNERS_DW),
                Map.entry("DBF", Assets.Interfaces.BANNERS_DW),
                Map.entry("元宵", Assets.Interfaces.BANNERS_YX),
                Map.entry("LR", Assets.Interfaces.BANNERS_YX),
                Map.entry("七夕", Assets.Interfaces.BANNERS_QX),
                Map.entry("QX", Assets.Interfaces.BANNERS_QX),
                Map.entry("生日", Assets.Interfaces.BANNERS_BD),
                Map.entry("BR", Assets.Interfaces.BANNERS_BD),
                Map.entry("清明", Assets.Interfaces.BANNERS_QM),
                Map.entry("QF", Assets.Interfaces.BANNERS_QM),
                Map.entry("万圣", Assets.Interfaces.BANNERS_HL),
                Map.entry("HW", Assets.Interfaces.BANNERS_HL),
                Map.entry("春节", Assets.Interfaces.BANNERS_CJ),
                Map.entry("SF", Assets.Interfaces.BANNERS_CJ),
                Map.entry("夏季", Assets.Interfaces.BANNERS_SM),
                Map.entry("SS", Assets.Interfaces.BANNERS_SM),
                Map.entry("冬季", Assets.Interfaces.BANNERS_WT),
                Map.entry("WR", Assets.Interfaces.BANNERS_WT),
                Map.entry("秋季", Assets.Interfaces.BANNERS_AT),
                Map.entry("AT", Assets.Interfaces.BANNERS_AT),
                Map.entry("春季", Assets.Interfaces.BANNERS_SR),
                Map.entry("SR", Assets.Interfaces.BANNERS_SR),
                Map.entry("劳动", Assets.Interfaces.BANNERS_LD),
                Map.entry("LD", Assets.Interfaces.BANNERS_LD),
                Map.entry("中元", Assets.Interfaces.BANNERS_ZY),
                Map.entry("ZY", Assets.Interfaces.BANNERS_ZY),
                Map.entry("国庆", Assets.Interfaces.BANNERS_GQ),
                Map.entry("GQ", Assets.Interfaces.BANNERS_GQ),
                Map.entry("重阳", Assets.Interfaces.BANNERS_CY),
                Map.entry("CY", Assets.Interfaces.BANNERS_CY),
                Map.entry("中秋", Assets.Interfaces.BANNERS_ZQ),
                Map.entry("ZQ", Assets.Interfaces.BANNERS_ZQ),
                Map.entry("碎生", Assets.Interfaces.BANNERS_BX),
                Map.entry("BX", Assets.Interfaces.BANNERS_BX)
        );
        return map.getOrDefault(key, Assets.Interfaces.BANNERS);
    }

    /** 原有中外节日横幅，无匹配返回 null */
    private static String getHolidayBanner() {
        if (RegularLevel.chinaHoliday == RegularLevel.ChinaHoliday.YX) {
            return SPDSettings.ClassUI()
                    ? Assets.Interfaces.BANNERS_BD
                    : Assets.Interfaces.BANNERS_YX;
        }
        if (RegularLevel.chinaHoliday == RegularLevel.ChinaHoliday.CJ) {
            return Assets.Interfaces.BANNERS_CJ;
        }
        if (RegularLevel.holiday == RegularLevel.WestHoliday.EASTER) {
            return Assets.Interfaces.BANNERS_EA;
        }
        if (RegularLevel.chinaHoliday == RegularLevel.ChinaHoliday.QMJ) {
            return Assets.Interfaces.BANNERS_QM;
        }
        if (RegularLevel.chinaHoliday == RegularLevel.ChinaHoliday.DWJ) {
            return Assets.Interfaces.BANNERS_DW;
        }
        if (RegularLevel.chinaHoliday == RegularLevel.ChinaHoliday.ZYJ) {
            return Assets.Interfaces.BANNERS_ZY;
        }
        if (RegularLevel.chinaHoliday == RegularLevel.ChinaHoliday.QXJ) {
            return Assets.Interfaces.BANNERS_QX;
        }
        if (RegularLevel.chinaHoliday == RegularLevel.ChinaHoliday.GQJ) {
            return Assets.Interfaces.BANNERS_GQ;
        }
        if (RegularLevel.chinaHoliday == RegularLevel.ChinaHoliday.ZQJ) {
            return Assets.Interfaces.BANNERS_ZQ;
        }
        if (RegularLevel.chinaHoliday == RegularLevel.ChinaHoliday.CYJ) {
            return Assets.Interfaces.BANNERS_CY;
        }
        if (RegularLevel.holiday == RegularLevel.WestHoliday.HWEEN) {
            return Assets.Interfaces.BANNERS_HL;
        }
        if (RegularLevel.holiday == RegularLevel.WestHoliday.XMAS) {
            return Assets.Interfaces.BANNERS_SD;
        }
        return null;
    }

    public static void PropsScore() {
        ArrayList<Prop> AllProps = hero.belongings.getAllItems(Prop.class);
        for (Prop w : AllProps.toArray(new Prop[0])){
            if(w.kind == 0){
                Statistics.goodMultiplier += 0.2f;
            } else if(w.kind == 2) {
                Statistics.badMultiplier += 0.25f;
            } else {
                Statistics.badMultiplier += 0.5f;
            }
        }
    }
}
