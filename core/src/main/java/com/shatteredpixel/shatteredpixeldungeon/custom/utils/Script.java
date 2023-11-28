package com.shatteredpixel.shatteredpixeldungeon.custom.utils;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.watabou.noosa.Image;
import com.watabou.noosa.tweeners.AlphaTweener;
import com.watabou.utils.SparseArray;

public class Script {
    //Well if we wants to make plot related(mostly dialogue)clear and able to store enough date(if we still have an chronicle-like campaign?)
    //Whatever,this at least help save some data needed
    //More information can be found at WndDialog.java
    //By Teller 2021/8/20

    public enum Character{
        NOBODY,REN,SMALLB,RED,EXUSIAI,FROSTNOVA
    }

    public enum FormalPlot
    {
        NOBODY,SEWER,PRISON,CAVES,CITY,HALLS
    }


    public static final int ID_SEWERS		= 0;
    public static final int ID_PRISON		= 1;
    public static final int ID_CAVES		= 2;
    public static final int ID_CITY     	= 3;
    public static final int ID_HALLS		= 4;

    public static final int ID_FROSTNOVA_INTERACT = 5;

    private static final SparseArray<String> CHAPTERS = new SparseArray<String>();

    static {
        CHAPTERS.put( ID_SEWERS, "sewers" );
        CHAPTERS.put( ID_PRISON, "prison" );
        CHAPTERS.put( ID_CAVES, "caves" );
        CHAPTERS.put( ID_CITY, "city" );
        CHAPTERS.put( ID_HALLS, "halls" );
    };

    public static boolean checkChapter(int chapter) {
        if(Dungeon.chapters.contains(chapter)) {
            return false;
        }
        else {
            Dungeon.chapters.add(chapter);
            return true;
        }
    }

    public static String Name(Character character)
    {
        String str = "";
        switch (character) {
            default:
            case NOBODY:
                str = "Test";
                break;
            case REN:
                str = Messages.get(Script.class,"name_ren");
                break;
            case SMALLB:
                str = Messages.get(Script.class,"name_smallb");
                break;
            case RED:
                str = Messages.get(Script.class,"name_red");
                break;
            case EXUSIAI:
                str = Messages.get(Script.class,"name_exusiat");
                break;
            case FROSTNOVA:
                str = Messages.get(Script.class,"name_frostnova");
                break;
        }
        //GLog.i(str);
        return str;
    }

    public static Image Portrait(Character character)
    {
        int row = -1;
        switch (character) {
            default:
            case NOBODY:
                row = 0;
                break;
            case REN:
                row = 0;
                break;
            case SMALLB:
                row = 1;
                break;
            case RED:
                row = 2;
                break;
            case EXUSIAI:
                row = 3;
                break;
            //case FROSTNOVA:
            //    row = 4;
            //    break;
        }
        Image image = new Image("splashes/portraits.png", 0, 48* row,48, 48);

        switch (character)
        {
            case FROSTNOVA:
                image = new Image(Assets.Sprites.ASDW, 0, 48* row, 36, 48);

                AlphaTweener invisible = new AlphaTweener( image, 0.4f, 0.4f );
                if (image.parent != null){
                    image.parent.add(invisible);
                } else
                    image.alpha( 0.4f );

                image.alpha(0.4f);
                break;
            default:break;
        }

        return image;
    }

    public static Plot ReturnPlot(FormalPlot plot)
    {
        switch (plot)
        {
            default:
            case SEWER: return new RenPlot();
            case PRISON:
            case CAVES:
            case CITY:
            case HALLS: return null;
        }
    }
}
