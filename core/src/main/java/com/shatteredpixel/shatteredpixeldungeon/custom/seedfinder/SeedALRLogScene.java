package com.shatteredpixel.shatteredpixeldungeon.custom.seedfinder;

import com.badlogic.gdx.Gdx;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.SeedFinderScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.Archs;
import com.shatteredpixel.shatteredpixeldungeon.ui.ExitButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.ScrollPane;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.utils.DungeonSeed;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndTextInput;
import com.watabou.noosa.Camera;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.ui.Component;

import java.util.ArrayList;
import java.util.Objects;

public class SeedALRLogScene extends PixelScene {

    public ScrollPane list;
    public String s;
    public CreditsBlock txt;
    public RenderedTextBlock r;
    public boolean stop;
    public Thread thread;
    public Component contentx;
    public WndTextInput wndTextInput;
    private RedButton btnCopy;
    private String lastSeedCode = "";

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

        contentx = list.content();
        contentx.clear();
        lastSeedCode = "";
        addCopySeedButton();

        ShatteredPixelDungeon.scene().addToFront( wndTextInput = new WndTextInput(Messages.get(this, "title"), Messages.get(this, "body"), Messages.get(this, "initial_value", SPDSettings.seedfinderFloors()), 1000, true, Messages.get(this, "clear"),null) {
            @Override
            public void onSelect(boolean positive, String text) {
                if (positive && !Objects.equals(text, "")) {

                    text = DungeonSeed.formatText(text);
                    long seed = DungeonSeed.convertFromText(text);
                    if (seed >= 0) {
                        lastSeedCode = DungeonSeed.convertToCode(seed);
                    } else {
                        lastSeedCode = "";
                    }

                    contentx = list.content();
                    contentx.clear();

                    r = PixelScene.renderTextBlock("Loading....",9);
                    r.maxWidth(w - 40);
                    r.setPos(width/2f,20);
                    ShatteredPixelDungeon.scene().addToFront(r);

                    thread = new Thread(() -> {
                        s = new SeedFinder(new ArrayList<>(), SPDSettings.seedfinderFloors(), HeroClass.WARRIOR)
                                .logSeedItems(seed);
                        Gdx.app.postRunnable(() -> {
                            if (stop) return;
                            r.destroy();

                            txt = new SeedALRLogScene.CreditsBlock(true, Window.TITLE_COLOR,s);
                            txt.setRect((Camera.main.width - colWidth)/2f, 12, colWidth, 0);

                            if (!thread.isInterrupted()) {
                                contentx.add(txt);
                                contentx.setSize( fullWidth, txt.bottom()+10 );
                            }

                            if (list.isActive()) {
                                list.setRect( 0, 0, w, h );
                                list.scrollTo(0, 0);
                            }
                            if (btnCopy != null) bringToFront(btnCopy);

                        });
                    });
                    thread.start();
                } else {
                    lastSeedCode = "";
                    ShatteredPixelDungeon.switchNoFade(SeedFinderScene.class);
                }
            }
        });

        ExitButton btnExit = new ExitButton() {
            @Override
            protected void onClick() {
                stop = true;
                if (thread!= null && thread.isAlive())thread.interrupt();
                Dungeon.overrideSeed = -1;
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
        stop = true;
        if (thread != null && thread.isAlive()) thread.interrupt();
        Dungeon.overrideSeed = -1;
        ShatteredPixelDungeon.switchScene(SeedFinderScene.class);
    }

    @Override
    public void destroy() {
        stop = true;
        if (thread != null && thread.isAlive()) thread.interrupt();
        Dungeon.overrideSeed = -1;
        super.destroy();
    }

    /** 复制当前查询到的种子码到自定义种子栏（需玩家显式确认） */
    private void addCopySeedButton() {
        btnCopy = new RedButton(Messages.get(SeedFindScene.class, "copy_yes"), 8) {
            @Override
            protected void onClick() {
                if (lastSeedCode == null || lastSeedCode.isEmpty()) return;

                addToFront(new WndOptions(Icons.get(Icons.RENAME_ON),
                        Messages.get(SeedFindScene.class, "copy_title"),
                        Messages.get(SeedFindScene.class, "copy_body"),
                        Messages.get(SeedFindScene.class, "copy_yes"),
                        Messages.get(SeedFindScene.class, "copy_no")) {
                    @Override
                    protected void onSelect(int index) {
                        if (index == 0) {
                            SPDSettings.customSeed(lastSeedCode);
                        }
                    }
                });
            }
        };
        addToFront(btnCopy);
        btnCopy.setRect(0, 0, 44, 18);
        btnCopy.enable(true);
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
