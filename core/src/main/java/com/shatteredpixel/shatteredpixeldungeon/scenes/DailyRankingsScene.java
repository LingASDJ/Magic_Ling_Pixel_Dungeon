package com.shatteredpixel.shatteredpixeldungeon.scenes;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.services.daily.DailyImpl;
import com.shatteredpixel.shatteredpixeldungeon.services.daily.DailyService;
import com.shatteredpixel.shatteredpixeldungeon.services.daily.DailyUtils;
import com.shatteredpixel.shatteredpixeldungeon.services.daily.LeaderboardData;
import com.shatteredpixel.shatteredpixeldungeon.effects.Flare;
import com.shatteredpixel.shatteredpixeldungeon.ui.Archs;
import com.shatteredpixel.shatteredpixeldungeon.ui.Button;
import com.shatteredpixel.shatteredpixeldungeon.ui.IconButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.ScrollPane;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.NetIcons;
import com.shatteredpixel.shatteredpixeldungeon.windows.IconTitle;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndLeaderboardEntry;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.Camera;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.ui.Component;
import com.watabou.utils.Callback;

import java.util.List;

/**
 * Full-screen daily leaderboard, intentionally modeled on RankingsScene.
 * Only total score ordering is used (the server already sorts by score).
 */
public class DailyRankingsScene extends PixelScene {

    public static String date = "";

    /** true = 重玩榜（每名玩家每天最高分），false = 正常每日榜。 */
    public static boolean replayMode = false;

    private static final int PAGE_SIZE = 20;
    private static final float ROW_HEIGHT = 16;
    private static final float MAX_ROW_WIDTH = 160;

    private IconTitle title;
    private RenderedTextBlock dateText;
    private RenderedTextBlock statusText;
    private RenderedTextBlock pageText;
    private ScrollPane pane;
    private Component content;
    private IconButton prevBtn;
    private IconButton nextBtn;
    private IconButton prevDateBtn;
    private IconButton nextDateBtn;
    private IconButton refreshBtn;
    private IconButton exitBtn;
    private RedButton weeklyBtn;
    private RedButton replayBtn;

    private int viewWidth;
    private int requestId = 0;

    private int currentPage = 1;
    private int totalPlayers = 0;
    private int totalPage = 1;

