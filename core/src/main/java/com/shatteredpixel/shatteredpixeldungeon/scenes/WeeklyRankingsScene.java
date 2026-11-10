package com.shatteredpixel.shatteredpixeldungeon.scenes;

import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.NetIcons;
import com.shatteredpixel.shatteredpixeldungeon.effects.Flare;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.services.daily.DailyImpl;
import com.shatteredpixel.shatteredpixeldungeon.services.daily.DailyService;
import com.shatteredpixel.shatteredpixeldungeon.services.daily.DailyUtils;
import com.shatteredpixel.shatteredpixeldungeon.services.daily.LeaderboardData;
import com.shatteredpixel.shatteredpixeldungeon.ui.Archs;
import com.shatteredpixel.shatteredpixeldungeon.ui.Button;
import com.shatteredpixel.shatteredpixeldungeon.ui.IconButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.ScrollPane;
import com.shatteredpixel.shatteredpixeldungeon.windows.IconTitle;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndLeaderboardEntry;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.Camera;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.ui.Component;
import com.watabou.utils.Callback;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 周榜：汇总本周 7 天每日挑战成绩，同一玩家取本周最高分。
 * 前三名使用金银铜样式，其余显示“第 X 名”。
 */
public class WeeklyRankingsScene extends PixelScene {

    /** 0=本周，-1=上周，以此类推。 */
    public static int weekOffset = 0;

    private static final int PAGE_SIZE = 20;
    private static final int FETCH_PAGE_SIZE = 100;
    private static final int MAX_FETCH_PAGES = 20;
    private static final float ROW_HEIGHT = 26;
    private static final float PODIUM_HEIGHT = 52;
    private static final float MAX_ROW_WIDTH = 184;

    private IconTitle title;
    private RenderedTextBlock rangeText;
    private RenderedTextBlock statusText;
    private RenderedTextBlock pageText;
    private ScrollPane pane;
    private Component content;
    private IconButton prevWeekBtn;
    private IconButton nextWeekBtn;
    private IconButton prevPageBtn;
    private IconButton nextPageBtn;
    private IconButton refreshBtn;
    private IconButton exitBtn;
    private ColorBlock headerBg;
    private ColorBlock headerLine;
    private ColorBlock navBg;

    private int viewWidth;
    private String weekStart = "";
    private String weekEnd = "";

    private int currentPage = 1;
    private int totalPage = 1;
    private int fetchGeneration = 0;
    private volatile int daysDone = 0;
    private volatile int daysFailed = 0;
    private boolean loading = false;
    private boolean leaving = false;

    private final List<WeeklyEntry> entries = new ArrayList<>();
    private final Map<String, WeeklyEntry> byPlayer = new HashMap<>();

