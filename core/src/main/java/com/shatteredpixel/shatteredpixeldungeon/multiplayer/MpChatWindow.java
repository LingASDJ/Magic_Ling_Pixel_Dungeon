package com.shatteredpixel.shatteredpixeldungeon.multiplayer;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.shatteredpixel.shatteredpixeldungeon.Chrome;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.IconButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.ScrollPane;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.input.PointerEvent;
import com.watabou.noosa.Image;
import com.watabou.noosa.TextInput;
import com.watabou.noosa.ui.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 联机聊天窗口：可滚动的历史记录 + 输入框 + 发送按钮。
 *
 * <p>与旧的事件栏聊天不同，本窗口保留本次联机会话的聊天记录（最多
 * {@link Mp#CHAT_MAX_LINES} 条），关闭窗口后仍继续记录，下次打开可查看。
 * 加入/离开/死亡等系统提示仍走游戏事件栏，不进入本窗口。</p>
 */
public class MpChatWindow extends Window {

    /** 横屏窗口设计宽度；竖屏会自动铺满屏幕宽度。 */
    private static final float WIDTH_LANDSCAPE = 240f;
    /** 消息区设计高度。 */
    private static final float LIST_HEIGHT = 110f;
    /** 输入行设计高度。 */
    private static final float INPUT_HEIGHT = 34f;
    /** 发送按钮设计宽度。 */
    private static final float SEND_WIDTH = 46f;
    /** 关闭按钮设计尺寸。 */
    private static final float CLOSE_SIZE = 16f;
    /** 边距。 */
    private static final float MARGIN = 3f;
    /** 消息之间的间距。 */
    private static final float LINE_GAP = 2f;

    /** 自己发送的消息：粉色。 */
    private static final int COLOR_SELF = Window.Pink_COLOR;
    /** 其他玩家：紫色。 */
    private static final int COLOR_OTHER = 0xB06BE0;
    /** 特殊事件/系统提示：金色。 */
    private static final int COLOR_SYSTEM = 0xFFD700;

    /** 复用同一个 Pixmap，让 TextureCache 能缓存聊天图标纹理。 */
    private static Pixmap chatIconPixmap;

    private final Runnable onClose;
    private final ScrollPane list;
    private final Component content;
    private final TextInput input;
    private final RedButton sendBtn;

    private final List<RenderedTextBlock> blocks = new ArrayList<>();
    private final List<Long> blockIds = new ArrayList<>();
    private RenderedTextBlock emptyText;

    private float listTop;
    private float listHeight;
    private long lastVersion = -1L;
    private boolean destroyed = false;
    /** 自己发送消息后强制滚到最新一条。 */
    private boolean scrollToBottomRequested = false;

    /**
     * @param onClose 窗口销毁时的回调（由联机核心清理打开状态）。
     */
    public MpChatWindow(Runnable onClose) {
        super();
        this.onClose = onClose;

        // 横屏保持设计宽度；竖屏铺满屏幕宽度（只留很小的两侧留白）
        float width = MpUi.windowWidth(WIDTH_LANDSCAPE, chrome.marginHor());
        float margin = MpUi.px(MARGIN);
        float closeSize = MpUi.px(CLOSE_SIZE);

        RenderedTextBlock title = PixelScene.renderTextBlock(
                Messages.get(Mp.class, "chat_title"), MpUi.font(9));
        title.hardlight(TITLE_COLOR);
        title.maxWidth((int) Math.max(MpUi.px(20f), width - margin * 2 - closeSize));
        title.setPos(margin, MpUi.px(2f));
        add(title);

        IconButton close = new IconButton(Icons.get(Icons.CLOSE)) {
            @Override
            protected void onClick() {
                if (list != null && (list.isDragging() || list.wasDragging())) return;
                hide();
            }
        };
        close.setRect(width - margin - closeSize, MpUi.px(1f), closeSize, closeSize);
        add(close);

        listTop = Math.max(title.bottom(), close.bottom()) + MpUi.px(3f);
        listHeight = MpUi.px(LIST_HEIGHT);
        float inputHeight = Math.max(MpUi.px(INPUT_HEIGHT), 18f);
        list = new ScrollPane(new Component());
        content = list.content();
        add(list);
        list.setRect(margin, listTop, width - margin * 2, listHeight);

        float rowY = listTop + listHeight + MpUi.px(4f);
        int textSize = Math.max(6, (int) PixelScene.uiCamera.zoom * MpUi.font(8));
        // 作者要求：聊天界面不要用 Scroll（卷轴）样式，输入框改用普通窗口样式。
        input = new TextInput(Chrome.get(Chrome.Type.WINDOW), false, textSize) {
            @Override
            public void enterPressed() {
                sendMessage();
            }
        };
        input.setMaxLength(200);
        // 手机端手动关闭软键盘后，TextField 可能仍保有焦点，单纯再次点击不会重新弹出键盘。
        // 在输入框内部点击时显式重新启用输入（并请求软键盘）。
        if (input.container != null && input.container.getActor() != null) {
            input.container.getActor().addListener(new InputListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    input.setInputEnabled(true);
                    return false;   // 不拦截 TextField 自身的光标/选择处理
                }
            });
        }
        add(input);

        sendBtn = new RedButton(Messages.get(Mp.class, "chat_send"), MpUi.font(8)) {
            @Override
            protected void onPointerDown() {
                super.onPointerDown();
                // 点发送按钮时不要把聊天输入框的键盘焦点清掉
                PointerEvent.clearKeyboardThisPress = false;
            }

            @Override
            protected void onPointerUp() {
                super.onPointerUp();
                PointerEvent.clearKeyboardThisPress = false;
            }

            @Override
            protected void onClick() {
                if (list != null && (list.isDragging() || list.wasDragging())) return;
                sendMessage();
            }
        };
        add(sendBtn);

        float inputWidth = width - margin * 2 - MpUi.px(SEND_WIDTH) - MpUi.px(4f);
        input.setRect(margin, rowY, inputWidth, inputHeight);
        sendBtn.setRect(margin + inputWidth + MpUi.px(4f), rowY,
                MpUi.px(SEND_WIDTH), inputHeight);

        // Window.resize 会把窗口居中到游戏画面正中央（局内/局外都适用）。
        // 这里不再对 Android 做“贴顶”偏移，避免聊天窗口位置随屏幕比例变化而偏移。
        resize((int) Math.ceil(width),
                (int) Math.ceil(rowY + inputHeight + margin));
        // Window.resize 会移动窗口内部 camera；输入框和滚动区需要按最终位置重排一次
        list.setRect(margin, listTop, width - margin * 2, listHeight);
        input.setRect(margin, rowY, inputWidth, inputHeight);
        sendBtn.setRect(margin + inputWidth + MpUi.px(4f), rowY,
                MpUi.px(SEND_WIDTH), inputHeight);

        lastVersion = Mp.get().getChatVersion();
        refreshMessages(true);
    }

    @Override
    public void update() {
        super.update();
        long version = Mp.get().getChatVersion();
        if (version != lastVersion) {
            lastVersion = version;
            refreshMessages(false);
        }
    }

    /** 发送输入框里的内容；发送后清空并保持窗口/输入焦点。 */
    private void sendMessage() {
        if (input == null) return;
        String msg = input.getText();
        input.setText("");
        input.setInputEnabled(true);
        if (msg == null || msg.trim().isEmpty()) return;
        scrollToBottomRequested = true;
        Mp.get().sendChat(msg);
    }

    /**
     * 按聊天记录快照增量刷新消息区。
     *
     * @param first 是否首次刷新（首次总是滚到最底部）。
     */
    private void refreshMessages(boolean first) {
        List<Mp.ChatLine> lines = Mp.get().getChatLines();
        float width = Math.max(MpUi.px(20f), list.width() - MpUi.px(2f));

        boolean atBottom = first || scrollToBottomRequested
                || content.height() <= list.height() + 1f
                || content.camera.scroll.y + list.height() >= content.height() - 1f;
        scrollToBottomRequested = false;

        // 丢弃已经超出 200 条上限的旧消息
        while (!blockIds.isEmpty()
                && (lines.isEmpty() || blockIds.get(0) < lines.get(0).id)) {
            removeBlock(0);
        }
        while (!lines.isEmpty() && blocks.size() > lines.size()) {
            removeBlock(0);
        }

        long lastId = blockIds.isEmpty() ? Long.MIN_VALUE : blockIds.get(blockIds.size() - 1);
        for (Mp.ChatLine line : lines) {
            if (line.id <= lastId) continue;
            addBlock(line, width);
        }

        if (lines.isEmpty()) {
            if (emptyText == null) {
                emptyText = PixelScene.renderTextBlock(
                        Messages.get(Mp.class, "chat_empty"), MpUi.font(7));
                emptyText.hardlight(0x888888);
                emptyText.maxWidth((int) width);
                emptyText.setPos(MpUi.px(2f), MpUi.px(4f));
                content.add(emptyText);
            }
        } else if (emptyText != null) {
            content.remove(emptyText);
            emptyText.destroy();
            emptyText = null;
        }

        float y = 0f;
        for (RenderedTextBlock b : blocks) {
            b.setPos(0, y);
            y = b.bottom() + MpUi.px(LINE_GAP);
        }
        if (emptyText != null) {
            emptyText.setPos(MpUi.px(2f), MpUi.px(4f));
            y = Math.max(y, emptyText.bottom() + MpUi.px(LINE_GAP));
        }

        content.setSize(width, Math.max(list.height(), y));
        // 内容尺寸变化后重新布局滚动区，更新滚动条并钳制滚动位置
        list.setRect(list.x, list.y, list.width(), list.height());

        float maxScroll = Math.max(0f, content.height() - list.height());
        if (atBottom) {
            list.scrollTo(0f, maxScroll);
        } else {
            list.scrollTo(0f, Math.min(content.camera.scroll.y, maxScroll));
        }
    }

    private void addBlock(Mp.ChatLine line, float width) {
        int color;
        switch (line.type) {
            case Mp.CHAT_TYPE_SELF:
                color = COLOR_SELF;
                break;
            case Mp.CHAT_TYPE_SYSTEM:
                color = COLOR_SYSTEM;
                break;
            default:
                color = COLOR_OTHER;
                break;
        }
        RenderedTextBlock block = PixelScene.renderTextBlock(MpUi.font(7));
        block.hardlight(color);
        block.text("[" + line.time + "] " + line.name + ": " + line.text, (int) width);
        block.setPos(0, 0);
        content.add(block);
        blocks.add(block);
        blockIds.add(line.id);
    }

    private void removeBlock(int index) {
        RenderedTextBlock block = blocks.remove(index);
        blockIds.remove(index);
        content.remove(block);
        block.destroy();
    }

    @Override
    public void onBackPressed() {
        // 滑动聊天记录后的抬手不关闭窗口
        if (list != null && (list.isDragging() || list.wasDragging())) return;
        super.onBackPressed();
    }

    @Override
    public void destroy() {
        super.destroy();
        if (!destroyed) {
            destroyed = true;
            if (onClose != null) onClose.run();
        }
    }

    /**
     * 聊天按钮图标（16×16 像素对话气泡，金色）。
     *
     * @return 返回值。
     */
    public static Image chatIcon() {
        if (chatIconPixmap == null) {
            Pixmap pm = new Pixmap(16, 16, Pixmap.Format.RGBA8888);
            pm.setBlending(Pixmap.Blending.None);
            pm.setColor(0f, 0f, 0f, 0f);
            pm.fill();
            pm.setColor(1f, 1f, 1f, 1f);
            // 气泡主体
            pm.fillRectangle(1, 2, 14, 9);
            // 小尾巴
            pm.fillTriangle(4, 11, 8, 11, 4, 14);
            // 去掉四角，形成圆角
            pm.setColor(0f, 0f, 0f, 0f);
            pm.fillRectangle(1, 2, 1, 1);
            pm.fillRectangle(14, 2, 1, 1);
            pm.fillRectangle(1, 10, 1, 1);
            pm.fillRectangle(14, 10, 1, 1);
            // 三个省略号点（挖空）
            pm.fillRectangle(4, 5, 2, 2);
            pm.fillRectangle(7, 5, 2, 2);
            pm.fillRectangle(10, 5, 2, 2);
            chatIconPixmap = pm;
        }
        Image icon = new Image(chatIconPixmap);
        icon.hardlight(0xFFD700);
        return icon;
    }
}
