package com.shatteredpixel.shatteredpixeldungeon;

import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;

import java.util.ArrayList;

public class Conducts {
    public enum Conduct {
        NULL,
        EASY,
        NORMAL,
        HARD,
        DEV;


        @Override
        public String toString() {
            return Messages.get(Conducts.class, this.name());
        }

        public String desc(){
            return Messages.get(Conducts.class, name() + "_desc");
        }
    }

    public static class ConductStorage implements Bundlable {

        public ArrayList<Conduct> conducts;

        public ConductStorage() {
            conducts = new ArrayList<>();
        }

        public ConductStorage(ConductStorage storage) {this.conducts = new ArrayList<>(storage.conducts);}

        @Override
        public void storeInBundle(Bundle bundle) {
            ArrayList<String> conductIds = new ArrayList<>();
            for (Conduct conduct: conducts){
                conductIds.add(conduct.name());
            }
            bundle.put("conduct", conductIds.toArray(new String[0]));
        }

        @Override
        public void restoreFromBundle(Bundle bundle) {
            conducts.clear();
            if (bundle.getStringArray("conduct") != null) {
                String[] conductIds = bundle.getStringArray("conduct");
                for (String conduct : conductIds) {
                    conducts.add(Conduct.valueOf(conduct));
                }
            }
        }

        public boolean isConductedAtAll(){
            return !conducts.isEmpty();
        }

        public boolean isConducted(Conduct mask){
            return isConductedAtAll() && conducts.contains(mask);
        }

        public Conduct getFirst(){
            if (isConductedAtAll()) return conducts.get(0);
            return null;
        }
    }
}
