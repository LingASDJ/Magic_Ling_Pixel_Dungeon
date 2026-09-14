package com.shatteredpixel.shatteredpixeldungeon.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.shatteredpixel.shatteredpixeldungeon.Chrome;
import com.shatteredpixel.shatteredpixeldungeon.GamesInProgress;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.HeroSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.Archs;
import com.shatteredpixel.shatteredpixeldungeon.ui.ExitButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.IconButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.ScrollPane;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.utils.DungeonSeed;
import com.shatteredpixel.shatteredpixeldungeon.windows.IconTitle;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndMessage;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Image;
import com.watabou.noosa.NinePatch;
import com.watabou.noosa.ui.Component;
import com.watabou.utils.DeviceCompat;
import com.watabou.utils.FileUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * 存档备份场景（BackupSaveScene）
 *
 * <p>该场景负责游戏存档的「备份管理」功能，核心能力包括：</p>
 * <ul>
 *   <li>列出备份目录下所有 .mlsp 备份文件（按修改时间倒序，最新在前）</li>
 *   <li>将某个存档槽位（Slot 1~6）导出为 .mlsp 压缩备份文件</li>
 *   <li>将 .mlsp 备份文件导入到指定存档槽位（导入前会清空目标槽位旧数据）</li>
 *   <li>删除单个备份、清除全部备份、以及打开备份目录（桌面端）</li>
 * </ul>
 *
 * <p>.mlsp 本质是一个 ZIP 压缩包，内部存放该存档槽位目录下的全部 .dat 存档数据文件。</p>
 */
public class BackupSaveScene extends PixelScene {

    /**
     * 备份文件列表信息集合，每个 BackupInfo 包含一列 BackupFileButton 按钮。
     * 当前实现只使用一个 BackupInfo 来承载全部备份文件按钮。
     */
    private final ArrayList<BackupInfo> infos = new ArrayList<>();

    /**
     * 备份文件存放目录：
     * <ul>
     *   <li>桌面端：AppData/Roaming/.shatteredpixel/Magic Ling Pixel Dungeon/pd_backups（外部存储）</li>
     *   <li>移动端：pd_backups（相对路径，由 libGDX 解析到应用私有目录）</li>
     * </ul>
     */
    public static String BACKUP_FOLDER = DeviceCompat.isDesktop() ? "AppData/Roaming/.shatteredpixel/Magic Ling Pixel Dungeon/pd_backups" : "pd_backups";

    /** 备份文件扩展名（Magic Ling Save Package 的缩写） */
    public static final String MLSP_EXT = ".mlsp";

    /** 场景 UI 的边距常量（像素） */
    private static final int MARGIN = 8;

    /** 场景 UI 中控件之间的间距常量（像素） */
    private static final int GAP = 10;

    /** 备份文件修改时间的格式化器：yyyy-MM-dd HH:mm，使用系统默认时区 */
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter
            .ofPattern("yyyy-MM-dd HH:mm")
            .withZone(ZoneId.systemDefault());

