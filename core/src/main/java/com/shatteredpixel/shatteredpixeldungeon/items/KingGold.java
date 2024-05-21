package com.shatteredpixel.shatteredpixeldungeon.items;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

public class KingGold extends Item {

    {
        image = ItemSpriteSheet.BOSSRUSH_GOLD;
        stackable = true;
    }

    public KingGold() {
        this( 1 );
    }

    public KingGold( int value ) {
        this.quantity = value;
    }

    @Override
    public boolean doPickUp(Hero hero, int pos) {

        Dungeon.rushgold += quantity;

        GameScene.pickUp( this, pos );
        hero.spendAndNext( TIME_TO_PICK_UP );
        hero.sprite.showStatus(CharSprite.NEGATIVE, Messages.get(this, "ling",quantity));
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

}

