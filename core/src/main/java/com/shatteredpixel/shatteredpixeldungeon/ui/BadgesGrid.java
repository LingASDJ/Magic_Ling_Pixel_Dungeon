/*  * Pixel Dungeon  * Copyright (C) 2012-2015 Oleg Dolya  *  * Shattered Pixel Dungeon  * Copyright (C) 2014-2024 Evan Debenham  *  * This program is free software: you can redistribute it and/or modify  * it under the terms of the GNU General Public License as published by  * the Free Software Foundation, either version 3 of the License, or  * (at your option) any later version.  *  * This program is distributed in the hope that it will be useful,  * but WITHOUT ANY WARRANTY; without even the implied warranty of  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the  * GNU General Public License for more details.  *  * You should have received a copy of the GNU General Public License  * along with this program.  If not, see <http://www.gnu.org/licenses/>  */
package com.shatteredpixel.shatteredpixeldungeon.ui;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Chrome;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.effects.BadgeBanner;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;

import com.shatteredpixel.shatteredpixeldungeon.windows.WndBadge;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.ui.Component;
import com.watabou.utils.DeviceCompat;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class BadgesGrid extends Component {

	private static final float NAV_HEIGHT = 18;
	//==== 静态缓存：记住两种模式上次浏览页码，会话内永久保存，关闭游戏重置 ====
	private static int savedGlobalPage = 0;
	private static int savedHeroPage = 0;

	ArrayList<BadgeButton> badgeButtons;

	private StyledButton btnPrev;
	private StyledButton btnNext;
	private RenderedTextBlock pageLabel;

	private int pageIndex = 0;
	private int perPage;
	private int totalPages;
	private boolean isGlobalMode; //标记当前是global还是英雄个人徽章

	public BadgesGrid( boolean global ){
		super();
		isGlobalMode = global;
		badgeButtons = new ArrayList<>();

		if (DeviceCompat.isDesktop() && SPDSettings.interfaceSize() > 0 && Game.width > Game.height){
			perPage = 50 - ((Game.scene().getClass() == GameScene.class) ? 15 : 0);
		} else if (Game.width > Game.height){
			perPage = 40 - ((Game.scene().getClass() == GameScene.class) ? 5 : 0);
		} else {
			perPage = 30 - ((Game.scene().getClass() == GameScene.class) ? 5 : 0);
		}

		for (Badges.Badge badge : Badges.filterReplacedBadges( global )) {
			if (badge.type == Badges.BadgeType.HIDDEN) {
				continue;
			}

			BadgeButton button = new BadgeButton( badge, true );
			add( button );
			badgeButtons.add(button);
		}

		if (global) {
			ArrayList<Badges.Badge> lockedBadges = new ArrayList<>();
			for (Badges.Badge badge : Badges.Badge.values()) {
				if (badge.type != Badges.BadgeType.HIDDEN && !Badges.isUnlocked(badge)) {
					lockedBadges.add(badge);
				}
			}
			Badges.filterBadgesWithoutPrerequisites(lockedBadges);

			for (Badges.Badge badge : lockedBadges) {
				BadgeButton button = new BadgeButton( badge, false );
				add(button);
				badgeButtons.add(button);
			}
		}

		totalPages = Math.max( 1, (int)Math.ceil( badgeButtons.size() / (float)perPage ) );

		//翻页控件，修复图标方向！
		btnPrev = new StyledButton(Chrome.Type.TOAST_TR,"") {
			@Override
			protected void onClick() {
				super.onClick();
				changePage( -1 );
			}
		};
		btnPrev.icon(Icons.RIGHT_DICT.get());
		add( btnPrev );

		btnNext = new StyledButton(Chrome.Type.TOAST_TR,"") {
			@Override
			protected void onClick() {
				super.onClick();
				changePage( 1 );
			}
		};
		btnNext.icon(Icons.LEFT_DICT.get());
		add( btnNext );

		pageLabel = PixelScene.renderTextBlock( 8 );
		pageLabel.hardlight( Window.TITLE_COLOR );
		add( pageLabel );

		//==== 读取缓存页码，而不是固定0 ====
		int startPage = isGlobalMode ? savedGlobalPage : savedHeroPage;
		//做边界保护：缓存页码不能超出当前总页数
		startPage = Math.max(0, Math.min(startPage, totalPages - 1));
		showPage( startPage );
	}

	private void changePage( int delta ) {
		showPage( pageIndex + delta );
	}

	private void showPage( int index ) {
		if (index < 0) index = 0;
		if (index >= totalPages) index = totalPages - 1;
		pageIndex = index;

		//==== 更新静态缓存，保存当前页码 ====
		if (isGlobalMode){
			savedGlobalPage = pageIndex;
		} else {
			savedHeroPage = pageIndex;
		}

		int start = pageIndex * perPage;
		int end = Math.min( start + perPage, badgeButtons.size() );

		//只显示当前页的徽章
		for (int i = 0; i < badgeButtons.size(); i++) {
			BadgeButton b = badgeButtons.get( i );
			b.visible = b.active = (i >= start && i < end);
		}

		boolean multiPage = totalPages > 1;
		pageLabel.visible = pageLabel.active = multiPage;
		btnPrev.visible = btnPrev.active = multiPage && pageIndex > 0;
		btnNext.visible = btnNext.active = multiPage && pageIndex < totalPages - 1;

		if (multiPage) pageLabel.text( (pageIndex + 1) + "/" + totalPages );

		layout();
	}

	@Override
	protected void layout() {
		super.layout();

		//翻页控件固定在底部
		btnNext.setRect( x + width - NAV_HEIGHT, y + height - NAV_HEIGHT, NAV_HEIGHT, NAV_HEIGHT );
		btnPrev.setRect( x, y + height - NAV_HEIGHT, NAV_HEIGHT, NAV_HEIGHT );

		pageLabel.setPos(
				x + (width - pageLabel.width()) / 2,
				y + height - NAV_HEIGHT + (NAV_HEIGHT - pageLabel.height()) / 2 );
		PixelScene.align( pageLabel );

		//只对当前页的徽章做布局
		ArrayList<BadgeButton> visible = new ArrayList<>();
		for (BadgeButton b : badgeButtons) {
			if (b.visible) visible.add( b );
		}

		if (visible.isEmpty()) return;

		//原有的网格自适应算法完全保留
		float badgeArea = (float) Math.sqrt( (width * (height - NAV_HEIGHT)) / visible.size() );
		int nCols = Math.max( 1, Math.round( width / badgeArea ) );

		int nRows = (int) Math.ceil( visible.size() / (float) nCols );
		float badgeWidth  = width() / nCols;
		float badgeHeight = (height() - NAV_HEIGHT) / nRows;

		for (int i = 0; i < visible.size(); i++){
			int row = i / nCols;
			int col = i % nCols;
			BadgeButton button = visible.get( i );
			button.setPos(
					left() + col * badgeWidth  + (badgeWidth  - button.width())  / 2,
					top()  + row * badgeHeight + (badgeHeight - button.height()) / 2 );
			PixelScene.align( button );
		}
	}

	private static class BadgeButton extends Button {

		private Badges.Badge badge;
		private boolean unlocked;

		private Image icon;

		public BadgeButton( Badges.Badge badge, boolean unlocked ) {
			super();

			this.badge = badge;
			this.unlocked = unlocked;

			icon = BadgeBanner.image( badge.image );
			if (!unlocked) {
				icon.brightness( 0.4f );
			}
			add( icon );

			setSize( icon.width(), icon.height() );
		}

		@Override
		protected void layout() {
			super.layout();

			icon.x = x + (width - icon.width()) / 2;
			icon.y = y + (height - icon.height()) / 2;
		}

		@Override
		public void update() {
			super.update();

			if (unlocked && Random.Float() < Game.elapsed * 0.1) {
				BadgeBanner.highlight( icon, badge.image );
			}
		}

		@Override
		protected void onClick() {
			Sample.INSTANCE.play( Assets.Sounds.CLICK, 0.7f, 0.7f, 1.2f );
			Game.scene().addToFront( new WndBadge( badge, unlocked ) );
		}

		@Override
		protected String hoverText() {
			return badge.title();
		}
	}
}
