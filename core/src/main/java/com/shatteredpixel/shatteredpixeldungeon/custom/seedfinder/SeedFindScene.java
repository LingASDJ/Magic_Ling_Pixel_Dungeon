package com.shatteredpixel.shatteredpixeldungeon.custom.seedfinder;

import com.shatteredpixel.shatteredpixeldungeon.Chrome;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.GamesInProgress;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.Constants;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.Artifact;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.Ring;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.journal.Catalog;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.SeedFinderScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.Archs;
import com.shatteredpixel.shatteredpixeldungeon.ui.ExitButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.IconButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.OptionSlider;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.ScrollPane;
import com.shatteredpixel.shatteredpixeldungeon.ui.ScrollingGridPane;
import com.shatteredpixel.shatteredpixeldungeon.ui.StyledButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.utils.DungeonSeed;
import com.shatteredpixel.shatteredpixeldungeon.windows.IconTitle;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndChallenges;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndJournal;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndMessage;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndTabbed;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndTextInput;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.PointerArea;
import com.watabou.noosa.ui.Component;
import com.watabou.utils.Reflection;

import java.util.ArrayList;

public class SeedFindScene extends PixelScene {
    public static String seedCode = "";
    private static final ArrayList<Item> wantedItems = new ArrayList<>();
    public HeroClass currentHero = HeroClass.WARRIOR;
    public static int currentFloor = 24;
    public void create() {
        super.create();

        int w = Camera.main.width;
        int h = Camera.main.height;

        Archs archs = new Archs();
        archs.setSize(w, h);
        add(archs);

        INSTANCE = this;

        // 预热：必须无条件初始化一次。Scroll/Potion/Ring 的 ItemStatusHandler
        // 都在 Dungeon.init() 内创建，否则网格里 identify() 未鉴定物品会 NPE。
        // 默认角色为战士，直接用它完成初始化。
        GamesInProgress.selectedClass = currentHero;
        SPDSettings.customSeed("");
        Dungeon.init();
        // 加载徽章/密码徽章，供 NewSeedFinder.isLocked() 判定条件生成物品
        com.shatteredpixel.shatteredpixeldungeon.Badges.loadGlobal();
        com.shatteredpixel.shatteredpixeldungeon.PaswordBadges.loadGlobal();

        addToFront(mainWindow = new WndFinder());

        buildSearchView();

        addCopySeedButton();
        addExitButton();
    }

    // ======================== 搜索结果视图（scene 级，盖住 WndFinder） ========================
    private RenderedTextBlock currentSeedText;
    private RenderedTextBlock threadInfoText;
    private ScrollPane resultScroll;
    private Component resultContent;
    private boolean searchViewVisible = false;
    private float seedDisplayCooldown = 0f;

    private void buildSearchView() {
        currentSeedText = PixelScene.renderTextBlock("", 6);
        currentSeedText.hardlight(0xFFFFFF);
        currentSeedText.visible = false;
        add(currentSeedText);

        threadInfoText = PixelScene.renderTextBlock("", 5);
        threadInfoText.hardlight(0xAAAAAA);
        threadInfoText.visible = false;
        add(threadInfoText);

        resultContent = new Component();
        resultScroll = new ScrollPane(resultContent);
        resultScroll.visible = false;
        resultScroll.active = false;
        add(resultScroll);
    }

    // 关闭 Tab（隐藏 WndFinder），显示搜索视图（全屏，与 GitHub 版一致）
    private void showSearchView() {
        searchViewVisible = true;
        mainWindow.visible = false;
        mainWindow.active = false;

        float screenW = Camera.main.width;
        float screenH = Camera.main.height;
        float contentW = Math.min(120, screenW);

        float cx = (screenW - contentW) / 2f;

        currentSeedText.text(Messages.get(SeedFindScene.class, "searching"));
        currentSeedText.setRect(cx, 12, contentW, 0);
        currentSeedText.visible = true;

        // 初始化线程信息（空文本，由 update() 节流填充）；
        // 强力查种总开关关闭或线程数为 1 时直接隐藏，避免残留
        threadInfoText.text("");
        threadInfoText.setRect(cx, currentSeedText.bottom() + 1, contentW, 0);
        threadInfoText.visible = SPDSettings.PlusSearch() && SPDSettings.PlusThread() > 1;

        if (btnCopy != null)
            btnCopy.enable(btnCopy.visible = false);

        resultContent.clear();
        resultContent.setSize(contentW, 0);
        float top = threadInfoText.bottom() + 2;
        resultScroll.setRect(cx, top, contentW, screenH - top);
        resultScroll.visible = true;
        resultScroll.active = true;
        resultScroll.scrollTo(0, 0);
    }

