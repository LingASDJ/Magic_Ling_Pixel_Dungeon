package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ElmoParticle;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ShopkKingSprite;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndQuest;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndShopKing;
import com.watabou.noosa.Game;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;

import java.util.ArrayList;

public class ShopKing_Two extends NTNPC {

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
        sprite.killAndErase();
        CellEmitter.get( pos ).burst( ElmoParticle.FACTORY, 6 );
    }

    @Override
    public boolean interact(Char c) {
        sprite.turnTo( pos, Dungeon.hero.pos );
        if(first){
            WndQuest.chating(this,chat);
            first=false;
        }else {
            for (Mob mob : Dungeon.level.mobs) {
                if (mob instanceof ShopKing_Two) {
                    Game.runOnRenderThread(new Callback() {
                        @Override
                        public void call() {
                            GameScene.show(new WndShopKing());
                        }
                    }
                    );
                    break;
                }
            }
        }
        return true;
    }

    {
        spriteClass = ShopkKingSprite.class;

        chat = new ArrayList<String>() {
            {
                add(Messages.get(ShopKing.class, "talk_8", Dungeon.hero.name()));
                add(Messages.get(ShopKing.class, "talk_9"));
            }
        };

    }
}