    @Override
    public void create() {
        super.create();
        PixelScene.uiCamera.visible = false;

        int w = Camera.main.width;
        int h = Camera.main.height;
        viewWidth = w;
        if (weekOffset > 0) weekOffset = 0;

        Archs archs = new Archs();
        archs.setSize(w, h);
        add(archs);

        headerBg = new ColorBlock(w, 48, 0x0A1520);
        headerBg.alpha(0.45f);
        add(headerBg);

        title = new IconTitle(Icons.RANKINGS.get(), Messages.get(this, "title"));
        title.setRect(0, 0, w, 0);
        title.setPos((w - title.reqWidth()) / 2f, 2);
        add(title);

        navBg = new ColorBlock(0, 0, 0x122333);
        navBg.alpha(0.55f);
        add(navBg);

        rangeText = PixelScene.renderTextBlock("", 6);
        rangeText.hardlight(0x888888);
        add(rangeText);

        prevWeekBtn = new IconButton(Icons.get(Icons.LEFTARROW)) {
            @Override
            protected void onClick() {
                changeWeek(-1);
            }
        };
        prevWeekBtn.setSize(14, 14);
        add(prevWeekBtn);

        nextWeekBtn = new IconButton(Icons.get(Icons.RIGHTARROW)) {
            @Override
            protected void onClick() {
                changeWeek(1);
            }
        };
        nextWeekBtn.setSize(14, 14);
        add(nextWeekBtn);

        statusText = PixelScene.renderTextBlock("", 7);
        statusText.setPos(8, title.bottom() + 28);
        add(statusText);

        content = new Component();
        pane = new ScrollPane(content);
        add(pane);
        pane.setRect(0, statusText.top(), w, Math.max(1, h - statusText.top() - 26));

        prevPageBtn = new IconButton(Icons.get(Icons.LEFTARROW)) {
            @Override
            protected void onClick() {
                if (currentPage > 1) {
                    currentPage--;
                    renderPage();
                }
            }
        };
        prevPageBtn.setRect(4, h - 18, 16, 16);
        add(prevPageBtn);

        pageText = PixelScene.renderTextBlock("", 6);
        pageText.setPos(w / 2f - pageText.width() / 2f, h - 15);
        add(pageText);

        nextPageBtn = new IconButton(Icons.get(Icons.RIGHTARROW)) {
            @Override
            protected void onClick() {
                if (currentPage < totalPage) {
                    currentPage++;
                    renderPage();
                }
            }
        };
        nextPageBtn.setRect(w - 20, h - 18, 16, 16);
        add(nextPageBtn);

        refreshBtn = new IconButton(NetIcons.get(NetIcons.GLOBE)) {
            @Override
            protected void onClick() {
                loadWeek();
            }
        };
        refreshBtn.setRect(w - 40, 2, 16, 16);
        add(refreshBtn);

        exitBtn = new IconButton(Icons.get(Icons.EXIT)) {
            @Override
            protected void onClick() {
                leaveScene();
            }
        };
        exitBtn.setRect(w - 20, 2, 16, 16);
        add(exitBtn);

        updateWeekHeader();
        headerBg.size(w, rangeText.bottom() + 4);
        statusText.setPos(8, rangeText.bottom() + 6);
        pane.setRect(0, statusText.top(), w, Math.max(1, h - statusText.top() - 26));
        loadWeek();
    }

    @Override
    public void onBackPressed() {
        leaveScene();
    }

    private void leaveScene() {
        leaving = true;
        fetchGeneration++;
        ShatteredPixelDungeon.switchNoFade(DailyRankingsScene.class);
    }

    @Override
    public void destroy() {
        leaving = true;
        fetchGeneration++;
        super.destroy();
    }

    private boolean isSceneAlive() {
        return !leaving && ShatteredPixelDungeon.scene() instanceof WeeklyRankingsScene;
    }

    private void updateWeekHeader() {
        String today = DailyUtils.todayUtc();
        weekStart = DailyUtils.shiftUtcDay(DailyUtils.startOfUtcWeek(today), weekOffset * 7);
        weekEnd = DailyUtils.shiftUtcDay(weekStart, 6);
        rangeText.text(Messages.get(this, "week_range", weekStart, weekEnd));
        rangeText.setPos(0, 0);
        float y = title.bottom() + 1;
        float x = (viewWidth - rangeText.width()) / 2f;
        rangeText.setPos(x, y);
        PixelScene.align(rangeText);

        navBg.x = x - 20;
        navBg.y = y - 4;
        navBg.size(rangeText.width() + 40, rangeText.height() + 8);

        float buttonY = y + (rangeText.height() - 14) / 2f;
        prevWeekBtn.setRect(x - 18, buttonY, 14, 14);
        nextWeekBtn.setRect(x + rangeText.width() + 4, buttonY, 14, 14);
        nextWeekBtn.enable(weekOffset < 0);
    }

    private void changeWeek(int delta) {
        int target = weekOffset + delta;
        if (target > 0) return;
        weekOffset = target;
        currentPage = 1;
        updateWeekHeader();
        loadWeek();
    }

