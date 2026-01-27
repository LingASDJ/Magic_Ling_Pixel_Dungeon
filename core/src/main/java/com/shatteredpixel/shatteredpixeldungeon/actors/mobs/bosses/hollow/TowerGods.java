package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.hollow;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Boss;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FrostBurning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.HalomethaneBurning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Healing;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hex;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LockedFloor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Ooze;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Eye;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Scorpio;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.ShieldHuntsman;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfCleansing;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfElements;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.TowerGodSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.noosa.Image;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class TowerGods extends Boss {
    private boolean LastHP = HP*2 <= HT;
    public int magicDefence = 100;
    public int physicDefence = 100;

    private int summonedMobs = 1;

    {
        initProperty();
        initBaseStatus(15, 20, 33, 10, 500, 0, 0);
        initStatus(120);
        noDropIceCoin = true;
        spriteClass = TowerGodSprite.class;

        viewDistance = 100;

        properties.add(Property.IMMOVABLE);
        properties.add(Property.BOSS);

        immunities.add(FrostBurning.class);
        immunities.add(HalomethaneBurning.class);
        immunities.add(Burning.class);
        immunities.add(Ooze.class);
        immunities.add(Terror.class);
        immunities.add(Hex.class);
        immunities.add(Vertigo.class);
        immunities.add(Blindness.class);
        immunities.add(TowerMachine.DeadAlive.class);
        immunities.add(Blob.class);
    }

    private static final String LAST_HP = "last_hp";
    private static final String MAGIC_DEFENCE = "magic_defence";
    private static final String PHYSIC_DEFENCE = "physic_defence";
    private static final String SUMMONED_MOBS = "summoned_mobs";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(LAST_HP, LastHP);
        bundle.put(MAGIC_DEFENCE, magicDefence);
        bundle.put(PHYSIC_DEFENCE, physicDefence);
        bundle.put(SUMMONED_MOBS, summonedMobs);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        LastHP        = bundle.getBoolean(LAST_HP);
        magicDefence  = bundle.getInt(MAGIC_DEFENCE);
        physicDefence = bundle.getInt(PHYSIC_DEFENCE);
        summonedMobs  = bundle.getInt(SUMMONED_MOBS);
    }

    @Override
    public int damageRoll() {
        return 0;
    }

    public static class DreamEye extends Eye {

        {
            maxLvl = -31;
            HP = HT = 120;
        }

        @Override
        public int attackSkill( Char target ) {
            return 50;
        }

        @Override
        public int drRoll() {
            return super.drRoll() + Random.NormalIntRange(4, 10);
        }
    }

    public static class DreamShieldHuntsman extends ShieldHuntsman {

        {
            maxLvl = -31;
            HP = HT = 60;
        }

        @Override
        public int attackSkill( Char target ) {
            return 40;
        }

        @Override
        public int drRoll() {
            return super.drRoll() + Random.NormalIntRange(0, 16);
        }
    }

    public static class DreamScorpio extends Scorpio {

        {
            maxLvl = -31;
            HP = HT = 90;
        }

        @Override
        public int attackSkill( Char target ) {
            return 30;
        }

        @Override
        public int drRoll() {
            return super.drRoll() + Random.NormalIntRange(9, 16);
        }
    }

    private Mob getSummonTimeMobs() {
        List<Class<? extends Mob>> mobTypes = new ArrayList<>();
        mobTypes.add(DreamScorpio.class);
        mobTypes.add(DreamEye.class);
        mobTypes.add(DreamShieldHuntsman.class);

        Random.shuffle(mobTypes);
        Class<? extends Mob> selectedMobType = mobTypes.get(0);
        Mob mob = null;
        try {
            mob = selectedMobType.getDeclaredConstructor().newInstance();
        } catch (Exception ignored) {}
        return mob;
    }

    public void TryGetSummonedMobs() {
        ArrayList<Integer> positions = new ArrayList<>();
        if (buff(GodsSummonColdDown.class) == null && summonedMobs <= 4) {
            Mob testActor = getSummonTimeMobs();
            testActor.state = testActor.HUNTING;
            GameScene.add(testActor);
            /****************/
            positions.add(306);
            positions.add(302);
            positions.add(354);
            positions.add(229);
            /****************/
            Random.shuffle(positions);
            ScrollOfTeleportation.appear(testActor,positions.get(0));
            Mob testActor2 = getSummonTimeMobs();
            testActor2.state = testActor2.HUNTING;
            GameScene.add(testActor2);
            /****************/
            positions.add(306);
            positions.add(302);
            positions.add(354);
            positions.add(229);
            /****************/
            Random.shuffle(positions);
            ScrollOfTeleportation.appear(testActor2,positions.get(0));
            Buff.affect(this, Barrier.class).setShield(100);
            Buff.affect(this, GodsSummonColdDown.class, 50f);
            summonedMobs+=2;
        }
    }

    public void getAllyBuffs() {
        for (Mob boss : Dungeon.level.mobs.toArray(new Mob[0])) {
            if (boss.alignment == Alignment.ENEMY && !(boss instanceof TowerGods || boss instanceof TowerTime|| boss instanceof TowerMachine || boss instanceof TowerMind )) {
                if(!LastHP){
                    switch (Random.Int(13)){
                        case 9: case 11: case 12:
                            Buff.affect(boss, Double_AttackUP_Palf.class, AttackUP_Palf.DURATION);
                            break;
                        case 6: case 7: case 8:
                            Buff.affect(boss, Healing.class).setHeal(45, 0f, 6);
                            break;
                        case 3: case 4: case 5:
                            PotionOfCleansing.cleanse(boss);
                            break;
                        case 0: case 1: case 2:
                            Buff.affect(boss, Barrier.class).setShield(75);
                            break;
                    }
                } else {
                    switch (Random.Int(13)){
                        case 9: case 11: case 12:
                            Buff.affect(boss, AttackUP_Palf.class, AttackUP_Palf.DURATION);
                            break;
                        case 6: case 7: case 8:
                            Buff.affect(boss, Healing.class).setHeal(30, 0f, 6);
                            break;
                        case 3: case 4: case 5:
                            PotionOfHealing.cure(boss);
                            break;
                        case 0: case 1: case 2:
                            Buff.affect(boss, Barrier.class).setShield(50);
                            break;
                    }
                }

            }
        }
    }

    @Override
    public void die(Object cause) {
        super.die(cause);
         for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
            if (mob instanceof TowerMind || mob instanceof TowerTime||mob instanceof TowerGods||mob instanceof TowerMachine) {
                Buff.affect(mob, TowerParalysis.class).set((21), 1);
            }
            if(mob instanceof Morphs){
                ((Morphs) mob).phase+=0.25f;
            }
            if(mob instanceof DreamEye || mob instanceof DreamScorpio || mob instanceof DreamShieldHuntsman){
                mob.die(true);
            }
        };
    }

    protected boolean act() {
        alerted = false;
        TowerParalysis towerParalysis = buff(TowerParalysis.class);
        if(towerParalysis == null) {
            if(!LastHP && buff(GetAllyBuffs.class) == null){
                getAllyBuffs();
                Buff.affect(this, GetAllyBuffs.class, 20f);
            } else if(LastHP && buff(GetAllyBuffs.class) == null) {
                getAllyBuffs();
                Buff.affect(this, GetAllyBuffs.class, 25f);
            }

            TryGetSummonedMobs();

            state = PASSIVE;
        }

        return super.act();
    }

    public static class GetAllyBuffs extends FlavourBuff {

        {
            type = buffType.POSITIVE;
        }

        public static final float DURATION	= 5f;

        @Override
        public float iconFadePercent() {
            return Math.max(0, (DURATION - visualcooldown()) / DURATION);
        }

    }

    public static class AttackUP_Palf extends FlavourBuff {

        {
            type = buffType.POSITIVE;
            announced = true;
        }

        public static final float DURATION	= 5f;

        @Override
        public float iconFadePercent() {
            return Math.max(0, (DURATION - visualcooldown()) / DURATION);
        }

        @Override
        public void tintIcon(Image icon) {
            icon.hardlight(Window.TITLE_COLOR);
        }

        @Override
        public int icon() {
            return BuffIndicator.WEAPON;
        }

    }

    public static class Double_AttackUP_Palf extends FlavourBuff {

        {
            type = buffType.POSITIVE;
        }

        public static final float DURATION	= 5f;

        @Override
        public float iconFadePercent() {
            return Math.max(0, (DURATION - visualcooldown()) / DURATION);
        }

        @Override
        public void tintIcon(Image icon) {
            icon.hardlight(Window.RED_COLOR);
        }

        @Override
        public int icon() {
            return BuffIndicator.WEAPON;
        }

    }



    @Override
    public void damage(int dmg, Object src, DamageType type) {
        if(src == TowerMachine.class){
            return;
        }
        BossHealthBar.assignBoss(this);

        LockedFloor lock = hero.buff(LockedFloor.class);
        if (lock != null) {
            int multiple = 2;
            lock.addTime(dmg*multiple);
        }
        BossHealthBar.assignBoss(this);
        // 神选之人：计算有效增益Buff数量
        int buffCount = 0;
        for (Buff b : Dungeon.hero.buffs()) {
            if (b.type == Buff.buffType.POSITIVE) {
                buffCount++;
            }
        }

        // 至多加成50%（10层）
        float buffMultiplier = 1 + Math.min(buffCount * 0.05f, 0.5f);
        dmg = (int)(dmg * buffMultiplier);

        // 元素抗性检测开始
        Class<?> srcClass = src.getClass();
        HashSet<Class> resists = new HashSet<>(RingOfElements.RESISTS);
        boolean flag = false;
        for (Class c : resists){
            if (c.isAssignableFrom(srcClass)){
                flag=true;
                break;
            }
        }

        if (flag) {
            if(enemy != null){
                if (Dungeon.level.distance(pos,enemy.pos)>=5) {
                    dmg *= (int) 0.25f;
                }
            } else {
                float rate = ((float) magicDefence / 100);
                dmg *= (int) rate;
            }
        } else if (!(src instanceof Buff)) {
            float rate = ((float) physicDefence / 100);
            dmg *= rate;
        }

        super.damage(dmg, src, type);
    }

    public static class GodsSummonColdDown extends FlavourBuff {

        {
            type = buffType.POSITIVE;
        }

        public static final float DURATION	= 10f;

        @Override
        public float iconFadePercent() {
            return Math.max(0, (DURATION - visualcooldown()) / DURATION);
        }

    }

    @Override
    public boolean reset() {
        return true;
    }

    @Override
    protected boolean getCloser(int target) {
        return false;
    }

    @Override
    protected boolean getFurther(int target) {
        return false;
    }
}
