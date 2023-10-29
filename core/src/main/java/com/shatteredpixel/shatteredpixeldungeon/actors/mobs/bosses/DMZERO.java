package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.PaswordBadges;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Fire;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ChampionEnemy;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Corrosion;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LockedFloor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.RandomBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.TestDwarfMasterLock;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.FlameB01;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.NewDM300;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Spinner;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NullDiedTO;
import com.shatteredpixel.shatteredpixeldungeon.effects.BlobEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Pushing;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BlastParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ElmoParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.IronKey;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfBlastWave;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.NewCavesBossLevel;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DM275Sprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DM300AttackSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DM300SpiderSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DM300Sprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

/*
* DMZERO 隐藏Boss
* 总共四个形态
*/

public class DMZERO extends Mob
{

    public static class DM300AttackMode extends FlameB01 implements Callback
    {

        public int attackProc(Char var1, int var2) {
            byte var3 = 0;
            int var4;

            int var5 = super.attackProc(var1, var2);
            var4 = var1.pos;
            CellEmitter.center(var4).burst(BlastParticle.FACTORY, 30);
            GameScene.add(Blob.seed(var4, 2, Fire.class));
            int[] var7 = PathFinder.NEIGHBOURS9;
            int var6 = var7.length;

            for(var2 = var3; var2 < var6; ++var2) {
                int var8 = var7[var2];
                if (!Dungeon.level.solid[var4 + var8]) {
                    GameScene.add(Blob.seed(var4 + var8, 2, Fire.class));
                }
            }
            return var5;
        }

        public int attackSkill(Char char1)
        {
            return 28;
        }

        public void dropRocks( Char target ) {

            Dungeon.hero.interrupt();
            final int rockCenter;

            if (Dungeon.level.adjacent(pos, target.pos)){
                int oppositeAdjacent = target.pos + (target.pos - pos);
                Ballistica trajectory = new Ballistica(target.pos, oppositeAdjacent, Ballistica.MAGIC_BOLT);
                WandOfBlastWave.throwChar(target, trajectory, 2, false, false, getClass());
                if (target == Dungeon.hero){
                    Dungeon.hero.interrupt();
                }
                rockCenter = trajectory.path.get(Math.min(trajectory.dist, 2));
            } else {
                rockCenter = target.pos;
            }

            int safeCell;
            do {
                safeCell = rockCenter + PathFinder.NEIGHBOURS8[Random.Int(8)];
            } while (safeCell == pos
                    || (Dungeon.level.solid[safeCell] && Random.Int(2) == 0)
                    || (Blob.volumeAt(safeCell, NewCavesBossLevel.PylonEnergy.class) > 0 && Random.Int(2) == 0));

            ArrayList<Integer> rockCells = new ArrayList<>();

            int start = rockCenter - Dungeon.level.width() * 3 - 3;
            int pos;
            for (int y = 0; y < 7; y++) {
                pos = start + Dungeon.level.width() * y;
                for (int x = 0; x < 7; x++) {
                    if (!Dungeon.level.insideMap(pos)) {
                        pos++;
                        continue;
                    }
                    //add rock cell to pos, if it is not solid, and isn't the safecell
                    if (!Dungeon.level.solid[pos] && pos != safeCell && Random.Int(Dungeon.level.distance(rockCenter, pos)) == 0) {
                        //don't want to overly punish players with slow move or attack speed
                        rockCells.add(pos);
                    }
                    pos++;
                }
            }
            Buff.append(this, NewDM300.FallingRockBuff.class, Math.min(target.cooldown(), 3*TICK)).setRockPositions(rockCells);

        }

        @Override
        public void damage(int dmg, Object src) {
            super.damage(dmg, src);
            LockedFloor lock = Dungeon.hero.buff(LockedFloor.class);
            if (lock != null && !isImmune(src.getClass())) lock.addTime(dmg*1.5f);
        }

        public int damageRoll()
        {
            return Random.NormalIntRange(20, 25);
        }

        public void die(Object obj)
        {
            super.die(obj);

            Dungeon.level.drop(new IronKey(Dungeon.depth), pos).sprite.drop();
            obj = new DM300SpiderMode();
            ((DM300SpiderMode) obj).pos = pos;
            GameScene.add(((Mob) (obj)));
            Actor.addDelayed(new Pushing(((Char) (obj)), pos, ((DM300SpiderMode) (obj)).pos), -1F);
            yell( Messages.get(this, "defeated") );
        }

