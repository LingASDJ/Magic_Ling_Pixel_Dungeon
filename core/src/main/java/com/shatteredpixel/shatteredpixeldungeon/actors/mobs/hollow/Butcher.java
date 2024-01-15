package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.hollow;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ElementalBuff.BaseBuff.ScaryBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ButcherSprite;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Butcher extends Mob {

    private int blinkCooldown = 0;

    @Override
    protected boolean getCloser( int target ) {
        if (fieldOfView[target] && Dungeon.level.distance( pos, target ) > 2 && blinkCooldown <= 0 && !rooted) {

            if (blink( target )) {
                spend(-1 / speed());
                return true;
            } else {
                return false;
            }

        } else {

            blinkCooldown--;
            return super.getCloser( target );

        }
    }

    {
        spriteClass = ButcherSprite.class;

        HP = HT = 80;
        defenseSkill = 18;

        maxLvl = 35;

        EXP = 16;

        properties.add(Property.HOLLOW);
    }

    private boolean blink( int target ) {

        Ballistica route = new Ballistica( pos, target, Ballistica.PROJECTILE);
        int cell = route.collisionPos;

        //can't occupy the same cell as another char, so move back one.
        if (Actor.findChar( cell ) != null && cell != this.pos)
            cell = route.path.get(route.dist-1);

        if (Dungeon.level.avoid[ cell ] || (properties().contains(Property.LARGE) && !Dungeon.level.openSpace[cell])){
            ArrayList<Integer> candidates = new ArrayList<>();
            for (int n : PathFinder.NEIGHBOURS8) {
                cell = route.collisionPos + n;
                if (Dungeon.level.passable[cell]
                        && Actor.findChar( cell ) == null
                        && (!properties().contains(Property.LARGE) || Dungeon.level.openSpace[cell])) {
                    candidates.add( cell );
                }
            }
            if (candidates.size() > 0)
                cell = Random.element(candidates);
            else {
                blinkCooldown = Random.IntRange(4, 6);
                return false;
            }
        }

        ScrollOfTeleportation.appear( this, cell );

        blinkCooldown = Random.IntRange(4, 6);
        return true;
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange( 30, 50 );
    }

    @Override
    public int attackProc( Char enemy, int damage ) {
        damage = super.attackProc( enemy, damage );


        if(enemy!=null && enemy == hero) {
            for (Buff buff : hero.buffs()) {
                if (buff instanceof ScaryBuff) {
                    //5损伤
                    ((ScaryBuff) buff).damgeScary( 5);
                } else {
                    //未寻找到元损立刻附加
                    Buff.affect( enemy, ScaryBuff.class ).set( (100), 1 );
                }
            }
        }
        return damage;
    }

    @Override
    public int attackSkill( Char target ) {
        return 20;
    }

    private static final String BLINK_CD = "blink_cd";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(BLINK_CD, blinkCooldown);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        blinkCooldown = bundle.getInt(BLINK_CD);
    }

    @Override
    public int drRoll() {
        return 6;
    }

}
