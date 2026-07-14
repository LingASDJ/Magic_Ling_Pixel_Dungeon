package com.shatteredpixel.shatteredpixeldungeon.windows;

import static com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene.renderTextBlock;

import com.shatteredpixel.shatteredpixeldungeon.Chrome;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.NetIcons;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.services.daily.DailyImpl;
import com.shatteredpixel.shatteredpixeldungeon.services.daily.DailyService;
import com.shatteredpixel.shatteredpixeldungeon.services.daily.LeaderboardData;
import com.shatteredpixel.shatteredpixeldungeon.ui.IconButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.ScrollPane;
import com.shatteredpixel.shatteredpixeldungeon.ui.StyledButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.ui.Component;
import com.watabou.utils.Callback;

import java.util.List;

public class WndLeaderboard extends Window {

    private static final int WIDTH  = 130;
    private static final int HEIGHT = 150;
    private boolean needsLayout = true;

    private String date;
    private int currentPage = 1;
    private int totalPlayers = 0;
    private int myRank = 0;
    private int myScore = 0;
    private int totalPage = 1;
    private int pageSize = 0;
    private List<LeaderboardData.Entry> entries = null;

    private IconTitle title;
    private ScrollPane pane;
    private Component content;
    private RenderedTextBlock statusText;
    private RenderedTextBlock pageText;
    private IconButton prevBtn;
    private IconButton nextBtn;
    private LeaderboardData leaderboardData = null;
    private StyledButton refreshBtn;
    private long lastRefreshTime = 0;

    public WndLeaderboard(String date) {
        this.date = date;

        resize(WIDTH, HEIGHT);

        title = new IconTitle(Icons.CALENDAR.get(), Messages.get(this, "title", date));
        title.imIcon.hardlight(0x80BFFF);
        title.setRect(0, 0, WIDTH, 0);
        title.setPos(0, 0);
        add(title);

        statusText = renderTextBlock(Messages.get(this, "loading"), 7);
        add(statusText);

        refreshBtn = new StyledButton(Chrome.Type.BLANK,Messages.get(this,"refresh")){
            @Override
            protected void onClick() {
                super.onClick();

                if(Game.realTime - lastRefreshTime < 30)
                    return;

                lastRefreshTime = Game.realTime;
                loadPage();
            }
        };
        refreshBtn.icon(NetIcons.get(NetIcons.GLOBE));
        refreshBtn.icon().scale.set(0.6f);
        add(refreshBtn);

        pane = new ScrollPane(new Component());
        add(pane);

        prevBtn = new IconButton(Icons.get(Icons.LEFTARROW)) {
            @Override
            protected void onClick() {
                currentPage--;
                updateLayout();
            }
        };
        prevBtn.setRect(0, HEIGHT - 14, 14, 14);
        add(prevBtn);

        pageText = renderTextBlock("", 6);
        pageText.setPos(WIDTH / 2f - pageText.width() / 2f, HEIGHT - 14);
        add(pageText);

        nextBtn = new IconButton(Icons.get(Icons.RIGHTARROW)) {
            @Override
            protected void onClick() {
                currentPage++;
                updateLayout();
            }
        };
        nextBtn.setRect(WIDTH - 14, HEIGHT - 14, 14, 14);
        add(nextBtn);

        loadPage();
    }

    @Override
    public void update() {
        super.update();
        if (needsLayout) {
            needsLayout = false;
            statusText.setPos(0, 16);
            refreshBtn.setRect(title.x + title.reqWidth(),title.y,refreshBtn.reqWidth(),refreshBtn.reqHeight());
            pane.setRect(0, 24, WIDTH, HEIGHT - 38);
            prevBtn.setRect(0, HEIGHT - 14, 14, 14);
            pageText.setPos(WIDTH / 2f - pageText.width() / 2f, HEIGHT - 14);
            nextBtn.setRect(WIDTH - 14, HEIGHT - 14, 14, 14);
        }
    }

    private void loadPage() {
        if( leaderboardData == null )
            statusText.text(Messages.get(this, "loading"));

        if (currentPage == 1) {
            prevBtn.visible = false;
            prevBtn.active = false;
        }else {
            prevBtn.visible = true;
            prevBtn.active = true;
        }

        if(currentPage == totalPage) {
            nextBtn.visible = false;
            nextBtn.active = false;
        }else {
            nextBtn.visible = true;
            nextBtn.active = true;
        }

        DailyImpl.getService().fetchLeaderboard(date,
                new DailyService.DailyResultCallback<LeaderboardData>() {
                    @Override
                    public void onSuccess(LeaderboardData result) {
                        if (result.data == null || result.data.entries.isEmpty()) {
                            statusText.text(Messages.get(this, "empty"));
                            pane.content().clear();
                            pane.content().setRect(0, 0, WIDTH, 0);
                            pageText.text("");
                            return;
                        }

                        leaderboardData = result;
                        statusText.text("");
                        Game.runOnRenderThread( () -> {
                            updateLayout();
                        });
                    }

                    @Override
                    public void onFailure(String error) {
                        statusText.text(Messages.get(WndLeaderboard.this, "load_failed"));
                        pane.content().clear();
                        pane.content().setRect(0, 0, WIDTH, 0);
                        pageText.text("");
                        ShatteredPixelDungeon.scene().addToFront(new WndError(error));
                    }
                });
    }

