package com.shatteredpixel.shatteredpixeldungeon.ui.changelist.mlpd;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.PaswordBadges;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.lb.ShadowSprite;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.NetIcons;
import com.shatteredpixel.shatteredpixeldungeon.effects.BadgeBanner;
import com.shatteredpixel.shatteredpixeldungeon.effects.PasswordBadgeBanner;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.ChangesScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ApprenticeWitchSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ButcherSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CerberusSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ColdRatSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CrivusFruitsSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DM300AttackSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DM720Sprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DeathRongSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DiedMonkLoaderSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DimandKingSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DrTerrorSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DragonGirlBlueSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.FlowerSlimeSprites;
import com.shatteredpixel.shatteredpixeldungeon.sprites.FrankensteinSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GunHuntsmanSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.HeroSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.IceSlowGirlSprites;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MimicSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MiniSakaFishBossSprites;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MolotovHuntsmanSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.NxhySprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.PoltergeistSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.PumkingBomberSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.RedDragonSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.RedNecromancerSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ShamanSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SpectralNecromancerSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SuccubusQueenSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.TyphonSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.YogSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ZeroBoatDiedSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIcon;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.ui.changelist.ChangeButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.changelist.ChangeInfo;
import com.watabou.noosa.Image;

import java.util.ArrayList;

public class vM0_6_6_Changes {

    public static void addAllChanges(ArrayList<ChangeInfo> changeInfos) {
        add_SC4_Changes(changeInfos);
        add_SC3_Changes(changeInfos);
        add_SC2_Changes(changeInfos);
        add_SC1_Changes(changeInfos);
        add_SC_Changes(changeInfos);
        add_v0_6_8_0_Changes(changeInfos);
        add_v0_6_7_0_Changes(changeInfos);
        add_v0_6_6_0_Changes(changeInfos);
    }