    // 显示查找结果（GitHub 版 CreditsBlock 风格）
    private void showSearchResult(String body) {
        if (!searchViewVisible) return;
        // 查询结束，清掉“正在查找……”状态文本与线程信息
        currentSeedText.text("");
        currentSeedText.visible = false;
        threadInfoText.text("");
        threadInfoText.visible = false;
        currentSeedValue = -1;

        btnCopy.enable(true);
        btnCopy.visible = true;

        resultContent.clear();
        CreditsBlock txt = new CreditsBlock(true, Window.TITLE_COLOR, body);
        resultContent.add(txt);
        float contentW = Math.min(120, (float) Camera.main.width);
        txt.setRect(0, 0, contentW, 0);
        resultContent.setSize(contentW, txt.bottom() + 4);
        resultScroll.scrollTo(0, 0);
    }
    RedButton btnCopy;
    private void addCopySeedButton() {
        btnCopy = new RedButton("") {
            @Override
            protected void onClick() {
                if (SeedFindScene.seedCode != null && !SeedFindScene.seedCode.isEmpty()) {
                    addToFront(new WndOptions(new Image(new ItemSprite(ItemSpriteSheet.SEED_HOLDER)),
                            Messages.get(SeedFindScene.class, "copy_title"),
                            Messages.get(SeedFindScene.class, "copy_body"),
                            Messages.get(SeedFindScene.class, "copy_yes"),
                            Messages.get(SeedFindScene.class, "copy_no")) {
                        @Override
                        protected void onSelect(int index) {
                            if (index == 0) SPDSettings.customSeed(SeedFindScene.seedCode);
                        }
                    });
                }
            }
        };
        btnCopy.icon(Icons.RENAME_ON.get());
        add(btnCopy);
        btnCopy.setRect(0, 0, 20, 20);
        btnCopy.enable(btnCopy.visible = false);
    }
    private void addExitButton() {
        ExitButton exitBtn = new ExitButton() {
            @Override
            public void onClick() {
                stopSearch();
                ShatteredPixelDungeon.switchNoFade(SeedFinderScene.class);
            }
        };
        exitBtn.setPos((float) Camera.main.width - exitBtn.width(), 0);
        add(exitBtn);
    }

    @Override
    protected void onBackPressed() {
        stopSearch();
        ShatteredPixelDungeon.switchNoFade(SeedFinderScene.class);
    }

    // 挑战文本
    private static String challengeText() {
        return challengeText(SPDSettings.challenges());
    }
    private static String challengeText(int challenges) {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (int i = 0; i < com.shatteredpixel.shatteredpixeldungeon.Challenges.NAME_IDS.length; i++) {
            if ((challenges & com.shatteredpixel.shatteredpixeldungeon.Challenges.MASKS[i]) != 0) {
                if (!first) sb.append(Messages.get(SeedFindScene.class, "separator"));
                sb.append(Messages.titleCase(Messages.get(com.shatteredpixel.shatteredpixeldungeon.Challenges.class,
                        com.shatteredpixel.shatteredpixeldungeon.Challenges.NAME_IDS[i])));
                first = false;
            }
        }
        if (first) sb.append(Messages.get(SeedFindScene.class, "none"));
        return sb.toString();
    }
    private volatile long currentSeedValue = -1;
    private volatile boolean stopThread = false;
    // 后台线程只写这个值，渲染线程每 250ms 聚合刷新一次，避免逐种子 post 事件
    private long lastShownSeed = -1;

    private static final int BTN_H = 16;

    // ======================== 新窗口 ========================
    private final class WndFinder extends WndTabbed {
        private final int winW;
        private final int winH;
        private final Component[] pages = new Component[5];
        private final Tab[] tabs = new Tab[5];
        private RedButton testBtn;
        private RenderedTextBlock page1Info;
        private StyledButton floorBtn;
        private IconButton challengeBtn;
        private int lastChallenges = -1;
        private final int HERO_BTN_W = 18;
        private final int HERO_BTN_H = 16;

        WndFinder() {
            super();

            winW = PixelScene.landscape() ? WndJournal.WIDTH_L : WndJournal.WIDTH_P;
            winH = PixelScene.landscape() ? WndJournal.HEIGHT_L : WndJournal.HEIGHT_P;

            // 含 ScrollPane 的窗口必须最先 resize
            resize(winW, winH);

            // ---- 五个页签，initPage 内部同时添加页和 Tab ----
            initPage(0, settingPage(),       Icons.get(Icons.PREFS));
            initPage(1, equipmentPane(),     new ItemSprite(ItemSpriteSheet.WEAPON_HOLDER));
            initPage(2, stackablePage(),     new ItemSprite(ItemSpriteSheet.POTION_HOLDER));
            initPage(3, listPage(),          Icons.get(Icons.BACKPACK));
            initPage(4, startFindingPane(),  Icons.get(Icons.TARGET));

            layoutTabs();
            // 初始只显示第一页（select(0) 仅对 tab[0] 调 select(true)，其余需手动隐藏）
            for (int i = 1; i < pages.length; i++) {
                pages[i].visible = pages[i].active = false;
            }
            select(0);
        }

        private void initPage(final int index, Component page, Image tabIcon) {
            pages[index] = page;
            add(page);
            page.setRect(0, 0, winW, winH);
            Tab tab = new IconTab(tabIcon) {
                @Override protected void select(boolean value) {
                    super.select(value);
                    pages[index].active = pages[index].visible = value;
                }
            };
            tabs[index] = tab;
            add(tab);
        }
        @Override
        public void update() {
            super.update();
            boolean heroReady = currentHero != null;
            tabs[1].active = heroReady;
            tabs[2].active = heroReady;
            tabs[3].active = heroReady;
            tabs[4].active = heroReady;
            if (testBtn != null) testBtn.enable(heroReady);

            // 轮询挑战值：挑战窗一关（无论点外部、Esc 还是按钮），
            // 立刻更新按钮图标并同步本页"当前设置"文本。
            int challenges = SPDSettings.challenges();
            if (challenges != lastChallenges) {
                lastChallenges = challenges;
                if (challengeBtn != null)
                    challengeBtn.icon(Icons.get(challenges > 0 ? Icons.CHALLENGE_ON : Icons.CHALLENGE_OFF));
                refreshPage1Info();
            }
        }

