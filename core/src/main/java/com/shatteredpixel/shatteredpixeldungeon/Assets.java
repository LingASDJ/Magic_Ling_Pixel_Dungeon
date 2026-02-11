/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2024 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon;

public class Assets {

	public static class Effects {
		public static final String EFFECTS      = "effects/effects.png";
		public static final String FIREBALL     = "effects/fireball.png";
		public static final String SPECKS       = "effects/specks.png";
		public static final String SPELL_ICONS  = "effects/spell_icons.png";
		public static final String SHAKCO       = "effects/shockwave.png";
		public static final String TEXT_ICONS 	= "effects/text_icons.png";
	}

	public static class Map_Luas {
		public static final String LockedOneRoom_MapLua      = "lua/iron/iron_key1.lua";
		public static final String LockedTwoRoom_MapLua      = "lua/iron/iron_key2.lua";
		public static final String LockedFourRoom_MapLua      = "lua/iron/iron_key4.lua";
		public static final String LockedFiveRoom_MapLua      = "lua/iron/iron_key5.lua";
		public static final String LockedSevenRoom_MapLua      = "lua/iron/iron_key7.lua";

		public static final String GoldOneRoom_MapLua       = "lua/gold/gold_key1.lua";
		public static final String GoldTwoRoom_MapLua       = "lua/gold/gold_key2.lua";
		public static final String GoldThreeRoom_MapLua     = "lua/gold/gold_key3.lua";
		public static final String GoldFourRoom_MapLua      = "lua/gold/gold_key4.lua";
		public static final String GoldFiveRoom_MapLua      = "lua/gold/gold_key5.lua";

		public static final String CryStalOneRoom_MapLua      = "lua/crystal/crystal_key1.lua";
		public static final String CryStalTwoRoom_MapLua      = "lua/crystal/crystal_key2.lua";
		public static final String CryStalThreeRoom_MapLua      = "lua/crystal/crystal_key3.lua";

		public static final String GuardOneRoom_MapLua        = "lua/guard/guard_room1.lua";
		public static final String GuardTwoRoom_MapLua        = "lua/guard/guard_room2.lua";
		public static final String GuardThreeRoom_MapLua      = "lua/guard/guard_room3.lua";
		public static final String GuardFourRoom_MapLua       = "lua/guard/guard_room4.lua";
		public static final String GuardFiveRoom_MapLua       = "lua/guard/guard_room5.lua";

		public static final String PinkGhostRoom_MapLua      = "lua/pinkghostroom.lua";
		public static final String SliceDeadRoom_MapLua 	= "lua/slicetan.lua";

		public static final String FocuRoom_MapLua      = "lua/focu_room.lua";
	}

	public static class Environment {

		//GALAXY YOG EYE TO HAPPY BIRTHDAY 820-2024
		public static final String GALAXY_AE  =  "environment/yog/bubble_above.png";
		public static final String GALAXY_BD  = "environment/yog/bubble_behind.png";
		public static final String GALAXY_TILED  =  "environment/yog/tiles_bubble.png";
		public static final String GALAXY_WATER  = "environment/yog/starry.png";

		public static final String GALAXY_BACKGROUND  = "environment/custom_tiles/galaxy_background.png";

		public static final String TERRAIN_FEATURES = "environment/terrain_features.png";

		public static final String TILES_GARDEN = "environment/tiles_garden.png";
		public static final String LAVACAVE_OP  = "environment/custom_tiles/lavecave_behind.png";
		public static final String LAVACAVE_OP_HRAD  = "environment/custom_tiles/lavecave_behind_hard.png";
		public static final String LAVACAVE_PO  = "environment/custom_tiles/lavecave_above.png";

		public static final String HOLLOW_OP  = "environment/custom_tiles/cerberus_behind.png";
		public static final String HOLLOW_PO  = "environment/custom_tiles/cerberus_above.png";

		public static final String HALL_OP  = "environment/custom_tiles/hall_behind.png";

		public static final String HALL_OPX  = "environment/custom_tiles/hall_behind-opendoor.png";
		public static final String HALL_PO  = "environment/custom_tiles/hall_above.png";
		public static final String HALL_POX  = "environment/custom_tiles/hall_above_b.png";


		public static final String CITY_PO  = "environment/custom_tiles/parliament_above.png";
		public static final String CITY_POX  = "environment/custom_tiles/parliament_before_behind.png";
		public static final String CITY_PSD  = "environment/custom_tiles/parliament_behind.png";

		public static final String HOTEL_PO  = "environment/custom_tiles/hotel_above.png";
		public static final String HOTEL_POX  = "environment/custom_tiles/hotel_behind.png";

		public static final String ICE_PO  = "environment/custom_tiles/ice.png";
		public static final String ICE_POX  = "environment/custom_tiles/ice_above.png";

		public static final String ZERO_BACK  = "environment/custom_tiles/zero_spring.png";
		public static final String HOTEL_BACK  = "environment/custom_tiles/zero2_spring.png";

		public static final String GHOST_HOUSE  = "environment/custom_tiles/ghost_house.png";

		public static final String PEACH_PO  =  "environment/custom_tiles/peach_forest_above.png";

		public static final String PEACH_BOSS  = "environment/peach/peach_boss.png";
		public static final String PEACH_PATCH  = "environment/peach/peach_patch.png";

		public static final String VISUAL_GRID  = "environment/visual_grid.png";
		public static final String WALL_BLOCKING= "environment/wall_blocking.png";

		public static final String TILES_SEWERS = "environment/tiles_sewers.png";
		public static final String TILES_PRISON = "environment/tiles_prison.png";
		public static final String TILES_COLDCHEST  = "environment/tiles_coldchest.png";
		public static final String TILES_FIRE  = "environment/tiles_fire.png";
		public static final String Dreamcatcher  = "environment/custom_tiles/blue_quest.png";

		public static final String BoilerQuest  = "environment/custom_tiles/candlestick_room.png";

		public static final String Magic_Marker  = "environment/custom_tiles/teleportation_circle.png";

		public static final String FireQuest  = "environment/custom_tiles/fire_quest.png";
		public static final String OilWell  = "environment/custom_tiles/oil_well.png";
		public static final String TILES_HOLLOW  = "environment/tiles_halloween.png";

		public static final String TILES_GHOST  = "environment/tiles_ghost.png";

		public static final String TILES_THEATRE  = "environment/custom_tiles/theatre.png";

		public static final String ALTAR_SPRING  = "environment/custom_tiles/spring_altar.png";

		public static final String TILES_HOLLOW_CS  = "environment/tiles_halloween-cs.png";

		public static final String TILES_CAVES  = "environment/tiles_caves.png";
		public static final String TILES_CITY   = "environment/tiles_city.png";

		public static final String TILES_CITY_CS   = "environment/tiles_city-cs.png";
		public static final String TILES_ANCIENT  = "environment/tiles_ancient.png";
		public static final String TILES_HALLS  = "environment/tiles_halls.png";

