package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.watabou.noosa.Image;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;

/**
 * 皮肤配置注册中心——所有皮肤相关信息的唯一来源。
 *
 * 每个皮肤可以配置：
 * <ul>
 *   <li><b>splashPath</b> + <b>splashFrameW/H</b>：皮肤选择界面的大立绘预览图</li>
 *   <li><b>spriteFactory</b>：游戏内精灵类（帧布局不同于默认 HeroSprite 时）</li>
 *   <li><b>avatarFactory</b>：小头像（StatusPane、WndHero 等）</li>
 * </ul>
 *
 * 新增皮肤只需在 static 块里 register 一行，无需修改 GameScene、WndSelectSkin、
 * SurfaceScene、HeroSelectScene 或 HeroSprite。
 */
public final class HeroSkins {

    private HeroSkins() {}

    /** 单个皮肤的全部配置。所有字段可为 null，表示使用默认行为。 */
    public static final class SkinDef {
        /** 大立绘资源路径，如 "splashes/skin/giftskin_warrior.png" */
        public final String splashPath;
        /** 大立绘帧宽，默认 80 */
        public final int splashFrameW;
        /** 大立绘帧高，默认 112 */
        public final int splashFrameH;
        /** 游戏内精灵工厂；null 表示用默认 HeroSprite */
        public final Supplier<HeroSprite> spriteFactory;
        /** 小头像工厂；null 表示用默认 tier-based 头像 */
        public final Supplier<Image> avatarFactory;

        // ---- 自定义 sprite 纹理与帧布局（供 MirrorSprite/PrismaticSprite 等克隆体使用） ----
        /** 游戏内精灵纹理路径；null 表示用默认职业 spritesheet */
        public final String texturePath;
        /** 自定义帧宽；<=0 表示用默认 12 */
        public final int frameW;
        /** 自定义帧高；<=0 表示用默认 15 */
        public final int frameH;
        /** idle 动画帧索引；null 表示用默认 */
        public final int[] idleFrames;
        /** run 动画帧索引；null 表示用默认 */
        public final int[] runFrames;
        /** die 动画帧索引；null 表示用默认 */
        public final int[] dieFrames;
        /** attack 动画帧索引；null 表示用默认 */
        public final int[] attackFrames;

        // ---- 自定义动画帧率（fps），供 MirrorSprite/PrismaticSprite 等克隆体复用 ----
        /** idle 帧率，默认 1 */
        public final int idleFps;
        /** run 帧率，默认 20 */
        public final int runFps;
        /** die 帧率，默认 8 */
        public final int dieFps;
        /** attack 帧率，默认 15 */
        public final int attackFps;

        public SkinDef(String splashPath, int splashFrameW, int splashFrameH,
                       Supplier<HeroSprite> spriteFactory,
                       Supplier<Image> avatarFactory,
                       String texturePath, int frameW, int frameH,
                       int[] idleFrames, int[] runFrames, int[] dieFrames, int[] attackFrames,
                       int idleFps, int runFps, int dieFps, int attackFps) {
            this.splashPath = splashPath;
            this.splashFrameW = splashFrameW;
            this.splashFrameH = splashFrameH;
            this.spriteFactory = spriteFactory;
            this.avatarFactory = avatarFactory;
            this.texturePath = texturePath;
            this.frameW = frameW;
            this.frameH = frameH;
            this.idleFrames = idleFrames;
            this.runFrames = runFrames;
            this.dieFrames = dieFrames;
            this.attackFrames = attackFrames;
            this.idleFps = idleFps;
            this.runFps = runFps;
            this.dieFps = dieFps;
            this.attackFps = attackFps;
        }

        /** 只注册大立绘的便捷构造器。 */
        public static SkinDef splash(String path, int w, int h) {
            return new SkinDef(path, w, h, null, null, null, 0, 0, null, null, null, null,
                    1, 20, 8, 15);
        }

        /** 注册大立绘 + 特殊 sprite + 头像的完整构造器。 */
        public static SkinDef full(String path, int w, int h,
                                   Supplier<HeroSprite> spriteFactory,
                                   Supplier<Image> avatarFactory) {
            return new SkinDef(path, w, h, spriteFactory, avatarFactory, null, 0, 0, null, null, null, null,
                    1, 20, 8, 15);
        }

        /** 注册带游戏内纹理与帧布局的完整 sprite 皮肤（fps 取默认值 1/20/8/15）。 */
        public static SkinDef full(String path, int w, int h,
                                   Supplier<HeroSprite> spriteFactory,
                                   Supplier<Image> avatarFactory,
                                   String texturePath, int frameW, int frameH,
                                   int[] idleFrames, int[] runFrames, int[] dieFrames, int[] attackFrames) {
            return new SkinDef(path, w, h, spriteFactory, avatarFactory,
                    texturePath, frameW, frameH, idleFrames, runFrames, dieFrames, attackFrames,
                    1, 20, 8, 15);
        }

