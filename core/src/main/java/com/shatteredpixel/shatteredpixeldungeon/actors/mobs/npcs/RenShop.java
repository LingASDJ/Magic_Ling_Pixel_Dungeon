//package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs;
//
//import static com.shatteredpixel.shatteredpixeldungeon.BGMPlayer.playBGM;
//
//import com.shatteredpixel.shatteredpixeldungeon.Assets;
//import com.shatteredpixel.shatteredpixeldungeon.BGMPlayer;
//import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
//import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
//import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
//import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.NxhyGuard;
//import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
//import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ElmoParticle;
//import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
//import com.shatteredpixel.shatteredpixeldungeon.items.Item;
//import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
//import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
//import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
//import com.shatteredpixel.shatteredpixeldungeon.sprites.RenSprite;
//import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
//import com.shatteredpixel.shatteredpixeldungeon.windows.WndBag;
//import com.shatteredpixel.shatteredpixeldungeon.windows.WndTradeItem;
//import com.watabou.noosa.Game;
//import com.watabou.utils.Callback;
//
//public class RenShop extends NPC {
//
//    {
//        spriteClass = RenSprite.class;
//
//        properties.add(Property.IMMOVABLE);
//    }
//    private boolean seenBefore = false;
//    @Override
//    protected boolean act() {
//
//        if (!seenBefore && Dungeon.level.heroFOV[pos]) {
//            yell( Messages.get(this, "greetings", Dungeon.hero.name() ) );
//            seenBefore = true;
//        }
//        if (!Dungeon.level.heroFOV[pos]) {
//            BGMPlayer.playBGMWithDepth();
//        } else {
//           playBGM(Assets.Music.RENSHOP, true);
//        }
//
//        throwItem();
//
//        sprite.turnTo( pos, Dungeon.hero.pos );
//        spend( TICK );
//        return true;
//    }
//
//    @Override
//    public void damage( int dmg, Object src ) {
//        flee();
//    }
//
//    @Override
//    public void add( Buff buff ) {
//        flee();
//    }
//
//    public void flee() {
//        destroy();
//
//        sprite.killAndErase();
//        CellEmitter.get( pos ).burst( ElmoParticle.FACTORY, 6 );
//        GLog.negative(Messages.get(this,"guards"));
//        new NxhyGuard();
//        next();
//    }
//
//    @Override
//    public void destroy() {
//        super.destroy();
//        for (Heap heap: Dungeon.level.heaps.valueList()) {
//            if (heap.type == Heap.Type.FOR_SALE) {
//                CellEmitter.get( heap.pos ).burst( ElmoParticle.FACTORY, 4 );
//                heap.destroy();
//            }
//        }
//    }
//
//    @Override
//    public boolean reset() {
//        return true;
//    }
//
//    public static WndBag sell() {
//        return GameScene.selectItem( itemSelector );
//    }
//
//    public static boolean canSell(Item item){
//        if (item.value() <= 0)                                              return false;
//        if (item.unique && !item.stackable)                                 return false;
//        if (item instanceof Armor && ((Armor) item).checkSeal() != null)    return false;
//        if (item.isEquipped(Dungeon.hero) && item.cursed)                   return false;
//        return true;
//    }
//
//    private static WndBag.ItemSelector itemSelector = new WndBag.ItemSelector() {
//        @Override
//        public String textPrompt() {
//            return Messages.get(Shopkeeper.class, "sell");
//        }
//
//        @Override
//        public boolean itemSelectable(Item item) {
//            return Shopkeeper.canSell(item);
//        }
//
//        @Override
//        public void onSelect( Item item ) {
//            if (item != null) {
//                WndBag parentWnd = sell();
//                GameScene.show( new WndTradeItem( item, parentWnd ) );
//            }
//        }
//    };
//
//    @Override
//    public boolean interact(Char c) {
//        if (c != Dungeon.hero) {
//            return true;
//        }
//        Game.runOnRenderThread(new Callback() {
//            @Override
//            public void call() {
//                sell();
//            }
//        });
//        return true;
//    }
//}
//
