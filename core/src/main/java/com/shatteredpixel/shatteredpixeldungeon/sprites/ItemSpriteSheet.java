/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2023 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

public class ItemSpriteSheet {

    private static final int WIDTH = 32;
    public static final int SIZE = 16;

    public static TextureFilm film = new TextureFilm( Assets.Sprites.ITEMS, SIZE, SIZE );

    private static int xy(int x, int y){
        x -= 1; y -= 1;
        return x + WIDTH*y;
    }

    private static void assignItemRect( int item, int width, int height ){
        int x = (item % WIDTH) * SIZE;
        int y = (item / WIDTH) * SIZE;
        film.add( item, x, y, x+width, y+height);
    }

    private static final int PLACEHOLDERS   =                               xy(1, 1);   //16 slots
    //SOMETHING is the default item sprite at position 0. May show up ingame if there are bugs.
    public static final int SOMETHING       = PLACEHOLDERS+0;
    public static final int WEAPON_HOLDER   = PLACEHOLDERS+1;
    public static final int ARMOR_HOLDER    = PLACEHOLDERS+2;
    public static final int MISSILE_HOLDER  = PLACEHOLDERS+3;
    public static final int WAND_HOLDER     = PLACEHOLDERS+4;
    public static final int RING_HOLDER     = PLACEHOLDERS+5;
    public static final int ARTIFACT_HOLDER = PLACEHOLDERS+6;
    public static final int FOOD_HOLDER     = PLACEHOLDERS+7;
    public static final int BOMB_HOLDER     = PLACEHOLDERS+8;
    public static final int POTION_HOLDER   = PLACEHOLDERS+9;
    public static final int SCROLL_HOLDER   = PLACEHOLDERS+11;
    public static final int SEED_HOLDER     = PLACEHOLDERS+10;
    public static final int STONE_HOLDER    = PLACEHOLDERS+12;
    public static final int CATA_HOLDER     = PLACEHOLDERS+13;
    public static final int ELIXIR_HOLDER   = PLACEHOLDERS+14;
    public static final int SPELL_HOLDER    = PLACEHOLDERS+15;
    static{
        assignItemRect(SOMETHING,       8,  13);
        assignItemRect(WEAPON_HOLDER,   13, 13);
        assignItemRect(ARMOR_HOLDER,    14, 12);
        assignItemRect(MISSILE_HOLDER,  14, 14);
        assignItemRect(WAND_HOLDER,     14, 14);
        assignItemRect(RING_HOLDER,     8,  10);
        assignItemRect(ARTIFACT_HOLDER, 14, 14);
        assignItemRect(FOOD_HOLDER,     15, 11);
        assignItemRect(BOMB_HOLDER,     10, 13);
        assignItemRect(POTION_HOLDER,   12, 14);
        assignItemRect(SEED_HOLDER,     10, 10);
        assignItemRect(SCROLL_HOLDER,   15, 14);
        assignItemRect(STONE_HOLDER,    14, 12);
        assignItemRect(CATA_HOLDER,     12,  14);
        assignItemRect(ELIXIR_HOLDER,   12, 14);
        assignItemRect(SPELL_HOLDER,    8,  16);
    }

    private static final int UNCOLLECTIBLE  =                               xy(1, 2);   //16 slots
    public static final int GOLD            = UNCOLLECTIBLE+0;
    public static final int ENERGY          = UNCOLLECTIBLE+1;

    public static final int EMPTY           = UNCOLLECTIBLE+2;

    public static final int DEWDROP         = UNCOLLECTIBLE+3;
    public static final int PETAL           = UNCOLLECTIBLE+4;
    public static final int SANDBAG         = UNCOLLECTIBLE+5;
    public static final int SPIRIT_ARROW    = UNCOLLECTIBLE+6;

    public static final int TENGU_BOMB      = UNCOLLECTIBLE+8;
    public static final int TENGU_SHOCKER   = UNCOLLECTIBLE+9;

    public static final int GREEN_DARK   = UNCOLLECTIBLE+11;

    public static final int RED_BLOOD   = UNCOLLECTIBLE+12;

    public static final int GLASS_CI   = UNCOLLECTIBLE+13;

    public static final int ICEGOLD   = UNCOLLECTIBLE+14;

    public static final int BEST_RAT  = UNCOLLECTIBLE+15;

    static{
        assignItemRect(GOLD,        15, 13);
        assignItemRect(ENERGY,      16, 16);

        assignItemRect(DEWDROP,     10, 10);
        assignItemRect(PETAL,       8,  8);
        assignItemRect(SANDBAG,     10, 10);
        assignItemRect(SPIRIT_ARROW,11, 11);

        assignItemRect(TENGU_BOMB,      10, 10);
        assignItemRect(TENGU_SHOCKER,   10, 10);

        assignItemRect(GREEN_DARK,      14, 14);
        assignItemRect(RED_BLOOD,       11, 11);

        assignItemRect(BEST_RAT,       15, 14);
    }

    private static final int CONTAINERS     =                               xy(1, 3);   //16 slots
    public static final int BONES           = CONTAINERS+0;
    public static final int REMAINS         = CONTAINERS+1;
    public static final int TOMB            = CONTAINERS+2;
    public static final int GRAVE           = CONTAINERS+3;
    public static final int CHEST           = CONTAINERS+4;
    public static final int LOCKED_CHEST    = CONTAINERS+5;
    public static final int CRYSTAL_CHEST   = CONTAINERS+6;
    public static final int EBONY_CHEST     = CONTAINERS+7;
    public static final int RIP     = CONTAINERS+8;

    public static final int SHPD_CHEST     = CONTAINERS+9;
    public static final int SHPD_KEY     = CONTAINERS+10;

    public static final int RICE_LIQUOR     = CONTAINERS+11;

    public static final int EASTER_EGG     = CONTAINERS+14;

    static{
        assignItemRect(BONES,           14, 11);
        assignItemRect(REMAINS,         14, 11);
        assignItemRect(TOMB,            14, 15);
        assignItemRect(GRAVE,           14, 15);
        assignItemRect(CHEST,           16, 14);
        assignItemRect(LOCKED_CHEST,    16, 14);
        assignItemRect(CRYSTAL_CHEST,   16, 14);
        assignItemRect(EBONY_CHEST,     16, 14);
        assignItemRect(SHPD_CHEST,       16,14);
        assignItemRect(SHPD_KEY,         8, 14);

        assignItemRect(RICE_LIQUOR,      15,16);

        assignItemRect(EASTER_EGG,       12,14);
    }

    private static final int MISC_CONSUMABLE =                              xy(1, 4);   //16 slots
    public static final int ANKH            = MISC_CONSUMABLE +0;
    public static final int STYLUS          = MISC_CONSUMABLE +1;
    public static final int SEAL            = MISC_CONSUMABLE +2;
    public static final int TORCH           = MISC_CONSUMABLE +3;
    public static final int BEACON          = MISC_CONSUMABLE +4;
    public static final int HONEYPOT        = MISC_CONSUMABLE +5;
    public static final int SHATTPOT        = MISC_CONSUMABLE +6;
    public static final int IRON_KEY        = MISC_CONSUMABLE +7;
    public static final int GOLDEN_KEY      = MISC_CONSUMABLE +8;
    public static final int CRYSTAL_KEY     = MISC_CONSUMABLE +9;
    public static final int SKELETON_KEY    = MISC_CONSUMABLE +10;
    public static final int MASK            = MISC_CONSUMABLE +11;
    public static final int CROWN           = MISC_CONSUMABLE +12;
    public static final int AMULET          = MISC_CONSUMABLE +13;
    public static final int MASTERY         = MISC_CONSUMABLE +14;
    public static final int KIT             = MISC_CONSUMABLE +15;
    static{
        assignItemRect(ANKH,            10, 16);
        assignItemRect(STYLUS,          12, 13);

        assignItemRect(SEAL,            9,  15);
        assignItemRect(TORCH,           12, 15);
        assignItemRect(BEACON,          16, 15);

        assignItemRect(HONEYPOT,        14, 13);
        assignItemRect(SHATTPOT,        14, 12);
        assignItemRect(IRON_KEY,        8,  14);
        assignItemRect(GOLDEN_KEY,      8,  14);
        assignItemRect(CRYSTAL_KEY,     8,  14);
        assignItemRect(SKELETON_KEY,    8,  14);
        assignItemRect(MASK,            11,  9);
        assignItemRect(CROWN,           13,  7);
        assignItemRect(AMULET,          16, 16);
        assignItemRect(MASTERY,         13, 16);
        assignItemRect(KIT,             16, 15);
    }

    private static final int BOMBS          =                               xy(1, 5);   //16 slots
    public static final int BOMB            = BOMBS+0;
    public static final int DBL_BOMB        = BOMBS+1;
    public static final int FIRE_BOMB       = BOMBS+2;
    public static final int FROST_BOMB      = BOMBS+3;
    public static final int REGROWTH_BOMB   = BOMBS+4;
    public static final int FLASHBANG       = BOMBS+5;
    public static final int SHOCK_BOMB      = BOMBS+6;
    public static final int HOLY_BOMB       = BOMBS+7;
    public static final int WOOLY_BOMB      = BOMBS+8;
    public static final int NOISEMAKER      = BOMBS+9;
    public static final int ARCANE_BOMB     = BOMBS+10;
    public static final int SHRAPNEL_BOMB   = BOMBS+11;

    public static final int BLACK_KEY    = BOMBS+12;

    public static final int CRYSTAL_LING    = BOMBS+13;

    public static final int CRYSTAL_QUESTION    = BOMBS+14;

    public static final int RICKROLL   = BOMBS+15;


    static{
        assignItemRect(BOMB,            12, 14);
        assignItemRect(DBL_BOMB,        15, 14);
        assignItemRect(FIRE_BOMB,       11, 14);
        assignItemRect(FROST_BOMB,      11, 14);
        assignItemRect(REGROWTH_BOMB,   11, 14);
        assignItemRect(FLASHBANG,       11, 14);
        assignItemRect(SHOCK_BOMB,      16, 14);
        assignItemRect(HOLY_BOMB,       13, 12);
        assignItemRect(WOOLY_BOMB,      10, 14);
        assignItemRect(NOISEMAKER,      14, 14);
        assignItemRect(ARCANE_BOMB,     10, 13);
        assignItemRect(SHRAPNEL_BOMB,   13, 15);
        assignItemRect(BLACK_KEY,   	8, 14);
        assignItemRect(CRYSTAL_QUESTION,   	15, 11);
    }


