package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.PaswordBadges;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Boss;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Amok;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ArcaneArmor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Corruption;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Degrade;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FireImbue;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.HaloFireImBlue;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Haste;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Healing;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LifeLink;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LockedFloor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Poison;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.RoseShiled;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.TestDwarfMasterLock;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vulnerable;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.DM100;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.DM200;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.DM201;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Eye;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.FungalSpinner;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Ghoul;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.GnollGuard;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.GnollShiled;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Monk;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Necromancer;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.OGPDZSLS;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.RedMurderer;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.SRPDHBLR;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Skeleton;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Tengu;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Warlock;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.YogReal;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.BallisticaReal;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.timing.VirtualActor;
import com.shatteredpixel.shatteredpixeldungeon.effects.Beam;
import com.shatteredpixel.shatteredpixeldungeon.effects.BeamCustom;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Effects;
import com.shatteredpixel.shatteredpixeldungeon.effects.Flare;
import com.shatteredpixel.shatteredpixeldungeon.effects.FloatingText;
import com.shatteredpixel.shatteredpixeldungeon.effects.Pushing;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BlastParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ElmoParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.EnergyParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.HalomethaneFlameParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.PurpleParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ScanningBeam;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ShadowParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SmokeParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.Viscosity;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.DriedRose;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Bomb;
import com.shatteredpixel.shatteredpixeldungeon.items.food.MeatPie;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.BlackKey;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.CrystalKey;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.GoldenKey;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.IronKey;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.SkeletonKey;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.levels.DwarfMasterBossLevel;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DiedMonkSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MissileSprite;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.BArray;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

import java.util.ArrayList;
import java.util.HashSet;

public class DwarfMaster extends Boss {

    {
        initProperty();
        initBaseStatus(3, 8, 14, 10, 800, 0, 2);
        initStatus(20);

        properties.add(Property.BOSS);
        properties.add(Property.FIERY);
        properties.add(Property.DEMONIC);
        properties.add(Property.ACIDIC);
        spriteClass = DiedMonkSprite.class;

    }

    private int beamCD = 23;
    private float[] skillBalance = new float[]{100f, 100f, 100f};
    private int summonsMade = 0;

    public static class YogScanHalf extends Buff implements ScanningBeam.OnCollide{
        private int left = 5;
        //00:x- 01:x+ 10:y- 11:y+
        private int direction = 0;
        private int center = -3;

        public DwarfMaster.YogScanHalf setPos(int c, int d){
            this.center = c;
            this.direction = d;
            return this;
        }

        @Override
        public void storeInBundle(Bundle b){
            super.storeInBundle(b);
            b.put("centerPos", center);
            b.put("fourDirections", direction);
            b.put("leftTime", left);
        }

        @Override
        public void restoreFromBundle(Bundle b){
            super.restoreFromBundle(b);
            center = b.getInt("centerPos");
            direction = b.getInt("fourDirections");
            left = b.getInt("leftTime");
        }

        @Override
        public boolean act(){
            spend(TICK);

            if(left > 0){
                renderWarning((direction & 2) == 0, (direction & 1) != 0);
                --left;
            }else {
                renderSkill((direction & 2) == 0, (direction & 1) != 0);
                diactivate();
            }

            return true;
        }
        //warning
        protected void renderWarning(boolean isx, boolean positive){
            int w = Dungeon.level.width();
            int h = Dungeon.level.height();
            int xOfs = center % w;
            int yOfs = center / w;
            int startX; int startY;
            int endX; int endY;
            if(isx){
                startX = xOfs + (5 - left) * (positive ? 1: -1) * 2;
                endX = startX;
                startY = 1;
                endY = h - 1;
            }else{
                startY = yOfs + (5 - left) * (positive ? 1: -1) * 2;
                endY = startY;
                startX = 1;
                endX = w - 1;
            }
            target.sprite.parent.add(new BeamCustom(
                    new PointF(startX, startY).offset(0.5f, 0.5f).scale(DungeonTilemap.SIZE),
                    new PointF(endX, endY).offset(0.5f, 0.5f).scale(DungeonTilemap.SIZE),
                    Effects.Type.BLUE_RAY)
                    .setLifespan(0.7f).setColor(0x00ffff)
            );
        }
        //damage
        protected void renderSkill(boolean isx, boolean positive){
            int w = Dungeon.level.width();
            int xOfs = center % w;
            int yOfs = center / w;
            float startX; float startY;
            float xsp = 0; float ysp = 0;
            float ang;
            float r;
            if(isx){
                startX = xOfs;
                startY = 3;
                xsp = 10f * (positive ? 1f : -1f);
                ang = 90f;
                r = w - 6;
            }else{
                startY = yOfs;
                startX = 3;
                ysp = 10f * (positive ? 1f : -1f);
                ang = 0f;
                r = Dungeon.level.height() - 6;
            }

            ScanningBeam.setCollide(this);
            target.sprite.parent.add(new ScanningBeam(Effects.Type.BLUE_RAY, BallisticaReal.STOP_TARGET,
                            new ScanningBeam.BeamData()
                                    .setPosition(startX+0.8f, startY + 0.8f, ang, r)
                                    .setSpeed(xsp, ysp, 0f)
                                    .setTime(0.32f, 2.5f, 0.5f)
                    ).setDiameter(3f)
            );
            VirtualActor.delay(1.8f, ()->{
                detach();
                Camera.main.shake(2f, 0.3f);
            });

            Camera.main.shake(2f, 100f);

        }

        @Override
        public int onHitProc(Char ch) {
            if(ch.alignment == Alignment.ENEMY) return 0;
            ch.damage( Random.Int(50, 80), YogReal.class );
            Statistics.bossScores[3] -= 500;
            if(ch == Dungeon.hero){
                Sample.INSTANCE.play(Assets.Sounds.BLAST, Random.Float(1.1f, 1.5f));
                Buff.affect( ch, Poison.class ).set( (5f)+(ch.HP/8f) );
                if(!ch.isAlive()) Dungeon.fail(getClass());
            }
            ch.sprite.centerEmitter().burst( PurpleParticle.BURST, Random.IntRange( 15, 10 ) );ch.sprite.flash();
            return 1;
        }

        @Override
        public int cellProc(int i) {
            if(Dungeon.level.flamable[i]){
                Dungeon.level.destroy(i);
                GameScene.updateMap( i );
            }
            return 0;
        }
    }

