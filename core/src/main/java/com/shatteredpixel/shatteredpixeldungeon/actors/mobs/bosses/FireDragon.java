package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.BGMPlayer;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Boss;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Fire;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AscensionChallenge;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bleeding;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Chill;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FrostBurning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.HalomethaneBurning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Healing;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Warlock;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.notsync.DiedClearElemet;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Chains;
import com.shatteredpixel.shatteredpixeldungeon.effects.Effects;
import com.shatteredpixel.shatteredpixeldungeon.effects.Pushing;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ElmoParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.DriedRose;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.FireDragonSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.List;

public class FireDragon extends Boss implements Callback {

    private int fireAttackCooldown;
    private int eatCooldown;
    private boolean captured = false;
    public int summonedElementals = 0;
    private int targetingPos = -1;

    {
        initProperty();
        initBaseStatus(9, 15, 17, 12, 225, 7, 12);
        initStatus(30);
        spriteClass = FireDragonSprite.class;
        immunities.add(Burning.class);
        immunities.add(HalomethaneBurning.class);
        immunities.add(FrostBurning.class);
        immunities.add(Chill.class);
        immunities.add(Vertigo.class);
        immunities.add(ToxicGas.class);
    }

    @Override
    public float speed() {
        return super.speed() * (buff(HasteEffect.class) != null ? 2f : 1f);
    }

    @Override
    public boolean isInvulnerable(Class effect) {
        return !Dungeon.level.locked;
    }

    @Override
    public int drRoll() {
        int result = super.drRoll();
        if ( buff(VertigoEffect.class) != null){
            result = result * 3;
        }
        return result;
    }


    @Override
    public int damageRoll() {
        int damage = super.damageRoll();
        if (buff(DamageUpEffect.class) != null){
            damage = (int) (damage*1.4f);
        }
        return damage;
    }

    @Override
    public float attackDelay() {
        float speed = super.attackDelay();
        if (buff(DamageUpEffect.class) != null){
            speed = speed * 1.5f;
        }
        return speed;
    }

    private void pullEnemy( Char enemy, int pullPos ){
        enemy.pos = pullPos;
        enemy.sprite.place(pullPos);
        Dungeon.level.occupyCell(enemy);
        if(enemy instanceof DiedClearElemet.ClearElemetalGreen){
            Buff.prolong(this, ToxicGasEffect.class, ToxicGasEffect.DURATION);
        } else if(enemy instanceof DiedClearElemet.ClearElemetalGold){
            Buff.prolong(this, HasteEffect.class, HasteEffect.DURATION);
        } else if(enemy instanceof DiedClearElemet.ClearElemetalPure){
            Buff.prolong(this, VertigoEffect.class, VertigoEffect.DURATION);
        } else if(enemy instanceof DiedClearElemet.ClearElemetalDark){
            Buff.prolong(this, DamageUpEffect.class, DamageUpEffect.DURATION);
        } else {
            Buff.prolong(this,BleedingEffect.class, BleedingEffect.DURATION);
        }
        enemy.die(null);
    }

    @Override
    public boolean act() {
        if (fireAttackCooldown<21) {
            fireAttackCooldown++;
        }

        if (buff(SummonColdDown.class) == null && state != SLEEPING && summonedElementals <= 5) {
            Mob testActor = Clearly();
            testActor.state = testActor.HUNTING;
            GameScene.add(testActor);
            ScrollOfTeleportation.appear(testActor,334);
            Buff.affect(this, SummonColdDown.class, HP<151 ? 5f : 10f);
            // 更新召唤的元素数量
            summonedElementals++;
            //召唤精灵
            yell(Messages.get(this, "elemental"));
            if(Random.Float()<=0.4f && HP<181){
                Buff.affect(this, Healing.class).setHeal(30, 0f, 6);
                yell(Messages.get(this, "healing"));
            }
        }

        //毒气
        if (buff(ToxicGasEffect.class) != null){
            GameScene.add(Blob.seed(pos, 120, ToxicGas.class));
        }

        if(fireAttackCooldown == 20 && Dungeon.level.distance(pos,target)<2){
            ScrollOfTeleportation.appear(this,334);
            GLog.n(Messages.get(this,"toy"));
        }

        if(summonedElementals>=3 && eatCooldown<30){
            eatCooldown++;
        }

        if (eatCooldown > 29 && summonedElementals >= 3 && HP<180) {
            captured = true; // 标记是否捕获了一个目标
            ArrayList<DiedClearElemet> diedClearElemets = new ArrayList<>();
            for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
                if (mob instanceof DiedClearElemet) {
                    diedClearElemets.add((DiedClearElemet) mob);
                }
            }
            if (!diedClearElemets.isEmpty() && captured) {
                // 打乱顺序
                Random.shuffle(diedClearElemets);

                DiedClearElemet randomEnemy = diedClearElemets.get(0);
                yell(Messages.get(this, "scorpion"));
                new Item().throwSound();
                Sample.INSTANCE.play(Assets.Sounds.CHAINS);
                sprite.parent.add(new Chains(sprite.center(),
                        randomEnemy.sprite.destinationCenter(),
                        Effects.Type.RED_CHAIN,
                        new Callback() {
                            public void call() {
                                Actor.add(new Pushing(randomEnemy, randomEnemy.pos, pos, new Callback() {
                                    public void call() {
                                        pullEnemy(randomEnemy, pos);
                                        captured = false;
                                    }
                                }));
                                next();
                                eatCooldown = 0;
                            }
                        }));
            }
        }

