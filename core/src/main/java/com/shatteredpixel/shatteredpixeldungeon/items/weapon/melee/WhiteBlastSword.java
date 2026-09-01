package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.BlobImmunity;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Poison;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.status.WhiteBlastSwordStatus;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.BlobEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ElmoParticle;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.AttackIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.BArray;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class WhiteBlastSword extends MeleeWeapon {

    public int attack_Teleology;

    {
        image = ItemSpriteSheet.WHITE_BAST;
        tier = 5;
    }

    @Override
    public int proc(Char attacker, Char defender, int damage) {
        attack_Teleology++;
        if(attack_Teleology >= 14 - buffedLvl()/5){
            Buff.affect(hero, WhiteBlastSwordStatus.class).set(1,100);
        }
        return super.proc(attacker, defender, damage);
    }

    @Override
    public int STRReq(int lvl) {
        int req = (7 + tier * 2) - (int)(Math.sqrt(8 * lvl + 1) - 1)/2;
        if (masteryPotionBonus){
            req -= 2;
        }
        return req;
    }

    @Override
    public int min(int lvl) {
        return 7 + lvl * 2;
    }
    @Override
    public int max(int lvl) {
        return 20 + lvl * 6;
    }

    private static final String INTERVAL    = "acs";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( INTERVAL, attack_Teleology );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        attack_Teleology = bundle.getInt( INTERVAL );
    }

    public static String[] TXT_RANDOM = {
            Messages.get(WhiteBlastSword.class,"roll1"),
            Messages.get(WhiteBlastSword.class,"roll2"),
            Messages.get(WhiteBlastSword.class,"roll3"),
    };

    /*
    public void whiteBlast_Sword() {
        if(GameScene.scene != null){
            int mapLength = Dungeon.level.length();

            for (int i : PathFinder.NEIGHBOURS13) {
                int targetingPos = hero.pos + i;
                if (targetingPos >= 0 && targetingPos < mapLength && !Dungeon.level.solid[targetingPos]) {
                    Emitter emitter = CellEmitter.get(targetingPos);
                    emitter.burst(ElmoParticle.FACTORY, 5);
                    GameScene.add(Blob.seed(targetingPos, 1, LastBlobs.class));
                }
            }
            hero.spend( Actor.TICK );
            hero.busy();
            curUser = hero;
            hero.sprite.operate( curUser.pos );
            Sample.INSTANCE.play( Assets.Sounds.HIT_SLASH );
            BuffIndicator.refreshHero();
            attack_Teleology = 0;
        }
    }


    public static class LastBlobs extends Blob implements Hero.Doom {
        private static ArrayList<Class> affectedBlobs;
        {
            affectedBlobs = new ArrayList<>(new BlobImmunity().immunities());
        }

        @Override
        public String tileDesc() {
            return "";
        }
        @Override
        protected void evolve() {
            super.evolve();

            int damage = (int) (hero.damageRoll()*1.2f);

            Char ch;
            int cell;

            ArrayList<Blob> blobs = new ArrayList<>();
            for (Class c : affectedBlobs){
                Blob b = Dungeon.level.blobs.get(c);
                if (b != null && b.volume > 0){
                    blobs.add(b);
                }
            }

            for (int i = area.left; i < area.right; i++){
                for (int j = area.top; j < area.bottom; j++){
                    cell = i + j*Dungeon.level.width();
                    if (cur[cell] > 0 && (ch = Actor.findChar( cell )) != null && ch != hero) {
                        if (!ch.isImmune(this.getClass())) {
                            ch.damage(damage, this);
                            Buff.affect( ch, Vertigo.class,8f);
                            int poisonLevel = 2 + (hero.belongings.weapon != null ? hero.belongings.weapon.level() : 4);
                            Buff.affect( ch, Poison.class ).set(poisonLevel);
                            for (Blob blob : blobs) {
                                blob.clear(i);
                            }
                        }
                    }
                }
            }
        }

        @Override
        public void use( BlobEmitter emitter ) {
            emitter.pour( Speck.factory( Speck.DISCOVER ), 0.4f );
        }

        @Override
        public void onDeath() {
        }
    }
    */

    @Override
    protected int baseChargeUse(Hero hero, Char target) {
        return 2;
    }

    @Override
    public String targetingPrompt() {
        return Messages.get(this, "prompt");
    }

    @Override
    protected void duelistAbility(Hero hero, Integer target) {
        if (target == null) return;

        boolean targetIsWall = Dungeon.level.solid[target];
        PathFinder.buildDistanceMap(target, BArray.or(Dungeon.level.passable, Dungeon.level.avoid, null));

        if (targetIsWall ||PathFinder.distance[curUser.pos] == Integer.MAX_VALUE){
            GLog.w(Messages.get(MeleeWeapon.class, "ability_bad_position"));
            return;
        }

        //===== 普通敌人目标，原有逻辑，消耗充能、逐浪突刺，最大3格 =====
        int dmgBoost =  augment.damageFactor(5 + Math.round(1.5f*buffedLvl()));
        waveLunge(hero, target, 1, dmgBoost, this);
    }

    @Override
    public String abilityInfo() {
        int dmgBoost = levelKnown ? 5 + Math.round(1.5f*buffedLvl()) : 5;
        if (levelKnown){
            return Messages.get(this, "ability_desc", augment.damageFactor(min()+dmgBoost), augment.damageFactor(max()+dmgBoost));
        } else {
            return Messages.get(this, "typical_ability_desc", min(0)+dmgBoost, max(0)+dmgBoost);
        }
    }

    public String upgradeAbilityStat(int level){
        int dmgBoost = 5 + Math.round(1.5f*level);
        return augment.damageFactor(min(level)+dmgBoost) + "-" + augment.damageFactor(max(level)+dmgBoost);
    }

    // ====================== 逐浪突刺（复刻解离魔杖 弹道穿透伤害） ======================
    public static void waveLunge(Hero hero, Integer target, float dmgMulti, int dmgBoost, MeleeWeapon wep){
        if (target == null) return;

        // 目标校验
        Char enemy = Actor.findChar(target);
        if (Dungeon.level.heroFOV[target]) {
            if (enemy == null || enemy == hero || hero.isCharmedBy(enemy)) {
                GLog.w(Messages.get(wep, "ability_no_target"));
                return;
            }
        }

        // 核心规则：至多3格，超过禁用
        final int MAX_RANGE = 3;
        Ballistica ballistica = new Ballistica(hero.pos, target, Ballistica.PROJECTILE);
        if (Dungeon.level.distance(hero.pos,target) > MAX_RANGE || hero.rooted) {
            GLog.w(Messages.get(wep, "ability_target_range"));
            return;
        }

        // 突进落点：目标所在格子的相邻位置（安全可站立）
        int lungeCell = -1;
        for (int i : PathFinder.NEIGHBOURS8) {
            int cell = target + i;
            if (Actor.findChar(cell) == null && (Dungeon.level.passable[cell] || Dungeon.level.avoid[cell] && hero.flying)) {
                lungeCell = cell;
                break;
            }
        }
        if (lungeCell == -1) lungeCell = hero.pos; // 无落点则原地

        final int dest = lungeCell;

        hero.busy();
        Sample.INSTANCE.play(Assets.Sounds.MISS);
        hero.sprite.jump(hero.pos, dest, 0, 0.15f, new Callback() {
            @Override
            public void call() {
                hero.pos = dest;
                Dungeon.level.occupyCell(hero);
                Dungeon.observe();

                hero.belongings.abilityWeapon = wep;
                // ========== 完全复刻解离魔杖：遍历弹道路径，沿途所有敌人造成伤害 ==========
                for (int cell : ballistica.subPath(1, ballistica.dist)) {
                    Char ch = Actor.findChar(cell);
                    if (ch != null && ch != hero && ch.alignment == Char.Alignment.ENEMY) {
                        // 必定命中，沿途伤害
                        hero.attack(ch, dmgMulti, dmgBoost, Char.INFINITE_ACCURACY);
                        AttackIndicator.target(ch);
                    }
                }

                // 主目标已由路径遍历命中，不再重复攻击；此处仅结算充能消耗
                if (enemy != null) {
                    wep.beforeAbilityUsed(hero, enemy);
                    wep.afterAbilityUsed(hero);
                }

                Sample.INSTANCE.play(Assets.Sounds.HIT_STRONG);
                hero.sprite.showStatus(CharSprite.NEGATIVE, WhiteBlastSword.TXT_RANDOM[Random.Int(WhiteBlastSword.TXT_RANDOM.length)]);

                Invisibility.dispel();
                hero.spendAndNext(hero.attackDelay());
                hero.belongings.abilityWeapon = null;
            }
        });
    }

}
