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

package com.shatteredpixel.shatteredpixeldungeon.messages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.I18NBundle;
import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.watabou.utils.DeviceCompat;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IllegalFormatException;
import java.util.Locale;
import java.util.Properties;

/*
	Simple wrapper class for libGDX I18NBundles.

	The core idea here is that each string resource's key is a combination of the class definition and a local value.
	An object or static method would usually call this with an object/class reference (usually its own) and a local key.
	This means that an object can just ask for "name" rather than, say, "items.weapon.enchantments.death.name"
 */
public class Messages {

	private static ArrayList<I18NBundle> bundles;
	private static Languages lang;
	private static Locale locale;

	public static Languages lang(){
		return lang;
	}

	public static Locale locale(){
		return locale;
	}

	/**
	 * Setup Methods
	 */

	private static String[] prop_files = new String[]{
			Assets.Messages.ACTORS,
			Assets.Messages.ITEMS,
			Assets.Messages.JOURNAL,
			Assets.Messages.LEVELS,
			Assets.Messages.MISC,
			Assets.Messages.PLANTS,
			Assets.Messages.SCENES,
			Assets.Messages.UI,
			Assets.Messages.WINDOWS,
	};

	static{
		setup(SPDSettings.language());
	}

	public static void setup( Languages lang ){
		//seeing as missing keys are part of our process, this is faster than throwing an exception
		I18NBundle.setExceptionOnMissingKey(false);

		//store language and locale info for various string logic
		Messages.lang = lang;
		if (lang == Languages.ENGLISH){
			locale = Locale.ENGLISH;
		} else {
			locale = new Locale(lang.code());
		}

		//strictly match the language code when fetching bundles however
		bundles = new ArrayList<>();
		Locale bundleLocal = new Locale(lang.code());
		for (String file : prop_files) {
			bundles.add(I18NBundle.createBundle(Gdx.files.internal(file), bundleLocal));
		}
	}



	/**
	 * Resource grabbing methods
	 */
	public static String errorName;

	public static String get(String key, Object...args){
		return get(null, key, args);
	}

	public static String get(Object o, String k, Object...args){
		return get(o.getClass(), k, args);
	}

	public static String get(Class c, String k, Object...args) {
		return get(c, k, null, args);
	}

	private static String get(Class c, String k, String baseName, Object...args){
		String key;
		if (c != null){
			key = c.getName();
			key = key.replace("com.shatteredpixel.shatteredpixeldungeon.", "");
			key += "." + k;
		} else
			key = k;

		String value = getFromBundle(key.toLowerCase(Locale.CHINESE));
		if (value != null){
			if (args.length > 0) return format(value, args);
			else return value;
		}  else {
			//Use baseName so the missing string is clear what exactly needs replacing. Otherwise, it just says java.lang.Object.[key]
			if (baseName == null) {
				baseName = key;
				errorName = baseName;
				baseName = baseName.toLowerCase();
			}
			//this is so child classes can inherit properties from their parents.
			//in cases where text is commonly grabbed as a utility from classes that aren't mean to be instantiated
			//(e.g. flavourbuff.dispTurns()) using .class directly is probably smarter to prevent unnecessary recursive calls.

			if (c != null && c.getSuperclass() != null){
				return get(c.getSuperclass(), k, baseName, args);
			} else {
				//本地调试+桌面
				if (DeviceCompat.isDesktop_Dev()){
					System.out.println("[MissString]: "+baseName);
				}
				return "Ms:"+baseName;
			}

		}
	}

	private static String getFromBundle(String key){
		String result;
		for (I18NBundle b : bundles){
			result = b.get(key);
			//if it isn't the return string for no key found, return it
			if (result.length() != key.length()+6 || !result.contains(key)){
				return result;
			}
		}
		return null;
	}



	/**
	 * String Utility Methods
	 */

	public static String format( String format, Object...args ) {
		try {
			return String.format(Locale.ENGLISH, format, args);
		} catch (IllegalFormatException e) {
			ShatteredPixelDungeon.reportException( new Exception("formatting error for the string: " + format, e) );
			return format;
		}
	}

	private static HashMap<String, DecimalFormat> formatters = new HashMap<>();