		public static final String TILES_MORGALAXY  = "environment/tiles_morpheus.png";
		public static final String WATER_GALAXY  = "environment/yog/starry.png";

		public static final String TILES_CAVES_GNOLL    = "environment/tiles_cold_gnoll.png";

		public static final String TILES_ZERO_SPRING    = "environment/tiles_zero_spring.png";

		public static final String WATER_SEWERS = "environment/water0.png";
		public static final String WATER_PRISON = "environment/water1.png";
		public static final String WATER_CAVES  = "environment/water2.png";
		public static final String WATER_CITY   = "environment/water3.png";
		public static final String WATER_HALLS  = "environment/water4.png";

		public static final String WATER_ANCIENT  = "environment/water5.png";

		public static final String WATER_HOLLOW  = "environment/water7.png";

		public static final String WATER_GHOST  = "environment/water8.png";

		public static final String WATER_ZERO  = "environment/water_zero.png";

		public static final String WEAK_FLOOR       = "environment/custom_tiles/weak_floor.png";
		public static final String SEWER_BOSS       = "environment/custom_tiles/sewer_boss.png";
		public static final String PRISON_QUEST     = "environment/custom_tiles/prison_quests.png";

		public static final String PRISON_MAGIC     = "environment/custom_tiles/magic.png";
		//public static final String PRISON_QUEST     = "environment/custom_tiles/prison_quest.png";
		public static final String PRISON_EXIT      = "environment/custom_tiles/prison_exit.png";
		public static final String CAVES_QUEST      = "environment/custom_tiles/caves_quest.png";
		public static final String CAVES_BOSS       = "environment/custom_tiles/caves_boss.png";
		public static final String CITY_BOSS        = "environment/custom_tiles/city_boss.png";
		public static final String HALLS_SP         = "environment/custom_tiles/halls_special.png";

		public static final String TILES_COLD	= "environment/tiles_cold.png";
		public static final String TILES_COLD_CS	= "environment/tiles_cold-cs.png";
		public static final String TILES_COLD_MINE	= "environment/tiles_cold_mine.png";
		public static final String RELOAD	= "environment/Reload.png";
		public static final String TILES_DIED	= "environment/tiles_died.png";
		public static final String TILES_GOLD	= "environment/tiles_gold.png";

		public static final String WATER_COLD= "environment/water-1.png";
		public static final String PRISON_EXIT_OLD  = "environment/custom_tiles/prison_exit_old.png";
		public static final String PRISON_EXIT_NEW  = "environment/custom_tiles/prison_exit_new.png";
		public static final String P_BOSS       = "environment/custom_tiles/p_boss.png";
	}
	
	//TODO include other font assets here? Some are platform specific though...
	public static class Fonts {
		public static final String PIXELFONT= "fonts/pixel_font.png";
	}

	public static class Interfaces {

		public static final String BLACK_RECT  = "interfaces/black_rect.png";
		public static final String WHITE_RECT  = "interfaces/white_rect.png";

		public static final String CHALLENGES_BARS   = "interfaces/challgesbar.png";

		//Boss 贴图
		public static final String QliPhothEX_Title  = "interfaces/bosslogo/qliphoth-title-ex.png";
		public static final String QliPhoth_Title  = "interfaces/bosslogo/qliphoth-title.png";
		public static final String QliPhoth_Clear  = "interfaces/bosslogo/qliphoth-clear.png";

		public static final String DMOR_Title  = "interfaces/bosslogo/dm300or-title.png";

		public static final String Goo_Title   = "interfaces/bosslogo/goo-title.png";
		public static final String SGoo_Title  = "interfaces/bosslogo/skygoo-title.png";
		public static final String SCORE_BAR   = "interfaces/scoreboard.png";

		public static final String DIZF_Title  = "interfaces/bosslogo/dizzfar-title.png";
		public static final String DIZF_Slain  = "interfaces/bosslogo/dizzfar-slain.png";

		public static final String UPBARS  = "interfaces/updatebar.png";


		public static final String SakaBJY_Title  = "interfaces/bosslogo/sakabjy-title.png";
		public static final String SakaBJY_Clear  = "interfaces/bosslogo/sakabjy-clear.png";

		public static final String YogZot_Title  = "interfaces/bosslogo/yog-title.png";
		public static final String YogZot_Slain  = "interfaces/bosslogo/yog-slain.png";

		public static final String Cerdog_Title  = "interfaces/bosslogo/cerberus-title.png";
		public static final String Cerdog_Clear  = "interfaces/bosslogo/cerberus-clear.png";

		public static final String Morpheus_Title  = "interfaces/bosslogo/Morpheus-title.png";
		public static final String Morpheus_Clear  = "interfaces/bosslogo/Morpheus-clear.png";

		public static final String General_Title  = "interfaces/bosslogo/general-title.png";
		public static final String General_Clear  = "interfaces/bosslogo/general-clear.png";

		public static final String Tawi_Title  = "interfaces/bosslogo/Tawil-title.png";
		public static final String Tawi_Clear  = "interfaces/bosslogo/Tawil-slain.png";

		public static final String Tengu_Title  = "interfaces/bosslogo/tengu-title.png";
		public static final String Tengu_Clear  = "interfaces/bosslogo/tengu-clear.png";

		public static final String D_Title  = "interfaces/bosslogo/dimand.png";
		public static final String D_Clear  = "interfaces/bosslogo/dimand-clear.png";

		public static final String ARCS_BG  = "interfaces/arcs1.png";
		public static final String ARCS_FG  = "interfaces/arcs2.png";

		public static final String ARCS_FGN  = "interfaces/arcs2_night.png";

		public static final String LANTERLING  = "interfaces/LanterLing.png";

		public static final String LANTERLING_N  = "interfaces/LanterLing_N.png";

		public static final String BANNERS     = "interfaces/Banners/banners.png";
		public static final String BANNERS_AT  = "interfaces/Banners/banners_at.png";
		public static final String BANNERS_CJ  = "interfaces/Banners/banners_cj.png";
		public static final String BANNERS_EA  = "interfaces/Banners/banners_easter.png";
		public static final String BANNERS_HL  = "interfaces/Banners/banners_hlwn.png";
		public static final String BANNERS_BD  = "interfaces/Banners/banners_MLBD.png";
		public static final String BANNERS_SD  = "interfaces/Banners/banners_sd.png";
		public static final String BANNERS_QM  = "interfaces/Banners/banners_qm.png";
		public static final String BANNERS_DW  = "interfaces/Banners/banners_dw.png";
		public static final String BANNERS_SR  = "interfaces/Banners/banners_spr.png";
		public static final String BANNERS_SM  = "interfaces/Banners/banners_sum.png";
		public static final String BANNERS_WT  = "interfaces/Banners/banners_win.png";
		public static final String BANNERS_ZY  = "interfaces/Banners/banners_zy.png";
		public static final String BANNERS_QX  = "interfaces/Banners/banners_qx.png";
		public static final String BANNERS_YX  = "interfaces/Banners/banners_yx.png";
		public static final String BANNERS_SP  = "interfaces/Banners/banners_shattered.png";
		public static final String BANNERS_LD  = "interfaces/Banners/banners_labor.png";

