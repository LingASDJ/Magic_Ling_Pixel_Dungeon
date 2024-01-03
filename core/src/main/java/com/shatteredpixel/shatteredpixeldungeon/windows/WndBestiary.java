package com.shatteredpixel.shatteredpixeldungeon.windows;

import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Albino;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.BruteBot;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.CausticSlime;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.ClearElemental;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.ColdMagicRat;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Crab;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.FetidRat;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Fire_Scorpio;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.GnollTrickster;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Golem;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Goo;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.GreatCrab;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.IceGolem;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Ice_Scorpio;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.MoloHR;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.NewDM720;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Rat;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.RedSwarm;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Salamander;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.ShieldHuntsman;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Slime_Orange;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Slime_Red;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Swarm;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Wraith;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.Cerberus;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.CrivusFruits;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.CrivusFruitsLasher;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.SakaFishBoss;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.hollow.ApprenticeWitch;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.hollow.Frankenstein;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Ghost;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.RatKing;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.ScrollPane;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.ui.Component;

import java.util.ArrayList;

public class WndBestiary extends Window {

    private static final int WIDTH		= 132;
    private static final int HEIGHT_P	= 163;
    private static final int TTL_HEIGHT = 12;
    private static final int GAP= 2;
    private static final int ITEM_HEIGHT	= 25;

    private static final String TXT_TITLE	= "Bestiary";


    private ScrollPane list;
    private ArrayList<ListItem> items = new ArrayList<WndBestiary.ListItem>();
    public WndBestiary() {
        super();
        updateList();
    }


    private void updateList(){
        Mob.Mobs.clear(); // 清空已有的实例，确保不重复添加

        try {
            //怪物图鉴仅对会在游戏中出现的怪物做显示
            Class<?>[] mobClasses = {
                    Rat.class, Albino.class, Crab.class,
                    GreatCrab.class, GnollTrickster.class, FetidRat.class,
                    Salamander.class, ColdMagicRat.class, IceGolem.class, Golem.class,Swarm.class, RedSwarm.class,
                    ClearElemental.class,
                    Slime_Red.class, Slime_Orange.class,
                    CrivusFruitsLasher.class, CrivusFruits.class,
                    RatKing.class, Ghost.class, Wraith.class,
                    CausticSlime.class, Goo.class,
                    ApprenticeWitch.class, Cerberus.class,
                    Frankenstein.class,
                    Fire_Scorpio.class, ShieldHuntsman.class, Ice_Scorpio.class,
                    BruteBot.class, SakaFishBoss.class, NewDM720.class, MoloHR.class

            };

            for (Class<?> mobClass : mobClasses) {
                Mob mobInstance = (Mob) mobClass.getDeclaredConstructor().newInstance();
                Mob.Mobs.add(mobInstance);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        resize( WIDTH, HEIGHT_P );

        RenderedTextBlock txtTitle = PixelScene.renderTextBlock(Messages.get(this, "title"), TTL_HEIGHT - GAP);
        txtTitle.setPos(WIDTH/2f - txtTitle.width()/2, GAP);
        PixelScene.align(txtTitle);
        txtTitle.hardlight(0xFFFF00);
        add(txtTitle);

        list = new ScrollPane( new Component() ){
            @Override
            public void onClick( float x, float y ) {
                int size = items.size();
                for (int i=0; i < size; i++) {
                    if (items.get( i ).onClick( x, y )) {
                        break;
                    }
                }
            }
        };
        add( list );

        list.setRect( 0, TTL_HEIGHT + GAP, WIDTH, height - txtTitle.height() );
        Component content = list.content();
        float pos = 0;
        items.clear();
        content.clear();
        for(Mob mob : Mob.Mobs){

            ListItem item = new ListItem( mob );
            item.setRect( 0, pos, WIDTH, ITEM_HEIGHT );
            content.add( item );
            items.add( item );
            pos += item.height();
        }

        content.setSize( WIDTH, pos );


    }
    private static class ListItem extends Component {

        private RenderedTextBlock feature;
        private BitmapText depth;
        private Mob mob1;

        private CharSprite icon;

        public ListItem( Mob mob ) {
            super();
            mob1 = mob;
            String temptext = mob.name();

            icon = mob.sprite();
            if(!mob.discovered){
                icon.hardlight(0x111111);
                temptext = "暂无资料";
            }
            if(icon.width()>21){
                icon.scale.set(PixelScene.align(0.7f));
            }

            feature.text( temptext );
            if(!mob.discovered) {
                feature.hardlight(Window.CBLACK);
            }
            add( icon );
        }

        @Override
        protected void createChildren() {
            feature = PixelScene.renderTextBlock(7);
            add( feature );

            depth = new BitmapText( PixelScene.pixelFont );
            add( depth );



        }

        @Override
        protected void layout() {

            icon.x = (width - icon.width)/1.05f;

            depth.x = icon.x - 1 - depth.width();
            depth.y = PixelScene.align( y + (height - depth.height()) / 2 );

            icon.y = depth.y - 6;

            feature.setPos(feature.height(),depth.y);
        }
        public boolean onClick( float x, float y ) {
            if (inside( x, y )) {
                if(!mob1.discovered){
                    GameScene.show( new WndMessage("你必须击败敌人才能获得对应情报。"));
                } else {
                    GameScene.show( new WndInfoMob( mob1 ) );
                }

                return true;
            } else {
                return false;
            }
        }
    }
}
