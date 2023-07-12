/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2021 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.windows;

import static com.shatteredpixel.shatteredpixeldungeon.SPDSettings.ATBSettings;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIcon;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.HealthBar;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.Image;
import com.watabou.noosa.ui.Component;

public class WndInfoMob extends WndTitledMessage {

	public WndInfoMob( Mob mob ) {

		super( new MobTitle( mob ), mob.info() );

	}

	private static class MobTitle extends Component {

		private static final int GAP	= 2;

		private final CharSprite image;
		private final RenderedTextBlock name;
		private final HealthBar health;
		private final BuffIndicator buffs;

		public MobSixInfo mobSixInfo;

		private String DKLevel(Mob mob) {
			String level;
			if(mob.defenseSkill > 23){
				level = "S";
			} else if (mob.defenseSkill > 20) {
				level = "A+";
			} else if (mob.defenseSkill > 15) {
				level = "A";
			} else if (mob.defenseSkill > 10) {
				level = "B+";
			} else if (mob.defenseSkill > 5) {
				level = "B";
			} else if (mob.defenseSkill > 2) {
				level = "C";
			} else {
				level = "D";
			}
			return level;
		}

		private String SPLevel(Mob mob) {
			String level;
			if(mob.speed() == 1){
				level = "C";
			} else if (mob.speed() >= 2) {
				level = "S";
			} else if (mob.speed() >= 1.5) {
				level = "A+";
			} else if (mob.speed() > 1.0) {
				level = "A";
			} else if (mob.speed() < 0.8) {
				level = "D+";
			} else if (mob.speed() < 0.5) {
				level = "D";
			} else {
				level = "D-";
			}
			return level;
		}


		private String AttackDelayLevel(Mob mob) {
			String level;
			if(mob.attackDelay() == 1){
				level = "C";
			} else if (mob.attackDelay() <= 0.8) {
				level = "B+";
			} else if (mob.attackDelay() <= 0.6) {
				level = "B";
			} else if (mob.attackDelay() <= 0.5) {
				level = "A";
			} else if (mob.attackDelay() <= 0.4) {
				level = "A+";
			} else if (mob.attackDelay() <= 0.3) {
				level = "S";
			} else {
				level = "D";
			}
			return level;
		}

		private String HPLevel(Mob mob) {
			String level;
			if(mob.HP>600){
				level = "S+";
			} else if (mob.HP>500){
				level = "S";
			} else if (mob.HP>400){
				level = "S-";
			} else if (mob.HP>100) {
				level = "A+";
			} else if (mob.HP>50) {
				level = "A-";
			} else if (mob.HP>40){
				level = "B";
			} else if (mob.HP>30){
				level = "C";
			} else if (mob.HP>20) {
				level = "D";
			} else if (mob.HP>10) {
				level = "E";
			} else if (mob.HP==1) {
				level = "G";
			} else {
				level = "F";
			}
			return level;
		}

		private String ProName(Mob mob) {
			String level;
			if (mob.properties.contains(Char.Property.BOSS)){
				level = "领袖";
			} else if (mob.properties.contains(Char.Property.MINIBOSS)){
				level = "精英";
			} else if (mob.properties.contains(Char.Property.ABYSS)){
				level = "深渊";
			} else if (mob.properties.contains(Char.Property.UNDEAD)){
				level = "亡灵";
			} else if (mob.properties.contains(Char.Property.DEMONIC)){
				level = "恶魔";
			} else if (mob.properties.contains(Char.Property.NPC)){
				level = "中立";
			} else if (mob.properties.contains(Char.Property.FIERY) || mob.properties.contains(Char.Property.ICY) || mob.properties.contains(Char.Property.ELECTRIC)){
				level = "元素";
			} else {
				level = "普通";
			}
			return level;
		}

		private String MaxLevelName(Mob mob) {
			String level;

			if(Dungeon.hero.lvl <= mob.maxLvl || mob.properties.contains(Char.Property.BOSS) || mob.properties.contains(Char.Property.MINIBOSS)){
				level = "可掉落";
			} else {
				level = "不掉落";
			}
			return level;
		}

