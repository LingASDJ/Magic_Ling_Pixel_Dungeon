package com.shatteredpixel.shatteredpixeldungeon.windows;

import static com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene.align;
import static com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene.defaultZoom;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Chrome;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.HeroSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.IconButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.ScrollingGridPane;
import com.shatteredpixel.shatteredpixeldungeon.ui.StyledButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.gltextures.SmartTexture;
import com.watabou.gltextures.TextureCache;
import com.watabou.glwrap.Quad;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.Group;
import com.watabou.noosa.Image;
import com.watabou.noosa.NoosaScript;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.Visual;
import com.watabou.noosa.ui.Component;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Calendar;

public class WndSelectSkin extends Window {

    private static final int WIDTH = 170;
    private static final int HEIGHT = 140;

    private static final int LIST_WIDTH = 44;
    private static final int PREVIEW_WIDTH = 80;
    private static final int INFO_WIDTH = WIDTH - LIST_WIDTH - PREVIEW_WIDTH - 12;

    private static final int PREVIEW_HEIGHT = 120;
    private static final int NAME_HEIGHT = 22;

    private static final int AVATAR_FRAME_W = 64;
    private static final int AVATAR_FRAME_H = 64;

    private HeroClass heroClass;

    private ScrollingGridPane skinList;
    private SkinPreview preview;
    private SkinInfo info;
    public SkinNameBar nameBar;
    private Image frame;
    private int selectedSkin;

    private static final class SkinConfig {
        public final HeroClass heroClass;
        public final int skinId;
        public final String texPath;
        public final int frameW;
        public final int frameH;

        public SkinConfig(HeroClass heroClass, int skinId, String texPath, int frameW, int frameH) {
            this.heroClass = heroClass;
            this.skinId = skinId;
            this.texPath = texPath;
            this.frameW = frameW;
            this.frameH = frameH;
        }
    }

    private static final SkinConfig[] SPECIAL_SKINS = {
            new SkinConfig(HeroClass.WARRIOR,  4, "splashes/skin/giftskin_warrior.png", 80, 112),
            new SkinConfig(HeroClass.ROGUE,    4, "splashes/skin/giftskin_rogue.png", 80, 112),
            new SkinConfig(HeroClass.MAGE,     4, "splashes/skin/mage_collagedays.png", 80, 112),
            new SkinConfig(HeroClass.DUELIST,  4, "splashes/skin/duelist_kitsunemimi.png", 80, 112),
            new SkinConfig(HeroClass.DUELIST,  5, "splashes/skin/duelist_desertspirit.png", 80, 112),
    };

    private static final int FRAME_WIDTH    = 89;
    private static final int FRAME_HEIGHT    = 128;
    private static final int BUTTON_HEIGHT    = 20;
    private static final int SKY_WIDTH    = 80;
    private static final int SKY_HEIGHT    = 112;
    private static final int NSTARS		= 100;
    private static final int NCLOUDS	= 5;
    private Camera viewport;
    public WndSelectSkin(HeroClass heroClass) {

        super(Game.width > Game.height ? WIDTH : 135, HEIGHT, Chrome.get(Chrome.Type.GREY_BUTTON_TR));

        boolean dayTime =
                Calendar.getInstance().get(Calendar.HOUR_OF_DAY) < 18 && Calendar.getInstance().get(Calendar.HOUR_OF_DAY) > 7;

        int w = Camera.main.width;
        int h = Camera.main.height;
        float vx = align((w - SKY_WIDTH) / 2f);
        float vy = align((h - SKY_HEIGHT - BUTTON_HEIGHT) / 2f);

        Point s = Camera.main.cameraToScreen( vx, vy );
        viewport = new Camera( s.x, s.y, SKY_WIDTH, SKY_HEIGHT, defaultZoom );
        Camera.add( viewport );

        Group window = new Group();
        window.camera = viewport;
        add( window );


        frame = new Image( Assets.Interfaces.NEW_MENU );

        this.heroClass = heroClass;
        this.selectedSkin = heroClass.GetSkin();

        skinList = new ScrollingGridPane();
        skinList.setCellSize(32, 36);

        add(skinList);
        skinList.setRect(0, 0, LIST_WIDTH, PREVIEW_HEIGHT);

        preview = new SkinPreview();
        preview.setRect(LIST_WIDTH + 3, 0, PREVIEW_WIDTH, PREVIEW_HEIGHT);
        add(preview);

        frame.frame( 0, 0, TextureRules(), FRAME_HEIGHT );
        frame.x = preview.x;
        frame.y = preview.y;
        addToFront( frame );

        info = new SkinInfo();
        info.setRect(Game.width > Game.height ? LIST_WIDTH + PREVIEW_WIDTH + 8: LIST_WIDTH + PREVIEW_WIDTH , 0, INFO_WIDTH, PREVIEW_HEIGHT);
        add(info);

        nameBar = new SkinNameBar();
        nameBar.setRect(0, PREVIEW_HEIGHT + 3, WIDTH, NAME_HEIGHT);
        add(nameBar);

        refreshSkinList();
        refreshDisplay();

        skinList.scrollToCell(selectedSkin);
    }

