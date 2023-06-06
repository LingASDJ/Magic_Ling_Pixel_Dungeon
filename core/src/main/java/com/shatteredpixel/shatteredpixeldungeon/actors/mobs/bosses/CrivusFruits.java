package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;
import static com.shatteredpixel.shatteredpixeldungeon.Statistics.crivusfruitslevel2;
import static com.shatteredpixel.shatteredpixeldungeon.levels.ForestBossLevel.BRatKingRoom;
import static com.shatteredpixel.shatteredpixeldungeon.levels.ForestBossLevel.ForestBossLasherTWOPos;
import static com.shatteredpixel.shatteredpixeldungeon.levels.ForestBossLevel.RatKingRoom;
import static com.shatteredpixel.shatteredpixeldungeon.levels.ForestBossLevel.WIDTH;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Level.set;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Conducts;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.CorrosiveGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Amok;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Dread;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LockedFloor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Sleep;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.BlobEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.DriedRose;
import com.shatteredpixel.shatteredpixeldungeon.items.food.CrivusFruitsFood;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Food;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.CrystalKey;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.IronKey;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.CrivusFruitsFlake;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.LifeTreeSword;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.ConeAOE;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CrivusFruitsSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.HashSet;

//克里弗斯之果 本体
public class CrivusFruits extends Mob {
    //the actual affected cells
    private HashSet<Integer> affectedCells;
    //the cells to trace fire shots to, for visual effects.
    private HashSet<Integer> visualCells;
    //基本属性
    {
        spriteClass = CrivusFruitsSprite.class;

        HP = HT = 120;
        defenseSkill = 4;

        EXP = 20;

        state = WANDERING;

        properties.add(Property.IMMOVABLE);
        properties.add(Property.BOSS);
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
            for (Mob m : Dungeon.level.mobs){
                if (m instanceof CrivusFruits){
                    m.damage((int)7.5, this);
                }
            }
        }
    }

    @Override
    public boolean isInvulnerable(Class effect) {
        return this.HP > 36 && effect != DiedDamager.class;
    }

    //对话框架
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

    //回合
    @Override
    public void damage(int dmg, Object src) {
        LockedFloor lock = hero.buff(LockedFloor.class);
        if (lock != null) lock.addTime(dmg * 2);
        super.damage(dmg, src);
    }


    @Override
    protected boolean act() {
        //毒雾扩散
        if(!crivusfruitslevel2){
            GameScene.add(Blob.seed(pos, HP<65 ? 50 : 30, DiedBlobs.class));
        } else {
            if(Dungeon.isChallenged(Challenges.STRONGER_BOSSES)){
                sprite.attack( hero.pos );
                spend( 25f );
                shoot(this, hero.pos);
            }
            GameScene.add(Blob.seed(pos, HP<36 ? 150 : 50, DiedBlobs.class));
        }


        //判定是否第一次加进入游戏
        if( hero.buff(LockedFloor.class) != null){
            notice();
        }
        state = PASSIVE;

        //二阶段
        if( HP<65 && !crivusfruitslevel2){
            HP=HT=64;
            GLog.n(Messages.get(this,"anargy"));
            crivusfruitslevel2 = true;
            GameScene.flash(0x808c8c8c);
            for (int i : ForestBossLasherTWOPos) {
                CrivusFruitsLasher csp = new CrivusFruitsLasher();
                csp.pos = i;
                GameScene.add(csp);
                if(Dungeon.isChallenged(Challenges.STRONGER_BOSSES)){
                    Buff.affect(csp, Barrier.class).setShield((int) (2.4f * csp.HT + 10));
                }
            }
            Sample.INSTANCE.play( Assets.Sounds.CHALLENGE );
            this.sprite.showStatus(CharSprite.NEGATIVE, "!!!");

            //在二阶段就应该构建好房间
            //A号鼠王房间
            for (int i : RatKingRoom) {
                set( i, Terrain.WALL );
                GameScene.updateMap( i );
                set( WIDTH*11+6, Terrain.DOOR );
                GameScene.updateMap( WIDTH*11+6 );
            }
            //B号鼠王房间
            for (int i : BRatKingRoom) {
                set( i, Terrain.WALL );
                GameScene.updateMap( i );
                set( WIDTH*11+25, Terrain.DOOR );
                GameScene.updateMap( WIDTH*11+25 );
            }


            if(Dungeon.isChallenged(Challenges.STRONGER_BOSSES)){
                sprite.attack( hero.pos );
                spend( 3f );
                shoot(this, hero.pos);
            }


        }

        //三阶段
        if(HP==36){
            GameScene.flash(0x80009c9c);
            HP=HT=35;
            if(Dungeon.isChallenged(Challenges.STRONGER_BOSSES)){
                Buff.affect(this, Barrier.class).setShield((int) (3f * this.HT + 10));
            }
            GLog.n(Messages.get(this,"died!!!"));
            GLog.w(Messages.get(this,"!!!"));
            Sample.INSTANCE.play( Assets.Sounds.CHALLENGE );
            this.sprite.showStatus(CharSprite.NEGATIVE, "!!!");
            if(Dungeon.isChallenged(Challenges.STRONGER_BOSSES)){
                sprite.attack( hero.pos );
                spend( 3f );
                shoot(this, hero.pos);
            }
        }

        return super.act();
    }

    //红雾弥漫
    public static class DiedBlobs extends Blob implements Hero.Doom {

        //Boss死亡后改变描述
        @Override
        public String tileDesc() {
            return Messages.get(this, hero.buff(LockedFloor.class) != null? "desc" : "csed" );
        }
        @Override
        protected void evolve() {
            super.evolve();

            int damage = 6;

            Char ch;
            int cell;

            for (int i = area.left; i < area.right; i++){
                for (int j = area.top; j < area.bottom; j++){
                    cell = i + j*Dungeon.level.width();
                    if (cur[cell] > 0 && (ch = Actor.findChar( cell )) != null) {
                        if (!ch.isImmune(this.getClass())) {
                            if( hero.buff(LockedFloor.class) != null) {
                                //不为空为4 否则就是0
                                ch.damage(hero.buff(LockedFloor.class) != null ? damage : 0, this);
                                Statistics.bossScores[0] -= 200;
                            }
                        }
                    }
                }
            }
        }

        @Override
        public void use( BlobEmitter emitter ) {
            super.use( emitter );

            emitter.pour( Speck.factory( Speck.DIED ), 0.4f );
        }

        @Override
        public void onDeath() {
            //
        }
    }

    @Override
    public void beckon(int cell) {
        //do nothing
    }

    @Override
    public void die(Object cause) {
        super.die(cause);
        Dungeon.level.unseal();
        GameScene.bossSlain();
        GLog.n(Messages.get(this,"dead"));
        Dungeon.level.drop( new CrystalKey( Dungeon.depth ), pos-2 ).sprite.drop();
        Dungeon.level.drop( new IronKey( Dungeon.depth ), pos-1 ).sprite.drop();
        Dungeon.level.drop( new IronKey( Dungeon.depth ), pos+1 ).sprite.drop();
        Badges.validateBossSlain();
        Statistics.bossScores[0] += 1000;

        if (!Badges.isUnlocked(Badges.Badge.KILL_APPLE)){
            Dungeon.level.drop( new LifeTreeSword(), pos ).sprite.drop();
        } else if (Random.Float()<0.4f) {
            Dungeon.level.drop( new LifeTreeSword(), pos ).sprite.drop();
        } else {
            Dungeon.level.drop( new Food(), pos ).sprite.drop();
        }

        Badges.KILLSAPPLE();

        int blobs = Random.chances(new float[]{0, 0, 6, 3, 1});
        for (int i = 0; i < blobs; i++){
            int ofs;
            do {
                ofs = PathFinder.NEIGHBOURS8[Random.Int(3)];
            } while (!Dungeon.level.passable[pos + ofs]);
            Dungeon.level.drop( new CrivusFruitsFood(), pos + ofs ).sprite.drop( pos );
        }

        int flakes = Random.chances(new float[]{0, 0, 6, 3, 1});
        for (int i = 0; i < flakes; i++){
            int ofs;
            do {
                ofs = PathFinder.NEIGHBOURS8[Random.Int(4)];
            } while (!Dungeon.level.passable[pos + ofs]);
            Dungeon.level.drop( new CrivusFruitsFlake(), pos + ofs ).sprite.drop( pos );
        }

        if (Dungeon.isDLC(Conducts.Conduct.BOSSRUSH)) {
            GetBossLoot();
        }
    }

    public void shoot(Char ch, int pos){
        final Ballistica shot = new Ballistica( hero.pos, pos, Ballistica.MAGIC_BOLT);
        fx(shot, ch);
    }
    ConeAOE cone;

    protected void fx(Ballistica bolt, Char ch ) {
        //need to perform flame spread logic here so we can determine what cells to put flames in.
        affectedCells = new HashSet<>();
        visualCells = new HashSet<>();

        int maxDist = 2 + 4*4;
        int dist = Math.min(bolt.dist, maxDist);

        for (int i = 0; i < PathFinder.CIRCLE8.length; i++){
            if (bolt.sourcePos+PathFinder.CIRCLE8[i] == bolt.path.get(1)){
                break;
            }
        }

        cone = new ConeAOE( bolt,
                maxDist,
                80 + 40,Ballistica.MAGIC_BOLT);

        visualCells.remove(bolt.path.get(dist));

        for (Ballistica ray : cone.rays){
            ((MagicMissile)ch.sprite.parent.recycle( MagicMissile.class )).reset(
                    MagicMissile.SHADOW_CONE,
                    ch.sprite,
                    ray.path.get(ray.dist),
                    null
            );
        }

        GameScene.add(Blob.seed(hero.pos, 120, CorrosiveGas.class));

        if(Dungeon.level.heroFOV[bolt.sourcePos] || Dungeon.level.heroFOV[bolt.collisionPos]){
            Sample.INSTANCE.play( Assets.Sounds.ZAP );
        }
    }


    {
        immunities.add( Paralysis.class );
        immunities.add( Amok.class );
        immunities.add( Sleep.class );
        immunities.add( ToxicGas.class );
        immunities.add( DiedBlobs.class );
        immunities.add( Terror.class );
        immunities.add( Dread.class );
        immunities.add( Vertigo.class );
    }

}