    private void loadWeek() {
        final int generation = ++fetchGeneration;
        loading = true;
        daysDone = 0;
        daysFailed = 0;
        currentPage = 1;
        totalPage = 1;
        entries.clear();
        byPlayer.clear();

        statusText.visible = true;
        statusText.text(Messages.get(this, "loading"));
        pane.visible = false;
        content.clear();
        content.setRect(0, 0, Camera.main.width, 0);
        pageText.text("");
        prevPageBtn.visible = false;
        nextPageBtn.visible = false;

        fetchDay(0, generation);
    }

    private void fetchDay(final int dayIndex, final int generation) {
        if (generation != fetchGeneration) return;
        if (dayIndex >= 7) {
            finishFetch(generation);
            return;
        }

        final String day = DailyUtils.shiftUtcDay(weekStart, dayIndex);
        if (day.compareTo(DailyUtils.todayUtc()) > 0) {
            // 本周尚未到来的日期没有成绩，不需要发请求
            dayFinished(dayIndex, generation, false);
            return;
        }
        fetchPage(dayIndex, day, 1, generation);
    }

    private void fetchPage(final int dayIndex, final String day, final int page,
                           final int generation) {
        if (generation != fetchGeneration) return;

        DailyImpl.getService().fetchLeaderboard(day, page, FETCH_PAGE_SIZE,
                new DailyService.DailyResultCallback<LeaderboardData>() {
                    @Override
                    public void onSuccess(LeaderboardData result) {
                        if (generation != fetchGeneration) return;

                        if (result != null && result.data != null) {
                            merge(result.data.entries, day);

                            int size = result.data.pageSize > 0
                                    ? result.data.pageSize : FETCH_PAGE_SIZE;
                            if (page * size < result.data.totalPlayers
                                    && page < MAX_FETCH_PAGES) {
                                fetchPage(dayIndex, day, page + 1, generation);
                                return;
                            }
                        }
                        dayFinished(dayIndex, generation, false);
                    }

                    @Override
                    public void onFailure(String error) {
                        if (generation == fetchGeneration) {
                            dayFinished(dayIndex, generation, true);
                        }
                    }
                });
    }

    private void dayFinished(final int dayIndex, final int generation, boolean failed) {
        if (generation != fetchGeneration) return;
        daysDone++;
        if (failed) daysFailed++;

        Game.runOnRenderThread(new Callback() {
            @Override
            public void call() {
                if (generation != fetchGeneration || !isSceneAlive()) return;
                statusText.text(Messages.get(WeeklyRankingsScene.this, "loading")
                        + "  " + daysDone + "/7");
            }
        });

        fetchDay(dayIndex + 1, generation);
    }

    private void finishFetch(final int generation) {
        Game.runOnRenderThread(new Callback() {
            @Override
            public void call() {
                if (generation != fetchGeneration || !isSceneAlive()) return;
                loading = false;
                entries.sort(new Comparator<WeeklyEntry>() {
                    @Override
                    public int compare(WeeklyEntry a, WeeklyEntry b) {
                        if (a.best.score != b.best.score) {
                            return Integer.compare(b.best.score, a.best.score);
                        }
                        if (a.best.depth != b.best.depth) {
                            return Integer.compare(b.best.depth, a.best.depth);
                        }
                        String an = a.best.playerName == null ? "" : a.best.playerName;
                        String bn = b.best.playerName == null ? "" : b.best.playerName;
                        return an.compareToIgnoreCase(bn);
                    }
                });

                for (int i = 0; i < entries.size(); i++) {
                    entries.get(i).rank = i + 1;
                    // 详情窗口按周榜名次展示，而不是该玩家某一天的日榜名次
                    entries.get(i).best.rank = i + 1;
                }
                totalPage = Math.max(1, (entries.size() + PAGE_SIZE - 1) / PAGE_SIZE);
                if (currentPage > totalPage) currentPage = totalPage;

                if (entries.isEmpty()) {
                    statusText.visible = true;
                    statusText.text(Messages.get(WeeklyRankingsScene.this,
                            daysFailed >= 7 ? "load_failed" : "empty"));
                    pane.visible = false;
                    pageText.text("");
                    prevPageBtn.visible = false;
                    nextPageBtn.visible = false;
                } else {
                    renderPage();
                }
            }
        });
    }

