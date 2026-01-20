package com.shatteredpixel.shatteredpixeldungeon.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.Archs;
import com.shatteredpixel.shatteredpixeldungeon.ui.Button;
import com.shatteredpixel.shatteredpixeldungeon.ui.IconButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.ScrollPane;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndError;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndMessage;
import com.watabou.noosa.Camera;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.ui.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class TexturePackScene extends PixelScene {

    private static final String TEXTURE_PACKS_DIR = "texture_packs";
    private static final String[] ALLOWED_EXTENSIONS = {".zip"};

    private static final int GAP = 2;
    private static final int ITEM_HEIGHT = 20;

    private ArrayList<TexturePackItem> items;
    private ScrollPane list;
    private Component content;

    @Override
    public void create() {
        super.create();

        uiCamera.visible = false;

        // 创建背景
        Image background = new Image(Assets.Interfaces.BANNERS);
        background.scale.set(Camera.main.width/background.width, Camera.main.height/background.height);
        add(background);

        Archs archs = new Archs();
        archs.setSize(Camera.main.width, Camera.main.height);
        add(archs);

        // 标题
        RenderedTextBlock title = PixelScene.renderTextBlock(Messages.get(this, "title"), 9);
        title.hardlight(Window.TITLE_COLOR);
        title.setPos(
                (Camera.main.width - title.width()) / 2f,
                (Camera.main.height - title.height()) / 2f - 80
        );
        add(title);

        // 创建内容区域
        content = new Component();
        items = new ArrayList<>();

        // 创建返回按钮
        Button btnExit = new IconButton(new ItemSprite(ItemSpriteSheet.MAGNETIC_CROWN)) {
            @Override
            protected void onClick() {
                Game.switchScene(TitleScene.class);
            }
        };
        btnExit.setRect(Camera.main.width - 20, 5, 15, 15);
        add(btnExit);

        // 创建导入按钮
        Button btnImport = new Button() {
            @Override
            protected void onClick() {
                importTexturePack();
            }
        };
        btnImport.setRect(5, Camera.main.height - 25, 60, 20);
        add(btnImport);

        // 初始化材质包列表
        refreshList();

        // 创建滚动面板
        list = new ScrollPane(content) {
            @Override
            public void onClick(float x, float y) {
                int item = (int) (y / ITEM_HEIGHT);
                if (item >= 0 && item < items.size()) {
                    items.get(item).onClick();
                }
            }
        };
        add(list);

        fadeIn();
    }

    private void refreshList() {
        content.clear();
        items.clear();

        float pos = 0;
        File dir = new File(Gdx.files.getLocalStoragePath() + TEXTURE_PACKS_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File[] files = dir.listFiles((d, name) -> {
            for (String ext : ALLOWED_EXTENSIONS) {
                if (name.toLowerCase().endsWith(ext)) {
                    return true;
                }
            }
            return false;
        });

        if (files != null) {
            for (File file : files) {
                TexturePackItem item = new TexturePackItem(file);
                item.setRect(0, pos, Camera.main.width - 10, ITEM_HEIGHT);
                content.add(item);
                items.add(item);
                pos += ITEM_HEIGHT + GAP;
            }
        }

        content.setSize(Camera.main.width - 10, pos);
    }

    private void importTexturePack() {
        try {
            // 这里需要根据平台实现不同的文件选择器
            // Android可以使用Intent打开文件选择器
            // Desktop可以使用JFileChooser
            // 这里是一个简化的示例实现
            FileHandle source = Gdx.files.external("Downloads/texture_pack.zip");
            if (!source.exists()) {
                add(new WndError(Messages.get(this, "no_file_selected")));
                return;
            }

            // 验证zip文件
            if (!isValidTexturePack(source.file())) {
                add(new WndError(Messages.get(this, "invalid_pack")));
                return;
            }

            // 复制文件到材质包目录
            FileHandle dest = Gdx.files.local(TEXTURE_PACKS_DIR + "/" + source.name());
            source.copyTo(dest);

            refreshList();
            add(new WndMessage(Messages.get(this, "import_success")));

        } catch (Exception e) {
            add(new WndError(Messages.get(this, "import_failed") + "\n" + e.getMessage()));
        }
    }

    private boolean isValidTexturePack(File file) {
        try (ZipFile zipFile = new ZipFile(file)) {
            // 检查必要的文件是否存在
            boolean hasManifest = zipFile.getEntry("manifest.json") != null;
            boolean hasTextures = false;

            // 检查是否包含纹理文件
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

    private static class TexturePackItem extends Component {
        private final File file;
        private RenderedTextBlock name;
        private ColorBlock bg;

        public TexturePackItem(File file) {
            this.file = file;
        }

        @Override
        protected void createChildren() {
            bg = new ColorBlock(width, height, 0x44444444);
            add(bg);

            name = PixelScene.renderTextBlock(file.getName(), 6);
            add(name);
        }

        @Override
        protected void layout() {
            bg.x = x;
            bg.y = y;
            bg.size(width, height);

            name.x = x + 5;
            name.y = y + (height - name.maxWidth()) / 2f;
        }

        public void onClick() {
            //SPDSettings.customTexturePack(file.getName());
            Game.switchScene(TitleScene.class);
        }
    }
}
