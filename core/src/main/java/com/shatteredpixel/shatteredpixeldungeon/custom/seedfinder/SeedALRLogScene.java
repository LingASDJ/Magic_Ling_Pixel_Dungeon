package com.shatteredpixel.shatteredpixeldungeon.custom.seedfinder;

import com.badlogic.gdx.Gdx;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.SeedFinderScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.TitleScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.Archs;
import com.shatteredpixel.shatteredpixeldungeon.ui.ExitButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.ScrollPane;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.utils.DungeonSeed;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndTextInput;
import com.watabou.noosa.Camera;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.ui.Component;

public class SeedALRLogScene extends PixelScene {

    public ScrollPane list;
    public String s;
    public static CreditsBlock txt;
    public static RenderedTextBlock r;
    public boolean stop;
    public static Thread thread;

    public WndTextInput wndTextInput;

    @Override
    public void create() {
        super.create();

        final float colWidth = 120;
        final float fullWidth = colWidth * (landscape() ? 2 : 1);

        int w = Camera.main.width;
        int h = Camera.main.height;

        s = null;

        Archs archs = new Archs();
        archs.setSize(w, h);
        add(archs);

        //darkens the arches
        add(new ColorBlock(w, h, 0x88000000));

        list = new ScrollPane(new Component());
        add(list);

        Component content = list.content();
        content.clear();

        ShatteredPixelDungeon.scene().addToFront( wndTextInput = new WndTextInput(Messages.get(this, "title"), Messages.get(this, "body"), Messages.get(this, "initial_value", SPDSettings.seedfinderFloors()), 1000, true, Messages.get(this, "clear"),null) {
            @Override
            public void onSelect(boolean positive, String text) {
                if (positive) {

                    text = DungeonSeed.formatText(text);
                    long seed = DungeonSeed.convertFromText(text);

                    r = PixelScene.renderTextBlock("Loading....",9);
                    r.maxWidth(w - 40);
                    r.setPos(width/2f,20);
                    ShatteredPixelDungeon.scene().addToFront(r);

                    thread = new Thread(() -> {
                        s = new SeedFinder().logSeedItems(Long.toString(seed),SPDSettings.seedfinderFloors(),SPDSettings.challenges());
                        Gdx.app.postRunnable(() -> {
                            r.destroy();

                            txt = new SeedALRLogScene.CreditsBlock(true, Window.TITLE_COLOR,s);
                            txt.setRect((Camera.main.width - colWidth)/2f, 12, colWidth, 0);

                            if (!thread.isInterrupted()) {
                                content.add(txt);
                                content.setSize( fullWidth, txt.bottom()+10 );
                            }

                            if (list.isActive()) {
                                list.setRect( 0, 0, w, h );
                                list.scrollTo(0, 0);
                            }

                        });
                    });
                    thread.start();

                } else {
                    SPDSettings.customSeed("");
                    ShatteredPixelDungeon.switchNoFade(TitleScene.class);
                }
            }
        });

        ExitButton btnExit = new ExitButton() {
            @Override
            protected void onClick() {
                if (thread!= null && thread.isAlive())thread.interrupt();
                ShatteredPixelDungeon.switchNoFade(SeedFinderScene.class);
                System.gc();
            }
        };
        btnExit.setPos(Camera.main.width - btnExit.width(), 0);
        add(btnExit);

        fadeIn();
    }
    @Override
    protected void onBackPressed() {
        ShatteredPixelDungeon.switchScene(SeedFinderScene.class);
    }

    public static class CreditsBlock extends Component {

        boolean large;

        public RenderedTextBlock body;

        public CreditsBlock(boolean large, int highlight, String body) {
            super();

            this.large = large;

            this.body = PixelScene.renderTextBlock(body, 6);
            if (highlight != -1)
                this.body.setHightlighting(true, highlight);
            if (large)
                this.body.align(RenderedTextBlock.CENTER_ALIGN);
            add(this.body);
        }

        @Override
        protected void layout() {
            super.layout();

            float topY = top();

            if (large){
                body.maxWidth((int)width());
                body.setPos( x + (width() - body.width())/2f, topY);
            } else {
                topY += 1;
                body.maxWidth((int)width());
                body.setPos( x, topY);
            }

            topY += body.height();

            height = Math.max(height, topY - top());
        }

    }
}
