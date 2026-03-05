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

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BloodParticle;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndInfoMob;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.Image;
import com.watabou.noosa.particles.Emitter;
import com.watabou.noosa.ui.Component;

public class BossHealthBar extends Component {

	// 每个血条的高度，用于垂直排列
	private static final int BAR_HEIGHT = 20;
	// 最大支持的Boss数量
	private static final int MAX_BOSSES = 4;

	// 单个血条的UI组件（数组形式）
	private Image[] bars;
	private Image[] rawShieldings;
	private Image[] shieldedHPs;
	private Image[] hps;
	private BitmapText[] hpTexts;
	private Button[] bossInfos;
	private BuffIndicator[] buffs;
	private Image[] skulls;
	private Emitter[] bloods;

	// 静态变量改为数组
	private static Mob[] bosses = new Mob[MAX_BOSSES];
	private static boolean[] bleeding = new boolean[MAX_BOSSES];

	private static String asset = Assets.Interfaces.BOSSHP;
	private static BossHealthBar instance;

	public BossHealthBar() {
		super();
		// 检查是否有活跃的Boss
		boolean hasActiveBoss = false;
		for (Mob boss : bosses) {
			if (boss != null) {
				hasActiveBoss = true;
				break;
			}
		}
		visible = active = hasActiveBoss;
		instance = this;
	}

	@Override
	public synchronized void destroy() {
		super.destroy();
		if (instance == this) instance = null;
		if (buffs != null) {
			for (int i = 0; i < MAX_BOSSES; i++) {
				BuffIndicator.setBossInstance(i, null);
			}
		}
	}

	@Override
	protected void createChildren() {
		// 初始化所有血条组件数组
		bars = new Image[MAX_BOSSES];
		rawShieldings = new Image[MAX_BOSSES];
		shieldedHPs = new Image[MAX_BOSSES];
		hps = new Image[MAX_BOSSES];
		hpTexts = new BitmapText[MAX_BOSSES];
		bossInfos = new Button[MAX_BOSSES];
		buffs = new BuffIndicator[MAX_BOSSES];
		skulls = new Image[MAX_BOSSES];
		bloods = new Emitter[MAX_BOSSES];

		for (int i = 0; i < MAX_BOSSES; i++) {
			// 血条背景
			bars[i] = new Image(asset, 0, 0, 64, 16);
			add(bars[i]);

			// 护盾底层
			rawShieldings[i] = new Image(asset, 15, 25, 47, 4);
			rawShieldings[i].alpha(0.5f);
			add(rawShieldings[i]);

			// 护盾血条
			shieldedHPs[i] = new Image(asset, 15, 25, 47, 4);
			add(shieldedHPs[i]);

			// 生命值血条
			hps[i] = new Image(asset, 15, 19, 47, 4);
			add(hps[i]);

			// 生命值文本
			hpTexts[i] = new BitmapText(PixelScene.pixelFont);
			hpTexts[i].alpha(0.6f);
			add(hpTexts[i]);

			// Boss信息按钮（闭包中使用final的i）
			final int index = i;
			bossInfos[i] = new Button() {
				@Override
				protected void onClick() {
					super.onClick();
					if (bosses[index] != null) {
						GameScene.show(new WndInfoMob(bosses[index]));
					}
				}

				@Override
				protected String hoverText() {
					if (bosses[index] != null) {
						return bosses[index].name();
					}
					return super.hoverText();
				}
			};
			add(bossInfos[i]);

			// Buff指示器
			if (bosses[i] != null) {
				buffs[i] = new BuffIndicator(bosses[i], false);
				BuffIndicator.setBossInstance(index, buffs[i]);
				add(buffs[i]);
				// 新增：强制刷新Buff布局
				buffs[i].needsRefresh = true;
				buffs[i].layout();
			}

			// 骷髅图标
			skulls[i] = new Image(asset, 5, 18, 6, 6);
			add(skulls[i]);

			// 流血特效
			bloods[i] = new Emitter();
			bloods[i].pos(skulls[i]);
			bloods[i].pour(BloodParticle.FACTORY, 0.3f);
			bloods[i].autoKill = false;
			bloods[i].on = false;
			add(bloods[i]);
		}

		// 设置组件总尺寸
		width = bars[0].width;
		height = BAR_HEIGHT * MAX_BOSSES;
	}

