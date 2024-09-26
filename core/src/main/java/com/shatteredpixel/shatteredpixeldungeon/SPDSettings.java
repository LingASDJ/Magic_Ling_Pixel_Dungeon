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

import com.shatteredpixel.shatteredpixeldungeon.custom.utils.Constants;
import com.shatteredpixel.shatteredpixeldungeon.messages.Languages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.DeviceCompat;
import com.watabou.utils.GameSettings;
import com.watabou.utils.Point;

import java.util.Locale;

public class SPDSettings extends GameSettings {

	//Version info

	public static final String KEY_VERSION      = "version";

	public static void quickslots( int value ){
		put( KEY_QUICKSLOTS, value );
	}

	public static int quickslots(){
		return getInt( KEY_QUICKSLOTS, 4, Constants.MIN_QUICKSLOTS, Constants.MAX_QUICKSLOTS);
	}

	public static void level3boss( int value ){
		put( KEY_L3BOSS, value );
	}

	public static int level3boss(){
		return getInt( KEY_L3BOSS, 1, 1, 3);
	}

	public static void TimeLimit(boolean value) {
		put(KEY_TIME, value );
	}

	public static boolean TimeLimit() {
		return getBoolean(KEY_TIME, true);
	}

	public static void HiICE(boolean value) {
		put(KEY_ICE, value );
	}

	public static boolean HiICE() {
		return getBoolean(KEY_ICE, true);
	}

	public static void UPICE(boolean value) {
		put(KEY_UP_ICE, value );
	}

	public static boolean UPICE() {
		return getBoolean(KEY_UP_ICE, true);
	}


	private static final String DEBUG_REPORT  = "debug_report";
	public static boolean debugReport() {
		return getBoolean(DEBUG_REPORT,false);
	}

	public static boolean startPort(boolean value) {
		return getBoolean(START_PORT, value);
	}

	public static void version( int value)  {
		put( KEY_VERSION, value );
	}

	public static int version() {
		return getInt( KEY_VERSION, 0 );
	}

	//Graphics

	public static String heroName() {
		return !Badges.isUnlocked(Badges.Badge.BOSS_SLAIN_1) ? "" : GameSettings.getString("name", "", 20);
	}

	public static void heroName(String str) {
		GameSettings.put("name", str);
	}

	public static final String KEY_CUSTOM_SEED	= "custom_seed";

	public static final String KEY_FULLSCREEN	= "fullscreen";
	public static final String KEY_LANDSCAPE	= "landscape";
	public static final String KEY_POWER_SAVER 	= "power_saver";
	public static final String KEY_FIRE_BASE    = "fire_base";
	public static final String KEY_SCALE		= "scale";
	public static final String KEY_ZOOM			= "zoom";
	public static final String KEY_BRIGHTNESS	= "brightness";
	public static final String KEY_GRID 	    = "visual_grid";
	public static final String KEY_SPLASH_SCREEN= "splash_screen";

	public static final String KEY_TIMEOUT= "timeout";

	private static final String START_PORT  = "start_report";

	//瀑布系统
	public static void splashScreen( int value ) {
		put( KEY_SPLASH_SCREEN, value );
	}

	public static int splashScreen() {
		return getInt( KEY_SPLASH_SCREEN, 1 );
	}

	public static void timeOutSeed( int value ) {
		put( KEY_TIMEOUT, value );
	}

	public static int  timeOutSeed() {
		return getInt( KEY_TIMEOUT, 4 );
	}

	public static void customSeed( String value ){
		put( KEY_CUSTOM_SEED, value );
	}

	public static String customSeed() {
		return getString( KEY_CUSTOM_SEED, "", 20);
	}

	private static final String KEY_TIME = "fps";

	private static final String KEY_ICE = "hice";

	private static final String KEY_UP_ICE = "iceupdate_21";

	private static final String KEY_DARK	= "dark_ui";

	private static final String KEY_SKIN	= "skin_ui";

	private static final String KEY_PAGE	= "page_ui";

