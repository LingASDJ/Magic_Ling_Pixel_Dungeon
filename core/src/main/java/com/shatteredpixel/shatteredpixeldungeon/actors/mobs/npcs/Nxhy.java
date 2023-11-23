package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs;

import static com.shatteredpixel.shatteredpixeldungeon.BGMPlayer.playBGM;
import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.BGMPlayer;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.journal.Notes;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.NxhySprite;
import com.watabou.noosa.Game;
import com.watabou.utils.Callback;

public class Nxhy extends Shopkeeper {

    {
        spriteClass = NxhySprite.class;
        properties.add(Property.BOSS);
        properties.add(Property.IMMOVABLE);
    }
    private boolean seenBefore = false;

    @Override
    protected boolean act() {

        Game.runOnRenderThread(new Callback() {
            @Override
            public void call() {
                if (!seenBefore && Dungeon.level.heroFOV[pos]) {
                    yell(Messages.get(Nxhy.class, "greetings", Dungeon.hero.name()));
                    seenBefore = true;
                    playBGM(Assets.SHOP, true);
                }  else if(seenBefore && !Dungeon.level.heroFOV[pos]) {
                    seenBefore = false;
                    yell(Messages.get(Nxhy.class, "goodbye", hero.name()));
                    BGMPlayer.playBGMWithDepth();
                }
            }
        });

        if (Dungeon.level.heroFOV[pos]){
            Notes.add(Notes.Landmark.SHOP);
        }
        throwItem();

        sprite.turnTo( pos, Dungeon.hero.pos );
        spend( TICK );
        return true;
    }

    @Override
    public int defenseSkill( Char enemy ) {
        return 0;
    }

}

