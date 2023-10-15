package com.shatteredpixel.shatteredpixeldungeon.ui.changelist.mlpd;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.PaswordBadges;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.PinkLingSprite;
import com.shatteredpixel.shatteredpixeldungeon.effects.BadgeBanner;
import com.shatteredpixel.shatteredpixeldungeon.effects.PasswordBadgeBanner;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.ChangesScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ApprenticeWitchSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CrivusFruitsSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DM300AttackSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.FireDragonSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.FlowerSlimeSprites;
import com.shatteredpixel.shatteredpixeldungeon.sprites.FrankensteinSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GhostSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.IceSlowGirlSprites;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.RedNecromancerSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SakaFishBossSprites;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SalamanderSprites;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ShopkeeperSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SlimeKingSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.ui.changelist.ChangeButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.changelist.ChangeInfo;
import com.watabou.noosa.Image;

import java.util.ArrayList;

public class vM0_6_4_P_Changes {
    public static void addAllChanges(ArrayList<ChangeInfo> changeInfos) {
        add_v0_6_56_Changes(changeInfos);
        add_v0_6_55_Changes(changeInfos);
        add_v0_6_54_Changes(changeInfos);
    }
    public static void add_v0_6_56_Changes( ArrayList<ChangeInfo> changeInfos ) {
        ChangeInfo changes = new ChangeInfo("Coming Soon V0.6.6.0", true, "");
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

        changes.addButton(new ChangeButton(Icons.get(Icons.CHANGES), ("底层迭代"),
                ("魔绫的底层版本现在是1.2.3,然而经过一年的维护,我们必须再一次进行底层同步。\n\n" +
                        "至于迭代哪个破碎，敬请期待冬季版本。")));


        Image s = new DM300AttackSprite();
        s.scale.set(PixelScene.align(0.74f));
        changes.addButton(new ChangeButton(s, ("DM-ZERO"),
                ("时间在这里停止流动，与DMZERO正面对决的即将到来")));

        Image c = new IceSlowGirlSprites();
        c.scale.set(PixelScene.align(0.74f));
        changes.addButton(new ChangeButton(c, ("冰雪魔女重做"),
                ("石碑上记录了有关于她的一切，她的过去，她的现在，她的未来。而当你与她对峙时，你是否能够看清她的真实面目？")));



        changes = new ChangeInfo("v0.6.5.0-Alpha6-Patch6", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes = new ChangeInfo("预载", false, null);
        changes.hardlight(Window.CBLACK);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new ApprenticeWitchSprite(), ("新中立怪物:见习魔女"),
                ("魔法学院的劣等生，见习中的魔女。\n\n该数据已预载，将在万圣节活动启用。")));

        changes.addButton(new ChangeButton(new FrankensteinSprite(), ("新怪物:未尽梦偶"),
                ("它在寻找它的创造者，以及，撕碎所有拥有生命的气息。\n\n该数据已预载，将在万圣节活动启用。")));

