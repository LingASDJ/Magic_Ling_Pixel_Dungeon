package com.shatteredpixel.shatteredpixeldungeon.ui.changelist.mlpd;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.gold.TribemanOldSprite;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.lb.RivalSprite;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.NetIcons;
import com.shatteredpixel.shatteredpixeldungeon.items.props.ArmorScalesOfBzmdr;
import com.shatteredpixel.shatteredpixeldungeon.items.props.BrokenBone;
import com.shatteredpixel.shatteredpixeldungeon.items.props.BrokenRing;
import com.shatteredpixel.shatteredpixeldungeon.items.props.CatGirlCosplay;
import com.shatteredpixel.shatteredpixeldungeon.items.props.DeadOrAlive;
import com.shatteredpixel.shatteredpixeldungeon.items.props.DeliciousRecipe;
import com.shatteredpixel.shatteredpixeldungeon.items.props.Dirt_KnifeStand;
import com.shatteredpixel.shatteredpixeldungeon.items.props.DreamSeed;
import com.shatteredpixel.shatteredpixeldungeon.items.props.FaintGlimmer;
import com.shatteredpixel.shatteredpixeldungeon.items.props.HeartOfCrystalFractal;
import com.shatteredpixel.shatteredpixeldungeon.items.props.HellButterfly;
import com.shatteredpixel.shatteredpixeldungeon.items.props.KillEye;
import com.shatteredpixel.shatteredpixeldungeon.items.props.KnightStabbingSword;
import com.shatteredpixel.shatteredpixeldungeon.items.props.Monocular;
import com.shatteredpixel.shatteredpixeldungeon.items.props.NoteOfBzmdr;
import com.shatteredpixel.shatteredpixeldungeon.items.props.PortableWhetstone;
import com.shatteredpixel.shatteredpixeldungeon.items.props.PureRouge;
import com.shatteredpixel.shatteredpixeldungeon.items.props.RapidEarthRoot;
import com.shatteredpixel.shatteredpixeldungeon.items.props.StarDust;
import com.shatteredpixel.shatteredpixeldungeon.items.props.WenStudyingPaperOne;
import com.shatteredpixel.shatteredpixeldungeon.items.props.YanStudyingPaperOne;
import com.shatteredpixel.shatteredpixeldungeon.items.props.YanStudyingPaperTwo;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.ChangesScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ATRISprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.AoReadyDragonSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ArchettoSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.BloodsSwarmSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ClearElemtGuardGirlSprites;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CrabSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CrivusStarFruitsSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DM111Sprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DeadEyeSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DogDogMusicSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ElementalSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.FodderSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GhoulPlusSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GiantFlowerSlimeSprites;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GuardCapitalSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.HeroSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.KusumiMagicGirlSprites;
import com.shatteredpixel.shatteredpixeldungeon.sprites.KusumiSprites;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MageHandSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MoonCatSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MorpheusSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MyCoreHeartSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.NyarlathotepSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.PeachGodStateSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.PumkingBomberSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.RedNecromancerSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SRPDHBLRTT;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ShopkKingSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ShubNiggurathSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SliceGirlSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SpawnereEvilSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.TowerMachineSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.VeryColdRatSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.WarlockHeadSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.WhiteGirlSprites;
import com.shatteredpixel.shatteredpixeldungeon.sprites.WispSprite;
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

public class vm0_9_X_Changes {

    public static void addAllChanges(ArrayList<ChangeInfo> changeInfos) {
        add_V0950_Changes(changeInfos);
        add_V0948_Changes(changeInfos);
        add_V0945_Changes(changeInfos);
        add_V0942_Changes(changeInfos);
        add_V0940_Changes(changeInfos);
        add_V0931_Changes(changeInfos);
        add_V0925_Changes(changeInfos);
        add_V0920_Changes(changeInfos);
        add_V0915_Changes(changeInfos);
        add_V0910_Changes(changeInfos);
        add_V0900_Changes(changeInfos);
    }

    public static void add_V0950_Changes(ArrayList<ChangeInfo> changeInfos ) {

        ChangeInfo changes = new ChangeInfo("Lost-失落古墓-敬请期待-Tomb", true,"");
        changes.hardlight(0xCCCCCC);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(Icons.get(Icons.SHPX), "开篇寄语",
                "由衷感谢大家一路陪伴、支持魔绫像素地牢！\n" +
                        "在前作古堡外传沉淀开发经验后，全新外传篇章企划正式启动——失落古墓。\n\n" +
                        "这座宏伟的哥特式陵寝曾矗立地表，却因一场浩劫深埋地底。永恒无光的死寂圣堂、布满裂痕的石廊间，亡魂与畸变怪物四处游荡，深埋地下的真相等待冒险者前来发掘。"));

        changes.addButton(new ChangeButton(HeroSprite.avatar(HeroClass.SPELLSWORD, 1), "新登场英雄：魔剑士",
                "全新可玩英雄，将随同冒险者一同深入危机四伏的失落古墓，并肩对抗地底亡灵。"));

        changes.addButton(new ChangeButton(new Image(Assets.Environment.TILES_PLACE, 48, 80, 16
                , 16), "场景美术",
                "为本篇外传量身打造全套古墓像素美术资源：\n" +
                        "整体采用冷灰幽蓝的哥特像素画风，极致渲染永不见天日的地底陵寝压抑氛围，敬请期待古墓外传章节。"));

        changes.addButton(new ChangeButton( new WispSprite(), "古墓章节专属怪物",
                "失落古墓区域将实装若干区域独有的亡灵魔物，以及多个新Boss\n" +
                        "敬请期待古墓外传章节，更多未知敌人后续逐步披露。"));

        changes.addButton(new ChangeButton(Icons.get(Icons.AUDIO), "全篇原创配乐",
                "整个失落古墓篇章配备专属原创背景音乐，由首席作曲犬罗全权创作。烘托地底古墓孤寂阴森的探索氛围。"));

        changes.addButton(new ChangeButton(new Image(Icons.get(Icons.NEWS)), "后续开发预告",
                "失落古墓外传正在稳步推进开发，更多场景、怪物、专属武器与剧情内容将陆续放出，敬请期待后续版本情报！"));