    //16 free slots

    private static final int WEP_TIER1      =                               xy(18, 1);   //8 slots
    public static final int WORN_SHORTSWORD = WEP_TIER1+0;
    public static final int GLOVES          = WEP_TIER1+1;
    public static final int RAPIER          = WEP_TIER1+2;
    public static final int DAGGER          = WEP_TIER1+3;
    public static final int MAGES_STAFF     = WEP_TIER1+4;
    static{
        assignItemRect(WORN_SHORTSWORD, 13, 13);
        assignItemRect(GLOVES,          12, 16);
        assignItemRect(RAPIER,          16, 16);
        assignItemRect(DAGGER,          12, 13);
        assignItemRect(MAGES_STAFF,     15, 16);
    }

    private static final int WEP_TIER2      =                               xy(18, 3);   //8 slots
    public static final int RITUAL_SWORD_M      = WEP_TIER2+0;
    public static final int SHOP_SWORD        = WEP_TIER2+1;
    public static final int SPEAR           = WEP_TIER2+2;
    public static final int TREELIST    = WEP_TIER2+3;
    public static final int DIRK            = WEP_TIER2+4;
    public static final int SICKLE          = WEP_TIER2+5;

    public static final int	ENDDIED			= WEP_TIER2+7;
    static{
        assignItemRect(RITUAL_SWORD_M,      13, 13);
        assignItemRect(SHOP_SWORD,        14, 16);
        assignItemRect(SPEAR,           16, 16);
        assignItemRect(TREELIST,    14, 13);
        assignItemRect(DIRK,            13, 14);
        assignItemRect(SICKLE,          15, 15);
    }

    private static final int WEP_TIER3      =                               xy(18, 5);   //8 slots

    public static final int SAD_MAGIC_BOOK  = WEP_TIER3;
    public static final int SHEPHERD_FLUTE  = WEP_TIER3+1;
    public static final int MACE            = WEP_TIER3+1;
    public static final int SCIMITAR        = WEP_TIER3+2;
    public static final int ROUND_SHIELD    = WEP_TIER3+3;
    public static final int SAI             = WEP_TIER3+4;
    public static final int WHIP            = WEP_TIER3+5;
    public static final int SKY_SHIELD      = WEP_TIER3+6;
    public static final int DAIRI_KYAN      = WEP_TIER3+7;
    public static final int MAGIC_TORCH      = WEP_TIER3+8;
    public static final int JUNGLE_SWORD      = WEP_TIER3+9;
    public static final int	SDBlade			    = WEP_TIER3+10;
    public static final int	HHBlade				= WEP_TIER3+11;
    public static final int LifeTreeSword       = WEP_TIER3+12;

    static{
        assignItemRect(SAD_MAGIC_BOOK,     14, 13);
        assignItemRect(SHEPHERD_FLUTE,     11, 16);
        assignItemRect(MACE,               15, 15);
        assignItemRect(SCIMITAR,           13, 16);
        assignItemRect(ROUND_SHIELD,       16, 16);
        assignItemRect(SAI,                16, 16);
        assignItemRect(WHIP,               14, 14);
        assignItemRect(SKY_SHIELD,         13, 15);
        assignItemRect(MAGIC_TORCH,        14, 16);
        assignItemRect(JUNGLE_SWORD,       16, 15);
    }

    private static final int WEP_TIER4      =                               xy(18, 7);   //8 slots
    public static final int LONGSWORD       = WEP_TIER4+0;
    public static final int BATTLE_AXE      = WEP_TIER4+1;
    public static final int FLAIL           = WEP_TIER4+2;
    public static final int KING_SWORD      = WEP_TIER4+3;
    public static final int ASSASSINS_BLADE = WEP_TIER4+4;
    public static final int CROSSBOW        = WEP_TIER4+5;
    public static final int KATANA          = WEP_TIER4+6;

    public static final int GRILLED_FISH     = WEP_TIER4+7;
    public static final int GRILLED_NOTE     = WEP_TIER4+8;
    public static final int SEEKSXS  		= WEP_TIER4+9;
    public static final int MOONDAILY		= WEP_TIER4+10;
    public static final int GOLDLANGGUN    = 	WEP_TIER4+11;
    public static final int CICRE_MUSIC    = 	WEP_TIER4+12;

    public static final int RedBloodMoon      =  WEP_TIER4+13;

    static{
        assignItemRect(LONGSWORD,       15, 15);
        assignItemRect(BATTLE_AXE,      16, 16);
        assignItemRect(FLAIL,           14, 14);

        assignItemRect(ASSASSINS_BLADE, 14, 15);
        assignItemRect(CROSSBOW,        15, 15);
        assignItemRect(KATANA,          15, 16);
    }

    private static final int WEP_TIER5      =                               xy(18, 9);   //8 slots


    public static final int LOCK_SWORD_IRON     = WEP_TIER5+0;
    public static final int LOCK_SWORD_GOLD     = WEP_TIER5+1;
    public static final int LOCK_SWORD_DIAMOND = WEP_TIER5+2;

    public static final int GREATSWORD      = WEP_TIER5+3;
    public static final int WAR_HAMMER      = WEP_TIER5+4;
    public static final int GLAIVE          = WEP_TIER5+5;
    public static final int GREATAXE        = WEP_TIER5+6;
    public static final int GREATSHIELD     = WEP_TIER5+7;
    public static final int	WASH_CRIME		= WEP_TIER5+8;
    public static final int BOMB_SWORD     = WEP_TIER5+9;
    public static final int DIEDCROSSBOW = WEP_TIER5+10;
    public static final int WAR_SCYTHE      = WEP_TIER5+11;
    public static final int  THEDIED       = WEP_TIER5+12;
    public static final int DRAGONSHILED = WEP_TIER5+13;
    public static final int KINGAXE=		WEP_TIER5+14;
    public static final int RICESWORD=		WEP_TIER5+32;
    public static final int GAUNTLETS       = WEP_TIER5+33;
    public static final int  CLEARPRO       = WEP_TIER5+34;
    public static final int  FORESTCROSSBOW       = WEP_TIER5+35;
    public static final int	WHITE_BAST		= WEP_TIER5+36;
    public static final int  FIVEREN= WEP_TIER5+37;
    public static final int DEATHRONG_BOAT = WEP_TIER5+38;



    static{
        assignItemRect(GREATSWORD,  16, 16);
        assignItemRect(WAR_HAMMER,  16, 16);
        assignItemRect(GLAIVE,      16, 16);
        assignItemRect(GREATAXE,    12, 16);

        assignItemRect(GAUNTLETS,   13, 15);
        assignItemRect(WAR_SCYTHE,  14, 15);
        assignItemRect(WASH_CRIME, 15, 14);

        assignItemRect(BOMB_SWORD, 16, 13);
        assignItemRect(DRAGONSHILED,12,16);
        assignItemRect(CLEARPRO,    15,15);

        assignItemRect(FIVEREN, 16, 13);
        assignItemRect(DEATHRONG_BOAT,            15, 15);
    }

    private static final int WEP_TIER6      =                               xy(18, 12);   //8 slots
    public static final int	FIREFISHSWORD				= WEP_TIER6;
    public static final int	ICEFISHSWORD				= WEP_TIER6+1;
    public static final int	BloodDir				    = WEP_TIER6+2;
    public static final int	BloodDied				    = WEP_TIER6+3;
    public static final int	SLIVER_LOCK				    = WEP_TIER6+4;

    //8 free slots

    private static final int MISSILE_WEP    =                               xy(1, 10);  //16 slots. 3 per tier + bow
    public static final int SPIRIT_BOW      = MISSILE_WEP+0;

    public static final int THROWING_SPIKE  = MISSILE_WEP+1;
    public static final int THROWING_KNIFE  = MISSILE_WEP+2;
    public static final int THROWING_STONE  = MISSILE_WEP+3;

    public static final int FISHING_SPEAR   = MISSILE_WEP+4;
    public static final int SHURIKEN        = MISSILE_WEP+5;
    public static final int THROWING_CLUB   = MISSILE_WEP+6;

    public static final int THROWING_SPEAR  = MISSILE_WEP+7;
    public static final int BOLAS           = MISSILE_WEP+8;
    public static final int KUNAI           = MISSILE_WEP+9;

    public static final int JAVELIN         = MISSILE_WEP+10;
    public static final int TOMAHAWK        = MISSILE_WEP+11;
    public static final int BOOMERANG       = MISSILE_WEP+12;

    public static final int TRIDENT         = MISSILE_WEP+13;
    public static final int THROWING_HAMMER = MISSILE_WEP+14;
    public static final int FORCE_CUBE      = MISSILE_WEP+15;

    static{
        assignItemRect(SPIRIT_BOW,      16, 16);

        assignItemRect(THROWING_SPIKE,  11, 10);
        assignItemRect(THROWING_KNIFE,  12, 13);
        assignItemRect(THROWING_STONE,  12, 10);

        assignItemRect(FISHING_SPEAR,   11, 11);
        assignItemRect(SHURIKEN,        12, 12);
        assignItemRect(THROWING_CLUB,   12, 12);

        assignItemRect(THROWING_SPEAR,  13, 13);
        assignItemRect(BOLAS,           15, 14);
        assignItemRect(KUNAI,           15, 15);

        assignItemRect(JAVELIN,         16, 16);
        assignItemRect(TOMAHAWK,        13, 13);
        assignItemRect(BOOMERANG,       14, 14);

        assignItemRect(TRIDENT,         16, 16);
        assignItemRect(THROWING_HAMMER, 12, 12);
        assignItemRect(FORCE_CUBE,      11, 12);
    }

    public static final int DARTS    =                                      xy(1, 11);  //16 slots
    public static final int DART            = DARTS+0;
    public static final int ROT_DART        = DARTS+1;
    public static final int INCENDIARY_DART = DARTS+2;
    public static final int ADRENALINE_DART = DARTS+3;
    public static final int HEALING_DART    = DARTS+4;
    public static final int CHILLING_DART   = DARTS+5;
    public static final int SHOCKING_DART   = DARTS+6;
    public static final int POISON_DART     = DARTS+7;
    public static final int CLEANSING_DART  = DARTS+8;
    public static final int PARALYTIC_DART  = DARTS+9;
    public static final int HOLY_DART       = DARTS+10;
    public static final int DISPLACING_DART = DARTS+11;
    public static final int BLINDING_DART   = DARTS+12;