        public void ventGas( Char target ){
            hero.interrupt();

            int gasVented = 0;

            Ballistica trajectory = new Ballistica(pos, target.pos, Ballistica.STOP_TARGET);

            for (int i : trajectory.subPath(0, trajectory.dist)){
                GameScene.add(Blob.seed(i, 20, ToxicGas.class));
                gasVented += 20;
            }

            GameScene.add(Blob.seed(trajectory.collisionPos, 100, ToxicGas.class));

            if(Random.Float()>=0.4f){
                dropRocks(enemy);
            }


            if (gasVented < 250){
                int toVentAround = (int)Math.ceil((250 - gasVented)/8f);
                for (int i : PathFinder.NEIGHBOURS8){
                    GameScene.add(Blob.seed(pos+i, toVentAround, ToxicGas.class));
                }

            }

        }

        public int drRoll()
        {
            return Random.NormalIntRange(0, 10);
        }

        public void move(int i)
        {
            super.move(i);
            if(Dungeon.level.map[i] == 19 && HP < HT)
            {
                HP = HP + Random.Int(1, HT - HP);
                sprite.emitter().burst(ElmoParticle.FACTORY, 5);
                if(Dungeon.level.heroFOV[i] && Dungeon.hero.isAlive())
                    GLog.n(Messages.get(this, "repair"));
            }
            if(Dungeon.level.heroFOV[i])
            {
                CellEmitter.get(i).start(Speck.factory(8), 0.07F, 10);
                Camera.main.shake(3F, 0.7F);
                Sample.INSTANCE.play("sound/snd_rocks.mp3");
                if(Dungeon.level.water[i])
                    GameScene.ripple(i);
                else
                if(Dungeon.level.map[i] == 1)
                {
                    Level.set(i, 20);
                    GameScene.updateMap(i);
                }
            }
        }

        public void notice()
        {
            super.notice();
            BossHealthBar.assignBoss(this);
            yell(Messages.get(this, "notice"));
        }

        public void restoreFromBundle(Bundle bundle)
        {
            super.restoreFromBundle(bundle);
            BossHealthBar.assignBoss(this);
        }

        public DM300AttackMode()
        {
            spriteClass = DM300AttackSprite.class;
            HT = 140;
            HP = 140;
            EXP = 30;
            Buff.affect(this, ChampionEnemy.Blazing.class);
            defenseSkill = 18;
        }

        protected boolean doAttack( Char enemy ) {

            if (Dungeon.level.adjacent( pos, enemy.pos )) {

                return super.doAttack( enemy );

            } else {

                if (sprite != null && (sprite.visible || enemy.sprite.visible)) {
                    sprite.zap(enemy.pos);
                    return false;
                } else {
                    ventGas(enemy);
                    Sample.INSTANCE.play(Assets.Sounds.GAS);
                    return true;
                }
            }
        }

        //used so resistances can differentiate between melee and magical attacks
        public static class DarkBolt{}

        private static final float TIME_TO_ZAP	= 4f;
        private void zap() {
            spend( TIME_TO_ZAP );

            if (hit( this, enemy, true )) {
                //TODO would be nice for this to work on ghost/statues too
                if (enemy == Dungeon.hero && Random.Int( 2 ) == 0) {
                    Buff.affect( hero, Burning.class ).reignite( hero, 12f );
                    Sample.INSTANCE.play( Assets.Sounds.DEBUFF );
                }

                int dmg = Random.NormalIntRange( 10, 25 );
                enemy.damage( dmg, new DM300AttackMode.DarkBolt() );

                if (enemy == Dungeon.hero && !enemy.isAlive()) {
                    Dungeon.fail( getClass() );
                    //GLog.n( Messages.get(this, "frost_kill") );
                }
            } else {
                enemy.sprite.showStatus( CharSprite.NEUTRAL,  enemy.defenseVerb() );
            }
        }

        public void onZapComplete() {
            zap();
            next();
        }

        @Override
        public void call() {
            next();
        }
    }

    public static class DM300DeathBall extends Mob
    {

        public int attackSkill(Char char1)
        {
            return 15;
        }