    //Dead Laser Ray
    private void actScanning(){
        if(phase>0) {
            --beamCD;
            if (beamCD <= 0) {
                Buff.detach(this, DwarfMaster.YogScanHalf.class);
                int skill = Random.chances(skillBalance);
                if (skill == 0) {
                    Char enemy = (this.enemy == null ? Dungeon.hero : this.enemy);
                    int w = Dungeon.level.width();
                    int dx = enemy.pos % w - pos % w;
                    int dy = enemy.pos / w - pos / w;
                    int direction = 2 * (Math.abs(dx) > Math.abs(dy) ? 0 : 1);
                    direction += (direction > 0 ? (dy > 0 ? 1 : 0) : (dx > 0 ? 1 : 0));
                    Buff.affect(this, DwarfMaster.YogScanHalf.class).setPos(pos, direction);
                    skillBalance[skill] /= 1.75f;
                    beamCD = 20 + 8 - (phase == 5 ? 19 : 0);
                    Buff.affect(this, Healing.class).setHeal(5, 0f, 6);
                    sprite.showStatus(0xff0000, Messages.get(this, "dead"));
                } else {
                    Buff.affect(this, YogReal.YogScanRound.class).setPos(pos);
                    skillBalance[skill] /= 2f;
                    Buff.affect(this, Healing.class).setHeal(5, 0f, 6);
                    beamCD = 20 + 10 - (phase == 5?19:0);
                    sprite.showStatus(0x00ff00, Messages.get(this, "life"));

                }
            }
        }
    }



    private float summonCooldown = 0;
    private float abilityCooldown = 6;
    private static final int MIN_COOLDOWN = 7;
    private static final int MAX_COOLDOWN = 11;

    private static float[] chanceMap = {0f, 100f, 100f, 100f, 100f, 100f, 100f};
    private int wave=0;

    private int lastAbility = 0;
    private static final int NONE = 0;
    private static final int LINK = 1;
    private static final int TELE = 2;
    private static final int ENRAGE = 3;
    private static final int DEATHRATTLE = 4;
    private static final int SACRIFICE = 5;
    private static final int SUMMON = 6;

    private int phase = 1;