    //MLPD
    public static final int HALO_DART   =   DARTS+13;
    public static final int LIGT_DART   = DARTS+14;

    public static final int RedBloodMoonEX     = DARTS+15;


    static {
        for (int i = DARTS; i < DARTS+16; i++)
            assignItemRect(i, 15, 15);
    }

    private static final int ARMOR          =                               xy(1, 12);  //16 slots
    public static final int ARMOR_CLOTH     = ARMOR+0;
    public static final int ARMOR_LEATHER   = ARMOR+1;
    public static final int ARMOR_MAIL      = ARMOR+2;
    public static final int ARMOR_SCALE     = ARMOR+3;
    public static final int ARMOR_PLATE     = ARMOR+4;

    public static final int ARMOR_LAMELLAR  = ARMOR+5;
    public static final int ARMOR_WARRIOR   = ARMOR+6;
    public static final int ARMOR_MAGE      = ARMOR+7;
    public static final int ARMOR_ROGUE     = ARMOR+8;
    public static final int ARMOR_HUNTRESS  = ARMOR+9;
    public static final int ARMOR_DUELIST   = ARMOR+10;

    public static final int ARMOR_ANCITY     = ARMOR+11;


    public static final int DW_FT     = ARMOR+12;

    static{
        assignItemRect(ARMOR_WARRIOR,   15, 13);
        assignItemRect(ARMOR_MAGE,      13, 13);
        assignItemRect(ARMOR_ROGUE,     11, 13);
        assignItemRect(ARMOR_HUNTRESS,  14, 13);
        assignItemRect(ARMOR_DUELIST,   13, 13);

        assignItemRect(ARMOR_CLOTH,     14, 14);
        assignItemRect(ARMOR_LEATHER,   15, 15);
        assignItemRect(ARMOR_MAIL,      15, 15);
        assignItemRect(ARMOR_SCALE,     16, 16);
        assignItemRect(ARMOR_PLATE,     15, 15);
        assignItemRect(ARMOR_ANCITY,    15, 14);
        assignItemRect(ARMOR_LAMELLAR,  14, 14);

        assignItemRect(DW_FT,           10, 10);
    }

    //16 free slots

    private static final int WANDS              =                           xy(1, 14);  //16 slots
    public static final int WAND_MAGIC_MISSILE  = WANDS+0;
    public static final int WAND_FIREBOLT       = WANDS+1;
    public static final int WAND_FROST          = WANDS+2;
    public static final int WAND_LIGHTNING      = WANDS+3;
    public static final int WAND_DISINTEGRATION = WANDS+4;
    public static final int WAND_PRISMATIC_LIGHT= WANDS+5;
    public static final int WAND_CORROSION      = WANDS+6;
    public static final int WAND_LIVING_EARTH   = WANDS+7;
    public static final int WAND_BLAST_WAVE     = WANDS+8;
    public static final int WAND_CORRUPTION     = WANDS+9;
    public static final int WAND_WARDING        = WANDS+10;
    public static final int WAND_REGROWTH       = WANDS+11;
    public static final int WAND_TRANSFUSION    = WANDS+12;

    private static final int RINGS          =                               xy(1, 15);  //16 slots
    public static final int WAND_UNKNOWN   = RINGS+13;

    public static final int KEYS_UNKNOWN   = RINGS+14;
    public static final int WAND_HAND_CONTROL   = RINGS+15;

    static {
        for (int i = WANDS; i < WANDS+15; i++)
            assignItemRect(i, 14, 14);
        assignItemRect(WAND_UNKNOWN, 14, 14);

        assignItemRect(KEYS_UNKNOWN, 8, 14);

        assignItemRect(WAND_HAND_CONTROL, 14, 14);
    }

    public static final int RING_GARNET     = RINGS+0;
    public static final int RING_RUBY       = RINGS+1;
    public static final int RING_TOPAZ      = RINGS+2;
    public static final int RING_EMERALD    = RINGS+3;
    public static final int RING_ONYX       = RINGS+4;
    public static final int RING_OPAL       = RINGS+5;
    public static final int RING_TOURMALINE = RINGS+6;
    public static final int RING_SAPPHIRE   = RINGS+7;
    public static final int RING_AMETHYST   = RINGS+8;
    public static final int RING_QUARTZ     = RINGS+9;
    public static final int RING_AGATE      = RINGS+10;
    public static final int RING_DIAMOND    = RINGS+11;
    public static final int RING_UNKNOWN    = RINGS+12;
    static {
        for (int i = RINGS; i < RINGS+13; i++)
            assignItemRect(i, 8, 10);
    }

    private static final int ARTIFACTS          =                            xy(1, 16);  //32 slots
    public static final int ARTIFACT_CLOAK      = ARTIFACTS+0;
    public static final int ARTIFACT_ARMBAND    = ARTIFACTS+1;
    public static final int ARTIFACT_CAPE       = ARTIFACTS+2;
    public static final int ARTIFACT_TALISMAN   = ARTIFACTS+3;
    public static final int ARTIFACT_HOURGLASS  = ARTIFACTS+4;
    public static final int ARTIFACT_TOOLKIT    = ARTIFACTS+5;
    public static final int ARTIFACT_SPELLBOOK  = ARTIFACTS+6;
    public static final int ARTIFACT_BEACON     = ARTIFACTS+7;
    public static final int ARTIFACT_CHAINS     = ARTIFACTS+8;
    public static final int ARTIFACT_HORN1      = ARTIFACTS+9;
    public static final int ARTIFACT_HORN2      = ARTIFACTS+10;
    public static final int ARTIFACT_HORN3      = ARTIFACTS+11;
    public static final int ARTIFACT_HORN4      = ARTIFACTS+12;
    public static final int ARTIFACT_CHALICE1   = ARTIFACTS+13;
    public static final int ARTIFACT_CHALICE2   = ARTIFACTS+14;
    public static final int ARTIFACT_CHALICE3   = ARTIFACTS+15;
    public static final int ARTIFACT_SANDALS    = ARTIFACTS+16;
    public static final int ARTIFACT_SHOES      = ARTIFACTS+17;
    public static final int ARTIFACT_BOOTS      = ARTIFACTS+18;
    public static final int ARTIFACT_GREAVES    = ARTIFACTS+19;
    public static final int ARTIFACT_ROSE1      = ARTIFACTS+20;
    public static final int ARTIFACT_ROSE2      = ARTIFACTS+21;
    public static final int ARTIFACT_ROSE3      = ARTIFACTS+22;
    public static final int Gold_Iron       = ARTIFACTS+23;

    public static final int MIME_ONE       = ARTIFACTS+24;
    public static final int MIME_TWO       = ARTIFACTS+25;
    public static final int MIME_THREE       = ARTIFACTS+26;
    public static final int MIME_FOUR       = ARTIFACTS+27;
    public static final int MIME_FIVE       = ARTIFACTS+28;
    public static final int WHITEROSE      = ARTIFACTS+29;
    public static final int REDWHITEROSE      = ARTIFACTS+30;

    public static final int ICESTONE      = ARTIFACTS+31;

    public static final int	WRALIPS			 = ARTIFACTS+32;
    public static final int	DM100RELEY				 = ARTIFACTS+35;
    static{
        assignItemRect(ARTIFACT_CLOAK,      9,  15);
        assignItemRect(ARTIFACT_ARMBAND,    16, 13);
        assignItemRect(ARTIFACT_CAPE,       16, 14);
        assignItemRect(ARTIFACT_TALISMAN,   15, 13);
        assignItemRect(ARTIFACT_HOURGLASS,  16, 16);
        assignItemRect(ARTIFACT_TOOLKIT,    16, 16);
        assignItemRect(ARTIFACT_SPELLBOOK,  13, 16);
        assignItemRect(ARTIFACT_BEACON,     16, 16);
        assignItemRect(ARTIFACT_CHAINS,     16, 16);
        assignItemRect(ARTIFACT_HORN1,      15, 15);
        assignItemRect(ARTIFACT_HORN2,      15, 15);
        assignItemRect(ARTIFACT_HORN3,      15, 15);
        assignItemRect(ARTIFACT_HORN4,      15, 15);
        assignItemRect(ARTIFACT_CHALICE1,   12, 15);
        assignItemRect(ARTIFACT_CHALICE2,   12, 15);
        assignItemRect(ARTIFACT_CHALICE3,   12, 15);
        assignItemRect(ARTIFACT_SANDALS,    16, 6 );
        assignItemRect(ARTIFACT_SHOES,      16, 6 );
        assignItemRect(ARTIFACT_BOOTS,      16, 9 );
        assignItemRect(ARTIFACT_GREAVES,    16, 14);
        assignItemRect(ARTIFACT_ROSE1,      14, 14);
        assignItemRect(ARTIFACT_ROSE2,      14, 14);
        assignItemRect(ARTIFACT_ROSE3,      14, 14);

        assignItemRect(Gold_Iron,           16, 16);
        assignItemRect(REDWHITEROSE,        15, 15);
        assignItemRect(MIME_TWO,            11, 14);
        assignItemRect(MIME_THREE,          15, 16);
        assignItemRect(WRALIPS, 14, 12);
        assignItemRect(DM100RELEY, 15, 13);
    }

    //16 free slots

    private static final int SCROLLS        =                               xy(1, 19);  //16 slots
    public static final int SCROLL_KAUNAN   = SCROLLS+0;
    public static final int SCROLL_SOWILO   = SCROLLS+1;
    public static final int SCROLL_LAGUZ    = SCROLLS+2;
    public static final int SCROLL_YNGVI    = SCROLLS+3;
    public static final int SCROLL_GYFU     = SCROLLS+4;
    public static final int SCROLL_RAIDO    = SCROLLS+5;
    public static final int SCROLL_ISAZ     = SCROLLS+6;
    public static final int SCROLL_MANNAZ   = SCROLLS+7;
    public static final int SCROLL_NAUDIZ   = SCROLLS+8;
    public static final int SCROLL_BERKANAN = SCROLLS+9;
    public static final int SCROLL_ODAL     = SCROLLS+10;
    public static final int SCROLL_TIWAZ    = SCROLLS+11;

