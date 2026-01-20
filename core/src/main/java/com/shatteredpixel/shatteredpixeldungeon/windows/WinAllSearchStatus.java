package com.shatteredpixel.shatteredpixeldungeon.windows;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Chrome;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LostInventory;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.status.ActivePoint;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.status.ScoreBuff;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TimekeepersHourglass;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.Key;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.hollow.AllSearchIQuest;
import com.shatteredpixel.shatteredpixeldungeon.journal.Notes;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.levels.hollow.AllSearchHollowActorLevel;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Swiftthistle;
import com.shatteredpixel.shatteredpixeldungeon.scenes.InterlevelScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.StyledButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;

import java.util.ArrayList;
import java.util.List;

public class WinAllSearchStatus extends Window {
    private static final int WIDTH = 120;
    private static final int HEIGHT = 100;
    private static final int BOX_HEIGHT = 20;
    private static final int TTL_HEIGHT = 12;
    private static final int GAP= 2;

    Image escfail;

    RenderedTextBlock totalsocre;
    StyledButton searchButton;
    public static void removeKeysInArea(int targetDepth) {
        List<Notes.KeyRecord> keyRecords = new ArrayList<>(Notes.getRecords(Notes.KeyRecord.class));
        for (Notes.KeyRecord record : keyRecords) {
            if (record.depth() == targetDepth) {
                Key key = record.key;
                Notes.remove(key);
            }
        }
    }


    public WinAllSearchStatus(){

        int totalPriceItem = 0;
        int lowItem = 0;
        int middleItem = 0;
        int highItem = 0;

        resize(WIDTH, HEIGHT);

        RenderedTextBlock rtb = PixelScene.renderTextBlock(Rules(), TTL_HEIGHT - GAP);
        rtb.setPos(WIDTH/2f - rtb.width()/2, GAP);
        PixelScene.align(rtb);
        rtb.hardlight(0xFFFF00);
        add(rtb);

        escfail = ImageRules();
        escfail.setPos(WIDTH/10f, 5);
        escfail.scale.set(0.2f);
        add(escfail);
        PixelScene.align(escfail);

        ArrayList<AllSearchIQuest> asi = hero.belongings.getAllItems(AllSearchIQuest.class);
        for (AllSearchIQuest w : asi.toArray(new AllSearchIQuest[0])){
            totalPriceItem++;
           switch (w.RXlevel){
               case 1:
                   lowItem++;
                   break;
               case 2:
                   middleItem++;
                   break;
               case 3:
                   highItem++;
                   break;
           }
        }

        ScoreBuff buff = hero.buff(ScoreBuff.class);
        if (hero.buff(ScoreBuff.class) != null) {
            totalsocre = PixelScene.renderTextBlock(Messages.get(WinAllSearchStatus.class, "score",buff.score,totalPriceItem,lowItem,middleItem,highItem), 6);
            totalsocre.setPos(WIDTH/2f - totalsocre.width()/2, 50);
            PixelScene.align(totalsocre);
            totalsocre.hardlight(0xFFFF00);
            add(totalsocre);
        }

        final Chrome.Type GREY_TR = Chrome.Type.GREY_BUTTON_TR;
        searchButton = new StyledButton(GREY_TR, Messages.get(this, "quest")){
            @Override
            protected void onClick() {
                hide();
                int levelc = Math.min(Statistics.getAlLSearchScore / (20000 / 6),6);
                if (levelc > 0) {
                    Statistics.miniGamesTotalLevel += levelc;
                }
                if(buff.score >= 20000 * 0.75f){
                    Badges.MINIGAME_MASTER_THREE();
                }
                InterlevelScene.mode = InterlevelScene.Mode.DESCEND;
                TimekeepersHourglass.timeFreeze timeFreeze = Dungeon.hero.buff(TimekeepersHourglass.timeFreeze.class);
                if (timeFreeze != null) timeFreeze.disarmPresses();
                Swiftthistle.TimeBubble timeBubble = Dungeon.hero.buff(Swiftthistle.TimeBubble.class);
                if (timeBubble != null) timeBubble.disarmPresses();
                InterlevelScene.curTransition = new LevelTransition();
                InterlevelScene.curTransition.destDepth = 32;
                InterlevelScene.curTransition.destType = LevelTransition.Type.REGULAR_ENTRANCE;
                InterlevelScene.curTransition.destBranch = 0;
                InterlevelScene.curTransition.type = LevelTransition.Type.REGULAR_EXIT;
                InterlevelScene.curTransition.centerCell  = -1;
                Game.switchScene( InterlevelScene.class );
                Buff.detach( hero, LostInventory.class);
                Buff.detach( hero, ScoreBuff.class);
                Buff.detach(hero, ActivePoint.class);
                Buff.detach(hero, AllSearchHollowActorLevel.RecordTimeDead.class);

                removeKeysInArea(Dungeon.depth);
            }
        };
        searchButton.icon(new Image(Icons.get(Icons.CATALOG)));
        searchButton.setRect(0, totalsocre.bottom()+10, WIDTH, BOX_HEIGHT);
        add(searchButton);


        resize(WIDTH, (int) (searchButton.bottom()+GAP));
    }

    private Image ImageRules() {
        Image image;
        if(Statistics.AllSearchSuccessEsc){
            image = new Image(Assets.Sprites.ESCSSFL);
        } else if(Statistics.AllSearchFailedEsc){
            image = new Image(Assets.Sprites.ESCSOUT);
        } else {
            image = new Image(Assets.Sprites.ESCFAIL);
        }
        return image;
    }

    private String Rules() {
        String string;
        if(Statistics.AllSearchSuccessEsc){
            string = Messages.get(WinAllSearchStatus.class, "success");
        } else if(Statistics.AllSearchFailedEsc){
            string = Messages.get(WinAllSearchStatus.class, "timeout");
        } else {
            string = Messages.get(WinAllSearchStatus.class, "fail");
        }
        return string;
    }

    @Override
    public void onBackPressed() {
    }

}

