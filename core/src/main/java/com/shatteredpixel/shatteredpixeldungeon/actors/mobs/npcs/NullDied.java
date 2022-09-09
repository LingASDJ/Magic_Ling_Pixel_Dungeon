package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.EnergyParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SnowParticle;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ShopkKingSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndQuest;
import com.watabou.utils.Bundle;

import java.util.ArrayList;

public class NullDied extends NTNPC {

    private static final String FIRST = "first";
    private boolean first=true;
    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(FIRST, first);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        first = bundle.getBoolean(FIRST);
    }

    public void flee() {
        destroy();
        Dungeon.level.seal();
        GameScene.flash(0x8000FFFF);
        sprite.killAndErase();
        CellEmitter.get( pos ).burst(SnowParticle.FACTORY, 6 );
        CellEmitter.get( pos ).burst(EnergyParticle.FACTORY, 6 );
    }

    @Override
    public boolean interact(Char c) {
        sprite.turnTo( pos, Dungeon.hero.pos );
        if(first){
            WndQuest.chating(this,chat);
            first=false;
        }else {
            GLog.n( Messages.get(NullDied.class, "talk_r",Dungeon.hero.name()) );
            flee();
        }
        return true;
    }

    {
        spriteClass = ShopkKingSprite.class;

        chat = new ArrayList<String>() {
            {
                add(Messages.get(NullDied.class, "talk_x"));
            }
        };

    }
}