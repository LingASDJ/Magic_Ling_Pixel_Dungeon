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
import com.shatteredpixel.shatteredpixeldungeon.sprites.SunSprite;
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

//虚空剑
//四阶，力量需求15
//初始6-30，成长2-5
//在进行攻击后，立刻将视野内可达位置中的一名随机敌人吸引至身前。
//这把剑身中传来强大的吸引力，仿佛你正置身死寂的黑洞之中。
//武技：奇点坍缩，消耗10充能，在指定位置放置1个奇点，奇点会持续定身9*9范围内的敌人，并且每回合会使范围内的所有敌人和物品向奇点方向强制位移一格。
//奇点每回合都会摧毁与他相邻或重叠的物品，接触奇点的非boss单位会直接死亡，boss单位会受到20%最大生命值的伤害随后摧毁奇点。
public class VoidSword extends MeleeWeapon {
    {
        image = ItemSpriteSheet.VOID_SWORD;

        hitSound = Assets.Sounds.HIT_CRUSH;
        hitSoundPitch = 1f;

        tier = 4;
    }

    @Override
    public int STRReq(int lvl) {
        int req = STRReq(tier, lvl) + 1;
        if (masteryPotionBonus) {
            req -= 3;
        }
        return req;
    }

    // ========== 武器特效实现 ==========
    @Override
    public int proc(Char attacker, Char defender, int damage) {
        Char enemy = chooseRandomEnemy(attacker);
        if (enemy != null && enemy != defender) {
            Attraction(attacker, enemy);
        }
        return super.proc(attacker, defender, damage);
    }

    // 武器特效的吸引效果
    public void Attraction(Char attacker, Char enemy) {
        PathFinder.buildDistanceMap(enemy.pos, BArray.or(Dungeon.level.passable, Dungeon.level.avoid, null));
        if (!(Dungeon.level instanceof MiningLevel)
                && PathFinder.distance[curUser.pos] == Integer.MAX_VALUE) {
            return;
        }
        final Ballistica chain = new Ballistica(curUser.pos, enemy.pos, Ballistica.STOP_TARGET);
        if (Actor.findChar(chain.collisionPos) != null) {
            chainEnemy(chain, curUser, Actor.findChar(chain.collisionPos)); // 撞到人 → 拉怪
        }
    }

    // 抄的SniperSupport的视野内随机索敌
    private Char chooseRandomEnemy(Char attacker) {
        if (attacker.fieldOfView == null) return null;
        ArrayList<Char> enemies = new ArrayList<>();
        for (Char ch : Actor.chars()) {
            if (ch != attacker
                    && ch.pos >= 0                             // 目标位置存在
                    && ch.pos < attacker.fieldOfView.length
                    && attacker.fieldOfView[ch.pos]  // 目标得在攻击者视野内
                    && ch.alignment != attacker.alignment  // 目标得和使用者不是一个阵营
                    && ch.alignment != Char.Alignment.NEUTRAL  // 不攻击中立阵营以规避宝箱怪
                    && !(ch instanceof NPC || ch instanceof KusumiMagicGirl)  // 不攻击NPC和久住
                    && ch.isAlive()  // 目标得是活的
                    && !ch.isInvulnerable(getClass())) {
                enemies.add(ch);
            }
        }
        if (enemies.isEmpty()) return null;
        return Random.element(enemies);
    }

    // 抄的EtherealChains的拉人效果
    private void chainEnemy(Ballistica chain, final Hero hero, final Char enemy) {

        if (enemy.properties().contains(Char.Property.IMMOVABLE)) {
            return;
        }

        int bestPos = -1;
        for (int i : chain.subPath(1, chain.dist)) {
            if (!Dungeon.level.solid[i]
                    && Actor.findChar(i) == null
                    && (!Char.hasProp(enemy, Char.Property.LARGE) || Dungeon.level.openSpace[i])) {
                bestPos = i;
                break;
            }
        }

        if (bestPos == -1) {
            return;
        }

        final int pulledPos = bestPos;

        hero.busy();

        throwSound();

        Sample.INSTANCE.play(Assets.Sounds.CHAINS);
        hero.sprite.parent.add(new Chains(hero.sprite.center(),
                enemy.sprite.center(),
                Effects.Type.CHAIN,
                new Callback() {
                    public void call() {
                        Actor.add(new Pushing(enemy, enemy.pos, pulledPos, new Callback() {
                            public void call() {
                                enemy.pos = pulledPos;

                                Invisibility.dispel(hero);
                                Talent.onArtifactUsed(hero);
                                updateQuickslot();

                                Dungeon.level.occupyCell(enemy);
                                Dungeon.observe();
                                GameScene.updateFog();
                            }
                        }));
                        hero.next();
                    }
                }));
    }

    // ========== 武技实现 ==========
    // ========== 武技：奇点坍缩 ==========
    @Override
    public String targetingPrompt() {
        return Messages.get(this, "prompt");
    }

    @Override
    protected int baseChargeUse(Hero hero, Char target) {
        return 10; // 消耗10充能
    }

    @Override
    protected void duelistAbility(Hero hero, Integer target) {
        if (target == null) return;
        if (!Dungeon.level.insideMap(target) || Dungeon.level.solid[target]) {
            GLog.w(Messages.get(this, "ability_no_target"));
            return;
        }

        beforeAbilityUsed(hero, null);

        Singularity dark = new Singularity();
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

        {
            spriteClass = SunSprite.class;
            properties.add(Property.UNKNOWN);
        }

        @Override
        public boolean act() {
            boolean removed = false;

            int w = Dungeon.level.width();
            int h = Dungeon.level.height();
            int cx = pos % w;
            int cy = pos / w;

            // 1. 9*9范围内：持续定身 + 向奇点强制位移一格
            for (int dy = -4; dy <= 4; dy++) {
                for (int dx = -4; dx <= 4; dx++) {
                    int nx = cx + dx;
                    int ny = cy + dy;
                    if (nx < 0 || ny < 0 || nx >= w || ny >= h) continue;
                    int cell = ny * w + nx;

                    Char ch = Actor.findChar(cell);
                    if (ch != null && ch.isAlive()
                            && ch != Dungeon.hero
                            && ch.alignment != Char.Alignment.ALLY
                            && ch.alignment != Char.Alignment.NEUTRAL) {
                        // 持续定身（每回合刷新）
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

            spend(TICK);
            return true;
        }

        // 接触奇点：非boss直接死亡；boss受20%最大生命伤害并摧毁奇点，返回是否摧毁
        private boolean onContact(Char ch) {
            if (ch.properties().contains(Char.Property.BOSS)) {
                ch.damage(Math.round(ch.HT * 0.2f), this, DamageType.REAL);
                return true;
            } else {
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
            heap.destroy();
        }

        @Override
        public void die(Object cause) {

        }

        @Override
        public void storeInBundle(Bundle bundle) {
            super.storeInBundle(bundle);
            bundle.put(POS, pos);
        }

        @Override
        public void restoreFromBundle(Bundle bundle) {
            super.restoreFromBundle(bundle);
            pos = bundle.getInt(POS);
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



}