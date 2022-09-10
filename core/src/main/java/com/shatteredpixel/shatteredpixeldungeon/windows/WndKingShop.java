package com.shatteredpixel.shatteredpixeldungeon.windows;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Chrome;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.RandomBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ReloadShop;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NullDiedTO;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Shopkeeper;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ShopkKingSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.ItemSlot;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.StyledButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Game;
import com.watabou.noosa.NinePatch;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.ui.Component;
import com.watabou.utils.Callback;

public class WndKingShop extends Window {
    private static final int WIDTH		= 120;
    private static final int BTN_SIZE	= 16;
    private static final int BTN_GAP	= 3;
    private static final int GAP		= 3;
    public WndKingShop() {
        IconTitle titlebar = new IconTitle();
        titlebar.setRect(0, 0, WIDTH, 0);
        titlebar.icon(new ShopkKingSprite());
        titlebar.label(Messages.get(WndKingShop.class,"king"));
        add( titlebar );
        RenderedTextBlock message = PixelScene.renderTextBlock( (Messages.get(WndKingShop.class,"select")), 6 );
        message.maxWidth(WIDTH);
        message.setPos(0, titlebar.bottom() + GAP);
        add( message );

        WndKingShop.RewardButton shop1 = new WndKingShop.RewardButton( NullDiedTO.shop1 );
        shop1.setRect( (WIDTH - BTN_GAP) / 6 - BTN_SIZE, message.top() + message.height() + BTN_GAP, BTN_SIZE,
                BTN_SIZE );
        add( shop1 );

        WndKingShop.RewardButton shop2 = new WndKingShop.RewardButton( NullDiedTO.shop2 );
        shop2.setRect( shop1.right() + BTN_GAP, shop1.top(), BTN_SIZE, BTN_SIZE );
        add(shop2);

        WndKingShop.RewardButton shop3 = new WndKingShop.RewardButton( NullDiedTO.shop3 );
        shop3.setRect( shop2.right() + BTN_GAP, shop2.top(), BTN_SIZE, BTN_SIZE );
        add(shop3);

        WndKingShop.RewardButton shop4 = new WndKingShop.RewardButton( NullDiedTO.shop4 );
        shop4.setRect( shop3.right() + BTN_GAP, shop3.top(), BTN_SIZE, BTN_SIZE );
        add(shop4);

        WndKingShop.RewardButton shop5 = new WndKingShop.RewardButton( NullDiedTO.shop5 );
        shop5.setRect( shop4.right() + BTN_GAP, shop4.top(), BTN_SIZE, BTN_SIZE );
        add(shop5);

        WndKingShop.RewardButton shop6 = new WndKingShop.RewardButton( NullDiedTO.shop6 );
        shop6.setRect( shop5.right() + BTN_GAP, shop5.top(), BTN_SIZE, BTN_SIZE );
        add(shop6);

        WndKingShop.RewardButton2 bomb1 = new WndKingShop.RewardButton2( NullDiedTO.shop7 );
        bomb1.setRect( shop1.left() , shop1.bottom(), BTN_SIZE, BTN_SIZE );
        add(bomb1);

        WndKingShop.RewardButton2 bomb2 = new WndKingShop.RewardButton2( NullDiedTO.shop8 );
        bomb2.setRect( bomb1.right()+ BTN_GAP , bomb1.top(), BTN_SIZE, BTN_SIZE );
        add(bomb2);

        WndKingShop.RewardButton2 bomb3 = new WndKingShop.RewardButton2( NullDiedTO.shop9 );
        bomb3.setRect( bomb2.right()+ BTN_GAP , bomb2.top(), BTN_SIZE, BTN_SIZE );
        add(bomb3);

        WndKingShop.RewardButton2 bomb4 = new WndKingShop.RewardButton2( NullDiedTO.shop10);
        bomb4.setRect( bomb3.right()+ BTN_GAP , bomb3.top(), BTN_SIZE, BTN_SIZE );
        add(bomb4);

        WndKingShop.RewardButton2 bomb5 = new WndKingShop.RewardButton2( NullDiedTO.shop11 );
        bomb5.setRect( bomb4.right()+ BTN_GAP , bomb4.top(), BTN_SIZE, BTN_SIZE );
        add(bomb5);

        WndKingShop.RewardButton2 bomb6 = new WndKingShop.RewardButton2( NullDiedTO.shop12 );
        bomb6.setRect( bomb5.right() + BTN_GAP, bomb5.top(), BTN_SIZE, BTN_SIZE );
        add(bomb6);

        StyledButton btnSite = new StyledButton(Chrome.Type.WINDOW, Messages.get(this,"sellmod")){
            @Override
            protected void onClick() {
                super.onClick();
                Game.runOnRenderThread(new Callback() {
                    @Override
                    public void call() {
                        sell();
                    }
                });
            }
        };
        btnSite.icon(Icons.get(Icons.GOLD));
        btnSite.textColor(Window.CYELLOW);
        btnSite.setRect(56,-2, 65, 20 );
        add(btnSite);

        resize(WIDTH, (int) bomb6.bottom());
    }

