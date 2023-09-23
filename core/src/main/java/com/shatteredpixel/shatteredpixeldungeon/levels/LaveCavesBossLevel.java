package com.shatteredpixel.shatteredpixeldungeon.levels;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.tiles.CustomTilemap;
import com.watabou.noosa.Tilemap;

/***
 * TODO LIST
 * 熔岩地块-【5层-EX子层】-火龙巢穴
 * 先决条件：在新版本不击败任何纯晶元素，在第五层解救纯晶意识集合体，与之对话，进入火龙的巢穴。
 *
 * Boss档案：
 * 名称：熔岩火龙 	血量：300 	常规攻击：9-14
 * 防御力：8 	魔法防御：0 移速：正常 攻速：正常
 * 特性：每次攻击最多造成15伤害，至少需要20次攻击。
 * 性质：飞行单位	异常抗性：免疫腐化，燃烧，鬼磷，霜火
 *
 * 区块描述：
 * 	 丛林曾经没有如此的死寂，直到火龙的到来改变了这一切。它污染了曾经的森林核心“奥克拉赫”之花，
 * 	 导致这里的守护者纯晶元素失去能量，而森林核心也成为了现在的“毒瘤”。如今我们受纯晶智能集合体的委托，
 * 	 前往森林最底部的熔岩地块，寻找一切的罪魁祸首，熔岩火龙！
 *
 * Boss技能描述：
 * 普攻：每3回合发射一次熔岩冲击波，造成一次烈焰区域范围。
 *
 * 定向进化-毒雾缠绕：
 * 若场上存在缠绕元素，火龙可在后续定向进化获得免疫毒气的能力，并且场上地图毒雾弥漫。
 *
 * 定向进化-烈焰吐息：
 * 若场上存在纯晶元素，火龙可在后续定向进化中获得烈焰强化冲击波，强化普通攻击
 * 定向进化-暗影行踪：
 * 若场上存在暗影元素，火龙可在后续定向进化中获得暗影袭击，
 * 在攻击前发起警告，并立刻隐身数回合。隐身时玩家无法攻击火龙，火龙对玩家的伤害减半。
 *
 * 定向进化-集结号令：
 * 若场上存在赤红元素，火龙可在后续定向进化中获得集结号令。
 * 此时火龙召唤两个分身。并且移速提升。分身仅获得本体的25%伤害。
 *
 * 定向进化-狂野攻击：
 * 若场上存在紫晶元素，火龙可在后续定向进化中获得狂野攻击。此时火龙每隔10回合立刻尝试锁定玩家，
 * 该技能有30%失败，一旦成功，玩家会失去3回合，并且受到普攻3次伤害+3级流血伤害
 *
 * 阶段说明：
 *
 * 第一阶段：（300-200）
 * 火龙进行常规性攻击，在此期间会每隔7回合随机召唤各种元素。
 * 元素没有任何攻击能力，但会周期性攻击玩家赋予Debuff,
 * 当场上高于4个元素时，火龙会随机污染一个元素获得对应的定向进化。
 *
 * 第二阶段：（<200-100）
 * 火龙立刻失去全部定向进化，进入短暂无敌时间。
 * 火龙的攻击在无敌时间频率更加频繁，且无敌时间结束后直接随机定向进化两个词条。赋予玩家极度燃烧的Debuff,在燃烧阈值满后，立刻造成25%的玩家最大血量伤害。
 *
 * 第三阶段（<100）
 * 火龙立刻获得1000护盾，但失去攻击能力。火龙试图逃离丛林，利用纯晶集合体给的物品击中火龙四次，火龙将被彻底击败，并使用火龙之心净化丛林，整场战斗结束。
 *
 *
 * 成就徽章：
 * 屠龙勇士—击败丛林的真正罪魁祸首“熔岩火龙”
 * 丛林英雄—击败熔岩火龙并净化丛林
 *
 * 加密徽章：
 * 熔岩审判-在岩浆地块下给予火龙最后一击！
 *
 * 后续剧情：
 * 1.纯晶元素将在后续丛林新档中变为中立，不会主动攻击玩家
 * 2.在下半段更新中，会有额外剧情
 * 3.在今后的旅程中，
 * 毒苹果将有概率变成奥克拉赫之花考验冒险者的意志。
 * */
