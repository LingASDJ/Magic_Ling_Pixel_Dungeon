package com.shatteredpixel.shatteredpixeldungeon.items.wands.hightwand;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.VenomGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Poison;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.VenomParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfArcana;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.DamageWand;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MagesStaff;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.utils.ColorMath;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class WandOfVenom extends DamageWand {

    {
        image = ItemSpriteSheet.SOMETHING;

        collisionProperties = Ballistica.STOP_TARGET | Ballistica.STOP_SOLID;
    }

    @Override
    public void onZap(Ballistica bolt) {
        Blob venomGas = Blob.seed((Integer) bolt.collisionPos, (int) (50 + 10 * ( level() + RingOfArcana.enchantPowerMultiplier(Dungeon.hero) )), VenomGas.class);
        CellEmitter.center(bolt.collisionPos).burst( VenomParticle.SPLASH, 10 );
        ((VenomGas)venomGas).setStrength((int) (( level() + RingOfArcana.enchantPowerMultiplier(Dungeon.hero) )+3));
        GameScene.add(venomGas);

        for (int i : PathFinder.NEIGHBOURS9) {
            Char ch = Actor.findChar(bolt.collisionPos + i);
            if (ch != null) {
                processSoulMark(ch, chargesPerCast());
            }
        }
    }

    @Override
    public void onHit(MagesStaff staff, Char attacker, Char defender, int damage) {
        // lvl 0 - 33%
        // lvl 1 - 50%
        // lvl 2 - 60%
        if (Random.Int( level() + 3 ) >= 2) {

            Buff.affect( defender, Poison.class ).set( 11f );
            CellEmitter.center(defender.pos).burst( VenomParticle.SPLASH, 5 );

        }
    }



    @Override
    public void staffFx(MagesStaff.StaffParticle particle) {
        particle.color( ColorMath.random( 0x00FF00, 0x8844FF) );
        particle.am = 0.6f;
        particle.setLifespan( 1f );
        particle.acc.set(0, 20);
        particle.setSize( 0.5f, 3f );
        particle.shuffleXY( 1f );
    }

    public int min(int lvl){
        return (1+lvl) * chargesPerCast();
    }

    //2/8/18 base damage with 2/4/6 scaling based on charges used
    public int max(int lvl){
        switch (chargesPerCast()){
            case 1: default:
                return 2 + 2*lvl;
            case 2:
                return 2*(4 + 2*lvl);
            case 3:
                return 3*(6+2*lvl);
        }
    }
}