    /**
     * 场景创建入口：初始化整个备份管理界面的 UI。
     *
     * <p>布局结构从上到下依次为：</p>
     * <ul>
     *   <li>顶部：标题文字 + 退出按钮 + 「清除全部备份」按钮</li>
     *   <li>中部：一个带滚动条的面板，用于展示所有备份文件按钮</li>
     *   <li>底部：横贯屏幕的「导出存档」按钮</li>
     * </ul>
     */
    @Override
    public void create() {
        super.create();

        // 获取屏幕宽高，用于后续布局计算
        int w = Camera.main.width;
        int h = Camera.main.height;

        // 中部备份列表面板的尺寸：宽度占满屏幕，高度为屏幕高度减去底部按钮区（45px）
        int panelWidth = Camera.main.width;
        int panelHeight = h - 45;

        // ---- 顶部标题 ----
        RenderedTextBlock title = PixelScene.renderTextBlock(Messages.get(this, "title"), 11);
        title.hardlight(0x88CCEE); // 标题文字颜色：浅蓝色
        title.setPos((w - title.width()) / 2f, MARGIN); // 水平居中，垂直方向位于顶部边距处
        align(title);
        add(title);

        // ---- 右上角退出按钮 ----
        ExitButton btnExit = new ExitButton();
        btnExit.setPos(w - btnExit.width() - MARGIN, MARGIN);
        add(btnExit);

        // ---- 「清除全部备份」按钮（左上角）----
        // 点击后弹出确认对话框，确认后删除备份目录下所有 .mlsp 文件并刷新场景
        RedButton btnClearAll = new RedButton(Messages.get(this, "clear_all"), 7) {
            @Override
            protected void onClick() {
                ShatteredPixelDungeon.scene().add(new WndOptions(
                        Icons.get(Icons.WARNING), // 警告图标
                        Messages.get(BackupSaveScene.class, "clear_all_title"), // 弹窗标题
                        Messages.get(BackupSaveScene.class, "clear_all_msg"),   // 弹窗提示语
                        Messages.get(BackupSaveScene.class, "confirm"),         // 确认按钮
                        Messages.get(BackupSaveScene.class, "cancel")           // 取消按钮
                ) {
                    @Override
                    protected void onSelect(int index) {
                        // index == 0 表示用户点击了「确认」，执行清除操作
                        if (index == 0) {
                            FileHandle backupDir = Gdx.files.external(BACKUP_FOLDER);
                            if (backupDir.exists()) {
                                FileHandle[] files = backupDir.list();
                                if (files != null) {
                                    // 逐个删除扩展名为 .mlsp 的备份文件
                                    for (FileHandle f : files) {
                                        if (f.name().endsWith(MLSP_EXT)) {
                                            f.delete();
                                        }
                                    }
                                }
                            }
                            // 清除完成后无过渡切换回本场景，实现界面刷新
                            ShatteredPixelDungeon.switchNoFade(BackupSaveScene.class);
                        }
                    }
                });
            }
        };
        btnClearAll.setSize(36, 20);
        btnClearAll.setPos(2, MARGIN);
        add(btnClearAll);

        // ---- 「导出存档」按钮（屏幕底部）----
        // 点击后唤起内部弹窗 WndChooseSlotExport，让用户选择要导出的存档槽位
        RedButton btnExport = new RedButton(Messages.get(this, "export_slot"), 7) {
            @Override
            protected void onClick() {
                ShatteredPixelDungeon.scene().addToFront(new WndChooseSlotExport());
            }
        };
        btnExport.setSize(w, 20);
        btnExport.setPos(0, h - 20);
        add(btnExport);

        // ---- 中部备份列表面板（银灰色窗口样式九宫格背景）----
        NinePatch panel = Chrome.get(Chrome.Type.WINDOW_SILVER);
        panel.size(panelWidth, panelHeight);
        panel.x = (w - panelWidth) / 2f; // 水平居中
        panel.y = title.bottom() + GAP;  // 紧贴标题下方
        align(panel);
        add(panel);

        // ---- 可滚动的备份文件列表 ----
        // 点击列表区域时，将坐标分发给每个备份按钮，命中则触发对应按钮的 onClick
        ScrollPane list = new ScrollPane(new Component()) {
            @Override
            public void onClick(float x, float y) {
                for (BackupInfo info : infos) {
                    if (info.onClick(x, y)) {
                        return; // 已被某个按钮消费，停止分发
                    }
                }
            }
        };
        add(list);

        // ---- 扫描备份目录，收集所有 .mlsp 备份文件 ----
        ArrayList<BackupFile> backupList = new ArrayList<>();
        FileHandle backupDir = Gdx.files.external(BACKUP_FOLDER);
        if (backupDir.exists()) {
            try {
                FileHandle[] files = backupDir.list();
                if (files != null && files.length > 0) {
                    // 只保留扩展名为 .mlsp 的文件，并记录文件名与最后修改时间
                    for (FileHandle f : files) {
                        if (f.name().endsWith(MLSP_EXT)) {
                            String fileName = f.name();
                            long lastModified = f.lastModified();
                            backupList.add(new BackupFile(fileName, f, lastModified));
                        }
                    }
                    // 按修改时间降序排列，最新备份显示在最上方
                    Collections.sort(backupList, (o1, o2) -> Long.compare(o2.modTime, o1.modTime));
                } else {
                    // 目录存在但没有文件：显示「暂无备份」提示并结束初始化
                    showEmptyMsg(w, h, Messages.get(this, "no_backups"), 0x88CCEE);
                    return;
                }
            } catch (Exception e) {
                // 读取目录出错：上报异常并显示「读取失败」提示（红色）
                ShatteredPixelDungeon.reportException(e);
                showEmptyMsg(w, h, Messages.get(this, "read_error"), 0xFF5555);
                return;
            }
        } else {
            // 备份目录不存在：视为暂无备份
            showEmptyMsg(w, h, Messages.get(this, "no_backups"), 0x88CCEE);
            return;
        }

        // ---- 为每个备份文件创建按钮，并装入 BackupInfo 容器 ----
        BackupInfo info = new BackupInfo();
        for (BackupFile bf : backupList) {
            BackupFileButton bfb = new BackupFileButton(bf);
            info.addButton(bfb);
        }
        infos.add(info);

        // ---- 将 BackupInfo 布局到滚动面板的内容层中 ----
        Component content = list.content();
        content.clear();
        float posY = 0;
        for (BackupInfo info1 : infos) {
            // setRect 的 height 传 0，实际高度由 BackupInfo 的 layout 计算得出
            info1.setRect(0, posY, panel.innerWidth(), 0);
            content.add(info1);
            posY += info1.height() + GAP; // 累加每个容器的纵向偏移
        }
        content.setSize(panel.innerWidth(), (int) Math.ceil(posY));

        // 将滚动面板对齐到面板内部区域（去掉九宫格边距）
        list.setRect(
                panel.x + panel.marginLeft(),
                panel.y + panel.marginTop(),
                panel.innerWidth(),
                panel.innerHeight()
        );
        list.scrollTo(0, 0); // 滚动到顶部

        // ---- 背景装饰（游戏风格的拱形装饰纹理）----
        Archs archs = new Archs();
        archs.setSize(w, h);
        addToBack(archs);

        fadeIn(); // 场景淡入动画
    }

