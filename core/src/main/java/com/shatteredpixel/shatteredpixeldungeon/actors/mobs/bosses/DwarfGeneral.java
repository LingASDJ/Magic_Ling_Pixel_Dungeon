package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.BGMPlayer;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.PaswordBadges;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Boss;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.BrokenArmorFire;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ConfusionGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.CorrosiveGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Adrenaline;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Amok;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Chill;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Degrade;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Dread;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ElementalBuff.ElementalBaseBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LockedFloor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Sleep;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.DM100;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Monk;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Senior;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Warlock;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.FloatingText;
import com.shatteredpixel.shatteredpixeldungeon.effects.Pushing;
import com.shatteredpixel.shatteredpixeldungeon.effects.TargetedCell;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ElmoParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.KingsCrown;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.DriedRose;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.BlackKey;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.CrystalKey;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.GoldenKey;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.SkeletonKey;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfBlastWave;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.legend.KingAxe;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DwarfGeneralSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.GameMath;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.List;

public class DwarfGeneral extends Boss {
    protected int rangedCooldown = Random.NormalIntRange( 3, 5 );
    private int targetingPos = -1;

    //技能1.2--坚如磐石
    private int armorCooldown;
    private int swordCooldown;

    private int magicAttackCooldown;

    private int limitDamage;

    //大阶段控制

    public int phase;
    private int jumpCooldown;
    private int armyCooldown;

    private int enderCooldown;
    @Override
    public int damageRoll() {
        return Char.combatRoll( 20, 42 );
    }


    {
        initProperty();
        initBaseStatus(12, 24, 23,
                buff(DwarfGeneral.ArmorEffect.class) == null ? 1 : 18, 1000, 9, 16);
        initStatus(40);

        properties.add(Property.DEMONIC);

        spriteClass = DwarfGeneralSprite.class;

        HUNTING = new Hunting();

        immunities.add(BrokenArmorFire.class);
        immunities.add( Paralysis.class );
        immunities.add( Amok.class );
        immunities.add( Sleep.class );
        immunities.add( CorrosiveGas.class );
        immunities.add( ConfusionGas.class );
        immunities.add( Terror.class );
        immunities.add( Dread.class );
        immunities.add( Vertigo.class );
        immunities.add( CrivusFruits.DiedBlobs.class );
        immunities.add( ToxicGas.class );
    }