	private static final String HelpSettings	= "helpsettings";


	private static final String ATBSettings	= "ATBsettings";

	private static final String V1TOOL = "v1tool";

	private static final String KEY_SMALLLEAF = "SMALLLEAF";

	private static final String KEY_DRAGON = "DRAGON";

	private static final String KEY_KILLADF = "DWAXF";


	private static final String KEY_VSB = "VSB";

	public static void fullscreen( boolean value ) {
		put( KEY_FULLSCREEN, value );

		ShatteredPixelDungeon.updateSystemUI();
	}

	public static boolean fullscreen() {
		return getBoolean( KEY_FULLSCREEN, DeviceCompat.isDesktop() );
	}

	public static void landscape( boolean value ){
		put( KEY_LANDSCAPE, value );
		((ShatteredPixelDungeon)ShatteredPixelDungeon.instance).updateDisplaySize();
	}

	//can return null because we need to directly handle the case of landscape not being set
	// as there are different defaults for different devices
	public static Boolean landscape(){
		if (contains(KEY_LANDSCAPE)){
			return getBoolean(KEY_LANDSCAPE, false);
		} else {
			return null;
		}
	}

	public static void powerSaver( boolean value ){
		put( KEY_POWER_SAVER, value );
		((ShatteredPixelDungeon)ShatteredPixelDungeon.instance).updateDisplaySize();
	}

	public static boolean powerSaver(){
		return getBoolean( KEY_POWER_SAVER, false );
	}

	public static void firebase( boolean value ){
		put( KEY_FIRE_BASE, value );
		((ShatteredPixelDungeon)ShatteredPixelDungeon.instance).updateDisplaySize();
	}

	public static boolean firebase(){
		return getBoolean( KEY_FIRE_BASE, true );
	}

	public static void scale( int value ) {
		put( KEY_SCALE, value );
	}

	public static int scale() {
		return getInt( KEY_SCALE, 0 );
	}

	public static void zoom( int value ) {
		put( KEY_ZOOM, value );
	}

	public static int zoom() {
		return getInt( KEY_ZOOM, 0 );
	}

	public static void brightness( int value ) {
		put( KEY_BRIGHTNESS, value );
		GameScene.updateFog();
	}

	public static int brightness() {
		return getInt( KEY_BRIGHTNESS, 0, -1, 1 );
	}

	public static void visualGrid( int value ){
		put( KEY_GRID, value );
		GameScene.updateMap();
	}

	public static int visualGrid() {
		return getInt( KEY_GRID, 0, -1, 2 );
	}

	//Interface

	public static final String KEY_VIBRATION    = "vibration";

    public static final String KEY_ONE_CONDUCT = "one_conduct";
    public static final String KEY_TWO_CONDUCT = "two_conduct";

    public static final String KEY_UI_SIZE = "full_ui";
    public static final String KEY_QUICKSLOTS = "quickslots";
    public static final String KEY_L3BOSS = "bossl3";
    public static final String KEY_FLIPTOOLBAR = "flipped_ui";
    public static final String KEY_FLIPTAGS = "flip_tags";
    public static final String KEY_BARMODE = "toolbar_mode";

    public static final String KEY_CAMERA_FOLLOW = "camera_follow";
    public static final String KEY_SCREEN_SHAKE = "screen_shake";

    //0 = mobile, 1 = mixed (large without inventory in main UI), 2 = large
    public static final String KEY_LAST_DAILY = "last_daily";
    public static final String KEY_SUPPORT_NAGGED = "support_nagged";
    public static final String KEY_CONTROLLER_SENS = "controller_sens";
    public static final String KEY_MOVE_SENS = "move_sens";

	public static final String KEY_ICECOIN = "iceGoldMagic2";

	public static final String KEY_UNLOCKITEM = "forever_unlock_item";

	public static final String KEY_CURRENTHEROSKIN = "current_hero_skin";

	public static final String KEY_BOSS_WEAPON_COUNT1 = "boss_weapon_count1";