        //第一页设置
        private Component settingPage() {
            Component root = new Component();
            float w = winW - 4;

            RenderedTextBlock title = PixelScene.renderTextBlock(Messages.get(SeedFindScene.class, "current_settings"), 9);
            title.hardlight(TITLE_COLOR);
            root.add(title);
            title.setPos(2, 1);

            // 英雄选择行（原独立设置窗口内容，直接搬入主页）
            float spacing = (w - HeroClass.values().length * HERO_BTN_W) /
                    (HeroClass.values().length + 1f);
            float curX = 2 + spacing;
            float heroY = title.bottom() + 4;
            for (HeroClass cl : HeroClass.values()) {
                HeroBtn button = new HeroBtn(cl);
                root.add(button);
                button.setRect(curX, heroY, HERO_BTN_W, HERO_BTN_H);
                curX += HERO_BTN_W + spacing;
            }

            float rowY = heroY + HERO_BTN_H + 5;

            floorBtn = new StyledButton(Chrome.Type.GREY_BUTTON_TR,
                    Messages.get(SeedFindScene.class, "floor_info", currentFloor), 8) {
                @Override
                protected void onClick() {
                    ShatteredPixelDungeon.scene().addToFront(new WndSelectLevel());
                }
            };
            floorBtn.icon(new ItemSprite(ItemSpriteSheet.SEED_SUNGRASS));
            root.add(floorBtn);
            floorBtn.setRect(2, rowY, w / 2f - 3, 16);

            challengeBtn = new IconButton(
                    Icons.get(SPDSettings.challenges() > 0 ? Icons.CHALLENGE_ON : Icons.CHALLENGE_OFF)) {
                @Override
                protected void onClick() {
                    // 必须 editable=true：否则挑战勾选框 active=false，完全无法勾选。
                    // 关闭后的图标/文本刷新统一由本窗 update() 轮询处理。
                    ShatteredPixelDungeon.scene().addToFront(
                            new WndChallenges(SPDSettings.challenges(), true, null));
                }
            };
            root.add(challengeBtn);
            challengeBtn.setRect(w / 2f + 3, rowY, 16, 16);

            page1Info = PixelScene.renderTextBlock("", 6);
            root.add(page1Info);
            page1Info.setRect(2, rowY + 20, w, 24);
            refreshPage1Info();

            testBtn = new RedButton(Messages.get(SeedFindScene.class, "btn_test"), 8) {
                @Override
                protected void onClick() {
                    ShatteredPixelDungeon.scene().addToFront(
                            new WndTextInput(
                                    Messages.get(SeedFindScene.class, "input_title"),
                                    Messages.get(SeedFindScene.class, "input_body"),
                                    SeedFindScene.seedCode,
                                    20,
                                    false,
                                    Messages.get(SeedFindScene.class, "confirm"),
                                    Messages.get(SeedFindScene.class, "cancel")
                            ) {
                                @Override
                                public void onSelect(boolean check, String text) {
                                    if(check) {
                                        seedCode = DungeonSeed.formatText(text);
                                        startSearch();
                                    }
                                }
                            }
                    );
                }
            };
            testBtn.icon(new ItemSprite(ItemSpriteSheet.SEED_SUNGRASS));
            root.add(testBtn);
            testBtn.setRect(2, rowY + 48, w, 16);

            return root;
        }
        private void refreshPage1Info() {
            if (page1Info == null) return;
            page1Info.text(
                    Messages.get(SeedFindScene.class, "hero_info",
                            Messages.capitalize(currentHero.title())) + "\n"
                            + Messages.get(SeedFindScene.class, "floor_info", currentFloor) + "\n"
                            + Messages.get(SeedFindScene.class, "challenges_info", challengeText()), winW);
            // 挑战全选时文本会折行变高，测试按钮跟随文本底部，避免遮挡
            if (testBtn != null)
                testBtn.setRect(2, page1Info.bottom() + 4, winW - 4, 16);
        }

        // ======================== 英雄选择按钮 ========================
        private final class HeroBtn extends IconButton {

            private final HeroClass cl;
            private final Image hero;

            HeroBtn(HeroClass cl) {
                super();
                this.cl = cl;
                add(hero = new Image(cl.spritesheet(), 0, 90, 12, 15));
            }

            @Override
            protected void layout() {
                super.layout();
                hero.x = x + (width - hero.width()) / 2f;
                hero.y = y + (height - hero.height()) / 2f;
                PixelScene.align(hero);
            }

            @Override
            public void update() {
                super.update();
                if (cl != GamesInProgress.selectedClass) {
                    hero.brightness(cl.isUnlocked() ? 0.6f : 0.3f);
                } else {
                    hero.brightness(1f);
                }
            }

            @Override
            protected void onClick() {
                super.onClick();
                if (!cl.isUnlocked()) {
                    ShatteredPixelDungeon.scene().addToFront(new WndMessage(cl.unlockMsg()));
                } else {
                    GamesInProgress.selectedClass = cl;
                    currentHero = cl;
                    refreshPage1Info();
                }
            }
        }

        // ======================== 楼层选择 ========================
        private final class WndSelectLevel extends Window {
            private static final int PICKER_W = 120;
            private static final int GAP = 2;
            private static final int BTN_SIZE = 16;
            private static final int PANE_MAX_HEIGHT = 96;

            private int selectedFloor = currentFloor;

            private RedButton confirm;

