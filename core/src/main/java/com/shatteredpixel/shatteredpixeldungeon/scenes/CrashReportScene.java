package com.shatteredpixel.shatteredpixeldungeon.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.shatteredpixel.shatteredpixeldungeon.Chrome;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.CrashHandler;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.Archs;
import com.shatteredpixel.shatteredpixeldungeon.ui.ExitButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.ScrollPane;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.windows.IconTitle;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Image;
import com.watabou.noosa.NinePatch;
import com.watabou.noosa.ui.Component;

import java.util.ArrayList;

public class CrashReportScene extends PixelScene {

    private final ArrayList<CrashInfo> infos = new ArrayList<>();

    // 响应式布局参数
    private static final int WIDTH_P = 120;
    private static final int WIDTH_L = 160;
    private static final int MARGIN = 8;
    private static final int GAP = 4;

    @Override
    public void create() {
        super.create();

        int w = Camera.main.width;
        int h = Camera.main.height;

        // 响应式布局计算
        boolean isLandscape = SPDSettings.landscape() != null;
        int panelWidth = isLandscape ? WIDTH_L : WIDTH_P;
        int panelHeight = h-20;

        // 现代化标题
        RenderedTextBlock title = PixelScene.renderTextBlock(Messages.get(this, "title"), 11);
        title.hardlight(0x88CCEE);
        title.setPos((w - title.width()) / 2f, MARGIN);
        align(title);
        add(title);

        // 退出按钮
        ExitButton btnExit = new ExitButton();
        btnExit.setPos(w - btnExit.width() - MARGIN, MARGIN);
        add(btnExit);

        // 清除按钮 - 现代化设计
        RedButton btnDelete = new RedButton(Messages.get(this, "clear"), 8) {
            @Override
            protected void onClick() {
                try {
                    FileHandle crashDir = Gdx.files.local(CrashHandler.CRASH_DIR);
                    if (crashDir.exists()) {
                        FileHandle[] files = crashDir.list();
                        if (files != null) {
                            for (FileHandle file : files) {
                                if (file.name().endsWith(CrashHandler.CRASH_FILE_EXTENSION)) {
                                    file.delete();
                                }
                            }
                        }
                        if (crashDir.list().length == 0) {
                            crashDir.deleteDirectory();
                        }
                    }
                    Gdx.app.log(CrashHandler.TAG, "All crash logs cleared");
                    onBackPressed();
                } catch (Exception e) {
                    Gdx.app.error(CrashHandler.TAG, "Failed to clear crash logs", e);
                }
            }
        };
        btnDelete.icon(Icons.get(Icons.WARNING));
        btnDelete.setSize(50, 20);
        btnDelete.setPos(MARGIN, MARGIN);
        add(btnDelete);

        // 现代化面板
        NinePatch panel = Chrome.get(Chrome.Type.WINDOW_SILVER);
        panel.size(panelWidth, panelHeight);
        panel.x = (w - panelWidth) / 2f;
        panel.y = title.bottom() + GAP;
        align(panel);
        add(panel);

        // 滚动列表
        ScrollPane list = new ScrollPane(new Component()) {
            @Override
            public void onClick(float x, float y) {
                for (CrashInfo info : infos) {
                    if (info.onClick(x, y)) {
                        return;
                    }
                }
            }
        };
        add(list);

        ArrayList<CrashHandler.ExceptionStrings> exceptions = new ArrayList<>();

        // 检查崩溃日志
        FileHandle crashDir = Gdx.files.local(CrashHandler.CRASH_DIR);
        if (crashDir.exists()) {
            try {
                FileHandle[] files = crashDir.list();
                if (files != null && files.length > 0) {
                    for (FileHandle file : files) {
                        if (file.name().endsWith(CrashHandler.CRASH_FILE_EXTENSION)) {
                            String crashContent = file.readString();
                            exceptions.add(new CrashHandler.ExceptionStrings(
                                    file.name(),
                                    crashContent,
                                    "Crash log from " + file.name()
                            ));
                        }
                    }
                } else {
                    showNoCrashMessage(w, h, Messages.get(this, "no_crashes"), 0x88CCEE);
                    return;
                }
            } catch (Exception e) {
                ShatteredPixelDungeon.reportException(e);
                showNoCrashMessage(w, h, Messages.get(this, "read_error"), 0xFF5555);
                return;
            }
        } else {
            showNoCrashMessage(w, h, Messages.get(this, "no_crashes"), 0x88CCEE);
            return;
        }

        // 创建崩溃信息列表
        CrashInfo info = new CrashInfo();
        for (CrashHandler.ExceptionStrings expt : exceptions) {
            CrashReportButton crb = new CrashReportButton(expt);
            info.addButton(crb);
        }
        infos.add(info);

        // 设置列表内容
        Component content = list.content();
        content.clear();

        float posY = 0;
        for (CrashInfo info1 : infos) {
            info1.setRect(0, posY, panel.innerWidth(), 0);
            content.add(info1);
            posY += info1.height() + GAP;
        }

        content.setSize(panel.innerWidth(), (int) Math.ceil(posY));

        // 设置列表位置和大小
        list.setRect(
                panel.x + panel.marginLeft(),
                panel.y + panel.marginTop(),
                panel.innerWidth(),
                panel.innerHeight());
        list.scrollTo(0, 0);

        // 添加现代化背景
        Archs archs = new Archs();
        archs.setSize(w, h);
        addToBack(archs);

        // 添加底部说明文字
        RenderedTextBlock footer = PixelScene.renderTextBlock(Messages.get(this, "footer"), 6);
        footer.hardlight(0x999999);
        footer.setPos((w - footer.width()) / 2f, h - footer.height() - MARGIN);
        align(footer);
        add(footer);

        fadeIn();
    }

