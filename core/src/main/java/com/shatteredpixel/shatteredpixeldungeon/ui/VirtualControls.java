package com.shatteredpixel.shatteredpixeldungeon.ui;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;
import static com.shatteredpixel.shatteredpixeldungeon.SPDAction.E;
import static com.shatteredpixel.shatteredpixeldungeon.SPDAction.N;
import static com.shatteredpixel.shatteredpixeldungeon.SPDAction.S;
import static com.shatteredpixel.shatteredpixeldungeon.SPDAction.W;

import com.shatteredpixel.shatteredpixeldungeon.Chrome;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.SPDAction;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.status.ScoreBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.levels.hollow.MoveBoxHollowActorLevel;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Group;
import com.watabou.utils.Callback;
import com.watabou.utils.Point;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class VirtualControls extends Group {

    @SuppressWarnings("all")
    public VirtualControls() {
        super();

        float screenHeight = PixelScene.uiCamera.height;
        float centerY = screenHeight / 2;
        float bottomY = screenHeight - 20;

        float initwidth = PixelScene.uiCamera.width-52;


        float buttonAreaY = centerY + (bottomY - centerY) / 2;

        StyledButton upButton = new StyledButton(Chrome.Type.WINDOW, "") {
            @Override
            protected void onClick() {
                moveHero((SPDAction) N);
            }
        };

        upButton.setRect( initwidth , buttonAreaY - 10, 20, 20);
        upButton.icon(Icons.get(Icons.UP_DICT));
        add(upButton);

        StyledButton downButton = new StyledButton(Chrome.Type.WINDOW, "") {
            @Override
            protected void onClick() {
                moveHero((SPDAction) S);
            }
        };
        downButton.setRect(upButton.x, upButton.bottom() + 10, 20, 20);
        downButton.icon(Icons.get(Icons.DOWN_DICT));
        add(downButton);

        StyledButton leftButton = new StyledButton(Chrome.Type.WINDOW, "") {
            @Override
            protected void onClick() {
                moveHero((SPDAction) W);
            }
        };
        leftButton.setRect(upButton.left() - 25, upButton.bottom() + 10, 20, 20);
        leftButton.icon(Icons.get(Icons.RIGHT_DICT));
        add(leftButton);

        StyledButton rightButton = new StyledButton(Chrome.Type.WINDOW, "") {
            @Override
            protected void onClick() {
                moveHero((SPDAction) E);
            }
        };
        rightButton.setRect(upButton.right() + 5, upButton.bottom() + 10, 20, 20);
        rightButton.icon(Icons.get(Icons.LEFT_DICT));
        add(rightButton);

        StyledButton reloadButton = new StyledButton(Chrome.Type.WINDOW_SILVER, Messages.get(VirtualControls.class, "reload"), 6) {
            @Override
            protected void onClick() {
                ScrollOfTeleportation.appear(hero, Dungeon.level.entrance());
                hero.busy();
                MoveBoxHollowActorLevel level = (Dungeon.level instanceof MoveBoxHollowActorLevel) ? (MoveBoxHollowActorLevel) Dungeon.level : null;
                if (level != null) {
                    // 获取目标位置数组
                    int[] targetPositions = level.BoxRules();

                    // 创建一个HashSet来快速查找目标位置
                    Set<Integer> targetPosSet = new HashSet<>();
                    for (int pos : targetPositions) {
                        targetPosSet.add(pos);
                    }

                    if (hero.buff(ScoreBuff.class) != null) {
                        GLog.w(Messages.get(VirtualControls.class, "down_score"));
                        ScoreBuff buff = hero.buff(ScoreBuff.class);
                        buff.downScore(100);
                        hero.sprite.showStatus(Window.R_COLOR, "-" + 100);
                    }

                    // 用于记录已经被占用的目标位置
                    Set<Integer> occupiedPositions = new HashSet<>();

                    // 第一次遍历：检查并标记已经在正确位置的箱子
                    for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
                        if (mob instanceof MoveBoxHollowActorLevel.Box) {
                            int currentPos = mob.pos;
                            if (targetPosSet.contains(currentPos)) {
                                occupiedPositions.add(currentPos);
                            }
                        }
                    }

                    // 创建列表存储所有需要移动的箱子和它们的目标位置
                    List<BoxMove> movesToMake = new ArrayList<>();

                    // 第二次遍历：收集所有需要移动的箱子
                    for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
                        if (mob instanceof MoveBoxHollowActorLevel.Box) {
                            int currentPos = mob.pos;
                            if (!targetPosSet.contains(currentPos)) {
                                int nearestTarget = findNearestUnoccupiedPosition(currentPos, targetPositions, occupiedPositions);
                                if (nearestTarget != -1) {
                                    movesToMake.add(new BoxMove((MoveBoxHollowActorLevel.Box) mob, currentPos, nearestTarget));
                                    occupiedPositions.add(nearestTarget);
                                }
                            }
                        }
                    }

                    // 按顺序执行移动
                    executeMovesSequentially(movesToMake, 0, () -> {
                        // 所有移动完成后，确保所有目标位置都有箱子
                        for (int targetPos : targetPositions) {
                            boolean hasBox = false;
                            for (Mob mob : Dungeon.level.mobs) {
                                if (mob instanceof MoveBoxHollowActorLevel.Box && mob.pos == targetPos) {
                                    hasBox = true;
                                    break;
                                }
                            }
                            if (!hasBox) {
                                // 创建新箱子
                                MoveBoxHollowActorLevel.Box box = new MoveBoxHollowActorLevel.Box();
                                box.pos = targetPos;
                                level.mobs.add(box);
                            }
                        }
                    });
                }
            }

            // 辅助类：存储箱子移动信息
            class BoxMove {
                final MoveBoxHollowActorLevel.Box box;
                final int from;
                final int to;

                BoxMove(MoveBoxHollowActorLevel.Box box, int from, int to) {
                    this.box = box;
                    this.from = from;
                    this.to = to;
                }
            }

            // 顺序执行移动动画
            private void executeMovesSequentially(List<BoxMove> moves, int index, Runnable onComplete) {
                if (index >= moves.size()) {
                    onComplete.run();
                    hero.spendAndNext(1);
                    hero.sprite.operate(hero.pos);
                    return;
                }

                BoxMove move = moves.get(index);
                move.box.sprite.jump(move.from, move.to, new Callback() {
                    @Override
                    public void call() {
                        ScrollOfTeleportation.appear(move.box, move.to);
                        // 移动完成后，继续下一个移动
                        executeMovesSequentially(moves, index + 1, onComplete);
                    }
                });
            }

            private int findNearestUnoccupiedPosition(int currentPos, int[] targetPositions, Set<Integer> occupiedPositions) {
                int nearestPos = -1;
                int minDistance = Integer.MAX_VALUE;

                for (int targetPos : targetPositions) {
                    if (!occupiedPositions.contains(targetPos)) {
                        int distance = calculateDistance(currentPos, targetPos);
                        if (distance < minDistance) {
                            minDistance = distance;
                            nearestPos = targetPos;
                        }
                    }
                }

                return nearestPos;
            }

            private int calculateDistance(int pos1, int pos2) {
                int levelWidth = Dungeon.level.width();
                int x1 = pos1 % levelWidth;
                int y1 = pos1 / levelWidth;
                int x2 = pos2 % levelWidth;
                int y2 = pos2 / levelWidth;

                return Math.abs(x1 - x2) + Math.abs(y1 - y2);
            }

        };
        reloadButton.setRect(0, upButton.y, 50, 20);
        reloadButton.icon(Icons.get(Icons.CHANGES));
        add(reloadButton);

