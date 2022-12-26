package com.shatteredpixel.shatteredpixeldungeon.ui.changelist.mlpd;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.NewDM300;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.ChangesScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.AutoShopRoBotSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.BlueBatSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ColdGuardSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ColdRatSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DM300Sprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DiedMonkLoaderSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.FlameBoiSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GhostSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.IceStalSprites;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.KagenoNusujinSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MagicGirlSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MimicSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MolotovHuntsmanSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MurdererSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.RedSwarmSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SRPDHBLRTT;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ShopkKingSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SlimeKingSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.WFSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIcon;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.ui.changelist.ChangeButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.changelist.ChangeInfo;
import com.watabou.noosa.Image;
import java.util.ArrayList;

public class vM0_6_7_X_Changes {

  public static void addAllChanges(ArrayList<ChangeInfo> changeInfos) {
    add_v0_6_20_Changes(changeInfos);
    add_v0_6_19_Changes(changeInfos);
    add_v0_6_18_Changes(changeInfos);
    add_v0_6_17_Changes(changeInfos);
    add_v0_6_16_Changes(changeInfos);
    add_v0_6_15_Changes(changeInfos);
    add_v0_6_14_Changes(changeInfos);
    add_v0_6_13_Changes(changeInfos);
    add_v0_6_12_Changes(changeInfos);
    add_v0_6_11_Changes(changeInfos);
    add_v0_6_10_Changes(changeInfos);
    add_v0_6_9_Changes(changeInfos);
    add_v0_6_8_Changes(changeInfos);
    add_v0_6_7_Changes(changeInfos);
    add_v0_6_6_Changes(changeInfos);
    add_v0_6_5_Changes(changeInfos);
    add_v0_6_4_Changes(changeInfos);
    add_v0_6_3_Changes(changeInfos);
    add_v0_6_2_Changes(changeInfos);
    add_v0_6_1_Changes(changeInfos);
    add_v0_6_0_Changes(changeInfos);
  }

  public static void add_v0_6_20_Changes(ArrayList<ChangeInfo> changeInfos) {
    ChangeInfo changes = new ChangeInfo("Coming Soon-Beta21--P3", true, "");
    changes.hardlight(Window.TITLE_COLOR);
    changeInfos.add(changes);

    changes.addButton(
        new ChangeButton(
            new ItemSprite(ItemSpriteSheet.AMULET),
            ("MLPD上半段-试炼与水晶之心"),
            ("我们成功的拿到了水晶之心，然而我们在最终调查中得知YOG不是真正的罪魁祸首……")));

    changes.addButton(
        new ChangeButton(
            new ItemSprite(ItemSpriteSheet.DG20),
            ("BossRush"),
            ("常规的开局你可能已经有所厌倦，地牢中的首领也是如此。这一次，不为成败，只为实力而战")));

    changes.addButton(
        new ChangeButton(
            new ItemSprite(ItemSpriteSheet.ICEBOOK), ("新年活动"), ("新年的钟声即将敲响，你接到了一份神秘的邀请……")));

    changes.addButton(
        new ChangeButton(
            new ItemSprite(ItemSpriteSheet.FIREDVS),
            ("重构世界观"),
            ("魔绫的像素地牢已经陪你走过一个春秋了，然而，魔绫的剧情，你是否清楚呢？")));

    changes.addButton(
        new ChangeButton(
            new ItemSprite(ItemSpriteSheet.SKELETONGOLD),
            ("部分Boss重做"),
            ("为了让魔绫和底层破碎更能区分，部分boss会进行改进或者重做。敬请期待")));

    changes.addButton(
        new ChangeButton(
            new ItemSprite(ItemSpriteSheet.ICEDVS),
            ("不止于此"),
            ("P3的更新远远不止这些，而且这次我们会将一些想法和建议交给玩家，让我们一起制作P3吧，完善上半段旅程吧。")));
  }

  public static void add_v0_6_19_Changes(ArrayList<ChangeInfo> changeInfos) {
    ChangeInfo changes = new ChangeInfo("v0.6.0.0-Beta21-p2.910", true, "");
    changes.hardlight(Window.TITLE_COLOR);
    changeInfos.add(changes);

    changes = new ChangeInfo("新内容", false, null);
    changes.hardlight(Window.GREEN_COLOR);
    changeInfos.add(changes);

    changes.addButton(
        new ChangeButton(
            new AutoShopRoBotSprite(),
            ("自动食品售货机新房间"),
            ("房间会在游戏中生成，里面有固定四个普通补给宝箱，且有5%的概率出现两个金宝箱(二选一)。\n\n但售货机只会在节日期间"
                + "(万圣，圣诞，中秋，国庆）或开启药水癔症挑战后出现。且售货机随着地牢深度的增加，商品购买的金币会有所追加。")));

    changes = new ChangeInfo("修复", false, null);
    changes.hardlight(Window.CYELLOW);
    changeInfos.add(changes);

    changes.addButton(
        new ChangeButton(
            new Image("sprites/spinner.png", 144, 0, 16, 16),
            (Messages.get(ChangesScene.class, "bugfixes")),
            Messages.get(vM0_6_7_X_Changes.class, "bug_06X37")));

    changes = new ChangeInfo("调整", false, null);
    changes.hardlight(Window.SKYBULE_COLOR);
    changeInfos.add(changes);

    changes.addButton(
        new ChangeButton(new RedSwarmSprite(), ("血红苍蝇"), ("数值调整，攻击动画加入，远程魔法攻击3回合一次，并且有概率造成目标燃烧。")));

    changes.addButton(new ChangeButton(new ColdRatSprite(), ("寒冰魔鼠"), ("数值调整，攻击动画加入,远程魔法攻击3回合一次")));

    changes.addButton(
        new ChangeButton(
            new MagicGirlSprite(), ("寒冰魔女调整"), ("攻击AI调整，攻击动画加入，加入新的攻击策略，自身低血量时踩水不再狂暴")));

    changes.addButton(
        new ChangeButton(new IceStalSprites(), ("浊焰魔女调整"), ("激光攻击延长，攻击AI调整，攻击动画加入。")));

    changes.addButton(
        new ChangeButton(
            new ItemSprite(ItemSpriteSheet.CAKE),
            ("草莓奶油芝士蛋糕"),
            ("获得力量概率为9%，且每局游戏最多获得两次力量。之后触发将会转换为英雄血量25%的奥术护盾。")));
  }