public class LaveCavesBossLevel extends Level{
    private static final short W = Terrain.WALL;

    private static final short R = Terrain.WATER;

    private static final short Y = Terrain.EMPTY;

    private static final short V = Terrain.EMPTY_SP;

    private static final short X = Terrain.ENTRANCE;

    private static final short G = Terrain.HIGH_GRASS;

    private static final short D = Terrain.SECRET_DOOR;
    private static final short M = Terrain.WELL;
    private static final short L= Terrain.PEDESTAL;
    private static final int[] codedMap = {
            W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,
            W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,Y,W,W,W,W,W,W,W,
            W,W,W,W,W,W,W,W,R,R,R,R,R,R,R,R,R,R,R,R,R,R,R,W,W,W,W,W,W,W,W,W,
            W,W,W,W,W,W,R,R,R,R,R,R,R,R,R,R,R,R,R,R,Y,R,R,R,R,W,W,W,W,W,W,W,
            W,W,W,W,R,R,R,R,R,R,R,R,R,R,Y,Y,R,R,Y,R,R,R,R,R,Y,W,W,W,W,W,W,W,
            W,W,W,R,R,R,R,R,R,R,R,R,R,Y,G,Y,Y,Y,Y,R,R,R,R,Y,R,W,W,W,W,W,W,W,
            W,W,W,R,R,R,R,R,R,R,R,R,Y,Y,Y,Y,Y,Y,Y,Y,R,R,R,R,R,W,W,W,W,W,W,W,
            W,W,R,R,R,R,R,R,R,R,R,R,Y,Y,Y,Y,Y,R,R,Y,Y,R,R,R,Y,R,W,W,W,W,W,W,
            W,W,R,R,R,R,R,Y,Y,Y,Y,Y,Y,Y,Y,Y,Y,R,R,R,Y,Y,Y,Y,Y,Y,R,R,W,W,W,W,
            W,W,R,Y,Y,Y,Y,Y,Y,Y,Y,Y,Y,Y,Y,Y,Y,R,R,R,Y,Y,Y,Y,Y,R,R,R,W,W,W,W,
            W,R,Y,Y,Y,Y,R,R,Y,Y,Y,Y,Y,Y,L,Y,Y,Y,R,R,Y,Y,Y,Y,Y,R,R,R,W,W,W,W,
            W,R,Y,R,R,R,R,R,R,Y,Y,Y,Y,Y,Y,Y,Y,Y,Y,Y,Y,Y,Y,Y,Y,Y,Y,R,R,R,W,W,
            W,Y,Y,R,R,R,R,R,R,R,Y,Y,Y,Y,Y,Y,Y,Y,Y,Y,Y,R,R,Y,R,R,Y,Y,R,R,W,W,
            W,Y,Y,R,R,R,R,R,R,R,Y,Y,Y,Y,Y,Y,Y,Y,Y,Y,R,R,R,R,R,R,Y,Y,R,R,W,W,
            W,Y,R,R,R,R,Y,Y,R,R,R,R,Y,Y,Y,Y,Y,Y,Y,Y,R,R,R,R,R,R,R,Y,Y,R,W,W,
            W,Y,R,R,R,R,R,Y,R,R,R,R,Y,Y,Y,Y,Y,Y,R,R,Y,Y,Y,R,R,R,R,Y,Y,R,W,W,
            W,Y,R,R,R,R,R,Y,Y,R,R,R,Y,Y,Y,Y,Y,R,R,R,R,Y,R,R,R,R,R,Y,Y,R,W,W,
            W,Y,R,R,R,R,R,R,Y,Y,R,R,Y,Y,Y,R,R,R,R,R,R,R,R,R,R,R,Y,Y,Y,R,W,W,
            W,Y,Y,R,R,R,R,R,R,R,R,Y,Y,R,R,R,R,R,R,R,R,R,R,R,R,R,Y,Y,Y,Y,W,W,
            W,R,Y,R,R,R,R,Y,R,R,R,R,R,R,R,R,R,R,Y,R,R,R,R,R,R,Y,Y,Y,R,W,W,W,
            W,R,Y,Y,R,R,Y,Y,Y,R,R,R,R,R,R,Y,Y,Y,Y,R,R,R,R,R,Y,Y,Y,R,R,W,W,W,
            W,R,R,Y,Y,Y,Y,Y,Y,R,R,Y,R,R,Y,R,R,R,R,Y,R,R,R,Y,Y,Y,R,R,R,W,W,W,
            W,R,R,Y,Y,Y,Y,Y,Y,Y,Y,Y,Y,Y,Y,R,R,R,R,R,R,Y,Y,Y,R,R,R,W,W,W,W,W,
            W,R,R,Y,Y,Y,Y,Y,Y,Y,Y,Y,Y,Y,Y,Y,R,R,R,R,R,Y,Y,R,R,R,R,W,W,W,W,W,
            W,W,R,R,Y,Y,Y,Y,Y,Y,Y,Y,Y,Y,Y,Y,Y,R,R,R,R,Y,Y,Y,Y,Y,Y,W,W,W,W,W,
            W,W,W,R,R,Y,Y,Y,Y,Y,Y,Y,Y,Y,Y,Y,Y,Y,Y,Y,Y,Y,Y,W,W,W,W,W,W,W,W,W,
            W,W,W,R,R,R,Y,Y,R,R,Y,Y,Y,X,Y,Y,Y,Y,W,W,W,W,D,W,W,W,W,W,W,W,W,W,
            W,W,W,W,R,R,Y,Y,R,R,R,R,Y,Y,Y,Y,Y,Y,W,W,W,W,Y,Y,G,G,Y,R,R,G,W,W,
            W,W,W,W,W,R,R,Y,R,R,R,R,R,Y,Y,Y,Y,W,W,W,W,W,Y,G,Y,G,Y,Y,Y,Y,W,W,
            W,W,W,W,W,W,R,R,Y,R,R,R,R,R,Y,Y,Y,W,W,W,W,W,Y,Y,R,R,R,Y,M,Y,W,W,
            W,W,W,W,W,W,W,W,W,R,Y,R,R,R,Y,Y,Y,W,W,W,W,W,Y,Y,Y,Y,Y,Y,Y,Y,W,W,
            W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,

    };


