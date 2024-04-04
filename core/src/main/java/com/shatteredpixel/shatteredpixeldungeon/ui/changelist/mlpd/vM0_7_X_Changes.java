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
import com.shatteredpixel.shatteredpixeldungeon.sprites.DeepSeaSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DimandKingSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DragonGirlBlueSprite;
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
import com.shatteredpixel.shatteredpixeldungeon.sprites.RedNecromancerSprite;
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