  public static void add_v0_6_18_Changes(ArrayList<ChangeInfo> changeInfos) {
    ChangeInfo changes = new ChangeInfo("v0.6.0.0-Beta21-p2.9(圣诞)", true, "");
    changes.hardlight(Window.TITLE_COLOR);
    changeInfos.add(changes);

    changes = new ChangeInfo("新内容", false, null);
    changes.hardlight(Window.GREEN_COLOR);
    changeInfos.add(changes);

    changes.addButton(
        new ChangeButton(
            new AutoShopRoBotSprite(),
            ("自动食品售货机"),
            ("药水癔症的新东西，会在7，12，17，22层生成。【在圣诞节期间也会在地牢中常规生成】")));

    changes.addButton(
        new ChangeButton(
            Icons.get(Icons.NEWS),
            ("域名更换"),
            ("由于原lingasdj.github.io域名已经更换为www.jdsalingzx.top,所以现在进行游戏新闻的地址更换。")));

    changes.addButton(
        new ChangeButton(Icons.get(Icons.DISPLAY), ("FPS帧率现在可显示"), ("如果不习惯，可以到额外设置自行关闭。")));

    changes.addButton(
        new ChangeButton(
            Icons.get(Icons.CHALLENGE_ON), ("挑战改动-1"), ("突变巨兽改名为基因突变，隶属于T3挑战。具体挑战说明请参考挑战介绍")));

    changes.addButton(
        new ChangeButton(
            Icons.get(Icons.CHALLENGE_ON), ("挑战改动-2"), ("支离破碎现在不会再随机楼层，NPC除了红龙之王其他都会出现")));

    changes.addButton(
        new ChangeButton(
            Icons.get(Icons.CHALLENGE_ON), ("挑战改动-3"), ("药水癔症追加自动食品售货机，把金币准备好，去售货机里面购买需要的东西吧")));

    changes.addButton(
        new ChangeButton(
            Icons.get(Icons.CHALLENGE_ON), ("挑战改动-4"), ("污泥浊水的水灵药剂的治疗不再受药水异症影响，且有效时间变为100回合")));

    changes.addButton(
        new ChangeButton(new ItemSprite(ItemSpriteSheet.CAKE), ("新食物:草莓蛋糕"), ("在自动售货机内有售卖")));

    changes.addButton(
        new ChangeButton(new ItemSprite(ItemSpriteSheet.SWTR), ("新食物:魔法三明治"), ("在自动售货机内有售卖")));

    changes = new ChangeInfo("调整", false, null);
    changes.hardlight(Window.SKYBULE_COLOR);
    changeInfos.add(changes);

    changes.addButton(
        new ChangeButton(
            new ItemSprite(ItemSpriteSheet.GAUNTLETS),
            ("碧灰双刃优化"),
            ("碧灰双刃武器优化，现在效果是鬼磷燃烧，并且修正伤害，武器反噬成为概率，并且+3后不再反噬")));

    changes.addButton(
        new ChangeButton(new ItemSprite(ItemSpriteSheet.SAI), ("吸血鬼刀成长调整"), ("吸血鬼刀武器优化，成长数值调整")));

    changes.addButton(
        new ChangeButton(
            new ItemSprite(ItemSpriteSheet.WAR_HAMMER),
            ("白金大剑优化"),
            ("白金大剑武器优化，现在效果是恐惧+眩晕+燃烧，并且修正伤害，武器反噬成为概率，并且+3后不再反噬")));

    changes.addButton(
        new ChangeButton(
            new ItemSprite(ItemSpriteSheet.ICEFISHSWORD),
            ("尚方宝剑优化"),
            ("尚方宝剑武器优化，现在打一次相当于1.5个回合。")));

    changes = new ChangeInfo("修复", false, null);
    changes.hardlight(Window.CYELLOW);
    changeInfos.add(changes);

    changes.addButton(
        new ChangeButton(
            Icons.get(Icons.INFO),
            ("杂项改动"),
            ("1.修复部分文案的描述错误\n2.终焉暂时移除，将在后续特殊模式添加\n3.暂时移除红绿两剑的生成，旧档不受影响，将在未来重做后加回")));

    changes.addButton(
        new ChangeButton(
            new Image("sprites/spinner.png", 144, 0, 16, 16),
            (Messages.get(ChangesScene.class, "bugfixes")),
            Messages.get(vM0_6_7_X_Changes.class, "bug_06X36")));

    changes = new ChangeInfo("移除", false, null);
    changes.hardlight(Window.RED_COLOR);
    changeInfos.add(changes);

    changes.addButton(
        new ChangeButton(
            new MolotovHuntsmanSprite(), ("血月火把猎人"), ("移除此怪物的常规生成，将在未来版本调整后加入，但不再是普通怪物类型")));

    changes.addButton(
        new ChangeButton(new SRPDHBLRTT(), ("火把猎人"), ("移除此怪物的常规生成，将在未来版本调整后加入，但不再是普通怪物类型")));
  }

  public static void add_v0_6_17_Changes(ArrayList<ChangeInfo> changeInfos) {
    ChangeInfo changes = new ChangeInfo("v0.6.0.0-Beta21-p2.875", true, "");
    changes.hardlight(Window.TITLE_COLOR);
    changeInfos.add(changes);

    changes = new ChangeInfo("调整", false, null);
    changes.hardlight(Window.GREEN_COLOR);
    changeInfos.add(changes);

    changes.addButton(
        new ChangeButton(
            new SlimeKingSprite(), ("史莱姆王"), ("追加三阶段，在近战攻击情况下生成蚀化史莱姆，但生成的蚀化史莱姆血量将/4")));

    changes = new ChangeInfo("改动", false, null);
    changes.hardlight(Window.CYELLOW);
    changeInfos.add(changes);

    changes.addButton(
        new ChangeButton(
            new Image("sprites/spinner.png", 144, 0, 16, 16),
            (Messages.get(ChangesScene.class, "bugfixes")),
            Messages.get(vM0_6_7_X_Changes.class, "bug_06X35")));

    changes.addButton(
        new ChangeButton(
            Icons.get(Icons.NEWS),
            ("小道消息"),
            ("OM9已经在Discord宣传此版本，但英文翻译目前没有，因此国际化需要招人。当然，我们会在P3正式开始英化工作")));
  }

