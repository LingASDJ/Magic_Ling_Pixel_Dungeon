package com.shatteredpixel.shatteredpixeldungeon.windows;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Chrome;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AutoRandomBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ReloadShop;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.AutoShopReBot;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Shopkeeper;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.AutoShopRoBotSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.ItemSlot;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Game;
import com.watabou.noosa.NinePatch;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.ui.Component;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class WndAutoShop extends Window {
    private static final int WIDTH		= 120;
    private static final int BTN_SIZE	= 32;
    private static final int BTN_GAP	= 6;
    private static final int GAP		= 6;


    public WndAutoShop() {
        IconTitle titlebar = new IconTitle();
        titlebar.setRect(0, 0, WIDTH, 0);
        titlebar.icon(new AutoShopRoBotSprite());
        titlebar.label(Messages.get(WndAutoShop.class,"welcome") );
        add( titlebar );
        RenderedTextBlock message =
                PixelScene.renderTextBlock( (Messages.get(WndAutoShop.class,"xwelcome"))+ 200 * (Dungeon.depth / 5) +(Messages.get(WndAutoShop.class,"gold")), 6 );
        message.maxWidth(WIDTH);
        message.setPos(0, titlebar.bottom() + GAP);
        add( message );

        WndAutoShop.RewardButton shop1 = new WndAutoShop.RewardButton( AutoShopReBot.shop1 );
        shop1.setRect( (WIDTH - BTN_GAP) / 3f - BTN_SIZE, message.top() + message.height() + BTN_GAP, BTN_SIZE,
                BTN_SIZE );
        add( shop1 );

        WndAutoShop.RewardButton shop2 = new WndAutoShop.RewardButton( AutoShopReBot.shop2 );
        shop2.setRect( shop1.right() + BTN_GAP, shop1.top(), BTN_SIZE, BTN_SIZE );
        add(shop2);

        WndAutoShop.RewardButton shop3 = new WndAutoShop.RewardButton( AutoShopReBot.shop3 );
        shop3.setRect( shop2.right() + BTN_GAP, shop2.top(), BTN_SIZE, BTN_SIZE );
        add(shop3);

        WndAutoShop.RewardButton bomb1 = new WndAutoShop.RewardButton( AutoShopReBot.shop4 );
        bomb1.setRect( shop1.left() , shop1.bottom(), BTN_SIZE, BTN_SIZE );
        add(bomb1);

        WndAutoShop.RewardButton bomb2 = new WndAutoShop.RewardButton( AutoShopReBot.shop5);
        bomb2.setRect( bomb1.right()+ BTN_GAP , bomb1.top(), BTN_SIZE, BTN_SIZE );
        add(bomb2);

        WndAutoShop.RewardButton bomb3 = new WndAutoShop.RewardButton( AutoShopReBot.shop6 );
        bomb3.setRect( bomb2.right()+ BTN_GAP , bomb2.top(), BTN_SIZE, BTN_SIZE );
        add(bomb3);

        resize(WIDTH, (int) bomb3.bottom());
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
                if(Statistics.bossRushMode){
                    GameScene.show( new WndRushTradeItem( item, parentWnd ) );
                } else {
                    GameScene.show( new WndTradeItem( item, parentWnd ) );
                }
            }
        }
    };

    private void tell(String text) {
        Game.runOnRenderThread(new Callback() {
                                   @Override
                                   public void call() {
                                       GameScene.show(new WndQuest(new AutoShopReBot(), text));
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

            RedButton btnConfirm = new RedButton(Messages.get(WndAutoShop.class, "buy")){
                @Override
                protected void onClick() {
                    if(Dungeon.hero.buff(AutoRandomBuff.class) == null) {
                        tell(Messages.get(WndAutoShop.class,"maxbuy"));
                        for (Buff buff : hero.buffs()) {
                            if (buff instanceof AutoRandomBuff) {
                                buff.detach();
                            }
                        }
                    } else if(Dungeon.gold > 200 * (Dungeon.depth/5)) {
                        Dungeon.gold-=200 * (Dungeon.depth/5);
                        WndAutoShop.this.selectReward( item );
                        if(Dungeon.hero.buff(AutoRandomBuff.class) != null) {
                            AutoRandomBuff.level -= Random.Int(4);
                            if(AutoRandomBuff.level <= 0) {
                                for (Buff buff : hero.buffs()) {
                                    if (buff instanceof AutoRandomBuff) {
                                        buff.detach();
                                    }
                                }
                            }
                        }
                        Buff.prolong( hero, ReloadShop.class, 1f);
                        WndAutoShop.RewardWindow.this.hide();
                    } else {
                        tell(Messages.get(WndAutoShop.class,"nomoney"));
                        WndAutoShop.RewardWindow.this.hide();
                    }
                }
            };
            btnConfirm.setRect(0, height+2, width/2-1, 16);
            add(btnConfirm);

            RedButton btnCancel = new RedButton(Messages.get(WndAutoShop.class, "cancel")){
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
                    ShatteredPixelDungeon.scene().addToFront(new WndAutoShop.RewardWindow(item));
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

