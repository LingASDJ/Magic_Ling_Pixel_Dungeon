package com.shatteredpixel.shatteredpixeldungeon.plants;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Freezing;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.BlobImmunity;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FrostImbue;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MarigoldBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.utils.BArray;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Point;
import java.util.ArrayList;

import java.util.ArrayList;

//黄金菊
//踩踏获得15回合“菊寿凝身”
public class Marigold extends Plant {

    {
        image = 18;
        seedClass = Marigold.Seed.class;
    }

    @Override
    public void activate( Char ch ) {

        if (ch == null) return;

        Buff.affect(ch, MarigoldBuff.class, linkDuration(15f));
    }

    public static class Seed extends Plant.Seed {
        {
            image = ItemSpriteSheet.SEED_MARIGOLD;

            plantClass = Marigold.class;
        }
    }
}
