package com.shatteredpixel.shatteredpixeldungeon.windows;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Chrome;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.GamesInProgress;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.PinkLingSprite;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.multiplayer.Mp;
import com.shatteredpixel.shatteredpixeldungeon.multiplayer.WndHeroClassSelect;
import com.shatteredpixel.shatteredpixeldungeon.scenes.DailyRankingsScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.HeroSelectScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.InterlevelScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.services.daily.DailyConfig;
import com.shatteredpixel.shatteredpixeldungeon.services.daily.DailySeedData;
import com.shatteredpixel.shatteredpixeldungeon.services.daily.DailyUtils;
import com.shatteredpixel.shatteredpixeldungeon.ui.ActionIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.Button;
import com.shatteredpixel.shatteredpixeldungeon.ui.IconButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.StyledButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.NinePatch;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.ui.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 每日狩猎配置预览：图形化展示难度、挑战图标与开始/重玩状态。
 */
public class WndDailyConfig extends Window {

    private static final int WIDTH_MAX = 150;
    private static final int MARGIN = 4;
    private static final int TILE_SIZE = 40;
    private static final int TILE_GAP = 3;
    private static final int COLS = 3;
    private static final int PER_PAGE = COLS * 2;

    private static final long HOUR = 3600_000L;
    private static final long DAY = 24 * HOUR;

    private static final int COLOR_NORMAL = 0x2E8B57;
    private static final int COLOR_HARD = 0xC0392B;

    private final DailySeedData data;
    private final long diff;
    private final int page;
    private final int pageCount;
    private final boolean replay;

    public WndDailyConfig(DailySeedData data, long diff) {
        this(data, diff, 0);
    }