    public static final int SCROLL_CATALYST = SCROLLS+13;
    public static final int ARCANE_RESIN    = SCROLLS+14;
    static {
        for (int i = SCROLLS; i < SCROLLS+16; i++)
            assignItemRect(i, 15, 14);
        assignItemRect(SCROLL_CATALYST, 12, 11);
        assignItemRect(ARCANE_RESIN   , 12, 11);

        for (int i = SCROLLS+16; i < SCROLLS+32; i++)
            assignItemRect(i, 16, 14);
    }

    private static final int EXOTIC_SCROLLS =                               xy(1, 20);  //16 slots
    public static final int EXOTIC_KAUNAN   = EXOTIC_SCROLLS+0;
    public static final int EXOTIC_SOWILO   = EXOTIC_SCROLLS+1;
    public static final int EXOTIC_LAGUZ    = EXOTIC_SCROLLS+2;
    public static final int EXOTIC_YNGVI    = EXOTIC_SCROLLS+3;
    public static final int EXOTIC_GYFU     = EXOTIC_SCROLLS+4;
    public static final int EXOTIC_RAIDO    = EXOTIC_SCROLLS+5;
    public static final int EXOTIC_ISAZ     = EXOTIC_SCROLLS+6;
    public static final int EXOTIC_MANNAZ   = EXOTIC_SCROLLS+7;
    public static final int EXOTIC_NAUDIZ   = EXOTIC_SCROLLS+8;
    public static final int EXOTIC_BERKANAN = EXOTIC_SCROLLS+9;
    public static final int EXOTIC_ODAL     = EXOTIC_SCROLLS+10;
    public static final int EXOTIC_TIWAZ    = EXOTIC_SCROLLS+11;
    static {
        for (int i = EXOTIC_SCROLLS; i < EXOTIC_SCROLLS+16; i++)
            assignItemRect(i, 15, 14);
    }

    private static final int STONES             =                           xy(1, 21);  //16 slots
    public static final int STONE_AGGRESSION    = STONES+0;
    public static final int STONE_AUGMENTATION  = STONES+1;
    public static final int STONE_FEAR          = STONES+2;
    public static final int STONE_BLAST         = STONES+3;
    public static final int STONE_BLINK         = STONES+4;
    public static final int STONE_CLAIRVOYANCE  = STONES+5;
    public static final int STONE_SLEEP         = STONES+6;
    public static final int STONE_DISARM        = STONES+7;
    public static final int STONE_ENCHANT       = STONES+8;
    public static final int STONE_FLOCK         = STONES+9;
    public static final int STONE_INTUITION     = STONES+10;
    public static final int STONE_SHOCK         = STONES+11;
    static {
        for (int i = STONES; i < STONES+16; i++)
            assignItemRect(i, 14, 12);
    }

    private static final int POTIONS        =                               xy(1, 22);  //16 slots
    public static final int POTION_CRIMSON  = POTIONS+0;
    public static final int POTION_AMBER    = POTIONS+1;
    public static final int POTION_GOLDEN   = POTIONS+2;
    public static final int POTION_JADE     = POTIONS+3;
    public static final int POTION_TURQUOISE= POTIONS+4;
    public static final int POTION_AZURE    = POTIONS+5;
    public static final int POTION_INDIGO   = POTIONS+6;
    public static final int POTION_MAGENTA  = POTIONS+7;
    public static final int POTION_BISTRE   = POTIONS+8;
    public static final int POTION_CHARCOAL = POTIONS+9;
    public static final int POTION_SILVER   = POTIONS+10;
    public static final int POTION_IVORY    = POTIONS+11;

    public static final int POTION_SKYBLUE  = POTIONS+12;
    public static final int POTION_DEEPYELLOW = POTIONS+13;
    public static final int POTION_CATALYST = POTIONS+14;
    public static final int LIQUID_METAL    = POTIONS+15;
    static {
        for (int i = POTIONS; i < POTIONS+16; i++)
            assignItemRect(i, 14, 15);
        assignItemRect(POTION_CATALYST, 13, 15);
        assignItemRect(LIQUID_METAL,    8, 14);


        for (int i = POTIONS+16; i < POTIONS+32; i++)
            assignItemRect(i, 12, 15);
    }

    private static final int EXOTIC_POTIONS =                               xy(1, 23);  //16 slots
    public static final int EXOTIC_CRIMSON  = EXOTIC_POTIONS+0;
    public static final int EXOTIC_AMBER    = EXOTIC_POTIONS+1;
    public static final int EXOTIC_GOLDEN   = EXOTIC_POTIONS+2;
    public static final int EXOTIC_JADE     = EXOTIC_POTIONS+3;
    public static final int EXOTIC_TURQUOISE= EXOTIC_POTIONS+4;
    public static final int EXOTIC_AZURE    = EXOTIC_POTIONS+5;
    public static final int EXOTIC_INDIGO   = EXOTIC_POTIONS+6;
    public static final int EXOTIC_MAGENTA  = EXOTIC_POTIONS+7;
    public static final int EXOTIC_BISTRE   = EXOTIC_POTIONS+8;
    public static final int EXOTIC_CHARCOAL = EXOTIC_POTIONS+9;
    public static final int EXOTIC_SILVER   = EXOTIC_POTIONS+10;
    public static final int EXOTIC_IVORY    = EXOTIC_POTIONS+11;
    public static final int EXOTIC_BLUEFIREX    = EXOTIC_POTIONS+12;
    static {
        for (int i = EXOTIC_POTIONS; i < EXOTIC_POTIONS+16; i++)
            assignItemRect(i, 14, 15);
    }

    private static final int SEEDS              =                           xy(1, 24);  //16 slots
    public static final int SEED_ROTBERRY       = SEEDS+0;
    public static final int SEED_FIREBLOOM      = SEEDS+1;
    public static final int SEED_SWIFTTHISTLE   = SEEDS+2;
    public static final int SEED_SUNGRASS       = SEEDS+3;
    public static final int SEED_ICECAP         = SEEDS+4;
    public static final int SEED_STORMVINE      = SEEDS+5;
    public static final int SEED_SORROWMOSS     = SEEDS+6;
    public static final int SEED_MAGEROYAL = SEEDS+7;
    public static final int SEED_EARTHROOT      = SEEDS+8;
    public static final int SEED_STARFLOWER     = SEEDS+9;
    public static final int SEED_FADELEAF       = SEEDS+10;
    public static final int SEED_BLINDWEED      = SEEDS+11;

    public static final int SEED_SKYBLUEFIRE      = SEEDS+12;
    public static final int SEED_AIKELAIER      = SEEDS+13;

    public static final int GREEN_LING      = SEEDS+15;

    public static final int STDR      = SEEDS+16;


    static{
        for (int i = SEEDS; i < SEEDS+16; i++)
            assignItemRect(i, 10, 10);
        assignItemRect(GREEN_LING, 12, 15);
        assignItemRect(STDR, 10, 13);
    }

    private static final int BREWS          =                               xy(1, 25);  //8 slots
    public static final int  BREW_INFERNAL   = BREWS+0;
    public static final int BREW_BLIZZARD   = BREWS+1;
    public static final int BREW_SHOCKING   = BREWS+2;
    public static final int BREW_CAUSTIC    = BREWS+3;
    public static final int	REDDRAGON				= BREWS+5;
    public static final int	ANTILIGHT				= BREWS+6;
    public static final int	WATERSOUL				= BREWS+7;

    private static final int ELIXIRS        =                               xy(9, 25);  //8 slots
    public static final int ELIXIR_HONEY    = ELIXIRS+0;
    public static final int ELIXIR_AQUA     = ELIXIRS+1;
    public static final int ELIXIR_MIGHT    = ELIXIRS+2;
    public static final int ELIXIR_DRAGON   = ELIXIRS+3;
    public static final int ELIXIR_TOXIC    = ELIXIRS+4;
    public static final int ELIXIR_ICY      = ELIXIRS+5;
    public static final int ELIXIR_ARCANE   = ELIXIRS+6;
    static{
        assignItemRect( ELIXIR_DRAGON,    16, 16);
        assignItemRect( ELIXIR_ICY,    9, 15);
        assignItemRect( WATERSOUL,    16, 16);
        assignItemRect( REDDRAGON,    12, 16);
    }

    //16 free slots

    private static final int SPELLS         =                               xy(1, 27);  //16 slots
    public static final int MAGIC_PORTER    = SPELLS+0;
    public static final int PHASE_SHIFT     = SPELLS+1;
    public static final int TELE_GRAB       = SPELLS+2;
    public static final int WILD_ENERGY     = SPELLS+3;
    public static final int RETURN_BEACON   = SPELLS+4;
    public static final int SUMMON_ELE      = SPELLS+5;

    public static final int AQUA_BLAST      = SPELLS+7;
    public static final int FEATHER_FALL    = SPELLS+8;
    public static final int RECLAIM_TRAP    = SPELLS+9;

    public static final int CURSE_INFUSE    = SPELLS+11;
    public static final int MAGIC_INFUSE    = SPELLS+12;
    public static final int ALCHEMIZE       = SPELLS+13;
    public static final int RECYCLE         = SPELLS+14;
    static{
        assignItemRect(MAGIC_PORTER,    12, 11);
        assignItemRect(PHASE_SHIFT,     12, 11);
        assignItemRect(TELE_GRAB,       12, 11);
        assignItemRect(WILD_ENERGY,      8, 16);
        assignItemRect(RETURN_BEACON,    8, 16);
        assignItemRect(SUMMON_ELE,       8, 16);

        assignItemRect(AQUA_BLAST,      11, 11);
        assignItemRect(FEATHER_FALL,    11, 11);
        assignItemRect(RECLAIM_TRAP,    11, 11);

        assignItemRect(CURSE_INFUSE,    10, 15);
        assignItemRect(MAGIC_INFUSE,    10, 15);
        assignItemRect(ALCHEMIZE,       10, 15);
        assignItemRect(RECYCLE,         10, 15);
    }

    private static final int FOOD       =                                   xy(1, 28);  //16 slots
    public static final int MEAT        = FOOD+0;
    public static final int STEAK       = FOOD+1;
    public static final int STEWED      = FOOD+2;
    public static final int OVERPRICED  = FOOD+3;
    public static final int CARPACCIO   = FOOD+4;
    public static final int RATION      = FOOD+5;
    public static final int PASTY       = FOOD+6;
    public static final int PUMPKIN_PIE = FOOD+7;
    public static final int CANDY_CANE  = FOOD+8;
    public static final int MEAT_PIE    = FOOD+9;
    public static final int BLANDFRUIT  = FOOD+10;
    public static final int BLAND_CHUNKS= FOOD+11;
    public static final int BERRY =       FOOD+12;

