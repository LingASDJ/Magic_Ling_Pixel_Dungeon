package com.shatteredpixel.shatteredpixeldungeon.windows;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Chrome;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ReloadShop;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Shopkeeper;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.ZeroDreamShop;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.SKINITEM;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ZeroDreamSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.ItemSlot;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.StyledButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.NinePatch;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.ui.Component;

public class WndZeroShop extends Window {
    private static final int WIDTH		= 120;
    private static final int BTN_SIZE	= 20;
    private static final int BTN_GAP	= 3;
    private static final int GAP		= 6;


    public WndZeroShop() {

        //TODO 能跑就行
        ZeroDreamShop.shop1 = SPDSettings.isItemUnlock("avatars_warrior_1")  ? null : new SKINITEM.SKIN_WA();
        ZeroDreamShop.shop2 = SPDSettings.isItemUnlock("avatars_mage_1")     ? null : new SKINITEM.SKIN_MA();
        ZeroDreamShop.shop3 = SPDSettings.isItemUnlock("avatars_rogue_1")    ? null : new SKINITEM.SKIN_RA();
        ZeroDreamShop.shop4 = SPDSettings.isItemUnlock("avatars_huntress_1") ? null : new SKINITEM.SKIN_HA();
        ZeroDreamShop.shop5 = SPDSettings.isItemUnlock("avatars_duelist_1")  ? null : new SKINITEM.SKIN_DA();

        ZeroDreamShop.shop6 =  SPDSettings.isItemUnlock("avatars_warrior_2")  ? null :new SKINITEM.SKIN_WB();
        ZeroDreamShop.shop7 =  SPDSettings.isItemUnlock("avatars_mage_2")     ? null :new SKINITEM.SKIN_MB();
        ZeroDreamShop.shop8 =  SPDSettings.isItemUnlock("avatars_rogue_2")    ? null :new SKINITEM.SKIN_RB();
        ZeroDreamShop.shop9 =  SPDSettings.isItemUnlock("avatars_huntress_2") ? null :new SKINITEM.SKIN_HB();
        ZeroDreamShop.shop10 = SPDSettings.isItemUnlock("avatars_duelist_2")  ? null :new SKINITEM.SKIN_DB();

        IconTitle titlebar = new IconTitle();
        titlebar.setRect(0, 0, WIDTH, 0);
        titlebar.icon(new ZeroDreamSprite());
        titlebar.label(Messages.get(ZeroDreamShop.class,"name"));
        add( titlebar );
        RenderedTextBlock message =
                PixelScene.renderTextBlock( (Messages.get(ZeroDreamShop.class,"descx",hero.name())), 6 );
        message.maxWidth(WIDTH);
        message.setPos(0, titlebar.bottom() + GAP);
        add( message );

        WndZeroShop.RewardButton shop1 = new WndZeroShop.RewardButton( ZeroDreamShop.shop1 );
        shop1.setRect( (WIDTH - BTN_GAP) / 5f - BTN_SIZE, message.top() + message.height() + BTN_GAP, BTN_SIZE,
                BTN_SIZE );
        add( shop1 );

        WndZeroShop.RewardButton shop2 = new WndZeroShop.RewardButton( ZeroDreamShop.shop2 );
        shop2.setRect( shop1.right() + BTN_GAP, shop1.top(), BTN_SIZE, BTN_SIZE );
        add(shop2);

        WndZeroShop.RewardButton shop3 = new WndZeroShop.RewardButton( ZeroDreamShop.shop3 );
        shop3.setRect( shop2.right() + BTN_GAP, shop2.top(), BTN_SIZE, BTN_SIZE );
        add(shop3);

        WndZeroShop.RewardButton shop4 = new WndZeroShop.RewardButton( ZeroDreamShop.shop4 );
        shop4.setRect( shop3.right() + BTN_GAP, shop3.top(), BTN_SIZE, BTN_SIZE );
        add(shop4);

        WndZeroShop.RewardButton shop5 = new WndZeroShop.RewardButton( ZeroDreamShop.shop5 );
        shop5.setRect( shop4.right() + BTN_GAP, shop4.top(), BTN_SIZE, BTN_SIZE );
        add(shop5);

        RewardButton bomb1 = new RewardButton( ZeroDreamShop.shop6 );
        bomb1.setRect( shop1.left() , shop1.bottom(), BTN_SIZE, BTN_SIZE );
        add(bomb1);

        RewardButton bomb2 = new RewardButton( ZeroDreamShop.shop7);
        bomb2.setRect( bomb1.right()+ BTN_GAP , bomb1.top(), BTN_SIZE, BTN_SIZE );
        add(bomb2);

        RewardButton bomb3 = new RewardButton( ZeroDreamShop.shop8 );
        bomb3.setRect( bomb2.right()+ BTN_GAP , bomb2.top(), BTN_SIZE, BTN_SIZE );
        add(bomb3);

        RewardButton bomb4 = new RewardButton( ZeroDreamShop.shop9 );
        bomb4.setRect( bomb3.right()+ BTN_GAP , bomb3.top(), BTN_SIZE, BTN_SIZE );
        add(bomb4);

        RewardButton bomb5 = new RewardButton( ZeroDreamShop.shop10 );
        bomb5.setRect( bomb4.right()+ BTN_GAP , bomb4.top(), BTN_SIZE, BTN_SIZE );
        add(bomb5);

        resize(WIDTH, (int) bomb5.bottom());
    }

