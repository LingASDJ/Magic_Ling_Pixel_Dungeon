package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MimicSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;

public class BlackMimic extends Mimic {

    {
        spriteClass = MimicSprite.Golden.class;
        baseSpeed *= 3f;
    }

    @Override
    public String name() {
        if (alignment == Char.Alignment.NEUTRAL){
            return Messages.get(Heap.class, "locked_chest");
        } else {
            return super.name();
        }
    }

    @Override
    public String description() {
        if (alignment == Char.Alignment.NEUTRAL){
            return Messages.get(Heap.class, "locked_chest_desc") + "\n\n" + Messages.get(this, "hidden_hint");
        } else {
            return super.description();
        }
    }

    public void stopHiding(){
        state = HUNTING;
        if (Actor.chars().contains(this) && Dungeon.level.heroFOV[pos]) {
            enemy = Dungeon.hero;
            target = Dungeon.hero.pos;
            enemySeen = true;
            GLog.w(Messages.get(this, "reveal") );
            CellEmitter.get(pos).burst(Speck.factory(Speck.STAR), 10);
            Sample.INSTANCE.play(Assets.Sounds.MIMIC, 1, 0.85f);
        }
    }

    @Override
    public void setLevel(int level) {
        super.setLevel(Math.round(level*3.33f));
    }

    @Override
    protected void generatePrize() {
        super.generatePrize();
        //all existing prize items are guaranteed uncursed, and have a 50% chance to be +1 if they were +0
        for (Item i : items){
            i.cursed = false;
            i.cursedKnown = true;
        }
    }
}