    private WndDailyConfig(DailySeedData data, long diff, int page) {
        super();

        this.data = data;
        this.diff = diff;
        this.page = page;

        long serverDay = DailyUtils.parseUtcDay(data.date);
        this.replay = serverDay >= 0
                ? SPDSettings.lastDaily() >= serverDay
                : diff > 0;

        List<Integer> challenges = challengeIndices(data.challenges);
        this.pageCount = Math.max(1, (challenges.size() + PER_PAGE - 1) / PER_PAGE);

        int width = Math.min(WIDTH_MAX, (int) PixelScene.uiCamera.width - 12);
        float innerW = width - MARGIN * 2f;
        float pos = MARGIN;

        // ===== 顶部：角色图标 + 标题 + 日期 =====
        Image icon = new PinkLingSprite();
        icon.scale.set(1.5f);
        icon.x = MARGIN;
        icon.y = pos;
        PixelScene.align(icon);
        add(icon);

        RenderedTextBlock title = PixelScene.renderTextBlock(
                Messages.get(HeroSelectScene.class, "daily"), 12);
        title.hardlight(TITLE_COLOR);
        title.maxWidth((int) Math.max(40, innerW - icon.width() - 6));
        title.setPos(icon.x + icon.width() + 6, pos);
        PixelScene.align(title);
        add(title);

        String dateText = data.date == null ? "" : data.date;
        RenderedTextBlock date = PixelScene.renderTextBlock(dateText, 7);
        date.hardlight(0x9D9D9D);
        date.setPos(icon.x + icon.width() + 6, title.bottom() + 2);
        PixelScene.align(date);
        add(date);

        pos = Math.max(icon.y + icon.height(), date.bottom()) + 4;

        // 右上角信息按钮：查看完整的每日狩猎规则说明
        IconButton info = new IconButton(Icons.get(Icons.INFO)) {
            @Override
            protected void onClick() {
                super.onClick();
                ShatteredPixelDungeon.scene().addToFront(new WndMessage(
                        Messages.get(HeroSelectScene.class, replay ? "daily_repeat" : "daily_desc")));
            }
        };
        info.setSize(16, 16);
        info.setPos(width - MARGIN - 16, MARGIN);
        PixelScene.align(info);
        add(info);

        // 分隔线
        add(separator(MARGIN, pos, innerW));
        pos += 4;

        // ===== 难度 / 挑战数徽章（整组居中） =====
        boolean hard = "HARD".equalsIgnoreCase(data.difficulty);
        Badge diffBadge = new Badge(
                Messages.get(this, "difficulty") + " · " + DailyUtils.difficultyName(data.difficulty),
                hard ? COLOR_HARD : COLOR_NORMAL);
        add(diffBadge);

        float badgeX = MARGIN + (innerW - diffBadge.width()) / 2f;
        diffBadge.setPos(badgeX, pos);

        pos += 14;

        // ===== 得分状态提示 =====
        RenderedTextBlock hint = PixelScene.renderTextBlock(
                Messages.get(this, replay ? "score_replay" : "score_first"), 6);
        hint.hardlight(replay ? 0xE0A040 : 0x7FD07F);
        hint.maxWidth((int) innerW);
        hint.setPos(MARGIN, pos);
        PixelScene.align(hint);
        add(hint);
        pos += hint.height() + 3;

        // ===== 挑战图标网格（分页，每页 3x2） =====
        int start = page * PER_PAGE;
        int end = Math.min(challenges.size(), start + PER_PAGE);
        int visible = Math.max(0, end - start);

        if (visible == 0) {
            RenderedTextBlock none = PixelScene.renderTextBlock(Messages.get(this, "no_challenges"), 7);
            none.hardlight(0x9D9D9D);
            none.maxWidth((int) innerW);
            none.setPos(MARGIN + (innerW - none.width()) / 2f, pos + 6);
            PixelScene.align(none);
            add(none);
            pos += 20;
        } else {
            int rows = (visible + COLS - 1) / COLS;
            float tileW = (innerW - (COLS - 1) * TILE_GAP) / COLS;

            for (int i = 0; i < visible; i++) {
                ChallengeTile tile = new ChallengeTile(challenges.get(start + i));
                tile.setRect(
                        MARGIN + (i % COLS) * (tileW + TILE_GAP),
                        pos + (i / COLS) * (TILE_SIZE + TILE_GAP),
                        tileW, TILE_SIZE);
                add(tile);
            }

            pos += rows * (TILE_SIZE + TILE_GAP) - TILE_GAP;
        }

        // ===== 分页控件（挑战超过一页时，居中放在网格下方） =====
        if (pageCount > 1) {
            pos += 3;
            float navW = 14 + 4 + 24 + 4 + 14;
            float navX = MARGIN + (innerW - navW) / 2f;

            IconButton prev = new IconButton(Icons.get(Icons.LEFTBUTTON)) {
                @Override
                protected void onClick() {
                    super.onClick();
                    openPage(page - 1);
                }
            };
            prev.setSize(14, 14);
            prev.setPos(navX, pos);
            PixelScene.align(prev);
            add(prev);

            RenderedTextBlock pageText = PixelScene.renderTextBlock(
                    Messages.get(this, "page", page + 1, pageCount), 6);
            pageText.hardlight(0xBBBBBB);
            pageText.setPos(navX + 18 + (24 - pageText.width()) / 2f, pos + 4);
            PixelScene.align(pageText);
            add(pageText);

            IconButton next = new IconButton(Icons.get(Icons.RIGHTBUTTON)) {
                @Override
                protected void onClick() {
                    super.onClick();
                    openPage(page + 1);
                }
            };
            next.setSize(14, 14);
            next.setPos(navX + navW - 14, pos);
            PixelScene.align(next);
            add(next);

            pos += 17;
        }

        pos += 4;

        // ===== 主按钮：开始 / 重新挑战 =====
        RedButton startBtn = new RedButton(
                Messages.get(this, replay ? "start_replay" : "start"), 9) {
            @Override
            protected void onClick() {
                super.onClick();
                startDaily();
            }
        };
        startBtn.icon(Icons.get(Icons.ENTER));
        startBtn.setRect(MARGIN, pos, innerW, 16);
        add(startBtn);

        pos += 18;

        // ===== 次按钮：排行榜 / 设置名字 / 取消 =====
        float btnW = (innerW - 2 * 2) / 3f;

        StyledButton leaderboard = new StyledButton(Chrome.Type.GREY_BUTTON_TR,
                Messages.get(HeroSelectScene.class, "daily_leaderboard"), 6) {
            @Override
            protected void onClick() {
                super.onClick();
                DailyRankingsScene.date = data.date == null ? DailyUtils.todayUtc() : data.date;
                ShatteredPixelDungeon.switchNoFade(DailyRankingsScene.class);
            }
        };
        leaderboard.textColor(0xDDDDDD);
        leaderboard.setRect(MARGIN, pos, btnW, 16);
        add(leaderboard);

        StyledButton setName = new StyledButton(Chrome.Type.GREY_BUTTON_TR,
                Messages.get(Mp.class, "set_name"), 6) {
            @Override
            protected void onClick() {
                super.onClick();
                ShatteredPixelDungeon.scene().addToFront(new WndTextInput(
                        Messages.get(Mp.class, "set_name"),
                        Messages.get(Mp.class, "name_prompt"),
                        Mp.get().getMyName(), 16, false,
                        Messages.get(Mp.class, "ok"),
                        Messages.get(Mp.class, "cancel")) {
                    @Override
                    public void onSelect(boolean positive, String text) {
                        if (positive && text != null && !text.trim().isEmpty()) {
                            Mp.get().setName(text.trim());
                        }
                    }
                });
            }
        };
        setName.textColor(0xDDDDDD);
        setName.setRect(MARGIN + btnW + 2, pos, btnW, 16);
        add(setName);

        StyledButton cancel = new StyledButton(Chrome.Type.GREY_BUTTON_TR,
                Messages.get(HeroSelectScene.class, "daily_no"), 6) {
            @Override
            protected void onClick() {
                super.onClick();
                hide();
            }
        };
        cancel.textColor(0xDDDDDD);
        cancel.setRect(MARGIN + (btnW + 2) * 2, pos, btnW, 16);
        add(cancel);

        pos += 16;

        resize(width, (int) (pos + MARGIN));
    }