    public static WndBag sell() {
        return GameScene.selectItem( itemSelector );
    }

    private static WndBag.ItemSelector itemSelector = new WndBag.ItemSelector() {
        @Override
        public String textPrompt() {
            return Messages.get(Shopkeeper.class, "sell");
        }

        @Override
        public boolean itemSelectable(Item item) {
            return Shopkeeper.canSell(item);
        }

        @Override
        public void onSelect( Item item ) {
            if (item != null) {
                WndBag parentWnd = sell();
                GameScene.show( new WndTradeItem( item, parentWnd ) );
            }
        }
    };

    public void itemUnlock(Item item){
        if(item instanceof SKINITEM.SKIN_WA){
            SPDSettings.unlockItem("avatars_warrior_1");
        }
        if(item instanceof SKINITEM.SKIN_MA){
            SPDSettings.unlockItem("avatars_mage_1");
        }
        if(item instanceof SKINITEM.SKIN_RA){
            SPDSettings.unlockItem("avatars_rogue_1");
        }
        if(item instanceof SKINITEM.SKIN_HA){
            SPDSettings.unlockItem("avatars_huntress_1");
        }
        if(item instanceof SKINITEM.SKIN_DA){
            SPDSettings.unlockItem("avatars_duelist_1");
        }

        if(item instanceof SKINITEM.SKIN_WB){
            SPDSettings.unlockItem("avatars_warrior_2");
        }
        if(item instanceof SKINITEM.SKIN_MB){
            SPDSettings.unlockItem("avatars_mage_2");
        }
        if(item instanceof SKINITEM.SKIN_RB){
            SPDSettings.unlockItem("avatars_rogue_2");
        }
        if(item instanceof SKINITEM.SKIN_HB){
            SPDSettings.unlockItem("avatars_huntress_2");
        }
        if(item instanceof SKINITEM.SKIN_DB){
            SPDSettings.unlockItem("avatars_duelist_2");
        }
    }


    private class RewardWindow extends WndInfoItem {

        public RewardWindow( Item item ) {
            super(item);

            boolean locked = SPDSettings.isItemUnlock(item.getClass().getSimpleName());

            StyledButton btnConfirm = new StyledButton(SPDSettings.isItemUnlock(item.getClass().getSimpleName())? Chrome.Type.SCROLL : Chrome.Type.RED_BUTTON,Messages.get(WndIceTradeItem.class, (locked) ? "unlocked":"buy",item.iceCoinValue())){
                @Override
                protected void onClick() {
                    if(SPDSettings.iceCoin() >= item.iceCoinValue()) {
                        SPDSettings.iceDownCoin(item.iceCoinValue());
                        selectReward( item );
                        itemUnlock(item);
                        item.cursed = true;
                        Buff.prolong( hero, ReloadShop.class, 1f);
                        RewardWindow.this.hide();
                    } else {
                        GLog.n(Messages.get(ZeroDreamShop.class,"no"));
                        RewardWindow.this.hide();
                    }
                }
            };

            if(SPDSettings.isItemUnlock(item.getClass().getSimpleName())){
                btnConfirm.active = false;
                btnConfirm.setRect(0, height+2, WIDTH, 31);
            } else {
                btnConfirm.setRect(0, height+2, WIDTH, 16);
            }
            add(btnConfirm);

            resize(width, (int)btnConfirm.bottom());
        }
    }

    private void selectReward( Item reward ) {

        hide();
        GLog.i( Messages.get(hero, "you_now_have", reward.name()) );
    }

    public class RewardButton extends Component {

        protected NinePatch bg;
        protected ItemSlot slot;

        public RewardButton( Item item ){
            bg = Chrome.get( Chrome.Type.RED_BUTTON);
            add( bg );

            slot = new ItemSlot( item ){
                @Override
                protected void onPointerDown() {
                    bg.brightness( 1.2f );
                    Sample.INSTANCE.play( Assets.Sounds.CLICK );
                }
                @Override
                protected void onPointerUp() {
                    bg.resetColor();
                }
                @Override
                protected void onClick() {
                    ShatteredPixelDungeon.scene().addToFront(new RewardWindow(item));
                }
            };
            add(slot);
        }

        @Override
        protected void layout() {
            super.layout();

            bg.x = x;
            bg.y = y;
            bg.size( width, height );

            slot.setRect( x + 2, y + 2, width - 4, height - 4 );
        }
    }
}


