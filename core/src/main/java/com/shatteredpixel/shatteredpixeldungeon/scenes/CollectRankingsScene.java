package com.shatteredpixel.shatteredpixeldungeon.scenes;

import com.shatteredpixel.shatteredpixeldungeon.Rankings;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.custom.CollectRankings;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.Archs;
import com.shatteredpixel.shatteredpixeldungeon.ui.ExitButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.ScrollPane;
import com.shatteredpixel.shatteredpixeldungeon.windows.IconTitle;
import com.watabou.noosa.Camera;
import com.watabou.noosa.ui.Component;
import com.watabou.utils.GameMath;

public class CollectRankingsScene extends PixelScene{
    private static final float ROW_HEIGHT_MAX	= 20;
    private static final float ROW_HEIGHT_MIN	= 12;

    private static final float MAX_ROW_WIDTH    = 160;

    private static final float GAP	= 4;

    private Archs archs;

    public ScrollPane list;
    public static Component content;

    @Override
    public void create() {

        super.create();

        final float colWidth = 120;
        final float fullWidth = colWidth * (landscape() ? 2 : 1);

        PixelScene.uiCamera.visible = false;

        int w = Camera.main.width;
        int h = Camera.main.height;

        archs = new Archs();
        archs.setSize( w, h );
        add( archs );

        list = new ScrollPane(new Component());
        add(list);

        content = list.content();
        content.clear();

        CollectRankings.INSTANCE.load();

        IconTitle title = new IconTitle( Icons.RANKINGS.get(), Messages.get(this, "title"));
        title.setSize(200, 0);
        title.setPos(
                (w - title.reqWidth()) / 2f,
                (20 - title.height()) / 2f
        );
        align(title);
        add(title);

        if (CollectRankings.INSTANCE.records.size() > 0) {

            //attempts to give each record as much space as possible, ideally as much space as portrait mode
            float rowHeight = GameMath.gate(ROW_HEIGHT_MIN, (PixelScene.uiCamera.height - 26)/CollectRankings.INSTANCE.records.size(), ROW_HEIGHT_MAX);

            float left = (w - Math.min( MAX_ROW_WIDTH, w )) / 2 + GAP;
            float top = (h - rowHeight  * CollectRankings.INSTANCE.records.size()) / 2;

            int pos = 0;

            for (Rankings.Record rec : CollectRankings.INSTANCE.records.values()) {
                RankingsScene.Record row = new RankingsScene.Record( pos, false, rec );
                float offset = 0;
                if (rowHeight <= 14){
                    offset = (pos % 2 == 1) ? 5 : -5;
                }
                row.setRect( left+offset, top + pos * rowHeight, w - left * 2, rowHeight );
                content.add(row);
                content.setSize( fullWidth, row.bottom()+10 );
                pos++;
            }

        } else {

            RenderedTextBlock noRec = PixelScene.renderTextBlock(Messages.get(this, "no_games"), 8);
            noRec.hardlight( 0xCCCCCC );
            noRec.setPos(
                    (w - noRec.width()) / 2,
                    (h - noRec.height()) / 2
            );
            align(noRec);
            add(noRec);

        }

        list.setRect( 0, 0, w, h );
        list.scrollTo(0, 0);

        ExitButton btnExit = new ExitButton();
        btnExit.setPos( Camera.main.width - btnExit.width(), 0 );
        add( btnExit );

        fadeIn();
    }

    @Override
    protected void onBackPressed() {
        ShatteredPixelDungeon.switchNoFade(TitleScene.class);
    }
}
