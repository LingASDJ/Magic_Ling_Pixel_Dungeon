package com.shatteredpixel.shatteredpixeldungeon.windows;

import com.shatteredpixel.shatteredpixeldungeon.Conducts;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.GamesInProgress;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.HeroSelectScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.InterlevelScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.ActionIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.CheckBox;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.OptionSlider;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.utils.DungeonSeed;
import com.watabou.noosa.Game;

import java.io.IOException;
import java.util.ArrayList;

public class WndRestart extends Window {
    private static final int WIDTH = 120;
    private static final int HEIGHT = 120;
    private static final int BOX_HEIGHT = 16;
    private static final int TTL_HEIGHT = 12;
    private static final int GAP= 2;

    private final ArrayList<CheckBox> cbs;
    OptionSlider level3;
    public WndRestart(){
        resize(WIDTH, HEIGHT);

        RenderedTextBlock rtb = PixelScene.renderTextBlock(Messages.get(this, "title"), TTL_HEIGHT - GAP);
        rtb.setPos(WIDTH/2f - rtb.width()/2, GAP);
        PixelScene.align(rtb);
        rtb.hardlight(0xFFFF00);
        add(rtb);
        float pos = TTL_HEIGHT + GAP;
        cbs = new ArrayList<>();

        for(int i = 0; i<5; ++i){
            int finalI = i;
            CheckBox cb = new CheckBox(Messages.get(this, "option_"+ (finalI + 1))){
                public void checked( boolean value ) {
                    if (checked != value) {
                        checked = value;
                        icon.copy( Icons.get( checked ? Icons.CHECKED : Icons.UNCHECKED ) );
                    }
                }
            };
            cb.setRect(GAP, pos, WIDTH - GAP * 2, BOX_HEIGHT);


            add(cb);
            cbs.add(cb);
            pos += BOX_HEIGHT + GAP;
        }
        boolean shouldRestart = ( Dungeon.hero == null || !Dungeon.hero.isAlive() );
        if(shouldRestart){
            cbs.get(4).active = false;
            cbs.get(4).checked = false;
            cbs.get(4).visible = false;
        }

        RedButton confirm =new RedButton(Messages.get(this, "confirm")){
            @Override
            protected void onClick() {
                super.onClick();

                if( !cbs.get(4).checked() && GamesInProgress.checkAll().size() >= GamesInProgress.MAX_SLOTS ){
                    add( new WndError( Messages.get(WndRestart.class,"error") ) {
                        public void onBackPressed() {
                            super.onBackPressed();
                        }
                    } );
                    return;
                }

                HeroClass nextHero;
                Conducts.ConductStorage nextDifficulty;
                int nextChallenges;
                String nextSeed;
                String saveCustomSeed;
                long saveSeed;
                boolean heroAlive;
                GamesInProgress.Info info;

                try {
                    Dungeon.saveAll();
                } catch (IOException e) {
                    ShatteredPixelDungeon.reportException(e);
                }

                heroAlive = Dungeon.hero.isAlive();
                info = GamesInProgress.check( GamesInProgress.curSlot );
                saveCustomSeed = heroAlive ? info.customSeed : SPDSettings.customSeed();
                saveSeed = heroAlive ? info.seed : Dungeon.seed;
                nextSeed = cbs.get(0).checked() ? DungeonSeed.convertToCode( saveCustomSeed.isEmpty() ? saveSeed : DungeonSeed.convertFromText( saveCustomSeed ) ) : "";
                nextHero = cbs.get(1).checked() ? ( heroAlive ? info.heroClass : Dungeon.hero.heroClass ) : Dungeon.hero.heroClass;
                nextChallenges = cbs.get(2).checked() ? ( heroAlive ? info.challenges : SPDSettings.challenges() ) : 0;
                if (cbs.get(3).checked()) {
                    nextDifficulty = ( heroAlive ? info.dlcs : SPDSettings.dlc() );
                } else {
                    nextDifficulty = new Conducts.ConductStorage();
                    nextDifficulty.conducts.add(Conducts.Conduct.NULL);
                }

                SPDSettings.customSeed( nextSeed );
                GamesInProgress.selectedClass = nextHero;
                SPDSettings.challenges( nextChallenges );
                SPDSettings.dlc( nextDifficulty );

                try {
                    if( cbs.get(4).checked() && !shouldRestart )
                        Dungeon.deleteGame( GamesInProgress.curSlot,true );
                    GamesInProgress.curSlot = GamesInProgress.firstEmpty();
                } catch (Exception e) {
                    ShatteredPixelDungeon.reportException(e);
                }

                Dungeon.hero = null;
                ActionIndicator.action = null;
                InterlevelScene.mode = InterlevelScene.Mode.DESCEND;
                Game.switchScene( cbs.get(1).checked() ? InterlevelScene.class : HeroSelectScene.class );
            }
        };
        add(confirm);
        confirm.setRect(GAP, pos, WIDTH / 2f -2 * GAP, BOX_HEIGHT);

        RedButton cancel =new RedButton(Messages.get(this, "cancel")){
            @Override
            protected void onClick() {
                hide();
            }
        };
        add(cancel);
        cancel.setRect(confirm.right()+GAP*2, pos, confirm.width(), BOX_HEIGHT);
        resize(WIDTH, HEIGHT);
    }
}