	public static void vibration(boolean value){
		put(KEY_VIBRATION, value);
	}

	public static boolean vibration(){
		return getBoolean(KEY_VIBRATION, true);
	}

    public static void cameraFollow(int value) {
        put(KEY_CAMERA_FOLLOW, value);
    }

    public static int cameraFollow() {
        return getInt(KEY_CAMERA_FOLLOW, 4, 1, 4);
    }

	public static void flipToolbar( boolean value) {
		put(KEY_FLIPTOOLBAR, value );
	}

	public static boolean flipToolbar(){ return getBoolean(KEY_FLIPTOOLBAR, false); }

	public static void flipTags( boolean value) {
		put(KEY_FLIPTAGS, value );
	}

	public static boolean flipTags(){ return getBoolean(KEY_FLIPTAGS, false); }

	public static void toolbarMode( String value ) {
		put( KEY_BARMODE, value );
	}

	public static String toolbarMode() {
		return getString(KEY_BARMODE, PixelScene.landscape() ? "GROUP" : "SPLIT");
	}

	//Game State

	public static final String KEY_LAST_CLASS	= "last_class";
	public static final String KEY_CHALLENGES	= "challenges";

	public static final String KEY_FOUND_DEPTH	= "foundepth";

	public static final String KEY_DLC	= "dlc";
	public static final String KEY_DIFFICULTY	= "difficulty";

	public static final String KEY_INTRO		= "intro";

    public static void screenShake(int value) {
        put(KEY_SCREEN_SHAKE, value);
    }

    public static int screenShake() {
        return getInt(KEY_SCREEN_SHAKE, 2, 0, 4);
    }

    public static void interfaceSize(int value) {
        put(KEY_UI_SIZE, value);
    }

    public static int interfaceSize() {
        int size = getInt(KEY_UI_SIZE, DeviceCompat.isDesktop() ? 1 : 0);
        if (size > 0) {
            //force mobile UI if there is not enough space for full UI
            float wMin = Game.width / PixelScene.MIN_WIDTH_FULL;
			float hMin = Game.height / PixelScene.MIN_HEIGHT_FULL;
			if (Math.min(wMin, hMin) < 2*Game.density){
				size = 0;
			}
		}

		if(size == 2){
			size = 1;
		}
		return size;
	}

	public static void intro( boolean value ) {
		put( KEY_INTRO, value );
    }

    public static boolean intro() {
        return getBoolean(KEY_INTRO, true);
    }

    public static void lastClass(int value) {
        put(KEY_LAST_CLASS, value);
    }

    public static int lastClass() {
        return getInt(KEY_LAST_CLASS, 0, 0, 3);
    }

    public static void challenges(int value) {
        put(KEY_CHALLENGES, value);
    }

    public static int challenges() {
        return getInt(KEY_CHALLENGES, 0, 0, Challenges.MAX_VALUE);
    }

    public static void lastDaily(long value) {
        put(KEY_LAST_DAILY, value);
    }

    public static long lastDaily() {
        return getLong(KEY_LAST_DAILY, 0);
    }

    //DLC SYSTEM
    public static void dlc(Conducts.ConductStorage value) {
        put(KEY_DLC, value);
    }

	public static void difficulty(Difficulty.HardStorage value ) {
		cut( KEY_DIFFICULTY, value);
	}

	private static final String BUNDLABLE="b";

	public static <T extends Bundlable> T getBundlable(String key, T defValue){
		try {
			Bundle b = Bundle.fromString(getString(key,""));
			return (T)b.get(BUNDLABLE);
		} catch (Exception e) {
			return defValue;
		}
	}

	private static final String CUNDLABLE="c";

	public static <T extends Bundlable> T getCundlable(String key, T defValue){
		try {
			Bundle b = Bundle.fromString(getString(key,""));
			return (T)b.get(CUNDLABLE);
		} catch (Exception e) {
			return defValue;
		}
	}
	public static void put( String key, Bundlable value ) {
		Bundle b = new Bundle();
		b.put(BUNDLABLE,value);
		put(key,b.toString());
	}