    private void updateLayout(){
        if(leaderboardData == null)
            return;

        totalPlayers = leaderboardData.data.totalPlayers;
        myRank = leaderboardData.data.myRank;
        myScore = leaderboardData.data.myScore;
        totalPage = 1;
        pageSize = 100;
        entries = leaderboardData.data.entries;

        Component c = pane.content();
        c.clear();

        int top = 0;

        int from = (currentPage - 1) * pageSize;
        int to   = Math.min(from + pageSize, entries.size());
        for (LeaderboardData.Entry entry : entries.subList(from, to)) {
            PlayerData playerData = new PlayerData(entry);
            playerData.setPos(0, top);

            if (myRank > 0 && entry.rank == myRank) {
                playerData.hardlight(0x80BFFF);
            } else if (entry.won) {
                playerData.hardlight(0xCCFFCC);
            }

            c.add(playerData);
            top = (int) playerData.bottom() + 2;
        }

        c.setRect(0, 0, WIDTH, top);

        statusText.text(Messages.get(WndLeaderboard.this, "rank") + "  "
                + Messages.get(WndLeaderboard.this, "player") + "      "
                + Messages.get(WndLeaderboard.this, "score"));
        ColorBlock sep = new ColorBlock(WIDTH, 1, 0xFF000000);
        sep.y = statusText.bottom() - 1;
        pageText.text( Messages.get(WndLeaderboard.this, "page", currentPage, totalPage ) );
        pageText.setPos(WIDTH / 2f - pageText.width() / 2f, pageText.top());
    }

    private static final float ROW_HEIGHT = 16;

    private class PlayerData extends Component {
        private static final float GAP = 4;

        private BitmapText position;
        private RenderedTextBlock name;
        private Image steps;
        private BitmapText depth;
        private Image classIcon;
        private BitmapText level;

        public void hardlight(int color) {
            name.hardlight(color);
        }

        public PlayerData(LeaderboardData.Entry entry) {

            position.text(Integer.toString(entry.rank));
            position.measure();

            name.text(entry.playerName);

            level.text(Integer.toString(entry.score));
            level.measure();

            classIcon.copy(Icons.get(entry.heroClass));
            if (entry.heroClass == HeroClass.ROGUE) {
                classIcon.brightness(2f);
            }

            depth.text(Integer.toString(entry.depth));
            depth.measure();

            setSize(WIDTH, ROW_HEIGHT);
        }

        @Override
        protected void createChildren() {
            position = new BitmapText(PixelScene.pixelFont);
            add(position);

            name = renderTextBlock(6);
            add(name);

            depth = new BitmapText(PixelScene.pixelFont);
            add(depth);

            steps = new Image();
            add(steps);

            classIcon = new Image();
            add(classIcon);

            level = new BitmapText(PixelScene.pixelFont);
            add(level);
        }

        @Override
        protected void layout() {
            super.layout();

            float centerY = y + height / 2f;

            position.x = x + (16 - position.width()) / 2f;
            position.y = centerY - position.height() / 2f + 1;
            PixelScene.align(position);

            classIcon.x = x + width - 16 + (16 - classIcon.width()) / 2f;
            classIcon.y = centerY - classIcon.height() / 2f;
            PixelScene.align(classIcon);

            level.x = classIcon.x + (classIcon.width - level.width()) / 2f;
            level.y = classIcon.y + (classIcon.height - level.height()) / 2f + 1;
            PixelScene.align(level);

            steps.x = x + width - 32 + (16 - steps.width()) / 2f;
            steps.y = centerY - steps.height() / 2f;
            PixelScene.align(steps);

            depth.x = steps.x + (steps.width - depth.width()) / 2f;
            depth.y = steps.y + (steps.height - depth.height()) / 2f + 1;
            PixelScene.align(depth);

            name.maxWidth((int) (steps.x - (x + 16 + GAP)));
            name.setPos(x + 16 + GAP, centerY - name.height() / 2f + 1);
            PixelScene.align(name);
        }
    }
}