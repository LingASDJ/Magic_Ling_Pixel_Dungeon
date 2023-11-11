/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2022 Evan Debenham
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
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BloodParticle;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndInfoMob;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.Visual;
import com.watabou.noosa.particles.Emitter;
import com.watabou.noosa.ui.Component;

public class BossHealthBar extends Component {

	private Image bar;

	private Image rawShielding;
	private Image shieldedHP;
	private Image hp;
	private BitmapText hpText;

	private Button bossInfo;
	private BuffIndicator buffs;

	private static Mob boss;

	private Image skull;
	private Emitter blood;

	private static String asset = Assets.Interfaces.BOSSHP;

	private static BossHealthBar instance;
	private static boolean bleeding;

	public BossHealthBar() {
		super();
		visible = active = (boss != null);
		instance = this;
	}

	@Override
	public synchronized void destroy() {
		super.destroy();
		if (instance == this) instance = null;
		if (buffs != null) BuffIndicator.setBossInstance(null);
	}

	@Override
	protected void createChildren() {
		bar = new Image(asset, 0, 0, 64, 16);
		add(bar);

		width = bar.width;
		height = bar.height;

		rawShielding = new Image(asset, 15, 25, 47, 4);
		rawShielding.alpha(0.5f);
		add(rawShielding);

		shieldedHP = new Image(asset, 15, 25, 47, 4);
		add(shieldedHP);

		hp = new Image(asset, 15, 19, 47, 4);
		add(hp);

		hpText = new BitmapText(PixelScene.pixelFont);
		hpText.alpha(0.6f);
		add(hpText);

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
			buffs = new BuffIndicator(boss, false);
			BuffIndicator.setBossInstance(buffs);
			add(buffs);
		}

		skull = new Image(asset, 5, 18, 6, 6);
		add(skull);

		blood = new Emitter();
		blood.pos(skull);
		blood.pour(BloodParticle.FACTORY, 0.3f);
		blood.autoKill = false;
		blood.on = false;
		add( blood );
	}

	@Override
	protected void layout() {
		bar.x = x;
		bar.y = y;

		hp.x = shieldedHP.x = rawShielding.x = bar.x+15;
		hp.y = shieldedHP.y = rawShielding.y = bar.y+3;

		hpText.scale.set(PixelScene.align(0.5f));
		hpText.x = hp.x + 1;
		hpText.y = hp.y + (hp.height - (hpText.baseLine()+hpText.scale.y))/2f;
		hpText.y -= 0.001f; //prefer to be slightly higher
		PixelScene.align(hpText);

		bossInfo.setRect(x, y, bar.width, bar.height);

		if (buffs != null) {
			buffs.setRect(hp.x, hp.y + 5, 110, 7);
		}

		skull.x = bar.x+5;
		skull.y = bar.y+5;
	}
	private float time;
	@Override
	public void update() {
		super.update();
		if (boss != null){
			if (!boss.isAlive() || !Dungeon.level.mobs.contains(boss)){
				boss = null;
				visible = active = false;
			} else {

				float health = boss.HP;
				float shield = boss.shielding();
				float max = boss.HT;
				hp.scale.x = Math.max( 0, (health-shield)/max);
				shieldedHP.scale.x = health/max;
				rawShielding.scale.x = shield/max;


				if (hp.scale.x < 0.25f){
					bleed( true );
				}

				if (shield <= 0){
					hpText.text(health + "/" + max);
				}
				else {
					hpText.text(health + "+" + shield +  "/" + max);
				}

				//低于75%渲染成蓝色 低于35%渲染成红色
				//Boss血量文本显示
				Visual visual = new Visual(0,0,0,0);
				visual.am = 1f + 0.01f*Math.max(0f, (float)Math.sin( time += Game.elapsed ));
				time += Game.elapsed / 3.5f;;
				float r = 0.93f+0.57f*Math.max(0f, (float)Math.sin( time));
				float g = 0.53f+0.57f*Math.max(0f, (float)Math.sin( time - 10/Math.PI/5 ));
				float b = 0.03f+0.57f*Math.max(0f, (float)Math.sin( time + 4/Math.PI/2 ));

				if (hp.scale.x > 0.75f) {
					hpText.hardlight( TITLE_COLOR );
				} else if (hp.scale.x > 0.35f){
					hpText.hardlight( CYELLOW );
				} else {
					hpText.hardlight( r,g,b );
					hpText.text(health + "+" + shield +  "/" + max);
				}

				if (bleeding != blood.on){
					if (bleeding)   skull.tint( 0xcc0000, 0.5f );
					else            skull.resetColor();
					blood.on = bleeding;
				}
			}
		}
	}

	public static void assignBoss(Mob boss){
		if (BossHealthBar.boss == boss) {
			return;
		}
		BossHealthBar.boss = boss;
		bleed(false);
		if (instance != null) {
			instance.visible = instance.active = true;
			if (boss != null){
				if (instance.buffs != null){
					instance.buffs.killAndErase();
				}
				instance.buffs = new BuffIndicator(boss, false);
				BuffIndicator.setBossInstance(instance.buffs);
				instance.add(instance.buffs);
				instance.layout();
			}
		}
	}

	public static boolean isAssigned(){
		return boss != null && boss.isAlive() && Dungeon.level.mobs.contains(boss);
	}

	public static void bleed(boolean value){
		bleeding = value;
	}

	public static boolean isBleeding(){
		return bleeding;
	}

}