  public static void add_v0_6_16_Changes(ArrayList<ChangeInfo> changeInfos) {
    ChangeInfo changes = new ChangeInfo("v0.6.0.0-Beta21-p2.85", true, "");
    changes.hardlight(Window.TITLE_COLOR);
    changeInfos.add(changes);

    changes = new ChangeInfo("调整", false, null);
    changes.hardlight(Window.GREEN_COLOR);
    changeInfos.add(changes);

    changes.addButton(
        new ChangeButton(new IceStalSprites(), ("浊焰魔女"), ("浊焰魔女攻击力降低，且三阶段的结晶生命值下降。")));

    changes.addButton(
        new ChangeButton(new MagicGirlSprite(), ("冰雪魔女"), ("地形调整，冰雪魔女特殊情况下获得的玫瑰结界换成奥术护盾20点")));

    changes.addButton(new ChangeButton(new MimicSprite.Dimand(), ("钻石宝箱王"), ("梦魇Boss挑战下不再发射激光")));

    changes.addButton(
        new ChangeButton(
            new SlimeKingSprite(),
            ("史莱姆王"),
            ("几乎是完全重做：\n"
                + "机制说明:移动极其缓慢但有无限使用的锁链。\n"
                + "血量低于一半后立刻呼唤_当前楼层至多3个怪物_前来支援(只可触发一次),\n"
                + "并且移速恢复正常但_锁链功能失效_且_自身防御降为0_以及近战有概率造成_双倍伤害_。")));

    changes = new ChangeInfo("改动", false, null);
    changes.hardlight(Window.CYELLOW);
    changeInfos.add(changes);

    changes.addButton(
        new ChangeButton(
            Icons.get(Icons.INFO), ("杂项改动"), ("1.部分素材进行了调整优化……\n2.修复了突变巨兽的0血Bug,在新存档生效")));
  }

  public static void add_v0_6_15_Changes(ArrayList<ChangeInfo> changeInfos) {
    ChangeInfo changes = new ChangeInfo("v0.6.0.0-Beta21-p2.8", true, "");
    changes.hardlight(Window.TITLE_COLOR);
    changeInfos.add(changes);

    changes = new ChangeInfo("调整", false, null);
    changes.hardlight(Window.GREEN_COLOR);
    changeInfos.add(changes);

    changes.addButton(
        new ChangeButton(new ItemSprite(ItemSpriteSheet.DG24), ("极度秘卷"), ("修复了未被鉴定的问题")));

    changes.addButton(
        new ChangeButton(new ItemSprite(ItemSpriteSheet.SAI), ("吸血鬼刀"), ("调整了吸血鬼刀的成长数值")));

    changes = new ChangeInfo("改动", false, null);
    changes.hardlight(Window.CYELLOW);
    changeInfos.add(changes);

    changes.addButton(
        new ChangeButton(Icons.get(Icons.INFO), ("杂项改动"), ("1.部分素材和UI进行了调整优化……\n2.删除了用户ID系统")));
  }

  public static void add_v0_6_14_Changes(ArrayList<ChangeInfo> changeInfos) {
    ChangeInfo changes = new ChangeInfo("v0.6.0.0-Beta21-p2.7", true, "");
    changes.hardlight(Window.TITLE_COLOR);
    changeInfos.add(changes);

    changes = new ChangeInfo("调整", false, null);
    changes.hardlight(Window.GREEN_COLOR);
    changeInfos.add(changes);

    changes.addButton(
        new ChangeButton(
            new ItemSprite(ItemSpriteSheet.SCROLL_SOWILO),
            ("升级卷轴"),
            ("升级卷轴现在在第一层的出口区域追加了一个，不受挑战影响")));

    changes = new ChangeInfo("改动", false, null);
    changes.hardlight(Window.CYELLOW);
    changeInfos.add(changes);

    changes.addButton(
        new ChangeButton(
            new Image("sprites/spinner.png", 144, 0, 16, 16),
            (Messages.get(ChangesScene.class, "bugfixes")),
            Messages.get(vM0_6_7_X_Changes.class, "bug_06X34")));

    changes.addButton(
        new ChangeButton(Icons.get(Icons.CHALLENGE_ON), ("突变巨兽调整"), ("突变巨兽进行了一定的调整，详细查看挑战详情")));

    changes.addButton(new ChangeButton(Icons.get(Icons.INFO), ("监狱优化"), ("监狱的水进行了优化，现在不会瞎眼了。")));
  }

