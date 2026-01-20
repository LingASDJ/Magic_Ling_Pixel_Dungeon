package com.shatteredpixel.shatteredpixeldungeon.windows;

import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Chrome;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.custom.messages.M;
import com.shatteredpixel.shatteredpixeldungeon.ui.CheckBox;
import com.shatteredpixel.shatteredpixeldungeon.ui.IconButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.ScrollPane;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.noosa.Image;
import com.watabou.noosa.ui.Component;

import java.util.ArrayList;

public class WndRKChallenges extends Window {

    private static final int WIDTH		= 115;
    private static final int HEIGHT		= 120;
    private static final int BTN_HEIGHT = 16;
    private static final int GAP        = 1;

    private boolean editable;
    private ArrayList<CanScrollCheckBox> boxes;
    private ArrayList<CanScrollInfo> infos;

    public WndRKChallenges( long checked, boolean editable ) {

        super(0, 0, Chrome.get(Chrome.Type.BLANK));

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

        boolean isCustom = false;
        float pos = 0;

        for (int i = 0; i < Challenges.NAME_IDS.length; i++) {

            final String challenge = Challenges.NAME_IDS[i];

            CanScrollCheckBox cb = new CanScrollCheckBox( M.TL(isCustom ? TextChallenges.class : Challenges.class, challenge));
            cb.checked((checked & Challenges.MASKS[i]) != 0);
            cb.active = editable;
			/*
			if(Challenges.NAME_IDS[i].equals("mimic_dungeon")){
				cb.active = false;
				cb.checked(false);
				cb.alpha(0.5f);
			}

			 */

            if (i > 0) {
                pos += GAP;
            }
            cb.setRect(0, pos, WIDTH - 16, BTN_HEIGHT);

            content.add(cb);
            boxes.add(cb);

            boolean finalIsCustom = isCustom;
            CanScrollInfo info = new CanScrollInfo(Icons.get(Icons.INFO)) {
                @Override
                protected void onClick() {
                    super.onClick();
                    ShatteredPixelDungeon.scene().add(
                            new WndMessage(M.L(finalIsCustom ? TextChallenges.class : Challenges.class, challenge + "_desc"))
                    );
                }
            };
            info.setRect(cb.right(), pos, 16, BTN_HEIGHT);
            infos.add(info);
            content.add(info);

            pos = cb.bottom();
        }

        pos += GAP;


        content.setSize(WIDTH, (int) pos + GAP);
        pane.scrollTo(0, 0);
    }

    @Override
    public void onBackPressed() {

        if (editable) {
            int value = 0;
            for (int i=0; i < boxes.size(); i++) {
                if (boxes.get( i ).checked()) {
                    value |= Challenges.MASKS[i];
                }
            }
            SPDSettings.challenges( value );
        }

        super.onBackPressed();
    }

    public static class CanScrollCheckBox extends CheckBox{

        public CanScrollCheckBox(String label) {
            super(label);
        }

        protected boolean onClick(float x, float y){
            if(!inside(x,y)) return false;
            if(active) onClick();

            return true;
        }

        @Override
        protected void layout(){
            super.layout();
            hotArea.width = hotArea.height = 0;
        }
    }

    public static class CanScrollInfo extends IconButton{
        public CanScrollInfo(Image Icon){super(Icon);}

        protected boolean onClick(float x, float y){
            if(!inside(x,y)) return false;
            if(active) onClick();
            return true;
        }

        @Override
        protected void layout(){
            super.layout();
            hotArea.width = hotArea.height = 0;
        }
    }


    public static class CanScrollButton extends RedButton {

        public CanScrollButton(String label) {
            super(label, 7);
        }

        @Override
        protected void layout() {
            super.layout();
            hotArea.height = hotArea.width = 0;
        }

        protected boolean onClick(float x, float y){
            if(!inside(x,y)) return false;
            if(active) onClick();

            return true;
        }
    }
}