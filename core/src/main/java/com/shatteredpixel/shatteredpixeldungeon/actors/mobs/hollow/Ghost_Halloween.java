package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.hollow;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ElementalBuff.BaseBuff.ScaryBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GhostHalloweenSprite;

public class Ghost_Halloween extends Mob {

    private int invisible;
    public boolean activeLook;

    {
        spriteClass = GhostHalloweenSprite.class;

        HP = HT = 60;
        EXP = 19;
        flying = true;
        defenseSkill = 45;
        maxLvl = 35;
        properties.add(Char.Property.HOLLOW);
    }

    @Override
    public boolean act() {

        //yell(String.valueOf(invisible));

        if(invisible<5 && !activeLook){
            invisible++;
            ((GhostHalloweenSprite)sprite).lookGhost(this);
        } else if(invisible >= 6) {
            activeLook = true;
            ((GhostHalloweenSprite)sprite).lookGhost(this);
            invisible = 0;
        } else {
            activeLook = false;
            invisible++;
        }

        return super.act();
    }

    @Override
    public boolean attack(Char enemy, float dmgMulti, float dmgBonus, float accMulti,DamageType type ) {
        boolean result = super.attack( enemy, dmgMulti, dmgBonus, accMulti, type );
        if(enemy !=null && enemy == hero) {
            for (Buff buff : hero.buffs()) {
                if (buff instanceof ScaryBuff && invisible<6) {
                    sprite.showStatus(CharSprite.NEGATIVE, Messages.get(this,"hello"));
                    ((ScaryBuff) buff).damgeScary(8);
                }  else {
                    Buff.affect(enemy, ScaryBuff.class).set((100), 5);
                }
            }
        }
        return result;
    }


    @Override
    public int damageRoll() {
        return 0;
    }

    @Override
    public int attackSkill(Char target) {
        return 35;
    }

}