    @Override
    public void create() {
        super.create();

        PixelScene.uiCamera.visible = false;

        int w = Camera.main.width;
        int h = Camera.main.height;
        viewWidth = w;

        if (date == null || date.isEmpty() || DailyUtils.parseUtcDay(date) < 0) {
            date = DailyUtils.todayUtc();
        } else if (date.compareTo(DailyUtils.todayUtc()) > 0) {
            date = DailyUtils.todayUtc();
        }

        Archs archs = new Archs();
        archs.setSize(w, h);
        add(archs);

        title = new IconTitle(Icons.CALENDAR.get(),
                Messages.get(this, replayMode ? "replay_title" : "title"));
        title.setRect(0, 0, w, 0);
        title.setPos((w - title.reqWidth()) / 2f, 2);
        add(title);

        weeklyBtn = new RedButton(Messages.get(this, "weekly"), 7) {
            @Override
            protected void onClick() {
                WeeklyRankingsScene.weekOffset = 0;
                ShatteredPixelDungeon.switchNoFade(WeeklyRankingsScene.class);
            }
        };
        weeklyBtn.setRect(4, 2, 42, 16);
        add(weeklyBtn);

        replayBtn = new RedButton(Messages.get(this, replayMode ? "daily" : "replay"), 7) {
            @Override
            protected void onClick() {
                replayMode = !replayMode;
                ShatteredPixelDungeon.switchNoFade(DailyRankingsScene.class);
            }
        };
        replayBtn.setRect(50, 2, 42, 16);
        add(replayBtn);

        dateText = PixelScene.renderTextBlock(Messages.get(this, "date", date), 6);
        dateText.hardlight(0x888888);
        dateText.setPos((w - dateText.width()) / 2f, title.bottom() + 1);
        add(dateText);

        // 日期筛选：前后一天切换；到达今天后禁用“下一天”
        prevDateBtn = new IconButton(Icons.get(Icons.LEFTBUTTON)) {
            @Override
            protected void onClick() {
                changeDate(-1);
            }
        };
        prevDateBtn.setSize(14, 14);
        add(prevDateBtn);

        nextDateBtn = new IconButton(Icons.get(Icons.RIGHTBUTTON)) {
            @Override
            protected void onClick() {
                changeDate(1);
            }
        };
        nextDateBtn.setSize(14, 14);
        add(nextDateBtn);
        updateDateHeader();

        statusText = PixelScene.renderTextBlock("", 7);
        statusText.setPos(8, dateText.bottom() + 6);
        add(statusText);

        content = new Component();
        pane = new ScrollPane(content);
        add(pane);
        pane.setRect(0, dateText.bottom() + 6, w, Math.max(1, h - dateText.bottom() - 26));

        prevBtn = new IconButton(Icons.get(Icons.LEFTBUTTON)) {
            @Override
            protected void onClick() {
                if (currentPage > 1) {
                    currentPage--;
                    loadPage();
                }
            }
        };
        prevBtn.setRect(4, h - 18, 16, 16);
        add(prevBtn);

        pageText = PixelScene.renderTextBlock("", 6);
        pageText.setPos(w / 2f - pageText.width() / 2f, h - 15);
        add(pageText);

        nextBtn = new IconButton(Icons.get(Icons.RIGHTBUTTON)) {
            @Override
            protected void onClick() {
                if (currentPage < totalPage) {
                    currentPage++;
                    loadPage();
                }
            }
        };
        nextBtn.setRect(w - 20, h - 18, 16, 16);
        add(nextBtn);

        refreshBtn = new IconButton(NetIcons.get(NetIcons.GLOBE)) {
            @Override
            protected void onClick() {
                loadPage();
            }
        };
        refreshBtn.setRect(96, 2, 16, 16);
        add(refreshBtn);

        exitBtn = new IconButton(Icons.get(Icons.EXIT)) {
            @Override
            protected void onClick() {
                if (replayMode) {
                    replayMode = false;
                    ShatteredPixelDungeon.switchNoFade(DailyRankingsScene.class);
                } else {
                    ShatteredPixelDungeon.switchNoFade(HeroSelectScene.class);
                }
            }
        };
        exitBtn.setRect(w - 20, 2, 16, 16);
        add(exitBtn);

        loadPage();
    }

    @Override
    public void onBackPressed() {
        if (replayMode) {
            replayMode = false;
            ShatteredPixelDungeon.switchNoFade(DailyRankingsScene.class);
        } else {
            ShatteredPixelDungeon.switchNoFade(HeroSelectScene.class);
        }
    }

    private void updateDateHeader() {
        if (dateText == null) return;

        dateText.text(Messages.get(this, "date", date));
        dateText.setPos(0, 0);   // 先测量新文字宽度
        float y = title.bottom() + 1;
        float x = (viewWidth - dateText.width()) / 2f;
        dateText.setPos(x, y);
        PixelScene.align(dateText);

        float btnY = y + (dateText.height() - 14) / 2f;
        prevDateBtn.setRect(x - 18, btnY, 14, 14);
        nextDateBtn.setRect(x + dateText.width() + 4, btnY, 14, 14);
        nextDateBtn.enable(date.compareTo(DailyUtils.todayUtc()) < 0);
    }

    private void changeDate(int delta) {
        String target = DailyUtils.shiftUtcDay(date, delta);
        if (delta > 0 && target.compareTo(DailyUtils.todayUtc()) > 0) {
            return;
        }
        if (target.equals(date)) {
            return;
        }
        date = target;
        currentPage = 1;
        totalPage = 1;
        totalPlayers = 0;
        updateDateHeader();
        loadPage();
    }

