package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.CheckedCell;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfMagicMapping;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.ShadowCaster;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Point;

//指南剑
//四阶，力量需求16
//初始4-20，成长1-4
//执着的眼穿过当下，朦胧的未来被刹那照亮
//武技：指南
//立刻揭示以自身为中心半径15的圆形（相当于明示符石）。消耗5点充能。
//暂不清楚这把剑的运作原理……
public class SouthPointSword extends MeleeWeapon{
    {
        image = ItemSpriteSheet.COMPASS;

        hitSound = Assets.Sounds.HIT_CRUSH;
        hitSoundPitch = 1f;

        tier = 4;
    }
    @Override
    public int max(int lvl) { return 20 + lvl * 4; }

    @Override
    public int min(int lvl) { return 4 + lvl; }

    // ==================== 武技：指南 ====================

    // 每次使用武技消耗的充能点数（由决斗者的 Charger buff 提供）
    @Override
    protected int baseChargeUse(Hero hero, Char target){
        return 5;
    }

    @Override
    protected void duelistAbility(Hero hero, Integer target) {
        // 1. 先扣充能：beforeAbilityUsed 会按 baseChargeUse 的返回值扣掉对应充能
        beforeAbilityUsed(hero, null);

        // 2. 以英雄自己为中心，揭示半径 15 的圆形区域（相当于明示符石）
        revealArea(hero.pos, 15);

        // 3. 播放使用动作，并消耗一个回合
        hero.sprite.operate(hero.pos);
        hero.next();

        // 4. 武技收尾：处理与武技相关的天赋联动
        afterAbilityUsed(hero);
    }

    // 揭示以 center 为圆心、radius 为半径的圆形区域。
    // 逻辑与明示符石（StoneOfClairvoyance）一致：
    //   - 圆形内所有格子标记为已探索（mapped = true），地图上永久显示；
    //   - 隐藏门/隐藏陷阱就地揭示（Dungeon.level.discover）；
    //   - 英雄视野内的格子额外播放贴图变化动画和“发现”特效。
    private void revealArea(int center, int radius){
        // 一维格子索引 -> (x, y) 坐标
        Point c = Dungeon.level.cellToPoint(center);

        // ShadowCaster.rounding[i] 是“半径 i 的圆”的行宽表：
        // rounding[i][j] = 第 j 行（离圆心 j 格）在 x 方向上的半宽，
        // 用来把方形遍历削成圆形，避免把四个角也标记出来。
        int[] rounding = ShadowCaster.rounding[radius];

        int left, right;
        int curr;
        boolean noticed = false;

        // 逐行扫描：从圆心上方的 radius 行到下方的 radius 行，并夹在地图边界内
        GLog.b(Messages.get(this, "5g"));
        for (int y = Math.max(0, c.y - radius);
             y <= Math.min(Dungeon.level.height() - 1, c.y + radius);
             y++) {

            // 根据本行与圆心的距离，算出 x 方向的半宽，从而得到左右边界
            if (rounding[Math.abs(c.y - y)] < Math.abs(c.y - y)) {
                // 常规行：直接按半宽向左右扩展
                left = c.x - rounding[Math.abs(c.y - y)];
            } else {
                // 圆心所在的行附近：半宽 >= 距中心距离，
                // 需要从最外层往内找到第一个满足条件的值，保证左右对称
                left = radius;
                while (rounding[left] < rounding[Math.abs(c.y - y)]) {
                    left--;
                }
                left = c.x - left;
            }
            // 右边界与左边界关于圆心 x 对称，并夹在地图边界内
            right = Math.min(Dungeon.level.width() - 1, c.x + c.x - left);
            left = Math.max(0, left);

            // 逐格处理本行
            for (curr = left + y * Dungeon.level.width();
                 curr <= right + y * Dungeon.level.width();
                 curr++) {

                // 在迷雾上方叠加“探查”波纹动画（从圆心向外扩散）
                GameScene.effectOverFog(new CheckedCell(curr, center));

                // 标记为已探索：mapped 决定地图上是否会永久显示该格
                Dungeon.level.mapped[curr] = true;

                // 只有隐藏地形（隐藏门/隐藏陷阱）才需要真正“揭示”
                if (Dungeon.level.secret[curr]) {
                    // 记录揭示前的地形：GameScene.discoverTile 需要旧值做贴图动画
                    int oldValue = Dungeon.level.map[curr];

                    // 核心：隐藏门 -> 普通门，隐藏陷阱 -> 显形陷阱，并刷新该格显示
                    Dungeon.level.discover(curr);

                    // 只在英雄视野内的格子播放动画/特效，避免隔墙暴露视野外的信息
                    if (Dungeon.level.heroFOV[curr]) {
                        GameScene.discoverTile(curr, oldValue);
                        ScrollOfMagicMapping.discover(curr);
                        noticed = true;
                    }
                }
            }
        }

        // 发现了秘密（门/陷阱）就播放提示音
        if (noticed) {
            Sample.INSTANCE.play(Assets.Sounds.SECRET);
        }
        // 刷新迷雾显示（mapped 数组变了）
        GameScene.updateFog();
    }
}
