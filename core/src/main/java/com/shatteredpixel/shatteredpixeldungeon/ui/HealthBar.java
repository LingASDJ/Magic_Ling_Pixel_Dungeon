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

package com.shatteredpixel.shatteredpixeldungeon.ui;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.Game;
import com.watabou.noosa.ui.Component;

public class HealthBar extends Component {

	private static final int COLOR_BG		= 0xFFCC0000;
	private static final int COLOR_HP		= 0xFF00EE00;
	private static final int COLOR_SHLD 	= 0xFFBBEEBB;
	// 残血暗底色
	private static final int COLOR_LOST	= 0xAAC7FA;

	private static final int HEIGHT	= 2;
	// 残血消退动画速度，越大越快
	private static final float ANIM_SPEED = 1.5f;

	private ColorBlock Bg;
	private ColorBlock HpLost;	// 血量残迹条
	private ColorBlock ShldLost;// 护盾残迹条
	private ColorBlock Shld;
	private ColorBlock Hp;

	// 当前目标比例
	private float targetHealth;
	private float targetShield;
	// 残条实时缩放缓存
	private float lostHealthScale;
	private float lostShieldScale;

	@Override
	protected void createChildren() {
		Bg = new ColorBlock( 1, 1, COLOR_BG );
		add( Bg );

		// 残血条放在最底层，在实血下方
		HpLost = new ColorBlock(1, 1, COLOR_LOST);
		add(HpLost);
		ShldLost = new ColorBlock(1, 1, COLOR_LOST);
		add(ShldLost);

		Shld = new ColorBlock( 1, 1, COLOR_SHLD );
		add( Shld );

		Hp = new ColorBlock( 1, 1, COLOR_HP );
		add( Hp );

		height = HEIGHT;

		// 初始满血
		lostHealthScale = 1f;
		lostShieldScale = 1f;
	}

	@Override
	protected void layout() {
		Bg.x = Shld.x = Hp.x = HpLost.x = ShldLost.x = x;
		Bg.y = Shld.y = Hp.y = HpLost.y = ShldLost.y = y;

		Bg.size( width, height );

		// 像素对齐修正逻辑保留原版
		float pixelWidth = width;
		if (camera() != null) pixelWidth *= camera().zoom;

		// 实血、护盾使用目标值
		float shldW = width * (float)Math.ceil(targetShield * pixelWidth) / pixelWidth;
		float hpW = width * (float)Math.ceil(targetHealth * pixelWidth) / pixelWidth;
		Shld.size(shldW, height);
		Hp.size(hpW, height);

		// 残迹条使用平滑后的scale
		float lostShldW = width * lostShieldScale;
		float lostHpW = width * lostHealthScale;
		ShldLost.size(lostShldW, height);
		HpLost.size(lostHpW, height);
	}

	@Override
	public void update() {
		super.update();
		boolean needRelayout = false;

		// 残血条仅当残迹大于目标时平滑消退（扣血动画），回血直接同步
		if (lostHealthScale > targetHealth) {
			lostHealthScale -= (lostHealthScale - targetHealth) * ANIM_SPEED * Game.elapsed;
			needRelayout = true;
		} else if (lostHealthScale < targetHealth) {
			lostHealthScale = targetHealth;
			needRelayout = true;
		}

		if (lostShieldScale > targetShield) {
			lostShieldScale -= (lostShieldScale - targetShield) * ANIM_SPEED * Game.elapsed;
			needRelayout = true;
		} else if (lostShieldScale < targetShield) {
			lostShieldScale = targetShield;
			needRelayout = true;
		}

		// 只有缩放变化才重绘，节省性能
		if (needRelayout) layout();
	}

	public void level( float value ) {
		level( value, 0f );
	}

	public void level( float health, float shield ){
		this.targetHealth = health;
		this.targetShield = shield;
		layout();
	}

	public void level(Char c){
		float health = c.HP;
		float shield = c.shielding();
		float max = Math.max(health+shield, c.HT);

		level(health/max, (health+shield)/max);
	}
}