    public static void add_CS_Changes( ArrayList<ChangeInfo> changeInfos ) {
        ChangeInfo changes = new ChangeInfo("Coming Soon", true, "");
        changes.hardlight(Window.CBLACK);
        changeInfos.add(changes);

        changes = new ChangeInfo("计划", false, null);
        changes.hardlight(Window.CBLACK);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new RedNecromancerSprite(), ("BossRush2.0"),
                ("地牢发生了翻天覆地的改变，一切的矛头指向了死灵军团……")));

        changes.addButton(new ChangeButton(new Image(Assets.Interfaces.HAICONS, 16, 16, 16, 16), ("黄金时代改动预告"),
                ("黄金沉入黄沙，化为金水流入地下。\n" +
                        "黑暗的地牢就此发出了金色的光芒。\n" +
                        "树影融入黑夜，\n" +
                        "财富在此长眠，\n" +
                        "流沙，黄金与鲜血交织着，\n" +
                        "正如一首金色的华尔兹。\n" +
                        "冒险家，地牢与财富正在等着你。\n" +
                        "当然，你的舞伴可并不友好......\n" +
                        "黄金时代即将重做。\n" +
                        "策划by QinYue 不接入游戏主线剧情")));

        Image i = new Image("sprites/boss/fireDragon.png", 0, 0, 24, 24);
        i.scale.set(PixelScene.align(0.74f));
        changes.addButton(new ChangeButton(i, ("熔岩火龙"),
                ("丛林暴乱的真相")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.CHALLANEESICON_14), ("寒冷系统V3.0"),
                ("寒冷系统将会彻底实装，敬请期待冬季版本")));

        changes = new ChangeInfo("改进", false, null);
        changes.hardlight(Window.CBLACK);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new CrivusFruitsSprite(), ("克里弗斯之果重做"),
                ("作为MLPD替代Goo的Boss,它还有很长的路要走。")));


        Image s = new DM300AttackSprite();
        s.scale.set(PixelScene.align(0.74f));
        changes.addButton(new ChangeButton(s, ("DM-ZERO"),
                ("时间在这里停止流动，与DMZERO正面对决的即将到来")));

        Image c = new IceSlowGirlSprites();
        c.scale.set(PixelScene.align(0.74f));
        changes.addButton(new ChangeButton(c, ("冰雪魔女重做"),
                ("石碑上记录了有关于她的一切，她的过去，她的现在，她的未来。而当你与她对峙时，你是否能够看清她的真实面目？")));
    }

    public static void add_SC4_Changes( ArrayList<ChangeInfo> changeInfos ) {


        ChangeInfo changes = new ChangeInfo("v0.6.9.7-RC", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(PasswordBadgeBanner.image(PaswordBadges.Badge.HELLORWORLD.image), ("新徽章：终末赞歌"),
                ("完成孤城挑战即可获得。")));

        changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.GREATAXE,
                new ItemSprite.Glowing( 0x00FFFF )), "新稀有附魔：鬼磷",
                "这个附魔会使磷火从武器中喷薄而出，能够使用点燃敌人并对正在燃烧的敌人造成中毒的额外伤害。\n\n奥术戒加成效果：提升鬼磷几率"));

        changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.GREATSWORD,
                new ItemSprite.Glowing( 0xcc7770 )), "新常规附魔：爆破",
                "这个附魔会让使用者陷入癫狂，能使爆炸的能量从武器中喷薄而出，能够使敌人目标处受到一次范围伤害亦或者给造成敌人短暂的残废效果。\n\n奥术戒加成效果：提升伤害"));

        changes.addButton(new ChangeButton(new GunHuntsmanSprite(), ("新敌人：血月兽型猎人"),
                ("支离破碎3区特供，主要攻击手段是远程攻击。")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.WATERSOUL), ("水灵改动"),
                ("污泥浊水的水灵药剂的治疗不再受药水异症影响，也不会中毒，且有效时间变为250回合。但合成表有所改变")));

        changes.addButton(new ChangeButton(new DeathRongSprite(), ("新商人：卡戎"),
                ("她发现冥河渡人的生意越来越难做，所以现在在万圣大厅开起了商店，不久她就赚的盆满钵满。")));

        changes.addButton(new ChangeButton(new DrTerrorSprite(), ("新敌人：恐怖博士"),
                ("曾经的名人，而如今沦落到如此田地。物是人非，指的就是这个了吧。")));

        changes.addButton( new ChangeButton(new Image(Assets.Environment.TILES_SEWERS, 48, 80, 16
                , 16), "房间改动",
                "全新隐藏房间：\n\n" +
                        "1.双蜂相争房\n\n" +
                        "2.鱼与飞镖的故事"));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.CHALLANEESICON_17), ("挑战改进:孤城迷境"),
                ("1.夜晚状态下玩家不会受到失明效果，夜晚状态下主要是部分怪物属性有所提升\n\n" +
                        "2.清晨到正午 物理伤害提升10%，移速提升10%，所有伤害减免8%\n\n" +
                        "3.黄昏状态下，物理伤害提升5%，移速提升5%，所有伤害减免6%\n\n" +
                        "4.添加游戏天数显示\n\n" +
                        "5.精英生成概率提升，而非100%\n\n" +
                        "6.资源生成从固定-10变为减半\n\n" +
                        "7.第一层调整为5个老鼠，但经验翻倍\n\n" +
                        "8.优化时间显示逻辑")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.CHALLANEESICON_10), ("挑战改进:支离破碎"),
                ("支离破碎重做，现在更有趣，具体如下:\n\n" +
                        "1.每大层后两层有概率入侵下层敌人\n\n" +
                        "2.在6层后，将遭遇更多新的敌人\n\n" +
                        "3.整体难度优化")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.LANTERNB), ("灯火前路-V4.1"),
                ("1.灯火能量开局不会低于55，以确保拥有正向buff。\n\n" +
                        "2.隐没祝福修复了一些小型问题")));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
        changes.hardlight(CharSprite.WARNING);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(Icons.get(Icons.PREFS), ("杂项改动"),
                ("1.部分素材优化\n" +
                        "2.开发者模式优化，近期所有怪物全部可生成")));

        changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
                "修复了以下Bug:\n\n" +
                        "1.修复了魅魔女王闪退的问题。\n" +
                        "2.修复了提灯的异常崩溃问题\n" +
                        "3.修复了炼狱精英的处理问题\n" +
                        "4.修复了豺狼炼药长老的闪退问题\n" +
                        "5.修复了鱼与飞镖的故事飞镖异常问题"
        ));

        changes.addButton( new ChangeButton(new BuffIcon(BuffIndicator.CORRUPT, true), ("基因突变"),
                ("移除酸液体，在变幻莫测中也移除。")));

    }

    public static void add_SC3_Changes( ArrayList<ChangeInfo> changeInfos ) {


        ChangeInfo changes = new ChangeInfo("v0.6.9.6-RC", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes.addButton( new ChangeButton(Icons.get(Icons.LANGS), Messages.get(ChangesScene.class, "language"),
                "语言更新：繁体中文\n\n同步0.6.9.6的翻译--贡献者：Sotis"));

        changes.addButton(new ChangeButton(new ShadowSprite(), ("暗影"),
                ("在低于40，包括40灯火的情况下，死亡会产生自己的暗影，他会掠走你的金币和背包。")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.CHALLANEESICON_17), ("挑战改进:孤城迷境"),
                ("魔绫像素地牢-v0696-HF1版更新说明:\n" +
                        "挑战优化:\n" +
                        "1.在6层后才是全精英\n" +
                        "2.在孤城状态下的精英生成异常和支离破碎挑战1层怪物生成异常修正\n" +
                        "3.在黑夜点亮提灯可以免疫失明\n" +
                        "4.修复黑夜状态下领袖伤害没有生效的问题")));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
        changes.hardlight(CharSprite.WARNING);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
                "修复了以下Bug:\n\n" +
                        "1.修复0.6.9.6果子层闪退问题。\n" +
                        "2.修复孤城挑战下的部分NPC的不合理对话\n" +
                        "3.修复油井房在非灯火状态下出现\n" +
                        "4.修复暗影活死人现象"
        ));

        changes = new ChangeInfo("v0.6.9.6", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        Image issxsaxs =new DimandKingSprite();
        issxsaxs.scale.set(PixelScene.align(0.8f));
        changes.addButton(new ChangeButton(issxsaxs, ("拟态之王"),
                ("现在拟态之王二阶段进行了一些优化，并且神秘传送门也进行了一些调整,在特定情况下传送门可以打人。")));

        changes.addButton(new ChangeButton(new SuccubusQueenSprite(), ("变幻莫测V0.4"),
                ("为精英地形添加独立怪物，四区原计划拟态魔，由于一些原因暂时搁置。")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.LANTERNB), ("灯火前路-V4.0"),
                ("灯火从现在开始在10挑以上计为1挑，且难度调整为：" +
                        "\n\n新房间：油井\n" +
                        "在此房间中将提灯扔进井中将可以填充一定的灯油。\n\n" +
                        "随着挑战的深入，火把给予的灯油充能将会更加困难。")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.CHALLANEESICON_17), ("新挑战:孤城迷境"),
                ("这是魔绫像素地牢的最后一个挑战，他非常的困难，所以还请小心行事。")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.SEED_SKYBLUEFIRE), ("种子复制V2.0"),
                ("现在种子复制后会携带一些关键信息")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.CHALLANEESICON_9), ("新精英：炼狱"),
                ("炼狱精英回归，并修复了苦痛精英的问题")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.CHALLANEESICON_10), ("挑战改进:支离破碎"),
                ("支离破碎卷土重来，还带来更多恐怖的东西。")));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
        changes.hardlight(CharSprite.WARNING);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.WHITEROSE), ("白玫瑰"),
                ("修复一些异常错误")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.REDWHITEROSE), ("一种载体"),
                ("取消自然回血加成")));

        changes.addButton(new ChangeButton(Icons.get(Icons.PREFS), ("杂项改动"),
                ("1.部分素材优化迭代\n" +
                        "2.部分无用音乐删除\n" +
                        "3.部分细节优化更新\n" +
                        "4.部分界面优化迭代\n" +
                        "5.字体数据源更新")));

        changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
                "修复了以下Bug:\n\n" +
                        "1.修复了提丰一言不发的问题\n" +
                        "2.修复了小蓝任务的一些异常问题\n" +
                        "3.其他小型错误纠正"
        ));

    }

    public static void add_SC2_Changes( ArrayList<ChangeInfo> changeInfos ) {

        ChangeInfo changes = new ChangeInfo("v0.6.9.5", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        Image issxsaxs =new DimandKingSprite();
        issxsaxs.scale.set(PixelScene.align(0.8f));
        changes.addButton(new ChangeButton(issxsaxs, ("拟态之王"),
                ("现在拟态之王二阶段进行了一些优化，并且神秘传送门也进行了一些调整。")));

        changes.addButton( new ChangeButton(new Image(Assets.Environment.TILES_COLD, 48, 80, 16
                , 16), "雕像优化",
                "现在前三区的雕像有独立描述。"));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
        changes.hardlight(CharSprite.WARNING);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(Icons.get(Icons.PREFS), ("杂项改动"),
                ("1.部分素材优化迭代\n" +
                        "2.部分无用音乐删除\n" +
                        "3.部分细节优化更新\n" +
                        "4.部分界面优化迭代\n" +
                        "5.字体数据源更新")));

        changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
                "修复了以下Bug:\n\n" +
                        "1.修复了跳楼房相关问题\n" +
                        "2.修复了30层的一些崩溃错误"
        ));

        changes = new ChangeInfo("v0.6.9.4", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "inter"), false, null);
        changes.hardlight(Window.GDX_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(NetIcons.get(NetIcons.GLOBE), ("网络时间同步系统V2"),
                ("在游戏进入时，会自动同步网络时间，以防止通过修改时间作弊活动。同时修正时差问题。")));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
        changes.hardlight(CharSprite.WARNING);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.GREEN_DARK), ("新投武：流光飞刀"),
                ("修复一些错误。")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.RED_BLOOD), ("新投武：土木人之怒"),
                ("修复一些错误。")));

        changes = new ChangeInfo("v0.6.9.2-3", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes.addButton( new ChangeButton(Icons.get(Icons.LANGS), Messages.get(ChangesScene.class, "language"),
                "添加繁体中文翻译---感谢Sotis提供的文本"));

        changes.addButton( new ChangeButton(new Image(Assets.Environment.TILES_COLD, 48, 80, 16
                , 16), "房间改动&素材优化",
                "全新秘密房间：神秘实验炼金室加入\n\n" +
                        "仙人跳房间优化：\n\n" +
                        "现在有未祝福的十字架死亡时，遗物始终在每层入口处。\n\n" +
                        "矮人区素材迭代更新！！！"));

        changes.addButton(new ChangeButton(PasswordBadgeBanner.image(PaswordBadges.Badge.ZQJ_GHOST.image), ("中秋复刻加长"),
                ("复刻时间再次追加，并且这次有网络时间验证，请在规定时间完成活动获得徽章。\n\n复刻持续到2月5号0点结束,请各位冒险者抓紧时间。错过了就要等到永久货币实装了。")));

        changes.addButton(new ChangeButton(NetIcons.get(NetIcons.GLOBE), ("网络时间同步系统"),
                ("在游戏进入时，会自动同步网络时间，以防止通过修改时间作弊活动。")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.GREEN_DARK), ("新投武：流光飞刀"),
                ("刀刃由翡翠雕刻而成的飞刀，锋利无比，轻巧耐用\n\n" +
                 "4阶，8～18伤害，2～4伤害成长，耐久20（升级无法提升耐久）\n\n" +
                 "在命中地面或生物时，会释放耀眼的光芒，使5*5范围内的敌人失明4+（等级/4）回合（会失明自己）\n\n" +
                 "（会留在敌人身上）\n\n" +
                        "注意：有一个小bug是剩余1耐久用出不会销毁，而是让你捡到0个。捡了吗？如捡。")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.RED_BLOOD), ("新投武：土木人之怒"),
                ("是建造这里时留下的吗，土木工程圣锤！\n\n" +
                 "3阶，16～24伤害，2～6伤害成长，初始耐久8，攻击需要两回合\n\n" +
                 "命中敌人时眩晕敌人3+（等级/4）回合\n\n" +
                 "（不会留在敌人身上）")));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
        changes.hardlight(CharSprite.WARNING);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new DragonGirlBlueSprite(), ("远古遗迹v1.2"),
                ("升级最多一次，剩余的再次对话全部换为金币，10 ：1的比列。10分等于1金币。")));

        changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
                "修复了以下Bug:\n\n" +
                        "1.修复了六区秘密房间的越界问题\n" +
                        "2.修复了提丰在强制击杀狗子不能对话的问题\n" +
                        "3.修复决斗家在部分徽章的显示异常\n" +
                        "4.修复幽灵护符在部分拥有狂暴阶段（例如豺狼暴徒）的敌人被代码杀仍然残留护盾的问题\n" +
                        "5.修复FireBase提到的各种报错\n" +
                        "6.修复粉色幽灵的古老花园可以生成跳楼房的错误\n" +
                        "7.修复部分子层可以产生升级卷轴的错误\n" +
                        "8.修复灯火效果异常问题"
        ));

        changes.addButton(new ChangeButton(Icons.get(Icons.AUDIO), ("部分音乐进行调整"),
                ("1.冰雪层音乐迭代---泰坦之魂冻土层\n\n" +
                        "2.恶魔层音乐迭代----破碎地牢的地狱轨道2")));

        changes.addButton(new ChangeButton(Icons.get(Icons.PREFS), ("杂项改动"),
                ("1.部分素材优化迭代\n" +
                 "2.部分无用音乐删除\n" +
                 "3.部分细节优化更新\n" +
                 "4.部分界面优化迭代\n" +
                 "5.字体数据源更新")));

        changes.addButton(new ChangeButton(new RedDragonSprite(), ("红龙之王"),
                ("现在红龙之王的奖励窗口不能被动关闭，并且UI进行一些调整。")));

        Image issxsaxss =new DimandKingSprite();
        issxsaxss.scale.set(PixelScene.align(0.8f));
        changes.addButton(new ChangeButton(issxsaxss, ("拟态之王"),
                ("现在拟态之王二阶段进行了一些优化，并且神秘传送门也进行了一些调整。")));

        changes.addButton(new ChangeButton(new FrankensteinSprite(), ("怪物调整：未尽梦偶"),
                ("移速有所提升。")));

        changes.addButton(new ChangeButton(new ApprenticeWitchSprite(), ("怪物调整：见习魔女"),
                ("攻击优化调整")));

        changes.addButton(new ChangeButton(new ButcherSprite(), ("怪物调整：电锯狂人"),
                ("现在习得相位传送")));

        changes.addButton(new ChangeButton(new PumkingBomberSprite(), ("怪物调整：南瓜姜饼自爆人"),
                ("现在爆炸威力更加强大")));

        changes.addButton(new ChangeButton(new MimicSprite.HollowWall(), ("墙怪"),
                ("现在墙怪会在6区刷新在一个随机门的位置")));
    }

    public static void add_SC1_Changes( ArrayList<ChangeInfo> changeInfos ) {
        ChangeInfo changes = new ChangeInfo("v0.6.9.1", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
        changes.hardlight(CharSprite.WARNING);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(Icons.get(Icons.PREFS), ("杂项改动"),
                ("1.部分素材优化\n" +
                        "2.开发者模式优化")));

        changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
                "修复了以下Bug:\n\n" +
                        "1.修复了六区的越界问题\n" +
                        "2.修复了仙人跳房间在六区是非特别版本\n" +
                        "3.修复了部分房间缺少钥匙的问题"
        ));

        Image cr = new CerberusSprite();
        cr.scale.set(PixelScene.align(0.7f));
        changes.addButton(new ChangeButton(cr, ("地狱噩梦-刻耳柏洛斯"),
                ("添加死亡处刑动画，和提丰相关对话。")));

        changes.addButton(new ChangeButton(new PumkingBomberSprite(), ("新怪物-南瓜姜饼自爆人"),
                ("速度降低，血量提升")));

        changes = new ChangeInfo("新NPC", false, null);
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new TyphonSprite(), ("万妖之祖-提丰"),
                ("虽然这个世界不在我的管理范围之内，但古堡的一切都不应该存在于这里。")));


    }


    public static void add_SC_Changes( ArrayList<ChangeInfo> changeInfos ) {
        ChangeInfo changes = new ChangeInfo("v0.6.9.0", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(Icons.get(Icons.TALENT), ("元损机制"),
                ("添加了元损机制，小心行事。")));

        changes = new ChangeInfo("新Boss", false, null);
        changes.hardlight(Window.R_COLOR);
        changeInfos.add(changes);

//        Image mp = new MorpheusSprite();
//        mp.scale.set(PixelScene.align(0.6f));
//        changes.addButton(new ChangeButton(mp, ("影境之主-墨菲厄斯"),
//                ("那么，盛大的剧目即将开演。\n\n该数据已预载，将在万圣节活动启用")));

        Image cr = new CerberusSprite();
        cr.scale.set(PixelScene.align(0.7f));
        changes.addButton(new ChangeButton(cr, ("地狱噩梦-刻耳柏洛斯"),
                ("死亡会找到每一个人，而你，则会优先死去。")));

        changes = new ChangeInfo("新怪物", false, null);
        changes.hardlight(Window.CYELLOW);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new FrankensteinSprite(), ("新怪物-未尽梦偶"),
                ("未见其名，未尽之事，被主抛弃，未知消逝……")));

        changes.addButton(new ChangeButton(new ApprenticeWitchSprite(), ("新怪物-见习魔女"),
                ("不给糖就捣蛋！")));

        changes.addButton(new ChangeButton(new ButcherSprite(), ("新怪物-电锯狂人"),
                ("Here is Johny!!!")));

        changes.addButton(new ChangeButton(new PumkingBomberSprite(), ("新怪物-南瓜姜饼自爆人"),
                ("Happy Happy Happy")));

        changes = new ChangeInfo("新NPC", false, null);
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