//        StyledButton skip = new StyledButton(Chrome.Type.WINDOW_SILVER, Messages.get(VirtualControls.class, "skip"), 6) {
//            @Override
//            protected void onClick() {
//                MoveBoxHollowActorLevel level = (Dungeon.level instanceof MoveBoxHollowActorLevel) ? (MoveBoxHollowActorLevel) Dungeon.level : null;
//                level.SkipGame = true;
//            }
//        };
//        skip.setRect(0, reloadButton.y + reloadButton.height(), 50, 20);
//        skip.icon(Icons.get(Icons.SKIP));
//        add(skip);
    }


    private void moveHero(SPDAction action) {
        if (hero != null && hero.ready && !GameScene.interfaceBlockingHero()) {
            Point direction = directionFromAction(action);
            int cell = hero.pos + direction.x + direction.y * Dungeon.level.width();

            if (hero.handle(cell)) {
                hero.next();
            }
        }
    }

    private Point directionFromAction(SPDAction action) {
        if (action.equals(N)) {
            return new Point(0, -1);
        } else if (action.equals(E)) {
            return new Point(1, 0);
        } else if (action.equals(S)) {
            return new Point(0, 1);
        } else if (action.equals(W)) {
            return new Point(-1, 0);
        }
        return new Point();
    }
}
