package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses;
import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;
import static com.shatteredpixel.shatteredpixeldungeon.Statistics.lanterfireactive;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.Ratmogrify;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.sprites.RatSprite;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DimandKingSprite;

public class DimandKing extends Mob {

    {
        spriteClass = DimandKingSprite.class;
        
        HP = HT = 8;
        defenseSkill = 2;
        
        maxLvl = 5;
    }

    @Override
    public int attackProc( Char enemy, int damage ) {
        damage = super.attackProc( enemy, damage );
    if (lanterfireactive) {
            if (Random.Float()<=0.05f && enemy instanceof Hero && hero.lanterfire < 85) {
                ((Hero) enemy).damageLantern(1);
                hero.sprite.showStatus( 0x808080, "1");
            }
        } else {
            super.attackProc( enemy, damage );
        }
        return damage;
    }


    @Override
    protected boolean act() {
        if (Dungeon.level.heroFOV[pos] && hero.armorAbility instanceof Ratmogrify){
            alignment = Alignment.ALLY;
            if (state == SLEEPING) state = WANDERING;
        }
        return super.act();
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange( 1, 4 );
    }
    
    @Override
    public int attackSkill( Char target ) {
        return 8;
    }
    
    @Override
    public int drRoll() {
        return Random.NormalIntRange(0, 1);
    }

    private static final String RAT_ALLY = "rat_ally";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        if (alignment == Alignment.ALLY) bundle.put(RAT_ALLY, true);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        if (bundle.contains(RAT_ALLY)) alignment = Alignment.ALLY;
    }
}
