package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.hollow.minigame;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MiniGhostSprite;

public class Ghost_Smart extends Mob {
    {
        spriteClass = MiniGhostSprite.OrangeGhostHaste.class;
        HT = HP = 10;
    }

    // 分散模式下的目标位置（左下角）
    private static final int SCATTER_TARGET = 1; // 假设位置1是地图的左下角
    // 追逐模式的距离阈值
    private static final int CHASE_DISTANCE = 8;

    @Override
    public int attackSkill(Char target) {
        return Integer.MAX_VALUE;
    }

    @Override
    public int drRoll() {
        return 0;
    }

    @Override
    protected boolean act() {
        AiState lastState = state;
        if (lastState == SLEEPING) {
            ScrollOfTeleportation.appear(this, 218);
            state = HUNTING;
        }

        // 计算与玩家的距离
        int heroPos = Dungeon.hero.pos;
        int distance = Dungeon.level.distance(pos, heroPos);

        // 确定目标位置
        int targetPos;
        if (distance <= CHASE_DISTANCE) {
            // 检查8格范围内是否有其他Mob
            boolean hasOtherMob = false;
            for (Mob mob : Dungeon.level.mobs) {
                if (mob != this && Dungeon.level.distance(pos, mob.pos) <= CHASE_DISTANCE) {
                    hasOtherMob = true;
                    break;
                }
            }

            if (hasOtherMob) {
                // 如果有其他Mob，则目标设置为玩家位置
                targetPos = heroPos;
            } else {
                // 否则，使用分散模式的目标位置
                targetPos = SCATTER_TARGET;
            }
        } else {
            // 如果距离大于8格，目标设置为玩家位置
            targetPos = heroPos;
        }

        // 向目标位置移动
        beckon(targetPos);

        return super.act();
    }

    @Override
    protected boolean getCloser(int target) {
        if (state == HUNTING) {
            int heroPos = Dungeon.hero.pos;
            int distance = Dungeon.level.distance(pos, heroPos);

            // 检查4格范围内是否有其他Mob
            boolean hasOtherMob = false;
            for (Mob mob : Dungeon.level.mobs) {
                if (mob != this && Dungeon.level.distance(pos, mob.pos) <= 4) {
                    hasOtherMob = true;
                    break;
                }
            }

            if (enemy != null && distance <= CHASE_DISTANCE && !hasOtherMob) {
                // 如果在8格范围内且没有其他Mob，则远离目标
                return getFurther(target);
            } else if (enemy != null) {
                // 否则，接近目标
                return super.getCloser(target);
            }
            return super.getCloser(target);
        } else {
            return super.getCloser(target);
        }
    }
}