	public static void cut( String key, Bundlable value ) {
		Bundle b = new Bundle();
		b.put(CUNDLABLE,value);
		put(key,b.toString());
	}

	public static boolean twoConduct() {return getBoolean(KEY_TWO_CONDUCT, true);}

	public static boolean oneConduct() {return getBoolean(KEY_ONE_CONDUCT, true);}

	public static void supportNagged( boolean value ) {
		put( KEY_SUPPORT_NAGGED, value );
	}

	public static boolean supportNagged() {
		return getBoolean(KEY_SUPPORT_NAGGED, false);
	}

	//Audio

	public static final String KEY_MUSIC		= "music";
	public static final String KEY_MUSIC_VOL    = "music_vol";
	public static final String KEY_SOUND_FX		= "soundfx";
	public static final String KEY_SFX_VOL      = "sfx_vol";
	public static final String KEY_IGNORE_SILENT= "ignore_silent";

	public static final String KEY_MUSIC_BG     = "music_bg";
	public static void playMusicInBackground( boolean value ){
		put( KEY_MUSIC_BG, value);
	}

	public static boolean playMusicInBackground(){
		return getBoolean( KEY_MUSIC_BG, true);
	}

	public static void music( boolean value ) {
		Music.INSTANCE.enable( value );
		put( KEY_MUSIC, value );
	}

	public static boolean music() {
		return getBoolean( KEY_MUSIC, true );
	}

	public static void musicVol( int value ){
		Music.INSTANCE.volume(value*value/100f);
		put( KEY_MUSIC_VOL, value );
	}

	public static int musicVol(){
		return getInt( KEY_MUSIC_VOL, 10, 0, 10 );
	}

	public static void soundFx( boolean value ) {
		Sample.INSTANCE.enable( value );
		put( KEY_SOUND_FX, value );
	}

	public static boolean soundFx() {
		return getBoolean( KEY_SOUND_FX, true );
	}

	public static void SFXVol( int value ) {
		Sample.INSTANCE.volume(value*value/100f);
		put( KEY_SFX_VOL, value );
	}

	public static int SFXVol() {
		return getInt( KEY_SFX_VOL, 10, 0, 10 );
	}

	public static void ignoreSilentMode( boolean value ){
		put( KEY_IGNORE_SILENT, value);
		Game.platform.setHonorSilentSwitch(!value);
	}

	public static boolean ignoreSilentMode(){
		return getBoolean( KEY_IGNORE_SILENT, false);
	}

	//Languages and Font

	public static final String KEY_LANG         = "language";
	public static final String KEY_SYSTEMFONT	= "system_font";

	public static void language(Languages lang) {
		put( KEY_LANG, lang.code());
	}

	public static Languages language() {
		String code = getString(KEY_LANG, null);
		if (code == null){
			return Languages.matchLocale(Locale.getDefault());
		} else {
			return Languages.matchCode(code);
		}
	}

	public static void systemFont(boolean value){
		put(KEY_SYSTEMFONT, value);
	}

	public static boolean systemFont(){
		return getBoolean(KEY_SYSTEMFONT,
				(language() == Languages.CHINESE || language() == Languages.JAPANESE));
	}

	//Connectivity

	public static final String KEY_NEWS     = "news";
	public static final String KEY_UPDATES	= "updates";
	public static final String KEY_BETAS	= "betas";
	public static final String KEY_WIFI     = "wifi";

	public static final String KEY_NEWS_LAST_READ = "news_last_read";

	public static final String KEY_UPDATE_LAST_READ = "news_update_read";

	public static void news(boolean value){
		put(KEY_NEWS, value);
	}

	public static boolean news(){
		return getBoolean(KEY_NEWS, true);
	}

	public static void updates(boolean value){
		put(KEY_UPDATES, value);
	}

