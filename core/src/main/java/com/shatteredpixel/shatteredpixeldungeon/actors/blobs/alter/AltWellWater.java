package com.shatteredpixel.shatteredpixeldungeon.actors.blobs.alter;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.LockSword;
import com.shatteredpixel.shatteredpixeldungeon.journal.Notes;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

abstract public class AltWellWater extends Blob {

    @Override
    protected void evolve() {
        int cell;
        for (int i=area.top-1; i <= area.bottom; i++) {
            for (int j = area.left-1; j <= area.right; j++) {
                cell = j + i* Dungeon.level.width();
                if (Dungeon.level.insideMap(cell)) {
                    off[cell] = cur[cell];
                    volume += off[cell];
                }
            }
        }
    }

    protected boolean affect( int pos ) {

        Heap heap;

        if (pos == Dungeon.hero.pos && affectHero( Dungeon.hero )) {

            clear(pos);
            if (volume <= 0 && landmark() != null) Notes.remove(landmark());
            return true;

        } else if ((heap = Dungeon.level.heaps.get( pos )) != null) {

            Item oldItem = heap.peek();
            Item newItem = affectItem( oldItem, pos );

            if (newItem != null) {

                if (newItem == oldItem || oldItem instanceof LockSword) {

                } else if (oldItem.quantity() > 1) {

                    oldItem.quantity( oldItem.quantity() - 1 );
                    heap.drop( newItem );

                } else {
                    heap.replace( oldItem, newItem );
                }

                heap.sprite.link();
                clear(pos);
                if (volume <= 0 && landmark() != null) Notes.remove(landmark());

                return true;

            } else {

                int newPlace;
                do {
                    newPlace = pos + PathFinder.NEIGHBOURS8[Random.Int( 8 )];
                } while (!Dungeon.level.passable[newPlace] && !Dungeon.level.avoid[newPlace]);
                Dungeon.level.drop( heap.pickUp(), newPlace ).sprite.drop( pos );

                return false;

            }

        } else {

            return false;

        }
    }

    protected abstract boolean affectHero( Hero hero );

    protected abstract Item affectItem( Item item, int pos );

    public static void affectCell( int cell ) {

        // 外层数组包含所有井水类型
        Class<?>[] waters = {AWaterOfHealth.class, AWaterOfAwareness.class, AWaterOfTransmutation.class};
        Game.runOnRenderThread(new Callback() {
            @Override
            public void call() {
                GameScene.show(
                        new WndOptions( new Image(Dungeon.level.tilesTex(), 48, 16, 16, 16),
                                Messages.get(AltWellWater.class, "wellreading"),
                                Messages.get(AltWellWater.class, "drink"),
                                Messages.get(AltWellWater.class, "yes"),
                                Messages.get(AltWellWater.class, "no") ) {

                            private float elapsed = 0f;

                            @Override
                            public synchronized void update() {
                                super.update();
                                elapsed += Game.elapsed;
                            }

                            @Override
                            public void hide() {
                                if (elapsed > 0.2f){
                                    super.hide();
                                }
                            }

                            @Override
                            protected void onSelect( int index ) {
                                if (index == 0 && elapsed > 0.2f) {
                                    for (Class<?>waterClass : waters) {
                                        AltWellWater water = (AltWellWater)Dungeon.level.blobs.get( waterClass );
                                        if (water != null &&
                                                water.volume > 0 &&
                                                water.cur[cell] > 0 &&
                                                water.affect( cell )) {

                                            Level.set( cell, Terrain.EMPTY_WELL );
                                            GameScene.updateMap( cell );

                                            Class<?>[] threeWaters = {AWaterOfHealth.class,AWaterOfAwareness.class, AWaterOfTransmutation.class};
                                            for (Class<?> threeWaterClass : threeWaters) {
                                                Blob blob = Dungeon.level.blobs.get(threeWaterClass);
                                                if (blob != null) {

                                                    for (int blobCell : blob.getActiveCells()) {
                                                        Level.set(blobCell, Terrain.EMPTY_WELL);
                                                        GameScene.updateMap(blobCell);
                                                    }
                                                    blob.fullyClear();
                                                    Notes.remove(blob.landmark());
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                );
            }
        });

    }
}

