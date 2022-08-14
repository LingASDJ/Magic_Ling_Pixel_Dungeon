package com.shatteredpixel.shatteredpixeldungeon.windows;

import com.watabou.utils.GameSettings;

public class CustomGameSettings extends GameSettings {

    public static void putSeedString(String str){
        put("seed_string_input_val", str);
    }

    public static String getSeedString(){
        return getString("seed_string_input_val", "");
    }

    public static void putnameString(String str){
        put("name_string_input_val", str);
    }

    public static String getnameString(){
        return getString("name_string_input_val", "");
    }


    public static void getnameString(String text) {
    }
}