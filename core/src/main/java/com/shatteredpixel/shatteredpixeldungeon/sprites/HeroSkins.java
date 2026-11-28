package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.ui.TextEffect;
import com.watabou.noosa.Image;
import com.watabou.noosa.TextureFilm;

import java.util.ArrayList;
import java.util.Collections;
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

        /**
         * 皮肤选择界面标题文字的艺术字效果，默认 NORMAL。
         * 只有特殊皮肤才按需设置（例如群星契约用 GALAXY）。
         */
        public TextEffect titleEffect = TextEffect.NORMAL;

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

        /** 链式配置标题艺术字效果，例如 SkinDef.splash(...).titleEffect(TextEffect.GALAXY)。 */
        public SkinDef titleEffect(TextEffect effect) {
            this.titleEffect = effect == null ? TextEffect.NORMAL : effect;
            return this;
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
        SkinDef def = get(hero.heroClass, hero.skinIndex());
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
        Collections.sort(result);
        return result;
    }

    // ===================== 工厂 API =====================

    /** 为当前英雄创建游戏内 sprite。未注册特殊 sprite 时返回默认 HeroSprite。 */
    public static HeroSprite createSprite(Hero hero) {
        SkinDef def = get(hero.heroClass, hero.skinIndex());
        if (def != null && def.spriteFactory != null) {
            return def.spriteFactory.get();
        }
        return new HeroSprite(hero);
    }

    /**
     * 为远端幽灵/克隆体创建 sprite：按该英雄自己的 skinIndex 映射，
     * 自定义皮肤使用注册的纹理与帧布局，普通皮肤交给 HeroSprite（tier 已按皮肤映射）。
     *
     * @param hero 远端或预览英雄。
     * @return 对应皮肤的精灵。
     */
    public static HeroSprite createRemoteSprite(Hero hero) {
        SkinDef def = get(hero.heroClass, hero.skinIndex());
        if (def != null && def.texturePath != null && def.frameW > 0 && def.frameH > 0) {
            return new RemoteHeroSprite(hero, def);
        }
        return new HeroSprite(hero);
    }

    /** 当前英雄的小头像；非特殊头像返回 null。 */
    public static Image customAvatar(Hero hero) {
        return avatarFor(hero.heroClass, hero.skinIndex());
    }

    /** 按 (职业, skinIndex) 获取小头像；无特殊头像返回 null。 */
    public static Image avatarFor(HeroClass cls, int skinIndex) {
        SkinDef def = get(cls, skinIndex);
        if (def != null && def.avatarFactory != null) {
            return def.avatarFactory.get();
        }
        return null;
    }

    /**
     * 联机房间/玩家列表行用的小头像：特殊皮肤优先用注册头像，
     * 普通皮肤按 skinIndex 映射到职业精灵的对应 tier 帧。
     *
     * @param cls 职业；null 时返回 null。
     * @param skinIndex 皮肤索引（-1=未知，按默认皮肤处理）。
     * @return 头像图片；调用方负责缩放。
     */
    public static Image listAvatar(HeroClass cls, int skinIndex) {
        return listAvatar(cls, skinIndex, -1);
    }

    /**
     * 带护甲外观的联机头像：tier>=0 时直接使用对端同步的精灵 tier（默认皮肤也能显示护甲），
     * 否则退回按皮肤映射的 tier。
     *
     * @param cls 职业。
     * @param skinIndex 皮肤索引。
     * @param tier 对端精灵 tier；-1=未知。
     * @return 头像图片；调用方负责缩放。
     */
    public static Image listAvatar(HeroClass cls, int skinIndex, int tier) {
        if (cls == null) return null;
        Image custom = avatarFor(cls, skinIndex);
        if (custom != null) return custom;
        return HeroSprite.avatar(cls, tier >= 0 ? tier : listSkinTier(skinIndex));
    }

    /** 皮肤索引对应的精灵 tier（与皮肤选择界面的缩略图映射保持一致）。 */
    private static int listSkinTier(int skinIndex) {
        switch (skinIndex) {
            case 1:  return 9;
            case 2:  return 11;
            case 3:  return 10;
            case 4:  return 13;
            case 5:  return 14;
            default: return 0;
        }
    }

    // ===================== 已注册的皮肤 =====================

    /**
     * 按 {@link SkinDef} 的纹理/帧布局渲染的 HeroSprite，用于远端幽灵与克隆体。
     * 构造时先走默认 HeroSprite 初始化，再套用自定义皮肤纹理/帧；普通皮肤不受影响。
     */
    public static class RemoteHeroSprite extends HeroSprite {

        private final Hero owner;
        private final SkinDef def;

        public RemoteHeroSprite(Hero owner, SkinDef def) {
            super(owner);   // 此时 def 尚未赋值，父类 updateArmor 会先走默认逻辑
            this.owner = owner;
            this.def = def;
            updateArmor();
        }

        @Override
        public void updateArmor() {
            if (def == null || def.texturePath == null
                    || def.frameW <= 0 || def.frameH <= 0
                    || def.idleFrames == null || def.idleFrames.length == 0
                    || def.runFrames == null || def.runFrames.length == 0
                    || def.dieFrames == null || def.dieFrames.length == 0
                    || def.attackFrames == null || def.attackFrames.length == 0) {
                super.updateArmor();
                return;
            }

            texture(def.texturePath);
            TextureFilm film = new TextureFilm(def.texturePath, def.frameW, def.frameH);

            idle = new Animation(def.idleFps, true);
            idle.frames(film, box(def.idleFrames));

            run = new Animation(def.runFps, true);
            run.frames(film, box(def.runFrames));

            die = new Animation(def.dieFps, false);
            die.frames(film, box(def.dieFrames));

            attack = new Animation(def.attackFps, false);
            attack.frames(film, box(def.attackFrames));

            zap = attack.clone();
            operate = attack.clone();
            read = attack.clone();
            fly = new Animation(1, true);
            fly.frames(film, def.idleFrames[0]);

            if (owner.isAlive()) {
                idle();
            } else {
                die();
            }
        }

        /** 将 int[] 装箱为 Object[]，供 Animation.frames(TextureFilm, Object...) 使用。 */
        private static Object[] box(int[] arr) {
            Object[] result = new Object[arr.length];
            for (int i = 0; i < arr.length; i++) result[i] = arr[i];
            return result;
        }
    }

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
        ).titleEffect(TextEffect.FIRE));

        register(HeroClass.HUNTRESS, 4, SkinDef.full(
                "splashes/skin/huntress_stellar_covenant.png", 80, 112,
                GalaxyLinkSprite::new,
                () -> {
                    Image img = new Image(Assets.Sprites.GALAXYLINK);
                    img.frame(0, 0, 26, 17);
                    return img;
                },
                Assets.Sprites.GALAXYLINK, 26, 17,
                new int[]{0,1,2,3,4,0,1,2,3,4,0,1,2,3,4,0,1,2,3,4,5,5,6,7,7,7},
                new int[]{8,9,10,11,12,13},
                new int[]{14,15,16,17,18,19},
                new int[]{20,21,22,23,24},
                4, 20, 8, 15
        ).titleEffect(TextEffect.GALAXY));
    }
}