    private void showNoCrashMessage(int w, int h, String message, int color) {
        RenderedTextBlock msg = PixelScene.renderTextBlock(message, 8);
        msg.hardlight(color);
        msg.setPos((w - msg.width()) / 2f, (h - msg.height()) / 2f);
        align(msg);
        add(msg);
    }

    @Override
    protected void onBackPressed() {
        ShatteredPixelDungeon.switchNoFade(TitleScene.class);
    }

    private static class CrashInfo extends Component {
        private ArrayList<CrashReportButton> buttons = new ArrayList<>();

        public void addButton(CrashReportButton button) {
            buttons.add(button);
            add(button);
            layout();
        }

        public boolean onClick(float x, float y) {
            // 转换坐标到组件本地坐标系
            float localX = x - this.x;
            float localY = y - this.y;

            for (CrashReportButton button : buttons) {
                // 检查点击位置是否在按钮范围内
                if (localX >= button.x && localX <= button.x + button.width() &&
                        localY >= button.y && localY <= button.y + button.height()) {
                    button.onClick();
                    return true;
                }
            }
            return false;
        }

        @Override
        protected void layout() {
            float posY = GAP;
            float maxWidth = 0;

            for (CrashReportButton button : buttons) {
                button.setPos(GAP, posY);
                posY += button.height() + GAP;
                maxWidth = Math.max(maxWidth, button.width());
            }

            height = posY;
            width = maxWidth + GAP * 2;
        }
    }

    private static class CrashReportButton extends Component {
        protected Image icon;
        protected CrashHandler.ExceptionStrings es;
        protected RenderedTextBlock title;
        protected RenderedTextBlock timestamp;
        protected NinePatch bg;

        public CrashReportButton(CrashHandler.ExceptionStrings es) {
            super();

            // 现代化背景
            bg = Chrome.get(Chrome.Type.SCROLL);
            add(bg);

            this.icon = Icons.get(Icons.WARNING);
            this.icon.hardlight(0xFFCC00);
            add(this.icon);

            this.es = es;

            // 解析文件名获取时间戳
            String displayName = es.fileName;
            String timeInfo = "";
            try {
                if (es.fileName.contains("_")) {
                    String[] parts = es.fileName.split("_");
                    if (parts.length >= 2) {
                        timeInfo = parts[1].replace(".crash", "");
                        displayName = "Crash " + timeInfo;
                    }
                }
            } catch (Exception e) {
                // 忽略解析错误
            }

            title = PixelScene.renderTextBlock(displayName, 8);
            title.hardlight(0xFFFFFF);
            add(title);

            if (!timeInfo.isEmpty()) {
                timestamp = PixelScene.renderTextBlock(timeInfo, 6);
                timestamp.hardlight(0xCCCCCC);
                add(timestamp);
            }

            layout();
        }