        changes = new ChangeInfo("v0.9.5.0", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);
        changes.addButton(new ChangeButton(new Image("Ling.png", 0, 0, 16, 16), "开发者的话",
                "v0.9.5.0 — 完成于 2026-08-14 13:45\n\n" +
                        "没想到上次更新魔绫已经是两个月前的事了。近期，开发组一直在全力进行古墓内容的制作。\n" +
                        "不过在路途中，我们结识了新的伙伴，同时，作为一次补充更新，这个版本我们将早期惊鸿杯的优胜创作全部实装，并完成了诸多bug修复与游戏优化。祝大家玩的愉快！\n\n" +
                        "——MLPD主开发：JDSALing\n" +
                        "翻译：久住"));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new KusumiSprites(), ("新NPC"),
                ("神秘的少女，来历不详，履历不详……")));

        changes.addButton(new ChangeButton(new KusumiMagicGirlSprites(), ("新NPC"),
                ("神秘的少女，来历不详，履历不详……")));

        changes.addButton(new ChangeButton(new ArchettoSprite(), "弦事件推进",
                "弦近期似乎换上了新衣服……？"));

        changes.addButton(new ChangeButton(Icons.get(Icons.AUDIO), ("新音乐：Ice Ruins"),
                ("作者：禊-misogi-\n\n为三区全新音乐，欢迎欣赏。")));

        changes.addButton (new ChangeButton (new ItemSprite(ItemSpriteSheet.SOS_3), "新神器：支援信号套组",
                "2024惊鸿杯，炼金乐园赛道第一名，现已正式实装。"));

        changes.addButton (new ChangeButton (new ItemSprite(ItemSpriteSheet.SKY_PEN_PLUS), "新神器：天界画笔",
                "2024惊鸿杯，秘器研究赛道第二名，现已正式实装。"));

        changes.addButton (new ChangeButton (HeroSprite.avatar (HeroClass.MAGE, 13), "新皮肤：学院时光",
                "售价1500钴币\n\n原稿：Priestess\n\n绘制：Noah-7385 & Daniel-Calan\n\n茉莉就读于法师学院时的制服，在校时，她是数一数二的魔法尖子生。传闻她常独自待在图书馆最深处，研读那些失传已久的古老法术……"));

        changes.addButton( new ChangeButton( new TalentIcon(Talent.TESTED_HYPOTHESIS), Talent.TESTED_HYPOTHESIS.title(),
                "法师T1天赋，实验假设天赋更换为 灵光一闪"));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
        changes.hardlight(CharSprite.WARNING);
        changeInfos.add(changes);

        changes.addButton (new ChangeButton (new ItemSprite(ItemSpriteSheet.BEACON), "皮肤下架说明",
                "由于 晚宴邀请 and 虔圣书徒 两款皮肤的画师单方面发狂，现制作组已与其永久解除合作，并永久移除所有与其有关的内容。\n" +
                        "对喜爱这两款皮肤的玩家深表歉意，但这也是不可抗力的因素，对于此前已拥有晚宴邀请的玩家，将在版本更新后免费赠送新皮肤：学院时光。\n" +
                        "对于此前已拥有虔圣书徒的玩家，将在更新后启动游戏后，自动发放500钴币。"));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.SCROLL_GOLD), ("金蝶模式V2.5"),
                (
                        "金蝶模式-V2.5\n\n" +
                                "_-_ 优化金蝶神器嬗变，现在只能嬗变未装备的神器\n" +
                                "_-_ 金蝶中与狐狸对话有概率获得全新特殊武器，开启特殊模式！")));

        changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
                ("V0.9.5.0：\n\n" +
                        "_-_ 修复白浪刺剑武技先前可以以墙体为目标使用，无限距离且不消耗充能的严重问题\n" +
                        "_-_ 优化苍白花环新增特判：不会被拟态之王与浊焰魔女收走，防止利用该特性逃课成就\n" +
                        "_-_ 修复携带bzm的礼物会导致开灯不增加视野的问题\n" +
                        "_-_ 修复钻石大剑与轻音铃铛在乾坤宗师的\"伴生强化\"天赋下获取假升级的问题，虚拟等级仅显示，不再错误影响面板与实际效果\n" +
                        "_-_ 修复魔女的印记——浊燃祝福效果异常的问题，描述为20%全伤减免，此前错误实现了免疫法术伤害，现已修正为正确的20%全伤减免\n" +
                        "_-_ 修复灵魂卷轴部分设计问题 & 钻石大剑武技的异常问题\n"),
                (
                        "_-_ 修复莲娜Boss部分技能有可能卡死玩家的问题\n" +
                        "_-_ 修复部分极端情况下寻路发生了存档永久崩溃问题，更新该版本进入存档将恢复正常\n" +
                        "_-_ 修复在古堡全面搜查传送卡死在房间的问题，与0层一样，可以点击卡死固定传送回搜查入口点\n" +
                        "_-_ 修复空间残片在古堡三大小游戏中生效导致的各种问题\n" +
                        "_-_ 修复大冰杖在高等级因为移速减至100%卡死的严重问题\n" +
                        "_-_ 修复艾诺琳娜部分技能可能导致游戏闪退的严重问题\n" +
                        "_-_ 修复牧笛贴图尺寸对齐不一致的问题\n" +
                                "_-_ 修复天赋-戒指强化-对部分神器无加成的异常")));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "nerfs"), false, null);
        changes.hardlight(Window.R_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.ARTIFACT_CAPE), "荆棘斗篷",
                "_-_ 物理直伤由等级削弱至等级/2\n" +
                          "_-_ 荆棘力场范围由半径等级/2削弱至半径等级/4 _向上取整_\n" +
                          "_-_ 隐身状态下开启荆棘力场会破除隐身状态"));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.FAINT_GLIMMER), "无暇微光-T3-削弱",
                Messages.get(FaintGlimmer.class,"desc")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.HELL_BUTTERFLY), "炼狱赤蝶-T3-削弱",
                Messages.get(HellButterfly.class,"desc")));
    }

    public static void add_V0948_Changes(ArrayList<ChangeInfo> changeInfos ) {
        ChangeInfo changes = new ChangeInfo("v0.9.4.8-9", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);
        changes.addButton(new ChangeButton(new Image("Ling.png", 0, 0, 16, 16), "开发者的话",
                "v0.9.4.8 — 完成于 2026-06-18 23:45\n\n" +
                        "距离上次写下这段心里话，已经是2023年10月6日，一晃近三年时光匆匆而过。\n" +
                        "曾经懵懂的大学生，如今也走过了许多路途——踏过祖国边疆，也穿梭于繁华都市。\n" +
                        "沿途见识百态，尝过挫折与坎坷，但始终不愿停下脚步，像一株百折不挠的野草稳步前行。\n\n" +
                        "回首望去，魔绫像素地牢早已从当初不起眼的小型模组，在地牢玩家圈子里拥有了属于自己的一席之地，这份成果让我由衷欣喜。\n" +
                        "更让我倍感温暖的是，制作组的伙伴们不断汇聚，队伍愈发壮大：\n" +
                        "Bzmdr、Kuzumi、Sotis、索托斯、手电\n" +
                        "Daniel Calan、寄神、犬罗、小叶、Archetto等制作组的成员\n\n" +
                        "还有每一位一路相伴的群友与玩家，你们是支撑魔绫持续走下去的全部动力。若无诸位，便没有如今的魔绫。\n\n" +
                        "我今年即将年满23岁，或许再过数年，生活琐事会让我无法再像现在这样高频更新、活跃在社区。\n" +
                        "但那一天尚且遥远，当下仍有无限值得珍惜的时光，愿我们都认真活好每一天。\n\n" +
                        "本次更新周期偏长，在此向大家致歉，近期现实生活繁杂，挤占了大量开发时间。\n" +
                        "本次更新内容体量虽不算庞大，但足够各位细细游玩。更新结束后，项目将正式转入「失落古墓」DLC的集中开发周期。\n" +
                        "期待下次相见时，我们能在古墓版本再度畅谈。最后祝愿所有冒险者游玩愉快！\n\n" +
                        "—— MLPD 主开发：JDSALing"));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new ArchettoSprite(), "弦事件调整",
                "现在给予弦珍贵的东西，将可以获得 _灵魂卷轴_。"));

        changes.addButton (new ChangeButton (new ItemSprite(ItemSpriteSheet.BEACON), "新道具：梦想通讯器",
                "完全隐藏道具，只能在异度时空获得（兑换码），可以在这里兑换任意一套时装皮肤。"));

        changes.addButton (new ChangeButton (new ItemSprite(ItemSpriteSheet.SKIN_14), "新皮肤：女仆红狐",
                "售价1500钴币\n\n作者：Priestess & 索托斯 & Daniel Calan\n\n炉边煮好了咖啡，主人，要不要来一杯歇歇？"));

        changes.addButton (new ChangeButton (HeroSprite.avatar (HeroClass.HUNTRESS, 13), "新皮肤：虔书圣徒",
                "售价1500钴币\n\n作者：海鸥\n\n黑袍藏弩，古籍诵咒，以祷言与劲矢一同肃清地牢滋生的邪秽。"));

        changes.addButton (new ChangeButton (HeroSprite.avatar (HeroClass.DUELIST, 14), "新皮肤：埃及艳后",
                "售价1500钴币\n\n作者：过街的白菜——人人喊吃 & 索托斯 & Daniel Calan\n\n大漠热风裹挟着金饰微光，绿洲的女王在此等候你的到来。"));

        changes.addButton (new ChangeButton (HeroSprite.avatar (HeroClass.DUELIST, 7), "武技实装・第二轮更新",
                "以下新增武技武器：\n" +
                        "国王大剑、倒悬的生命树、钻石大剑、丛林毒刺、白浪刺剑、烤鲱鱼、糯米大剑、五仁月饼、\n" +
                        "红魔巨剑、尚方宝剑、煮熟的尚方宝剑、嗜血荆棘、炸弹匕首、肃杀、重型弩炮、涤罪、卡戎的船桨、寻觅长枪、龙鳞 \n\n" +
                        "欢迎体验！"));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.HHBlade), "新Boss专武：钻石大剑",
                "在未击败拟态之王时，首次击杀必定获得。之后武器加入生成池，拟态之王也有小概率掉落。\n\n" +
                        "武器数据：\n\n" +
                        "拟态之王的佩剑，由钻石制成的大剑，漂亮而致命。\n" +
                        "上面附带了一丝拟态之王的魔力，虽然在一定程度上限制了它，但似乎也带来了不错的效果\n" +
                        "4阶，5-25，1-6成长，1.5延迟，2距，17力\n" +
                        "对1距目标造成物理伤害并施加1/5伤害量回合数的中毒\n" +
                        "对2距目标造成法术伤害并施加1/5伤害量回合数的流血\n" +
                        "\n" +
                        "无法增加攻击距离"));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.KING_SWORD), "新Boss专武：国王大剑",
                "在矮人国王处未获得‘掣君无刃’成就时，首次完成该成就必定获得。之后武器加入生成池，矮人国王也有小概率掉落。\n\n" +
                        "武器数据：\n\n" +
                        "矮人国王的仪式用剑，对于矮人来说是一把十足的重剑，就算如此你挥舞它也需要费一番力气\n" +
                        "对恶魔和精英敌人伤害增加50%\n" +
                        "有40%+等级x5的概率对敌人造成5+等级/2回合易伤状态\n" +
                        " 4阶17力，1-7成长，1.5延迟基础，10-35基础伤害\n" +
                        "注：该武器为贤者之剑迭代，老存档将会自动同步！"));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.JUNGLE_SWORD), "新Boss专武：丛林毒刺",
                "在未击败克璃诺斯-森林意志时，首次击杀必定获得。之后武器加入生成池，克璃诺斯-森林意志也有小概率掉落。\n\n" +
                        "武器数据：\n\n" +
                        "从克璃诺斯之果身上拆下的一根强壮枝条，蕴含了极强的毒性以及那些触手的力量。挥舞起来很顺手，不过却不怎么锋利\n" +
                        "3阶，6-12，1-2成长，0.5延迟\n" +
                        "1距，14力\n" +
                        "每次攻击叠加(2LV+3)层中毒\n" +
                        "当中毒层数叠加至目标最大生命值/3层时，对目标施加缠绕与失明，持续回合数与中毒层数相等\n" +
                        "注：生命树也进行了重做"));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.ARTIFACT_CAPE), ("神器重做：荆棘斗篷"),
                ("升级同暗影斗篷(10%充能对应斗篷1充能)，主动使用，可主动关闭，充能百分比式\n" +
                        "自然充能4t/1%，受到伤害额外加(本次伤害/2)%，神器充能1t/5%\n" +
                        "\n" +
                        "使用时1t/5%的消耗\n" +
                        "\n" +
                        "使用时(20+2LV)%~(30+4LV)%免伤\n" +
                        "使用时对半径为(LV/2)的圆形(向下取整)的敌人每回合造成LV点物理伤害并施加1.5LV回合数的流血(不可叠加，重复获取取最高)\n" +
                        "\n" +
                        "挨打使攻击目标受到本次原始伤害的反伤，并叠加LV回合数的流血")));

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

        changes.addButton( new ChangeButton( new TalentIcon(Talent.RUNIC_TRANSFERENCE), Talent.RUNIC_TRANSFERENCE.title(),
                "现在该天赋新增了其他英雄的嬗变策略，详情可参考该英雄的蜕变描述。"));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.FLOWERY), "新书籍：元素编年史",
                        "大概率提供魔能透视，小概率提供元素祝福。\n" +
                        "在那些回忆商店有概率出现，出现时，绿林圣境守则书籍将不再该商店生成"));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
        changes.hardlight(CharSprite.WARNING);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.SEED_AIKELAIER), ("查种器V3.1"),
                ("1.移除强力搜查模式\n" +
                        "2.修复搜索过程中可能卡住的问题\n" +
                        "3.使用多线程提升查询速度（也是移除强力搜查模式的原因）\n" +
                        "4.查种结束后显示对应的物品楼层位置")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.ARTIFACT_HORN4), "丰饶之角",
                "现在贮存食物时，将自动跳转到食物袋！"));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.RANDOM_CHEST), "存档提取系统",
                "鉴于0.9.4.8旧存档崩溃过多，但是存档克隆问题太多，所以加了这个，如果你真的需要老存档，欢迎自行提取并联系我们人工恢复。"));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.LONGSWORD), "伤害系统v0.3",
                "现在真实伤害将会破盾，请万般小心！"));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.LANTERNB), "灯火前路：v3.3",
                "现在灯火25以下后，常规怪物不再掉落物品。"));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.RING_DIAMOND), "戒指重构",
                "原有韧性、元素、精准、闪避四枚戒指实战强度偏弱，本次更新将其整合重构，替换为两枚全新戒指：\n\n" +
                        "_-_ 庇护之戒：同时提供韧性与元素抗性加成\n" +
                        "_-_ 集中之戒：同时提升命中精准与闪避能力\n\n" +
                        "注：旧存档内的对应戒指会自动转换，若出现效果失效问题，取下戒指重新装备即可恢复正常。"));

        changes.addButton(new ChangeButton(Icons.get(Icons.DISPLAY), ("UI优化"),
                ("_-_ 现在Buff显示进行了优化，特别感谢Shower-沐沐的帮忙\n" +
                        "_-_ 现在Boss血条同步了破碎3.3.8，并且追加了血条动画，小血条也同步加入\n" +
                        "_-_ 部分UI界面进行了优化迭代")));

        changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
                ("V0.9.4.9：\n\n" +
                        "_-_ 修复旧存档兼容性问题导致的各种异常\n" +
                        "_-_ 修复引力王冠的一些崩溃问题\n" +
                        "_-_ 修复怪物不掉落东西的异常问题\n" +
                        "_-_ 修复女猎三皮伤害异常的问题\n" +
                        "_-_ 修复Bzmdr礼物英雄视野异常的问题\n" +
                        "_-_ 修复蓄血圣杯可以在荆棘激活时献祭的异常\n" +
                        "_-_ 修复部分戒指的数值显示异常\n" +
                        "_-_ 修复英雄小人素材缺少1px的问题，来自watabou的特性（2014-07-27）在4349天后被处理\n" +
                        "_-_ 修复部分徽章无法显示的异常\n" +
                        "_-_ 修复变形天赋文本不显示的问题\n"+
                        "_-_ 粽子的生成概率下调至0.85f，比肉馅饼稍微低了一些，而不是之前的3权重"),
                (	    "V0.9.4.8：\n\n" +
                        "_-_ 修复异果层自爆会摧毁物品的异常\n" +
                        "_-_ 修复在br1层触发传送类陷阱/空间残片会导致地上跟着一起传送(3*3范围)的物品消失\n" +
                        "_-_ 多Boss血条移除，因为这导致了很多崩溃\n" +
                        "_-_ 修复石油大井部分文案异常\n" +
                        "_-_ 修复星尘藏品会导致主动效果回合全部减半的异常\n" +
                        "_-_ 修复商人佩剑倒卖也要打折的异常\n" +
                        "_-_ 修复新0层可使用返回晶柱的异常\n" +
                        "_-_ 创世神之心现在不会再被祝福\n" +
                        "_-_ 修复近期的工单，游戏崩溃等相关问题")));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "nerfs"), false, null);
        changes.hardlight(Window.R_COLOR);
        changeInfos.add(changes);

        Image s =new CrivusStarFruitsSprite();
        s.scale.set(PixelScene.align(0.8f));
        changes.addButton(new ChangeButton(s, ("异果调整"),
                ("_-_ 现在异果二阶段的触手可以被攻击，攻击会提供给英雄少量灵视，在异果战斗结束后会去除灵视效果（无论你有多少回合）\n" +
                        "_-_ 异果新增 丛林毒刺 Boss专武")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.GREENBOOKS), "绿林圣境守则书籍调整",
                "绿林圣境守则普通效果更改，改成给予0-20树肤。"));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.WAND_HAND_CONTROL), "法师之手调整",
                "鉴于目前法师之手轻微超模，将法师之手英雄可见视野移除。"));

        changes.addButton(new ChangeButton(new PumkingBomberSprite(), "南瓜姜饼人调整",
                "鉴于目前与时间气泡有严重卡死冲突且短时间无法解决，在困难模式下将自动死亡，但无经验提供。"));

    }

    public static void add_V0945_Changes(ArrayList<ChangeInfo> changeInfos ) {
        ChangeInfo changes = new ChangeInfo("v0.9.4.5", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(Icons.get(Icons.AUDIO), ("新原创音乐：王宫议事-将军"),
                ("矮人将军专属音乐，现已正式到来。\n\nBy Tatsro")));

        changes.addButton( new ChangeButton(HeroSprite.avatar(HeroClass.MAGE, 1), "大立绘更新",
                "原皮大立绘进行迭代更新"));

        changes.addButton(new ChangeButton(new ShopkKingSprite(), ("抢劫商店优化"),
                ("如果拾取回溯之书，则不能前往抢劫。")));

        Image s =new CrivusStarFruitsSprite();
        s.scale.set(PixelScene.align(0.8f));
        changes.addButton(new ChangeButton(s, ("异果优化"),
                ("死亡时将产生延时爆炸。")));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
        changes.hardlight(CharSprite.WARNING);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
                (	    "V0.9.4.5：\n\n" +
                        "_-_ 修复大雷霆法杖ARC效果可能空指针导致的闪退游戏异常\n" +
                        "_-_ 修复解离法杖不能穿透免伤的异常\n" +
                        "_-_ 修复地狱赤蝶在以下情况的异常\n" +
                        "_-_ ①磷火飞镖扎幻想结界水晶现在可以上地狱火\n" +
                        "_-_ ②地狱火现在可以清除异果三阶段的护盾\n" +
                        "_-_ 修复藏品望远镜导致投掷物打开面板异常\n" +
                        "_-_ 修复近期的闪退崩溃相关问题")));
    }

    public static void add_V0942_Changes(ArrayList<ChangeInfo> changeInfos ) {
        ChangeInfo changes = new ChangeInfo("v0.9.4.2", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(Icons.get(Icons.LANGS), ("语言更新：繁体中文"),
                ("来自Sotis的繁体中文已经更新\n\n当前繁体中文语言包版本：2026/4/16")));

        changes.addButton( new ChangeButton(new BuffIcon(BuffIndicator.ANCIENT_SURVEY, true), "远古遗迹Buff",
                "现在游玩远古遗迹，可以实时查看自己的调查点数。"));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
        changes.hardlight(CharSprite.WARNING);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(Icons.get(Icons.INFO), ("伤害系统v0.2"),
                ("修复伤害系统默认为魔法伤害图标的显示，伤害实际来源是没有问题的。\n\n并且现在真实伤害拥有独特图标：绿剑")));

        changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
                (	    "V0.9.4.2：\n\n" +
                        "_-_ 修复雷霆法杖ARC效果可能空指针导致的闪退游戏异常\n" +
                        "_-_ 修复英雄护甲一个潜在的可能导致空指针的问题闪退游戏异常\n" +
                        "_-_ 修复Boss多血条渲染潜在的一些问题\n" +
                        "_-_ 修复部分远古存档徽章更新后的闪退，但部分过于老旧版本的徽章可能会直接作废\n" +
                        "_-_ 修复糯米大剑的一些潜在游戏崩溃问题\n" +
                        "_-_ 修复因伤害系统改变，远古遗迹怪物部分技能异常\n" +
                        "_-_ 优化法师之手图鉴显示需求\n" +
                        "_-_ 优化传送方法底层代码")));


        changes = new ChangeInfo(Messages.get(ChangesScene.class, "nerfs"), false, null);
        changes.hardlight(Window.R_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new NyarlathotepSprite(), ("古堡奈亚调整"),
                ("奈亚子生成陷阱现在不包含缴械陷阱")));
    }

    public static void add_V0940_Changes(ArrayList<ChangeInfo> changeInfos ) {
        ChangeInfo changes = new ChangeInfo("v0.9.4.0-1", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(Icons.get(Icons.LANGS), ("语言更新：英文"),
                ("英文翻译更新，本次使用PDTR和英文翻译组初审通过，也许还有诸多问题，不过翻译完善率已达到95%")));

        changes.addButton(new ChangeButton(Icons.get(Icons.AUDIO), ("音乐调整"),
                ("魔绫像素地牢音乐正在进行原创迭代，因此部分音乐已有调整。")));

        changes.addButton(new ChangeButton(Icons.get(Icons.BADGES), ("新-隐藏徽章"),
                ("新的隐藏徽章现已上线，游玩游戏获得！\n\n" +
                        "隐藏徽章：与子偕行")));

        changes.addButton(new ChangeButton(Icons.get(Icons.NEWS), "在线兑换码系统",
                "全新在线兑换码系统实装，可通过落白商店或游戏菜单兑换奖励。\n\n" +
                        "从0.9.4.0开始，兑换码将在游戏新闻，官方群动态更新。"));

        changes.addButton(new ChangeButton(Icons.get(Icons.CATALOG), "排行榜收藏夹系统",
                "排行榜收藏夹功能上线，可收藏关注心仪的排行榜记录。\n\n至多可记录20份"));

        changes.addButton(new ChangeButton(new DogDogMusicSprite(),"0层内容更新",
                "常规0层完善推进，新增大量NPC与交互内容。\n" +
                        "- 新增0层NPC：犬、A神、幽寂\n" +
                        "- 薄绿每局30%概率在旅馆出现\n" +
                        "- 犬罗固定在旅馆出现\n" +
                        "- 旅馆四组桌子随机NPC组合实装"));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.CICRE_MUSIC), "新武器：轻音铃铛",
                "身形移动，便会打断鸣响的连击。\n攻击奏响「轻音」，连续攻击引动「回音」"));

        changes.addButton(new ChangeButton(new KusumiSprites(), "久住事件推进",
                "久住相关剧情持续更新，全新相关事件等待探索。"));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.HELL_BUTTERFLY), "新藏品：炼狱赤蝶",
                "你获得<#E5579A>磷火和地狱火<RGB>免疫，在怪物被施加_磷火_时替换为<#E94134>地狱火<RGB>。携带地狱火的怪物每回合受到<#E94134>20%最大生命值<RGB>的魔法伤害，对于没有<#E94134>任何精英或突变词条的怪物，伤害类型变为真实<RGB>。\n\nBoss单位携带地狱火时，效果改为每回合受到<#E94134>20点魔法伤害<RGB>，并清空身上的任何护盾。地狱火可以像磷火一样传播。\n\n“扣1送地狱火”\n\n“骗你的，不扣也送。嘻嘻。”"));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.SPACE_DEBRIS), "新藏品：空间残片",
                "某处被强行撕裂开的空间崩裂出的碎片，似乎并不能借助它的力量…\n\n每<#52B9D8>50回合进行一次本层的随机传送（不会传送到被钥匙门封锁的房间里）<RGB>， 如果主动跨层传送将会在原传送位偏移3层(不会进入新楼层)。\n\n持有时，每次使用被残片传送，或使用传送卷轴，或触发传送/虫洞/扭曲陷阱都会<#52B9D8>损失10%当前生命值<RGB>（生命值为1时可致死），如果是残片传送，还能获得<#52B9D8>2回合眩晕<RGB>"));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.EASTER_EGG), "清明+复活-双节活动",
                "清明限定复活活动正式开启，参与可获得对应活动奖励。"));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.SHOP_SWORD), "新武器：商人配剑",
                "商人配剑正式加入，将自动替换旧版存档中的手斧。\n\n它通常作为商人之间用来互相证明身份的道具，但在应急情况下你也可以把它当做武器。\n\n这把武器目前可以使商人降价 10% ，最高降价90%%。"));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.UNLESSFLOWER), "新物品：不朽花环",
                "看上去是用不知名的花朵与坚韧的藤条编织而成的花环。戴在头上时，你几乎感受不到它的重量。\n\n全新十字架类物品，当死亡时，该优先级最高"));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
        changes.hardlight(CharSprite.WARNING);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new MoonCatSprite(), "0层NPC规则",
                "老板娘：完成13挑以上后，给予额外净化药剂\n\n" +
                        "以下为常规，无需13挑：\n"+
                        "一号桌子（随机2人）：雨夜、小骑士、晓梦\n" +
                        "二号桌子（随机2人）：月华、狐狸、魔法绵羊、喵音·椛\n" +
                        "三号桌子（随机2人）：紫澪、小蓝、深海、浅月深白\n" +
                        "四号桌子（斗地主常驻）：小叶、琴里、Choco"));

        changes.addButton(new ChangeButton(new ATRISprite(), "2楼NPC刷新",
                "澪：概率出没，无限制\n" +
                        "2号房间：萝卜子默认常驻，可兑换口粮换皮\n" +
                        "1号房间：30%概率出现莲娜（完成抢劫事件），奖励烈焰药剂\n" +
                        "3号房间：30%概率出现冬铃（击败冰雪魔女），奖励冰霜药剂"));

        changes.addButton(new ChangeButton(new SliceGirlSprite(), "古堡分数系统",
                "【刻耳柏洛斯】\n" +
                        "- 恶狗扑食每击中玩家：-200分\n" +
                        "- 凝血晶体每吸收一次：-100分\n" +
                        "- 击败Boss：+6000分\n\n" +
                        "【墨菲厄斯】\n" +
                        "- 小游戏结束后：获得总分×0.25\n" +
                        "- 机械之柱抛射体中心命中：-300分\n" +
                        "- 时间之柱激光命中：-400分\n" +
                        "- 放逐Boss：+10000分"));

        changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
                (	    "V0.9.4.1：\n\n" +
                        "_-_ 修复空间残片在1血时传送不致死\n" +
                        "_-_ 修复0层法伊娜未生成\n" +
                        "_-_ 修复空间残片传送会触发很多次扣血，导致血量迅速降低\n" +
                        "_-_ 修复指南书未在0层生成\n" +
                        "_-_ 修复在商人处回购不消耗金币\n" +
                        "_-_ 修复炼金指南中部分炼金能量不一致\n" +
                        "_-_ 修复轻音铃铛武器效果异常问题\n" +
                        "_-_ 修复0层时装自选凭证未生成的问题\n" +
                        "_-_ 修复幽寂未在孤城生成的问题\n" +
                        "_-_ 修复矮人将军处小叶错误生成的问题")));

        changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
                ("_-_ 修复幸运手套回购Bug\n" +
                        "_-_ 修复微光向导在Boss层逃课问题\n" +
                        "_-_ 修复无用之盾未生效问题\n" +
                        "_-_ 部分地牢底层代码优化\n" +
                        "_-_ 开发者模式状态查看器新增常规/综合掉落率\n" +
                        "_-_ 优化异果层判定，未锁定楼层可召唤盟友\n" +
                        "_-_ 绝对失明效果正式启用\n" +
                        "_-_ 新战法重命名：敕灵天女\n" +
                        "_-_ 修复吃豆人地图无法拾取有形之手法杖\n" +
                        "_-_ 修复创世神之心活死人问题\n" +
                        "_-_ 修复共生、胭脂物品掉落异常\n" +
                        "_-_ 修复古堡非彩蛋结局无法获得挑战徽章\n" +
                        "_-_ 修复磷焰飞镖部分问题\n" +
                        "_-_ 修复小叶跨存档阻止下楼问题")));

        changes.addButton( new ChangeButton(new Image(Assets.Environment.TILES_SEWERS, 48, 80, 16
                , 16), "房间/事件改动",
                        "优化慧眼墓碑房间、黑魂事件\n\n" +
                                "现在在触发这两个事件后，将在重生十字架（未祝福）里面有相应的提示"));

        changes.addButton(new ChangeButton(Icons.get(Icons.CHALLENGE_ON), ("药水癔症v0.4"),
                ("现在全肉大饼可显示激活力量的概率区间。")));

        changes.addButton(new ChangeButton(new RivalSprite(), ("黑魂事件"),
                ("在灯火低于40+未祝福的十字架会出现黑魂，现在黑魂你可以看见，并且黑魂的描述也有所优化")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.SKELETONGOLD), ("染血金币控制终端"),
                ("现在里面的选中可以常驻，无需每局重新勾选Boss。")));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "nerfs"), false, null);
        changes.hardlight(Window.R_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(Icons.get(Icons.WARNING), "内容延期",
                "螃蟹王Boss更新延期，具体上线时间日后再议。"));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.SPACE_DEBRIS), "空间残片",
                "现在只有主动传送扣血了，且变为区间8~15%"));

        changes.addButton(new ChangeButton(new FodderSprite(), "恶魔盛宴",
                "因为其AI逻辑特殊性，现在胭脂触发沉沦恶魔盛宴时，恶魔盛宴立刻死亡。"));

        changes.addButton( new ChangeButton(new TalentIcon(Talent.STRONGMAN), "天赋调整",
                        "排山倒海调整：\n" +
                        "修复排山倒海加成异常，现在不会+1因为Bug就吃到全部精准了。"));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.CATGIRL_COSPLAY), "猫娘cosPlay",
                "触发概率从12%→3.25%"));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.BROKENBONE), "折断之骨",
                "幸运掉落从降至30%-->降至70%"));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.BROKEN_RING), "破碎之环",
                "正在优化该藏品的手感，本版本暂时移除生成"));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "buffs"), false, null);
        changes.hardlight(Window.GREEN_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.BOMB), "炸弹加强",
                "电击炸弹改为雷霆炸弹，奥术改为奥磷炸弹"));

        changes.addButton(new ChangeButton(Icons.get(Icons.UP_DICT), "破碎间接同步",
                "_-_ 涌流附魔特效同步\n" +
                        "_-_ 焰浪、冲击波、雷霆、战法技能同步\n" +
                        "_-_ 大雷、磷火战法技能与雷霆、焰浪一致迭代\n" +
                        "_-_ 同步破碎新饰品：雪貂绒束、幻象裂镜\n" +
                        "_-_ 全面同步破碎炸弹\n" +
                        "_-_ 全面同步破碎闪避图标，新增鱼甲闪避图标"));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.HIGHTWAND_3), "磷焰法杖加强",
                "现在施法与焰浪法杖范围一致，但磷焰的火焰持续比焰浪久。并且可穿透。"));
    }

    public static void add_V0931_Changes(ArrayList<ChangeInfo> changeInfos ) {
        ChangeInfo changes = new ChangeInfo("v0.9.3.1", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new Image(Assets.Interfaces.HAICONS, 32, 0, 16, 16), "困难模式-2026",
                "困难模式重磅回归！欢迎前来测试！\n" +
                        "详情请参考难度描述中的介绍。"));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.KILL_EYES), "猎杀者之眼-T3-正面",
                Messages.get(KillEye.class,"desc")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.PURE_ROUGE), "珍贵的胭脂-T3-正面",
                Messages.get(PureRouge.class,"desc")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.FAINT_GLIMMER), "无暇微光-T3-正面",
                Messages.get(FaintGlimmer.class,"desc")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.DIRT_KNIFE_STAND), "土刀架-T2-负面",
                Messages.get(Dirt_KnifeStand.class,"desc")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.CATGIRL_COSPLAY), "猫娘cosply手册-UNDEF",
                Messages.get(CatGirlCosplay.class,"desc")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.RESOLVE_DIE), "去往死的决意-UNDEF",
                Messages.get(DeadOrAlive.class,"desc")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.DREAM_SEED), "梦之种-UNDEF",
                Messages.get(DreamSeed.class,"desc")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.NOTEOFBZMDR), "Bzmdr的笔记-T3-负面",
                Messages.get(NoteOfBzmdr.class,"desc")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.BROKEN_RING), "破碎之环-T3-负面",
                Messages.get(BrokenRing.class,"desc")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.STAR_DUST), "星尘-T3-负面",
                Messages.get(StarDust.class,"desc")));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
        changes.hardlight(CharSprite.WARNING);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.SEED_FADELEAF), "种子系统优化",
                "同一个种子，不同的生成问题由来已久。\n\n" +
                        "我已经在新版进行了优化，可保证主楼层95%吻合度，子楼层暂时还在思考解决方法。"));

        changes.addButton(new ChangeButton(Icons.get(Icons.PREFS), ("杂项改动"),
                ("1.素材优化迭代\n" +
                        "2.墓志铭文案修改\n" +
                        "3.骸骨房间英雄遗骸改为箱子")));

        changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
                ("_-_ 修复盗贼角色皮肤未生效的问题\n" +
                        "_-_ 修复金蝶0分倍率异常\n" +
                        "_-_ 修复藏品高SDK的异常\n" +
                        "_-_ 修复土刀架造成的黄饿不回血的异常\n" +
                        "_-_ 修复古神Boss极端情况下的无敌异常\n" +
                        "_-_ 修复跳楼房在子层生成异常\n" +
                        "_-_ 修复猎杀者的独眼的穿甲对猎杀者自身的必定伏击不生效的异常\n" +
                        "_-_ 修复在小叶处选择获取三阶藏品时有概率获得垃圾(藏品池未空)的异常\n" +
                        "_-_ 修复部分楼层无视小叶直接下楼的卡死")
        ));

        changes.addButton(new ChangeButton(Icons.get(Icons.DISPLAY), ("显示调整"),
                ("_-_ 现在支持多Boss血条\n" +
                        "_-_ 现在特殊皮肤不再显示草")));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "nerfs"), false, null);
        changes.hardlight(Window.R_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.TREELIST), ("树痕调整"),
                ("每下攻击攒2+等级/5 生命护盾量，上限20+等级*2\n" +
                        "储存盾量只可吸收奥术护盾，不吸收生命护盾")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.ARMORSCALESOFBZMDR), "Bzmdr的盔甲鳞片-降级到T1",
                Messages.get(ArmorScalesOfBzmdr.class,"desc")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.HEARTOFCRYSTALFRACTAL), "水晶之心的噩梦-T1-负面",
                Messages.get(HeartOfCrystalFractal.class,"desc")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.PORTABLEWHETSTONE), "携带型砥石-T1-正面",
                Messages.get(PortableWhetstone.class,"desc")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.BROKENBONE), "折断之骨-T1-负面",
                Messages.get(BrokenBone.class,"desc")));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "buffs"), false, null);
        changes.hardlight(Window.GREEN_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.DELICIOUSRECIPE), "惠民食谱-T1-正面",
                Messages.get(DeliciousRecipe.class,"desc")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.MONOCULAR), "单筒望远镜-T2-正面",
                Messages.get(Monocular.class,"desc")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.YANSTUDYINGPAPERTWO), "言的研究手稿<其二>-T2-正面",
                Messages.get(YanStudyingPaperTwo.class,"desc")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.WENSTUDYINGPAPERONE), "文的研究手稿<其一>-T2-正面",
                Messages.get(WenStudyingPaperOne.class,"desc")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.YANSTUDYINGPAPERONE), "言的研究手稿<其一>-T2-负面",
                Messages.get(YanStudyingPaperOne.class,"desc")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.KINGHTSTABBINGSWORD), "迅捷骑士刺剑-T2-正面",
                Messages.get(KnightStabbingSword.class,"desc")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.RAPIDEARTHROOT), "速生地缚根-提升到T2",
                Messages.get(RapidEarthRoot.class,"desc")));

    }

    public static void add_V0925_Changes(ArrayList<ChangeInfo> changeInfos ) {
        ChangeInfo changes = new ChangeInfo("v0.9.2.5-6", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new WhiteGirlSprites(), ("魔绫5岁啦！"),
                ("2021-2-12---2026-2-12\n\n五载同行，共赴新程\n" +
                        "祝所有魔绫地牢玩家心想事成，鸿运当头，马到成功。")));

        changes.addButton(new ChangeButton(Icons.get(Icons.WARNING), ("新系统：突发事件"),
                ("新系统：突发事件 开始投入测试，它会让你的冒险增加更多随机性。\n\n" +
                        "_-_ No.1---突发事件-冰与火之歌\n" +
                        "_-_ 触发说明：与莲娜战斗时，有20% + 挑战数量 x 5%的概率出现。\n" +
                        "_-_ 事件说明：冰雪魔女-冬铃加入本场战斗，且冬铃的行为发生改变。")));

        changes.addButton(new ChangeButton(new ElementalSprite.HaloFire(), ("新怪物：磷焰精英"),
                ("火元素生成时有10%的概率变成该怪物，击败必定掉落一瓶龙王祝福合剂。")));

        changes.addButton(new ChangeButton(Icons.get(Icons.NEWS), ("新兑换码：FiveYearsOld"),
                ("进入游戏通过落白商店 或者 游戏菜单 获取奖励！\n\n有效期：2026-3-4 00:00前")));

        changes.addButton(new ChangeButton(Icons.get(Icons.DISPLAY), ("新系统：周年庆UI"),
                ("现在可以自选周年庆了，默认五周年，从2周年-5周年均可选择。")));

        changes.addButton(new ChangeButton(Icons.get(Icons.LANGS), ("语言更新：繁体中文"),
                ("来自Sotis提供的繁体中文已经更新。")));

        changes.addButton(new ChangeButton(Icons.get(Icons.UP_DICT), ("0层翻新"),
                ("0层再次翻新，接近30个NPC加入，还要各种各样的新年赠送，欢迎各位游玩！")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.BZMDR_GIFT), ("新物品：Bzmdr的新年礼物"),
                ("想要挑战极限？那就战！！！")));

        changes.addButton(new ChangeButton(Icons.get(Icons.CATALOG), ("上下楼文本更新"),
                ("五周年更新了全新38个文本，来自众多的玩家投稿。\n\n让你上下楼也会有新的体验！")));

        changes.addButton(new ChangeButton(new ClearElemtGuardGirlSprites(), ("法伊娜后续剧情"),
                ("火龙事件之后，新年即将到来之际，法伊娜突然离开了小镇，她去了哪里？\n\n前往火龙曾经的巢穴，或许会有情报。")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.MOTO_BREW), ("新物品：莫洛托夫烈焰特调"),
                ("一瓶冒着火星的琥珀色烈酒，瓶塞缠裹着浸油火绒。\n\n饮用后可获得治疗、激素涌动、极速与火焰之力加持，但酒性刚烈，饮用后会短暂头晕目眩。")));

        changes.addButton(new ChangeButton(Icons.get(Icons.BADGES), ("新-普通徽章 & 隐藏徽章"),
                ("新的一批普通 & 隐藏徽章现已上线，游玩游戏获得！\n\n" +
                        "普通徽章：尽竭\n\n" +
                        "隐藏徽章：勇敢者的印记")));

        changes.addButton(new ChangeButton(new KusumiSprites(), ("久住新年特别版"),
                ("新年到来之际，她却只能在无尽轮回中默默等待毁灭降临，如果你能去看看她，说不定会感谢你呢。\n\n" +
                        "在9层下楼携带一种特殊的信物将有概率前往。")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.Flower_Cake), ("新食物：桃花饼"),
                ("与新年小镇的白宴交谈可获得。")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.BLESS_SCROLL), ("新物品：赐福卷轴"),
                ("与新年小镇的小叶交谈可获得。")));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
        changes.hardlight(CharSprite.WARNING);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
                            ("_-_ 修复拟态之王的无敌异常\n" +
                             "_-_ 修复白浪刺剑的部分判定导致的游戏崩溃问题\n" +
                             "_-_ 修复法师之手极端情况下的卡死问题"),

                            ("_-_ 修复部分素材的渲染异常崩溃\n" +
                            "_-_ 修复低安卓系统，部分新API无法调用导致的崩溃异常\n" +
                            "_-_ 修复FireBase上面提到的一系列崩溃异常\n" +
                            "_-_ 修复烈阳&暗金护符在魔法免疫下仍可使用的问题\n" +
                            "_-_ 修复藏品重复问题")

        ));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.TREELIST), ("树痕调整"),
                ("二阶，力量需求13\n" +
                        "\n" +
                        "初始3-14，成长1-1\n" +
                        "\n" +
                        "初始护甲0-3，成长0-1\n" +
                        "\n" +
                        "这把武器可以积蓄生命能量以释放生命护盾，上限为30+5*武器等级\n" +
                        "\n" +
                        "攻击将积攒2+0.2*等级点护盾。可以花费1回合将这些护盾释放出来。\n" +
                        "\n" +
                        "能够吸收空气中魔力生长的植物。")));

        changes.addButton(new ChangeButton(Icons.get(Icons.PREFS), ("杂项改动"),
                ("1.素材优化迭代\n" +
                        "2.部分文案优化迭代\n" +
                        "3.V3对话框已重构，可直接跳过（除部分关键剧情无法跳过）\n" +
                        "4.火龙剧情优化，撒谎分支路线移除\n" +
                        "5.游戏新闻界面滑动区域优化，请在右侧区域滑动")));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "nerfs"), false, null);
        changes.hardlight(Window.R_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(Icons.get(Icons.BADGES), ("徽章移除"),
                ("药水研究员，道具专家，卷轴研究员因无法获取现已移除。")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.HIGHTWAND_7), ("法杖调整"),
                ("_-_ 大冰杖每级额外减速5%\n" +
                        "_-_ 烈阳每五级可以多召唤一个太阳，初始只能召唤一个太阳，每点充能只能续2回合命\n" +
                        "_-_ 老魔杖在转职战斗巫女(旧版)的时候大幅度提升基础数值，小幅度调整成长\n" +
                        "_-_ 共生法杖将无法受到任何形式的充能，只能在每获得半级经验的时候充能一次")));

        changes.addButton(new ChangeButton(new AoReadyDragonSprite(), ("奥尔祖龙"),
                ("由于剧情编写组上有一个错误的乌龙，导致此怪诞生。此怪与现有新剧情框架冲突，故而移除。")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.LINGPEA), ("棱晶调整"),
                ("每75回合生成20回合的丛林守护之盾。")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.WEAPON_HOLDER), ("寒冰神扇"),
                ("此武器正式移除。")));
        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.WEAPON_HOLDER), ("终焉"),
                ("此武器正式移除。")));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "buffs"), false, null);
        changes.hardlight(Window.GREEN_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.FIVEREN), ("五仁月饼"),
                ("完成中秋节事件且获得真结局后，该武器可在游戏内生成。")));


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

        changes.addButton(new ChangeButton(Icons.get(Icons.CATALOG), ("新系统：效果百科"),
                ("在图鉴中新增效果百科，目前尚不完善，后续版本会陆续完善。")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.SKIN_11), ("新皮肤：晚宴邀请"),
                ("已可在时装商人处购买，售价1500钴币")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.OLDSUNSHADOW), ("新道具:往日投影"),
                ("水晶上面映射出各种可能性，你能否找到属于你的位面？")));

        changes.addButton(new ChangeButton(Icons.get(Icons.DISPLAY), ("材质包系统1.0"),
                ("材质包系统正式登场，目前预置了5个材质包，你也可以自行定制材质包。\n\n" +
                        "如要定制材质包，请查阅游戏新闻材质包置顶使用说明。")));

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
                ("精巧纹章天赋生效时，可对纹章使用祛邪卷轴；诅咒菱晶现在也可以对诅咒纹章提供一次永久升级。")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.SCROLL_GOLD), ("金蝶模式调整"),
                ("1.金蝶任务2：每大层至多因为猎杀金蝶怪物获得额外1次嬗变，五区累计总计5次嬗变\n" +
                        "2.修复抢劫可上楼导致的严重bug")));

        changes.addButton(new ChangeButton(new NyarlathotepSprite(), ("古堡奈亚调整"),
                ("奈亚子生成陷阱现在不包含塌方陷阱")));

        Image sss =new MyCoreHeartSprite();
        sss.scale.set(PixelScene.align(0.4f));
        changes.addButton(new ChangeButton(sss, ("古堡宇宙之心调整"),
                ("_往昔的幻影_若转化为友方阵营将会被宇宙之心处决")));


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

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.RITUAL_SWORD_M), ("新武器：仪式短剑"),
                ("二阶，力量需求12\n" +
                        "\n" +
                        "初始3-15，成长1-3\n" +
                        "\n" +
                        "这把武器不会被诅咒，并且使献祭仪式更加高效（只需要杀一个怪）。\n" +
                        "\n" +
                        "曾被主教用于主持多场宗教仪式。")));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.TREELIST), ("新武器：树痕"),
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

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.WEAPON_HOLDER), ("新武器：破军"),
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
}