		public static final String BANNERS_GQ  = "interfaces/Banners/banners_gq.png";
		public static final String BANNERS_ZQ  = "interfaces/Banners/banners_zq.png";
		public static final String BANNERS_BX  = "interfaces/Banners/banners_bx.png";
		public static final String BANNERS_CY  = "interfaces/Banners/banners_cy.png";

		public static final String MENUTITLE  = "interfaces/menutitle.png";



		public static final String TWO_YEARS  	= "interfaces/Years/2years.png";
		public static final String Three_YEARS  = "interfaces/Years/3years.png";
		public static final String Four_YEARS  =  "interfaces/Years/4years.png";

		public static final String FIVE_YEARS  =  "interfaces/Years/5years.png";

		//网络贴图
		public static final String NETICONS = "interfaces/net_icons.png";



		public static final String BADGES   = "interfaces/badges.png";
		public static final String PROBADGES   = "interfaces/passwordbadges.png";
		public static final String LOCKED   = "interfaces/locked_badge.png";

		public static final String CHROME   = "interfaces/chrome.png";
		public static final String CHROME_DARK   = "interfaces/chrome_normal.png";
		public static final String ICONS    = "interfaces/icons.png";

		public static final String ICONS_NORMAL    = "interfaces/icons_normal.png";
		public static final String STATUS   = "interfaces/status_pane.png";

		public static final String STATUS_HOLLOW   = "interfaces/status_pane_halloween.png";


		public static final String TOOLBARDRAK	= "interfaces/toolbar_normal.png";
		public static final String STATUS_DARK   = "interfaces/status_pane_normal.png";
		public static final String MENU     = "interfaces/menu_pane.png";
		public static final String MENU_BTN = "interfaces/menu_button.png";
		public static final String TOOLBAR  = "interfaces/toolbar.png";
		public static final String SHADOW   = "interfaces/shadow.png";
		public static final String BOSSHP   = "interfaces/boss_hp.png";
		public static final String BOSSHPX   = "interfaces/dr_boss_hp.png";
		public static final String BOSSHP_DARK   = "interfaces/boss_hp_normal.png";

		public static final String SURFACE  = "interfaces/surface.png";
		public static final String SURFACE_DARK  = "interfaces/surface_normal.png";

		public static final String NEW_MENU  = "interfaces/go-surface.png";
		public static final String NEW_MENU_DARK  = "interfaces/go-surface_normal.png";

		public static final String LOADING_GOLD   = "interfaces/loading_gold.png";
		public static final String LOADING_SEWERS   = "interfaces/loading_sewers.png";
		public static final String LOADING_PRISON   = "interfaces/loading_prison.png";
		public static final String LOADING_COLD    = "interfaces/loading_cold.png";
		public static final String LOADING_CITY     = "interfaces/loading_city.png";
		public static final String LOADING_HALLS    = "interfaces/loading_halls.png";

		public static final String LOADING_HOLLOW    = "interfaces/loading_halloween.png";

		public static final String LOADING_THRATRE   = "interfaces/loading_theatre.png";

		public static final String BUFFS_SMALL      = "interfaces/buffs.png";
		public static final String BUFFS_LARGE      = "interfaces/large_buffs.png";

		public static final String TALENT_ICONS  = "interfaces/talent_icons.png";
		public static final String TALENT_BUTTON = "interfaces/talent_button.png";
		public static final String TALENT_BUTTON_DARK = "interfaces/talent_button_dark.png";
		public static final String HERO_ICONS   = "interfaces/hero_icons.png";
		public static final String HAICONS   = "interfaces/happymode.png";

		public static final String RADIAL_MENU      = "interfaces/radial_menu.png";
	}

	//these points to resource bundles, not raw asset files
	public static class Messages {
		public static final String ACTORS   = "messages/actors/actors";
		public static final String ITEMS    = "messages/items/items";
		public static final String JOURNAL  = "messages/journal/journal";
		public static final String LEVELS   = "messages/levels/levels";
		public static final String MISC     = "messages/misc/misc";
		public static final String PLANTS   = "messages/plants/plants";
		public static final String SCENES   = "messages/scenes/scenes";
		public static final String UI       = "messages/ui/ui";
		public static final String WINDOWS  = "messages/windows/windows";
	}

	public static class Music {



		public static final String THEME      = "music/theme.ogg";
		public static final String GO      = "music/Open.ogg";
		public static final String SHOP      = "music/shop.ogg";
		public static final String THEME_1      = "music/theme.ogg";
		public static final String THEME_2      = "music/theme_sky.ogg";
		public static final String JUNGLE_FOREST = "music/Level1.ogg";

		public static final String BGM_1A = "music/forestflower.ogg";

		public static final String BGM_2 = "music/Level2.ogg";
		public static final String BGM_3 = "music/Level3.ogg";
		public static final String BGM_4 = "music/Level4.ogg";
		public static final String BGM_5 = "music/Level5.ogg";

		public static final String PACMAN = "music/hollow/pacman_10yearold_mix.ogg";
		public static final String MOVEBOX = "music/hollow/movebox.ogg";

		public static final String SEACH = "music/hollow/seach.ogg";

		public static final String CHRAMSS     = "music/chrimas.ogg";
		public static final String SEWERS_BOSS  = "music/Boss1.ogg";
		public static final String PRISON_BOSS  = "music/Tengu.ogg";
		public static final String CITY_BOSS    = "music/Boss4.ogg";

		public static final String HIRO    = "music/hiro.ogg";

        public static final String HALLS_BOSS = "music/Boss5.ogg";
        public static final String NBPL = "music/nbpl.ogg";
        public static final String THEME_FINALE = "music/theme_finale.ogg";
        public static final String PRISON_TENSE = "music/prison_tense.ogg";
        public static final String CAVES_TENSE = "music/caves_tense.ogg";
        public static final String CAVES_BOSS_FINALE = "music/caves_boss_finale.ogg";
        public static final String CITY_BOSS_FINALE = "music/city_boss_finale.ogg";

		public static final String SAND = "music/Sand.ogg";

		public static final String PRACH = "music/peach.ogg";

        public static final String HALLS_TENSE = "music/halls_tense.ogg";

		public static final String ANCITY = "music/Level_Ancity.ogg";
        public static final String HALLS_BOSS_FINALE = "music/halls_boss_finale.ogg";

		public static final String DIAMAND_KING_INTRO = "music/boss/Diamd_Boss.ogg";
		public static final String DRAGON_LING  = "music/boss/Dragon.ogg";

