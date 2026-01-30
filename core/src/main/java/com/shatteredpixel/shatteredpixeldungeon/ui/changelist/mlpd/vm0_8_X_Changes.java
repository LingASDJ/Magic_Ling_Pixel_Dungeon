package com.shatteredpixel.shatteredpixeldungeon.ui.changelist.mlpd;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.ShopGuardDead;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.gold.ArtilleristSprite;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.gold.GnollBlindSprite;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.gold.HermitCrabSprite;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.gold.TribemanOldSprite;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.lb.RivalSprite;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.NetIcons;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.ChangesScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ApprenticeWitchSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.BloodsSwarmSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ButcherSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CrabSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CrivusStarFruitsSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DM111Sprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DeadDogCerberusSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DeadEyeSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DimandKingSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DreamSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.FireDragonSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.FodderSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.FrankensteinSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GhostHalloweenSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GhostSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GhoulPlusSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GiantFlowerSlimeSprites;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GuardCapitalSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.HeroSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.HiroSprites;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.LuoWhiteSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MageHandSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MorpheusSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MyCoreHeartSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.NyarlathotepSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.PeachGodStateSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.PumkingGhostSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.QliphothSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.QuestionSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.RedNecromancerSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SRPDHBLRTT;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ShieldHuntsmanSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ShubNiggurathSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SkyDeadSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SpawnereEvilSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.TowerMachineSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.VampireSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.VeryColdRatSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.WarlockHeadSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.WhiteGirlSprites;
import com.shatteredpixel.shatteredpixeldungeon.sprites.YogSoulSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIcon;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.TalentIcon;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.ui.changelist.ChangeButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.changelist.ChangeInfo;
import com.watabou.noosa.Image;

import java.util.ArrayList;

public class vm0_8_X_Changes {


    public static void addAllChanges(ArrayList<ChangeInfo> changeInfos) {
        add_V0920_Changes(changeInfos);
        add_V0915_Changes(changeInfos);
        add_V0910_Changes(changeInfos);
        add_V0900_Changes(changeInfos);
        add_V0880_Changes(changeInfos);
        add_V0874_Changes(changeInfos);
        add_V0872_Changes(changeInfos);
        add_V0871_Changes(changeInfos);
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
    }

    public static void add_V0920_Changes(ArrayList<ChangeInfo> changeInfos ) {
        ChangeInfo changes = new ChangeInfo("v0.9.2.0", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
        changes.hardlight(Window.GREEN_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(Icons.get(Icons.NEWS), ("新兑换码：Pre-5YearsOld"),
                ("进入游戏通过落白商店 或者 游戏菜单 获取奖励！\n\n有效期：2026-2-12 22:00前")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.SKIN_11), ("新皮肤：晚宴邀请"),
                ("已可在时装商人处购买，售价1500钴币")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.OLDSUNSHADOW), ("新道具:往日投影"),
                ("水晶上面映射出各种可能性，你能否找到属于你的位面？")));

        changes.addButton(new ChangeButton(Icons.get(Icons.DISPLAY), ("材质包系统1.0"),
                ("材质包系统正式登场，目前预置了5个材质包，你也可以自行定制材质包。\n\n" +
                        "如要定制材质包，请查阅游戏新闻材质包置顶使用说明。")));

