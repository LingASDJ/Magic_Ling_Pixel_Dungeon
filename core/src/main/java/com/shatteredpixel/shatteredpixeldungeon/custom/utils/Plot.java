package com.shatteredpixel.shatteredpixeldungeon.custom.utils;

import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialog;
import com.watabou.utils.Bundle;

import java.util.HashMap;

public abstract class Plot{

    public int process;

    public int needed_process = 0;

    {
        needed_process = 0;
    }

    public WndDialog diagulewindow;

    private static final String PLOT_INPUTS   = "plot_inputs";
    private static final String PLOT_PROCESS  = "plot_process";

    private static final String PLOT_SEWER = "plot_sewer";
    private static final String PLOT_PRISON = "plot_prison";
    private static final String PLOT_CAVE= "plot_cave";
    private static final String PLOT_CITY = "plot_city";
    private static final String PLOT_HALL = "plot_hall";

    private static final String PLOT_FROSTNOVA_QUEST = "plot_frostnovaquest";

    protected static final String PLOT_NAME = "void_name";
    protected static final String SEWER_NAME = "sewer_name";
    protected static final String PRISON_NAME = "prison_name";
    protected static final String CAVE_NAME = "cave_name";
    protected static final String CITY_NAME = "city_name";
    protected static final String HALL_NAME = "hall_name";

    protected static final String FROSTNOVA_QUEST_NAME = "frostnova_quest_name";

    public Plot()
    {
        process = 1;
    }

    public static final HashMap<String ,Plot> PlotsForName = new HashMap<String, Plot>()
    {
        {
            put(PLOT_SEWER ,  new RenPlot());
            put(PLOT_PRISON , new RenPlot());
            put(PLOT_CAVE, new RenPlot());
            put(PLOT_CITY , new RenPlot());
            put(PLOT_HALL , new RenPlot());

            //put(PLOT_FROSTNOVA_QUEST,new FrostNovaQuestPlot());
        }
    };

    public static final HashMap<String ,String> NameForPlot = new HashMap<String, String>()
    {
        {
            put(SEWER_NAME , PLOT_SEWER);
            put(PRISON_NAME ,PLOT_SEWER);
            put(CAVE_NAME, PLOT_SEWER);
            put(CITY_NAME , PLOT_SEWER);
            put(HALL_NAME , PLOT_SEWER);

            put(FROSTNOVA_QUEST_NAME , PLOT_FROSTNOVA_QUEST);
        }
    };

    protected abstract String getPlotName();

    //It sounds weird as we need to distinguish plot with a nickname,well,that's still better than use instanceof again and again.
    public static void storeInBundle(Bundle b, Plot settedPlot) {
        b.put(PLOT_PROCESS , settedPlot.process);
        b.put(PLOT_INPUTS , NameForPlot.get(settedPlot.getPlotName()));
    }

    public static Plot restorFromBundle(Bundle b) {

        if(b.contains(PLOT_INPUTS) && b.contains(PLOT_PROCESS))
        {
            String plotname = b.getString(PLOT_INPUTS);

            Plot plot= PlotsForName.get(plotname);

            plot.needed_process = b.getInt(PLOT_PROCESS);

            return plot;
        }
        else return null;
    }

    public abstract void reachProcess(WndDialog wndDialog);

    public abstract void process();

    public abstract void initial(WndDialog wndDialog);

    public abstract boolean end();

    public abstract void skip();

    public void setProcess(int p)
    {
        process = p;
    }
}