//        changes.addButton(new ChangeButton(new SliceGirlSprite(), ("外神碎片-幽寂"),
//                ("古堡一切皆为幻想，冒险者，你真的知道自己的真实情况吗？")));
//
//        changes.addButton(new ChangeButton(new TyphonSprite(), ("万妖之祖-提丰"),
//                ("虽然这个世界不在我的管理范围之内，但古堡的一切都不应该存在于这里。\n\n该数据已预载，将在万圣节活动启用")));

        Image crx = new ZeroBoatDiedSprite();
        crx.scale.set(PixelScene.align(0.7f));
        changes.addButton(new ChangeButton(crx, ("灵魂摆渡人-卡戎"),
                ("想过冥河，先交钱（")));


        changes = new ChangeInfo("说明", false, null);
        changes.hardlight(Window.SHPX_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new MimicSprite.HollowWall(), ("六区说明"),
                ("六区属于特别区域，皆在为挑战极限的朋友进行。")));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
        changes.hardlight(CharSprite.WARNING);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(Icons.get(Icons.PREFS), ("杂项改动"),
                ("1.部分素材优化迭代\n" +
                        "2.部分无用音乐删除\n" +
                        "3.部分细节优化更新\n" +
                        "4.部分界面优化迭代\n" +
                        "5.字体数据源更新")));

        changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
                "修复了以下Bug:\n\n" +
                        "1.修复了玫瑰卷轴使用异常问题\n" +
                        "2.修复了部分特效异常问题\n" +
                        "3.修复了部分灯火异常问题\n" +
                        "4.修复了沉睡符石的异常问题\n" +
                        "5.修复了很多崩溃错误\n" +
                        "6.现在种子局可以被计入排行榜，但最终得分减半"
        ));
    }

    public static void add_v0_6_8_0_Changes( ArrayList<ChangeInfo> changeInfos ) {

        ChangeInfo changes = new ChangeInfo("v0.6.8.0-Release2", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(Icons.get(Icons.TALENT), ("新气体环境-圣咒"),
                ("在圣咒气体环境中，英雄将有概率获得诅咒和虚弱效果，怪物将有概率获得护盾。\n\n" +
                        "目前此效果与新黑色怨灵绑定，未来会出新植物和buff调整以供玩家使用。")));

        changes.addButton(new ChangeButton(new SpectralNecromancerSprite(), ("新怪物-招魂法师"),
                ("它们和死灵法师一样，也喜欢召唤死灵帮忙，不同的是，它们从不嫌自己的召唤的死灵多。")));

        changes.addButton(new ChangeButton(new PoltergeistSprite(), ("黑色怨灵重做"),
                ("黑色怨灵现在不在监狱做常规怪，转而变成精英怪。是招魂法师的精英体。\n\n" +
                        "版本兼容性：老存档仍然是以前那个黑色怨灵。")));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
        changes.hardlight(CharSprite.WARNING);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new ShamanSprite.Purple(), ("萨满组"),
                ("在矿洞14层会生成。")));

        changes.addButton(new ChangeButton(Icons.get(Icons.PREFS), ("杂项改动"),
                ("1.部分素材优化迭代\n" +
                        "2.部分无用音乐删除\n" +
                        "3.部分细节优化更新\n" +
                        "4.部分界面优化迭代")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.CHALLANEESICON_5), ("怪组优化"),
                ("监狱怪组进行了优化，不再会出现6层典狱长，DM100(支离破碎除外)\n\n" +
                        "支离破碎后面要重做。")));

        changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
                "修复了以下Bug:\n\n" +
                        "1.修复了警报陷阱的一些小型崩溃错误\n" +
                        "2.部分文案修正\n" +
                        "3.修复“净化怨灵根源”获得异常问题"
        ));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "buffs"), false, null);
        changes.hardlight( CharSprite.POSITIVE );
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new DragonGirlBlueSprite(), ("远古遗迹v1.1"),
                ("在玩家尚未获得“远古开拓者”前，固定18层生成。\n\n" +
                        "如已获得该徽章，则会在后续以49%的概率生成。")));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "nerfs"), false, null);
        changes.hardlight( CharSprite.NEGATIVE );
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new ColdRatSprite(), ("寒冰魔鼠"),
                ("护甲现在不是固定10，而是最高为10，最低为5。\n\n" +
                        "但攻击精准度提高两点，最大等级限制从18级变成17级\n\n" +
                        "获取经验从12变成9经验，\n\n" +
                        "失明从30调整为6回合\n\n" +
                        "且现在可以20%概率掉落金币。")));

        changes.addButton(new ChangeButton(new NxhySprite(), ("那些回忆大商店"),
                ("现在不再是固定刷新，仅有20%的概率生成该额外商店。")));

        changes = new ChangeInfo("v0.6.8.0-Release", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
        changes.hardlight(CharSprite.WARNING);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
                "修复了以下Bug:\n\n" +
                        "1.修复了新手教程完成前可以使用v1快捷栏导致的崩溃问题\n" +
                        "2.部分文案修正\n" +
                        "3.修复萨卡班甲鱼Boss层未祝福十字架重置楼层的相关问题\n" +
                        "4.修复部分Boss徽章职业获得异常问题"
        ));

        changes.addButton(new ChangeButton(Icons.get(Icons.PREFS), ("杂项改动"),
                ("1.部分素材优化迭代\n" +
                        "2.部分小细节优化更新")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.ARTIFACT_ROSE3), ("干枯玫瑰"),
                ("悲伤幽灵的文本变得更加丰富，\n\n" +
                        "包括DM720,冰雪魔女,拟态之王")));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "nerfs"), false, null);
        changes.hardlight( CharSprite.NEGATIVE );
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.LANTERNB), ("灯火前路-V3.4"),
                ("以下Buff被削弱：\n\n" +
                        "前路现在在极度饥饿失去效果,且仅在灯火高于90(包括90)才能生效。\n" +
                        "提灯：\n\n" +
                        "充能文案修复，火把减低为50%充能。")));

        changes.addButton( new ChangeButton(new Image(Assets.Environment.TILES_SEWERS, 48, 80, 16, 16 ), "房间改动",
                "太极房间现在最高+2，\n" +
                        "且裂缝区域必定诅咒高等级，\n水面区域必定非诅咒但有概率白板。"));

        changes = new ChangeInfo("v0.6.8.0-Beta3", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new MiniSakaFishBossSprites(), ("宠物系统-v0.1"),
                ("宠物系统现在开始加入游戏中，目前有观赏功能形宠物一只。快去试试看吧。")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.FISHSKELETON), ("萨卡班甲鱼鱼骨重做"),
                ("重做了这个物品，现在它同时也是首个宠物道具。")));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
        changes.hardlight(CharSprite.WARNING);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(Icons.get(Icons.PREFS), ("杂项改动"),
                ("1.部分素材优化迭代\n" +
                        "2.部分无用音乐删除\n" +
                        "3.部分细节优化更新\n" +
                        "4.部分界面优化迭代")));

        changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
                "修复了以下Bug:\n\n" +
                        "1.修复了部分文案丢失\n" +
                        "2.修复了部分界面浏览崩溃\n" +
                        "3.修复了PC版本部分快捷键不生效的问题\n" +
                        "4.部分楼层错误坐标和盟友无法加入战斗的问题\n" +
                        "5.修复钥匙剑的全局共享问题\n" +
                        "5.其他崩溃Bug修正"
        ));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "buffs"), false, null);
        changes.hardlight( CharSprite.POSITIVE );
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.RANDOM_CHEST), ("补给箱"),
                ("价钱降低为350金币，\n\n" +
                        "且钥匙剑\n魔药和秘卷\n萨卡班甲鱼骨头(需要前置徽章:远古生物调查员)加入池子\n\n" +
                        "背包类型移出奖励池子。")));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "nerfs"), false, null);
        changes.hardlight( CharSprite.NEGATIVE );
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.DG3), ("钥匙剑"),
                ("钥匙剑现在只能通过钥匙升级，\n且只有以下钥匙加经验：\n\n" +
                        "1.铁钥匙5经验\n" +
                        "2.黄金钥匙15经验\n" +
                        "3.水晶钥匙25经验")));

        changes = new ChangeInfo("v0.6.8.0-Beta2", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
        changes.hardlight(CharSprite.WARNING);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(Icons.get(Icons.PREFS), ("杂项改动"),
                ("1.部分素材优化迭代\n" +
                        "2.部分界面优化")));

        changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
                "修复了以下Bug:\n\n" +
                        "1.修复了部分文案丢失\n" +
                        "2.修复了部分界面浏览崩溃\n" +
                        "3.修复传说书籍的相关问题"
        ));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "buffs"), false, null);
        changes.hardlight( CharSprite.POSITIVE );
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(BadgeBanner.image(Badges.Badge.NYZ_SHOP.image), ("徽章调整"),
                ("获得该徽章后初始金币从320金币-->400金币")));

        changes.addButton(new ChangeButton(PasswordBadgeBanner.image(PaswordBadges.Badge.EXSG.image), ("徽章调整"),
                ("获得该徽章后初始金币从720金币-->648金币")));

        changes = new ChangeInfo("v0.6.8.0-Beta1", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(HeroSprite.avatar(HeroClass.DUELIST, 7), ("武技实装第一轮"),
                ("部分武器武技已经实装，欢迎尝鲜")));

        changes.addButton(new ChangeButton(new DragonGirlBlueSprite(), ("远古遗迹强势归来"),
                ("远古遗迹重大更新，新增3个怪物，3+1个徽章，还有更多惊喜，欢迎前来体验。")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.RANDOM_CHEST), ("随机宝箱"),
                ("现在0层十字架和刻笔换成随机宝箱，你究竟是欧皇还是酋长，快来抽抽看吧。")));


        changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
        changes.hardlight(CharSprite.WARNING);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(HeroSprite.avatar(HeroClass.MAGE, 7), ("英雄调整"),
                ("人物从初始自带一瓶冶疗改为\n\n" +
                        "1.战士初始为冶疗药剂\n" +
                        "2.法师初始为液火药剂\n" +
                        "3.女猎为灵视药剂\n" +
                        "4.盗贼为隐身药剂\n" +
                        "5.武斗为镜像卷轴")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.ARTIFACT_ROSE3), ("0层调整"),
                ("神器初始不再给予，改为牛皮书袋。")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.WHIP), ("冰澪匕首"),
                ("以前的冰霜长鞭改为冰澪匕首，伤害不变。武技视觉效果优化。")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.MAGIC_TORCH), ("魔法火把"),
                ("武技视觉效果优化和完善武技")));

        changes.addButton(new ChangeButton(Icons.get(Icons.PREFS), ("杂项改动"),
                ("1.部分素材优化迭代\n" +
                        "2.部分无用音乐删除\n" +
                        "3.少量Bug修复\n" +
                        "4.部分小细节优化更新")));

        changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
                "修复了以下Bug:\n\n" +
                        "1.修复了女猎解锁异常问题\n" +
                        "2.修复矮人国王相关问题\n" +
                        "3.修复了暴力Boss的额外拳头血量异常\n" +
                        "4.修复了部分文案缺失问题\n" +
                        "5.修复了盗贼血影蝙蝠血量异常问题"
        ));


        changes = new ChangeInfo(Messages.get(ChangesScene.class, "buffs"), false, null);
        changes.hardlight( CharSprite.POSITIVE );
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.WAR_HAMMER), ("白檩剑"),
                ("以前的白金真银战锤大剑改为白檩剑，精准度提高，其余不变")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.DG2), ("蛮人战斧"),
                ("武技视觉效果优化和完善武技，自身属性加强")));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "nerfs"), false, null);
        changes.hardlight( CharSprite.NEGATIVE );
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.YELLOWBOOKS), ("传说书籍"),
                ("现在如果存在英雄精英效果的状态下，不能继续阅读其他传说书籍。同时传说书籍在商人售卖处价格上涨。奈亚子终端仍然是720金币。")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.DG3), ("钥匙剑"),
                ("钥匙剑现在只能通过钥匙升级，且获得途径仅在常规商人处有概率出现，且商人处可能有等级，但价钱也会上涨")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.LANTERNB), ("灯火前路-V3.3"),
                ("以下Buff被削弱：\n\n" +
                        "1.前路现在在极度饥饿失去效果\n" +
                        "2.共生仅在一区有概率出现\n" +
                        "3.富饶从三折改为六折\n" +
                        "4.守护护盾刷新回合从150改为270")));

        changes.addButton( new ChangeButton(new BuffIcon(BuffIndicator.CORRUPT, true), ("基因突变"),
                ("移除酸液体")));

    }

    public static void add_v0_6_7_0_Changes( ArrayList<ChangeInfo> changeInfos ) {
        ChangeInfo changes = new ChangeInfo("v0.6.7.0", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(HeroSprite.avatar(HeroClass.DUELIST, 7), ("艾诺琳娜"),
                ("现已修复决斗家的相关问题，部分武技后续实装，敬请期待")));

        changes.addButton(new ChangeButton(Icons.get(Icons.PREFS), ("杂项改动"),
                ("1.部分素材优化迭代\n" +
                        "2.部分无用音乐删除\n" +
                        "3.少量Bug修复\n" +
                        "4.部分小细节优化更新")));

        changes.addButton(new ChangeButton(Icons.get(Icons.DISPLAY), ("UI优化"),
                ("部分页面的UI已经进行了优化，带给玩家更好的游戏体验")));

        changes.addButton(new ChangeButton(Icons.get(Icons.DISPLAY_PORT), ("快捷键优化"),
                ("PC键位有一点问题，已进行优化处理")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.MASTERY), ("新手引导&更多指南"),
                ("1.现在如果是第一次打开魔绫，将会有新手引导\n" +
                        "2.添加了更多引导指南，欢迎探索")));


        changes = new ChangeInfo(Messages.get(ChangesScene.class, "nerfs"), false, null);
        changes.hardlight( CharSprite.NEGATIVE );
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new DiedMonkLoaderSprite(), ("矮人将军重做"),
                ("矮人将军纳入重做范围，敬请期待后续更新。")));

        changes.addButton(new ChangeButton(new FlowerSlimeSprites(), ("繁花史莱姆"),
                ("为了确保玩家能更好的在前期发展，繁花史莱姆面板进行了优化。具体如下：\n\n" +
                        "血量：12--->随机5到9\n" +
                        "伤害：2-5--->1-5\n" +
                        "种子掉率:5%-->9%")));

        changes.addButton(new ChangeButton(new CrivusFruitsSprite(), ("克里弗斯之果"),
                ("二阶段触手玩家攻击后，保底拥有4回合的回复回合")));

    }

    public static void add_v0_6_6_0_Changes( ArrayList<ChangeInfo> changeInfos ) {
        ChangeInfo changes = new ChangeInfo("v0.6.6.0", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(Icons.get(Icons.PREFS), ("杂项改动"),
                ("种子方面改动如下：\n" +
                        "1.种子存档会显示绿色存档提示玩家\n" +
                        "2.种子可以在存档信息界面直接看见\n" +
                        "3.种子局最终结算总分减半\n" +
                        "4.种子局排行榜的样子有所不同\n" +
                        "\n其他方面：\n" +
                        "1.武器/法杖重命名功能回归\n" +
                        "2.迭代优化了一些素材\n" +
                        "3.底层代码优化")));

        changes.addButton( new ChangeButton(new BuffIcon(BuffIndicator.FIREDIED, true), ("水晶意志"),
                ("魔绫的返程之旅，准备好接受考验了吗？")));

        changes.addButton(new ChangeButton(Icons.get(Icons.CHANGES), ("重大更新"),
                ("魔绫现已更新底层到破碎V2.2.1版本!" +
                        "鸣谢：\n\n" +
                        "-首席内部测试：bzmdr\n" +
                        "-素材迭代优化：Danicel\n\n" +
                        "以及MLPD的开发团队的所有人员和广大玩家,\n" +
                        "两年以来，MLPD感谢有你")));

        changes.addButton(new ChangeButton(HeroSprite.avatar(HeroClass.DUELIST, 7), ("新英雄：艾诺琳娜"),
                ("破碎版本的决斗家，现已正式纳入MLPD。")));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "buffs"), false, null);
        changes.hardlight( CharSprite.POSITIVE );
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.ARTIFACT_CHALICE3), ("蓄血圣杯"),
                ("同步回1.2.3的圣杯形式")));

        changes.addButton( new ChangeButton(new BuffIcon(BuffIndicator.ARMOR, true), ("灯火前路-守护"),
                ("现在护盾不会自然衰减，且只要护盾不消失，可以缓慢回复护盾。")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.GAUNTLETS), ("碧灰双刃"),
                ("伤害提升，并且升为四阶武器")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.SEED_AIKELAIER), ("闪电花"),
                ("用该物品制作的无味果改名为雷暴果，拥有闪电链式反应效果。")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.WAND_HTR), ("风暴雷霆法杖"),
                ("使用法杖在水中攻击时，有10%的概率获得3次的闪电链式反应效果")));

        changes.addButton( new ChangeButton(new BuffIcon(BuffIndicator.CORRUPT, true), ("鬼磷精英"),
                ("现在死亡时会释放磷火。")));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "nerfs"), false, null);
        changes.hardlight( CharSprite.NEGATIVE );
        changeInfos.add(changes);

        changes.addButton( new ChangeButton(new BuffIcon(BuffIndicator.LIGHT_DIED, true), ("闪电链式反应"),
                ("现在必须在可见范围内才能攻击敌人，" +
                        "伤害小幅提升，并且调整为闪电链不自然衰减。")));

        changes.addButton( new ChangeButton(new BuffIcon(BuffIndicator.CORRUPT, true), ("精英挑战优化调整"),
                        "_-_A.精英强敌\n\n" +
                        "1.烈焰精英：不会在含有的水的地块上面死亡产生火焰\n\n" +
                        "2.索敌精英：现在不再是无限格子，而是4格\n\n" +
                        "3.鬼磷精英：现在攻击敌人不再必定追加磷火效果，调整为10%的概率，但死亡时无论有没有水都能引燃以自身为3x3的半径的鬼磷火焰。",
                        "_-_B.基因突变\n\n" +
                        "1.突变酸液体：现在生成概率5%，且每大层最多累计出现3个，在击败关键Boss后计算清铃。并且酸液有限，消耗完酸液后，变为常规攻击。\n\n" +
                        "2.新的突变体：\n\n" +
                        "突变相位体：这种突变体在前面4次攻击命中敌人后会随机传送，并且不处于你视野中时可缓慢回复生命。"));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
        changes.hardlight(CharSprite.WARNING);
        changeInfos.add(changes);

        changes.addButton( new ChangeButton(new RedDragonSprite(), ("红龙任务"),
                ("红龙任务进行了一些优化，并正式加入0.6.6.0的游戏中。")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.LANTERNB), ("DLC模式返修"),
                ("BossRush等DLC模式返修中，敬请期待后续版本。")));

        Image i = new YogSprite();
        i.scale.set(0.8f);
        changes.addButton(new ChangeButton(i, ("YogDzewa"),
                ("对梦魇领袖进行了一些优化")));

        Image s = new DM720Sprite();
        s.scale.set(0.8f);
        changes.addButton(new ChangeButton(s, ("DM720"),
                ("现在如果在DM720中使用十字架重生不会roll到DM300战斗楼层，DM300也同理")));

        changes.addButton(new ChangeButton(new MolotovHuntsmanSprite(), ("血月赏金猎人长老"),
                ("DM720与长老贴图优化，并修复了一些bug")));

        changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
                "修复了以下Bug:\n\n" +
                        "特别感谢首席测试官_bzmdr_的大力支持\n\n" +
                        "Demo1(内部测试):\n\n" +
                        "_-_ 1.修复了版本迁移后25层的闪退问题\n" +
                        "_-_ 2.修复了护甲贴图，数值错乱\n" +
                        "_-_ 3.修复了排行榜界面必定崩溃的问题\n" +
                        "_-_ 4.修复了战士初始武器攻击距离异常的问题\n" +
                        "_-_ 5.修复了部分极端情况下Buff显示字段缺失\n" +
                        "_-_ 6.修复了结束游戏点击排行榜爆炸的问题\n" +
                        "_-_ 7.修复了板甲贴图错位\n" +
                        "_-_ 8.修复了部分素材的位置不符合\n" +
                        "_-_ 9.修复了图鉴闪退的问题\n" +
                        "_-_ 10.修复了部分天赋文本缺失的问题\n" +
                        "_-_ 11.修复了开发者模式怪物生成器的一些问题",

                "Demo2(内部测试):\n\n" +
                        "_-_ 1.修复了王冠转移能给鱼甲的问题，并且补充缺失文本\n" +
                        "_-_ 2.修复了部分英雄贴图定位问题\n" +
                        "_-_ 3.修复了基因突变，支离破碎，药水癔症在迁移后不生效的问题\n" +
                        "_-_ 4.修复了迁移后矮人将军不能正常出现的问题\n" +
                        "_-_ 5.修复了霜焰审判卷轴使用不扣减数量的问题\n" +
                        "_-_ 6.修复了toSting错误回环逻辑导致的文本显示异常\n" +
                        "_-_ 7.修复了幻影食人鱼的文本缺失和鱼肉贴图问题\n" +
                        "_-_ 8.修复了武力之戒的文本缺失问题\n" +
                        "_-_ 9.修复了文本气泡的问题\n" +
                        "_-_ 10.修复了炫目诅咒文本缺失问题",

                "Demo3(内部测试):\n\n" +
                        "_-_ 1.修复了使用卷轴额外多扣一个使用数量的问题\n" +
                        "_-_ 2.修复了艾诺琳娜的技能文本缺失问题\n" +
                        "_-_ 3.修复了部分徽章贴图错乱和文本异常问题\n" +
                        "_-_ 4.修复了部分Buff图标错位的问题",

                "Demo4(内部测试):\n\n" +
                        "1:元素怪物攻击造成栈溢出崩溃-结果判定：Mob迁移后未优化，导致爆栈\n" +
                        "2:决斗一层天赋文本 \n-结果判定：一般问题--文本缺失\n" +
                        "3:短柄镰在武器生成器的贴图和位置\n" +
                        "4:新版无诅咒的诅咒附魔武器文本-结果判定：一般问题--文本缺失\n" +
                        "5:水晶尖塔可以被十字镐以外的武器伤害-结果判定：一般问题--逻辑缺陷\n" +
                        "6.开发者模式升降器探索物品功能缺失-结果判定：一般问题--逻辑缺陷\n" +
                        "7.书籍一本时无法阅读-结果判定：一般问题--逻辑缺陷\n" +
                        "8.嬗变泉房间扔东西无法转换-结果判定：一般问题--功能缺陷\n" +
                        "9.DM275攻击策略优化-结果判定：一般问题--优化体验\n" +
                        "10.优化那些回忆，奈亚子的对话框架（现在的比较卡顿）\n" +
                        "结果判定：一般问题--优化体验\n" +
                        "11.需要修复部分界面的严重贴图错乱-结果判定：一般问题--优化体验\n" +
                        "12.需要修复一个4阶武器贴图缺失的问题-结果判定：一般问题--优化体验\n" +
                        "13.需要修复咒缚灵生成失效的问题\n\n" +
                        "以上问题均已修复",

                "Demo5(大群首测):\n\n"+
                "1.修复了草鞋灌注闪退的问题\n" +
                        "2.修复了天狗闪退的问题\n" +
                        "3.修复了新版对话框的异常问题\n" +
                        "4.修复了幻影食人鱼鱼肉贴图和文本缺失的问题\n" +
                        "5.修复了天赋文本缺失的问题" +
                        "6.修复了来自FireBase报告的诸多错误",


                "Demo6-7:\n\n" +
                        "1.修复了幻影食人鱼文本贴图缺失的问题\n" +
                        "2.修复了英雄精英buff尚未生效的问题\n" +
                        "3.同步了蓄血圣杯为原版1.2.3\n" +
                        "4.修复了BR的冲突问题\n" +
                        "5.修复了决斗家的10层闪退bug\n" +
                        "6.修复了子层下楼的错误问题\n" +
                        "7.其他来自FireBase后台报告的Bug集中修复",
                "Demo8:\n\n" +
                        "1.修复了Dm275和矮人将军地图的相关问题，新存档开始生效\n" +
                        "2.修复了错误生成尚未整改的丛林毒刺问题\n" +
                        "3.部分素材贴图迭代优化\n" +
                        "4.其他来自FireBase后台报告的Bug集中修复",
                "RC:\n\n" +
                        "1.修复了返程相关问题\n" +
                        "2.修复了变幻莫测的一些问题\n" +
                        "3.部分素材贴图迭代优化\n" +
                        "4.其他来自FireBase后台报告的Bug集中修复"));




    }

}
