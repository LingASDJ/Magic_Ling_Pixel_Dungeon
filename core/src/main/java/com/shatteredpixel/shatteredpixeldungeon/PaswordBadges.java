package com.shatteredpixel.shatteredpixeldungeon;

import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.fiveyears.BzmdrNewYears;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.FileUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class PaswordBadges {

    public static void KILLDWARF() {
        displayBadge( PaswordBadges.Badge.DRAWF_HEAD );
    }

    public static void KILLFIREGIRL() {
        displayBadge( PaswordBadges.Badge.FIREGIRL );
    }

    public static void KILLSAKA() {
        displayBadge( PaswordBadges.Badge.SAKA_DIED);
    }

    public static void REHOMESKY() {
        displayBadge( PaswordBadges.Badge.RESET_DAY );
    }

    public static void BOSSRUSH() {
        displayBadge( PaswordBadges.Badge.BRCLER );
    }

    public static void WHATSUP() {
        displayBadge( Badge.WHATSUP );
    }

    public static void KILLDWG() {
        displayBadge( Badge.KILL_DWG );
    }

    public static void UNLOCK_KING() {
        displayBadge( Badge.KING_ISTO );
    }

    public static void KILL_FISH() {
        displayBadge( Badge.KILL_FISHBOSS );
    }

    public static void UNLOCK_RICESWORD() {
        displayBadge( Badge.UNLOCK_RICESWORD );
    }

    public static void KILL_YOGSTS() {
        displayBadge( Badge.KILL_YOGSTS );
    }

    public static void SWORDDRAGON() {
        displayBadge( Badge.SWORDDREAM );
    }

    public static void ALLCS(int challenges){
        PaswordBadges.Badge badge = null;

        boolean isNoSeedMode = !Statistics.seedCustom;

        if (challenges == 0 || !isNoSeedMode) return;
        boolean isNoChampion = Statistics.bossRushMode || Statistics.RandMode;


        if (challenges >= 15 && !(Dungeon.isDLC(Conducts.Conduct.DEV))|| !isNoChampion  && challenges >= 15) {
            disown( Badge.ALLCHSX );
            badge = Badge.GODCHSX;
            local.add( badge );
        } else if (challenges >= 13 && !(Dungeon.isDLC(Conducts.Conduct.DEV))|| !isNoChampion && challenges >= 13) {
            badge = Badge.ALLCHSX;
            local.add( badge );
        }
        displayBadge( badge );
    }

    public static void KILLALLBOSS() {
        displayBadge( Badge.SPICEALBOSS );
    }

    public static void BIGX() {
        displayBadge( PaswordBadges.Badge.BIG_X );
    }

    public static void EXSG() {
        displayBadge( PaswordBadges.Badge.EXSG );
    }

    public static void NIGHT_CAT() {
        displayBadge( PaswordBadges.Badge.NIGHT_CAT );
    }

    public static void ZQJ_FLOWER() {
        displayBadge( Badge.ZQJ_GHOST);
    }

    public static void GOOD_BLUE() {
        displayBadge( Badge.GOOD_BLUE);
    }

    public static void NightOrHell() {
        displayBadge( Badge.HELLORWORLD);
    }

    public static void HelloLing() {
        displayBadge( Badge.PINK_LING);
    }

    public static void LEGEND() {
        displayBadge(Badge.PRAY_LEGEND);
    }

    public static void VAM_GHOST() {
        displayBadge(Badge.VAMGHOST_DEAD);
    }

    public static void SUGAR_FRIENDLY() {
        displayBadge(Badge.SUGAR_FRIENDLY);
    }

    public static void DOG_TOUCH() {
        displayBadge(Badge.DOG_TOUCH);
    }

    public static void HELL_BACK() {
        displayBadge(Badge.HELL_BACK);
    }

    public static void HIRO() {
        displayBadge(Badge.TIME_CIRCLE);
    }


    public static void HERO_CLRE(int challenges) {
        boolean isNoSeedMode = !Statistics.seedCustom;

        if (challenges == 0 || !isNoSeedMode) return;

        boolean isNoChampion = Statistics.bossRushMode || Statistics.RandMode;

        BzmdrNewYears.BzmdrGift bzmdrGift = Dungeon.hero.belongings.getItem(BzmdrNewYears.BzmdrGift.class);
        if(bzmdrGift != null){
            if(challenges >= 10 && !isNoChampion ){
                displayBadge(Badge.HERO_CLRE);
            }
        }
    }

    public static void GHOST_HUNTER() {
        displayBadge(Badge.GHOST_HUNTER);
    }

    public static void MYTHOLOGY() {
        displayBadge(Badge.PRAY_MYTHOLOGY);
    }

    public static void SKY_DEAD() {
        displayBadge(Badge.SKY_XEAD);
    }



    public enum Badge {

        TAKE_ITEM( 0 ),
        FIREGIRL(1),
        SLIMEPRS(2),
        DRAWF_HEAD(3),
        SPICEALBOSS 				 (4),
        SAKA_DIED(5),
        RESET_DAY(6),
        BRCLER(7),


        ALLCHSX(8,true,true),
        GODCHSX(9,true,true),

        SKY_XEAD(10),
        GODD_MAKE(12),

        BIG_X(13),
        EXSG(14),

        SWORDDREAM(15),

        NIGHT_CAT(16),
        ZQJ_GHOST(17),
        GOOD_BLUE(18),

        HELLORWORLD(21),

        PINK_LING(22,true),

        WHATSUP(23),
        KILL_DWG(24),
        KING_ISTO(25),

        KILL_FISHBOSS(26),

        UNLOCK_RICESWORD(27),

        KILL_YOGSTS(28),

        //祈愿
        PRAY_LEGEND (30),
        PRAY_MYTHOLOGY (31,true),

        VAMGHOST_DEAD(32),

        SUGAR_FRIENDLY(33,true),

        DOG_TOUCH(34,true),

        GHOST_HUNTER(35,true),

        HELL_BACK					(36),

        TIME_CIRCLE     (37,true),

        HERO_CLRE       (38,true);

        public boolean meta;

        public boolean challenges;

        public int image;

        Badge( int image ) {
            this( image, false,false );
        }

        Badge( int image,boolean meta ) {
            this( image, false,false );
        }

        Badge( int image, boolean meta,boolean challenges ) {
            this.image = image;
            this.meta = meta;
            this.challenges = challenges;
        }

        public String title(){
            return Messages.get(this, name()+".title");
        }

        public String desc(){
            if(challenges) {
                return Messages.get(this, name() + ".desc", SPDSettings.RecordChallengs());
            } else {
                return Messages.get(this, name()+".desc");
            }

        }

        Badge() {
            this( -1 );
        }
    }

    public static HashSet<Badge> global;
    public static HashSet<Badge> local = new HashSet<>();

    private static boolean saveNeeded = false;

    public static void reset() {
        local.clear();
        loadGlobal();
    }

    public static final String ZBADGES_FILE	= "z-badges.dat";
    private static final String BADGES		= "badges";

    private static final HashSet<String> removedBadges = new HashSet<>();
    static{
        //removed in 0.6.5
        removedBadges.addAll(Arrays.asList("RARE_ALBINO", "RARE_BANDIT", "RARE_SHIELDED",
                "RARE_SENIOR", "RARE_ACIDIC", "RARE", "TUTORIAL_WARRIOR", "TUTORIAL_MAGE"));
    }

    private static final HashMap<String, String> renamedBadges = new HashMap<>();
    static{
        //0.6.5
        renamedBadges.put("CHAMPION", "CHAMPION_1");
    }

    public static HashSet<Badge> restore( Bundle bundle ) {
        HashSet<Badge> badges = new HashSet<>();
        if (bundle == null) return badges;

        String[] names = bundle.getStringArray( BADGES );
        for (int i=0; i < names.length; i++) {
            try {
                if (renamedBadges.containsKey(names[i])){
                    names[i] = renamedBadges.get(names[i]);
                }
                if (!removedBadges.contains(names[i])){
                    badges.add( Badge.valueOf( names[i] ) );
                }
            } catch (Exception e) {
                ShatteredPixelDungeon.reportException(e);
            }
        }

        return badges;
    }

    public static void store( Bundle bundle, HashSet<Badge> badges ) {
        int count = 0;
        String names[] = new String[badges.size()];

        for (Badge badge:badges) {
            names[count++] = badge.toString();
        }
        bundle.put( BADGES, names );
    }

    public static void loadLocal( Bundle bundle ) {
        local = restore( bundle );
    }

    public static void saveLocal( Bundle bundle ) {
        store( bundle, local );
    }

    public static void loadGlobal() {
        if (global == null) {
            try {
                Bundle bundle = FileUtils.bundleFromFile( ZBADGES_FILE );
                global = restore( bundle );

            } catch (IOException e) {
                global = new HashSet<>();
            }
        }
    }

    public static void saveGlobal() {
        if (saveNeeded) {

            Bundle bundle = new Bundle();
            store( bundle, global );

            try {
                FileUtils.bundleToFile(ZBADGES_FILE, bundle);
                saveNeeded = false;
            } catch (IOException e) {
                ShatteredPixelDungeon.reportException(e);
            }
        }
    }

    public static void validateOMP() {
        displayBadge( Badge.TAKE_ITEM );
    }

    public static void displayBadge( Badge badge ) {
        PaswordBadges.loadGlobal();
        if (badge == null) {
            return;
        }

        if (global.contains( badge )) {

            if (!badge.meta) {
                GLog.h( Messages.get(PaswordBadges.class, "endorsed", badge.title()) );
            }

        } else {

            global.add( badge );
            saveNeeded = true;

            if (badge.meta) {
                GLog.h( Messages.get(PaswordBadges.class, "new_super", badge.title()) );
            } else {
                GLog.h( Messages.get(Badges.class, "new", badge.title()) );
            }
            PixelScene.showProBadge( badge );
        }
    }

    public static boolean isUnlocked( Badge badge ) {
        return global.contains( badge );
    }

    public static HashSet<Badge> allUnlocked(){
        loadGlobal();
        return new HashSet<>(global);
    }

    public static void disown( Badge badge ) {
        loadGlobal();
        global.remove( badge );
        saveNeeded = true;
    }

    public static void addGlobal( Badge badge ){
        if (!global.contains(badge)){
            global.add( badge );
            saveNeeded = true;
        }
    }

    public static List<Badge> filtered( boolean global ) {

        HashSet<Badge> filtered = new HashSet<>(global ? PaswordBadges.global : PaswordBadges.local);

        Iterator<Badge> iterator = filtered.iterator();
        while (iterator.hasNext()) {
            Badge badge = iterator.next();
            if ((!global && badge.meta) || badge.image == -1) {
                iterator.remove();
            }
        }

        ArrayList<Badge> list = new ArrayList<>(filtered);
        Collections.sort( list );

        return list;
    }

    private static void leaveBest( HashSet<Badge> list, Badge...badges ) {
        for (int i=badges.length-1; i > 0; i--) {
            if (list.contains( badges[i])) {
                for (int j=0; j < i; j++) {
                    list.remove( badges[j] );
                }
                break;
            }
        }
    }
}