		public static final String NIGHT	= "music/DwarfGeneral.ogg";

		public static final String YOGALXY	= "music/rgjt/galaxy.ogg";
		public static final String YOGGOD	= "music/rgjt/god.ogg";
		public static final String STARLXY	= "music/rgjt/star.ogg";

		public static final String SKBJY		= "music/Boss-Saka.ogg";

		public static final String TOWN 		= "music/town.ogg";

		public static final String TOWN_YEARS 		= "music/years.ogg";
		public static final String BGM_BOSSA 	= "music/Boss1.ogg";
		public static final String BGM_YOU 		= "music/boss/you.ogg";
		public static final String BGM_BOSSC 	= "music/Boss3.ogg";
		public static final String BGM_BOSSD 	= "music/Boss4.ogg";
		public static final String BGM_BOSSD2 	= "music/DwarfGeneral.ogg";
		public static final String BGM_BOSSE3 	= "music/Yogdead.ogg";

		public static final String BGM_SHOP 	= "music/Shop.ogg";
		public static final String BGM_BOSSE4 	= "music/YogTime.ogg";
		public static final String HOLLOW_CITY 	= "music/hollow/Mischief_Managed-Easy.ogg";
		public static final String HOLLOW_CITY_HARD 	= "music/hollow/Mischief_Managed.ogg";
		public static final String BOSSDOG 		= "music/boss/dogboss.ogg";
		public static final String MORP_BOSS 	= "music/hollow/morpheus.ogg";
	}

	public static class Sounds {
		public static final String CLICK    = "sounds/click.mp3";
		public static final String BADGE    = "sounds/badge.mp3";
		public static final String GOLD     = "sounds/gold.mp3";

		public static final String OPEN     = "sounds/door_open.mp3";
		public static final String UNLOCK   = "sounds/unlock.mp3";
		public static final String ITEM     = "sounds/item.mp3";
		public static final String DEWDROP  = "sounds/dewdrop.mp3";
		public static final String STEP     = "sounds/step.mp3";
		public static final String WATER    = "sounds/water.mp3";
		public static final String GRASS    = "sounds/grass.mp3";
		public static final String TRAMPLE  = "sounds/trample.mp3";
		public static final String STURDY   = "sounds/sturdy.mp3";

		public static final String HIT              = "sounds/hit.mp3";
		public static final String MISS             = "sounds/miss.mp3";
		public static final String HIT_SLASH        = "sounds/hit_slash.mp3";
		public static final String HIT_STAB         = "sounds/hit_stab.mp3";
		public static final String HIT_CRUSH        = "sounds/hit_crush.mp3";
		public static final String HIT_MAGIC        = "sounds/hit_magic.mp3";
		public static final String HIT_STRONG       = "sounds/hit_strong.mp3";
		public static final String HIT_PARRY        = "sounds/hit_parry.mp3";
		public static final String HIT_ARROW        = "sounds/hit_arrow.mp3";
		public static final String ATK_SPIRITBOW    = "sounds/atk_spiritbow.mp3";
		public static final String ATK_CROSSBOW     = "sounds/atk_crossbow.mp3";
		public static final String HEALTH_WARN      = "sounds/health_warn.mp3";
		public static final String HEALTH_CRITICAL  = "sounds/health_critical.mp3";

		public static final String DESCEND  = "sounds/descend.mp3";
		public static final String EAT      = "sounds/eat.mp3";
		public static final String READ     = "sounds/read.mp3";
		public static final String LULLABY  = "sounds/lullaby.mp3";
		public static final String DRINK    = "sounds/drink.mp3";
		public static final String SHATTER  = "sounds/shatter.mp3";
		public static final String ZAP      = "sounds/zap.mp3";
		public static final String LIGHTNING= "sounds/lightning.mp3";
		public static final String LEVELUP  = "sounds/levelup.mp3";
		public static final String DEATH    = "sounds/death.mp3";
		public static final String CHALLENGE= "sounds/challenge.mp3";
		public static final String CURSED   = "sounds/cursed.mp3";
		public static final String TRAP     = "sounds/trap.mp3";
		public static final String EVOKE    = "sounds/evoke.mp3";
		public static final String TOMB     = "sounds/tomb.mp3";
		public static final String ALERT    = "sounds/alert.mp3";
		public static final String MELD     = "sounds/meld.mp3";
		public static final String BOSS     = "sounds/boss.mp3";
		public static final String BLAST    = "sounds/blast.mp3";
		public static final String PLANT    = "sounds/plant.mp3";
		public static final String RAY      = "sounds/ray.mp3";
		public static final String BEACON   = "sounds/beacon.mp3";
		public static final String TELEPORT = "sounds/teleport.mp3";

		public static final String DOG_ANAGY = "sounds/dog_growl.ogg";

		public static final String CHARMS   = "sounds/charms.mp3";
		public static final String MASTERY  = "sounds/mastery.mp3";
		public static final String PUFF     = "sounds/puff.mp3";
		public static final String ROCKS    = "sounds/rocks.mp3";
		public static final String BURNING  = "sounds/burning.mp3";
		public static final String FALLING  = "sounds/falling.mp3";
		public static final String GHOST    = "sounds/ghost.mp3";
		public static final String SECRET   = "sounds/secret.mp3";
		public static final String BONES    = "sounds/bones.mp3";
		public static final String BEE      = "sounds/bee.mp3";
		public static final String DEGRADE  = "sounds/degrade.mp3";
		public static final String MIMIC    = "sounds/mimic.mp3";
		public static final String DEBUFF   = "sounds/debuff.mp3";
		public static final String CHARGEUP = "sounds/chargeup.mp3";
		public static final String GAS      = "sounds/gas.mp3";
		public static final String CHAINS   = "sounds/chains.mp3";
		public static final String SCAN     = "sounds/scan.mp3";
		public static final String SHEEP    = "sounds/sheep.mp3";
		public static final String ANSDOSHIP    = "sounds/ansdoship.mp3";
		public static final String MINE    = "sounds/mine.mp3";

		public static final String[] all = new String[]{
				CLICK, BADGE, GOLD,

				OPEN, UNLOCK, ITEM, DEWDROP, STEP, WATER, GRASS, TRAMPLE, STURDY,

				HIT, MISS, HIT_SLASH, HIT_STAB, HIT_CRUSH, HIT_MAGIC, HIT_STRONG, HIT_PARRY,
				HIT_ARROW, ATK_SPIRITBOW, ATK_CROSSBOW, HEALTH_WARN, HEALTH_CRITICAL,

				DESCEND, EAT, READ, LULLABY, DRINK, SHATTER, ZAP, LIGHTNING, LEVELUP, DEATH,
				CHALLENGE, CURSED, TRAP, EVOKE, TOMB, ALERT, MELD, BOSS, BLAST, PLANT, RAY, BEACON,
				TELEPORT, CHARMS, MASTERY, PUFF, ROCKS, BURNING, FALLING, GHOST, SECRET, BONES,
				BEE, DEGRADE, MIMIC, DEBUFF, CHARGEUP, GAS, CHAINS, SCAN, SHEEP, ANSDOSHIP,MINE,
				DOG_ANAGY
		};
	}

