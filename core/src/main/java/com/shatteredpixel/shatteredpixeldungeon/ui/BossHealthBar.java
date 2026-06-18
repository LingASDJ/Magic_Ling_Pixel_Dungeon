/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2026 Evan Debenham
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

import static com.shatteredpixel.shatteredpixeldungeon.ui.Window.CYELLOW;
import static com.shatteredpixel.shatteredpixeldungeon.ui.Window.TITLE_COLOR;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BloodParticle;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndInfoMob;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.particles.Emitter;
import com.watabou.noosa.ui.Component;
import com.watabou.utils.Callback;

public class BossHealthBar extends Component {

	private Image bar;

	private Image shieldHP;
	private Image hp;
	// 动画残血条
	private Image hpLost;
	private Image shieldLost;

	private BitmapText hpText;

	private Button bossInfo;
	private BuffIndicator buffs;

	private static Mob boss;

	private Image skull;
	private Emitter blood;

	private static String asset = Assets.Interfaces.BOSSHP;

	private static BossHealthBar instance;
	private static boolean bleeding;

	private boolean large;
	private float time;

	// 动画速度，数值越大消退越快
	private static final float ANIM_SPEED = 1.5f;
	private float targetHealthScale;
	private float targetShieldScale;

	public BossHealthBar() {
		super();
		visible = active = (boss != null);
		instance = this;
		targetHealthScale = 1f;
		targetShieldScale = 1f;
	}

	@Override
	public synchronized void destroy() {
		super.destroy();
		if (instance == this) instance = null;
		if (buffs != null) BuffIndicator.setBossInstance(null);
	}

	@Override
	protected void createChildren() {
		this.large = SPDSettings.interfaceSize() != 0;

		// 1. 底层背景框（最先添加，层级最低）
		bar = large ? new Image(asset, 0, 16, 128, 30) : new Image(asset, 0, 0, 64, 16);
		add(bar);

		width = bar.width;
		height = bar.height;

		// 2. 残血条（底层血条，在实血下方）
		hpLost = large ? new Image(asset, 0, 46, 96, 9) : new Image(asset, 71, 0, 47, 4);
		hpLost.tint(0x000000, 0.5f);
		add(hpLost);

		shieldLost = large ? new Image(asset, 0, 55, 96, 9) : new Image(asset, 71, 5, 47, 4);
		shieldLost.tint(0x000000, 0.5f);
		add(shieldLost);

		// 3. 护盾条
		shieldHP = large ? new Image(asset, 0, 55, 96, 9) : new Image(asset, 71, 5, 47, 4);
		add(shieldHP);

		// 4. 血量条
		hp =  large ? new Image(asset, 0, 46, 96, 9) : new Image(asset, 71, 0, 47, 4);
		add(hp);

		// 5. 血量文字
		hpText = new BitmapText(PixelScene.pixelFont);
		hpText.alpha(0.6f);
		add(hpText);

		// 全屏点击按钮（透明遮罩）
		bossInfo = new Button(){
			@Override
			protected void onClick() {
				super.onClick();
				if (boss != null){
					GameScene.show(new WndInfoMob(boss));
				}
			}

			@Override
			protected String hoverText() {
				if (boss != null){
					return boss.name();
				}
				return super.hoverText();
			}
		};
		add(bossInfo);

		if (boss != null) {
			buffs = new BuffIndicator(boss, large);
			BuffIndicator.setBossInstance(buffs);
			add(buffs);
		}

		// 6. 骷髅图标
		if (boss != null && large) {
			skull = boss.sprite();
		} else {
			skull = new Image(asset, 64, 0, 6, 6);
		}
		add(skull);

		// 7. Buff指示器（顶层UI，血条永远挡不住buff）
		if (boss != null) {
			buffs = new BuffIndicator(boss, large);
			BuffIndicator.setBossInstance(buffs);
			add(buffs);
		}

		// 8. 流血粒子（最顶层特效）
		blood = new Emitter();
		blood.pos(skull);
		blood.pour(BloodParticle.FACTORY, 0.3f);
		blood.autoKill = false;
		blood.on = false;
		add( blood );

		// 创建完统一置顶buff，保证buff在所有血条上方
		forceBuffTopLayer();
	}

	// 强制BuffIndicator渲染在最上层，解决遮挡
	private void forceBuffTopLayer(){
		if (buffs != null){
			addToFront(buffs);
		}
		addToFront(blood);
	}

	@Override
	protected void layout() {
		bar.x = x;
		bar.y = y;

		// 所有血条X/Y统一对齐
		hp.x = shieldHP.x = hpLost.x = shieldLost.x = bar.x+(large ? 30 : 15);
		hp.y = shieldHP.y = hpLost.y = shieldLost.y = bar.y+(large ? 2 : 3);

		if (!large) hpText.scale.set(PixelScene.align(0.5f));
		hpText.x = hp.x + (large ? (96-hpText.width())/2f : 1);
		hpText.y = hp.y + (hp.height - (hpText.baseLine()+hpText.scale.y))/2f;
		hpText.y -= 0.001f;
		PixelScene.align(hpText);

		bossInfo.setRect(x, y, bar.width, bar.height);

		if (buffs != null) {
			if (large) {
				buffs.maxBuffs = 6;
				buffs.setRect(hp.x+1, hp.y + 12, 96, 34);
			} else {
				buffs.maxBuffs = 8;
				buffs.setRect(hp.x, hp.y + 5, 47, 16);
			}
		}

		int paneSize = large ? 30 : 16;
		skull.x = bar.x + (paneSize - skull.width())/2f;
		skull.y = bar.y + (paneSize - skull.height())/2f;

		// layout完成再次置顶buff
		forceBuffTopLayer();
	}