  public static void add_v0_6_13_Changes(ArrayList<ChangeInfo> changeInfos) {
    ChangeInfo changes = new ChangeInfo("v0.6.0.0-Beta21-p2.6", true, "");
    changes.hardlight(Window.TITLE_COLOR);
    changeInfos.add(changes);

    changes = new ChangeInfo("新内容", false, null);
    changes.hardlight(Window.GREEN_COLOR);
    changeInfos.add(changes);

    changes.addButton(
        new ChangeButton(
            Icons.get(Icons.CHALLENGE_ON),
            ("BUG同步修复-实验功能"),
            ("实验性功能，将在每次更新游戏的时候对无法通过代码直接同步的数据进行自动同步。")));

    changes = new ChangeInfo("改动", false, null);
    changes.hardlight(Window.CYELLOW);
    changeInfos.add(changes);

    changes.addButton(
        new ChangeButton(
            Icons.get(Icons.INFO), ("杂项改动"), ("1.总体刷怪体系调整\n2.书籍改为720金币起一本。\n3.矮人大师不再召唤猩红大盗")));

    changes.addButton(
        new ChangeButton(
            new Image("sprites/spinner.png", 144, 0, 16, 16),
            (Messages.get(ChangesScene.class, "bugfixes")),
            Messages.get(vM0_6_7_X_Changes.class, "bug_06X33")));

    Image i = new Image(new DM300Sprite());
    i.scale.set(PixelScene.align(0.74f));
    changes.addButton(
        new ChangeButton(i, Messages.get(NewDM300.class, "name"), "DM300每次激活能量塔玩家获得20回合灵视和7回合极速"));

    changes.addButton(new ChangeButton(new ColdGuardSprite(), ("雪凛守卫"), ("调整雪凛峡谷守卫的总体难度")));

    changes = new ChangeInfo("移除", false, null);
    changes.hardlight(Window.RED_COLOR);
    changeInfos.add(changes);

    changes.addButton(
        new ChangeButton(new KagenoNusujinSprite(), ("影子盗贼"), ("移除此怪物，将在未来版本调整后加入，但不再是普通怪物类型")));

    changes.addButton(
        new ChangeButton(
            new MurdererSprite.RedMuderer(), ("猩红大盗"), ("移除此怪物，将在未来版本调整后加入，但不再是普通怪物类型")));
  }

  public static void add_v0_6_12_Changes(ArrayList<ChangeInfo> changeInfos) {
    ChangeInfo changes = new ChangeInfo("v0.6.0.0-Beta21-p2", true, "");
    changes.hardlight(Window.TITLE_COLOR);
    changeInfos.add(changes);

    changes = new ChangeInfo("新内容", false, null);
    changes.hardlight(Window.GREEN_COLOR);
    changeInfos.add(changes);

    changes.addButton(
        new ChangeButton(
            new ItemSprite(ItemSpriteSheet.LANTERNB),
            ("灯火机制-1"),
            ("提灯引路-灯火前行（规则说明-情报公开）：\n"
                + "玩家高于_3挑后_或开启_灯火前路_玩法，将在开局追加灯火值。并立刻给予一个增益Buff。\n"
                + "灯火被分为_6个阶段_（从光芒前路-->寂灭若寞)\n"
                + "任何增益Buff和Debuff都会在击败Boss后完全移除，并在首次进入下一大层后进行一次Roll判定。\n"
                + "Roll判定规则如下:\n"
                + "100灯火    必定增益Buff\n"
                + "99-90灯火  必定获得1个增益Buff\n"
                + "89-80灯火  5%概率获得负面Buff 85%增益Buff 10%啥都不出现\n"
                + "79-60灯火  25%概率获得负面Buff 70%增益Buff 5%啥都不出现\n"
                + "59-35灯火  40%概率获得负面Buff 35%增益Buff 25%啥都不出现\n"
                + "34-1灯火    40%概率获得负面Buff 20%概率获得增益Buff 40%啥都不出现\n"
                + "0以下灯火   48%概率获得负面Buff 10%获得增益Buff 42%啥都不出现\n"
                + "特殊说明：（首次下到6层 必定获得1个增益Buff) ")));

    changes.addButton(
        new ChangeButton(
            new ItemSprite(ItemSpriteSheet.SKNANO),
            ("灯火机制-2"),
            ("提灯引路-灯火前行（规则说明-情报公开）：\n"
                + "随着灯火的微弱，部分怪物将有概率使灯火的能量更低：\n\n"
                + "（非必要说明，一切按照近战攻击算)\n"
                + "老鼠     低于85灯火 5%概率扣减1灯火\n"
                + "监狱守卫  低于85灯火 10%概率扣减1灯火\n"
                + "豺狼萨满  低于70灯火 1%概率扣减2灯火 远程法术攻击7%概率扣减1灯火\n"
                + "黑色怨灵  低于95灯火 4%概率扣减4灯火(赋予Debuff的情况下) （但触发后此怪物立刻死亡) \n"
                + "幽灵任务怪 低于100灯火 20%概率扣减7灯火(除螃蟹外，其他是赋予Debuff的情况下)  \n"
                + "（但触发后此怪物立刻死亡) \n"
                + "幽灵任务怪-巨型螃蟹 低于90灯火 格挡时 6%概率扣减1灯火 \n"
                + "新生火元素 低于80灯火 6%概率扣减2灯火\n"
                + "矿洞蜘蛛 低于70灯火 15%概率扣减1灯火 \n"
                + "血月矿洞炸弹猎人 低于75灯火 35%概率扣减3灯火 （远程攻击) \n"
                + "怨灵 40%概率扣减8灯火（但触发后此怪物立刻死亡)  \n\n"
                + "矮人术士 低于50灯火远程攻击必定扣减2灯火，但触发后此怪物立刻死亡")));

    changes.addButton(
        new ChangeButton(
            new ItemSprite(ItemSpriteSheet.LANTERNA),
            ("灯火机制-3"),
            ("提灯引路-灯火前行（规则说明-情报公开）：\n"
                + "提灯激活后，将以_每4回合+1回复灯火_ （后续每到新的一大层将_增加3回合_）\n"
                + "从第六层开始，玩家将会_每17回合持续扣减1灯火_。但_商人_和_回忆_商店会售卖灯油瓶\n"
                + "5.玩家在首次进入_6,11,16,21层_会进行Roll判定，根据上方Roll规则进行给予Buff。\n\n"
                + "6.灯火低于30以下，被未祝福的十字架重生，之前的东西会被你的暗影带走，击败它，夺回自己的力量\n\n"
                + "7.大部分Debuff优先级高于增益Buff,因此有Debuff存在，增益Buff可能无效。\n\n"
                + "8.死亡重生后，会立刻根据当前灯火进行一次_Roll判定_\n\n"
                + "9.追加了_6个Debuff_和_6个增益Buff_,愿你在灯火前行的道路中一路向前！！！")));

    changes.addButton(
        new ChangeButton(
            new BuffIcon(BuffIndicator.DEBUFF_DOWN, true),
            "灯火专有-Debuff",
            "追加了6种Debuff\n\n"
                + "无力 本大层将临时-3力量\n"
                + "束缚 本大层每下一层获得新的诅咒。\n"
                + "审判 英雄近战伤害削弱10%\n"
                + "迟钝 本大层英雄行走一回合相当于3回合。\n"
                + "软弱 本大层所有被护甲减免过的物理伤害增加5%\n"
                + "贪婪 本大层商店的价格除十字架和食物和治疗药水打1折，其他全部1.5倍价格。"));

    changes.addButton(
        new ChangeButton(
            new BuffIcon(BuffIndicator.GOBUFF_UPRD, true),
            "灯火专有-Gobuff",
            "追加了6种增益Buff\n\n"
                + "纯洁的赞歌:\n"
                + "前路 本大层内，饥饿高于50%的情况下，获得每回合+2的血量。\n"
                + "富饶 本大层内，商店打3折!\n"
                + "守护 本大层内，满血可额外获得一些护盾值 冷却：300回合\n"
                + "安息 本大层内，玩家物理伤害x2倍\n"
                + "隐没 本大层内，玩家免疫魔法效果和减免20%伤害\n"
                + "坚毅 本大层内，玩家力量+8"));

    changes = new ChangeInfo("改动", false, null);
    changes.hardlight(Window.CYELLOW);
    changeInfos.add(changes);

    changes.addButton(
        new ChangeButton(
            new Image("sprites/spinner.png", 144, 0, 16, 16),
            (Messages.get(ChangesScene.class, "bugfixes")),
            Messages.get(vM0_6_7_X_Changes.class, "bug_06X32")));

    Image issxa = new DM300Sprite();
    issxa.scale.set(PixelScene.align(0.69f));
    changes.addButton(new ChangeButton(issxa, "DM300改动", "DM300每次激活能量塔时，玩家会获得7回合灵视"));

    changes.addButton(
        new ChangeButton(new ItemSprite(ItemSpriteSheet.ENDDIED), ("终焉调整"), ("终焉力量现在是500能量+1力量")));
  }

