/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2021 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.shatteredpixel.shatteredpixeldungeon.windows;

import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.custom.messages.M;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.CustomGameSettings;
import com.shatteredpixel.shatteredpixeldungeon.custom.visuals.TextField;
import com.shatteredpixel.shatteredpixeldungeon.ui.CheckBox;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.ScrollPane;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.utils.DungeonSeed;
import com.watabou.noosa.ui.Component;

import java.util.ArrayList;

public class WndSeed extends Window {

    private static final int WIDTH		= 120;
    private static final int HEIGHT		= 26;
    private static final int GAP        = 1;

    private final boolean editable;
    private final ArrayList<CanScrollCheckBox> boxes;
    private final CanScrollTextField cstf;
    private final CanScrollButton deleteSeedInput;

    public WndSeed(boolean editable ) {

        super();

        resize(WIDTH, HEIGHT);

        this.editable = editable;

        ScrollPane pane = new ScrollPane(new Component()) {
            @Override
            public void onClick(float x, float y) {
                if(cstf.onClick(x, y)){
                    return;
                }
                deleteSeedInput.onClick(x, y);
            }
        };
        add(pane);
        pane.setRect(0, GAP, WIDTH, HEIGHT - 2 * GAP);
        Component content = pane.content();

        boxes = new ArrayList<>();

        float pos = 0;

        pos += GAP;

        cstf = new CanScrollTextField(M.L(TextChallenges.class, "seed_custom_title"));
        cstf.setHint(M.L(TextChallenges.class, "hint"));
        content.add(cstf);
        cstf.enable(editable);
        cstf.setLarge(false);
        cstf.setMaxStringLength(22);
        cstf.setRect(0, pos, WIDTH-16-GAP, 22);
        cstf.text(editable ? CustomGameSettings.getSeedString() : DungeonSeed.convertToCode(Dungeon.seed));

        deleteSeedInput = new CanScrollButton(M.L(TextChallenges.class, "delete_seed_input")){
            @Override
            protected void onClick() {
                super.onClick();
                cstf.text("");
                cstf.onTextChange();
            }
        };
        content.add(deleteSeedInput);
        deleteSeedInput.enable(editable);
        deleteSeedInput.setRect(cstf.right() + GAP, pos, 16, 22);

        pos = cstf.bottom();

        content.setSize(WIDTH, (int) pos + GAP*2);
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

    public static class CanScrollTextField extends TextField{

        public CanScrollTextField(String label) {
            super(label);
        }

        @Override
        public void onTextChange() {
            CustomGameSettings.putSeedString(text());
        }

        @Override
        public void onTextCancel() {

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

    public static class CanScrollButton extends RedButton{

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