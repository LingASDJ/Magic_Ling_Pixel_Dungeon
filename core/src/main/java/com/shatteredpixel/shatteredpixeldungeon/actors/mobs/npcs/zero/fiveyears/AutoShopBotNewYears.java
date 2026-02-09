package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.fiveyears;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.FiveYearsNPC;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.AutoShopRoBotSprite;

public class AutoShopBotNewYears extends FiveYearsNPC {

    {
        spriteClass = AutoShopRoBotSprite.class;
    }

    @Override
    public boolean interact(Char c) {
        sprite.turnTo( pos, c.pos );
        if(first){
            if (Statistics.zeroItemLevel < 4) {
                yell(Messages.get(this,"messages1"));
                yell(Messages.get(this,"messages2"));
                Dungeon.level.drop(Generator.random( Generator.Category.FOOD ), hero.pos);
            } else {
                yell(Messages.get(this,"messages3"));
                yell(Messages.get(this,"messages4"));
            }
            Statistics.zeroItemLevel++;
            first = false;
        }
        return true;
    }

}
