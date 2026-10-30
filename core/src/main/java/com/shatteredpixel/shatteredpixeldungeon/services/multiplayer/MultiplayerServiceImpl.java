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

import com.shatteredpixel.shatteredpixeldungeon.multiplayer.Mp;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.TitleScene;

public class MultiplayerServiceImpl extends MultiplayerService {

	@Override
	public void tick(GameScene scene) {
		Mp.I.tick(scene);
	}

	@Override
	public void tickTitle(TitleScene scene) {
		Mp.I.tickTitle(scene);
	}

	@Override
	public void onGamePause() {
		Mp.I.onGamePause();
	}

	@Override
	public boolean isClientConnected() {
		return Mp.I.isConnected() && Mp.I.isClient();
	}
}
