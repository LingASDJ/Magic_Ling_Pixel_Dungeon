package com.shatteredpixel.shatteredpixeldungeon.scenes;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Chrome;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.Button;
import com.shatteredpixel.shatteredpixeldungeon.ui.ExitButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.IconButton;
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
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.Game;
import com.watabou.noosa.Group;
import com.watabou.noosa.Image;
import com.watabou.noosa.NinePatch;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.ui.Component;
import com.watabou.utils.Bundle;
import com.watabou.utils.FileUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 远拓石碑。
 *
 * <p>界面分两层：
 * <ol>
 *     <li>篇章层：以“大模块”（渊落古堡、雪景小镇、魔绫地牢……）列出所有篇章及已记载数量；</li>
 *     <li>阅读层：点击大模块后，中间以卷轴形式展示该篇章的故事，左右两侧（窄屏时为底部）的
 *     箭头用来翻页。</li>
 * </ol>
 *
 * <p>新增篇章：在 {@link YuanTaStoryManager#MODULES} 里登记一个 {@link YuanTaModule}，
 * 并在 scenes*.properties 中补上 {@code module_<id>} 与 {@code module_<id>_desc} 两条文案。
 * 故事用 {@code unlockStory(id, moduleId, ...)} 显式归入篇章，或让故事 id/标题带上篇章关键字，
 * 由 {@link YuanTaStoryManager#resolveModuleId(String, String)} 自动推断。
 */
public class YuanTaStoneScene extends PixelScene {

    private static final int BTN_HEIGHT = 20;
    private static final int GAP = 3;
    private static final int MARGIN = 12;
    private static final int MODULE_ROW_HEIGHT = 48;
    private static final int ARROW_SIZE = 20;

    private static final int DOT_COLOR_CURRENT = 0xFFFFFF;
    private static final int DOT_COLOR_UNREAD = Window.TITLE_COLOR;
    private static final int DOT_COLOR_READ = 0x606060;

    private IconTitle title;

    //当前显示的是篇章层还是阅读层
    private Group view;

    private YuanTaModule activeModule;
    private final ArrayList<YuanTaStory> pages = new ArrayList<>();
    private int pageIndex = 0;

    //阅读层几何信息，翻页时需要复用
    private float panelX;
    private float panelY;
    private float panelW;

    private float pageX;
    private float pageY;
    private float pageW;
    private float pageH;

    private Group pageHolder;
    private Group pageGroup;

    private IconButton prevBtn;
    private IconButton nextBtn;
    private RenderedTextBlock pageCounter;
    private final ArrayList<ColorBlock> pageDots = new ArrayList<>();

    @Override
    public void create() {
        super.create();

        uiCamera.visible = false;

        int w = Camera.main.width;
        int h = Camera.main.height;
        Music.INSTANCE.play(Assets.Music.BGM_1A, true);
        add(new TitleBackground(w, h));

        Image im = new Image(TextureCache.createGradient(0x66000000, 0x88000000, 0xAA000000, 0xCC000000, 0xFF000000));
        im.angle = 90;
        im.x = w;
        im.scale.x = h / 5f;
        im.scale.y = w;
        add(im);

        title = new IconTitle(Icons.get(Icons.CATALOG), Messages.get(YuanTaStoneScene.class, "title"));
        title.setSize(200, 0);
        title.setPos((w - title.reqWidth()) / 2f, (20 - title.height()) / 2f);
        align(title);
        add(title);

        ExitButton btnExit = new ExitButton() {
            @Override
            protected void onClick() {
                Game.switchScene(GameScene.class);
            }
        };
        btnExit.setPos(w - btnExit.width(), 0);
        add(btnExit);

        showModuleList();

        fadeIn();
    }

    @Override
    protected void onBackPressed() {
        //阅读层时先回到篇章层，篇章层时退出场景
        if (activeModule != null) {
            showModuleList();
        } else {
            ShatteredPixelDungeon.switchNoFade(GameScene.class);
        }
    }

    private void setView(Group next) {
        if (view != null) {
            view.destroy();
            remove(view);
        }
        view = next;
        add(view);
    }

    // ====================== 第一层：篇章（大模块） ======================

    private void showModuleList() {
        activeModule = null;
        pages.clear();
        pageIndex = 0;
        pageHolder = null;
        pageGroup = null;
        prevBtn = null;
        nextBtn = null;
        pageCounter = null;
        pageDots.clear();

        Group root = new Group();
        setView(root);

        int w = Camera.main.width;
        int h = Camera.main.height;

        float x = MARGIN;
        float contentTop = title.bottom() + 6;
        float width = w - MARGIN * 2f;
        float maxHeight = Math.max(20, h - BTN_HEIGHT - 12 - contentTop);

        ScrollPane list = new ScrollPane(new Component());
        Component content = list.content();

        List<YuanTaStory> all = YuanTaStoryManager.getUnlockedStories();
        float posY = 0;
        if (all.isEmpty()) {
            RenderedTextBlock empty = renderTextBlock(Messages.get(YuanTaStoneScene.class, "no_stories"), 7);
            empty.maxWidth((int) (width - 16));
            empty.hardlight(0xBBBBBB);
            empty.setPos((width - 8 - empty.width()) / 2f, 2);
            content.add(empty);
            posY = empty.bottom() + 6;
        }

        //卡片宽度 < 150 时改用紧凑排版（隐藏简介/箭头/新标签），行高也随之收窄
        boolean compact = width - 16 < 150;
        int rowHeight = compact ? 40 : MODULE_ROW_HEIGHT;

        for (YuanTaModule module : YuanTaStoryManager.MODULES) {
            ModuleCard card = new ModuleCard(module, YuanTaStoryManager.getStories(module.id));
            card.setRect(4, posY, width - 16, rowHeight);
            content.add(card);
            posY = card.bottom() + GAP;
        }
        posY = Math.max(0, posY - GAP);

        float contentHeight = Math.max(12, posY + 2);

        // 面板按内容自适应高度：章节少时是一块紧凑的石碑，章节多时在面板内滚动
        float panelHeight = Math.max(28, Math.min(maxHeight, posY + 12));
        float panelY = contentTop;
        //空间很充裕时把石碑放中间，避免顶部堆叠、底部大片留白
        float extra = maxHeight - panelHeight;
        if (extra > 48) {
            panelY = contentTop + extra / 2f;
        }

        NinePatch panel = Chrome.get(Chrome.Type.GREY_BUTTON_TR);
        panel.x = x;
        panel.y = panelY;
        panel.size(width, panelHeight);
        align(panel);
        root.add(panel);

        root.add(list);
        content.setSize(width - 8, contentHeight);
        list.setRect(x + 4, panelY + 5, width - 8, panelHeight - 10);
        //让滚动控制器重新回到指针监听队列最前，保证在卡片上也可以拖动列表
        list.refreshPointerPriority();

        StyledButton readMore = new StyledButton(Chrome.Type.GREY_BUTTON_TR,
                Messages.get(YuanTaStoneScene.class, "read_more")) {
            @Override
            protected void onClick() {
                super.onClick();
                ShatteredPixelDungeon.scene().addToFront(new WndTitledMessage(
                        new Image(Icons.get(Icons.CATALOG)),
                        Messages.get(YuanTaStoneScene.class, "morestory"),
                        Messages.get(YuanTaStoneScene.class, "morestory_desc")));
            }
        };
        readMore.icon(Icons.get(Icons.NEWS));
        readMore.textColor(Window.TITLE_COLOR);
        readMore.setRect(x, h - BTN_HEIGHT - 8, width, BTN_HEIGHT);
        root.add(readMore);
    }

    /** 一个“大模块”条目：图标 + 名称 + 简介 + 记载数量。 */
    private class ModuleCard extends Button {

        private final YuanTaModule module;
        private final List<YuanTaStory> stories;
        private final int newCount;

        private final NinePatch bg;
        private final ItemSprite icon;
        private final RenderedTextBlock name;
        private final RenderedTextBlock desc;
        private final RenderedTextBlock count;
        private final RenderedTextBlock newTag;
        private final Image enter;

        ModuleCard(YuanTaModule module, List<YuanTaStory> stories) {
            super();

            this.module = module;
            this.stories = stories;

            long lastRead = SPDSettings.yuantuoLastRead();
            int fresh = 0;
            for (YuanTaStory story : stories) {
                if (story.unlockTime > lastRead) fresh++;
            }
            newCount = fresh;

            bg = Chrome.get(Chrome.Type.GREY_BUTTON_TR);
            addToBack(bg);

            icon = new ItemSprite(module.iconIdx, null);
            icon.scale.set(1.5f);
            add(icon);

            name = renderTextBlock(module.title(), 10);
            name.hardlight(Window.TITLE_COLOR);
            add(name);

            desc = renderTextBlock(module.desc(), 6);
            desc.hardlight(0x9D9D9D);
            add(desc);

            count = renderTextBlock(stories.isEmpty()
                    ? Messages.get(YuanTaStoneScene.class, "module_none")
                    : Messages.get(YuanTaStoneScene.class, "module_count", stories.size()), 6);
            count.hardlight(stories.isEmpty() ? 0x777777 : 0xCCCCCC);
            add(count);

            newTag = newCount > 0
                    ? renderTextBlock(Messages.get(YuanTaStoneScene.class, "new"), 6)
                    : null;
            if (newTag != null) {
                newTag.hardlight(Window.CYELLOW);
                add(newTag);
            }

            enter = Icons.get(Icons.RIGHTBUTTON);
            enter.alpha(0.55f);
            add(enter);

            if (stories.isEmpty()) {
                icon.alpha(0.45f);
                name.alpha(0.55f);
                desc.alpha(0.45f);
                enter.alpha(0.25f);
            }
        }

        @Override
        protected void layout() {
            super.layout();

            bg.x = x;
            bg.y = y;
            bg.size(width, height);

            boolean compact = width < 150;

            float right = x + width - (compact ? 6 : 5);

            enter.visible = !compact;
            if (!compact) {
                enter.x = right - enter.width();
                enter.y = y + (height - enter.height()) / 2f;
                align(enter);
                right -= enter.width() + 4;
            }

            count.setPos(right - count.width(), y + (height - count.height()) / 2f);
            align(count);
            //窄屏没有“新”标签，用数量文字的颜色提示有新故事
            if (compact) {
                count.hardlight(newCount > 0 ? Window.CYELLOW
                        : (stories.isEmpty() ? 0x777777 : 0xCCCCCC));
            }
            right -= count.width() + (compact ? 4 : 5);

            if (newTag != null) {
                newTag.visible = !compact;
                if (!compact) {
                    newTag.setPos(right - newTag.width(), y + (height - newTag.height()) / 2f);
                    align(newTag);
                    right -= newTag.width() + 4;
                }
            }

            //窄屏上把图标缩回原始尺寸，给章节名留出足够宽度
            icon.scale.set(compact ? 1f : 1.5f);
            icon.x = x + (compact ? 6 : 8);
            icon.y = y + (height - icon.height()) / 2f;
            align(icon);

            float textX = icon.x + icon.width() + (compact ? 6 : 8);
            int textW = (int) Math.max(16, right - textX);
            name.maxWidth(textW);
            desc.maxWidth(textW);

            boolean showDesc = !compact && width >= 150;
            if (showDesc) {
                float blockH = name.height() + 2 + desc.height();
                float startY = y + Math.max(2, (height - blockH) / 2f);
                name.setPos(textX, startY);
                desc.setPos(textX, name.bottom() + 2);
                //简介放不下时优先保证名称完整显示
                if (desc.bottom() > y + height - 2) {
                    showDesc = false;
                }
            }
            if (!showDesc) {
                name.setPos(textX, y + (height - name.height()) / 2f);
            }
            desc.visible = showDesc;
            align(name);
            align(desc);
        }

        @Override
        protected void onPointerDown() {
            bg.brightness(1.2f);
            Sample.INSTANCE.play(Assets.Sounds.CLICK);
        }

        @Override
        protected void onPointerUp() {
            bg.resetColor();
        }

        @Override
        protected void onClick() {
            openModule(module);
        }

        @Override
        protected String hoverText() {
            return module.title();
        }
    }

    // ====================== 第二层：篇章内的故事阅读 ======================

    private void openModule(YuanTaModule module) {
        activeModule = module;
        pages.clear();
        pages.addAll(YuanTaStoryManager.getStories(module.id));

        pageIndex = 0;
        long lastRead = SPDSettings.yuantuoLastRead();
        for (int i = 0; i < pages.size(); i++) {
            if (pages.get(i).unlockTime > lastRead) {
                pageIndex = i;
                break;
            }
        }

        buildStoryView();
    }

    private void buildStoryView() {
        Group root = new Group();
        setView(root);

        pageDots.clear();
        pageHolder = null;
        pageGroup = null;

        int w = Camera.main.width;
        int h = Camera.main.height;

        panelX = MARGIN;
        panelY = title.bottom() + 6;
        panelW = w - MARGIN * 2f;
        float panelH = h - 12 - panelY;
        if (panelH < 40) panelH = 40;
        float panelBottom = panelY + panelH;

        float innerX = panelX + 4;
        float innerW = panelW - 8;

        NinePatch panel = Chrome.get(Chrome.Type.GREY_BUTTON_TR);
        panel.x = panelX;
        panel.y = panelY;
        panel.size(panelW, panelH);
        align(panel);
        root.add(panel);

        // 返回按钮
        IconButton back = new IconButton(Icons.get(Icons.LEFTBUTTON)) {
            @Override
            protected void onClick() {
                super.onClick();
                showModuleList();
            }

            @Override
            protected String hoverText() {
                return Messages.titleCase(Messages.get(YuanTaStoneScene.class, "back"));
            }
        };
        back.setSize(18, 18);
        back.setPos(innerX + 2, panelY + 4);
        align(back);
        root.add(back);

        // 页码
        pageCounter = renderTextBlock("", 7);
        pageCounter.hardlight(0xAAAAAA);
        pageCounter.text(Messages.get(YuanTaStoneScene.class, "page",
                pages.isEmpty() ? 0 : pageIndex + 1, pages.size()));
        pageCounter.setPos(innerX + innerW - 2 - pageCounter.width(),
                panelY + 4 + (18 - pageCounter.height()) / 2f);
        align(pageCounter);
        root.add(pageCounter);

        // 篇章名
        RenderedTextBlock header = renderTextBlock(activeModule.title(), 9);
        header.hardlight(Window.TITLE_COLOR);
        float headerX = back.right() + 6;
        float headerMax = Math.max(24, pageCounter.left() - 6 - headerX);
        header.maxWidth((int) headerMax);
        header.setPos(headerX, panelY + 4 + (18 - header.height()) / 2f);
        align(header);
        root.add(header);

        float headerBottom = Math.max(back.bottom(), Math.max(header.bottom(), panelY + 22));

        // 阅读区：宽屏左右翻页，窄屏底部翻页
        boolean sideArrows = panelW >= 200;
        float navH = sideArrows ? 12 : 22;
        float pageTop = headerBottom + 6;
        float pageBottom = panelBottom - navH - 2;

        if (sideArrows) {
            pageX = innerX + ARROW_SIZE + 6;
            pageW = innerW - (ARROW_SIZE + 6) * 2;
        } else {
            pageX = innerX + 4;
            pageW = innerW - 8;
        }
        pageY = pageTop;
        pageH = Math.max(24, pageBottom - pageTop);

        pageHolder = new Group();
        root.add(pageHolder);

        prevBtn = new IconButton(Icons.get(Icons.LEFTBUTTON)) {
            @Override
            protected void onClick() {
                super.onClick();
                showPage(pageIndex - 1);
            }
        };
        prevBtn.setSize(ARROW_SIZE, ARROW_SIZE);

        nextBtn = new IconButton(Icons.get(Icons.RIGHTBUTTON)) {
            @Override
            protected void onClick() {
                super.onClick();
                showPage(pageIndex + 1);
            }
        };
        nextBtn.setSize(ARROW_SIZE, ARROW_SIZE);

        if (sideArrows) {
            float arrowY = pageY + (pageH - ARROW_SIZE) / 2f;
            prevBtn.setPos(innerX + 2, arrowY);
            nextBtn.setPos(innerX + innerW - 2 - ARROW_SIZE, arrowY);
        } else {
            float arrowY = panelBottom - navH + (navH - ARROW_SIZE) / 2f;
            prevBtn.setPos(innerX + 2, arrowY);
            nextBtn.setPos(innerX + innerW - 2 - ARROW_SIZE, arrowY);
        }
        align(prevBtn);
        align(nextBtn);
        root.add(prevBtn);
        root.add(nextBtn);

        // 页码圆点（故事过多时只保留页码，不画圆点）
        int dots = pages.size() <= 9 ? pages.size() : 0;
        if (dots > 1) {
            float dotW = 4;
            float dotH = 3;
            float dotGap = 3;
            float total = dots * dotW + (dots - 1) * dotGap;
            float dotX = innerX + (innerW - total) / 2f;
            float dotY = sideArrows
                    ? panelBottom - 11
                    : panelBottom - navH / 2f - dotH / 2f;

            for (int i = 0; i < dots; i++) {
                // 颜色是 ARGB，必须带不透明的 alpha
                ColorBlock dot = new ColorBlock(dotW, dotH, 0xFFFFFFFF);
                dot.color(DOT_COLOR_READ);
                dot.x = dotX + i * (dotW + dotGap);
                dot.y = dotY;
                align(dot);
                root.add(dot);
                pageDots.add(dot);
            }
        }

        showPage(pageIndex);
    }

    private void showPage(int index) {
        if (pageHolder == null) return;

        if (pageGroup != null) {
            pageGroup.destroy();
            pageHolder.remove(pageGroup);
            pageGroup = null;
        }

        if (pages.isEmpty()) {
            pageIndex = 0;
        } else {
            pageIndex = Math.max(0, Math.min(index, pages.size() - 1));
        }

        pageGroup = new Group();
        pageHolder.add(pageGroup);

        YuanTaStory story = pages.isEmpty() ? null : pages.get(pageIndex);

        buildPage(story);

        //打开即视为已读
        if (story != null && story.unlockTime > SPDSettings.yuantuoLastRead()) {
            SPDSettings.yuantuoLastRead(story.unlockTime);
        }

        updateNavigation();
    }

    private void updateNavigation() {
        int total = pages.size();
        pageCounter.text(Messages.get(YuanTaStoneScene.class, "page", total == 0 ? 0 : pageIndex + 1, total));
        pageCounter.setPos(panelX + panelW - 6 - pageCounter.width(),
                panelY + 4 + (18 - pageCounter.height()) / 2f);
        align(pageCounter);

        prevBtn.enable(pageIndex > 0);
        nextBtn.enable(pageIndex < total - 1);

        long lastRead = SPDSettings.yuantuoLastRead();
        for (int i = 0; i < pageDots.size(); i++) {
            boolean unread = i < total && pages.get(i).unlockTime > lastRead;
            if (i == pageIndex) {
                pageDots.get(i).color(DOT_COLOR_CURRENT);
            } else if (unread) {
                pageDots.get(i).color(DOT_COLOR_UNREAD);
            } else {
                pageDots.get(i).color(DOT_COLOR_READ);
            }
        }
    }

    /** 在卷轴里画出当前这一页；story 为 null 时显示篇章的空状态。 */
    private void buildPage(YuanTaStory story) {
        if (pageW < 16 || pageH < 16) return;

        NinePatch scroll = Chrome.get(Chrome.Type.SCROLL);
        scroll.x = pageX;
        scroll.y = pageY;
        scroll.size(pageW, pageH);
        align(scroll);
        pageGroup.add(scroll);

        float ix = scroll.x + scroll.marginLeft();
        float iy = scroll.y + scroll.marginTop();
        float iw = scroll.innerWidth();
        float ih = scroll.innerHeight();
        if (iw <= 8 || ih <= 8) return;

        if (story == null) {
            buildEmptyPage(ix, iy, iw, ih);
            return;
        }

        ItemSprite icon = new ItemSprite(story.iconIdx, null);
        icon.x = ix;
        icon.y = iy + 1;
        align(icon);
        pageGroup.add(icon);

        float textX = ix + icon.width() + 4;
        float textW = Math.max(16, iw - icon.width() - 4);

        RenderedTextBlock storyTitle = renderTextBlock(getLangText(story.title), 9);
        storyTitle.maxWidth((int) textW);
        storyTitle.invert();
        storyTitle.setPos(textX, iy);
        align(storyTitle);
        pageGroup.add(storyTitle);

        RenderedTextBlock date = renderTextBlock(YuanTaStoryManager.formatTime(story.unlockTime), 6);
        date.invert();
        date.alpha(0.75f);
        date.setPos(textX, storyTitle.bottom() + 1);
        align(date);
        pageGroup.add(date);

        float headerBottom = Math.max(iy + icon.height(), date.bottom());

        ColorBlock divider = new ColorBlock(iw, 1, 0xFFFFFFFF);
        divider.color(0x6B563A);
        divider.alpha(0.55f);
        divider.x = ix;
        divider.y = headerBottom + 4;
        pageGroup.add(divider);

        float contentY = divider.y + 4;
        float contentH = iy + ih - contentY;
        if (contentH < 10) return;

        Component content = new Component();
        RenderedTextBlock body = renderTextBlock(getLangText(story.content), 7);
        body.maxWidth((int) Math.max(16, iw - 6));
        body.invert();
        body.setPos(0, 0);
        content.add(body);
        content.setSize(iw, body.height() + 4);

        ScrollPane pane = new ScrollPane(content);
        //先挂到页面上再布局，ScrollPane.layout() 需要能取到父级 camera
        pageGroup.add(pane);
        pane.setRect(ix, contentY, iw, contentH);
    }

    /** 空篇章：图标 + 篇章名 + 简介 + 提示，整块在卷轴内居中。 */
    private void buildEmptyPage(float ix, float iy, float iw, float ih) {
        ItemSprite icon = new ItemSprite(activeModule.iconIdx, null);
        icon.scale.set(1.5f);

        RenderedTextBlock name = renderTextBlock(activeModule.title(), 9);
        name.maxWidth((int) Math.max(16, iw - 8));
        name.align(RenderedTextBlock.CENTER_ALIGN);
        name.invert();

        RenderedTextBlock desc = renderTextBlock(activeModule.desc(), 7);
        desc.maxWidth((int) Math.max(16, iw - 8));
        desc.align(RenderedTextBlock.CENTER_ALIGN);
        desc.invert();
        desc.alpha(0.85f);

        RenderedTextBlock hint = renderTextBlock(Messages.get(YuanTaStoneScene.class, "empty_module"), 6);
        hint.maxWidth((int) Math.max(16, iw - 8));
        hint.align(RenderedTextBlock.CENTER_ALIGN);
        hint.invert();
        hint.alpha(0.7f);

        float blockH = icon.height() + 3 + name.height() + 4 + desc.height() + 8 + hint.height();
        float startY = iy + Math.max(2, (ih - blockH) / 2f);
        float bottom = iy + ih;

        icon.x = ix + (iw - icon.width()) / 2f;
        icon.y = startY;
        align(icon);
        pageGroup.add(icon);

        name.setPos(ix + (iw - name.width()) / 2f, icon.y + icon.height() + 3);
        align(name);
        pageGroup.add(name);

        desc.setPos(ix + (iw - desc.width()) / 2f, name.bottom() + 4);
        align(desc);

        if (desc.bottom() + 8 <= bottom) {
            pageGroup.add(desc);
        } else {
            desc.destroy();
            return;
        }

        hint.setPos(ix + (iw - hint.width()) / 2f, desc.bottom() + 8);
        align(hint);

        if (hint.bottom() + 2 <= bottom) {
            pageGroup.add(hint);
        } else {
            hint.destroy();
        }
    }

    // ====================== 文本解析 ======================

    /**
     * 故事标题/正文既可能是 “Ms:某个消息键”，也可能是已经本地化好的文本。
     * 先按场景作用域解析，再按全局键解析，最后原样返回，避免出现 "Ms:..." 字样。
     */
    static String getLangText(String key) {
        if (key == null || key.isEmpty()) return "???";
        String pureKey = key.startsWith("Ms:") ? key.substring(3) : key;

        String text = Messages.get(YuanTaStoneScene.class, pureKey);
        if (text != null && !text.startsWith("Ms:")) return text;

        text = Messages.get(pureKey);
        if (text != null && !text.startsWith("Ms:")) return text;

        return pureKey;
    }

    // ====================== 剧情实体 ======================

    public static class YuanTaStory {
        public String id;
        public String title;
        public String content;
        public String module;
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

    /** 一个“大模块”（篇章），例如渊落古堡。 */
    public static class YuanTaModule {
        public final String id;
        public final int iconIdx;

        public YuanTaModule(String id, int iconIdx) {
            this.id = id;
            this.iconIdx = iconIdx;
        }

        public String title() {
            return Messages.get(YuanTaStoneScene.class, "module_" + id);
        }

        public String desc() {
            return Messages.get(YuanTaStoneScene.class, "module_" + id + "_desc");
        }
    }

    // ====================== 剧情管理器（兼容原生Bundle） ======================
    public static class YuanTaStoryManager {

        private static final String FILE_NAME = "yuantuo_stories.dat";

        /** 篇章顺序即界面中的展示顺序。 */
        public static final YuanTaModule[] MODULES = new YuanTaModule[]{
                new YuanTaModule("main", ItemSpriteSheet.MAGICGIRLBOOKS),
                new YuanTaModule("hollow", ItemSpriteSheet.CASTLE_AIRPORT),
                new YuanTaModule("losttomb", ItemSpriteSheet.TOMB)
        };

        private static final SimpleDateFormat STORY_DATE_FORMAT = new SimpleDateFormat("yyyy.MM.dd", Locale.ENGLISH);

        private static List<YuanTaStory> allStories = new ArrayList<>();

        static {
            loadStories();
        }

        public static List<YuanTaStory> getUnlockedStories() {
            return new ArrayList<>(allStories);
        }

        /** 某个篇章下已解锁的故事，按“置顶优先、时间正序”排列，恰好是翻页顺序。 */
        public static List<YuanTaStory> getStories(String moduleId) {
            ArrayList<YuanTaStory> result = new ArrayList<>();
            for (YuanTaStory story : allStories) {
                if (moduleOf(story).id.equals(moduleId)) {
                    result.add(story);
                }
            }
            Collections.sort(result, (a, b) -> {
                if (a.isTop != b.isTop) return a.isTop ? -1 : 1;
                return Long.compare(a.unlockTime, b.unlockTime);
            });
            return result;
        }

        /** 根据故事 id/标题推断所属篇章，兼容旧存档（旧存档没有 module 字段）。 */
        public static String resolveModuleId(String storyId, String title) {
            String s = ((storyId == null ? "" : storyId) + " " + (title == null ? "" : title))
                    .toLowerCase(Locale.ENGLISH);
            if (s.contains("hollow") || s.contains("castle") || s.contains("gufu") || s.contains("gubao")) {
                return "hollow";
            }
            if (s.contains("icetown") || s.contains("snow")) {
                return "icetown";
            }
            return "main";
        }

        public static YuanTaModule moduleOf(YuanTaStory story) {
            if (story.module != null) {
                for (YuanTaModule module : MODULES) {
                    if (module.id.equals(story.module)) return module;
                }
            }
            story.module = resolveModuleId(story.id, story.title);
            for (YuanTaModule module : MODULES) {
                if (module.id.equals(story.module)) return module;
            }
            return MODULES[MODULES.length - 1];
        }

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

                    String module = b.getString("module");
                    story.module = isValidModule(module)
                            ? module
                            : resolveModuleId(story.id, story.title);

                    allStories.add(story);
                }
            } catch (Exception e) {
                allStories = new ArrayList<>();
            }
        }

        private static boolean isValidModule(String id) {
            if (id == null || id.isEmpty()) return false;
            for (YuanTaModule module : MODULES) {
                if (module.id.equals(id)) return true;
            }
            return false;
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
                    b.put("module", s.module == null ? resolveModuleId(s.id, s.title) : s.module);
                    b.put("time", s.unlockTime);
                    b.put("isTop", s.isTop);
                    b.put("iconIdx", s.iconIdx);
                    bundle.put("story_" + i, b);
                }

                FileUtils.bundleToFile(FILE_NAME, bundle);
            } catch (Exception ignored) {
            }
        }

        public static void unlockStory(String storyId, String title, String content, int iconIdx) {
            unlockStory(storyId, null, title, content, iconIdx);
        }

        /**
         * @param moduleId 所属篇章 id，传 null 时按故事 id/标题自动推断
         */
        public static void unlockStory(String storyId, String moduleId, String title, String content, int iconIdx) {
            // 第一步：检查是否已解锁
            for (YuanTaStory s : allStories) {
                if (s.id != null && s.id.equals(storyId)) {
                    return;
                }
            }
            GLog.b(Messages.get(YuanTaStoneScene.class, "time_record", getLangText(title)));

            YuanTaStory story = new YuanTaStory(storyId, title, content, false, iconIdx);
            story.module = isValidModule(moduleId) ? moduleId : resolveModuleId(storyId, title);
            allStories.add(story);
            saveStories();
        }

        /** 全局数据被外部覆盖后调用：丢掉内存列表，重新从磁盘读 */
        public static void reload() {
            loadStories();   // 成功失败都会给 allStories 赋非 null 列表，不用先置 null
        }
    }
}