	public static class Splashes {
		public static final String WARRIOR  = "splashes/warrior.jpg";
		public static final String MLPDBR     = "splashes/mlpd.png";
		public static final String ROGUE    = "splashes/rogue.jpg";
		public static final String HUNTRESS = "splashes/huntress.jpg";
		public static final String GDX = "splashes/gdx.png";
		public static final String ANSDOSHIP = "splashes/ansdoship.png";

		public static final String LINPX = "splashes/sx.png";
		public static final String DUELIST  = "splashes/duelist.jpg";

		public static final String MOSRDX_0 = "splashes/hollow/Morphs/morpheus_portraits(0).png";
		public static final String MOSRDX_1 = "splashes/hollow/Morphs/morpheus_portraits(1).png";
		public static final String MOSRDX_2 = "splashes/hollow/Morphs/morpheus_portraits(2).png";
		public static final String MOSRDX_3 = "splashes/hollow/Morphs/morpheus_portraits(3).png";
		public static final String MOSRDX_4 = "splashes/hollow/Morphs/morpheus_portraits(4).png";
		public static final String MOSRDX_5 = "splashes/hollow/Morphs/morpheus_portraits(5).png";
		public static final String MOSRDX_6 = "splashes/hollow/Morphs/morpheus_portraits(6).png";
		public static final String MOSRDX_7 = "splashes/hollow/Morphs/morpheus_portraits(7).png";
		public static final String MOSRDX_8 = "splashes/hollow/Morphs/morpheus_portraits(8).png";


		public static final String FAYINA_0 = "splashes/firedragon/guard_npc_portraits(0).png";
		public static final String FAYINA_1 = "splashes/firedragon/guard_npc_portraits(1).png";
		public static final String FAYINA_2 = "splashes/firedragon/guard_npc_portraits(2).png";
		public static final String FAYINA_3 = "splashes/firedragon/guard_npc_portraits(3).png";



		public static final String SMLF = "splashes/smallleaf_portraits.png";
		public static final String SMLS = "splashes/card_smallleaf_portraits.png";

		public static final String BLUECJ = "splashes/blue_cj.png";

		public static final String XMS_GDZ= "splashes/Xmas_gdz_portraits.png";
		public static final String GDZ= "splashes/gdz_portraits.png";
		public static final String YSGDZ= "splashes/gdz_yeas.png";

		public static final String GQGDZ= "splashes/gdz_plus.png";


		public static final String PIANO = "splashes/piano_portraits.png";

		public static final String ATRP = "splashes/ATRI_portraits.png";

		public static final String BZMR = "splashes/bzmdr_portraits.png";

		//WhiteYan Boss Emoji
		public static final String WY_HAPPY = "splashes/peach/whiteyan_happy.png";
		public static final String WY_NOLAN = "splashes/peach/whiteyan_nolang.png";
		public static final String WY_SMALE = "splashes/peach/whiteyan_smale.png";
		public static final String WY_WORRY = "splashes/peach/whiteyan_worry.png";
		public static final String WY_SORRY = "splashes/peach/whiteyan_sorry.png";
		public static final String WY_STICK = "splashes/peach/whiteyan_stick.png";
		public static final String WY_WXHAT = "splashes/peach/whiteyan_what.png";
		public static final String WY_WARLS = "splashes/peach/whiteyan_war.png";

		public static final String DG = "splashes/general_portraits.png";

		public static final String FG = "splashes/guard_npc_portraits.png";

		public static final String HK = "splashes/hk.png";

		public static final String YTZY = "splashes/YetYogR.png";

		public static final String TRZY = "splashes/true_yog_portraits.png";

		public static final String YXF = "splashes/huntress_portraits.png";

		public static final String NYZ = "splashes/nyz_portraits.png";
		public static final String MT = "splashes/mint_portraits.png";
		public static final String MT_CJ = "splashes/mint_cj_portraits.png";
		public static final String LN = "splashes/ln_portraits.png";
		public static final String WTX = "splashes/yan_portraits.png";
		public static final String JIT = "splashes/JIT_portraits.png";

		public static final String MOON = "splashes/moon_portraits.png";

		public static final String GDZPLS = "splashes/gdz_goldplus.png";

		public static final String COON = "splashes/⑨.png";

		public static final String YOGSTS_5 = "splashes/yetsts5.png";

		public static final String PINKFOX = "splashes/pinkfox.png";

		public static final String COMPLEX = "splashes/complex.png";

		public static final String LMOON = "splashes/lowmoon_portraits.png";

		public static final String CHOCO = "splashes/choco_portraits.png";

		public static final String FYNX = "splashes/guard_portraits.png";



		public static final String LINP = "splashes/Ling_portraits.png";

		public static final String GHOSTJ = "splashes/apprentice_portraits.png";

		//HOLLOW PORTRAITS
		public static final String KARONG_0 = "splashes/hollow/DeathRong/death_portraits(0).png";
		public static final String KARONG_1 = "splashes/hollow/DeathRong/death_portraits(1).png";
		public static final String KARONG_2 = "splashes/hollow/DeathRong/death_portraits(2).png";
		public static final String KARONG_3 = "splashes/hollow/DeathRong/death_portraits(3).png";
		public static final String KARONG_4 = "splashes/hollow/DeathRong/death_portraits(4).png";
		public static final String KARONG_5 = "splashes/hollow/DeathRong/death_portraits(5).png";
		public static final String KARONG_6 = "splashes/hollow/DeathRong/death_portraits(6).png";

		public static final String Morphs_0 = "splashes/hollow/Morphs/morpheus_portraits(0).png";
		public static final String Morphs_1 = "splashes/hollow/Morphs/morpheus_portraits(1).png";
		public static final String Morphs_2 = "splashes/hollow/Morphs/morpheus_portraits(2).png";
		public static final String Morphs_3 = "splashes/hollow/Morphs/morpheus_portraits(3).png";
		public static final String Morphs_4 = "splashes/hollow/Morphs/morpheus_portraits(4).png";
		public static final String Morphs_5 = "splashes/hollow/Morphs/morpheus_portraits(5).png";
		public static final String Morphs_6 = "splashes/hollow/Morphs/morpheus_portraits(6).png";
		public static final String Morphs_7 = "splashes/hollow/Morphs/morpheus_portraits(7).png";
		public static final String Morphs_8 = "splashes/hollow/Morphs/morpheus_portraits(8).png";

		public static final String Silence_0 = "splashes/hollow/Silence/silence_portraits(0).png";
		public static final String Silence_1 = "splashes/hollow/Silence/silence_portraits(1).png";
		public static final String Silence_2 = "splashes/hollow/Silence/silence_portraits(2).png";
		public static final String Silence_3 = "splashes/hollow/Silence/silence_portraits(3).png";
		public static final String Silence_4 = "splashes/hollow/Silence/silence_portraits(4).png";

