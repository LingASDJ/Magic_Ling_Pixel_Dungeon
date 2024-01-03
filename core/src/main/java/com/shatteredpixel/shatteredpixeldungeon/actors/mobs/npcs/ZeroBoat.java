package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;
import static com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfMagicMapping.discover;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Levitation;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.TrueInvisibiity;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.huntress.SpiritHawk;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.BloodBat;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.pets.Pets;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.DriedRose;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ZeroBoatSprite;
import com.watabou.noosa.Camera;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;

public class ZeroBoat extends NTNPC {

    {
        spriteClass = ZeroBoatSprite.class;
        properties.add(Char.Property.IMMOVABLE);
        flying = true;
    }

    private boolean first = false;
    private boolean secnod = false;
    protected boolean rd = false;

    private static final String FIRST = "first";
    private static final String SECNOD = "secnod";
    private static final String RD = "rd";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(FIRST, first);
        bundle.put(RD,rd);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        first = bundle.getBoolean(FIRST);
        secnod = bundle.getBoolean(SECNOD);
        rd = bundle.getBoolean(RD);
    }

    @Override
    protected boolean act() {

        throwItem();

        sprite.turnTo( pos, hero.pos );
        spend( TICK );
        return true;
    }

    @Override
    public void damage( int dmg, Object src ) {
    }

    @Override
    public int defenseSkill( Char enemy ) {
        return INFINITE_EVASION;
    }

    @Override
    public boolean reset() {
        return true;
    }

    @Override
    public boolean interact(Char c) {

        sprite.turnTo(pos, hero.pos);

        if( !first && rd ){
            Buff.affect( hero, Levitation.class, Levitation.DURATION );
            Buff.affect( hero, TrueInvisibiity.class, Levitation.DURATION );
            ScrollOfTeleportation.appear(hero,1357);
            ScrollOfTeleportation.appear(this,1357);
            Dungeon.observe();
            int length = Dungeon.level.length();
            int[] map = Dungeon.level.map;
            boolean[] mapped = Dungeon.level.mapped;
            boolean[] discoverable = Dungeon.level.discoverable;
            for (int i=0; i < length; i++) {

                int terr = map[i];

                if (discoverable[i]) {

                    mapped[i] = true;
                    if ((Terrain.flags[terr] & Terrain.SECRET) != 0) {

                        Dungeon.level.discover( i );

                        if (Dungeon.level.heroFOV[i]) {
                            GameScene.discoverTile( i, terr );
                            discover( i );
                        }
                    }
                }
            }

            int cell = 922;



            final int dest = 921;

            GameScene.updateFog();
            Dungeon.level.discover( 922 );
            hero.sprite.jump(1357, cell, new Callback() {
                @Override
                public void call() {
                    Dungeon.level.occupyCell(hero);
                    Dungeon.observe();
                    GameScene.updateFog();

                    Camera.main.shake(2, 0.5f);

                    hero.spendAndNext(1);
                    moves(dest);
                    int doorPos = 918;

                    for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])){
                        if (	mob instanceof Pets ||
                                mob instanceof DriedRose.GhostHero ||
                                mob instanceof BloodBat ||
                                mob instanceof SpiritHawk.HawkAlly) {
                            ScrollOfTeleportation.appear(mob, doorPos);
                        }
                    }
                }
            });
            sprite.jump(1357, cell, new Callback() {
                @Override
                public void call() {
                    move(cell);
                    Dungeon.level.occupyCell(c);
                    Dungeon.observe();
                    GameScene.updateFog();

                    Camera.main.shake(2, 0.5f);

                    hero.spendAndNext(1);

                }

            });

            hero.busy();
            first = true;
        } else if(first && rd) {
            first  = false;
            Buff.affect( hero, Levitation.class, Levitation.DURATION );
            Buff.affect( hero, TrueInvisibiity.class, Levitation.DURATION );
            ScrollOfTeleportation.appear(hero,922);
            hero.sprite.jump(922, 1388, new Callback() {
                @Override
                public void call() {
                    Dungeon.level.occupyCell(hero);
                    Dungeon.observe();
                    GameScene.updateFog();

                    Camera.main.shake(2, 0.5f);

                    hero.spendAndNext(1);
                    moves(1387);
                    int doorPos = 1449;

                    for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])){
                        if (	mob instanceof Pets ||
                                mob instanceof DriedRose.GhostHero ||
                                mob instanceof BloodBat ||
                                mob instanceof SpiritHawk.HawkAlly) {
                            ScrollOfTeleportation.appear(mob, doorPos);
                        }
                    }
                }
            });
            sprite.jump(922, 1388, new Callback() {
                @Override
                public void call() {
                    move(1388);
                    Dungeon.level.occupyCell(c);
                    Dungeon.observe();
                    GameScene.updateFog();

                    Camera.main.shake(2, 0.5f);

                    hero.spendAndNext(1);

                }
            });

            hero.busy();

        }

        return true;
    }

    public void moves(int step) {
        ScrollOfTeleportation.appear(hero, step);
        Buff.detach( hero, Levitation.class );
        Buff.detach( hero, TrueInvisibiity.class );
    }

}

