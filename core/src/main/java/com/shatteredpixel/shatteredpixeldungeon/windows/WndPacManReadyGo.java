package com.shatteredpixel.shatteredpixeldungeon.windows;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.depth;
import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LostInventory;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.status.ScoreMiniGame;
import com.shatteredpixel.shatteredpixeldungeon.custom.testmode.TestItem;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.ClassArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.Artifact;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.EtherealChains;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TimekeepersHourglass;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.Ring;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfHaste;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfMagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Swiftthistle;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.InterlevelScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIcon;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.noosa.Game;

import java.util.ArrayList;

public class WndPacManReadyGo extends Window {

    private static final int WIDTH		= 120;
    private static final int BTN_HEIGHT	= 20;
    private static final float GAP		= 2;
    private static final float BTN_GAP  = 5;

    private static final int BTN_SIZE	= 24;

    public static Object instance;

    private WndBlacksmith.ItemButtonX btnItem1;
    private WndBlacksmith.ItemButtonX btnItem2;
    private WndBlacksmith.ItemButtonX btnItem3;
    private WndBlacksmith.ItemButtonX btnItem4;
    private WndBlacksmith.ItemButtonX btnPressed;

    RedButton btnContinue;

    public WndPacManReadyGo( ) {

        super();

        instance = this;

        IconTitle titlebar = new IconTitle();
        titlebar.icon( new BuffIcon(BuffIndicator.PACMAN_GAME,true) );
        titlebar.label( Messages.titleCase(Messages.get(this, "title")) );
        titlebar.setRect( 0, 0, WIDTH, 0 );
        add( titlebar );

        RenderedTextBlock message = PixelScene.renderTextBlock(Messages.get(this, "message"), 6 );
        message.maxWidth(WIDTH);
        message.setPos(0, titlebar.bottom() + GAP);
        add( message );

        btnItem1 = new WndBlacksmith.ItemButtonX() {
            @Override
            protected void onClick() {
                btnPressed = btnItem1;
                GameScene.selectItem( itemSelector );
            }
        };
        btnItem1.item(null);
        btnItem1.setRect( (WIDTH - BTN_GAP) / 4 - BTN_SIZE, message.bottom() + BTN_GAP, BTN_SIZE, BTN_SIZE );
        add( btnItem1 );

        btnItem2 = new WndBlacksmith.ItemButtonX() {
            @Override
            protected void onClick() {
                btnPressed = btnItem2;
                GameScene.selectItem( itemSelector );
            }
        };
        btnItem2.item(null);
        btnItem2.setRect( btnItem1.right() + BTN_GAP, btnItem1.top(), BTN_SIZE, BTN_SIZE );
        add( btnItem2 );

        btnItem3 = new WndBlacksmith.ItemButtonX() {
            @Override
            protected void onClick() {
                btnPressed = btnItem3;
                GameScene.selectItem( itemSelector );
            }
        };
        btnItem3.item(null);
        btnItem3.setRect( btnItem2.right() + BTN_GAP, btnItem2.top(), BTN_SIZE, BTN_SIZE );
        add( btnItem3 );


        btnItem4 = new WndBlacksmith.ItemButtonX() {
            @Override
            protected void onClick() {
                btnPressed = btnItem4;
                GameScene.selectItem( itemSelector );
            }
        };
        btnItem4.item(null);
        btnItem4.setRect( btnItem3.right() + BTN_GAP, btnItem3.top(), BTN_SIZE, BTN_SIZE );
        add( btnItem4);

        btnContinue = new RedButton( Messages.get(this, "confirm") ) {
            @Override
            protected void onClick() {
                hide();

                ArrayList<TestItem> asi = hero.belongings.getAllItems(TestItem.class);
                for (TestItem w : asi.toArray(new TestItem[0])){
                    w.keptThoughLostInvent = true;
                }

                WandOfMagicMissile.AltWandOfMagicMissile staff = new WandOfMagicMissile.AltWandOfMagicMissile();
                staff.upgrade().collect();
                staff.identify().level(10);
                staff.curCharges = staff.maxCharges = 10;
                staff.keptThoughLostInvent = true;
                Dungeon.quickslot.setSlot(0, staff);

                if (btnItem1.item != null){
                    btnItem1.item.keptThoughLostInvent = true;
                }
                if (btnItem2.item != null){
                    btnItem2.item.keptThoughLostInvent = true;
                }
                if (btnItem3.item != null){
                    btnItem3.item.keptThoughLostInvent = true;
                }
                if (btnItem4.item != null){
                    btnItem4.item.keptThoughLostInvent = true;
                }

                InterlevelScene.mode = InterlevelScene.Mode.REDSTART;
                TimekeepersHourglass.timeFreeze timeFreeze = Dungeon.hero.buff(TimekeepersHourglass.timeFreeze.class);
                if (timeFreeze != null) timeFreeze.disarmPresses();
                Swiftthistle.TimeBubble timeBubble = Dungeon.hero.buff(Swiftthistle.TimeBubble.class);
                if (timeBubble != null) timeBubble.disarmPresses();
                InterlevelScene.curTransition = new LevelTransition();
                InterlevelScene.curTransition.destDepth = depth;
                InterlevelScene.curTransition.destType = LevelTransition.Type.REGULAR_ENTRANCE;
                InterlevelScene.curTransition.destBranch = 1;
                InterlevelScene.curTransition.type = LevelTransition.Type.REGULAR_EXIT;
                InterlevelScene.curTransition.centerCell  = -1;
                Game.switchScene( InterlevelScene.class );
                Buff.affect( hero, LostInventory.class);
                Buff.affect( hero, ScoreMiniGame.class).set((100), 1);
                GameScene.cure( Dungeon.hero );
            }
        };
        btnContinue.setRect( 0, btnItem1.bottom() + BTN_GAP, WIDTH, BTN_HEIGHT );
        add( btnContinue );

        resize( WIDTH, (int)btnContinue.bottom() );
    }

    protected WndBag.ItemSelector itemSelector = new WndBag.ItemSelector() {

        @Override
        public String textPrompt() {
            return Messages.get(WndResurrect.class, "prompt");
        }

        @Override
        public boolean itemSelectable(Item item) {
            return item instanceof Weapon || item instanceof Ring && !(item instanceof RingOfHaste) || item instanceof Armor && !(item instanceof ClassArmor) || item instanceof Artifact && !(item instanceof EtherealChains);
        }

        @Override
        public void onSelect(Item item) {
            if (item != null && btnPressed.parent != null) {
                btnPressed.item(item);  // 设置当前按钮的物品

                // 定义按钮数组
                WndBlacksmith.ItemButtonX[] buttons = {btnItem1, btnItem2, btnItem3, btnItem4};

                // 遍历按钮数组，检查是否有相同物品
                for (int i = 0; i < buttons.length; i++) {
                    for (int j = i + 1; j < buttons.length; j++) {
                        if (buttons[i].item != null && buttons[i].item.equals(buttons[j].item)) {
                            // 如果找到重复物品，清空另一个按钮的物品
                            if (btnPressed == buttons[i]) {
                                buttons[j].clear();
                            } else {
                                buttons[i].clear();
                            }
                        }
                    }
                }
            }
        }
    };

    @Override
    public void destroy() {
        super.destroy();
        instance = null;
    }

    @Override
    public void onBackPressed() {
    }
}