  public static void add_v0_6_11_Changes(ArrayList<ChangeInfo> changeInfos) {
    ChangeInfo changes = new ChangeInfo("v0.6.0.0-Beta21-p1.4-5", true, "");
    changes.hardlight(Window.TITLE_COLOR);
    changeInfos.add(changes);

    changes = new ChangeInfo("新内容", false, null);
    changes.hardlight(Window.TITLE_COLOR);
    changeInfos.add(changes);

    changes.addButton(new ChangeButton(new IceStalSprites(), ("浊焰魔女AI调整"), ("重新调整了浊焰魔女的AI属性")));

    changes.addButton(new ChangeButton(new BlueBatSprite(), ("血影蝙蝠调整"), ("英雄15级后法术伤害面板调整")));

    changes = new ChangeInfo("改动", false, null);
    changes.hardlight(Window.CYELLOW);
    changeInfos.add(changes);

    changes.addButton(
        new ChangeButton(
            new Image("sprites/spinner.png", 144, 0, 16, 16),
            (Messages.get(ChangesScene.class, "bugfixes")),
            Messages.get(vM0_6_7_X_Changes.class, "bug_06X31")));
  }

  public static void add_v0_6_10_Changes(ArrayList<ChangeInfo> changeInfos) {
    ChangeInfo changes = new ChangeInfo("v0.6.0.0-Beta21-p1.2", true, "");
    changes.hardlight(Window.TITLE_COLOR);
    changeInfos.add(changes);

    changes = new ChangeInfo("新内容", false, null);
    changes.hardlight(Window.GREEN_COLOR);
    changeInfos.add(changes);

    changes.addButton(
        new ChangeButton(new ItemSprite(ItemSpriteSheet.ENDDIED), ("彩蛋武器：终焉"), ("正式实装，击败火元素概率掉落")));

    changes = new ChangeInfo("改动", false, null);
    changes.hardlight(Window.CYELLOW);
    changeInfos.add(changes);

    changes.addButton(
        new ChangeButton(
            new Image("sprites/spinner.png", 144, 0, 16, 16),
            (Messages.get(ChangesScene.class, "bugfixes")),
            Messages.get(vM0_6_7_X_Changes.class, "bug_06X30")));
  }

  public static void add_v0_6_9_Changes(ArrayList<ChangeInfo> changeInfos) {
    ChangeInfo changes = new ChangeInfo("v0.6.0.0-Beta21-p1", true, "");
    changes.hardlight(Window.TITLE_COLOR);
    changeInfos.add(changes);

    changes = new ChangeInfo("新内容", false, null);
    changes.hardlight(Window.GREEN_COLOR);
    changeInfos.add(changes);

    changes.addButton(
        new ChangeButton(
            new ItemSprite(ItemSpriteSheet.ENDDIED), ("彩蛋武器：终焉"), ("在命运的尽头，亦或者是命运的起点")));

    changes.addButton(
        new ChangeButton(new ItemSprite(ItemSpriteSheet.GAUNTLETS), ("碧灰双刃"), ("武器降级到3阶")));

    changes.addButton(
        new ChangeButton(new ItemSprite(ItemSpriteSheet.DG3), ("钥匙剑"), ("武器提格到5阶，进行了一定的重做")));

    changes = new ChangeInfo("改动", false, null);
    changes.hardlight(Window.CYELLOW);
    changeInfos.add(changes);

    changes.addButton(new ChangeButton(new MimicSprite.Dimand(), ("钻石宝箱王"), ("在梦魇Boss中出现新的技能")));

    changes.addButton(
        new ChangeButton(new GhostSprite(), ("幽灵奖励改变"), ("幽灵现在更能出现+3，以及+4，+5品种的武器或护甲")));

    changes.addButton(new ChangeButton(Icons.get(Icons.CHANGES), ("药水癔症"), ("暂时为以前的样子，后续会改善")));

    changes.addButton(
        new ChangeButton(
            Icons.get(Icons.INFO),
            ("杂项改动"),
            ("1.部分物品的显示逻辑更新\n2.用户ID系统\n3.调整红龙之王的奖励\n4.修复了一系列的Bug")));
  }

