package com.shatteredpixel.shatteredpixeldungeon;

import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;

//难度系统
public enum Difficulty {
    EASY,
    MEDIUM,
    HARD
//    IMPOSSIBLE

    {
        @Override
        public boolean isUnlocked() {
            return Badges.isUnlocked(Badges.Badge.VICTORY_ALL_CLASSES);
        }
    };

    public boolean isUnlocked() {
        return true;
    }


    public float mobHealthFactor() {
        switch (this) {
            case EASY:
                return 2/3f;
            case MEDIUM: default:
                return 1f;
            case HARD:
                return 1 + 1/3f;
//            case IMPOSSIBLE:
//                return 2f;
        }
    }

    public float mobDamageFactor() {
        switch (this) {
            case EASY:
                return 0.5f;
            case MEDIUM: default:
                return 1f;
            case HARD:
                return 1.5f;
//            case IMPOSSIBLE:
//                return 2f;
        }
    }

    public float moraleFactor() {
        switch (this) {
            case EASY:
                return 2/3f;
            case MEDIUM: default:
                return 1f;
            case HARD:
                return 1 + 1/3f;
//            case IMPOSSIBLE:
//                return 2f;
        }
    }

    public int degradationAmount() {
        switch (this) {
            case EASY://Easy = 3 drop in durability per hit (200 hits until break)
                return 3;
            case MEDIUM: default://Medium = 6 drop in durability per hit (100 hits until break)
                return 6;
            case HARD://Hard = 12 drop in durability per hit (83 hits until break)
                return 12;
//            case IMPOSSIBLE:
//                return 20;//Impossible = 20 drop in durability per hit (50 hits until break)
        }
    }

    public static Difficulty fromInt(int diff) {
        switch (diff) {
            case 1:
                return EASY;
            case 2: default:
                return MEDIUM;
            case 3:
                return HARD;
//            case 4:
//                return IMPOSSIBLE;
        }
    }

    public String title() {
        return Messages.get(Difficulty.class, name());
    }
}