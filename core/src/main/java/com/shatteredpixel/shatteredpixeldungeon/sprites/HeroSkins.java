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

        public SkinDef(String splashPath, int splashFrameW, int splashFrameH,
                       Supplier<HeroSprite> spriteFactory,
                       Supplier<Image> avatarFactory) {
            this.splashPath = splashPath;
            this.splashFrameW = splashFrameW;
            this.splashFrameH = splashFrameH;
            this.spriteFactory = spriteFactory;
            this.avatarFactory = avatarFactory;
        }

        /** 只注册大立绘的便捷构造器。 */
        public static SkinDef splash(String path, int w, int h) {
            return new SkinDef(path, w, h, null, null);
        }

        /** 注册大立绘 + 特殊 sprite + 头像的完整构造器。 */
        public static SkinDef full(String path, int w, int h,
                                   Supplier<HeroSprite> spriteFactory,
                                   Supplier<Image> avatarFactory) {
            return new SkinDef(path, w, h, spriteFactory, avatarFactory);
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

        // ---- Bunny：Warrior skin 6，大立绘 + 独立 20x22 游戏内 sprite ----
        register(HeroClass.WARRIOR, 6, SkinDef.full(
            "splashes/skin/warrior_bunny.png", 80, 112,
            BunnySprite::new,
            () -> {
                Image img = new Image(Assets.Sprites.BUNNY);
                img.frame(0, 0, 20, 22);
                return img;
            }
        ));
    }
}