            WndSelectLevel() {
                super();
                ScrollPane sp = new ScrollPane(new Component());
                add(sp);

                confirm = new RedButton(Messages.get(SeedFindScene.class, "confirm_floor", selectedFloor)) {
                    @Override
                    protected void onClick() {
                        currentFloor = selectedFloor;
                        // 立刻刷新主页楼层按钮文字与"当前设置"文本
                        floorBtn.text(Messages.get(SeedFindScene.class, "floor_info", currentFloor));
                        refreshPage1Info();
                        hide();
                    }
                };
                add(confirm);

                Component content = sp.content();
                float xPos = (PICKER_W - 5 * BTN_SIZE - GAP * 8) / 2f;
                float each = GAP * 2 + BTN_SIZE;
                for (int i = 0; i < Constants.MAX_DEPTH; ++i) {
                    StyledButton btn = levelBtn(i);
                    btn.setRect(xPos + (i % 5) * each, (i / 5) * each, BTN_SIZE, BTN_SIZE);
                    PixelScene.align(btn);
                    content.add(btn);
                }

                int rows = (Constants.MAX_DEPTH - 1) / 5 + 1;
                float contentHeight = rows * each - GAP * 2;
                content.setSize(PICKER_W, contentHeight);
                sp.setRect(0, 0, PICKER_W, contentHeight);
                confirm.setRect(0, PANE_MAX_HEIGHT + GAP * 2, PICKER_W, BTN_SIZE);
                resize(PICKER_W, (int) confirm.bottom());
                sp.setRect(0, 0, PICKER_W, PANE_MAX_HEIGHT);
                sp.scrollTo(0, 0);
            }

