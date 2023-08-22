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

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.SPDAction;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.Constants;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTerrainTilemap;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndBag;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndKeyBindings;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndQuickBag;
import com.watabou.gltextures.TextureCache;
import com.watabou.input.GameAction;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.Gizmo;
import com.watabou.noosa.Image;
import com.watabou.noosa.PointerArea;
import com.watabou.noosa.ui.Component;
import com.watabou.utils.Point;
import com.watabou.utils.PointF;

public class Toolbar extends Component {

	private Tool btnWait;
	private Tool btnSearch;
	private Tool btnInventory;
	private QuickslotTool[] btnQuick;

	private PickedUpItem pickedUp;

	private boolean lastEnabled = true;
	public boolean examining = false;

	private static Toolbar instance;

	public enum Mode {
		SPLIT,
		GROUP,
		CENTER
	}

	public Toolbar() {
		super();

		instance = this;

		height = btnInventory.height();
	}

	@Override
	protected void createChildren() {

		btnQuick = new QuickslotTool[Constants.MAX_QUICKSLOTS];

		for (int i = 0; i < btnQuick.length; i++) {
			btnQuick[i] = new QuickslotTool(64, 0, 22, 24, i);
			if (i < SPDSettings.quickslots()) {
				add(btnQuick[i]);
			}
		}

		add(btnWait = new Tool(24, 0, 20, 26) {
			@Override
			protected void onClick() {
				if (Dungeon.hero.ready && !GameScene.cancel()) {
					examining = false;
					Dungeon.hero.rest(false);
				}
			}

			@Override
			public GameAction keyAction() {
				return SPDAction.WAIT;
			}

			@Override
			protected String hoverText() {
				return Messages.titleCase(Messages.get(WndKeyBindings.class, "wait"));
			}

			protected boolean onLongClick() {
				if (Dungeon.hero.ready && !GameScene.cancel()) {
					examining = false;
					Dungeon.hero.rest(true);
				}
				return true;
			}
		});

		add(new Button(){
			@Override
			protected void onClick() {
				if (Dungeon.hero.ready && !GameScene.cancel()) {
					examining = false;
					Dungeon.hero.rest(true);
				}
			}

			@Override
			public GameAction keyAction() {
				if (btnWait.active) return SPDAction.REST;
				else				return null;
			}
		});

		add(btnSearch = new Tool(44, 0, 20, 26) {
			@Override
			protected void onClick() {
				if (Dungeon.hero.ready) {
					if (!examining && !GameScene.cancel()) {
						GameScene.selectCell(informer);
						examining = true;
					} else if (examining) {
						informer.onSelect(null);
						Dungeon.hero.search(true);
					}
				}
			}

			@Override
			public GameAction keyAction() {
				return SPDAction.EXAMINE;
			}

			@Override
			protected String hoverText() {
				return Messages.titleCase(Messages.get(WndKeyBindings.class, "examine"));
			}

			@Override
			protected boolean onLongClick() {
				Dungeon.hero.search(true);
				return true;
			}
		});

		add(btnInventory = new Tool(0, 0, 24, 26) {
			private CurrencyIndicator ind;

			private Image arrow;

			@Override
			protected void onClick() {
				if (Dungeon.hero.ready || !Dungeon.hero.isAlive()) {
					if (SPDSettings.interfaceSize() == 2) {
						GameScene.toggleInvPane();
					} else {
						if (!GameScene.cancel()) {
							GameScene.show(new WndBag(Dungeon.hero.belongings.backpack));
						}
					}
				}
			}

			@Override
			public GameAction keyAction() {
				return SPDAction.INVENTORY;
			}

			@Override
			protected String hoverText() {
				return Messages.titleCase(Messages.get(WndKeyBindings.class, "inventory"));
			}

			@Override
			protected boolean onLongClick() {
				GameScene.show(new WndQuickBag(null));
				return true;
			}

			@Override
			protected void createChildren() {
				super.createChildren();
				arrow = Icons.get(Icons.COMPASS);
				arrow.originToCenter();
				arrow.visible = SPDSettings.interfaceSize() == 2;
				arrow.tint(0x3D2E18, 1f);
				add(arrow);

				ind = new CurrencyIndicator();
				add(ind);
			}

			@Override
			protected void layout() {
				super.layout();
				ind.fill(this);

				arrow.x = left() + (width - arrow.width())/2;
				arrow.y = bottom()-arrow.height-1;
				arrow.angle = bottom() == camera().height ? 0 : 180;
			}
		});

		add(pickedUp = new PickedUpItem());
	}

