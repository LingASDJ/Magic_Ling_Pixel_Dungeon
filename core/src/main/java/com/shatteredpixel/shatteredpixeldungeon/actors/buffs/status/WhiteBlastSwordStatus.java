package com.shatteredpixel.shatteredpixeldungeon.actors.buffs.status;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.WhiteBlastSword;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class WhiteBlastSwordStatus extends Buff {

    {
        type = buffType.POSITIVE;
        skills = true;
    }

    private int level = 0;
    private int interval = 1;

    private int ankhs = 0;


    @Override
    public boolean act() {
        if (target.isAlive()) {

            spend( interval );
            if (level <= 0) {
                detach();
            }

        } else {

            detach();

        }

        return true;
    }

    @Override
    public void detach() {
        super.detach();
        /*
        if(hero != null && hero.belongings.weapon instanceof WhiteBlastSword){
            ((WhiteBlastSword) hero.belongings.weapon).whiteBlast_Sword();
            if(GameScene.scene != null) {
                hero.sprite.showStatus(CharSprite.NEGATIVE, WhiteBlastSword.TXT_RANDOM[Random.Int(WhiteBlastSword.TXT_RANDOM.length)]);
            }
        }
        */
    }

    public int level() {
        return level;
    }

    public void set( int value, int time) {
        if (level <= value) {
            level = value;
            interval = time;
            spend(time - cooldown() - 1);
        }
    }

    @Override
    public int icon() {
        return BuffIndicator.WEAPON;
    }

    @Override
    public String desc() {
        return Messages.get(this, "desc", 2 + (hero == null ? 4 : hero.belongings.weapon != null ? hero.belongings.weapon.level() : 4));
    }

    private static final String LEVEL	    = "level";
    private static final String INTERVAL    = "interval";

    private static final String ID = "id";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( INTERVAL, interval );
        bundle.put( LEVEL, level );
        bundle.put( ID, ankhs );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        interval = bundle.getInt( INTERVAL );
        level = bundle.getInt( LEVEL );
        ankhs = bundle.getInt( ID );
    }
}