    private int TextureRules() {
        if(SPDSettings.customTexturePack() == null){
            return 89;
        }
        switch (SPDSettings.customTexturePack()){
            case "texture_load/mlpd.mlpack":
                return 89;
            default:
                return 88;
        }
    }

    private void refreshSkinList() {
        skinList.clear();

        Image skinSheet = new Image(heroClass.GetSkinAssest());
        int skinCount = skinSheet.texture.width / AVATAR_FRAME_W;

        for (int i = 0; i < skinCount; i++) {
            final int skinIndex = i;
            boolean unlocked = isSkinUnlocked(skinIndex);

            SkinListItem item = new SkinListItem(heroClass, skinIndex, unlocked, skinIndex == selectedSkin);
            item.setSkinClickListener(() -> {
                if (unlocked) {
                    selectedSkin = skinIndex;
                    refreshSkinList();
                    refreshDisplay();
                }
            });
            skinList.addItem(item);
        }

        skinList.layout();
    }

    private boolean isSkinUnlocked(int skinIndex) {
        if (skinIndex == 0) return true;

        String key;
        switch (heroClass) {
            default:
            case WARRIOR:   key = "avatars_warrior_" + skinIndex; break;
            case MAGE:      key = "avatars_mage_" + skinIndex; break;
            case ROGUE:     key = "avatars_rogue_" + skinIndex; break;
            case HUNTRESS:  key = "avatars_huntress_" + skinIndex; break;
            case DUELIST:   key = "avatars_duelist_" + skinIndex; break;
            case SPELLSWORD:key = "avatars_spellsword_" + skinIndex; break;
        }
        return SPDSettings.isItemUnlock(key);
    }

    private void refreshDisplay() {
        preview.setSkin(heroClass, selectedSkin);
        info.setSkin(heroClass, selectedSkin);
        nameBar.setSkin(heroClass, selectedSkin);
    }

    @Override
    public void hide() {
        SPDSettings.setHeroSkin(heroClass.ordinal(), selectedSkin);
        Char target = Dungeon.hero;
        ((HeroSprite)target.sprite).disguise(heroClass);
        GameScene.updateAvatar();
        super.hide();
    }

    private static SkinConfig getSpecialSkinConfig(HeroClass cl, int skinId) {
        for (SkinConfig cfg : SPECIAL_SKINS) {
            if (cfg.heroClass == cl && cfg.skinId == skinId) {
                return cfg;
            }
        }
        return null;
    }

    private static int skinIndexToTier(int skinIndex) {
        switch (skinIndex) {
            case 1:  return 9;
            case 2:  return 11;
            case 3:  return 10;
            case 4:  return 13;
            case 5:  return 14;
            default: return 7;
        }
    }

    private static class SkinListItem extends ScrollingGridPane.GridItem {

        private Runnable onClick;

        public SkinListItem(HeroClass heroClass, int skinIndex, boolean unlocked, boolean selected) {
            super(createThumbnail(heroClass, skinIndex, unlocked));

            if (selected) {
                hardLightBG(1f, 0.85f, 0.2f);
            } else if (!unlocked) {
                hardLightBG(0.35f, 0.35f, 0.35f);
            }
        }