	@Override
	protected void layout() {
		// 为每个Boss血条布局，垂直排列
		for (int i = 0; i < MAX_BOSSES; i++) {
			if (bars[i] == null) continue;

			// 计算当前血条的Y坐标（垂直排列）
			float barY = y + (i * BAR_HEIGHT);

			// 血条背景位置
			bars[i].x = x;
			bars[i].y = barY;

			// 血条和护盾位置
			hps[i].x = shieldedHPs[i].x = rawShieldings[i].x = bars[i].x + 15;
			hps[i].y = shieldedHPs[i].y = rawShieldings[i].y = bars[i].y + 3;

			// 生命值文本位置
			hpTexts[i].scale.set(PixelScene.align(0.5f));
			hpTexts[i].x = hps[i].x + 1;
			hpTexts[i].y = hps[i].y + (hps[i].height - (hpTexts[i].baseLine() + hpTexts[i].scale.y)) / 2f;
			hpTexts[i].y -= 0.001f;
			PixelScene.align(hpTexts[i]);

			// 信息按钮区域
			bossInfos[i].setRect(x, barY, bars[i].width, bars[i].height);

			// Buff指示器位置
			if (buffs[i] != null) {
				buffs[i].setRect(hps[i].x, hps[i].y + 5, 47, 8);
				// 新增：确保BuffIndicator可见并刷新
				buffs[i].visible = true;
				buffs[i].needsRefresh = true;
			}

			// 骷髅图标位置
			skulls[i].x = bars[i].x + 5;
			skulls[i].y = bars[i].y + 5;

			// 流血特效位置
			bloods[i].pos(skulls[i]);
		}
	}

	@Override
	public void update() {
		super.update();
		boolean hasActiveBoss = false;

		// 更新每个Boss的血条状态
		for (int i = 0; i < MAX_BOSSES; i++) {
			Mob boss = bosses[i];
			if (boss != null) {
				hasActiveBoss = true;

				// 检查Boss是否存活
				if (!boss.isAlive() || !Dungeon.level.mobs.contains(boss)) {
					// 移除已死亡的Boss
					removeBoss(i);
					continue;
				}

				// 更新血条数值
				int health = boss.HP;
				int shield = boss.shielding();
				int max = boss.HT;

				hps[i].scale.x = Math.max(0, (health - shield) / (float) max);
				shieldedHPs[i].scale.x = health / (float) max;
				rawShieldings[i].scale.x = shield / (float) max;

				// 更新流血特效
				if (bleeding[i] != bloods[i].on) {
					if (bleeding[i]) {
						skulls[i].tint(0xcc0000, 0.6f);
					} else {
						skulls[i].resetColor();
					}
					bloods[i].on = bleeding[i];
				}

				// 更新生命值文本
				if (shield <= 0) {
					hpTexts[i].text(health + "/" + max);
				} else {
					hpTexts[i].text(health + "+" + shield + "/" + max);
				}

				if (buffs[i] != null) {
					buffs[i].needsRefresh = true;
					buffs[i].update();
					buffs[i].layout();
				}

				// 显示当前血条组件
				setComponentVisible(i, true);
			} else {
				// 隐藏空的血条组件
				setComponentVisible(i, false);
			}
		}

		BuffIndicator.refreshAllBosses();

		// 更新整体可见性
		visible = active = hasActiveBoss;
	}

	/**
	 * 设置指定索引的血条组件可见性
	 */
	private void setComponentVisible(int index, boolean visible) {
		bars[index].visible = visible;
		rawShieldings[index].visible = visible;
		shieldedHPs[index].visible = visible;
		hps[index].visible = visible;
		hpTexts[index].visible = visible;
		bossInfos[index].visible = visible;
		skulls[index].visible = visible;
		bloods[index].visible = visible;
		if (buffs[index] != null) {
			buffs[index].visible = visible;
		}
	}