		public static final String TYPHON = "splashes/hollow/Typhon/typhon_portraits.png";

	}

	public static class Sprites {

		//Gold
		public static final String GOLD_CRAB	   = "sprites/gold/crab.png";
		public static final String GOLD_GNOLL	   = "sprites/gold/gnoll.png";
		public static final String GIANT_WORM      = "sprites/gold/giant_worm.png";
		public static final String HEROGNOLLOLD    = "sprites/gold/tribesman_old.png";
		public static final String MAYFLY          = "sprites/gold/mayfly.png";
		public static final String PRISONER        = "sprites/gold/prisoner.png";
		public static final String DRAKE           = "sprites/gold/drake.png";
		public static final String ARTILLERIST     = "sprites/gold/artillerist.png";
		public static final String ROYAL_GUARD     = "sprites/gold/royal_guard.png";
		public static final String GORGON          = "sprites/gold/gorgon.png";
		public static final String DEMONLORD       = "sprites/gold/demon_lord.png";
		public static final String SKULLS			= "sprites/gold/skulls.png";
		public static final String GOBLINSHAMAN       = "sprites/gold/goblinshaman.png";


		//NPCS
		public static final String KEEPERKING	= "sprites/npc/ShopKing.png";
		public static final String YETYOG	= "sprites/npc/yetyogyears.png";
		public static final String YETYOG_R2	= "sprites/npc/yetyogyears5.png";
		public static final String YUYE	= "sprites/npc/yuye.png";
		public static final String MINT	= "sprites/npc/mint_cj.png";

		public static final String PINKFOX	= "sprites/npc/pinkfox.png";

		public static final String ZAKO	= "sprites/npc/zako.png";
		public static final String XMS_GUDAZI	= "sprites/npc/Xms_gdz.png";

		public static final String XS_GUDAZI	= "sprites/npc/gdz1.png";

		public static final String HF_GUDAZI	= "sprites/npc/gdz2.png";

		public static final String GS_GUDAZI	= "sprites/npc/gdz_gold.png";

		public static final String GQ_GUDAZI	= "sprites/npc/gdzgq.png";

		public static final String GUDAZI	= "sprites/npc/gdz.png";
		public static final String DEEP	= "sprites/npc/deepsea.png";
		public static final String QUESTION	= "sprites/npc/question.png";

		public static final String BZ	= "sprites/npc/bzmder.png";

		public static final String HK	= "sprites/npc/hk.png";

		public static final String CHOCO	= "sprites/npc/choco.png";

		public static final String PIA	= "sprites/npc/PianoLe.png";
		public static final String JIT	= "sprites/npc/JIT.png";

		public static final String FIREGIRL_NPC	= "sprites/npc/firemagicgirl.png";
		public static final String ICEGIRL_NPC	= "sprites/npc/icemagicgirl.png";


		public static final String WFS	    = "sprites/items/wf.png";
		//ANCITY
		public static final String TURTLE= "sprites/ancity/cltb.png";
		public static final String ANOMALO= "sprites/ancity/anomalocaris.png";

		public static final String THREEBUG = "sprites/ancity/threeleafbug.png";

		//HOLLOW

		public static final String GHOST_MINI	= "sprites/hollow/minigame/ghosts.png";

		public static final String ESCFAIL	= "sprites/hollow/minigame/escfail.png";
		public static final String ESCSSFL	= "sprites/hollow/minigame/escsucessful.png";

		public static final String ESCSOUT	= "sprites/hollow/minigame/esctimeout.png";

		public static final String BOX_MINI	= "sprites/hollow/minigame/box.png";


		public static final String TELE_FOCU	= "sprites/hollow/minigame/teleportation_focu.png";

		//BLOOD MOBS
		public static final String RED_GOLEM	=  "sprites/hollow/minigame/golem-ex.png";
		public static final String RED_SNAKE	=  "sprites/hollow/minigame/snake-ex.png";
		public static final String RED_TORCH	=  "sprites/hollow/minigame/torchhuntsman-ex.png";
		public static final String RED_SAMH	=  "sprites/hollow/minigame/shieldhuntsman-ex.png";
		public static final String RED_SPIDER	=  "sprites/hollow/minigame/spinner-ex.png";
		public static final String RED_GNOLL	=  "sprites/hollow/minigame/skullshaman-ex.png";



		public static final String APWHEEL = "sprites/hollow/apprentice_witch.png";

		public static final String BTSLIMH = "sprites/hollow/butcher.png";

		public static final String ZEROBOAT = "sprites/hollow/death.png";

		public static final String BOAT = "sprites/hollow/boat.png";
		public static final String SWTICH = "sprites/hollow/Silence_Witch.png";

		public static final String SWTICH_ALTER = "sprites/hollow/silence_altar.png";

		public static final String CSBR = "sprites/hollow/Cerberus.png";
		public static final String NCSBR = "sprites/hollow/New_Cerberus.png";

		public static final String SCSR = "sprites/hollow/Cerberus_sleep.png";

		public static final String DGS = "sprites/boss/dwarf_general.png";

		public static final String DFL = "sprites/boss/army_flag.png";

		public static final String DXL = "sprites/boss/dwarf_soldier.png";

		public static final String DER = "sprites/boss/DiedElement.png";
		public static final String TYPHON = "sprites/hollow/typhon.png";
		public static final String MPHON = "sprites/hollow/morpheus.png";

		public static final String MINDCODE 	= "sprites/hollow/messy_code.png";

		public static final String TowerGods = "sprites/hollow/tower_gods.png";
		public static final String TowerTime = "sprites/hollow/tower_time.png";
		public static final String TowerMind = "sprites/hollow/tower_mind.png";
		public static final String TowerMachine = "sprites/hollow/tower_machine.png";

		public static final String SHUBNIGGURATH = "sprites/hollow/shub_niggurath.png";
		public static final String NYARLATHOTEP = "sprites/hollow/nyarlathotep.png";

		public static final String YOG_SOUL = "sprites/hollow/yogsoul.png";

		public static final String MYSTIC_CORE = "sprites/hollow/mystic_core.png";

		public static final String BLEED_SENTRY= "sprites/hollow/bleed_star.png";


		public static final String ZOMBIE = "sprites/hollow/frankenstein.png";

		public static final String GHOST_HE = "sprites/hollow/ghost_halloween.png";

		public static final String GHOST_HP = "sprites/hollow/pumpkin_ghost.png";

		public static final String CRUMB = "sprites/hollow/crumb.png";

		public static final String BOMB = "sprites/hollow/gingerbread.png";

		public static final String VAMPIRE = "sprites/hollow/vampire.png";

		public static final String KEEPERKINGBOT	= "sprites/npc/autoshop.png";

