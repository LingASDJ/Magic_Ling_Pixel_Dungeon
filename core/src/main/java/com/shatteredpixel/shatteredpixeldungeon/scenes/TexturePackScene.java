package com.shatteredpixel.shatteredpixeldungeon.scenes;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Chrome;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.Archs;
import com.shatteredpixel.shatteredpixeldungeon.ui.ExitButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.ScrollPane;
import com.shatteredpixel.shatteredpixeldungeon.ui.StyledButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.windows.IconTitle;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndError;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndMessage;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.watabou.gltextures.SmartTexture;
import com.watabou.gltextures.TextureCache;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Image;
import com.watabou.noosa.NinePatch;
import com.watabou.noosa.ui.Component;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class TexturePackScene extends PixelScene {

    public static final int REQUEST_CODE_IMPORT_PACK = 1001;

    private static final String TEXTURE_PACKS_DIR = "mlpd_packages";
    private static final String ASSET_PACKS_DIR = "texture_load";

    private static final ArrayList<String> PRESET_PACKS = new ArrayList<>();

    static {
        PRESET_PACKS.add("shpd.mlpack");
        PRESET_PACKS.add("classic.mlpack");
        PRESET_PACKS.add("alpha.mlpack");
        PRESET_PACKS.add("mlpd.mlpack");
        PRESET_PACKS.add("ancity.mlpack");
    }

    private static final String[] ALLOWED_EXTENSIONS = {".zip", ".mlpack"};

    private static final int BTN_HEIGHT = 22;
    private static final int GAP = 2;
    private static final int SCROLL_MARGIN = 20;

    private Component content;

    private boolean isImporting = false;
    private StyledButton btnImport;

    private static Map<String, String> assetPaths = new HashMap<>();

    private static TexturePackScene instance;

    static {
        scanAssetPaths();
    }

    private static void scanAssetPaths() {
        try {
            Class<?>[] innerClasses = Assets.class.getDeclaredClasses();

            for (Class<?> innerClass : innerClasses) {
                Field[] fields = innerClass.getDeclaredFields();

                for (Field field : fields) {
                    try {
                        if (!Modifier.isStatic(field.getModifiers())) {
                            continue;
                        }

                        if (field.getType().isArray()) {
                            continue;
                        }

                        Object value = field.get(null);

                        if (value instanceof String) {
                            String path = (String) value;

                            if (path.endsWith(".png")) {
                                String key = innerClass.getSimpleName() + "." + field.getName();
                                assetPaths.put(key, path);
                            }
                        }
                    } catch (IllegalAccessException ignored) {
                    }
                }
            }
        } catch (Exception e) {
            ShatteredPixelDungeon.reportException(e);
        }
    }

    public static void handleActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_IMPORT_PACK && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            if (instance != null) {
                instance.importPackFromUri(uri);
            }
        } else if (requestCode == REQUEST_CODE_IMPORT_PACK) {
            if (instance != null) {
                instance.isImporting = false;
            }
        }
    }

    private static FileHandle getTexturePacksDir() {
        FileHandle texturePacksDir = Gdx.files.external(TEXTURE_PACKS_DIR);

        if (!texturePacksDir.exists()) {
            texturePacksDir.mkdirs();
        }

        return texturePacksDir;
    }

    private static String getFileNameFromUri(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            try {
                AndroidApplication androidApp = (AndroidApplication) Gdx.app;
                Activity activity = (Activity) androidApp.getContext();

                android.database.Cursor cursor = activity.getContentResolver().query(uri, null, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME);
                    if (index != -1) {
                        result = cursor.getString(index);
                    }
                    cursor.close();
                }
            } catch (Exception e) {
                String path = uri.getPath();
                int cut = path.lastIndexOf('/');
                if (cut != -1) {
                    result = path.substring(cut + 1);
                }
            }
        } else if (uri.getScheme().equals("file")) {
            result = uri.getLastPathSegment();
        }

        return result;
    }

    public static void loadCustomTexture() {
        String path = SPDSettings.customTexturePack();

        if (path != null && !path.isEmpty()) {
            Object source;

            if (path.contains(ASSET_PACKS_DIR)) {
                String fileName = new File(path).getName();
                source = Gdx.files.internal(ASSET_PACKS_DIR + "/" + fileName);
            } else {
                source = new File(path);
            }

            if (source != null) {
                replaceAssets(source);
            }
        }
    }

    @Override
    public void create() {
        super.create();

        instance = this;

        PixelScene.uiCamera.visible = false;

        int w = Camera.main.width;
        int h = Camera.main.height;
        boolean landscape = PixelScene.landscape();

        Archs archs = new Archs();
        archs.setSize(w, h);
        addToBack(archs);

        IconTitle title = new IconTitle(Icons.DATA.get(), Messages.get(this, "title"));
        title.setSize(200, 0);
        title.setPos(
                (w - title.reqWidth()) / 2f,
                (20 - title.reqWidth()) / 2f
        );
        align(title);
        add(title);

        ExitButton btnExit = new ExitButton();
        btnExit.setPos(w - btnExit.width(), 0);
        add(btnExit);

        NinePatch panel = Chrome.get(Chrome.Type.BLANK);
        int pw = w - SCROLL_MARGIN * 2;
        int ph = h - 36 - BTN_HEIGHT - GAP;

        panel.size(pw, ph);
        panel.x = (w - pw) / 2f;
        panel.y = title.bottom() + 5;
        align(panel);
        add(panel);

        ScrollPane list = new ScrollPane(new Component());
        add(list);

        content = list.content();
        content.clear();

        float posY = 0;
        float nextPosY;
        boolean second;
        int columns = landscape ? 2 : 1;

        Component packInfo = new PackInfo();
        packInfo.setRect(0, posY, panel.innerWidth(), 0);
        content.add(packInfo);
        posY = nextPosY = packInfo.bottom() + GAP;
        second = false;

        FileHandle dir = getTexturePacksDir();

        FileHandle[] localFiles = dir.list((d, name) -> {
            for (String ext : ALLOWED_EXTENSIONS) {
                if (name.toLowerCase().endsWith(ext)) {
                    return true;
                }
            }
            return false;
        });

        List<FileHandle> assetFiles = new ArrayList<>();
        for (String fileName : PRESET_PACKS) {
            FileHandle handle = Gdx.files.internal(ASSET_PACKS_DIR + "/" + fileName);
            if (handle.exists()) {
                assetFiles.add(handle);
            }
        }

        String activePath = SPDSettings.customTexturePack();

        boolean hasPacks = false;

        if (localFiles != null && localFiles.length > 0) {
            hasPacks = true;
            for (FileHandle file : localFiles) {
                TexturePackItem item = new TexturePackItem(file, activePath, false);
                addItemToContent(item, columns, panel, posY, nextPosY, second);
                if (columns == 1) {
                    posY = nextPosY = item.bottom() + GAP;
                } else {
                    second = !second;
                    if (!second) posY = nextPosY;
                    nextPosY = Math.max(item.bottom(), nextPosY);
                }
            }
        }

        if (!assetFiles.isEmpty()) {
            hasPacks = true;
            for (FileHandle handle : assetFiles) {
                TexturePackItem item = new TexturePackItem(handle, activePath, true);
                addItemToContent(item, columns, panel, posY, nextPosY, second);
                if (columns == 1) {
                    posY = nextPosY = item.bottom() + GAP;
                } else {
                    second = !second;
                    if (!second) posY = nextPosY;
                    nextPosY = Math.max(item.bottom(), nextPosY);
                }
            }
        }

        if (!hasPacks) {
            RenderedTextBlock emptyText = PixelScene.renderTextBlock(Messages.get(this, "no_packs"), 8);
            emptyText.hardlight(Window.TITLE_COLOR);
            emptyText.setPos((panel.innerWidth() - emptyText.width()) / 2f, posY);
            align(emptyText);
            content.add(emptyText);
            posY = emptyText.bottom() + GAP;
        }

        content.setSize(panel.innerWidth(), (int) Math.ceil(posY));
        list.setRect(panel.x, panel.y, panel.width(), panel.height());

        float btnWidth = (panel.width() - GAP);
        float btnY = Camera.main.height - 35;

        btnImport = new StyledButton(Chrome.Type.GREY_BUTTON_TR, Messages.get(this, "import")) {
            @Override
            protected void onClick() {
                if (isImporting) return;
                importTexturePack();
            }
        };
        btnImport.icon(Icons.get(Icons.PREFS));
        btnImport.textColor(Window.TITLE_COLOR);
        btnImport.setRect(panel.x, btnY, btnWidth, BTN_HEIGHT);
        add(btnImport);

        fadeIn();
    }

    private void addItemToContent(TexturePackItem item, int columns, NinePatch panel, float posY, float nextPosY, boolean second) {
        item.multiline = true;
        if (columns == 1) {
            item.setRect(0, posY, panel.innerWidth(), BTN_HEIGHT);
        } else {
            if (!second) {
                item.setRect(0, posY, panel.innerWidth() / 2f - GAP / 2f, BTN_HEIGHT);
            } else {
                item.setRect(panel.innerWidth() / 2f + GAP / 2f, posY, panel.innerWidth() / 2f - GAP / 2f, BTN_HEIGHT);
            }
        }
        content.add(item);
    }

    @Override
    protected void onBackPressed() {
        ShatteredPixelDungeon.switchNoFade(TitleScene.class);
    }

    @Override
    public void update() {
        super.update();
        if (btnImport != null) {
            btnImport.active = !isImporting;
        }
    }

    private void importTexturePack() {
        if (Gdx.app.getType() == com.badlogic.gdx.Application.ApplicationType.Android) {
            importTexturePackAndroid();
        } else {
            add(new WndError(Messages.get(this, "unsupported_platform")));
        }
    }

    private void importTexturePackAndroid() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        AndroidApplication androidApp = (AndroidApplication) Gdx.app;
        Activity activity = (Activity) androidApp.getContext();
        activity.startActivityForResult(intent, REQUEST_CODE_IMPORT_PACK);
    }

    private void importPackFromUri(final Uri uri) {
        isImporting = true;

        new Thread(() -> {
            FileHandle tempFileHandle = null;
            try {
                AndroidApplication androidApp = (AndroidApplication) Gdx.app;
                Activity activity = (Activity) androidApp.getContext();
                InputStream inputStream = activity.getContentResolver().openInputStream(uri);

                String displayName = getFileNameFromUri(uri);
                if (displayName == null || displayName.isEmpty()) {
                    displayName = "temp_pack.zip";
                }

                boolean isValid = false;
                for (String ext : ALLOWED_EXTENSIONS) {
                    if (displayName.toLowerCase().endsWith(ext)) {
                        isValid = true;
                        break;
                    }
                }

                if (!isValid) {
                    Gdx.app.postRunnable(() -> {
                        add(new WndError(Messages.get(this, "invalid_pack")));
                        isImporting = false;
                    });
                    return;
                }

                tempFileHandle = Gdx.files.local("temp_validate_pack_" + System.currentTimeMillis());

                try (FileOutputStream fos = new FileOutputStream(tempFileHandle.file())) {
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = inputStream.read(buffer)) > 0) {
                        fos.write(buffer, 0, length);
                    }
                } finally {
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (IOException e) {
                            add(new WndError("Error closing input stream" + "\n" + e.getMessage()));
                        }
                    }
                }

                if (!isValidTexturePack(tempFileHandle.file())) {
                    Gdx.app.postRunnable(() -> {
                        add(new WndError(Messages.get(this, "invalid_pack")));
                        isImporting = false;
                    });
                    return;
                }

                FileHandle destDir = getTexturePacksDir();
                FileHandle destFile = destDir.child(displayName);

                byte[] bytes = tempFileHandle.readBytes();
                destFile.writeBytes(bytes, false);

                if (tempFileHandle.exists()) {
                    tempFileHandle.delete();
                }

                Gdx.app.postRunnable(() -> {
                    ShatteredPixelDungeon.seamlessResetScene();
                    isImporting = false;
                });

            } catch (Exception e) {
                Gdx.app.postRunnable(() -> {
                    add(new WndError(Messages.get(this, "import_failed") + "\n" + e.getMessage()));
                    isImporting = false;
                });
            } finally {
                if (tempFileHandle != null && tempFileHandle.exists()) {
                    try {
                        tempFileHandle.delete();
                        add(new WndError("TexturePackScene"+"\n"+"Deleted temp file in finally block: " + tempFileHandle.name()));
                    } catch (Exception e) {
                        add(new WndError("TexturePackScene"+"\n"+"Failed to delete temp file:  " + tempFileHandle.name()+e.getMessage()));
                    }
                }
            }
        }).start();
    }

    private boolean isValidTexturePack(File file) {
        try (ZipFile zipFile = new ZipFile(file)) {
            boolean hasManifest = zipFile.getEntry("manifest.json") != null;
            boolean hasTextures = false;

            for (ZipEntry entry : Collections.list(zipFile.entries())) {
                if (!entry.isDirectory() && entry.getName().endsWith(".png")) {
                    hasTextures = true;
                    break;
                }
            }

            return hasManifest && hasTextures;
        } catch (IOException e) {
            return false;
        }
    }

    public static void replaceAssets(Object source) {
        try {
            ZipInputStream zipInputStream;

            if (source instanceof FileHandle) {
                FileHandle handle = (FileHandle) source;
                zipInputStream = new ZipInputStream(handle.read());
            } else {
                zipInputStream = new ZipInputStream(new FileInputStream((File) source));
            }

            ZipEntry entry;
            while ((entry = zipInputStream.getNextEntry()) != null) {
                if (!entry.isDirectory() && entry.getName().endsWith(".png")) {
                    String texturePath = entry.getName();
                    String matchedPath = null;

                    for (Map.Entry<String, String> assetEntry : assetPaths.entrySet()) {
                        if (assetEntry.getValue().equals(texturePath)) {
                            matchedPath = assetEntry.getValue();
                            break;
                        }
                    }

                    if (matchedPath == null) {
                        continue;
                    }

                    try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                        byte[] buffer = new byte[1024];
                        int len;
                        while ((len = zipInputStream.read(buffer)) > 0) {
                            baos.write(buffer, 0, len);
                        }

                        Pixmap pixmap = new Pixmap(baos.toByteArray(), 0, baos.size());
                        SmartTexture smartTexture = new SmartTexture(pixmap);
                        TextureCache.add(matchedPath, smartTexture);
                    }
                }
                zipInputStream.closeEntry();
            }
            zipInputStream.close();

            TextureCache.reload();
        } catch (Exception e) {
            ShatteredPixelDungeon.reportException(e);
        }
    }

    private static class PackInfo extends Component {

        NinePatch bg;
        RenderedTextBlock text;
        RedButton button;

        @Override
        protected void createChildren() {
            bg = Chrome.get(Chrome.Type.BLANK);
            add(bg);

            String message = "";
            message += Messages.get(TexturePackScene.class, "title");

            text = PixelScene.renderTextBlock(message, 9);
            text.align(RenderedTextBlock.CENTER_ALIGN);
            add(text);
        }

        @Override
        protected void layout() {
            bg.x = x;
            bg.y = y;

            text.maxWidth((int) width - bg.marginHor());
            text.setPos(x + (width - text.width()) / 2f, y + 8);

            height = (text.bottom()) - y;

            if (button != null) {
                height += 4;
                button.multiline = true;
                button.setSize(width - bg.marginHor(), 16);
                button.setSize(width - bg.marginHor(), Math.max(button.reqHeight(), 16));
                button.setPos(x + (width - button.width()) / 2, y + height);
                height = button.bottom() - y;
            }

            height += bg.marginBottom() + 1;

            bg.size(width, height);
        }
    }

    private static class TexturePackItem extends StyledButton {
        private final FileHandle file;
        private final FileHandle assetHandle;
        private final boolean isAsset;
        private Image cachedIcon;

        public TexturePackItem(FileHandle file, String activePath, boolean isAsset) {
            super(Chrome.Type.GREY_BUTTON_TR, getPackDisplayName(file, activePath, isAsset), 6);
            this.file = file;
            this.assetHandle = null;
            this.isAsset = isAsset;
            cachedIcon = getPackIcon();
            icon(cachedIcon);
        }

        private static String getPackDisplayName(FileHandle file, String activePath, boolean isAsset) {
            String name = getPackName(file);

            boolean isActive = false;
            if (activePath != null && !activePath.isEmpty()) {
                String currentPath;
                if (!isAsset) {
                    currentPath = file.path();
                } else {
                    currentPath = ASSET_PACKS_DIR + "/" + file.name();
                }

                if (activePath.equals(currentPath)) {
                    isActive = true;
                }
            }

            return isActive ? (name + " " + Messages.get(TexturePackScene.class, "enabled")) : name;
        }

        private static String getPackName(FileHandle file) {
            try (ZipInputStream zis = new ZipInputStream(file.read())) {
                ZipEntry entry;
                while ((entry = zis.getNextEntry()) != null) {
                    if (entry.getName().equals("manifest.json")) {
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        byte[] buffer = new byte[1024];
                        int len;
                        while ((len = zis.read(buffer)) > 0) {
                            baos.write(buffer, 0, len);
                        }
                        String manifestJson = baos.toString("UTF-8");
                        JsonReader jsonReader = new JsonReader();
                        JsonValue manifest = jsonReader.parse(manifestJson);
                        if (manifest.has("name")) {
                            return manifest.getString("name");
                        }
                        break;
                    }
                }
            } catch (Exception e) {
                System.out.println("Failed to read manifest for name: " + file.name());
            }
            return file.name();
        }

        private Image getPackIcon() {
            try {
                ZipInputStream zis = new ZipInputStream(getSource().read());
                ZipEntry entry;
                while ((entry = zis.getNextEntry()) != null) {
                    if (entry.getName().equals("icons.png")) {
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        byte[] buffer = new byte[1024];
                        int len;
                        while ((len = zis.read(buffer)) > 0) {
                            baos.write(buffer, 0, len);
                        }
                        Pixmap pixmap = new Pixmap(baos.toByteArray(), 0, baos.size());
                        return new Image(new SmartTexture(pixmap));
                    }
                }
            } catch (Exception ignored) {
            }
            return new ItemSprite(ItemSpriteSheet.CAVES_PAGE);
        }

        private FileHandle getSource() {
            return file != null ? file : assetHandle;
        }

        @Override
        protected void onClick() {
            try {
                ZipInputStream zis = new ZipInputStream(getSource().read());
                ZipEntry manifestEntry = null;
                ZipEntry entry;

                while ((entry = zis.getNextEntry()) != null) {
                    if (entry.getName().equals("manifest.json")) {
                        manifestEntry = entry;
                        break;
                    }
                }

                if (manifestEntry == null) {
                    applyTexturePack();
                    return;
                }

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int len;
                while ((len = zis.read(buffer)) > 0) {
                    baos.write(buffer, 0, len);
                }

                String manifestJson = baos.toString("UTF-8");
                JsonReader jsonReader = new JsonReader();
                JsonValue manifest = jsonReader.parse(manifestJson);

                String name = manifest.has("name") ? manifest.getString("name") : getSource().name();
                String version = manifest.has("version") ? manifest.getString("version") : Messages.get(TexturePackScene.class, "unknown");
                String author = manifest.has("author") ? manifest.getString("author") : Messages.get(TexturePackScene.class, "unknown");
                String description = manifest.has("description") ? manifest.getString("description") : Messages.get(TexturePackScene.class, "no_description");

                List<String> replacedTextures = new ArrayList<>();

                try (ZipInputStream zis2 = new ZipInputStream(getSource().read())) {
                    while ((entry = zis2.getNextEntry()) != null) {
                        if (!entry.isDirectory() && entry.getName().endsWith(".png")) {
                            String texturePath = entry.getName();
                            for (Map.Entry<String, String> assetEntry : assetPaths.entrySet()) {
                                if (assetEntry.getValue().equals(texturePath)) {
                                    String displayName = texturePath.replace('_', '-');
                                    replacedTextures.add(displayName);
                                    break;
                                }
                            }
                        }
                    }
                }

                String infoText = Messages.get(TexturePackScene.class, "version") + ": " + version + "\n" +
                        Messages.get(TexturePackScene.class, "author") + ": " + author + "\n\n" +
                        description;

                Image iconToShow = getPackIcon();
                if(!isAsset){
                    ShatteredPixelDungeon.scene().add(new WndEXPackInfo(iconToShow, getSource(), name, infoText));
                } else {
                    ShatteredPixelDungeon.scene().add(new WndPackInfo(iconToShow, getSource(), name, infoText));
                }

            } catch (Exception e) {
                ShatteredPixelDungeon.reportException(e);
                applyTexturePack();
            }
        }

        private void applyTexturePack() {
            replaceAssets(getSource());

            String pathToSave = "";
            if (!isAsset) {
                pathToSave = getSource().path();
            } else {
                pathToSave = ASSET_PACKS_DIR + "/" + getSource().name();
            }
            SPDSettings.customTexturePack(pathToSave);

            ShatteredPixelDungeon.seamlessResetScene();
        }
    }

    private static class WndPackInfo extends WndOptions {

        private final FileHandle source;

        public WndPackInfo(Image icon, FileHandle source, String title, String message) {
            super(icon, title, message, Messages.get(TexturePackScene.class, "apply"), Messages.get(TexturePackScene.class, "cancel"));
            this.source = source;
        }

        @Override
        protected void onSelect(int index) {
            if (index == 0) {
                ShatteredPixelDungeon.scene().add(new WndMessage(Messages.get(TexturePackScene.class, "applying_pack")));

                Gdx.app.postRunnable(() -> {
                    TexturePackScene.replaceAssets(source);

                    String pathToSave = "";
                    if (source.path().contains(ASSET_PACKS_DIR)) {
                        pathToSave = ASSET_PACKS_DIR + "/" + source.name();
                    } else {
                        pathToSave = source.path();
                    }
                    SPDSettings.customTexturePack(pathToSave);

                    hide();
                    ShatteredPixelDungeon.seamlessResetScene();
                });
            } else if (index == 1) {
                SPDSettings.customTexturePack("");
                TextureCache.clear();
                TexturePackScene.loadCustomTexture();
                hide();
                ShatteredPixelDungeon.seamlessResetScene();
            }
        }
    }

    private static class WndEXPackInfo extends WndOptions {

        private final FileHandle source;

        public WndEXPackInfo(Image icon, FileHandle source, String title, String message) {
            super(icon, title, message,
                    Messages.get(TexturePackScene.class, "apply"),
                    Messages.get(TexturePackScene.class, "delete"));
            this.source = source;
        }

        @Override
        protected void onSelect(int index) {
            if (index == 0) {
                ShatteredPixelDungeon.scene().add(new WndMessage(Messages.get(TexturePackScene.class, "applying_pack")));

                Gdx.app.postRunnable(() -> {
                    TexturePackScene.replaceAssets(source);

                    String pathToSave = "";
                    if (source.path().contains(ASSET_PACKS_DIR)) {
                        pathToSave = ASSET_PACKS_DIR + "/" + source.name();
                    } else {
                        pathToSave = source.path();
                    }
                    SPDSettings.customTexturePack(pathToSave);

                    hide();
                    ShatteredPixelDungeon.seamlessResetScene();
                });
            } else if (index == 1) {
                ShatteredPixelDungeon.scene().add(new WndOptions(
                        Messages.get(TexturePackScene.class, "delete_title"),
                        Messages.get(TexturePackScene.class, "delete_confirm"),
                        Messages.get(TexturePackScene.class, "yes"),
                        Messages.get(TexturePackScene.class, "no")) {
                    @Override
                    protected void onSelect(int index) {
                        if (index == 0) {

                            try {

                                String activePath = SPDSettings.customTexturePack();
                                boolean isActivePack = activePath != null && activePath.equals(source.path());

                                source.delete();

                                if (isActivePack) {
                                    SPDSettings.customTexturePack("");
                                    TextureCache.clear();
                                    TexturePackScene.loadCustomTexture();
                                }

                                WndEXPackInfo.this.hide();
                                hide();

                                ShatteredPixelDungeon.seamlessResetScene();
                            } catch (Exception e) {
                                ShatteredPixelDungeon.reportException(e);

                                ShatteredPixelDungeon.scene().add(new WndError(Messages.get(TexturePackScene.class, "delete_failed")));
                            }
                        }
                    }
                });
            }
        }
    }

    public static void cleanOldTempFiles() {
        int cleanedCount = 0;

        try {
            FileHandle localDir = Gdx.files.local("");
            cleanedCount += cleanTempFilesInDir(localDir);
            FileHandle externalDir = Gdx.files.external("");
            cleanedCount += cleanTempFilesInDir(externalDir);
            FileHandle texturePacksDir = getTexturePacksDir();
            if (texturePacksDir.exists()) {
                cleanedCount += cleanTempFilesInDir(texturePacksDir);
            }

        } catch (Exception e) {
            Gdx.app.error("TexturePackScene", "Error while cleaning temp files", e);
        }

    }

    private static int cleanTempFilesInDir(FileHandle dir) {
        int cleanedCount = 0;

        try {
            FileHandle[] tempFiles = dir.list((dir1, name) ->
                    name.startsWith("temp_")
            );

            if (tempFiles != null) {
                for (FileHandle file : tempFiles) {
                    try {
                        if (file.exists()) {
                            file.delete();
                            cleanedCount++;
                            Gdx.app.debug("TexturePackScene", "Deleted temp file: " + file.path());
                        }
                    } catch (Exception e) {
                        Gdx.app.error("TexturePackScene", "Failed to delete temp file: " + file.path(), e);
                    }
                }
            }
        } catch (Exception e) {
            Gdx.app.error("TexturePackScene", "Error while cleaning temp files in directory: " + dir.path(), e);
        }
        return cleanedCount;
    }

}