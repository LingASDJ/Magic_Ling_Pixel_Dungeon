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

public class BackupSaveScene extends PixelScene {

    private final ArrayList<BackupInfo> infos = new ArrayList<>();
    public static String BACKUP_FOLDER = DeviceCompat.isDesktop() ? "AppData/Roaming/.shatteredpixel/Magic Ling Pixel Dungeon/pd_backups" : "pd_backups";
    public static final String MLSP_EXT = ".mlsp";
    private static final int MARGIN = 8;
    private static final int GAP = 10;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter
            .ofPattern("yyyy-MM-dd HH:mm")
            .withZone(ZoneId.systemDefault());

    @Override
    public void create() {
        super.create();

        int w = Camera.main.width;
        int h = Camera.main.height;

        int panelWidth = Camera.main.width;
        int panelHeight = h - 45;

        RenderedTextBlock title = PixelScene.renderTextBlock(Messages.get(this, "title"), 11);
        title.hardlight(0x88CCEE);
        title.setPos((w - title.width()) / 2f, MARGIN);
        align(title);
        add(title);

        ExitButton btnExit = new ExitButton();
        btnExit.setPos(w - btnExit.width() - MARGIN, MARGIN);
        add(btnExit);

        // 清除全部备份按钮
        RedButton btnClearAll = new RedButton(Messages.get(this, "clear_all"), 7) {
            @Override
            protected void onClick() {
                ShatteredPixelDungeon.scene().add(new WndOptions(
                        Icons.get(Icons.WARNING),
                        Messages.get(BackupSaveScene.class, "clear_all_title"),
                        Messages.get(BackupSaveScene.class, "clear_all_msg"),
                        Messages.get(BackupSaveScene.class, "confirm"),
                        Messages.get(BackupSaveScene.class, "cancel")
                ) {
                    @Override
                    protected void onSelect(int index) {
                        if (index == 0) {
                            FileHandle backupDir = Gdx.files.external(BACKUP_FOLDER);
                            if (backupDir.exists()) {
                                FileHandle[] files = backupDir.list();
                                if (files != null) {
                                    for (FileHandle f : files) {
                                        if (f.name().endsWith(MLSP_EXT)) {
                                            f.delete();
                                        }
                                    }
                                }
                            }
                            ShatteredPixelDungeon.switchNoFade(BackupSaveScene.class);
                        }
                    }
                });
            }
        };
        btnClearAll.setSize(36, 20);
        btnClearAll.setPos(2, MARGIN);
        add(btnClearAll);

        // 导出存档按钮，唤起内部弹窗 WndChooseSlotExport
        RedButton btnExport = new RedButton(Messages.get(this, "export_slot"), 7) {
            @Override
            protected void onClick() {
                ShatteredPixelDungeon.scene().addToFront(new WndChooseSlotExport());
            }
        };
        btnExport.setSize(w, 20);
        btnExport.setPos(0, h-20);
        add(btnExport);

        NinePatch panel = Chrome.get(Chrome.Type.WINDOW_SILVER);
        panel.size(panelWidth, panelHeight);
        panel.x = (w - panelWidth) / 2f;
        panel.y = title.bottom() + GAP;
        align(panel);
        add(panel);

        ScrollPane list = new ScrollPane(new Component()) {
            @Override
            public void onClick(float x, float y) {
                for (BackupInfo info : infos) {
                    if (info.onClick(x, y)) {
                        return;
                    }
                }
            }
        };
        add(list);

        ArrayList<BackupFile> backupList = new ArrayList<>();
        FileHandle backupDir = Gdx.files.external(BACKUP_FOLDER);
        if (backupDir.exists()) {
            try {
                FileHandle[] files = backupDir.list();
                if (files != null && files.length > 0) {
                    for (FileHandle f : files) {
                        if (f.name().endsWith(MLSP_EXT)) {
                            String fileName = f.name();
                            long lastModified = f.lastModified();
                            backupList.add(new BackupFile(fileName, f, lastModified));
                        }
                    }
                    // 修改时间降序，最新在前
                    Collections.sort(backupList, (o1, o2) -> Long.compare(o2.modTime, o1.modTime));
                } else {
                    showEmptyMsg(w, h, Messages.get(this, "no_backups"), 0x88CCEE);
                    return;
                }
            } catch (Exception e) {
                ShatteredPixelDungeon.reportException(e);
                showEmptyMsg(w, h, Messages.get(this, "read_error"), 0xFF5555);
                return;
            }
        } else {
            showEmptyMsg(w, h, Messages.get(this, "no_backups"), 0x88CCEE);
            return;
        }

        BackupInfo info = new BackupInfo();
        for (BackupFile bf : backupList) {
            BackupFileButton bfb = new BackupFileButton(bf);
            info.addButton(bfb);
        }
        infos.add(info);

        Component content = list.content();
        content.clear();
        float posY = 0;
        for (BackupInfo info1 : infos) {
            info1.setRect(0, posY, panel.innerWidth(), 0);
            content.add(info1);
            posY += info1.height() + GAP;
        }
        content.setSize(panel.innerWidth(), (int) Math.ceil(posY));

        list.setRect(
                panel.x + panel.marginLeft(),
                panel.y + panel.marginTop(),
                panel.innerWidth(),
                panel.innerHeight()
        );
        list.scrollTo(0, 0);

        Archs archs = new Archs();
        archs.setSize(w, h);
        addToBack(archs);

        fadeIn();
    }

