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

package com.shatteredpixel.shatteredpixeldungeon.windows;

import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.HealthBar;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.watabou.noosa.ui.Component;

public class WndInfoMob extends WndTitledMessage {
	
	public WndInfoMob( Mob mob ) {

		super( new MobTitle( mob ), mob.info() );
		
	}
	
	private static class MobTitle extends Component {

		private static final int GAP	= 2;
		
		private CharSprite image;
		private RenderedTextBlock name;
		private RenderedTextBlock info;
		private RenderedTextBlock infoB;
		private HealthBar health;
		private BuffIndicator buffs;
		
		public MobTitle( Mob mob ) {
			
			name = PixelScene.renderTextBlock( Messages.titleCase( mob.name() )+"("+mob.HP+"/"+mob.HT+")", 9 );
			name.hardlight( TITLE_COLOR );
			add( name );

			info =
					PixelScene.renderTextBlock( Messages.get( WndInfoMob.class,"dsinfo" )+mob.defenseSkill+"|"+Messages.get( WndInfoMob.class,"maxinfo" )+mob.maxLvl+"|"+Messages.get( WndInfoMob.class,"getexp" )+mob.EXP, 5 );
			info.hardlight( 0xffff00);
			add( info );

			infoB =
					PixelScene.renderTextBlock( Messages.get( WndInfoMob.class,"itm" )+ mob.lootChance()+"|"+Messages.get( WndInfoMob.class,"getdmg" )+mob.damageRoll()+"|"+Messages.get( WndInfoMob.class,"shield" )+mob.drRoll(), 5 );
			infoB.hardlight( 0xffff00);
			add( infoB );

			image = mob.sprite();
			add( image );

			health = new HealthBar();
			health.level(mob);
			add( health );

			buffs = new BuffIndicator(mob);
			add( buffs );
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

			info.setRect(image.width() + GAP, health.bottom() + GAP, w, info.height());

			infoB.setRect(image.width() + GAP, info.bottom() + GAP, w, infoB.height());

			buffs.setPos(
				name.right() + GAP-1,
				name.bottom() - BuffIndicator.SACRIFICE-2
			);

			height = infoB.bottom();

		}
	}
}
