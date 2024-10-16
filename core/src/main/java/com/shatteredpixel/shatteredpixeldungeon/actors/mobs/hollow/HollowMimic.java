package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.hollow;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ElementalBuff.BaseBuff.ScaryBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ElementalBuff.Immunities.ScaryImmunitiesBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mimic;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MimicSprite;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HollowMimic extends Mimic {

    {
        spriteClass = MimicSprite.HollowWall.class;
        properties.add( Property.HOLLOW );
        HP = HT= Random.Int(35,60);
        EXP = 30;
        maxLvl = 40;
    }

    @Override
    public int damageRoll() {
        return 0;
    }

    @Override
    public int attackProc(Char enemy, int damage) {
        if(enemy!=null && enemy == hero) {
            for (Buff buff : hero.buffs()) {
                if(buff instanceof ScaryImmunitiesBuff){
                    ((ScaryImmunitiesBuff) buff).damgeScary();
                } else if (buff instanceof ScaryBuff) {
                    ((ScaryBuff) buff).damgeScary(20);
                } else {
                    Buff.affect(enemy, ScaryBuff.class).set((100), 5);
                }
            }
        }
        return damage; // Return adjusted damage
    }

    @Override
    public int drRoll() {
        return 0;
    }

    public static HollowMimic spawnAt(int pos, Item item, Class mimicType ){
        return spawnAt( pos, Arrays.asList(item), mimicType, false);
    }

    public static HollowMimic spawnAt(int pos, List<Item> items, Class mimicType,boolean useDeck ) {

        HollowMimic m;
        m = new HollowMimic();


        m.items = new ArrayList<>( items );
        m.setLevel( Dungeon.depth );
        m.pos = pos;

        //generate an extra reward for killing the mimic
        m.generatePrize(useDeck);

        return m;
    }

    @Override
    public void rollToDropLoot(){

        if (items != null) {
            for (Item item : items) {
                Dungeon.level.drop( item, hero.pos ).sprite.drop();
            }
            items = null;
        }

    }

    @Override
    public int attackSkill(Char target) {
        return 25; // Fixed attack skill
    }

}
