package com.shatteredpixel.shatteredpixeldungeon.windows;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Conducts;
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
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.ui.Component;

import java.util.ArrayList;
import java.util.Arrays;

public class WndDLC extends Window {

    private final int WIDTH = Math.min(138, (int) (PixelScene.uiCamera.width * 0.9));
    private final int HEIGHT = (int) (PixelScene.uiCamera.height * 0.4);
    private static final int TTL_HEIGHT    = 18;
    private static final int BTN_HEIGHT    = 18;
    private static final int GAP        = 1;

    private boolean editable;
    private ArrayList<IconButton> infos = new ArrayList<>();
    private ArrayList<ConduitBox> boxes = new ArrayList<>();

    public WndDLC(Conducts.ConductStorage conducts, boolean editable ) {

        super();

        this.editable = editable;
        resize(WIDTH, HEIGHT);

        RenderedTextBlock title = PixelScene.renderTextBlock( Messages.get(this, "title"), 12 );
        title.hardlight( TITLE_COLOR );
        title.setPos(
                (WIDTH - title.width()) / 2,
                (TTL_HEIGHT - title.height()) / 2
        );
        PixelScene.align(title);
        add( title );
        ArrayList<Conducts.Conduct> allConducts = editable ?
                new ArrayList<>(Arrays.asList(Conducts.Conduct.values())) :
                conducts.conducts;

        ScrollPane pane = new ScrollPane(new Component()) {
            @Override
            public void onClick(float x, float y) {
                int size = boxes.size();
                if (editable) {
                    for (int i = 0; i < size; i++) {
                        if (boxes.get(i).onClick(x, y)) break;
                    }
                }
                size = infos.size();
                for (int i = 1; i < size+1; i++) {
                    if (infos.get(i-1).inside(x, y)) {
                        int index = allConducts.contains(Conducts.Conduct.NULL) ? i : i-1;

                        String message = allConducts.get(index).desc();
                        String title = Messages.titleCase(Messages.get(Conducts.class, allConducts.get(index).name()));
                        ShatteredPixelDungeon.scene().add(
                                new WndTitledMessage(
                                        new Image(Assets.Interfaces.HAICONS,
                                                (allConducts.get(index).ordinal() - 1) * 16,
                                                16, 16, 16),
                                        title, message)
                        );

                        break;
                    }
                }
            }
        };
        add(pane);
        pane.setRect(0, title.bottom()+2, WIDTH, HEIGHT - title.bottom() - 2);
        Component content = pane.content();

        float pos = 2;
        for (Conducts.Conduct i : allConducts) {

            final String challenge = i.toString();

            ConduitBox cb = new ConduitBox( i == Conducts.Conduct.NULL ? challenge : "       " + challenge);
            cb.checked(conducts.isConducted(i));
            if (i == Conducts.Conduct.NULL && !conducts.isConductedAtAll()) cb.checked(true);
            cb.active = editable;
            cb.conduct = i;

            pos += GAP;
            cb.setRect(0, pos, WIDTH - 16, BTN_HEIGHT);
            if (i == Conducts.Conduct.NULL){
                cb.setSize(WIDTH, BTN_HEIGHT);
            }

            content.add(cb);
            boxes.add(cb);
            if (i != Conducts.Conduct.NULL) {
                IconButton info = new IconButton(Icons.get(Icons.INFO)) {
                    @Override
                    protected void layout() {
                        super.layout();
                        hotArea.y = -5000;
                    }
                };
                info.setRect(cb.right(), pos, 16, BTN_HEIGHT);
                content.add(info);
                infos.add(info);
                Image icon = new Image(Assets.Interfaces.HAICONS, (i.ordinal() - 1) * 16, 16, 16, 16);
                icon.x = cb.left()+1;
                icon.y = cb.top()+1;
                content.add(icon);
            }

            pos = cb.bottom();
        }

        content.setSize(WIDTH, pos);
    }

    @Override
    public void onBackPressed() {

        if (editable) {
            Conducts.ConductStorage value = new Conducts.ConductStorage();
            for (ConduitBox slot : boxes) {
                if (slot.checked() && slot.conduct != Conducts.Conduct.NULL) {
                    value.conducts.add(slot.conduct);
                }
            }
            SPDSettings.dlc( value );
        }

        super.onBackPressed();
    }

    public class ConduitBox extends CheckBox{

        public Conducts.Conduct conduct;

        public ConduitBox(String label) {
            super(label);
        }

        @Override
        protected void onClick() {
            super.onClick();
            if (active){
                boolean disableEverything = this.conduct == Conducts.Conduct.BOSSRUSH || SPDSettings.oneConduct();
                for (CheckBox slot : boxes){
                    if (slot != this && disableEverything) slot.checked(false);
                }
            }
        }

        protected boolean onClick(float x, float y) {
            if (!inside(x, y)) return false;
            Sample.INSTANCE.play(Assets.Sounds.CLICK);
            onClick();
            return true;
        }

        @Override
        protected void layout() {
            super.layout();
            hotArea.width = hotArea.height = 0;
            if (!editable) icon.alpha(0f);
        }
    }
}