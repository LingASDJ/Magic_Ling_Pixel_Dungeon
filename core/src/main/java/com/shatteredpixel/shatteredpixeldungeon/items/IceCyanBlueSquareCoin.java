package com.shatteredpixel.shatteredpixeldungeon.items;

import static com.shatteredpixel.shatteredpixeldungeon.Challenges.PRO;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.FloatingText;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class IceCyanBlueSquareCoin extends Item {
    {
        image = ItemSpriteSheet.ICEGOLD;
        stackable = true;
    }

    public IceCyanBlueSquareCoin() {
        this( 1 );
    }

    public IceCyanBlueSquareCoin( int value ) {
        this.quantity = value;
    }

    @Override
    public ArrayList<String> actions(Hero hero ) {
        return new ArrayList<>();
    }

    @Override
    public boolean doPickUp(Hero hero, int pos) {
        if(Dungeon.isChallenged(PRO) || Statistics.bossRushMode)
        {
            GLog.n(Messages.get(this,"no"));
            return false;
        }


        if(SPDSettings.Cheating()){
            //盗版蓝币只有正版的十分之一
            SPDSettings.iceCoin(quantity/10);
        } else {
            SPDSettings.iceCoin(quantity);
        }
        GameScene.pickUp( this, pos );
        hero.sprite.showStatusWithIcon(Window.TITLE_COLOR, Integer.toString(quantity), FloatingText.ICECOIN );
        hero.spendAndNext( TIME_TO_PICK_UP );

        Sample.INSTANCE.play( Assets.Sounds.GOLD, 1, 1, Random.Float( 0.9f, 1.1f ) );
        updateQuickslot();

        return true;
    }

    @Override
    public boolean isUpgradable() {
        return false;
    }

    @Override
    public boolean isIdentified() {
        return true;
    }

    @Override
    public Item random() {
        quantity = Random.IntRange( 30 + Dungeon.depth * 10, 60 + Dungeon.depth * 20 );
        return this;
    }
}