	public static String decimalFormat( String format, double number ){
		if (!formatters.containsKey(format)){
			formatters.put(format, new DecimalFormat(format, DecimalFormatSymbols.getInstance(Locale.ENGLISH)));
		}
		return formatters.get(format).format(number);
	}

	public static String capitalize( String str ){
		if (str.length() == 0)  return str;
		else                    return str.substring( 0, 1 ).toUpperCase(locale) + str.substring( 1 );
	}

	//Words which should not be capitalized in title case, mostly prepositions which appear ingame
	//This list is not comprehensive!
	private static final HashSet<String> noCaps = new HashSet<>(
			Arrays.asList("a", "an", "and", "of", "by", "to", "the", "x", "for")
	);

	public static String titleCase( String str ){
		//English capitalizes every word except for a few exceptions
		if (lang == Languages.ENGLISH){
			String result = "";
			//split by any unicode space character
			for (String word : str.split("(?<=\\p{Zs})")){
				if (noCaps.contains(word.trim().toLowerCase(Locale.ENGLISH).replaceAll(":|[0-9]", ""))){
					result += word;
				} else {
					result += capitalize(word);
				}
			}
			//first character is always capitalized.
			return capitalize(result);
		}

		//Otherwise, use sentence case
		return capitalize(str);
	}

	public static String upperCase( String str ){
		return str.toUpperCase(locale);
	}

	public static String lowerCase( String str ){
		return str.toLowerCase(locale);
	}


	/**
	 * 计算指定语言的翻译进度
	 * 进度 = 目标语言中已翻译的 key 数量 / 中文源文件中 key 的总数量 × 100%
	 * 已翻译定义：key存在且值与【中文基准原文】不同（值相同则视为未翻译）
	 */
	public static double getTranslationProgress(Languages targetLang) {
		if (targetLang == Languages.CHINESE) {
			return 100.0; // 中文基准源语言，100%
		}

		try {
			// 加载【中文基准原文】作为对比基底
			HashSet<String> baseCNKeys = new HashSet<>();
			HashMap<String, String> baseCNValues = new HashMap<>();
			loadAllProperties(Languages.CHINESE, baseCNKeys, baseCNValues);

			if (baseCNKeys.isEmpty()) {
				return 0.0;
			}

			// 加载目标语言
			HashSet<String> targetKeys = new HashSet<>();
			HashMap<String, String> targetValues = new HashMap<>();
			loadAllProperties(targetLang, targetKeys, targetValues);

			int translatedCount = 0;
			for (String key : baseCNKeys) {
				String targetValue = targetValues.get(key);
				String baseValue = baseCNValues.get(key);

				if (targetValue != null && !targetValue.isEmpty()) {
					// 和中文基准原文不一样，才算翻译；相同则不算
					if (!targetValue.equals(baseValue)) {
						translatedCount++;
					}
				}
			}

			return (translatedCount * 100.0) / baseCNKeys.size();
		} catch (Exception e) {
			ShatteredPixelDungeon.reportException(e);
			return 0.0;
		}
	}

	/**
	 * 加载指定语言的所有 properties 文件中的 key-value
	 * 保留你的特殊规则：中文是无后缀 .properties
	 */
	private static void loadAllProperties(Languages lang, HashSet<String> keys, HashMap<String, String> values) {
		keys.clear();
		values.clear();

		for (String file : prop_files) {
			try {
				// 你的特殊命名规则保留
				String filePath;
				if (lang == Languages.CHINESE) {
					filePath = file + ".properties";
				} else {
					filePath = file + "_" + lang.code() + ".properties";
				}

				FileHandle handle = Gdx.files.internal(filePath);
				if (handle.exists()) {
					Properties props = new Properties();
					props.load(handle.reader());

					for (String key : props.stringPropertyNames()) {
						keys.add(key);
						values.put(key, props.getProperty(key));
					}
				}
			} catch (Exception e) {
				// 文件不存在/读取失败直接跳过
			}
		}
	}

	/**
	 * 获取翻译进度百分比字符串（保留两位小数）
	 */
	public static String getTranslationProgressString(Languages lang) {
		double progress = getTranslationProgress(lang);
		return String.format(Locale.ENGLISH, "%.2f%%", progress);
	}


}