    /**
     * 将指定存档槽位导出为 .mlsp 备份文件。
     *
     * <p>导出流程：</p>
     * <ol>
     *   <li>检查目标槽位是否存在存档，空槽直接提示并返回</li>
     *   <li>确保备份目录存在（不存在则创建）</li>
     *   <li>按「slot{槽位}-{种子代码}.mlsp」的规则生成文件名</li>
     *   <li>将该槽位目录下所有 .dat 文件打包写入 ZIP（即 .mlsp 文件）</li>
     *   <li>弹出导出成功提示并刷新场景</li>
     * </ol>
     *
     * @param slot 存档槽位编号（1~6）
     */
    public static void exportSlotToMLSP(int slot) {
        // 检查该槽位是否有存档，空槽不允许导出
        GamesInProgress.Info saveInfo = GamesInProgress.check(slot);
        if (saveInfo == null) {
            ShatteredPixelDungeon.scene().addToFront(new WndMessage(Messages.get(BackupSaveScene.class, "export_empty_slot")));
            return;
        }
        try {
            // 存档数据目录与备份目录
            String slotFolder = GamesInProgress.gameFolder(slot);
            FileHandle backupDirHandle = Gdx.files.external(BACKUP_FOLDER);
            if (!backupDirHandle.exists()) {
                backupDirHandle.mkdirs(); // 首次导出时自动创建备份目录
            }

            // 优先使用自定义种子，否则使用随机种子数值
            String seedStr = saveInfo.customSeed.isEmpty() ? String.valueOf(saveInfo.seed) : saveInfo.customSeed;

            // 文件名示例：slot1-ABCD1234.mlsp（种子转成可读的字母数字代码）
            String fileName = String.format(Locale.US, "slot%s%s_%s%s", slot, "-", DungeonSeed.convertToCode(Long.parseLong(seedStr)), MLSP_EXT);
            FileHandle mlspHandle = backupDirHandle.child(fileName);

            // 以 ZIP 方式写出 .mlsp 文件：只打包该槽位目录下扩展名为 .dat 的数据文件
            try (ZipOutputStream zos = new ZipOutputStream(mlspHandle.write(false))) {
                ArrayList<String> fileList = FileUtils.filesInDir(slotFolder);
                for (String fname : fileList) {
                    if (fname.endsWith(".dat")) {
                        String fullPath = slotFolder + "/" + fname;
                        FileHandle datHandle = FileUtils.getFileHandle(fullPath);
                        byte[] data = datHandle.readBytes();

                        // 每个 .dat 文件作为一个 ZIP 条目写入，条目名沿用原文件名
                        ZipEntry entry = new ZipEntry(fname);
                        zos.putNextEntry(entry);
                        zos.write(data);
                        zos.closeEntry();
                    }
                }
            }

            // 导出成功提示 + 刷新场景
            ShatteredPixelDungeon.scene().addToFront(new WndMessage(Messages.get(BackupSaveScene.class, "export_success", fileName)));
            ShatteredPixelDungeon.switchNoFade(BackupSaveScene.class);
        } catch (IOException e) {
            // 导出失败：上报异常并提示
            ShatteredPixelDungeon.reportException(e);
            ShatteredPixelDungeon.scene().addToFront(new WndMessage(Messages.get(BackupSaveScene.class, "export_fail")));
        }
    }

