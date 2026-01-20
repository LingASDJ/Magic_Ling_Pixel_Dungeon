package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.DwarfKing;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.MageHand;
import com.shatteredpixel.shatteredpixeldungeon.effects.Beam;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Lightning;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SparkParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfDisintegration;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfLightning;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfPrismaticLight;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfSun;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfTransfusion;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.hightwand.WandOfHightHunderStorm;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.ConeAOE;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;

public class MageHandSprite extends MobSprite {
    public MageHandSprite() {
        super();

        texture( Assets.Sprites.MAGEHAND );

        TextureFilm frames = new TextureFilm( texture, 24, 16 );

        idle = new Animation( 5, true );
        idle.frames( frames, 0,1,2,3,4,5);

        run = new Animation( 5, true );
        run.frames( frames, 0,1,2,3,4,5);

        attack = new Animation( 11, false );
        attack.frames( frames, 6,7,8,0 );

        die = new Animation( 11, false );
        die.frames( frames, 13,14,15,16,17);

        zap = new Animation(11,false);
        zap.frames(frames, 9,10,11,12);

        play( idle );
    }

    private int getWandMissileType(Wand wand) {
        if (wand != null) {
            return wand.getMissileType();
        }
        return MagicMissile.MAGIC_MISSILE;
    }

    public void zap( int cell ) {
        turnTo(ch.pos, cell);
        play(zap);

        MageHand mageHand = (MageHand) ch;
        Wand equippedWand = ((MageHand) ch).magesStaff != null ? mageHand.getEquippedMageStaffWand() : mageHand.getEquippedWand();

        int missileType;

        Char handuser = null;
        for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])){
            if (mob instanceof MageHand) {
                handuser = mob;
            }
        }
        if (equippedWand != null) {
            if(!equippedWand.cursed){
                missileType = getWandMissileType(equippedWand);
                if (equippedWand instanceof WandOfPrismaticLight || equippedWand instanceof WandOfSun){
                    if(equippedWand instanceof WandOfSun){
                        equippedWand.tryToZap(Dungeon.hero, cell);
                    }
                    mageHand.sprite.parent.add(
                            new Beam.LightRay(mageHand.sprite.center(), DungeonTilemap.raisedTileCenterToWorld(cell)));
                    mageHand.onZapComplete();
                } else if(equippedWand instanceof WandOfTransfusion) {
                    mageHand.sprite.parent.add(
                            new Beam.HealthRay(mageHand.sprite.center(), DungeonTilemap.raisedTileCenterToWorld(cell)));
                    mageHand.onZapComplete();
                } else if(equippedWand instanceof WandOfHightHunderStorm){
                    ((WandOfHightHunderStorm) equippedWand).affected.clear();
                    ((WandOfHightHunderStorm) equippedWand).arcs.clear();

                    // 4/6/8 distance
                    int maxDist = (1 + 2*equippedWand.chargesPerCast())* equippedWand.level/5+2;

                    ((WandOfHightHunderStorm) equippedWand).cone = new ConeAOE( new Ballistica(ch.pos, cell,Ballistica.STOP_TARGET),
                            maxDist,
                            30 + 40*((WandOfHightHunderStorm) equippedWand).chargesPerCast(),
                            ((WandOfHightHunderStorm) equippedWand).collisionProperties | Ballistica.STOP_TARGET);

                    //cast to cells at the tip, rather than all cells, better performance.
                    for (Ballistica ray : ((WandOfHightHunderStorm) equippedWand).cone.rays){
                        ((MagicMissile)handuser.sprite.parent.recycle( MagicMissile.class )).reset(
                                MagicMissile.ELMO,
                                handuser.sprite,
                                ray.path.get(ray.dist),
                                null
                        );
                    }

                    Char ch = Actor.findChar( cell );
                    if (ch != null) {
                        ((WandOfHightHunderStorm) equippedWand).affected.add( ch );
                        ((WandOfHightHunderStorm) equippedWand).arcs.add( new Lightning.Arc(handuser.sprite.center(), ch.sprite.center()));
                        ((WandOfHightHunderStorm) equippedWand).arc(ch);
                    } else {
                        ((WandOfHightHunderStorm) equippedWand).arcs.add( new Lightning.Arc(handuser.sprite.center(), DungeonTilemap.raisedTileCenterToWorld(cell)));
                        CellEmitter.center( cell ).burst( SparkParticle.FACTORY, 3 );
                    }

                    handuser.sprite.parent.addToFront( new Lightning( ((WandOfHightHunderStorm) equippedWand).arcs, null ) );
                    mageHand.onZapComplete();
                } else if(equippedWand instanceof WandOfLightning){
                    ((WandOfLightning) equippedWand).affected.clear();
                    ((WandOfLightning) equippedWand).arcs.clear();
                    Char ch = Actor.findChar( cell );
                    if (ch != null) {
                        if (ch instanceof DwarfKing){
                            Statistics.qualifiedForBossChallengeBadge = false;
                        }
                        ((WandOfLightning) equippedWand).affected.add( ch );
                        ((WandOfLightning) equippedWand).arcs.add( new Lightning.Arc(mageHand.sprite.center(), ch.sprite.center()));
                        ((WandOfLightning) equippedWand).arc(ch);
                    } else {
                        ((WandOfLightning) equippedWand).arcs.add( new Lightning.Arc(mageHand.sprite.center(), DungeonTilemap.raisedTileCenterToWorld(cell)));
                        CellEmitter.center( cell ).burst( SparkParticle.FACTORY, 3 );
                    }
                    mageHand.sprite.parent.addToFront( new Lightning(((WandOfLightning) equippedWand).arcs, null ) );
                    Sample.INSTANCE.play( Assets.Sounds.LIGHTNING );
                    mageHand.onZapComplete();
                } else if(equippedWand instanceof WandOfDisintegration){
                    mageHand.sprite.parent.add(new Beam.DeathRay(mageHand.sprite.center(), DungeonTilemap.raisedTileCenterToWorld( cell )));
                    mageHand.onZapComplete();
                } else {
                    MagicMissile.boltFromChar( parent,
                            missileType,
                            this,
                            cell,
                            new Callback() {
                                @Override
                                public void call() {
                                    mageHand.onZapComplete();
                                }
                            } );
                }
            } else {
                MagicMissile.boltFromChar( parent,
                        MagicMissile.RAINBOW_CONE,
                        this,
                        cell,
                        new Callback() {
                            @Override
                            public void call() {
                                mageHand.onZapComplete();
                            }
                        } );
            }
            Sample.INSTANCE.play( Assets.Sounds.ZAP );
        }
    }
}
