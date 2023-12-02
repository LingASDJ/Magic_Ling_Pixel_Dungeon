package com.shatteredpixel.shatteredpixeldungeon.items.quest;

import static com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special.ShopRoom.ChooseBag;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Ankh;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Honeypot;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.Stylus;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.LeatherArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.Bag;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Bomb;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfIdentify;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfMagicMapping;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRemoveCurse;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Alchemize;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfAugmentation;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.darts.TippedDart;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class RandomChest  extends Item {

    public static final String AC_AAT	= "KITEM";

    {
        stackable = true;
        image = ItemSpriteSheet.RANDOM_CHEST;
        defaultAction = AC_AAT;
        bones = true;
    }

    @Override
    public ArrayList<String> actions(Hero hero ) {
        ArrayList<String> actions = super.actions( hero );
        actions.add(AC_AAT);
        return actions;
    }

    @Override
    public void execute( Hero hero, String action ) {

        super.execute( hero, action );

        if (action.equals( AC_AAT )) {
            detach( hero.belongings.backpack );
            GLog.i( Messages.get(this, "look_msg") );
            Dungeon.level.drop(convert(), Dungeon.hero.pos).sprite.drop();
            hero.sprite.operate( hero.pos );
            hero.busy();
            Sample.INSTANCE.play( Assets.Sounds.DRINK );
        }
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
    public int value() {
        return 75 * quantity;
    }

    private Item convert(){
        Item w = new Item();
        switch (Random.Int(1,20)){
            default:
            case 0:
                w = Generator.random(Generator.wepTiers[1]);
            break;
            case 1:
                w = Generator.random(Generator.misTiers[1]).quantity(2).identify(false);
            break;
            case 2:
                w = new LeatherArmor().identify(false);
            break;
            case 3:
                Item ws;
                ws = TippedDart.randomTipped(2);
                w = ws;
            break;
            case 4:
                w = new Alchemize().quantity(Random.IntRange(2, 3));
            break;
            case 5:
                Bag bag = ChooseBag(Dungeon.hero.belongings);
                if (bag != null) {
                    w = (bag);
                }
            break;
            case 6:
                w = new PotionOfHealing().quantity(1);
            break;
            case 7:
                w = Generator.randomUsingDefaults( Generator.Category.POTION );
            break;
            case 8:
                w = new Ankh();
            break;
            case 9:
                w = new StoneOfAugmentation();
            break;
            case 10:
                switch (Random.Int(4)){
                    case 0:
                        w = ( new Bomb() );
                        break;
                    case 1:
                    case 2:
                        w = ( new Bomb.DoubleBomb() );
                        break;
                    case 3:
                        w = ( new Honeypot() );
                        break;
                }
            break;
            case 11:
                w = Generator.randomUsingDefaults( Generator.Category.SCROLL );
            break;
            case 12:
                w = ( new ScrollOfIdentify() );
                break;
            case 13:
                w = ( new ScrollOfRemoveCurse() );
                break;
            case 14:
                w = ( new ScrollOfMagicMapping() );
                break;
            case 15:
                w = new Stylus();
                break;
            case 16:
                w = Generator.randomUsingDefaults( Generator.Category.RING );
                break;
            case 17:
                w = Generator.randomUsingDefaults( Generator.Category.WAND );
                break;
            case 18:
                w = Generator.randomUsingDefaults( Generator.Category.STONE );
                break;
            case 19:
                w = Generator.randomUsingDefaults( Generator.Category.WEAPON );
                w.level(Random.Int(0,2));
                break;
        }
        return w;
    }
}