    /**
     * 将 .mlsp 备份文件导入到指定存档槽位。
     *
     * <p>导入流程：</p>
     * <ol>
     *   <li>目标槽位目录若存在则先清空其中所有 .dat 文件，不存在则创建</li>
     *   <li>解压 .mlsp（ZIP），将其中所有 .dat 条目写入目标槽位目录</li>
     *   <li>将该槽位标记为「未知状态」，提示导入成功</li>
     * </ol>
     *
     * @param mlspFile   备份文件句柄
     * @param targetSlot 目标存档槽位编号（1~6）
     */
    private static void importMLSPtoSlot(FileHandle mlspFile, int targetSlot) {
        try {
            String slotFolder = GamesInProgress.gameFolder(targetSlot);
            FileHandle slotDirHandle = FileUtils.getFileHandle(slotFolder);

            // 清空目标槽位中所有旧存档 .dat 文件，保证导入后是干净状态
            if (slotDirHandle.exists()) {
                ArrayList<String> oldFiles = FileUtils.filesInDir(slotFolder);
                if (oldFiles != null) {
                    for (String fName : oldFiles) {
                        if (fName.endsWith(".dat")) {
                            FileUtils.deleteFile(slotFolder + "/" + fName);
                        }
                    }
                }
            } else {
                slotDirHandle.mkdirs(); // 槽位目录不存在则创建
            }

            // 逐条解压 .mlsp（ZIP）中的 .dat 文件到目标槽位目录
            try (InputStream fis = mlspFile.read();
                 ZipInputStream zis = new ZipInputStream(fis)) {

                ZipEntry entry;
                byte[] buffer = new byte[4096]; // 4KB 拷贝缓冲区
                int len;
                while ((entry = zis.getNextEntry()) != null) {
                    String entryName = entry.getName();
                    if (entryName.endsWith(".dat")) {
                        // 只还原 .dat 数据文件，忽略压缩包内其他条目
                        FileHandle outDat = FileUtils.getFileHandle(slotFolder + "/" + entryName);
                        try (OutputStream os = outDat.write(false)) {
                            while ((len = zis.read(buffer)) > 0) {
                                os.write(buffer, 0, len);
                            }
                        }
                    }
                    zis.closeEntry();
                }
            }

            // 导入后该槽位数据已变化，标记为未知状态（下次进入游戏时重新读取）
            GamesInProgress.setUnknown(targetSlot);
            ShatteredPixelDungeon.scene().addToFront(new WndMessage(Messages.get(BackupSaveScene.class, "import_success")));
        } catch (Exception e) {
            // 导入失败：上报异常并提示
            ShatteredPixelDungeon.reportException(e);
            ShatteredPixelDungeon.scene().addToFront(new WndMessage(Messages.get(BackupSaveScene.class, "import_fail")));
        }
    }

    /**
     * 在系统文件管理器中打开指定目录（仅桌面端有效，移动端不支持）。
     *
     * <p>根据操作系统选择不同的打开命令：</p>
     * <ul>
     *   <li>Windows：explorer.exe</li>
     *   <li>macOS：open</li>
     *   <li>Linux：xdg-open</li>
     *   <li>其他平台（如 Android）：不支持，返回 false</li>
     * </ul>
     *
     * @param dirHandle 要打开的目录句柄
     * @return 是否成功发起打开操作
     */
    private static boolean openDirectory(FileHandle dirHandle) {
        // 目录不存在或不是目录时直接失败
        if (!dirHandle.exists() || !dirHandle.isDirectory()) return false;
        String path = dirHandle.file().getAbsolutePath();
        String os = System.getProperty("os.name").toLowerCase();
        try {
            // 按操作系统分发到对应的系统文件管理器
            if (os.contains("win")) {
                Runtime.getRuntime().exec(new String[]{"explorer.exe", path});
            } else if (os.contains("mac")) {
                Runtime.getRuntime().exec(new String[]{"open", path});
            } else if (os.contains("nix") || os.contains("nux")) {
                Runtime.getRuntime().exec(new String[]{"xdg-open", path});
            } else {
                return false; // 安卓等移动平台无法调用系统文件管理器
            }
            return true;
        } catch (IOException e) {
            // 打开失败（安卓端常见）：上报异常并返回 false
            ShatteredPixelDungeon.reportException(e);
            return false;
        }
    }