    private void merge(List<LeaderboardData.Entry> list, String day) {
        if (list == null) return;
        for (LeaderboardData.Entry entry : list) {
            if (entry == null) continue;
            String key = entry.playerId != null && !entry.playerId.isEmpty()
                    ? "id:" + entry.playerId
                    : "name:" + (entry.playerName == null ? "" : entry.playerName)
                    + "|" + (entry.heroClass == null ? "" : entry.heroClass.name());

            WeeklyEntry weekly = byPlayer.get(key);
            if (weekly == null) {
                weekly = new WeeklyEntry(entry);
                byPlayer.put(key, weekly);
                entries.add(weekly);
            } else if (entry.score > weekly.best.score
                    || (entry.score == weekly.best.score && entry.depth > weekly.best.depth)) {
                weekly.best = entry;
            }

            // 同一玩家同一天只算一把，避免分页重复计数
            String playedDate = entry.date != null && !entry.date.isEmpty()
                    ? entry.date : day;
            if (weekly.playedDates.add(playedDate)) {
                weekly.runs++;
                if (entry.rank == 1) weekly.gold++;
                else if (entry.rank == 2) weekly.silver++;
                else if (entry.rank == 3) weekly.bronze++;
            }
        }
    }

    private void renderPage() {
        if (entries.isEmpty()) {
            statusText.visible = true;
            statusText.text(Messages.get(this, "empty"));
            pane.visible = false;
            return;
        }

        if (daysFailed > 0) {
            statusText.visible = true;
            statusText.text(Messages.get(this, "partial"));
        } else {
            statusText.visible = false;
        }

        pane.visible = true;
        content.clear();
        int width = Camera.main.width;
        float rowWidth = Math.min(MAX_ROW_WIDTH, width - 8);
        float left = (width - rowWidth) / 2f;

        int start = (currentPage - 1) * PAGE_SIZE;
        int end = Math.min(entries.size(), start + PAGE_SIZE);
        float y = 0;
        for (int i = start; i < end; i++) {
            WeeklyEntry entry = entries.get(i);
            float rowH = currentPage == 1 && entry.rank <= 3 ? PODIUM_HEIGHT : ROW_HEIGHT;
            WeeklyRecord row = new WeeklyRecord(entry);
            row.setRect(left, y, rowWidth, rowH);
            content.add(row);
            y += rowH + 2;
        }
        content.setRect(0, 0, width, y);

        pageText.text(Messages.get(this, "page", currentPage, totalPage));
        pageText.setPos(width / 2f - pageText.width() / 2f, pageText.top());
        prevPageBtn.visible = currentPage > 1;
        nextPageBtn.visible = currentPage < totalPage;
    }

    private static class WeeklyEntry {
        LeaderboardData.Entry best;
        int rank;
        int runs;
        int gold;
        int silver;
        int bronze;
        final java.util.HashSet<String> playedDates = new java.util.HashSet<>();

        WeeklyEntry(LeaderboardData.Entry entry) {
            best = entry;
        }
    }

    private class WeeklyRecord extends Button {
        private final WeeklyEntry weekly;
        private final int rank;

        private RenderedTextBlock rankText;
        private RenderedTextBlock name;
        private RenderedTextBlock goldText;
        private RenderedTextBlock silverText;
        private RenderedTextBlock bronzeText;
        private RenderedTextBlock runsText;
        private BitmapText score;
        private RenderedTextBlock scoreBig;
        private Image classIcon;
        private ColorBlock background;
        private ColorBlock rankBg;
        private ColorBlock accent;
        private ColorBlock avatarBg;
        private ColorBlock scoreBg;
        private Flare flare;
        private final boolean podium;