        private static Image createThumbnail(HeroClass heroClass, int skinIndex, boolean unlocked) {
            int tier = skinIndexToTier(skinIndex);
            Image avatar = HeroSprite.avatar(heroClass, tier);

            if (!unlocked) {
                avatar.hardlight(0.4f, 0.4f, 0.4f);
            }

            return avatar;
        }

        public void setSkinClickListener(Runnable listener) {
            this.onClick = listener;
        }

        @Override
        public boolean onClick(float x, float y) {
            if (x < this.x || x >= this.x + width() || y < this.y || y >= this.y + height()) {
                return false;
            }
            if (onClick != null) {
                onClick.run();
                return true;
            }
            return false;
        }
    }

    private static class SkinPreview extends Component {

        private Image skinImage;
        private boolean isSpecialSkin;

        private Sky sky;
        private ArrayList<Cloud> clouds = new ArrayList<>();
        private StyledButton descButton;

        public SkinPreview() {
            super();
        }

        private String getClassPrefix(HeroClass heroClass) {
            switch (heroClass) {
                case WARRIOR:   return "w";
                case MAGE:      return "m";
                case ROGUE:     return "r";
                case HUNTRESS:  return "h";
                case DUELIST:   return "d";
                default:        return "";
            }
        }

        private String getSkinItemKey(HeroClass heroClass, int skinIndex) {
            String prefix = getClassPrefix(heroClass);
            if (prefix.isEmpty()) return "";

            switch (skinIndex) {
                case 1:  return "skin_" + prefix + "a";
                case 2:  return "skin_" + prefix + "b";
                case 3:  return "skin_" + prefix + "d";
                case 4:  return "skin_" + prefix + "c";
                case 5:  return heroClass == HeroClass.DUELIST ? "skin_" + prefix + "d" : "skin_" + prefix + "e";
                default: return "";
            }
        }