	@Override
	public void update() {
		super.update();
		if (boss != null){
			if (!boss.isAlive() || !Dungeon.level.mobs.contains(boss)){
				boss = null;
				visible = active = false;
				targetHealthScale = 0;
				targetShieldScale = 0;
				if (boss != null) {
					buffs = new BuffIndicator(boss, large);
					BuffIndicator.setBossInstance(buffs);
					add(buffs);
				}
				return;
			}

			int health = boss.HP;
			int shield = boss.shielding();
			int max = boss.HT;

			float healthPercent = health/(float)max;
			float shieldPercent = shield/(float)max;

			if (healthPercent + shieldPercent > 1f){
				float excess = healthPercent + shieldPercent;
				healthPercent /= excess;
				shieldPercent /= excess;
			}

			// 目标缩放值更新
			targetHealthScale = healthPercent;
			targetShieldScale = healthPercent + shieldPercent;

			// 实血条立即更新
			hp.scale.x = targetHealthScale;
			shieldHP.scale.x = targetShieldScale;

			if (buffs != null) {
				if (large) {
					buffs.maxBuffs = 6;
					buffs.setRect(hp.x+1, hp.y + 12, 96, 34);
				} else {
					buffs.maxBuffs = 8;
					buffs.setRect(hp.x, hp.y + 5, 47, 16);
				}
			}

			// 残血条平滑追赶（仅衰减动画，回血无动画）
			if (hpLost.scale.x > targetHealthScale) {
				hpLost.scale.x -= (hpLost.scale.x - targetHealthScale) * ANIM_SPEED * Game.elapsed;
			} else {
				hpLost.scale.x = targetHealthScale;
			}

			if (shieldLost.scale.x > targetShieldScale) {
				shieldLost.scale.x -= (shieldLost.scale.x - targetShieldScale) * ANIM_SPEED * Game.elapsed;
			} else {
				shieldLost.scale.x = targetShieldScale;
			}

			// 流血特效逻辑
			if (bleeding != blood.on){
				if (bleeding)   skull.tint( 0xcc0000, large ? 0.3f : 0.6f );
				else            skull.resetColor();
				blood.pos(skull);
				blood.on = bleeding;
				forceBuffTopLayer();
			}

			// 血量文字变色逻辑
			time += Game.elapsed / 3.5f;
			float r = 0.93f+0.57f*Math.max(0f, (float)Math.sin( time));
			float g = 0.53f+0.57f*Math.max(0f, (float)Math.sin( time - 10/Math.PI/5 ));
			float b = 0.03f+0.57f*Math.max(0f, (float)Math.sin( time + 4/Math.PI/2 ));

			if (hp.scale.x > 0.75f) {
				hpText.hardlight( TITLE_COLOR );
			} else if (hp.scale.x > 0.35f){
				hpText.hardlight( CYELLOW );
			} else {
				hpText.hardlight(r, g, b);
			}

			// 更新文字内容
			if (shield <= 0){
				hpText.text(health + "/" + max);
			} else {
				hpText.text(health + "+" + shield +  "/" + max);
			}
			hpText.measure();
			hpText.x = hp.x + (large ? (96-hpText.width())/2f : 1);

			// 不在update重复setRect，避免buff位置跳动
		} else {
			// 无Boss时重置残条动画
			hpLost.scale.x = 0;
			shieldLost.scale.x = 0;
		}
	}

	public static void assignBoss(Mob boss){
		if (BossHealthBar.boss == boss) {
			return;
		}
		BossHealthBar.boss = boss;
		bleed(false);
		if (instance != null) {
			ShatteredPixelDungeon.runOnRenderThread(new Callback() {
				@Override
				public void call() {
					instance.visible = instance.active = true;
					// 切换Boss重置残条动画
					instance.targetHealthScale = 1f;
					instance.targetShieldScale = 1f;
					instance.hpLost.scale.x = 1f;
					instance.shieldLost.scale.x = 1f;

					if (boss != null){
						if (instance.large){
							if (instance.skull != null){
								instance.remove(instance.skull);
								instance.skull.destroy();
							}
							instance.skull = boss.sprite();
							instance.add(instance.skull);
						}
						// 重建Buff指示器
						if (instance.buffs != null){
							instance.remove(instance.buffs);
							instance.buffs.destroy();
						}
						instance.buffs = new BuffIndicator(boss, instance.large);
						BuffIndicator.setBossInstance(instance.buffs);
						instance.add(instance.buffs);

						// 重新布局并置顶buff
						instance.layout();
						instance.forceBuffTopLayer();
					}
				}
			});
		}
	}

	public static boolean isAssigned(){
		return boss != null && boss.isAlive() && Dungeon.level.mobs.contains(boss);
	}

	public static void bleed(boolean value){
		bleeding = value;
	}

	public static boolean isBleeding(){
		return isAssigned() && bleeding;
	}

}