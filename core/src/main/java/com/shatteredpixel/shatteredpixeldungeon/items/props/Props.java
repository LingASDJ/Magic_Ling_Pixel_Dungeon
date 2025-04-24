package com.shatteredpixel.shatteredpixeldungeon.items.props;

import com.shatteredpixel.shatteredpixeldungeon.items.Item;

import java.util.ArrayList;
import java.util.Arrays;

public class Props {

    public static int getRareness(Item p){
        for(Prop prop : propPositive0){
            if(p.getClass() == prop.getClass())
                return 0;
        }
        for(Prop prop : propNegative0){
            if(p.getClass() == prop.getClass())
                return 0;
        }
        for(Prop prop : propPositive1){
            if(p.getClass() == prop.getClass())
                return 1;
        }
        for(Prop prop : propNegative1){
            if(p.getClass() == prop.getClass())
                return 1;
        }
        for(Prop prop : propPositive2){
            if(p.getClass() == prop.getClass())
                return 2;
        }
        for(Prop prop : propNegative2){
            if(p.getClass() == prop.getClass())
                return 2;
        }
        return 0;
    }



    public static ArrayList<Prop> propPositive0 = new ArrayList<>(Arrays.asList(
            new ArmorScalesOfBzmdr(),
            new StarSachet(),
            new PortableWhetstone()
    ));

    public static ArrayList<Prop> propNegative0 = new ArrayList<>(Arrays.asList(
            new BlockingDrug(),
            new ConfusedMieMieTalisman(),
            new RustedGoldCoin()
    ));

    public static ArrayList<Prop> propPositive1 = new ArrayList<>(Arrays.asList(
            new DeliciousRecipe(),
            new NewStem(),
            new RapidEarthRoot(),
            new WenStudyingPaperOne(),
            new YanStudyingPaperTwo()
    ));

    public static ArrayList<Prop> propNegative1 = new ArrayList<>(Arrays.asList(
            new BottleWraith(),
            new EmotionalAggregationB(),
            new HeartOfCrystalFractal(),
            new NoteOfBzmdr(),
            new TheGriefOfSpeechless(),
            new WenStudyingPaperTwo()
    ));

    public static ArrayList<Prop> propPositive2 = new ArrayList<>(Arrays.asList(
            new LuckyGlove(),
            new EmotionalAggregation(),
            new Monocular(),
            new KnightStabbingSword()
    ));

    public static ArrayList<Prop> propNegative2 = new ArrayList<>(Arrays.asList(
            new CloakFragmentsOfBzmdr(),
            new BrokenBone(),
            new TerrorDoll(),
            new YanStudyingPaperOne()
    ));

    public static boolean hasAllRarenessProp(int rare,int kind){
        if(kind == 0){
            switch (rare){
                case 0:
                    return propPositive0.isEmpty();
                case 1:
                    return propPositive1.isEmpty();
                case 2:
                    return propPositive2.isEmpty();
            }
        }else{
            switch (rare){
                case 0:
                    return propNegative0.isEmpty();
                case 1:
                    return propNegative1.isEmpty();
                case 2:
                    return propNegative2.isEmpty();
            }
        }
        return  false;
    }

    public static void add(Prop prop){
        if( prop.kind == 0){
            switch (prop.rareness){
                case 0:
                    propPositive0.add(prop);
                    break;
                case 1:
                    propPositive1.add(prop);
                    break;
                case 2:
                    propPositive2.add(prop);
                    break;
            }
        }else{
            switch (prop.rareness){
                case 0:
                    propNegative0.add(prop);
                    break;
                case 1:
                    propNegative1.add(prop);
                    break;
                case 2:
                    propNegative2.add(prop);
                    if(prop instanceof TerrorDoll) propNegative2.add(new TerrorDollB());
                    if(prop instanceof TerrorDollB) propNegative2.add(new TerrorDoll());
                    break;
            }
        }
    }
}
