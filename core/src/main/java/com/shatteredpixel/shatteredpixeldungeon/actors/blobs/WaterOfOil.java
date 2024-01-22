package com.shatteredpixel.shatteredpixeldungeon.actors.blobs;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.BlobEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.lightblack.OilLantern;
import com.shatteredpixel.shatteredpixeldungeon.journal.Notes;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;

public class WaterOfOil extends WellWater {

    @Override
    protected boolean affectHero( Hero hero ) {
        return false;
    }

    @Override
    protected Item affectItem(Item item, int pos ) {
        if (item instanceof OilLantern && !((OilLantern)item).isFull()) {
            ((OilLantern)item).reoill(hero);
            hero.sprite.emitter().start( Speck.factory( Speck.STEAM), 0.4f, 4 );
            Sample.INSTANCE.play( Assets.Sounds.WATER );
            GLog.w( Messages.get(this, "clear"));
            return item;
        }
        return null;
    }

    @Override
    protected Notes.Landmark record() {
        return Notes.Landmark.WELL_OF_HEALTH;
    }

    @Override
    public void use( BlobEmitter emitter ) {
        super.use( emitter );
        emitter.start( Speck.factory( Speck.STEAM ), 0.25f, 0 );
    }

    @Override
    public String tileDesc() {
        return Messages.get(this, "desc");
    }
}

