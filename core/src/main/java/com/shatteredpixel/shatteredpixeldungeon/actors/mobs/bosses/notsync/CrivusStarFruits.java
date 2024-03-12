package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.notsync;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;
import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.level;
import static com.shatteredpixel.shatteredpixeldungeon.Statistics.crivusfruitslevel2;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Boss;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ConfusionGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.CorrosiveGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Amok;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AscensionChallenge;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Dread;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LockedFloor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ShieldBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Sleep;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.ClearElemental;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.CrivusFruits;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.items.IceCyanBlueSquareCoin;
import com.shatteredpixel.shatteredpixeldungeon.items.Torch;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.DriedRose;
import com.shatteredpixel.shatteredpixeldungeon.items.food.CrivusFruitsFood;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.CrystalKey;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.IronKey;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfPurity;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.CrivusFruitsFlake;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.LifeTreeSword;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.AlarmTrap;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CrivusStarFruitsSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class CrivusStarFruits extends Boss implements Hero.Doom {

    {
        spriteClass = CrivusStarFruitsSprite.class;

        HP = HT = 280;

        defenseSkill = 14;
        state = WANDERING;
        EXP = 20;

        properties.add(Property.IMMOVABLE);
        properties.add(Property.BOSS);
    }

    {
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

    @Override
    public void damage(int dmg, Object src) {
        if(!crivusfruitslevel2 && HP<160){
            HP = 160;
            return;
        }

        if(crivusfruitslevel2){
            float scaleFactor = AscensionChallenge.statModifier(this);
            int scaledDmg = Math.round(dmg/scaleFactor);
            if (scaledDmg >= 5){
                scaledDmg = 4 + (int)(Math.sqrt(8*(scaledDmg - 4) + 1) - 1)/2;
            }
            dmg = (int)(scaledDmg*AscensionChallenge.statModifier(this));
        }

        super.damage(dmg, src);
    }

    @Override
    public boolean isInvulnerable(Class effect) {
        return this.HP > 161 && effect != CrivusStarFruits.DiedDamager.class;
    }

    @Override
    public void die(Object cause) {
        super.die(cause);
        boolean heroKilled = false;
        for (int i = 0; i < PathFinder.NEIGHBOURS49.length; i++) {
            Char ch = findChar( pos + PathFinder.NEIGHBOURS49[i] );
            if (ch != null && ch.isAlive()) {
                int damage = Math.round(Random.NormalIntRange(12, 21));
                damage = Math.round( damage * AscensionChallenge.statModifier(this));
                //armor is 2x effective against bone explosion
                damage = Math.max( 0,  damage - (ch.drRoll() + ch.drRoll()) );
                ch.damage( damage, this );
                if (ch == Dungeon.hero && !ch.isAlive()) {
                    heroKilled = true;
                }
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
        Dungeon.level.drop( new CrystalKey( Dungeon.depth ), pos-1 ).sprite.drop();
        Dungeon.level.drop( new CrystalKey( Dungeon.depth ), pos+1 ).sprite.drop();
        Dungeon.level.drop( new IronKey( Dungeon.depth ), pos-2 ).sprite.drop();
        Dungeon.level.drop( new IronKey( Dungeon.depth ), pos+2 ).sprite.drop();
        Badges.validateBossSlain();
        Statistics.bossScores[0] += 2000;
        Badges.KILL_ST();
        Dungeon.level.drop( new LifeTreeSword(), pos ).sprite.drop();
        if(!Statistics.bossRushMode) {
            Dungeon.level.drop(new IceCyanBlueSquareCoin(15),pos);
            Dungeon.level.drop(new Gold(200),pos);
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
        for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])){
            if (	mob instanceof ClearElemental ||
                    mob instanceof CrivusStarFruitsLasher ) {
                mob.die( cause );
            }
        }
      if(Statistics.bossRushMode){
            GetBossLoot();
        }
    }

    //无敌也要扣减！
    public static class DiedDamager extends Buff {

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
            //遍历楼层生物，寻找 CrivusStarFruits执行扣血，在触手死亡时强制扣除本体CrivusStarFruits
            if(!Statistics.crivusfruitslevel3){
                for (Mob m : level.mobs.toArray(new Mob[0])){
                    if (m instanceof CrivusStarFruits){
                        m.damage(10, this);
                    }
                }
            }

        }
    }

    @Override
    protected boolean act() {

        if (HP > 60 && Statistics.crivusfruitslevel3){
            if(enemy!=null && enemy == hero){
                yell(Messages.get(this,"goodbye"));
                enemy.damage(enemy.HT/3,this);
                spend(16f);
            } else {
                this.damage(2,this);
            }

        }

        alerted = false;
        super.act();
        state = PASSIVE;
        if (alignment == Alignment.NEUTRAL) {
            return true;
        }
        if( hero.buff(LockedFloor.class) != null){
            notice();
        }

        GameScene.add(Blob.seed(pos, Statistics.crivusfruitslevel2 ? 0 : 20,  ConfusionGas.class));

        return super.act();
    }
    @Override
    public void beckon(int cell) {
        //do nothing
    }
    protected float focusCooldown = 0;
    public int count = Random.NormalIntRange(1,2);
    protected boolean hasRaged = false;
    private static final String NUM_CONTS = "numConts";
    private static final String HAS_RAGED = "has_raged";

    private static String FOCUS_COOLDOWN = "focus_cooldown";
    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(NUM_CONTS, count);
        bundle.put(HAS_RAGED, hasRaged);
        bundle.put( FOCUS_COOLDOWN, focusCooldown );
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        count = bundle.getInt(NUM_CONTS);
        hasRaged = bundle.getBoolean(HAS_RAGED);
        focusCooldown = bundle.getInt( FOCUS_COOLDOWN );
        if (state != SLEEPING) BossHealthBar.assignBoss(this);
        if ((HP*2 <= HT)) BossHealthBar.bleed(true);

    }
    @Override
    public synchronized boolean isAlive() {
        if (super.isAlive()){
            return true;
        } else {
            if (!hasRaged){
                triggerEnrage();
                Statistics.crivusfruitslevel3 = true;
            }
            return !buffs(Rage.class).isEmpty() || !Statistics.crivusfruitslevel3;
        }
    }

    protected void triggerEnrage(){
        Buff.affect(this, Rage.class).setShield(HT/2 + 40);
        if (Dungeon.level.heroFOV[pos]) {
            sprite.showStatus( CharSprite.NEGATIVE, "!!!" );
            AlarmTrap trap = new AlarmTrap();
            trap.pos = super.pos;
            trap.activate();
            ScrollOfTeleportation.teleportToLocation(this, 577);
            GLog.n(Messages.get(this, "died"));
        }
        spend( TICK );
        hasRaged = true;
    }

    public static class Rage extends ShieldBuff {

        {
            type = buffType.POSITIVE;
        }

        @Override
        public boolean act() {

            if (target.HP > 60){
                detach();
                return true;
            }

            absorbDamage( 0);

            if (shielding() <= 0){
                target.die(null);
            }

            spend( TICK );

            return true;
        }

        @Override
        public int icon () {
            return BuffIndicator.HEX;
        }

        @Override
        public void tintIcon(Image icon) {
            icon.hardlight(Window.RED_COLOR);
        }

        @Override
        public String toString () {
            return Messages.get(this, "name");
        }

        @Override
        public String desc () {
            return Messages.get(this, "desc", shielding());
        }

        {
            immunities.add(Terror.class);
        }
    }

    @Override
    public int defenseProc(Char enemy, int damage) {
        GameScene.add(Blob.seed(pos, 40, CrivusFruits.DiedBlobs.class));
        return super.defenseProc(enemy, damage);
    }

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
    public void onDeath() {

        if( hasRaged ) {
            GLog.n( Messages.get(this, "ondeath") );
        }

        Dungeon.fail( getClass() );
    }

}