        protected void onClick() {
            ModernCrashReportWindow window = new ModernCrashReportWindow(es);
            ShatteredPixelDungeon.scene().add(window);
        }

        @Override
        protected void layout() {
            super.layout();

            bg.x = x;
            bg.y = y;

            icon.x = x + GAP;
            icon.y = y + (height - icon.height()) / 2f;
            PixelScene.align(icon);

            title.maxWidth((int)(width - icon.width - GAP * 3));
            title.setPos(icon.x + icon.width + GAP, y + GAP);

            if (timestamp != null) {
                timestamp.maxWidth((int)(width - icon.width - GAP * 3));
                timestamp.setPos(title.left(), title.bottom() + 2);
                height = Math.max(height, timestamp.bottom() - y + GAP);
            } else {
                height = Math.max(height, title.bottom() - y + GAP);
            }

            bg.size(100, height);
        }

        @Override
        public float width() {
            return Math.max(super.width(), title.width() + icon.width() + GAP * 3);
        }

        @Override
        public float height() {
            return Math.max(24, super.height());
        }
    }

/**
 * 现代化崩溃报告窗口类，用于显示应用程序崩溃时的详细信息
 * 该类继承自Window，提供了格式化的崩溃信息展示和复制功能
 */
private static class ModernCrashReportWindow extends Window {

    public ModernCrashReportWindow(CrashHandler.ExceptionStrings es) {
        super();

        int width = 200;
        int height = 210;
        resize(width, height);

        // 现代化标题栏
        IconTitle titlebar = new IconTitle(Icons.get(Icons.WARNING),
                Messages.get(CrashReportScene.class, "details_title"));
        titlebar.color(0xFFCC00);
        titlebar.setRect(0, 0, width, 0);
        add(titlebar);

        // 格式化崩溃信息
        String formattedMessage = formatCrashMessage(es.message, es.stackTrace);

        // 创建文本块并设置自动换行
        RenderedTextBlock text = PixelScene.renderTextBlock(formattedMessage, 6);
        text.maxWidth(width - GAP * 3); // 确保文本块宽度适应窗口
        text.setPos(GAP, titlebar.bottom() + GAP);

        // 滚动视图
        ScrollPane list = new ScrollPane(new Component());
        add(list);

        Component content = list.content();
        content.clear();
        content.add(text);
        content.setSize(width - GAP * 2, text.height() + GAP * 3+20);
        list.setRect(GAP, titlebar.bottom(), width - GAP * 2, height - titlebar.height() - GAP);
        list.scrollTo(0, 0);

        // 复制按钮
        RedButton copyBtn = new RedButton(Messages.get(CrashReportScene.class, "copy"), 8) {
            @Override
            protected void onClick() {
                Gdx.app.getClipboard().setContents(es.message + "\n" + es.stackTrace);
            }
        };
        copyBtn.icon(Icons.get(Icons.COPY));
        copyBtn.setSize(40, 16);
        copyBtn.setPos(width - copyBtn.width() - GAP, height - copyBtn.height() - GAP);
        add(copyBtn);
    }

    private String formatCrashMessage(String message, String stackTrace) {
        StringBuilder sb = new StringBuilder();
        if (message != null && !message.isEmpty()) {
            sb.append("Message: ").append(message).append("\n\n");
        }
        if (stackTrace != null) {
            String[] lines = stackTrace.split("\n");
            sb.append("Stack Trace:\n");
            for (int i = 0; i < Math.min(lines.length, 15); i++) {
                sb.append(lines[i]).append("\n");
            }
            if (lines.length > 15) {
                sb.append("... (").append(lines.length - 15).append(" more lines)");
            }
        }
        return sb.toString();
    }
}
}
