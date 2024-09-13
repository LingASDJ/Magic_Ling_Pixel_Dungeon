package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Frost;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;

public class IceDewVialSword extends MeleeWeapon {

    {
        image = ItemSpriteSheet.ICEDVS;

        tier = 6;
        DLY = 2f; //4x speed
    }

    @Override
    public int proc(Char attacker, Char defender, int damage) {        if(attacker instanceof Hero){
        for(Mob mob : ((Hero) attacker).visibleEnemiesList()){
            bolt(mob.pos, mob);
        }
    }
        return super.proc(attacker, defender, damage);
    }

    public void bolt(Integer target, final Mob mob){
        if (target != null) {

            final Ballistica shot = new Ballistica( Dungeon.hero.pos, target, Ballistica.PROJECTILE);

            fx(shot, () -> onHit(shot, mob));
        }
    }

    public void fx( Ballistica beam, Callback callback ) {
        MagicMissile.boltFromChar( curUser.sprite.parent,
                MagicMissile.WARD_CONE,
                curUser.sprite,
                beam.collisionPos,
                callback);
        Sample.INSTANCE.play( Assets.Sounds.ZAP );
    }

    protected void onHit(Ballistica bolt, Mob mob) {

        //presses all tiles in the AOE first

        if (mob != null){

            if (mob.isAlive() && bolt.path.size() > bolt.dist+1) {
                Ballistica trajectory = new Ballistica(mob.pos, bolt.path.get(bolt.dist + 1), Ballistica.MAGIC_BOLT);
                int strength = 11 + level();
                throwChar(mob, trajectory, strength);
                Buff.affect(mob, Frost.class, Frost.duration(mob));
            }
        }

    }

    public static void throwChar(final Char ch, final Ballistica trajectory, int power){
        int dist = Math.min(trajectory.dist, power);

        if (ch.properties().contains(Char.Property.BOSS))
            dist /= 2;

        if (dist == 0 || ch.properties().contains(Char.Property.IMMOVABLE)) return;

        if (Actor.findChar(trajectory.path.get(dist)) != null){
            dist--;
        }

        final int newPos = trajectory.path.get(dist);

        if (newPos == ch.pos) return;
    }

    @Override
    public int max(int lvl) {
        return  tier+           //5 base damage
                lvl;            //+1 per upgrade
    }

}

