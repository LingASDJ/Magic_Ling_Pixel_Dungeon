package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NPC;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MoonCatSprite;
import com.watabou.noosa.tweeners.Delayer;
import com.watabou.utils.Random;

public class MoonCat extends NPC {

    {
        spriteClass = MoonCatSprite.class;
    }

    private String def_verb(){
        //格挡动画
        ((MoonCatSprite)sprite).What_Up();
        //1s延迟后，恢复闲置状态
        GameScene.scene.add(new Delayer(1f){
            @Override
            protected void onComplete() {
                ((MoonCatSprite)sprite).idle();
            }
        });
        if(Random.Int(100)>=50){
            return Messages.get(this, "def_verb_1");
        } else {
            return Messages.get(this, "def_verb_2");
        }

    }

    @Override
    public boolean interact(Char c) {

        sprite.turnTo(pos, Dungeon.hero.pos);
        return true;
    }

    @Override
    protected boolean act() {
        throwItem();
        sprite.turnTo( pos, Dungeon.hero.pos );
        spend( TICK );
        return true;
    }

    @Override
    public void damage( int dmg, Object src ) {
    }

    @Override
    public boolean add( Buff buff ) {
        return false;
    }

    @Override
    public boolean reset() {
        return true;
    }

    @Override
    public String defenseVerb() {
       return def_verb();
    }

    @Override
    public int defenseSkill( Char enemy ) {
        return INFINITE_EVASION;
    }


}
