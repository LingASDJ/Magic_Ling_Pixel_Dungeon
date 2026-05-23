package com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.status.BlessAWP;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.Scroll;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.watabou.noosa.Game;
import com.watabou.utils.Callback;

public class ScrollOfBlessGoTend extends Scroll {

    {
        image = ItemSpriteSheet.BLESS_SCROLL;
        unique = true;
    }

    @Override
    public boolean isIdentified() {
        return true;
    }

    @Override
    public boolean isKnown() {
        return true;
    }


    @Override
    public void doRead() {
        Game.runOnRenderThread(new Callback() {
            @Override
            public void call() {
                GameScene.show(new WndOptions(
                        new ItemSprite(image),
                        Messages.titleCase(name()),
                        Messages.get(ScrollOfBlessGoTend.class, "desc"),
                        Messages.get(ScrollOfBlessGoTend.class, "weapon"),
                        Messages.get(ScrollOfBlessGoTend.class, "armor")
                   ) {
                       @Override
                       protected void onSelect(int index) {
                           if (index==0){
                               Buff.affect(hero, BlessAWP.WeaponGetReady.class).set(100, 1);
                               detach(curUser.belongings.backpack);
                               identify();
                               readAnimation();
                               hero.sprite.emitter().start( Speck.factory( Speck.UP ), 0.2f, 3 );
                           } else if (index==1){
                               Buff.affect(hero, BlessAWP.ArmorGetReady.class).set(100, 1);
                               detach(curUser.belongings.backpack);
                               identify();
                               readAnimation();
                               hero.sprite.emitter().start( Speck.factory( Speck.UP ), 0.2f, 3 );
                           }
                       }
                   }
                );
            }});


    }
}

