package com.shatteredpixel.shatteredpixeldungeon.windows;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Shopkeeper;
import com.shatteredpixel.shatteredpixeldungeon.items.EquipableItem;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.IceCyanBlueSquareCoin;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.legend.DiedCrossBow;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.legend.MoonDao;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;

public class WndIceTradeItem extends WndInfoItem {

    private static final float GAP		= 2;
    private static final int BTN_HEIGHT	= 18;

    private WndBag owner;

    private boolean selling = false;

    //selling
    public WndIceTradeItem(final Item item, WndBag owner ) {

        super(item);

        selling = true;

        this.owner = owner;

        float pos = height;
        Shopkeeper shop = null;
        for (Char ch : Actor.chars()){
            if (ch instanceof Shopkeeper){
                shop = (Shopkeeper) ch;
                break;
            }
        }
        final Shopkeeper finalShop = shop;
        if (item.quantity() == 1) {

            RedButton btnSell = new RedButton( Messages.get(this, "sell", item.value()) ) {
                @Override
                protected void onClick() {
                    sell( item,finalShop );
                    hide();
                }
            };
            //tnSell.setHeight( BTN_HEIGHT );
            btnSell.setRect( 0, pos + GAP, width, BTN_HEIGHT );
            btnSell.icon(new ItemSprite(ItemSpriteSheet.ICEGOLD));
            add( btnSell );

            pos = btnSell.bottom();

        } else {

            int priceAll= item.value();
            RedButton btnSell1 = new RedButton( Messages.get(this, "sell_1", priceAll / item.quantity()) ) {
                @Override
                protected void onClick() {
                    sellOne( item );
                    hide();
                }
            };
            btnSell1.setRect( 0, pos + GAP, width, BTN_HEIGHT );
            btnSell1.icon(new ItemSprite(ItemSpriteSheet.ICEGOLD));
            add( btnSell1 );
            RedButton btnSellAll = new RedButton( Messages.get(this, "sell_all", priceAll ) ) {
                @Override
                protected void onClick() {
                    sell( item,finalShop );
                    hide();
                }
            };
            btnSellAll.setRect( 0, btnSell1.bottom() + 1, width, BTN_HEIGHT );
            btnSellAll.icon(new ItemSprite(ItemSpriteSheet.ICEGOLD));
            add( btnSellAll );

            pos = btnSellAll.bottom();

        }

        resize( width, (int)pos );
    }

    //buying
    public WndIceTradeItem( final Heap heap ) {

        super(heap);

        selling = false;

        Item item = heap.peek();

        float pos = height;

        final int price = Shopkeeper.sellIcePrice( item );

        RedButton btnBuy = new RedButton( SPDSettings.isItemUnlock( item.name() ) ? Messages.get(this, "unlocked") : Messages.get(this, "buy", price) ) {
            @Override
            protected void onClick() {
                hide();
                buy( heap );
            }
        };
        btnBuy.setRect( 0, pos + GAP, width, BTN_HEIGHT );
        btnBuy.icon(new ItemSprite(ItemSpriteSheet.ICEGOLD));


        btnBuy.enable( SPDSettings.isItemUnlock( item.name() ) ? false : price <= SPDSettings.iceCoin());
        add( btnBuy );

        pos = btnBuy.bottom();

        resize(width, (int) pos);
    }

    @Override
    public void hide() {

        super.hide();

        if (owner != null) {
            owner.hide();
        }
        if (selling) Shopkeeper.sell();
    }

    public static void sell( Item item, Shopkeeper shop ) {

        Hero hero = Dungeon.hero;

        if (item.isEquipped( hero ) && !((EquipableItem)item).doUnequip( hero, false )) {
            return;
        }
        item.detachAll( hero.belongings.backpack );

        //selling items in the sell interface doesn't spend time
        hero.spend(-hero.cooldown());

        new IceCyanBlueSquareCoin( item.value() ).doPickUp( hero );

        if (shop != null){
            shop.buybackItems.add(item);
            while (shop.buybackItems.size() > Shopkeeper.MAX_BUYBACK_HISTORY){
                shop.buybackItems.remove(0);
            }
        }
    }

    public static void sellOne( Item item ) {
        sellOne( item, null );
    }

    public static void sellOne( Item item, Shopkeeper shop ) {

        if (item.quantity() <= 1) {
            sell( item, shop );
        } else {

            Hero hero = Dungeon.hero;

            item = item.detach( hero.belongings.backpack );

            //selling items in the sell interface doesn't spend time
            hero.spend(-hero.cooldown());

            new IceCyanBlueSquareCoin( item.value() ).doPickUp( hero );

            if (shop != null){
                shop.buybackItems.add(item);
                while (shop.buybackItems.size() > Shopkeeper.MAX_BUYBACK_HISTORY){
                    shop.buybackItems.remove(0);
                }
            }
        }
    }

    private void buy( Heap heap ) {

        Item item = heap.pickUp();
        if (item == null) return;

        int price = Shopkeeper.sellIcePrice( item );
        SPDSettings.iceDownCoin(price);

        if( (item instanceof DiedCrossBow|| item instanceof MoonDao) && !SPDSettings.isItemUnlock( item.name() ) ){
            if(item instanceof DiedCrossBow)
                Generator.Category.WEP_T5.probs = new float[]{ 0, 3, 3, 3, 3, 3,3, 1.5f };
            if(item instanceof MoonDao)
                Generator.Category.WEP_T3.probs = new float[]{ 1, 5, 4, 4, 4 ,3,5,3,6,0, 0,0,0, 1.5f };
            SPDSettings.unlockItem( item.name() );
        }

        if (!item.doPickUp( Dungeon.hero )) {
            Dungeon.level.drop( item, heap.pos ).sprite.drop();
        }
    }
}
