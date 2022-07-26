package com.shatteredpixel.shatteredpixeldungeon.scenes;

import com.shatteredpixel.shatteredpixeldungeon.Chrome;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.Archs;
import com.shatteredpixel.shatteredpixeldungeon.ui.ExitButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.ScrollPane;
import com.shatteredpixel.shatteredpixeldungeon.ui.StyledButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.ui.changelist.ChangeInfo;
import com.shatteredpixel.shatteredpixeldungeon.ui.changelist.ChangeSelection;
import com.shatteredpixel.shatteredpixeldungeon.ui.changelist.S_Changes;
import com.shatteredpixel.shatteredpixeldungeon.ui.changelist.mlpd.vM0_5_X_Changes;
import com.shatteredpixel.shatteredpixeldungeon.ui.changelist.mlpd.vM0_6_7_X_Changes;
import com.watabou.noosa.Camera;
import com.watabou.noosa.NinePatch;
import com.watabou.noosa.ui.Component;

import java.util.ArrayList;

public class NewChangesScene extends PixelScene {

    public static int changesSelected = 0;

    public static boolean fromChangesScene = true;

    @Override
    public void create() {
        super.create();

        int w = Camera.main.width;
        int h = Camera.main.height;

        RenderedTextBlock title = PixelScene.renderTextBlock( Messages.get(ChangesScene.class, "title"), 9 );
        title.hardlight(Window.TITLE_COLOR);
        title.setPos(
                (w - title.width()) / 2f,
                (20 - title.height()) / 2f
        );
        align(title);
        add(title);

        ExitButton btnExit = new ExitButton();
        btnExit.setPos( Camera.main.width - btnExit.width(), 0 );
        add( btnExit );

        NinePatch panel = Chrome.get(Chrome.Type.TOAST);

        int pw = 135 + panel.marginLeft() + panel.marginRight() - 2;
        int ph = h - 36;

        panel.size( pw, ph );
        panel.x = (w - pw) / 2f;
        panel.y = title.bottom() + 5;
        align( panel );
        add( panel );

        final ArrayList<ChangeInfo> changeInfos = new ArrayList<>();

        switch (changesSelected){
            case 0: default:
                vM0_6_7_X_Changes.addAllChanges(changeInfos);
                break;
            case 1:
                vM0_5_X_Changes.addAllChanges(changeInfos);
                break;
            case 2:
                S_Changes.addAllChanges(changeInfos);
                break;
        }

        ScrollPane list = new ScrollPane( new Component() ){

            @Override
            public void onClick(float x, float y) {
                for (ChangeInfo info : changeInfos){
                    if (info.onClick( x, y )){
                        return;
                    }
                }
            }

        };
        add( list );

        Component content = list.content();
        content.clear();

        float posY = 0;
        float nextPosY = 0;
        boolean second = false;
        for (ChangeInfo info : changeInfos) {
            if (info.major) {
                posY = nextPosY;
                second = false;
                info.setRect(0, posY, panel.innerWidth(), 0);
                content.add(info);
                posY = nextPosY = info.bottom();
            } else {
                if (!second){
                    second = true;
                    info.setRect(0, posY, panel.innerWidth()/2f, 0);
                    content.add(info);
                    nextPosY = info.bottom();
                } else {
                    second = false;
                    info.setRect(panel.innerWidth()/2f, posY, panel.innerWidth()/2f, 0);
                    content.add(info);
                    nextPosY = Math.max(info.bottom(), nextPosY);
                    posY = nextPosY;
                }
            }
        }
        if (changesSelected == 0) {
            posY = nextPosY;
            ChangeSelection selection = new ChangeSelection(Messages.get(ChangesScene.class, "earlier"), Messages.get(ChangesScene.class, "shpd")) {
                @Override
                public void onClick() {
                    ChangesScene.changesSelected = 0;
                    ShatteredPixelDungeon.switchNoFade(ChangesScene.class);
                }
            };
            selection.icon(Icons.get(Icons.SHPX));
            selection.hardlight(Window.TITLE_COLOR);
            selection.setRect(0, posY, panel.innerWidth(), 0);
            content.add(selection);
            posY = selection.bottom();
        }

        content.setSize( panel.innerWidth(), (int)Math.ceil(posY) );

        list.setRect(
                panel.x + panel.marginLeft(),
                panel.y + panel.marginTop() - 1,
                panel.innerWidth() + 2,
                panel.innerHeight() + 2);
        list.scrollTo(0, fromChangesScene ? posY - list.height() : 0);

        StyledButton btnBeta = new StyledButton(Chrome.Type.TOAST, "0.6.X"){
            @Override
            protected void onClick() {
                super.onClick();
                if (changesSelected != 0) {
                    changesSelected = 0;
                    ShatteredPixelDungeon.seamlessResetScene();
                }
            }
        };

        if (changesSelected != 0) btnBeta.textColor( 0xBBBBBB );
        btnBeta.setRect(list.left()-4f, list.bottom(), 46, changesSelected == 0 ? 19 : 15);
        addToBack(btnBeta);

        StyledButton btn0_8 = new StyledButton(Chrome.Type.TOAST, "0.5.X"){
            @Override
            protected void onClick() {
                super.onClick();
                if (changesSelected != 1) {
                    changesSelected = 1;
                    ShatteredPixelDungeon.seamlessResetScene();
                }
            }
        };
        if (changesSelected != 1) btn0_8.textColor( 0xBBBBBB );
        btn0_8.setRect(btnBeta.right()+1, list.bottom(), 47, changesSelected == 1 ? 19 : 15);
        addToBack(btn0_8);

        StyledButton btn0_7 = new StyledButton(Chrome.Type.TOAST, "0.0-0.4"){
            @Override
            protected void onClick() {
                super.onClick();
                if (changesSelected != 2) {
                    changesSelected = 2;
                    ShatteredPixelDungeon.seamlessResetScene();
                }
            }
        };
        if (changesSelected != 2) btn0_7.textColor( 0xBBBBBB );
        btn0_7.setRect(btn0_8.right() + 1, btn0_8.top(), 46, changesSelected == 2 ? 19 : 15);
        addToBack(btn0_7);

        Archs archs = new Archs();
        archs.setSize( Camera.main.width, Camera.main.height );
        addToBack( archs );

        fadeIn();
    }

    @Override
    protected void onBackPressed() {
        ShatteredPixelDungeon.switchNoFade(TitleScene.class);
    }

}