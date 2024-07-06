package com.shatteredpixel.shatteredpixeldungeon.ui.changelist.mlpd;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.PaswordBadges;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.lb.RivalSprite;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.NetIcons;
import com.shatteredpixel.shatteredpixeldungeon.effects.PasswordBadgeBanner;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.ChangesScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CrivusStarFruitsSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DM720Sprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DeepSeaSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DimandKingSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DragonGirlBlueSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DwarfGeneralSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.FireCrystalSprites;
import com.shatteredpixel.shatteredpixeldungeon.sprites.FireDragonSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.HollowKnightSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.KatydidSprites;
import com.shatteredpixel.shatteredpixeldungeon.sprites.KongFuSprites;
import com.shatteredpixel.shatteredpixeldungeon.sprites.LuoWhiteSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MintSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MoonLowSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.NyzSprites;
import com.shatteredpixel.shatteredpixeldungeon.sprites.PianoLeSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.PiraLandSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.QinWolfSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.RedNecromancerSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SakaFishBossSprites;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ShopkeeperSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SmallLeafSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.WhiteGirlSprites;
import com.shatteredpixel.shatteredpixeldungeon.sprites.YetYogSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIcon;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.ui.changelist.ChangeButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.changelist.ChangeInfo;
import com.watabou.noosa.Image;

import java.util.ArrayList;

public class vM0_7_X_Changes {
    public static void addAllChanges(ArrayList<ChangeInfo> changeInfos) {
        add_V0760_Changes(changeInfos);
        add_V0755_Changes(changeInfos);
        add_V0751_Changes(changeInfos);
        add_V0750_Changes(changeInfos);
        add_V0740_Changes(changeInfos);
        add_V0735_Changes(changeInfos);
        add_V0732_Changes(changeInfos);
        add_V0731_Changes(changeInfos);
        add_V0730_Changes(changeInfos);
        add_V0723_Changes(changeInfos);
        add_V0721_Changes(changeInfos);
        add_V0716_Changes(changeInfos);
        add_V0715_Changes(changeInfos);
        add_V0714_Changes(changeInfos);
        add_V0713_Changes(changeInfos);
        add_V0712_Changes(changeInfos);
        add_V0711_Changes(changeInfos);
        add_V0710_Changes(changeInfos);
        add_V079_Changes(changeInfos);
        add_V078_Changes(changeInfos);
        add_V077_Changes(changeInfos);
        add_V076_Changes(changeInfos);
        add_V075_Changes(changeInfos);
        add_V074_Changes(changeInfos);
        add_V071_Changes(changeInfos);
        add_GYD_Changes(changeInfos);
    }

