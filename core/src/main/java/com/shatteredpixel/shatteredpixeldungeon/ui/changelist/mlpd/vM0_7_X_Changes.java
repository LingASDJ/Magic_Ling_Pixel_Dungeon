package com.shatteredpixel.shatteredpixeldungeon.ui.changelist.mlpd;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.PaswordBadges;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.ShopGuardDead;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.gold.ArtilleristSprite;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.gold.GnollBlindSprite;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.gold.HermitCrabSprite;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.lb.RivalSprite;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.NetIcons;
import com.shatteredpixel.shatteredpixeldungeon.effects.PasswordBadgeBanner;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.ChangesScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ApprenticeWitchSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ButcherSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CrivusStarFruitsSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DM720Sprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DeadDogCerberusSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DeepSeaSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DimandKingSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DragonGirlBlueSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DreamSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DwarfGeneralSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.FireCrystalSprites;
import com.shatteredpixel.shatteredpixeldungeon.sprites.FireDragonSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.FrankensteinSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GhostHalloweenSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GhostSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GudaziSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.HollowKnightSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.KatydidSprites;
import com.shatteredpixel.shatteredpixeldungeon.sprites.KongFuSprites;
import com.shatteredpixel.shatteredpixeldungeon.sprites.LuoWhiteSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MintSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MoonLowSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.NyzSprites;
import com.shatteredpixel.shatteredpixeldungeon.sprites.PeachGodStateSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.PianoLeSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.PiraLandSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.PumkingGhostSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.QinWolfSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.QliphothSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.QuestionSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.RedNecromancerSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SakaFishBossSprites;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ShieldHuntsmanSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ShopkKingSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ShopkeeperSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SkyDeadSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SmallLeafSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.VampireSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.WhiteGirlSprites;
import com.shatteredpixel.shatteredpixeldungeon.sprites.YetYogSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ZakoSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIcon;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.TalentIcon;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.ui.changelist.ChangeButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.changelist.ChangeInfo;
import com.watabou.noosa.Image;

import java.util.ArrayList;