    public static void exportSlotToMLSP(int slot) {
        GamesInProgress.Info saveInfo = GamesInProgress.check(slot);
        if (saveInfo == null) {
            ShatteredPixelDungeon.scene().addToFront(new WndMessage(Messages.get(BackupSaveScene.class, "export_empty_slot")));
            return;
        }
        try {
            String slotFolder = GamesInProgress.gameFolder(slot);
            FileHandle backupDirHandle = Gdx.files.external(BACKUP_FOLDER);
            if (!backupDirHandle.exists()) {
                backupDirHandle.mkdirs();
            }

            String seedStr = saveInfo.customSeed.isEmpty() ? String.valueOf(saveInfo.seed) : saveInfo.customSeed;

            String fileName = String.format(Locale.US, "slot%s%s_%s%s", slot,"-", DungeonSeed.convertToCode(Long.parseLong(seedStr)), MLSP_EXT);
            FileHandle mlspHandle = backupDirHandle.child(fileName);

            try (ZipOutputStream zos = new ZipOutputStream(mlspHandle.write(false))) {
                ArrayList<String> fileList = FileUtils.filesInDir(slotFolder);
                for (String fname : fileList) {
                    if (fname.endsWith(".dat")) {
                        String fullPath = slotFolder + "/" + fname;
                        FileHandle datHandle = FileUtils.getFileHandle(fullPath);
                        byte[] data = datHandle.readBytes();

                        ZipEntry entry = new ZipEntry(fname);
                        zos.putNextEntry(entry);
                        zos.write(data);
                        zos.closeEntry();
                    }
                }
            }

            ShatteredPixelDungeon.scene().addToFront(new WndMessage(Messages.get(BackupSaveScene.class, "export_success", fileName)));
            ShatteredPixelDungeon.switchNoFade(BackupSaveScene.class);
        } catch (IOException e) {
            ShatteredPixelDungeon.reportException(e);
            ShatteredPixelDungeon.scene().addToFront(new WndMessage(Messages.get(BackupSaveScene.class, "export_fail")));
        }
    }