		public MobTitle( Mob mob ) {

			name = PixelScene.renderTextBlock( Messages.titleCase( mob.name() ), 9 );
			name.hardlight( TITLE_COLOR );
			add( name );

			image = mob.sprite();
			add( image );

			health = new HealthBar();
			health.level(mob);
			add( health );

			buffs = new BuffIndicator( mob,false );
			add( buffs );

			mobSixInfo = new MobSixInfo(mob);
			add(mobSixInfo);

			mobSixInfo.info1 = PixelScene.renderTextBlock((ATBSettings() ? String.valueOf(mob.HP) : HPLevel(mob)),6);
			//String.valueOf((double)Math.round(mob.attackDelay() * 10) /10)
			mobSixInfo.info2 = PixelScene.renderTextBlock(ATBSettings() ?
					String.valueOf((double)Math.round(mob.attackDelay() * 10) /10) : AttackDelayLevel(mob),6);

			mobSixInfo.info3 = PixelScene.renderTextBlock( ATBSettings() ? String.valueOf(mob.maxLvl) : MaxLevelName(mob),5);
			mobSixInfo.info4 = PixelScene.renderTextBlock(String.valueOf(mob.drRoll()),6);

			mobSixInfo.info5 = PixelScene.renderTextBlock(ATBSettings() ? String.valueOf(mob.defenseSkill) : DKLevel(mob),6);
			mobSixInfo.info6 = PixelScene.renderTextBlock(ATBSettings() ?
					String.valueOf((double)Math.round(mob.speed()*10)/10): SPLevel(mob),6);

			mobSixInfo.info7 = PixelScene.renderTextBlock(ProName(mob),6);
			mobSixInfo.info8 = PixelScene.renderTextBlock(String.valueOf(mob.damageRoll()),6);

			add(mobSixInfo.info1);
			add(mobSixInfo.info2);
			add(mobSixInfo.info3);
			add(mobSixInfo.info4);
			add(mobSixInfo.info5);
			add(mobSixInfo.info6);
			add(mobSixInfo.info7);
			add(mobSixInfo.info8);
		}

		@Override
		protected void layout() {

			image.x = 0;
			image.y = Math.max( 0, name.height() + health.height() - image.height() );

			float w = width - image.width() - GAP;

			name.maxWidth((int)w);
			name.setPos(x + image.width + GAP,
					image.height() > name.height() ? y +(image.height() - name.height()) / 2 : y);

			health.setRect(image.width() + GAP, name.bottom() + GAP, w, health.height());

			buffs.setPos(
					name.right() + GAP-1,
					name.bottom() - BuffIndicator.SIZE_SMALL-2
			);

			height = health.bottom();

			mobSixInfo.setPos(-5,Math.max(health.bottom(),image.height()+5));
			mobSixInfo.layout();

			height = mobSixInfo.bottom();
		}

	}

	public static class MobSixInfo extends Component {

		public Mob mob;

		public ColorBlock colorBlock;

		public Image image1;
		public Image image2;
		public Image image3;
		public Image image4;
		public Image image5;
		public Image image6;
		public Image image7;
		public Image image8;

		public RenderedTextBlock info1;
		public RenderedTextBlock info2;
		public RenderedTextBlock info3;
		public RenderedTextBlock info4;
		public RenderedTextBlock info5;
		public RenderedTextBlock info6;
		public RenderedTextBlock info7;
		public RenderedTextBlock info8;

		public float WIDTH = 120f;
		public float GAP = 1f;

		public MobSixInfo(Mob mob) {
			this.mob = mob;
		}

		@Override
		public void createChildren() {
			super.createChildren();

			colorBlock = new ColorBlock(1,1, SPDSettings.ClassUI() ? 0xFF555555 : 0xFF462d00);
			add(colorBlock);

			image1 = new BuffIcon(68, false);
			image2 = new BuffIcon(69, false);
			image3 = new BuffIcon(70, false);
			image4 = new BuffIcon(71, false);
			image5 = new BuffIcon(72, false);
			image6 = new BuffIcon(73, false);
			image7 = new BuffIcon(74, false);
			image8 = new BuffIcon(75, false);
			add(image1);
			add(image2);
			add(image3);
			add(image4);
			add(image5);
			add(image6);
			add(image7);
			add(image8);
		}

		@Override
		public void layout() {
			colorBlock.x = 0;
			colorBlock.y = y;

			image1.x = image5.x = WIDTH * 0 / 4 + GAP;
			image2.x = image6.x = WIDTH * 1 / 4 + GAP;
			image3.x = image7.x = WIDTH * 2 / 4 + GAP;
			image4.x = image8.x = WIDTH * 3 / 4 + GAP;

			image1.y = image2.y = image3.y = image4.y = y + GAP;
			image5.y = image6.y = image7.y = image8.y = (image1.y + image1.height() + GAP * 2);

			info1.setPos((image2.x + image1.x + image1.width() - info1.width()) / 2,
					image1.y + image1.height() / 2-info1.height()/2);
			info2.setPos((image3.x + image2.x + image2.width() - info2.width()) / 2,image1.y + image1.height() / 2-info1.height()/2);
			info3.setPos((image4.x + image3.x + image3.width() - info3.width()) / 2,image1.y + image1.height() / 2-info1.height()/2);
			info4.setPos((WIDTH + image4.x + image4.width() - info4.width()) / 2,
					image1.y + image1.height() / 2-info1.height()/2);

			info5.setPos((image6.x + image5.x + image5.width() - info5.width()) / 2,image5.y + image5.height() / 2-info1.height()/2);
			info6.setPos((image7.x + image6.x + image6.width() - info6.width()) / 2,image5.y + image5.height() / 2-info1.height()/2);
			info7.setPos((image8.x + image7.x + image7.width() - info7.width()) / 2,image5.y + image5.height() / 2-info1.height()/2);
			info8.setPos((WIDTH + image8.x + image8.width() - info8.width()) / 2,image5.y + image5.height() / 2-info1.height()/2);

			colorBlock.size(WIDTH,2 * image8.height() + 4 * GAP);

			height = 2 * image8.height() + 4 * GAP;
		}
	}


}
