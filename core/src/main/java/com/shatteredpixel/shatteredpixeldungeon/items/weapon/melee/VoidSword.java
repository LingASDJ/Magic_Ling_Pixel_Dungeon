package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Roots;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NPC;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NTNPC;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.extra.KusumiMagicGirl;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Chains;
import com.shatteredpixel.shatteredpixeldungeon.effects.Effects;
import com.shatteredpixel.shatteredpixeldungeon.effects.Pushing;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.levels.MiningLevel;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SingularitySprite;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Game;
import com.watabou.noosa.Group;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.BArray;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.LinkedList;

import static com.shatteredpixel.shatteredpixeldungeon.Statistics.duration;
import static com.shatteredpixel.shatteredpixeldungeon.items.Item.updateQuickslot;

//虚空剑
//四阶，力量需求15
//初始6-30，成长2-5
//在进行攻击后，立刻将视野中的一名随机敌人吸引至身前，这种力量甚至强大到能够穿透墙壁。
//这把剑身中传来强大的吸引力，仿佛你正置身死寂的黑洞之中。
//武技：奇点坍缩，消耗全部充能（至少3点），在指定位置放置1个奇点，奇点会持续定身5*5范围内的敌人，每多消耗3充能使生效范围扩大一圈，并且每回合会使范围内的所有敌人和物品向奇点方向强制位移一格。
//奇点每回合都会摧毁与他相邻或重叠的物品，接触奇点的非boss单位会直接死亡并且不提供经验，boss单位会受到(30+10lvl)的真实伤害随后减少奇点30回合持续时间。释放时消耗的每点充能使奇点存在最大时间+6回合。
public class VoidSword extends MeleeWeapon {
    {
        image = ItemSpriteSheet.VOID_SWORD;

        hitSound = Assets.Sounds.HIT_CRUSH;
        hitSoundPitch = 1f;

        tier = 4;
    }

    @Override
    public int STRReq(int lvl) {
        int req = STRReq(tier, lvl) - 1;
        if (masteryPotionBonus) {
            req -= 2;
        }
        return req;
    }

    //最大伤害
    @Override
    public int max(int lvl) { return 30 + lvl * 5; }

    //最小伤害
    @Override
    public int min(int lvl) { return 6 + lvl * 2; }


    // ========== 武器特效实现 ==========
    // 增加合法吸引目标判定，以解决吸引不稳定的问题
    @Override
    public int proc(Char attacker, Char defender, int damage) {
        PullTarget target = choosePullTarget(attacker, defender);
        if (target != null) {
            chainEnemy(attacker, defender, target);
        }
        return super.proc(attacker, defender, damage);
    }

    // 创建一个数据类，用于存储吸引目标与与落点
    private static class PullTarget {
        final Char enemy;
        final int pullPos;

        PullTarget(Char enemy, int pullPos) {
            this.enemy = enemy;
            this.pullPos = pullPos;
        }
    }

    // 抄的SniperSupport的视野内随机索敌
    private PullTarget choosePullTarget(Char attacker, Char defender) {
        if (attacker.fieldOfView == null) {return null;
    }
        ArrayList<PullTarget> targets = new ArrayList<>();
        for (Char ch : Actor.chars()) {
            if (ch != attacker
                    && ch != defender
                    && ch.pos >= 0                             // 目标位置存在
                    && ch.pos < attacker.fieldOfView.length
                    && attacker.fieldOfView[ch.pos]  // 目标得在攻击者视野内
                    && ch.alignment != attacker.alignment  // 目标得和使用者不是一个阵营
                    && ch.alignment != Char.Alignment.NEUTRAL  // 不攻击中立阵营以规避宝箱怪
                    && !(ch instanceof NPC || ch instanceof KusumiMagicGirl)  // 不攻击NPC和久住
                    && ch.isAlive()  // 目标得是活的
                    && !ch.isInvulnerable(getClass())) {

                // 目标需进行合法判定
                PullTarget target = buildPullTarget(attacker, ch);
                if (target != null) {
                    targets.add(target);
                }
            }
        }
        if (targets.isEmpty()) {
            return null;
        }
        return Random.element(targets);
    }