    private static void importMLSPtoSlot(FileHandle mlspFile, int targetSlot) {
        try {
            String slotFolder = GamesInProgress.gameFolder(targetSlot);
            FileHandle slotDirHandle = FileUtils.getFileHandle(slotFolder);

            //清空旧存档所有dat文件
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
                slotDirHandle.mkdirs();
            }

            try (InputStream fis = mlspFile.read();
                 ZipInputStream zis = new ZipInputStream(fis)) {

                ZipEntry entry;
                byte[] buffer = new byte[4096];
                int len;
                while ((entry = zis.getNextEntry()) != null) {
                    String entryName = entry.getName();
                    if (entryName.endsWith(".dat")) {
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

            GamesInProgress.setUnknown(targetSlot);
            ShatteredPixelDungeon.scene().addToFront(new WndMessage(Messages.get(BackupSaveScene.class, "import_success")));
        } catch (Exception e) {
            ShatteredPixelDungeon.reportException(e);
            ShatteredPixelDungeon.scene().addToFront(new WndMessage(Messages.get(BackupSaveScene.class, "import_fail")));
        }
    }

    private static boolean openDirectory(FileHandle dirHandle) {
        if (!dirHandle.exists() || !dirHandle.isDirectory()) return false;
        String path = dirHandle.file().getAbsolutePath();
        String os = System.getProperty("os.name").toLowerCase();
        try {
            if (os.contains("win")) {
                Runtime.getRuntime().exec(new String[]{"explorer.exe", path});
            } else if (os.contains("mac")) {
                Runtime.getRuntime().exec(new String[]{"open", path});
            } else if (os.contains("nix") || os.contains("nux")) {
                Runtime.getRuntime().exec(new String[]{"xdg-open", path});
            } else {
                return false;
            }
            return true;
        } catch (IOException e) {
            ShatteredPixelDungeon.reportException(e);
            return false;
        }
    }

    private void showEmptyMsg(int w, int h, String msg, int color) {
        RenderedTextBlock text = PixelScene.renderTextBlock(msg, 8);
        text.hardlight(color);
        text.setPos((w - text.width()) / 2f, (h - text.height()) / 2f);
        align(text);
        add(text);
    }

    @Override
    protected void onBackPressed() {
        ShatteredPixelDungeon.switchNoFade(TitleScene.class);
    }

    public static class WndChooseSlotExport extends Window {
        private static final int WIDTH = 160;
        private static final float GAP = 2;

        public WndChooseSlotExport() {
            super();

            RenderedTextBlock message = PixelScene.renderTextBlock(8);
            message.text(Messages.get(BackupSaveScene.class, "select_slot_export"), WIDTH);
            message.setPos(0, 0);
            add(message);

            float pos = message.bottom() + 3 * GAP;

            for (int slot = 1; slot <= GamesInProgress.MAX_SLOTS; slot++) {
                GamesInProgress.Info info = GamesInProgress.check(slot);
                String btnLabel;
                if (info != null) {
                    btnLabel = "Slot" + slot + " — " + info.heroClass.title();
                } else {
                    btnLabel = "Slot" + slot + " — 空槽";
                }

                final int selectedSlot = slot;

                RedButton btnSlot = new RedButton(btnLabel, 6) {
                    @Override
                    protected void onClick() {
                        hide();
                        GamesInProgress.Info saveInfo = GamesInProgress.check(selectedSlot);
                        if (saveInfo == null) {
                            ShatteredPixelDungeon.scene().addToFront(new WndMessage(Messages.get(BackupSaveScene.class, "export_empty_slot")));
                        } else {
                            ShatteredPixelDungeon.scene().addToFront(new WndOptions(
                                    Icons.get(Icons.WARNING),
                                    Messages.get(BackupSaveScene.class, "confirm_export_title"),
                                    Messages.get(BackupSaveScene.class, "confirm_export_msg"),
                                    Messages.get(BackupSaveScene.class, "yes"),
                                    Messages.get(BackupSaveScene.class, "no")
                            ) {
                                @Override
                                protected void onSelect(int index) {
                                    if (index == 0) {
                                        exportSlotToMLSP(selectedSlot);
                                    }
                                }
                            });
                        }
                    }
                };
                btnSlot.leftJustify = true;
                btnSlot.multiline = true;
                btnSlot.setRect(0, pos, WIDTH - 20, btnSlot.reqHeight() + 10);
                add(btnSlot);

                GamesInProgress.Info slotInfo = info;
                IconButton clsInfo = new IconButton(Icons.get(Icons.INFO)) {
                    @Override
                    protected void onClick() {
                        ShatteredPixelDungeon.scene().addToFront(new WndInfoSlotSave(slotInfo, selectedSlot));
                    }
                };
                clsInfo.setRect(WIDTH - 20, btnSlot.top() + (btnSlot.height() - 20) / 2, 20, 20);
                add(clsInfo);

                pos = btnSlot.bottom() + GAP;
            }
            resize(WIDTH, (int) pos);
        }
    }

    // ==============================================
    // 内部静态窗口2：INFO详情弹窗
    // ==============================================
    public static class WndInfoSlotSave extends Window {
        private static final int WIDTH = 120;

        public WndInfoSlotSave(GamesInProgress.Info info, int slotNum) {
            super();
            if (info == null) {
                RenderedTextBlock txt = PixelScene.renderTextBlock(8);
                txt.text(Messages.get(BackupSaveScene.class, "empty_slot_tip"), WIDTH);
                txt.setPos(0, 0);
                add(txt);
                resize(WIDTH, (int) txt.bottom());
                return;
            }

            IconTitle titlebar = new IconTitle();
            titlebar.icon(HeroSprite.avatar(info.heroClass, info.armorTier));
            titlebar.label("Slot" + slotNum + "-" + info.heroClass.title());
            titlebar.setRect(0, 0, WIDTH, 0);
            add(titlebar);

            RenderedTextBlock message = PixelScene.renderTextBlock(7);
            String seed = info.customSeed.isEmpty() ? String.valueOf(info.seed) : info.customSeed;
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

            resize(WIDTH, (int) message.bottom());
        }
    }

    // 备份文件实体
    private static class BackupFile {
        String fileName;
        FileHandle file;
        long modTime;

        BackupFile(String fileName, FileHandle file, long modTime) {
            this.fileName = fileName;
            this.file = file;
            this.modTime = modTime;
        }
    }

    private static class BackupInfo extends Component {
        private ArrayList<BackupFileButton> buttons = new ArrayList<>();

        public void addButton(BackupFileButton btn) {
            buttons.add(btn);
            add(btn);
            layout();
        }

        public boolean onClick(float x, float y) {
            float lx = x - this.x;
            float ly = y - this.y;
            for (BackupFileButton b : buttons) {
                if (lx >= b.x && lx <= b.x + b.width() && ly >= b.y && ly <= b.y + b.height()) {
                    b.onClick();
                    return true;
                }
            }
            return false;
        }

        @Override
        protected void layout() {
            float posY = GAP;
            float maxW = 0;
            for (BackupFileButton b : buttons) {
                b.setPos(GAP, posY);
                posY += b.height() + GAP;
                maxW = Math.max(maxW, b.width());
            }
            height = posY;
            width = maxW + GAP * 2;
        }
    }

    private static class BackupFileButton extends Component {
        protected Image icon;
        protected BackupFile backup;
        protected RenderedTextBlock nameText;
        protected RenderedTextBlock timeText;
        protected NinePatch bg;

        public BackupFileButton(BackupFile backupFile) {
            super();
            bg = Chrome.get(Chrome.Type.WINDOW_SILVER);
            add(bg);
            icon = Icons.get(Icons.CATALOG);
            icon.hardlight(0x44DD88);
            add(icon);
            this.backup = backupFile;

            nameText = PixelScene.renderTextBlock(backup.fileName, 6);
            nameText.hardlight(0xFFFFFF);
            add(nameText);

            Instant instant = Instant.ofEpochMilli(backup.modTime);
            String timeStr = FORMATTER.format(instant);
            timeText = PixelScene.renderTextBlock(timeStr, 6);
            timeText.hardlight(0xCCCCCC);
            add(timeText);
            layout();
        }

        protected void onClick() {
            ShatteredPixelDungeon.scene().add(new WndOptions(
                    Icons.get(Icons.CATALOG),
                    backup.fileName,
                    Messages.get(BackupSaveScene.class, "backup_options"),
                    Messages.get(BackupSaveScene.class, "import_backup"),
                    Messages.get(BackupSaveScene.class, "extract_file"), // 提取文件
                    Messages.get(BackupSaveScene.class, "delete_backup"),
                    Messages.get(BackupSaveScene.class, "cancel")
            ) {
                @Override
                protected void onSelect(int index) {
                    if (index == 0) {
                        //导入，选择槽位
                        ShatteredPixelDungeon.scene().add(new WndOptions(
                                Icons.get(Icons.WARNING),
                                Messages.get(BackupSaveScene.class, "import_confirm_title"),
                                Messages.get(BackupSaveScene.class, "import_confirm_desc"),
                                "Slot1", "Slot2", "Slot3", "Slot4", "Slot5", "Slot6",
                                Messages.get(BackupSaveScene.class, "cancel")
                        ) {
                            @Override
                            protected void onSelect(int slotIdx) {
                                if (slotIdx >= 0 && slotIdx <= 5) {
                                    int targetSlot = slotIdx + 1;
                                    ShatteredPixelDungeon.scene().add(new WndOptions(
                                            Icons.get(Icons.WARNING),
                                            Messages.get(BackupSaveScene.class, "warn_overwrite_title"),
                                            Messages.get(BackupSaveScene.class, "warn_overwrite_desc"),
                                            Messages.get(BackupSaveScene.class, "confirm_overwrite"),
                                            Messages.get(BackupSaveScene.class, "cancel")
                                    ) {
                                        @Override
                                        protected void onSelect(int yesno) {
                                            if (yesno == 0) {
                                                importMLSPtoSlot(backup.file, targetSlot);
                                            }
                                        }
                                    });
                                }
                            }
                        });
                    } else if (index == 1) {
                        boolean ok = openDirectory(backup.file.parent());
                        if (!ok) {
                            ShatteredPixelDungeon.scene().addToFront(new WndMessage(Messages.get(BackupSaveScene.class, "extract_fail")));
                        }
                    } else if (index == 2) {
                        ShatteredPixelDungeon.scene().add(new WndOptions(
                                Icons.get(Icons.WARNING),
                                Messages.get(BackupSaveScene.class, "del_backup_title"),
                                Messages.get(BackupSaveScene.class, "del_backup_desc"),
                                Messages.get(BackupSaveScene.class, "confirm"),
                                Messages.get(BackupSaveScene.class, "cancel")
                        ) {
                            @Override
                            protected void onSelect(int delIdx) {
                                if (delIdx == 0) {
                                    backup.file.delete();
                                    ShatteredPixelDungeon.switchNoFade(BackupSaveScene.class);
                                }
                            }
                        });
                    }
                }
            });
        }

        @Override
        protected void layout() {
            super.layout();
            bg.x = x;
            bg.y = y;
            icon.x = x + GAP;
            icon.y = y + (height - icon.height()) / 2f;
            PixelScene.align(icon);

            nameText.setPos(icon.x + icon.width + GAP, y + GAP);
            if (timeText != null) {
                timeText.setPos(nameText.left(), nameText.bottom() + 2);
                height = Math.max(height, timeText.bottom() - y + GAP);
            } else {
                height = Math.max(height, nameText.bottom() - y + GAP);
            }
            bg.size(Camera.main.width-40, height);
        }

        @Override
        public float width() {
            return Math.max(super.width(), nameText.width() + icon.width() + GAP * 3);
        }

        @Override
        public float height() {
            return Math.max(24, super.height());
        }
    }
}
