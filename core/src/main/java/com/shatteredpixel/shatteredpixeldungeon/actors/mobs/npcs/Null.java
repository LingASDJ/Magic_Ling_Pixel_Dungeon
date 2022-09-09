package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs;

import static com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cost.cost;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.ColdGurad;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.IronKey;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ShopkKingSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;

public class Null extends Mob {

    {
        spriteClass = ShopkKingSprite.class;
        properties.add(Property.IMMOVABLE);
        HP = HT = 114514;
    }

    @Override
    public void damage( int dmg, Object src ) {
    }

    @Override
    public int defenseSkill( Char enemy ) {
        return INFINITE_EVASION;
    }

    @Override
    public void die( Object cause ) {
        super.die( cause );
        Dungeon.level.drop( new IronKey( Dungeon.depth ), pos ).sprite.drop();
    }

    @Override
    protected boolean act() {
        if(cost == 0 && Dungeon.level.heroFOV[pos]){
                    die( this );
            GameScene.flash(0x80FFFFFF);
            GLog.p(Messages.get(ColdGurad.class,"cost"));
            } else {
            spend(TICK);

        }
        return true;
    }

}
