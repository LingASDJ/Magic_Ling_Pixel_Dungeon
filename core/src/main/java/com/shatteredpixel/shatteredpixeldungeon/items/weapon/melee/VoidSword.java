package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NPC;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.extra.KusumiMagicGirl;
import com.shatteredpixel.shatteredpixeldungeon.effects.Chains;
import com.shatteredpixel.shatteredpixeldungeon.effects.Effects;
import com.shatteredpixel.shatteredpixeldungeon.effects.Pushing;
import com.shatteredpixel.shatteredpixeldungeon.levels.MiningLevel;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.BArray;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

//虚空剑
//四阶，力量需求15
//初始6-30，成长2-5
//在进行攻击后，立刻将视野内可达位置中的一名随机敌人吸引至身前。
//这把剑身中传来强大的吸引力，仿佛你正置身死寂的黑洞之中。
//武技：奇点坍缩，消耗10充能，在指定位置放置1个奇点，奇点会持续定身9*9范围内的敌人，并且每回合会使范围内的所有敌人和物品向奇点方向强制位移一格。
//奇点每回合都会摧毁与他相邻或重叠的物品，接触奇点的非boss单位会直接死亡，boss单位会受到20%最大生命值的伤害随后摧毁奇点。
public class VoidSword extends MeleeWeapon{
    {
        image = ItemSpriteSheet.SKIN_5;

        hitSound = Assets.Sounds.HIT_CRUSH;
        hitSoundPitch = 1f;

        tier = 4;
    }

    @Override
    public int STRReq(int lvl){
        int req = STRReq(tier, lvl)+1;
        if (masteryPotionBonus){
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
        if (Actor.findChar(chain.collisionPos) != null){
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
    private void chainEnemy(Ballistica chain, final Hero hero, final Char enemy ){

        if (enemy.properties().contains(Char.Property.IMMOVABLE)) {
            return;
        }

        int bestPos = -1;
        for (int i : chain.subPath(1, chain.dist)){
            if (!Dungeon.level.solid[i]
                    && Actor.findChar(i) == null
                    && (!Char.hasProp(enemy, Char.Property.LARGE) || Dungeon.level.openSpace[i])){
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

        Sample.INSTANCE.play( Assets.Sounds.CHAINS );
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

}