        if (state != SLEEPING){
            Dungeon.level.seal();
        }

        return super.act();
    }

    private Mob Clearly() {
        List<Class<? extends Mob>> mobTypes = new ArrayList<>();
        mobTypes.add(DiedClearElemet.ClearElemetalBlood.class);
        mobTypes.add(DiedClearElemet.ClearElemetalDark.class);
        mobTypes.add(DiedClearElemet.ClearElemetalGreen.class);
        mobTypes.add(DiedClearElemet.ClearElemetalPure.class);
        mobTypes.add(DiedClearElemet.ClearElemetalGold.class);

        Random.shuffle(mobTypes);
        Class<? extends Mob> selectedMobType = mobTypes.get(0);
        Mob mob = null;
        try {
            mob = selectedMobType.getDeclaredConstructor().newInstance();
        } catch (Exception ignored) {}

        return mob;
    }

    @Override
    public int attackSkill( Char target ) {
        return 12;
    }

    @Override
    protected boolean canAttack( Char enemy ) {
        if(fireAttackCooldown<21 && Dungeon.level.distance(pos,target)>1){
            return false;
        } else {
            return super.canAttack(enemy) || new Ballistica( pos, enemy.pos, Ballistica.MAGIC_BOLT).collisionPos == enemy.pos;
        }

    }

    @Override
    public int attackProc(Char enemy, int damage ) {
        damage = super.attackProc( enemy, damage );
        if (Random.Int( 2,5 ) > 3) {
            Buff.affect( enemy, Burning.class ).reignite( enemy, 4f );
        }
        if (buff(BleedingEffect.class) != null){
            Buff.affect( enemy, Bleeding.class ).set( damage/3f );
        }

        return damage;
    }

    protected boolean doAttack( Char enemy ) {

        if (Dungeon.level.adjacent( pos, enemy.pos )
                || new Ballistica( pos, enemy.pos, Ballistica.MAGIC_BOLT).collisionPos != enemy.pos) {

            return super.doAttack( enemy );

        } else {
            ArrayList<Integer> candidates = new ArrayList<>();
            for (int i : PathFinder.NEIGHBOURS9) {
                int target = enemy.pos + i;
                if (target != pos && new Ballistica(pos, target, Ballistica.STOP_SOLID | Ballistica.STOP_TARGET).collisionPos == target) {
                    candidates.add(target);
                }
            }
            targetingPos = Random.element(candidates);

            if(fireAttackCooldown>=20){
                if (sprite != null && (sprite.visible || enemy.sprite.visible)) {
                    sprite.zap( enemy.pos );
                    return false;
                } else {
                    zap();
                    return true;
                }
            } else {
                return super.doAttack( enemy );
            }

        }
    }

    protected void zap() {
        if (targetingPos != -1) {

            spend(1f);

            for (int i : PathFinder.NEIGHBOURS9) {
                if (!Dungeon.level.solid[targetingPos + i]) {
                    CellEmitter.get(targetingPos + i).burst(ElmoParticle.FACTORY, 5);
                    GameScene.add(Blob.seed(targetingPos + i, 8, Fire.class));
                }
            }
            fireAttackCooldown = 0;

            Invisibility.dispel(this);
            int dmg = Random.NormalIntRange(4, 10);
            dmg = Math.round(dmg * AscensionChallenge.statModifier(this));
            enemy.damage(dmg, new Warlock.DarkBolt());

            if (enemy == Dungeon.hero && !enemy.isAlive()) {
                Badges.validateDeathFromEnemyMagic();
                Dungeon.fail(this);
                GLog.n(Messages.get(this, "fire_kill"));
            }

            enemy.sprite.showStatus(CharSprite.NEUTRAL, enemy.defenseVerb());
        }
    }

    public void onZapComplete() {
        zap();
        next();
    }
    private static final String TARGETING_POS = "targeting_pos";
    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(TARGETING_POS, targetingPos);
        bundle.put("count",fireAttackCooldown);
        bundle.put("se",summonedElementals);
        bundle.put("eat",eatCooldown);
        bundle.put("attack",captured);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        targetingPos = bundle.getInt(TARGETING_POS);
        fireAttackCooldown = bundle.getInt("count");
        summonedElementals = bundle.getInt("se");
        eatCooldown = bundle.getInt("eat");
        captured = bundle.getBoolean("attack");
        if (state != SLEEPING) BossHealthBar.assignBoss(this);
    }

    @Override
    public void call() {
        next();
    }

    @Override
    public void notice() {
        super.notice();
        if (!BossHealthBar.isAssigned()) {
            BossHealthBar.assignBoss(this);
            GameScene.bossReady();
            BGMPlayer.playBoss();
            GameScene.flash(Window.ANSDO_COLOR);
            Camera.main.shake(1f,3f);
            this.sprite.showStatus(CharSprite.NEGATIVE, "!!!");
            Dungeon.level.seal();
            GLog.n(Messages.get(this, "notice"));
            for (Char ch : Actor.chars()){
                if (ch instanceof DriedRose.GhostHero){
                    ((DriedRose.GhostHero) ch).sayBoss();
                }
            }
        }
    }

    /**BUFF组*/
    public static class BleedingEffect extends FlavourBuff {
        {
            type = buffType.POSITIVE;
            announced = true;
        }

        public static final float DURATION	= 30f;
        @Override
        public int icon() {
            return BuffIndicator.UPGRADE;
        }
        @Override
        public void tintIcon(Image icon) {
            icon.hardlight(Window.GDX_COLOR);
        }

        @Override
        public float iconFadePercent() {
            return Math.max(0, (DURATION - visualcooldown()) / DURATION);
        }
    }

    public static class ToxicGasEffect extends FlavourBuff {
        {
            type = buffType.POSITIVE;
            announced = true;
        }

        public static final float DURATION	= 70f;
        @Override
        public int icon() {
            return BuffIndicator.UPGRADE;
        }
        @Override
        public void tintIcon(Image icon) {
            icon.hardlight(Window.GREEN_COLOR);
        }
        @Override
        public float iconFadePercent() {
            return Math.max(0, (DURATION - visualcooldown()) / DURATION);
        }
    }

    public static class DamageUpEffect extends FlavourBuff {
        {
            type = buffType.POSITIVE;
            announced = true;
        }

        public static final float DURATION	= 50f;
        @Override
        public int icon() {
            return BuffIndicator.UPGRADE;
        }
        @Override
        public void tintIcon(Image icon) {
            icon.hardlight(Window.CBLACK);
        }
        @Override
        public float iconFadePercent() {
            return Math.max(0, (DURATION - visualcooldown()) / DURATION);
        }
    }

    public static class VertigoEffect extends FlavourBuff {
        {
            type = buffType.POSITIVE;
            announced = true;
        }

        public static final float DURATION	= 80f;
        @Override
        public int icon() {
            return BuffIndicator.UPGRADE;
        }
        @Override
        public void tintIcon(Image icon) {
            icon.hardlight(Window.DeepPK_COLOR);
        }
        @Override
        public float iconFadePercent() {
            return Math.max(0, (DURATION - visualcooldown()) / DURATION);
        }
    }

    public static class HasteEffect extends FlavourBuff {
        {
            type = buffType.POSITIVE;
            announced = true;
        }

        public static final float DURATION	= 20f;
        @Override
        public int icon() {
            return BuffIndicator.UPGRADE;
        }
        @Override
        public void tintIcon(Image icon) {
            icon.hardlight(Window.CYELLOW);
        }
        @Override
        public float iconFadePercent() {
            return Math.max(0, (DURATION - visualcooldown()) / DURATION);
        }
    }

    public static class SummonColdDown extends FlavourBuff {

        {
            type = buffType.POSITIVE;
        }

        public static final float DURATION	= 10f;

        @Override
        public float iconFadePercent() {
            return Math.max(0, (DURATION - visualcooldown()) / DURATION);
        }

    }

}