            private StyledButton levelBtn(int i) {
                // 魔绫地牢楼层从0层开始，按钮直接显示深度值
                return new StyledButton(Chrome.Type.GEM, String.valueOf(i), 8) {
                    {
                        hotArea.blockLevel = PointerArea.NEVER_BLOCK;
                    }
                    @Override
                    protected void onClick() {
                        selectedFloor = i;
                        confirm.text(Messages.get(SeedFindScene.class, "confirm_floor", selectedFloor));
                    }
                };
            }
        }
        //第二页装备
        @SuppressWarnings("unchecked")
        private Component equipmentPane() {
            ArrayList<ItemGroup> groups = new ArrayList<>();

            // 2-6阶近战武器
            Generator.Category category;
            ItemGroup g;
            for (int t = 1; t < Generator.wepTiers.length; t++) {
                category = Generator.wepTiers[t];
                g = new ItemGroup("T" + (t + 1) + " " + Catalog.MELEE_WEAPONS.title());
                for (int i = 0; i < category.classes.length; i++)
                    if (category.probs[i] >= 0f && !NewSeedFinder.isUngenerated(category.classes[i]))
                        g.items.add((Class<? extends Item>) category.classes[i]);
                if (!g.items.isEmpty())
                    groups.add(g);
            }

            // 2-5阶护甲
            category = Generator.Category.ARMOR;
            g = new ItemGroup(Catalog.ARMOR.title());
            for (int i = 0; i < category.classes.length; i++)
                if (category.probs[i] >= 0f)
                    g.items.add((Class<? extends Item>) category.classes[i]);
            groups.add(g);

            // wands
            category = Generator.Category.WAND;
            g = new ItemGroup(Catalog.WANDS.title());
            for (int i = 0; i < category.classes.length; i++)
                if (category.probs[i] >= 0f)
                    g.items.add((Class<? extends Item>) category.classes[i]);
            if (!g.items.isEmpty())
                groups.add(g);

            // rings
            category = Generator.Category.RING;
            g = new ItemGroup(Catalog.RINGS.title());
            for (int i = 0; i < category.classes.length; i++)
                if (category.probs[i] >= 0f)
                    g.items.add((Class<? extends Item>) category.classes[i]);
            if (!g.items.isEmpty())
                groups.add(g);

            // artifacts
            category = Generator.Category.ARTIFACT;
            g = new ItemGroup(Catalog.ARTIFACTS.title());
            for (int i = 0; i < category.classes.length; i++)
                if (category.defaultProbs[i] >= 0f && !NewSeedFinder.isUngenerated(category.classes[i]))
                    g.items.add((Class<? extends Item>) category.classes[i]);
            if (!g.items.isEmpty())
                groups.add(g);

            ScrollingGridPane grid = new ScrollingGridPane();
            for (ItemGroup gr : groups) {
                grid.addHeader(gr.header);
                for (Class<? extends Item> cl : gr.items)
                    grid.addItem(new PickGridItem(cl));
            }
            grid.scrollTo(0, 0);
            return grid;
        }
        //第三页消耗品
        @SuppressWarnings("unchecked")
        private Component stackablePage() {
            ArrayList<ItemGroup> groups = new ArrayList<>();
            Generator.Category category;
            ItemGroup g;

            category = Generator.Category.POTION;
            g = new ItemGroup(Catalog.POTIONS.title());
            for (int i = 0; i < category.classes.length; i++)
                if (category.probs[i] >= 0f)
                    g.items.add((Class<? extends Item>) category.classes[i]);
            groups.add(g);

            category = Generator.Category.SCROLL;
            g = new ItemGroup(Catalog.SCROLLS.title());
            for (int i = 0; i < category.classes.length; i++)
                if (category.probs[i] >= 0f)
                    g.items.add((Class<? extends Item>) category.classes[i]);
            groups.add(g);

            category = Generator.Category.STONE;
            g = new ItemGroup(Catalog.STONES.title());
            for (int i = 0; i < category.classes.length; i++)
                if (category.probs[i] >= 0f)
                    g.items.add((Class<? extends Item>) category.classes[i]);
            groups.add(g);

            category = Generator.Category.FOOD;
            g = new ItemGroup(Catalog.FOOD.title());
            for (int i = 0; i < category.classes.length; i++)
                if (category.probs[i] >= 0f)
                    g.items.add((Class<? extends Item>) category.classes[i]);
            groups.add(g);

            ScrollingGridPane grid = new ScrollingGridPane();
            for (ItemGroup gr : groups) {
                grid.addHeader(gr.header);
                for (Class<? extends Item> cl : gr.items)
                    grid.addItem(new PickGridItem(cl));
            }
            grid.scrollTo(0, 0);
            return grid;
        }
        //第四页物品清单
        private Component listPage() {
            return new listPageWrapper();
        }
        private class listPageWrapper extends Component {
            listPane pane;
            RedButton removeAllBtn;
            listPageWrapper() {
                super();
                pane = new listPane();
                add(pane);
                removeAllBtn = new RedButton(Messages.get(SeedFindScene.class, "remove_all")) {
                    @Override
                    protected void onClick() {
                        wantedItems.clear();
                        pane.lastCount = -1;
                    }
                };
                add(removeAllBtn);
            }
            @Override
            protected void layout() {
                // 下方常驻"移除全部"按钮(24px)，上方为滚动列表
                pane.setRect(0, 0, width, Math.max(0, height - 24));
                removeAllBtn.setRect(0, Math.max(0, height - 22), width, 20);
            }
        }
        private class listPane extends ScrollPane {
            int lastCount = -1;
            ArrayList<WantedItemButton> buttons = new ArrayList<>();
            public listPane() {
                super(new Component());
            }
            @Override
            public void update() {
                super.update();
                int count = wantedItems.size();
                if (count != lastCount) {
                    resetButton();
                    lastCount = count;
                }
            }
            void resetButton() {
                for (WantedItemButton button : buttons)
                    button.destroy();
                content.clear();
                buttons.clear();

                float pos = 0;
                for (Item item : wantedItems.toArray(new Item[0])) {
                    String[] actions = item.quantity() > 1
                            ? new String[]{Messages.get(SeedFindScene.class, "remove_one"),
                                    Messages.get(SeedFindScene.class, "remove_all"),
                                    Messages.get(SeedFindScene.class, "cancel")}
                            : new String[]{Messages.get(SeedFindScene.class, "remove"),
                                    Messages.get(SeedFindScene.class, "cancel")};
                    WantedItemButton button = new WantedItemButton(item);
                    button.setRect( 0, pos, width, 20 );
                    button.setClickAction(() -> ShatteredPixelDungeon.scene().addToFront(
                            new WndOptions(item.toString(), item.desc(), actions) {
                                @Override
                                public void onSelect(int index) {
                                    if (item.quantity() == 1)
                                        index++;

                                    if (index == 0) {
                                        item.quantity(item.quantity() - 1);
                                        lastCount = -1;
                                        hide();
                                    }
                                    else if (index == 1) {
                                        wantedItems.remove(item);
                                        lastCount = -1;
                                        hide();
                                    }
                                    else if (index == 2)
                                        hide();
                                }
                            }));
                    content.add(button);
                    buttons.add(button);
                    pos += button.height() + 1;
                }
                content.setSize(width, pos - 1);
            }
        }
        //第五页文字清单+开始查询
        private Component startFindingPane() {
            return new summaryPane();
        }
        private class summaryPane extends ScrollPane {
            int lastCount = -1;
            int lastFloor = -1;
            int lastChallenges = -1;
            int lastThreads = -1;
            HeroClass lastHero = null;
            RedButton startBtn;
            public summaryPane() {
                super(new Component());
            }
            @Override
            public void update() {
                super.update();
                // 物品数量、楼层、挑战、角色、线程数任一变化都要重建，否则摘要显示旧值
                int count = wantedItems.size();
                int challenges = SPDSettings.challenges();
                // 强力查种关闭时按 0 处理：触发重建且不再显示线程数行
                int threads = SPDSettings.PlusSearch()
                        ? Math.max(1, Math.min(SeedFinder.parallelSeeds.length(), SPDSettings.PlusThread()))
                        : 0;
                if (count != lastCount || currentFloor != lastFloor
                        || challenges != lastChallenges || currentHero != lastHero
                        || threads != lastThreads) {
                    resetButton();
                    lastCount = count;
                    lastFloor = currentFloor;
                    lastChallenges = challenges;
                    lastHero = currentHero;
                    lastThreads = threads;
                }
            }
            void resetButton() {
                Component content = this.content();
                content.clear();
                float w = width;

                StringBuilder sb = new StringBuilder();
                sb.append(Messages.get(SeedFindScene.class, "hero_info",
                        Messages.capitalize(currentHero.title()))).append("\n");
                sb.append(Messages.get(SeedFindScene.class, "challenges_info",
                        challengeText())).append("\n");
                sb.append(Messages.get(SeedFindScene.class, "floor_info",
                        currentFloor)).append("\n");
                // 仅强力查种开启时显示线程数行，关掉后不留残留
                if (SPDSettings.PlusSearch()) {
                    sb.append(Messages.get(SeedFindScene.class, "threads_info",
                            Math.max(1, Math.min(SeedFinder.parallelSeeds.length(), SPDSettings.PlusThread())))).append("\n");
                }
                sb.append(Messages.get(SeedFindScene.class, "requirements",
                        wantedItems.size())).append("\n");
                for (int i = 0; i < wantedItems.size(); i++) {
                    sb.append(i + 1).append(". ").append(wantedItems.get(i).name()).append("\n");
                }

                RenderedTextBlock summary = PixelScene.renderTextBlock("", 6);
                summary.text(sb.toString(), (int) w);
                content.add(summary);
                summary.setRect(1, 1, w - 2, 0);

                startBtn = new RedButton(wantedItems.isEmpty()
                        ? Messages.get(SeedFindScene.class, "start_empty")
                        : Messages.get(SeedFindScene.class, "start")) {
                    @Override
                    protected void onClick() {
                        // 未选择物品时不再静默锁定，弹窗提示
                        if (wantedItems.isEmpty()) {
                            ShatteredPixelDungeon.scene().addToFront(new WndMessage(
                                    Messages.get(SeedFindScene.class, "no_items")));
                            return;
                        }
                        startSearch();
                    }
                };
                content.add(startBtn);
                startBtn.setRect(1, summary.bottom() + 4, w - 2, 18);

                content.setSize(w, startBtn.bottom() + 2);
            }
        }
        @Override
        public void onBackPressed() {

        }
    }