		public static final String ARCHETTO	= "sprites/npc/avrt.png";

		//BOSS
		public static final String FRDG	= "sprites/boss/fireDragon.png";
		public static final String DICT		= "sprites/boss/DictFish.png";

		public static final String ROOMSTONE		= "sprites/boss/RoomStone.png";

		public static final String VSGR	= "sprites/boss/SeaVastGirl.png";

		public static final String FSGR	= "sprites/boss/FireWitch.png";

		public static final String SXGR	= "sprites/boss/IceSlowGirl.png";

		public static final String SKFS	 = "sprites/boss/SakaFishBoss.png";

		public static final String SOTS	 = "sprites/boss/yog_sts.png";
		public static final String LATS	 = "sprites/boss/sts_lasher.png";
		public static final String SATS	 = "sprites/boss/yog_servantavgomon.png";
		public static final String FSOE	= "sprites/boss/four_stone.png";

		public static final String WHITEYAN = "sprites/npc/whitepinkgirl.png";
		public static final String DIMK	 = "sprites/boss/DimandKing.png";

		public static final String DIED	= "sprites/items/died.png";

		public static final String CRID	= "sprites/boss/ColdGuard.png";

		public static final String FRAS	= "sprites/boss/FireWitch.png";

		public static final String CFAS	= "sprites/boss/FruitsCrivus.png";

		public static final String QLPH	= "sprites/boss/crivus2.png";
		public static final String QLPH_LASHER	= "sprites/boss/crivus2lasher.png";

		public static final String CFZS	= "sprites/boss/overworld/qliphoth_ex.png";
		public static final String CLGR	= "sprites/boss/overworld/clearelemt_guard.png";
		public static final String NECROREDEX	= "sprites/boss/rednecromancer_ex.png";
		public static final String SLIMEPRINCESS	= "sprites/boss/SlimePrincess.png";
		public static final String ICEALICE	= "sprites/boss/IceFireCrstal.png";

		public static final String FREA	= "sprites/boss/FireCrstal.png";

		public static final String DREA	= "sprites/boss/DCrstal.png";
		public static final String TPDP	= "sprites/boss/TPDoor.png";

		public static final String ITEMS	    = "sprites/items/items.png";

		public static final String MAGEHAND	    = "sprites/npc/mage_hand.png";

		public static final String ITEM_ICONS   = "sprites/item_icons.png";

		public static final String MGAS	= "sprites/icemagicgirl.png";

		public static final String WARRIOR	= "sprites/warrior.png";
		public static final String MAGE		= "sprites/mage.png";
		public static final String ROGUE	= "sprites/rogue.png";
		public static final String HUNTRESS	= "sprites/huntress.png";
		public static final String COMINGSOON	= "sprites/slimekingch.png";
		public static final String AVATARS	= "sprites/avatars.png";
		public static final String AVATARS_WARRIOR	= "sprites/avatars_warrior.png";
		public static final String AVATARS_MAGE	= "sprites/avatars_mage.png";
		public static final String AVATARS_ROGUE	= "sprites/avatars_rogue.png";
		public static final String AVATARS_HUNTRESS	= "sprites/avatars_huntress.png";
		public static final String AVATARS_DUELIST	= "sprites/avatars_duelist.png";

		public static final String AVATARS_SPELLSWORD	= "sprites/avatars_spellsword.png";

		public static final String AMULET	= "sprites/amulet.png";
		public static final String JAMULET	= "sprites/jamulet.png";
		public static final String COLDRAT	= "sprites/coldrat.png";

		public static final String VERYCOLDRAT = "sprites/rat_ice.png";

		public static final String WARLOCKHEAD = "sprites/warlock_chief.png";
		public static final String AOREADY	= "sprites/aodragon.png";

		public static final String HEALRIGH	= "sprites/healfire.png";

		public static final String REDSWARM				= "sprites/RedSearm.png";
		public static final String REN					= "sprites/npc/ren.png";
		public static final String HIRO					= "sprites/npc/hiro.png";
		public static final String DragonBlueGirl		= "sprites/npc/smallblue.png";
		public static final String DKGirl				= "sprites/npc/DKGirl.png";

		public static final String LanFire				= "sprites/npc/lanfire.png";
		public static final String LanFire_CJ			= "sprites/npc/lanfire_cj.png";

		public static final String CJBL					= "sprites/npc/blue_cj.png";

		public static final String SAESD		= "sprites/bluedragon.png";

		public static final String KATID		= "sprites/venom_flower.png";

		public static final String WHITE		= "sprites/npc/whiteling.png";

		public static final String FAYINA		= "sprites/npc/guard_npc.png";

		public static final String XR		= "sprites/npc/Xiayuan.png";

		public static final String TOMB		= "sprites/npc/tomb.png";

		public static final String DEADEYE		= "sprites/eye_doom.png";

		public static final String MOONC		= "sprites/npc/mooncat.png";

		public static final String SMALLEAF		= "sprites/npc/smallleaf.png";

		public static final String MORUS		= "sprites/npc/mruos.png";

		public static final String LXF		= "sprites/npc/lostwhite.png";

		public static final String LXFCJ    = "sprites/npc/lostwhitecj.png";

		public static final String WOLF		= "sprites/npc/wolf.png";

		public static final String LEZI		= "sprites/npc/le_zi.png";

		public static final String SKSH		= "sprites/npc/skin_shop.png";

		public static final String KONG		= "sprites/npc/ChineseGongFu.png";
		public static final String MOLX		= "sprites/npc/lowmoon.png";

		public static final String ATRI		= "sprites/npc/ATRI.png";
		public static final String BBAT	= "sprites/bloodbat.png";

		public static final String RAT		= "sprites/rat.png";

		public static final String EVIL_SPAWN		= "sprites/spawne_evil.png";

		public static final String EVIL		= "sprites/demon_fodder.png";
		public static final String BLOOD_SWARM		= "sprites/swarm_red.png";

		public static final String GHOUL_HILL		= "sprites/ghoul_hill.png";

		public static final String PEACHGODSTATUE	= "sprites/peachgodstatue.png";

		public static final String NONE		= "sprites/none.png";
		public static final String FLOWER_SLIME		= "sprites/flowers_slime.png";
        public static final String GIANT_FLOWER_SLIME		= "sprites/flowers_slime_giant.png";

		public static final String GREEN		= "sprites/greenslting.png";

		public static final String SKY_DEAD		= "sprites/skydead.png";

		public static final String GREEN_KING		= "sprites/greenslting_king.png";

		public static final String CLEAR		= "sprites/clearelemt.png";
		public static final String BRUTE	= "sprites/brute.png";
		public static final String SPINNER	= "sprites/spinner.png";
		public static final String ASDW	= "sprites/Viaw.png";
		public static final String ASDWX	= "sprites/FireBallMob.png";
		public static final String DM300	= "sprites/dm300.png";
		public static final String DM720	= "sprites/dm720.png";
		public static final String WRAITH	= "sprites/wraith.png";

