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

	public static String[] getAllStringArray( String key ) {
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

	/*
	 * @Comments 删除xml设置文件中指定关键字 key 里的指定主键 value 所对应的数组;允许批量删除，在value中以";"作为主键之间的分隔符
	 * @Parameters key,value
	 * @NativeName: delete
	 * @NativeFunction: void delete(String,String)
	 */
	public static void delete( String key, String value ) {
		String[] itemArray = value.split( ";" );
		String str = get().getString( key, "" );
		StringBuilder stringArray = new StringBuilder( str );

		for( String target : itemArray ) {
			String[] result = exist(stringArray.toString(), target);
			int start = Integer.valueOf(result[0]);
			int end = Integer.valueOf(result[1]);

			if (end > 0) {
				stringArray.delete( start, end );
			}
		}

		get().putString(key, stringArray.toString());
		get().flush();
	}

	/*
	 * @Comments 修改xml设置文件中指定关键字 key 里的指定主键 target 所对应的数组，value为修改后的数组
	 * @Parameters key,target,value
	 * @NativeName: modifyArray
	 * @NativeFunction: void modifyArray(String,String,String)
	 */
	public static void modifyArray( String key, String target, String value ) {
		String str = get().getString( key, "" );
		StringBuilder stringArray = new StringBuilder( str );
		String[] result = exist( str, target );
		int start = Integer.valueOf( result[0] );
		int end = Integer.valueOf( result[1] );

		if (end > 0) {
			StringBuilder tempStr = new StringBuilder(value);
			int length = tempStr.length() - 1;

			if ( value.lastIndexOf(",") == length )
				tempStr.deleteCharAt( length ).append(";");
			if ( tempStr.lastIndexOf(";") != length )
				tempStr.append(";");

			stringArray.replace(start, end, tempStr.toString());

			get().putString(key, stringArray.toString());
			get().flush();
		}
	}

	/*
	 * @Comments 修改xml设置文件中指定关键字 key 里的指定主键 target 所对应的数组中的第 index 个元素，value为修改后的元素
	 * @Parameters key,target,index,value
	 * @NativeName: modifyArrayElement
	 * @NativeFunction: void modifyArrayElement(String,String,int,String)
	 */
	public static void modifyArrayElement( String key, String target, int index, String value ) {
		String str = get().getString( key, "" );
		StringBuilder stringArray = new StringBuilder( str );
		String[] result = exist( str, target );
		int start = Integer.valueOf( result[0] );
		int end = Integer.valueOf( result[1] );

		if( end > 0 ){
			int count = 0;
			int startIndex = 0;
			int endIndex = 0;
			StringBuilder tempStr = new StringBuilder( result[2] );
			int elementCount = result[2].split(",").length;

			for (int i = 0; i < ( end - start ); i++) {
				if (tempStr.charAt(i) == ',') {
					if (count == index) {
						break;
					} else {
						startIndex = i;
						count++;
					}
				}
			}

			endIndex = tempStr.indexOf(",", startIndex + 1 );
			if(endIndex == -1)
				endIndex = tempStr.length();
			startIndex = index == 0 ? 0 : startIndex + 1;
			int charResult = start + endIndex;
			int charIndex = charResult == str.length() ? charResult - 1 : charResult;

			if( ( stringArray.charAt( end - 1 ) == ';' || stringArray.charAt( charIndex ) == ',' ) && ( value.charAt( value.length() - 1 ) == ';' || value.charAt( value.length() - 1 ) == ',' ) )
				value = value.substring( 0, value.length() - 1 );
			if( index == elementCount - 1 && stringArray.charAt( end - 1 ) == ';' && value.charAt( value.length() - 1 ) != ';' )
				tempStr.append(";");

			tempStr.replace(startIndex, endIndex, value);
			stringArray.replace(start, end, tempStr.toString());

			get().putString(key, stringArray.toString());
			get().flush();
		}
	}

	/*
	 * @Comments 获取xml设置文件中指定关键字 key 里的指定主键 target 所对应的数组中的第 index 个元素
	 * @Parameters key,target,index
	 * @NativeName: getArrayElement
	 * @NativeFunction: String getArrayElement(String,String,int)
	 */
	public static String getArrayElement( String key, String target, int index ) {
		String str = get().getString( key, "" );
		StringBuilder stringArray = new StringBuilder( str );
		String[] result = exist( stringArray.toString(), target );
		int start = Integer.valueOf( result[0] );
		int end = Integer.valueOf( result[1] );

		if( end > 0 ){
			int count = 0;
			int startIndex = 0;
			int endIndex = 0;
			StringBuilder tempStr = new StringBuilder( result[2] );

			for (int i = 0; i < ( end - start ); i++) {
				if (tempStr.charAt(i) == ',') {
					if (count == index) {
						break;
					} else {
						startIndex = i;
						count++;
					}
				}
			}

			endIndex = tempStr.indexOf(",", startIndex + 1 );
			if( endIndex == -1 )
				endIndex = tempStr.length();
			startIndex = index == 0 ? 0 : startIndex + 1;

			if( tempStr.indexOf(",") == -1 )
				startIndex = 0;

			return tempStr.substring( startIndex, endIndex ).replace(";","");
		}

		return null;
	}

	/*
	 * @Comments 获取xml设置文件中指定关键字 key 里的指定主键 target 所对应的数组
	 * @Parameters key,target
	 * @NativeName: getArray
	 * @NativeFunction: String getArray(String,String)
	 */
	public static String getArray( String key, String target ) {
		String str = get().getString( key, "" );
		StringBuilder stringArray = new StringBuilder( str );
		String[] result = exist( stringArray.toString(), target );

		if( Integer.valueOf( result[1] ) > 0 ){
			return result[2];
		}

		return null;
	}

	/*
	 * @Comments 查询xml设置文件中指定关键字 key 里的指定主键 target 所对应的数组是否存在
	 * @Parameters key,target
	 * @NativeName: query
	 * @NativeFunction: boolean query(String,String)
	 */
	public static boolean query( String key, String target ) {
		return Integer.valueOf( exist(key,target)[1] ) > 0;
	}

	/*
	 * @Comments 查询字符串 str 中是否包含指定主键 target 所对应的数组，并返回一个字符串数组
	 * 返回值内容:
	 * 指定主键 target 所对应的数组的起始位置，终止位置，数组本身
	 * @Parameters str,target
	 * @NativeName: exist
	 * @NativeFunction: String[] exist(String,String)
	 */
	public static String[] exist( String str, String target ) {
		StringBuilder stringArray = new StringBuilder( str );
		int start = -1;
		boolean check = false;
		String targetString = null;
		int end = -1;

		if ( str != null && ( ( start = stringArray.indexOf( target ) ) != -1 ) ) {
			while ( !check ) {
				end = stringArray.indexOf(";", start) + 1;
				if( end <= 0 )
					break;

				targetString = stringArray.substring(start, end);

				if ( target.equals( targetString.split(",")[0].replace(";","") ) ) {
					check = true;
					if( targetString.contains(";") && !targetString.contains(",") ){
						end -= 1;
						targetString = stringArray.substring( start, end );
					}
				}else {
					start = end;
				}
			}
		}

		return new String[]{ String.valueOf( start ), String.valueOf( end ), targetString };
	}
}
