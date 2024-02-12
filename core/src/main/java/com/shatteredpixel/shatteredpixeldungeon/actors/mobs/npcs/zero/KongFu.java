package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;
import static com.shatteredpixel.shatteredpixeldungeon.Statistics.zeroItemLevel;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NTNPC;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Pasty;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.KongFuSprites;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndQuest;
import com.watabou.noosa.tweeners.Delayer;
import com.watabou.utils.Bundle;

import java.util.ArrayList;

public class KongFu extends NTNPC {

    protected ArrayList<String> chat;
    protected ArrayList<String> B_chat;

    protected ArrayList<String> C_chat;

    {
        spriteClass = KongFuSprites.class;
        chat = new ArrayList<String>() {
            {
                add((Messages.get(KongFu.class, "a_message1")));
            }
        };

        B_chat = new ArrayList<String>() {
            {
                add((Messages.get(KongFu.class, "b_message1")));
            }
        };

        C_chat = new ArrayList<String>() {
            {
                add((Messages.get(KongFu.class, "c_message1")));
            }
        };
    }

    private boolean first=true;

    private String def_verb(){
        //格挡动画
        ((KongFuSprites)sprite).What_Up();
        //1s延迟后，恢复闲置状态
        GameScene.scene.add(new Delayer(1f){
            @Override
            protected void onComplete() {
                ((KongFuSprites)sprite).idle();
            }
        });
        return Messages.get(this, "def_verb_1");

    }

    @Override
    public String defenseVerb() {
        return def_verb();
    }


    private static final String FIRST = "first";
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

    @Override
    public boolean interact(Char c) {

        sprite.turnTo(pos, hero.pos);

        if(first){
            WndQuest.chating(this,chat);


            if(Statistics.zeroItemLevel >=4 && Dungeon.depth == 0) {
                Dungeon.level.drop(new Gold(1), hero.pos);
            } else {
                Dungeon.level.drop( new Pasty(), hero.pos );
            }

            first=false;
            zeroItemLevel++;
        } else if(Statistics.amuletObtained) {
            WndQuest.chating(this,B_chat);
        } else {
            WndQuest.chating(this,C_chat);
        }

        return true;
    }

}
