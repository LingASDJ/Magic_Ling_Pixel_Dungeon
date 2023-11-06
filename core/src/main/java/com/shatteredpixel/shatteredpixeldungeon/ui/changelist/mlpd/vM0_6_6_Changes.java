package com.shatteredpixel.shatteredpixeldungeon.ui.changelist.mlpd;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.ChangesScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CrivusFruitsSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DM300AttackSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.HeroSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.IceSlowGirlSprites;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.RedNecromancerSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.ui.changelist.ChangeButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.changelist.ChangeInfo;
import com.watabou.noosa.Image;

import java.util.ArrayList;

public class vM0_6_6_Changes {

    public static void addAllChanges(ArrayList<ChangeInfo> changeInfos) {
        add_CS_Changes(changeInfos);
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

    public static void add_v0_6_6_0_Changes( ArrayList<ChangeInfo> changeInfos ) {
        ChangeInfo changes = new ChangeInfo("v0.6.6.0-Demo1-4", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

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

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.GAUNTLETS), ("碧灰双刃"),
                ("伤害提升，并且升为四阶武器")));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
        changes.hardlight(Window.GREEN_COLOR);
        changeInfos.add(changes);

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
                        "_-_ 待反馈"));
    }

}