    public static void add_V0760_Changes( ArrayList<ChangeInfo> changeInfos ) {
        ChangeInfo changes = new ChangeInfo("v0.7.6.0-NER2", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "buffs"), false, null);
        changes.hardlight(Window.GREEN_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.WORN_SHORTSWORD), ("战士天赋调整"),
                ("战士的天赋进一步加强，具体参考下方说明\n\n" +
                        "T1调整：\n" +
                        "丰收喜悦<--加强-->丰收一餐：\n" +
                        "种子概率提升\n" +
                        "T2调整：\n\n" +
                        "钢铁之胃<--加强-->荆棘之胃：\n" +
                        "+1 进食期间受到攻击则反伤_9点_伤害\n" +
                        "+2 进食期间受到攻击则反伤_12点_伤害")));


        changes = new ChangeInfo("v0.7.6.0-NER", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "buffs"), false, null);
        changes.hardlight(Window.GREEN_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.WORN_SHORTSWORD), ("战士天赋加强"),
                ("战士1 和 2阶 的天赋进行加强，具体参考下方说明\n\n" +
                        "T1调整：\n\n" +
                        "丰盛一餐-->加强-->丰收时刻：\n" +
                        "+1 在原有基础上，给予食用种子权利，并且有5%概率触发有益种子的相同药水效果\n" +
                        "+2 在+1基础上，触发药水概率提升至10%\n\n" +
                        "重做-->钢铁意志：\n" +
                        "+1 斩杀敌人后获得_2点护盾_。\n" +
                        "+2 斩杀敌人后获得_3点护盾_。\n\n" +
                        "T2调整：\n" +
                        "钢铁之胃<--加强-->荆棘之胃：\n" +
                        "+1 进食期间受到攻击则获得_ 1回合 荆棘护身效果_\n" +
                        "+2 进食期间受到攻击则获得_ 2回合 荆棘护身效果_\n\n" +
                        "液蕴意志<--重做-->再生愈合：\n" +
                        "+1：每当 蕾零安洁 受到一次直接损失生命值的物理伤害后，她在下一回合可获得_ 2点 _护盾。\n" +
                        "+2：每当 蕾零安洁 受到一次直接损失生命值的物理伤害后，她在下一回合获得_ 4点 _护盾，且获得 _ 弱效治疗 _效果\n" +
                        "弱效治疗：最大生命的_ 5% _的恢复量\n\n" +
                        "即兴投掷<--重做-->靶心瞄准：\n" +
                        "+1：蕾零安洁向敌人扔出非投掷武器的物品时会对其造成 _2 回合_的瞄准效果和_ 2回合 _致盲效果。这个天赋有 50 回合的冷却时间。\n" +
                        "+2：蕾零安洁向敌人扔出非投掷武器的物品时会对其造成 _3 回合_的瞄准效果和_ 3回合 _致盲效果，并且可以_选择 闪现 _到目标面前。这个天赋有 50 回合的冷却时间。\n" +
                        "瞄准效果：物理伤害提升10%。")));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
        changes.hardlight(CharSprite.WARNING);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
                "0.7.5.6版本:\n\n" +
                        "1.修复超级远古版本跨版本升级异常" +
                        "2.灯火现在不再有挑战限制，勾选即为一挑。但后续版本灯火会从挑战下线，并作为特别模块在常规局启用。"));

    }

    public static void add_V0755_Changes( ArrayList<ChangeInfo> changeInfos ) {
        ChangeInfo changes = new ChangeInfo("v0.7.5.5-6", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        Image s =new PiraLandSprite();
        s.scale.set(PixelScene.align(1f));
        changes.addButton(new ChangeButton(s, ("新MiniBoss：鱼人号"),
                "端午节特别行动限时作战Boss，作战时间6.11-6.21\n\n6.15:修复重大异常Bug"));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.RICESWORD), ("新传奇武器：糯米大剑"),
                ("击败端午节限时行动后获得生成权利！\n\n6.15:修复异常问题")));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
        changes.hardlight(CharSprite.WARNING);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
                "0.7.5.5版本:\n\n" +
                        "1.修复难度没有正确加载的问题\n" +
                        "2.修复部分数据异常问题\n" +
                        "3.修复矮人将军楼层未准确加载问题\n" +
                        "4.修复隐藏徽章的一些潜在问题\n" +
                        "5.修复FireBase上次提到的一些主要问题"));

    }

    public static void add_V0751_Changes( ArrayList<ChangeInfo> changeInfos ) {
        ChangeInfo changes = new ChangeInfo("v0.7.5.1", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "buffs"), false, null);
        changes.hardlight(CharSprite.POSITIVE);
        changeInfos.add(changes);

        changes.addButton( new ChangeButton(new Image(Assets.Environment.TILES_SEWERS, 48, 80, 16, 16 ), "房间调整",
                "太极房间现在诅咒区域保底+1，最高+3，\n" +
                        "且裂缝区域必定诅咒高等级，\n水面区域必定非诅咒但有必定白板"));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
        changes.hardlight(CharSprite.WARNING);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
                "0.7.5.1版本:\n\n" +
                        "1.修复难度没有正确加载的问题\n" +
                        "2.人生模拟器难度选项启用\n" +
                        "3.优化人生模拟器逻辑\n" +
                        "4.修复暴风水汽扩散至地图外导致的闪退问题\n" +
                        "5.修复挑战异常问题，修复难度异常\n" +
                        "6.修复0层部分NPC奖励异常问题"));

    }

    public static void add_V0750_Changes( ArrayList<ChangeInfo> changeInfos ) {
        ChangeInfo changes = new ChangeInfo("v0.7.5.0-SUMMER", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.DG25), ("开发者模式v0.8"),
                ("1、追加 自定义属性 物品，可调节英雄属性\n\n" +
                        "2.开发者模式已经搬迁至难度选择，请注意。")));


        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.HIGHTWAND_1), ("高阶法杖调整"),
                ("风暴雷霆法杖 与 冰雪魔女 掉落的法杖基本重新制作，磷火法杖伤害略微提升。")));

        changes.addButton(new ChangeButton(NetIcons.get(NetIcons.CHAT), ("查种器V0.4"),
                ("重新制作了查种器界面，并且取消钴币支付\n\n" +
                        "同时再次强调：长按可以进行输入一个参数查找种子（种子分析器）")));

        changes.addButton(new ChangeButton(Icons.get(Icons.PREFS), ("徽章改进"),
                ("徽章部分奖励进行了调整：\n\n" +
                        "1.DM720徽章-猫人给予额外一个诅咒菱晶\n" +
                        "2.冰雪公主徽章-法杖可以自然生成在地牢中\n" +
                        "更多调整，参阅徽章具体说明")));

        changes.addButton(new ChangeButton(Icons.get(Icons.PREFS),("其他改动"),
                ("1.部分素材优化迭代\n" +
                        "2.部分文案优化\n" +
                        "3.部分细节优化更新")));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "buffs"), false, null);
        changes.hardlight(CharSprite.POSITIVE);
        changeInfos.add(changes);

        Image s =new DwarfGeneralSprite();
        s.scale.set(PixelScene.align(0.8f));
        changes.addButton(new ChangeButton(s, ("新Boss：矮人将军"),
                "1.削弱一些技能\n2.完善剧情方面\n3.小恶魔商店在每局击败将军后有强化"));


        changes = new ChangeInfo(Messages.get(ChangesScene.class, "nerfs"), false, null);
        changes.hardlight(Window.R_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.THEDIED), ("肃杀"),
                ("修复回血效果偷袭100%触发问题，修复死神附魔的触发异常问题")));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
        changes.hardlight(CharSprite.WARNING);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
                "0.7.4.0版本:\n\n" +
                        "1、 修复挑战解锁的异常问题\n" +
                        "2、 修复0层部分NPC的对话异常\n" +
                        "3、 修复部分神器的崩溃异常"));
    }

    public static void add_V0740_Changes( ArrayList<ChangeInfo> changeInfos ) {
        ChangeInfo changes = new ChangeInfo("v0.7.4.0", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        Image s =new DwarfGeneralSprite();
        s.scale.set(PixelScene.align(0.8f));
        changes.addButton(new ChangeButton(s, ("新Boss：矮人将军"),
                "剧情设计：QinYue\n\n代码设计：JDSALing\n\n技能原案：设寄师"));

        changes.addButton(new ChangeButton(new QinWolfSprite(), ("秦月的生日"),
                ("在5.14-5.21期间，与秦月交谈获得特别物品。在特定时间下有巨大的作用。\n\n" +
                        "再次祝 秦月酱 生日快乐！！！")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.KINGAXE), ("新武器：王庭和约"),
                ("矮人将军专武，武道技能也同步实装。")));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
        changes.hardlight(CharSprite.WARNING);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.THEDIED), ("肃杀优化"),
                ("修复偷袭攻击伤害异常问题，回血效果略微调整")));

        changes.addButton(new ChangeButton(Icons.get(Icons.BADGES),("新徽章加入"),
                ("两个隐藏徽章加入。")));

        changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
                "0.7.4.0版本:\n\n" +
                        "1、 修复变幻莫测的一些小问题\n" +
                        "2、 优化归返密卷的一些问题\n" +
                        "3、 常规果子的地图发生一些小的改变\n" +
                        "4、 修复寻觅长枪的一些问题"
        ));

        changes.addButton(new ChangeButton(Icons.get(Icons.PREFS),("其他改动"),
                ("1.部分素材优化迭代\n" +
                        "2.部分文案优化\n" +
                        "3.部分细节优化更新")));


        changes = new ChangeInfo(Messages.get(ChangesScene.class, "nerfs"), false, null);
        changes.hardlight(Window.R_COLOR);
        changeInfos.add(changes);

        Image issxsaxs =new DimandKingSprite();
        issxsaxs.scale.set(PixelScene.align(0.8f));
        changes.addButton(new ChangeButton(issxsaxs, ("拟态之王"),
                ("现在拟态之王的套武器伤害不再完全复刻，而是75%。\n\n移速初始改为0.85。")));
    }

    public static void add_V0735_Changes( ArrayList<ChangeInfo> changeInfos ) {
        ChangeInfo changes = new ChangeInfo("v0.7.3.5", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.CHALLANEESICON_16), ("挑战：变幻莫测V0.5"),
                ("对昨日临时更新的血晶层完全重做，\n\n" +
                        "并且血晶层的门改为隐藏门。" +
                        "\n\n不再需要钥匙开门。")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.SEEKSXS), ("新武器：寻觅长枪"),
                ("来自异界的物品，欢迎你的使用与尝鲜。完善武器数据，包括暴击效果。")));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
        changes.hardlight(CharSprite.WARNING);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
                "0.7.3.5版本:\n\n" +
                        "1、 修复变幻莫测的一些小问题\n" +
                        "2、 修复寻觅长枪的一些机制未生效的问题"
        ));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "nerfs"), false, null);
        changes.hardlight(Window.R_COLOR);
        changeInfos.add(changes);

        Image s =new CrivusStarFruitsSprite();
        s.scale.set(PixelScene.align(0.8f));
        changes.addButton(new ChangeButton(s, ("Boss调整:克里璃斯之果"),
                "修复异果没有回合的问题。"));

        Image dragonSprite = new FireDragonSprite();
        dragonSprite.scale.set(PixelScene.align(0.52f));
        changes.addButton(new ChangeButton(dragonSprite, ("熔岩火龙"),
                ("修复火龙没有回合的问题，并且降低火龙的防御与移除限伤机制。")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.CHALLANEESICON_14), ("战术对抗挑战下线"),
                ("下线战术对抗，同时移除酸液体。")));
    }

    public static void add_V0732_Changes( ArrayList<ChangeInfo> changeInfos ) {
        ChangeInfo changes = new ChangeInfo("v0.7.3.2", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.CHALLANEESICON_16), ("挑战：变幻莫测V0.4"),
                ("修复了一堆问题，并追加了3个全新地形感知。")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.SEEKSXS), ("新武器：寻觅长枪"),
                ("来自异界的物品，欢迎你的使用与尝鲜。")));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
        changes.hardlight(CharSprite.WARNING);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.CHALLANEESICON_16), ("DM275"),
                ("DM275伤害提升。")));

    }

    public static void add_V0731_Changes( ArrayList<ChangeInfo> changeInfos ) {
        ChangeInfo changes = new ChangeInfo("v0.7.3.1", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.SMTITEM), ("预载物品：微光向导"),
                ("将在后续版本更新，现在的半成品可在开发者模式测试。")));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
        changes.hardlight(CharSprite.WARNING);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
                "0.7.3.1版本:\n\n" +
                        "1、 修复地狱犬楼层的一些问题\n" +
                        "2、 修复灯火的暗影的一些异常错误\n" +
                        "3、 修复6阶武器嬗变崩溃的问题\n" +
                        "4、 优化拟态王三阶段变身秒人问题 和 拟态王楼层英雄的召唤物异常丢失的问题\n" +
                        "5、 修复恶魔层刷怪异常的问题\n" +
                        "6、 修复全肉大饼贴图异常问题"
        ));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.LINGPEA), ("棱晶调整"),
                ("仅在最大深度为10层之前生效。")));
    }

    public static void add_V0730_Changes( ArrayList<ChangeInfo> changeInfos ) {
        ChangeInfo changes = new ChangeInfo("v0.7.3.0", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.SKIN_7), ("新系统；皮肤系统"),
                ("更新10款新皮肤，欢迎挑选。")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.ICEGOLD), ("限时活动：双倍钴币掉落"),
                ("活动时间：2024-5.1-2024-5.20\n\n" +
                        "活动期间，钴币双倍掉落，欢迎你的游玩。")));

        changes.addButton(new ChangeButton(NetIcons.get(NetIcons.GLOBE),("服务器问题"),
                ( "由于服务器近期有问题，目前仅提供更新接口。将在5月中旬恢复，还请谅解。")));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
        changes.hardlight(CharSprite.WARNING);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
                "0.7.3.0版本:\n\n" +
                        "1、 修复子层的一些问题\n" +
                        "2、 修复一些崩溃问题\n" +
                        "3、 修复酸液体魔法攻击无视生物和障碍物的穿墙问题"
        ));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.CHALLANEESICON_17), ("挑战改动预告"),
                ("难度系统将在后续实装，孤城挑战将会迁移到困难模式中。\n\n同时，开发者模式也将迁移至难度系统中。")));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "nerfs"), false, null);
        changes.hardlight(Window.R_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(Icons.get(Icons.BUFFS), ("平衡调整"),
                ("1.矮人术士血量和攻速降低\n" +
                        "2.矮人术士血量和命中率降低")));
    }

    public static void add_V0723_Changes( ArrayList<ChangeInfo> changeInfos ) {
        ChangeInfo changes = new ChangeInfo("v0.7.2.2-5", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(NetIcons.get(NetIcons.GLOBE),("服务器升级"),
                ( "MLPD服务器已全面升级，将为您带来全新体验。")));

        Image xax = new SakaFishBossSprites();
        xax.scale.set(PixelScene.align(0.72f));
        changes.addButton(new ChangeButton(xax,  "萨卡班甲鱼",
                "AI加强:萨卡班甲鱼将会主动避战，受到伤害有小概率传送。"));

        Image dragonSprite = new FireDragonSprite();
        dragonSprite.scale.set(PixelScene.align(0.52f));
        changes.addButton(new ChangeButton(dragonSprite, ("熔岩火龙"),
                ("AI加强：火龙会主动避战，召唤元素的效果更加频繁。")));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
        changes.hardlight(CharSprite.WARNING);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.LINGPEA), ("主线剧情：无光烈焰之章 V0.3"),
                ("1.部分错误选择不会再中止剧情\n" +
                        "2.纯晶元素击杀后不会影响剧情\n" +
                        "3.添加后续剧情，奥克拉赫线敬请期待\n" +
                        "4.减少火龙层陷阱")));

        changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
                "0.7.2.2-5版本:\n\n" +
                        "1、 修复BR相关问题\n" +
                        "2、 修复0层复活的问题\n" +
                        "3、 修复萨卡班甲鱼层未拾取背包造成的一些异常\n" +
                        "4、 修复异果护甲防御异常\n" +
                        "5、 修复自动售货机的金额异常\n" +
                        "6、 优化部分文案"
        ));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.CHALLANEESICON_14), ("挑战：战术对抗V1.7"),
                ("1.战术对抗不再影响 荒芜之地 挑战\n" +
                        "2.战术对抗聪明的敌人不再会规避悬崖。\n" +
                        "3.战术对抗的酸液体几率改为10%")));
    }

    public static void add_V0721_Changes( ArrayList<ChangeInfo> changeInfos ) {
        ChangeInfo changes = new ChangeInfo("v0.7.2.1", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        Image dragonSprite = new DM720Sprite();
        dragonSprite.scale.set(PixelScene.align(0.72f));
        changes.addButton(new ChangeButton(dragonSprite, ("DM720调整"),
                ("1.去除常态在水上回复和导线加血。\n" +
                        "2.绝命头目难度实装，详见挑战。")));

        changes.addButton(new ChangeButton(Icons.get(Icons.PREFS),("第二轮平衡"),
                        ( "1:挑战调整\n"  +
                        "※ 药水瘾症:大饼获取力量每区拥有上限,在低于 12 力量必定追加\n" +
                        "大层/限制次数：(1/2/3/4/5 大层，1/2/2/1/1 次)\n" +
                        "※ 冻肉获取护盾调整为目前一半\n" +
                        "※ 变幻莫测:固定大楼层取消，没有常规陷阱层，只有陷阱Plus层，以及仍然有概率出现大楼层（简单点说，就是地形仍然有，只不过多了精英，三井，常规陷阱换为刀山火海（陷阱Plus层））\n\n" +
                        "2:武器调整\n" +
                        "※ 碧灰:常规成长伤害数值改为1-3，格挡 0-3 改为 无成长格挡 0-10，无任何加成。\n" +
                        "※ 荆棘:异果生命树掉落改为80%掉落，且无保底\n" +
                        "※ 双斧:成长改为 1-2\n" +
                        "※ 筝盾:格挡成长改为与圆盾一致\n" +
                        "※ 龙血鳞片:格挡成长改为 0-4!!!\n\n" +
                        "3:火魔女商店\n" +
                        "※ 火魔女商店进行了优化，不能通过等待白嫖回合。")));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
        changes.hardlight(CharSprite.WARNING);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
                "0.7.2.0版本:\n\n" +
                        "1、 修复拟态王闪退Bug\n" +
                        "2、 修复DM720不合理的生成逻辑\n" +
                        "3、 商店领主奖励进一步优化\n" +
                        "4、 修正孤城移速异常，修正天狗和传送门护甲值异常\n" +
                        "5、 修正爆炸体移速异常\n" +
                        "6、 修正吸血鬼刀攻速异常",
                "本次版本已经由中部群审核通过：\n" +
                        "审核人：正经的仙，WTE\n" +
                        "代码维护：JDSALing"
        ));
    }

    public static void add_V0716_Changes( ArrayList<ChangeInfo> changeInfos ) {
        ChangeInfo changes = new ChangeInfo("v0.7.1.8", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        Image dragonSprite = new FireDragonSprite();
        dragonSprite.scale.set(PixelScene.align(0.72f));
        changes.addButton(new ChangeButton(dragonSprite, ("新Boss：熔岩火龙"),
                ("丛林的幕后主谋，你能否与之一战？同时祝火龙一周年生日快乐。")));


        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.DRAGONSHILED), ("新boss专武：龙血鳞片"),
                ("欢迎试水")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.LINGPEA), ("新主线剧情：无光烈焰之章"),
                ("丛林的真相，在这里呈现……")));

        changes.addButton(new ChangeButton(Icons.get(Icons.BADGES),("新徽章加入"),
                ("一个隐藏徽章，一个常规徽章。")));

        changes.addButton(new ChangeButton(Icons.get(Icons.PREFS),("其他改动"),
                ("1.部分素材优化迭代\n" +
                "2.部分文案优化\n" +
                        "3.部分细节优化更新\n" +
                        "4.图鉴系统初步应用")));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "nerfs"), false, null);
        changes.hardlight(Window.R_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(Icons.get(Icons.BUFFS), ("平衡调整"),
                ("1.归返秘卷和返回晶柱以及空间信标在子层无效\n" +
                        "2.升级卷轴不会再在子层生成")));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
        changes.hardlight(CharSprite.WARNING);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
                "0.7.1.8版本:\n\n" +
                        "1.修复BR模式中的相关异常\n" +
                        "2.部分文案异常修复\n" +
                        "3.修复孤城数值异常相关问题,修复拟态王相关问题"
        ));

    }


    public static void add_V0715_Changes( ArrayList<ChangeInfo> changeInfos ) {
        ChangeInfo changes = new ChangeInfo("v0.7.1.6", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new RedNecromancerSprite(),("新精英怪：死灵领主"),
                ("火苍蝇的精英变体。")));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "nerfs"), false, null);
        changes.hardlight(Window.R_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(Icons.get(Icons.BUFFS), ("功能移除：效果按钮"),
                ("由于BUG太多，被移除。")));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
        changes.hardlight(CharSprite.WARNING);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.SKELETONGOLD), ("BossRushV1.7"),
                ("1.修复异果红雾异常\n\n" +
                        "2.修复启动系统未识别为BR模式")));

        changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
                "0.7.1.6版本:\n\n" +
                        "1.修复BR模式中的相关异常\n" +
                        "2.修复怪物在裂缝的一些异常\n" +
                        "5.修复灯火的一些问题\n" +
                        "6.修复部分BOSS的一些问题"
        ));

    }

    public static void add_V0714_Changes( ArrayList<ChangeInfo> changeInfos ) {
        ChangeInfo changes = new ChangeInfo("v0.7.1.5", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.BZMDR_BOOKS), ("新玩家书籍"),
                ("新玩家书籍加入")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.GRILLED_FISH), ("新武器：烤鲱鱼"),
                ("惊鸿杯第一届-百炼成钢胜出者，现已加入游戏内。")));

        changes.addButton(new ChangeButton(Icons.get(Icons.BUFFS), ("功能更新：效果按钮"),
                ("现在可以主动取消有益效果")));

        changes.addButton(new ChangeButton(new HollowKnightSprite(),("新NPC:小骑士"),
                ("圣巢一哥。")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.SKELETONGOLD), ("BossRushV1.6"),
                ("1.修复一些异常\n\n" +
                        "2.第四难度开放")));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
        changes.hardlight(CharSprite.WARNING);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.BOMB_SWORD), ("炸弹匕首"),
                ("修复了炸弹可以被计数的问题")));

        changes.addButton(new ChangeButton(new RivalSprite(),("暗影"),
                ("强化AI，加强效果。")));


        changes.addButton(new ChangeButton(Icons.get(Icons.PREFS),("杂项调整"),
                ("1.白宴NPC调整：优化对话逻辑\n" +
                        "2.底层代码优化迭代\n" +
                        "3.优化BGM大小\n" +
                        "4.挑战等级调整：\n\n" +
                        "1，2，3挑-D,\n" +
                        "4，5，6挑-C,\n" +
                        "7，8挑-B\n" +
                        "9，10挑-B+\n" +
                        "11，12，13挑-A\n" +
                        "14挑-A+\n" +
                        "15挑-S\n" +
                        "16挑-SS\n" +
                        "17挑-SSS")));
    }


    public static void add_V0713_Changes( ArrayList<ChangeInfo> changeInfos ) {
        ChangeInfo changes = new ChangeInfo("v0.7.1.3-4", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(Icons.get(Icons.LANGS), ("语言更新：繁体中文"),
                ("来自Sotis的繁体中文已经更新")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.BOMB_SWORD), ("炸弹匕首回归"),
                ("孩子们，我回来了。并变为5阶武器。")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.HIGHTWAND_1), ("新法杖：毒液侵染法杖"),
                ("由于代码错误，这个应该是毒液浸染的，而非紊乱法杖\n\n合成方式：沉睡符石+腐蚀酸雾合剂+酸杖\n\n这根法杖能射出一颗会在目标位置爆炸成一团猩红雾气的法球，可以释放出一种极具腐蚀性的气体，进入猩红毒雾的任何单位都会持续受到起始为若干点的持续递增伤害。在高度中毒情况下，会导致目标失明。在严重中毒情况下，会直接窒息目标，直至死亡降临。\n\n0.7.1.4：修复弹道问题")));


        changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
        changes.hardlight(CharSprite.WARNING);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
                "0.7.1.3版本:\n\n" +
                        "1.修复BR模式中的钴币错误，不过老存档无法同步\n" +
                        "2.修复怪物在裂缝的一些异常\n" +
                        "3.修复蝎子的一些异常问题\n" +
                        "4.修复战术对抗的一些问题\n" +
                        "5.修复灯火隐没可以在20层后获得的问题\n" +
                        "6.修复消逝草的一些异常问题"
        ));

        changes.addButton(new ChangeButton(Icons.get(Icons.PREFS), ("杂项改动"),
                ("1.手机端的buff显示现在最多一行六个\n" +
                        "2.部分UI迭代优化")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.CHALLANEESICON_14), ("挑战：战术对抗V1.6"),
                ("1.战术对抗竞技场改动：目标拥有Boss或者NPC属性或者处于子层时，竞技场不会生效\n\n" +
                        "2.战术对抗竞技场粒子效果优化")));
    }

    public static void add_V0712_Changes( ArrayList<ChangeInfo> changeInfos ) {
        ChangeInfo changes = new ChangeInfo("v0.7.1.2", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(NetIcons.get(NetIcons.GLOBE), ("在线更新系统V2.0"),
                ("进行了全方位优化，包括下载进度，游戏局内进度查看。可边下边更。")));

        changes.addButton(new ChangeButton(Icons.get(Icons.AUDIO), ("新音乐：沙漠"),
                ("作者：犬\n\n将在后续更新黄金时代第一部分同步实装。目前可在现实时间的夜晚状态彩蛋剧情欣赏音乐。")));


        changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
        changes.hardlight(CharSprite.WARNING);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
                "0.7.1.2版本:\n\n" +
                        "1.修复钴币相关问题\n" +
                        "2.修复FireBase上个版本提到的一些崩溃错误\n" +
                        "3.修复变幻莫测粘咕水晶钥匙的一些异常"
        ));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.CHALLANEESICON_14), ("挑战：战术对抗V1.5"),
                ("1.添加战术对抗竞技场\n\n2.聪明的敌人20%的概率不掉落于悬崖\n\n3.在同时开启此挑战和恐药后，阳春草不再提供治疗，反而给予和治疗药水相同的中毒效果")));

        changes.addButton(new ChangeButton(Icons.get(Icons.PREFS), ("杂项改动"),
                ("1.部分素材优化迭代\n" +
                        "2.部分文案优化\n" +
                        "3.部分细节优化更新\n" +
                        "4.主界面进行了优化，并丰富了更多界面音乐")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.SKELETONGOLD), ("BossRushV1.5"),
                ("1.修复2层钥匙异常\n\n2.修复14层和29层的一些异常\n\n3.修复部分地形的异常")));

    }

    public static void add_V0711_Changes( ArrayList<ChangeInfo> changeInfos ) {
        ChangeInfo changes = new ChangeInfo("v0.7.1.0", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new SmallLeafSprite(), ("小叶"),
                ("完善NPC")));

        changes.addButton(new ChangeButton(new PianoLeSprite(), ("琴里"),
                ("小叶的姐姐，完善NPC")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.CHALLANEESICON_14), ("新挑战：战术对抗"),
                ("很显然，怪物已经受够你了，现在，你不会好过了。\n\n你遭遇的敌人将会更加聪明或者更加愚钝(各占50%)。\n\n1.愚钝的敌人大部分时间会跟着聪明的敌人，且种族必须一致（也就是老鼠跟老鼠）\n\n2.聪明的敌人在血量小于等于50%并脱战后，会寻找周围的有益植物并获得各种增益并将地块更新为水地形，愚钝的敌人则会完全随机踩植物\n\n(注意：如果开启此挑战，荒芜挑战仍然会有植物和种子，但仍然无法种植和没有露水)\n\n3.聪明的敌人无法掉入裂缝，在掉入前会爬上来\n\n4.(12层之后)聪明的敌人在产生时有1%的概率变成突变酸液体，孩子们,我回来了。")));

        changes.addButton(new ChangeButton(NetIcons.get(NetIcons.GLOBE), ("在线更新系统"),
                ("删除原有的老更新系统，改为在线更新系统。可在线下载apk并安装。\n\n电脑版仍然只能手动打开URL自行下载。")));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
        changes.hardlight(CharSprite.WARNING);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.CHALLANEESICON_17), ("挑战优化：孤城迷境"),
                ("孤城印记现在是有概率出现,概率25%")));

        changes.addButton( new ChangeButton(new Image(Assets.Environment.TILES_COLD, 48, 80, 16
                , 16), "房间改动",
                "仙人跳房间现在有5%的概率出现更加恐怖的怪组"));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.SKELETONGOLD), ("BossRushV1.2"),
                ("1.修复一些问题，现在全部改为金券，5个堆叠可以换一个金券，一次性堆叠50金币可以获得一个金券\n\n" +
                        "2.狗子现在可以吃到词条加成，矮人武将怪组调整，加强14层Boss\n\n3.Boss可以掉落更多金券")));

        changes.addButton(new ChangeButton(Icons.get(Icons.PREFS), ("杂项改动"),
                ("1.部分素材优化迭代\n" +
                        "2.部分文案优化\n" +
                        "3.部分细节优化更新\n" +
                        "4.主界面进行了优化，并丰富了更多界面音乐")));

    }

    public static void add_V0710_Changes( ArrayList<ChangeInfo> changeInfos ) {
        ChangeInfo changes = new ChangeInfo("v0.7.0.X", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.SKELETONGOLD), ("新模式：BossRush"),
                ("仍然在测试，请谨慎游玩。")));


        changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
        changes.hardlight(CharSprite.WARNING);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new DragonGirlBlueSprite(), ("远古遗迹v1.3"),
                ("1.小蓝固定刷新\n\n" +
                        "2.优化钥匙剑生成")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.FISHING_SPEAR), "投掷武器优化",
                "优化投掷武器在附魔后的一些问题，感谢手电的修正"));

        changes.addButton(new ChangeButton(Icons.get(Icons.PREFS), ("杂项改动"),
                ("1.部分素材优化迭代\n" +
                        "2.部分文案优化\n" +
                        "3.部分细节优化更新")));

    }

    public static void add_V079_Changes( ArrayList<ChangeInfo> changeInfos ) {
        ChangeInfo changes = new ChangeInfo("v0.7.0.9-10", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        Image ss =new CrivusStarFruitsSprite();
        ss.scale.set(PixelScene.align(0.8f));
        changes.addButton(new ChangeButton(ss, ("Boss调整:克里璃斯之果"),
                "二和三阶段进行了一些调整"));

        changes.addButton(new ChangeButton(new NyzSprites(), ("奈亚子"),
                ("奈亚子立绘加入，修正一些bug")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.HIGHTWAND_1), ("新法杖：毒素紊乱法杖"),
                ("这根法杖的能量非常紊乱，可能释放出各种魔法。释放魔法造成伤害并产生一种随机法术。")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.MEAT_PIE), ("炼金追加新的配方"),
                ("炼金拥有更多配方，欢迎尝鲜。")));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
        changes.hardlight(CharSprite.WARNING);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(NetIcons.get(NetIcons.CHAT), ("查种器V0.3"),
                ("修复了一些bug，现在查找速度更加的快。等级也会渲染出特殊颜色来。并且长按可以进行输入一个参数查找种子（此功能无需钴币）")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.HIGHTWAND_6), ("共生法杖"),
                ("优化发射效果。")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.BLINDING_DART), ("投掷武器"),
                ("修复投掷武器的相关问题。")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.CHALLANEESICON_17), ("挑战改进:孤城迷境"),
                ("在全新孤城中，孤城就是老0层，且孤城特别房间补给从最多4级变成最多2级。\n\n" +
                        "孤城从第2层到第24层追加孤城印记怪。Boss层没有。\n\n" +
                        "孤城每过一天，属性加1%。且孤城返程进行了一些优化。")));

        changes.addButton(new ChangeButton(Icons.get(Icons.PREFS), ("杂项改动"),
                ("1.部分素材优化迭代\n" +
                        "2.部分文案优化\n" +
                        "3.部分细节优化更新\n" +
                        "4.金币为负数时会变成红色")));

        changes = new ChangeInfo("v0.7.0.8", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        Image s =new CrivusStarFruitsSprite();
        s.scale.set(PixelScene.align(0.8f));
        changes.addButton(new ChangeButton(s, ("新Boss:克里璃斯之果"),
                "9挑战以上出现，机制说明请查阅后续选项卡。" +
                        "异果出现顺序：\n" +
                        "9挑以上出现（包括9挑），\n" +
                        "还没有获得异果徽章，100%出现，\n" +
                        "获得异果徽章后，70%出现。\n" +
                        "获得徽章后，在无挑环境中也有20%的概率出现\n" +
                        "\n" +
                        "注意一旦出现，本局固定异果。\n" +
                        "十字架重置是无效的",
                "第一阶段：280-160\n\n" +
                        "果子在中心，12个触手\n" +
                        "第一阶段还有四个英雄遗骸，里面有四个小净化\n" +
                        "注意:四个小净化在阶段转换如果在地面会被清除 阶段转换的其他物品都会立刻还给玩家（类似于念力结晶）\n" +
                        "触手技能：十字毒雾，玩家在非危险区可周期捕获敌人。（就是监狱长那种锁链）\n" +
                        "危险区：指触手和玩家中间有不可跨越地块，这时触手不会抓捕玩家。\n" +
                        "12个触手打完后，玩家进行任意行走后，进入第二阶段。（不要想着卡bug，因为第一阶段最多扣到160，在玩家尚未移动前，不会继续扣减）\n" +
                        "在第一阶段掉入裂缝传到安全区不会有任何伤害。",
                "第二阶段：160-0\n\n" +
                        "触手完全无敌，并产生酸雾效果。\n" +
                        "果子周期性瞬移（嗯，是的，异果是反着来的）\n" +
                        "触手也会进行周期性抓捕\n" +
                        "场上出现两个额外纯晶元素 50血+50盾\n" +
                        "在从第二阶段开始，掉落悬崖除了传送到安全区外，还会固定扣减3滴血。\n" +
                        "在这个阶段打本体会产生固定6伤害的红雾，请小心行事，合理躲避。",
                "最终阶段：0\n\n" +
                        "异果血量降到0后，立刻呼吁大家向它给予能量。在这期间，玩家有两个选择，强杀果子或者是杀触手，从第三阶段开始，因为触手需要给予本体能量，自身不再坚不可摧，可以被攻击。\n" +
                        "如果异果血量大于60以上（不包括60，异果锁定的敌人将周期性受到敌人自身最大血量的三分之一真实伤害）\n" +
                        "在这个阶段打触手会产生固定6伤害的红雾，请小心行事，合理躲避。\n" +
                        "并且果子死亡会造成爆炸伤害，不要靠的太近\n" +
                        "地图中也有一些可利用的元素，更多的等各位自行探索哦。"));

        Image issxsaxs =new FireCrystalSprites();
        issxsaxs.scale.set(PixelScene.align(0.8f));
        changes.addButton(new ChangeButton(issxsaxs, ("商店抢劫常驻啦！！"),
                "商店抢劫进行了优化，并修正了一些问题，并且常驻！！！\n\n我们还会在后续更新带来商店抢劫的额外故事线，也就是说后续还不会100%打莲娜，而是前往神秘的雪凛峡谷帮助莲娜哦。\n\n" +
                        "火魔女也进行了相关优化，具体参考后续表。",
                "莲娜优化\n\n" +
                        "1.修复战斗结束还给予魔女诅咒\n" +
                        "2.水晶现在摧毁莲娜的玫瑰结界会立刻消失",
                "商人领主优化\n\n" +
                        "1.商人领主的东西以原价贩卖，并且在春节期间打折。\n" +
                        "2.莲娜战斗结束不再掉落归溯之书，并且归溯之书仅能回到本层主深度,不再给予选择楼层。"));

        changes.addButton( new ChangeButton(new BuffIcon(BuffIndicator.CORRUPT, true), ("精英挑战优化调整"),
                "新的突变体：\n\n" +
                        "突变乱码体：认知能力坍缩，无法用常理表示……\n\n" +
                        "攻击力提高20%，这个突变体的全部信息被隐藏，包括数据和描述以及形象，但你依然可以通过战斗方式判断大部分敌人的种类。"));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
        changes.hardlight(CharSprite.WARNING);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new DragonGirlBlueSprite(), ("远古遗迹v1.3"),
                ("修正一些问题")));

        changes.addButton(new ChangeButton(new ShopkeeperSprite(), ("商人返程文本"),
                ("商人返程文本添加，包括奈亚子，小恶魔，商人，那些回忆")));

        changes.addButton(new ChangeButton(new LuoWhiteSprite(), ("0层部分优化调整"),
                ("1.优化落白商店\n\n" +
                        "2.奈亚子商店和落白商店商品位置调整")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.GREEN_DARK), ("投武：流光飞刀"),
                ("修复成长开方错误。")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.RED_BLOOD), ("投武：土木人之怒"),
                ("修复成长开方错误。")));

        changes.addButton(new ChangeButton(Icons.get(Icons.PREFS), ("杂项改动"),
                ("1.部分素材优化迭代\n" +
                        "2.部分无用音乐删除\n" +
                        "3.部分细节优化更新\n" +
                        "4.金币为负数时会变成红色")));
    }

    public static void add_V078_Changes( ArrayList<ChangeInfo> changeInfos ) {
        ChangeInfo changes = new ChangeInfo("v0.7.0.8-P1", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        Image ss =new CrivusStarFruitsSprite();
        ss.scale.set(PixelScene.align(0.8f));
        changes.addButton(new ChangeButton(ss, ("Boss调整:克里璃斯之果"),
                "9挑战以上出现，机制说明请查阅后续选项卡,有颜色标注的代表修正。" +
                        "异果出现顺序：\n" +
                        "9挑以上出现（包括9挑），\n" +
                        "还没有获得异果徽章，100%出现，\n" +
                        "获得异果徽章后，70%出现。\n" +
                        "获得徽章后，在无挑环境中也有20%的概率出现\n" +
                        "\n" +
                        "注意一旦出现，本局固定异果。\n" +
                        "十字架重置是无效的",
                "第一阶段：280-160\n\n" +
                        "果子在中心，12个触手，_触手血量第一阶段：60->50，第二阶段：30->40，果子自身闪避从15降为14_\n" +
                        "第一阶段还有四个英雄遗骸，里面有四个小净化\n" +
                        "注意:四个小净化在阶段转换如果在地面会被清除 阶段转换的其他物品都会立刻还给玩家（类似于念力结晶）\n" +
                        "触手技能：十字毒雾，玩家在非危险区可周期捕获敌人。（就是监狱长那种锁链）\n" +
                        "危险区：指触手和玩家中间有不可跨越地块，这时触手不会抓捕玩家。\n" +
                        "12个触手打完后，玩家进行任意行走后，进入第二阶段。（不要想着卡bug，因为第一阶段最多扣到160，在玩家尚未移动前，不会继续扣减）\n" +
                        "在第一阶段掉入裂缝传到安全区不会有任何伤害。",
                "第二阶段：160-0\n\n" +
                        "触手完全无敌，并产生酸雾效果。\n" +
                        "果子周期性瞬移,_瞬移冷却加长,从12回合变成16回合_（嗯，是的，异果是反着来的）\n" +
                        "触手也会进行周期性抓捕\n" +
                        "场上出现两个额外纯晶元素 _30血+30盾_\n" +
                        "在从第二阶段开始，掉落悬崖除了传送到安全区外，还会固定扣减3滴血。\n" +
                        "在这个阶段打本体会产生固定6伤害的红雾，请小心行事，合理躲避。",
                "最终阶段：0\n\n" +
                        "异果血量降到0后，立刻呼吁大家向它给予能量。_6个触手每次给予果子12血,2x6_\n" +
                        "在这期间，玩家有两个选择，强杀果子或者是杀触手，从第三阶段开始，因为触手需要给予本体能量，自身不再坚不可摧，可以被攻击。\n" +
                        "_族群意识不会在60血以上自动消失_" +
                        "如果异果血量大于60以上（不包括60，异果锁定的敌人将周期性受到敌人自身最大血量的三分之一真实伤害）\n" +
                        "在这个阶段打触手会产生固定6伤害的红雾，请小心行事，合理躲避。_同时果子不再传送_\n" +
                        "并且果子死亡会造成_大规模爆炸伤害_，不要靠的太近\n" +
                        "地图中也有一些可利用的元素，更多的等各位自行探索哦。",
                "奖励：\n\n" +
                        "除普通果子的基础奖励还有\n1.200金币\n2.15钴币\n3.2个食物\n4._如果开启灯火,还会在打完后给予一个额外火把_"));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
        changes.hardlight(CharSprite.WARNING);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.ALL_BAG), ("物品调整：武甲背包"),
                ("不能放入投掷武器。")));

        changes.addButton(new ChangeButton(Icons.get(Icons.PREFS), ("杂项改动"),
                ("1.部分素材优化迭代\n" +
                        "2.部分文案优化\n" +
                        "3.部分细节优化更新\n" +
                        "4.金币为负数时会变成红色")));

        changes = new ChangeInfo("v0.7.0.8", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        Image s =new CrivusStarFruitsSprite();
        s.scale.set(PixelScene.align(0.8f));
        changes.addButton(new ChangeButton(s, ("新Boss:克里璃斯之果"),
                "9挑战以上出现，机制说明请查阅后续选项卡。" +
                        "异果出现顺序：\n" +
                        "9挑以上出现（包括9挑），\n" +
                        "还没有获得异果徽章，100%出现，\n" +
                        "获得异果徽章后，70%出现。\n" +
                        "获得徽章后，在无挑环境中也有20%的概率出现\n" +
                        "\n" +
                        "注意一旦出现，本局固定异果。\n" +
                        "十字架重置是无效的",
                        "第一阶段：280-160\n\n" +
                                "果子在中心，12个触手\n" +
                                "第一阶段还有四个英雄遗骸，里面有四个小净化\n" +
                                "注意:四个小净化在阶段转换如果在地面会被清除 阶段转换的其他物品都会立刻还给玩家（类似于念力结晶）\n" +
                                "触手技能：十字毒雾，玩家在非危险区可周期捕获敌人。（就是监狱长那种锁链）\n" +
                                "危险区：指触手和玩家中间有不可跨越地块，这时触手不会抓捕玩家。\n" +
                                "12个触手打完后，玩家进行任意行走后，进入第二阶段。（不要想着卡bug，因为第一阶段最多扣到160，在玩家尚未移动前，不会继续扣减）\n" +
                                "在第一阶段掉入裂缝传到安全区不会有任何伤害。",
                "第二阶段：160-0\n\n" +
                "触手完全无敌，并产生酸雾效果。\n" +
                        "果子周期性瞬移（嗯，是的，异果是反着来的）\n" +
                        "触手也会进行周期性抓捕\n" +
                        "场上出现两个额外纯晶元素 50血+50盾\n" +
                        "在从第二阶段开始，掉落悬崖除了传送到安全区外，还会固定扣减3滴血。\n" +
                        "在这个阶段打本体会产生固定6伤害的红雾，请小心行事，合理躲避。",
                "最终阶段：0\n\n" +
                "异果血量降到0后，立刻呼吁大家向它给予能量。在这期间，玩家有两个选择，强杀果子或者是杀触手，从第三阶段开始，因为触手需要给予本体能量，自身不再坚不可摧，可以被攻击。\n" +
                        "如果异果血量大于60以上（不包括60，异果锁定的敌人将周期性受到敌人自身最大血量的三分之一真实伤害）\n" +
                        "在这个阶段打触手会产生固定6伤害的红雾，请小心行事，合理躲避。\n" +
                        "并且果子死亡会造成爆炸伤害，不要靠的太近\n" +
                        "地图中也有一些可利用的元素，更多的等各位自行探索哦。"));

        Image issxsaxs =new FireCrystalSprites();
        issxsaxs.scale.set(PixelScene.align(0.8f));
        changes.addButton(new ChangeButton(issxsaxs, ("商店抢劫常驻啦！！"),
                "商店抢劫进行了优化，并修正了一些问题，并且常驻！！！\n\n我们还会在后续更新带来商店抢劫的额外故事线，也就是说后续还不会100%打莲娜，而是前往神秘的雪凛峡谷帮助莲娜哦。\n\n" +
                        "火魔女也进行了相关优化，具体参考后续表。",
                "莲娜优化\n\n" +
                        "1.修复战斗结束还给予魔女诅咒\n" +
                        "2.水晶现在摧毁莲娜的玫瑰结界会立刻消失",
                "商人领主优化\n\n" +
                        "1.商人领主的东西以原价贩卖，并且在春节期间打折。\n" +
                        "2.莲娜战斗结束不再掉落归溯之书，并且归溯之书仅能回到本层主深度,不再给予选择楼层。"));

        changes.addButton( new ChangeButton(new BuffIcon(BuffIndicator.CORRUPT, true), ("精英挑战优化调整"),
                "新的突变体：\n\n" +
                        "突变乱码体：认知能力坍缩，无法用常理表示……\n\n" +
                        "攻击力提高20%，这个突变体的全部信息被隐藏，包括数据和描述以及形象，但你依然可以通过战斗方式判断大部分敌人的种类。"));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
        changes.hardlight(CharSprite.WARNING);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new DragonGirlBlueSprite(), ("远古遗迹v1.3"),
                ("修正一些问题")));

        changes.addButton(new ChangeButton(new ShopkeeperSprite(), ("商人返程文本"),
                ("商人返程文本添加，包括奈亚子，小恶魔，商人，那些回忆")));

        changes.addButton(new ChangeButton(new LuoWhiteSprite(), ("0层部分优化调整"),
                ("1.优化落白商店\n\n" +
                        "2.奈亚子商店和落白商店商品位置调整")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.GREEN_DARK), ("投武：流光飞刀"),
                ("修复成长开方错误。")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.RED_BLOOD), ("投武：土木人之怒"),
                ("修复成长开方错误。")));

        changes.addButton(new ChangeButton(Icons.get(Icons.PREFS), ("杂项改动"),
                ("1.部分素材优化迭代\n" +
                        "2.部分无用音乐删除\n" +
                        "3.部分细节优化更新\n" +
                        "4.金币为负数时会变成红色")));
    }

    public static void add_V077_Changes( ArrayList<ChangeInfo> changeInfos ) {
        ChangeInfo changes = new ChangeInfo("v0.7.0.7", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.ALL_BAG), ("新物品：武甲背包"),
                ("帮忙收纳你的武器护甲的背包，但在高于14挑时会被小偷认为是珍贵物品(指疯狂和常规，猩红大盗不会偷取)，\n\n" +
                        "如果你的武甲背包被偷，小偷将陷入0.6移速，并给予你6回合灵视\n\n")));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
        changes.hardlight(CharSprite.WARNING);
        changeInfos.add(changes);

        Image s =new DimandKingSprite();
        s.scale.set(PixelScene.align(0.8f));
        changes.addButton(new ChangeButton(s, ("拟态之王"),
                ("1.修复近战攻击变傻的问题\n" +
                        "2.优化掉落物切换阶段的问题\n" +
                        "3.优化去打天狗楼层掉落物的问题\n" +
                        "4.传送门移速降低为0.85，孤城无法提速传送门\n" +
                        "5.彻底修复拟态王楼层卡存档问题（旧存档可能没有用）")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.CHALLANEESICON_17), ("挑战改进:孤城迷境"),
                ("在全新0层中，现在孤城所有人回归。")));

        changes.addButton(new ChangeButton(new MintSprite(), ("薄绿调整"),
                ("进行了优化，薄绿厨万岁。")));

        changes.addButton(new ChangeButton(new MoonLowSprite(), ("浅月调整"),
                ("进行了优化，添加了立绘。")));

        changes.addButton(new ChangeButton(new DeepSeaSprite(), ("小海调整"),
                ("素材进行了优化。")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.FISHING_SPEAR), "投掷武器优化",
                "优化投掷武器在附魔后的一些问题，感谢手电的修正"));

        changes = new ChangeInfo("削弱", false, null);
        changes.hardlight(Window.RED_COLOR);
        changeInfos.add(changes);


        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.DG3), "归溯钥剑",
                "整体成长属性削弱，从1-11改为1-6。"));

    }

    public static void add_V076_Changes( ArrayList<ChangeInfo> changeInfos ) {
        ChangeInfo changes = new ChangeInfo("v0.7.0.6-HC", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new MintSprite(), ("新NPC:睡觉的猫咪"),
                ("在旅馆处有40%概率生成，奖励一本传奇书籍，不计入0层NPC物品限制中。")));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
        changes.hardlight(CharSprite.WARNING);
        changeInfos.add(changes);

        Image s =new DimandKingSprite();
        s.scale.set(PixelScene.align(0.8f));
        changes.addButton(new ChangeButton(s, ("拟态之王"),
                ("修复了0血还不死亡的问题。")));

        changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
                "0.7.0.6版本:\n\n" +
                        "1.修复阶段性Boss的相关问题\n" +
                        "2.修复莲娜的错误技能使用\n" +
                        "3.修复拟态王无敌问题"
        ));

        changes.addButton(new ChangeButton(Icons.get(Icons.PREFS), ("杂项改动"),
                ("1.部分素材优化迭代\n" +
                        "2.部分细节优化更新\n" +
                        "3.部分界面优化\n")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.CHALLANEESICON_17), ("挑战改进:孤城迷境"),
                ("在全新0层中，除小叶，澪，奈亚，泡泡，落白，旅馆老板娘以外。其他全部在孤城中消失。")));
    }

    public static void add_V075_Changes( ArrayList<ChangeInfo> changeInfos ) {
        ChangeInfo changes = new ChangeInfo("v0.7.0.5", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.ICEGOLD), "冰蓝方孔钴币V0.4",
                "现在绝命头目里面的天狗给予额外10钴币，拟态王给予额外20钴币"));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.THEDIED), "新传奇武器：“肃杀”",
                "定价：600钴币，可以通过药水癔症徽章变成9折。\n\n并且从这个版本开始，传奇武器购买后在地牢中永久解锁实装，直接加入生成池。"));

        Image s =new DimandKingSprite();
        s.scale.set(PixelScene.align(0.8f));
        changes.addButton(new ChangeButton(s, ("绝命头目：拟态之王"),
                ("拟态王绝命头目登场,具体参见挑战内容详细说明。")));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
        changes.hardlight(CharSprite.WARNING);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new KongFuSprites(), ("鸽子调整"),
                ("奖励从蜜枣甜粽改为肉馅饼")));

        changes.addButton(new ChangeButton(new YetYogSprite(), ("泡泡调整"),
                ("素材优化")));

        Image issxsaxs =new FireCrystalSprites();
        issxsaxs.scale.set(PixelScene.align(0.8f));
        changes.addButton(new ChangeButton(issxsaxs, ("商店抢劫优化"),
                ("商店抢劫进行了优化，并修正了一些问题")));

        changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
                "0.7.0.5版本:\n\n" +
                        "1.修复阶段性Boss的相关问题\n" +
                        "2.修复莲娜的错误技能使用"
        ));

        changes.addButton(new ChangeButton(Icons.get(Icons.PREFS), ("杂项改动"),
                ("1.部分素材优化迭代\n" +
                        "2.部分细节优化更新\n" +
                        "3.部分界面优化\n")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.CHALLANEESICON_17), ("挑战改进:孤城迷境"),
                ("在全新0层中，除小叶，澪，奈亚，泡泡，落白以外。其他全部在孤城中消失。")));

    }

    public static void add_V074_Changes( ArrayList<ChangeInfo> changeInfos ) {
        ChangeInfo changes = new ChangeInfo("v0.7.0.4", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(PasswordBadgeBanner.image(PaswordBadges.Badge.WHATSUP.image), ("新隐藏徽章"),
                ("白嫖不规范，金币分“一瓣”")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.ICEGOLD), "冰蓝方孔钴币V0.3",
                "常规获取方法：(当前所在楼层/5)*5 \n\n挑战奖励：10挑以上x2,15挑以上x3\n\n" +
                        "返程讨伐莲娜（商店抢劫）：额外15钴币\n\n" +
                        "孤城Yog-Zot：额外30钴币\n\n注意：由于上个版本有一些错误，导致钴币严重失调。\n\n不过你可以前往落白的商店领取补偿的50钴币（每个设备仅限一次）。\n\n" +
                        "开发者模式Boss可以掉钴币，方便看具体数量，但拾取不计入。"));

        changes.addButton(new ChangeButton(new YetYogSprite(), ("新NPC:泡泡"),
                ("与奈亚同时生成，奈亚和泡泡的关系，懂得都懂。")));

        changes.addButton(new ChangeButton(NetIcons.get(NetIcons.CHAT), ("查种器V0.2"),
                ("修复了一些bug，现在支持多序列查种，红龙之王的奖励也会显示。\n\n默认值也改的更加合理，在16层前。")));

        changes.addButton(new ChangeButton(NetIcons.get(NetIcons.GLOBE), ("解析区域更换"),
                ("由于国内流量负载严重，近期已换到香港节点，网站下载可能会稍微慢一点，还请理解。")));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
        changes.hardlight(CharSprite.WARNING);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new KongFuSprites(), ("鸽子调整"),
                ("奖励从全肉大饼改为蜜枣甜粽")));

        changes.addButton(new ChangeButton(new WhiteGirlSprites(), ("小镇调整"),
                ("除澪，泡泡给予的物品以及落白给予的蓝币，其他npc一局最多领取4次，4次后，自动变为1金币。")));

        changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
                "0.7.0.4版本:\n\n" +
                        "没有任何崩溃错误，它非常的稳定:)"
        ));

        changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.GREATAXE,
                new ItemSprite.Glowing( 0x00FFFF )), "附魔调整：鬼磷（罕见--》稀有（降级））",
                "这个附魔会使磷火从武器中喷薄而出，能够使用点燃敌人并对正在燃烧的敌人造成中毒的额外伤害。\n\n奥术戒加成效果：提升鬼磷几率"));

        changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.GREATSWORD,
                new ItemSprite.Glowing( 0xcc7770 )), "附魔调整：爆破（稀有--》罕见(上位)）",
                "这个附魔会让使用者陷入癫狂，能使爆炸的能量从武器中喷薄而出，能够使敌人目标处受到一次范围伤害亦或者给造成敌人短暂的残废效果。\n\n奥术戒加成效果：提升伤害"));


        changes.addButton(new ChangeButton(Icons.get(Icons.PREFS), ("杂项改动"),
                ("1.部分素材优化迭代\n" +
                        "2.部分细节优化更新\n" +
                        "3.部分界面优化\n")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.CHALLANEESICON_17), ("挑战改进:孤城迷境"),
                ("在全新0层中，除小叶，澪，奈亚，泡泡。其他全部在孤城中消失。")));

    }

    public static void add_V071_Changes( ArrayList<ChangeInfo> changeInfos ) {
        ChangeInfo changes = new ChangeInfo("v0.7.0.0-3", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes.addButton( new ChangeButton(new Image(Assets.Environment.TILES_SEWERS, 48, 80, 16
                , 16), "0层大翻修",
                "0层进行超级翻修，追加了近20个NPC,欢迎游玩。"));

        changes.addButton(new ChangeButton(new KatydidSprites(), ("新怪物：喷毒花"),
                ("由于有人反馈说以前的虫子有一点反感，所以改成了这个。")));

        changes.addButton(new ChangeButton(new NyzSprites(), ("奈亚子房间翻修"),
                ("在0层的某个房间里，新年还有更好的东西等着你。")));

        changes.addButton(new ChangeButton(NetIcons.get(NetIcons.CHAT), ("全新查种器"),
                ("更大 更好 的查种器，但是要收一点钴币（啊，为什么要钴币，肯定要进行资源回收啊（乐）\n\n" +
                        "总之，查种器已经加入，新年还在打折，欢迎来玩哦。")));

        Image issxsaxs =new FireCrystalSprites();
        issxsaxs.scale.set(PixelScene.align(0.8f));
        changes.addButton(new ChangeButton(issxsaxs, ("商店抢劫 限时归来"),
                ("商店抢劫限时归来，初一到元宵节均可抢劫。并且火魔女进行了一些优化。")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.Fish_A), "清蒸鱼",
                "我有鱼鱼蒸，初一到元宵节可生成。"));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.ICEGOLD), "冰蓝方孔钴币V0.2",
                "常规获取方法：(当前所在楼层/5)*5 \n\n(每上升5挑奖励提升)\n\n" +
                        "返程讨伐莲娜（商店抢劫）：额外15钴币\n\n" +
                        "孤城Yog-Zot：额外30钴币\n\n注意：由于上个版本有群友恶意刷币，且有一些问题，为保证公平，已重置钴币。\n\n不过你可以前往落白的商店领取新人的30钴币（每个设备仅限一次）。"));

        changes.addButton( new ChangeButton(new BuffIcon(BuffIndicator.CORRUPT, true), ("基因突变"),
                ("优化基因突变粒子效果,现在可以更好的分辨。")));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
        changes.hardlight(CharSprite.WARNING);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
                "0.7.0.0版本:\n\n" +
                        "1.修复了测试模式和常规模式相反的严重问题" +
                        "2.修复了一些小问题",
                "0.7.0.1版本:\n\n" +
                        "1.修复0层部分地形错误\n" +
                        "2.修复丛林和26层上下楼异常\n" +
                        "3.修复了一些其他问题",
                "0.7.0.2-3版本:\n\n" +
                        "1.修复了部分Boss不掉钴币\n" +
                        "2.修复了浊焰魔女可以使用返回晶柱的问题\n" +
                        "3.修复了暗金宝石护符重进CD丢失的问题\n" +
                        "4.修复了商店抢劫回家因为新版是子层导致循环事件的问题\n" +
                        "5.修复了血月火焰赏金猎人的一些异常\n" +
                        "6.修复了拟态王楼层相关问题，老存档可能不能获得解决，十分抱歉\n" +
                        "7.修复了商店抢劫楼层的一些楼层异常问题\n" +
                        "8.修复了重生十字架在Boss层重生的闪退问题"

        ));

    }

    public static void add_GYD_Changes( ArrayList<ChangeInfo> changeInfos ) {

        ChangeInfo changes = new ChangeInfo("", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes = new ChangeInfo("鱼鱼蒸现状\n\n\n", true, "\n\n\n");
        changes.hardlight(CharSprite.WARNING);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new Image("fish.png"), "清蒸鱼",
                "我有鱼鱼蒸，初一到元宵节可生成。"));


    }

}