    // 清单页物品行：图标 + 名称（替代旧版 WndRanking.canScrollItemButton）
    private static final class WantedItemButton extends StyledButton {
        private Runnable clickAction;

        WantedItemButton(Item item) {
            super(Chrome.Type.GREY_BUTTON_TR, item.toString(), 7);
            icon(new ItemSprite(item));
        }

        void setClickAction(Runnable action) {
            clickAction = action;
        }

        @Override
        protected void onClick() {
            if (clickAction != null) clickAction.run();
        }
    }

    private static final class PickGridItem extends ScrollingGridPane.GridItem {
        private final Class<? extends Item> cls;
        private final boolean locked;
        private volatile boolean tinted = false;
        PickGridItem(Class<? extends Item> cls) {
            super(image(cls));
            this.cls = cls;
            // 条件生成物品（如黄金长枪未购买）：网格中保留显示但置灰不可选
            locked = NewSeedFinder.isLocked(cls);
            if (locked)
                icon.brightness(0.3f);
            if (Artifact.class.isAssignableFrom(cls))
                for (Item item : wantedItems)
                    if (cls.isInstance(item)) {
                        tinted = true;
                        break;
                    }
        }
        @Override
        public void update() {
            super.update();
            if (locked)
                hardLightBG(0.1f, 0.1f, 0.1f);
            else if (tinted)
                hardLightBG(0.25f, 0.7f, 0.3f);
            else
                bg.resetColor();
        }