    public static class KingDamager extends Buff {

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
                if (m instanceof DwarfMaster){
                    m.damage(20, this);
                }
            }
        }
    }

    private boolean summonSubject( int delay, Class<?extends Mob> type ){
        DwarfMaster.Summoning s = new DwarfMaster.Summoning();
        s.pos = ((DwarfMasterBossLevel)Dungeon.level).getSummoningPos();
        if (s.pos == -1) return false;
        s.summon = type;
        s.delay = delay;
        s.attachTo(this);
        return true;
    }

    private boolean summonSubject( int delay ){

        //4th summon is always a monk or warlock, otherwise ghoul
        //4 1n 13 are monks/warlocks
        if (summonsMade % 13 == 7 || summonsMade % 13 == 11) {
            return summonSubject(delay, DwarfMaster.DKMonk.class );
        } else if(summonsMade % 13 == 5 || summonsMade % 13 == 12) {
            return summonSubject(delay, DwarfMaster.DKWarlock.class);
        }else{
            return summonSubject(delay, DwarfMaster.DKGhoul.class);
        }

    }

    public static class Summoning extends Buff {

        private int delay;
        private int pos;
        private Class<?extends Mob> summon;

        private Emitter particles;

        public int getPos() {
            return pos;
        }

        @Override
        public boolean act() {



            delay--;
            if (delay <= 0){
                for (Buff buff : hero.buffs()) {
                    if (buff instanceof RoseShiled) {
                        buff.detach();
                        GLog.b(Messages.get(DwarfMaster.class,"no_rose"));
                    }
                    if (buff instanceof HaloFireImBlue ||buff instanceof FireImbue ) {
                        buff.detach();
                        GLog.w(Messages.get(DwarfMaster.class,"no_fire"));
                    }
                    if (buff instanceof Invisibility) {
                        buff.detach();
                        GLog.p(Messages.get(DwarfMaster.class,"no_inst"));
                    }
                }
                if (summon == DwarfMaster.DKWarlock.class){
                    particles.burst(ShadowParticle.CURSE, 10);
                    Sample.INSTANCE.play(Assets.Sounds.CURSED);
                } else if (summon == DwarfMaster.DKMonk.class){
                    particles.burst( ShadowParticle.MISSILE, 10 );
                    Sample.INSTANCE.play(Assets.Sounds.BURNING);
                } else {
                    particles.burst(EnergyParticle.FACTORY, 10);
                    Sample.INSTANCE.play(Assets.Sounds.READ);
                }
                particles = null;

                if (Actor.findChar(pos) != null){
                    ArrayList<Integer> candidates = new ArrayList<>();
                    for (int i : PathFinder.NEIGHBOURS8){
                        if (Dungeon.level.passable[pos+i] && Actor.findChar(pos+i) == null){
                            candidates.add(pos+i);
                        }
                    }
                    if (!candidates.isEmpty()){
                        pos = Random.element(candidates);
                    }
                }

                if (Actor.findChar(pos) == null) {
                    Mob m = Reflection.newInstance(summon);
                    m.pos = pos;
                    m.maxLvl = -2;
                    GameScene.add(m);
                    m.state = m.HUNTING;
                    if (((DwarfMaster)target).phase == 2){
                        Buff.affect(m, DwarfMaster.KingDamager.class);
                    }
                } else {
                    Char ch = Actor.findChar(pos);
                    ch.damage(Random.NormalIntRange(20, 40), summon);
                    if (((DwarfMaster)target).phase == 2){
                        target.damage(20, new DwarfMaster.KingDamager());
                    }
                }

                detach();
            }

            spend(TICK);
            return true;
        }

        @Override
        public void fx(boolean on) {
            if (on && particles == null) {
                particles = CellEmitter.get(pos);

                if (summon == DwarfMaster.DKWarlock.class){
                    particles.pour(ShadowParticle.UP, 0.1f);
                } else if (summon == DwarfMaster.DKMonk.class){
                    particles.pour(ElmoParticle.FACTORY, 0.1f);
                } else {
                    particles.pour(Speck.factory(Speck.RATTLE), 0.1f);
                }

            } else if (!on && particles != null) {
                particles.on = false;
            }
        }

        private static final String DELAY = "delay";
        private static final String POS = "pos";
        private static final String SUMMON = "summon";

        @Override
        public void storeInBundle(Bundle bundle) {
            super.storeInBundle(bundle);
            bundle.put(DELAY, delay);
            bundle.put(POS, pos);
            bundle.put(SUMMON, summon);
        }

        @Override
        public void restoreFromBundle(Bundle bundle) {
            super.restoreFromBundle(bundle);
            delay = bundle.getInt(DELAY);
            pos = bundle.getInt(POS);
            summon = bundle.getClass(SUMMON);
        }
    }

    private int pumpedUp = 0;

    private int surroundingWater(){
        int water = 0;
        for(int n: PathFinder.NEIGHBOURS8){
            if(Dungeon.level.water[n+pos]){
                water++;
            }
        }
        return water;
    }

    @Override
    public int damageRoll() {
        return Math.round(super.damageRoll() * (berserk()?1.4f:1f) * (pumpedUp>0?3f:1f) *(surroundingWater()>7?1.75f:1f));
    }

    @Override
    public int attackSkill( Char target ) {
        return Math.round(super.attackSkill(target)*(pumpedUp>0?3f:1f)*(HP*2<HT?1.5f:1f)*(surroundingWater()>7?10f:1f));
    }

    @Override
    public int defenseSkill(Char enemy) {
        return (int)(super.defenseSkill(enemy) * ((HP*2 <= HT)? 1.5f : 1f)*(surroundingWater()>7?2f:1f));
    }

    @Override
    public float speed(){
        return super.speed()*(Dungeon.level.water[pos]?1.33f:1f)*(surroundingWater()>7?2f:1f);
    }

    private boolean berserk(){ return HP*2<HT; }

    @Override
    protected boolean canAttack( Char enemy ) {
        return (pumpedUp > 0) ? distance( enemy ) <= 2 : super.canAttack(enemy);
    }

    protected static boolean isSummonedByDK(Mob m){
        return (m instanceof DwarfMaster.DKGhoul || m instanceof DwarfMaster.DKWarlock || m instanceof DwarfMaster.DKMonk);
    }

    private int aliveSummons(){
        int count = 0;
        for(Mob m: Dungeon.level.mobs.toArray(new Mob[0])){
            if(isSummonedByDK(m)){
                count++;
            }
        }
        return count;
    }

    private void rollForAbility(){
        lastAbility = Random.chances(chanceMap);
        chanceMap[lastAbility] /= 4f;
        if(chanceMap[lastAbility] < 0.0001f) resetChanceMap();
    }

    private void resetChanceMap(){
        for(int i=1;i<chanceMap.length;++i){
            chanceMap[i]=100f;
        }
        chanceMap[0]=0f;
    }

    public static class SacrificeSubjectListener extends FlavourBuff {
        Emitter charge;
        @Override
        public void fx(boolean on){
            if(on) {
                charge = target.sprite.emitter();
                charge.autoKill = false;
                charge.pour( HalomethaneFlameParticle.FACTORY, 0.06f );
                charge.on = false;
            }else{
                if(charge != null) {
                    charge.on = false;
                    charge = null;
                }

            }
        }
        @Override
        public void detach(){
            int[] area = PathFinder.NEIGHBOURS8;
            for(Mob m: Dungeon.level.mobs.toArray(new Mob[0])){
                if(isSummonedByDK(m)){
                    m.die(DwarfMaster.class);
                    for(int i: area) {
                        CellEmitter.center(i+m.pos).burst(Speck.factory(Speck.BONE), 3);
                        Char ch = findChar(i+m.pos);
                        if(ch != null){
                            if(ch.alignment != Alignment.ENEMY){
                                ch.damage(Random.IntRange(25, 36), m);
                                if(ch == Dungeon.hero && !ch.isAlive()){
                                    Dungeon.fail(getClass());
                                }
                            }
                        }
                    }
                    CellEmitter.center(m.pos).burst(Speck.factory(Speck.BONE), 6);
                }
            }
            super.detach();
        }
    }

    public static class DKBarrior extends Barrier {

        @Override
        public boolean act() {
            incShield();
            return super.act();
        }

        @Override
        public int icon() {
            return BuffIndicator.NONE;
        }
    }



    @Override
    public void damage(int dmg, Object src) {

        if(HP > 400 && dmg > 300){
            dmg = 300;
        }

        if (HP > 299 && HP <= 500 && dmg >= 9){
            dmg = 15+ (int)(Math.sqrt(8*(dmg - 4) + 1) - 1)/2;
        } else  {
            super.damage(dmg, src);
        }

        if (isInvulnerable(src.getClass())){
            super.damage(dmg, src);
            return;
        } else if (phase == 3 && !(src instanceof Viscosity.DeferedDamage)){
            if(src instanceof DwarfMaster.KingDamager) dmg = 1;
            if (dmg >= 10) {
                Viscosity.DeferedDamage deferred = Buff.affect( this, Viscosity.DeferedDamage.class );
                deferred.prolong( dmg );

                sprite.showStatus( CharSprite.WARNING, Messages.get(Viscosity.class, "deferred", dmg) );
            }
            return;
        }
        int preHP = HP;
        super.damage(dmg, src);

        LockedFloor lock = Dungeon.hero.buff(LockedFloor.class);
        if (lock != null && !isImmune(src.getClass())) lock.addTime(dmg/3);


        if (phase == 1) {
            int dmgTaken = preHP - HP;
            abilityCooldown -= dmgTaken/8f;
            summonCooldown -= dmgTaken/8f;
            if (HP <= 400) {
                Actor.add(new Actor() {

                    {
                        actPriority = VFX_PRIO;
                    }

                    @Override
                    protected boolean act() {
                        Actor.remove(this);
                        HP = 400;
                        sprite.showStatus(CharSprite.POSITIVE, Messages.get(Char.class, "invulnerable"));
                        properties.add(Property.IMMOVABLE);
                        phase = 2;
                        summonsMade = 0;
                        sprite.idle();

                        return true;
                    }
                });
                ScrollOfTeleportation.appear(this, DwarfMasterBossLevel.throne);
                Buff.affect(this, DwarfMaster.DKBarrior.class).setShield(12*25);
                for (DwarfMaster.Summoning s : buffs(DwarfMaster.Summoning.class)) {
                    s.detach();
                }
                for (Mob m : Dungeon.level.mobs.toArray(new Mob[0])) {
                    if (m instanceof DKGhoul || m instanceof DKMonk || m instanceof DKWarlock) {
                        m.die(null);
                    }
                }
                for (Mob m : Dungeon.level.mobs.toArray(new Mob[0])) {
                    if (m instanceof Ghoul || m instanceof Monk || m instanceof Warlock) {
                        m.die(null);
                    }
                }
                Buff.detach(this, DwarfMaster.SacrificeSubjectListener.class);
                Char enemy = (this.enemy == null ? Dungeon.hero : this.enemy);
                int w = Dungeon.level.width();
                int dx = enemy.pos % w - pos % w;
                int dy = enemy.pos / w - pos / w;
                int direction = 2 * (Math.abs(dx) > Math.abs(dy) ? 0 : 1);
                direction += (direction > 0 ? (dy > 0 ? 1 : 0) : (dx > 0 ? 1 : 0));
                Buff.affect(this, DwarfMaster.YogScanHalf.class).setPos(pos, direction);
                beamCD = 20 + 8 - (phase == 5 ? 19 : 0);
                sprite.showStatus(0xff0000, Messages.get(this, "dead"));
            }
        } else if (phase == 2 && shielding() == 0) {
            properties.remove(Property.IMMOVABLE);
            sprite.centerEmitter().start( Speck.factory( Speck.SCREAM ), 0.4f, 2 );
            Sample.INSTANCE.play( Assets.Sounds.CHALLENGE );
            phase = 3;
            yell(  Messages.get(this, "enraged", Dungeon.hero.name()) );
            Buff.detach(this, DwarfMaster.SacrificeSubjectListener.class);
            Char enemy = (this.enemy == null ? Dungeon.hero : this.enemy);
            int w = Dungeon.level.width();
            int dx = enemy.pos % w - pos % w;
            int dy = enemy.pos / w - pos / w;
            int direction = 2 * (Math.abs(dx) > Math.abs(dy) ? 0 : 1);
            direction += (direction > 0 ? (dy > 0 ? 1 : 0) : (dx > 0 ? 1 : 0));
            Buff.affect(this, DwarfMaster.YogScanHalf.class).setPos(pos, direction);
            beamCD = 20 + 8 - (phase == 5 ? 19 : 0);
            sprite.showStatus(0xff0000, Messages.get(this, "dead"));
        } else if (phase == 3 && preHP > 50 && HP <= 50){
            yell( Messages.get(this, "losing") );
        }
    }


    @Override
    public boolean isInvulnerable(Class effect) {
        return phase == 2 && effect != DwarfMaster.KingDamager.class;
    }

    @Override
    protected boolean act() {
        if (phase == 1) {
            Actor.add(new Actor() {

                {
                    actPriority = VFX_PRIO;
                }

                @Override
                protected boolean act() {
                    Actor.remove(this);
                    if (summonCooldown <= 0 && summonSubject(3)){
                        summonsMade++;
                        summonCooldown += Random.NormalIntRange(MIN_COOLDOWN, MAX_COOLDOWN)-1f;
                    } else if (summonCooldown > 0){
                        summonCooldown-=1f/speed();
                    }

                    if (paralysed > 0){
                        spend(TICK);
                        return true;
                    }

                    if (abilityCooldown <= 0){
                        int alive = aliveSummons();
                        //NEVER use skills if no mobs alive.
                        if(alive == 0) {
                            lastAbility = NONE;
                            //summon faster when no ability is available.
                            summonCooldown -= 2f;
                        }
                        else {
                            do {
                                rollForAbility();
                            } while ((lastAbility == ENRAGE || lastAbility == SACRIFICE) && alive < 2);
                        }
                        if(buff(SacrificeSubjectListener.class)!= null){
                            lastAbility = NONE;
                            //cd faster while prepare to sacrifice
                            abilityCooldown --;
                        }

                        if (lastAbility == LINK && lifeLinkSubject()){
                            abilityCooldown += Random.NormalIntRange(MIN_COOLDOWN, MAX_COOLDOWN);
                            spend(TICK);
                            return true;
                        } else if (lastAbility == TELE && teleportSubject()) {
                            lastAbility = TELE;
                            abilityCooldown += Random.NormalIntRange(MIN_COOLDOWN, MAX_COOLDOWN);
                            spend(TICK);
                            return true;
                        }else if(lastAbility == ENRAGE){
                            enrageSubject();
                            abilityCooldown += Random.NormalIntRange(MIN_COOLDOWN, MAX_COOLDOWN);
                            spend(TICK);
                            return true;
                        }else if(lastAbility == DEATHRATTLE){
                            deathRattleSubject();
                            lastAbility = DEATHRATTLE;
                            abilityCooldown += Random.NormalIntRange(MIN_COOLDOWN, MAX_COOLDOWN);
                            spend(TICK);
                        }else if(lastAbility == SACRIFICE){
                            sacrificeSubject();
                            abilityCooldown += Random.NormalIntRange(MIN_COOLDOWN, MAX_COOLDOWN);
                            spend(TICK);
                        }else if(lastAbility == SUMMON){
                            extraSummonSubject();
                            abilityCooldown += Random.NormalIntRange(MIN_COOLDOWN, MAX_COOLDOWN);
                            spend(TICK);
                        }

                    } else {
                        abilityCooldown--;
                    }

                    return true;
                }
            });
        } else if (phase == 2){
            actPhaseTwoSummon();
            return true;
        } else if (phase == 3 && buffs(DwarfMaster.Summoning.class).size() < 4){
            if (summonSubject(2)) summonsMade++;
        }
        actScanning();

        return super.act();
    }

    private int targetAbilityUses(){
        //1 base ability use, plus 2 uses per jump
        int targetAbilityUses = 1 + 2*arenaJumps;

        //and ane extra 2 use for jumps 3 and 4
        targetAbilityUses += Math.max(0, arenaJumps-2);

        return targetAbilityUses;
    }

    public static boolean throwFire(final Char thrower, final Char target){

        Ballistica aim = new Ballistica(thrower.pos, target.pos, Ballistica.WONT_STOP);

        for (int i = 0; i < PathFinder.CIRCLE8.length; i++){
            if (aim.sourcePos+PathFinder.CIRCLE8[i] == aim.path.get(1)){
                thrower.sprite.zap(target.pos);
                Buff.append(thrower, Tengu.FireAbility.class).direction = i;

                thrower.sprite.emitter().start(Speck.factory(Speck.STEAM), .03f, 10);
                return true;
            }
        }

        return false;
    }

    public boolean canUseAbility(){

        if (HP > HT/2) return false;

        if (abilitiesUsed >= targetAbilityUses()){
            return false;
        } else {

            abilityCooldown--;

            if (targetAbilityUses() - abilitiesUsed >= 4){
                //Very behind in ability uses, use one right away!
                abilityCooldown = 0;

            } else if (targetAbilityUses() - abilitiesUsed >= 3){
                //moderately behind in uses, use one every other action.
                if (abilityCooldown == -1 || abilityCooldown > 1) abilityCooldown = 1;

            } else {
                //standard delay before ability use, 1-4 turns
                if (abilityCooldown == -1) abilityCooldown = Random.IntRange(1, 4);
                boolean visible = fieldOfView[pos] || fieldOfView[enemy.pos];
                if (visible) {
                    sprite.attack( enemy.pos );
                }
                if (HP >= 2 + Dungeon.depth*2) {
                    ArrayList<Integer> candidates = new ArrayList<>();
                    boolean[] solid = Dungeon.level.solid;

                    int[] neighbours = {pos + 1, pos - 1, pos + Dungeon.level.width(), pos - Dungeon.level.width()};
                    for (int n : neighbours) {
                        if (!solid[n] && Actor.findChar( n ) == null) {
                            candidates.add( n );
                        }
                    }

                    if (candidates.size() > 0) {

                        SRPDHBLR clone = new SRPDHBLR();
                        clone.HP = 25;
                        clone.pos = Random.element( candidates );
                        clone.state = clone.HUNTING;

                        Dungeon.level.occupyCell(clone);

                        GameScene.add( clone, SPLIT_DELAY );
                        Actor.addDelayed( new Pushing( clone, pos, clone.pos ), -1 );

                        HP -= 1;
                    }
                }
            }

            if (abilityCooldown == 0){
                return true;
            } else {
                return false;
            }
        }
    }

    private static final float SPLIT_DELAY	= 4f;
    public int generation	= 0;

    public static boolean throwBomb(final Char thrower, final Char target){

        int targetCell = -1;

        //Targets closest cell which is adjacent to target, and at least 3 tiles away
        for (int i : PathFinder.NEIGHBOURS8){
            int cell = target.pos + i;
            if (Dungeon.level.distance(cell, thrower.pos) >= 3 && !Dungeon.level.solid[cell]){
                if (targetCell == -1 ||
                        Dungeon.level.trueDistance(cell, thrower.pos) < Dungeon.level.trueDistance(targetCell, thrower.pos)){
                    targetCell = cell;
                }
            }
        }

        if (targetCell == -1){
            return false;
        }

        final int finalTargetCell = targetCell;
        throwingChar = thrower;
        final BombAbility.BombItem item = new BombAbility.BombItem();
        thrower.sprite.zap(finalTargetCell);
        ((MissileSprite) thrower.sprite.parent.recycle(MissileSprite.class)).
                reset(thrower.sprite,
                        finalTargetCell,
                        item,
                        new Callback() {
                            @Override
                            public void call() {
                                item.onThrow(finalTargetCell);
                                thrower.next();
                            }
                        });
        return true;
    }

    //so that mobs can also use this
    private static Char throwingChar;


    private int abilitiesUsed = 0;
    private int arenaJumps = 0;

    //starts at 2, so one turn and then first ability

    private static final int BOMB_ABILITY    = 0;
    private static final int FIRE_ABILITY    = 1;
    private static final int SHOCKER_ABILITY = 2;

    public static class BombAbility extends Buff {

        public int bombPos = -1;
        private int timer = 3;

        private ArrayList<Emitter> smokeEmitters = new ArrayList<>();

        @Override
        public boolean act() {

            if (smokeEmitters.isEmpty()){
                fx(true);
            }

            PointF p = DungeonTilemap.raisedTileCenterToWorld(bombPos);
            if (timer == 3) {
                FloatingText.show(p.x, p.y, bombPos, "3...", CharSprite.NEUTRAL);
            } else if (timer == 2){
                FloatingText.show(p.x, p.y, bombPos, "2...", CharSprite.WARNING);
            } else if (timer == 1){
                FloatingText.show(p.x, p.y, bombPos, "1...", CharSprite.NEGATIVE);
            } else {
                PathFinder.buildDistanceMap( bombPos, BArray.not( Dungeon.level.solid, null ), 2 );
                for (int cell = 0; cell < PathFinder.distance.length; cell++) {

                    if (PathFinder.distance[cell] < Integer.MAX_VALUE) {
                        Char ch = Actor.findChar(cell);
                        if (ch != null && !(ch instanceof Tengu)) {
                            int dmg = Random.NormalIntRange(5 + Dungeon.depth, 10 + Dungeon.depth * 2);
                            dmg -= ch.drRoll();

                            if (dmg > 0) {
                                ch.damage(dmg, Bomb.class);
                            }

                            if (ch == Dungeon.hero && !ch.isAlive()) {
                                Dungeon.fail(Tengu.class);
                            }
                        }

                        Heap h = Dungeon.level.heaps.get(cell);
                        if (h != null) {
                            for (Item i : h.items.toArray(new Item[0])) {
                                if (i instanceof Tengu.BombAbility.BombItem) {
                                    h.remove(i);
                                }
                            }
                        }
                    }

                }
                Sample.INSTANCE.play(Assets.Sounds.BLAST);
                detach();
                return true;
            }

            timer--;
            spend(TICK);
            return true;
        }

        @Override
        public void fx(boolean on) {
            if (on && bombPos != -1){
                PathFinder.buildDistanceMap( bombPos, BArray.not( Dungeon.level.solid, null ), 2 );
                for (int i = 0; i < PathFinder.distance.length; i++) {
                    if (PathFinder.distance[i] < Integer.MAX_VALUE) {
                        Emitter e = CellEmitter.get(i);
                        e.pour( SmokeParticle.FACTORY, 0.25f );
                        smokeEmitters.add(e);
                    }
                }
            } else if (!on) {
                for (Emitter e : smokeEmitters){
                    e.burst(BlastParticle.FACTORY, 2);
                }
            }
        }

        private static final String BOMB_POS = "bomb_pos";
        private static final String TIMER = "timer";

        @Override
        public void storeInBundle(Bundle bundle) {
            super.storeInBundle(bundle);
            bundle.put( BOMB_POS, bombPos );
            bundle.put( TIMER, timer );
        }

        @Override
        public void restoreFromBundle(Bundle bundle) {
            super.restoreFromBundle(bundle);
            bombPos = bundle.getInt( BOMB_POS );
            timer = bundle.getInt( TIMER );
        }

        public static class BombItem extends Item {

            {
                dropsDownHeap = true;
                unique = true;

                image = ItemSpriteSheet.TENGU_BOMB;
            }

            @Override
            public boolean doPickUp( Hero hero ) {
                GLog.w( Messages.get(this, "cant_pickup") );
                return false;
            }

            @Override
            protected void onThrow(int cell) {
                super.onThrow(cell);
                if (throwingChar != null){
                    Buff.append(throwingChar, Tengu.BombAbility.class).bombPos = cell;
                    throwingChar = null;
                } else {
                    Buff.append(curUser, Tengu.BombAbility.class).bombPos = cell;
                }
            }

            @Override
            public Emitter emitter() {
                Emitter emitter = new Emitter();
                emitter.pos(7.5f, 3.5f);
                emitter.fillTarget = false;
                emitter.pour(SmokeParticle.SPEW, 0.05f);
                return emitter;
            }
        }
    }

    private void enrageSubject(){
        for(Mob m: Dungeon.level.mobs.toArray(new Mob[0])){
            if(isSummonedByDK(m)){
                Buff.affect(m, Healing.class).setHeal(42, 0f, 6);
            }
        }
        new Flare(5, 32).color(0xFF6060, false).show(sprite, 1.5f);
        yell(Messages.get(this,"buff_all"));
    }

    private void sacrificeSubject(){
        Buff.affect(this,  DwarfMaster.SacrificeSubjectListener.class, 3f);
        new Flare(6, 32).color(0xFF22FF, false).show(sprite, 1.5f);
        yell(Messages.get(this, "sacrifice"));
    }

    public static class DeathRattleSummon extends Buff{
        private Emitter charge;
        private int summonCate=-1;
        public void setId(int id){
            summonCate = id;
        }
        @Override
        public void storeInBundle(Bundle b){
            b.put("summonCategory", summonCate);
            super.storeInBundle(b);
        }
        @Override
        public void restoreFromBundle(Bundle b){
            super.restoreFromBundle(b);
            summonCate = b.getInt("summonCategory");
        }
        public Mob idToMob(){
            switch (summonCate){
                case 0: return Reflection.newInstance(DwarfMaster.DKGhoul.class);
                case 1: return Reflection.newInstance(DwarfMaster.DKWarlock.class);
                case 2: return Reflection.newInstance(DwarfMaster.DKMonk.class);
                case 3: return Reflection.newInstance(Skeleton.class);
                case 4: return Reflection.newInstance(SRPDHBLR.class);
                case 5: return Reflection.newInstance(DM201.class);
                case 6: default: return Reflection.newInstance(OGPDZSLS.class);
            }
        }
        public boolean summon(int pos){
            Mob m = idToMob();
            if(m == null) return false;
            m.pos = pos;
            m.HP = m.HT * 3 / 5;
            GameScene.add(m);
            return true;
        }
        @Override
        public void detach(){
            summon(target.pos);
            if(summonCate == 0) new Flare(6, 25).color(0xCCCCCC, false).show(target.sprite, 1f);
            else if(summonCate == 1) new Flare(6, 25).color(0x303030, false).show(target.sprite, 1f);
            else if(summonCate == 2) new Flare(6, 25).color(0x4040FF, false).show(target.sprite, 1f);
            super.detach();
        }
        @Override
        public void fx(boolean on){
            if (on && charge == null &&summonCate!=-1) {
                charge = target.sprite.emitter();

                if (summonCate == 1){
                    charge.pour(ShadowParticle.UP, 0.3f);
                } else if (summonCate == 2){
                    charge.pour(ElmoParticle.FACTORY, 0.3f);
                } else {
                    charge.pour(Speck.factory(Speck.RATTLE), 0.3f);
                }

            } else {
                if(charge != null) {
                    charge.on = false;
                    charge = null;
                }
            }
        }
        @Override
        public boolean act(){
            fx(true);
            spend(TICK*9999f);
            return true;
        }
    }

    private void deathRattleSubject(){
        int count = 0;
        for(Mob m: Dungeon.level.mobs.toArray(new Mob[0])){
            if(isSummonedByDK(m)){
                //theoretically multiple death rattles is ok but,
                //problems: 1. vfx  2. mobs at one tile
                if(m.buff(DwarfMaster.DeathRattleSummon.class)==null) {
                    Buff.affect(m, DwarfMaster.DeathRattleSummon.class).setId(Random.chances(new float[]{9f, 4f, 4f}));
                    count++;
                }
            }
            if(count>=4) break;
        }
        new Flare(7, 32).color(0x303030, false).show(sprite, 1.5f);
        yell(Messages.get(this,"death_rattle"));
    }

    private boolean teleportSubject(){
        if (enemy == null) return false;

        Mob furthest = null;

        for (Mob m : getSubjects()){
            if (furthest == null || Dungeon.level.distance(pos, furthest.pos) < Dungeon.level.distance(pos, m.pos)){
                furthest = m;
            }
        }

        if (furthest != null){

            float bestDist;
            int bestPos = pos;

            Ballistica trajectory = new Ballistica(enemy.pos, pos, Ballistica.STOP_TARGET);
            int targetCell = trajectory.path.get(trajectory.dist);
            //if the position opposite the direction of the hero is open, go there
            if (Actor.findChar(targetCell) == null && !Dungeon.level.solid[targetCell]){
                bestPos = targetCell;

                //Otherwise go to the neighbour cell that's open and is furthest
            } else {
                bestDist = Dungeon.level.trueDistance(pos, enemy.pos);

                for (int i : PathFinder.NEIGHBOURS8){
                    if (Actor.findChar(pos+i) == null
                            && !Dungeon.level.solid[pos+i]
                            && Dungeon.level.trueDistance(pos+i, enemy.pos) > bestDist){
                        bestPos = pos+i;
                        bestDist = Dungeon.level.trueDistance(pos+i, enemy.pos);
                    }
                }
            }

            Actor.add(new Pushing(this, pos, bestPos));
            pos = bestPos;

            //find closest cell that's adjacent to enemy, place subject there
            bestDist = Dungeon.level.trueDistance(enemy.pos, pos);
            bestPos = enemy.pos;
            for (int i : PathFinder.NEIGHBOURS8){
                if (Actor.findChar(enemy.pos+i) == null
                        && !Dungeon.level.solid[enemy.pos+i]
                        && Dungeon.level.trueDistance(enemy.pos+i, pos) < bestDist){
                    bestPos = enemy.pos+i;
                    bestDist = Dungeon.level.trueDistance(enemy.pos+i, pos);
                }
            }

            if (bestPos != enemy.pos) ScrollOfTeleportation.appear(furthest, bestPos);
            yell(Messages.get(this, "teleport_" + Random.IntRange(1, 2)));
            return true;
        }
        return false;
    }


    private void extraSummonSubject(){
        summonSubject(2);
        summonsMade++;
        summonSubject(3);
        summonsMade++;
        yell(Messages.get(this, "more_summon"));
        new Flare(4, 32).color(0x4040FF, false).show(sprite, 1.5f);
    }

    private HashSet<Mob> getSubjects(){
        HashSet<Mob> subjects = new HashSet<>();
        for (Mob m : Dungeon.level.mobs.toArray(new Mob[0])){
            if (m.alignment == alignment && (m instanceof Ghoul || m instanceof Monk || m instanceof Warlock)){
                subjects.add(m);
            }
        }
        return subjects;
    }

    private boolean lifeLinkSubject(){
        Mob furthest = null;

        for (Mob m : getSubjects()){
            boolean alreadyLinked = false;
            for (LifeLink l : m.buffs(LifeLink.class)){
                if (l.object == id()) alreadyLinked = true;
            }
            if (!alreadyLinked) {
                if (furthest == null || Dungeon.level.distance(pos, furthest.pos) < Dungeon.level.distance(pos, m.pos)){
                    furthest = m;
                }
            }
        }

        if (furthest != null) {
            Buff.append(furthest, LifeLink.class, 100f).object = id();
            Buff.append(this, LifeLink.class, 100f).object = furthest.id();
            yell(Messages.get(this, "lifelink_" + Random.IntRange(1, 2)));
            Buff.affect(this, Healing.class).setHeal(5, 0f, 6);
            sprite.parent.add(new Beam.HealthRay(sprite.destinationCenter(), furthest.sprite.destinationCenter()));
            return true;

        }
        return false;
    }

    private void actPhaseTwoSummon(){
        if(wave == 0){
            Actor.add(new Actor() {

                {
                    actPriority = VFX_PRIO;
                }

                @Override
                protected boolean act() {
                    Actor.remove(this);
                    yell(Messages.get(DwarfMaster.class, "wave_1"));
                    new GnollShiled().spawnAround(pos);
                    summonSubject(2, DwarfMaster.DKGhoul.class);
                    summonSubject(3, DwarfMaster.DKGhoul.class);
                    ++wave;
                    spend(TICK*9);
                    return true;
                }
            });
        }else if(wave == 1){
            summonSubject(1, DwarfMaster.DKGhoul.class);
            summonSubject(5, DwarfMaster.DKMonk.class);
            ++wave;
            Char enemy = (this.enemy == null ? Dungeon.hero : this.enemy);
            int w = Dungeon.level.width();
            int dx = enemy.pos % w - pos % w;
            int dy = enemy.pos / w - pos / w;
            int direction = 2 * (Math.abs(dx) > Math.abs(dy) ? 0 : 1);
            direction += (direction > 0 ? (dy > 0 ? 1 : 0) : (dx > 0 ? 1 : 0));
            Buff.affect(this, DwarfMaster.YogScanHalf.class).setPos(pos, direction);
            beamCD = 20 + 8 - (phase == 5 ? 19 : 0);
            spend(TICK*12);
        }else if(wave == 2){
            summonSubject(1, DwarfMaster.DKGhoul.class);
            summonSubject(2, DwarfMaster.DKWarlock.class);
            summonSubject(6, DwarfMaster.DKGhoul.class);
            summonSubject(6, DwarfMaster.DKGhoul.class);
            ++wave;
            spend(TICK*15);
        }else if(wave == 3){
            yell(Messages.get(this, "wave_2"));
            //Eye.spawnAround(pos);
            summonSubject(1, DwarfMaster.DKGhoul.class);
            summonSubject(2, DwarfMaster.DKWarlock.class);
            summonSubject(2, DwarfMaster.DKGhoul.class);
            summonSubject(11, DwarfMaster.DKMonk.class);
            summonSubject(5, OGPDZSLS.class);
            summonSubject(7, SRPDHBLR.class);
            Char enemy = (this.enemy == null ? Dungeon.hero : this.enemy);
            int w = Dungeon.level.width();
            int dx = enemy.pos % w - pos % w;
            int dy = enemy.pos / w - pos / w;
            int direction = 2 * (Math.abs(dx) > Math.abs(dy) ? 0 : 1);
            direction += (direction > 0 ? (dy > 0 ? 1 : 0) : (dx > 0 ? 1 : 0));
            Buff.affect(this, DwarfMaster.YogScanHalf.class).setPos(pos, direction);
            beamCD = 20 + 8 - (phase == 5 ? 19 : 0);
            ++wave;
            spend(TICK*15);
        }else if(wave == 4){
            summonSubject(2, DwarfMaster.DKGhoul.class);
            summonSubject(5, DwarfMaster.DKWarlock.class);
            summonSubject(5, DwarfMaster.DKMonk.class);
            summonSubject(2, DwarfMaster.DKGhoul.class);
            summonSubject(5, OGPDZSLS.class);
            summonSubject(7, SRPDHBLR.class);
            summonSubject(5, DM100.class);
            ++wave;
            spend(TICK*14);
        }else if(wave == 5){
            yell(Messages.get(this,"wave_3"));
            summonSubject(2, DwarfMaster.DKGhoul.class);
            summonSubject(4, DwarfMaster.DKWarlock.class);
            summonSubject(4, DwarfMaster.DKMonk.class);
            summonSubject(8, DwarfMaster.DKMonk.class);
            summonSubject(2, DwarfMaster.DKGhoul.class);
            summonSubject(4, OGPDZSLS.class);
            summonSubject(4, SRPDHBLR.class);
            summonSubject(3, DM100.class);
            Buff.affect(this, Haste.class, 5f);
            Buff.affect(this, Healing.class).setHeal(20, 0f, 6);
            Char enemy = (this.enemy == null ? Dungeon.hero : this.enemy);
            int w = Dungeon.level.width();
            int dx = enemy.pos % w - pos % w;
            int dy = enemy.pos / w - pos / w;
            int direction = 2 * (Math.abs(dx) > Math.abs(dy) ? 0 : 1);
            direction += (direction > 0 ? (dy > 0 ? 1 : 0) : (dx > 0 ? 1 : 0));
            Buff.affect(this, DwarfMaster.YogScanHalf.class).setPos(pos, direction);
            beamCD = 20 + 8 - (phase == 5 ? 19 : 0);
            ++wave;
            spend(TICK*13);
        }else if(wave == 6){
            summonSubject(3, DwarfMaster.DKWarlock.class);
            summonSubject(3, DwarfMaster.DKMonk.class);
            summonSubject(3, DwarfMaster.DKMonk.class);
            summonSubject(3, DwarfMaster.DKWarlock.class);
            summonSubject(2, DwarfMaster.DKGhoul.class);
            summonSubject(5, OGPDZSLS.class);
            summonSubject(7, SRPDHBLR.class);
            summonSubject(5, DM100.class);
            summonSubject(2, DM201.class);
            summonSubject(5, DM200.class);
            summonSubject(3, Skeleton.class);
            summonSubject(3, Necromancer.class);
            //summonSubject(3, RedNecromancer.class);
            Buff.affect(this, RoseShiled.class, 20f);
            Buff.affect(this, Haste.class, 5f);
            Buff.affect(this, ArcaneArmor.class).set(Dungeon.hero.lvl + 10, 10);
            Buff.affect(this, Healing.class).setHeal(40, 0f, 6);
            ++wave;
            spend(TICK*12);
        }else{
            //only need to kill one.
            summonSubject(3, DwarfMaster.DKWarlock.class);
            spend(TICK);
        }
    }

    public static class DKGhoul extends GnollGuard {
        {
            state = HUNTING;
            immunities.add(Corruption.class);
            resistances.add(Amok.class);
            lootChance=0f;
            maxLvl = -8848;

            HP=HT=10;
        }

        @Override
        protected boolean act() {
            return super.act();
        }

        @Override
        public int damageRoll(){
            boolean str = buff(StrengthEmpower.class)!=null;
            return Math.round(super.damageRoll()*(str? 1.5f:1f));
        }
    }

    public static class DKMonk extends Eye {
        {
            state = HUNTING;
            immunities.add(Corruption.class);
            resistances.add(Amok.class);
            lootChance=0f;
            maxLvl = -8848;
        }
        @Override
        public int damageRoll(){
            boolean str = buff(StrengthEmpower.class)!=null;
            return Math.round(super.damageRoll()*(str? 1.5f:1f));
        }
    }

    public static class StrengthEmpower extends FlavourBuff{
        Emitter charge;
        @Override
        public void fx(boolean on){
            if (on && charge == null) {
                charge = target.sprite.emitter();

                charge.pour(Speck.factory(Speck.UP), 0.7f);

            } else {
                if(charge != null) {
                    charge.on = false;
                    charge = null;
                }
            }
        }
        @Override
        public boolean attachTo(Char target){
            target.sprite.showStatus(0x00FF00, Messages.get(DwarfMaster.class, "str_empower"));
            return super.attachTo(target);
        }
    }

    public static class DKWarlock extends FungalSpinner {
        {
            state = HUNTING;
            immunities.add(Corruption.class);
            resistances.add(Amok.class);
            lootChance=0f;
            maxLvl = -8848;
        }
        @Override
        public int attackProc(Char enemy, int damage){
            if(Random.Int(10)==0) {
                Buff.affect(enemy, Degrade.class, 2f);
            }
            return super.attackProc(enemy, damage);
        }
        @Override
        public int damageRoll() {
            return Random.NormalIntRange( 10, 15 );
        }
    }

    @Override
    public int attackProc( Char enemy, int damage ) {
        damage = super.attackProc( enemy, damage );

        if (pumpedUp > 0) {
            Camera.main.shake( 3, 0.2f );
            Buff.affect(enemy, Cripple.class, 6f);
            Buff.affect(enemy, Vulnerable.class, 12f);
        }

        return damage;
    }

    @Override
    public int defenseProc(Char enemy, int damage){
//        if(damage > 3) summonMiniGoo();
        return super.defenseProc(enemy, damage);
    }

    private void summonMiniGoo(){
        ArrayList<Integer> candidates = new ArrayList<>();
        boolean[] solid = Dungeon.level.solid;

        for (int n : PathFinder.NEIGHBOURS8) {
            int p = n + pos;
            if (!solid[p] && Actor.findChar( p ) == null) {
                candidates.add( p );
            }
        }

//        if (candidates.size() > 0) {
//            RedMurderer mini = new RedMurderer();
//            Buff.affect(mini, Timer.class, berserk()? 5f:7f);
//            mini.pos = Random.element( candidates );
//            mini.state = mini.HUNTING;
//
//            Dungeon.level.occupyCell(mini);
//
//            GameScene.add( mini , 0f );
//            Actor.addDelayed( new Pushing( mini, pos, mini.pos ), -1 );
//        }
    }

    @Override
    public void updateSpriteState() {
        super.updateSpriteState();

        if (pumpedUp > 0){
            ((DiedMonkSprite)sprite).pumpUp( pumpedUp );
        }
    }

    @Override
    protected boolean doAttack( Char enemy ) {
        if (pumpedUp == 1) {
            ((DiedMonkSprite)sprite).pumpUp( 2 );
            pumpedUp++;
            Sample.INSTANCE.play( Assets.Sounds.CHARGEUP );

            spend( attackDelay() );

            return true;
        } else if (pumpedUp >= 2 || Random.Int( (HP*2 <= HT) ? 2 : 3 ) > 0) {

            boolean visible = Dungeon.level.heroFOV[pos];

            if (visible) {
                if (pumpedUp >= 2) {
                    ((DiedMonkSprite) sprite).pumpAttack();
                } else {
                    sprite.attack(enemy.pos);
                }
            } else {
                attack( enemy );
            }

            spend( attackDelay() );

            return !visible;

        } else {

            pumpedUp++;

            ((DiedMonkSprite)sprite).pumpUp( 1 );

            if (Dungeon.level.heroFOV[pos]) {
                sprite.showStatus( CharSprite.POSITIVE, Messages.get(this, "!!!") );
                yell( Messages.get(this, "pumpup") );
                Sample.INSTANCE.play( Assets.Sounds.CHARGEUP, 1f, 0.8f );
            }

            spend( attackDelay() );

            return true;
        }
    }

    @Override
    public boolean attack( Char enemy) {
        boolean result = super.attack(enemy);
        pumpedUp = 0;
        return result;
    }

    @Override
    protected boolean getCloser( int target ) {
        if (pumpedUp != 0) {
            pumpedUp = 0;
            sprite.idle();
        }
        return super.getCloser( target );
    }

    @Override
    public void die( Object cause ) {

        super.die( cause );
        Statistics.bossScores[3] += 6000;
        Dungeon.level.unseal();
        //酸液体清0
        Statistics.SiderLing = 0;
       for (Mob mob : Dungeon.level.mobs.toArray( new Mob[0] )) {
            if (	mob instanceof DwarfMaster.DKMonk ||
                    mob instanceof DwarfMaster.DKGhoul ||
                    mob instanceof DwarfMaster.DKWarlock||
                    mob instanceof GnollShiled || mob instanceof RedMurderer) {
                mob.die( cause );
            }
        }
      if(Statistics.happyMode){
            GetBossLoot();
        }
        for (Buff buff : hero.buffs()) {
            if (buff instanceof TestDwarfMasterLock) {
                buff.detach();
            }
        }

        GameScene.bossSlain();
        Dungeon.level.drop( new IronKey( Dungeon.depth ).quantity(4), pos ).sprite.drop();
        Dungeon.level.drop( new GoldenKey( Dungeon.depth ).quantity(3), pos ).sprite.drop();
        Dungeon.level.drop( new CrystalKey( Dungeon.depth ).quantity(2), pos ).sprite.drop();
        Dungeon.level.drop( new BlackKey( Dungeon.depth ).quantity(3), pos ).sprite.drop();
        Dungeon.level.drop( new SkeletonKey( Dungeon.depth ).quantity(1), pos ).sprite.drop();
        int blobs = Random.chances(new float[]{5, 4, 3, 2, 1}) + 3;
        for (int i = 0; i < blobs; i++){
            int ofs;
            do {
                ofs = PathFinder.NEIGHBOURS8[Random.Int(8)];
            } while (!Dungeon.level.passable[pos + ofs]);
            Dungeon.level.drop(new Gold().quantity(Random.Int(340, 450)), pos+ofs).sprite.drop();
        }
        //Dungeon.level.drop(new KingsCrown(), pos).sprite.drop();
        Dungeon.level.drop(new PotionOfHealing().quantity(Random.NormalIntRange(2,4)), pos).sprite.drop();
        Dungeon.level.drop(new MeatPie().quantity(Random.NormalIntRange(1,2)), pos).sprite.drop();
        PaswordBadges.KILLDWARF();
        Badges.validateBossSlain();

        yell( Messages.get(this, "defeated") );
    }

    @Override
    public void notice() {
        super.notice();
        if (!BossHealthBar.isAssigned()) {
            BossHealthBar.assignBoss(this);
            yell(Messages.get(this, "notice"));
            for (Char ch : Actor.chars()){
                if (ch instanceof DriedRose.GhostHero){
                    ((DriedRose.GhostHero) ch).sayBoss();
                }
            }
        }
    }

    private final String PUMPEDUP = "pumpedup";

    @Override
    public void storeInBundle( Bundle bundle ) {

        super.storeInBundle( bundle );

        bundle.put( PUMPEDUP , pumpedUp );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {

        super.restoreFromBundle( bundle );

        pumpedUp = bundle.getInt( PUMPEDUP );
        if (state != SLEEPING) BossHealthBar.assignBoss(this);
        if ((HP*2 <= HT)) BossHealthBar.bleed(true);

    }
}