    private void loadPage() {
        final int request = ++requestId;
        statusText.visible = true;
        statusText.text(Messages.get(this, "loading"));
        pane.visible = false;
        content.clear();
        content.setRect(0, 0, Camera.main.width, 0);
        prevBtn.visible = currentPage > 1;
        nextBtn.visible = currentPage < totalPage;

        fetchPage(
                new DailyService.DailyResultCallback<LeaderboardData>() {
                    @Override
                    public void onSuccess(LeaderboardData result) {
                        Game.runOnRenderThread(new Callback() {
                            @Override
                            public void call() {
                                if (request != requestId) return;
                                applyResult(result);
                            }
                        });
                    }

                    @Override
                    public void onFailure(String error) {
                        Game.runOnRenderThread(new Callback() {
                            @Override
                            public void call() {
                                if (request != requestId) return;
                                statusText.visible = true;
                                statusText.text(Messages.get(DailyRankingsScene.this, "load_failed"));
                                statusText.setPos(8, dateText.bottom() + 6);
                                pane.visible = false;
                                pageText.text("");
                                prevBtn.visible = currentPage > 1;
                                nextBtn.visible = false;
                            }
                        });
                    }
                });
    }

    private void fetchPage(DailyService.DailyResultCallback<LeaderboardData> callback) {
        if (replayMode) {
            DailyImpl.getService().fetchReplayLeaderboard(date, currentPage, PAGE_SIZE, callback);
        } else {
            DailyImpl.getService().fetchLeaderboard(date, currentPage, PAGE_SIZE, callback);
        }
    }

    private void applyResult(LeaderboardData result) {
        if (result == null || result.data == null) {
            statusText.visible = true;
            statusText.text(Messages.get(this, "load_failed"));
            pane.visible = false;
            return;
        }

        LeaderboardData.Data data = result.data;
        totalPlayers = data.totalPlayers;
        int pageSize = data.pageSize > 0 ? data.pageSize : PAGE_SIZE;
        totalPage = Math.max(1, (totalPlayers + pageSize - 1) / pageSize);
        if (currentPage > totalPage) currentPage = totalPage;

        List<LeaderboardData.Entry> entries = data.entries;
        if (entries == null || entries.isEmpty()) {
            statusText.visible = true;
            statusText.text(Messages.get(this, "empty"));
            pane.visible = false;
            pageText.text("");
            prevBtn.visible = currentPage > 1;
            nextBtn.visible = false;
            return;
        }

        statusText.visible = false;
        pane.visible = true;

        content.clear();
        float y = 0;
        int width = Camera.main.width;
        float rowWidth = Math.min(MAX_ROW_WIDTH, width - 8);
        float left = (width - rowWidth) / 2f;
        for (LeaderboardData.Entry entry : entries) {
            Record row = new Record(entry);
            row.setRect(left, y, rowWidth, ROW_HEIGHT);
            content.add(row);
            y += ROW_HEIGHT;
        }
        content.setRect(0, 0, width, y);

        pageText.text(Messages.get(this, "page", currentPage, totalPage));
        pageText.setPos(width / 2f - pageText.width() / 2f, pageText.top());
        prevBtn.visible = currentPage > 1;
        nextBtn.visible = currentPage < totalPage;
    }

    private class Record extends Button {

        private final LeaderboardData.Entry entry;

        private BitmapText position;
        private RenderedTextBlock name;
        private BitmapText score;
        private BitmapText depth;
        private Image steps;
        private Image classIcon;
        private BitmapText level;
        private ColorBlock background;
        private ColorBlock rankBg;
        private Image medal;
        private Flare flare;