    //迁移
    public static final int LSPDA =       FOOD+13;
    public static final int CAKE =       FOOD+14;
    public static final int SWTR =       FOOD+15;

    static{
        assignItemRect(MEAT,        14, 13);
        assignItemRect(STEAK,       14, 13);
        assignItemRect(STEWED,      14, 12);
        assignItemRect(OVERPRICED,  14, 11);
        assignItemRect(CARPACCIO,   14, 13);
        assignItemRect(RATION,      16, 12);
        assignItemRect(PASTY,       16, 11);
        assignItemRect(PUMPKIN_PIE, 16, 12);
        assignItemRect(CANDY_CANE,  13, 16);
        assignItemRect(MEAT_PIE,    16, 12);
        assignItemRect(BLANDFRUIT,  9,  12);
        assignItemRect(BLAND_CHUNKS,14, 6);
        assignItemRect(BERRY,       9,  11);
    }

    private static final int QUEST  =                                       xy(1, 29);  //32 slots
    public static final int SKULL   = QUEST+0;
    public static final int DUST    = QUEST+1;
    public static final int CANDLE  = QUEST+2;
    public static final int EMBER   = QUEST+3;
    public static final int PICKAXE = QUEST+4;
    public static final int ORE     = QUEST+5;
    public static final int TOKEN   = QUEST+6;
    public static final int BLOB    = QUEST+7;
    public static final int SHARD   = QUEST+8;

    public static final int CrivusFruitFood   = QUEST+9;
    public static final int CrivusFruitflake   = QUEST+10;

    public static final int RiceDumplingsRed   = QUEST+11;
    public static final int RiceDumplingsPink   = QUEST+12;
    public static final int RiceDumplingsOrange   = QUEST+13;
    public static final int RiceDumplingsLink  = QUEST+14;
    public static final int RiceDumplingsBottle   = QUEST+15;

    public static final int Fish_A   = QUEST+16;
    public static final int Fish_B   = QUEST+17;

    public static final int QKA   = QUEST+19;
    public static final int QKB   = QUEST+20;

    public static final int Dragon_Lei   = QUEST+22;

    public static final int Qie_Cake   = QUEST+24;
    public static final int Rice_Cake   = QUEST+25;

    public static final int Flower_Cake = QUEST+26;

    static{
        assignItemRect(Dragon_Lei,   9, 14);

        assignItemRect(SKULL,   16, 11);
        assignItemRect(DUST,    12, 11);
        assignItemRect(CANDLE,  12, 12);
        assignItemRect(EMBER,   12, 11);
        assignItemRect(PICKAXE, 14, 14);
        assignItemRect(ORE,     15, 15);
        assignItemRect(TOKEN,   12, 12);
        assignItemRect(BLOB,    10,  9);
        assignItemRect(SHARD,    8, 10);

        assignItemRect(CrivusFruitFood,    11, 14);
        assignItemRect(CrivusFruitflake,    13, 13);

        assignItemRect(RiceDumplingsRed,    14, 14);
        assignItemRect(RiceDumplingsPink,    14, 14);
        assignItemRect(RiceDumplingsOrange,    14, 14);
        assignItemRect(RiceDumplingsLink,    14, 14);
        assignItemRect(RiceDumplingsBottle,    14, 14);

        assignItemRect(Fish_A,    16, 12);
        assignItemRect(Fish_B,    16, 12);
        assignItemRect(QKA,    16, 14);
        assignItemRect(QKB,    16, 14);

        assignItemRect(Qie_Cake,    14, 11);
        assignItemRect(Rice_Cake,    11, 9);
        assignItemRect(Flower_Cake,  16,15);
    }

    //传奇武器
    private static final int LENGYWEAPONS    	 = xy(1, 39);  //16 slots

    public static final int  DCSURANG=		LENGYWEAPONS+2;



    public static final int  CURSEDAXE=		LENGYWEAPONS+4;

    public static final int  EAGLEPOWER= 	LENGYWEAPONS+5;

    public static final int  DIEDBOOK= 	LENGYWEAPONS+10;



    public static final int  SHADOWMEAT= 	LENGYWEAPONS+12;

    public static final int  REDCRAB= 	LENGYWEAPONS+13;

    public static final int  DRAGONHEART= 	LENGYWEAPONS+14;

    static {
        assignItemRect(SHADOWMEAT,16,14);
    }

    private static final int BAGS       =                                   xy(1, 31);  //16 slots
    public static final int WATERSKIN        = BAGS+0;
    public static final int POUCH       = BAGS+1;
    public static final int HOLDER      = BAGS+2;
    public static final int BANDOLIER   = BAGS+3;
    public static final int HOLSTER     = BAGS+4;
    public static final int MagicBlueSword      = BAGS+5;

    public static final int	MOONCAKE				= BAGS+8;
    public static final int SWORD_BAG       = BAGS+12;

    public static final int BOOK_BAG       = BAGS+13;
    public static final int HERB_BAG = BAGS+14;
    public static final int PROP_BAG = BAGS+15;

    public static final int	DG13				= BAGS+20;

    public static final int SHOP_DIED           = BAGS+27;
    public static final int	DG21				= BAGS+28;

    public static final int	DRAGONKINGHALOFIRE				= BAGS+44;

    public static final int BLESS_SCROLL = BAGS+64;
    public static final int FLAME_SCROLL = BAGS+66;
    public static final int ROSE_SCROLL  = BAGS+65;
    public static final int	SKELETONGOLD = BAGS+67;



    //public static final int	ICEDVS				= BAGS+48;



    public static final int	BLACKBOOK		= BAGS+69;
    public static final int	LOSTBAG			= BAGS+70;
    public static final int	LANTERNA		= BAGS+71;
    public static final int	LANTERNB		= BAGS+72;
    public static final int	SKPOTION		= BAGS+73;
    public static final int	SKNANO			= BAGS+74;



    static{
        assignItemRect(WATERSKIN,        16, 16);
        assignItemRect(POUCH,       16, 16);
        assignItemRect(HOLDER,      16, 16);
        assignItemRect(BANDOLIER,   16, 16);
        assignItemRect(HOLSTER,     15, 16);
        assignItemRect(DRAGONKINGHALOFIRE, 12, 14);
        assignItemRect(ANTILIGHT, 15, 16);
        assignItemRect(LANTERNA, 10, 16);
        assignItemRect(LANTERNB, 10, 16);
        assignItemRect(SKPOTION, 9, 15);
        assignItemRect(SKNANO, 15, 14);
        assignItemRect(PROP_BAG, 12, 11);
        assignItemRect(SDBlade, 15, 15);

        assignItemRect(SKELETONGOLD, 16, 15);
    }

    private static final int MODE_ICONS    	 = xy(1, 36);  //16 slots
    public static final int LASERPYTHON=		MODE_ICONS+0;
    public static final int DLCBOOKS=		    MODE_ICONS+1;
    public static final int DIFFCULTBOOT=		MODE_ICONS+2;
    public static final int STORYBOOKS=		    MODE_ICONS+3;
    public static final int HLPBOOKS=		    MODE_ICONS+4;
    public static final int PROPBOOKS=		    MODE_ICONS+5;

    private static final int MAINBOOKINDEX    	 = xy(18, 18);  //16 slots
    public static final int ICEBOOK=			MAINBOOKINDEX;
    public static final int BREDBOOK=			MAINBOOKINDEX+1;
    public static final int GREENBOOKS=			MAINBOOKINDEX+2;
    public static final int YELLOWBOOKS=		MAINBOOKINDEX+3;
    public static final int MOBBOOKS=			MAINBOOKINDEX+4;
    public static final int MAGICGIRLBOOKS=		MAINBOOKINDEX+5;

    public static final int NOKING=		MAINBOOKINDEX+6;
    public static final int FIRELIYD=		MAINBOOKINDEX+7;
    public static final int FBK=		MAINBOOKINDEX+8;
    public static final int SOYBOOKS=		MAINBOOKINDEX+9;
    public static final int FLOWERY=		MAINBOOKINDEX+10;


    static {
        for (int i = MAINBOOKINDEX; i < MAINBOOKINDEX+10; i++)
            assignItemRect(i, 13, 16);

        assignItemRect(FLOWERY, 12, 16);
    }


    private static final int EXFOODINDEX    	 = xy(1, 38);  //16 slots
    public static final int  FISHSKELETON=		EXFOODINDEX+0;
    public static final int  FISHBONE=			EXFOODINDEX+1;

    public static final int  BLACKMOON=			EXFOODINDEX+2;
    static {
        assignItemRect(FISHSKELETON,16,16);
        assignItemRect(BLACKMOON, 15, 12);
    }

    private static final int MAINPALYBOOKS     = xy(1, 40);
    public static final int MONEYBOOKS=		MAINPALYBOOKS+1;
    public static final int PINKBOOKS=		MAINPALYBOOKS+2;
    public static final int DEBOOKS=		MAINPALYBOOKS+3;

    public static final int LINGPEA=		MAINPALYBOOKS+5;
    public static final int DARKCRYSTAL=		MAINPALYBOOKS+6;
    public static final int HITLCRYSTAL=		MAINPALYBOOKS+7;



    public static final int SMTITEM=		MAINPALYBOOKS+9;

    public static final int BOOKSQINYUE=		MAINPALYBOOKS+11;

    static {
        assignItemRect(LINGPEA,13,15);
        assignItemRect(DARKCRYSTAL,9,9);
        assignItemRect(HITLCRYSTAL,9,9);
        assignItemRect(BOOKSQINYUE,12,14);
    }
    //16 free slots                                                  //16 free slots

    private static final int CHALLANEESICONINDEX   	 = xy(1, 42);  //16 slots

    public static final int CHALLANEESICON_1=			 CHALLANEESICONINDEX+0;
    public static final int CHALLANEESICON_2=			 CHALLANEESICONINDEX+1;
    public static final int CHALLANEESICON_3=			 CHALLANEESICONINDEX+2;
    public static final int CHALLANEESICON_4=			 CHALLANEESICONINDEX+3;
    public static final int CHALLANEESICON_5=			 CHALLANEESICONINDEX+4;
    public static final int CHALLANEESICON_6=			 CHALLANEESICONINDEX+5;