        public void setSkin(HeroClass heroClass, int skinIndex) {
            if (skinImage != null) {
                skinImage.kill();
                remove(skinImage);
                skinImage = null;
            }

            // 清除旧背景
            if (sky != null) {
                sky.kill();
                remove(sky);
                sky = null;
            }

            // 清除旧云
            for (Cloud cloud : clouds) {
                cloud.kill();
                remove(cloud);
            }
            clouds.clear();

            // 创建天空背景
            boolean dayTime = Calendar.getInstance().get(Calendar.HOUR_OF_DAY) < 18
                    && Calendar.getInstance().get(Calendar.HOUR_OF_DAY) > 7;

            sky = new Sky(Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
            sky.scale.set(SKY_WIDTH, SKY_HEIGHT);
            add(sky);

            float range = SKY_HEIGHT * 2 / 3;
            for (int i = 0; i < NCLOUDS; i++) {
                Cloud cloud = new Cloud(
                        (NCLOUDS - 1 - i) * (range / NCLOUDS) + Random.Float(range / NCLOUDS),
                        dayTime
                );
                add(cloud);
                clouds.add(cloud);
            }

            // 原有立绘逻辑
            SkinConfig special = getSpecialSkinConfig(heroClass, skinIndex);
            isSpecialSkin = (special != null);

            if (special != null) {
                skinImage = new Image(TextureCache.get(special.texPath));
                skinImage.frame(0, 0, special.frameW, special.frameH);
            } else {
                skinImage = new Image(heroClass.GetSkinAssest());
                TextureFilm film = new TextureFilm(skinImage.texture, AVATAR_FRAME_W, AVATAR_FRAME_H);
                skinImage.frame(film.get(skinIndex));
            }

            String itemKey = getSkinItemKey(heroClass, skinIndex);
            String desc = Messages.get("items.quest.skinitem$" + itemKey + ".desc");
            descButton = new StyledButton(Chrome.Type.BLANK,""){
                @Override
                protected void onClick() {
                    if(skinIndex != 0){
                        super.onClick();
                        Game.runOnRenderThread(() -> ShatteredPixelDungeon.scene().add(new WndMessage(desc)));
                    }
                }
            };
            add(descButton);

            float scale = Math.min(
                    width() / skinImage.width(),
                    height() / skinImage.height()
            );
            skinImage.scale.set(scale);
            add(skinImage);

            layout();
        }

        @Override
        protected void layout() {
            super.layout();

            // 背景缩放适配
            if (sky != null) {
                float bgScale = Math.min(width() / SKY_WIDTH, height() / SKY_HEIGHT);
                sky.scale.set(SKY_WIDTH * bgScale, SKY_HEIGHT * bgScale);
                sky.x = x + (width() - SKY_WIDTH * bgScale) / 2f + 5;
                sky.y = y + (height() - SKY_HEIGHT * bgScale) / 2f + 5;
            }

            if (skinImage != null) {
                float scaledW = skinImage.width() * skinImage.scale.x;
                float scaledH = skinImage.height() * skinImage.scale.y;

                if (isSpecialSkin) {
                    skinImage.x = x + (width() - skinImage.width() * skinImage.scale.x) / 2f + 5;
                    skinImage.y = (height() - scaledH) / 2f + 5;
                } else {
                    skinImage.x = x + (width() - skinImage.width() * skinImage.scale.x) / 2f + 15;
                    skinImage.y = height() - scaledH + 20;
                }
                align(skinImage);

                descButton.setRect(skinImage.x,skinImage.y,skinImage.width(),skinImage.height());
                align(descButton);
            }
        }

        private static class Sky extends Visual {
            private static final int[][] gradients = new int[][] {
                    { 0xff012459, 0xff001322 },
                    { 0xff003972, 0xff001322 },
                    { 0xff003972, 0xff001322 },
                    { 0xff004372, 0xff00182b },
                    { 0xff004372, 0xff011d34 },
                    { 0xff016792, 0xff00182b },
                    { 0xff07729f, 0xff042c47 },
                    { 0xff12a1c0, 0xff07506e },
                    { 0xff74d4cc, 0xff1386a6 },
                    { 0xffefeebc, 0xff61d0cf },
                    { 0xfffee154, 0xffa3dec6 },
                    { 0xfffdc352, 0xffe8ed92 },
                    { 0xffffac6f, 0xffffe467 },
                    { 0xfffda65a, 0xffffe467 },
                    { 0xfffd9e58, 0xffffe467 },
                    { 0xfff18448, 0xffffd364 },
                    { 0xfff06b7e, 0xfff9a856 },
                    { 0xffca5a92, 0xfff4896b },
                    { 0xff5b2c83, 0xffd1628b },
                    { 0xff371a79, 0xff713684 },
                    { 0xff28166b, 0xff45217c },
                    { 0xff192861, 0xff372074 },
                    { 0xff040b3c, 0xff233072 },
                    { 0xff040b3c, 0xff012459 },
            };

            private SmartTexture texture;
            private FloatBuffer verticesBuffer;

            public Sky(int hour) {
                super(0, 0, 1, 1);
                texture = TextureCache.createGradient(gradients[hour]);

                float[] vertices = new float[16];
                verticesBuffer = Quad.create();

                vertices[2]     = 0.25f;
                vertices[6]     = 0.25f;
                vertices[10]    = 0.75f;
                vertices[14]    = 0.75f;

                vertices[3]     = 0;
                vertices[7]     = 1;
                vertices[11]    = 1;
                vertices[15]    = 0;

                vertices[0]     = 0;
                vertices[1]     = 0;

                vertices[4]     = 1;
                vertices[5]     = 0;

                vertices[8]     = 1;
                vertices[9]     = 1;

                vertices[12]    = 0;
                vertices[13]    = 1;

                ((Buffer)verticesBuffer).position(0);
                verticesBuffer.put(vertices);
            }

            @Override
            public void draw() {
                super.draw();
                NoosaScript script = NoosaScript.get();
                texture.bind();
                script.camera(camera());
                script.uModel.valueM4(matrix);
                script.lighting(rm, gm, bm, am, ra, ga, ba, aa);
                script.drawQuad(verticesBuffer);
            }
        }
    }

    private static class Cloud extends Image {

        private static int lastIndex = -1;
        private float alpha = 0f;

