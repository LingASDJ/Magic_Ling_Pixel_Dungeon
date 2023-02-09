package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses;

import static com.shatteredpixel.shatteredpixeldungeon.Statistics.crivusfruitslevel2;
import static com.shatteredpixel.shatteredpixeldungeon.levels.ForestBossLevel.BRatKingRoom;
import static com.shatteredpixel.shatteredpixeldungeon.levels.ForestBossLevel.ForestBossLasherTWOPos;
import static com.shatteredpixel.shatteredpixeldungeon.levels.ForestBossLevel.RatKingRoom;
import static com.shatteredpixel.shatteredpixeldungeon.levels.ForestBossLevel.WIDTH;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Level.set;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Amok;
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
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.DriedRose;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.CrystalKey;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.IronKey;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CrivusFruitsSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;

//克里弗斯之果 本体
public class CrivusFruits extends Mob {

    //基本属性
    {
        spriteClass = CrivusFruitsSprite.class;

        HP = HT = 120;
        defenseSkill = 4;

        EXP = 20;

        state = PASSIVE;

        properties.add(Property.IMMOVABLE);
        properties.add(Property.MINIBOSS);
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
            this.sprite.showStatus(CharSprite.NEGATIVE, "!!!");
            for (Char ch : Actor.chars()){
                if (ch instanceof DriedRose.GhostHero){
                    ((DriedRose.GhostHero) ch).sayBoss();
                }
            }
        }
    }


    @Override
    protected boolean act() {
        //毒雾扩散
        if(!crivusfruitslevel2){
            GameScene.add(Blob.seed(pos, HP<65 ? 50 : 30, DiedBlobs.class));
        } else {
            GameScene.add(Blob.seed(pos, HP<36 ? 100 : 50, DiedBlobs.class));
        }


        //判定是否第一次加进入游戏
        if( Dungeon.hero.buff(LockedFloor.class) != null){
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

        }

        //三阶段
        if(HP==36){
            GameScene.flash(0x80009c9c);
            HP=HT=35;
            GLog.n(Messages.get(this,"died!!!"));
            GLog.w(Messages.get(this,"!!!"));
            Sample.INSTANCE.play( Assets.Sounds.CHALLENGE );
            this.sprite.showStatus(CharSprite.NEGATIVE, "!!!");
        }

        return super.act();
    }

    //红雾弥漫
    public static class DiedBlobs extends Blob implements Hero.Doom {

        //Boss死亡后改变描述
        @Override
        public String tileDesc() {
            return Messages.get(this, Dungeon.hero.buff(LockedFloor.class) != null? "desc" : "csed" );
        }
        @Override
        protected void evolve() {
            super.evolve();

            int damage = 4;

            Char ch;
            int cell;

            for (int i = area.left; i < area.right; i++){
                for (int j = area.top; j < area.bottom; j++){
                    cell = i + j*Dungeon.level.width();
                    if (cur[cell] > 0 && (ch = Actor.findChar( cell )) != null) {
                        if (!ch.isImmune(this.getClass())) {
                            if( Dungeon.hero.buff(LockedFloor.class) != null) {
                                //不为空为4 否则就是0
                                ch.damage(Dungeon.hero.buff(LockedFloor.class) != null ? damage : 0, this);
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
        Dungeon.level.drop( new CrystalKey( Dungeon.depth ), pos ).sprite.drop();
        Dungeon.level.drop( new IronKey( Dungeon.depth ), pos-1 ).sprite.drop();
        Dungeon.level.drop( new IronKey( Dungeon.depth ), pos+1 ).sprite.drop();
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