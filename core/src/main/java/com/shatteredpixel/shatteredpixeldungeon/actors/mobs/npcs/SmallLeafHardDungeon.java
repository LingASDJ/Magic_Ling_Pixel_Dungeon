package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.SmallLeaf;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.SmallLeafPlot;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.Bag;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.PropBag;
import com.shatteredpixel.shatteredpixeldungeon.items.props.Prop;
import com.shatteredpixel.shatteredpixeldungeon.items.props.TerrorDoll;
import com.shatteredpixel.shatteredpixeldungeon.items.props.TerrorDollB;
import com.shatteredpixel.shatteredpixeldungeon.items.props.Trash;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SmallLeafSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.ItemButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.IconTitle;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndBag;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.watabou.noosa.Game;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class SmallLeafHardDungeon extends NPC {
    {
        spriteClass = SmallLeafSprite.class;
        maxLvl = -1;
    }

    public boolean first = true;
    SmallLeafPlot.PropChange plot = new SmallLeafPlot.PropChange();

    @Override
    public boolean interact(Char c){
        if(!first) {
            Game.runOnRenderThread(new Callback() {
                @Override
                public void call() {
                    GameScene.show(new WndOptions(new SmallLeafSprite(),
                            Messages.get(SmallLeafHardDungeon.class, "title"),
                            Messages.get(SmallLeafHardDungeon.class, "desc"),
                            Messages.get(SmallLeafHardDungeon.class, "change"),
                            Messages.get(SmallLeafHardDungeon.class, "gamble"),
                            Messages.get(SmallLeafHardDungeon.class, "cancel")) {
                        @Override
                        protected void onSelect(int index) {
                            super.onSelect(index);
                            if (index == 0) {
                                plot.change =true;
                                Game.runOnRenderThread(new Callback() {
                                    @Override
                                    public void call() {
                                        GameScene.show( new WndChangeProp(SmallLeafHardDungeon.this,null,0));
                                    }
                                });
                            } else if (index == 1) {
                                plot.change =true;
                                Game.runOnRenderThread(new Callback() {
                                    @Override
                                    public void call() {
                                        GameScene.show( new WndChangeProp(SmallLeafHardDungeon.this,null,1));
                                    }
                                });
                            } else if (index == 2){
                                GLog.b(Messages.get(SmallLeafHardDungeon.class,"cancelchange"));
                                die(this);
                                destroy();
                            }
                        }
                    });
                }
            });
        }else if(first){
            Game.runOnRenderThread(new Callback() {
                @Override
                public void call() {
                    GameScene.show(new WndDialog(plot,false));
                }
            });
            first=false;
        }
        return true;
    }

    @Override
    public int defenseSkill( Char enemy ) {
        return INFINITE_EVASION;
    }

    @Override
    public void damage( int dmg, Object src ) {
        //do nothing
    }

    @Override
    public boolean add(Buff buff ) {
        return false;
    }

    private static final String FIRST = "first";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(FIRST, first);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        first = bundle.getBoolean(FIRST);
    }

    public static class WndChangeProp extends Window {

        private static final int WIDTH		= 60;

        private static final int BTN_SIZE	= 32;
        private static final float GAP		= 2;
        public int mode;

        private ItemButton btnPressed;

        private ItemButton btnItem;
        private RedButton btnChange;

        public WndChangeProp( SmallLeafHardDungeon smallLeaf, Window wndParent,int mo) {
            super();

            mode = mo;

            IconTitle titlebar = new IconTitle();
            titlebar.icon( new SmallLeaf().sprite() );
            titlebar.label( Messages.titleCase( smallLeaf.name() ) );
            titlebar.setRect( 0, 0, WIDTH, 0 );
            add( titlebar );

            RenderedTextBlock message = PixelScene.renderTextBlock( Messages.get(this, "message"), 6 );
            message.maxWidth( WIDTH);
            message.setPos(0, titlebar.bottom() + GAP);
            add( message );

            btnItem = new ItemButton() {
                @Override
                protected void onClick() {
                    btnPressed = btnItem;
                    GameScene.selectItem( itemSelector );
                }
            };
            btnItem.setRect( WIDTH - (BTN_SIZE * 1.4f), message.top() + message.height() , BTN_SIZE, BTN_SIZE );
            add( btnItem );

            btnChange = new RedButton( Messages.get(this, "change") ) {
                @Override
                protected void onClick() {
                    if(btnItem.item() instanceof Prop){
                        if(mode == 0) {

                            switch (((Prop) btnItem.item()).kind) {
                                case 0:
                                    Prop p = Prop.randomPropA(( (Prop) btnItem.item()).rareness);
                                    p.collect();
                                    GLog.i(Messages.get(hero, "you_now_have", p.name()));

                                    Statistics.add((Prop)btnItem.item());

                                    btnItem.item().detach(Dungeon.hero.belongings.backpack);
                                    break;

                                case 1:
                                    p = Prop.randomPropB(( (Prop) btnItem.item()).rareness);
                                    p.collect();
                                    GLog.i(Messages.get(hero, "you_now_have", p.name()));

                                    Statistics.add((Prop)btnItem.item());

                                    btnItem.item().detach(Dungeon.hero.belongings.backpack);
                                    break;
                            }
                        } else{
                            if(Random.Float()>0.66f){
                                switch (((Prop) btnItem.item()).kind) {
                                    case 0:
                                        Prop p1 = Prop.randomPropA(( (Prop) btnItem.item()).rareness+1);
                                        p1.collect();
                                        GLog.i(Messages.get(hero, "you_now_have", p1.name()));

                                        Prop p2 = Prop.randomPropA(( (Prop) btnItem.item()).rareness);
                                        p2.collect();
                                        GLog.i(Messages.get(hero, "you_now_have", p2.name()));

                                        Statistics.add((Prop)btnItem.item());
                                        btnItem.item().detach(Dungeon.hero.belongings.backpack);
                                        break;
                                    case 1:
                                        Prop p = Prop.randomPropA(( (Prop) btnItem.item()).rareness);
                                        p.collect();
                                        GLog.i(Messages.get(hero, "you_now_have", p.name()));

                                        Statistics.add((Prop)btnItem.item());
                                        btnItem.item().detach(Dungeon.hero.belongings.backpack);
                                        break;
                                }
                            }else{
                                switch (((Prop) btnItem.item()).kind) {
                                    case 0:
                                        Prop p = Prop.randomPropB(( (Prop) btnItem.item()).rareness);
                                        p.collect();
                                        GLog.i(Messages.get(hero, "you_now_have", p.name()));
                                        if(btnItem.item() instanceof TerrorDollB){
                                            Statistics.add(new TerrorDoll());
                                        }else{
                                            Statistics.add((Prop)(btnItem.item()));
                                        }
                                        btnItem.item().detach(Dungeon.hero.belongings.backpack);
                                        break;
                                    case 1:
                                        Prop p1 = Prop.randomPropB(( (Prop) btnItem.item()).rareness+1);
                                        p1.collect();
                                        GLog.i(Messages.get(hero, "you_now_have", p1.name()));

                                        Prop p2 = Prop.randomPropB(( (Prop) btnItem.item()).rareness);
                                        p2.collect();
                                        GLog.i(Messages.get(hero, "you_now_have", p2.name()));

                                        if(btnItem.item() instanceof TerrorDollB){
                                            Statistics.add(new TerrorDoll());
                                        }else{
                                            Statistics.add((Prop)(btnItem.item()));
                                        }
                                        btnItem.item().detach(Dungeon.hero.belongings.backpack);
                                        break;
                                }
                            }
                        }
                    }

                    if(Dungeon.hero.belongings.getItem(Trash.class)!= null){
                        for(Trash t : Dungeon.hero.belongings.getAllItems(Trash.class)){
                            t.detach(Dungeon.hero.belongings.backpack);
                        }
                        GLog.b(Messages.get(Trash.class,"trash"));
                    }

                    hide();
                    if (wndParent != null){
                        wndParent.hide();
                    }
                    GLog.b(Messages.get(SmallLeafHardDungeon.class,"changed"));
                    smallLeaf.die(smallLeaf);
                    smallLeaf.destroy();
                }
            };
            btnChange.enable( false );
            btnChange.setRect( 0, btnItem.bottom(), WIDTH, 20 );
            add(btnChange);


            resize( WIDTH, (int) btnChange.bottom() );
        }

        protected WndBag.ItemSelector itemSelector = new WndBag.ItemSelector() {

            @Override
            public String textPrompt() {
                return Messages.get(this, "select");
            }

            @Override
            public Class<?extends Bag> preferredBag(){
                return PropBag.class;
            }

            @Override
            public boolean itemSelectable(Item item) {
                return item instanceof Prop;
            }

            @Override
            public void onSelect( Item item ) {
                if (item != null && btnPressed.parent != null) {
                    btnPressed.item(item);
                    btnChange.enable(true);
                }
            }
        };

    }
}
