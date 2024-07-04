package com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ElementalBuff.BaseBuff.ScaryBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfBlastWave;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.watabou.noosa.tweeners.AlphaTweener;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class TimeReset extends Weapon.Enchantment {

    private static ItemSprite.Glowing BLUEISH = new ItemSprite.Glowing( 0xAA7B47 );

    private ArrayList<Mob> mobs = new ArrayList<>();
    private ArrayList<Integer> mobpos = new ArrayList<>();

    @Override
    public int proc(Weapon weapon, Char attacker, Char defender, int damage ) {
        int level = Math.max( 0, weapon.buffedLvl() );

        Buff.affect(defender, ScaryBuff.class).set((100), 1);
        ScaryBuff existingX = defender.buff(ScaryBuff.class);
        if (existingX != null) {
            existingX.damgeScary(Random.Int(2, 6)*level == 0?1:level);
        }

        if(mobs.isEmpty() && !mobpos.isEmpty()){
            mobpos.clear();
        }
        if(!mobs.isEmpty() && mobpos.isEmpty()){
            mobs.clear();
        }

        if(mobs.isEmpty() && mobpos.isEmpty()){
            for(Mob mob : Dungeon.level.mobs.toArray(new Mob[0])){
                if(Dungeon.level.heroFOV[mob.pos]){
                    mobs.add(mob);
                    mobpos.add(mob.pos);
                }
            }
        } else {
            for(int m1 = mobs.size()-1; m1 > 0; m1--){
                if(mobs.get(m1) != null && mobpos.get(m1) != null){
                    if(Actor.findChar(mobpos.get(m1)) == null){
                        appear(mobs.get(m1), mobpos.get(m1));

                        Buff.affect( mobs.get(m1), ScaryBuff.class ).set( (100), 1 );
                        ScaryBuff existing = mobs.get(m1).buff(ScaryBuff.class);
                        if (existing != null){
                            existing.damgeScary(Random.Int(3,8));
                        }
                    }
                }
            }
            mobs.clear();
            mobpos.clear();
        }

        if(attacker instanceof Hero){
            if(((Hero) attacker).belongings.armor.glyph != null){
                comboProc(this, ((Hero) attacker).belongings.armor.glyph, attacker, defender, damage);
            }
        }

        return damage;

    }

    public void appear( Char ch, int pos ) {
        if (ch.invisible == 0) {

            try {
                ch.sprite.interruptMotion();
                ch.move( pos );
                ch.sprite.place( pos );
                ch.sprite.alpha( 0 );
                int target = ch.pos;
                int oppositeAdjacent = target + (target - pos);
                Ballistica trajectory = new Ballistica(target, oppositeAdjacent, Ballistica.MAGIC_BOLT);
                WandOfBlastWave.throwChar(ch, trajectory, 2, false, false, this);

                ch.sprite.parent.add( new AlphaTweener( ch.sprite, 1, 0.4f ) );
            } catch (Exception e) {
                //
            }

        }

    }

    @Override
    public ItemSprite.Glowing glowing() {
        return BLUEISH;
    }
}