        Record(LeaderboardData.Entry entry) {
            super();
            this.entry = entry;

            int bgColor = 0;
            float bgAlpha = 0f;
            int rankColor = 0;
            if (entry.rank == 1) {
                bgColor = 0xFFD166;
                bgAlpha = 0.30f;
                rankColor = 0xFFD700;
            } else if (entry.rank == 2) {
                bgColor = 0xD8DEE9;
                bgAlpha = 0.24f;
                rankColor = 0xC0C0C0;
            } else if (entry.rank == 3) {
                bgColor = 0xE09A5B;
                bgAlpha = 0.28f;
                rankColor = 0xCD7F32;
            }
            if (bgColor != 0) {
                background = new ColorBlock(0, 0, bgColor);
                background.alpha(bgAlpha);
                addToBack(background);

                // 前三名用金/银/铜奖牌作为名次底图，名次数字叠在奖牌上。
                medal.visible = true;
                medal.frame(16 * (entry.rank - 1), 0, 16, 16);

                flare.visible = true;
                flare.color(rankColor, true);
                flare.alpha(0.75f);
            }

            position.text(Integer.toString(entry.rank));
            position.measure();

            name.text(entry.playerName == null ? "" : entry.playerName);

            score.text(Integer.toString(entry.score));
            score.measure();

            depth.text(Integer.toString(entry.depth));
            depth.measure();

            level.text(Integer.toString(Math.max(1, entry.heroLevel)));
            level.measure();

            classIcon.copy(Icons.get(entry.heroClass));
            steps.copy(Icons.get(Icons.STAIRS));

            int color;
            if (entry.rank == 1) {
                color = 0xFFF0A0;       // gold
            } else if (entry.rank == 2) {
                color = 0xF5F8FF;       // silver
            } else if (entry.rank == 3) {
                color = 0xFFD2A8;       // bronze
            } else {
                color = entry.won ? 0xFFFF88 : 0xDDDDDD;
            }
            // 前三名的数字叠在奖牌上，统一使用白色（与原样式一致）。
            position.hardlight(entry.rank <= 3 ? 0xFFFFFF : color);
            name.hardlight(color);
            score.hardlight(color);
            depth.hardlight(color);
            level.hardlight(color);
            steps.hardlight(color);
        }

        @Override
        protected void createChildren() {
            super.createChildren();

            flare = new Flare(6, 11);
            flare.angularSpeed = 45;
            flare.visible = false;
            add(flare);

            rankBg = new ColorBlock(0, 0, 0xFFFFFF);
            rankBg.visible = false;
            add(rankBg);

            medal = new Image(Assets.Interfaces.RANKING_BADGES);
            medal.visible = false;
            add(medal);

            position = new BitmapText(PixelScene.pixelFont);
            add(position);

            name = PixelScene.renderTextBlock(6);
            add(name);

            score = new BitmapText(PixelScene.pixelFont);
            add(score);

            steps = new Image();
            add(steps);

            depth = new BitmapText(PixelScene.pixelFont);
            add(depth);

            classIcon = new Image();
            add(classIcon);

            level = new BitmapText(PixelScene.pixelFont);
            add(level);
        }

        @Override
        protected void layout() {
            super.layout();

            float centerY = y + height / 2f;

            if (background != null) {
                background.x = x;
                background.y = y;
                background.size(width, height);
            }

            // Hero class icon at the very front of the row.
            classIcon.x = x + 2 + (16 - classIcon.width()) / 2f;
            classIcon.y = centerY - classIcon.height() / 2f;
            PixelScene.align(classIcon);

            level.x = classIcon.x + (classIcon.width - level.width()) / 2f;
            level.y = classIcon.y + (classIcon.height - level.height()) / 2f + 1;
            PixelScene.align(level);

            position.x = x + 20 + (14 - position.width()) / 2f;
            position.y = centerY - position.height() / 2f + 1;
            PixelScene.align(position);

            if (medal.visible) {
                // 奖牌 PNG 每格右侧有透明留白、圆章偏上，整体向右下微调，
                // 使圆章中心与名次数字对齐。
                medal.x = x + 21;
                medal.y = centerY - 6;
                PixelScene.align(medal);
            }

            if (flare.visible) {
                flare.x = x + 27;
                flare.y = centerY;
            }

            steps.x = x + width - 30 + (16 - steps.width()) / 2f;
            steps.y = centerY - steps.height() / 2f;
            PixelScene.align(steps);

            depth.x = steps.x + (steps.width - depth.width()) / 2f;
            depth.y = steps.y + (steps.height - depth.height()) / 2f + 1;
            PixelScene.align(depth);

            score.x = x + width - 34 - score.width();
            score.y = centerY - score.height() / 2f + 1;
            PixelScene.align(score);

            name.maxWidth((int) Math.max(1, score.x - (x + 36)));
            name.setPos(x + 36, centerY - name.height() / 2f + 1);
            PixelScene.align(name);
        }

        @Override
        protected void onClick() {
            if (!WndLeaderboardEntry.open(entry)) {
                ShatteredPixelDungeon.scene().addToFront(new WndLeaderboardEntry(entry));
            }
        }
    }
}