		//PETS
		public static final String SKFSBABY	= "sprites/pets/sakababy.png";
		public static final String SMSLIGHT	= "sprites/pets/smlt.png";

		public static final String REDWRAITH	= "sprites/bluewraith.png";

		public static final String UNDEAD	= "sprites/undead.png";
		public static final String KING		= "sprites/king.png";
		public static final String PIRANHA	= "sprites/piranha.png";

		public static final String PIRANHA_LAND	= "sprites/boss/piranha_land.png";
		public static final String PIRANHA_KING	= "sprites/boss/piranha_king.png";
		public static final String RICE_RAT	= "sprites/boss/ricemouse.png";

		public static final String EYE		= "sprites/eye.png";
		public static final String GNOLL	= "sprites/gnoll.png";
		public static final String CRAB		= "sprites/crab.png";

		public static final String NEWBORN_CRAB		= "sprites/crab_newborn.png";
		public static final String GOO		= "sprites/goo.png";
		public static final String SWARM	= "sprites/swarm.png";
		public static final String SKELETON	= "sprites/skeleton.png";
		public static final String SHAMAN	= "sprites/shaman.png";
		public static final String THIEF	= "sprites/thief.png";
		public static final String MUDTHIEF	= "sprites/thief2.png";
		public static final String TENGU	= "sprites/tengu.png";
		public static final String SHEEP	= "sprites/sheep.png";
		public static final String KEEPER	= "sprites/shopkeeper.png";
		public static final String BAT		= "sprites/bat.png";
		public static final String BATEX		= "sprites/BrownBat.png";

		public static final String DUELIST  = "sprites/duelist.png";
		public static final String PET      = "sprites/pet.png";
		public static final String ELEMENTAL= "sprites/elemental.png";
		public static final String REDDRAGONPET = "sprites/petreddragon.png";
		public static final String MONK		= "sprites/monk.png";
		public static final String MONKDIED	= "sprites/monkdied.png";
		public static final String WARLOCK	= "sprites/warlock.png";
		public static final String GOLEM	= "sprites/golem.png";
		public static final String ICEGOLEM	= "sprites/icegolem.png";
		public static final String STATUE	= "sprites/statue.png";
		public static final String SUCCUBUS	= "sprites/succubus.png";

		public static final String SUCCUBUS_QUEEN	= "sprites/succubus_queen.png";
		public static final String IFE_SCORPIO	= "sprites/ife_scriopi.png";
		public static final String SCORPIO	= "sprites/scorpio.png";
		public static final String FISTS	= "sprites/yog_fists.png";
		public static final String YOG		= "sprites/yog.png";
		public static final String LARVA	= "sprites/larva.png";
		public static final String GHOST	= "sprites/ghost.png";
		public static final String PINKGHOST	= "sprites/pinkghost.png";
		public static final String MAKER	= "sprites/wandmaker.png";
		public static final String NYZD	= "sprites/npc/nyz.png";
		public static final String TROLL	= "sprites/blacksmith.png";
		public static final String IMP		= "sprites/demon.png";
		public static final String RATKING	= "sprites/ratking.png";
		public static final String BEE      = "sprites/bee.png";
		public static final String MIMIC    = "sprites/mimic.png";
		public static final String ROT_LASH = "sprites/rot_lasher.png";
		public static final String ROT_HEART= "sprites/rot_heart.png";
		public static final String GUARD    = "sprites/guard.png";

		public static final String GUARD_CAPITAL = "sprites/guard_captain.png";

		public static final String HEROGNOLL    = "sprites/tribesman.png";

		public static final String WARDS    = "sprites/wards.png";
		public static final String GUARDIAN	= "sprites/guardian.png";
		public static final String SLIME	= "sprites/slime.png";
		public static final String SNAKE	= "sprites/snake.png";
		public static final String NECRO	= "sprites/necromancer.png";
		public static final String NECRORED	= "sprites/rednecromancer_ex.png";

		public static final String GHOUL	= "sprites/ghoul.png";
		public static final String RIPPER	= "sprites/ripper.png";
		public static final String SPAWNER	= "sprites/spawner.png";
		public static final String DM100	= "sprites/dm100.png";

		public static final String DM111	= "sprites/dm111.png";

		public static final String DM275	= "sprites/dm275-RPG.png";
		public static final String PYLON	= "sprites/pylon.png";
		public static final String PYLONCS	= "sprites/pylonks.png";
		public static final String CRSTAL	= "sprites/crstal.png";
		public static final String DM200	= "sprites/dm200.png";
		public static final String LOTUS	= "sprites/lotus.png";
		public static final String CRSTA	= "sprites/crstalspawn.png";
		public static final Object NECROBLUE =  "sprites/bluenecromancer.png";
		public static final String NINJA_LOG= "sprites/ninja_log.png";
		public static final String SPIRIT_HAWK= "sprites/spirit_hawk.png";
		public static final String RED_SENTRY= "sprites/red_sentry.png";

		public static final String YOW_SENTRY = "sprites/boss/yellow_star.png";
		public static final String PINK_SENTRY = "sprites/boss/pink_star.png";
		public static final String BUE_SENTRY= "sprites/npc/blue_star.png";

		public static final String CANDLESTICK = "sprites/npc/candlestick.png";

		public static final String SWORDLING= "Boss/swordling.png";
		public static final String CRYSTAL_WISP= "sprites/crystal_wisp.png";
		public static final String CRYSTAL_GUARDIAN = "sprites/crystal_guardian.png";
		public static final String CRYSTAL_SPIRE    = "sprites/crystal_spire.png";
		public static final String GNOLL_GUARD      = "sprites/gnoll_guard.png";
		public static final String GNOLL_SAPPER     = "sprites/gnoll_sapper.png";
		public static final String GNOLL_GEOMANCER  = "sprites/gnoll_geomancer.png";
		public static final String FUNGAL_SPINNER   = "sprites/fungal_spinner.png";
		public static final String FUNGAL_SENTRY    = "sprites/fungal_sentry.png";
		public static final String FUNGAL_CORE      = "sprites/fungal_core.png";


		//Animation Item
		public static final String ANIMATIONS_TERMIAL = "sprites/items/Animation/DM100-Terminal.png";
		public static final String ANIMATIONS_BOMBSWORD = "sprites/items/Animation/BoomSword.png";
		public static final String ANIMATIONS_SOS = "sprites/items/Animation/DistressSignalNesting.png";
		public static final String ANIMATIONS_SXS = "sprites/items/Animation/SliverLockSword.png";

		public static final String ANIMATIONS_SUN = "sprites/items/Animation/Sun.png";

	}



	public static class Shader {
		public static final String WATER_WAVE_VERT      = "shaders/water.vert";
		public static final String WATER_WAVE_FRAG      = "shaders/water.frag";
	}
}