    public static final int CHALLANEESICON_7=			 CHALLANEESICONINDEX+6;
    public static final int CHALLANEESICON_8=			 CHALLANEESICONINDEX+7;
    public static final int CHALLANEESICON_9=			 CHALLANEESICONINDEX+8;
    public static final int CHALLANEESICON_10=			 CHALLANEESICONINDEX+9;
    public static final int CHALLANEESICON_11=			 CHALLANEESICONINDEX+10;
    public static final int CHALLANEESICON_12=			 CHALLANEESICONINDEX+11;
    public static final int CHALLANEESICON_13=			 CHALLANEESICONINDEX+12;

    public static final int CHALLANEESICON_14=			 CHALLANEESICONINDEX+13;

    public static final int CHALLANEESICON_15=			 CHALLANEESICONINDEX+14;

    public static final int CHALLANEESICON_16=			 CHALLANEESICONINDEX+15;

    public static final int CHALLANEESICON_17=			 CHALLANEESICONINDEX+16;
    public static final int SCROLL_GOLD   	 		= xy(1, 46);  //16 slots
    public static final int  RANDOM_CHEST			= SCROLL_GOLD+1;
    public static final int  CITY_HOOD			= SCROLL_GOLD+2;
    public static final int  BOSSRUSH_GOLD			= SCROLL_GOLD+4;

    public static final int  BZMDR_BOOKS			= SCROLL_GOLD+5;

    public static final int  DARK_X		= SCROLL_GOLD+6;

    private static final int HOLLOW_INDEX   	 = xy(1, 44);

    public static final int CHOCOLATE = HOLLOW_INDEX;
    public static final int BOMB_PACMAN = HOLLOW_INDEX + 1;
    public static final int LOLLIPOP = HOLLOW_INDEX + 2;
    public static final int GUMDROP = HOLLOW_INDEX + 3;

    public static final int GELATIN =			 HOLLOW_INDEX + 4;
    public static final int SUGAR_BLOCK =		 HOLLOW_INDEX + 5;
    public static final int WHITE_SUGAR_B =		 HOLLOW_INDEX + 6;

    public static final int SOUL_CRACK =			 HOLLOW_INDEX + 10;
    public static final int TOFFEE =			 HOLLOW_INDEX + 15;
    public static final int HOLY_WATER =			 HOLLOW_INDEX + 12;

    static {
        assignItemRect(CHOCOLATE,  14, 14);
        assignItemRect(BOMB_PACMAN,12, 15);
        assignItemRect(LOLLIPOP,   13, 14);
        assignItemRect(GUMDROP,    14, 12);
        assignItemRect(TOFFEE,      13,15);

        assignItemRect(HOLY_WATER, 11, 15);
    }

    static {
        assignItemRect(SCROLL_GOLD, 15, 14);
        assignItemRect(BOSSRUSH_GOLD, 16, 12);
    }


    public static final int PUMPKM_LANTERN  =                                   xy(1, 45);

    public static final int CASTLE_AIRPORT      = PUMPKM_LANTERN+1;
    public static final int GOLD_CARDS          = PUMPKM_LANTERN+2;
    public static final int CRYSTAL_CHOCO       = PUMPKM_LANTERN+3;
    public static final int WORLD_HEART_MODEL   = PUMPKM_LANTERN+4;
    public static final int GHOST_BLUE_MODEL    = PUMPKM_LANTERN+5;
    public static final int GREEN_DAM_MODEL     = PUMPKM_LANTERN+6;
    public static final int GREEN_SLING_SMALL   = PUMPKM_LANTERN+7;
    public static final int THEATER_CARDS       = PUMPKM_LANTERN+8;
    public static final int HOLLOW_SUGAR        = PUMPKM_LANTERN+9;
    public static final int GREEN_PRISM         = PUMPKM_LANTERN+10;
    public static final int GNOLL_WOOD          = PUMPKM_LANTERN+11;
    public static final int FOUR_KIDS           = PUMPKM_LANTERN+12;

    public static final int SMALL_POINT     = PUMPKM_LANTERN+13;
    public static final int BIG_POINT       = PUMPKM_LANTERN+14;

    public static final int UNLESSFLOWER       = PUMPKM_LANTERN+15;

    static {
        assignItemRect(PUMPKM_LANTERN, 14, 13);
        assignItemRect(CASTLE_AIRPORT, 16, 16);
        assignItemRect(GOLD_CARDS,     12, 16);

        assignItemRect(CRYSTAL_CHOCO,  13, 13);
        assignItemRect(WORLD_HEART_MODEL,  14, 13);
        assignItemRect(GHOST_BLUE_MODEL,  13, 13);
        assignItemRect(GREEN_DAM_MODEL,  15, 15);
        assignItemRect(GREEN_SLING_SMALL,  12, 12);

        assignItemRect(THEATER_CARDS,  14, 9);
        assignItemRect(HOLLOW_SUGAR,  12, 10);
        assignItemRect(GREEN_PRISM,  9, 15);
        assignItemRect(GNOLL_WOOD,  11, 14);
        assignItemRect(FOUR_KIDS,  14, 14);

        assignItemRect(SMALL_POINT, 6, 6);
        assignItemRect(BIG_POINT,  10, 10);

        assignItemRect(UNLESSFLOWER, 16,13);
    }


    private static final int DOCUMENTS  =                                   xy(1, 48);  //16 slots
    public static final int GUIDE_PAGE  = DOCUMENTS+0;
    public static final int ALCH_PAGE   = DOCUMENTS+1;
    public static final int SEWER_PAGE  = DOCUMENTS+2;
    public static final int PRISON_PAGE = DOCUMENTS+3;
    public static final int CAVES_PAGE  = DOCUMENTS+4;
    public static final int CITY_PAGE   = DOCUMENTS+5;
    public static final int HALLS_PAGE  = DOCUMENTS+6;
    static{
        assignItemRect(GUIDE_PAGE,  10, 11);
        assignItemRect(ALCH_PAGE,   10, 11);
        assignItemRect(SEWER_PAGE,  10, 11);
        assignItemRect(PRISON_PAGE, 10, 11);
        assignItemRect(CAVES_PAGE,  10, 11);
        assignItemRect(CITY_PAGE,   10, 11);
        assignItemRect(HALLS_PAGE,  10, 11);
    }

    private static final int HIGHTWAND  =                                   xy(1, 50);  //16 slots
    public static final int HIGHTWAND_1  = + HIGHTWAND+0;
    public static final int HIGHTWAND_2  = + HIGHTWAND+1;
    public static final int HIGHTWAND_3  = + HIGHTWAND+2;
    public static final int HIGHTWAND_4  = + HIGHTWAND+3;
    public static final int HIGHTWAND_5  = + HIGHTWAND+4;

    public static final int HIGHTWAND_6  = + HIGHTWAND+5;
    public static final int HIGHTWAND_7  = + HIGHTWAND+6;

    public static final int DEV_1        = + HIGHTWAND+8;
    public static final int DEV_2        = + HIGHTWAND+9;
    public static final int DEV_3        = + HIGHTWAND+10;
    public static final int DEV_4        = + HIGHTWAND+11;
    public static final int DEV_5        = + HIGHTWAND+12;
    public static final int DEV_6        = + HIGHTWAND+13;
    public static final int DEV_7        = + HIGHTWAND+14;
    public static final int DEV_8        = + HIGHTWAND+15;

    static {
        for (int i =HIGHTWAND; i < HIGHTWAND+6; i++)
            assignItemRect(i, 14, 14);

        assignItemRect(DEV_1,  14, 16);
        assignItemRect(DEV_2,  16, 15);
        assignItemRect(DEV_3,  11, 16);
        assignItemRect(DEV_4,  14, 15);
        assignItemRect(DEV_5,  14, 16);
        assignItemRect(DEV_6,  15, 16);
        assignItemRect(DEV_7,  14, 16);
        assignItemRect(DEV_8,  16, 14);
    }

    private static final int SKIN  =                                   xy(1, 52);  //16 slots
    public static final int SKIN_1  = + SKIN+0;
    public static final int SKIN_2  = + SKIN+1;
    public static final int SKIN_3  = + SKIN+2;
    public static final int SKIN_4  = + SKIN+3;
    public static final int SKIN_5  = + SKIN+4;
    public static final int SKIN_6  = + SKIN+5;
    public static final int SKIN_7  = + SKIN+6;
    public static final int SKIN_8  = + SKIN+7;
    public static final int SKIN_9  = + SKIN+8;
    public static final int SKIN_10  = + SKIN+9;

    public static final int SKIN_11  = + SKIN+10;

    public static final int SKIN_12  = + SKIN+11;
    public static final int SKIN_13  = + SKIN+12;
    public static final int SKIN_14  = + SKIN+13;
    public static final int RGJT_4  = + SKIN+14;

    static {
        for (int i =SKIN; i < SKIN+10; i++)
            assignItemRect(i, 16, 16);

        //assignItemRect(RGJT_1, 13, 16);
    }

    private static final int TRINKETS        =                               xy(1, 54);  //24 slots
    public static final int RAT_SKULL       = TRINKETS+0;
    public static final int PARCHMENT_SCRAP = TRINKETS+1;
    public static final int PETRIFIED_SEED  = TRINKETS+2;
    public static final int EXOTIC_CRYSTALS = TRINKETS+3;
    public static final int MOSSY_CLUMP     = TRINKETS+4;
    public static final int SUNDIAL         = TRINKETS+5;
    public static final int CLOVER          = TRINKETS+6;
    public static final int TRAP_MECHANISM  = TRINKETS+7;
    public static final int MIMIC_TOOTH     = TRINKETS+8;
    public static final int WONDROUS_RESIN  = TRINKETS+9;
    public static final int EYE_OF_NEWT     = TRINKETS+10;
    public static final int TRINT_PLOCHR    = TRINKETS+15;

    public static final int SALT_CUBE       = TRINKETS+11;
    public static final int BLOOD_VIAL      = TRINKETS+12;
    public static final int OBLIVION_SHARD  = TRINKETS+13;
    public static final int CHAOTIC_CENSER  = TRINKETS+14;

