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
import com.shatteredpixel.shatteredpixeldungeon.windows.WndMessage;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndTextInput;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Image;
import com.watabou.noosa.NinePatch;
import com.watabou.noosa.ui.Component;
import com.watabou.utils.DeviceCompat;

import java.util.ArrayList;
import java.util.Collections;

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
        RedButton btnDelete = new RedButton(Messages.get(this, "clear"), 7) {
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
                    onBackPressed();
                } catch (Exception e) {
                    onBackPressed();
                }
            }
        };
        btnDelete.setSize(30, 20);
        btnDelete.setPos(2, MARGIN);
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
                    // 创建临时列表存储带时间戳的崩溃报告
                    ArrayList<CrashReportWithTime> crashReports = new ArrayList<>();

                    for (FileHandle file : files) {
                        if (file.name().endsWith(CrashHandler.CRASH_FILE_EXTENSION)) {
                            String crashContent = file.readString();
                            String crashTime = extractCrashTime(crashContent);
                            crashReports.add(new CrashReportWithTime(
                                    file.name(),
                                    crashContent,
                                    "Crash log from " + file.name(),
                                    crashTime
                            ));
                        }
                    }

                    // 按时间戳降序排序（最新的在前）
                    Collections.sort(crashReports, (o1, o2) -> o2.timestamp.compareTo(o1.timestamp));

                    // 转换为ExceptionStrings列表
                    for (CrashReportWithTime report : crashReports) {
                        exceptions.add(new CrashHandler.ExceptionStrings(
                                report.fileName,
                                report.content,
                                report.title
                        ));
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

    // 辅助类：带时间戳的崩溃报告
    private static class CrashReportWithTime {
        String fileName;
        String content;
        String title;
        String timestamp;

        CrashReportWithTime(String fileName, String content, String title, String timestamp) {
            this.fileName = fileName;
            this.content = content;
            this.title = title;
            this.timestamp = timestamp;
        }
    }

    // 辅助方法：从崩溃内容中提取时间戳
    private String extractCrashTime(String crashContent) {
        String[] lines = crashContent.split("\n");
        for (String line : lines) {
            if (line.startsWith("Time: ")) {
                return line.substring(6).trim();
            }
        }
        return "0000-00-00 00:00"; // 默认值，确保没有时间戳的记录排在最后
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
            bg = Chrome.get(Chrome.Type.WINDOW_SILVER);
            add(bg);

            this.icon = Icons.get(Icons.WARNING);
            this.icon.hardlight(0xFFCC00);
            add(this.icon);

            this.es = es;

            // 从堆栈跟踪中提取时间戳
            String crashTime = "";
            String[] lines = es.stackTrace.split("\n");
            for (String line : lines) {
                if (line.startsWith("Time: ")) {
                    crashTime = line.substring(6).trim();
                    break;
                }
            }

            title = PixelScene.renderTextBlock(Messages.get(CrashReportScene.class,"crash_error"), 6);
            title.hardlight(0xFFFFFF);
            add(title);

            timestamp = PixelScene.renderTextBlock(crashTime, 6);
            timestamp.hardlight(0xCCCCCC);
            add(timestamp);

            layout();
        }

        protected void onClick() {
            if(DeviceCompat.isAndroid()){
                StringBuilder sb = new StringBuilder();
                String[] lines = es.stackTrace.split("\n");
                int startIdx = 0;
                for (int i = 0; i < lines.length; i++) {
                    if (!lines[i].startsWith("Crash log from")) {
                        startIdx = i;
                        break;
                    }
                }
                for (int i = startIdx; i < lines.length; i++) {
                    if (lines[i].equals("=== NOTES ===")) break;
                    sb.append(lines[i]).append("\n");
                }

                com.shatteredpixel.shatteredpixeldungeon.android.AndroidLauncher.showNativeCrashDialog(sb.toString());
            } else {
                ModernCrashReportWindow window = new ModernCrashReportWindow(es);
                ShatteredPixelDungeon.scene().add(window);
            }
        }



        @Override
        protected void layout() {
            super.layout();

            bg.x = x;
            bg.y = y;

            icon.x = x + GAP;
            icon.y = y + (height - icon.height()) / 2f;
            PixelScene.align(icon);

            title.setPos(icon.x + icon.width + GAP, y + GAP);

            if (timestamp != null) {
                timestamp.setPos(title.left(), title.bottom() + 2);
                height = Math.max(height, timestamp.bottom() - y + GAP);
            } else {
                height = Math.max(height, title.bottom() - y + GAP);
            }

            bg.size(DeviceCompat.isAndroid() ? 140 : 100, height);
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

        private RenderedTextBlock notesText;
        private String notesContent = "";
        private final CrashHandler.ExceptionStrings es;
        private static final String NOTES_SEPARATOR = "\n=== NOTES ===\n";

        public ModernCrashReportWindow(CrashHandler.ExceptionStrings es) {
            super();
            this.es = es;

            // 读取已保存的备注
            loadNotes();

            int width = Camera.main.width-50;
            int height = Camera.main.height-50;
            resize(width, height);

            // 现代化标题栏
            IconTitle titlebar = new IconTitle(Icons.get(Icons.WARNING),
                    Messages.get(CrashReportScene.class, "details_title"));
            titlebar.color(0xFFCC00);
            titlebar.setRect(0, 0, width, 0);
            add(titlebar);

            // 创建滚动容器
            Component content = new Component();
            ScrollPane list = new ScrollPane(content);
            add(list);

            // 格式化崩溃信息
            String formattedMessage = formatCrashMessage(es.message, es.stackTrace);
            RenderedTextBlock text = PixelScene.renderTextBlock(formattedMessage, 6);
            text.maxWidth(width - GAP * 3);
            content.add(text);
            text.setPos(GAP, 0);

            float currentY = text.height() + GAP;

            // 添加问题备注部分
            RenderedTextBlock notesTitle = PixelScene.renderTextBlock(Messages.get(CrashReportScene.class, "notes_title"), 6);
            notesTitle.hardlight(0xFFCC00);
            notesTitle.setPos(GAP, currentY);
            content.add(notesTitle);
            currentY += notesTitle.height() + GAP;

            notesText = PixelScene.renderTextBlock("", 6);
            notesText.maxWidth(width - GAP * 3);
            notesText.setPos(GAP, currentY);
            content.add(notesText);
            currentY += notesText.height() + GAP;

            // 初始化显示备注内容
            notesText.text(notesContent.isEmpty() ?
                    Messages.get(CrashReportScene.class, "no_notes") :
                    notesContent);

            // 设置内容大小
            content.setSize(width - GAP * 2, currentY + 20);

            // 设置滚动区域
            list.setRect(GAP, titlebar.bottom(), width - GAP * 2, height - titlebar.height() - 40);
            list.scrollTo(0, 0);

            RedButton copyBtn = new RedButton(Messages.get(CrashReportScene.class, "copy"), 8) {
                @Override
                protected void onClick() {
                    StringBuilder contentToCopy = new StringBuilder();

                    // 获取完整的文件内容
                    String fullContent = es.message + "\n" + es.stackTrace;

                    // 移除开头的重复文件名
                    String[] lines = fullContent.split("\n");
                    int startIdx = 0;
                    for (int i = 0; i < lines.length; i++) {
                        if (!lines[i].startsWith("Crash log from")) {
                            startIdx = i;
                            break;
                        }
                    }

                    for (int i = startIdx; i < lines.length; i++) {
                        if (lines[i].equals("=== NOTES ===")) {
                            break;
                        }
                        contentToCopy.append(lines[i]).append("\n");
                    }

                    // 如果有备注，添加备注内容
                    if (!notesContent.isEmpty()) {
                        contentToCopy.append("\n")
                                .append(Messages.get(CrashReportScene.class, "notes_title"))
                                .append(":\n")
                                .append(notesContent);
                    }

                    Gdx.app.getClipboard().setContents(contentToCopy.toString());
                    ShatteredPixelDungeon.scene().addToFront(new WndMessage(Messages.get(CrashReportScene.class, "copy_success")));
                }
            };
            copyBtn.icon(Icons.get(Icons.COPY));
            copyBtn.setSize(50, 16);
            copyBtn.setPos(width - copyBtn.width(), height - copyBtn.height() - GAP);
            add(copyBtn);

            // 添加备注按钮
            RedButton addNoteBtn = new RedButton(Messages.get(CrashReportScene.class, "add_note"), 6) {
                @Override
                protected void onClick() {
                    ShatteredPixelDungeon.scene().add(new WndTextInput(
                            Messages.get(CrashReportScene.class, "add_note_title"),
                            Messages.get(CrashReportScene.class, "add_note_prompt"),
                            notesContent,
                            100,
                            false,
                            Messages.get(CrashReportScene.class, "confirm"),
                            Messages.get(CrashReportScene.class, "cancel")
                    ) {
                        @Override
                        public void onSelect(boolean positive, String text) {
                            if (positive) {
                                notesContent = text;
                                saveNotes(); // 保存备注
                                notesText.text(notesContent.isEmpty() ?
                                        Messages.get(CrashReportScene.class, "no_notes") :
                                        notesContent);
                                list.content();
                            }
                        }
                    });
                }
            };
            addNoteBtn.setSize(40, 16);
            addNoteBtn.setPos(0, copyBtn.top()-18);
            add(addNoteBtn);

            // 删除报告按钮
            RedButton deleteBtn = new RedButton(Messages.get(CrashReportScene.class, "delete_report"), 6) {
                @Override
                protected void onClick() {
                    ShatteredPixelDungeon.scene().add(new WndOptions(
                            Icons.get(Icons.WARNING),
                            Messages.get(CrashReportScene.class, "delete_confirm_title"),
                            Messages.get(CrashReportScene.class, "delete_confirm_message"),
                            Messages.get(CrashReportScene.class, "confirm"),
                            Messages.get(CrashReportScene.class, "cancel")
                    ) {
                        @Override
                        protected void onSelect(int index) {
                            if (index == 0) {
                                try {
                                    FileHandle file = Gdx.files.local(CrashHandler.CRASH_DIR + "/" + es.fileName);
                                    if (file.exists()) {
                                        file.delete();
                                    }
                                    hide();
                                    ShatteredPixelDungeon.switchNoFade(CrashReportScene.class);
                                } catch (Exception e) {
                                    Gdx.app.error(CrashHandler.TAG, "Failed to delete crash log", e);
                                }
                            }
                        }
                    });
                }
            };
            deleteBtn.setSize(40, 16);
            deleteBtn.setPos(addNoteBtn.right()+GAP, copyBtn.top()-18);
            add(deleteBtn);
        }

        private void saveNotes() {
            try {
                FileHandle file = Gdx.files.local(CrashHandler.CRASH_DIR + "/" + es.fileName);

                // 读取原始内容，移除所有NOTE分隔符和已有的备注
                String originalContent = es.message + "\n" + es.stackTrace;
                String[] lines = originalContent.split("\n");
                StringBuilder cleanContent = new StringBuilder();

                boolean skipNotes = false;
                for (String line : lines) {
                    if (line.equals("=== NOTES ===")) {
                        skipNotes = true;
                        continue;
                    }
                    if (!skipNotes) {
                        cleanContent.append(line).append("\n");
                    }
                }

                // 如果有新备注，添加单个NOTE分隔符和备注内容
                if (!notesContent.isEmpty()) {
                    // 统一处理换行符：将Windows换行符转为Unix换行符，并将多个连续换行符合并为单个
                    String normalizedNotes = notesContent
                            .replaceAll("\r\n", "\n")  // Windows换行符转为Unix
                            .replaceAll("\r", "\n")     // 旧Mac换行符转为Unix
                            .replaceAll("\n{2,}", "\n") // 多个连续换行符合并为单个
                            .trim();                    // 移除首尾空白

                    cleanContent.append(NOTES_SEPARATOR).append(normalizedNotes);
                }

                // 写入文件
                file.writeString(cleanContent.toString(), false);
            } catch (Exception e) {
                Gdx.app.error(CrashHandler.TAG, "Failed to save notes", e);
            }
        }

        private void loadNotes() {
            try {
                FileHandle file = Gdx.files.local(CrashHandler.CRASH_DIR + "/" + es.fileName);
                if (file.exists()) {
                    String content = file.readString();
                    // 查找最后一个NOTE分隔符
                    int lastNotesIndex = content.lastIndexOf(NOTES_SEPARATOR);
                    if (lastNotesIndex != -1) {
                        notesContent = content.substring(lastNotesIndex + NOTES_SEPARATOR.length()).trim();
                        // 统一处理换行符
                        notesContent = notesContent
                                .replaceAll("\r\n", "\n")  // Windows换行符转为Unix
                                .replaceAll("\r", "\n")     // 旧Mac换行符转为Unix
                                .replaceAll("\n{2,}", "\n") // 多个连续换行符合并为单个
                                .trim();                    // 移除首尾空白
                    }
                }
            } catch (Exception e) {
                Gdx.app.error(CrashHandler.TAG, "Failed to load notes", e);
            }
        }

        private String formatCrashMessage(String message, String stackTrace) {
            StringBuilder sb = new StringBuilder();
            String fullContent = message + "\n" + stackTrace;

            // 移除开头的重复文件名
            String[] lines = fullContent.split("\n");
            int startIdx = 0;
            for (int i = 0; i < lines.length; i++) {
                if (!lines[i].startsWith("Crash log from")) {
                    startIdx = i;
                    break;
                }
            }

            // 添加从第一个非文件名行开始到NOTE分隔符之前的内容
            for (int i = startIdx; i < lines.length; i++) {
                if (lines[i].equals("=== NOTES ===")) {
                    break;
                }
                if (lines[i].startsWith("Message: ")) {
                    sb.append(lines[i]).append("\n\n");
                } else if (lines[i].startsWith("Stack Trace:")) {
                    sb.append(lines[i]).append("\n");
                } else {
                    sb.append(lines[i]).append("\n");
                }
            }

            return sb.toString();
        }
    }
}