    // ====================== 挑战相关 ======================

    private static List<Integer> challengeIndices(int mask) {
        ArrayList<Integer> result = new ArrayList<>();
        for (int i = 0; i < Challenges.MASKS.length && i < Challenges.NAME_IDS.length; i++) {
            if ((mask & Challenges.MASKS[i]) != 0) {
                result.add(i);
            }
        }
        return result;
    }

    private void openPage(int target) {
        if (target < 0 || target >= pageCount || target == page) return;
        WndDailyConfig next = new WndDailyConfig(data, diff, target);
        hide();
        ShatteredPixelDungeon.scene().addToFront(next);
    }

    // ====================== 开始每日挑战 ======================

    /**
     * 点击开始后先弹出与联机一致的职业选择窗口，选定职业再进入每日挑战；
     * 取消选择则回到本配置窗口。职业选择会写入 {@link GamesInProgress#selectedClass}，
     * {@code Dungeon.init()} 用它创建英雄，不再依赖主界面当前选中的英雄。
     */
    private void startDaily() {
        ShatteredPixelDungeon.scene().addToFront(new WndHeroClassSelect(cls -> {
            if (cls == null) return;   // 取消：保留每日配置窗口
            beginDaily(cls);
        }));
    }

    private void beginDaily(HeroClass cls) {
        GamesInProgress.selectedClass = cls;

        long serverDay = DailyUtils.parseUtcDay(data.date);
        long last = SPDSettings.lastDaily();
        boolean isReplay = serverDay >= 0 ? last >= serverDay : diff > 0;

        if (!isReplay) {
            long time = serverDay >= 0
                    ? serverDay
                    : Math.max(Game.realTime - (Game.realTime % DAY), 20_148 * DAY);
            SPDSettings.lastDaily(time);
            Dungeon.dailyReplay = false;
        } else {
            Dungeon.dailyReplay = true;
        }

        DailyConfig.apply(data);
        Dungeon.overrideSeed = -1;
        Dungeon.seed = data.seed;
        Dungeon.customSeedText = data.date == null ? "" : data.date;

        Dungeon.hero = null;
        Dungeon.daily = true;
        ActionIndicator.clearAction();
        InterlevelScene.mode = InterlevelScene.Mode.DESCEND;

        hide();
        Game.switchScene(InterlevelScene.class);
    }

    // ====================== 组件 ======================

    private ColorBlock separator(float x, float y, float w) {
        ColorBlock line = new ColorBlock(w, 1, 0xFFFFFFFF);
        line.color(0x3A4A6B);
        line.alpha(0.6f);
        line.x = x;
        line.y = y;
        PixelScene.align(line);
        return line;
    }

    /** 难度/挑战数的小徽章：TAG 底板 + 居中文字。 */
    private class Badge extends Component {

        private final NinePatch bg;
        private final RenderedTextBlock text;

        Badge(String label, int color) {
            bg = Chrome.get(Chrome.Type.WINDOW);
            bg.hardlight(color);
            add(bg);

            text = PixelScene.renderTextBlock(label, 6);
            text.hardlight(0xFFFFFF);
            text.maxWidth(200);
            add(text);

            setSize(text.width() + 8, 12);
        }

        @Override
        protected void layout() {
            bg.x = x;
            bg.y = y;
            bg.size(width, height);

            text.setPos(x + (width - text.width()) / 2f, y + (height - text.height()) / 2f);
            PixelScene.align(text);
        }
    }

    /** 单个挑战图标卡片：点击查看名称与说明。 */
    private class ChallengeTile extends Button {

        private final int index;
        private final NinePatch bg;
        private final Image icon;

        ChallengeTile(int index) {
            super();
            this.index = index;

            bg = Chrome.get(Chrome.Type.GREY_BUTTON_TR);
            addToBack(bg);

            icon = ChallengeIcons.icon(index);
            add(icon);
        }

        @Override
        protected void layout() {
            super.layout();

            bg.x = x;
            bg.y = y;
            bg.size(width, height);

            icon.x = x + (width - icon.width()) / 2f;
            icon.y = y + (height - icon.height()) / 2f;
            PixelScene.align(icon);
        }

        @Override
        protected void onPointerDown() {
            bg.brightness(1.25f);
            Sample.INSTANCE.play(Assets.Sounds.CLICK);
        }

        @Override
        protected void onPointerUp() {
            bg.resetColor();
        }

        @Override
        protected void onClick() {
            ShatteredPixelDungeon.scene().addToFront(new WndTitledMessage(
                    ChallengeIcons.icon(index),
                    Messages.titleCase(Messages.get(Challenges.class, Challenges.NAME_IDS[index])),
                    Messages.get(Challenges.class, Challenges.NAME_IDS[index] + "_desc")));
        }

        @Override
        protected String hoverText() {
            return Messages.titleCase(Messages.get(Challenges.class, Challenges.NAME_IDS[index]));
        }
    }
}