        // 预览框宽度
        private static final float VIEW_WIDTH = SKY_WIDTH;  // 80
        // 不透明区域占中间的 1/3，两侧各 1/3 用于淡入淡出
        private static final float OPAQUE_START = VIEW_WIDTH / 3f;   // ~26.7
        private static final float OPAQUE_END = 170 / 2f; // ~53.3
        // 淡入淡出宽度
        private static final float FADE_WIDTH = VIEW_WIDTH / 3f;      // ~26.7
        // 活动范围
        private static final float MIN_X = -FADE_WIDTH;               // -26.7
        private static final float MAX_X = VIEW_WIDTH + FADE_WIDTH;   // 106.7

        public Cloud(float y, boolean dayTime) {
            super(Assets.Interfaces.SURFACE);

            int index;
            do {
                index = Random.Int(3);
            } while (index == lastIndex);

            switch (index) {
                case 0: frame(88, 0, 49, 20); break;
                case 1: frame(88, 20, 49, 22); break;
                case 2: frame(88, 42, 50, 18); break;
            }
            lastIndex = index;

            this.y = y + 5;
            scale.set(1 - y / SKY_HEIGHT);
            speed.x = scale.x * (dayTime ? +4 : -4);

            // 初始位置
            if (dayTime) {
                x = MIN_X;
            } else {
                x = MAX_X;
            }

            if (dayTime) {
                tint(0xCCEEFF, 1 - scale.y);
            } else {
                rm = gm = bm = +3.0f;
                ra = ga = ba = -2.1f;
            }
        }

        @Override
        public void update() {
            super.update();
            x += speed.x * Game.elapsed;

            // ===== 两侧各 1/3 用于淡入淡出，中间 1/3 完全不透明 =====
            //
            // 向右移动：
            //   淡入 [-26.7, 0]     → alpha 0→1
            //   不透明 [0, 53.3]     → alpha 1
            //   淡出 [53.3, 80]      → alpha 1→0
            //
            // 向左移动：
            //   淡入 [80, 106.7]     → alpha 0→1
            //   不透明 [26.7, 80]    → alpha 1
            //   淡出 [0, 26.7]       → alpha 1→0

            if (speed.x > 0) {
                // 向右移动
                if (x < 0) {
                    // 左侧淡入
                    alpha = (x - MIN_X) / FADE_WIDTH;
                } else if (x > OPAQUE_END) {
                    // 右侧淡出：x=53.3→80, alpha=1→0
                    alpha = (VIEW_WIDTH - x) / FADE_WIDTH;
                } else {
                    alpha = 1f;
                }
            } else {
                // 向左移动
                if (x > VIEW_WIDTH) {
                    // 右侧淡入
                    alpha = (MAX_X - x) / FADE_WIDTH;
                } else if (x < OPAQUE_START) {
                    // 左侧淡出：x=26.7→0, alpha=1→0
                    alpha = x / OPAQUE_START;  // x=26.7→0, alpha=1→0
                    // 等价于：alpha = x / FADE_WIDTH;
                } else {
                    alpha = 1f;
                }
            }

            alpha = Math.max(0f, Math.min(1f, alpha));

            // 边界重生
            if (speed.x > 0 && x > VIEW_WIDTH) {
                x = MIN_X;
            } else if (speed.x < 0 && x < 0) {
                x = MAX_X;
            }
        }

        @Override
        public void draw() {
            alpha(alpha);
            super.draw();
        }
    }

    private static class SkinInfo extends Component {

        private RenderedTextBlock titleText;

        public SkinInfo() {
            super();

            if(Game.width > Game.height ){
                titleText = PixelScene.renderTextBlock("", 16);
                titleText.setTextDirection(RenderedTextBlock.VERTICAL);
                titleText.maxHeight(100);
                titleText.setVerticalLetterSpacing(16f);
                titleText.setVerticalColumnSpacing(6f);
            } else {
                titleText = PixelScene.renderTextBlock("", 9);
                titleText.setWidth(titleText.width());
            }
            add(titleText);
        }

