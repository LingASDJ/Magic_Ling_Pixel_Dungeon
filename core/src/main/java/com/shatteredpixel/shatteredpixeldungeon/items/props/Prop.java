package com.shatteredpixel.shatteredpixeldungeon.items.props;

import com.shatteredpixel.shatteredpixeldungeon.Conducts;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Prop extends Item {

    {
        levelKnown = true;
        unique = true;
    }

    private static long seed;
    private static java.util.Random random = null;

    public int rareness = 0;
    public int kind = 0; // 0积极 1 消极 2混沌

    @Override
    public ArrayList<String> actions(Hero hero) {
        if (Dungeon.isDLC(Conducts.Conduct.DEV) ||
                (Statistics.deepestFloor > 10 &&
                        (Dungeon.isDLC(Conducts.Conduct.EASY) ||
                                Dungeon.isDLC(Conducts.Conduct.NORMAL) ||
                                Dungeon.isDLC(Conducts.Conduct.NULL)))) {
            return super.actions(hero);
        }
        return new ArrayList<>();
    }

    public String kindRules() {
        String string;
        switch (kind) {
            case 2:
                string = Messages.get(this, "chaos");
                break;
            case 1:
                string = Messages.get(this, "bad");
                break;
            default:
                string = Messages.get(this, "good");
                break;
        }
        return string;
    }

    @Override
    public String desc() {
        String c = Messages.get(this, "rareness", rareness + 1, kindRules());
        c += "\n\n" + super.desc();
        return c;
    }

    @Override
    public boolean isUpgradable() {
        return false;
    }

    @Override
    public boolean isIdentified() {
        return true;
    }

    public static void checkSeed() {
        if (random == null) {
            seed = Dungeon.seed;
            random = new java.util.Random(seed);
        }
    }

    public static Prop randomPropA() {
        return randomPropA(0);
    }

    public static Prop randomPropA(int rare) {
        checkSeed();

        // 修正：移除无效的空Prop初始化
        Prop prop;
        if (rare > 2) rare = 2;

        if (Statistics.hasAllRarenessProp(rare, 0)) {
            if (rare != 0) {
                return randomPropA(rare - 1);
            } else {
                return new Trash();
            }
        }

        switch (rare) {
            case 1:
                // 修复1：使用单参数nextInt，且先判断列表是否为空
                int total1 = Statistics.propPositive1.size() + Statistics.propChaotic1.size();
                if (total1 == 0) return new Trash(); // 空列表安全兜底
                int index1 = random.nextInt(total1);

                if (index1 > Statistics.propPositive1.size() - 1) {
                    index1 -= Statistics.propPositive1.size();
                    prop = Statistics.propChaotic1.get(index1);
                    Statistics.propChaotic1.remove(index1);
                } else {
                    prop = Statistics.propPositive1.get(index1);
                    Statistics.propPositive1.remove(index1);
                }
                break;
            case 2:
                // 修复2：移除双参数nextInt(0, xxx)，改为单参数
                int total2 = Statistics.propPositive2.size() + Statistics.propChaotic2.size();
                if (total2 == 0) return new Trash();
                int index2 = random.nextInt(total2);

                if (index2 > Statistics.propPositive2.size() - 1) {
                    index2 -= Statistics.propPositive2.size();
                    prop = Statistics.propChaotic2.get(index2);
                    Statistics.propChaotic2.remove(index2);
                } else {
                    prop = Statistics.propPositive2.get(index2);
                    Statistics.propPositive2.remove(index2);
                }
                break;
            case 0:
            default:
                // 修复3：统一单参数写法，增加空列表判断
                int total0 = Statistics.propPositive0.size() + Statistics.propChaotic0.size();
                if (total0 == 0) return new Trash();
                int index0 = random.nextInt(total0);

                if (index0 > Statistics.propPositive0.size() - 1) {
                    index0 -= Statistics.propPositive0.size();
                    prop = Statistics.propChaotic0.get(index0);
                    Statistics.propChaotic0.remove(index0);
                } else {
                    prop = Statistics.propPositive0.get(index0);
                    Statistics.propPositive0.remove(index0);
                }
                break;
        }
        return prop;
    }

    public static Prop randomPropB() {
        return randomPropB(0);
    }

    public static Prop randomPropB(int rare) {
        checkSeed();

        Prop prop;
        if (rare > 2) rare = 2;

        if (Statistics.hasAllRarenessProp(rare, 1)) {
            if (rare != 0) {
                return randomPropB(rare - 1);
            } else {
                return new Trash();
            }
        }

        switch (rare) {
            case 1:
                // 修复4：单参数nextInt + 空列表判断
                int total1 = Statistics.propNegative1.size() + Statistics.propChaotic1.size();
                if (total1 == 0) return new Trash();
                int index1 = random.nextInt(total1);

                if (index1 > Statistics.propNegative1.size() - 1) {
                    index1 -= Statistics.propNegative1.size();
                    prop = Statistics.propChaotic1.get(index1);
                    Statistics.propChaotic1.remove(index1);
                } else {
                    prop = Statistics.propNegative1.get(index1);
                    Statistics.propNegative1.remove(index1);
                }
                break;
            case 2:
                // 修复5：移除双参数nextInt
                int total2 = Statistics.propNegative2.size() + Statistics.propChaotic2.size();
                if (total2 == 0) return new Trash();
                int index2 = random.nextInt(total2);

                if (index2 > Statistics.propNegative2.size() - 1) {
                    index2 -= Statistics.propNegative2.size();
                    prop = Statistics.propChaotic2.get(index2);
                    Statistics.propChaotic2.remove(index2);
                } else {
                    prop = Statistics.propNegative2.get(index2);
                    Statistics.propNegative2.remove(index2);
                }
                break;
            case 0:
            default:
                // 修复6：统一单参数写法
                int total0 = Statistics.propNegative0.size() + Statistics.propChaotic0.size();
                if (total0 == 0) return new Trash();
                int index0 = random.nextInt(total0);

                if (index0 > Statistics.propNegative0.size() - 1) {
                    index0 -= Statistics.propNegative0.size();
                    prop = Statistics.propChaotic0.get(index0);
                    Statistics.propChaotic0.remove(index0);
                } else {
                    prop = Statistics.propNegative0.get(index0);
                    Statistics.propNegative0.remove(index0);
                }
                break;
        }

        // TerrorDoll 特殊处理逻辑保留
        if (prop instanceof TerrorDoll && Random.Float() > 0.75f) {
            prop = new TerrorDollB();
        }
        return prop;
    }
}