        @Override
        public boolean onClick(float x, float y) {
            // 必须先做命中检测：Pane 会遍历所有格子调用本方法，
            // 只有点击落在本格矩形内才响应，否则任意点击都会命中第一个格子。
            if (x < left() || x > right() || y < top() || y > bottom()) {
                return false;
            }
            if (locked) {
                ShatteredPixelDungeon.scene().addToFront(new WndMessage(
                        Messages.get(SeedFindScene.class, "locked_hint")));
                return true;
            }
            if (Artifact.class.isAssignableFrom(cls)) {
                if (tinted) {
                    for (Item item : wantedItems.toArray(new Item[0]))
                        if (cls.isInstance(item))
                            wantedItems.remove(item);
                }
                else
                    wantedItems.add(newInstance(cls));
                tinted = !tinted;
            }
            else
                ShatteredPixelDungeon.scene().addToFront(new ItemConfigWindow(cls));
            return true;
        }
        private final static class ItemConfigWindow extends Window {
            private static final int WIDTH = 120;
            private static final int GAP = 2;
            private static final int SLIDER_H = 22;
            private static final int AUG_MAX_LINES = 3; // 最多3行(8项/4每行=2行 + 当前1行)
            private Class<?>[][] pools;
            int augRare, augId;
            RenderedTextBlock augInfo;
            ItemConfigWindow(Class<? extends Item> cls) {
                super();
                Item item = newInstance(cls);
                int maxLevel = getMaxLevelForClass(cls);

                if (Weapon.class.isAssignableFrom(cls)) {
                    pools = enchant;
                } else if (Armor.class.isAssignableFrom(cls)) {
                    pools = glyph;
                }

                float pos = GAP;
                IconTitle title = new IconTitle(item);
                title.color(TITLE_COLOR);
                add(title);
                title.setRect(0, pos, WIDTH, 0);
                pos = title.bottom() + 2F;

                OptionSlider levelSlider = getSlider(maxLevel, item);
                add(levelSlider);
                levelSlider.setRect(0, pos, WIDTH, SLIDER_H);
                pos = levelSlider.bottom() + GAP;
                if (pools != null) {
                    augInfo = PixelScene.renderTextBlock("", 6);
                    updateAugText();
                    add(augInfo);
                    augInfo.setRect(0, pos, WIDTH, augInfo.height());
                    pos = augInfo.bottom() + GAP;

                    OptionSlider idSlider = new OptionSlider(
                            Messages.get(SeedFindScene.class, "enchant_or_glyph"), "1", "8", 0, 7) {
                        @Override
                        protected void onChange() {
                            augId = getSelectedValue();
                            updateAugText();
                        }
                    };
                    idSlider.setSelectedValue(0);
                    add(idSlider);
                    idSlider.setRect(0, pos, WIDTH, SLIDER_H);
                    pos = idSlider.bottom() + GAP;

                    OptionSlider rareSlider = new OptionSlider(
                            Messages.get(SeedFindScene.class, "rarity"),
                            Messages.get(SeedFindScene.class, "rarity_common"),
                            Messages.get(SeedFindScene.class, "rarity_cursed"), 0, 4) {
                        @Override
                        protected void onChange() {
                            augRare = getSelectedValue();
                            updateAugText();
                        }
                    };
                    rareSlider.setSelectedValue(0);
                    add(rareSlider);
                    rareSlider.setRect(0, pos, WIDTH, SLIDER_H);
                    pos = rareSlider.bottom() + GAP;
                }

                RedButton confirmBtn = new RedButton(Messages.get(SeedFindScene.class, "add")) {
                    @Override
                    protected void onClick() {
                        if (item.stackable) {
                            Item same = null;
                            for (Item i : wantedItems)
                                if (cls.isInstance(i))
                                    same = i;
                            if (same != null)
                                same.quantity(same.quantity() + item.quantity());
                            else
                                wantedItems.add(item);
                        }
                        else {
                            if (augRare > 0) {
                                Class<?>[] pool = pools[augRare - 1];
                                if (augId < pool.length) {
                                    Object aug = Reflection.newInstance(pool[augId]);
                                    if (aug instanceof Weapon.Enchantment)
                                        ((Weapon) item).enchant((Weapon.Enchantment) aug);
                                    else if (aug instanceof Armor.Glyph)
                                        ((Armor) item).inscribe((Armor.Glyph) aug);
                                }
                            }
                            wantedItems.add(item);
                        }
                        hide();
                    }
                };
                add(confirmBtn);
                confirmBtn.setRect(0, pos, WIDTH / 2f - 1, BTN_H);

                RedButton cancelBtn = new RedButton(Messages.get(SeedFindScene.class, "cancel")) {
                    @Override
                    protected void onClick() {
                        hide();
                    }
                };
                add(cancelBtn);
                cancelBtn.setRect(WIDTH / 2f + 1, pos, WIDTH / 2f - 1, BTN_H);
                pos = cancelBtn.bottom() + GAP;

                resize(WIDTH, (int) pos);

            }
            private OptionSlider getSlider(int maxLevel, Item item) {
                OptionSlider slider;
                if (maxLevel > 0) {
                    slider = new OptionSlider(Messages.get(SeedFindScene.class, "level"),
                            "0", "+" + maxLevel, 0, maxLevel) {
                        @Override
                        protected void onChange() {
                            item.level(getSelectedValue());
                        }
                    };
                slider.setSelectedValue(0);
                }
                else {
                    slider = new OptionSlider(Messages.get(SeedFindScene.class, "quantity"),
                            "1", "10", 1, 10) {
                        @Override
                        protected void onChange() {
                            int value = getSelectedValue();
                            if (value > 0)
                                item.quantity(value);
                        }
                    };
                slider.setSelectedValue(1);
            }
                return slider;
            }
            private String getAugName(Class<?> augClass) {
                return Messages.get(augClass, "name", "");
            }
            private void updateAugText() {
                StringBuilder info = new StringBuilder();
                int lines = 0;
                if (augRare == 0) {
                    info.append(Messages.get(SeedFindScene.class, "none"));
                    lines = 1;
                } else {
                    Class<?>[] pool = pools[augRare - 1];
                    for (int i = 0; i < pool.length; i++) {
                        info.append(i + 1).append(":").append(getAugName(pool[i])).append(" ");
                        if ((i + 1) % 4 == 0 || i == pool.length - 1) {
                            info.append("\n");
                            lines++;
                        }
                    }
                    if (augId < pool.length)
                        info.append(Messages.get(SeedFindScene.class, "current",
                                getAugName(pool[augId])));
                    else
                        info.append(Messages.get(SeedFindScene.class, "current_none"));
                    lines++;
                }
                // 填充到最大行数，保证高度恒定不变
                while (lines < AUG_MAX_LINES) {
                    info.append("\n");
                    lines++;
                }
                augInfo.text(info.toString(), WIDTH);
                // 触发 layout 重排文字并更新高度
                augInfo.setRect(augInfo.left(), augInfo.top(), augInfo.width(), augInfo.height());
            }
        }
    }

    static final Class<?>[][] enchant = {
            Weapon.Enchantment.common,
            Weapon.Enchantment.uncommon,
            Weapon.Enchantment.rare,
            Weapon.Enchantment.curses
    };
    static final Class<?>[][] glyph = {
            Armor.Glyph.common,
            Armor.Glyph.uncommon,
            Armor.Glyph.rare,
            Armor.Glyph.curses
    };
    private static Item newInstance(Class<? extends Item> cls) {
        Item item = Reflection.newInstance(cls);
        assert item != null;
        item.identify();
        return item;
    }
    private static Image image(Class<? extends Item> cls) {
        Item item = newInstance(cls);
        return new ItemSprite(item.image, item.glowing());
    }
    // ---- 物品分组（懒构建，全局复用） ----
    private static final class ItemGroup {
        final String header;
        final ArrayList<Class<? extends Item>> items = new ArrayList<>();