    /**
     * 在屏幕中央显示一段提示文字（用于「暂无备份」「读取失败」等场景）。
     *
     * @param w     屏幕宽度，用于水平居中
     * @param h     屏幕高度，用于垂直居中
     * @param msg   提示文本内容
     * @param color 文字颜色（如 0x88CCEE 浅蓝、0xFF5555 红色）
     */
    private void showEmptyMsg(int w, int h, String msg, int color) {
        RenderedTextBlock text = PixelScene.renderTextBlock(msg, 8);
        text.hardlight(color);
        text.setPos((w - text.width()) / 2f, (h - text.height()) / 2f);
        align(text);
        add(text);
    }

    /**
     * 处理系统返回键：返回标题场景（TitleScene）。
     */
    @Override
    protected void onBackPressed() {
        ShatteredPixelDungeon.switchNoFade(TitleScene.class);
    }

    /**
     * 内部弹窗 1：选择要导出的存档槽位（WndChooseSlotExport）。
     *
     * <p>为每个存档槽位（Slot 1~6）生成一行：左侧为导出按钮，右侧为信息按钮。</p>
     * <ul>
     *   <li>导出按钮：点击后弹出确认框，确认后调用 {@link #exportSlotToMLSP(int)} 导出</li>
     *   <li>信息按钮：点击后打开 {@link WndInfoSlotSave} 查看该槽位存档详情</li>
     * </ul>
     */
    public static class WndChooseSlotExport extends Window {
        /** 弹窗固定宽度（像素） */
        private static final int WIDTH = 160;

        /** 弹窗内部控件间距（像素） */
        private static final float GAP = 2;

        public WndChooseSlotExport() {
            super();

            // 顶部引导文字
            RenderedTextBlock message = PixelScene.renderTextBlock(8);
            message.text(Messages.get(BackupSaveScene.class, "select_slot_export"), WIDTH);
            message.setPos(0, 0);
            add(message);

            // 从引导文字下方开始逐行放置各槽位按钮
            float pos = message.bottom() + 3 * GAP;

            // 遍历全部存档槽位（通常为 1~6）
            for (int slot = 1; slot <= GamesInProgress.MAX_SLOTS; slot++) {
                GamesInProgress.Info info = GamesInProgress.check(slot);
                // 槽位有存档时显示职业名，无存档时显示「空槽」
                String btnLabel;
                if (info != null) {
                    btnLabel = "Slot" + slot + " — " + info.heroClass.title();
                } else {
                    btnLabel = "Slot" + slot + " — 空槽";
                }

                final int selectedSlot = slot;

                // ---- 每行的导出按钮 ----
                RedButton btnSlot = new RedButton(btnLabel, 6) {
                    @Override
                    protected void onClick() {
                        hide(); // 先关闭选择弹窗
                        GamesInProgress.Info saveInfo = GamesInProgress.check(selectedSlot);
                        if (saveInfo == null) {
                            // 空槽：提示无法导出
                            ShatteredPixelDungeon.scene().addToFront(new WndMessage(Messages.get(BackupSaveScene.class, "export_empty_slot")));
                        } else {
                            // 有存档：弹出最终确认框，确认后执行导出
                            ShatteredPixelDungeon.scene().addToFront(new WndOptions(
                                    Icons.get(Icons.WARNING),
                                    Messages.get(BackupSaveScene.class, "confirm_export_title"),
                                    Messages.get(BackupSaveScene.class, "confirm_export_msg"),
                                    Messages.get(BackupSaveScene.class, "yes"),
                                    Messages.get(BackupSaveScene.class, "no")
                            ) {
                                @Override
                                protected void onSelect(int index) {
                                    if (index == 0) { // 用户选择「是」
                                        exportSlotToMLSP(selectedSlot);
                                    }
                                }
                            });
                        }
                    }
                };
                btnSlot.leftJustify = true; // 文字左对齐
                btnSlot.multiline = true;   // 允许文字换行
                btnSlot.setRect(0, pos, WIDTH - 20, btnSlot.reqHeight() + 10);
                add(btnSlot);

                // ---- 每行右侧的「信息」图标按钮 ----
                GamesInProgress.Info slotInfo = info;
                IconButton clsInfo = new IconButton(Icons.get(Icons.INFO)) {
                    @Override
                    protected void onClick() {
                        // 打开该槽位的存档详情弹窗
                        ShatteredPixelDungeon.scene().addToFront(new WndInfoSlotSave(slotInfo, selectedSlot));
                    }
                };
                // 垂直居中于本行导出按钮的右侧
                clsInfo.setRect(WIDTH - 20, btnSlot.top() + (btnSlot.height() - 20) / 2, 20, 20);
                add(clsInfo);

                // 累加纵向位置，准备放置下一行
                pos = btnSlot.bottom() + GAP;
            }
            // 根据内容总高度调整弹窗尺寸
            resize(WIDTH, (int) pos);
        }
    }