        /** 注册带游戏内纹理、帧布局与自定义帧率的完整 sprite 皮肤。 */
        public static SkinDef full(String path, int w, int h,
                                   Supplier<HeroSprite> spriteFactory,
                                   Supplier<Image> avatarFactory,
                                   String texturePath, int frameW, int frameH,
                                   int[] idleFrames, int[] runFrames, int[] dieFrames, int[] attackFrames,
                                   int idleFps, int runFps, int dieFps, int attackFps) {
            return new SkinDef(path, w, h, spriteFactory, avatarFactory,
                    texturePath, frameW, frameH, idleFrames, runFrames, dieFrames, attackFrames,
                    idleFps, runFps, dieFps, attackFps);
        }
    }

    private static final HashMap<String, SkinDef> REGISTRY = new HashMap<>();

    private static String key(HeroClass cls, int skinIndex) {
        return cls.ordinal() + ":" + skinIndex;
    }

    /** 注册皮肤配置。 */
    public static void register(HeroClass cls, int skinIndex, SkinDef def) {
        REGISTRY.put(key(cls, skinIndex), def);
    }

    // ===================== 查询 API =====================

    /** 获取皮肤配置；未注册返回 null。 */
    public static SkinDef get(HeroClass cls, int skinIndex) {
        return REGISTRY.get(key(cls, skinIndex));
    }

    /** 该皮肤是否有特殊配置（大立绘或特殊 sprite）。 */
    public static boolean hasConfig(HeroClass cls, int skinIndex) {
        return REGISTRY.containsKey(key(cls, skinIndex));
    }

    /** 当前英雄是否使用了注册的特殊 sprite 皮肤。 */
    public static boolean isCustomSkin(Hero hero) {
        SkinDef def = get(hero.heroClass, hero.heroClass.GetSkin());
        return def != null && def.spriteFactory != null;
    }

    /** 返回某职业所有已注册的皮肤序号（升序）。 */
    public static List<Integer> registeredSkins(HeroClass cls) {
        List<Integer> result = new ArrayList<>();
        String prefix = cls.ordinal() + ":";
        for (String k : REGISTRY.keySet()) {
            if (k.startsWith(prefix)) {
                result.add(Integer.parseInt(k.substring(prefix.length())));
            }
        }
        java.util.Collections.sort(result);
        return result;
    }

    // ===================== 工厂 API =====================

    /** 为当前英雄创建游戏内 sprite。未注册特殊 sprite 时返回默认 HeroSprite。 */
    public static HeroSprite createSprite(Hero hero) {
        SkinDef def = get(hero.heroClass, hero.heroClass.GetSkin());
        if (def != null && def.spriteFactory != null) {
            return def.spriteFactory.get();
        }
        return new HeroSprite();
    }

    /** 当前英雄的小头像；非特殊头像返回 null。 */
    public static Image customAvatar(Hero hero) {
        return avatarFor(hero.heroClass, hero.heroClass.GetSkin());
    }

    /** 按 (职业, skinIndex) 获取小头像；无特殊头像返回 null。 */
    public static Image avatarFor(HeroClass cls, int skinIndex) {
        SkinDef def = get(cls, skinIndex);
        if (def != null && def.avatarFactory != null) {
            return def.avatarFactory.get();
        }
        return null;
    }

    // ===================== 已注册的皮肤 =====================

    static {
        // ---- 大立绘皮肤（普通 sprite，仅预览图不同） ----
        register(HeroClass.WARRIOR,  4, SkinDef.splash("splashes/skin/giftskin_warrior.png",  80, 112));
        register(HeroClass.ROGUE,    4, SkinDef.splash("splashes/skin/giftskin_rogue.png",    80, 112));
        register(HeroClass.MAGE,     4, SkinDef.splash("splashes/skin/mage_collagedays.png",  80, 112));
        register(HeroClass.DUELIST,  4, SkinDef.splash("splashes/skin/duelist_kitsunemimi.png",80, 112));
        register(HeroClass.DUELIST,  5, SkinDef.splash("splashes/skin/duelist_desertspirit.png",80,112));


        register(HeroClass.WARRIOR, 6, SkinDef.full(
            "splashes/skin/warrior_bunny.png", 80, 112,
            BunnySprite::new,
            () -> {
                Image img = new Image(Assets.Sprites.BUNNY);
                img.frame(0, 0, 20, 22);
                return img;
            },
            Assets.Sprites.BUNNY, 20, 22,
            new int[]{0, 1},
            new int[]{2, 3, 4, 5, 6, 7},
            new int[]{8, 9, 10, 11, 12, 13, 14},
            new int[]{15, 16, 17, 18}
        ));

        register(HeroClass.DUELIST, 6, SkinDef.full(
                "splashes/skin/duelist_crimson_fatebound.png", 80, 112,
                CrimsonSprite::new,
                () -> {
                    Image img = new Image(Assets.Sprites.CRIMSON);
                    img.frame(0, 0, 28, 19);
                    return img;
                },
                Assets.Sprites.CRIMSON, 28, 19,
                new int[]{0, 1, 2, 3, 4, 5},
                new int[]{6, 7, 8, 9 ,10,11},
                new int[]{12, 13, 14, 15, 16},
                new int[]{17, 18, 19, 20, 21},
                6, 20, 8, 15
        ));
    }
}
