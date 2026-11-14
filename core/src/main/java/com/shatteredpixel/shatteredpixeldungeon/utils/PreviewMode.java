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

package com.shatteredpixel.shatteredpixeldungeon.utils;

/**
 * Marks a read-only restore of another player's data (multiplayer /list,
 * ranking records).
 *
 * <p>{@code restoreFromBundle()} runs the same code as a real save load, and
 * that code has side effects meant for the active player: it unlocks badges,
 * shows badge banners, changes the global hero skin setting, registers action
 * button actions (e.g. Berserk / Combo), mutates quick slots and the item
 * catalog. When we restore somebody else's hero purely to display it, those
 * effects must not touch the local player's profile or HUD. Wrapping the
 * restore in this scope makes the guarded code paths no-ops.</p>
 *
 * <p>Scopes may nest; the preview only ends when the outermost {@link #end()}
 * runs.</p>
 */
public final class PreviewMode {

	// Restores also happen on the network thread (guest world bookkeeping).
	// Keep the scope per-thread so a background restore cannot suppress a
	// legitimate badge/action update on the render thread. Use the legacy
	// initialValue() form: ThreadLocal.withInitial() is not available on
	// Android below API 26 and the APK must keep minSdk 21.
	private static final ThreadLocal<Integer> DEPTH = new ThreadLocal<Integer>() {
		@Override
		protected Integer initialValue() {
			return 0;
		}
	};

	private PreviewMode() {
	}

	public static void begin() {
		DEPTH.set(DEPTH.get() + 1);
	}

	public static void end() {
		int depth = DEPTH.get();
		if (depth > 0) DEPTH.set(depth - 1);
	}

	public static boolean active() {
		return DEPTH.get() > 0;
	}
}