    /**
     * @return
     */
    @Override
    protected boolean build() {

        setSize(32, 32);

        map = codedMap.clone();

        CustomTilemap vis = new townBehind();
        vis.pos(0, 0);
        customTiles.add(vis);
        //((GameScene) RPD.scene()).addCustomTile(vis);
        CustomTilemap via = new townAbove();
        via.pos(0, 0);
        customTiles.add(via);


        buildFlagMaps();
        cleanWalls();


        entrance = 32*15 + 23;

        return true;
    }

    public static class townBehind extends CustomTilemap {

        {
            texture = Assets.Environment.LAVACAVE_OP;

            tileW = 32;
            tileH = 32;
        }

        final int TEX_WIDTH = 32*16;

        @Override
        public Tilemap create() {

            Tilemap v = super.create();

            int[] data = mapSimpleImage(0, 0, TEX_WIDTH);

            v.map(data, tileW);
            return v;
        }

    }

    public static class townAbove extends CustomTilemap {

        {
            texture = Assets.Environment.LAVACAVE_PO;

            tileW = 32;
            tileH = 32;
        }

        final int TEX_WIDTH = 32*16;

        @Override
        public Tilemap create() {

            Tilemap v = super.create();

            int[] data = mapSimpleImage(0, 0, TEX_WIDTH);

            v.map(data, tileW);
            return v;
        }

    }

    @Override
    public String tilesTex() {
        return Assets.Environment.TILES_FIRE;
    }

    @Override
    public String waterTex() {
        return Assets.Environment.WATER_HALLS;
    }

    /**
     *
     */
    @Override
    protected void createMobs() {

    }

    /**
     *
     */
    @Override
    protected void createItems() {

    }
}
