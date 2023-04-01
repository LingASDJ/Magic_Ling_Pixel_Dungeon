package com.shatteredpixel.shatteredpixeldungeon.windows;

import com.shatteredpixel.shatteredpixeldungeon.DLC;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.CheckBox;
import com.shatteredpixel.shatteredpixeldungeon.ui.IconButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.ScrollPane;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.noosa.Image;
import com.watabou.noosa.ui.Component;

import java.util.ArrayList;

public class WndDLC extends Window {

    private static final int WIDTH		= 120;
    private static final int HEIGHT		= 40;
    private static final int BTN_HEIGHT = 16;
    private static final int GAP        = 1;

    private boolean editable;
    private ArrayList<WndDLC.CanScrollCheckBox> boxes;
    private ArrayList<WndDLC.CanScrollInfo> infos;

    public WndDLC( long checked, boolean editable ) {

        super();

        resize(WIDTH, HEIGHT);

        this.editable = editable;

        ScrollPane pane = new ScrollPane(new Component()) {
            @Override
            public void onClick(float x, float y) {
                int max_size = boxes.size();
                for (int i = 0; i < max_size; ++i) {
                    if (boxes.get(i).onClick(x, y))
                        return;
                }
                max_size = infos.size();
                for(int i = 0; i<max_size;++i){
                    if(infos.get(i).onClick(x,y)){
                        return;
                    }
                }
            }
        };
        add(pane);
        pane.setRect(0, GAP, WIDTH, HEIGHT - 2 * GAP);
        Component content = pane.content();

        boxes = new ArrayList<>();
        infos = new ArrayList<>();
        float pos = 0;

        final int normal_mode = 0;
        final int hard_mode = 4;
        for (int i = 0; i < DLC.NAME_IDS.length; i++) {

            final String dlc = DLC.NAME_IDS[i];

            if(i==normal_mode || i==hard_mode){
                RenderedTextBlock block = PixelScene.renderTextBlock(9);
                switch (i){
                    case normal_mode:
                        block.text(Messages.get(WndDLC.class, "mode"));
                        block.hardlight(0x00FF00);
                        break;
                }
                block.setPos((WIDTH - block.width()) / 2,
                        pos + GAP *4);
                PixelScene.align(block);
                content.add(block);
                pos += block.height() + 11*GAP;

            }


            WndDLC.CanScrollCheckBox cb = new WndDLC.CanScrollCheckBox(Messages.get(DLC.class, dlc));
            cb.checked((checked & DLC.MASKS[i]) != 0);
            cb.active = editable;

            if (i > 0) {
                pos += GAP;
            }
            cb.setRect(0, pos, WIDTH - 16, BTN_HEIGHT);

            content.add(cb);
            boxes.add(cb);

            WndDLC.CanScrollInfo info = new WndDLC.CanScrollInfo(Icons.get(Icons.INFO)) {
                @Override
                protected void onClick() {
                    super.onClick();
                    ShatteredPixelDungeon.scene().add(
                            new WndMessage(Messages.get(DLC.class, dlc +
                                    "_desc"))
                    );
                }
            };
            info.setRect(cb.right(), pos, 16, BTN_HEIGHT);
            infos.add(info);
            content.add(info);

            pos = cb.bottom();
        }
    }



    @Override
    public void onBackPressed() {

        if (editable) {
            int value = 0;
            for (int i=0; i < boxes.size(); i++) {
                if (boxes.get( i ).checked()) {
                    value |= DLC.MASKS[i];
                }
            }
            SPDSettings.dlc( value );
        }

        super.onBackPressed();
    }

    public static class CanScrollCheckBox extends CheckBox {

        public CanScrollCheckBox(String label) {
            super(label);
        }

        protected boolean onClick(float x, float y){
            if (!inside(x, y)) return false;
            if(active) onClick();

            return true;
        }

        @Override
        protected void layout(){
            super.layout();
            hotArea.width = hotArea.height = 0;
        }
    }

    public static class CanScrollInfo extends IconButton {
        public CanScrollInfo(Image Icon){super(Icon);}

        protected boolean onClick(float x, float y){
            if(!inside(x,y)) return false;
            if (active) onClick();
            return true;
        }

        @Override
        protected void layout(){
            super.layout();
            hotArea.width = hotArea.height = 0;
        }
    }
}