    public static final int SPYGLASS        = TRINKETS+16;
    public static final int FERRET_TUFT     = TRINKETS+17;

    static{
        assignItemRect(RAT_SKULL,       15, 11);
        assignItemRect(PARCHMENT_SCRAP, 15, 14);
        assignItemRect(PETRIFIED_SEED,  15, 14);
        assignItemRect(EXOTIC_CRYSTALS, 13, 9);
        assignItemRect(MOSSY_CLUMP,     15, 14);
        assignItemRect(SUNDIAL,         16, 12);
        assignItemRect(CLOVER,          15, 14);
        assignItemRect(TRAP_MECHANISM,  15, 15);
        assignItemRect(MIMIC_TOOTH,     14,  15);
        assignItemRect(WONDROUS_RESIN,  15, 14);
        assignItemRect(EYE_OF_NEWT,     14, 13);
        assignItemRect(TRINT_PLOCHR,     16, 11);

        assignItemRect(SALT_CUBE,     15, 12);
        assignItemRect(BLOOD_VIAL,     13, 11);
        assignItemRect(OBLIVION_SHARD,     14, 14);
        assignItemRect(CHAOTIC_CENSER,     16, 15);

        assignItemRect(SPYGLASS, 16,11);
        assignItemRect(FERRET_TUFT, 14,16);
    }

    private static final int MISC        =                               xy(1, 56);  //24 slots
    public static final int MISC_ROLLBLOCK        = MISC+1;
    public static final int MISC_WATERBOMB        = MISC+3;
    public static final int MISC_ROLLPOTION       = MISC+4;
    public static final int MISC_EXFALLING        = MISC+5;

    public static final int RUIKE        = MISC+6;

    public static final int SOS_0        = MISC+8;
    public static final int SOS_1        = MISC+9;
    public static final int SOS_2       = MISC+10;
    public static final int SOS_3        = MISC+11;

    public static final int FLARE        = MISC+15;

    static{
        assignItemRect(MISC_ROLLBLOCK,       12, 13);
        assignItemRect(MISC_WATERBOMB,       9, 13);
        assignItemRect(MISC_ROLLPOTION,       12, 14);
        assignItemRect(MISC_EXFALLING,       14, 16);

        assignItemRect(SOS_0,       15, 16);
        assignItemRect(SOS_1,       15, 16);
        assignItemRect(SOS_2,       15, 16);
        assignItemRect(SOS_3,       15, 16);

        assignItemRect(FLARE,       14, 15);
    }

    private static final int PROJECTILES        =                               xy(1, 58);  //24 slots

    public static final int PROJECTILES_STAR        = PROJECTILES;

    public static final int SOUL_CRACK_A        = PROJECTILES + 1;
    public static final int SOUL_CRACK_B        = PROJECTILES + 2;
    public static final int SOUL_CRACK_C        = PROJECTILES + 3;
    public static final int SOUL_CRACK_D        = PROJECTILES + 4;
    public static final int SOUL_CRACK_E        = PROJECTILES + 5;

    public static final int STAR_CRYSTAL       = PROJECTILES + 6;

    public static final int SCROLL_GOLEM        = PROJECTILES + 7;


    public static final int BONESOUP            = PROJECTILES + 8;
    public static final int RATTAIL            = PROJECTILES + 9;
    public static final int ZAKOSOUP        = PROJECTILES + 10;

    public static final int LINGBAG  = PROJECTILES + 11;

    public static final int SZJ_REBACK  = PROJECTILES + 13;

    public static final int MAGNETIC_CROWN  = PROJECTILES + 15;


    static{
        assignItemRect(PROJECTILES_STAR,       9, 9);
        assignItemRect(SCROLL_GOLEM,       15, 14);

        assignItemRect(BONESOUP,         14, 13);
        assignItemRect(RATTAIL,          11, 13);
        assignItemRect(ZAKOSOUP,         15, 12);

        assignItemRect(SZJ_REBACK,            15, 15);

        assignItemRect(STAR_CRYSTAL,                    9,16);

        assignItemRect(MAGNETIC_CROWN,            14, 13);


    }

    public static final int RITUAL_SWORD        =                               xy(1, 60);

    public static final int TREE_LIST                                           = RITUAL_SWORD + 1;

    public static final int OLDSUNSHADOW                                        = RITUAL_SWORD + 8;

    public static final int FLOWERS                                             = RITUAL_SWORD + 9;

    public static final int LAN_FIRE_ROAD                                       = RITUAL_SWORD + 11;

    public static final int BZMDR_GIFT                                       = RITUAL_SWORD + 12;

    public static final int MOTO_BREW                                       = RITUAL_SWORD + 13;

    public static final int SOUL_SCROLL                                     = RITUAL_SWORD + 15;

    static{
        assignItemRect(RITUAL_SWORD,       13, 13);
        assignItemRect(TREE_LIST,          14, 13);

        assignItemRect(OLDSUNSHADOW,      14, 14);
        assignItemRect(LAN_FIRE_ROAD,      14,16);

        assignItemRect(BZMDR_GIFT,          12,15);
        assignItemRect(MOTO_BREW,          12,16);

        assignItemRect(SOUL_SCROLL,          16,14);
    }

    private static final int PROP1  = xy(9,46);
    private static final int PROP2  = xy(9,47);
    private static final int PROP3  = xy(9,48);
    private static final int PROP4  = xy(9,49);

    public static final int STARSACHET = PROP1+0;
    public static final int RAPIDEARTHROOT = PROP1+1;
    public static final int PORTABLEWHETSTONE = PROP1+2;
    public static final int NEWSTEM = PROP1+3;
    public static final int LUCKYGLOVE = PROP1+4;
    public static final int EMOTIONALAGGREGATION = PROP1+5;
    public static final int MONOCULAR = PROP1+6;
    public static final int DELICIOUSRECIPE = PROP1+7;
    public static final int KINGHTSTABBINGSWORD = PROP2+0;
    public static final int WENSTUDYINGPAPERONE = PROP2+1;
    public static final int YANSTUDYINGPAPERTWO = PROP2+2;
    public static final int ARMORSCALESOFBZMDR  = PROP2+3;
    public static final int BROKENBONE = PROP2+4;
    public static final int RUSTEDGOLDCOIN = PROP2+5;
    public static final int CONfUSEDMIEMIETALISMAN = PROP2+6;
    public static final int THEGRIEFOFSPEECHLESS = PROP2+7;
    public static final int BOTTLEDSPIRITS = PROP3+0;
    public static final int BLOCKINGDRUG = PROP3+1;
    public static final int TERRORDOLL = PROP3+2;
    public static final int TERRORDOLLB = PROP4+1;
    public static final int CLOAKFRAGMENTSOFBZMDR = PROP3+3;
    public static final int EMOTIONALAGGREGATIONB = PROP3+4;
    public static final int HEARTOFCRYSTALFRACTAL = PROP3+5;
    public static final int WENSTUDYINGPAPERTWO = PROP3+6;
    public static final int YANSTUDYINGPAPERONE = PROP3+7;
    public static final int NOTEOFBZMDR = PROP4+0;

    public static final int PROP5        =                               xy(1, 62);

    public static final int KILL_EYES           = PROP5+0;
    public static final int PURE_ROUGE          = PROP5+1;
    public static final int HELL_BUTTERFLY      = PROP5+2;
    public static final int FAINT_GLIMMER       = PROP5+3;
    public static final int DIRT_KNIFE_STAND    = PROP5+4;
    public static final int BROKEN_RING         = PROP5+5;
    public static final int STAR_DUST           = PROP5+6;
    public static final int SPACE_DEBRIS        = PROP5+7;
    public static final int RESOLVE_DIE         = PROP5+8;
    public static final int CATGIRL_COSPLAY     = PROP5+9;
    public static final int DREAM_SEED          = PROP5+10;

    static{
        assignItemRect(KILL_EYES,               14, 14);
        assignItemRect(PURE_ROUGE,              16, 10);
        assignItemRect(DIRT_KNIFE_STAND,        13, 13);
        assignItemRect(BROKEN_RING,             13, 13);
        assignItemRect(STAR_DUST,                 9, 9);
        assignItemRect(RESOLVE_DIE,              9, 13);
        assignItemRect(SPACE_DEBRIS,            11, 14);
        assignItemRect(CATGIRL_COSPLAY,         13, 14);
        assignItemRect(DREAM_SEED,              10, 10);
    }

    public static final int SPELLSWORD_ITEM       =                               xy(1, 64);

    public static final int IMPRIA_EXORCISM             = SPELLSWORD_ITEM;
    public static final int MAGIC_FLY_BLADE           = SPELLSWORD_ITEM+4;

    static
    {
        assignItemRect(MAGIC_FLY_BLADE,               11,11);
    }

    public static final int WEAPON_ITEM_T2       =                               xy(10, 64);

    public static final int SHOPKEEPERSWORD             = WEAPON_ITEM_T2;

    static
    {
        assignItemRect(SHOPKEEPERSWORD,               14,16);
    }


    //for smaller 8x8 icons that often accompany an item sprite
    public static class Icons {

        private static final int WIDTH = 16;
        public static final int SIZE = 8;

        public static TextureFilm film = new TextureFilm( Assets.Sprites.ITEM_ICONS, SIZE, SIZE );

        private static int xy(int x, int y){
            x -= 1; y -= 1;
            return x + WIDTH*y;
        }

        private static void assignIconRect( int item, int width, int height ){
            int x = (item % WIDTH) * SIZE;
            int y = (item / WIDTH) * SIZE;
            film.add( item, x, y, x+width, y+height);
        }

        private static final int RINGS          =                            xy(1, 1);  //16 slots
        public static final int RING_ACCURACY   = RINGS+0;
        public static final int RING_ARCANA     = RINGS+1;
        public static final int RING_ELEMENTS   = RINGS+2;
        public static final int RING_ENERGY     = RINGS+3;
        public static final int RING_EVASION    = RINGS+4;
        public static final int RING_FORCE      = RINGS+5;
        public static final int RING_FUROR      = RINGS+6;
        public static final int RING_HASTE      = RINGS+7;
        public static final int RING_MIGHT      = RINGS+8;
        public static final int RING_SHARPSHOOT = RINGS+9;
        public static final int RING_TENACITY   = RINGS+10;
        public static final int RING_WEALTH     = RINGS+11;
        public static final int RING_RETEACT    = RINGS+12;
        public static final int RING_CLASACT     = RINGS+13;