	public static boolean updates(){
		return getBoolean(KEY_UPDATES, true);
	}

	public static void betas(boolean value){
		put(KEY_BETAS, value);
	}

	public static boolean betas(){
		return getBoolean(KEY_BETAS, Game.version.contains("BETA") || Game.version.contains("RC"));
	}

	public static void WiFi(boolean value){
		put(KEY_WIFI, value);
	}

	public static boolean WiFi(){
		return getBoolean(KEY_WIFI, true);
	}

	public static void updatenewsLastRead(long updatelastRead){
		put(KEY_UPDATE_LAST_READ, updatelastRead);
	}

	public static long updatenewsLastRead(){
		return getLong(KEY_UPDATE_LAST_READ, 0);
	}

	public static void newsLastRead(long lastRead){
		put(KEY_NEWS_LAST_READ, lastRead);
	}

	public static long newsLastRead(){
		return getLong(KEY_NEWS_LAST_READ, 0);
	}

	//Window management (desktop only atm)

	public static final String KEY_WINDOW_WIDTH     = "window_width";
	public static final String KEY_WINDOW_HEIGHT    = "window_height";
	public static final String KEY_WINDOW_MAXIMIZED = "window_maximized";

	public static void windowResolution( Point p ){
		put(KEY_WINDOW_WIDTH, p.x);
		put(KEY_WINDOW_HEIGHT, p.y);
	}

	public static Point windowResolution(){
		return new Point(
				getInt( KEY_WINDOW_WIDTH, 800, 720, Integer.MAX_VALUE ),
				getInt( KEY_WINDOW_HEIGHT, 600, 400, Integer.MAX_VALUE )
		);
	}

	public static void windowMaximized( boolean value ){
		put( KEY_WINDOW_MAXIMIZED, value );
	}

	public static boolean windowMaximized(){
		return getBoolean( KEY_WINDOW_MAXIMIZED, false );
	}

	public static void ClassUI(boolean value) {
		put( KEY_DARK, value );
	}

	public static boolean ClassUI() {
		return getBoolean(KEY_DARK, false);
	}

	public static void HelpSettings(boolean value) {
		put( HelpSettings, value );
	}

	public static boolean HelpSettings() {
		return getBoolean(HelpSettings, false);
	}

	public static void ATBSettings(boolean value) {
		put( ATBSettings, value );
	}

	public static boolean ATBSettings() {
		return getBoolean(ATBSettings, false);
	}

	public static boolean V2IconDamage() {
		return getBoolean(KEY_SKIN, true);
	}

	public static void V2IconDamage(boolean value) {
		put( KEY_SKIN, value );
	}

	public static boolean ClassPage() {
		return getBoolean(KEY_PAGE, false);
	}

    public static Conducts.ConductStorage dlc() {
        return getBundlable(KEY_DLC, new Conducts.ConductStorage());
    }

    //HARD SYSTEM
	public static Difficulty.HardStorage difficulty() {
		return getCundlable(KEY_DIFFICULTY, new Difficulty.HardStorage());
	}

    public static void ClassPage(boolean value) {
        put(KEY_PAGE, value);
    }


    //Input

    public static boolean quickSwapper() {
        return getBoolean(V1TOOL, false);
    }

    public static void quickSwapper(boolean value) {
        put(V1TOOL, value);
    }

    public static void controllerPointerSensitivity(int value) {
        put(KEY_CONTROLLER_SENS, value);
    }

    public static int controllerPointerSensitivity() {
        return getInt(KEY_CONTROLLER_SENS, 5, 1, 10);
    }

    public static void movementHoldSensitivity(int value) {
        put(KEY_MOVE_SENS, value);
    }

    public static int movementHoldSensitivity() {
        return getInt(KEY_MOVE_SENS, 3, 0, 4);
    }

	public static void Cheating(boolean value) {
		put("cheatingfuck", value);
	}

	public static boolean Cheating() {
		return getBoolean("cheatingfuck", false);
	}



	public static void WT3(boolean value) {
		put("WT3", value);
	}