    public static WndBag sell() {
        return GameScene.selectItem( itemSelector );
    }

    public static boolean canSell(Item item){
        if (item.value() <= 0)                                              return false;
        if (item.unique && !item.stackable)                                 return false;
        if (item instanceof Armor && ((Armor) item).checkSeal() != null)    return false;
        if (item.isEquipped(Dungeon.hero) && item.cursed)                   return false;
        return true;
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

    private void tell(String text) {
        Game.runOnRenderThread(new Callback() {
                                   @Override
                                   public void call() {
                                       GameScene.show(new WndQuest(new NullDiedTO(), text));
                                   }
                               }
        );
    }

    private void selectReward( Item reward ) {

        hide();

        reward.identify();
        if (reward.doPickUp( hero )) {
            GLog.i( Messages.get(hero, "you_now_have", reward.name()) );
        }

        //Ghost.Quest.complete();
    }

    private class RewardWindow extends WndInfoItem {

        public RewardWindow( Item item ) {
            super(item);

            RedButton btnConfirm = new RedButton(Messages.get(WndKingShop.class, "buy")){
                @Override
                protected void onClick() {
                    if(Dungeon.gold > 200) {
                        Dungeon.gold-=200;
                        WndKingShop.this.selectReward( item );
                        if (RandomBuff.level-- >= 0) {
                        }
                        Buff.prolong( hero, ReloadShop.class, 1f);
                        //Statistics.naiyaziCollected += 1;
                        WndKingShop.RewardWindow.this.hide();
                        //Badges.nyzvalidateGoldCollected();
                    } else {
                        tell(Messages.get(WndKingShop.class,"nomoney"));
                        WndKingShop.RewardWindow.this.hide();
                    }
                }
            };
            btnConfirm.setRect(0, height+2, width/2-1, 16);
            add(btnConfirm);

            RedButton btnCancel = new RedButton(Messages.get(WndKingShop.class, "cancel")){
                @Override
                protected void onClick() {
                    hide();
                }
            };
            btnCancel.setRect(btnConfirm.right()+2, height+2, btnConfirm.width(), 16);
            add(btnCancel);

            resize(width, (int)btnCancel.bottom());
        }
    }

    private class RewardWindow2 extends WndInfoItem {

        public RewardWindow2( Item item ) {
            super(item);

            RedButton btnConfirm = new RedButton(Messages.get(WndKingShop.class, "buy")){
                @Override
                protected void onClick() {
                    if(Dungeon.gold > 200) {
                        Dungeon.gold-=200;
                        Buff.prolong( hero, ReloadShop.class, 1f);
                        WndKingShop.this.selectReward( item );
                        if (RandomBuff.level-- >= 0) {
                        }
                        //Badges.nyzvalidateGoldCollected();
                        //Statistics.naiyaziCollected += 1;
                        WndKingShop.RewardWindow2.this.hide();
                    } else {
                        tell(Messages.get(WndKingShop.class,"nomoney"));
                    }
                }
            };
            btnConfirm.setRect(0, height+2, width/2-1, 16);
            add(btnConfirm);

            RedButton btnCancel = new RedButton(Messages.get(WndKingShop.class, "cancel")){
                @Override
                protected void onClick() {
                    hide();
                }
            };
            btnCancel.setRect(btnConfirm.right()+2, height+2, btnConfirm.width(), 16);
            add(btnCancel);

            resize(width, (int)btnCancel.bottom());
        }
    }

    public class RewardButton2 extends Component {

        protected NinePatch bg;
        protected ItemSlot slot;

        public RewardButton2(Item item) {
            bg = Chrome.get(Chrome.Type.RED_BUTTON);
            add(bg);

            slot = new ItemSlot(item) {
                @Override
                protected void onPointerDown() {
                    bg.brightness(1.2f);
                    Sample.INSTANCE.play(Assets.Sounds.CLICK);
                }

                @Override
                protected void onPointerUp() {
                    bg.resetColor();
                }

                @Override
                protected void onClick() {
                    ShatteredPixelDungeon.scene().addToFront(new WndKingShop.RewardWindow2(item));
                }
            };
            add(slot);
        }

        @Override
        protected void layout() {
            super.layout();

            bg.x = x;
            bg.y = y;
            bg.size(width, height);

            slot.setRect(x + 2, y + 2, width - 4, height - 4);
        }
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
                    ShatteredPixelDungeon.scene().addToFront(new WndKingShop.RewardWindow(item));
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

