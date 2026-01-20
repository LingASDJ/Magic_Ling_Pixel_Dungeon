package com.shatteredpixel.shatteredpixeldungeon.ui;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;

public class LanterFireCator extends Button {

    protected float lightness = 0;

    public void flash() {
        lightness = 1f;
    }
    private static boolean targeting = false;

    private Toolbar.Tool slot;

    private static Item item = null;

    public LanterFireCator() {

        int color=0x7C8072;

        setSize( 21, 12 );

        visible=false;
    }

    @Override
    protected void createChildren() {

        super.createChildren();

        add(slot = new Toolbar.Tool(0, 64, 24, 70) {

            @Override
            protected void onClick() {
                super.onClick();
                //GameScene.show(new PageWindows());
            }
        });

    }

    @Override
    protected void layout() {
        super.layout();

        slot.setRect( x, y, width, height);
    }

    @Override
    public void update() {
        super.update();

        slot.enable(Dungeon.hero.isAlive() && Dungeon.hero.ready);
        if (item == null) {
            visible = true;
        }
        else visible=true;
    }

    public static boolean targeting()
    {
        return targeting;
    }
}