	public static boolean WT3() {
		return getBoolean("WT3", false);
	}


	//永久货币逻辑
	public static void iceCoin(int value) {
		int currentCoin = iceCoin();
		int newCoin = currentCoin + value;
		put(KEY_ICECOIN, newCoin);
	}

	public static void iceDownCoin(int value) {
		int currentCoin = iceCoin();
		int newCoin = currentCoin - value;
		put(KEY_ICECOIN, newCoin);
	}

	public static int iceCoin(){
		return getInt( KEY_ICECOIN, 0);
	}

	public static void setHeroSkin(int hero,int skinIndex) {
		StringBuilder items = new StringBuilder( getSkin() );
		int index= hero * 2;
		items.replace( index, index + 1 , String.valueOf(skinIndex));
		put(KEY_CURRENTHEROSKIN, items.toString());
	}

	public static int getHeroSkin(int hero){
		String[] itemArray = getSkin().split( ";" );
		return Integer.parseInt(itemArray[hero]);
	}

	public static String getSkin(){
		return getString( KEY_CURRENTHEROSKIN, "0;0;0;0;0;");
	}


	/*
	 * @Breif 永久解锁物品，允许批量解锁，以","作为元素分隔符,";"作为物品分隔符
	 * 输入格式为String itemName1,boolean allowMulti1,int itemLimit1;String itemName2,boolean allowMulti2,int itemLimit2;...
	 * @Pramas String
	 * @NativeName: unlockItem
	 * @NativeFunction: void unlockItem(String)
	 */
	public static void unlockItem( String itemName ){
		String[] itemArray = itemName.split( ";" );
		StringBuilder items = new StringBuilder( unlockItem() );

		for( String item : itemArray) {
			String[] tempItem = item.split( "," );
			if( !isItemUnlock( tempItem[0] ) ){
				switch( tempItem.length ){
					case 1:
						items.append( item ).append( ",false,1;" );
						break;
					case 2:
						if( tempItem[1].matches( "\\d+" ) ){
							items.append( tempItem[0] ).append( ",false," ).append( tempItem[1] ).append( ";" );
						}else if( tempItem[1].equals( "true" ) || tempItem[1].equals( "false" ) ){
							items.append( item ).append( ",1;" );
						}else {
							continue;
						}
						break;
					case 3:
						items.append( item ).append( ";" );
						break;
				}
			}
		}

		put( KEY_UNLOCKITEM, items.toString() );
	}

	/*
	 * @Breif 永久解锁物品，第一个参数为itemName，即物品的名称；第二个参数为allowMulti，即是否允许多持该物品
	 * @Pramas String,boolean
	 * @NativeName: unlockItem
	 * @NativeFunction: void unlockItem(String,boolean)
	 */
	public static void unlockItem( String itemName, boolean allowMulti ){
		if( !isItemUnlock( itemName ) ){
			StringBuilder items = new StringBuilder( unlockItem() );
			items.append( itemName ).append( "," );
			items.append( allowMulti ).append( ",1;" );
			put( KEY_UNLOCKITEM, items.toString() );
		}
	}

	/*
	 * @Breif 永久解锁物品，第一个参数为itemName，即物品的名称；第二个参数为allowMulti，即是否允许多持该物品；第三个参数为limit，即持有该物品的上限
	 * @Pramas String,boolean,int
	 * @NativeName: unlockItem
	 * @NativeFunction: void unlockItem(String,boolean,int)
	 */
	public static void unlockItem( String itemName, boolean allowMulti, int limit ){
		if( !isItemUnlock( itemName ) ){
			StringBuilder items = new StringBuilder( unlockItem() );
			items.append( itemName ).append( "," );
			items.append( allowMulti ).append( "," );
			items.append( limit ).append( ";" );
			put( KEY_UNLOCKITEM, items.toString() );
		}
	}

