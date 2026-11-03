/*
 * Magic Ling Pixel Dungeon multiplayer
 * Copyright (C) 2026 knox <knox_asuma@163.com>
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

package com.shatteredpixel.shatteredpixeldungeon.services.multiplayer;

/** 联机服务入口（对照 services/daily 的 DailyImpl）。 */
public class MultiplayerImpl {

	private static final MultiplayerService service = new MultiplayerServiceImpl();

	/**
	 * 获取联机服务实例。
	 *
	 * @return 返回值。
	 */
	public static MultiplayerService getService() {
		return service;
	}

	/**
	 * 当前版本是否支持联机（恒为 true）。
	 *
	 * @return 返回值。
	 */
	public static boolean supportsMultiplayer() {
		return true;
	}
}
