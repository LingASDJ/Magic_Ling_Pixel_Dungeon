package com.shatteredpixel.shatteredpixeldungeon;

import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;

import java.util.ArrayList;
import java.util.Arrays;

//难度系统
public class Difficulty {
    public enum DifficultyConduct {
        NULL,
        EASY(1.2f),
        NORMAL(2.5f);
//        HARD(1.75f);
//        IMPOSSIBLE(1.5f);

        public float scoreMod;

        DifficultyConduct(){
            scoreMod = 1f;
        }

        DifficultyConduct(float scoreMod){
            this.scoreMod = scoreMod;
        }

        @Override
        public String toString() {
            return Messages.get(Difficulty.class, this.name());
        }

        public String desc(){
            return Messages.get(Difficulty.class, name() + "_desc");
        }
    }

    public static class HardStorage implements Bundlable {

        public ArrayList<DifficultyConduct> difficultyConducts;

        public HardStorage() {
            difficultyConducts = new ArrayList<>();
        }

        public HardStorage(DifficultyConduct... difficultyConducts) {this.difficultyConducts = new ArrayList<>(Arrays.asList(difficultyConducts));}

        public HardStorage(HardStorage storage) {this.difficultyConducts = new ArrayList<>(storage.difficultyConducts);}

        @Override
        public void storeInBundle(Bundle bundle) {
            ArrayList<String> conductIds = new ArrayList<>();
            for (DifficultyConduct difficultyConduct : difficultyConducts){
                conductIds.add(difficultyConduct.name());
            }
            bundle.put("difficultyConducts", conductIds.toArray(new String[0]));
        }

        @Override
        public void restoreFromBundle(Bundle bundle) {
            difficultyConducts.clear();
            if (bundle.getStringArray("difficultyConducts") != null) {
                String[] difficultyConductsIds = bundle.getStringArray("difficultyConducts");
                for (String conduct : difficultyConductsIds) {
                    difficultyConducts.add(DifficultyConduct.valueOf(conduct));
                }
            }
        }

        public String getDebugString(){
            if (difficultyConducts.isEmpty()){
                return "NULL";
            }
            StringBuilder str = new StringBuilder();
            for (DifficultyConduct difficultyConduct : difficultyConducts){
                str.append(difficultyConduct.name()).append(",");
            }
            str.delete(str.length() - 1, str.length());
            return str.toString();
        }

        public boolean isConductedAtAll(){
            return !difficultyConducts.isEmpty();
        }

        public boolean oneConduct(){
            return difficultyConducts.size() == 1;
        }

        public boolean isConducted(DifficultyConduct mask){
            return isConductedAtAll() && difficultyConducts.contains(mask);
        }

        public DifficultyConduct getFirst(){
            if (isConductedAtAll()) return difficultyConducts.get(0);
            return null;
        }
    }
}
