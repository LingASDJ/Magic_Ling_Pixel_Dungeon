package com.shatteredpixel.shatteredpixeldungeon.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Bones;
import com.shatteredpixel.shatteredpixeldungeon.Chrome;
import com.shatteredpixel.shatteredpixeldungeon.GamesInProgress;
import com.shatteredpixel.shatteredpixeldungeon.PaswordBadges;
import com.shatteredpixel.shatteredpixeldungeon.Rankings;
import com.shatteredpixel.shatteredpixeldungeon.SPDAction;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.custom.CollectRankings;
import com.shatteredpixel.shatteredpixeldungeon.journal.Journal;
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
import com.watabou.input.KeyBindings;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.NinePatch;
import com.watabou.noosa.ui.Component;
import com.watabou.utils.DeviceCompat;
import com.watabou.utils.FileUtils;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

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
    private static volatile boolean importChooserOpened = false;
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

    /** 局外存档备份的文件名前缀，用于与槽位备份的 slot%d- 前缀区分 */
    public static final String GLOBAL_PREFIX = "whole-save";

    /** 局内存档备份的文件名前缀，用于与全局备份的 whole-save 前缀区分 */
    public static final String SLOT_PREFIX = "slot";

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
     * <p><font color="orange"><b><h1>UI初始化</h1></b></font></p>
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

        // 中部备份列表面板的尺寸：
        // 宽度占满屏幕，
        int panelWidth = Camera.main.width;
        // 高度为屏幕高度减去底部按钮区（45px）
        int panelHeight = h - 45;

        // ---- 顶部标题 ----
        // 这里的文本是 “存档备份”
        RenderedTextBlock title = PixelScene.renderTextBlock(Messages.get(this, "title"), 11);
        // 标题文字颜色：浅蓝色
        title.hardlight(0x88CCEE);
        // 设置UI元素的位置，水平居中，垂直方向位于顶部边距处
        title.setPos((w - title.width()) / 2f, MARGIN);
        // 对偏移像素做吸附处理
        align(title);
        // 添加UI元素到成员组
        add(title);

        // ---- 右上角退出按钮 ----
        // 创建退出按钮
        ExitButton btnExit = new ExitButton();
        // 设置退出按钮的坐标
        btnExit.setPos(w - btnExit.width() - MARGIN, MARGIN);
        add(btnExit);

        // ---- 「清除全部备份」按钮（左上角）----
        // 点击后弹出确认对话框，确认后删除备份目录下所有 .mlsp 文件并刷新场景
        // 这里的文本是 “全部清除”
        RedButton btnClearAll = new RedButton(Messages.get(this, "clear_all"), 7) {
            @Override
            protected void onClick() {
                ShatteredPixelDungeon.scene().add(new WndOptions(
                        Icons.get(Icons.WARNING),                                  // 警告图标
                        // 弹窗标题 这里的文本是 “确认清除全部备份？”
                        Messages.get(BackupSaveScene.class, "clear_all_title"),
                        // 弹窗提示语 这里的文本是 “该操作将删除所有备份文件，无法恢复。”
                        Messages.get(BackupSaveScene.class, "clear_all_msg"),
                        // 确认按钮 这里的文本是 “确认”
                        Messages.get(BackupSaveScene.class, "confirm"),
                        // 取消按钮 这里的文本是 “取消”
                        Messages.get(BackupSaveScene.class, "cancel")
                ) {
                    @Override
                    protected void onSelect(int index) {
                        // index == 0 表示用户点击了「确认」，执行清除操作
                        if (index == 0) {
                            // 解析以获取句柄（文件位置、某文件夹）
                            FileHandle backupDir = Gdx.files.external(BACKUP_FOLDER);
                            // 当存在这个文件夹时
                            if (backupDir.exists()) {
                                // 获取文件列表
                                FileHandle[] files = backupDir.list();
                                // 文件夹不为空
                                if (files != null) {
                                    // 增强for遍历文件列表
                                    for (FileHandle f : files) {
                                        // 若以.mlsp拓展名结尾
                                        if (f.name().endsWith(MLSP_EXT)) {
                                            // 删除文件
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
        // UI元素尺寸设置
        btnClearAll.setSize(36, 20);
        btnClearAll.setPos(2, MARGIN);
        add(btnClearAll);

        // ---- 「导出存档」按钮（屏幕底部）----
        // 点击后唤起内部弹窗 WndChooseSlotExport，让用户选择要导出的存档槽位
        // 这里的文本是 “存档备份”
        RedButton btnExport = new RedButton(Messages.get(this, "export_slot"), 7) {
            @Override
            protected void onClick() {
                ShatteredPixelDungeon.scene().addToFront(new WndChooseSlotExport());
            }
        };
        btnExport.setSize((float) (w / 2.0), 20);
        btnExport.setPos(0, h - 20);
        add(btnExport);

        // ---- 「导入存档」按钮（屏幕底部）----
        // 点击后唤起用户文件夹，并让用户选择.mlsp文件
        // 这里的文本是 “外部导入”
        // ---- 「导入存档」按钮（屏幕底部）----
        RedButton btnImport = new RedButton(Messages.get(this, "import_file"), 7) {
            @Override
            protected void onClick() {
                if (DeviceCompat.isDesktop()) {
                    // 锁：防止重复点击多次弹出对话框
                    if (importChooserOpened) {
                        return;
                    }
                    importChooserOpened = true;

                    // Swing操作必须放到AWT事件调度线程EDT
                    javax.swing.SwingUtilities.invokeLater(() -> {
                        JFileChooser chooser = new JFileChooser();
                        chooser.setDialogTitle(Messages.get(BackupSaveScene.class, "import_file"));
                        chooser.setCurrentDirectory(new File(System.getProperty("user.home")));
                        chooser.setPreferredSize(new Dimension(800, 600));
                        chooser.setFileFilter(new FileNameExtensionFilter("(*.mlsp)", "mlsp"));

                        // 创建一个隐藏的JFrame作为对话框owner，实现模态置顶，不显示窗口
                        javax.swing.JFrame tempOwnerFrame = new javax.swing.JFrame();
                        tempOwnerFrame.setUndecorated(true);
                        tempOwnerFrame.setSize(1,1);
                        tempOwnerFrame.setLocationRelativeTo(null);
                        tempOwnerFrame.setVisible(true);

                        int ret = chooser.showOpenDialog(tempOwnerFrame);

                        // 销毁临时owner窗口
                        tempOwnerFrame.dispose();

                        final File selected = chooser.getSelectedFile();

                        // 释放锁，允许下一次打开
                        importChooserOpened = false;

                        // 把结果切回libGDX渲染线程执行游戏逻辑
                        Gdx.app.postRunnable(() -> {
                            if (ret == JFileChooser.APPROVE_OPTION && selected != null) {
                                startImportFlow(Gdx.files.absolute(selected.getAbsolutePath()));
                            }
                        });
                    });

                } else {
                    // 安卓/iOS 走不了 Swing，先给提示
                    ShatteredPixelDungeon.scene().addToFront(new WndMessage(
                            Messages.get(BackupSaveScene.class, "desktop_not_supported")));
                }
            }
        };
        btnImport.setSize((float) (w / 2.0), 20);
        btnImport.setPos((float) (w / 2.0), h - 20);
        add(btnImport);

        // ---- 中部备份列表面板（银灰色窗口样式九宫格背景）----
        NinePatch panel = Chrome.get(Chrome.Type.WINDOW_SILVER);
        panel.size(panelWidth, panelHeight);
        // 水平居中
        panel.x = (w - panelWidth) / 2f;
        // 紧贴标题下方
        panel.y = title.bottom() + GAP;
        align(panel);
        add(panel);

        // ---- 可滚动的备份文件列表 ----
        // 点击列表区域时，将坐标分发给每个备份按钮，命中则触发对应按钮的 onClick
        ScrollPane list = new ScrollPane(new Component()) {
            @Override
            public void onClick(float x, float y) {
                // infos 就是开头声明的信息集合表
                for (BackupInfo info : infos) {
                    // 判断鼠标点击点位落在某个按钮的区域内
                    if (info.onClick(x, y)) {
                        // 已被某个按钮消费，停止分发
                        return;
                    }
                }
            }
        };
        add(list);

        // ---- 扫描备份目录，收集所有 .mlsp 备份文件 ----
        // 新建一个用于存放备份文件的变量
        ArrayList<BackupFile> backupList = new ArrayList<>();
        FileHandle backupDir = Gdx.files.external(BACKUP_FOLDER);
        if (backupDir.exists()) {
            try {
                // 获取文件列表
                FileHandle[] files = backupDir.list();
                // 文件列表不为空且长度大于0（两个操作都是防空引用）
                if (files != null && files.length > 0) {
                    // 只保留扩展名为 .mlsp 的文件，并记录文件名与最后修改时间
                    for (FileHandle f : files) {
                        if (f.name().endsWith(MLSP_EXT)) {
                            String fileName = f.name();
                            long lastModified = f.lastModified();
                            // 将文件存入到先前创建的变量中
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
            // 备份目录不存在：视为「暂无备份」
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
        // 防御性清空
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
     * <p><font color="orange"><b><h1>导出操作</h1></b></font></p>
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
        // 检查该槽位是否有存档，空槽不允许导出 「该槽位没有正在进行的存档，无法备份。」
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

            // 文件名示例：slot1-DTK-KSC-PVS.mlsp（自定义种子用原文，非自定义的根据随机到的种子用对应的种子码）
            // String.format是按格式拼接字符串，
            // 第一个参数Locale.US指的是按美国格式，第二个参数是预设计的格式，
            // 第三个参数是存档的槽位编号，第四个参数是种子码并将不符合windows文件命名规范的字符替换为下划线且当名字只剩下划线时返回unknown作为文件名，
            // 第五个参数是我们设定的备份存档文件的拓展名
            // 这里需要具体讲讲命名相关的东西，只是作为知识补充：
            // 其一：这里我们加了个前缀“slot%d-”，
            //      倘若不加这个前缀可能导致一些Windows操作系统保留名被作为文件名命名出来，这会导致输出文件经历各种乱七八糟的情况
            //      以nul为例，Windows大小写不敏感，如果种子名为nul，Windows操作系统比对保留字时拓展名不参与比对，这样命名就命中了保留字NUL
            //      后果分两种：现代 Windows 会直接拒绝创建；
            //      但更阴险的是历史行为——NUL 设备会「接受」写入并把数据丢进黑洞，写操作看起来成功了，实际什么都没留下。这类问题排查起来非常费劲。
            // 其二：Windows会自动删除文件末尾的“.”和“ ”（空格），而且不报告，不过我们这里末尾自动加了个“.mlpd”所以没事
            // 其三：同一个字符在Unicode编码中存在多种等价的码点写法，比如NFC、NFD，而各个平台的处理方式又不相同，会遇到各种难以预测的问题，
            //      而编码值不同的同一个字符其equal方法返回的结果往往是false，所以输入的文件名与其对应的预期编码值可能是有出入的，所以设计特定文字组成特定预设键时需要注意这个问题
            //      当然，这里由于没有这个需求，所以不会遇到这个问题
            String fileName = String.format(Locale.US, "slot%d-%s%s", slot, sanitizeForFileName(seedText(saveInfo)), MLSP_EXT);
            FileHandle mlspHandle = backupDirHandle.child(fileName);

            // 以 ZIP 方式写出 .mlsp 文件：只打包该槽位目录下扩展名为 .dat 的数据文件
            // mlspHandle.write(false) 以覆写方式打开一个文件，返回值为一个输出流（OutputStream）指向我们先前定义的存档文件，这会覆盖旧备份文件，若再在写过程中发生异常则会破坏旧备份
            // ZipOutputStream是将此赋予此输出流以zip的语义，能记住当前在写哪个条目、对每个条目做DEFAULT压缩、在 close() 时补写中央目录和结束记录（所以必须关否则会丢失部分标识性内容）
            try (ZipOutputStream zos = new ZipOutputStream(mlspHandle.write(false))) {
                // 打开存档文件夹记录其中的每个文件的文件名（当然也包括子文件夹）
                ArrayList<String> fileList = FileUtils.filesInDir(slotFolder);
                // 遍历存档文件夹里的每个文件
                for (String fname : fileList) {
                    // 只对.dat文件做操作，这一般是存储数据的文件
                    if (fname.endsWith(".dat")) {
                        // 因为只有文件名，所以要根据文件名补全文件路径
                        String fullPath = slotFolder + "/" + fname;
                        // 根据补全的文件路径打开文件
                        FileHandle datHandle = FileUtils.getFileHandle(fullPath);
                        // 读取并存储数据到data变量
                        byte[] data = datHandle.readBytes();

                        // 每个 .dat 文件作为一个 ZIP 条目写入，条目名沿用原文件名
                        ZipEntry entry = new ZipEntry(fname);
                        // 打开条目
                        zos.putNextEntry(entry);
                        // 写条目
                        zos.write(data);
                        // 关闭条目
                        zos.closeEntry();
                    }
                }
            }

            // 重建一个界面并重新显示但不播放切换界面动画，也许可以改为局部重建
            ShatteredPixelDungeon.switchNoFade(BackupSaveScene.class, new Game.SceneChangeCallback() {
                @Override public void beforeCreate() { }

                @Override public void afterCreate() {
                    // 导出成功提示 「备份已创建：%s」
                    ShatteredPixelDungeon.scene().addToFront(
                            new WndMessage(Messages.get(BackupSaveScene.class, "export_success", fileName)));
                }
            });
        // 这里我将异常捕获对象从IOException替换成了Exception e
        } catch (Exception e) {
            // 导出失败：上报异常并提示
            ShatteredPixelDungeon.reportException(e);
            // 「导出失败！」
            ShatteredPixelDungeon.scene().addToFront(new WndMessage(Messages.get(BackupSaveScene.class, "export_fail")));
        }
    }

    /**
     * 将局外存档导出为 .mlsp 备份文件。
     * <p><font color="orange"><b><h1>导出局外存档操作</h1></b></font></p>
     * <p>局外存档导出流程：</p>
     * <ol>
     *   <li>确保备份目录存在（不存在则创建）</li>
     *   <li>按「whole-save.mlsp」的规则生成文件名</li>
     *   <li>将该目录下所有 .dat 文件打包写入 ZIP（即 .mlsp 文件）</li>
     *   <li>弹出导出成功提示并刷新场景</li>
     * </ol>
     */
    public static void exportWholeSlotToMLSP() {
        try {
            FileHandle backupDirHandle = Gdx.files.external(BACKUP_FOLDER);
            if (!backupDirHandle.exists()) {
                backupDirHandle.mkdirs();
            }

            // 文件名示例：whole-save.mlsp
            String fileName = String.format(Locale.US, "%s%s", GLOBAL_PREFIX, MLSP_EXT);
            FileHandle mlspHandle = backupDirHandle.child(fileName);

            try (ZipOutputStream zos = new ZipOutputStream(mlspHandle.write(false))) {
                ArrayList<String> fileList = FileUtils.filesInDir("");
                for (String fname : fileList) {
                    if (fname.endsWith(".dat")) {
                        FileHandle datHandle = FileUtils.getFileHandle(fname);
                        byte[] data = datHandle.readBytes();
                        ZipEntry entry = new ZipEntry(fname);
                        zos.putNextEntry(entry);
                        zos.write(data);
                        zos.closeEntry();
                    }
                    if (fname.equals("settings.xml")) {
                        // 导出前先规范化：防止磁盘上的 settings.xml 被手工编辑成「裸 entry」格式后，
                        // 原样打包进备份，导致下次导入时 Lwjgl3Preferences 解析为空
                        ensureSettingsXmlValid( FileUtils.getFileHandle( fname ) );
                        FileHandle datHandle = FileUtils.getFileHandle(fname);
                        byte[] data = datHandle.readBytes();
                        ZipEntry entry = new ZipEntry(fname);
                        zos.putNextEntry(entry);
                        zos.write(data);
                        zos.closeEntry();
                    }
                }
            }

            ShatteredPixelDungeon.switchNoFade(BackupSaveScene.class, new Game.SceneChangeCallback() {
                @Override public void beforeCreate() { }
                @Override public void afterCreate() {
                    ShatteredPixelDungeon.scene().addToFront(
                            new WndMessage(Messages.get(BackupSaveScene.class, "export_success", fileName)));
                }
            });
        } catch (Exception e) {
            ShatteredPixelDungeon.reportException(e);
            ShatteredPixelDungeon.scene().addToFront(new WndMessage(Messages.get(BackupSaveScene.class, "export_fail")));
        }
    }

    /**
     * 将 .mlsp 备份文件导入到指定存档槽位。
     * <p><font color="orange"><b><h1>导入操作</h1></b></font></p>
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
            // 每个槽位的存档都放在一个独立目录里
            // 取该槽位存档目录的相对路径（纯字符串拼接，"game%d" 格式化而来）
            // 本质只是生成字符串，不涉及磁盘读写
            // 实际得到的就是 "game1"~"game6"
            String slotFolder = GamesInProgress.gameFolder(targetSlot);
            // 把上面这个相对路径包装成 libGDX 的 FileHandle
            // 「相对」是相对于哪个存储位置，由 FileType 决定（这里启动时注册的是 External = 外部存储根目录），
            // 各平台的实际根路径不同，因此不能写死绝对路径，这里使用这套工具大概也是为了跨平台方便考虑
            // FileHandle 自带一整套文件/目录操作方法（exists/mkdirs/list/read/write/delete...），后续所有 IO 都通过它进行
            // 注意：本行同样不产生磁盘 IO，也不要求目录已存在；
            // 真正访问磁盘的是后面的 slotDirHandle.exists()（第401行）与 mkdirs()（第411行）
            FileHandle slotDirHandle = FileUtils.getFileHandle(slotFolder);

            // 清空目标槽位中所有旧存档 .dat 文件，保证导入后是干净状态
            // 首先判定目录的存在性（不过当然也可以用于判定文件的存在性，此方法不对目录和文件做区分）
            // 这里也就是判定选中槽位是不是空的，是空的话就新建个目录，不是的话就把旧的文件删了（当然了，只是删存数据的.dat文件）
            if (slotDirHandle.exists()) {
                // 删除ing……
                ArrayList<String> oldFiles = FileUtils.filesInDir(slotFolder);
                if (oldFiles != null) {
                    for (String fName : oldFiles) {
                        if (fName.endsWith(".dat")) {
                            FileUtils.deleteFile(slotFolder + "/" + fName);
                        }
                    }
                }
            } else {
                // 新建ing……
                slotDirHandle.mkdirs(); // 槽位目录不存在则创建
            }

            // 逐条解压 .mlsp（ZIP）中的 .dat 文件到目标槽位目录
            // 将存档压缩文件读入到一个输入流fis
            // 给fis套一层zip解析器成为zis
            // try后面紧跟的括号在java语法里隐含了当try块结束（执行完或中断）自行关闭打开的文件
            try (InputStream fis = mlspFile.read();
                 ZipInputStream zis = new ZipInputStream(fis)) {
                // ZipEntry = 压缩包中「一条条目」的元数据（名字、大小、CRC、时间等），不包含数据本身，也就是条目的数据头、文件头、数据标识或者别的什么理解方法都行
                // 这里解释一下【条目】这个名词，这个指的是压缩包压缩之前的那个文件夹中的一个文件，姑且可以这样理解
                // 数据始终要通过 zis.read() 读取；每次 getNextEntry() 返回的是新对象，循环里复用变量名而已
                // getNextEntry() 返回 null 表示没有更多条目，循环结束；它顺带会关闭上一条目并重置解压状态
                // 注意：这里的 entry 只反映本地文件头，若条目带数据描述符则 getSize()/getCrc() 返回 -1，
                //      本项目导出用 ZipOutputStream 流式写入正属此列，因此只应使用 entry.getName()
                // （导出侧同一个类以相反方向使用：new ZipEntry(fname) + zos.putNextEntry(entry) 表示「要写这一条」）
                ZipEntry entry;
                byte[] buffer = new byte[4096]; // 4KB 拷贝缓冲区
                int len;
                // 获取下一个条目的元数据
                while ((entry = zis.getNextEntry()) != null) {
                    // 获取条目的名字，用来识别文件
                    String entryName = entry.getName();
                    // 如果是以.dat结尾的文件（这是存档的数据文件），将其解压（还原）
                    if (entryName.endsWith(".dat")) {
                        // 解压输出ing……
                        FileHandle outDat = FileUtils.getFileHandle(slotFolder + "/" + entryName);
                        try (OutputStream os = outDat.write(false)) {
                            // zis.read(buffer) 返回本次实际读入 buffer 的字节数，-1 表示「当前条目已读完」
                            //   1) 读取并从下标0开始存入len个字节，最多不超过先前设定的拷贝缓冲区的最大大小，
                            //      这里注意，每次读取只存入len个字节，假如发生了什么奇奇怪怪的情况，读取了200个字节，那就应该从0读到199，而199之后的是上次读取残留的数据，不能读
                            //   2) 返回的是解压后的字节数
                            //   3) -1 是条目级结束，不是压缩包级结束；此时内部 entry 会被置为 null，
                            //      所以内层循环只作用于单条 .dat，外层再由 getNextEntry() 推进
                            // 另外：条目读完后 readEnd() 会解析数据描述符并校验 size/csize/CRC，损坏会抛 ZipException；
                            //      但 ZipInputStream 不读中央目录，若文件被截断在条目边界上会静默少导入文件
                            while ((len = zis.read(buffer)) > 0) {
                                // 因此（上一段的注释），这里要写0，len，表示从0开始存入buffer中存的len个字节
                                os.write(buffer, 0, len);
                            }
                        }
                    }
                    // 结束此条目，关闭条目
                    // 实际上这里做的并不是真正的关闭条目，因为是在同一个文件内，也不存在关闭与否的说法，准确地说这里是一个收尾性质的工作

                    // 以下全部来自AI的注释：
                    // 结束当前条目的读取，把流推进到下一条目开头；注意它不关闭流、也不关闭文件
                    // 实现上就是把当前条目「还没读的字节全部读掉丢弃」（JDK ZipInputStream.closeEntry）
                    // 两种情形：
                    //   .dat 条目  → 上面已读到 -1，此处 read() 立即返回 -1，等于空操作
                    //   非 .dat 条目 → 一个字节都没读过，这里才是真正跳过该条目数据的地方
                    // 补充：getNextEntry() 内部本就会先关闭上一条目（entry != null 时），
                    //      因此本行属于显式化写法，去掉也不影响行为，保留是为了意图清晰并与写入侧对称
                    zis.closeEntry();
                }
            }

            // 删除旧存档
            GamesInProgress.setUnknown(targetSlot);
            // 「备份导入成功」
            ShatteredPixelDungeon.scene().addToFront(new WndMessage(Messages.get(BackupSaveScene.class, "import_success")));
        } catch (Exception e) {
            // 导入失败：上报异常并提示
            ShatteredPixelDungeon.reportException(e);
            // 「导入失败！」
            ShatteredPixelDungeon.scene().addToFront(new WndMessage(Messages.get(BackupSaveScene.class, "import_fail")));
        }
    }

    /**
     * 将 .mlsp 备份文件导入。
     * <p><font color="orange"><b><h1>导入局外存档操作</h1></b></font></p>
     * <p>导入流程：</p>
     * <ol>
     *   <li>目标目录若存在则先清空其中所有 .dat 文件，不存在则创建</li>
     *   <li>解压 .mlsp（ZIP），将其中所有 .dat 条目写入目标目录</li>
     *   <li>提示导入成功</li>
     * </ol>
     *
     * @param mlspFile   备份文件句柄
     */
    private static void importMLSPtoRoot(FileHandle mlspFile) {
        try {
            ArrayList<String> oldFiles = FileUtils.filesInDir("");
            if (oldFiles != null) {
                for (String fName : oldFiles) {
                    if (fName.endsWith(".dat")) {
                        FileUtils.deleteFile(fName);
                    }
                }
                for (String fName : oldFiles) {
                    if (fName.equals("settings.xml")) {
                        FileUtils.deleteFile(fName);
                    }
                }
            }
            try (InputStream fis = mlspFile.read();
                 ZipInputStream zis = new ZipInputStream(fis)) {
                ZipEntry entry;
                byte[] buffer = new byte[4096];
                int len;
                while ((entry = zis.getNextEntry()) != null) {
                    String entryName = entry.getName();
                    if (entryName.endsWith(".dat")) {
                        FileHandle outDat = FileUtils.getFileHandle(entryName);
                        try (OutputStream os = outDat.write(false)) {
                            while ((len = zis.read(buffer)) > 0) {
                                os.write(buffer, 0, len);
                            }
                        }
                    }
                    if (entryName.equals("settings.xml")) {
                        // 直接原样字节复制，无任何XML解析、修复、重写
                        FileHandle outDat = FileUtils.getFileHandle(entryName);
                        try (OutputStream os = outDat.write(false)) {
                            while ((len = zis.read(buffer)) > 0) {
                                os.write(buffer, 0, len);
                            }
                        }
                    }
                    zis.closeEntry();
                }
            }
            // =================================================================
            // 【关键修复】写完文件后【立即】作废旧内存设置缓存，然后再 resetGlobalCache()。
            // resetGlobalCache 内部存在会写 settings.xml 的路径（例如 Rankings.load() 里
            // `SPDSettings.lastDaily(...)` 就是 put→flush）：若在作废前执行，会用"导入前的旧缓存"
            // 整体重写磁盘文件，把刚导入的 settings.xml（如 dlc 困难模式等键）抹掉——
            // 这正是"第一次导入总丢数据、第二次导入才正常"的根因（第一次触发写入、把旧值覆盖上去，
            // 第二次因 lastDaily 已是最新不再触发 flush）。
            // 先作废后，resetGlobalCache 内任何 SPDSettings 写入都会先重读刚导入的文件，
            // flush 的也是导入后的值，导入结果不再被旧缓存覆盖。
            // =================================================================
            SPDSettings.set( null );
            resetGlobalCache();
            ShatteredPixelDungeon.seamlessResetScene(new Game.SceneChangeCallback(){
                @Override public void beforeCreate(){ }
                @Override public void afterCreate(){
                    ShatteredPixelDungeon.scene().addToFront(new WndMessage(
                            Messages.get(BackupSaveScene.class, "import_success")));
                }
            });
        } catch (Exception e) {
            ShatteredPixelDungeon.reportException(e);
            ShatteredPixelDungeon.scene().addToFront(new WndMessage(Messages.get(BackupSaveScene.class, "import_fail")));
        }
    }

    /**
     * 统一的导入分流入口：按文件名前缀判断备份类型，再走对应流程。
     * 列表里点「导入备份」和底部「外部导入」都汇到这里。
     */
    private static void startImportFlow(FileHandle backupFile) {
        // 未知文件分流：不存在或非既定的拓展名
        if (!backupFile.exists()
                || backupFile.isDirectory()
                || !backupFile.name().endsWith(MLSP_EXT)) {
            ShatteredPixelDungeon.scene().addToFront(new WndMessage(
                    Messages.get(BackupSaveScene.class, "import_fail_unknownfile")));
            return;
        }
        // 全局存档分流
        if (backupFile.name().startsWith(GLOBAL_PREFIX)) {
            ShatteredPixelDungeon.scene().addToFront(new WndOptions(
                    Icons.get(Icons.WARNING),
                    Messages.get(BackupSaveScene.class, "warn_overwrite_title"),
                    Messages.get(BackupSaveScene.class, "import_global_confirm_desc"),
                    Messages.get(BackupSaveScene.class, "confirm_overwrite"),
                    Messages.get(BackupSaveScene.class, "cancel")
            ) {
                @Override
                protected void onSelect(int yesno) {
                    if (yesno == 0) importMLSPtoRoot(backupFile);
                }
            });
        }
        // 局内存档分流
        else if (backupFile.name().startsWith(SLOT_PREFIX)) {
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
                                    importMLSPtoSlot(backupFile, targetSlot);
                                }
                            }
                        });
                    }
                }
            });
        }
        // 未知文件分流：非既定的前缀名
        else {
            ShatteredPixelDungeon.scene().addToFront(new WndMessage(Messages.get(BackupSaveScene.class, "import_fail_unknownfile")));
        }
    }

    /** 清空全局数据的缓存文件 */
    private static void resetGlobalCache() {
        // 双保险：方法开头就先作废设置缓存。这样本方法内任何 SPDSettings 读取/写入
        // （如 Rankings.load 里的 lastDaily 越界修正与回写）都会基于磁盘上【最新】的
        // settings.xml（导入后的内容），而绝不会把导入前的旧缓存 flush 覆盖回去。
        SPDSettings.set( null );
        Badges.global = null;                     Badges.loadGlobal();
        PaswordBadges.global = null;              PaswordBadges.loadGlobal();
        Rankings.INSTANCE.records = null;         Rankings.INSTANCE.load();
        CollectRankings.INSTANCE.records = null;  CollectRankings.INSTANCE.load();
        KeyBindings.setAllBindings(new LinkedHashMap<>());
        SPDAction.loadBindings();
        Journal.resetForReload();                 Journal.loadGlobal();
        Bones.resetForReload();
        YuanTaStoneScene.YuanTaStoryManager.reload();
        reloadSettingsFromDisk();
    }

    /**
     * 重新从磁盘加载设置。SPDSettings 封装 libGDX Preferences：启动时一次性读入内存 map，此后不会自动重读文件；
     * 导入会替换磁盘上的 settings.xml，必须作废旧的内存缓存，否则下一次 flush 会把旧值整体覆盖回去。
     * <p>修复要点：</p>
     * <ol>
     *   <li>先用 {@link #ensureSettingsXmlValid} 把「裸 entry」格式（缺 XML 声明/DOCTYPE/根节点，
     *       常见于手工编辑或旧版本备份）修复成标准 Properties XML，否则 loadFromXML 会抛
     *       InvalidPropertiesFormatException，Lwjgl3Preferences 会把设置解析为空；</li>
     *   <li>无论解析成功与否都无条件 {@code SPDSettings.set(null)} 作废内存缓存——
     *       只要旧缓存存活，下一帧 updateSystemUI 的 put→flush 就会把导入前的旧值整体覆盖回新文件。</li>
     * </ol>
     */
    private static void reloadSettingsFromDisk() {
        FileHandle settingsHandle = FileUtils.getFileHandle("settings.xml");
        if (!settingsHandle.exists()) {
            SPDSettings.set( null );
            return;
        }
        // 先规范化再校验：保证磁盘文件是 Lwjgl3Preferences 能读的标准格式
        ensureSettingsXmlValid( settingsHandle );
        try (InputStream in = settingsHandle.read()) {
            java.util.Properties p = new java.util.Properties();
            p.loadFromXML( in );
        } catch (Exception e) {
            // 记录异常但继续：下面仍然要作废缓存，避免旧值被 flush 覆盖回来
            ShatteredPixelDungeon.reportException( e );
        } finally {
            // 无条件作废旧内存缓存；下次 get() 会重新从磁盘读取（若文件仍不可读则 Preferences 从空表开始，
            // 随后第一次 put 会用标准格式重写文件，不会再用旧值覆盖导入结果）
            SPDSettings.set( null );
        }
        ShatteredPixelDungeon.updateSystemUI();
    }

    /**
     * 确保 settings.xml 是 java.util.Properties 可解析的标准 XML 格式。
     * 部分历史或手工生成的备份文件缺少 XML 声明、DOCTYPE 与 &lt;properties&gt; 根节点（「裸 entry」形式），
     * 直接交给 libGDX Preferences 解析会抛 InvalidPropertiesFormatException 导致设置整体丢失/被旧缓存覆盖；
     * 本方法将「裸 entry」形式重写为标准格式，已是标准格式则原样保留。
     * <p>入口幂等：标准格式且可解析时不做任何改动；带 DOCTYPE 但解析失败、或缺少 DOCTYPE 时按
     * entry 正则重建。重建时先 {@link #unescapeXmlText} 再 {@link #escapeXmlText}，保证原始值无论是
     * 已转义（storeToXML 产物）还是未转义（手工编辑）都能无损往返，避免双重转义。</p>
     *
     * @param settingsHandle settings.xml 的文件句柄（用 FileUtils 的相对根目录句柄）
     */
    public static void ensureSettingsXmlValid(FileHandle settingsHandle) {
        if (settingsHandle == null || !settingsHandle.exists()) return;
        String raw;
        try {
            raw = settingsHandle.readString( "UTF-8" );
        } catch (Exception e) {
            ShatteredPixelDungeon.reportException( e );
            return;
        }
        // 已含 DOCTYPE：尝试解析，能解析即为标准格式，不动
        if (raw.contains( "<!DOCTYPE properties" )) {
            try (InputStream in = settingsHandle.read()) {
                java.util.Properties p = new java.util.Properties();
                p.loadFromXML( in );
                return;
            } catch (Exception e) {
                // 带 DOCTYPE 但无法解析（如内容被破坏）：继续走重建流程
                ShatteredPixelDungeon.reportException( e );
            }
        }

        StringBuilder out = new StringBuilder();
        out.append( "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" );
        out.append( "<!DOCTYPE properties SYSTEM \"http://java.sun.com/dtd/properties.dtd\">\n" );
        out.append( "<properties>\n" );
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(
                "<entry\\s+key=\"([^\"]*)\"[^>]*>(.*?)</entry>",
                java.util.regex.Pattern.DOTALL );
        java.util.regex.Matcher m = p.matcher( raw );
        boolean found = false;
        while (m.find()) {
            out.append( "<entry key=\"" ).append( escapeXmlText( unescapeXmlText( m.group( 1 ) ) ) ).append( "\">" )
                    .append( escapeXmlText( unescapeXmlText( m.group( 2 ) ) ) ).append( "</entry>\n" );
            found = true;
        }
        if (!found) return; // 无法识别条目结构：不做改写，交由 reloadSettingsFromDisk 兜底
        out.append( "</properties>\n" );
        try {
            settingsHandle.writeString( out.toString(), false, "UTF-8" );
        } catch (Exception e) {
            ShatteredPixelDungeon.reportException( e );
        }
    }

    /**
     * 宽松解码 XML 实体：把 &amp; &lt; &gt; &quot; &apos; 以及 &#NN; / &#xNN; 数字字符引用还原为字符。
     * 仅用于把「裸 entry」格式转标准格式前的预处理，无法识别的 & 序列保持原样，
     * 保证未转义与已转义两种来源都能无损往返（配合 {@link #escapeXmlText} 使用，杜绝双重转义）。
     */
    private static String unescapeXmlText(String s) {
        if (s == null || s.indexOf( '&' ) == -1) return s;
        StringBuilder sb = new StringBuilder( s.length() );
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt( i );
            if (c != '&') {
                sb.append( c );
                continue;
            }
            int semi = s.indexOf( ';', i );
            // 不是合法实体（没有分号、或实体名过长）→ 原样保留
            if (semi == -1 || semi - i > 10) {
                sb.append( c );
                continue;
            }
            String ent = s.substring( i + 1, semi );
            switch (ent) {
                case "amp":  sb.append( '&' ); i = semi; continue;
                case "lt":   sb.append( '<' ); i = semi; continue;
                case "gt":   sb.append( '>' ); i = semi; continue;
                case "quot": sb.append( '"' ); i = semi; continue;
                case "apos": sb.append( '\'' ); i = semi; continue;
            }
            if (ent.length() > 1 && ent.charAt( 0 ) == '#') {
                try {
                    int cp = (ent.length() > 2 && (ent.charAt( 1 ) == 'x' || ent.charAt( 1 ) == 'X'))
                            ? Integer.parseInt( ent.substring( 2 ), 16 )
                            : Integer.parseInt( ent.substring( 1 ) );
                    sb.appendCodePoint( cp );
                    i = semi;
                    continue;
                } catch (NumberFormatException ignored) {
                    // 非法数字引用：原样保留
                }
            }
            sb.append( c );
        }
        return sb.toString();
    }

    /** 与 java.util.Properties.storeToXML 一致的转义，保证解析往返无损 */
    private static String escapeXmlText(String s) {
        StringBuilder sb = new StringBuilder( s.length() );
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt( i );
            switch (c) {
                case '&':  sb.append( "&amp;" );  break;
                case '<':  sb.append( "&lt;" );   break;
                case '>':  sb.append( "&gt;" );   break;
                case '"':  sb.append( "&quot;" ); break;
                case '\'': sb.append( "&apos;" ); break;
                case '\t': sb.append( "&#09;" );  break;
                case '\n': sb.append( "&#10;" );  break;
                case '\r': sb.append( "&#13;" );  break;
                default:   sb.append( c );
            }
        }
        return sb.toString();
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
     * 取用于显示与命名的种子文本。
     *
     * customSeed 存的是玩家输入的原始文本（可能是种子码，也可能是任意自定义文字），
     * 不能按数字解析；只有没有自定义种子时，才把随机种子数值转成种子码。
     */
    private static String seedText(GamesInProgress.Info info) {
        if (info.customSeed != null && !info.customSeed.isEmpty()) {
            return info.customSeed;
        }
        try {
            return DungeonSeed.convertToCode(info.seed);
        } catch (IllegalArgumentException e) {
            return String.valueOf(info.seed);
        }
    }

    /**
     * 把种子文本转成能安全放进文件名的片段（去掉 Windows 不允许的字符）。
     */
    private static String sanitizeForFileName(String text) {
        // String safe = text.replaceAll("[\\\\/:*?\"<>|\\s]", "_");
        // 上方是黑名单写法，这种写法不包含各种控制符，在linux系统中可能出错，以下换成白名单正则表达式
        String safe = text.replaceAll("[^\\p{L}\\p{N}._-]", "_");
        return safe.isEmpty() ? "unknown" : safe;
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
     * <p><font color="yellow"><b><h2>内部弹窗 1：选择要导出的存档槽位</h2></b></font></p>
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
            // 「选择要导出的存档槽：」
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
                            // 「该槽位没有正在进行的存档，无法备份。」
                            ShatteredPixelDungeon.scene().addToFront(new WndMessage(Messages.get(BackupSaveScene.class, "export_empty_slot")));
                        } else {
                            // 有存档：弹出最终确认框，确认后执行导出
                            ShatteredPixelDungeon.scene().addToFront(new WndOptions(
                                    Icons.get(Icons.WARNING),
                                    // 「确认导出」
                                    Messages.get(BackupSaveScene.class, "confirm_export_title"),
                                    // 「将为此槽位创建存档备份。」
                                    Messages.get(BackupSaveScene.class, "confirm_export_msg"),
                                    // 「是」
                                    Messages.get(BackupSaveScene.class, "yes"),
                                    // 「否」
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

            // ---- 局外存档（全局数据）导出入口 ----
            RedButton btnGlobal = new RedButton(
                    Messages.get(BackupSaveScene.class, "export_global"), 6) {
                @Override
                protected void onClick() {
                    hide();   // 先关掉本弹窗，与槽位行保持一致
                    ShatteredPixelDungeon.scene().addToFront(new WndOptions(
                            Icons.get(Icons.WARNING),
                            Messages.get(BackupSaveScene.class, "confirm_export_title"),
                            Messages.get(BackupSaveScene.class, "confirm_export_global_msg"),
                            Messages.get(BackupSaveScene.class, "yes"),
                            Messages.get(BackupSaveScene.class, "no")
                    ) {
                        @Override
                        protected void onSelect(int index) {
                            if (index == 0) {
                                exportWholeSlotToMLSP();
                            }
                        }
                    });
                }
            };
            btnGlobal.leftJustify = true;
            btnGlobal.multiline = true;
            btnGlobal.setRect(0, pos, WIDTH, btnGlobal.reqHeight() + 10);
            add(btnGlobal);
            pos = btnGlobal.bottom() + GAP;

            // 根据内容总高度调整弹窗尺寸
            resize(WIDTH, (int) pos);
        }
    }

    /**
     * 内部弹窗 2：查看指定存档槽位的详情（WndInfoSlotSave）。
     * <p><font color="yellow"><b><h2>内部弹窗 2：查看指定存档槽位的详情</h2></b></font></p>
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
            // 依次填充：等级、层数、种子、专精、生命值、力量
            String text = Messages.get(BackupSaveScene.class, "info_text",
                    info.level,
                    info.depth,
                    seedText(info),
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
     * <p><font color="green"><b><h3>备份文件类</h3></b></font></p>
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
     * <p><font color="green"><b><h3>备份文件按钮容器</h3></b></font></p>
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
     * <p><font color="green"><b><h3>备份文件按钮类</h3></b></font></p>
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
                    if (index == 0)
                    {
                        startImportFlow(backup.file);
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