    // 抄的EtherealChains的拉人效果
    // 检查可达性和移动落点
    private PullTarget buildPullTarget(Char attacker, Char enemy) {

        if (enemy == attacker || enemy.properties().contains(Char.Property.IMMOVABLE)) {
            return null;
        }

        PathFinder.buildDistanceMap(
                enemy.pos,
                BArray.or(
                        Dungeon.level.passable,
                        Dungeon.level.avoid,
                        null
                )
        );

        if (!(Dungeon.level instanceof MiningLevel)
                && PathFinder.distance[attacker.pos] == Integer.MAX_VALUE) {
            return null;
        }

        Ballistica chain = new Ballistica(
                attacker.pos,
                enemy.pos,
                Ballistica.STOP_TARGET
        );

        for (int i : chain.subPath(1, chain.dist)) {
            if (i == enemy.pos) {
                continue;
            }

            if (!Dungeon.level.solid[i]
                    && Actor.findChar(i) == null
                    && (!Char.hasProp(enemy, Char.Property.LARGE) || Dungeon.level.openSpace[i])) {
                return new VoidSword.PullTarget(enemy, i);
            }
        }
        return null;
    }
    // 执行已经确认合法的拉拽
    private void chainEnemy(
            final Char attacker,
            final Char defender,
            PullTarget target
    ) {
        final Char enemy = target.enemy;
        final int pulledPos = target.pullPos;

        if (!enemy.isAlive()
                || Actor.findChar(pulledPos) != null
                || attacker.sprite == null
                || attacker.sprite.parent == null
                || enemy.sprite == null) {
            return;
        }

        if (attacker instanceof Hero) {
            ((Hero) attacker).busy();
        }

        throwSound();

        Sample.INSTANCE.play(Assets.Sounds.CHAINS);
        attacker.sprite.parent.add(new Chains(
                attacker.sprite.center(),
                enemy.sprite.center(),
                Effects.Type.CHAIN,
                new Callback() {
                    @Override
                    public void call() {
                        // 锁链动画结束后再次检查
                        if (!enemy.isAlive()
                                || Actor.findChar(pulledPos) != null
                                || enemy.sprite == null) {
                            // 如果当前目标死亡，则尝试换一个目标
                            PullTarget retry = choosePullTarget(attacker, defender);

                            if (retry != null) {
                                chainEnemy(attacker, defender, retry);
                            } else {
                                attacker.next();
                            }
                            return;
                        }

                        Actor.add(new Pushing(enemy, enemy.pos, pulledPos, new Callback() {
                            @Override
                            public void call() {
                                // 动画结束后，再次确认敌人仍然存活
                                if (!enemy.isAlive()) {
                                    // 目标在移动期间死亡，则再次尝试选择其他目标
                                    PullTarget retry =
                                            choosePullTarget(attacker, defender);

                                    if (retry != null) {
                                        chainEnemy(attacker, defender, retry);
                                    }

                                    return;
                                }
                                enemy.pos = pulledPos;

                                //最终处理
                                Invisibility.dispel(attacker);
                                updateQuickslot();

                                Dungeon.level.occupyCell(enemy);
                                Dungeon.observe();
                                GameScene.updateFog();
                            }
                        }));
                        attacker.next();
                    }
                }));
    }

    // ========== 武技实现 ==========
    // ========== 武技：奇点坍缩 ==========
    @Override
    public String targetingPrompt() {
        return Messages.get(this, "prompt");
    }

    // 消耗全部充能（至少3点）
    @Override
    protected int baseChargeUse(Hero hero, Char target) {
        return 3;
    }
    @Override
    public void beforeAbilityUsed(Hero hero, Char target) {
        // 先执行父类扣除至少3点充能的逻辑
        super.beforeAbilityUsed(hero, target);

        Charger charger = Buff.affect(hero, Charger.class);

        if (hero.belongings.weapon == this) {
            // 主手武器：清空全部剩余充能和零头
            charger.charges = 0;
            charger.partialCharge = 0f;
        } else {
            // 副手武器：清空副手全部剩余充能和零头
            charger.secondCharges = 0;
            charger.secondPartialCharge = 0f;
        }

        updateQuickslot();
    }

    @Override
    protected void duelistAbility(Hero hero, Integer target) {
        if (target == null) return;
        if (!Dungeon.level.insideMap(target) || Dungeon.level.solid[target]) {
            GLog.w(Messages.get(this, "ability_no_target"));
            return;
        }

        Charger charger = Buff.affect(hero, Charger.class);

        float available;
        if (hero.belongings.weapon == this) {
            available = charger.charges + charger.partialCharge;
        } else {
            available = charger.secondCharges + charger.secondPartialCharge;
        }

        if (available < 3f) {
            GLog.w(Messages.get(this, "ability_no_charge"));
            return;
        }

        // 实际消耗的全部充能
        int spentCharge = Math.max(3, (int)Math.floor(available));

        // 5*5 基础半径是2，每额外3点充能扩大一圈
        int radius = 2 + (spentCharge - 3) / 3;

        // 每点充能提供6回合，没有基础43回合
        int duration = spentCharge * 6;

        // 30 + 10 × 武器等级的真实伤害
        int bossDamage = 30 + 10 * buffedLvl();

        // 这里会清空全部充能
        beforeAbilityUsed(hero, null);

        Singularity dark = new Singularity(radius,bossDamage,duration);
        dark.pos = target;
        GameScene.add(dark);
        Buff.affect(hero,Point.class,43f);

        // 放置特效
        CellEmitter.get(target).burst(Speck.factory(Speck.STAR), 10);

        hero.spendAndNext(hero.attackDelay());
        afterAbilityUsed(hero);
    }

