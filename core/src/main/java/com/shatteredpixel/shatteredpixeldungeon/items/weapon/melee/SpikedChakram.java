package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bleeding;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.Splash;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MissileSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.AttackIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.tweeners.AlphaTweener;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

//尖刺轮盘
//二阶，力量需求12
//初始2-15，成长1-3
//当你拥有护盾时，获得1-3+等级点额外伤害与护甲。
//当车轮完美，当车舵完蛋。
public class SpikedChakram extends MeleeWeapon {
    boolean circleBackhit = false;
    {
        image = ItemSpriteSheet.STICK_CIRCLE;

        hitSound = Assets.Sounds.HIT_CRUSH;
        hitSoundPitch = 1f;

        tier = 2;
    }

    @Override
    public int max(int lvl) {
        return 15 + lvl * 3;
    }

    @Override
    public int min(int lvl) {
        return 2 + lvl;
    }

    // 额外伤害:提高武器提供的伤害掷点上下限 这里是增加一个小伤害值,数学上在平均值和最大最小值上是等价的
    @Override
    public int damageRoll(Char owner) {
        int dmg = super.damageRoll(owner);
        if (owner.shielding() > 0) {
            dmg += Random.IntRange(1, 3 + buffedLvl());
        }
        return dmg;
    }

    // 额外护甲:提高武器提供的防御掷点上下限
    @Override
    public int defenseFactor(Char owner) {
        int def = super.defenseFactor(owner);
        if (owner.shielding() > 0) {
            def += Random.IntRange(1, 3 + buffedLvl());
        }
        return def;
    }

    // 实时显示护盾加成区间
    @Override
    public String statsInfo() {
        return Messages.get(this, "stats_desc", 1, 3 + buffedLvl());
    }

    // ==================== 决斗者武技：血滴子 ====================

    @Override
    public String targetingPrompt() {
        return Messages.get(this, "prompt");
    }

    @Override
    protected void duelistAbility(Hero hero, Integer target) {
        if (target == null) {
            return;
        }

        Char enemy = Actor.findChar(target);
        if (enemy == null || enemy == hero || hero.isCharmedBy(enemy) || !Dungeon.level.heroFOV[target]) {
            GLog.w(Messages.get(this, "ability_no_target"));
            return;
        }

        int dist = Dungeon.level.distance(hero.pos, enemy.pos);

        if (dist <= 1) {
            GLog.w(Messages.get(this, "ability_melee_forbid"));
            return;
        }

        if (dist > 8) {
            GLog.w(Messages.get(this, "ability_range_exceed"));
            return;
        }

        hero.belongings.abilityWeapon = this;
        hero.belongings.abilityWeapon = null;

        beforeAbilityUsed(hero, enemy);
        AttackIndicator.target(enemy);

        // ========== 新建一次性临时投射飞盘（MissileWeapon）==========
        TempChakramThrow tempDisc = new TempChakramThrow();
        // 复制当前尖刺轮盘等级/强化，保证伤害一致
        tempDisc.level = this.level();
        tempDisc.augment = this.augment;
        tempDisc.circleBackhit = false;

        // 投射飞盘飞向目标
        MissileSprite visual = (MissileSprite) hero.sprite.parent.recycle(MissileSprite.class);
        visual.reset(hero.pos, enemy.pos, tempDisc, new Callback() {
            @Override
            public void call() {
                // 命中判定 +50%精准，100%倍率
                boolean hit = hero.attack(enemy, 1f, 0, hero.attackSkill(enemy) * 1.5f);
                if (hit) {
                    int dealt = damageRoll(hero);
                    if (dealt > 0) {
                        Buff.affect(enemy, Bleeding.class).set(Math.round(dealt * 0.5f));
                    }
                    Sample.INSTANCE.play(Assets.Sounds.HIT_STRONG);
                }
                hero.spendAndNext(hero.attackDelay());
                afterAbilityUsed(hero);
            }
        });

        visual.alpha(0f);
        float duration = Dungeon.level.trueDistance(hero.pos, enemy.pos) / 20f;
        hero.sprite.parent.add(new AlphaTweener(visual, 1f, duration));
    }

    @Override
    public String abilityInfo() {
        if (levelKnown) {
            return Messages.get(this, "ability_desc");
        } else {
            return Messages.get(this, "typical_ability_desc");
        }
    }

    public static class TempChakramThrow extends MissileWeapon {
        public boolean circleBackhit;
        {
            tier = 2;
            baseUses = Float.MAX_VALUE;
            image = ItemSpriteSheet.STICK_CIRCLE;
        }

        @Override
        public int min(int lvl) {
            return 2 + lvl;
        }

        @Override
        public int max(int lvl) {
            return 15 + lvl * 3;
        }

        @Override
        protected void onThrow( int cell ) {
            Char enemy = Actor.findChar( cell );
            if (enemy == null || enemy == curUser) {
                parent = null;
                Splash.at( cell, 0xCC99FFFF, 1 );
            } else {
                if (!curUser.shoot( enemy, this )) {
                    Splash.at(cell, 0xCC99FFFF, 1);
                }
            }
        }
    }
}