        changes = new ChangeInfo("新内容", false, null);
        changes.hardlight(Window.GREEN_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.LANTERNB), ("灯火前路：v3.2"),
                ("1.现在灯火在超过6挑后相当于一个强制挑战，\n\n" +
                        "2.你的灯火和提灯在开局就会有扣减，但并不会影响你在第一大层。\n\n" +
                        "3.修复了怪物不扣减灯火的严重问题，这是自灯火系统来一直尚未修复的问题")));

        changes = new ChangeInfo("改动", false, null);
        changes.hardlight(Window.CYELLOW);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.DG25), ("开发者模式v0.7"),
                ("在开发模式中，支离破碎第1层是测试怪组。")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.BloodDied), "嗜血荆棘",
                "1.修复在特殊情况下献祭的问题，现在最多献祭到+10\n\n" +
                        "2.如果仍然在卡荆棘的问题，你将会在+14以上后(不包括+14)看见0伤害，且力量x5的下水道版本荆棘的处罚机制。"));


        changes = new ChangeInfo("v0.6.5.0-Alpha6-Patch5", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes = new ChangeInfo("新内容", false, null);
        changes.hardlight(Window.GREEN_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.LANTERNB), ("灯火前路：v3.1"),
                ("现在灯火最大值为100%，且有益BuffRoll的时候最多同时存在一个。\n\n" +
                        "高挑可能会更加困难，但低挑来说正常。")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.DG25), ("开发者模式v0.6"),
                ("修复了一些Bug")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.CHALLANEESICON_16), ("变幻莫测v0.4"),
                ("1.修复了部分情况下无法传送的问题\n\n" +
                        "2.修复灵视在特定MINIBOSS层无法使用的问题\n\n" +
                        "3.改动界面迭代优化")));

        changes = new ChangeInfo("改动", false, null);
        changes.hardlight(Window.CYELLOW);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(Icons.get(Icons.PREFS), ("杂项修改"),
                ("1.修复了部分文案问题\n\n" +
                        "2.修复了一些错误\n\n" +
                        "3.部分素材优化" )));

        changes.addButton(new ChangeButton(new FlowerSlimeSprites(), ("丛林繁花史莱姆"),
                ("掉落种子的概率从25%改为5%。")));


        changes = new ChangeInfo("v0.6.5.0-Alpha6-Patch4", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);


        changes = new ChangeInfo("新内容", false, null);
        changes.hardlight(Window.GREEN_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.DG25), ("开发者模式v0.5"),
                ("新增武器生成秘卷，感谢潘多拉的代码制作和注解版的代码支持。")));

        changes.addButton(new ChangeButton(Icons.get(Icons.CHALLENGE_ON), ("挑战：变幻莫测V0.3"),
                ("修复了一堆问题，并改善了部分体验")));

        changes = new ChangeInfo("改动", false, null);
        changes.hardlight(Window.CYELLOW);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(Icons.get(Icons.PREFS), ("杂项修改"),
                ("1.修复了部分文案问题\n\n" +
                        "2.调整了一些底层代码" )));

        changes = new ChangeInfo("v0.6.5.0-Alpha6.9-6.93-LAST", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes = new ChangeInfo("新内容", false, null);
        changes.hardlight(Window.GREEN_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton((new Image("Ling.png", 0, 0, 16, 16)), ("开发者的话"),
                ("v0.6.5.0-Alpha6.9-LAST--完成于2023-10-6\n\n" +
                        "今天对我来说是一个特别的日子，因为今天是我21岁生日。\n\n" +
                        "回想MLPD的两年，我也从一名曾经的准大学生变成了即将毕业的大学生\n\n" +
                        "生活的忙碌，社会的考验，实习的困难比比皆是。但好在我有一批非常爱我和这个游戏的玩家\n\n" +
                        "Alpha6.9，是MLPD的近期的最终版本。接下来，她(代指魔绫)将进入休整期阶段，\n\n" +
                        "但游戏的内容足够玩家游玩，当然，如果有bug，我会来修复。\n\n" +
                        "最近只是没有大版本更新了。最后，近期是我的生日，我在游戏中为各位玩家准备了水晶项链。\n\n" +
                        "她应该可以帮助你们更好的游玩游戏，希望水晶项链能给你们带来不错的体验。\n\n" +
                        "最后，让我们在冬季版本大更新中相见，再会！！！\n\n" +
                        "---MLPD主要开发者：JDSALing" )));

        changes.addButton(new ChangeButton(Icons.get(Icons.DISPLAY_LAND), ("农历计算系统"),
                ("现在MLPD支持农历计算系统，中秋节，端午节，以及作者的生日，均使用此系统计算。\n" +
                        "Ling的生日：农历8.22\n\n有效时间范围：8.22-8.25\n\n" +
                        "中秋节：农历8.15\n\n有效时间范围：中秋节前10天到中秋节后12天\n\n" +
                        "端午节：农历5.5\n\n有效时间范围：端午节前3天到端午节后7天\n\n注意：如果与西方节日冲突，优先将用中国传统节日！")));

        changes.addButton(new ChangeButton(Icons.get(Icons.AUDIO), ("新主题BGM"),
                ("在玩家在新版本胜利一次后，有机会在主界面播放新BGM和下半段暗示。")));

        changes.addButton(new ChangeButton(new FlowerSlimeSprites(), ("新生物：丛林繁花史莱姆"),
                ("替代普通老鼠，老鼠现在只会在丛林边缘(第一层生成),后面都是这个新生物。")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.REDWHITEROSE), ("一种载体彻底实装"),
                ("效果:力量+2,移速+10%，每5回合回复1HP")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.CRYSTAL_LING), ("新开发者物品：水晶项链"),
                ("在每年绫的生日有效活得时间直接在开局就能获得，自带韧性之戒效果，治疗效果小幅度提升。")));

        changes.addButton(new ChangeButton(PasswordBadgeBanner.image(PaswordBadges.Badge.NIGHT_CAT.image), (
                "新隐藏徽章：不眠之夜"),
                ("解密片段：月黑风高，古神陨落。")));

        changes.addButton(new ChangeButton(BadgeBanner.image(Badges.Badge.HIDEEN_BADAGEX.image), ("新徽章：隐秘探索"),
                ("获得方式：发现隐藏徽章的位置")));

        changes = new ChangeInfo("改动", false, null);
        changes.hardlight(Window.CYELLOW);
        changeInfos.add(changes);

        Image xax = new SakaFishBossSprites();
        xax.scale.set(PixelScene.align(0.72f));
        changes.addButton(new ChangeButton(xax,  "萨卡班甲鱼",
                "现在免疫恐惧！"));

        changes.addButton(new ChangeButton(new CrivusFruitsSprite(), ("克里弗斯之果"),
                ("现在净化药剂有特定版，且绝命头目地形改变。")));

        changes.addButton(new ChangeButton(Icons.get(Icons.CHALLENGE_ON), ("开发者模式V0.4"),
                ("开发者模式进行一些小型优化，追加新怪物：丛林繁花史莱姆")));

        changes.addButton(new ChangeButton(Icons.get(Icons.CHALLENGE_ON), ("挑战：变幻莫测V0.2"),
                ("修复了一堆问题：\n\n" +
                        "1.可能导致钥匙丢失\n\n2.重复Boss警报\n\n3.看见人不传送\n\n4.改善了Goo和DM300原型机的属性与地形。")));

        changes.addButton(new ChangeButton(Icons.get(Icons.PREFS), ("杂项改动-1"),
                ("1.优化了主题背景界面，并且植入夜间状态，在晚上22点到早上7点前是夜间状态。因此是夜间背景。反之则是白天背景。\n\n" +
                        "2.圣境密林层改善了几个地块,部分敌人素材迭代\n\n" +
                        "3.上个版本的一些FB-bug修复")));

        changes.addButton(new ChangeButton(Icons.get(Icons.PREFS), ("杂项改动-2"),
                ("1.修复了提灯环境下火把能量无法被保存的相关问题\n" +
                        "2.修复了变幻莫测挑战相关问题\n" +
                        "3.修复了DM300原型机无限护盾问题\n" +
                        "4.修复了徽章界面可能闪退的问题\n" +
                        "5.修复了沉沦效果动画丢失问题\n" +
                        "6.优化了部分地块贴图\n" +
                        "7.修复了DM300 DM720进入即过关的问题\n" +
                        "8.修复了隐藏徽章-不眠之夜，无法被获得的问题\n" +
                        "9.新增夜晚文本提醒")));


        changes = new ChangeInfo("v0.6.5.0-Alpha6-6.8-国庆", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes = new ChangeInfo("新内容", false, null);
        changes.hardlight(Window.GREEN_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(Icons.get(Icons.CHALLENGE_ON), ("新挑战：变幻莫测V0.1"),
                ("追加两个随机地形感觉，整体战斗全面处于大地型，怪物生成变多，追加两个迷你Boss战。")));

        changes.addButton(new ChangeButton(Icons.get(Icons.PREFS), ("杂项修改2"),
                ("1.修复三井协定的三井重复生成问题\n" +
                        "2.改善变幻莫测迷你Boss地形以及战利品\n" +
                        "3.种子现在分为A类，B类，BossRush类\n" +
                        "4.部分界面UI迭代，变幻莫测挑战平衡\n" +
                        "5.修复鼠王可以改造特定护甲，例如板鳖甲")));



        changes.addButton(new ChangeButton(Icons.get(Icons.PREFS), ("杂项修改"),
                ("1.迭代了UI材质\n\n2.优化了一些代码")));

        changes.addButton(new ChangeButton(Icons.get(Icons.DEPTH_WELLS), ("更多地形认知"),
                ("在变幻莫测中加入了更多地形认知!")));

        changes = new ChangeInfo("改动", false, null);
        changes.hardlight(Window.CYELLOW);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(Icons.get(Icons.CHALLENGE_ON), ("基因突变"),
                ("现在蝇群和重甲豺狼无法获得基因突变效果")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.LANTERNB), ("灯火平衡"),
                ("灯火进行了一波平衡，使之游玩起来更好玩。")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.CHALLANEESICON_9), ("梦魇领袖"),
                ("梦魇领袖进行了一些调整，并改名为绝命头目")));

        changes.addButton(new ChangeButton(new Image("sprites/spinner.png", 144, 0, 16, 16),
                Messages.get(ChangesScene.class, "bugfixes"), Messages.get(vM0_6_7_X_Changes.class, "bug_06X88")));

        changes.addButton(new ChangeButton(new Image("sprites/spinner.png", 144, 0, 16, 16),
                Messages.get(ChangesScene.class, "bugfixes"), Messages.get(vM0_6_7_X_Changes.class, "bug_06X87")));

        changes = new ChangeInfo("v0.6.5.0-Alpha5.2-国庆", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes = new ChangeInfo("改动", false, null);
        changes.hardlight(Window.CYELLOW);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new Image("sprites/spinner.png", 144, 0, 16, 16),
                Messages.get(ChangesScene.class, "bugfixes"), Messages.get(vM0_6_7_X_Changes.class, "bug_06X86")));

        changes = new ChangeInfo("v0.6.5.0-Alpha5-中秋", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes = new ChangeInfo("新内容", false, null);
        changes.hardlight(Window.GREEN_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new GhostSprite.PinkGhostSprites(), ("新限时任务：映月离合之殇"),
                ("在中秋和国庆节这个期间一起探索悲伤幽灵它们的故事……")));

        changes = new ChangeInfo("改动", false, null);
        changes.hardlight(Window.CYELLOW);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(Icons.get(Icons.INFO), ("修复"),
                ("修复上个版本的一些错误")));

        changes = new ChangeInfo("v0.6.5.0-Alpha4.5", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes = new ChangeInfo("新内容", false, null);
        changes.hardlight(Window.GREEN_COLOR);
        changeInfos.add(changes);

        changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.GREATSWORD,
                new ItemSprite.Glowing( 0xcc7770 )), "新附魔：爆破",
                "这个附魔会让使用者陷入癫狂，能使爆炸的能量从武器中喷薄而出，能够使敌人目标处受到一次范围伤害亦或者给造成敌人短暂的残废效果。（PS:爆炸效果不会摧毁物品）\n"));

        changes.addButton(new ChangeButton(PasswordBadgeBanner.image(PaswordBadges.Badge.ZQJ_GHOST.image), ("两个隐藏徽章资源预载"),
                ("两个隐藏徽章资源预载，将在后续版本更新。")));

        changes = new ChangeInfo("改动", false, null);
        changes.hardlight(Window.CYELLOW);
        changeInfos.add(changes);

        Image isa = new SlimeKingSprite();
        isa.scale.set(PixelScene.align(0.89f));
        changes.addButton( new ChangeButton(isa, "史莱姆王子",
                "修复错误的继承类导致的Cast Exception错误"));

        changes.addButton(new ChangeButton(Icons.get(Icons.DISPLAY), ("界面修复"),
                ("修复部分界面在新版的异常布局")));

        changes.addButton(new ChangeButton(Icons.get(Icons.CHALLENGE_ON), ("药水癔症v0.2"),
                ("现在全肉大饼在英雄低于12力量前必定加力量，且整体加力量的幸运度提升。")));

        changes.addButton(new ChangeButton(Icons.get(Icons.PREFS), ("其他改动"),
                ("1.鬼磷和爆破现在是稀有附魔\n\n" +
                        "2.修复了吸血鬼刀的异常吸血-贡献者：\nzxcPandora\n\n"+
                        "3.修复了药水癔症描述问题，纯洁的祝福-守护护盾描述异常，以及开发者升降器追加探查功能-\n贡献者：zxcPandora")));

        changes = new ChangeInfo("v0.6.5.0-Alpha4", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes = new ChangeInfo("新内容", false, null);
        changes.hardlight(Window.GREEN_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(Icons.get(Icons.CHALLENGE_ON), ("电子斗蛐蛐v0.2"),
                ("开发者模式怪物放置器拥有更多功能，欢迎尝试！另追加黏咕，史莱姆王，豺狼炼药长老，豺狼萨满长老，Flame-C01")));

        changes.addButton(new ChangeButton(Icons.get(Icons.DISPLAY), ("信息栏滚动"),
                ("在游戏缩放较大或者拥有较多词条时，该项会很有用。")));

        changes.addButton(new ChangeButton(BadgeBanner.image(Badges.Badge.BOMBBOW_DIED.image), ("新徽章：碎片飞溅"),
                ("又是一个死亡徽章，新人：悲鸣 因为意味着另类死亡爱好者多了一个新徽章（未解锁该徽章的就更麻烦了）")));

        changes = new ChangeInfo("改动", false, null);
        changes.hardlight(Window.CYELLOW);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new SalamanderSprites(), ("深蓝蝾螈"),
                ("修复被深蓝蝾螈击败不会记录在排行榜的问题。")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.DIEDCROSSBOW), ("重型弩炮"),
                ("修复使用还会扣减飞镖的问题。")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.WAND_KCX), ("共生法杖"),
                ("修复共生法杖可能能被永续的问题。")));

        changes.addButton(new ChangeButton(new Image("sprites/spinner.png", 144, 0, 16, 16),
                Messages.get(ChangesScene.class, "bugfixes"), Messages.get(vM0_6_7_X_Changes.class, "bug_06X85")));

        changes = new ChangeInfo("v0.6.5.0-Alpha3", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes = new ChangeInfo("新内容", false, null);
        changes.hardlight(Window.GREEN_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(Icons.get(Icons.CHALLENGE_ON), ("开发者模式更新"),
                ("添加了更多开发者选项，优化了一些问题，怪物放置器支持电子斗蛐蛐")));

        Image dragonSprite = new FireDragonSprite();
        dragonSprite.scale.set(PixelScene.align(0.72f));
        changes.addButton(new ChangeButton(dragonSprite, ("熔岩火龙资源预载"),
                ("丛林额外Boss-熔岩火龙资源预载，预计将在Alpha7加入。")));

        changes = new ChangeInfo("改动", false, null);
        changes.hardlight(Window.CYELLOW);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.DIEDCROSSBOW), ("重型弩炮"),
                ("现在不需要飞镖作为弹药，但有装填时间。")));

        Image xa = new SakaFishBossSprites();
        xa.scale.set(PixelScene.align(0.72f));
        changes.addButton(new ChangeButton(xa,  "萨卡班甲鱼",
                "初始防御调为35，常规伤害降低。"));

        changes.addButton(new ChangeButton(new Image("sprites/spinner.png", 144, 0, 16, 16),
                Messages.get(ChangesScene.class, "bugfixes"), Messages.get(vM0_6_7_X_Changes.class, "bug_06X84")));

        changes = new ChangeInfo("v0.6.5.0-Alpha1-2", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes = new ChangeInfo("新内容", false, null);
        changes.hardlight(Window.GREEN_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.DIEDCROSSBOW), ("传奇武器"),
                ("新类别传奇武器测试已经开始。在后续版本中需要通过货币解锁。")));

        changes.addButton(new ChangeButton(Icons.get(Icons.BADGES), ("加密徽章"),
                ("添加了加密徽章，长按徽章按钮进入。记录一些隐藏成就。\n\n_注意：_这些成就不会在获取之前不会显示出来")));

        changes.addButton(new ChangeButton(Icons.get(Icons.PREFS), ("杂项改动"),
                ("1.添加了暂停更新界面的功能\n\n" +
                        "2.少量bug修复")));

        changes = new ChangeInfo("改动", false, null);
        changes.hardlight(Window.CYELLOW);
        changeInfos.add(changes);

        Image a = new SakaFishBossSprites();
        a.scale.set(PixelScene.align(0.72f));
        changes.addButton(new ChangeButton(a,  "萨卡班甲鱼",
                "数值方面进行了一定平衡。"));

        changes.addButton(new ChangeButton(new Image("sprites/spinner.png", 144, 0, 16, 16),
                Messages.get(ChangesScene.class, "bugfixes"), Messages.get(vM0_6_7_X_Changes.class, "bug_06X83")));

        changes.addButton(new ChangeButton(Icons.get(Icons.PREFS), ("杂项调整"),
                ("1.书籍现在不可堆积，且成就有一些变化\n\n" +
                        "2.其它bug修复")));
    }

    public static void add_v0_6_55_Changes( ArrayList<ChangeInfo> changeInfos ) {
        ChangeInfo changes = new ChangeInfo("v0.6.4.0-BetaI-XIII", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes = new ChangeInfo("新内容", false, null);
        changes.hardlight(Window.GREEN_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton((new Image("Ling.png", 0, 0, 16, 16)), ("开发者的话"),
                ("你好，我应该有很久没有写开发者的话了。近期，真的很忙。\n" +
                        "我只有晚上更新一会魔绫，但总会熬夜到很晚。\n" +
                        "再这样下去，不清楚还能如此坚持多久。\n" +
                        "我想，我需要休息一下。\n" +
                        "并且降低更新频率，我想现在魔绫的更新速度已经太快了\n"+
                        "所以后面的更新，可能会慢一点。\n"+
                        "还是希望各位能玩的愉快吧，毕竟，人总是不能在虚拟世界里生活的。\n" +
                        "现实仍然有很多东西，我是时候调节一下了。\n"+
                        "那么还是祝各位游戏中冒险愉快吧！")));

        changes.addButton(new ChangeButton(Icons.get(Icons.PREFS), ("快捷栏V1/V2"),
                ("快捷栏降至9个，并且V1是魔绫原版快捷栏，V2是碳素那边借鉴优化的快捷栏。任君挑选。")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.ALCH_PAGE), ("炼金界面优化"),
                ("炼金指南进行优化。")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.TORCH), ("火把调整"),
                ("现在火把会在灯火中成为燃料。")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.ARMOR_ANCITY), ("板鳖甲"),
                ("来自远古的护甲，你是想现在使用，亦或者是收入囊中等待合适的时机？")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.RING_DIAMOND), ("财富之戒"),
                ("财富戒指同步为破碎。")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.FIREFISHSWORD), ("尚方宝剑"),
                ("尚方宝剑属性重新优化，并且拥有两个形态的弱切换。具体怎么切换，看你们自己探索了")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.LANTERNB), ("深度调查"),
                ("娱乐模式改名为深度调查，并且追加黄金时代新娱乐模式。")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.SCROLL_LAGUZ), ("升级卷轴"),
                ("现在追加一个使用全部升级卷轴的按钮")));

        changes.addButton(new ChangeButton(Icons.get(Icons.DISPLAY), ("游戏内区域文本"),
                ("现在每个大区有区域文本，给各位更加身临其境的感觉。")));

        Image a = new SakaFishBossSprites();
        a.scale.set(PixelScene.align(0.72f));
        changes.addButton(new ChangeButton(a,  "新Boss:萨卡班甲鱼",
                "远古遗迹的领袖级Boss,欢迎前来挑战。"));

        changes = new ChangeInfo("改动", false, null);
        changes.hardlight(Window.CYELLOW);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.SAI), ("吸血鬼刀-Beta13"),
                ("修复了一些吸血权重问题。")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.WRALIPS), ("暗金宝石护符"),
                ("现在最高上限+10，并调整优化了一些问题")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.SCROLL_ODAL), ("升级卷轴"),
                ("现在修复升级卷轴可能丢失的问题，并且采用平衡算法。例如，你在1区摸到了4个升级，在监狱只有两个。总量不变。")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.WAND_KCX), ("再生法杖"),
                ("修复一些问题，并且可以持续使用。")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.CHALLANEESICON_15), ("挑战加成"),
                ("在高挑中，提灯的容量更多，商店售卖更多灯油，但灯火的每次减少可能会加剧！")));

        changes.addButton(new ChangeButton(Icons.get(Icons.CHALLENGE_ON), ("支离破碎"),
                ("支离破碎进行了一些怪组优化,并且调整了巨魔铁匠任务(如果在支离破碎遇到巨魔的蝙蝠任务确实有突变buff)")));

        changes.addButton(new ChangeButton(Icons.get(Icons.PREFS), ("杂项修改"),
                ("添加Boss专武保底机制，连续三局未获得，下次必定获得")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.GREATSHIELD), ("5阶武器"),
                ("现在5阶武器生成平均化")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.SAI), ("吸血鬼刀-Beta12"),
                ("优化数值，调整吸血权重。")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.DG3), ("归溯钥剑"),
                ("现在只能在怪物那里获得极少经验，更多经验应该通过装备此武器后拾取钥匙吸收能量。")));

        changes.addButton(new ChangeButton(new Image("sprites/spinner.png", 144, 0, 16, 16),
                Messages.get(ChangesScene.class, "bugfixes"), Messages.get(vM0_6_7_X_Changes.class, "bug_06X82")));

    }

    public static void add_v0_6_54_Changes( ArrayList<ChangeInfo> changeInfos ) {
        ChangeInfo changes = new ChangeInfo("v0.6.4.0-Beta1", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes = new ChangeInfo("新内容", false, null);
        changes.hardlight(Window.GREEN_COLOR);
        changeInfos.add(changes);

        changes.addButton( new ChangeButton(new Image(Assets.Environment.TILES_SEWERS, 48, 48, 16
                , 16), "房间改动",
                "全新房间：慧眼墓碑房/太极八卦房加入"));

        changes.addButton(new ChangeButton(new ShopkeeperSprite(), ("回购系统"),
                ("商店追加回购系统，除了商人领主和奈亚子均可原价退回商品")));

        changes = new ChangeInfo("改动", false, null);
        changes.hardlight(Window.CYELLOW);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new PinkLingSprite(), ("杂项改动"),
                ("优化地牢部分数据，并且为即将更新的每日狩猎等设好底层。")));

        changes.addButton(new ChangeButton(new Image("sprites/spinner.png", 144, 0, 16, 16),
                Messages.get(ChangesScene.class, "bugfixes"), Messages.get(vM0_6_7_X_Changes.class, "bug_06X81")));

    }
}
