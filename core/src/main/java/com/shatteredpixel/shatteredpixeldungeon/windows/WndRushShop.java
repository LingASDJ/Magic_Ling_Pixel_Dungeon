package com.shatteredpixel.shatteredpixeldungeon.windows;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Chrome;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ReloadShop;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ReloadShopTwo;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
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

public class WndRushShop extends Window {
    private static final int WIDTH		= 120;
    private static final int BTN_SIZE	= 16;
    private static final int BTN_GAP	= 3;
    private static final int GAP		= 3;
    public WndRushShop() {
        IconTitle titlebar = new IconTitle();
        titlebar.setRect(0, 0, WIDTH, 0);
        titlebar.icon(new ShopkKingSprite());
        if (Statistics.bossRushMode) {
            titlebar.label(Messages.get(WndRushShop.class,"title_bar"));
        } else {
            titlebar.label(Messages.get(WndRushShop.class,"king"));
        }

        add( titlebar );

        RenderedTextBlock message = PixelScene.renderTextBlock( Statistics.bossRushMode ? (Messages.get(WndRushShop.class,"run")) :
                (Messages.get(WndRushShop.class,"select")), 6 );
        message.maxWidth(WIDTH);
        message.setPos(0, titlebar.bottom() + GAP);
        add( message );
        RewardButton2 s1 = null;
        RewardButton2 s6 = null;
        for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
            if (mob instanceof NullDiedTO) {
                RewardButton shop1 = new RewardButton(((NullDiedTO) mob).shop1);
                shop1.setRect((WIDTH - BTN_GAP) / 6f - BTN_SIZE, message.top() + message.height() + BTN_GAP, BTN_SIZE,
                        BTN_SIZE);
                add(shop1);

                RewardButton shop2 = new RewardButton(((NullDiedTO) mob).shop2);
                shop2.setRect(shop1.right() + BTN_GAP, shop1.top(), BTN_SIZE, BTN_SIZE);
                add(shop2);

                RewardButton shop3 = new RewardButton(((NullDiedTO) mob).shop3);
                shop3.setRect(shop2.right() + BTN_GAP, shop2.top(), BTN_SIZE, BTN_SIZE);
                add(shop3);

                RewardButton shop4 = new RewardButton(((NullDiedTO) mob).shop4);
                shop4.setRect(shop3.right() + BTN_GAP, shop3.top(), BTN_SIZE, BTN_SIZE);
                add(shop4);

                RewardButton shop5 = new RewardButton(((NullDiedTO) mob).shop5);
                shop5.setRect(shop4.right() + BTN_GAP, shop4.top(), BTN_SIZE, BTN_SIZE);
                add(shop5);

                RewardButton shop6 = new RewardButton(((NullDiedTO) mob).shop6);
                shop6.setRect(shop5.right() + BTN_GAP, shop5.top(), BTN_SIZE, BTN_SIZE);
                add(shop6);

                RewardButton2 bomb1 = new RewardButton2(((NullDiedTO) mob).shop7);
                bomb1.setRect(shop1.left(), shop1.bottom(), BTN_SIZE, BTN_SIZE);
                add(bomb1);

                RewardButton2 bomb2 = new RewardButton2(((NullDiedTO) mob).shop8);
                bomb2.setRect(bomb1.right() + BTN_GAP, bomb1.top(), BTN_SIZE, BTN_SIZE);
                add(bomb2);

                RewardButton2 bomb3 = new RewardButton2(((NullDiedTO) mob).shop9);
                bomb3.setRect(bomb2.right() + BTN_GAP, bomb2.top(), BTN_SIZE, BTN_SIZE);
                add(bomb3);

                RewardButton2 bomb4 = new RewardButton2(((NullDiedTO) mob).shop10);
                bomb4.setRect(bomb3.right() + BTN_GAP, bomb3.top(), BTN_SIZE, BTN_SIZE);
                add(bomb4);

                RewardButton2 bomb5 = new RewardButton2(((NullDiedTO) mob).shop11);
                bomb5.setRect(bomb4.right() + BTN_GAP, bomb4.top(), BTN_SIZE, BTN_SIZE);
                add(bomb5);

                RewardButton2 bomb6 = new RewardButton2(((NullDiedTO) mob).shop12);
                bomb6.setRect(bomb5.right() + BTN_GAP, bomb5.top(), BTN_SIZE, BTN_SIZE);
                add(bomb6);

                RewardButton2 x1 = new RewardButton2(((NullDiedTO) mob).shop13);
                x1.setRect(bomb1.left(), bomb1.bottom(), BTN_SIZE, BTN_SIZE);
                add(x1);

                RewardButton2 x2 = new RewardButton2(((NullDiedTO) mob).shop14);
                x2.setRect(x1.right() + BTN_GAP, x1.top(), BTN_SIZE, BTN_SIZE);
                add(x2);

                RewardButton2 x3 = new RewardButton2(((NullDiedTO) mob).shop15);
                x3.setRect(x2.right() + BTN_GAP, x2.top(), BTN_SIZE, BTN_SIZE);
                add(x3);

                RewardButton2 x4 = new RewardButton2(((NullDiedTO) mob).shop16);
                x4.setRect(x3.right() + BTN_GAP, x3.top(), BTN_SIZE, BTN_SIZE);
                add(x4);

                RewardButton2 x5 = new RewardButton2(((NullDiedTO) mob).shop17);
                x5.setRect(x4.right() + BTN_GAP, x4.top(), BTN_SIZE, BTN_SIZE);
                add(x5);

                RewardButton2 x6 = new RewardButton2(((NullDiedTO) mob).shop18);
                x6.setRect(x5.right() + BTN_GAP, x5.top(), BTN_SIZE, BTN_SIZE);
                add(x6);

                s1 = new RewardButton2(((NullDiedTO) mob).shop19);
                s1.setRect(x1.left(), x1.bottom(), BTN_SIZE, BTN_SIZE);
                add(s1);

                RewardButton2 s2 = new RewardButton2(((NullDiedTO) mob).shop20);
                s2.setRect(s1.right() + BTN_GAP, s1.top(), BTN_SIZE, BTN_SIZE);
                add(s2);

                RewardButton2 s3 = new RewardButton2(((NullDiedTO) mob).shop21);
                s3.setRect(s2.right() + BTN_GAP, s2.top(), BTN_SIZE, BTN_SIZE);
                add(s3);

                RewardButton2 s4 = new RewardButton2(((NullDiedTO) mob).shop22);
                s4.setRect(s3.right() + BTN_GAP, s3.top(), BTN_SIZE, BTN_SIZE);
                add(s4);

                RewardButton2 s5 = new RewardButton2(((NullDiedTO) mob).shop23);
                s5.setRect(s4.right() + BTN_GAP, s4.top(), BTN_SIZE, BTN_SIZE);
                add(s5);

                s6 = new RewardButton2(((NullDiedTO) mob).shop24);
                s6.setRect(s5.right() + BTN_GAP, s5.top(), BTN_SIZE, BTN_SIZE);
                add(s6);
            }
        }