  public static void add_v0_6_8_Changes(ArrayList<ChangeInfo> changeInfos) {
    ChangeInfo changes = new ChangeInfo("v0.6.0.0-Beta20.80", true, "");
    changes.hardlight(Window.TITLE_COLOR);
    changeInfos.add(changes);

    changes = new ChangeInfo("新内容", false, null);
    changes.hardlight(Window.GREEN_COLOR);
    changeInfos.add(changes);

    changes.addButton(new ChangeButton(new ColdGuardSprite(), ("全新区域：雪凛峡谷"), ("雪凛峡谷正式版")));

    changes.addButton(new ChangeButton(new ShopkKingSprite(), ("商人领主商店终端"), ("交易使人快乐")));
  }

  public static void add_v0_6_7_Changes(ArrayList<ChangeInfo> changeInfos) {
    ChangeInfo changes = new ChangeInfo("v0.6.0.0-Beta20.785", true, "");
    changes.hardlight(Window.TITLE_COLOR);
    changeInfos.add(changes);

    changes = new ChangeInfo("新内容", false, null);
    changes.hardlight(Window.GREEN_COLOR);
    changeInfos.add(changes);

    changes.addButton(
        new ChangeButton(
            new ColdGuardSprite(), ("全新区域：雪凛峡谷"), ("在雪凛峡谷中寻找300年前的支离破碎的线索……\n\n全新商店抢劫系统：V6.0")));

    changes.addButton(
        new ChangeButton(new ItemSprite(ItemSpriteSheet.DG1), ("月饼"), ("限时食物：9-10到10.1，中秋节特供")));

    changes = new ChangeInfo("改动", false, null);
    changes.hardlight(Window.GREEN_COLOR);
    changeInfos.add(changes);

    changes.addButton(
        new ChangeButton(Icons.get(Icons.INFO), ("杂项改动"), ("-NPC对话文本改变\n-部分BGM升级更新")));

    changes.addButton(
        new ChangeButton(
            new Image("sprites/spinner.png", 144, 0, 16, 16),
            (Messages.get(ChangesScene.class, "bugfixes")),
            Messages.get(vM0_6_7_X_Changes.class, "bug_06X28")));
  }

  public static void add_v0_6_6_Changes(ArrayList<ChangeInfo> changeInfos) {
    ChangeInfo changes = new ChangeInfo("v0.6.0.0-Beta20.75", true, "");
    changes.hardlight(Window.TITLE_COLOR);
    changeInfos.add(changes);

    changes = new ChangeInfo("改动", false, null);
    changes.hardlight(Window.SKYBULE_COLOR);
    changeInfos.add(changes);

    changes.addButton(
        new ChangeButton(
            new DiedMonkLoaderSprite(), ("矮人大师优化"), ("-1.矮人大师追加神秘之井\n-2.矮人大师血量追加到800")));

    changes.addButton(
        new ChangeButton(
            new Image("sprites/spinner.png", 144, 0, 16, 16),
            (Messages.get(ChangesScene.class, "bugfixes")),
            Messages.get(vM0_6_7_X_Changes.class, "bug_06X26")));

    changes = new ChangeInfo("调整", false, null);
    changes.hardlight(Window.CYELLOW);
    changeInfos.add(changes);

    changes.addButton(new ChangeButton(Icons.get(Icons.INFO), ("滚动文本调整"), ("-追加更多的过度文本")));
  }

  public static void add_v0_6_5_Changes(ArrayList<ChangeInfo> changeInfos) {
    ChangeInfo changes = new ChangeInfo("v0.6.0.0-Beta20.(1-6)", true, "");
    changes.hardlight(Window.TITLE_COLOR);
    changeInfos.add(changes);

    changes = new ChangeInfo("新内容", false, null);
    changes.hardlight(Window.GREEN_COLOR);
    changeInfos.add(changes);

    changes.addButton(
        new ChangeButton(Icons.get(Icons.INFO), ("其他说明"), ("1.圣境密林层贴图更新\n2.监狱BGM正式实装")));

    changes.addButton(
        new ChangeButton(
            Icons.get(Icons.RENAME_ON),
            ("重命名系统"),
            ("为你中意的英雄进行自定义重命名吧,击败第一大层的任意Boss解锁\n" + "为你中意的装备进行自定义重命名吧，解锁条件同上所述。")));

    changes = new ChangeInfo("改动", false, null);
    changes.hardlight(Window.SKYBULE_COLOR);
    changeInfos.add(changes);

    changes.addButton(new ChangeButton(new SRPDHBLRTT(), ("赏金猎人"), ("伤害下调至9-12")));

    changes.addButton(new ChangeButton(new KagenoNusujinSprite(), ("影子盗贼"), ("AI逻辑改变")));

    changes.addButton(
        new ChangeButton(
            new WFSprite(), ("法师初始改动"), ("茉莉伊洛是双属性的魔法少女，拥有强大的魔力，她将会把烈焰法杖和冰雪法杖进行随机化作为初始法杖")));

    changes.addButton(
        new ChangeButton(
            new Image("sprites/spinner.png", 144, 0, 16, 16),
            (Messages.get(ChangesScene.class, "bugfixes")),
            Messages.get(vM0_6_7_X_Changes.class, "bug_06X25")));
  }

