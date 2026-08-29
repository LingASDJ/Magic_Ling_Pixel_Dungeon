package com.shatteredpixel.shatteredpixeldungeon.scenes;

import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.effects.Flare;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.Archs;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.ScrollPane;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndError;
import com.watabou.input.PointerEvent;
import com.watabou.noosa.Camera;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.PointerArea;
import com.watabou.noosa.ui.Component;

/**
 * EULA 同意场景
 * 布局：顶部固定标题 | 中间可滚动文本 | 底部固定按钮
 *
 * 关键：ScrollPane 只占据中间区域，标题和按钮在 ScrollPane 之外独立存在
 */
public class EulaScene extends PixelScene {

    /* ===================== 常量 ===================== */
    private static final float COL_WIDTH         = 120f;
    private static final float BUTTON_WIDTH      = 40f;
    private static final float BUTTON_HEIGHT     = 18f;
    private static final float BUTTON_SPACING    = 10f;
    private static final float TITLE_TOP_PAD     = 10f;
    private static final float CONTENT_PAD       = 10f;
    private static final float BUTTON_BOTTOM_PAD = 10f;
    private static final int   OVERLAY_COLOR     = 0x88000000;

    /* ===================== 成员 ===================== */
    private int w, h;
    private float fullWidth;

    /* ===================== 生命周期 ===================== */

    @Override
    public void create() {
        super.create();
        initDimensions();
        buildBackground();

        // 关键：先构建 ScrollPane（它在底层）
        // 标题和按钮后构建（覆盖在 ScrollPane 之上）
        // 这样标题和按钮的触摸不会被 ScrollPane 拦截

        // 1. 先计算各区域边界
        float titleY = TITLE_TOP_PAD;
        float buttonY = h - BUTTON_HEIGHT - BUTTON_BOTTOM_PAD;

        // 2. 构建中间滚动区域（先添加，在底层）
        buildScrollContent(titleY, buttonY);

        // 3. 构建顶部标题（后添加，覆盖上层）
        buildTitle(titleY);

        // 4. 构建底部按钮（最后添加，最上层）
        buildButtons(buttonY);
    }

    @Override
    protected void onBackPressed() {
        ShatteredPixelDungeon.scene().add(
                new WndError(Messages.get(this, "need_read"))
        );
    }

    /* ===================== 构建步骤 ===================== */

    /** 计算屏幕尺寸与布局参数 */
    private void initDimensions() {
        w = Camera.main.width;
        h = Camera.main.height;
        fullWidth = COL_WIDTH * (landscape() ? 2 : 1);
    }

    /** 搭建背景：拱门 + 暗色遮罩 */
    private void buildBackground() {
        Archs archs = new Archs();
        archs.setSize(w, h);
        add(archs);
        add(new ColorBlock(w, h, OVERLAY_COLOR));
    }

    /**
     * 顶部固定标题
     * @param y 标题顶部 Y 坐标
     */
    private void buildTitle(float y) {
        CreditsBlock titleBlock = new CreditsBlock(
                false,
                Window.TITLE_COLOR,
                Messages.get(this, "title"),
                null,
                Messages.get(this, "desc"),
                null,
                null
        );

        float x = (w - fullWidth) / 2f - (landscape() ? 6 : 0);
        titleBlock.setRect(x, y, fullWidth, 0);
        add(titleBlock);
    }

    /**
     * 底部固定按钮
     * @param y 按钮顶部 Y 坐标
     */
    private void buildButtons(float y) {
        float totalWidth = BUTTON_WIDTH * 2 + BUTTON_SPACING;
        float startX = (w - totalWidth) / 2f;

        RedButton accept = new AcceptButton();
        RedButton reject = new RejectButton();

        accept.setRect(startX, y, BUTTON_WIDTH, BUTTON_HEIGHT);
        reject.setRect(startX + BUTTON_WIDTH + BUTTON_SPACING, y, BUTTON_WIDTH, BUTTON_HEIGHT);

        add(accept);
        add(reject);
    }

    /**
     * 中间可滚动文本区域
     * @param titleBottom 标题区域底部 Y（滚动区域顶部）
     * @param buttonTop   按钮区域顶部 Y（滚动区域底部）
     */
    private void buildScrollContent(float titleBottom, float buttonTop) {
        // 滚动区域 = 标题下方 + 间距  ~  按钮上方 - 间距
        float scrollTop = titleBottom + CONTENT_PAD;
        float scrollBottom = buttonTop - CONTENT_PAD;
        float scrollHeight = scrollBottom - scrollTop;

        // 安全校验：确保滚动区域有效
        if (scrollHeight < 30) scrollHeight = 30;

        ScrollPane scrollPane = new ScrollPane(new Component());
        add(scrollPane);

        Component content = scrollPane.content();
        content.clear();

        // EULA 详细文本
        CreditsBlock eulaText = new CreditsBlock(
                false,
                Window.TITLE_COLOR,
                null,                           // 无标题，纯正文
                null,
                Messages.get(this, "eula_text"), // 长协议文本
                null,
                null
        );

        // 文本在 content 内居中
        float textX = (w - fullWidth) / 2f;
        eulaText.setRect(textX, 0, fullWidth, 0);
        content.add(eulaText);

        // 强制布局获取准确高度，再设置 content 尺寸
        eulaText.layout();
        content.setSize(w, eulaText.height());

        // 设置 ScrollPane 显示区域：严格限制在中间
        scrollPane.setRect(0, scrollTop, w, scrollHeight);
        scrollPane.scrollTo(0, 0);
    }