    // ========== 奇点实体Buff  ==========
    public static class Point extends FlavourBuff{
        @Override
        public void detach() {
            super.detach();
            for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])){
                if (mob instanceof Singularity) {
                    mob.destroy();
                    mob.sprite.die();
                }
            }
        }

        @Override
        public int icon() {
            return BuffIndicator.VERTIGO;
        }

        @Override
        public void tintIcon(Image icon) {
            icon.hardlight(0xB29FBB);
        }

    };

    public static class Singularity extends NTNPC {
        private static final String RADIUS = "radius";
        private static final String BOSS_DAMAGE = "boss_damage";
        private static final String LIFE = "life";

        private int radius = 2;
        private int bossDamage = 30;
        private int life = 43;

        {
            spriteClass = SingularitySprite.class;
            properties.add(Property.UNKNOWN);
        }

        public Singularity() {
        }

        public Singularity(
                int radius,
                int bossDamage,
                int life
        ) {
            this.radius = radius;
            this.bossDamage = bossDamage;
            this.life = life;
        }

        @Override
        public boolean act() {
            boolean removed = false;

            int w = Dungeon.level.width();
            int h = Dungeon.level.height();
            int cx = pos % w;
            int cy = pos / w;

            // 1. 范围内：持续定身 + 向奇点强制位移一格
            for (int dy = -radius; dy <= radius; dy++) {
                for (int dx = -radius; dx <= radius; dx++) {
                    int nx = cx + dx;
                    int ny = cy + dy;
                    if (nx < 0 || ny < 0 || nx >= w || ny >= h) continue;
                    int cell = ny * w + nx;

                    Char ch = Actor.findChar(cell);
                    if (ch != null && ch.isAlive()
                            && ch != Dungeon.hero
                            && ch.alignment != Char.Alignment.ALLY
                            && ch.alignment != Char.Alignment.NEUTRAL) {
                        Buff.prolong(ch, Roots.class, 2f);
                        if (cell != pos) {
                            removed |=pullChar(ch, cell);
                        } else {
                            removed |=onContact(ch);
                        }
                    }
                    // 物品向奇点位移一格
                    pullHeap(cell);
                }
            }

            // 2. 摧毁与奇点相邻或重叠的物品
            destroyHeap(pos);
            for (int offset : PathFinder.NEIGHBOURS8) {
                int cell = pos + offset;
                if (Dungeon.level.insideMap(cell)) destroyHeap(cell);
            }

            // 3. 接触奇点（站在奇点格）的单位判定
            if (!removed) {
                Char c = Actor.findChar(pos);
                if (c != null && c.isAlive()) {
                    removed = onContact(c);
                }
            }

            if (removed) {
                destroy();
                sprite.die();
                return true;
            }

            life--;

            if (life <= 0) {
                destroy();
                sprite.die();
                return true;
            }

            spend(TICK);
            return true;
        }

        // 接触奇点：非Boss直接死亡且不给予经验；Boss受到固定真实伤害并减少30回合持续时间
        private boolean onContact(Char ch) {
            if (ch.properties().contains(Char.Property.BOSS)) {
                ch.damage( bossDamage, this, DamageType.REAL);
                life -= 30;
                return life <= 0;

            } else {
                if (ch instanceof Mob) {
                    ((Mob) ch).EXP = 0;
                    ((Mob) ch).maxLvl = -1;
                }

                ch.die(this);
                return false;
            }
        }

        // 敌人向奇点方向位移一格；若一步被吸入奇点则当场判定，返回是否摧毁奇点
        private boolean pullChar(Char ch, int cell) {
            if (ch.properties().contains(Char.Property.IMMOVABLE)) return false;
            int to = stepToward(cell);
            if (to < 0) return false;

            if (to == pos) {
                return onContact(ch); // 直接被吸入奇点
            }
            if (Dungeon.level.solid[to] || Actor.findChar(to) != null) return false;

            final int from = cell;
            final int dest = to;
            Actor.add(new Pushing(ch, from, dest, new Callback() {
                @Override
                public void call() {
                    ch.pos = dest;
                    Dungeon.level.occupyCell(ch);
                }
            }));
            BlackPoint.blast(pos,5);
            return false;
        }

        // 物品堆向奇点方向位移一格
        private void pullHeap(int cell) {
            Heap heap = Dungeon.level.heaps.get(cell);
            if (heap == null || heap.items == null || heap.items.isEmpty()) return;
            int to = stepToward(cell);
            if (to < 0 || Dungeon.level.solid[to] || Actor.findChar(to) != null) return;

            if (heap.sprite != null) heap.sprite.kill();
            Dungeon.level.heaps.remove(cell);

            LinkedList<Item> items = heap.items; // Heap.items 是 LinkedList
            if (!items.isEmpty()) {
                Item first = items.remove(0);
                Heap newHeap = Dungeon.level.drop(first, to);
                if (!items.isEmpty()){
                    newHeap.items.addAll(items);
                }
            }
            BlackPoint.blast(pos,5);
        }

        // 计算 cell 向奇点走一步的落点，不可达返回-1
        private int stepToward(int cell) {
            int w = Dungeon.level.width();
            int h = Dungeon.level.height();
            int fx = cell % w;
            int fy = cell / w;
            int cx = pos % w;
            int cy = pos / w;
            int dx = Integer.compare(cx, fx);
            int dy = Integer.compare(cy, fy);
            int nx = fx + dx;
            int ny = fy + dy;
            if (nx < 0 || ny < 0 || nx >= w || ny >= h) return -1;
            if (nx == fx && ny == fy) return -1;
            return ny * w + nx;
        }

        // 摧毁指定格的物品堆
        private void destroyHeap(int cell) {
            Heap heap = Dungeon.level.heaps.get(cell);
            if (heap == null || heap.items == null || heap.items.isEmpty()) return;

            for (Item item : heap.items) {
                if(item.unique){
                    return;
                }
            }

            heap.destroy();
        }

        @Override
        public void die(Object cause) {

        }

        @Override
        public void storeInBundle(Bundle bundle) {
            super.storeInBundle(bundle);
            bundle.put(POS, pos);
            bundle.put(RADIUS, radius);
            bundle.put(BOSS_DAMAGE, bossDamage);
            bundle.put(LIFE, life);
        }

        @Override
        public void restoreFromBundle(Bundle bundle) {
            super.restoreFromBundle(bundle);
            pos = bundle.getInt(POS);
            radius = bundle.getInt(RADIUS);
            bossDamage = bundle.getInt(BOSS_DAMAGE);
            life = bundle.getInt(LIFE);
        }
    }

    public static class BlackPoint extends Image {

        private static final float TIME_TO_FADE = 0.9f;

        private float time;
        private float size;

        public BlackPoint(){
            super(Effects.get(Effects.Type.RIPPLE));
            origin.set(width / 2, height / 2);
        }

        public void reset(int pos, float size) {
            revive();

            x = (pos % Dungeon.level.width()) * DungeonTilemap.SIZE + (DungeonTilemap.SIZE - width) / 2;
            y = (pos / Dungeon.level.width()) * DungeonTilemap.SIZE + (DungeonTilemap.SIZE - height) / 2;

            time = TIME_TO_FADE;
            this.size = size;
            alpha(0f); // 初始透明，随后淡入
        }

        @Override
        public void update() {
            super.update();

            if ((time -= Game.elapsed) <= 0) {
                kill();
            } else {
                float p = time / TIME_TO_FADE; // 1 → 0

                // 黑洞：从外圈向中心收缩（大圈 → 中心点）
                scale.y = scale.x = p * size;

                // 吸入感：前30%时间淡入，之后淡出至消失
                float a;
                if (p > 0.7f) {
                    a = (1f - p) / 0.3f; // 淡入阶段
                } else {
                    a = p / 0.7f;        // 淡出阶段
                }
                alpha(a);
            }
        }

        public static void blast(int pos) {
            blast(pos, 3);
        }

        public static void blast(int pos, float radius) {
            Group parent = Dungeon.hero.sprite.parent;
            BlackPoint b = (BlackPoint) parent.recycle(BlackPoint.class);
            parent.bringToFront(b);
            b.reset(pos, radius);
        }
    }
    //武技数值文本
        @Override
        public String abilityInfo() {
            float available = 0;

            if (Dungeon.hero != null) {
                Charger charger = Buff.affect(
                        Dungeon.hero,
                        Charger.class
                );

                if (Dungeon.hero.belongings.weapon == this) {
                    available = charger.charges + charger.partialCharge;
                } else {
                    available = charger.secondCharges + charger.secondPartialCharge;
                }
            }
            int spentCharge = Math.max(3, (int)Math.floor(available));

            // 计算范围字符串
            int radius = 2 + (spentCharge - 3) / 3;
            int size = radius * 2 + 1;
            String range = size + "*" + size;

            // 计算 Boss 真实伤害
            int bossDamage = 30 + 10 * buffedLvl();

            // 每点充能持续6回合
            int duration = spentCharge * 6;

            if (levelKnown) {
                return Messages.get(
                        this,
                        "ability_desc",
                        range,
                        bossDamage,
                        duration
                );
            } else {
                return Messages.get(
                        this,
                        "typical_ability_desc",
                        "5*5",
                        30,
                        18
                );
            }
        }

}