	@Override
	protected void layout() {
		/**
		 * 现在的代码是为了适应不同的屏幕尺寸
		 * 但是这样做会导致在不同的屏幕尺寸下，按钮的位置不一样
		 * 为此使用三目运算符判定当前屏幕尺寸，然后根据不同的屏幕尺寸设置按钮的位置
		 */
		float wMin = Game.width / PixelScene.MIN_WIDTH_FULL;
		float hMin = Game.height / PixelScene.MIN_HEIGHT_FULL;
		final int maxHorizontalQuickslots = PixelScene.landscape() ? 9 : 3;
		float right = width;
		if (SPDSettings.interfaceSize() > 0){
			btnInventory.setPos(right - btnInventory.width(), y);
			btnWait.setPos(btnInventory.left() - btnWait.width(), y);
			btnSearch.setPos(btnWait.left() - btnSearch.width(), y);
			right = btnSearch.left();
			for(int i = btnQuick.length-1; i >= 0; i--) {
				if (i == btnQuick.length-1){
					btnQuick[i].border(0, 2);
					btnQuick[i].frame(106, 0, 19, 24);
				} else if (i == 0){
					btnQuick[i].border(2, 1);
					btnQuick[i].frame(86, 0, 20, 24);
				} else {
					btnQuick[i].border(0, 1);
					btnQuick[i].frame(88, 0, 18, 24);
				}
				btnQuick[i].setPos(right-btnQuick[i].width(), y+2);
				right = btnQuick[i].left();
			}
			return;
		}

		for (int i = 0; i < btnQuick.length; i++) {
			if (i < SPDSettings.quickslots()) {
				if (btnQuick[i] == null) btnQuick[i] = new QuickslotTool(64, 0, 24, 24, i);
				add(btnQuick[i]);
			} else {
				remove(btnQuick[i]);
			}
		}

		for(int i = 0; i < (Math.min(wMin, hMin) >= 2*Game.density ? Constants.MOX_QUICKSLOTS :
				Constants.MAX_QUICKSLOTS); i++) {
			//FIXME doesn't work for portrait mode and no longer dynamically resizes.
			if (i == 0 && !SPDSettings.flipToolbar() ||
					i == Math.min(SPDSettings.quickslots(), maxHorizontalQuickslots)-1 && SPDSettings.flipToolbar()) {
				btnQuick[i].border(0, 2);
				btnQuick[i].frame(86, 0, 20, 24);
			} else if (i == 0 && SPDSettings.flipToolbar() ||
					i == Math.min(SPDSettings.quickslots(), maxHorizontalQuickslots)-1 && !SPDSettings.flipToolbar()) {
				btnQuick[i].border(2, 1);
				btnQuick[i].frame(86, 0, 20, 24);
			} else {
				btnQuick[i].border(2, 2);
				btnQuick[i].frame(64, 0, 22, 24);
			}

		}
		float startX, startY;
		switch(Mode.valueOf(SPDSettings.toolbarMode())){
			case SPLIT:
			case GROUP:
			case CENTER:
				btnWait.setPos(x, y);
				btnSearch.setPos(btnWait.right(), y);

				btnInventory.setPos(right - btnInventory.width(), y);

				startX = btnInventory.left() - btnQuick[0].width();
				for (int i = 0; i < maxHorizontalQuickslots; i++) {
					QuickslotTool tool = btnQuick[i];

					//修复反向工具栏的错误
					if (!SPDSettings.flipToolbar()) {
						tool.setPos(startX, y + 2);
					} else if (i >= 1) {
						tool.setPos(startX, y + 2);
					} else {
						tool.setPos(startX-16, y + 2);
					}

					if (i + 1 < btnQuick.length) {
						startX = btnQuick[i].left() - btnQuick[i+1].width();
					}
				}

				startY = SPDSettings.scale() >= 4 ? 38 : 50;
				for (int i = maxHorizontalQuickslots; i < btnQuick.length; i++) {
					QuickslotTool tool = btnQuick[i];
					tool.setPos(width - (tool.width() + 2), startY);
					if (i + 1 < btnQuick.length) {
						startY = btnQuick[i].bottom();
					}
				}


				//center the quickslots if they
				if (btnQuick[btnQuick.length-1].left() < btnSearch.right()){
					float diff = Math.round(btnSearch.right() - btnQuick[btnQuick.length-1].left())/2f;
					for( int i = 0; i < maxHorizontalQuickslots; i++){
						btnQuick[i].setPos( btnQuick[i].left()+diff, btnQuick[i].top() );
					}
				}
				break;

			//center = group but.. well.. centered, so all we need to do is pre-emptively set the right side further in.
		}
		right = width;

		if (SPDSettings.flipToolbar()) {

			btnWait.setPos( (right - btnWait.right()), y);
			btnSearch.setPos( (right - btnSearch.right()), y);
			btnInventory.setPos( (right - btnInventory.right()), y);

		}
	}