	/*
	 * @Breif 获取已解锁的物品列表，以","作为元素分隔符,";"作为物品分隔符
	 * 输出格式为String itemName1,boolean allowMulti1,int itemLimit1;String itemName2,boolean allowMulti2,int itemLimit2;...
	 * @Pramas
	 * @NativeName: unlockItem
	 * @NativeFunction: String unlockItem()
	 */
	public static String unlockItem(){ return getString( KEY_UNLOCKITEM, ""); }

	/*
	 * @Breif 返回目标物品是否已经解锁
	 * @Pramas String
	 * @NativeName: isItemUnlock
	 * @NativeFunction: Boolean isItemUnlock(String)
	 */
	public static Boolean isItemUnlock( String itemName ){ return unlockItem().indexOf( itemName ) != -1; }

	/*
	 * @Breif 返回目标物品是否允许多持
	 * @Pramas String
	 * @NativeName: isUnlockItemAllowMulti
	 * @NativeFunction: Boolean isUnlockItemAllowMulti(String)
	 */
	public static Boolean isUnlockItemAllowMulti( String itemName ){
		if( !isItemUnlock( itemName ) ){
			return false;
		}

		String[] items = unlockItem().split( ";" );
		for( String item : items ){
			if( item.indexOf( itemName ) != -1 ){
				return Boolean.parseBoolean( item.split( "," )[1] );
			}
		}

		return false;
	}

	/*
	 * @Breif 返回目标物品的持有上限
	 * @Pramas int
	 * @NativeName: getUnlockItemLimit
	 * @NativeFunction: int getUnlockItemLimit(String)
	 */
	public static int getUnlockItemLimit( String itemName ){
		if( !isItemUnlock( itemName ) ){
			return -1;
		}

		String[] items = unlockItem().split( ";" );
		for( String item : items ){
			if( item.indexOf( itemName ) != -1 ){
				return Integer.parseInt( item.split( ",")[2] );
			}
		}

		return -1;
	}


	/*
	 * @Breif 将已解锁物品移除，允许同时移除多个物品
	 * @Pramas String
	 * @NativeName: removeUnlockItem
	 * @NativeFunction: void removeUnlockItem(String)
	 */
	public static void removeUnlockItem( String itemName ){
		String[] itemArray = itemName.split( ";" );
		StringBuilder items = new StringBuilder( unlockItem() );

		int index;
		for( String target : itemArray ) {
			if ( ( index = items.indexOf( target ) ) != -1 ) {
					items.delete( index, index + items.indexOf( ";", index ) + 1 );
			}
		}

		put( KEY_UNLOCKITEM, items.toString() );
	}

	//首次给予钴币 小叶
	public static void SmallLeafGetCoin(boolean value) {
		put(KEY_SMALLLEAF, value );
	}

	public static boolean SmallLeafGetCoin() {
		return getBoolean(KEY_SMALLLEAF, false);
	}


	//首次击败火龙
	public static void KillDragon(boolean value) {
		put(KEY_DRAGON, value );
	}

	public static boolean KillDragon() {
		return getBoolean(KEY_DRAGON, false);
	}


	//首次击败将军
	public static void KillDwarf(boolean value) {
		put(KEY_KILLADF, value );
	}

	public static boolean KillDwarf() {
		return getBoolean(KEY_KILLADF, false);
	}


	//查找深度
	public static void FoundDepth(int value) {
		put(KEY_FOUND_DEPTH, value);
	}

	public static int FoundDepth() {
		return getInt(KEY_FOUND_DEPTH, 0, 0, 30);
	}

	public static void BossWeaponCount1(int value) {
		put(KEY_BOSS_WEAPON_COUNT1, value);
	}

	public static int BossWeaponCount1(){
		return getInt( KEY_BOSS_WEAPON_COUNT1, 0);
	}

	public static void visualBuddle(boolean value) {
		put(KEY_KILLADF, value );
	}

	public static boolean visualBuddle() {
		return getBoolean(KEY_KILLADF, true);
	}

	//Seedfinder