	/**
	 * 分配Boss到第一个空的位置
	 */
	public static void assignBoss(Mob boss) {
		// 检查是否已存在
		for (int i = 0; i < MAX_BOSSES; i++) {
			if (bosses[i] == boss) {
				// 如果已存在，强制刷新其Buff
				if (instance != null && instance.buffs[i] != null) {
					instance.buffs[i].needsRefresh = true;
					instance.buffs[i].layout();
				}
				return;
			}
		}

		// 找到第一个空位置
		int emptyIndex = -1;
		for (int i = 0; i < MAX_BOSSES; i++) {
			if (bosses[i] == null) {
				emptyIndex = i;
				break;
			}
		}

		// 如果有空位置，分配Boss
		if (emptyIndex != -1) {
			bosses[emptyIndex] = boss;
			bleeding[emptyIndex] = false;

			if (instance != null) {
				instance.visible = instance.active = true;

				// 更新BuffIndicator
				if (instance.buffs[emptyIndex] != null) {
					instance.remove(instance.buffs[emptyIndex]);
					instance.buffs[emptyIndex].destroy();
				}
				instance.buffs[emptyIndex] = new BuffIndicator(boss, false);
				BuffIndicator.setBossInstance(emptyIndex, instance.buffs[emptyIndex]);
				instance.add(instance.buffs[emptyIndex]);

				instance.buffs[emptyIndex].needsRefresh = true;
				instance.buffs[emptyIndex].layout();  // 强制布局
				instance.buffs[emptyIndex].update();  // 强制更新

				instance.layout();

				// 全局刷新所有Boss Buff
				BuffIndicator.refreshAllBosses();
			}
		}
	}

	/**
	 * 移除指定的Boss
	 */
	public static void removeBoss(Mob boss) {
		for (int i = 0; i < MAX_BOSSES; i++) {
			if (bosses[i] == boss) {
				removeBoss(i);
				break;
			}
		}
	}

	/**
	 * 移除指定索引的Boss
	 */
	public static void removeBoss(int index) {
		if (index >= 0 && index < MAX_BOSSES) {
			bosses[index] = null;
			bleeding[index] = false;

			if (instance != null && instance.buffs[index] != null) {
				BuffIndicator.setBossInstance(index, null);
				instance.remove(instance.buffs[index]);
				instance.buffs[index].destroy();
				instance.buffs[index] = null;
			}

			// 移除后刷新剩余Boss的Buff
			BuffIndicator.refreshAllBosses();
		}
	}

	/**
	 * 检查是否有已分配的Boss
	 */
	public static boolean isAssigned() {
		for (Mob boss : bosses) {
			if (boss != null && boss.isAlive() && Dungeon.level.mobs.contains(boss)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 检查指定Boss是否已分配
	 */
	public static boolean isAssigned(Mob boss) {
		for (Mob b : bosses) {
			if (b == boss && b.isAlive() && Dungeon.level.mobs.contains(b)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 设置指定Boss的流血状态
	 */
	public static void bleed(Mob boss, boolean value) {
		for (int i = 0; i < MAX_BOSSES; i++) {
			if (bosses[i] == boss) {
				bleeding[i] = value;
				break;
			}
		}
	}

	/**
	 * 设置指定索引Boss的流血状态（兼容原有方法）
	 */
	public static void bleed(boolean value) {
		// 兼容原有调用方式，设置第一个Boss的流血状态
		if (MAX_BOSSES > 0) {
			bleeding[0] = value;
		}
	}

	/**
	 * 检查指定Boss是否在流血
	 */
	public static boolean isBleeding(Mob boss) {
		for (int i = 0; i < MAX_BOSSES; i++) {
			if (bosses[i] == boss) {
				return isAssigned(boss) && bleeding[i];
			}
		}
		return false;
	}

	/**
	 * 兼容原有方法
	 */
	public static boolean isBleeding() {
		return isAssigned() && bleeding[0];
	}

	/**
	 * 获取已分配的Boss数量
	 */
	public static int getBossCount() {
		int count = 0;
		for (Mob boss : bosses) {
			if (boss != null) {
				count++;
			}
		}
		return count;
	}

	public static void clearAllBossData() {
		for (int i = 0; i < MAX_BOSSES; i++) {
			bosses[i] = null;
			bleeding[i] = false;
		}
		if (instance != null) {
			instance.destroy();
			instance = null;
		}
	}


	/**
	 * 获取指定索引的Boss
	 */
	public static Mob getBoss(int index) {
		if (index >= 0 && index < MAX_BOSSES) {
			return bosses[index];
		}
		return null;
	}
}