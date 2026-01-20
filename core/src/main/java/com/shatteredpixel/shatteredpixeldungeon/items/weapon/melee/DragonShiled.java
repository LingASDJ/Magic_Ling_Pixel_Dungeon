package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.utils.Random;

public class DragonShiled extends MeleeWeapon {

    {
        image = ItemSpriteSheet.DRAGONSHILED;
        tier = 5;
    }


    @Override
    public int proc(Char attacker, Char defender, int damage ) {
        switch (Random.Int(6)) {
            case 0:case 1:case 2:case 3:
            default:
                return super.proc( attacker, defender, damage );
            case 4: case 5:
                Buff.affect(defender, Burning.class).reignite(defender);
                return super.proc( attacker, defender, damage );
        }
    }

    @Override
    public int max(int lvl) {
        return  Math.round(2.5f*(tier+4)) +     //15 base, down from 30
                lvl*(tier-1);                   //+3 per level, down from +6
    }

    @Override
    public int defenseFactor( Char owner ) {
        return 1+4*buffedLvl();    //6 extra defence, plus 3 per level;
    }

    @Override
    public String statsInfo(){
        if (isIdentified()){
            return Messages.get(this, "stats_desc", 1+4*buffedLvl());
        } else {
            return Messages.get(this, "typical_stats_desc", 6);
        }
    }

//    @Override
//    protected void duelistAbility(Hero hero, Integer target) {
//        RoundShield.guardAbility(hero, 2, this);
//    }

}
