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

import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.TitleScene;

/**
 * 联机服务接口。
 * 写法对照 services/daily/DailyService.java：
 *   - 抽象类；
 *   - 具体实现由 MultiplayerServiceImpl 提供；
 *   - 游戏核心只依赖这个接口。
 */
public abstract class MultiplayerService {

	/** 每帧调用（GameScene.update 开头） */
	public abstract void tick(GameScene scene);

	/** 每帧调用（TitleScene.update 开头） */
	public abstract void tickTitle(TitleScene scene);

	/** GameScene.onPause 开头调用（保存访客英雄等） */
	public abstract void onGamePause();

	/** StartScene 用来拦截"联机中单独进游戏" */
	public abstract boolean isClientConnected();
}