public class vM0_7_X_Changes {
    public static void addAllChanges(ArrayList<ChangeInfo> changeInfos) {
        add_V0870_Changes(changeInfos);
        add_V0860_Changes(changeInfos);
        add_V0852_Changes(changeInfos);
        add_V0850_Changes(changeInfos);
        add_V0848_Changes(changeInfos);
        add_V0845_Changes(changeInfos);
        add_V0840_Changes(changeInfos);
        add_V0831_Changes(changeInfos);
        add_V0830_Changes(changeInfos);
        add_V0822_Changes(changeInfos);
        add_V0820_Changes(changeInfos);
        add_V0810_Changes(changeInfos);
        add_V0808_Changes(changeInfos);
        add_V0805_Changes(changeInfos);
        add_V0801_Changes(changeInfos);
        add_V0800M1_Changes(changeInfos);
        add_V07993_Changes(changeInfos);
        add_V07990_Changes(changeInfos);
        add_V078_90_Changes(changeInfos);
        add_V0770_Changes(changeInfos);
        add_V0765_Changes(changeInfos);
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

    public static void add_V0870_Changes(ArrayList<ChangeInfo> changeInfos ) {
        ChangeInfo changes = new ChangeInfo("v0.8.7.0", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
        changes.hardlight(Window.GREEN_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new Image(Assets.Interfaces.HAICONS, 32, 0, 16, 16), "困难模式",
                "困难模式重磅回归！欢迎前来测试！"));

        changes.addButton(new ChangeButton(Icons.get(Icons.NEWS), ("新兑换码：little surprise of bzmdr"),
                ("进入游戏通过落白商店 或者 游戏菜单 获取奖励！\n\n有效期：永久有效")));

        changes.addButton(new ChangeButton(Icons.get(Icons.NEWS), ("新兑换码：RedFish Bomb Gifts"),
                ("进入游戏通过落白商店 或者 游戏菜单 获取奖励！\n\n有效期：永久有效")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.SCROLL_GOLEM), ("巨像卷轴"),
                ("在BR和金蝶中有概率在商店中生成。")));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
        changes.hardlight(CharSprite.WARNING);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(Icons.get(Icons.PREFS), ("其他改动"),
                ("1.矮人将军加强，对使用小甲鱼逃课进行反制\n" +
                        "2.古堡活动时间延长，第一阶段继续开启")));


        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.DG3), ("归溯钥剑"),
                ("现在只能在商店处购买，且移出五阶武器生成池。")));

        changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
                "魔绫像素地牢-修复内容：\n\n" +
                        (       "_-_ 修复炼金菱晶在BR快捷栏可使用的Bug\n" +
                                "_-_ 修复飞镖特效导致的崩溃\n" +
                                "_-_ 修复触发下楼0层前的对话后再选择模式物品时，无法通过非孤城的普通模式下楼口进入特殊模式\n" +
                                "_-_ 修复森林弩伤害加成异常的问题\n" +
                                "_-_ 修复有buff取下电子烟时闪退\n" +
                                "_-_ 修复部分藏品概率异常问题")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.SCROLL_GOLD), ("金蝶模式V2.3"),
                (
                        "金蝶模式-V2.3\n\n" +
                                "金蝶Boss怪组调整：\n\n" +
                                "5层：果子 异果 史莱姆Wang 天痕" +
                                "\n10层： 暗影 拟态王 绿野精灵完全体 天狗 DM300" +
                                "\n15层：冰雪魔女 DM720 矮人武将 萨卡班甲鱼" +
                                "\n20层：矮人武将 矮人将军 矮人国王 浊燃魔女")));

        changes = new ChangeInfo("v0.8.6.0中测验收通过，予以更新", true, null);
        changes.hardlight(Window.CYELLOW);
        changeInfos.add(changes);
    }

    public static void add_V0860_Changes(ArrayList<ChangeInfo> changeInfos ) {
        ChangeInfo changes = new ChangeInfo("v0.8.6.0", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
        changes.hardlight(Window.GREEN_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.CLEARPRO), ("新传说武器：清道夫"),
                ("商店上新，欢迎各位前来购买。")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.FORESTCROSSBOW), ("新传说武器：森林弓"),
                ("商店上新，欢迎各位前来购买。")));

        changes.addButton(new ChangeButton(Icons.get(Icons.NEWS), ("新兑换码：SHPD-BIRTHDAY"),
                ("进入游戏通过落白商店 或者 游戏菜单 获取奖励！\n\n有效期：20205-8-26 00:00前")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.SHPD_CHEST), ("限时绿宝石宝箱生成"),
                ("2025.8.6-2025.8.25，限时生成绿宝石宝箱，获得丰厚道具奖励。")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.SCROLL_GOLEM), ("塔防联动：巨像卷轴"),
                ("在绿宝石宝箱中有概率获得，欢迎尝鲜。\n\n授权者：Fixed")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.BONESOUP), ("饭桶联动：大骨浓汤"),
                ("在绿宝石宝箱中有概率获得，欢迎尝鲜。\n\n授权者：彦木")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.RATTAIL), ("饭桶联动：耗子尾脂"),
                ("在绿宝石宝箱中有概率获得，欢迎尝鲜。\n\n授权者：彦木")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.ZAKOSOUP), ("饭桶联动：杂鱼汤"),
                ("在绿宝石宝箱中有概率获得，欢迎尝鲜。\n\n授权者：彦木")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.KINGHTSTABBINGSWORD), ("困难模式-Alpha测试"),
                ("在绿宝石宝箱中有概率获得藏品，目前开放了4类，欢迎尝鲜。")));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
        changes.hardlight(CharSprite.WARNING);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
                "魔绫像素地牢-修复内容：\n\n" +
                        (       "_-_ 修复水爆魔药的一些异常\n" +
                                "_-_ 修复一些闪退问题\n" +
                                "_-_ 修复古神充能回合异常\n" +
                                "_-_ 修复寒冰之拳与鬼磷之拳无敌范围异常\n" +
                                "_-_ 修复特殊模式可以进入古堡")));


        changes.addButton(new ChangeButton(Icons.get(Icons.DISPLAY), ("UI迭代"),
                ("_-_ 落白商店现在可以翻页\n" +
                        "_-_ 快捷栏部分插槽更加立体化")));

        changes = new ChangeInfo("v0.8.6.0中测验收通过，予以更新", true, null);
        changes.hardlight(Window.CYELLOW);
        changeInfos.add(changes);
    }

    public static void add_V0852_Changes(ArrayList<ChangeInfo> changeInfos ) {
        ChangeInfo changes = new ChangeInfo("v0.8.5.1-2", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
        changes.hardlight(Window.GREEN_COLOR);
        changeInfos.add(changes);


        Image xs =new DeadDogCerberusSprite();
        xs.scale.set(PixelScene.align(0.6f));
        changes.addButton(new ChangeButton(xs, ("Boss加强：刻耳柏洛斯"),
                ("绝对领域：如有敌方和自身一个位置且非英雄单位，立刻秒杀。如为英雄，立刻弹开数米远，造成冲击距离伤害。")));

        changes.addButton(new ChangeButton(Icons.get(Icons.NEWS), ("新兑换码：NFYIG"),
                ("进入游戏通过落白商店 或者 游戏菜单 获取奖励，祝各位古堡调查愉快！\n\n有效期：20205-8-8 00:00前")));

        changes.addButton(new ChangeButton(Icons.get(Icons.NEWS), ("新兑换码：ARMYDAY"),
                ("进入游戏通过落白商店 或者 游戏菜单 获取奖励祝各位古堡调查愉快！\n\n有效期：20205-8-8 00:00前")));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
        changes.hardlight(CharSprite.WARNING);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
                "魔绫像素地牢-修复内容：\n\n" +
                        (       "_-_ 修复古堡解锁房间生成异常\n" +
                                "_-_ 三井层房间优化和架构增强\n" +
                                "_-_ 修复15挑徽章不生成的部分特殊NPC的异常\n" +
                                "_-_ 修复姜饼人不能击退的异常\n" +
                                "_-_ 修复圣水瓶对人形态的吸血鬼使用后导致游戏进入循环\n" +
                                "_-_ 钥匙剑不再参与嬗变，从列表中移除\n" +
                                "_-_ 创世神之心现在在背包中时，玩家不会进入游戏结束状态\n" +
                                "_-_ 优化28层后跳楼，默认返回入口处\n" +
                                "_-_ 修复苔藓丛簇和陷阱元件饰品未生效")));


        Image issxsaxs =new DimandKingSprite();
        issxsaxs.scale.set(PixelScene.align(0.8f));
        changes.addButton(new ChangeButton(issxsaxs, ("拟态之王优化"),
                ("现在拟态之王攻击前会将玩家的十字架物品寄存在水晶宝箱怪处，战斗结束后返还。")));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "nerfs"), false, null);
        changes.hardlight(Window.R_COLOR);
        changeInfos.add(changes);

        changes.addButton( new ChangeButton(new Image(Assets.Environment.TILES_SEWERS, 48, 80, 16
                , 16), "房间改动",
                "变幻莫测三井房间现在只能选一个，且有二次警告选择。\n\n变幻莫测房子生成权重池子扩大两倍，以确保有大概率是常规楼层（此时饰品生效）"));

        changes = new ChangeInfo("v0.8.5.1-2中测验收通过，予以更新", true, null);
        changes.hardlight(Window.CYELLOW);
        changeInfos.add(changes);
    }

    public static void add_V0850_Changes(ArrayList<ChangeInfo> changeInfos ) {
        ChangeInfo changes = new ChangeInfo("v0.8.5.0", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
        changes.hardlight(Window.GREEN_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(Icons.get(Icons.NEWS), ("新永久兑换码：YLGB"),
                ("进入游戏通过落白商店 或者 游戏菜单 获取奖励，祝各位古堡调查愉快！")));

        changes.addButton(new ChangeButton(Icons.get(Icons.NEWS), ("新永久兑换码：MHJH"),
                ("进入游戏通过落白商店 或者 游戏菜单 获取奖励祝各位古堡调查愉快！")));

        changes.addButton(new ChangeButton(new ApprenticeWitchSprite(), ("新怪物:见习魔女"),
                ("魔法学院的劣等生，见习中的魔女。")));

        changes.addButton(new ChangeButton(new FrankensteinSprite(), ("新怪物:残梦魔偶"),
                ("它在寻找它的创造者，以及，撕碎所有拥有生命的气息。")));

        changes.addButton(new ChangeButton(new GhostHalloweenSprite(), ("新怪物:鬼魂"),
                ("令人捉摸不透的亡魂……。。")));

        changes.addButton(new ChangeButton(new PumkingGhostSprite(), ("新怪物:南瓜头鬼魂"),
                ("令人捉摸不透的亡魂……，还携带着一个南瓜头。")));

        changes.addButton(new ChangeButton(new VampireSprite(), ("新怪物:吸血鬼"),
                ("在古堡伪装成蝙蝠的怪物，十分危险！")));

        Image xs =new DeadDogCerberusSprite();
        xs.scale.set(PixelScene.align(0.6f));
        changes.addButton(new ChangeButton(xs, ("新Boss：刻耳柏洛斯"),
                ("古堡第一阶段的31层Boss，等待你的挑战")));

        changes.addButton(new ChangeButton(Icons.get(Icons.LANGS), ("语言更新：英文"),
                ("英文翻译初步实在，特别感谢Biostarbluexray,1000Mistakes的翻译支持")));

        changes.addButton(new ChangeButton(Icons.get(Icons.BADGES),("加密徽章新增两个，还有一个镀层"),
                ("新增更多加密徽章，欢迎前去探索")));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
        changes.hardlight(CharSprite.WARNING);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(Icons.get(Icons.PREFS), ("其他改动"),
                ("1.部分文案优化\n" +
                        "2.部分素材细节优化更新")));

        changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
                "魔绫像素地牢-修复内容：\n\n" +
                        (       "- br火龙地图设计缺陷 报告:sks123456\n- 拟态王二阶段幼年拟态问题 报告:sks123456\n- 古堡仙人跳房文本缺失 报告:sks123456\n- 创世神之心治疗增幅失效 报告:sks123456\n- 异果摔死问题 报告:Archetto\n- 白浪刺剑技能按钮冲突 报告:sks123456\n- 抢劫相关文本纰漏 报告:sks123456\n- 古神召唤拳头无回复回合 报告:sks123456\n- 变幻莫测徽章异常 报告:sks123456\n- 液蕴机敏天赋失效 报告:sks123456\n- 火龙地图设计缺陷 报告:sks123456")));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "nerfs"), false, null);
        changes.hardlight(Window.R_COLOR);
        changeInfos.add(changes);

        changes.addButton( new ChangeButton(new Image(Assets.Environment.TILES_SEWERS, 48, 80, 16
                , 16), "房间改动",
                "变幻莫测三井房间现在只能选一个，且地图上有其他井使用后也是其他完全失效。"));

        changes.addButton(new ChangeButton(Icons.get(Icons.BADGES),("挑战徽章"),
                ("13挑以上挑战徽章现在不能通过其他特殊模式获取")));

        changes = new ChangeInfo("v0.8.5.0中测验收通过，予以更新", true, null);
        changes.hardlight(Window.CYELLOW);
        changeInfos.add(changes);
    }

    public static void add_V0848_Changes(ArrayList<ChangeInfo> changeInfos ) {
        ChangeInfo changes = new ChangeInfo("v0.8.4.8-9", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
        changes.hardlight(Window.GREEN_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(Icons.get(Icons.NEWS), ("新兑换码：端午安康"),
                ("进入游戏通过落白商店 或者 游戏菜单 获取奖励，祝各位端午安康！")));

        changes.addButton(new ChangeButton(Icons.get(Icons.NEWS), ("新兑换码：吉祥锦鲤"),
                ("进入游戏通过落白商店 或者 游戏菜单 获取奖励，祝各位端午安康！")));

        changes.addButton(new ChangeButton(new WhiteGirlSprites(), ("0层NPC优化：澪"),
                ("在完成13挑后，旅馆2层澪生成概率提升20%，并且澪有概率在旅馆外看见（两者为互斥生成）并且法杖不会再携带诅咒")));

        Image xs =new SkyDeadSprite();
        xs.scale.set(PixelScene.align(0.8f));
        changes.addButton(new ChangeButton(xs, ("新迷你Boss：天罚"),
                ("变幻莫测全新Boss，带来全新的挑战！\n\n所在区域：8层")));

        changes.addButton(new ChangeButton(Icons.get(Icons.PREFS), ("新全局系统：自定义横幅"),
                ("在额外设置中，可以定义游戏内带的横幅主题，新增端午节主题")));

        Image xsx =new Image(SPDSettings.ClassUI() ? Assets.Interfaces.TOOLBAR : Assets.Interfaces.TOOLBARDRAK, 0, 26, 24, 26);
        xsx.scale.set(PixelScene.align(0.8f));
        changes.addButton( new ChangeButton(xsx, "新全局系统：魔绫活动板块",
                "通过活动板块，可以查看各类活动信息，并参加活动，获得奖励。\n\n" +
                        "魔绫像素地牢，基于单机的一款“次世代网游”（Bushi）"));

        changes.addButton(new ChangeButton(Icons.get(Icons.BADGES),("加密徽章新增一个"),
                ("新增更多加密徽章，欢迎前去探索")));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
        changes.hardlight(CharSprite.WARNING);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(Icons.get(Icons.PREFS), ("其他改动"),
                ("1.部分文案优化\n" +
                        "2.部分素材细节优化更新")));

        changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
                "Bug修复-来自开发者汇总\n\n" +
                        (       "_-_ 修复暗影Boss的一些问题，现在不会再次读档\n" +
                                "_-_ 修复火龙Boss的一些问题\n" +
                                "_-_ 修复端午节活动的一些问题\n" +
                                "_-_ 修复治疗类型效果对盟友不生效的问题\n" +
                                "_-_ 修复创世神之心治疗不翻倍的问题\n" +
                                "_-_ 修复锐刻五代的一些潜在小问题\n" +
                                "_-_ 修复拟态王2阶段卡死异常，优化了钻石宝箱王幼年状态无法记录在图鉴的问题\n" +
                                "_-_ 修复大仙人跳房间的文本缺失异常，并且只能在古堡中生成")
        ));

        changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
                "Bug修复-来自Order系统汇总\n\n" +
                        ( "_-_ 修复了拟态王转阶段3的问题\n" +
                                "_-_ 修复了旧存档怪物生成器导致崩溃的问题\n" +
                                "_-_ 修复了金蝶模式地图错误问题（多个条目合并）\n" +
                                "_-_ 修复了迷你太阳贴图错误问题\n" +
                                "_-_ 修复了微光导向卡死问题\n" +
                                "_-_ 修复了痛苦刻痕不死bug残留问题\n" +
                                "_-_ 修复了BR冰雪魔女异常卡死问题\n" +
                                "_-_ 修复了古堡残梦魔偶异常问题\n" +
                                "_-_ 修复了悲伤幽灵等级生成异常问题\n" +
                                "_-_ 修复了甲鱼boss触发流程bug问题\n" +
                                "_-_ 修复了机械之柱友伤缺失问题\n" +
                                "_-_ 修复了火龙地图设计缺陷问题")
        ));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "nerfs"), false, null);
        changes.hardlight(Window.R_COLOR);
        changeInfos.add(changes);

        changes.addButton( new ChangeButton(new BuffIcon(BuffIndicator.SACRIFICE, true), ("霜火极寒"),
                ("最低伤害4点不变，但最大伤害不能超过16伤。")));

        changes.addButton( new ChangeButton(new Image(Assets.Environment.TILES_SEWERS, 48, 80, 16
                , 16), "房间改动",
                "变幻莫测迷你Boss奖励房现在奖励为至多六选二。"));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "buffs"), false, null);
        changes.hardlight(Window.GREEN_COLOR);
        changeInfos.add(changes);

        changes.addButton( new ChangeButton(new ShieldHuntsmanSprite(), "血月魔盾猎人",
                "修复之前命中率为一区的异常问题，并且加强击飞 武器/护甲/杂项栏/ 的效果\n\n添加精神集中，血量调整为固定72，初始移速0.8。\n\n有六分之一概率可以掉落奥术护盾合剂，至多掉落2瓶。"));

        changes = new ChangeInfo("v0.8.4.8中测验收通过，予以更新", true, null);
        changes.hardlight(Window.CYELLOW);
        changeInfos.add(changes);


        changes = new ChangeInfo("2025-5-29", true, null);
        changes.hardlight(Window.CPINK);
        changeInfos.add(changes);
    }

    public static void add_V0845_Changes(ArrayList<ChangeInfo> changeInfos ) {
        ChangeInfo changes = new ChangeInfo("v0.8.4.5-6", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
        changes.hardlight(Window.GREEN_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(Icons.get(Icons.NEWS), ("新兑换码：WolvesGoodFish"),
                ("兑换内容：400钴币 全肉大饼")));

        changes.addButton(new ChangeButton(Icons.get(Icons.NEWS), ("新兑换码：GoldFish-All-KPL"),
                ("兑换内容：390钴币 神秘奖励")));

        changes.addButton(new ChangeButton(Icons.get(Icons.NEWS), ("新兑换码：GoodGoldFish"),
                ("兑换内容：400钴币 咩咩护符")));

        changes.addButton(new ChangeButton(Icons.get(Icons.NEWS), ("新兑换码：ThanksKPLReadPVP"),
                ("兑换内容：300钴币 4个不同类型的粽子")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.CHALLANEESICON_8), ("挑战优化：污泥浊水"),
                ("1.修复离开水地块不立刻处理对应效果的异常" +
                        "\n2.优化处理逻辑，优先级更高")));

        changes.addButton(new ChangeButton(Icons.get(Icons.PREFS), ("新全局系统：自定义横幅"),
                ("在额外设置中，可以定义游戏内带的横幅主题，新增劳动节主题")));

        changes.addButton(new ChangeButton(Icons.get(Icons.ALERT), ("限时复刻"),
                ("5.3-5.21 限时复刻\n\n" +
                        "中秋节一期 & 端午节鱼人号复刻")));

        changes.addButton( new ChangeButton(new Image(Assets.Environment.TILES_SEWERS, 48, 80, 16
                , 16), "房间改动",
                "全新房间：十字墓穴房/骸骨迷宫房加入"));

        changes.addButton(new ChangeButton(Icons.get(Icons.AUDIO), ("全新音乐系统登场"),
                ("4年屎山音乐系统重置，带来更多可能")));


        changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
        changes.hardlight(CharSprite.WARNING);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(Icons.get(Icons.PREFS), ("其他改动"),
                ("1.部分文案优化\n" +
                        "2.部分素材细节优化更新")));

        changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
                "Bug修复\n\n" +
                        (       "_-_ 修复污泥浊水的一些严重异常\n" +
                                "_-_ 优化中秋节任务的一些细节\n" +
                                "_-_ 优化白玫瑰的一些东西")

        ));

        changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
                "Bug修复\n\n" +
                        ( "_-_ 修复拟态之王0血不死亡的严重异常，优化污泥浊水判定\n" +
                                "_-_ 修复污泥浊水的一些严重异常\n" +
                                "_-_ 修复烈阳法杖在元素风暴的异常，并且效果为可视怪物产生磷火+火焰\n" +
                                "_-_ 优化在26层后，陷阱房将完全替换为深渊浮空房\n" +
                                "_-_ 修复先前的子层掉落规则导致的异常闪退，现在调整为在子层掉落深渊的物品，将在本体楼层出现\n" +
                                "_-_ 修复拟态怪类型的掉落规则异常，同时也同步新子层深渊物品掉落规则\n" +
                                "_-_ 修复宝藏迷宫的宝箱怪数量不足导致的游戏卡死")

        ));

        changes = new ChangeInfo("v0.8.4.5中测验收通过，予以更新", true, null);
        changes.hardlight(Window.CYELLOW);
        changeInfos.add(changes);



        changes = new ChangeInfo("2025-5-03", true, null);
        changes.hardlight(Window.CPINK);
        changeInfos.add(changes);
    }

    public static void add_V0840_Changes(ArrayList<ChangeInfo> changeInfos ) {
        ChangeInfo changes = new ChangeInfo("v0.8.4.0", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);


        changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
        changes.hardlight(Window.GREEN_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.CHALLANEESICON_14), ("限时挑战：血族契约"),
                ("限时挑战，将在后续版本收纳为特殊模式")));

        changes.addButton(new ChangeButton(Icons.get(Icons.NEWS), ("新兑换码：Tomb-Sweeping-FestivaDay"),
                ("进入游戏通过落白商店 或者 游戏菜单 获取奖励，祝各位清明安康！")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.CHALLANEESICON_8), ("挑战重做：污泥浊水"),
                ("地牢的水也被污染了，你需要小心谨慎\n\n因为地牢上方的生活污染，水已经被污染了，你需要小心谨慎\n\n_-_ 你踩踏过的水会变为仅对你有效的污泥地块，进入污泥地块会为你附加腐蚀淤泥，并且身上大部分的负面效果的剩余回合时间不会低于5回合。\n_-_踩踏水的祝福不再生效，但水灵药剂的效果不变。\n_-_ 全水地形感觉在获得后还会再有概率80%变成普通地形\n_-_ 每大层会额外产生食人鱼房间\n_-_ 常规商店额外追加“生石灰固体理想合剂")));

        changes.addButton(new ChangeButton(Icons.get(Icons.PREFS), ("新全局系统：自定义横幅"),
                ("在额外设置中，可以定义游戏内带的横幅主题")));


        changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
        changes.hardlight(CharSprite.WARNING);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
                "Bug修复:\n\n" +
                        ("_-_ 修复拟态之王因多段伤害导致跨阶段异常\n" +
                                "_-_ 在宝藏迷宫添加更多宝箱怪以避免无法超过5个击杀数导致游戏卡死\n" +
                                "_-_ 生石灰固态理想试剂范围加强，在污泥挑战中商店中可被购买\n" +
                                "_-_ 现在水爆魔药，暴风迷雾可以将污泥地块直接蒸发，变成普通地块\n" +
                                "_-_ 修复召唤守卫描述异常\n" +
                                "_-_ 修复火龙地形跨存档的严重异常\n" +
                                "_-_ 修复26层下楼古堡后未在初始房间\n" +
                                "_-_ 修复不动如山的削弱后描述未更新的小异常\n" +
                                "_-_ 修复抢劫层选择逃跑获取的回溯之书无法正常回到楼层(使用后会传送至X-？层)\n" +
                                "_-_ 优化商店抢劫：抢劫前往峡谷前，十字架会被留在原地，莲娜的烈焰结晶不会再被自带索敌，优化部分攻击特效\n" +
                                "_-_ 开发者模式杂项生成器追加水爆，羽落，紊乱魔药和无序结晶的生成\n" +
                                "_-_ 修复药水鉴定徽章和卷轴鉴定徽章在迁移之后的版本后无法正常解锁\n" +
                                "_-_ 部分缺失文案修正，部分文案优化\n" +
                                "_-_ 修复子层的一些罕见崩溃问题")
        ));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "nerfs"), false, null);
        changes.hardlight(Window.R_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.DIEDCROSSBOW), ("重型弩炮"),
                ("修复弩炮的子弹伤害错误的为武器自身的伤害。")));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "buffs"), false, null);
        changes.hardlight(Window.GREEN_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.DG18), ("生石灰固体理想合剂"),
                ("现在的蒸发范围为8向2x2的范围")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.ELIXIR_AQUA), ("水爆魔药"),
                ("如果目标为污泥地块，则会将其直接蒸发，变成普通地块。\n\n同时附近3x3范围内的污泥地块也会被蒸发。")));

        changes = new ChangeInfo("v0.8.4.0中测验收通过，予以更新", true, null);
        changes.hardlight(Window.CYELLOW);
        changeInfos.add(changes);



        changes = new ChangeInfo("2025-4-04", true, null);
        changes.hardlight(Window.CPINK);
        changeInfos.add(changes);
    }

    public static void add_V0831_Changes(ArrayList<ChangeInfo> changeInfos ) {
        ChangeInfo changes = new ChangeInfo("v0.8.3.1", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);


        changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
        changes.hardlight(Window.GREEN_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.RECLAIM_TRAP), ("开发者模式工具：陷阱放置器"),
                ("新增陷阱放置器，可方便地在地图上放置陷阱。更多功能，前往游戏内探索。")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.CANDLE), ("开发者模式工具：怪物放置器"),
                ("完全进行重做，可在地图上放置80%的怪物。更多功能，前往游戏内探索。")));

        changes.addButton(new ChangeButton(Icons.get(Icons.CHANGES), ("开发者模式：评分系统实时评估"),
                ("计分版进行优化，开发者模式中可进行实时评估，之前的排行榜也可以看见评分细则")));


        changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
        changes.hardlight(CharSprite.WARNING);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
                "0.8.3.1版本:\n\n" +
                        "1.修复了0挑钴币掉落异常\n" +
                        "2.修复了抢劫商店后全部类型商人跑路的问题\n" +
                        "3.部分文案修正\n" +
                        "4.修复了部分初始背包生成异常问题"

        ));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.ICEGOLD), ("钴币掉落调整"),
                (
                        "_-_ 常规获取方法掉落调整：\n" +
                                "_-_ 【英雄等级 + 地牢深度】 / 5 x 【最大挑战数, 5】\n" +
                                "_-_ 如果最大挑战数不足5，则默认保底最小值为5\n\n" +
                                "_-_ 限时双倍掉落：\n" +
                                "_-_ 2025.3.7-2025.3.31 钴币全局双倍掉落")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.DG12), ("开发者模式工具：升降器"),
                ("现在升降器支持任意楼层传送，且可以传送到0层。")));

        Image issxsaxs =new ShopGuardDead.ShopGuardianRedSprite();
        issxsaxs.scale.set(PixelScene.align(1f));
        changes.addButton(new ChangeButton(issxsaxs, ("商店抢劫调整"),
                "现在商店抢劫后，只能选择一个层的商店进行全部领取。\n\n" +
                        "其他层的商店将会不再生成，且即便是_已经生成的商店也自动移除所有东西_。\n\n且仅普通商人会被移除"));

        changes = new ChangeInfo("v0.8.3.1中测验收通过，予以更新", true, null);
        changes.hardlight(Window.CYELLOW);
        changeInfos.add(changes);

        changes = new ChangeInfo("2025-3-14", true, null);
        changes.hardlight(Window.CPINK);
        changeInfos.add(changes);
    }


    public static void add_V0830_Changes(ArrayList<ChangeInfo> changeInfos ) {
        ChangeInfo changes = new ChangeInfo("v0.8.3.0", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "thanks"), false, null);
        changes.hardlight(Window.Pink_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.RUIKE), ("锐刻五代"),
                ("全新神器，锐克五代!\n\n" +
                        "惊鸿杯神器赛道第一名，你也想抽一根电子烟吗？\n\n" +
                        "物品介绍：被动效果：地牢里的动物朋友变为中立生物\n" +
                        "\n" +
                        "主动效果：神器开启后每回合消耗100%÷（10+神器等级）充能，自身每回合获得1回合体力充沛，每回合在自身位置释放100单位浓浓白烟，白烟初始能遮挡敌人视野，可加入气体类药剂使白烟带有该药剂的效果（白烟仅对敌人生效，每瓶药剂持续100%充能，可叠加）\n" +
                        "\n" +
                        "升级方式：每添加1个雷鸣魔药升2级，每添加1个充能卷轴升1级，上限10级\n" +
                        "\n"),
                        ("充能方式：自动充能，每回合回复0.1%+（0.01%×神器等级）充能，上限100%充能\n" +
                        "\n" +
                        "神器充能效果：每回合回复2%的充能\n" +
                        "\n" +
                        "神器诅咒效果：每回合都有10%概率使自身获得2回合失明并在自身位置释放30回合毒气；地牢里的动物朋友不再睡觉，自动追踪你的位置，所有卷轴名字变为未知，图标和底色变成相同\n" +
                        "\n" +
                        "其它细节：开启神器消耗1回合，关闭神器不耗回合" )));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.SMTITEM), ("微光向导"),
                ("全新炼金物品，微光向导!\n\n" +
                        "惊鸿杯炼金赛道第二名，指引方向，指引前路\n\n" +
                        "生命值1，无法被攻击，移动时不会惊醒睡着的怪物，移速1.5，视野5\n" +
                        "微光向导和玩家共享视野，生成后会自动寻找并前往离出口最近的路径，\n" +
                        "遇到怪物挡路会将怪物牵拉至身后并使它陷入沉睡，到达出口后死亡并点亮出口房间的视野。" )));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.HIGHTWAND_7), ("烈阳法杖"),
                ("全新法杖，烈阳法杖!\n\n" +
                        "惊鸿杯法杖赛道第一名，以阳光照亮黑暗，以智慧引导光明。\n\n" +
                        "第一个尝试把太阳带入地下的人没有想到，死灵与恶魔其实也没有那么害怕虚假的太阳。\n" +
                        "\n" +
                        "在指定地点释放一个烈阳光球，你会同步光球周围7*7的圆形视野\n" +
                        "光球每回合会对周围3*3范围内的所有敌人造成2-8（+2/+4）点伤害，每额外扩散一格则伤害减少25%，光球的伤害范围最高为7*7的圆\n" +
                        "光球本身没有碰撞体积，进入光球内的敌人受到其他所有光球的伤害增加25%\n" +
                        "光球会持续3+法杖等级*0.3回合（向下取整），若未主动驱散光球其持续回合即将耗尽而法杖仍有充能时，会自动消耗1充能为该光球追加3回合的持续时间\n" +
                        "\n" +
                        "战法效果为刷新所有光球的持续时间" )));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.GRILLED_FISH), ("烤鲱鱼"),
                ("全新武器，烤鲱鱼!\n\n" +
                        "惊鸿杯武器赛道第一名，烤的是鲱鱼，吃的是武器。\n\n" +
                        "4阶16力，4-25，成长1-5\n" +
                        "焦香四溢，滋滋冒油，热气腾腾。你很确定这把武器不能当成普通的烤鲱鱼看待，但它看起来很香。\n" +
                        "你可以食用这条烤鲱鱼3次，每次恢复50饱食度，食用次数耗尽后变为鱼骨。\n" +
                        "你只能食用鉴定过的、无诅咒的烤鲱鱼。烤鲱鱼的每一级升级都会为其增加1次食用次数。\n" +
                        "附属武器：鱼骨\n" +
                        "3阶14力，3-15，成长1-3\n" +
                        "攻击对敌人造成伤害值50%的流血。\n" +
                        "你最终没能经受住饥饿的考验。好在它真的很香。" )));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
        changes.hardlight(Window.GREEN_COLOR);
        changeInfos.add(changes);

        Image xs =new PeachGodStateSprite();
        xs.scale.set(PixelScene.align(0.8f));
        changes.addButton(new ChangeButton(xs, ("桃源祈愿"),
                ("【限时活动--桃源祈愿·十连必得】-活动时间：3.1-3.31\n\n" +
                        "地牢深处将随机生成 「桃源祈愿间」55%概率 \n" +
                        "（13挑战以下触发【不包含13挑】）\n" +
                        "祈愿规则：\n" +
                        "单次投掷 50钴币\n" +
                        "保底机制 ：\n\n" +
                        "【小保底】\n" +
                        "每10次祈愿雕像必出 1 次罕见奖励 \n" +
                        "\n【大保底】--【可跨存档】" +
                        "累计40抽且未出现传说及以上，\n下次必出传说以上奖励\n"),
                        ("奖励类型概率公示【以单抽标准】：\n\n"+
                        "常规【40%】 稀有【35%】 \n\n" +
                        "罕见【20%】 传说【4%】 神话【1%】\n\n" +
                        "10连概率规则：\n" +
                        "小保底必出概率：\n" +
                        "罕见70% 传说28% 神话2%\n" +
                        "则9+1【必出罕见以上】\n" +
                        "累计40抽还未出现传说的10抽：\n" +
                        "大保底10连必出概率：\n\n" +
                        "罕见70% 传说28% 神话2%\n" +
                        "传说95% 神话5%")));

        changes.addButton(new ChangeButton(Icons.get(Icons.BADGES),("加密徽章新增两个"),
                ("新增更多加密徽章，欢迎前去探索")));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
        changes.hardlight(CharSprite.WARNING);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.ICEGOLD), ("钴币掉落调整"),
                (
                        "_-_ 常规获取方法掉落调整：\n" +
                        "_-_ （（玩家等级+当前楼层）/5）x（1+挑战数量/3））\n\n" +
                        "_-_ 限时双倍掉落：\n" +
                        "_-_ 2025.3.7-2025.3.31 钴币全局双倍掉落")));

        changes.addButton(new ChangeButton(new GhostSprite(),("悲伤幽灵调整"),
                //50%:+0, 20%:+1, 15%:+2, 12%:+3 3% +4
                ("悲伤幽灵武器品质概率调整：\n\n"+
                        "_-_ +0 武器：50%-原50%\n" +
                        "_-_ +1 武器：20%-原30%\n" +
                        "_-_ +2 武器：15%-原12%\n" +
                        "_-_ +3 武器：12%-原06%\n" +
                        "_-_ +4 武器：03%-原02%")));

        changes.addButton(new ChangeButton(Icons.get(Icons.DISPLAY), ("UI改动"),
                (       "_-_ 挑战界面优化，挑战数量动态显示为一个进度条\n" +
                        "_-_ 复制种子界面优化，可显示具体挑战\n" +
                        "_-_ 超过9999钴币的显示为科学计数法")));

        changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
                "0.8.3.0-Bug修复列表1:\n\n" +
                        ("_-_ 修复卡戎可能会出售已获得的容器\n" +
                                "_-_ 修复法师法杖回收天赋异常\n" +
                                "_-_ 修复普果，异果转阶段判断方式错误导致无敌\n" +
                                "_-_ 修复法师天赋失效问题\n" +
                                "_-_ 修复决斗家天赋失效\n" +
                                "_-_ 修复改动界面问题\n" +
                                "_-_ 修复拟态王boss层地图错误\n" +
                                "_-_ 修复肃杀文本错误\n" +
                                "_-_ 修复普果异常无敌"),
                ("0.8.3.0-Bug修复列表2:\n\n" +
                        "_-_ 修复0层法伊娜对话异常\n" +
                        "_-_ 修复金蝶任务文本异常\n" +
                        "_-_ 修复甲鱼越阶段斩杀\n" +
                        "_-_ 修复染血金币终端文本错误\n" +
                        "_-_ 修复法师灌注数值显示错误\n" +
                        "_-_ 修复微光导向卡死bug\n" +
                        "_-_ 修复普果，血量显示不匹配\n" +
                        "_-_ 修复问题描述：异果，血量不匹配\n" +
                        "_-_ 修复BossRush,普果，无法转阶段"),
                ("0.8.3.0-Bug修复列表3:\n\n" +
                        "_-_ 修复火龙无限二阶段，修复火龙层跨存档地形异常\n" +
                        "_-_ 修复甲鱼宠物导致的蜜蜂索敌异常\n" +
                        "_-_ 修复烈阳法杖的越界异常\n" +
                        "_-_ 修复神器充能对锐刻五代无效的效果\n" +
                        "_-_ 修复英雄极端情况下透视问题\n" +
                        "_-_ 修复孤城挑战下生成的怪物初始未全部苏醒的异常\n" +
                        "_-_ 修复金蝶20层boss随机到小刻时上下楼梯无法使用"),
                ("0.8.3.0-Bug修复列表4:\n\n" +
                        "_-_ 修复部分金蝶文案异常\n" +
                        "_-_ 部分缺失文本修复\n" +
                        "_-_ 修复灯火异常清理逻辑\n" +
                        "_-_ 修复饰品投影的计时器问题\n" +
                        "_-_ 修复迷你太阳的视野越界问题\n" +
                        "_-_ 修复果子卡死问题\n" +
                        "_-_ 修复泡泡残留新年奖励逻辑\n"+
                        "_-_ 修复部分武器无法直接参与炼金\n" +
                        "_-_ 调整0层武器固定生成为T2\n" +
                        "_-_ 优化部分金蝶文本\n" +
                        "_-_ 修复金蝶重复饰品的问题\n" +
                        "_-_ 指南书页异常位置修复")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.DG3), "归溯钥剑",
                "现在获得钥匙的经验相比之前翻倍，并且成长调整为1-5。\n\n" +
                        "并且在16层必定出现钥匙剑在商店,如果完成了_远古开拓者_成就，\n\n" +
                        "则钥匙剑在16层必定出现保底+3,并单独打5折左右。"));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.DG12), ("开发者模式工具：升降器"),
                ("现在升降器支持任意楼层传送，且可以传送到0层。")));

        changes.addButton(new ChangeButton(Icons.get(Icons.STAIRS), ("全局房间优化"),
                ("现在入口房间不会再被其他房间合并，独立成一个单独的房间。")));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "nerfs"), false, null);
        changes.hardlight(Window.R_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.DIEDCROSSBOW), ("重型弩炮"),
                ("修复弩炮的子弹伤害错误的为武器自身的伤害。")));

        Image issxsaxs =new ShopGuardDead.ShopGuardianRedSprite();
        issxsaxs.scale.set(PixelScene.align(1f));
        changes.addButton(new ChangeButton(issxsaxs, ("商店抢劫调整"),
                "现在商店抢劫后，只能选择一个层的商店进行全部领取。\n\n" +
                        "其他层的商店将会不再生成，且即便是_已经生成的商店也自动移除所有东西_。"));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.CHALLANEESICON_14), ("轻装上阵挑战下线"),
                ("下线此挑战。")));

        changes = new ChangeInfo("v0.8.3.0中测验收通过，予以更新", true, null);
        changes.hardlight(Window.CYELLOW);
        changeInfos.add(changes);

        changes = new ChangeInfo("2025 2-28-->3-07", true, null);
        changes.hardlight(Window.CPINK);
        changeInfos.add(changes);
    }

    public static void add_V0822_Changes(ArrayList<ChangeInfo> changeInfos ) {

        ChangeInfo changes = new ChangeInfo("v0.8.2.1-2", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
        changes.hardlight(Window.GREEN_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new GhostSprite(), ("悲伤幽灵任务优化"),
                ("现在悲伤幽灵的任务怪物会显示血条以方便玩家查看目标怪物，并且在任务完成后自动消失。")));

        Image s =new CrivusStarFruitsSprite();
        s.scale.set(PixelScene.align(0.8f));
        changes.addButton(new ChangeButton(s, ("Boss优化:克里璃斯之果"),
                "异果调整说明：\n" +
                        "1.第二阶段血量调整为200血\n" +
                        "2.第三阶段的盾不再消失，回复到93血以上后周期性对敌人造成伤害\n" +
                        "异果奖励追加：\n" +
                        "极速药水x1 圣愈秘药x1 强能晶柱x1"));

        Image xs =new RivalSprite();
        xs.scale.set(PixelScene.align(0.8f));
        changes.addButton(new ChangeButton(xs, ("Boss优化:暗影"),
                "1. 半血以下优先使用投掷武器，弹药耗尽后逼近玩家。  \n" +
                        "2. 半血以上优先用法杖攻击，充能耗尽后强制近战。  \n" +
                        "3. 有远程手段时，距离过近会主动远离保持射程。  \n" +
                        "4. 无弹药时不再卡死，直接切换近战攻击。  \n" +
                        "5. 修复了Boss在无弹药时游戏死循环的问题。"));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
        changes.hardlight(CharSprite.WARNING);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
                "0.8.2.1-2:\n\n" +
                        "1.果子系列boss遇到完全体会卡关【严重游戏阻断问题】\n" +
                        "2.悲伤幽灵的任务BGM缺失【一般问题】\n" +
                        "3.BossRush的一些文案修正【一般问题】\n" +
                        "4.BossRush的Hell难度下4层血红史莱姆数值异常【一般问题】\n" +
                        "5.修复暗影的隐藏楼层异常\n" +
                        "6.部分武器应该可参与炼金，但在之前无法直接放入"));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "nerfs"), false, null);
        changes.hardlight(Window.R_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.WRALIPS), ("暗金宝石护符"),
                        ("_-_ 经验获取调整：40/20 → 25/20（低于5级/高于5级）\n" +
                        "_-_ 冷却优化：40-等级 → 40-1.65*等级\n" +
                        "_-_ 充能公式调整：\n90基础 → 120基础，3倍 → 4倍等级系数\n" +
                        "_-_ 诅咒伤害降低：4-6倍 → 2-4倍等级\n" +
                        "_-_ 升级条件：修正双倍升级的异常\n" +
                        "_-_ 暗杀消耗保持6充能不变" )));

        changes.addButton(new ChangeButton(new QuestionSprite(), ("问号君暂时下线"),
                (
                        "问号君重制了自己的NPC代码，但有着许多Bug, 其中还有严重导致游戏崩溃的BUG，" +
                                "\n\n为此暂时下线，暂时不再0层生成，请等待后续版本更新。")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.HIGHTWAND_1), ("毒液侵染法杖"),
                ("为BOSS添加不在毒气内则累积毒素清零\n" +
                        "气体量由150 +50*lvl削弱至 50+10*lvl\n" +
                        "修复带有气体免疫的生物仍然会受到毒杖效果的bug")));

        changes = new ChangeInfo("v0.8.2.2中测验收通过，予以更新", true, null);
        changes.hardlight(Window.CYELLOW);
        changeInfos.add(changes);

        changes = new ChangeInfo("2025-2-14", true, null);
        changes.hardlight(Window.CPINK);
        changeInfos.add(changes);
    }

    public static void add_V0820_Changes(ArrayList<ChangeInfo> changeInfos ) {

        ChangeInfo changes = new ChangeInfo("v0.8.2.0", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes = new ChangeInfo("预载", false, null);
        changes.hardlight(Window.CBLACK);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.EMOTIONALAGGREGATION), ("藏品系统V0.1"),
                ("为后续困难模式打下良好的基础，已可在开发者模式的杂项生成器进行试玩")));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
        changes.hardlight(Window.GREEN_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(Icons.get(Icons.NEWS), ("新兑换码：MLPD-YX4YearsBirthday"),
                ("进入游戏通过落白商店 或者 游戏菜单 获取奖励，祝各位元宵快乐！")));

        changes.addButton(new ChangeButton(Icons.get(Icons.PREFS), ("杂项改动"),
                ("1.素材系统初始化，本次元宵和魔绫时间冲突，开启魔绫主题是魔绫4周年效果，关闭则是元宵Logo效果。\n" +
                        "2.部分细节优化更新\n" +
                        "3.部分界面优化")));

        changes.addButton(new ChangeButton(new QuestionSprite(), ("问号君2.0"),
                (
                        "问号君重制了自己的NPC代码，有着许多有趣的新功能\n\n" +
                                "如果想去玩玩，去看看旅馆附近的湖边看看他吧。")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.BOSSRUSH_GOLD), ("BossRush3.0正式版"),
                (
                        "完全重做BossRush\n\n" +
                                "1.至多与20个Boss进行作战，每一次BR都是一次新的冒险\n" +
                                "2.幽妹固定4层生成，完成任务即可让幽妹一同行动\n" +
                                "3.商人领主与商人一同在12，18，22，24，32，40层出现，可在这里购买补给品\n" +
                                "4.大部分Boss已经获得加强，请查看右侧的星级图标获取详情\n" +
                                "5.卡戎现在可以使用金券，补给层大调整，调整请查看右侧的楼层图标获取详情\n" +
                                "6.BossRush酸液体的生成概率调整为5%，爆炸体替换为完全体\n" +
                                "7.BossRush精英现在只有：鬼磷，索敌，天佑，烈焰")));

        changes.addButton(new ChangeButton(Icons.get(Icons.TALENT), ("BossRush3.0-Boss调整一览表"),
                ("随机音乐【已实装】\n" +
                        "BossRush的Boss音乐完全随机，除暗影，Yog-Zot，拟态王，矮人将军\n" +
                        "\n" +
                        "水中祝福 & 污泥浊水 & 支离破碎【已实装】\n" +
                        "不再有效\n" +
                        "饮用水灵药剂，转换为英雄当前血量的一半奥术护盾。【已实装】\n" +
                        "\n"),
                        ("金券商店【已实装】\n" +
                        "12，18，22，24，32，40加入商人领主\n" +
                        "\n" +
                        "液金刷金券【已实装】\n" +
                        "不再有效，必须打包购买。且至多10个，后续购买液金将不再给予金券。\n" +
                        "\n" +
                        "暗影调整【已实装】\n" +
                        "暗影在每次切换地图后，都会召唤2/3/4/5区的敌人前来助阵\n" +
                        "数量：2-6个 随机\n" +
                        "平衡：5区的敌人闪避减半，血量为以前的75%\n" +
                        "暗影抗性追加：酸蚀，寒冷，霜火\n" +
                        "暗影召唤的敌人追加抗性：酸蚀，寒冷\n" +
                        "暗影最终法杖替换为：毒杖【包括金蝶】\n" +
                        "暗影最终掉落法杖替换为随机法杖，等级0-2级【包括金蝶】\n" +
                        "\n" +
                        "绿野调整【已实装】\n" +
                        "调整到19层" +
                        "\n怪组：随机史莱姆，冰魔像，吸血蝙蝠\n" +
                        "血量：450\n" +
                        "\n"),
                        ("始祖食人鱼【已实装】\n" +
                        "在21层，\n" +
                        "怪组为：幻影食人鱼，食人鱼使徒，糯米老鼠\n" +
                        "此层为奖励关，可以挖掘暗金在下层兑换更多金券，\n" +
                        "小心，这里陷阱比较多，而且食人鱼不会让你走的潇洒的！\n" +
                        "奖励关说明：未开启Boss战时，可以相当于常规楼层\n" +
                        "\n" +
                        "浊焰魔女【已实装】\n" +
                        "调整到25层，由于莲娜自身就是成长Boss,\n" +
                        "这里不再加强，但对于吃塔激光的，\n" +
                        "会有更加严重的惩罚\n" +
                        "其赋予的效果与矮人污泥浊水的效果完全一致\n" +
                        "抗性追加：酸蚀，寒冷，眩晕，普通火焰（莲娜能免疫火不是很正常吗（）\n" +
                        "\n" +
                        "DM300调整【已实装】\n" +
                        "血量：850\n" +
                        "防御区间：7-10\n" +
                        "需要摧毁全部塔\n" +
                        "伤害区间调整：25-50\n" +
                        "所有技能全部是绝命头目挑战（无论有没有开）\n" +
                        "额外奖励：龙之泪x1\n" +
                        "\n"),
                        ("冰雪魔女【已实装】\n" +
                        "调整到27层，数值有所调整\n" +
                        "伤害区间：25-46【加强】\n" +
                        "期命中值：50【加强】\n" +
                        "闪避值：20【加强】\n" +
                        "血量：1000【加强】\n" +
                        "物理防御：4-8【不变】\n" +
                        "\n"),
                        ("冰雪魔女晶塔调整\n" +
                        "1.塔的血量提升到90，原先为45\n" +
                        "2.增加新塔：\n" +
                        "幻想结界水晶-暴食\n" +
                        "攻击模式：向目标地点发射一条射线，下回合更新目标地点，从上次命中地点发射一条射线。给目标造成磷火效果\n" +
                        "\n" +
                        "幻想结界水晶-贪欲\n" +
                        "攻击模式：向目标地点发射一条射线，下回合更新目标地点，从上次命中地点发射一条射线。给目标造成降级效果\n" +
                        "\n" +
                        "幻想结界水晶-咒灵\n" +
                        "攻击模式：向目标地点发射一条射线，下回合更新目标地点，从上次命中地点发射一条射线。给目标造成定命效果【在冰雪魔女本体死亡后解除】\n" +
                        "\n" +
                        "3.抗性追加：冻结，霜火\n" +
                        "\n"),
                        ("DM720\n" +
                        "调整到27层，数值有所调整\n" +
                        "血量：720\n" +
                        "伤害区间：24-36\n" +
                        "命中值：35\n" +
                        "所有技能和强化全部是绝命头目挑战（无论有没有开）\n" +
                        "\n"),
                        ("血月长老\n" +
                        "1.获得天佑精英\n" +
                        "2.血量：120（原：180）\n" +
                        "3.伤害区间：15-50\n" +
                        "所有技能全部是绝命头目挑战（无论有没有开）\n" +
                        "\n" +
                        "萨卡班甲鱼【已实装】\n" +
                        "调整到31层，数值有所调整\n" +
                        "1.血量：648（原480)\n" +
                        "2.闪避：20（原10)\n" +
                        "\n" +
                        "死亡激光\n" +
                        "伤害提升至：50-70\n" +
                        "抗性追加：失明，眩晕，幻惑\n" +
                        "\n" +
                        "修复Bug:\n" +
                        "1.两个随从召唤后醒着，在所有模式生效\n" +
                        "2.两个随从命中率少加一个0，现为5，应为50，在所有模式生效\n" +
                        "\n"),
                        ("矮人将军【已实装】\n" +
                        "1.额外召唤一次军旗\n" +
                        "2.添加了阶段控制，现在不可能被秒杀（全模式生效）\n" +
                        "\n" +
                        "矮人武将【已实装】\n" +
                        "1.怪组调整为：矮人炮手，美杜莎，矮人武僧\n" +
                        "2.添加了阶段控制，现在不可能被秒杀（全模式生效）\n" +
                        "\n" +
                        "Yog\n" +
                        "1.全部为绝命头目的加成\n" +
                        "\n" +
                        "磷焰拳头\n" +
                        "1.激光伤害提升至2倍\n" +
                        "\n" +
                        "Yog-Zot【已实装】\n" +
                        "1.最终阶段追加额外敌人\n" +
                        "2.如果有孤城，则全部敌人获得孤城印记")));

        Image dragonSprite = new FireDragonSprite();
        dragonSprite.scale.set(PixelScene.align(0.72f));
        changes.addButton(new ChangeButton(dragonSprite, ("熔岩火龙-BR模式"),
                ("掉落调整：\n" +
                        "BR中火龙自身不再掉落十字架，神器，戒指以及药水和龙之泪\n" +
                        "\n" +
                        "普通攻击物理伤害调整为：12-16【加强】\n" +
                        "命中值调整为：30【加强】\n" +
                        "闪避值调整为：12【加强】\n" +
                        "最大血量调整为：260【加强】\n" +
                        "物理防御调整为：4-5【加强】\n" +
                        "\n" +
                        "抗性增加：\n" +
                        "恐惧，冰冻【已完成】\n" +
                        "\n" +
                        "新增技能\n" +
                        "龙族威严（主动）：【已完成】\n" +
                        "在首次进入0血后立刻召唤5个元素并立刻吸收，回复160血。\n" +
                        "\n" +
                        "神秘仪式（被动）：【已完成】\n" +
                        "在失去敌人的状态下，立刻返回到祭坛上，并立刻至多吸收2个元素，此效果有20回合冷却\n" +
                        "\n" +
                        "龙族咒锁（主动）：【已完成】\n" +
                        "在发现敌人时，立刻将敌人拉出来，此效果有35回合冷却（无视任何地形）\n" +
                        "\n" +
                        "险峻地势（地形）：【已完成】\n" +
                        "治疗区域被上锁了，击败火龙后自行解除")));

        changes.addButton(new ChangeButton(Icons.get(Icons.STAIRS), ("BossRush3.0-补给层调整一览表"),
                ("T1 补给层（楼层：3, 5, 7, 8, 10）\n" +
                        "1 力量药水，2 治疗药水，1 随机宝箱，1 升级卷轴，1 经验药水，2 随机食物\n" +
                        "\n" +
                        "T2 补给层（楼层：12, 14, 15）\n" +
                        "1 经验药水，1 治疗药水，1 随机食物，\n" +
                        "1 随机武器，1 随机护甲，40% 概率生成 1 随机宝箱\n" +
                        "新增（BossRush 3.0）\n：1 升级卷轴，1 随机 2 阶投掷物品，1 嬗变卷轴\n" +
                        "\n" +
                        "T2+ 补给层（楼层：19, 20）\n" +
                        "1 经验药水，1 治疗药水，1 随机食物，1 随机武器，\n" +
                        "1 随机护甲，1 升级卷轴，40% 概率生成 1 随机宝箱\n" +
                        "新增（BossRush 3.0）\n：1 随机 3 阶投掷物品，1 嬗变卷轴，1 随机药水\n" +
                        "\n" +
                        "T3 补给层（楼层：22, 25）\n" +
                        "1 升级卷轴，1 力量药水，\n" +
                        "1 治疗药水，1 随机食物，1 随机武器\n" +
                        "\n" +
                        "T3+ 补给层（楼层：28, 30）\n" +
                        "1 力量药水，1 治疗药水，1 随机食物，\n" +
                        "1 随机武器，20% 概率生成 1 随机宝箱，1 随机护甲\n" +
                        "新增（BossRush 3.0）\n：1 随机魔药（水爆/淤泥/冰爆/炼狱/雷鸣），" +
                        "1 升级卷轴，2 嬗变卷轴，1 随机药水\n" +
                        "\n" +
                        "T4 补给层（楼层：32, 34, 36, 38, 40, 41）\n" +
                        "1 奥术护盾合剂，" +
                        "1-2 随机秘卷（决斗/虹卫/蜕变/灵爆/塞壬之歌）" +
                        "\n2 随机宝箱，1 随机药水，1 随机 4 阶投掷物品，1 嬗变卷轴")));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
        changes.hardlight(CharSprite.WARNING);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
                "0.8.2.0修正以下问题:\n\n" +
                        "1. 修复金蝶克隆严重异常 - 报告员：Archetto - 处理人：JDSALing\n" +
                        "2. 修复部分怪物数值异常，支离破碎在金蝶模式中不再生效 - 报告员：sks123456 - 处理人：JDSALing\n" +
                        "3. 优化嬗变，神器现在嬗变不再获得升级 - 报告员：xiaomeng - 处理人：JDSALing\n" +
                        "4. 修复哥布林祭司异常 - 报告员：Archetto - 处理人：JDSALing\n" +
                        "5. 修复老法杖充能异常 - 报告员：Archetto - 处理人：JDSALing\n" +
                        "6. 修复吸血鬼刀数值显示问题 - 报告员：Archetto - 处理人：JDSALing\n" +
                        "7. 修复愚人号层幻影食人鱼带有精英效果 - 报告员：xiaomeng - 处理人：JDSALing\n" +
                        "8. 修复无敌buff效果异常 - 报告员：sks123456 - 处理人：JDSALing\n" +
                        "9. 修复甲鱼地图生成问题 - 报告员：sks123456 - 处理人：JDSALing\n" +
                        "10. 修复异果boss战音乐错误 - 报告员：sks123456 - 处理人：JDSALing\n" +
                        "11. 修复br拟态王进入传送门会进入13-d子层 - 报告员：xiaomeng - 处理人：JDSALing\n" +
                        "12. 修复br模式npc生成异常 - 报告员：sks123456 - 处理人：JDSALing\n" +
                        "13. 修复br污水异常 - 报告员：sks123456 - 处理人：JDSALing\n" +
                        "14. 修复br冰女异常 - 报告员：Archetto - 处理人：JDSALing\n" +
                        "15. 修复br异果异常 - 报告员：Archetto - 处理人：JDSALing\n" +
                        "16. 修复变幻莫测部分楼层文本缺失 - 报告员：JDSALing - 处理人：JDSALing\n" +
                        "17. 修复肌肉合剂对部分武器没有效果 - 报告员：QinYue - 处理人：JDSALing"));

                changes = new ChangeInfo(Messages.get(ChangesScene.class, "nerfs"), false, null);
                changes.hardlight(Window.R_COLOR);
                changeInfos.add(changes);

                changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.DIEDCROSSBOW), ("重型弩炮"),
                ("弩炮技能更改：造成5*5范围的115%近战伤害，可以给飞镖伤害与附魔加成")));

                changes.addButton( new ChangeButton(new TalentIcon(Talent.STRONGMAN), "天赋调整",
                "角斗士天赋保持警惕退回为破碎的“连战热忱”\n\n" +
                        " 排山倒海数值调整：\n" +
                        " 获得0.137%/0.33%/0.5%伤害加成，上限50%\n" +
                        " 获得0.33%/0.66%/1%的精准加成，上限75%"));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.HIGHTWAND_1), ("毒液侵染法杖"),
                ("进入猩红毒雾的任何单位都会持续受到起始为_X点_的持续伤害并持续积累毒素，当同一单位累计受到的伤害达到_X*8_时将会在毒雾内持续失明，达到_X*20_时将会在毒雾内持续麻痹 \n\n" +
                        "服用_治疗药水_或_全面净化合剂_可清除毒素积累量。")));

        changes = new ChangeInfo("v0.8.2中测验收通过，予以更新", true, null);
        changes.hardlight(Window.CYELLOW);
        changeInfos.add(changes);

        changes = new ChangeInfo("2025-2-12", true, null);
        changes.hardlight(Window.CPINK);
        changeInfos.add(changes);


    }

    public static void add_V0810_Changes(ArrayList<ChangeInfo> changeInfos ) {

        ChangeInfo changes = new ChangeInfo("v0.8.1.0-1", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
        changes.hardlight(Window.GREEN_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.Dragon_Lei), ("新物品：龙之泪"),
                ("固定20露珠效果")));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
        changes.hardlight(CharSprite.WARNING);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.SCROLL_GOLD), ("金蝶模式V2.1-2.2"),
                (
                        "金蝶模式-V2.2\n\n" +
                        "1.修复金蝶克隆严重异常\n" +
                        "2.修复部分怪物数值异常，支离破碎在金蝶模式中不再生效\n" +
                        "3.优化嬗变回合，补偿无敌回合，且神器现在嬗变不再获得升级（嬗变次数有时，可无限嬗变）"),
                        "金蝶模式-V2.1\n\n" +
                        "1.追加更多怪物，详见日志金蝶特殊敌人列表"));

        changes.addButton(new ChangeButton(new HermitCrabSprite(), ("寄居蟹"),
                ("寄居蟹数值优化，现在破壳前0.95移速，破壳后2.0移速。\n\n" +
                        "破壳时，同时获得6回合极速+恐惧效果。伤害削弱")));

        changes.addButton(new ChangeButton(new ArtilleristSprite(), ("矮人炮手"),
                ("修正攻击力为1000%的严重异常")));

        changes.addButton(new ChangeButton(new GnollBlindSprite(), ("老年豺狼"),
                ("老年豺狼在第一层防御降为0，其他情况下为0-2。")));

        changes.addButton(new ChangeButton(Icons.get(Icons.PREFS), ("其他改动"),
                ("1.兑换码追加自动清理和NTP验证\n" +
                        "2.部分文案优化\n" +
                        "3.部分素材细节优化更新")));

        changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
                "0.8.1.0-1:\n\n" +
                        "-修复老魔杖的嬗变一些异常\n" +
                        "-优化饰品嬗变，超过6级的饰品现在也可嬗变\n" +
                        "-优化嬗变底层，文案优化\n" +
                        "-优化破碎神器升级底层，现在至多+10，老存档无法同步，遇到旧版本超过+10卡死异常请自行处理。"));

    }

    public static void add_V0808_Changes(ArrayList<ChangeInfo> changeInfos ) {

        ChangeInfo changes = new ChangeInfo("v0.8.0.8", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
        changes.hardlight(Window.GREEN_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.SCROLL_GOLD), ("金蝶模式V2.0"),
                ("完全重做，详见金蝶游戏内部说明")));

        changes.addButton(new ChangeButton(Icons.get(Icons.NEWS), ("新兑换码：MLPD-SnakeYearsOld"),
                ("进入游戏通过落白商店 或者 游戏菜单 获取奖励，祝各位新年快乐！")));

        changes.addButton(new ChangeButton(Icons.get(Icons.NEWS), ("新兑换码：MLPD-4YearsOld"),
                ("进入游戏通过落白商店 或者 游戏菜单 获取奖励，祝各位新年快乐！")));

        changes.addButton(new ChangeButton(Icons.get(Icons.BADGES),("新徽章加入"),
                ("一个隐藏徽章")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.CHALLANEESICON_17), ("挑战改进:孤城迷境"),
                ("挑战优化:\n" +
                        "1.0层翻新，奈亚子进入常规入口会有温馨提示\n" +
                        "2.孤城Bzmdr\n" +
                        "3.孤城可游玩特殊模式")));

        changes.addButton(new ChangeButton(new DreamSprite(), ("新NPC:晓梦加入"),
                ("风里雨里，0层等你")));

        changes.addButton(new ChangeButton(new RedNecromancerSprite(), ("BossRush2.4"),
                ("Boss现在不会获得成长精英效果。")));

        Image issxsaxs =new QliphothSprite();
        issxsaxs.scale.set(PixelScene.align(0.8f));
        changes.addButton(new ChangeButton(issxsaxs, ("果子系列Boss优化"),
                ("现在果子在不能攻击前不再被自动瞄准，且异果藤曼开局追加10回合失明")));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
        changes.hardlight(CharSprite.WARNING);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
                "0.8.0.8:\n\n" +
                        "1.修复决斗家转职副完全不充能的问题\n" +
                        "2.修复金蝶测试版的各种问题\n" +
                        "3.0层魔像现在不会传送\n" +
                        "4.其他小型崩溃Bug修正,这里不做过多描述"));
    }

    public static void add_V0805_Changes(ArrayList<ChangeInfo> changeInfos ) {

        ChangeInfo changes = new ChangeInfo("v0.8.0.5", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
        changes.hardlight(Window.GREEN_COLOR);
        changeInfos.add(changes);

        Image dragonSprite = new FireDragonSprite();
        dragonSprite.scale.set(PixelScene.align(0.72f));
        changes.addButton(new ChangeButton(dragonSprite, ("熔岩火龙-绝命模式"),
                ("调整熔岩火龙地图地形，使之更加刺激")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.SCROLL_GOLD), ("金蝶模式V1.5"),
                ("1.背包中的武器不再参与嬗变\n" +
                        "2.修复了一些潜在问题，优化了底层模块")));

        changes.addButton(new ChangeButton(Icons.get(Icons.NEWS), ("新兑换码：MLPD_XiaonianYei"),
                ("进入游戏通过落白商店 或者 游戏菜单 获取奖励，祝各位小年夜快乐！")));

        changes.addButton(new ChangeButton(new RedNecromancerSprite(), ("BossRush2.2"),
                ("第一大层Boss顺序发生了变化，现在是粘咕->普果->异果->天痕（困难模式），并且现在最大层不再是100。")));

        Image issxsaxs =new DimandKingSprite();
        issxsaxs.scale.set(PixelScene.align(0.8f));
        changes.addButton(new ChangeButton(issxsaxs, ("拟态之王2.0"),
                ("现在拟态之王因为实装音乐所以重新优化了一些阶段，并且迷宫不再是隐藏门" +
                        "尤其是第二阶段，同时绝命有所调整，请自行游戏内查看")));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
        changes.hardlight(CharSprite.WARNING);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
                "0.8.0.5:\n\n" +
                        "1.修复部分武器肌肉合剂不生效的异常\n" +
                        "2.修复苦痛刻痕使用祝福的十字架死亡的异常，以及可反复退出的问题\n" +
                        "3.修复英雄精英效果不生效的问题\n" +
                        "4.其他小型崩溃Bug修正,这里不做过多描述"));
    }

    public static void add_V0801_Changes(ArrayList<ChangeInfo> changeInfos ) {

        ChangeInfo changes = new ChangeInfo("v0.8.0.1-2", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
        changes.hardlight(Window.GREEN_COLOR);
        changeInfos.add(changes);

        Image dragonSprite = new FireDragonSprite();
        dragonSprite.scale.set(PixelScene.align(0.72f));
        changes.addButton(new ChangeButton(dragonSprite, ("熔岩火龙-绝命模式"),
                ("新增熔岩火龙绝命模式，以及修复一些buff不生效的异常")));

        changes.addButton(new ChangeButton(new RedNecromancerSprite(), ("BossRush2.1"),
                ("第一大层Boss顺序发生了变化，现在是普果->史莱姆王->异果，并且现在最大层不再是100。")));

        Image issxsaxs =new DimandKingSprite();
        issxsaxs.scale.set(PixelScene.align(0.8f));
        changes.addButton(new ChangeButton(issxsaxs, ("拟态之王2.0"),
                ("现在拟态之王因为实装音乐所以重新优化了一些阶段，" +
                        "尤其是第二阶段，同时绝命有所调整，请自行游戏内查看")));

        changes.addButton(new ChangeButton(Icons.get(Icons.AUDIO), ("新原创音乐：拟态之王-宝藏迷宫"),
                ("由作曲家'Tatsro'制作的原创Boss战BGM")));

        changes.addButton(new ChangeButton(Icons.get(Icons.AUDIO), ("新三方音乐：Titanic（泰坦之魂）"),
                ("泰坦之魂游戏原声，应用于熔岩火龙Boss战")));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
        changes.hardlight(CharSprite.WARNING);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.HIGHTWAND_1), ("法杖调整：毒液侵染法杖"),
                ("回退代码到稳定版本，但伤害数值下降至原有70%")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.LANTERNB), ("寂灭灯火V1.6"),
                ("1.灯油降价，从原来的四倍变成两倍！" +
                        "2.灯火自然扣减变慢4回合")));

        changes.addButton(new ChangeButton(Icons.get(Icons.DISPLAY), ("UI改动"),
                ("1.加密徽章现在可以看见自己的获得量/总量\n" +
                        "2.13挑徽章可以看见自己的最高记录挑战数量,但需要在新版本进行一次通过才能记录。\n" +
                        "3.现在特殊模式可显示对应图标和东西")));

        changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
                "0.8.0.1:\n\n" +
                        "1.修复子层物品掉落的闪退异常\n" +
                        "2.宝藏迷宫可适配在BossRush\n" +
                        "3.其他小型Bug修正,太多了这里不做过多描述"));

        changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
                "0.8.0.0:\n\n" +
                        "1.修复子层丢东西导致的小异常\n" +
                        "2.修复BossRush裂缝层地形异常问题\n" +
                        "3.修复BossRush模式部分楼层的异常问题\n" +
                        "4.修复玩家到达白墓碑和黑宝箱上方时会显示其中的物品的小异常\n" +
                        "5.修复部分音乐冲突导致的部分机型的严重卡顿\n" +
                        "6.修复火龙buff不生效以及无敌失效异常\n" +
                        "7.修复法伊娜拦路的问题\n" +
                        "8.修复玫瑰卷轴失效的问题\n" +
                        "9.修复子层的跳楼异常\n" +
                        "10.修复部分特效异常"));

    }

    public static void add_V0800M1_Changes(ArrayList<ChangeInfo> changeInfos ) {

        ChangeInfo changes = new ChangeInfo("v0.8.0.0-M1", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "middle"), false, null);
        changes.hardlight(Window.Pink_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new VampireSprite(), ("万圣古堡①测-中测"),
                ("万圣节古堡①测，中测群正在测试，敬请期待古堡正式版本！")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.HOLY_WATER), ("新物品：圣水瓶"),
                ("可以重创部分敌人，可以让部分敌人失效，每层生成1-3个圣水瓶")));

        changes.addButton(new ChangeButton(new GhostHalloweenSprite(), ("新怪物：鬼魂"),
                ("游荡在万圣大殿的鬼魂，虽然无法对敌人造成伤害，但可以造成精神摧残。")));

        changes.addButton(new ChangeButton(new PumkingGhostSprite(), ("新怪物：南瓜头鬼魂"),
                ("游荡在万圣大殿的鬼魂，虽然无法对敌人造成伤害，但可以造成精神摧残。")));

        changes.addButton(new ChangeButton(new ApprenticeWitchSprite(), ("新怪物：见习魔女"),
                ("她曾是某魔法学院的学生，因为太过顽皮，没能通过魔法考试，所以无法成为一名合格的法师。")));

        changes.addButton(new ChangeButton(new ButcherSprite(), ("新怪物：电锯狂人"),
                ("电锯狂人曾经也是一个冒险者，可现在由于各种原因，他已失去了神智。它徘徊这古堡周围，寻找一切可以被撕碎的东西。")));

        changes.addButton(new ChangeButton(new FrankensteinSprite(), ("新怪物：残梦魔偶"),
                ("游荡在万圣大殿的未完成人偶，据说制作它们的主人因为它们失败的外型和缓慢的速度最终抛弃了它们。它们已经陷入了癫狂之中，会寻找一切活着的生命，并撕碎他们的灵魂。")));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
        changes.hardlight(Window.GREEN_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new LuoWhiteSprite(), ("兑换码系统V2.0"),
                ("兑换码系统现在可在游戏界面中进入！")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.LANTERNB), ("寂灭灯火V1.5"),
                ("1.没入黑暗灯火扣减从原灯火扣减 + 2*挑战数量/5，改为固定+2，且仅在自动扣减中\n\n" +
                                "2.灯火最大值提升至72")));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
        changes.hardlight(CharSprite.WARNING);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
                "0.7.9.9-SP4版本:\n\n" +
                        "1.修复部分文本缺失\n" +
                        "2.修复哨位法杖异常\n" +
                        "3.优化纯晶元素4层AI\n" +
                        "4.火龙，莲娜，矮人武将阶段调整追加无敌"));

    }

    public static void add_V07993_Changes( ArrayList<ChangeInfo> changeInfos ) {

        ChangeInfo changes = new ChangeInfo("v0.7.9.9-SP3", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
        changes.hardlight(Window.GREEN_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(Icons.get(Icons.AUDIO), ("音乐优化"),
                ("音乐进行了一些压缩，保证音乐质量情况下减负安装包大小")));

        changes.addButton(new ChangeButton(Icons.get(Icons.PREFS), ("其他改动"),
                ("1.部分素材优化迭代\n" +
                        "2.部分文案优化\n" +
                        "3.部分素材细节优化更新")));

        changes.addButton(new ChangeButton(Icons.get(Icons.WARNING), ("错误报告V2登场"),
                ("错误报告v2界面全新来袭，感谢薄荷的界面制作")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.GOLDLANGGUN), ("新传奇武器：黄金长枪"),
                ("堤喀眷顾之人才能找到的武器，给予使用者无与伦比的祝福")));

        changes.addButton(new ChangeButton(new LuoWhiteSprite(), ("兑换码系统V1.0"),
                ("兑换码系统来袭，替代更新补偿")));


        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.LANTERNB), ("寂灭灯火V1.2"),
                (
                        "1.没入黑暗灯火扣减从全局双倍调为原灯火扣减 + 2*挑战数量/5，且仅在自动扣减中\n\n" +
                                "2.优化魔女的低语：束缚，现在会在效果结束后给予装备的净化，作为代价，装备会降一级")));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
        changes.hardlight(CharSprite.WARNING);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
                "0.7.9.9-SP3版本:\n\n" +
                        "1.修复部分文本缺失\n" +
                        "2.修复毒杖，火把的崩溃和无限回合异常"));

        changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.SAI,
                new ItemSprite.Glowing( Window.GDX_COLOR )), "附魔：审判",
                "暂时下架这个附魔"));

    }

    public static void add_V07990_Changes( ArrayList<ChangeInfo> changeInfos ) {

        ChangeInfo changes = new ChangeInfo("v0.7.9.9-SP1", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
        changes.hardlight(Window.GREEN_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.SEED_AIKELAIER), ("查种器V3登场"),
                ("查种器V3登场，更强大的查种，更快的搜索！\n\n" +
                        "1.基于JDK21引擎，查种速度大幅度提升\n" +
                        "2.强力搜查模式 现在登场\n" +
                        "3.修复查种部分界面异常Bug\n" +
                        "4.修复以字符串查种子的显示异常\n" +
                        "5.追加格式复原，不清楚格式可以点击这个")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.LANTERNB), ("寂灭灯火V1.1"),
                (
                        "1.新增自动装填模式\n" +
                        "2.修复寂灭循环的一些传送异常\n" +
                                "3.稀有怪生成率调成35%")));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
        changes.hardlight(CharSprite.WARNING);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(Icons.get(Icons.CHALLENGE_ON), ("药水癔症"),
                ("药水癔症已经进行了一定的改进，详见挑战说明。")));

        changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
                "0.7.9.9版本:\n\n" +
                        "1.修复中部群反馈的一些bug\n" +
                        "2.其他闪退bug修正"));

        changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.SAI,
                new ItemSprite.Glowing( Window.GDX_COLOR )), "附魔：审判",
                "修复审判带来的一些异常问题"));

        changes = new ChangeInfo("v0.7.9.9", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
        changes.hardlight(Window.GREEN_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new GudaziSprite(),("新NPC:古达子"),
                ("来自远方的旅客，来到雪景小镇篝火这里。")));

        changes.addButton(new ChangeButton(new ZakoSprite(),("新NPC:喵音·椛"),
                ("团宠，别惹她生气")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.WHITE_BAST), ("新武器：白浪刺剑"),
                ("我的灯将净化邪恶！ 我的剑将劈开海潮！ 我的眼将找出真相！ 我的心会作出判决。")));

        changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.SAI,
                new ItemSprite.Glowing( Window.GDX_COLOR )), "新附魔：审判",
                "这个附魔会使审判之力从武器中释放而出，能够至多标记一个敌人，并在这个敌人死亡时将其最大生命值的若干转为一次对所有可见敌人进行一次同等的物理伤害。"));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.CHALLANEESICON_15), ("新挑战:寂灭灯火"),
                ("灯火现在正式成为挑战，具体查阅挑战说明。")));

        Image s = new QliphothSprite();
        s.scale.set(PixelScene.align(0.74f));
        changes.addButton(new ChangeButton(s, ("新Boss：克里弗斯之果2代"),
                ("它们编织成一张大网，将所有不慎跌落谷底的生命体供给给中间那粒鲜红的果实。果子重做，祝各位玩的愉快")));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
        changes.hardlight(CharSprite.WARNING);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(Icons.get(Icons.PREFS), ("其他改动"),
                ("1.部分素材优化迭代\n" +
                        "2.部分文案优化\n" +
                        "3.部分细节优化更新\n" +
                        "4.现在饰品可以在开发者模式中升级")));

        changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
                "0.7.9.8版本:\n\n" +
                        "1.修复部分炼金合成异常\n" +
                        "2.修复中部群反馈的一些bug\n" +
                        "3.其他bug修正"));
    }

    public static void add_V078_90_Changes( ArrayList<ChangeInfo> changeInfos ) {
        ChangeInfo changes = new ChangeInfo("v0.7.8-v0.7.9", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
        changes.hardlight(Window.GREEN_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(Icons.get(Icons.CHANGES), ("底层迭代"),
                ("魔绫像素地牢现在迭代至2.5.0破碎底层")));

        changes.addButton(new ChangeButton(Icons.get(Icons.CATALOG), ("平衡案3.1 上半部分"),
                ("实现了平衡案3.1上半部分，具体如下：\n\n" +
                        "【武器调整】：\n\n" +
                        "1.月刃：攻击延迟改为0.45回合，变为4阶武器\n\n" +
                        "2.吸血鬼刀：面板提升至3-15，成长改为2-2\n\n" +
                        "3.碧灰：改为5阶武器，攻击延迟改为0.40\n\n" +
                        "4.荆棘：去除所谓的低等级负面，去除加距离，附加恐惧回合变为等同等级的回合数，诅咒棱晶不可对其使用，触发惩罚的等级改为11级，生命树出现概率改为35%\n\n" +
                        "5.冰鱼：配方改为:冰合剂+烤鲱鱼+注魔棱晶。双鱼成长改为3-7，攻击延迟改为1\n\n" +
                        "6.涤罪：延迟改为1.76，成长改为1-6，基础面板改2-25，每杀一个敌人减0.0067的延迟，计数上限为100，每杀2个怪成长一点上下限，计数上限为26\n\n" +
                        "7.糯米大剑:基础属性改为大剑属性\n\n"),
                        ("【挑战调整】：\n\n" +
                        "1.灯火：在开启黑暗挑战的情况下，灯火消耗速度翻倍(已处理，目前为x2)\n\n" +
                        "2.药水癔症：负面效果的触发概率变为95%，除大饼外无正面效果。"),
                        "【怪物调整】：\n\n" +
                        "1.甲鱼:减少第一阶段10护盾，攻击上升7\n\n" +
                        "2.火魔女的属性决定于最大深度而不是抢劫深度\n\n" +
                        "3.棕色蝙蝠：闪避变为与正常二区怪物一致\n\n" +
                        "4.黄昏史莱姆：25%概率上燃烧\n\n" +
                        "5.冰老鼠：护甲改为1-3\n\n" +
                        "6.火苍蝇：护甲改为1-4\n\n" +
                        "7.重甲豺狼：血量改为80，亡语护盾改为75%血上线"));

        changes = new ChangeInfo("预载", false, null);
        changes.hardlight(Window.CBLACK);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new ApprenticeWitchSprite(), ("新怪物:见习魔女"),
                ("魔法学院的劣等生，见习中的魔女。\n\n该数据已预载，将在万圣后启用。")));

        changes.addButton(new ChangeButton(new FrankensteinSprite(), ("新怪物:残梦魔偶"),
                ("它在寻找它的创造者，以及，撕碎所有拥有生命的气息。\n\n该数据已预载，将在万圣后启用。")));

        changes.addButton(new ChangeButton(new GhostHalloweenSprite(), ("新怪物:鬼魂"),
                ("令人捉摸不透的亡魂……。\n\n该数据已预载，将在万圣后启用。")));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
        changes.hardlight(CharSprite.WARNING);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(Icons.get(Icons.PREFS), ("其他改动"),
                ("1.部分素材优化迭代\n" +
                        "2.部分文案优化\n" +
                        "3.部分细节优化更新")));

        changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
                "0.7.9.0版本:\n\n" +
                        "1.修复部分炼金合成异常\n" +
                        "2.修复中部群反馈的一些bug\n" +
                        "3.其他bug修正\n"));