  public static void add_v0_6_4_Changes(ArrayList<ChangeInfo> changeInfos) {
    ChangeInfo changes = new ChangeInfo("v0.6.0.0-Beta19", true, "");
    changes.hardlight(Window.TITLE_COLOR);
    changeInfos.add(changes);

    changes = new ChangeInfo("新内容", false, null);
    changes.hardlight(Window.GREEN_COLOR);
    changeInfos.add(changes);

    changes.addButton(
        new ChangeButton(
            Icons.get(Icons.BUFFS), ("Boss血量优化"), ("现在Boss不仅会显示血量数值，还会实时显示Boss获得的Buff状态条")));

    changes.addButton(
        new ChangeButton(
            Icons.get(Icons.DISPLAY), ("怪物面板属性优化"), ("现在怪物面板移除了可获得经验并改为上次伤害，并追加移速。且小数点精确到一位。")));

    changes = new ChangeInfo("改动", false, null);
    changes.hardlight(Window.SKYBULE_COLOR);
    changeInfos.add(changes);

    changes.addButton(
        new ChangeButton(
            new Image("sprites/spinner.png", 144, 0, 16, 16),
            (Messages.get(ChangesScene.class, "bugfixes")),
            Messages.get(vM0_6_7_X_Changes.class, "bug_06X24")));

    changes = new ChangeInfo("调整", false, null);
    changes.hardlight(Window.CYELLOW);
    changeInfos.add(changes);

    changes.addButton(
        new ChangeButton(
            new BlueBatSprite(), ("小血影改动"), ("-小血影在主人15级后将会开启远程攻击\n\n" + "-伤害削弱20%，但血量提升20%")));
  }

  public static void add_v0_6_3_Changes(ArrayList<ChangeInfo> changeInfos) {
    ChangeInfo changes = new ChangeInfo("v0.6.0.0-BetaXVIII", true, "");
    changes.hardlight(Window.TITLE_COLOR);
    changeInfos.add(changes);

    changes = new ChangeInfo("新内容", false, null);
    changes.hardlight(Window.GREEN_COLOR);
    changeInfos.add(changes);

    changes.addButton(
        new ChangeButton(
            Icons.get(Icons.CHALLENGE_ON),
            ("新挑战:污泥浊水"),
            ("1-4层 给予1回合残废\n"
                + "6-9层 给予1回合失明\n"
                + "11-14层 给予2回合冻伤\n"
                + "16-19层 给予 上面的全部效果附加幻惑3回合\n"
                + "\n"
                + "应对策略：\n"
                + "1.玩家开局获得x4水灵药剂，可获得40回合免疫\n"
                + "2.制作生石灰来将水蒸发，制作水灵药剂来免疫效果。\n\n"
                + "挑战开启期间4,8,13,18层将会追加一个食人鱼池子,并且食人鱼属性大幅度下降。")));

    changes = new ChangeInfo("改动", false, null);
    changes.hardlight(Window.SKYBULE_COLOR);
    changeInfos.add(changes);

    changes.addButton(
        new ChangeButton(
            new Image("sprites/spinner.png", 144, 0, 16, 16),
            (Messages.get(ChangesScene.class, "bugfixes")),
            Messages.get(vM0_6_7_X_Changes.class, "bug_06X23")));
  }

  public static void add_v0_6_2_Changes(ArrayList<ChangeInfo> changeInfos) {
    ChangeInfo changes = new ChangeInfo("v0.6.0.0-BetaXVII", true, "");
    changes.hardlight(Window.TITLE_COLOR);
    changeInfos.add(changes);

    changes = new ChangeInfo("改动", false, null);
    changes.hardlight(Window.SKYBULE_COLOR);
    changeInfos.add(changes);

    changes.addButton(
        new ChangeButton(
            new Image("sprites/spinner.png", 144, 0, 16, 16),
            (Messages.get(ChangesScene.class, "bugfixes")),
            Messages.get(vM0_6_7_X_Changes.class, "bug_06X22")));

    changes = new ChangeInfo("调整", false, null);
    changes.hardlight(Window.CYELLOW);
    changeInfos.add(changes);

    changes.addButton(new ChangeButton(new BlueBatSprite(), ("小血影初始改动"), ("小血影10级后将会开启远程攻击")));
  }

  public static void add_v0_6_1_Changes(ArrayList<ChangeInfo> changeInfos) {
    ChangeInfo changes = new ChangeInfo("v0.6.0.0-BetaXV", true, "");
    changes.hardlight(Window.TITLE_COLOR);
    changeInfos.add(changes);

    changes = new ChangeInfo("新内容", false, null);
    changes.hardlight(Window.GREEN_COLOR);
    changeInfos.add(changes);

    changes.addButton(new ChangeButton(new BlueBatSprite(), ("血影蝙蝠"), ("极影铃虹的宠物，为主人而战")));

    changes = new ChangeInfo("改动", false, null);
    changes.hardlight(Window.SKYBULE_COLOR);
    changeInfos.add(changes);

    changes.addButton(
        new ChangeButton(
            new Image("sprites/spinner.png", 144, 0, 16, 16),
            (Messages.get(ChangesScene.class, "bugfixes")),
            Messages.get(vM0_6_7_X_Changes.class, "bug_06X21")));

    changes = new ChangeInfo("调整", false, null);
    changes.hardlight(Window.CYELLOW);
    changeInfos.add(changes);

    changes.addButton(
        new ChangeButton(
            new ItemSprite(ItemSpriteSheet.SPIRIT_ARROW),
            ("女猎初始改动"),
            ("灵能短弓伤害面板从1-6提升到4-9,成长系数不变")));

    changes.addButton(
        new ChangeButton(
            new ItemSprite(ItemSpriteSheet.WAND_FROST),
            ("法师初始改动"),
            ("茉莉伊洛是双属性的魔法少女，拥有强大的魔力，她将会把烈焰法杖和冰雪法杖幻化到快捷栏里，但是这两个法杖受地牢魔力影响，不能升级，不能灌注，但是开局固定3级。\n\n"
                + "且一旦替换幻化的快捷栏或者退出游戏重新进入将消失。\n"
                + "\n"
                + "低语：错误的选择将让你万劫不复的，茉莉！")));

    changes.addButton(
        new ChangeButton(
            new ItemSprite(ItemSpriteSheet.SUMMON_ELE),
            ("战士初始改动"),
            ("蕾零安洁虽然本身没有魔力，但家族世世代代都在研究一种高超的技术，这项技术足以让她在地牢里面有冒险的资本。\n\n使用战士将追加2个唤魔晶柱")));

    changes.addButton(
        new ChangeButton(
            new BlueBatSprite(), ("盗贼初始改动"), ("极影铃虹的潜能非常强大，她的小蝙蝠经常与她为伴。\n\n盗贼将在开局追加一个小蝙蝠。")));
  }