    // ==============================================
    // 内部静态窗口 2：INFO 详情弹窗（WndInfoSlotSave）
    // ==============================================

    /**
     * 内部弹窗 2：查看指定存档槽位的详情（WndInfoSlotSave）。
     *
     * <p>展示内容：槽位头像、槽位号 + 职业名、以及等级、层数、种子代码、
     * 副职业、生命值、力量等存档信息。</p>
     */
    public static class WndInfoSlotSave extends Window {
        /** 弹窗固定宽度（像素） */
        private static final int WIDTH = 120;

        /**
         * @param info    该槽位的存档信息；为 null 时表示空槽，仅显示提示
         * @param slotNum 槽位编号，用于标题显示
         */
        public WndInfoSlotSave(GamesInProgress.Info info, int slotNum) {
            super();
            // 空槽：只显示「空槽」提示并直接结束
            if (info == null) {
                RenderedTextBlock txt = PixelScene.renderTextBlock(8);
                txt.text(Messages.get(BackupSaveScene.class, "empty_slot_tip"), WIDTH);
                txt.setPos(0, 0);
                add(txt);
                resize(WIDTH, (int) txt.bottom());
                return;
            }

            // ---- 标题栏：职业头像 + 槽位号/职业名 ----
            IconTitle titlebar = new IconTitle();
            titlebar.icon(HeroSprite.avatar(info.heroClass, info.armorTier)); // 根据职业与护甲等级生成头像
            titlebar.label("Slot" + slotNum + "-" + info.heroClass.title());
            titlebar.setRect(0, 0, WIDTH, 0);
            add(titlebar);

            // ---- 详情正文：多行文本 ----
            RenderedTextBlock message = PixelScene.renderTextBlock(7);
            // 优先使用自定义种子，否则使用随机种子数值
            String seed = info.customSeed.isEmpty() ? String.valueOf(info.seed) : info.customSeed;
            // 依次填充：等级、层数、种子代码、副职业、生命值、力量
            String text = Messages.get(BackupSaveScene.class, "info_text",
                    info.level,
                    info.depth,
                    DungeonSeed.convertToCode(Long.parseLong(seed)),
                    info.subClass.title(),
                    info.hp,
                    info.str
            );
            message.text(text, WIDTH);
            message.setPos(titlebar.left(), titlebar.bottom() + 4);
            add(message);

            // 根据内容总高度调整弹窗尺寸
            resize(WIDTH, (int) message.bottom());
        }
    }

    /**
     * 备份文件实体：封装单个 .mlsp 备份文件的名称、文件句柄与最后修改时间。
     */
    private static class BackupFile {
        /** 备份文件名（含 .mlsp 扩展名） */
        String fileName;

        /** 备份文件句柄，用于读取/删除等操作 */
        FileHandle file;

        /** 文件最后修改时间（毫秒时间戳），用于排序与展示 */
        long modTime;

        BackupFile(String fileName, FileHandle file, long modTime) {
            this.fileName = fileName;
            this.file = file;
            this.modTime = modTime;
        }
    }