        WeeklyRecord(WeeklyEntry weekly) {
            super();
            this.weekly = weekly;
            this.rank = weekly.rank;
            this.podium = rank <= 3;

            int bgColor = 0x0B1622;
            float bgAlpha = 0.45f;
            int accentColor = 0x45637D;
            int rankColor = 0;
            if (rank == 1) {
                bgColor = 0xFFD166;
                bgAlpha = 0.18f;
                accentColor = 0xFFD700;
                rankColor = 0xFFD700;
            } else if (rank == 2) {
                bgColor = 0xD8DEE9;
                bgAlpha = 0.15f;
                accentColor = 0xC0C0C0;
                rankColor = 0xC0C0C0;
            } else if (rank == 3) {
                bgColor = 0xE09A5B;
                bgAlpha = 0.17f;
                accentColor = 0xCD7F32;
                rankColor = 0xCD7F32;
            }

            background = new ColorBlock(0, 0, bgColor);
            background.alpha(bgAlpha);
            addToBack(background);
            accent.color(accentColor);

            if (podium) {
                rankBg.visible = true;
                rankBg.color(rankColor);
                rankBg.alpha(0.95f);
                flare.visible = true;
                flare.color(rankColor, true);
                flare.alpha(0.75f);
            }

            rankText.text(rank <= 3
                    ? Integer.toString(rank)
                    : Messages.get(WeeklyRankingsScene.this, "rank_n", rank));
            int color = rank == 1 ? 0xFFF0A0
                    : rank == 2 ? 0xF5F8FF
                    : rank == 3 ? 0xFFD2A8
                    : 0xDDDDDD;
            rankText.hardlight(rank <= 3 ? 0xFFFFFF : color);
            name.hardlight(color);
            score.hardlight(color);
            scoreBig.hardlight(color);
            goldText.hardlight(0xFFD700);
            silverText.hardlight(0xC0C0C0);
            bronzeText.hardlight(0xCD7F32);
            runsText.hardlight(0xAAAAAA);

            name.text(weekly.best.playerName == null ? "" : weekly.best.playerName);
            score.text(Integer.toString(weekly.best.score));
            score.measure();
            scoreBig.text(Integer.toString(weekly.best.score));
            score.visible = !podium;
            scoreBig.visible = podium;

            goldText.visible = weekly.gold > 0;
            silverText.visible = weekly.silver > 0;
            bronzeText.visible = weekly.bronze > 0;
            if (goldText.visible) {
                goldText.text(Messages.get(WeeklyRankingsScene.this, "medal_gold", weekly.gold));
            }
            if (silverText.visible) {
                silverText.text(Messages.get(WeeklyRankingsScene.this, "medal_silver", weekly.silver));
            }
            if (bronzeText.visible) {
                bronzeText.text(Messages.get(WeeklyRankingsScene.this, "medal_bronze", weekly.bronze));
            }
            runsText.text(Messages.get(WeeklyRankingsScene.this, "runs_week", weekly.runs));

            HeroClass cls = weekly.best.heroClass == null
                    ? HeroClass.WARRIOR : weekly.best.heroClass;
            classIcon.copy(Icons.get(cls));
        }

        @Override
        protected void createChildren() {
            super.createChildren();

            accent = new ColorBlock(0, 0, 0x45637D);
            add(accent);

            flare = new Flare(6, 13);
            flare.angularSpeed = 45;
            flare.visible = false;
            add(flare);

            rankBg = new ColorBlock(0, 0, 0xFFFFFF);
            rankBg.visible = false;
            add(rankBg);

            rankText = PixelScene.renderTextBlock(7);
            add(rankText);

            avatarBg = new ColorBlock(0, 0, 0x0A121A);
            avatarBg.alpha(0.9f);
            add(avatarBg);

            classIcon = new Image();
            add(classIcon);

            name = PixelScene.renderTextBlock(7);
            add(name);

            goldText = PixelScene.renderTextBlock(6);
            goldText.visible = false;
            add(goldText);

            silverText = PixelScene.renderTextBlock(6);
            silverText.visible = false;
            add(silverText);

            bronzeText = PixelScene.renderTextBlock(6);
            bronzeText.visible = false;
            add(bronzeText);

            runsText = PixelScene.renderTextBlock(6);
            add(runsText);

            scoreBg = new ColorBlock(0, 0, 0x142333);
            scoreBg.alpha(0.9f);
            add(scoreBg);

            score = new BitmapText(PixelScene.pixelFont);
            add(score);

            scoreBig = PixelScene.renderTextBlock(9);
            add(scoreBig);
        }

