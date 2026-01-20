package com.shatteredpixel.shatteredpixeldungeon.items.bombs;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.CrossTownProc;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.CrossDiedTower;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;

public class LaserPython extends Bomb {

    {
        image = ItemSpriteSheet.LASERPYTHON;
    }

    @Override
    public void explode(int cell) {
        super.explode(cell);
        CrossDiedTower csp = new CrossDiedTower();
        csp.pos = cell;
        GameScene.add(csp);
        Buff.append(hero, CrossTownProc.class).towerPosLing = cell;

        Sample.INSTANCE.play(Assets.Sounds.BURNING);
    }

    @Override
    public boolean doPickUp(Hero hero, int pos) {
            GLog.w( Messages.get(this, "didnot_pick") );
        return false;
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return fuse != null ? new ItemSprite.Glowing( 0x268065, 0.6f) : null;
    }

    @Override
    public int value() {
        //prices of ingredients
        return quantity * (20 + 30);
    }
}