    /**
     * 备份文件按钮容器：负责把一组 {@link BackupFileButton} 纵向排列，
     * 并将滚动面板的点击坐标分发到具体按钮上。
     */
    private static class BackupInfo extends Component {
        /** 容器内所有的备份文件按钮 */
        private ArrayList<BackupFileButton> buttons = new ArrayList<>();

        /**
         * 添加一个备份按钮，并立即重新布局。
         *
         * @param btn 要添加的备份按钮
         */
        public void addButton(BackupFileButton btn) {
            buttons.add(btn);
            add(btn);
            layout();
        }

        /**
         * 点击命中检测：将外部坐标转换为容器内相对坐标后，逐个判断是否落在某按钮范围内。
         *
         * @param x 点击点 X（场景坐标）
         * @param y 点击点 Y（场景坐标）
         * @return 是否命中某个按钮（命中后已触发该按钮的 onClick）
         */
        public boolean onClick(float x, float y) {
            float lx = x - this.x; // 转为容器内相对坐标
            float ly = y - this.y;
            for (BackupFileButton b : buttons) {
                // 判断点击点是否落在按钮矩形区域内
                if (lx >= b.x && lx <= b.x + b.width() && ly >= b.y && ly <= b.y + b.height()) {
                    b.onClick();
                    return true;
                }
            }
            return false;
        }

        /**
         * 布局：从上到下依次排列所有按钮，并计算容器的总宽度与总高度。
         */
        @Override
        protected void layout() {
            float posY = GAP;
            float maxW = 0;
            for (BackupFileButton b : buttons) {
                b.setPos(GAP, posY);
                posY += b.height() + GAP;
                maxW = Math.max(maxW, b.width());
            }
            height = posY;              // 总高度 = 各按钮高度 + 间距
            width = maxW + GAP * 2;     // 总宽度 = 最宽按钮 + 两侧边距
        }
    }

    /**
     * 单个备份文件的展示按钮：显示图标、文件名与修改时间。
     *
     * <p>点击后弹出操作菜单，支持：</p>
     * <ul>
     *   <li>导入备份（选择目标槽位，确认覆盖后导入）</li>
     *   <li>提取文件（桌面端打开备份目录）</li>
     *   <li>删除备份（确认后删除该 .mlsp 文件并刷新场景）</li>
     * </ul>
     */
    private static class BackupFileButton extends Component {
        /** 左侧图标（文件夹图标） */
        protected Image icon;

        /** 该按钮对应的备份文件实体 */
        protected BackupFile backup;

        /** 文件名文本 */
        protected RenderedTextBlock nameText;

        /** 修改时间文本（yyyy-MM-dd HH:mm） */
        protected RenderedTextBlock timeText;

        /** 按钮背景九宫格（银灰色窗口样式） */
        protected NinePatch bg;

        /**
         * @param backupFile 该按钮要展示的备份文件
         */
        public BackupFileButton(BackupFile backupFile) {
            super();
            // 背景
            bg = Chrome.get(Chrome.Type.WINDOW_SILVER);
            add(bg);
            // 图标：文件夹图标，染成绿色
            icon = Icons.get(Icons.CATALOG);
            icon.hardlight(0x44DD88);
            add(icon);
            this.backup = backupFile;

            // 文件名文本（白色）
            nameText = PixelScene.renderTextBlock(backup.fileName, 6);
            nameText.hardlight(0xFFFFFF);
            add(nameText);

            // 修改时间文本（浅灰色）：把毫秒时间戳格式化为可读字符串
            Instant instant = Instant.ofEpochMilli(backup.modTime);
            String timeStr = FORMATTER.format(instant);
            timeText = PixelScene.renderTextBlock(timeStr, 6);
            timeText.hardlight(0xCCCCCC);
            add(timeText);
            layout();
        }