        public int damageRoll()
        {
            return Random.NormalIntRange(20, 25);
        }
        private int pumpedUp = 0;
        private int healInc = 1;
        @Override
        public boolean act() {

            if (HP < HT) {
                HP += Dungeon.level.water[pos] ? healInc+3 : healInc;

                LockedFloor lock = Dungeon.hero.buff(LockedFloor.class);
                if (lock != null) lock.removeTime(healInc*2);

                if (Dungeon.level.heroFOV[pos] ){
                    sprite.emitter().burst( Speck.factory( Speck.HEALING ), healInc );
                }
                if (healInc < 1) {
                    healInc++;
                }
                if (HP*2 > HT) {
                    BossHealthBar.bleed(false);
                    HP = Math.min(HP, HT);
                }
            } else {
                healInc = 1;
            }

            if (state != SLEEPING){
                Dungeon.level.seal();
            }

            return super.act();
        }

        {
            immunities.add( ToxicGas.class );
            immunities.add( Corrosion.class );
            immunities.add( Terror.class );
        }

        private final String PUMPEDUP = "pumpedup";
        private final String HEALINC = "healinc";

        @Override
        public void storeInBundle( Bundle bundle ) {

            super.storeInBundle( bundle );

            bundle.put( PUMPEDUP , pumpedUp );
            bundle.put( HEALINC, healInc );
        }

        public void die(Object obj)
        {
            super.die(obj);
            GameScene.bossSlain();
            for (Buff buff : hero.buffs()) {
                if (buff instanceof TestDwarfMasterLock) {
                    buff.detach();
                }
            }
            for (Buff buff : hero.buffs()) {
                if (buff instanceof RandomBuff.DiedBuff) {
                    buff.detach();
                }
            }
            Statistics.amuletObtained = true;
            obj = new NullDiedTO();
            ((NullDiedTO) obj).pos = pos;
            GameScene.add(((Mob) (obj)));
            Actor.addDelayed(new Pushing(((Char) (obj)), pos, ((NullDiedTO) (obj)).pos), -1F);
            Statistics.bossScores[4] += 3000;


            yell( Messages.get(this, "defeated") );
        }

        public int drRoll()
        {
            return Random.NormalIntRange(0, 8);
        }

        public void move(int i)
        {
            super.move(i);
            if(Dungeon.level.map[i] == 19 && HP < HT)
            {
                HP = HP + Random.Int(1, HT - HP);
                sprite.emitter().burst(ElmoParticle.FACTORY, 5);
                if(Dungeon.level.heroFOV[i] && Dungeon.hero.isAlive())
                    GLog.n(Messages.get(this, "repair"));
            }
            int[] ai = new int[8];
            int j = i - 1;
            ai[0] = j;
            int k = i + 1;
            ai[1] = k;
            ai[2] = i - Dungeon.level.width();
            ai[3] = i + Dungeon.level.width();
            ai[4] = j - Dungeon.level.width();
            ai[5] = j + Dungeon.level.width();
            ai[6] = k - Dungeon.level.width();
            ai[7] = k + Dungeon.level.width();
            i = ai[Random.Int(ai.length)];
            if(Dungeon.level.heroFOV[i])
            {
                CellEmitter.get(i).start(Speck.factory(8), 0.07F, 10);
                Camera.main.shake(3F, 0.7F);
                Sample.INSTANCE.play("sound/snd_rocks.mp3");
                if(Dungeon.level.water[i])
                    GameScene.ripple(i);
                else
                if(Dungeon.level.map[i] == 1)
                {
                    Level.set(i, 20);
                    GameScene.updateMap(i);
                }
            }
            Char char1 = Actor.findChar(i);
            if(char1 != null && char1 != this)
                Buff.prolong( char1, Paralysis.class, 2 );
        }

        public void notice()
        {
            super.notice();
            BossHealthBar.assignBoss(this);
            yell(Messages.get(this, "notice"));
        }

        public void restoreFromBundle(Bundle bundle)
        {
            super.restoreFromBundle(bundle);
            BossHealthBar.assignBoss(this);
        }

        public DM300DeathBall()
        {
            spriteClass = DM275Sprite.class;
            HT = 220;
            HP = 220;
            EXP = 400;
            defenseSkill = 25;
            Buff.affect(this, ChampionEnemy.AntiMagic.class);
            Buff.affect(this, ChampionEnemy.Growing.class);
        }
    }