        static {
            assignIconRect( RING_ACCURACY,      7, 7 );
            assignIconRect( RING_ARCANA,        7, 7 );
            assignIconRect( RING_ELEMENTS,      7, 7 );
            assignIconRect( RING_ENERGY,        7, 5 );
            assignIconRect( RING_EVASION,       7, 7 );
            assignIconRect( RING_FORCE,         5, 6 );
            assignIconRect( RING_FUROR,         7, 6 );
            assignIconRect( RING_HASTE,         6, 6 );
            assignIconRect( RING_MIGHT,         7, 7 );
            assignIconRect( RING_SHARPSHOOT,    7, 7 );
            assignIconRect( RING_TENACITY,      6, 6 );
            assignIconRect( RING_WEALTH,        7, 6 );

            assignIconRect( RING_RETEACT,         7, 7 );
            assignIconRect( RING_CLASACT,         7, 7 );
        }

        //16 free slots

        private static final int SCROLLS        =                            xy(1, 3);  //16 slots
        public static final int SCROLL_UPGRADE  = SCROLLS+0;
        public static final int SCROLL_IDENTIFY = SCROLLS+1;
        public static final int SCROLL_REMCURSE = SCROLLS+2;
        public static final int SCROLL_MIRRORIMG= SCROLLS+3;
        public static final int SCROLL_RECHARGE = SCROLLS+4;
        public static final int SCROLL_TELEPORT = SCROLLS+5;
        public static final int SCROLL_LULLABY  = SCROLLS+6;
        public static final int SCROLL_MAGICMAP = SCROLLS+7;
        public static final int SCROLL_RAGE     = SCROLLS+8;
        public static final int SCROLL_RETRIB   = SCROLLS+9;
        public static final int SCROLL_TERROR   = SCROLLS+10;
        public static final int SCROLL_TRANSMUTE= SCROLLS+11;
        static {
            assignIconRect( SCROLL_UPGRADE,     7, 7 );
            assignIconRect( SCROLL_IDENTIFY,    4, 7 );
            assignIconRect( SCROLL_REMCURSE,    7, 7 );
            assignIconRect( SCROLL_MIRRORIMG,   7, 5 );
            assignIconRect( SCROLL_RECHARGE,    7, 5 );
            assignIconRect( SCROLL_TELEPORT,    7, 7 );
            assignIconRect( SCROLL_LULLABY,     7, 6 );
            assignIconRect( SCROLL_MAGICMAP,    7, 7 );
            assignIconRect( SCROLL_RAGE,        6, 6 );
            assignIconRect( SCROLL_RETRIB,      5, 6 );
            assignIconRect( SCROLL_TERROR,      5, 7 );
            assignIconRect( SCROLL_TRANSMUTE,   7, 7 );
        }

        private static final int EXOTIC_SCROLLS =                            xy(1, 4);  //16 slots
        public static final int SCROLL_ENCHANT  = EXOTIC_SCROLLS+0;
        public static final int SCROLL_DIVINATE = EXOTIC_SCROLLS+1;
        public static final int SCROLL_ANTIMAGIC= EXOTIC_SCROLLS+2;
        public static final int SCROLL_PRISIMG  = EXOTIC_SCROLLS+3;
        public static final int SCROLL_MYSTENRG = EXOTIC_SCROLLS+4;
        public static final int SCROLL_PASSAGE  = EXOTIC_SCROLLS+5;
        public static final int SCROLL_AFFECTION= EXOTIC_SCROLLS+6;
        public static final int SCROLL_FORESIGHT= EXOTIC_SCROLLS+7;
        public static final int SCROLL_CONFUSION= EXOTIC_SCROLLS+8;

        public static final int SCROLL_PSIBLAST = EXOTIC_SCROLLS+9;
        public static final int SCROLL_CHALLENGE= EXOTIC_SCROLLS+8;

        public static final int SCROLL_SHEEP= EXOTIC_SCROLLS+11;

        public static final int SCROLL_SIREN    = EXOTIC_SCROLLS+12;
        public static final int SCROLL_STONE   = EXOTIC_SCROLLS+13;
        public static final int SCROLL_DREAD	= EXOTIC_SCROLLS+14;
        public static final int SCROLL_METAMORPH	= EXOTIC_SCROLLS+15;

        static {
            assignIconRect( SCROLL_ENCHANT,     7, 7 );
            assignIconRect( SCROLL_DIVINATE,    7, 6 );
            assignIconRect( SCROLL_ANTIMAGIC,   7, 7 );
            assignIconRect( SCROLL_PRISIMG,     5, 7 );
            assignIconRect( SCROLL_MYSTENRG,    7, 5 );
            assignIconRect( SCROLL_PASSAGE,     5, 7 );
            assignIconRect( SCROLL_AFFECTION,   7, 6 );
            assignIconRect( SCROLL_FORESIGHT,   7, 5 );
            assignIconRect( SCROLL_CONFUSION,   7, 7 );
            assignIconRect( SCROLL_PSIBLAST,    5, 6 );
            assignIconRect( SCROLL_STONE,      7, 8 );
            assignIconRect( SCROLL_SHEEP,   7, 8 );
            assignIconRect( SCROLL_CHALLENGE,   7, 7 );
            assignIconRect( SCROLL_SIREN,       7, 6 );
            assignIconRect( SCROLL_DREAD,       8, 8 );
            assignIconRect( SCROLL_METAMORPH,       8, 8 );
        }

        //16 free slots

        private static final int POTIONS        =                            xy(1, 6);  //16 slots
        public static final int POTION_STRENGTH = POTIONS+0;
        public static final int POTION_HEALING  = POTIONS+1;
        public static final int POTION_MINDVIS  = POTIONS+2;
        public static final int POTION_FROST    = POTIONS+3;
        public static final int POTION_LIQFLAME = POTIONS+4;
        public static final int POTION_TOXICGAS = POTIONS+5;
        public static final int POTION_HASTE    = POTIONS+6;
        public static final int POTION_INVIS    = POTIONS+7;
        public static final int POTION_LEVITATE = POTIONS+8;
        public static final int POTION_PARAGAS  = POTIONS+9;
        public static final int POTION_PURITY   = POTIONS+10;
        public static final int POTION_EXP      = POTIONS+11;


        public static final int POTION_BLUE     = POTIONS+12;
        public static final int POTION_DEPS     = POTIONS+13;

        static {
            assignIconRect( POTION_STRENGTH,    7, 7 );
            assignIconRect( POTION_HEALING,     6, 7 );
            assignIconRect( POTION_MINDVIS,     7, 5 );
            assignIconRect( POTION_FROST,       7, 7 );
            assignIconRect( POTION_LIQFLAME,    5, 7 );
            assignIconRect( POTION_TOXICGAS,    7, 7 );
            assignIconRect( POTION_HASTE,       6, 6 );
            assignIconRect( POTION_INVIS,       5, 7 );
            assignIconRect( POTION_LEVITATE,    6, 7 );
            assignIconRect( POTION_PARAGAS,     7, 7 );
            assignIconRect( POTION_PURITY,      5, 7 );
            assignIconRect( POTION_EXP,         7, 7 );

            assignIconRect( POTION_BLUE,         7, 7 );
            assignIconRect( POTION_DEPS,         7, 7 );
        }

        private static final int EXOTIC_POTIONS =                            xy(1, 7);  //16 slots
        public static final int POTION_MASTERY  = EXOTIC_POTIONS+0;
        public static final int POTION_SHIELDING= EXOTIC_POTIONS+1;
        public static final int POTION_MAGISIGHT= EXOTIC_POTIONS+2;
        public static final int POTION_SNAPFREEZ= EXOTIC_POTIONS+3;
        public static final int POTION_DRGBREATH= EXOTIC_POTIONS+4;
        public static final int POTION_CORROGAS = EXOTIC_POTIONS+5;
        public static final int POTION_STAMINA  = EXOTIC_POTIONS+6;
        public static final int POTION_SHROUDFOG= EXOTIC_POTIONS+7;
        public static final int POTION_STRMCLOUD= EXOTIC_POTIONS+8;
        public static final int POTION_EARTHARMR= EXOTIC_POTIONS+9;
        public static final int POTION_CLEANSE  = EXOTIC_POTIONS+10;
        public static final int POTION_DIVINE   = EXOTIC_POTIONS+11;

        public static final int POTION_DRAGONKING= EXOTIC_POTIONS+12;
        public static final int POTION_SRTDIED= EXOTIC_POTIONS+13;

        public static final int SCROLL_GOLEM	= EXOTIC_POTIONS+14;

        static {
            assignIconRect( POTION_MASTERY,     7, 7 );
            assignIconRect( POTION_SHIELDING,   6, 6 );
            assignIconRect( POTION_MAGISIGHT,   7, 5 );
            assignIconRect( POTION_SNAPFREEZ,   7, 7 );
            assignIconRect( POTION_DRGBREATH,   7, 7 );
            assignIconRect( POTION_CORROGAS,    7, 7 );
            assignIconRect( POTION_STAMINA,     6, 6 );
            assignIconRect( POTION_SHROUDFOG,   7, 7 );
            assignIconRect( POTION_STRMCLOUD,   7, 7 );
            assignIconRect( POTION_EARTHARMR,   6, 6 );
            assignIconRect( POTION_CLEANSE,     7, 7 );
            assignIconRect( POTION_DIVINE,      7, 7 );
            assignIconRect( POTION_DRAGONKING,   7, 7 );
            assignIconRect( POTION_SRTDIED,   6, 7 );

            assignIconRect( SCROLL_GOLEM,   8, 8 );
        }

        private static final int CPTION_NOSTR =                            xy(1, 9);  //16 slots
        public static final int CPOTION_NOSTR   = CPTION_NOSTR;
        public static final int CPOTION_NOLEG   = CPTION_NOSTR+1;
        public static final int CPOTION_NODIED  = CPTION_NOSTR+2;
        public static final int CPOTION_NOEYE   = CPTION_NOSTR+3;

        static {
            assignIconRect( CPTION_NOSTR,     9, 9 );
            assignIconRect( CPOTION_NOLEG,     9, 9 );
            assignIconRect( CPOTION_NODIED,     9, 9 );
            assignIconRect( CPOTION_NOEYE,     9, 9 );

        }

        //16 free slots



    }

}