        @Override
        protected void layout() {
            super.layout();

            float centerY = y + height / 2f;
            if (background != null) {
                background.x = x + 1;
                background.y = y + 1;
                background.size(width - 2, height - 2);
            }
            accent.x = x + 1;
            accent.y = y + 1;
            accent.size(3, height - 2);

            float cardX = x + 1;
            float rankW;
            float textX;
            float scoreW = podium ? scoreBig.width() : score.width();
            float scoreX = x + width - 6 - scoreW;

            if (podium) {
                float badge = 22;
                rankW = badge;
                rankBg.x = cardX + 7;
                rankBg.y = centerY - badge / 2f;
                rankBg.size(badge, badge);
                rankText.setPos(rankBg.x + (badge - rankText.width()) / 2f,
                        centerY - rankText.height() / 2f + 1);
                flare.x = rankBg.x + badge / 2f;
                flare.y = centerY;

                avatarBg.x = cardX + 36;
                avatarBg.y = centerY - 10;
                avatarBg.size(20, 20);
                classIcon.x = avatarBg.x + (20 - classIcon.width()) / 2f;
                classIcon.y = avatarBg.y + (20 - classIcon.height()) / 2f;
                textX = cardX + 61;
            } else {
                rankW = Math.max(22, rankText.width() + 6);
                rankText.setPos(cardX + (rankW - rankText.width()) / 2f,
                        centerY - rankText.height() / 2f + 1);

                avatarBg.x = cardX + rankW + 3;
                avatarBg.y = centerY - 9;
                avatarBg.size(18, 18);
                classIcon.x = avatarBg.x + (18 - classIcon.width()) / 2f;
                classIcon.y = avatarBg.y + (18 - classIcon.height()) / 2f;
                textX = avatarBg.x + 22;
            }

            PixelScene.align(rankText);
            PixelScene.align(classIcon);

            scoreBg.x = scoreX - 4;
            scoreBg.y = centerY - 7;
            scoreBg.size(scoreW + 8, 14);
            if (podium) {
                scoreBig.x = scoreX;
                scoreBig.y = centerY - scoreBig.height() / 2f + 1;
                PixelScene.align(scoreBig);
            } else {
                score.x = scoreX;
                score.y = centerY - score.height() / 2f + 1;
                PixelScene.align(score);
            }

            name.maxWidth((int) Math.max(1, scoreBg.x - textX - 3));
            name.setPos(0, 0);
            float groupH = name.height() + 1 + runsText.height();
            float top = centerY - groupH / 2f;
            name.setPos(textX, top);
            PixelScene.align(name);

            float metaX = textX;
            float metaY = top + name.height() + 1;
            if (goldText.visible) {
                goldText.setPos(metaX, metaY);
                metaX += goldText.width() + 3;
            }
            if (silverText.visible) {
                silverText.setPos(metaX, metaY);
                metaX += silverText.width() + 3;
            }
            if (bronzeText.visible) {
                bronzeText.setPos(metaX, metaY);
                metaX += bronzeText.width() + 3;
            }
            runsText.setPos(metaX + (metaX > textX ? 2 : 0), metaY);
            PixelScene.align(goldText);
            PixelScene.align(silverText);
            PixelScene.align(bronzeText);
            PixelScene.align(runsText);
        }

        @Override
        protected void onClick() {
            if (!WndLeaderboardEntry.open(weekly.best)) {
                ShatteredPixelDungeon.scene().addToFront(new WndLeaderboardEntry(weekly.best));
            }
        }
    }
}