  public static void add_v0_6_0_Changes(ArrayList<ChangeInfo> changeInfos) {
    ChangeInfo changes = new ChangeInfo("v0.6.0.0-BetaX", true, "");
    changes.hardlight(Window.TITLE_COLOR);
    changeInfos.add(changes);

    changes = new ChangeInfo("新内容", false, null);
    changes.hardlight(Window.GREEN_COLOR);
    changeInfos.add(changes);

    changes.addButton(
        new ChangeButton(
            (new Image("Ling.png", 0, 0, 16, 16)),
            ("开发者的话"),
            ("你好，这里是绫。如你所见，这是全新的魔绫像素地牢，她已经步入了破碎1.2"
                + ".3的版本。\n\n至此，魔绫像素地牢以后将针对于此破碎底层进行更新。今后不会继续同步底层破碎版本。\n\n"
                + "同时，本次更新后，后续应该还有几个补丁版。很高兴一路以来有那么多的朋友，非常谢谢你们的支持。\n\n现在，旅途才刚刚开始，魔绫下半段，将会更加精彩。\n"
                + "在这之前，就让我们继续在上半段的魔绫里面探索前进吧。")));

    changes.addButton(
        new ChangeButton(
            Icons.get(Icons.CHALLENGE_ON),
            ("新挑战:空洞旅程"),
            ("开启本挑战将会在开局获得240点理智，在_没有光芒下_和_部分怪物的近战_将会使你理智降低。详情查看下表:\n"
                 + "-棕色老鼠:20%概率(-1理智/每回合)\n"
                 + "-黑色怨灵:10%概率(-3理智/每回合)\n"
                 + "-火把猎人:40%概率(-1理智/每回合)\n"
                 + "-矮人术士:15%概率(-5理智/每回合)\n"
                 + "-寒冰老鼠:25%概率(-2理智/每回合)\n"
                 + "-DM200:10%概率(-7理智/每回合)\n"
                 + "-矮人武僧:30%的概率(-3理智/每回合)\n"
                 + "-没有光芒Buff的情况下:-1/每回合\n\n"
                 + "_理智回复策略_:\n"
                 + "-_1_:存在光芒的情况下以:[(+1理智+楼层深度/10)/每回合](举例:20层没有光芒的情况下,1+20/10=3(+3智/每回合)\n"
                 + "-_2_.商人售卖信仰药水，喝下去追加40回合理智\n"
                 + "-_3_.击败敌人可以获得敌人的灵魂,灵魂到一定数量可以使提灯可以再次点亮道路。灵魂也可以缓慢回复理智。(50灵魂=1理智回复)\n\n"
                 + "尚未制作完成，敬请期待")));

    changes = new ChangeInfo("改动", false, null);
    changes.hardlight(Window.SKYBULE_COLOR);
    changeInfos.add(changes);

    changes.addButton(
        new ChangeButton(
            new Image("sprites/spinner.png", 144, 0, 16, 16),
            (Messages.get(ChangesScene.class, "bugfixes")),
            Messages.get(vM0_6_7_X_Changes.class, "bug_06X20")));

    changes.addButton(
        new ChangeButton(
            new ItemSprite(ItemSpriteSheet.DG24), ("极度秘卷"), ("原炎魔秘卷，现在重做。具体请游戏里面自行探索。")));

    changes.addButton(
        new ChangeButton(
            Icons.get(Icons.INFO),
            ("快捷栏调整"),
            ("快捷栏使用说明:\n"
                 + "-1.请最好根据自己的分辨率进行调整，避免出现快捷栏叠加情况\n"
                 + "-2.由于技术问题，快捷栏仅在游戏中才可以设置\n"
                 + "-3.最多支持12个快捷栏，玩得高兴！！！")));

    changes.addButton(
        new ChangeButton(Icons.get(Icons.CHANGES), ("重大更新"), ("魔绫现已更新底层到破碎V1.2.3版本!")));

    changes.addButton(
        new ChangeButton(
            Icons.get(Icons.CHALLENGE_ON),
            ("新挑战和部分挑战改动"),
            ("部分挑战进行重新调整，同时追加全新挑战，欢迎前来探索\n\n注意：部分挑战尚未完成，请等待后续版本")));

    changes.addButton(
        new ChangeButton(Icons.get(Icons.LANGS), ("本地化模块升级"), ("魔绫已经对本地化语言模块优化，使部分低配设备性能更好")));

    changes.addButton(
        new ChangeButton(Icons.get(Icons.DISPLAY), ("UI优化改动"), ("魔绫已经对UI优化改动进行大规模调整，欢迎前来体验")));

    changes = new ChangeInfo("调整", false, null);
    changes.hardlight(Window.CYELLOW);
    changeInfos.add(changes);

    changes.addButton(
        new ChangeButton(
            new Image(Assets.Environment.TILES_SEWERS, 48, 80, 16, 16),
            "水晶十字房加强",
            "水晶十字房间改动说明：\n"
                + "第一个房间：概率在80-270范围给予金币\n"
                + "第二个房间：概率在符石，种子，卷轴，食物中随机抽取\n"
                + "第三个房间：概率在1，3，5阶武器，戒指中随机抽取\n"
                + "第四个房间：概率在护甲，符石，法杖，神器中随机抽取"));

    changes.addButton(
        new ChangeButton(
            new ItemSprite(ItemSpriteSheet.FIREFISHSWORD),
            ("尚方宝剑特效重写"),
            ("优化了尚方宝剑特效，并最大程度上进行了处理")));

    changes = new ChangeInfo("移除", false, null);
    changes.hardlight(Window.RED_COLOR);
    changeInfos.add(changes);

    changes.addButton(
        new ChangeButton(new FlameBoiSprite(), ("火焰机器人"), ("移除火焰机器人在常规局的出现，仅出现在支离破碎的精英怪概率里面")));

    changes.addButton(
        new ChangeButton(new ItemSprite(ItemSpriteSheet.BlackDog), ("黑狗爪"), ("移除黑狗爪，它已不再有当年的威风")));
  }
}
