package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.watabou.noosa.Image;

public class Butter extends FlavourBuff {

    {
        type = buffType.NEGATIVE;
    }
    public int pos;
    public static final float DURATION	= 10f;



    @Override
    public void tintIcon(Image icon) {
        greyIcon(icon, 5f, cooldown());
    }

    @Override
    public String toString() {
        return Messages.get(this, "name");
    }

    @Override
    public String heroMessage() {
        return Messages.get(this, "heromsg");
    }

    @Override
    public String desc() {
        return Messages.get(this, "desc", dispTurns());
    }

    @Override
    public void fx(boolean on) {
        if (on) target.sprite.add(CharSprite.State.BUTTER);
        else target.sprite.remove(CharSprite.State.BUTTER);
    }
}