	public static void updateLayout(){
		if (instance != null) instance.layout();
	}


	@Override
	public void update() {
		super.update();

		if (lastEnabled != (Dungeon.hero.ready && Dungeon.hero.isAlive())) {
			lastEnabled = (Dungeon.hero.ready && Dungeon.hero.isAlive());

			for (Gizmo tool : members.toArray(new Gizmo[0])) {
				if (tool instanceof Tool) {
					((Tool)tool).enable( lastEnabled );
				}
			}
		}

		if (!Dungeon.hero.isAlive()) {
			btnInventory.enable(true);
		}
	}

	public void pickup( Item item, int cell ) {
		pickedUp.reset( item,
				cell,
				btnInventory.centerX(),
				btnInventory.centerY());
	}

	private static CellSelector.Listener informer = new CellSelector.Listener() {
		@Override
		public void onSelect( Integer cell ) {
			instance.examining = false;
			GameScene.examineCell( cell );
		}
		@Override
		public String prompt() {
			return Messages.get(Toolbar.class, "examine_prompt");
		}
	};

	public static class Tool extends Button {

		private static final int BGCOLOR = 0x7B8073;

		private Image base;

		public Tool( int x, int y, int width, int height ) {
			super();

			hotArea.blockLevel = PointerArea.ALWAYS_BLOCK;
			frame(x, y, width, height);
		}

		public void frame( int x, int y, int width, int height) {
			base.frame( x, y, width, height );

			this.width = width;
			this.height = height;
		}

		@Override
		protected void createChildren() {
			super.createChildren();

			base = new Image( Assets.Interfaces.TOOLBAR );
			add( base );
		}

		@Override
		public void update() {
			super.update();
			if (SPDSettings.ClassUI()) {
				base.texture = TextureCache.get(Assets.Interfaces.TOOLBAR);
			} else {
				base.texture = TextureCache.get(Assets.Interfaces.TOOLBARDRAK);
			}
		}

		@Override
		protected void layout() {
			super.layout();

			base.x = x;
			base.y = y;
		}

		@Override
		protected void onPointerDown() {
			base.brightness( 1.4f );
		}

		@Override
		protected void onPointerUp() {
			if (active) {
				base.resetColor();
			} else {
				base.tint( BGCOLOR, 0.7f );
			}
		}

		public void enable( boolean value ) {
			if (value != active) {
				if (value) {
					base.resetColor();
				} else {
					base.tint( BGCOLOR, 0.7f );
				}
				active = value;
			}
		}
	}

	private static class QuickslotTool extends Tool {

		private QuickSlotButton slot;
		private int borderLeft = 2;
		private int borderRight = 2;

		public QuickslotTool( int x, int y, int width, int height, int slotNum ) {
			super( x, y, width, height );

			slot = new QuickSlotButton( slotNum );
			add( slot );
		}

		public void border( int left, int right ){
			borderLeft = left;
			borderRight = right;
			layout();
		}

		@Override
		protected void layout() {
			super.layout();
			slot.setRect( x, y, width, height );
			slot.slotMargins(borderLeft, 4, borderRight, 2);
		}

		@Override
		public void enable( boolean value ) {
			super.enable( value );
			slot.enable( value );
		}
	}

	public static class PickedUpItem extends ItemSprite {

		private static final float DURATION = 0.5f;

		private float startScale;
		private float startX, startY;
		private float endX, endY;
		private float left;

		public PickedUpItem() {
			super();

			originToCenter();

			active =
					visible =
							false;
		}

		public void reset( Item item, int cell, float endX, float endY ) {
			view( item );

			active =
					visible =
							true;

			PointF tile = DungeonTerrainTilemap.raisedTileCenterToWorld(cell);
			Point screen = Camera.main.cameraToScreen(tile.x, tile.y);
			PointF start = camera().screenToCamera(screen.x, screen.y);

			x = this.startX = start.x - width() / 2;
			y = this.startY = start.y - width() / 2;

			this.endX = endX - width() / 2;
			this.endY = endY - width() / 2;
			left = DURATION;

			scale.set( startScale = Camera.main.zoom / camera().zoom );

		}

		@Override
		public void update() {
			super.update();

			if ((left -= Game.elapsed) <= 0) {

				visible =
						active =
								false;
				if (emitter != null) emitter.on = false;

			} else {
				float p = left / DURATION;
				scale.set( startScale * (float)Math.sqrt( p ) );

				x = startX*p + endX*(1-p);
				y = startY*p + endY*(1-p);
			}
		}
	}
}