        /**
         * 点击处理：弹出该备份文件的操作菜单。
         *
         * <p>菜单项：导入备份 / 提取文件 / 删除备份 / 取消。</p>
         */
        protected void onClick() {
            ShatteredPixelDungeon.scene().add(new WndOptions(
                    Icons.get(Icons.CATALOG),
                    backup.fileName, // 标题为备份文件名
                    Messages.get(BackupSaveScene.class, "backup_options"),
                    Messages.get(BackupSaveScene.class, "import_backup"),
                    Messages.get(BackupSaveScene.class, "extract_file"), // 提取文件
                    Messages.get(BackupSaveScene.class, "delete_backup"),
                    Messages.get(BackupSaveScene.class, "cancel")
            ) {
                @Override
                protected void onSelect(int index) {
                    if (index == 0) {
                        // ---- 导入备份：先选择目标槽位（Slot1~6），再二次确认覆盖 ----
                        ShatteredPixelDungeon.scene().add(new WndOptions(
                                Icons.get(Icons.WARNING),
                                Messages.get(BackupSaveScene.class, "import_confirm_title"),
                                Messages.get(BackupSaveScene.class, "import_confirm_desc"),
                                "Slot1", "Slot2", "Slot3", "Slot4", "Slot5", "Slot6",
                                Messages.get(BackupSaveScene.class, "cancel")
                        ) {
                            @Override
                            protected void onSelect(int slotIdx) {
                                // slotIdx 0~5 对应 Slot1~Slot6
                                if (slotIdx >= 0 && slotIdx <= 5) {
                                    int targetSlot = slotIdx + 1;
                                    // 二次确认：导入会覆盖目标槽位现有存档
                                    ShatteredPixelDungeon.scene().add(new WndOptions(
                                            Icons.get(Icons.WARNING),
                                            Messages.get(BackupSaveScene.class, "warn_overwrite_title"),
                                            Messages.get(BackupSaveScene.class, "warn_overwrite_desc"),
                                            Messages.get(BackupSaveScene.class, "confirm_overwrite"),
                                            Messages.get(BackupSaveScene.class, "cancel")
                                    ) {
                                        @Override
                                        protected void onSelect(int yesno) {
                                            if (yesno == 0) { // 用户确认覆盖
                                                importMLSPtoSlot(backup.file, targetSlot);
                                            }
                                        }
                                    });
                                }
                            }
                        });
                    } else if (index == 1) {
                        // ---- 提取文件：桌面端打开该备份所在目录 ----
                        boolean ok = openDirectory(backup.file.parent());
                        if (!ok) {
                            // 打开失败（如移动端）给出提示
                            ShatteredPixelDungeon.scene().addToFront(new WndMessage(Messages.get(BackupSaveScene.class, "extract_fail")));
                        }
                    } else if (index == 2) {
                        // ---- 删除备份：确认后删除文件并刷新场景 ----
                        ShatteredPixelDungeon.scene().add(new WndOptions(
                                Icons.get(Icons.WARNING),
                                Messages.get(BackupSaveScene.class, "del_backup_title"),
                                Messages.get(BackupSaveScene.class, "del_backup_desc"),
                                Messages.get(BackupSaveScene.class, "confirm"),
                                Messages.get(BackupSaveScene.class, "cancel")
                        ) {
                            @Override
                            protected void onSelect(int delIdx) {
                                if (delIdx == 0) { // 用户确认删除
                                    backup.file.delete();
                                    ShatteredPixelDungeon.switchNoFade(BackupSaveScene.class); // 刷新场景
                                }
                            }
                        });
                    }
                }
            });
        }

        /**
         * 布局：排列背景、图标、文件名与时间文本的相对位置。
         */
        @Override
        protected void layout() {
            super.layout();
            // 背景铺满整个按钮区域
            bg.x = x;
            bg.y = y;
            // 图标：位于左侧，垂直居中
            icon.x = x + GAP;
            icon.y = y + (height - icon.height()) / 2f;
            PixelScene.align(icon);

            // 文件名：位于图标右侧
            nameText.setPos(icon.x + icon.width + GAP, y + GAP);
            // 时间文本：位于文件名下方；有则按时间底部计算高度，无则按文件名计算
            if (timeText != null) {
                timeText.setPos(nameText.left(), nameText.bottom() + 2);
                height = Math.max(height, timeText.bottom() - y + GAP);
            } else {
                height = Math.max(height, nameText.bottom() - y + GAP);
            }
            // 背景宽度：屏幕宽度减去两侧 40px 边距
            bg.size(Camera.main.width - 40, height);
        }

        /**
         * 组件宽度：取内部内容（文件名 + 图标 + 间距）与父类宽度的较大值。
         */
        @Override
        public float width() {
            return Math.max(super.width(), nameText.width() + icon.width() + GAP * 3);
        }

        /**
         * 组件高度：保证按钮至少 24px 高，防止内容过少时按钮过矮。
         */
        @Override
        public float height() {
            return Math.max(24, super.height());
        }
    }
}
