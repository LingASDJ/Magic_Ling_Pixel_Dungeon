package com.shatteredpixel.shatteredpixeldungeon.custom.seedfinder;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Chrome;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.GamesInProgress;
import com.shatteredpixel.shatteredpixeldungeon.PaswordBadges;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.Constants;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.Artifact;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.Potion;
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
    /** 文本输入解析出的查询目标（含纯等级通配，如 "+4"），启动查种时并入 targets */
    private static ArrayList<WantedTarget> textTargets = null;
    // 查种不需要选角色：角色不影响地形与奖励生成，固定用战士（与旧版一致）
    public final HeroClass currentHero = HeroClass.WARRIOR;
    public static int currentFloor = Constants.MAX_DEPTH;
    public void create() {
        super.create();

        int w = Camera.main.width;
        int h = Camera.main.height;

        Archs archs = new Archs();
        archs.setSize(w, h);
        add(archs);

        INSTANCE = this;

        // 预热：以战士初始化一次地牢（含 ItemStatusHandler 等静态状态）
        GamesInProgress.selectedClass = HeroClass.WARRIOR;
        Dungeon.overrideSeed = -1;
        Dungeon.init();
        GamesInProgress.selectedClass = null;
        // 预热徽章状态：未解锁判定（SeedItemFilters.isLocked）依赖 Badges/PaswordBadges
        Badges.loadGlobal();
        PaswordBadges.loadGlobal();

        addToFront(mainWindow = new WndFinder());

        buildSearchView();

        addCopySeedButton();
        addExitButton();
    }

    // ======================== 搜索结果视图（scene 级，盖住 WndFinder） ========================
    private RenderedTextBlock currentSeedText;
    private ScrollPane resultScroll;
    private Component resultContent;
    private boolean searchViewVisible = false;
    private float seedDisplayCooldown = 0f;

    private void buildSearchView() {
        currentSeedText = PixelScene.renderTextBlock("", 6);
        currentSeedText.hardlight(0xFFFFFF);
        currentSeedText.visible = false;
        add(currentSeedText);

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

        if (btnCopy != null)
            btnCopy.enable(btnCopy.visible = false);

        resultContent.clear();
        resultContent.setSize(contentW, 0);
        float top = currentSeedText.bottom() + 2;
        resultScroll.setRect(cx, top, contentW, screenH - top);
        resultScroll.visible = true;
        resultScroll.active = true;
        resultScroll.scrollTo(0, 0);
    }

    // 显示查找结果（GitHub 版 CreditsBlock 风格）
    private void showSearchResult(String body) {
        if (!searchViewVisible) return;
        // 查询结束，清掉“正在查找……”状态文本
        currentSeedText.text("");
        currentSeedText.visible = false;
        currentSeedValue = -1;
        searchRunning = false;//【临时·性能测试】

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
    // 挑战文本
    private static String challengeText() {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (int i = 0; i < Challenges.NAME_IDS.length; i++) {
            if ((SPDSettings.challenges() & Challenges.MASKS[i]) != 0) {
                if (!first) sb.append(Messages.get(SeedFindScene.class, "separator"));
                sb.append(Messages.titleCase(Messages.get(Challenges.class, Challenges.NAME_IDS[i])));
                first = false;
            }
        }
        if (first) sb.append(Messages.get(SeedFindScene.class, "none"));
        return sb.toString();
    }
    //【临时·性能测试】本轮扫描起点时间、已扫描种子总数与进度显示开关（后续移除）
    volatile long scanStartMs = 0;
    volatile long scannedSeeds = -1;
    private volatile boolean searchRunning = false;

    private volatile long currentSeedValue = -1;
    private volatile boolean stopThread = false;
    // 后台线程只写进度数据，渲染线程每 250ms 聚合成整段文案刷新，避免逐种子 post 事件
    private String lastShownStatus = null;

    private static final int BTN_H = 16;

    // ======================== 新窗口（五个页签：设置/装备/消耗品/清单/开始） ========================
    private final class WndFinder extends WndTabbed {
        private final int winW;
        private final int winH;
        private final Component[] pages = new Component[5];
        private final Tab[] tabs = new Tab[5];
        private RedButton testBtn;
        private RenderedTextBlock page1Info;

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
            boolean hasItems = !wantedItems.isEmpty();
            tabs[1].active = true;
            tabs[2].active = true;
            tabs[3].active = true;
            tabs[4].active = hasItems;
            if (testBtn != null) testBtn.enable(true);
        }

        //第一页设置
        private Component settingPage() {
            Component root = new Component();
            float w = winW - 4;

            RenderedTextBlock title = PixelScene.renderTextBlock(Messages.get(SeedFindScene.class, "current_settings"), 9);
            title.hardlight(TITLE_COLOR);
            root.add(title);
            title.setPos(2, 1);

            page1Info = PixelScene.renderTextBlock("", 6);
            root.add(page1Info);
            page1Info.setRect(2, 13, w, 40);
            refreshPage1Info();

            RenderedTextBlock hint = PixelScene.renderTextBlock(
                    Messages.get(SeedFindScene.class, "settings_hint"), 6);
            root.add(hint);
            hint.setRect(2, 55, w, 32);

            RedButton settingsBtn = new RedButton(Messages.get(SeedFindScene.class, "btn_settings"), 8) {
                @Override
                protected void onClick() {
                    ShatteredPixelDungeon.scene().addToFront(new WndFinderSettings());
                }
            };
            settingsBtn.icon(Icons.get(Icons.PREFS));
            root.add(settingsBtn);
            settingsBtn.setRect(2, 89, w, 18);

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
            testBtn.setRect(2, 110, w, 18);

            // 文本输入查询目标：支持「物品名+等级」与纯等级模糊（如 +4 → 任意 +4 及以上物品）
            RedButton itemInputBtn = new RedButton(Messages.get(SeedFindScene.class, "btn_input_items"), 8) {
                @Override
                protected void onClick() {
                    ShatteredPixelDungeon.scene().addToFront(
                            new WndTextInput(
                                    Messages.get(SeedFindScene.class, "input_items_title"),
                                    Messages.get(SeedFindScene.class, "input_items_body"),
                                    "",
                                    200,
                                    true,
                                    Messages.get(SeedFindScene.class, "confirm"),
                                    Messages.get(SeedFindScene.class, "cancel")
                            ) {
                                @Override
                                public void onSelect(boolean check, String text) {
                                    if (!check || text == null) return;
                                    ArrayList<WantedTarget> parsed =
                                            SeedFinder.parseWanted(text.split("\n"));
                                    if (parsed.isEmpty()) {
                                        ShatteredPixelDungeon.scene().addToFront(
                                                new WndMessage(Messages.get(SeedFindScene.class, "input_items_none")));
                                        return;
                                    }
                                    textTargets = parsed;
                                    StringBuilder sb = new StringBuilder(
                                            Messages.get(SeedFindScene.class, "input_items_ok", parsed.size()));
                                    for (WantedTarget t : parsed)
                                        sb.append("\n").append(t.cls == null
                                                ? Messages.get(SeedFindScene.class, "input_items_any") + "+" + t.minLevel
                                                : t.cls.getSimpleName() + "+" + t.minLevel);
                                    ShatteredPixelDungeon.scene().addToFront(new WndMessage(sb.toString()));
                                }
                            }
                    );
                }
            };
            itemInputBtn.icon(Icons.get(Icons.MAGNIFY));
            root.add(itemInputBtn);
            itemInputBtn.setRect(2, 131, w, 18);

            return root;
        }
        private void refreshPage1Info() {
            if (page1Info == null) return;
            page1Info.text(
                    Messages.get(SeedFindScene.class, "hero_fixed") + "\n"
                            + Messages.get(SeedFindScene.class, "deep_floor", currentFloor) + "\n"
                            + Messages.get(SeedFindScene.class, "challenges_info", challengeText()), winW);
        }
        //第二页装备
        @SuppressWarnings("unchecked")
        private Component equipmentPane() {
            ArrayList<ItemGroup> groups = new ArrayList<>();

            // 2-6阶近战武器（Catalog 不分阶，标题沿用旧版 "T阶数" 写法）
            Generator.Category category;
            ItemGroup g;
            for (int t = 1; t < Generator.wepTiers.length; t++) {
                category = Generator.wepTiers[t];
                g = new ItemGroup("T" + (t + 1) + " " + Catalog.MELEE_WEAPONS.title());
                for (int i = 0; i < category.classes.length; i++)
                    if (category.probs[i] >= 0f && !SeedItemFilters.isUngenerated(category.classes[i]))
                        g.items.add((Class<? extends Item>) category.classes[i]);
                if (!g.items.isEmpty())
                    groups.add(g);
            }

            // 2-5阶护甲
            category = Generator.Category.ARMOR;
            g = new ItemGroup(Catalog.ARMOR.title());
            for (int i = 0; i < category.classes.length; i++)
                if (category.probs[i] >= 0f && !SeedItemFilters.isUngenerated(category.classes[i]))
                    g.items.add((Class<? extends Item>) category.classes[i]);
            groups.add(g);

            // wands
            category = Generator.Category.WAND;
            g = new ItemGroup(Catalog.WANDS.title());
            for (int i = 0; i < category.classes.length; i++)
                if (category.probs[i] >= 0f && !SeedItemFilters.isUngenerated(category.classes[i]))
                    g.items.add((Class<? extends Item>) category.classes[i]);
            if (!g.items.isEmpty())
                groups.add(g);

            // rings
            category = Generator.Category.RING;
            g = new ItemGroup(Catalog.RINGS.title());
            for (int i = 0; i < category.classes.length; i++)
                if (category.probs[i] >= 0f && !SeedItemFilters.isUngenerated(category.classes[i]))
                    g.items.add((Class<? extends Item>) category.classes[i]);
            if (!g.items.isEmpty())
                groups.add(g);

            // artifacts
            category = Generator.Category.ARTIFACT;
            g = new ItemGroup(Catalog.ARTIFACTS.title());
            for (int i = 0; i < category.classes.length; i++)
                if (category.defaultProbs[i] >= 0f && !SeedItemFilters.isUngenerated(category.classes[i]))
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
                if (category.probs[i] >= 0f && !SeedItemFilters.isUngenerated(category.classes[i]))
                    g.items.add((Class<? extends Item>) category.classes[i]);
            groups.add(g);

            category = Generator.Category.SCROLL;
            g = new ItemGroup(Catalog.SCROLLS.title());
            for (int i = 0; i < category.classes.length; i++)
                if (category.probs[i] >= 0f && !SeedItemFilters.isUngenerated(category.classes[i]))
                    g.items.add((Class<? extends Item>) category.classes[i]);
            groups.add(g);

            category = Generator.Category.STONE;
            g = new ItemGroup(Catalog.STONES.title());
            for (int i = 0; i < category.classes.length; i++)
                if (category.probs[i] >= 0f && !SeedItemFilters.isUngenerated(category.classes[i]))
                    g.items.add((Class<? extends Item>) category.classes[i]);
            groups.add(g);

            category = Generator.Category.FOOD;
            g = new ItemGroup(Catalog.FOOD.title());
            for (int i = 0; i < category.classes.length; i++)
                if (category.probs[i] >= 0f && !SeedItemFilters.isUngenerated(category.classes[i]))
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
                    button.setRect( 0, pos, width, 23 );
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
            RedButton startBtn;
            RenderedTextBlock summaryText;
            public summaryPane() {
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
            //摘要文案：角色（固定战士）/挑战/楼层/物品需求 + 当前并行进程数
            private String summaryBody() {
                StringBuilder sb = new StringBuilder();
                sb.append(Messages.get(SeedFindScene.class, "hero_fixed")).append("\n");
                sb.append(Messages.get(SeedFindScene.class, "challenges_info", challengeText())).append("\n");
                sb.append(Messages.get(SeedFindScene.class, "deep_floor", currentFloor)).append("\n");
                sb.append(Messages.get(SeedFindScene.class, "requirements", wantedItems.size())).append("\n");
                for (int i = 0; i < wantedItems.size(); i++) {
                    sb.append(i + 1).append(". ").append(wantedItems.get(i).name()).append("\n");
                }
                // 并行进程数：开启强力搜索 → 用设置页「种子线程」滑块的数值；关闭 → 单进程
                int workers = SeedFinderCoordinator.resolveWorkers(
                        SPDSettings.PlusSearch() ? SPDSettings.PlusThread() : 1);
                sb.append(Messages.get(SeedFindScene.class, "workers_info", workers));
                if (workers < 2) sb.append(Messages.get(SeedFindScene.class, "single_process"));
                return sb.toString();
            }
            //滑块变动时原地刷新数值（行数不变，不影响布局）
            void refreshSummary() {
                if (summaryText != null) summaryText.text(summaryBody(), (int) (width - 2));
            }
            void resetButton() {
                Component content = this.content();
                content.clear();
                float w = width;

                summaryText = PixelScene.renderTextBlock("", 6);
                summaryText.text(summaryBody(), (int) (w - 2));
                content.add(summaryText);
                summaryText.setRect(1, 1, w - 2, 0);

                float nextY = summaryText.bottom() + 4;

                startBtn = new RedButton(wantedItems.isEmpty()
                        ? Messages.get(SeedFindScene.class, "start_empty")
                        : Messages.get(SeedFindScene.class, "start")) {
                    @Override
                    protected void onClick() {
                        startSearch();
                    }
                };
                content.add(startBtn);
                startBtn.setRect(1, nextY, w - 2, 18);

                content.setSize(w, startBtn.bottom() + 2);
            }
        }
        @Override
        public void onBackPressed() {

        }
    }
    // ======================== 查种设置窗口（楼层/挑战，自建 Window 版；查种不需要选角色，固定战士） ========================
    public static class WndFinderSettings extends Window {
        private static final int WIN_W = 120;

        private int tempFloor = currentFloor;

        public WndFinderSettings() {
            super();

            RenderedTextBlock title = PixelScene.renderTextBlock(Messages.get(SeedFindScene.class, "settings_title"), 9);
            title.hardlight(Window.TITLE_COLOR);
            title.setPos((WIN_W - title.width()) / 2f, 3);
            add(title);

            // ---- 挑战 + 楼层按钮行 ----
            float rowY = title.bottom() + 8;

            IconButton challengeBtn = new IconButton(
                    Icons.get(SPDSettings.challenges() > 0 ? Icons.CHALLENGE_ON : Icons.CHALLENGE_OFF)) {
                @Override
                protected void onClick() {
                    // 必须 editable=true：否则挑战勾选框 active=false，完全无法勾选
                    ShatteredPixelDungeon.scene().addToFront(
                            new WndChallenges(SPDSettings.challenges(), true, null) {
                                public void onBackPressed() {
                                    super.onBackPressed();
                                    if (parent != null) {
                                        icon(Icons.get(SPDSettings.challenges() > 0 ?
                                                Icons.CHALLENGE_ON : Icons.CHALLENGE_OFF));
                                    }
                                }
                            });
                }
                @Override
                public void update() {
                    icon(Icons.get(SPDSettings.challenges() > 0 ? Icons.CHALLENGE_ON : Icons.CHALLENGE_OFF));
                    super.update();
                }
            };
            challengeBtn.setRect(0, rowY, 20, 20);
            add(challengeBtn);

            StyledButton floorBtn = new FloorButton();
            floorBtn.setRect(WIN_W - 40, rowY, 40, 20);
            add(floorBtn);

            // ---- 确认按钮 ----
            RedButton confirmBtn = new RedButton(Messages.get(SeedFindScene.class, "confirm")) {
                @Override
                public void onClick() {
                    currentFloor = tempFloor;
                    Dungeon.overrideSeed = -1;
                    Dungeon.init();
                    mainWindow.refreshPage1Info();
                    hide();
                }
            };
            confirmBtn.setRect(0, rowY + 24, WIN_W, 20);
            add(confirmBtn);

            resize(WIN_W, (int) confirmBtn.bottom() + 2);
        }

        public final class FloorButton extends StyledButton {
            public FloorButton() {
                super(Chrome.Type.GEM, String.valueOf(tempFloor), 8);
            }
            @Override
            protected void onClick() {
                ShatteredPixelDungeon.scene().addToFront(new WndSelectLevel());
            }
            public final class WndSelectLevel extends Window {
                private static final int PICKER_W = 120;
                private static final int GAP = 2;
                private static final int BTN_SIZE = 16;
                private static final int PANE_MAX_HEIGHT = 96;

                private int selectedFloor = tempFloor;
                final public RedButton confirm;

                WndSelectLevel() {
                    super();
                    ScrollPane sp = new ScrollPane(new Component());
                    add(sp);

                    confirm = new RedButton(Messages.get(SeedFindScene.class, "confirm_floor", selectedFloor)) {
                        @Override
                        protected void onClick() {
                            tempFloor = selectedFloor;
                            FloorButton.this.text(String.valueOf(selectedFloor));
                            hide();
                        }
                    };
                    add(confirm);

                    Component content = sp.content();
                    float xPos = (PICKER_W - 5 * BTN_SIZE - GAP * 8) / 2f;
                    float each = GAP * 2 + BTN_SIZE;
                    for (int i = 0; i < Constants.MAX_DEPTH; ++i) {
                        StyledButton btn = floorBtn(i);
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

                private StyledButton floorBtn(int i) {
                    final int j = i + 1;
                    return new StyledButton(Chrome.Type.GEM, String.valueOf(j), 8) {
                        {
                            hotArea.blockLevel = PointerArea.NEVER_BLOCK;
                        }
                        @Override
                        protected void onClick() {
                            selectedFloor = j;
                            confirm.text(Messages.get(SeedFindScene.class, "confirm_floor", selectedFloor));
                        }
                    };
                }
            }
        }
    }

    private static final class PickGridItem extends ScrollingGridPane.GridItem {
        private final Class<? extends Item> cls;
        // 条件生成物品（商店解锁/徽章解锁等，见 SeedItemFilters）：网格保留显示但置灰不可选，点击提示无法查找
        private final boolean locked;
        private volatile boolean tinted = false;
        PickGridItem(Class<? extends Item> cls) {
            super(image(cls));
            this.cls = cls;
            this.locked = SeedItemFilters.isLocked(cls);
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
                ShatteredPixelDungeon.scene().addToFront(
                        new WndMessage(Messages.get(SeedFindScene.class, "locked_hint")));
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
            boolean isEnchant;
            int augRare, augId;
            RenderedTextBlock augInfo;
            ItemConfigWindow(Class<? extends Item> cls) {
                super();
                Item item = newInstance(cls);
                int maxLevel = getMaxLevelForClass(cls);

                if (Weapon.class.isAssignableFrom(cls)) {
                    pools = enchant;
                    isEnchant = true;
                } else if (Armor.class.isAssignableFrom(cls)) {
                    pools = glyph;
                    isEnchant = false;
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
                            isEnchant ? "" : "", "1", "8", 0, 7) {
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
                            isEnchant ? "" : "", "", "", 0, 4) {
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
                    slider = new OptionSlider(Messages.get(SeedFindScene.class, "level"), "0", "+" + maxLevel, 0, maxLevel) {
                        @Override
                        protected void onChange() {
                            item.level(getSelectedValue());
                        }
                    };
                slider.setSelectedValue(0);
                }
                else {
                    slider = new OptionSlider(Messages.get(SeedFindScene.class, "quantity"), "1", "10", 1, 10) {
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
                        info.append(Messages.get(SeedFindScene.class, "current", getAugName(pool[augId])));
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
        // 戒指/药水的贴图由 ItemStatusHandler 按种子随机分配，未点开前无法辨认效果；
        // 改用 item_icons.png 中的固定 8x8 图标（放大 2 倍对齐 16px 网格）
        if ((item instanceof Potion || item instanceof Ring) && item.icon >= 0) {
            Image im = new Image(Assets.Sprites.ITEM_ICONS);
            im.frame(ItemSpriteSheet.Icons.film.get(item.icon));
            im.scale.set(2f);
            return im;
        }
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
        // 搜索进度显示（节流到 4 次/秒，避免与查找线程争抢 CPU）
        seedDisplayCooldown += Game.elapsed;
        if (seedDisplayCooldown >= 0.25f) {
            seedDisplayCooldown = 0f;
            if (searchViewVisible && searchRunning) {//【临时·性能测试】改为按查找进行中判断，起步阶段也能看到计时
                String status = progressText();
                if (!status.equals(lastShownStatus)) {
                    lastShownStatus = status;
                    currentSeedText.text(status);
                }
            }
        }
    }
    // 进度文案：计时与已扫描种子数；多进程时逐进程列出各自正在遍历的种子
    private String progressText() {
        //【临时·性能测试】已用时按壁钟计算，上限文案跟随 SEARCH_LIMIT_MS
        long elapsedMs = System.currentTimeMillis() - scanStartMs;
        StringBuilder sb = new StringBuilder(Messages.get(SeedFindScene.class, "searching")).append(" ")
                .append(Messages.get(SeedFindScene.class, "progress_elapsed",
                        (elapsedMs / 100) / 10.0, SeedFinder.SEARCH_LIMIT_MS / 1000));
        long[] ws = SeedFinderCoordinator.workerSeeds;
        if (ws != null && ws.length > 1) {
            sb.append("\n").append(Messages.get(SeedFindScene.class, "progress_scanned",
                    Math.max(0, scannedSeeds), ws.length));
            for (int i = 0; i < ws.length; i++) {
                sb.append('\n').append(Messages.get(SeedFindScene.class, "progress_worker", i + 1));
                sb.append(ws[i] >= 0 ? Long.toString(ws[i])
                        : Messages.get(SeedFindScene.class, "progress_starting"));
            }
            return sb.toString();
        }
        sb.append("\n").append(Messages.get(SeedFindScene.class, "progress_current",
                currentSeedValue >= 0 ? Long.toString(currentSeedValue)
                        : Messages.get(SeedFindScene.class, "progress_starting")));
        if (SeedFinderCoordinator.activeWorkers >= 2)
            sb.append(Messages.get(SeedFindScene.class, "progress_active", SeedFinderCoordinator.activeWorkers));
        sb.append(Messages.get(SeedFindScene.class, "progress_total", Math.max(0, scannedSeeds)));
        return sb.toString();
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
    private volatile SeedFinderCoordinator coordinator;

    // 查找种子：平台支持且进程数>=2 时走多进程，否则单进程线程查找
    private void startSearch() {
        stopThread = false;
        currentSeedValue = -1;
        lastShownStatus = null;
        scanStartMs = System.currentTimeMillis();//【临时·性能测试】
        scannedSeeds = 0;//【临时·性能测试】
        searchRunning = true;//【临时·性能测试】

        final ArrayList<WantedTarget> targets = new ArrayList<>();
        for (Item item : wantedItems)
            for (int i = 0; i < item.quantity(); i++)
                targets.add(new WantedTarget(item));
        // 并入文本输入的目标（含纯等级通配）
        if (textTargets != null) targets.addAll(textTargets);

        showSearchView();

        // 并行进程数：开启强力搜索 → 用设置页「种子线程」滑块的数值；关闭 → 单进程
        final int workers = SeedFinderCoordinator.resolveWorkers(
                SPDSettings.PlusSearch() ? SPDSettings.PlusThread() : 1);
        if (SeedFinderCoordinator.launcher != null && !targets.isEmpty() && workers >= 2) {
            // 多进程：每个子进程独立 JVM，物品生成依赖的静态状态互不干扰
            coordinator = new SeedFinderCoordinator(targets, currentFloor, currentHero, workers);
            findSeedThread = new Thread(coordinator, "seed-finder-coordinator");
        } else {
            coordinator = null;
            final SeedFinder finder = new SeedFinder(targets, currentFloor, currentHero);
            findSeedThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        finder.run();
                    } catch (Exception e) {
                        e.printStackTrace();
                        if (!stopThread) {
                            text = Messages.get(SeedFinder.class, "find_failed", e.getMessage());
                            needUpdate = true;
                        }
                    }
                }
            }, "seed-finder");
        }
        findSeedThread.start();
    }

    private void stopSearch() {
        stopThread = true;
        if (coordinator != null) {
            coordinator.stop();
            coordinator = null;
        }
        if (findSeedThread != null && findSeedThread.isAlive()) {
            findSeedThread.interrupt();
        }
        SeedFinder.SeedFinding = false;
        SeedFinder.running = false;
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

    // 清单页物品行：图标 + 名称（替代旧版 WndRanking.canScrollItemButton）
    private static final class WantedItemButton extends StyledButton {
        private Runnable clickAction;

        WantedItemButton(Item item) {
            super(Chrome.Type.GREY_BUTTON_TR, item.toString(), 7);
            // 戒指/药水用固定小图标，避免随机贴图无法辨认
            if ((item instanceof Potion || item instanceof Ring) && item.icon >= 0) {
                Image im = new Image(Assets.Sprites.ITEM_ICONS);
                im.frame(ItemSpriteSheet.Icons.film.get(item.icon));
                im.scale.set(2f);
                icon(im);
            } else {
                icon(new ItemSprite(item));
            }
        }

        void setClickAction(Runnable action) {
            clickAction = action;
        }

        @Override
        protected void onClick() {
            if (clickAction != null) clickAction.run();
        }
    }
}
