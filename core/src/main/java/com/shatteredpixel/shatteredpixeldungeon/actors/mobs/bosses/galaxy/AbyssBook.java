package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.galaxy;

import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ChampionHero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.books.Books;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.watabou.noosa.Game;
import com.watabou.utils.Callback;

public class AbyssBook extends Books {

    private static final String Read	= "Read";
    {
        image = ItemSpriteSheet.RGJT_1;
        unique= true;
    }

    @Override
    public void execute(final Hero hero, String action) {
        super.execute(hero, action);
        for (Buff b : hero.buffs(ChampionHero.class)) {
            if (b != null) {
                GLog.w(Messages.get(Books.class, "your_characicter"));
                return;
            }
        }

        if (action.equals(Read)) {
            Game.runOnRenderThread(new Callback() {
                @Override
                public void call() {
                    GameScene.show(new WndOptions(new ItemSprite(ItemSpriteSheet.RGJT_1),
                            Messages.titleCase(Messages.get(AbyssBook.class, "name")),
                            Messages.get(AbyssBook.class, "book_blessed"),
                           "1","2","3","4","5","6","7","8") {
                        @Override
                        protected void onSelect(int index) {
                           switch (index){
                               case 0:
                           }
                        }
                    });
                }

            });
        }
    }
}

