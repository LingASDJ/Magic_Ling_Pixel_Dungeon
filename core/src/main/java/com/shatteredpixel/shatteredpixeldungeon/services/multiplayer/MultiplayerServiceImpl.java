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

/** MultiplayerService 的默认实现，委托给 multiplayer.Mp 单例。 */
public class MultiplayerServiceImpl extends MultiplayerService {

    /** Android 前台保活服务的桥接；桌面端不注册，调用为空操作。 */
    public interface KeepAliveListener {
        void setKeepAlive(boolean active);
    }

    private static volatile KeepAliveListener keepAliveListener;

    /** 注册 Android 前台保活服务回调（仅 Android 启动器调用）。 */
    public static void setKeepAliveListener(KeepAliveListener listener) {
        keepAliveListener = listener;
    }

    /** 通知平台更新前台保活状态；未注册监听器时无操作。 */
    public static void updateKeepAlive(boolean active) {
        KeepAliveListener listener = keepAliveListener;
        if (listener != null) {
            try {
                listener.setKeepAlive(active);
            } catch (Throwable ignored) {}
        }
    }

	/**
	 * GameScene 每帧调用：委托给联机核心。
	 *
	 * @param scene 参数。
	 */
	@Override
	public void tick(GameScene scene) {
		Mp.get().tick(scene);
	}

	/**
	 * TitleScene 每帧调用：委托给联机核心。
	 *
	 * @param scene 参数。
	 */
	@Override
	public void tickTitle(TitleScene scene) {
		Mp.get().tickTitle(scene);
	}

	/** GameScene.onPause 钩子：委托给联机核心保存访客英雄。 */
	@Override
	public void onGamePause() {
		Mp.get().onGamePause();
	}

	@Override
	public void onAppPaused() {
		Mp.get().onAppPaused();
	}

	@Override
	public void onAppResumed() {
		Mp.get().onAppResumed();
	}

	/**
	 * 访客连接中（用于 StartScene 拦截单独进入游戏）。
	 *
	 * @return 返回值。
	 */
	@Override
	public boolean isClientConnected() {
		return Mp.get().isConnected() && Mp.get().isClient();
	}
}