    public static class DM300SpiderMode extends Spinner
    {
        {
            immunities.add( ToxicGas.class );
            immunities.add( Terror.class );
            immunities.add( DiedBlobs.class );
        }
        public int attackSkill(Char char1)
        {
            return 26;
        }

        public boolean act()
        {
            GameScene.add(Blob.seed(pos, 30, DiedBlobs.class));
            return super.act();
        }

        @Override
        public int attackProc(Char var1, int var2) {
            byte var3 = 0;
            int var4;

            int var5 = super.attackProc(var1, var2);
            var4 = var1.pos;
            CellEmitter.center(var4).burst(BlastParticle.FACTORY, 30);
            GameScene.add(Blob.seed(var4, 2, Fire.class));
            int[] var7 = PathFinder.NEIGHBOURS9;
            int var6 = var7.length;

            for(var2 = var3; var2 < var6; ++var2) {
                int var8 = var7[var2];
                if (!Dungeon.level.solid[var4 + var8]) {
                    GameScene.add(Blob.seed(var4 + var8, 2, Fire.class));
                }
            }
            return var5;
        }

        @Override
        public void restoreFromBundle(Bundle b){
            super.restoreFromBundle(b);
            BossHealthBar.assignBoss(this);
            if (HP < 200) BossHealthBar.bleed(true);
        }


        @Override
        public void damage(int dmg, Object src) {
            super.damage(dmg, src);
            LockedFloor lock = Dungeon.hero.buff(LockedFloor.class);
            if (lock != null && !isImmune(src.getClass())) lock.addTime(dmg*1.5f);
        }

        public int damageRoll()
        {
            return Random.NormalIntRange(30, 50);
        }



        public void die(Object obj)
        {
            super.die(obj);
            obj = new DM300DeathBall();
            Dungeon.level.drop(new IronKey(Dungeon.depth), pos).sprite.drop();
            ((DM300DeathBall) obj).pos = pos;
            GameScene.add(((Mob) (obj)));
            Actor.addDelayed(new Pushing(((Char) (obj)), pos, ((DM300DeathBall) (obj)).pos), -1F);
            yell( Messages.get(this, "defeated") );
        }

        public int drRoll()
        {
            return Random.NormalIntRange(0, 8);
        }

        public void move(int i)
        {
            super.move(i);
            if(Dungeon.level.map[i] == 19 && HP < HT)
            {
                HP = HP + Random.Int(1, HT - HP);
                sprite.emitter().burst(ElmoParticle.FACTORY, 5);
                if(Dungeon.level.heroFOV[i] && Dungeon.hero.isAlive())
                    GLog.n(Messages.get(this, "repair"));
            }
            int[] ai = new int[8];
            int j = i - 1;
            ai[0] = j;
            int k = i + 1;
            ai[1] = k;
            ai[2] = i - Dungeon.level.width();
            ai[3] = i + Dungeon.level.width();
            ai[4] = j - Dungeon.level.width();
            ai[5] = j + Dungeon.level.width();
            ai[6] = k - Dungeon.level.width();
            ai[7] = k + Dungeon.level.width();
            i = ai[Random.Int(ai.length)];
            if(Dungeon.level.heroFOV[i])
            {
                CellEmitter.get(i).start(Speck.factory(8), 0.07F, 10);
                Camera.main.shake(3F, 0.7F);
                Sample.INSTANCE.play("sound/snd_rocks.mp3");
                if(Dungeon.level.water[i])
                    GameScene.ripple(i);
                else
                if(Dungeon.level.map[i] == 1)
                {
                    Level.set(i, 20);
                    GameScene.updateMap(i);
                }
            }
            Char char1 = Actor.findChar(i);
            if(char1 != null && char1 != this)
                Buff.prolong( char1, Paralysis.class, 2 );
        }

        public void notice()
        {
            super.notice();
            BossHealthBar.assignBoss(this);
            yell(Messages.get(this, "notice"));
        }

        public DM300SpiderMode ()
        {
            spriteClass = DM300SpiderSprite.class;
            HT = 240;
            HP = 240;
            EXP = 10;
            defenseSkill = 4;
            Buff.affect(this, ChampionEnemy.Halo.class);
            Buff.affect(this, ChampionEnemy.AntiMagic.class);
            maxLvl = -1;
        }
    }

