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

package com.watabou.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.watabou.noosa.Game;

public class GameSettings {
	
	public static final String DEFAULT_PREFS_FILE = "settings.xml";
	private static final String BUNDLABLE="b";

	public static <T extends Bundlable> T getBundlable(String key, T defValue){
		try {
			Bundle b = Bundle.fromString(getString(key,""));
			return (T)b.get(BUNDLABLE);
		} catch (Exception e) {
			return defValue;
		}
	}
	private static Preferences prefs;
	
	private static Preferences get() {
		if (prefs == null) {
			prefs = Gdx.app.getPreferences( DEFAULT_PREFS_FILE );
		}
		return prefs;
	}
	
	//allows setting up of preferences directly during game initialization
	public static void set( Preferences prefs ){
		GameSettings.prefs = prefs;
	}
	
	public static boolean contains( String key ){
		return get().contains( key );
	}
	
	public static int getInt( String key, int defValue ) {
		return getInt(key, defValue, Integer.MIN_VALUE, Integer.MAX_VALUE);
	}
	
	public static int getInt( String key, int defValue, int min, int max ) {
		try {
			int i = get().getInteger( key, defValue );
			if (i < min || i > max){
				int val = (int)GameMath.gate(min, i, max);
				put(key, val);
				return val;
			} else {
				return i;
			}
		} catch (Exception e) {
			Game.reportException(e);
			put(key, defValue);
			return defValue;
		}
	}

	public static long getLong( String key, long defValue ) {
		return getLong(key, defValue, Long.MIN_VALUE, Long.MAX_VALUE);
	}

	public static long getLong( String key, long defValue, long min, long max ) {
		try {
			long i = get().getLong( key, defValue );
			if (i < min || i > max){
				long val = (long)GameMath.gate(min, i, max);
				put(key, val);
				return val;
			} else {
				return i;
			}
		} catch (Exception e) {
			Game.reportException(e);
			put(key, defValue);
			return defValue;
		}
	}
	
	public static boolean getBoolean( String key, boolean defValue ) {
		try {
			return get().getBoolean(key, defValue);
		} catch (Exception e) {
			Game.reportException(e);
			return defValue;
		}
	}
	
	public static String getString( String key, String defValue ) {
		return getString(key, defValue, Integer.MAX_VALUE);
	}
	
	public static String getString( String key, String defValue, int maxLength ) {
		try {
			String s = get().getString( key, defValue );
			if (s != null && s.length() > maxLength) {
				put(key, defValue);
				return defValue;
			} else {
				return s;
			}
		} catch (Exception e) {
			Game.reportException(e);
			put(key, defValue);
			return defValue;
		}
	}

	public static String[] getStringArray( String key ) {
		try {
			String s = get().getString( key, "" );
			if ( s != null && s.length() > Integer.MAX_VALUE ) {
				return new String[0];
			} else {
				return s.split( ";" );
			}
		} catch (Exception e) {
			Game.reportException(e);
			return new String[0];
		}
	}
	
	public static void put( String key, int value ) {
		get().putInteger(key, value);
		get().flush();
	}

	public static void put( String key, long value ) {
		get().putLong(key, value);
		get().flush();
	}
	
	public static void put( String key, boolean value ) {
		get().putBoolean(key, value);
		get().flush();
	}
	
	public static void put( String key, String value ) {
		get().putString(key, value);
		get().flush();
	}

	public static void put( String key, String[] value ) {
		int length = value.length;
		String str = get().getString( key, "" );
		StringBuilder stringArray = new StringBuilder( str );

		for(int i = 0; i < length; i++) {
			if( query(key, value[i].split(",")[0]) )
				continue;

			if (value[i].indexOf(";") != -1) {
				stringArray.append(value[i]);
			}else {
				stringArray.append(value[i]).append(";");
			}
		}

		get().putString(key, stringArray.toString());
		get().flush();
	}

	public static void delete( String key, String value ) {
		String[] itemArray = value.split( ";" );
		String str = get().getString( key, "" );
		StringBuilder stringArray = new StringBuilder( str );

		int index;
		for( String target : itemArray ) {
			if ( ( index = stringArray.indexOf( target ) ) != -1 ) {
				stringArray.delete( index, index + stringArray.indexOf( ";", index ) + 1 );
			}
		}

		get().putString(key, stringArray.toString());
		get().flush();
	}

	public static void modify( String key, String target, String value ) {
		String str = get().getString( key, "" );
		StringBuilder stringArray = new StringBuilder( str );
		int index;

		if( str != null && ( ( index = stringArray.indexOf( target ) ) != -1 ) ) {
			stringArray.replace(index, index + stringArray.indexOf(";", index) + 1, value);

			get().putString(key, stringArray.toString());
			get().flush();
		}
	}

	public static boolean query( String key, String target ) {
		return get().getString( key, "" ).indexOf( target ) != -1;
	}
}
