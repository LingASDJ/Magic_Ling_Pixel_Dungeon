package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Fire;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Poison;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SalamanderSprites;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class Salamander extends Mob {

    {
        spriteClass = SalamanderSprites.class;
        EXP = 5;
        HP = HT = 5;
        defenseSkill = 25;

        loot = Generator.Category.SEED;
        lootChance = 0.05f;

        maxLvl = 12;
    }

    public static class DarkBolt{}

    private void zap() {
        spend( 1f );

        if(enemy == null){
            return;
        }

        if (hit( this, enemy, true )) {
            int dmg = Random.NormalIntRange(2, 4);
            enemy.damage( dmg, new DarkBolt() );
        } else {
            enemy.sprite.showStatus( CharSprite.NEUTRAL,  enemy.defenseVerb() );
        }

        if (enemy == Dungeon.hero && !enemy.isAlive()) {
            Dungeon.fail( getClass() );
            GLog.n( Messages.capitalize(Messages.get(Char.class, "kill", name())) );
        }
    }

    public void onZapComplete() {
        zap();
        next();
    }

    private int combo = 0;

    @Override
    public int attackSkill( Char target ) {
        return 16;
    }

    @Override
    protected boolean canAttack( Char enemy ) {
        if(Dungeon.level.distance(pos,target)>3)
            return false;
        Ballistica attack = new Ballistica( pos, enemy.pos, Ballistica.PROJECTILE);
        return !Dungeon.level.adjacent(pos, enemy.pos) && attack.collisionPos == enemy.pos;
    }

    //todo Ghost Quest Mob-2
    @Override
    public int attackProc( Char enemy, int damage ) {
        damage = super.attackProc( enemy, damage );
        //The gnoll's attacks get more severe the more the player lets it hit them
        combo++;
        int effect = Random.Int(4)+combo;

        if (effect > 2) {

            if (effect >=6 && enemy.buff(Burning.class) == null){

                if (Dungeon.level.flamable[enemy.pos])
                    GameScene.add(Blob.seed(enemy.pos, 4, Fire.class));
                Buff.affect(enemy, Burning.class).reignite( enemy );

            } else
                Buff.affect( enemy, Poison.class).set((effect-2) );
        }
        return damage;
    }

    @Override
    protected boolean getCloser( int target ) {
        combo = 0; //if he's moving, he isn't attacking, reset combo.
        if (state == HUNTING) {
            if(Dungeon.level.distance(pos,target)>3)
                return super.getCloser( target );
            return enemySeen && getFurther( target );
        } else {
            return super.getCloser( target );
        }
    }

    @Override
    public void aggro(Char ch) {
        //cannot be aggroed to something it can't see
        if (ch == null || fieldOfView == null || fieldOfView[ch.pos]) {
            super.aggro(ch);
        }
    }

    private static final String COMBO = "combo";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle(bundle);
        bundle.put(COMBO, combo);
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        combo = bundle.getInt( COMBO );
    }

}
