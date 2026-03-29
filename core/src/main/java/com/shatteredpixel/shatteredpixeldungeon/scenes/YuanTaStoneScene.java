package com.shatteredpixel.shatteredpixeldungeon.scenes;

import static com.shatteredpixel.shatteredpixeldungeon.scenes.YuanTaStoneScene.StoryCard.getLangText;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Chrome;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.ExitButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.ScrollPane;
import com.shatteredpixel.shatteredpixeldungeon.ui.StyledButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.TitleBackground;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.IconTitle;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndTitledMessage;
import com.watabou.gltextures.TextureCache;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.NinePatch;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.ui.Component;
import com.watabou.utils.Bundle;
import com.watabou.utils.FileUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class YuanTaStoneScene extends PixelScene {

    private static final int BTN_HEIGHT = 24;
    private static final int GAP = 2;
    private static final int SCROLL_MARGIN = 20;
    private boolean noStoriesUnlocked = false;
    private final ArrayList<StoryCard> cards = new ArrayList<>();

    @Override
    public void create() {
        super.create();

        uiCamera.visible = false;

        int w = Camera.main.width;
        int h = Camera.main.height;
        boolean landscape = landscape();
        Music.INSTANCE.play(Assets.Music.BGM_1A, true);
        TitleBackground BG = new TitleBackground( w, h );
        add( BG );

        Image im = new Image(TextureCache.createGradient(0x66000000, 0x88000000, 0xAA000000, 0xCC000000, 0xFF000000));
        im.angle = 90;
        im.x = Camera.main.width;
        im.scale.x = Camera.main.height/5f;
        im.scale.y = Camera.main.width;
        add(im);

        IconTitle title = new IconTitle(Icons.get(Icons.CATALOG), Messages.get(this, "title"));
        title.setSize(200, 0);
        title.setPos((w - title.reqWidth()) / 2f, (20 - title.height()) / 2f);
        align(title);
        add(title);

        ExitButton btnExit = new ExitButton(){
            @Override
            protected void onClick() {
                Game.switchScene(GameScene.class);
            }
        };
        btnExit.setPos( Camera.main.width - btnExit.width(), 0 );
        add( btnExit );

        NinePatch panel = Chrome.get(Chrome.Type.BLANK);
        int pw = w - SCROLL_MARGIN * 2;
        int ph = h - 36 - BTN_HEIGHT - GAP;
        panel.size(pw, ph);
        panel.x = (w - pw) / 2f;
        panel.y = title.bottom() + 5;
        align(panel);
        add(panel);

        ScrollPane list = new ScrollPane(new Component()) {
            @Override
            public void onClick(float x, float y) {
                for (StoryCard card : cards) {
                    if (card.inside(x, y)) {
                        card.onClick();
                        break;
                    }
                }
            }
        };
        add(list);

        Component content = list.content();
        content.clear();

        float posY = 0;
        boolean secondColumn = false;
        int columns = landscape ? 2 : 1;

        List<YuanTaStory> stories = YuanTaStoryManager.getUnlockedStories();
        noStoriesUnlocked = stories.isEmpty();

        if (noStoriesUnlocked) {
            StoryEmptyInfo emptyInfo = new StoryEmptyInfo();
            emptyInfo.setRect(0, posY, panel.innerWidth(), 0);
            content.add(emptyInfo);
            posY = emptyInfo.bottom() + GAP;
        } else {
            Collections.sort(stories, (s1, s2) -> {
                if (s1.isTop && !s2.isTop) return -1;
                if (!s1.isTop && s2.isTop) return 1;
                return Long.compare(s2.unlockTime, s1.unlockTime);
            });

            for (YuanTaStory story : stories) {
                StoryCard card = new StoryCard(story);
                cards.add(card);

                if (columns == 1) {
                    card.setRect(0, posY, panel.innerWidth(), BTN_HEIGHT);
                    posY = card.bottom() + GAP;
                } else {
                    if (!secondColumn) {
                        card.setRect(0, posY, panel.innerWidth()/2f - GAP/2f, BTN_HEIGHT);
                        secondColumn = true;
                    } else {
                        card.setRect(panel.innerWidth()/2f + GAP/2f, posY, panel.innerWidth()/2f - GAP/2f, BTN_HEIGHT);
                        secondColumn = false;
                        posY = card.bottom() + GAP;
                    }
                }
                content.add(card);
            }
        }

        content.setSize(panel.innerWidth(), (int) Math.ceil(posY));
        list.setRect(panel.x, panel.y, w, panel.height());

        StyledButton btnSite = new StyledButton(Chrome.Type.GREY_BUTTON_TR, Messages.get(this, "read_more")){
            @Override
            protected void onClick() {
                super.onClick();
                ShatteredPixelDungeon.scene().add(new WndTitledMessage(new Image(Icons.get(Icons.CATALOG)),Messages.get(this,"morestory"),Messages.get(this,"morestory_desc")));
            }
        };
        btnSite.icon(Icons.get(Icons.NEWS));
        btnSite.textColor(Window.TITLE_COLOR);
        btnSite.setRect(panel.x, Camera.main.height-35,panel.width(), BTN_HEIGHT);
        add(btnSite);

        fadeIn();
    }

    @Override
    protected void onBackPressed() {
        ShatteredPixelDungeon.switchNoFade(GameScene.class);
    }

    // ====================== 剧情卡片（核心修复：用原生ItemSprite） ======================
    public static class StoryCard extends StyledButton {
        private final YuanTaStory story;
        private ItemSprite icon;

        public StoryCard(YuanTaStory story) {
            super(Chrome.Type.GREY_BUTTON_TR, getLangText(story.title), 6);
            this.story = story;

            icon = new ItemSprite();
            icon.view(story.iconIdx,null);
            add(icon);
        }

        static String getLangText(String key){
            if (key == null) return "???";
            String pureKey = key.replace("Ms:", "");
            return Messages.get(YuanTaStoneScene.class, pureKey);
        }

        @Override
        protected void layout() {
            super.layout();
            // 图标布局
            icon.x = x + bg.marginLeft() + 2;
            icon.y = y + (height - icon.height()) / 2f;
            align(icon);

            text.setPos(text.x + 18, text.y);
        }

        @Override
        protected void onClick() {
            super.onClick();
            textColor(Window.WHITE);
            if (story.unlockTime > SPDSettings.yuantuoLastRead()) {
                SPDSettings.yuantuoLastRead(story.unlockTime);
            }
            ShatteredPixelDungeon.scene().add(new WndStoryDetail(story));
        }
    }

    // ====================== 无剧情提示（最终无报错版：整体居中+文本居中） ======================
    private static class StoryEmptyInfo extends Component {
        private final NinePatch bg;
        private final RenderedTextBlock text;

        public StoryEmptyInfo() {
            bg = Chrome.get(Chrome.Type.GREY_BUTTON_TR);
            add(bg);

            text = renderTextBlock(Messages.get(YuanTaStoneScene.class, "no_stories"), 9);
            text.hardlight(0xffffff);
            add(text);
        }

        @Override
        protected void layout() {
            // 计算自身大小（原生写法）
            text.maxWidth((int)width() - bg.marginHor());
            height = text.bottom() - y + bg.marginBottom() + 1;
            bg.size(width(), height()+50);
            bg.y = 70;

            // 文本在卡片内部居中
            text.setPos(
                    x + (width() - text.width()) / 2f,
                    y + 100
            );
            align(text);
        }
    }

    // ====================== 详情弹窗（多语言修复） ======================
    private static class WndStoryDetail extends WndTitledMessage {
        public WndStoryDetail(YuanTaStory story) {
            super(new ItemSprite(story.iconIdx, null),
                    getLangText(story.title),
                    getLangText(story.content));
        }
    }

    // ====================== 剧情实体 ======================
    public static class YuanTaStory {
        public String id;
        public String title;
        public String content;
        public long unlockTime;
        public boolean isTop;
        public int iconIdx;

        public YuanTaStory(String id, String title, String content, boolean isTop, int iconIdx) {
            this.id = id;
            this.title = title;
            this.content = content;
            this.unlockTime = System.currentTimeMillis();
            this.isTop = isTop;
            this.iconIdx = iconIdx;
        }
    }

    // ====================== 剧情管理器（兼容原生Bundle） ======================
    public static class YuanTaStoryManager {
        private static final String FILE_NAME = "yuantuo_stories.dat";
        private static List<YuanTaStory> allStories;

        static {
            loadStories();
        }

        public static List<YuanTaStory> getUnlockedStories() {
            return new ArrayList<>(allStories);
        }

        private static final SimpleDateFormat STORY_DATE_FORMAT = new SimpleDateFormat("MM/dd", Locale.ENGLISH);
        public static String formatTime(long time) {
            return STORY_DATE_FORMAT.format(new Date(time));
        }

        private static void loadStories() {
            try {
                Bundle bundle = FileUtils.bundleFromFile(FILE_NAME);
                allStories = new ArrayList<>();

                if (!bundle.contains("story_count")) return;

                int count = bundle.getInt("story_count");
                for (int i = 0; i < count; i++) {
                    Bundle b = bundle.getBundle("story_" + i);
                    if (b == null) continue;

                    int iconIdx = b.getInt("iconIdx");
                    if (iconIdx == 0) iconIdx = ItemSpriteSheet.YELLOWBOOKS;

                    YuanTaStory story = new YuanTaStory(
                            b.getString("id"),
                            b.getString("title"),
                            b.getString("content"),
                            b.getBoolean("isTop"),
                            iconIdx
                    );
                    story.unlockTime = b.getLong("time");
                    allStories.add(story);
                }
            } catch (Exception e) {
                allStories = new ArrayList<>();
            }
        }

        private static void saveStories() {
            try {
                Bundle bundle = new Bundle();
                bundle.put("story_count", allStories.size());

                for (int i = 0; i < allStories.size(); i++) {
                    YuanTaStory s = allStories.get(i);
                    Bundle b = new Bundle();
                    b.put("id", s.id);
                    b.put("title", s.title);
                    b.put("content", s.content);
                    b.put("time", s.unlockTime);
                    b.put("isTop", s.isTop);
                    b.put("iconIdx", s.iconIdx);
                    bundle.put("story_" + i, b);
                }

                FileUtils.bundleToFile(FILE_NAME, bundle);
            } catch (Exception ignored) {}
        }

        public static void unlockStory(String storyId, String title, String content, int iconIdx) {
            // 第一步：检查是否已解锁
            for (YuanTaStory s : allStories) {
                if (s.id.equals(storyId)){
                    return;
                }
            }
            GLog.b(Messages.get(YuanTaStoneScene.class,"time_record",getLangText(title)));
            allStories.add(new YuanTaStory(storyId, title, content, false, iconIdx));
            saveStories();
        }
    }
}