    /**
     * 技能1.4 战吼
     */
    private void getArmyFouns(){
        for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])){
            if (!(mob instanceof DwarfGeneral)) {
                Buff.affect(mob, Adrenaline.class, 8f);
                Sample.INSTANCE.play( Assets.Sounds.CHALLENGE );
                Camera.main.shake(1f,3f);
                WandOfBlastWave.BlastWave.blast(pos);
            }
        }
    }

    @Override
    public int attackProc( Char enemy, int damage ) {
        damage = super.attackProc( enemy, damage );
        int reg = Math.min( damage, 800 - HP );
        DwarfGeneral.NoHealDied f = buff(DwarfGeneral.NoHealDied.class);
        if (reg > 0 && f != null) {
            HP += reg;
            sprite.showStatusWithIcon(CharSprite.POSITIVE, Integer.toString(reg), FloatingText.HEALING);
        }

        if(buff(DwarfGeneral.MagicAttack.class) != null){
            Buff.prolong( enemy, Degrade.class, Degrade.DURATION/3f );

            if(Random.Int(10)>=6){
                Buff.affect(enemy, Wither.class).set((10),1);
            }

        }

        return damage;
    }

    @Override
    protected boolean act() {
        // 第一阶段技能组
        if(phase == 0){
            rangedCooldown--;
        }

        for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])){
            if (mob instanceof DwarfSolider) {
               if(mob.buff(Barrier.class) != null){
                    getArmyFouns();
                    mob.buff(Barrier.class).detach();
                    yell(Messages.get(this,"wtf"));
                }
            }
        }

        if(armorCooldown<13 && buff(DwarfGeneral.ArmorEffect.class) == null && HP>800){
            armorCooldown++;
        } else if(armorCooldown >= 12) {
            Buff.prolong(this, DwarfGeneral.ArmorEffect.class,  DwarfGeneral.ArmorEffect.DURATION);
            yell(Messages.get(this,"gotime"));
            armorCooldown = 0;
        }

        // 第2阶段技能组
        if(phase >= 1){
            if(buff(DwarfGeneral.NoHealDied.class) == null) {
                swordCooldown++;
            }
            if(swordCooldown>12 && buff(DwarfGeneral.NoHealDied.class) == null){
                Buff.prolong(this, DwarfGeneral.NoHealDied.class,DwarfGeneral.NoHealDied.DURATION);
                swordCooldown = 0;
            }
            AiState lastState = state;
            if (paralysed <= 0) leapCooldown --;

            //跳跃技能冷却
            jumpCooldown++;

            //用兵不疑 OR 疑兵不用
            armyCooldown++;
            if(armyCooldown>=40){
                if(Random.Int(0,100)>50){
                    summonGetFuzeOrSolider();
                } else {
                    summonGetNormal();
                }
                armyCooldown = 0;
            }

            //if state changed from wandering to hunting, we haven't acted yet, don't update.
            if (!(lastState == WANDERING && state == HUNTING)) {
                if (enemy != null) {
                    lastEnemyPos = enemy.pos;
                } else {
                    lastEnemyPos = hero.pos;
                }
            }
        }

        //第三阶段技能组
        if(phase>=2){
            if(buff(DwarfGeneral.MagicAttack.class) == null){
                magicAttackCooldown++;
            }
            if(magicAttackCooldown>=25){
                Buff.prolong(this, DwarfGeneral.MagicAttack.class,DwarfGeneral.MagicAttack.DURATION);
                magicAttackCooldown = 0;
            }
        }

        if(phase>3){
            if(enderCooldown>=10){
                enderCooldown = 0;
                summonGetNormal();
                summonGetSolider();
                damage(5,this);
                alerted = false;
                state = PASSIVE;
            }
        }

        int heroPos = Dungeon.hero.pos;
        int bossToHeroDistance = Dungeon.level.distance(pos, heroPos);
        if (targetingPos != -1 && bossToHeroDistance < 4){
            if (sprite != null && (sprite.visible || Dungeon.level.heroFOV[targetingPos])) {
                ((DwarfGeneralSprite)sprite).skills( targetingPos );
                return false;
            } else {
                zap();
                return true;
            }
        } else {
            return super.act();
        }
    }

    @Override
    protected boolean canAttack( Char enemy ) {
        if (super.canAttack(enemy)){
            return true;
        } else {
            return rangedCooldown > 30 && new Ballistica(pos, enemy.pos, Ballistica.STOP_SOLID | Ballistica.STOP_TARGET).collisionPos == enemy.pos;
        }
    }

    protected boolean doAttack( Char enemy ) {
        if (rangedCooldown > 0) {
            return super.doAttack(enemy);
        } else if (new Ballistica(pos, enemy.pos, Ballistica.STOP_SOLID | Ballistica.STOP_TARGET).collisionPos == enemy.pos) {
                //set up an attack for next turn
                ArrayList<Integer> candidates = new ArrayList<>();
                for (int i : PathFinder.NEIGHBOURS8) {
                    int target = enemy.pos + i;
                    if (target != pos && new Ballistica(pos, target, Ballistica.STOP_SOLID | Ballistica.STOP_TARGET).collisionPos == target) {
                        candidates.add(target);
                    }
                }

                if (!candidates.isEmpty()) {
                    targetingPos = Random.element(candidates);

                    for (int i : PathFinder.NEIGHBOURS8) {
                        if (!Dungeon.level.solid[targetingPos + i]) {
                            sprite.parent.addToBack(new TargetedCell(targetingPos + i, 0xFF0000));
                        }
                    }

                    spend(2f);
                    yell(Messages.get(this, "charging"));
                    rangedCooldown = 30;
                    return true;
                } else {
                    rangedCooldown = 1;
                    return super.doAttack(enemy);
                }

        } else {
            rangedCooldown = 1;
            return super.doAttack(enemy);
        }
    }



    //技能1.1
    protected void zap() {
        int dmg = damageRoll();
        if (targetingPos != -1) {
            spend(1f);

            Invisibility.dispel(this);

            for (int i : PathFinder.NEIGHBOURS8) {
                if (!Dungeon.level.solid[targetingPos + i]) {
                    CellEmitter.get(targetingPos + i).burst(ElmoParticle.FACTORY, 5);
                    if (Dungeon.level.water[targetingPos + i]) {
                        GameScene.add(Blob.seed(targetingPos + i, 2, BrokenArmorFire.class));
                    } else {
                        GameScene.add(Blob.seed(targetingPos + i, 8, BrokenArmorFire.class));
                    }
                }
            }

            //技能1.1
            final Char leapVictim = Actor.findChar(targetingPos);
            if (leapVictim != null){
                if (hit(this, leapVictim, true)) {
                    enemy.damage(dmg*Random.NormalIntRange(1,3), new DM100.LightningBolt());
                    Buff.affect(enemy, Chill.class, 10f);
                    yell(Messages.get(this, "spear_warn"));
                }
            }


            Sample.INSTANCE.play(Assets.Sounds.BURNING);
        }

        targetingPos = -1;
        rangedCooldown = Random.NormalIntRange( 3, 5 );
    }

    public void onZapComplete() {
        zap();
        next();
    }

    private int leapPos = -1;
    private float leapCooldown = 0;
    private int lastEnemyPos = -1;
    private static final String TARGETING_POS = "targeting_pos";
    private static final String COOLDOWN = "cooldown";
    private static final String ARMORCOOLDOWN = "armorcooldown";
    private static final String LIMITDAMAGE = "limitdamage";

    private static final String PHASE = "limitdamage";

    private static final String SWORDCOOLDOWN = "swordcooldown";

    private static final String MAGICCOOLDOWN = "magiccooldown";

    private static final String LAST_ENEMY_POS = "last_enemy_pos";
    private static final String LEAP_POS = "leap_pos";
    private static final String LEAP_CD = "leap_cd";
    private static final String JUMP = "jump";
    private static final String COLDDOWN = "colddown";
    private static final String EDCOLDDOWN = "edcolddown";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(TARGETING_POS, targetingPos);
        bundle.put( COOLDOWN, rangedCooldown );

        //技能1.2
        bundle.put(ARMORCOOLDOWN,armorCooldown);
        bundle.put(LIMITDAMAGE,limitDamage);

        bundle.put(PHASE,phase);

        bundle.put(SWORDCOOLDOWN,swordCooldown);

        bundle.put(LAST_ENEMY_POS, lastEnemyPos);
        bundle.put(LEAP_POS, leapPos);

        bundle.put(LEAP_CD, leapCooldown);
        bundle.put(JUMP,jumpCooldown);

        bundle.put(COLDDOWN,armyCooldown);

        bundle.put(MAGICCOOLDOWN,magicAttackCooldown);

        bundle.put(EDCOLDDOWN,enderCooldown);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);

        if (state != SLEEPING) BossHealthBar.assignBoss(this);
        if ((HP*2 <= HT)) BossHealthBar.bleed(true);

        targetingPos = bundle.getInt(TARGETING_POS);
        rangedCooldown = bundle.getInt(COOLDOWN);

        armorCooldown = bundle.getInt(ARMORCOOLDOWN);
        limitDamage = bundle.getInt(LIMITDAMAGE);
        phase = bundle.getInt(PHASE);

        swordCooldown = bundle.getInt(SWORDCOOLDOWN);
        lastEnemyPos = bundle.getInt(LAST_ENEMY_POS);
        leapPos = bundle.getInt(LEAP_POS);

        leapCooldown = bundle.getFloat(LEAP_CD);
        jumpCooldown = bundle.getInt(JUMP);

        armyCooldown = bundle.getInt(COLDDOWN);

        magicAttackCooldown = bundle.getInt(MAGICCOOLDOWN);

        enderCooldown = bundle.getInt(EDCOLDDOWN);
    }

    private void armyPhase_one(){
        ArmyFlag flag = new ArmyFlag();
        flag.pos = 1;
        GameScene.add(flag);

        ArmyFlag flag2 = new ArmyFlag();
        flag2.pos = 1;
        GameScene.add(flag2);

        ScrollOfTeleportation.appear(this, 367);

        ScrollOfTeleportation.appear(flag, 364);
        ScrollOfTeleportation.appear(flag2, 370);

        GameScene.flash(Window.GDX_COLOR);
    }

    private void armyPhase_two(){

        for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])){
            if (mob instanceof ArmyFlag) {
                mob.die(null);
            }
        }

        ArmyFlag flag = new ArmyFlag();
        flag.pos = 365;
        GameScene.add(flag);

        ArmyFlag flag2 = new ArmyFlag();
        flag2.pos = 369;
        GameScene.add(flag2);

        ArmyFlag flag3 = new ArmyFlag();
        flag3.pos = 325;
        GameScene.add(flag3);

        ArmyFlag flag4 = new ArmyFlag();
        flag4.pos = 409;
        GameScene.add(flag4);

        ScrollOfTeleportation.appear(this, 367);

        Buff.affect(flag, CrivusFruits.CFBarrior.class).setShield(20);
        Buff.affect(flag2, CrivusFruits.CFBarrior.class).setShield(20);
        Buff.affect(flag3, CrivusFruits.CFBarrior.class).setShield(20);
        Buff.affect(flag4, CrivusFruits.CFBarrior.class).setShield(20);

        GameScene.flash(Window.WHITE);
    }

    @Override
    public boolean isInvulnerable(Class effect) {
        boolean isAlive = false;
        for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])){
            if (mob instanceof ArmyFlag) {
                isAlive = true;
                break;
            }
        }
        return isAlive;
    }

    private void summonGetSolider(){
        for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])){
            if (mob instanceof DwarfSolider) {
                mob.die(null);
            }
        }

        DwarfSolider solider = new DwarfSolider();
        solider.pos = 1;
        GameScene.add(solider);

        DwarfSolider solider2 = new DwarfSolider();
        solider2.pos = 1;
        GameScene.add(solider2);

        ScrollOfTeleportation.appear(solider, 364);
        ScrollOfTeleportation.appear(solider2, 370);

        ScrollOfTeleportation.appear(this, 367);

        armorCooldown = -50;

        GameScene.flash(Window.WHITE);
    }

    private Mob HighDwarfArmy() {
        List<Class<? extends Mob>> mobTypes = new ArrayList<>();
        mobTypes.add(DwarfSolider.class);
        mobTypes.add(DwarfFuze.class);

        Random.shuffle(mobTypes);
        Class<? extends Mob> selectedMobType = mobTypes.get(0);
        Mob mob = null;
        try {
            mob = selectedMobType.getDeclaredConstructor().newInstance();
            mob.maxLvl = -1;
        } catch (Exception ignored) {}

        return mob;
    }

    private void summonGetFuzeOrSolider(){
        Mob testActor = HighDwarfArmy();
        testActor.state = testActor.HUNTING;
        GameScene.add(testActor);
        ScrollOfTeleportation.appear(testActor,323);

        Mob testActor2 = HighDwarfArmy();
        testActor2.state = testActor2.HUNTING;
        GameScene.add(testActor2);
        ScrollOfTeleportation.appear(testActor2,327);

        yell(Messages.get(this,"fuze"));
    }

    private Mob NormalDwarfArmy() {
        List<Class<? extends Mob>> mobTypes = new ArrayList<>();
        mobTypes.add(Warlock.class);
        mobTypes.add(Monk.class);
        mobTypes.add(Senior.class);

        Random.shuffle(mobTypes);
        Class<? extends Mob> selectedMobType = mobTypes.get(0);
        Mob mob = null;
        try {
            mob = selectedMobType.getDeclaredConstructor().newInstance();
            mob.maxLvl = -1;
        } catch (Exception ignored) {}

        return mob;
    }

    private void summonGetNormal(){
        Mob testActor = NormalDwarfArmy();
        testActor.state = testActor.HUNTING;
        GameScene.add(testActor);
        ScrollOfTeleportation.appear(testActor,238);

        Mob testActor2 = NormalDwarfArmy();
        testActor2.state = testActor2.HUNTING;
        GameScene.add(testActor2);
        ScrollOfTeleportation.appear(testActor2,244);

        if(phase >= 2){
            Mob testActor3 = NormalDwarfArmy();
            testActor3.state = testActor3.HUNTING;
            GameScene.add(testActor3);
            ScrollOfTeleportation.appear(testActor3,318);

            Mob testActor4 = NormalDwarfArmy();
            testActor4.state = testActor4.HUNTING;
            GameScene.add(testActor4);
            ScrollOfTeleportation.appear(testActor4,332);
        }

        yell(Messages.get(this,"normal"));
    }

    @Override
    public void damage(int dmg, Object src) {

        LockedFloor lock = hero.buff(LockedFloor.class);
        if (lock != null) {
            int multiple = 2;
            lock.addTime(dmg*multiple);
        }

        DwarfGeneral.ArmorEffect f = buff(DwarfGeneral.ArmorEffect.class);

        if(f != null){
            dmg = 1;
            limitDamage++;
            if(limitDamage >= 5){
                summonGetSolider();
                limitDamage = 0;
                f.detach();
                GLog.n(Messages.get(this,"support"));
                yell(Messages.get(this,"wow"));
            }
        }

        if(phase == 0 && HP<800){
            armyPhase_one();
            yell(Messages.get(this,"army_one"));
            phase++;
            HP = 800;
        } else if(phase == 1 && HP<600){
            armyPhase_two();
            summonGetNormal();
            summonGetSolider();
            yell(Messages.get(this,"army_two"));
            phase++;
            HP = 600;
        } else if(phase == 3 && HP<200){
            armyPhase_two();
            summonGetNormal();
            summonGetSolider();
            yell(Messages.get(this,"army_three"));
            phase++;
            HP = 200;
        }

        for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
            if (mob instanceof ArmyFlag) {
                if (((ArmyFlag) mob).ThreePhase >= 4 && phase == 2) {
                    phase++;
                    HP = 400;
                }
            }
        }



        super.damage(dmg, src);
    }

    @Override
    public void notice() {
        //super.notice();
        if (!BossHealthBar.isAssigned()) {
            BossHealthBar.assignBoss(this);
            yell(Messages.get(this, "notice"));
            BGMPlayer.playBoss();
            Camera.main.shake(1f,3f);
            GameScene.bossReady();
            for (Char ch : Actor.chars()){
                if (ch instanceof DriedRose.GhostHero){
                    ((DriedRose.GhostHero) ch).sayBoss();
                }
            }
        }
    }

    //BUFF组
    public static class ArmorEffect extends FlavourBuff {

        {
            type = buffType.POSITIVE;
            announced = true;
        }

        public static final float DURATION	= 7f;
        @Override
        public int icon() {
            return BuffIndicator.ARMOR;
        }
        @Override
        public void tintIcon(Image icon) {
            icon.hardlight(Window.SHPX_COLOR);
        }

        @Override
        public float iconFadePercent() {
            return Math.max(0, (DURATION - visualcooldown()) / DURATION);
        }
    }

    //三阶段
    public static class MagicAttack extends FlavourBuff {
        {
            type = buffType.POSITIVE;
            announced = true;
        }

        public static final float DURATION	= 15f;
        @Override
        public int icon() {
            return BuffIndicator.BLESS;
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

    public static class NoHealDied extends FlavourBuff {
        {
            type = buffType.POSITIVE;
            announced = true;
        }

        public static final float DURATION	= 6f;
        @Override
        public int icon() {
            return BuffIndicator.BLEEDING;
        }
        @Override
        public void tintIcon(Image icon) {
            icon.hardlight(Window.RED_COLOR);
        }

        @Override
        public float iconFadePercent() {
            return Math.max(0, (DURATION - visualcooldown()) / DURATION);
        }
    }

    public static class Wither extends ElementalBaseBuff {

        {
            type = buffType.NEUTRAL;
            announced = true;
        }


        public static final float DURATION	= 30f;
        private float damageInc = 0;
        @Override
        public boolean act() {
            if (target.isAlive()) {

                damageInc = Random.Int(2,5);
                target.damage((int)damageInc, this);
                damageInc -= (int)damageInc;

                spend(1f);
                if (--level <= 0) {
                    detach();
                }
                if (target == hero && !target.isAlive()){
                    GLog.n(Messages.get(this, "on_kill"));
                }


            } else {
                detach();
            }

            return true;
        }

        @Override
        public int icon() {
            return BuffIndicator.POISON;
        }

        public static final String DAMAGE = "damage_inc";

        @Override
        public void storeInBundle(Bundle bundle) {
            super.storeInBundle(bundle);
            bundle.put(DAMAGE, damageInc);
        }

        @Override
        public void restoreFromBundle(Bundle bundle) {
            super.restoreFromBundle(bundle);
            damageInc = bundle.getFloat(DAMAGE);
        }

        @Override
        public void tintIcon(Image icon) {
            icon.hardlight(1f, 0f, 0f);
        }

    }

    public class Hunting extends Mob.Hunting {
        @Override
        public boolean act( boolean enemyInFOV, boolean justAlerted ) {

            if (leapPos != -1){

                leapCooldown = Random.NormalIntRange(2, 4);
                Ballistica b = new Ballistica(pos, leapPos, Ballistica.STOP_TARGET | Ballistica.STOP_SOLID);

                //check if leap pos is not obstructed by terrain
                if (rooted || b.collisionPos != leapPos){
                    leapPos = -1;
                    return true;
                }

                final Char leapVictim = Actor.findChar(leapPos);
                final int endPos;

                //ensure there is somewhere to land after leaping
                if (leapVictim != null){
                    int bouncepos = -1;
                    for (int i : PathFinder.NEIGHBOURS8){
                        if ((bouncepos == -1 || Dungeon.level.trueDistance(pos, leapPos+i) < Dungeon.level.trueDistance(pos, bouncepos))
                                && Actor.findChar(leapPos+i) == null && Dungeon.level.passable[leapPos+i]){
                            bouncepos = leapPos+i;
                        }
                    }
                    if (bouncepos == -1) {
                        leapPos = -1;
                        return true;
                    } else {
                        endPos = bouncepos;
                    }
                } else {
                    endPos = leapPos;
                }

                //do leap
                sprite.visible = Dungeon.level.heroFOV[pos] || Dungeon.level.heroFOV[leapPos] || Dungeon.level.heroFOV[endPos];
                Ballistica route = new Ballistica(leapPos, target, Ballistica.STOP_TARGET | Ballistica.STOP_SOLID);
                int cell = route.collisionPos;

                //can't occupy the same cell as another char, so move back one.
                int backTrace = route.dist-1;
                while (Actor.findChar( cell ) != null && cell != leapPos) {
                    cell = route.path.get(backTrace);
                    backTrace--;
                }

                ((DwarfGeneralSprite) sprite).leap(leapPos);
                sprite.jump(pos, leapPos, 4, 1f,new Callback() {
                    @Override
                    public void call() {


                        if (leapVictim != null && alignment != leapVictim.alignment){

                            sprite.attack(leapPos);

                            for (int i : PathFinder.NEIGHBOURS8) {
                                if (!Dungeon.level.solid[endPos + i]) {
                                    CellEmitter.get(endPos + i).burst(ElmoParticle.FACTORY, 5);
                                    if (Dungeon.level.water[endPos + i]) {
                                        GameScene.add(Blob.seed(endPos + i, 2, BrokenArmorFire.class));
                                    } else {
                                        GameScene.add(Blob.seed(endPos + i, 8, BrokenArmorFire.class));
                                    }
                                }
                            }

                            enemy.damage(damageRoll(), new DM100.LightningBolt());
                            Buff.affect(enemy, Chill.class, 10f);
                            yell(Messages.get(DwarfGeneral.class, "spear_warn"));
                        }

                        Char ch = hero;
                        if(hero != null){
                            if (!ch.isAlive()) {
                                Dungeon.fail( getClass() );
                                GLog.n( Messages.get(DwarfGeneral.class, "kill"),Dungeon.hero.name() );
                            }
                        }

                        if (endPos != leapPos){
                            Actor.addDelayed(new Pushing(DwarfGeneral.this, leapPos, endPos), -1);
                        }

                        pos = endPos;
                        leapPos = -1;

                        Dungeon.level.occupyCell(DwarfGeneral.this);
                        next();
                    }
                });



                return false;
            }

            enemySeen = enemyInFOV;
            if (enemyInFOV && !isCharmedBy( enemy ) && canAttack( enemy )) {

                return doAttack( enemy );

            } else {

                if (enemyInFOV) {
                    target = enemy.pos;
                } else if (enemy == null) {
                    state = WANDERING;
                    target = Dungeon.level.randomDestination( DwarfGeneral.this );
                    return true;
                }


                if (leapCooldown <= 0 && enemyInFOV && !rooted
                        && Dungeon.level.distance(pos, enemy.pos) >= 3 && phase>=1 && jumpCooldown > 15) {

                    int targetPos = enemy.pos;
                    if (lastEnemyPos != enemy.pos) {
                        int closestIdx = 0;
                        for (int i = 1; i < PathFinder.CIRCLE8.length; i++) {
                            if (Dungeon.level.trueDistance(lastEnemyPos, enemy.pos + PathFinder.CIRCLE8[i])
                                    < Dungeon.level.trueDistance(lastEnemyPos, enemy.pos + PathFinder.CIRCLE8[closestIdx])) {
                                closestIdx = i;
                            }
                        }
                        targetPos = enemy.pos + PathFinder.CIRCLE8[(closestIdx + 4) % 8];
                    }

                    Ballistica b = new Ballistica(pos, targetPos, Ballistica.STOP_TARGET | Ballistica.STOP_SOLID);
                    //try aiming directly at hero if aiming near them doesn't work
                    if (b.collisionPos != targetPos && targetPos != enemy.pos) {
                        targetPos = enemy.pos;
                        b = new Ballistica(pos, targetPos, Ballistica.STOP_TARGET | Ballistica.STOP_SOLID);
                    }
                    if (b.collisionPos == targetPos) {
                        //get ready to leap
                        leapPos = targetPos;
                        //don't want to overly punish players with slow move or attack speed
                        spend(GameMath.gate(TICK, enemy.cooldown(), 3 * TICK));
                        if (Dungeon.level.heroFOV[pos] || Dungeon.level.heroFOV[leapPos]) {
                            yell(Messages.get(DwarfGeneral.this, "leap"));
                            sprite.parent.addToBack(new TargetedCell(leapPos, 0xFF0000));
                            ((DwarfGeneralSprite) sprite).leap(leapPos);
                            jumpCooldown = 0;
                            hero.interrupt();
                        }
                        return true;
                    }
                }

                int oldPos = pos;
                if (target != -1 && getCloser( target )) {

                    spend( 1 / speed() );
                    return moveSprite( oldPos,  pos );

                } else {
                    spend( TICK );
                    if (!enemyInFOV) {
                        sprite.showLost();
                        state = WANDERING;
                        target = Dungeon.level.randomDestination( DwarfGeneral.this );
                    }
                    return true;
                }
            }
        }

    }

    @Override
    public void die( Object cause ) {

        super.die( cause );

        Dungeon.level.unseal();

        GameScene.bossSlain();
        Dungeon.level.drop( new BlackKey( Dungeon.depth ).quantity(2), pos ).sprite.drop();
        Dungeon.level.drop( new CrystalKey( Dungeon.depth ).quantity(2), pos ).sprite.drop();
        Dungeon.level.drop( new SkeletonKey( Dungeon.depth ).quantity(1), pos ).sprite.drop();
        Dungeon.level.drop( new GoldenKey( Dungeon.depth ).quantity(1), pos ).sprite.drop();

        Badges.validateBossSlain();

        PaswordBadges.UNLOCK_KING();

        if(!SPDSettings.KillDwarf()) {
            DwarfGeneralNTNPC boss = new DwarfGeneralNTNPC();
            boss.pos = 367;
            GameScene.add(boss);
            yell( Messages.get(this, "defeated") );
            Item w = new KingAxe();
            w.level(Random.Int(5));
            Dungeon.level.drop(w, pos).sprite.drop();

        } else {
            Statistics.dwarfKill = true;
            yell( Messages.get(this, "died",hero.name()) );
        }

        Dungeon.level.drop(new KingsCrown(), pos).sprite.drop();


        for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])){
            if (mob instanceof Warlock || mob instanceof Monk ||
                    mob instanceof DwarfSolider || mob instanceof DwarfFuze) {
                mob.die( cause );
            }
        }
    }


}