        StyledButton btnReload = new StyledButton(Chrome.Type.GREY_BUTTON_TR, Messages.get(this,"reloadshop"),6){
            @Override
            protected void onClick() {
                super.onClick();
                Game.runOnRenderThread(new Callback() {
                    @Override
                    public void call() {
                        if(Dungeon.rushgold>=1) {
                            hide();
                            Buff.prolong(hero, ReloadShopTwo.class, 1f);
                            Dungeon.rushgold -= 1;
                        } else {
                            GLog.n(Messages.get(WndRushShop.class,"x_gold"));
                        }
                    }
                });
            }
        };
        btnReload.textColor(Window.CYELLOW);
        btnReload.icon(Icons.get(Icons.CHANGES));
        btnReload.setRect(80,-2, 40, 20 );
        add(btnReload);

        StyledButton btnGoBack = new StyledButton(Chrome.Type.GREY_BUTTON_TR, Messages.get(this,"sellmod"),6){
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
        btnGoBack.textColor(Window.CYELLOW);
        btnGoBack.icon(Icons.get(Icons.DEPTH));

        btnGoBack.setRect(s1.left(), s6.bottom()+5,111,BTN_SIZE );
        add(btnGoBack);

        resize(WIDTH, (int) btnGoBack.bottom());
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
                GameScene.show( new WndRushTradeItem( item, parentWnd ) );
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
            int sellPrice = item.RushValue();
            RedButton btnConfirm = new RedButton(Messages.get(WndRushShop.class, "buy",sellPrice)){
                @Override
                protected void onClick() {

                    if(Dungeon.rushgold >=sellPrice) {
                        Dungeon.rushgold-= sellPrice;
                        WndRushShop.this.selectReward( item );
                        Buff.prolong( hero, ReloadShop.class, 1f);
                        //Statistics.naiyaziCollected += 1;
                        WndRushShop.RewardWindow.this.hide();
                        //Badges.nyzvalidateGoldCollected();
                    } else {
                        tell(Messages.get(WndRushShop.class,"nomoney"));
                        WndRushShop.RewardWindow.this.hide();
                    }
                }
            };
            btnConfirm.setRect(0, height+2, width, 16);
            add(btnConfirm);

            resize(width, (int)btnConfirm.bottom());
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
                    ShatteredPixelDungeon.scene().addToFront(new WndRushShop.RewardWindow(item));
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
                    ShatteredPixelDungeon.scene().addToFront(new WndRushShop.RewardWindow(item));
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