        public void setSkin(HeroClass heroClass, int skinIndex) {
            // skinIndex 0 是默认皮肤
            if (skinIndex == 0) {
                String defaultName = Messages.get(HeroClass.class, heroClass.name().toLowerCase());
                titleText.text(defaultName);
                layout();
                return;
            }

            String itemKey = getSkinItemKey(heroClass, skinIndex);
            if (itemKey.isEmpty()) {
                titleText.text(Messages.get(WndSelectSkin.class, "default_skin_name", skinIndex));
                layout();
                return;
            }

            // ===== 修复：直接传完整 key，使用 $ 分隔内部类 =====
            String name = Messages.get("items.quest.skinitem$" + itemKey + ".name");
            String desc = Messages.get("items.quest.skinitem$" + itemKey + ".desc");

            // 检查是否未找到（Messages 找不到时返回 "Ms:xxx"）
            if (name.startsWith("Ms:")) {
                name = Messages.get(WndSelectSkin.class, "default_skin_name", skinIndex);
            }
            if (desc.startsWith("Ms:")) {
                desc = Messages.get(WndSelectSkin.class, "default_skin_desc", skinIndex);
            }

            titleText.text(name);
            layout();
        }

        /**
         * 根据职业和皮肤索引获取 SKINITEM 的 messages key（小写）
         */
        private String getSkinItemKey(HeroClass heroClass, int skinIndex) {
            String prefix = getClassPrefix(heroClass);
            if (prefix.isEmpty()) return "";

            switch (skinIndex) {
                case 1:  return "skin_" + prefix + "a";
                case 2:  return "skin_" + prefix + "b";
                case 3:  return "skin_" + prefix + "d";
                case 4:  return "skin_" + prefix + "c";
                case 5:  return heroClass == HeroClass.DUELIST ? "skin_" + prefix + "d" : "skin_" + prefix + "e";
                default: return "";
            }
        }

        private String getClassPrefix(HeroClass heroClass) {
            switch (heroClass) {
                case WARRIOR:   return "w";
                case MAGE:      return "m";
                case ROGUE:     return "r";
                case HUNTRESS:  return "h";
                case DUELIST:   return "d";
                default:        return "";
            }
        }

        @Override
        protected void layout() {
            super.layout();
            titleText.setPos(Game.width > Game.height ? x+16 : x-123, Game.width > Game.height ? y+12 : y+130);
        }
    }

    private static class SkinNameBar extends Component {

        private RenderedTextBlock nameText;
        private IconButton infoBtn;

        private HeroClass heroClass;
        private int skinIndex;

        public SkinNameBar() {
            super();
            nameText = PixelScene.renderTextBlock("", 8);
            add(nameText);

            // 信息按钮，使用原版info图标
            infoBtn = new IconButton(Icons.INFO.get()){
                @Override
                protected void onClick() {
                    super.onClick();
                    showSkinInfo();
                }
            };
            infoBtn.setSize(16,16);
            add(infoBtn);
        }

        public void setSkin(HeroClass heroClass, int skinIndex) {
            this.heroClass = heroClass;
            this.skinIndex = skinIndex;

            String nameKey = heroClass.name().toLowerCase() + "_skin_" + skinIndex;
            String name = Messages.get(WndSelectSkin.class, nameKey);
            if(skinIndex <= 3){
                name = Messages.get(WndSelectSkin.class, "default_skin_name");
            }
            nameText.text("Design:"+name);
            layout();
        }

        // 弹出皮肤信息弹窗
        private void showSkinInfo(){
            String infoKey = heroClass.name().toLowerCase() + "_skin_info_" + skinIndex;
            String infoText = Messages.get(WndSelectSkin.class, infoKey);
            if (infoText.startsWith("Ms:")) {
                infoText = Messages.get(WndSelectSkin.class, "default_skin_info");
            } else {
                infoText = Messages.get(WndSelectSkin.class, infoKey);
            }

            Game.scene().addToFront(new WndMessage(infoText));
        }

        @Override
        protected void layout() {
            super.layout();

            nameText.maxWidth ((int) width ());
            nameText.align (RenderedTextBlock.CENTER_ALIGN);
            nameText.setPos (x + (width () - nameText.width ()) / 2f-4, y + (height () - nameText.height ()) / 2f);
            align (nameText);

            infoBtn.setPos(nameText.right()+1,nameText.top()-6);
        }
    }
}