        changes.addButton(new ChangeButton(new HiroSprites(), ("新NPC:久住"),
                ("在时间静止的区域，她在那里静静的等待毁灭的降临。\n\n" +
                        "在9层下楼携带一种特殊的信物将有概率前往")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.BOMB_SWORD), ("新武器：炸弹匕首"),
                ("炸弹匕首重做归来，并实装了动画效果！\n\n这件武器会在击杀敌人时有概率获取一枚弹药，通过升级该武器可以提高概率，和解锁更加强大的炸弹。" )));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.WAND_HAND_CONTROL), ("新战法相关调整"),
                ("新增以下功能\n\n" +
                        "1.针对于部分特殊地图（异果，小游戏地图）将自动死亡并掉落物品在英雄脚下\n" +
                        "2.现在装配法杖的充能数看得见了\n" +
                        "3.装配法杖现在有右侧快捷键了\n" +
                        "4.当老魔杖耗尽时，将自动转为近战攻击，每次攻击都会给予英雄1回合敌意效果\n" +
                        "5.现在法师之手控制器检测到法师之手不存在时，贴图持续闪红光且快捷键直接变成【召唤】\n" +
                        "6.现在法师之手不会再寻找不存在的敌人\n" +
                        "7.现在法师之手装配老法杖时，近远智能使用，而不是冷却期间发呆\n" +
                        "8.魔力补偿天赋废弃\n" +
                        "9.魔力汲取天赋实装\n" +
                        "10.实现哨位适配，法师之手巡查AI，视野共享\n" +
                        "11.多样打击现在只需要英雄背包有老魔杖或法师之手有老魔杖即可触发")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.STORYBOOKS), ("新开发者工具:思维之书"),
                ("进入各种领袖的思维，去回忆那些和它们的决战。")));


        changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
        changes.hardlight(CharSprite.WARNING);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.SEAL), ("精巧纹章天赋调整"),
                ("精巧纹章天赋生效时，可对纹章使用驱邪卷轴；诅咒菱晶现在也可以对诅咒纹章提供一次永久升级。")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.SCROLL_GOLD), ("金蝶模式调整"),
                ("1.金蝶任务2：每大层至多因为猎杀金蝶怪物获得额外1次嬗变，五区累计总计5次嬗变\n" +
                        "2.修复抢劫可上楼导致的严重bug")));



        changes.addButton(new ChangeButton(new NyarlathotepSprite(), ("古堡奈亚调整"),
                ("奈亚子生成陷阱现在不包含塌方陷阱")));

        Image sss =new MyCoreHeartSprite();
        sss.scale.set(PixelScene.align(0.4f));
        changes.addButton(new ChangeButton(sss, ("古堡宇宙之心调整"),
                ("宇宙之心对于友方阵营将进行自动处决")));


        changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
                ("1.修复部分素材的渲染异常崩溃\n" +
                        "2.修复英雄存活读取的优先级异常崩溃\n" +
                        "3.修复风暴雷霆法杖的一些异常崩溃\n" +
                        "4.修复法师之手的传送异常崩溃，无限卡死修复\n" +
                        "5.修复法师之手瞬间指向异常"),
                ("6.修复法师之手无法充能\n" +
                        "7.修复属性查看器会导致英雄buff显示异常问题\n" +
                        "8.修复激流陷阱越界异常\n" +
                        "9.升降器现在至多降到5子层\n" +
                        "10.修复解离战法的攻击距离增加效果异常" ),

                ("11.修复老魔杖没有说明战法效果的文本\n" +
                        "12.少量文案错误修正\n" +
                        "13.血饮附魔异常修复，现在不能对中立与友方生物生效\n" +
                        "14.修复烟雾体索敌距离异常\n" +
                        "15.修复狱火附魔受奥术加成的一些异常"),

                ("16.修复拟态之王坠楼导致死档\n" +
                        "17.修复空间信标未判定锁定楼层的异常\n" +
                        "18.现在子层死亡的遗物背包均会在死亡地点生成\n" +
                        "19.修复龙血鳞片一般显示的文案数值异常\n" +
                        "20.修复夏渔雾溟对话时给予的饰品获取文本与实际获取的不相符"),

                ("21.修复破损纹章携带的部分附魔未正常生效的异常\n" +
                        "22.修复暗金宝石护符诅咒特效失效\n" +
                        "23.修复普通古神战中，孤城情况下，拳头在古神附近应有的无敌效果失效\n" +
                        "24.修复弩炮显示法伤的异常\n" +
                        "25.定身期间现在无敌 & 树痕护盾为0时，快捷栏按钮不会生效")

        ));

        changes.addButton(new ChangeButton(Icons.get(Icons.PREFS), ("杂项调整"),
                ("1.Mob更安全的边界检查\n" +
                        "2.萨卡班甲鱼现在进入场地才会生成，且死亡时清除所有地图上的霜火粒子效果\n" +
                        "3.在子层系统中，陷阱房间将不再存在裂缝\n" +
                        "4.萨卡班甲鱼存活判定变得更加细致\n" +
                        "5.开发者模式状态查看器可查阅免疫状态\n" +
                        "6.DM720电塔激活时，将指向电塔\n" +
                        "7.三井层在探险笔记中会同时显示三个井，使用其中一个这三个都会在探险笔记中被移除\n" +
                        "8.特殊模式Boss不再掉落天狗面具，金蝶到11层，BR到10层的楼层入口自行领取\n" +
                        "9.钴币开发者模式可以获取，但无效果")));


        Image ss =new ShubNiggurathSprite();
        ss.scale.set(PixelScene.align(0.4f));
        changes.addButton(new ChangeButton(ss, ("莎布·尼古拉丝"),
                "1.如果与墨菲厄斯在同一个格子 直接死亡\n" +
                        "2.召唤上限调整至18"));

        Image st =new TowerMachineSprite();
        st.scale.set(PixelScene.align(0.4f));
        changes.addButton(new ChangeButton(st, ("古堡四柱调整"),
                "1.修复机械之柱范围伤害异常\n" +
                        "2.四柱近战伤害都为0\n" +
                        "3.思维模块相关Bug修复\n" +
                        "4.修复机械之柱的炮弹击杀会多次弹出死亡文本\n" +
                        "5.修复神明之柱的元素抗性异常，现在半血前50%，半血后75%"));
    }

    public static void add_V0915_Changes(ArrayList<ChangeInfo> changeInfos ) {
        ChangeInfo changes = new ChangeInfo("v0.9.1.5", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
        changes.hardlight(CharSprite.WARNING);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.WAND_HAND_CONTROL), ("法师之手控制器"),
                ("新增以下功能\n\n" +
                        "1.可以指定敌人，法师之手将优先攻击此敌人\n\n" +
                        "2.如果在召唤过程中失败，可在这里进行再次召唤")));

        changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
                ("1.修复有形之手天赋造成的崩溃\n" +
                        "2.隐藏控件暂时删除\n" +
                        "3.修复矮人尸群因为多线程导致的检查崩溃\n" +
                        "4.修复创世神之心的判定优先级问题导致的崩溃\n" +
                        "5.修复并发检查带来的崩溃\n" +
                        "6.修复苦痛刻痕判定优先级问题带来的崩溃异常\n" +
                        "7.修复熔岩火龙视野判定导致的崩溃异常\n" +
                        "8.修复绝命头目-拟态之王 流血公式计算异常\n"),
                ("9.修复思维之柱的判定优先级的崩溃异常\n" +
                        "10.修复哨位素材无法读取时，则直接摧毁素材实体\n" +
                        "11.修复矮人尸山的越界判定异常\n" +
                        "12.修复灵壤，雷霆，注魂，腐化的使用者始终为英雄，即便在法师之手上\n" +
                        "13.修复BossRush-绿野精灵国王 的伤害重复调用传送，可能导致栈溢出的异常\n" +
                        "14.修复因上次2.5破碎底层的迁移导致的各种异常残留\n" +
                        "15.修复机械之柱的敌人判定的一些异常\n" +
                        "16.修复法师之手没有贵重物品标签\n" ),

                ("17.修复幽寂错误的对话逻辑导致的崩溃\n" +
                        "18.修复了魔力补偿的相关异常崩溃\n" +
                        "19.修复了纯晶护卫长火墙描述文本缺失\n" +
                        "20.部分文案优化和错误修正")));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "nerfs"), false, null);
        changes.hardlight(Window.R_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new DeadEyeSprite(), ("毁灭魔眼"),
                ("现在可解离背包内的物品，增大了解离的物品池子（包括容器内的物品，但仍会排除贵重物品、露水、国王袋等特殊物品）")));

        changes.addButton(new ChangeButton(Icons.get(Icons.PREFS), ("熔岩洞穴"),
                ("现在只会固定生成随机2卷轴，1符石，1食物，1药剂")));

        Image ss =new ShubNiggurathSprite();
        ss.scale.set(PixelScene.align(0.4f));
        changes.addButton(new ChangeButton(ss, ("莎布·尼古拉丝"),
              "_-_ 分身分裂上限：最多分裂9个黑山羊分身\n" +
                        "_-_ 血量恢复机制：本体在分身存在时可恢复1000点生命值，但仅限5次\n" +
                        "_-_ 死亡条件：第6次尝试恢复生命值时，本体与所有分身将直接死亡 或 黑山羊无任何分身时可直接死亡"));

        changes = new ChangeInfo("预载", false, null);
        changes.hardlight(Window.CBLACK);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(Icons.get(Icons.NEWS), ("1.0.0-MLPD"),
                ("1.0.0相关资源预载")));
    }


    public static void add_V0910_Changes(ArrayList<ChangeInfo> changeInfos ) {
        ChangeInfo changes = new ChangeInfo("v0.9.1.0-1", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
        changes.hardlight(Window.CYELLOW);
        changeInfos.add(changes);

        changes.addButton( new ChangeButton(HeroSprite.avatar(HeroClass.MAGE, 13), "新皮肤：晚宴邀请",
                "2025.12.25-2026.1.1 0点前，完成特别委托任务，获得此皮肤。\n\n" +
                        "未在活动期间完成委托的，将在0.9.2更新后以 _1500_钴币在时装商人处购买\n\n" +
                        "             --Art Design By:戈壁滩-Seagull"));

        changes.addButton(new ChangeButton(Icons.get(Icons.NEWS), ("新兑换码：MerryChristmas"),
                ("进入游戏通过落白商店 或者 游戏菜单 获取奖励！\n\n有效期至：2026年1月1日0：00前。")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.STAR_CRYSTAL), ("新物品：星空水晶"),
                ("完成古堡真结局后，卡戎商店将会有此商品，进入猩红剧院线的31层，将立刻前往最终决战。")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.MAGNETIC_CROWN), ("新神器：引力王冠"),
                ("戴上这顶王冠时，你感受到了空间中微弱的磁力，这种力量也许能把你和其他生物拖向某一地点……\n" +
                        "\n" +
                        "使用消耗1点充能，点击任意视野内的地格，将距离那个地格最近的单位拖拽至此处，在一次使用中最多因此法位移3+等级*0.5格。\n" +
                        "（如果有相同距离判定则优先拉敌人）\n" +
                        "\n" +
                        "当视野内的一次性陷阱被触发时获得10经验，每获得50+7*等级点经验神器升一级。\n" +
                        "初始充能3，最大充能10，每升级一级获得一点最大充能。每50-等级回合恢复一点充能。\n" +
                        "\n" +
                        "神器充能效果为每回合恢复0.3点充能。\n" +
                        "诅咒效果为无法使用。")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.RITUAL_SWORD), ("新武器：仪式短剑"),
                ("二阶，力量需求12\n" +
                        "\n" +
                        "初始3-15，成长1-3\n" +
                        "\n" +
                        "这把武器不会被诅咒，并且使献祭仪式更加高效（只需要杀一个怪）。\n" +
                        "\n" +
                        "曾被主教用于主持多场宗教仪式。")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.TREE_LIST), ("新武器：树痕"),
                ("二阶，力量需求13\n" +
                        "\n" +
                        "初始3-14，成长1-3\n" +
                        "\n" +
                        "初始护甲0-3，成长0-1\n" +
                        "\n" +
                        "这把武器会吸收自然损失的护盾值，上限为25+15*区域数\n" +
                        "\n" +
                        "10*等级，攻击也会积攒2+0.2*等级点护盾。可以花费1回合将这些护盾释放出来。\n" +
                        "\n" +
                        "能够吸收空气中魔力生长的植物。")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.SAD_MAGIC_BOOK), ("新武器：悲情法典"),
                ("三阶，力量需求14\n" +
                        "\n" +
                        "初始2-20，成长2-2\n" +
                        "\n" +
                        "使用这把武器击杀敌人后，有（10+3*等级）%的概率鉴定背包内的一件随机物品。\n" +
                        "\n" +
                        "这本书上记载着一些称得上悲惨的内容，包括无饭可吃而被迫食用简单处理的怪物肉、被逐渐石化致死，甚至包括变形为非知性体，金属制品的过程。")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.SHEPHERD_FLUTE), ("新武器：牧笛"),
                ("三阶，力量需求14\n" +
                        "\n" +
                        "初始2-9，成长1-2，攻击距离10\n" +
                        "\n" +
                        "目标距离你每有1距离，对其造成的攻击伤害减少10%，攻击无视物理防御并造成法术伤害。\n" +
                        "\n" +
                        "城郊牧笛声落在那座野村，缘分落地生根是我们。")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.BREAK), ("新武器：破军"),
                ("五阶，力量需求18\n" +
                        "\n" +
                        "初始6-28，成长1-6\n" +
                        "\n" +
                        "当你装备着这把武器时，对生命值低于50%的敌人造成的伤害提升（30+3*等级）%，若使用这把武器攻击，则效果提升至（100+10*等级）%\n" +
                        "\n" +
                        "孩子们，等我……")));

        Image gf = new Image(new GiantFlowerSlimeSprites());
        gf.scale.set(PixelScene.align(0.8f));
        changes.addButton(new ChangeButton(gf, ("新怪物：巨型繁花史莱姆"),
                ("相对来说比较稀有的繁花史莱姆，因为体型更大导致粘上了更多的植物和种子，并因此导致攻击可能会产生植物的效果\n" +
                        "14血，2闪，1速，8命中，2经验，最高5等级获取经验\n" +
                        "攻击时有1/3的概率施加2回合火焰，1/3概率施加3回合冻伤，1/3概率施加5回合缠绕，1/5概率5回合植物疗养或者2回合时空气泡\n" +
                        "掉落俩种子。")));

        changes.addButton(new ChangeButton(new Image(new CrabSprite.NewBornCrabSprite()), ("新怪物：新生螃蟹"),
                ("丛林螃蟹的青年体，甲壳还未发育成熟，因此防御会更脆弱，但是速度会更快，动作会更灵活\n" +
                        "12血，1-7攻击，0-3防御，移动速度为2.5闪避为6，命中为12，掉落等级和螃蟹一样\n" +
                        "掉落奇怪的肉一块")));

        changes.addButton(new ChangeButton(new Image(new DM111Sprite()), ("新怪物：DM-111"),
                ("dm100的加强版本，其实是第一版加强，但是第二版已经叫dm200了，拥有更强的火力，更厚重的装甲，并且可以蓄力放电制服犯人，但是引擎没有跟上，所以移动速度会更慢\n" +
                        "35血，4-10攻击，远程5-13，防御为0-6，可以蓄力俩回合造成5-15伤害的远程攻击，并施加3回合麻痹，蓄力攻击会被水传导，不传导麻痹效果，只传导伤害，移动速度0.5蓄力攻击后2-8回合不能攻击\n" +
                        "掉落一张充能卷轴，7经验，最高13等级")));

        changes.addButton(new ChangeButton(new Image(new GuardCapitalSprite()), ("新怪物：狱卒小队长"),
                ("作为狱卒的小头领，相比其他狱卒更加强壮，但不使用锁链将犯人拉近而是更习惯将犯人击退或配合其他狱卒远程攻击\n" +
                        "50血 6-16攻击 防御0-7 闪避12 命中12，远程攻击伤害6-12\n" +
                        "玩家靠近时将玩家击退3格，不会掉入悬崖，优先寻找其他狱卒一起行动，单独行动时遇到玩家会保持距离，在其他狱卒附近会保持攻击玩家，击退玩家有20回合冷却，7经验14等级\n" +
                        "掉落随机护甲武器")));

        changes.addButton(new ChangeButton(new Image(new VeryColdRatSprite()), ("新怪物：极寒冰鼠"),
                ("长期的寒冷环境与冰系魔力导致其体内魔力狂暴而又非常不稳定\n" +
                        "70血，攻击15-25，5闪避，20精准，1.5速，0-3防\n" +
                        "周身一直散发冰寒气体(类似腐鼠)，\n" +
                        "攻击有25%概率造成冻结\n" +
                        "战斗状态下每回合损失5%最大生命值，没有目标时每回合回复5%最大生命值\n" +
                        "死亡前必定掉落一个冰霜炸弹，并且自身_4回合后_会产生一次爆炸，范围与效果等同寒霜炸弹\n" +
                        "8经验，最高16等级")));

        changes.addButton(new ChangeButton(new Image(new BloodsSwarmSprite()), ("新怪物：血红蝇群"),
                ("因为过于寒冷导致血红苍蝇依靠挤一起取暖，一起行动，攻击会导致他们分裂开，非常危险\n" +
                        "被攻击扣20%血当前血量分裂出一只火苍蝇，分裂出来的火苍蝇会和蝇群一起行动，和火苍蝇一样，会远程攻击\n" +
                        "80血，攻击5-10，防御0-2，闪避15，命中20，掉落等级和火苍蝇一样，必定掉落一瓶磷火")));

        Image js = new Image(new GhoulPlusSprite());
        js.scale.set(PixelScene.align(0.8f));
        changes.addButton(new ChangeButton(js, ("新怪物：矮人尸山"),
                ("矮人尸群拥有可以源源不断战斗的能力，但是魔法总是不那么可靠，所以他们在恢复的时候粘到一起了，并且在不停的吸收其他的尸群\n" +
                        "会主动和其他尸群一起行动，如果旁边有尸群存在则会削弱他们并加强自身\n" +
                        "生成时代替一组尸群，不再生成俩只尸群而是只生成一只尸山，尸山会优先向其他尸群移动并和其他尸群一起行动\n" +
                        "尸山5x5范围内每有一只尸群尸山获得15%减伤和15%的攻击加成，尸山附近的尸群受到的伤害增加30%，附近的尸群死亡会直接死亡，并让尸山回复50点血量并增加30血上限和10点攻击力和5点防御\n" +
                        "初始100血，15-20攻击，0-3防御，闪避10，命中25，必定掉落4倍矮人尸群的金币，经验，经验等级同尸群")));

        changes.addButton(new ChangeButton(new Image(new WarlockHeadSprite()), ("新怪物：矮人首席术士"),
                ("矮人术士是矮人中的掌权者，而这位更是术士的领导者，他对黑暗魔法的掌握远超其他术士，可以对目标造成更加严重的影响\n" +
                        "85血，18-23攻击\n" +
                        "防御为0-10，闪避为18，精准为28\n" +
                        "远程攻击有2/3的概率让目标降级40回合，1/3概率让目标流血5-8，1/3概率让目标幻惑30回合，1/5的概率让目标失明12回合，如果触发了，每一种buff，就额外施加5回合虚弱\n" +
                        "近战时，获得10-20的奥术护盾\n" +
                        "固定掉落一瓶经验，掉落11经验，最大经验等级为21级")));

        changes.addButton(new ChangeButton(new Image(new SpawnereEvilSprite()), ("新怪物：恶孽血巢"),
                ("这一团矮人血肉比其他血巢更加活跃，浓厚的血气和死气正是恶魔们最好的食粮。\n" +
                        "\n" +
                        "血量上限140，防御0-12\n" +
                        "每隔50/40/35/30回合召唤一只“恶魔盛宴”\n" +
                        "大幅减少超过14的伤害\n" +
                        "每受到1伤害减少1回合召唤cd\n" +
                        "必定掉落治疗药水*1，大饼*1\n" +
                        "掉落等级同血巢")));

        changes.addButton(new ChangeButton(new Image(new FodderSprite()), ("新怪物：'恶魔盛宴'"),
                ("恶魔世界中非原生的存在，天生比正常的恶魔弱小。通过被其他的恶魔蚕食，它会成为强大恶魔的一部分。\n" +
                        "同恶魔撕裂者，但只有一半移速。会成为其他任何一种敌怪的攻击目标，被其他敌怪击杀时，完全治疗那个敌怪后赋予其等于最大生命一半的奥术护盾，并赋予其随机精英/突变双词条。生命上限50，攻击为10-20，命中为30，防御为0，攻击延迟0.5，闪避为15，不会攻击其他怪物，被其他怪物攻击时必中，不掉落东西，不获得经验")));

        Image vs = new Image(new DeadEyeSprite());
        vs.scale.set(PixelScene.align(0.8f));
        changes.addButton(new ChangeButton(vs, ("新怪物：毁灭魔眼"),
                ("魔眼是一种邪眼的变体，拥有和邪眼一样的能力，但是比邪眼更容易发现敌人，其的死亡凝视比邪眼更具破坏性，也许你不应该和他硬碰硬\n" +
                        "110血，攻击为22-35，防御0-10，闪避20，命中30，视野距离8\n" +
                        "蓄力2回合射出死亡凝视，攻击30-50，蓄力期间免伤3/4，解离被死亡凝视命中的玩家背包中的除贵重物品和武甲背包以外的物品2-3件，掉落复仇一张，掉落13经验，26获取等级")));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
        changes.hardlight(CharSprite.WARNING);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new Image(new MageHandSprite()), ("法师之手AI初步优化"),
                ("_-_ 添加如果英雄在弹道上，先到英雄的弹道不可抵达处来，但对于范围伤害法杖仍然可能误伤英雄\n\n" +
                        "_-_ 对于怪物威胁度，添加优先级：正在攻击英雄的怪物 > 精英Buff怪物  > (血量+伤害)(从高到低) 怪物 > 随机选择\n\n" +
                        "_-_ 智能评估自身站位，以更好的援助自己的主人")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.NEWSTEM), ("初生树枝"),
                ("一截发芽的枯枝，渴盼着再长出来的一天。\n\n在非严重饥饿状态下，自然回复时额外获得_+1生命值_")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.CONfUSEDMIEMIETALISMAN), ("咩咩护符"),
                ("也许制作者的本意是使用魔法绵羊来保护自己，但很显然，他手艺不精。\n\n你<#F00>每回合有2%<RGB>的概率触发绵羊符石效果\n触发后有<#F00>10回合<RGB>的预警时间\n效果触发后的_75回合_内不会再次触发。")));

        changes.addButton(new ChangeButton(Icons.get(Icons.CHALLENGE_ON), ("挑战调整"),
                ("变幻莫测：新增稀有怪全局生成概率提升10%")));

        changes.addButton(new ChangeButton(Icons.get(Icons.WARNING), ("错误报告历史记录"),
                ("现在游戏崩溃后，错误报告将会记录在本地存储中，方便给开发者提供更多数据，以追查问题。")));

        changes.addButton(new ChangeButton(Icons.get(Icons.PREFS), ("杂项调整"),
                ("_-_ 新闻界面调整，现在可滚动（手机端用户请用新闻卡片缝隙处进行滑动，此UI将会在092继续调整）\n" +
                        "_-_ 新闻界面现在可以读取置顶卡片，链接需求卡片\n" +
                        "_-_ 皮肤系统底层升级\n" +
                        "_-_ 部分素材优化迭代")));

        changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
                "修复了上个版本的诸多问题。"));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "nerfs"), false, null);
        changes.hardlight(Window.R_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new RedNecromancerSprite(), ("豺狼祭司"),
                ("此怪物在生成中移除，但仍然保留在开发者模式和图鉴中。")));

        changes.addButton(new ChangeButton(new SRPDHBLRTT(), ("火把猎人"),
                ("此怪物在生成中移除，仅能在特殊房间中生成，但仍然保留在开发者模式和图鉴中。")));

        Image ss =new ShubNiggurathSprite();
        ss.scale.set(PixelScene.align(0.4f));
        changes.addButton(new ChangeButton(ss, ("莎布·尼古拉丝"),
                ("分裂个体至多为9个，超过这个后不再自动分裂，攻击也无法自动分裂")));
    }

    public static void add_V0900_Changes(ArrayList<ChangeInfo> changeInfos ) {
        ChangeInfo changes = new ChangeInfo("v0.9.0.3", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
        changes.hardlight(Window.CYELLOW);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(Icons.get(Icons.CHANGES), ("重大更新"),
                ("魔绫现已进入0.9.0.0版本！感谢所有支持魔绫像素地牢的玩家们。\n\n全新古堡外传章节现已开启，欢迎尝鲜！")));

        changes.addButton(new ChangeButton(NetIcons.get(NetIcons.GLOBE), ("在线更新系统V2.1"),
                ("因协议调整，0.9.0.3起将使用新的更新接口。老版本的更新接口将陆续退场。")));

        changes.addButton(new ChangeButton(Icons.get(Icons.BADGES), ("新-普通徽章 & 隐藏徽章"),
                ("新的一批普通 & 隐藏徽章现已上线，游玩游戏获得！\n\n" +
                        "普通徽章：剧院新星->知名演员->金奖影帝，幽寂之梦\n\n" +
                        "隐藏徽章：我从地狱归来，神圣裁决，不给糖就捣蛋，再摸亿下，幽灵猎人")));

        changes.addButton(new ChangeButton(Icons.get(Icons.AUDIO), ("新原创音乐：决战星海"),
                ("作者：犬罗\n\n为古堡最终Boss的战斗BGM，欢迎欣赏！")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.SZJ_REBACK), ("新投掷武器：十字回旋镖"),
                ("武器设计：小叶\n\n在这枚投掷武器的命中敌人处会产生电墙，持续若干回合。墙范围内的敌人每回合受到若干点伤害。\n\n异端必被裁决，在此宣告审判！！！\n\n<#FFA500>渊落古堡外传 神圣裁决 徽章后解锁的限定武器，再次感谢你游玩魔绫的首个外传。<RGB>")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.DEATHRONG_BOAT), ("新武器：卡戎的船桨"),
                ("武器设计：小叶\n\n如果你的脚下是水面，则对此次攻击目标的攻击伤害将传导至水面上的所有敌人，每扩大一格，伤害就丢失若干。\n\n小船儿荡起双桨，让我们推开波浪~\n\n<#FFA500>渊落古堡外传击败刻耳柏洛斯限定武器，再次感谢你游玩魔绫的首个外传。<RGB>")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.IRON_KEY), ("新机制：赘余钥匙清理"),
                ("在小游戏全面搜查结束后，将移除全部多余的钥匙。")));

        Image mp = new MorpheusSprite();
        mp.scale.set(PixelScene.align(0.6f));
        changes.addButton(new ChangeButton(mp, ("新Boss：墨菲厄斯"),
                ("盛大的剧目即将开演，猩红的宴会现在开始！")));

        changes.addButton(new ChangeButton(Icons.get(Icons.LANGS), ("语言更新：繁体中文"),
                ("来自Sotis的繁体中文已经更新")));

        changes.addButton(new ChangeButton(Icons.get(Icons.DISPLAY_LAND), ("UI更新"),
                ("由 BrogField 制作的桌面端全尺寸已经实装，欢迎使用！")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.CASTLE_AIRPORT), ("首个外传：渊落古堡"),
                (
                        "魔绫像素地牢-渊落古堡外传，于2023年10月立项，期间经过了太多问题和冲突，但最终，她在2025年11月01日圆满完成！\n\n" +
                                "制作名单：\n\n" +
                                "_-_ 总策划：JDSALing\n" +
                                "_-_ 编码支持：JDSALing 手电 潘多拉\n" +
                                "_-_ 剧情原案：JDSALing\n" +
                                "_-_ 美术设计：Daniel Calan\n" +
                                "_-_ 领袖设计：寄神\n" +
                                "_-_ 文案设计：久住 小叶\n" +
                                "_-_ 武器设计：小叶\n" +
                                "_-_ 音乐制作：犬罗 スタ-Tatsuro\n" +
                                "_-_ 领航测试：Archetto喵 大猫 太上忘情\n\n" +
                                "谨以此外传，献给所有热爱 魔绫像素地牢 的玩家！！！\n\n" +
                                "祝各位玩家玩的愉快，冒险之路永不停歇！")));

        changes.addButton(new ChangeButton(Icons.get(Icons.NEWS), ("新兑换码：NINENINEZEROONE"),
                ("进入游戏通过落白商店 或者 游戏菜单 获取奖励！\n\n有效期：永久")));

        changes.addButton(new ChangeButton(Icons.get(Icons.NEWS), ("新兑换码：HELLO-MLPD-V0.9"),
                ("进入游戏通过落白商店 或者 游戏菜单 获取奖励！\n\n有效期：永久")));

        changes.addButton( new ChangeButton(new BuffIcon(BuffIndicator.PACMAN_GAME, true), "三大小游戏登场",
                "吃豆人，推箱子，全面搜查小游戏登场，探索渊落古堡外传，游玩这些小游戏。"));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
        changes.hardlight(CharSprite.WARNING);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(Icons.get(Icons.LIBGDX), ("LibGDX 1.11"),
                ("魔绫现在使用LibGDX1.11系统，此版本对于所有系统都有较好的兼容性。")));

        Image xs =new PeachGodStateSprite();
        xs.scale.set(PixelScene.align(0.8f));
        changes.addButton(new ChangeButton(xs, ("桃源祈愿-复刻"),
                ("【限时活动--桃源祈愿·十连必得】-复刻活动时间：2025.11.2-2025.11.30\n\n" +
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

        changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
                "魔绫像素地牢-修复内容：\n\n" +
                        ("0.9.0.3-修复内容：\n" +
                                "_-_ 修复种子局下得分为0\n" +
                                "_-_ 修复查种崩溃\n" +
                                "_-_ 优化墨菲厄斯更好的背包赠送\n" +
                                "_-_ 优化吃豆人战前默认没有任何装备。避免携带部分被Ban物品直接被带入\n" +
                                "_-_ 种子局也显示古堡结局图标\n" +
                                "_-_ 修复吃豆人游戏卡死的严重异常\n" +
                                "_-_ 优化商店抢劫十字架处理\n" +
                                "_-_ 奈亚不再生成捕猎陷阱\n" +
                                "_-_ 黑山羊分身如果与本体重合将自动死亡"
                        )));

        changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
                "魔绫像素地牢-修复内容：\n\n" +
                        ("0.9.0.2-修复内容：\n" +
                                "_-_ 修复三大小游戏流程问题，即便崩溃也不会再卡死游戏进程\n" +
                                "_-_ 修复查种器因为控件调用不一致导致的崩溃\n" +
                                "_-_ 修复角斗士连击技能的一些判定问题导致的游戏崩溃\n" +
                                "_-_ 修复撤离点效果的一些判定问题导致的游戏崩溃\n" +
                                "_-_ 修复推箱子错误的坐标算法导致的部分状态下的按钮错乱\n" +
                                "_-_ 任何类型十字架现在小游戏中无任何作用，但英雄也永远不会在小游戏中死亡\n" +
                                "_-_ 修复抢劫返还十字架异常"
                        )));

        changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
                "魔绫像素地牢-修复内容：\n\n" +
                        ("0.9.0.1-修复内容：\n" +
                                "_-_ 修复传送晶柱可回到 0 层的异常问题\n" +
                                "_-_ 修复0层使用金蝶可以荆棘嬗变相关异常问题\n" +
                                "_-_ 修复墨菲厄斯三阶段四柱异常问题\n" +
                                "_-_ 修复部分古堡物品图鉴无法正常解锁异常问题\n" +
                                "_-_ 修复注魂法杖加盾失效问题\n" +
                                "_-_ 修复陷阱给予心魔损伤异常问题\n" +
                                "_-_ 修复全面搜查刷分异常问题\n" +
                                "_-_ 修复吃豆人可以使用虚空锁链的问题"
                        )));

        changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
                "魔绫像素地牢-修复内容：\n\n" +
                        ("0.9.0.0-修复内容：\n" +
                                "中测相关反馈问题已经完全修复。\n" +
                                "中测协力领航人：Archetto喵 大猫 太上忘情"
                        )));

        changes.addButton(new ChangeButton(Icons.get(Icons.PREFS), ("杂项改动"),
                ("1.素材优化迭代\n" +
                        "2.古堡图块优化迭代")));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "buffs"), false, null);
        changes.hardlight(Window.GREEN_COLOR);
        changeInfos.add(changes);

        Image s =new ShubNiggurathSprite();
        s.scale.set(PixelScene.align(0.4f));
        changes.addButton(new ChangeButton(s, ("莎布·尼古拉丝"),
                ("灵爆秘卷对其的伤害降低60%。")));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "nerfs"), false, null);
        changes.hardlight(Window.R_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new TribemanOldSprite(), ("豺狼悍匪"),
                ("攻速改为2.0，而非之前的0.5。")));

        Image ss =new ShubNiggurathSprite();
        ss.scale.set(PixelScene.align(0.4f));
        changes.addButton(new ChangeButton(ss, ("莎布·尼古拉丝"),
                ("1.分裂个体至多为18个，超过这个后不再自动分裂，攻击也无法自动分裂\n" +
                        "2.现在本体有 突变爆炸体 的特殊标记\n" +
                        "3.现在可以吃到灵爆秘卷的伤害\n" +
                        "\n4.当场上只剩下黑山羊和它的分身时，攻击_黑山羊本体_将导致它的血量重置时，给予英雄50000回合灵视。\n同时，在_黑山羊本体_死亡时，将去除英雄的灵视效果。")));

        changes.addButton(new ChangeButton(new YogSoulSprite(), ("犹格·索托斯"),
                ("在 犹格·索托斯 周围时，犹格·索托斯将不再释放任何技能。")));
    }

    public static void add_V0880_Changes(ArrayList<ChangeInfo> changeInfos ) {
        ChangeInfo changes = new ChangeInfo("v0.8.8.0-1", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
        changes.hardlight(Window.CYELLOW);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(Icons.get(Icons.LIBGDX), ("LibGDX 1.13！"),
                ("魔绫现在使用LibGDX1.13系统，对高版本安卓设备支持性能更好。")));

        changes.addButton(new ChangeButton(Icons.get(Icons.AUDIO), ("Really Slow Motion-Neutral Assault"),
                ("此音乐暂时作为 地狱三头犬 的作战BGM")));

        changes.addButton(new ChangeButton(Icons.get(Icons.HEART), ("特殊地形图标系统"),
                ("现在新增了更多特殊地形的图标：心红空间，火龙巢穴，食物售货机")));

        changes.addButton(new ChangeButton(Icons.get(Icons.NEWS), ("新兑换码：ChinaBirthDay"),
                ("进入游戏通过落白商店 或者 游戏菜单 获取奖励！\n\n有效期至：2025年10月7日1：05前。")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.SCROLL_GOLD), ("金蝶模式V2.4"),
                (
                        "金蝶模式-V2.4\n\n" +
                                "_-_ 修复金蝶模式九宫格嬗变不升级：升级次数大于18时会出现嬗变但不升级的情况\n" +
                                "_-_ 修复金蝶模式NPC“夏渔雾溟”的次数给予是重置当前次数为2，而非添加2次\n" +
                                "_-_ 修复金蝶模式20层Roll到矮人将军楼层上下楼失效\n" +
                                "_-_ 金蝶模式现在开局对话“夏渔雾溟”自动给予3饰品\n" +
                                "_-_ 金蝶模式中的哥布林祭司得到显著增强")));

        changes.addButton(new ChangeButton(Icons.get(Icons.CATALOG), ("平衡案V3.4 第二部分"),
                ("实现了平衡案-V3.4第二部分，详情如下：\n\n" +
                        "【综合说明】：\n\n" +
                        "_-_ 全肉大饼去掉低于12力量必定加成的保底\n" +
                        "_-_ 毒素浸染法杖增加递增伤害\n" +
                        "_-_ 绿宝石加入300回合冷却，且只有40回合隐身")));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
        changes.hardlight(CharSprite.WARNING);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(Icons.get(Icons.PREFS), ("全局系统：自定义横幅"),
                ("在额外设置中，可以定义游戏内带的横幅主题，新增国庆，中秋，重阳主题")));

        changes.addButton(new ChangeButton(Icons.get(Icons.DISPLAY), ("安卓系统"),
                ("修复部分变种安卓版本调用安装器闪退失败的问题")));

        changes.addButton(new ChangeButton(Icons.get(Icons.NEWS), ("节日系统大修"),
                ("现在节日系统优先级为 游戏特殊节日 > 中国传统节日 > 西方传统节日")));

        changes.addButton(new ChangeButton(Icons.get(Icons.CHALLENGE_ON), ("药水癔症v0.3"),
                ("现在全肉大饼在英雄低于12力量前不再必定追加力量，但在11力量以下饮用力量药剂为必定追加力量。")));

        changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
                "魔绫像素地牢-修复内容：\n\n" +
                        ("0.8.8.1-修复内容：\n" +
                                "1.修复火龙死亡前越界的严重异常\n" +
                                "2.修复金蝶任务2：金蝶见证者 效果异常的问题\n" +
                                "3.修复烈阳法杖必定造成灵魂标记的异常" +
                                "4.修复FireBase上反馈的一系列问题\n" +
                                "5.修复国庆节时间计算错误\n" +
                                "6.暂时让Bzmdr的笔记效果失效"
                        ),
                (       "0.8.8.0-修复内容：\n" +
                        "一、武器与道具系统修复\n" +
                        "修复：0 层传说武器购买异常，已修正\n" +
                        "修复：创世神之心卡死异常，已修正\n" +
                        "修复：创世神之心在特定情况下导致的严重异常，已修正\n\n" +
                        "二、怪物与 NPC 行为修复\n" +
                        "修复：亡魂残躯异常，已修正\n" +
                        "修复：鬼魂及南瓜头鬼魂异常，已修正\n" +
                        "修复：火魔女充能回合异常，已修正\n" +
                        "修复：残魔余卷对护甲不生效的问题，已修正")));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "nerfs"), false, null);
        changes.hardlight(Window.R_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(Icons.get(Icons.PREFS), ("一些NPC移除"),
                ("根据NPC作者委托方需要，部分NPC已被移除。")));

        changes = new ChangeInfo("v0.8.8.0中测验收通过，予以更新", true, null);
        changes.hardlight(Window.CYELLOW);
        changeInfos.add(changes);
    }

    public static void add_V0874_Changes(ArrayList<ChangeInfo> changeInfos ) {
        ChangeInfo changes = new ChangeInfo("v0.8.7.3-4", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
        changes.hardlight(Window.CYELLOW);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.CLEARPRO), ("传说武器：清道夫"),
                ("修复归来，欢迎使用！")));

        changes.addButton(new ChangeButton(Icons.get(Icons.NEWS), ("新兑换码：PropsFixed"),
                ("进入游戏通过落白商店 或者 游戏菜单 获取奖励！\n\n有效期：永久有效")));

        changes.addButton(new ChangeButton(new Image("firebase/FireBase.png",0,0,16,16), ("FireBase游戏数据收集"),
                ("新增玩家装备收集，开发者模式不再进行收集。(仅安卓)")));

        changes.addButton(new ChangeButton(Icons.get(Icons.PREFS), ("圆盘快捷栏"),
                ("在V2对话框下，PC使用默认快捷键T，或全设备情况下长按切换区呼出。")));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
        changes.hardlight(CharSprite.WARNING);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
                "魔绫像素地牢-修复内容：\n\n" +
                        (       "1.在寂灭灯火情况下 60以上即可不会触发低语\n" +
                                "2.修复藏品崩溃的严重异常\n" +
                                "3.修复以种子查物品，无任何文本时的崩溃")));

        changes.addButton(new ChangeButton(Icons.get(Icons.CATALOG), ("平衡案3.4 第一部分"),
                ("实现了平衡案-V3.4第一部分，详情如下：\n\n" +
                        "【怪物与掉落调整】：\n\n" +
                        "1.冰老鼠的药水掉落概率降低，移除治疗经验掉落。首次掉落概率100%，后续每次掉落概率减少2/3\n" +
                        "2.神秘博士调整为稀有怪，与血月魔盾猎人出现权重互换\n" +
                        "3.冰魔女不再掉落升级卷轴\n" +
                        "4.矮人将军后的小恶魔不再出售升级卷轴\n" +
                        "5.小蓝的祝福效果由1-4级随机调整为1-2级随机"),
                ("【荆棘机制变更】：\n\n" +
                        "荆棘现在一般情况下无法进行嬗变。若荆棘在10级时尝试嬗变，将返回一个升级卷轴（每局仅生效一次）。"),
                "【怪物经验等级调整】：\n\n" +
                        "1.一区喷毒花和蝾螈改为10级\n" +
                        "2.影子盗贼改为13级\n" +
                        "3.冰老鼠改为15级\n" +
                        "4.豺狼祭司改为17级（14层生成-新怪物）\n" +
                        "5.霜冻魔像改为22级\n" +
                        "6.丛林土鳖、三叶虫、奇虾改为23级\n" +
                        "7.血月魔盾/火焰赏金猎人、猩红大盗改为25级\n" +
                        "8.恐怖博士改为25级\n" +
                        "9.血月魔盾/火焰赏金猎人改为27级（掉落物品等级为获取经验等级+2）\n" +
                        "10.丛林土鳖的精准属性改为26\n" +
                        "11.传说武器掉落权重提升至不低于3",
                ("【道具与商店调整：】：\n\n" +
                        "1.贩卖机购买次数限制改为：每区域最多购买次数=区域数-[力量-(10+2×区域数)]（次数固定不实时变化），价格调整为325+(区域数-1)×50\n" +
                        "2.火魔女商店所有物品价格为正常商店的60%，刷新价格改为350，正常商店价格按最深进度计算\n" +
                        "3.闪电种子掉落概率提升（权重从1改为4）\n" +
                        "4.回忆商店每局必定14层刷新"),
                ("【机制与效果调整：】：\n\n" +
                        "1.水域效果调整：\n" +
                        "_-_二区踩水移除隐身效果，修复极速buff不生效的问题，极速效果离水后失效\n" +
                        "_-_磷火现在可被摔碎的药水清除\n" +
                        "_-_污泥buff触发阈值从7-当前深度/5改为10-当前深度/5\n" +
                        "_-_污泥水爆移除蒸发机制，恢复正常效果\n" +
                        "2.快捷栏逻辑改为前3栏与后3栏分页切换（回滚至破碎逻辑）"),
                ("【Boss与古堡调整：】：\n\n" +
                        "1.天罚奖励新增2个附魔符石，火龙鳞片自带1级等级\n" +
                        "2.甲鱼物理防御降低10点，激光技能需多蓄力1回合\n" +
                        "3.古堡怪物属性调整：\n" +
                        "_-_见习魔女：HP125、闪避25-37、经验18、最大等级35、伤害25-45、命中35-45\n" +
                        "_-_电锯狂人：HP100、闪避30-40、经验16、最大等级35、伤害32-42、命中40-50\n" +
                        "_-_姜饼人：HP90、闪避25-35、经验20、最大等级36、伤害25-40、命中20-35\n" +
                        "_-_残梦魔偶：HP100、闪避20-40、经验15、最大等级34、伤害25-35、命中35\n" +
                        "_-_南瓜炸弹人：HP120、闪避25-30、经验19、最大等级35、伤害25-30、命中50\n" +
                        "_-_吸血鬼：HP110、闪避25-40、移速2.0、经验15、最大等级35、伤害30-35、命中40")));
    }

    public static void add_V0872_Changes(ArrayList<ChangeInfo> changeInfos ) {
        ChangeInfo changes = new ChangeInfo("v0.8.7.2", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
        changes.hardlight(Window.CYELLOW);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(Icons.get(Icons.PREFS), ("新全局系统：自定义横幅"),
                ("在额外设置中，可以定义游戏内带的横幅主题，新增中元节主题")));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
        changes.hardlight(CharSprite.WARNING);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
                "魔绫像素地牢-修复内容：\n\n" +
                        (       "1.修复创世神之心在一些极端情况导致的游戏崩溃异常\n" +
                                "2.修复狗子层重复提交排行榜的Bug\n" +
                                "3.修复天痕粘咕的毒雾可能导致越界的异常\n" +
                                "4.修复混乱香炉在一些极端情况导致的游戏崩溃异常\n" +
                                "5.修复重复藏品Bug")));


        changes = new ChangeInfo("v0.8.7.2中测验收通过，予以更新", true, null);
        changes.hardlight(Window.CYELLOW);
        changeInfos.add(changes);
    }

    public static void add_V0871_Changes(ArrayList<ChangeInfo> changeInfos ) {
        ChangeInfo changes = new ChangeInfo("v0.8.7.1", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
        changes.hardlight(Window.CYELLOW);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(Icons.get(Icons.PREFS), ("新全局系统：自定义横幅"),
                ("在额外设置中，可以定义游戏内带的横幅主题，新增七夕节主题")));

        changes.addButton(new ChangeButton(Icons.get(Icons.INFO), ("伤害系统重构"),
                ("现在伤害系统进行了重构，有物理/魔法/元素/真实 四类伤害")));

        changes.addButton(new ChangeButton(Icons.get(Icons.NEWS), ("Eula协议实装"),
                ("辉梦魔绫Eula协议已实装，进入即可阅览具体说明。")));

        changes.addButton(new ChangeButton(new Image("firebase/FireBase.png",0,0,16,16), ("FireBase游戏数据收集"),
                ("收集一些基本游戏数据，供开发者日后通过数据分析，仅在安卓端有效。\n\n同时庆祝该一历史性时刻，0层商人领主处赠送时装自选凭证。")));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "buffs"), false, null);
        changes.hardlight(Window.GREEN_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.HIGHTWAND_1), "法杖加强",
                ("冲击：伤害成长由1-3上调至1.5-4\n\n腐化：腐化时长由6+等级*3上调为6+等级*4，使拥有精英buff的敌人可以被腐化而非定命\n\n解离：伤害成长由1-4上调至1-5，穿透固体方块的增伤需求由穿透3个加强为穿透2个，无视减伤\n\n焰浪：伤害成长由1*充能-2*充能上调至2*充能-3*充能\n\n磷火：高充能给予的残废与麻痹效果回合数增加1\n\n冰霜：伤害成长由1-5上调至1-6，至多衰减30%的伤害\n\n雷霆：伤害成长由1-5上调至1-6\n"),
                ("大雷：对自身的伤害降低至25%\n\n土块：伤害成长由1-2上调至1-3，守卫的生命值由16+8*等级上升为16+12*等级，守卫具有等同于法杖 等级/2 的减伤\n\n魔弹：在释放魔弹法杖后，下一次非魔弹法杖释放不消耗回合，这个效果持续5回合\n\n棱光：伤害成长由1-3上调至1.5-4，对亡灵和恶魔的额外伤害上升为50%，+5及以上必定失明\n\n注魂：护盾量由5+等级上升为5+1.5*等级\n\n哨卫：伤害成长由1-4上调至2-5")));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
        changes.hardlight(CharSprite.WARNING);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
                "魔绫像素地牢-修复内容：\n\n" +
                        (       "_-_ 修复瓶装怨灵的闪退，以及优化该藏品\n" +
                                "_-_ 修复武甲背包会被爆炸波及\n" +
                                "_-_ 修复铁匠楼层重生十字架相关问题，优化天痕粘咕的地图\n" +
                                "_-_ 修复藏品效果重生后异常的问题")));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "nerfs"), false, null);
        changes.hardlight(Window.R_COLOR);
        changeInfos.add(changes);

        changes.addButton( new ChangeButton(new BuffIcon(BuffIndicator.CORRUPT, true), "敌法精英",
                "敌法精英拥有 25% 的伤害减免，且对于法术伤害拥有额外50%的伤害减免。"));


        changes = new ChangeInfo("v0.8.7.1中测验收通过，予以更新", true, null);
        changes.hardlight(Window.CYELLOW);
        changeInfos.add(changes);
    }

    public static void add_V0870_Changes(ArrayList<ChangeInfo> changeInfos ) {
        ChangeInfo changes = new ChangeInfo("v0.8.7.0", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
        changes.hardlight(Window.GREEN_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new Image(Assets.Interfaces.HAICONS, 32, 0, 16, 16), "困难模式",
                "困难模式重磅回归！欢迎前来测试！\n" +
                        "【从本版本开始，非困难模式10层后可丢弃藏品】"));

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

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.CLEARPRO), ("传说武器：清道夫"),
                ("暂时移除生成，待修复后归来。")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.FORESTCROSSBOW), ("新传说武器：森林弓"),
                ("暂时移除生成，待修复后归来。")));

        changes = new ChangeInfo("v0.8.7.0中测验收通过，予以更新", true, null);
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

        Image xsx =new Image(Assets.Interfaces.TOOLBAR, 0, 26, 24, 26);
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
                                "_-_ 修复了苦痛刻痕不死bug残留问题\n" +
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

}