        ItemGroup(String header) {
            this.header = header;
        }
    }
    private static int getMaxLevelForClass(Class<?> cls) {
        if (Wand.class.isAssignableFrom(cls)) {
            // 已选 +3 任务配件，其余配件最多 +2（一局仅一根任务杖）
            return hasQuestLevel(Wand.class)
                    ? 2 : 3;
        }
        if (Ring.class.isAssignableFrom(cls)) {
            // 已选 +3（小恶魔任务奖励 +3/+4 的下限）瞄准镜，其余只能 +2
            return hasQuestLevel(Ring.class)
                    ? 2 : 4;
        }
        return (Weapon.class.isAssignableFrom(cls) || Armor.class.isAssignableFrom(cls))
                ? 3 : 0;
    }
    private static boolean hasQuestLevel(Class<?> type) {
        for (Item item : wantedItems)
            if (type.isInstance(item) && item.trueLevel() >= 3)
                return true;
        return false;
    }
    // ======================== update() / seed 显示节流 ========================
    @Override
    public void update() {
        super.update();
        // 查找结果（不节流——查找一结束就立刻展示）
        if (text != null && !text.isEmpty()) {
            String result = text;
            text = "";
            showSearchResult(result);
        }
        // 当前种子显示（节流到 4 次/秒，避免与查找线程争抢 CPU）
        seedDisplayCooldown += Game.elapsed;
        if (seedDisplayCooldown >= 0.25f) {
            seedDisplayCooldown = 0f;
            if (searchViewVisible && !stopThread) {
                int threads = SeedFinder.searchThreadCount;
                if (threads > 1) {
                    // 多线程：显示总线程数与每个线程的当前种子（9字母码）
                    StringBuilder sb = new StringBuilder();
                    sb.append(Messages.get(SeedFinder.class, "threads", threads)).append("\n");
                    for (int ts = 0; ts < threads; ts++) {
                        long s = SeedFinder.parallelSeeds.get(ts);
                        if (s >= 0) {
                            String code;
                            try {
                                code = DungeonSeed.convertToCode(s);
                            } catch (Exception e) {
                                code = Long.toString(s);
                            }
                            sb.append(Messages.get(SeedFinder.class, "thread_seed", ts + 1, code)).append("\n");
                        }
                    }
                    String info = sb.toString();
                    if (!info.equals(threadInfoText.text())) {
                        threadInfoText.text(info);
                        threadInfoText.setRect(threadInfoText.left(), threadInfoText.top(),
                                threadInfoText.width(), 0);
                    }
                    currentSeedText.text(Messages.get(SeedFindScene.class, "searching"));
                } else if (currentSeedValue != lastShownSeed) {
                    // 单线程：只显示当前遍历种子
                    lastShownSeed = currentSeedValue;
                    if (currentSeedValue >= 0) {
                        currentSeedText.text(Messages.get(SeedFindScene.class, "searching_current")
                                + currentSeedValue);
                    }
                }
            }
        }
    }
    public static SeedFindScene INSTANCE = null;
    volatile boolean needUpdate;
    volatile String text = "";
    public void updateCurrentSeed(long seed) {
        currentSeedValue = seed;
        needUpdate = true;
    }

    @Override
    public void destroy() {
        super.destroy();
        stopSearch();
    }

    private static WndFinder mainWindow;
    private Thread findSeedThread;

    // 查找种子：构建目标列表；PlusSearch 总开关关闭时强制单线程（与旧查种器一致）
    private void startSearch() {
        stopThread = false;
        currentSeedValue = -1;
        lastShownSeed = -1;

        // 预置线程数，避免工作线程启动前的空窗期里 UI 读到上一次搜索的残留值。
        // 注意：多线程按钮是 PlusSearch 总开关，PlusThread 只是线程数。
        final int threads = SPDSettings.PlusSearch()
                ? Math.max(1, Math.min(SeedFinder.parallelSeeds.length(), SPDSettings.PlusThread()))
                : 1;
        SeedFinder.searchThreadCount = threads;
        for (int t = 0; t < SeedFinder.parallelSeeds.length(); t++)
            SeedFinder.parallelSeeds.set(t, -1);

        final ArrayList<WantedTarget> targets = new ArrayList<>();
        for (Item item : wantedItems)
            for (int i = 0; i < item.quantity(); i++)
                targets.add(new WantedTarget(item));

        showSearchView();
        final NewSeedFinder finder = new NewSeedFinder(targets, currentFloor, currentHero);
        findSeedThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    finder.run(threads);
                } catch (Exception e) {
                    e.printStackTrace();
                    if (!stopThread) {
                        text = Messages.get(SeedFindScene.class, "find_failed", e.getMessage());
                        needUpdate = true;
                    }
                }
            }
        });
        findSeedThread.start();
    }

    private void stopSearch() {
        stopThread = true;
        if (findSeedThread != null && findSeedThread.isAlive()) {
            findSeedThread.interrupt();
        }
        NewSeedFinder.SeedFinding = false;
        NewSeedFinder.running = false;
        // synchronized 等待不可中断，置位并行完成标记让锁内工作线程拿到锁后立即退出
        SeedFinder.parallelFound = true;
        // 复位内存态种子，避免中断后残留覆盖正常游戏种子
        Dungeon.overrideSeed = -1;
    }

    // ======================== CreditsBlock（保留原样） ========================
    public static class CreditsBlock extends Component {
        boolean large;
        RenderedTextBlock body;

        public CreditsBlock(boolean large, int highlight, String body) {
            this.large = large;
            this.body = PixelScene.renderTextBlock(body, 6);
            if (highlight != -1) {
                this.body.setHightlighting(true, highlight);
            }

            if (large) {
                this.body.align(2);
            }

            this.add(this.body);
        }

        protected void layout() {
            super.layout();
            float topY = this.top();
            if (this.large) {
                this.body.maxWidth((int) this.width());
                this.body.setPos(this.x + (this.width() - this.body.width()) / 2.0F, topY);
            } else {
                ++topY;
                this.body.maxWidth((int) this.width());
                this.body.setPos(this.x, topY);
            }

            topY += this.body.height();
            this.height = Math.max(this.height, topY - this.top());
        }
    }
}
