package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.spical;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.CorrosiveGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Fire;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.ArcaneBomb;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Bomb;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Firebomb;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Flashbang;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.FrostBomb;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.ShrapnelBomb;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.RedTrap;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DM275Sprite;
import com.watabou.utils.BArray;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class DM275 extends Mob {

    {
        spriteClass = DM275Sprite.class;

        HP = HT = 120;
        defenseSkill = 12;

        flying = true;

        EXP = 15;
        maxLvl = 30;

        loot = Random.oneOf(Generator.Category.WEAPON, Generator.Category.ARMOR);
        lootChance = 0.125f; //initially, see lootChance()

        properties.add(Property.INORGANIC);
        properties.add(Property.LARGE);
        immunities.add(ToxicGas.class);
        immunities.add(CorrosiveGas.class);
        HUNTING = new Hunting();
    }

    @Override
    public int damageRoll() {
        return Char.combatRoll( 24, 32 );
    }

    @Override
    public int attackSkill( Char target ) {
        return 20;
    }

    @Override
    public int drRoll() {
        return super.drRoll() + Random.NormalIntRange(6, 8);
    }

    @Override
    public float lootChance(){
        //each drop makes future drops 1/2 as likely
        // so loot chance looks like: 1/8, 1/16, 1/32, 1/64, etc.
        return super.lootChance() * (float)Math.pow(1/2f, Dungeon.LimitedDrops.DM200_EQUIP.count);
    }

    public Item createLoot() {
        Dungeon.LimitedDrops.DM200_EQUIP.count++;
        //uses probability tables for dwarf city
        if (loot == Generator.Category.WEAPON){
            return Generator.randomWeapon(4, true);
        } else {
            return Generator.randomArmor(4);
        }
    }

    private int ventCooldown = 0;

    private static final String VENT_COOLDOWN = "vent_cooldown";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(VENT_COOLDOWN, ventCooldown);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        ventCooldown = bundle.getInt( VENT_COOLDOWN );
    }

    @Override
    protected boolean act() {
        ventCooldown--;
        return super.act();
    }

    public void onZapComplete(){
        zap();
        next();
    }

    public Bomb missile = new Firebomb();
    private Bomb missile() {
        switch (Random.Int(11)) {
            case 1:
                missile = new ArcaneBomb();
                break;
            case 2:
                missile = new Firebomb();
                break;
            case 3:
                missile = new Flashbang();
                break;
            case 4:
                missile = new FrostBomb();
                break;
            default:
            case 8:
                missile = new ShrapnelBomb();
                break;
        }
        return missile;
    }

    @Override
    public void die(Object cause) {
        super.die(cause);
        int blobs = Random.chances(new float[]{0, 0, 6, 3, 1});
        for (int i = 0; i < blobs; i++){
            int ofs;
            do {
                ofs = PathFinder.NEIGHBOURS8[Random.Int(8)];
            } while (!Dungeon.level.passable[pos + ofs]);
            Dungeon.level.drop( missile(), pos + ofs ).sprite.drop( pos );
        }
    }


    private void toss() {
        spend( 1f );

        RedTrap trapx = new RedTrap();
        trapx.pos = super.pos;
        trapx.activate();

        if (hit( this, enemy, true )) {
            Bomb missile = missile();
            missile.isLit = true;
            Bomb.Fuse fuse = new Bomb.Fuse();
            missile.fuse = fuse;
            GameScene.add(Blob.seed(enemy.pos, 20, Fire.class));
            Actor.addDelayed(missile.fuse = missile.createFuse().ignite(missile), 2);
            Dungeon.level.drop( missile,enemy.pos );
        } else {
            enemy.sprite.showStatus( CharSprite.NEUTRAL, enemy.defenseVerb() );
        }
    }

    public void onTossComplete() {
        toss();
        next();
    }

    protected boolean doAttack( Char enemy ) {
        if (Dungeon.level.adjacent( pos, enemy.pos )) {

            return super.doAttack( enemy );

        } else {
            boolean visible = fieldOfView[pos] || fieldOfView[enemy.pos];


            if (missile.quantity() > -1) {
                if (visible) {
                    sprite.toss( enemy.pos );
                } else {
                    toss();
                }
                //missile.quantity--;
            }

            return !visible;
        }
    }

    private void zap( ){
        spend( TICK );
        ventCooldown = 30;

        if(enemy!= null){
            Ballistica trajectory = new Ballistica(pos, enemy.pos, Ballistica.STOP_TARGET);

            for (int i : trajectory.subPath(0, trajectory.dist)){
                GameScene.add(Blob.seed(i, 20, ToxicGas.class));
                GameScene.add(Blob.seed(i, 20, CorrosiveGas.class));
            }
            GameScene.add(Blob.seed(trajectory.collisionPos, 100, ToxicGas.class));

        }

    }

    protected boolean canVent(int target){
        if (ventCooldown > 0) return false;
        PathFinder.buildDistanceMap(target, BArray.not(Dungeon.level.solid, null), Dungeon.level.distance(pos, target)+1);
        //vent can go around blocking terrain, but not through it
        if (PathFinder.distance[pos] == Integer.MAX_VALUE){
            return false;
        }
        return true;
    }

    private class Hunting extends Mob.Hunting{

        @Override
        public boolean act(boolean enemyInFOV, boolean justAlerted) {
            if (!enemyInFOV || canAttack(enemy)) {
                return super.act(enemyInFOV, justAlerted);
            } else {
                enemySeen = true;
                target = enemy.pos;

                int oldPos = pos;

                if (distance(enemy) >= 1 && Random.Int(100/distance(enemy)) == 0 && canVent(target)){
                    if (sprite != null && (sprite.visible || enemy.sprite.visible)) {
                        sprite.zap( enemy.pos );
                        return false;
                    } else if(enemy!=null) {
                        zap();
                        return true;
                    } else {
                        spend( TICK );
                        return true;
                    }

                } else if (getCloser( target )) {
                    spend( 1 / speed() );
                    return moveSprite( oldPos,  pos );

                } else if (canVent(target)) {
                    if (sprite != null && (sprite.visible || enemy.sprite.visible)) {
                        sprite.zap( enemy.pos );
                        return false;
                    } else if(enemy!=null) {
                        zap();
                        return true;
                    } else {
                        spend( TICK );
                        return true;
                    }

                } else {
                    spend( TICK );
                    return true;
                }

            }
        }
    }

}