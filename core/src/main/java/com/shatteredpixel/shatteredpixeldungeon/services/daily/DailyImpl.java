package com.shatteredpixel.shatteredpixeldungeon.services.daily;

public class DailyImpl {
    private static DailyService service = new DailyServiceImpl();

    public static DailyService getService() { return service; }
    public static boolean supportsDaily() { return true; }
}