	public static final String KEY_FLOORS	= "number_of_floors";
	public static final String KEY_USEROOMS	= "use_rooms";
	public static final String KEY_IGNOREBLACKLIST	= "ignore_blacklist";

	public static final String KEY_LOGTRINKETS	= "logging_option_trinkets";
	public static final String KEY_LOGEQUIPMENT	= "logging_option_equipment";
	public static final String KEY_LOGSCROLLS	= "logging_option_scrolls";
	public static final String KEY_LOGPOTIONS= "logging_option_potions";
	public static final String KEY_LOGRINGS	= "logging_option_rings";
	public static final String KEY_LOGWANDS= "logging_option_wands";
	public static final String KEY_LOGARTIFACTS	= "logging_option_artifacts";
	public static final String KEY_LOGMISC= "logging_option_other";

	public static final String KEY_SEEDITEMS_TEXT= "remember_seeditems_text";
	public static final String KEY_SEEDINPUT_TEXT= "remember_seedinput_text";
	public static final String KEY_CONDITION= "seedfinder_condition";

	public static void seedfinderFloors( int value ) {
		put( KEY_FLOORS, value );
	}

	public static int seedfinderFloors() {
		return getInt( KEY_FLOORS, 7, 1, 31 );
	}

	public static void seeditemsText(String value) {
		put(KEY_SEEDITEMS_TEXT, value);
	}

	public static String seeditemsText() {
		return getString(KEY_SEEDITEMS_TEXT, "焰浪法杖 +1");
	}

	public static void seedinputText(String value) {
		put(KEY_SEEDINPUT_TEXT, value);
	}

	public static String seedinputText() {
		return getString(KEY_SEEDINPUT_TEXT, "");
	}

	public static void seedfinderConditionANY(boolean value) {
		put(KEY_CONDITION, value);
	}

	public static boolean seedfinderConditionANY() {
		return getBoolean(KEY_CONDITION, false);
	}

	public static void useRooms( boolean value ) {
		put( KEY_USEROOMS, value );
	}

	public static boolean useRooms() {
		return getBoolean( KEY_USEROOMS, false );
	}

	public static void ignoreBlacklist( boolean value ) {
		put( KEY_IGNOREBLACKLIST, value );
	}

	public static boolean ignoreBlacklist() {
		return getBoolean( KEY_IGNOREBLACKLIST, false );
	}

	public static void logTrinkets( boolean value ) {
		put( KEY_LOGTRINKETS, value );
	}

	public static boolean logTrinkets() {
		return getBoolean( KEY_LOGTRINKETS, true );
	}

	public static void logEquipment( boolean value ) {
		put( KEY_LOGEQUIPMENT, value );
	}

	public static boolean logEquipment() {
		return getBoolean( KEY_LOGEQUIPMENT, true );
	}

	public static void logScrolls( boolean value ) {
		put( KEY_LOGSCROLLS, value );
	}

	public static boolean logScrolls() {
		return getBoolean( KEY_LOGSCROLLS, true );
	}
	public static void logPotions( boolean value ) {
		put( KEY_LOGPOTIONS, value );
	}

	public static boolean logPotions() {
		return getBoolean( KEY_LOGPOTIONS, true );
	}
	public static void logRings( boolean value ) {
		put( KEY_LOGRINGS, value );
	}

	public static boolean logRings() {
		return getBoolean( KEY_LOGRINGS, true );
	}
	public static void logWands( boolean value ) {
		put( KEY_LOGWANDS, value );
	}

	public static boolean logWands() {
		return getBoolean( KEY_LOGWANDS, true );
	}
	public static void logArtifacts( boolean value ) {
		put( KEY_LOGARTIFACTS, value );
	}

	public static boolean logArtifacts() {
		return getBoolean( KEY_LOGARTIFACTS, true );
	}
	public static void logMisc( boolean value ) {
		put( KEY_LOGMISC, value );
	}

	public static boolean logMisc() {
		return getBoolean( KEY_LOGMISC, false );
	}

}