    /* ===================== 按钮行为 ===================== */

    /** 接受按钮：启用 Firebase 并进入游戏 */
    private static class AcceptButton extends RedButton {
        AcceptButton() {
            super(Messages.get(EulaScene.class, "accept"));
        }

        @Override
        protected void onClick() {
            SPDSettings.firebase(true);
            SPDSettings.firebaseRecords(true);
            ShatteredPixelDungeon.switchScene(GoScene.class);
        }
    }

    /** 拒绝按钮：直接退出应用 */
    private static class RejectButton extends RedButton {
        RejectButton() {
            super(Messages.get(EulaScene.class, "reject"));
        }

        @Override
        protected void onClick() {
            Game.instance.finish();
        }
    }

    /* ===================== CreditsBlock（保持不变）==================== */

    private static class CreditsBlock extends Component {

        boolean large;
        RenderedTextBlock title;
        Image avatar;
        Flare flare;
        RenderedTextBlock body;
        RenderedTextBlock link;
        ColorBlock linkUnderline;
        PointerArea linkButton;

        private CreditsBlock(boolean large, int highlight, String title,
                             Image avatar, String body, String linkText, String linkUrl) {
            super();
            this.large = large;

            if (title != null) {
                this.title = PixelScene.renderTextBlock(title, large ? 8 : 6);
                if (highlight != -1) this.title.hardlight(highlight);
                add(this.title);
            }

            if (avatar != null) {
                this.avatar = avatar;
                add(this.avatar);
            }

            if (large && highlight != -1 && this.avatar != null) {
                this.flare = new Flare(7, 24).color(highlight, true).show(this.avatar, 0);
                this.flare.angularSpeed = 20;
            }

            this.body = PixelScene.renderTextBlock(body, 6);
            if (highlight != -1) this.body.setHightlighting(true, highlight);
            if (large) this.body.align(RenderedTextBlock.LEFT_ALIGN);
            add(this.body);

            if (linkText != null && linkUrl != null) {
                int color = (highlight != -1) ? (0xFF000000 | highlight) : 0xFFFFFFFF;
                this.linkUnderline = new ColorBlock(1, 1, color);
                add(this.linkUnderline);

                this.link = PixelScene.renderTextBlock(linkText, 6);
                if (highlight != -1) this.link.hardlight(highlight);
                add(this.link);

                linkButton = new PointerArea(0, 0, 0, 0) {
                    @Override
                    protected void onClick(PointerEvent event) {
                        ShatteredPixelDungeon.platform.openURI(linkUrl);
                    }
                };
                add(linkButton);
            }
        }

        @Override
        protected void layout() {
            super.layout();
            float topY = top();

            if (title != null) {
                title.maxWidth((int) width());
                title.setPos(x + (width() - title.width()) / 2f, topY);
                topY += title.height() + (large ? 2 : 1);
            }

            if (large) {
                if (avatar != null) {
                    avatar.x = x + (width() - avatar.width()) / 2f;
                    avatar.y = topY;
                    PixelScene.align(avatar);
                    if (flare != null) flare.point(avatar.center());
                    topY = avatar.y + avatar.height() + 2;
                }
                body.maxWidth((int) width());
                body.setPos(x + (width() - body.width()) / 2f, topY);
                topY += body.height() + 2;
            } else {
                if (avatar != null) {
                    avatar.x = x;
                    body.maxWidth((int) (width() - avatar.width - 1));
                    float fullAvHeight = Math.max(avatar.height(), 16);
                    if (fullAvHeight > body.height()) {
                        avatar.y = topY + (fullAvHeight - avatar.height()) / 2f;
                        PixelScene.align(avatar);
                        body.setPos(avatar.x + avatar.width + 1, topY + (fullAvHeight - body.height()) / 2f);
                        topY += fullAvHeight + 1;
                    } else {
                        avatar.y = topY + (body.height() - fullAvHeight) / 2f;
                        PixelScene.align(avatar);
                        body.setPos(avatar.x + avatar.width + 1, topY);
                        topY += body.height() + 2;
                    }
                } else {
                    topY += 1;
                    body.maxWidth((int) width());
                    body.setPos(x, topY);
                    topY += body.height() + 2;
                }
            }

            if (link != null) {
                if (large) topY += 1;
                link.maxWidth((int) width());
                link.setPos(x + (width() - link.width()) / 2f, topY);
                topY += link.height() + 2;

                linkButton.x = link.left() - 1;
                linkButton.y = link.top() - 1;
                linkButton.width = link.width() + 2;
                linkButton.height = link.height() + 2;

                linkUnderline.size(link.width(), PixelScene.align(0.49f));
                linkUnderline.x = link.left();
                linkUnderline.y = link.bottom() + 1;
            }

            topY -= 2;
            height = Math.max(height, topY - top());
        }
    }
}