    public DMZERO()
    {
        spriteClass = DM300Sprite.class;

        HP = HT = 200;
        EXP = 30;
        defenseSkill = 18;


        properties.add(Property.BOSS);
        properties.add(Property.INORGANIC);
    }

    public boolean act()
    {
        GameScene.add(Blob.seed(pos, 30, ToxicGas.class));
        return super.act();
    }

    public int attackSkill(Char char1)
    {
        return 20;
    }

    @Override
    public void damage(int dmg, Object src) {
        super.damage(dmg, src);
        LockedFloor lock = Dungeon.hero.buff(LockedFloor.class);
        if (lock != null && !isImmune(src.getClass())) lock.addTime(dmg*1.5f);
    }

    public int damageRoll()
    {

        return (HT/2 >= HP) ? Random.NormalIntRange(25, 35) : Random.NormalIntRange(15, 20);
    }

    public void die(Object obj)
    {
        super.die(obj);
        Dungeon.level.drop(new IronKey(Dungeon.depth), pos).sprite.drop();
        obj = new DM300AttackMode();
        ((DM300AttackMode) obj).pos = pos;
        GameScene.add(((Mob) (obj)));
        Actor.addDelayed(new Pushing(((Char) (obj)), pos, ((DM300AttackMode) (obj)).pos), -1F);

        yell( Messages.get(this, "defeated") );
    }

    public int drRoll()
    {
        return Random.NormalIntRange(0, 8);
    }

    public void move(int i)
    {
        super.move(i);
        if(Dungeon.level.map[i] == 19 && HP < HT)
        {
            HP = HP + Random.Int(1, HT - HP);
            sprite.emitter().burst(ElmoParticle.FACTORY, 5);
            if(Dungeon.level.heroFOV[i] && Dungeon.hero.isAlive())
                GLog.n(Messages.get(this, "repair"));
        }
        int[] ai = new int[8];
        int j = i - 1;
        ai[0] = j;
        int k = i + 1;
        ai[1] = k;
        ai[2] = i - Dungeon.level.width();
        ai[3] = i + Dungeon.level.width();
        ai[4] = j - Dungeon.level.width();
        ai[5] = j + Dungeon.level.width();
        ai[6] = k - Dungeon.level.width();
        ai[7] = k + Dungeon.level.width();
        i = ai[Random.Int(ai.length)];
        if(Dungeon.level.heroFOV[i])
        {
            CellEmitter.get(i).start(Speck.factory(8), 0.07F, 10);
            Camera.main.shake(3F, 0.7F);
            Sample.INSTANCE.play("sound/snd_rocks.mp3");
            if(Dungeon.level.water[i])
                GameScene.ripple(i);
            else
            if(Dungeon.level.map[i] == 1)
            {
                Level.set(i, 20);
                GameScene.updateMap(i);
            }
        }
        Char char1 = Actor.findChar(i);
        if(char1 != null && char1 != this)
            Buff.prolong( char1, Paralysis.class, 2 );
    }

    public void notice()
    {
        super.notice();
        BossHealthBar.assignBoss(this);
        yell(Messages.get(this, "notice"));
        Buff.affect(hero, RandomBuff.DiedBuff.class).set( (500), 1 );
    }

    public void restoreFromBundle(Bundle bundle)
    {
        super.restoreFromBundle(bundle);
        BossHealthBar.assignBoss(this);
    }

    //死亡国度
    public static class DiedBlobs extends ToxicGas{
        @Override
        public void use( BlobEmitter emitter ) {
            super.use( emitter );

            emitter.pour( Speck.factory( Speck.DIED ), 0.4f );
        }

        @Override
        protected void evolve() {
            super.evolve();

            int damage = 9 + Dungeon.depth/5;

            Char ch;
            int cell;

            for (int i = area.left; i < area.right; i++){
                for (int j = area.top; j < area.bottom; j++){
                    cell = i + j*Dungeon.level.width();
                    if (cur[cell] > 0 && (ch = Actor.findChar( cell )) != null) {
                        if (!ch.isImmune(this.getClass())) {
                            ch.damage(damage, this);
                        }
                    }
                }
            }
        }
    }
}