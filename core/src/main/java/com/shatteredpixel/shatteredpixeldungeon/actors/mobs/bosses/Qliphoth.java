package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;
import static com.shatteredpixel.shatteredpixeldungeon.levels.ForestPoisonBossLevel.ForestBoss2_LasherPos;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Boss;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Electricity;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Freezing;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AscensionChallenge;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.BlobImmunity;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LockedFloor;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.ClearElemental;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.PhantomPiranha;
import com.shatteredpixel.shatteredpixeldungeon.effects.BlobEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ShaftParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.IceCyanBlueSquareCoin;
import com.shatteredpixel.shatteredpixeldungeon.items.Torch;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.DriedRose;
import com.shatteredpixel.shatteredpixeldungeon.items.food.CrivusFruitsFood;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Food;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.CrystalKey;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfPurity;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.CrivusFruitsFlake;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.LifeTreeSword;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.FishingSpear;
import com.shatteredpixel.shatteredpixeldungeon.levels.RegularLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MissileSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.QliphothSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class Qliphoth extends Boss {

    public int state_boss;

    public int state_two_phase;

    public int boss_teleport;

    //被击杀的藤蔓数量，在藤蔓死亡时++
    public int amount = 0;

    {
        initProperty();
        initBaseStatus(8, 13, 33, 8, 150, 0, 3);
        initStatus(20);
        defenseSkill = 8;

        spriteClass = QliphothSprite.class;

        properties.add(Property.BOSS);
        properties.add(Property.ACIDIC);

        SLEEPING = new Sleeping();
        WANDERING = new Wandering();
        HUNTING = new Hunting();

        state = WANDERING;
        alignment = Alignment.NEUTRAL;
    }

    @Override
    public void damage(int dmg, Object src, DamageType type) {
        LockedFloor lock = hero.buff(LockedFloor.class);
        if (lock != null) lock.addTime(dmg * 2);

        if(state_boss>=2){
            float scaleFactor = AscensionChallenge.statModifier(this);
            int scaledDmg = Math.round(dmg/scaleFactor);
            if (scaledDmg >= 5){
                //takes 5/6/7/8/9/10 dmg at 5/7/10/14/19/25 incoming dmg
                scaledDmg = 4 + (int)(Math.sqrt(8*(scaledDmg - 4) + 1) - 1)/2;
            }
            dmg = (int)(scaledDmg*AscensionChallenge.statModifier(this));
        }

        super.damage(dmg, src, type);
    }

    public static int[] SuperAttack_Pos = new int[]{
            772,650,283,438
    };

    public static int[] Ling_Pos = new int[]{
            650,438
    };


    @Override
    public boolean isAlive() {
        return super.isAlive() || state_boss<2;
    }

    private boolean onlyFish;
    @Override
    protected boolean act() {

        for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])){
            onlyFish = mob instanceof PhantomPiranha && mob.alignment == Alignment.ENEMY;
        }

        boolean ST1 = amount == 4;

        if(state_boss >=2){
            GameScene.add(Blob.seed(pos,100, CrivusFruits.DiedBlobs.class));
        } else {
            GameScene.add(Blob.seed(pos,120, CrivusFruits.DiedBlobs.class));
        }

        for (int i : SuperAttack_Pos) {
            GameScene.add(Blob.seed(i,10, Invisibility_Fogs.class));
        }

        if(state_boss==0 && HP == 150 && ST1){
            HP = 100;
            state_boss++;
            summon_Lasher();
            GLog.n(Messages.get(this,"summon"));
            for (int i : SuperAttack_Pos) {
                MagicMissile.boltFromChar(sprite.parent,
                        MagicMissile.SHAMAN_PURPLE,
                        new MissileSprite(),
                        i,
                        () -> {
                            if(!(Dungeon.isChallenged(Challenges.STRONGER_BOSSES))) {
                                Dungeon.level.drop(new FishingSpear(), i);
                            }
                            Dungeon.level.drop(new FishingSpear(),i);
                        });
            }
        } else if(state_boss == 1 && amount>7){
            state_boss++;
            HP = 60;

            alignment = Alignment.ENEMY;

            Sample.INSTANCE.play( Assets.Sounds.CHALLENGE );
            this.sprite.showStatus(CharSprite.NEGATIVE, "!!!");
            GameScene.flash(0x80cc0000);
            GLog.n(Messages.get(this,"an_argy"));
            for (int i : Ling_Pos) {
                MagicMissile.boltFromChar(sprite.parent,
                        MagicMissile.SHAMAN_BLUE,
                        new MissileSprite(),
                        i,
                        () -> {
                            if(!(Dungeon.isChallenged(Challenges.STRONGER_BOSSES))) {
                                Dungeon.level.drop(new PotionOfPurity.PotionOfPurityLing(),i);
                            } else {
                                int randomPos;
                                if(Random.Float()>0.5f){
                                    randomPos = 650;
                                } else {
                                    randomPos = 438;
                                }
                                Dungeon.level.drop(new PotionOfPurity.PotionOfPurityLing(),randomPos);
                            }
                        });
            }

            if(Dungeon.isChallenged(Challenges.STRONGER_BOSSES)) {
                for (int iz : SC) {

                    for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
                        if (mob instanceof Qliphoth) {
                            MagicMissile.boltFromChar(mob.sprite.parent,
                                    MagicMissile.HALOFIRE,
                                    new MissileSprite(),
                                    iz,
                                    () -> {
                                        PhantomPiranha csp = new PhantomPiranha();
                                        csp.HT = csp.HP = 30;
                                        csp.state = csp.WANDERING;
                                        csp.pos = iz;
                                        GameScene.add(csp);
                                    });
                        }
                    }

                }
            }
        }
        return super.act();
    }

    public static int[] CS = new int[]{
            541,547
    };

    public static int[] SC = new int[]{
            408,680
    };

    private void summon_Lasher(){
        for (int i : ForestBoss2_LasherPos) {
            QliphothLasher qliphothLasher = new QliphothLasher();
            qliphothLasher.HT = qliphothLasher.HP = 20;
            Buff.affect(qliphothLasher,Qliphoth.Lasher_Damage.class);
            for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
                if (mob instanceof Qliphoth) {
                    MagicMissile.boltFromChar(mob.sprite.parent,
                            MagicMissile.HALOFIRE,
                            new MissileSprite(),
                            i,
                            () -> {
                                qliphothLasher.pos = i;
                                GameScene.add(qliphothLasher);
                                qliphothLasher.state_lasher_boss = 1;
                            });
                }
            }
        }

        if(Dungeon.isChallenged(Challenges.STRONGER_BOSSES)) {
            for (int iz : CS) {

                for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
                    if (mob instanceof Qliphoth) {
                        MagicMissile.boltFromChar(mob.sprite.parent,
                                MagicMissile.HALOFIRE,
                                new MissileSprite(),
                                iz,
                                () -> {
                                    ClearElemental csp = new ClearElemental();
                                    csp.HT = csp.HP = 30;
                                    Buff.affect(csp, CrivusFruits.CFBarrior.class).setShield(30);
                                    csp.state = csp.WANDERING;
                                    csp.pos = iz;
                                    GameScene.add(csp);
                                });
                    }
                }

            }
        }
        Sample.INSTANCE.play( Assets.Sounds.CHALLENGE );
        this.sprite.showStatus(CharSprite.NEGATIVE, "!!!");
        GameScene.flash(0x808c8c8c);
    }

    @Override
    public boolean isInvulnerable(Class effect) {
        boolean invulnerable;
        if(state_boss < 2){
            invulnerable = true;
        } else {
            invulnerable = onlyFish;
        }
        return invulnerable;
    }

    @Override
    public boolean reset() {
        return true;
    }

    public static class Lasher_Damage extends Buff {

        @Override
        public boolean act() {
            if (target.alignment != Alignment.ENEMY){
                detach();
            }
            spend( TICK );
            return true;
        }

        @Override
        public void detach() {
            super.detach();
            for (Mob m : Dungeon.level.mobs.toArray(new Mob[0])){
                if (m instanceof Qliphoth){
                    m.damage(10, this, DamageType.REAL);
                }
            }
        }
    }

    @Override
    protected boolean getCloser( int target ) {

        if (rooted) {
            return false;
        }

        int step = Dungeon.findStep( this, target, Dungeon.level.water, fieldOfView, true );
        if (step != -1) {
            move( step );
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected boolean getFurther( int target ) {
        int step = Dungeon.flee( this, target, Dungeon.level.water, fieldOfView, true );
        if (step != -1) {
            move( step );
            return true;
        } else {
            return false;
        }
    }

    {
        for (Class c : new BlobImmunity().immunities()){
            if (c != Electricity.class && c != Freezing.class){
                immunities.add(c);
            }
        }
        immunities.add( Burning.class );
    }

    //if there is not a path to the enemy, piranhas act as if they can't see them
    private class Sleeping extends Mob.Sleeping{
        @Override
        public boolean act(boolean enemyInFOV, boolean justAlerted) {
            if (enemyInFOV) {
                PathFinder.buildDistanceMap(enemy.pos, Dungeon.level.water, viewDistance);
                enemyInFOV = PathFinder.distance[pos] != Integer.MAX_VALUE;
            }

            return super.act(enemyInFOV, justAlerted);
        }
    }

    private class Wandering extends Mob.Wandering{
        @Override
        public boolean act(boolean enemyInFOV, boolean justAlerted) {
            if (enemyInFOV) {
                PathFinder.buildDistanceMap(enemy.pos, Dungeon.level.water, viewDistance);
                enemyInFOV = PathFinder.distance[pos] != Integer.MAX_VALUE;
            }

            return super.act(enemyInFOV, justAlerted);
        }
    }

    private class Hunting extends Mob.Hunting{

        @Override
        public boolean act(boolean enemyInFOV, boolean justAlerted) {
            if (enemyInFOV && !(Dungeon.isChallenged(Challenges.STRONGER_BOSSES))) {
                PathFinder.buildDistanceMap(enemy.pos, Dungeon.level.water, viewDistance);
                enemyInFOV = PathFinder.distance[pos] != Integer.MAX_VALUE;
            } else {

            }
            return super.act(enemyInFOV,justAlerted);
        }
    }

    @Override
    public void notice() {
        super.notice();
        if (!BossHealthBar.isAssigned()) {
            BossHealthBar.assignBoss(this);

            GLog.n(Messages.get(this, "notice"));
            GameScene.flash(0x8000cc00);
            Camera.main.shake(1f,3f);
            this.sprite.showStatus(CharSprite.NEGATIVE, "!!!");
            GameScene.bossReady();
            for (Char ch : Actor.chars()){
                if (ch instanceof DriedRose.GhostHero){
                    ((DriedRose.GhostHero) ch).sayBoss();
                }
            }
        }
    }

    @Override
    public void die(Object cause) {
        super.die(cause);
        boolean heroKilled = false;

        if(Dungeon.isChallenged(Challenges.STRONGER_BOSSES) && !SprintableModeBoolean){
            Dungeon.level.drop(new IceCyanBlueSquareCoin(10),pos);
        }

        for (int i = 0; i < PathFinder.NEIGHBOURS49.length; i++) {
            Char ch = findChar( pos + PathFinder.NEIGHBOURS49[i] );
            if (ch != null && ch.isAlive()) {
                int damage = Math.round(Random.NormalIntRange(8, 14));
                damage = Math.round( damage * AscensionChallenge.statModifier(this));
                damage = Math.max( 0,  damage - (ch.drRoll() + ch.drRoll()) );
                ch.damage( damage, this );
                if (ch == Dungeon.hero && !ch.isAlive()) {
                    heroKilled = true;
                }
            }
        }

        for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])){
            if (mob instanceof PhantomPiranha) {
                mob.die( cause );
            }
        }

        if (heroKilled) {
            Dungeon.fail( this );
            GLog.n( Messages.get(this, "explo_kill") );
        }

        if (Dungeon.level.heroFOV[pos]) {
            Sample.INSTANCE.play( Assets.Sounds.BONES );
        }

        PotionOfPurity.PotionOfPurityLing potionOfPurityLing = Dungeon.hero.belongings.getItem(PotionOfPurity.PotionOfPurityLing.class);
        if(potionOfPurityLing != null){
            potionOfPurityLing.detachAll( hero.belongings.backpack );
        }

        Dungeon.level.unseal();
        GameScene.bossSlain();
        GLog.w(Messages.get(this,"dead"));

        int pos = 610;

        Dungeon.level.drop( new CrystalKey( Dungeon.depth ), pos ).sprite.drop();
        Badges.validateBossSlain();
        Statistics.bossScores[0] += 2000;
        Badges.KILLSAPPLE();

        ScrollOfTeleportation.appear(hero, 676);

        if (!Badges.isUnlocked(Badges.Badge.KILL_APPLE)){
            Dungeon.level.drop( new LifeTreeSword(), pos ).sprite.drop();
        } else if (Random.Float()<0.65f || SPDSettings.BossWeaponCount1() >= 3) {
            SPDSettings.BossWeaponCount1(SPDSettings.BossWeaponCount1() + 1);
            Dungeon.level.drop( new LifeTreeSword(), pos ).sprite.drop();
            if(SPDSettings.BossWeaponCount1() >= 3){
                SPDSettings.BossWeaponCount1(0);
                GLog.w(Messages.get(CrivusFruits.class,"weapon"));
            }
        } else {
            Dungeon.level.drop( new Food(), pos ).sprite.drop();
        }

        boolean holiday = RegularLevel.chinaHoliday == RegularLevel.ChinaHoliday.PQJ;

        if(!Statistics.bossRushMode) {
            int normal = ((hero.lvl + Dungeon.depth)/5) * (Math.max(Challenges.activeChallenges(), 5));
            int count = holiday ? 2 : 1;
            Dungeon.level.drop(new IceCyanBlueSquareCoin(normal * count),643);
        }

        Dungeon.level.drop(Generator.randomUsingDefaults(Generator.Category.FOOD),pos);
        Dungeon.level.drop(Generator.randomUsingDefaults(Generator.Category.FOOD),pos);

        int blobs = Random.chances(new float[]{0, 0, 6, 3, 1});
        for (int i = 0; i < blobs; i++){
            int ofs;
            do {
                ofs = PathFinder.NEIGHBOURS8[Random.Int(6)];
            } while (!Dungeon.level.passable[pos + ofs]);
            Dungeon.level.drop( new CrivusFruitsFood(), pos + ofs ).sprite.drop( pos );
        }

        if(Statistics.lanterfireactive){
            Dungeon.level.drop( new Torch(), pos ).sprite.drop();
        }

        int flakes = Random.chances(new float[]{0, 0, 6, 3, 1});
        for (int i = 0; i < flakes; i++){
            int ofs;
            do {
                ofs = PathFinder.NEIGHBOURS8[Random.Int(6)];
            } while (!Dungeon.level.passable[pos + ofs]);
            Dungeon.level.drop( new CrivusFruitsFlake(), pos + ofs ).sprite.drop( pos );
        }

        if(Statistics.bossRushMode){
            GetBossLoot(pos);
        }
    }

    private static final String STATE_BOSS     = "state_lasher_boss";
    private static final String TELEPORT_BOSS  = "teleport_boss";
    private static final String STATE_TWO_BOSS = "state_two_phase";
    private static final String AMOUNT = "amount";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(STATE_TWO_BOSS, state_two_phase);
        bundle.put(STATE_BOSS, state_boss);
        bundle.put(TELEPORT_BOSS,boss_teleport);
        bundle.put(AMOUNT,amount);
    }


    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        bundle.put(STATE_TWO_BOSS, state_two_phase);
        state_boss = bundle.getInt(STATE_BOSS);
        boss_teleport = bundle.getInt(TELEPORT_BOSS);
        if (state != SLEEPING) BossHealthBar.assignBoss(this);
        if ((HP*2 <= HT)) BossHealthBar.bleed(true);
        if(state_boss == 2){
            alignment = Alignment.ENEMY;
        } else {
            alignment = Alignment.NEUTRAL;
        }
        amount = bundle.getInt(AMOUNT);
    }

    private void pullEnemy( Char enemy, int pullPos ){
        enemy.pos = pullPos;
        enemy.sprite.place(pullPos);
        Dungeon.level.occupyCell(enemy);
        Cripple.prolong(enemy, Cripple.class, 4f);
        if (enemy == Dungeon.hero) {
            Dungeon.hero.interrupt();
            Dungeon.observe();
            GameScene.updateFog();
        }
    }


    @Override
    public String description() {
        String desc = super.description();
        if (onlyFish) {
            desc += "\n\n" + Messages.get(this, "onlyFish");
        }
        return desc;
    }

    public static class Invisibility_Fogs extends Blob implements Hero.Doom {

        @Override
        public String tileDesc() {
            return Messages.get(this, hero.buff(LockedFloor.class) != null? "desc" : "csed" );
        }
        @Override
        protected void evolve() {
            super.evolve();

            int[] map = Dungeon.level.map;

            boolean seen = false;

            int cell;

            for (int i = area.left; i < area.right; i++) {
                for (int j = area.top; j < area.bottom; j++) {
                    cell = i + j*Dungeon.level.width();
                    if (cur[cell] > 0) {

                        off[cell] = cur[cell];
                        volume += off[cell];

                        if (map[cell] == Terrain.EMBERS) {
                            map[cell] = Terrain.GRASS;
                            GameScene.updateMap(cell);
                        }

                        seen = seen || Dungeon.level.visited[cell];

                    } else {
                        off[cell] = 0;
                    }
                }
            }

            Hero hero = Dungeon.hero;
            if (hero.isAlive() && cur[hero.pos] > 0 && hero.invisible != 1) {
                Buff.prolong(hero, Invisibility.class, 2f);
            }
        }

        @Override
        public void use( BlobEmitter emitter ) {
            super.use( emitter );
            emitter.start( ShaftParticle.FACTORY, 0.9f, 0 );
        }

        @Override
        public void onDeath() {}
    }



}