//        changes = new ChangeInfo(Messages.get(ChangesScene.class, "nerfs"), false, null);
//        changes.hardlight(Window.R_COLOR);
//        changeInfos.add(changes);
//
//        changes.addButton(new ChangeButton(new ShopkKingSprite(), ("商店Boss优化"),
//                ("不再出现敌法精英效果加成，并且激素涌动有效时长减少")));
//
//        changes.addButton(new ChangeButton(Icons.get(Icons.PREFS), ("其他改动"),
//                ("查找器移除，日后作为网页版上线。若仍要查询种子，请到开发者模式使用物品查询器观看")));


    }

    public static void add_V0770_Changes( ArrayList<ChangeInfo> changeInfos ) {
        ChangeInfo changes = new ChangeInfo("v0.7.7.0", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
        changes.hardlight(Window.GREEN_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.RedBloodMoon), ("血剑回归"),
                ("猩红血剑回归，并且成为传奇武器！！！")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.WORN_SHORTSWORD), ("战士天赋全面升级"),
                ("战士天赋T1-T3都进行了一些相应改进，欢迎各位前来游玩")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.DIFFCULTBOOT), ("难度系统预载"),
                ("难度系统底层预载成功，8月不见不散！")));

        Image i = new Image("sprites/boss/fireDragon.png", 0, 0, 24, 24);
        i.scale.set(PixelScene.align(0.74f));
        changes.addButton(new ChangeButton(i, ("熔岩火龙-完整版"),
                ("丛林暴乱的真相，追加扫尾和龙车，血量略微提升，限制调整为11层前\n\n" +
                        "奖励调整：固定一个_祝福十字架_+_20滴露珠_+—_一个神器_，其他奖励与现在一致")));

        changes.addButton(new ChangeButton(Icons.get(Icons.SKULL), ("暗影AI加强"),
                ("暗影现在更加聪明，你能否击败“你”?")));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
        changes.hardlight(CharSprite.WARNING);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(Icons.get(Icons.PREFS),("其他改动"),
                ("1.部分素材优化迭代\n" +
                        "2.部分文案优化\n" +
                        "3.部分细节优化更新")));

        changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
                "0.7.7.0版本:\n\n" +
                        "1.修复部分炼金合成异常\n" +
                        "2.修复中部群反馈的一些bug\n" +
                        "3.其他bug修正\n"));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "nerfs"), false, null);
        changes.hardlight(Window.R_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new ShopkKingSprite(), ("商店Boss优化"),
                ("不再出现敌法精英效果加成，并且激素涌动有效时长减少")));

        changes.addButton(new ChangeButton(Icons.get(Icons.PREFS),("其他改动"),
                ("查找器移除，日后作为网页版上线。若仍要查询种子，请到开发者模式使用物品查询器观看")));

    }

    public static void add_V0765_Changes( ArrayList<ChangeInfo> changeInfos ) {
        ChangeInfo changes = new ChangeInfo("v0.7.6.5", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
        changes.hardlight(Window.GREEN_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.SCROLL_GOLD), ("百变之旅1.0"),
                ("全新特殊模式：百变之旅登场")));

        changes.addButton(new ChangeButton(Icons.get(Icons.CHANGES), ("重大更新"),
                ("魔绫现已更新底层到破碎V2.4.2版本!\n\n" +
                        "全新饰品，炼金系统等你来玩")));

        changes.addButton(new ChangeButton(Icons.get(Icons.DISPLAY), ("UI优化改动"),
                ("魔绫已经对UI优化改动进行调整，欢迎前来体验")));

        changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.MOONDAILY,
                new ItemSprite.Glowing( Window.BLUE_COLOR )), "新附魔：流逝",
                "这个附魔会让视野中的敌人的时间回溯到较早的时候且带有一次和冲击波法杖效果相似的一次冲击，并且对敌人可造成_心魔损伤:我方_元素损伤效果。"));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
        changes.hardlight(CharSprite.WARNING);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
                "0.7.6,5版本:\n\n" +
                        "修复了一堆问题，具体可在Q群中查阅魔绫错误日志汇总"));

        changes.addButton(new ChangeButton(Icons.get(Icons.CHALLENGE_ON), ("挑战调整"),
                ("基因突变 和 变幻莫测 挑战优化调整")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.WORN_SHORTSWORD), ("战士天赋调整"),
                ("战士天赋的再生不再可以回血，修复战士靶心瞄准的瞬移异常")));

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
