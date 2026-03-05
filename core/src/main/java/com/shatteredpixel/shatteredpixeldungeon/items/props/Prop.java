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

    public int rareness = 0;
    public int kind = 0; // 0积极 1消极 2混沌

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

    // 抽积极+混沌藏品（A类）
    public static Prop randomPropA(boolean noChaotic) {
        return randomPropA(0, noChaotic);
    }

    public static Prop randomPropA(int rare, boolean noChaotic) {
        // 1. 限制稀有度范围（0-2）
        rare = Math.max(0, Math.min(2, rare));

        // 2. 全局判定：该稀有度积极藏品已拿完 → 降级/返回垃圾
        if (Statistics.hasAllRarenessProp(rare, 0)) {
            return rare != 0 ? randomPropA(rare - 1, noChaotic) : new Trash();
        }

        Prop prop = null;
        int positiveSize = 0;
        int chaoticSize = 0;

        // 3. 根据稀有度获取对应池子长度（排除混沌池如果noChaotic=true）
        switch (rare) {
            case 1:
                positiveSize = Statistics.propPositive1.size();
                chaoticSize = noChaotic ? 0 : Statistics.propChaotic1.size();
                break;
            case 2:
                positiveSize = Statistics.propPositive2.size();
                chaoticSize = noChaotic ? 0 : Statistics.propChaotic2.size();
                break;
            case 0:
            default:
                positiveSize = Statistics.propPositive0.size();
                chaoticSize = noChaotic ? 0 : Statistics.propChaotic0.size();
                break;
        }

        // 4. 兜底：总长度为0 → 返回垃圾（避免越界）
        int totalSize = positiveSize + chaoticSize;
        if (totalSize == 0) {
            return rare != 0 ? randomPropA(rare - 1, noChaotic) : new Trash();
        }

        // 5. 修正随机数范围：[0, totalSize) 左闭右开，不会越界
        int index = Random.Int(totalSize);

        // 6. 根据索引选择藏品（先积极池，后混沌池）
        switch (rare) {
            case 1:
                if (index < positiveSize) {
                    prop = Statistics.propPositive1.get(index);
                    Statistics.propPositive1.remove(index);
                } else {
                    index -= positiveSize;
                    prop = Statistics.propChaotic1.get(index);
                    Statistics.propChaotic1.remove(index);
                }
                break;
            case 2:
                if (index < positiveSize) {
                    prop = Statistics.propPositive2.get(index);
                    Statistics.propPositive2.remove(index);
                } else {
                    index -= positiveSize;
                    prop = Statistics.propChaotic2.get(index);
                    Statistics.propChaotic2.remove(index);
                }
                break;
            case 0:
            default:
                if (index < positiveSize) {
                    prop = Statistics.propPositive0.get(index);
                    Statistics.propPositive0.remove(index);
                } else {
                    index -= positiveSize;
                    prop = Statistics.propChaotic0.get(index);
                    Statistics.propChaotic0.remove(index);
                }
                break;
        }

        // 7. 兜底：如果prop仍为null（极端情况）→ 降级/返回垃圾
        return prop != null ? prop : (rare != 0 ? randomPropA(rare - 1, noChaotic) : new Trash());
    }

    // 抽消极+混沌藏品（B类）
    public static Prop randomPropB(boolean noChaotic) {
        return randomPropB(0, noChaotic);
    }

    public static Prop randomPropB(int rare, boolean noChaotic) {
        // 1. 限制稀有度范围（0-2）
        rare = Math.max(0, Math.min(2, rare));

        // 2. 全局判定：该稀有度消极藏品已拿完 → 降级/返回垃圾
        if (Statistics.hasAllRarenessProp(rare, 1)) {
            return rare != 0 ? randomPropB(rare - 1, noChaotic) : new Trash();
        }

        Prop prop = null;
        int negativeSize = 0;
        int chaoticSize = 0;

        // 3. 根据稀有度获取对应池子长度（排除混沌池如果noChaotic=true）
        switch (rare) {
            case 1:
                negativeSize = Statistics.propNegative1.size();
                chaoticSize = noChaotic ? 0 : Statistics.propChaotic1.size();
                break;
            case 2:
                negativeSize = Statistics.propNegative2.size();
                chaoticSize = noChaotic ? 0 : Statistics.propChaotic2.size();
                break;
            case 0:
            default:
                negativeSize = Statistics.propNegative0.size();
                chaoticSize = noChaotic ? 0 : Statistics.propChaotic0.size();
                break;
        }

        // 4. 兜底：总长度为0 → 返回垃圾（避免越界）
        int totalSize = negativeSize + chaoticSize;
        if (totalSize == 0) {
            return rare != 0 ? randomPropB(rare - 1, noChaotic) : new Trash();
        }

        // 5. 修正随机数范围：[0, totalSize) 左闭右开，不会越界
        int index = Random.Int(totalSize);

        // 6. 根据索引选择藏品（先消极池，后混沌池）
        switch (rare) {
            case 1:
                if (index < negativeSize) {
                    prop = Statistics.propNegative1.get(index);
                    Statistics.propNegative1.remove(index);
                } else {
                    index -= negativeSize;
                    prop = Statistics.propChaotic1.get(index);
                    Statistics.propChaotic1.remove(index);
                }
                break;
            case 2:
                if (index < negativeSize) {
                    prop = Statistics.propNegative2.get(index); // 原报错行171修复
                    Statistics.propNegative2.remove(index);
                } else {
                    index -= negativeSize;
                    prop = Statistics.propChaotic2.get(index);
                    Statistics.propChaotic2.remove(index);
                }
                break;
            case 0:
            default:
                if (index < negativeSize) {
                    prop = Statistics.propNegative0.get(index);
                    Statistics.propNegative0.remove(index);
                } else {
                    index -= negativeSize;
                    prop = Statistics.propChaotic0.get(index);
                    Statistics.propChaotic0.remove(index);
                }
                break;
        }

        // 7. TerrorDoll 特殊处理（保留原有逻辑）
        if (prop instanceof TerrorDoll && Random.Float() > 0.75f) {
            prop = new TerrorDollB();
        }

        // 8. 兜底：如果prop仍为null → 降级/返回垃圾
        return prop != null ? prop : (rare != 0 ? randomPropB(rare - 1, noChaotic) : new Trash());
    }
}