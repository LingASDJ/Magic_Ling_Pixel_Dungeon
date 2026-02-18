package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.hollow.minigame;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.PaswordBadges;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicalSight;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.status.ScoreBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.hollow.PacManQuest;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MiniGhostSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.utils.Bundle;

import java.util.List;

public class Ghost_Junko extends GhostTemplate {  // 蓝鬼
    {
        spriteClass = MiniGhostSprite.BlueHappyGhost.class;
        HT = HP = 10;
        baseSpeed = 1.0f;
    }

    @Override
    public boolean isInvulnerable(Class effect) {
        if (effect == PacManQuest.SugarBomb.class) {
            return false;
        }
        return hero.buff(PacManQuest.AntiAttack.class) == null || super.isInvulnerable(effect);
    }

    @Override
    public int attackSkill(Char target) {
        return Integer.MAX_VALUE;
    }

    @Override
    public int drRoll() {
        return 0;
    }

    private static final String LAST_ENEMY_POS = "last_enemy_pos";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(LAST_ENEMY_POS, lastEnemyPos);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        lastEnemyPos = bundle.getInt(LAST_ENEMY_POS);
    }

    private int lastEnemyPos = -1;
    // 定义视野范围和最大距离
    private static final int SIGHT_DISTANCE = 6;
    private static final int MAX_BLINKY_DISTANCE = 6;

    @Override
    public synchronized boolean isAlive() {
        return true;
    }

    @Override
    protected boolean canAttack(Char enemy) {
        if (enemy != null) {
            if (enemy.buff(PacManQuest.AntiAttack.class) != null) {
                return false;
            }
        }
        return Dungeon.level.adjacent(pos, enemy.pos);
    }

    @Override
    protected boolean getCloser(int target) {
        if (state == HUNTING) {
            if (enemy != null && enemy.buff(PacManQuest.AntiAttack.class) != null) {
                return getFurther(target);
            } else if (enemy != null && enemy.buff(PacManQuest.AntiAttack.class) == null) {
                return super.getCloser(target);
            }
        }
        return super.getCloser(target);
    }

    @Override
    protected boolean act() {
        if (state == WANDERING) {
            // 如果在游荡状态，重置一些变量
            lastEnemyPos = -1;
        }

        AiState lastState = state;
        boolean result = super.act();
        if(HP < 1){
            ScrollOfTeleportation.appear(this, 276);
            Buff.affect(this, Paralysis.class, Paralysis.DURATION);
            HP = 10;
            PacManQuest.AntiAttack buff = hero.buff(PacManQuest.AntiAttack.class);
            if(buff != null){
                int powerOfTwo = 1 << buff.Plus;
                PaswordBadges.loadGlobal();
                List<PaswordBadges.Badge> passwordbadges = PaswordBadges.filtered(true);
                if(200 * powerOfTwo > 800 && !(passwordbadges.contains(PaswordBadges.Badge.GHOST_HUNTER))){
                    PaswordBadges.GHOST_HUNTER();
                } else if(200 * powerOfTwo <= 1600) {
                    hero.sprite.showStatus(Window.Pink_COLOR, "+"+200 * powerOfTwo);
                    PacManQuest.GetScore(hero, 200 * powerOfTwo);
                    buff.Plus++;
                }
            }
        }
        if(lastState == SLEEPING || (buff(Paralysis.class)==null && hero.buff(PacManQuest.AntiAttack.class)==null &&
                (pos == 255 || pos == 256 || pos == 257 || pos == 274 || pos == 275 || pos == 276))) {
            ScrollOfTeleportation.appear(this, 221);
            Buff.affect(hero, MagicalSight.class, MagicalSight.DURATION*200);
            state = HUNTING;
        }

        // 如果状态不是从游荡变为狩猎，则更新敌人位置
        if (!(lastState == WANDERING && state == HUNTING)) {
            if (enemy != null) {
                lastEnemyPos = enemy.pos;
            } else {
                lastEnemyPos = Dungeon.hero.pos;
            }
        }

        // 如果处于狩猎状态，计算目标位置
        if (state == HUNTING) {
            int targetPos = calculateTargetPosition();
            // 向目标位置移动
            beckon(targetPos);
        }

        return result;
    }

    /**
     * 计算目标位置
     * 基于红鬼（Ghost_Anger）和玩家的位置来计算
     */
    private int calculateTargetPosition() {
        // 获取红鬼的位置
        int blinkyPos = findBlinkyPosition();

        // 检查红鬼是否在视野范围内且距离不超过6格
        if (blinkyPos != -1 && isBlinkyInSight(blinkyPos)) {
            // 获取玩家当前位置和上一位置
            int heroPos = Dungeon.hero.pos;

            // 如果有记录的敌人上一位置，计算玩家的移动方向
            if (lastEnemyPos != -1 && lastEnemyPos != heroPos) {
                int dx = (heroPos % Dungeon.level.width()) - (lastEnemyPos % Dungeon.level.width());
                int dy = (heroPos / Dungeon.level.width()) - (lastEnemyPos / Dungeon.level.width());

                // 计算玩家前方第二格的位置
                int heroTargetX = heroPos % Dungeon.level.width();
                int heroTargetY = heroPos / Dungeon.level.width();

                // 根据移动方向确定玩家前方第二格的位置
                if (dx > 0) { // 向右
                    heroTargetX += 2;
                } else if (dx < 0) { // 向左
                    heroTargetX -= 2;
                } else if (dy > 0) { // 向下
                    heroTargetY += 2;
                } else if (dy < 0) { // 向上
                    // 向上时，目标位置是左上方
                    heroTargetX -= 2;
                    heroTargetY -= 2;
                }

                // 确保位置在地图范围内
                heroTargetX = Math.max(0, Math.min(Dungeon.level.width() - 1, heroTargetX));
                heroTargetY = Math.max(0, Math.min(Dungeon.level.height() - 1, heroTargetY));

                int heroTargetPos = heroTargetY * Dungeon.level.width() + heroTargetX;

                // 计算目标位置：将红鬼与玩家目标位置连线，并延长一倍
                int blinkyX = blinkyPos % Dungeon.level.width();
                int blinkyY = blinkyPos / Dungeon.level.width();

                int targetDx = heroTargetX - blinkyX;
                int targetDy = heroTargetY - blinkyY;

                int targetX = heroTargetX + targetDx;
                int targetY = heroTargetY + targetDy;

                // 确保目标位置在地图范围内
                targetX = Math.max(0, Math.min(Dungeon.level.width() - 1, targetX));
                targetY = Math.max(0, Math.min(Dungeon.level.height() - 1, targetY));

                // 转换为一维坐标
                return targetY * Dungeon.level.width() + targetX;
            }
        }

        // 如果找不到红鬼或红鬼不在视野范围内，或者无法确定玩家的移动方向，则进行正常巡逻
        return Dungeon.level.randomDestination(this);
    }

    /**
     * 检查红鬼是否在视野范围内且距离不超过6格
     */
    private boolean isBlinkyInSight(int blinkyPos) {
        // 计算与红鬼的距离
        int distance = Dungeon.level.distance(pos, blinkyPos);

        // 如果距离超过6格，返回false
        return distance <= MAX_BLINKY_DISTANCE;

        // 检查红鬼是否在视野范围内（假设有视野检查方法）
        // 这里简化为直接检查距离，实际游戏中可能需要更复杂的视野检查
        // 例如检查是否有障碍物阻挡视野
    }

    @Override
    public int attackProc(Char enemy, int damage) {
        if(enemy == hero) {
            if (hero.buff(ScoreBuff.class) != null) {
                ScoreBuff buff = hero.buff(ScoreBuff.class);
                buff.downScore(200);
                hero.sprite.showStatus(Window.R_COLOR, "-" + 200);
                ScrollOfTeleportation.appear(this, 276);
                ScrollOfTeleportation.appear(hero, 389);
                Buff.affect(hero, Invisibility.class, 3f);
            }
        } else {
            enemy.damage(10000,this,DamageType.MAGIC);
        }
        return super.attackProc(enemy, damage);
    }

    /**
     * 查找红鬼（Ghost_Anger）的位置
     */
    private int findBlinkyPosition() {
        for (Mob mob : Dungeon.level.mobs) {
            if (mob instanceof Ghost_Anger) {
                return mob.pos;
            }
        }
        return -1; // 找不到红鬼
    }
}
