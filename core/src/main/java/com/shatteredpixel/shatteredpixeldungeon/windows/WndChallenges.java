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

package com.shatteredpixel.shatteredpixeldungeon.windows;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.IconButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.ui.Component;
import com.watabou.utils.Callback;

import java.util.ArrayList;

public class WndChallenges extends Window {

	private static final int WIDTH		= 120;
	private static final int TTL_HEIGHT = 16;
	private static final int BTN_HEIGHT = 16;
	private static final int GAP        = 1;

	private boolean editable;
	private ArrayList<ChallengeButton> boxes;

	public int index;
	private static int boundIndex(int index) {
		int result = index;
		while (result < 0) result += 9;
		while (result >= Challenges.NAME_IDS.length) result -= 9;
		result = (int) (Math.floor(result / 9.0)) * 9;
		return result;
	}

	private Callback callback;

	private int checked;

	public WndChallenges( int checked, boolean editable, Callback callback ) {
		this(checked, 0, editable, callback);
	}

	public int getSelectedButtonCount() {
		int count = 0;
		for (ChallengeButton button : boxes) {
			if (button.checked()) {
				count++;
			}
		}
		return count;
	}

	private float time;
	
	private WndChallenges( int checked, int index, boolean editable, Callback callback ) {

		super();

		this.checked = checked;
		this.index = index;
		this.editable = editable;
		this.callback = callback;

		RenderedTextBlock title = PixelScene.renderTextBlock( Messages.get(this, "title"+
				WndChallenges.this.index), 12 );
		title.setPos(
				(WIDTH - title.width()) / 2,
				(TTL_HEIGHT - title.height()) / 2
		);
		PixelScene.align(title);
		add( title );




		boxes = new ArrayList<>();

		float pos = TTL_HEIGHT;

		float infoBottom = 0;
		index = boundIndex(index);
		for (int i = index; i < index + 9; i++) {

			if (i >= Challenges.NAME_IDS.length) break;
			final String challenge = Challenges.NAME_IDS[i];

			if (i > index && i % 3 == 0) {
				pos += GAP;
			}
			ChallengeInfo info = new ChallengeInfo(foundChallangeIcon(i), challenge, false,
					null);

			ChallengeButton cb = info.check;
			cb.checked((checked & Challenges.MASKS[i]) != 0);
			cb.active = editable;

			for (int ch : Challenges.MASKS) {
				if ((Dungeon.challenges & ch) != 0) {
					getSelectedButtonCount();
				}
			}

			boxes.add( cb );
			info.setRect((i % 3) * 40, pos, 40, 0);
			infoBottom = Math.max(infoBottom, info.bottom());

			add( info );

			if (i % 3 == 2) {
				pos = infoBottom;
			}
		}
		RedButton btnPrev;
		RedButton btnNext;
		btnPrev = new RedButton(Messages.get(WndChallenges.class,"prev")) {
			@Override
			protected void onClick() {
				final int lastIndex = WndChallenges.this.index;
				WndChallenges.this.hide();
				ShatteredPixelDungeon.scene().addToFront(new WndChallenges(refreshChecked(checked),
						WndChallenges.boundIndex(lastIndex - 9), editable, callback));
			}
		};
		add( btnPrev );
		pos = 120;

		Image bg = new Image(Assets.Interfaces.CHALLENGES_BARS){
			@Override
			public synchronized void update() {
				super.update();
			}
		};
		bg.setPos(btnPrev.left()+5, pos-6);
		add(bg);
		btnPrev.setRect(0, pos+3, (WIDTH - GAP) * 0.5f, BTN_HEIGHT);

		Image download = new Image(Assets.Interfaces.STATUS, 0, 54, 54, 5){
			@Override
			public synchronized void update() {
				super.update();

				time += Game.elapsed;

				if (getSelectedButtonCount() > 5) {
					float r = (getSelectedButtonCount()/9f) * Math.max(0.35f, (float) Math.sin(time));
					float g = 0f;
					float b = 0f;
					hardlight(r, g, b);
				} else if(getSelectedButtonCount() > 3) {
					hardlight(Window.ORAGNECOLOR);
				} else {
					hardlight(Window.G_COLOR);
				}

				scale.x = (getSelectedButtonCount() / 4.42f);

			}
		};
		download.setPos(btnPrev.left()+6, pos-5);
		add(download);

		btnNext = new RedButton(Messages.get(WndChallenges.class,"prex")) {
			@Override
			protected void onClick() {
				final int lastIndex = WndChallenges.this.index;
				WndChallenges.this.hide();
				ShatteredPixelDungeon.scene().addToFront(new WndChallenges(refreshChecked(checked),
						WndChallenges.boundIndex(lastIndex + 9), editable, callback));
			}
		};
		add( btnNext );
		btnNext.setRect(btnPrev.right() + GAP, pos+3, (WIDTH - GAP) * 0.5f, BTN_HEIGHT);

		pos += BTN_HEIGHT;

		RedButton btnClear = new RedButton(Messages.get(WndChallenges.class,"clear"), 7) {
			@Override
			protected void onClick() {
				for (int i = 0; i < boxes.size(); i++) {
					setCheckedNoUpdate(i);
				}
			}
		};
		btnClear.setRect(btnPrev.left(), pos+2, WIDTH/2f, BTN_HEIGHT);
		add(btnClear);
		if (!editable) {
			btnClear.enable(false);
		}

		RedButton btnEnabll = new RedButton(Messages.get(WndChallenges.class,"enablealls"), 7) {
			@Override
			protected void onClick() {
				for (int i = 0; i < boxes.size(); i++) {
					setOpenCheckedNoUpdate(i);
				}
			}
		};
		btnEnabll.setRect(btnClear.right(), pos+2, WIDTH/2f, BTN_HEIGHT);
		add(btnEnabll);
		if (!editable) {
			btnEnabll.enable(false);
		}

		RedButton btnGoReady = new RedButton(Messages.get(WndChallenges.class,"middle"), 7) {
			@Override
			protected void onClick() {
				SPDSettings.challenges( 0 );
				WndChallenges.this.hide();
			}
		};
		btnGoReady.setRect(btnPrev.left(), pos+20, WIDTH-24, BTN_HEIGHT);
		add(btnGoReady);

		IconButton btnGoReadyInfo = new IconButton( Icons.get( Icons.INFO ) ) {
			@Override
			protected void onClick() {
				ShatteredPixelDungeon.scene().addToFront(
						new WndTitledMessage(new Image(Icons.get( Icons.INFO )),
								Messages.titleCase(Messages.get(this,"name")),
								Messages.get(this,"middle_desc"))
				);
			}
		};
		btnGoReadyInfo.setRect(btnGoReady.right()+GAP, pos+20, 24, BTN_HEIGHT);
		add(btnGoReadyInfo);
		pos += BTN_HEIGHT+20;

		float challenges =(float) Math.pow(1.25, Challenges.activeChallenges());
		float trueChallenges = Math.round(challenges * 20f) / 20f;

		if (!editable) {
			btnGoReady.enable(false);
			if(Game.scene().getClass() == GameScene.class){
				btnGoReady.text(Messages.get(WndChallenges.class, "totalcount",Challenges.activeChallenges(),trueChallenges));
				btnGoReadyInfo.icon(Icons.get( Icons.CHALLENGE_ON ));
				btnGoReady.active =false;
			}
		}

		resize( WIDTH, (int)pos );
	}

	private void setCheckedNoUpdate(int id) {
		boxes.get(id).checked(false);
	}
	private void setOpenCheckedNoUpdate(int id) {
		boxes.get(id).checked(true);
	}

	@Override
	public void onBackPressed() {

		if (editable) {
			SPDSettings.challenges( refreshChecked(checked) );
		}

		super.onBackPressed();
		if (callback != null) callback.call();
	}

	private int refreshChecked(int checked) {
		int value = checked;
		for (int i = 0; i < boxes.size(); i ++) {
			if (index + i >= Challenges.MASKS.length) break;
			value &= ~Challenges.MASKS[index + i];
			if (boxes.get( i ).checked()) {
				value |= Challenges.MASKS[index + i];
			}
		}
		return value;
	}

	private static class ChallengeButton extends IconButton {
		private boolean checked = false;

		private String name;

		public String getName() {
			return name;
		}

		public ChallengeButton() {
			super( Icons.get( Icons.MISSON_OFF));
		}

		@Override
		protected void onClick() {
			checked(!checked);
		}
		public boolean checked() {
			return checked;
		}
		public void checked( boolean checked ) {
			this.checked = checked;
			icon( Icons.get( checked ? Icons.MISSON_ON : Icons.MISSON_OFF ) );
		}

		public void setName(String name) {
			this.name = name;
		}
	}

	private static class ChallengeInfo extends Component {
		Image icon;
		IconButton info;
		ChallengeButton check;
		IconButton conflict;
		public ChallengeInfo(Image icon, String challenge, boolean conflict, Callback onConflict) {
			super();
			this.icon = icon;
			add( icon );
			info = new IconButton(Icons.get(Icons.INFO)){
				@Override
				protected void onClick() {
						ShatteredPixelDungeon.scene().add(
								new WndTitledMessage(new Image(ChallengeInfo.this.icon),
										Messages.titleCase(Messages.get(Challenges.class, challenge)),
										Messages.get(Challenges.class, challenge+"_desc"))
						);
				}
				protected boolean onLongClick() {

					String more = Messages.get(Challenges.class, challenge+"_moredesc");
					if (more.contains("Ms:")) {
						more = Messages.get(Challenges.class, "nomore");
					}

					ShatteredPixelDungeon.scene().add(
							new WndTitledMessage(new Image(ChallengeInfo.this.icon),
									Messages.titleCase(Messages.get(Challenges.class, challenge)),
									more));
					return true;
				}

			};
			add( info );
			check = new ChallengeButton();
			add( check );
			if (conflict) {
				this.conflict = new IconButton(Icons.get(Icons.HUNTRESS)) {
					@Override
					protected void onClick() {
						if (onConflict != null)
							onConflict.call();
					}
				};
				add( this.conflict );
			}
			else this.conflict = null;
		}

		@Override
		protected void layout() {
			icon.x = x + (width - 32) / 2;
			icon.y = y;
			PixelScene.align( icon );
			info.setRect(icon.x + icon.width, y, 16, BTN_HEIGHT);
			PixelScene.align( info );
			if (conflict == null) {
				check.setRect(x + (width - 16) / 4, y + BTN_HEIGHT, 16, BTN_HEIGHT);
				PixelScene.align( check );
			} else {
				check.setRect(x + (width - 32) / 2, y + BTN_HEIGHT, 16, BTN_HEIGHT);
				PixelScene.align( check );
				conflict.setRect(check.right(), check.top(), 16, BTN_HEIGHT);
				PixelScene.align( conflict );
			}
			check.setSize( 32,16 );
			height = BTN_HEIGHT * 2;
		}

	}

	private Image foundChallangeIcon(int i) {
		switch (Challenges.NAME_IDS[i]) {
			case "no_food":
				return new ItemSprite(ItemSpriteSheet.CHALLANEESICON_1, new ItemSprite.Glowing(0xFF0000));
			case "no_armor":
				return new ItemSprite(ItemSpriteSheet.CHALLANEESICON_2, new ItemSprite.Glowing(0x8f8f8f));
			case "no_healing":
				return new ItemSprite(ItemSpriteSheet.CHALLANEESICON_3, new ItemSprite.Glowing(0xff0000));
			case "no_herbalism":
				return new ItemSprite(ItemSpriteSheet.CHALLANEESICON_4);
			case "swarm_intelligence":
				return new ItemSprite(ItemSpriteSheet.CHALLANEESICON_5, new ItemSprite.Glowing(0xffff00));
			case "darkness":
				return new ItemSprite(ItemSpriteSheet.CHALLANEESICON_6, new ItemSprite.Glowing(0x8f8f8f));
			case "no_scrolls":
				return new ItemSprite(ItemSpriteSheet.CHALLANEESICON_7, new ItemSprite.Glowing(0x00dd00));
			case "aquaphobia":
				return new ItemSprite(ItemSpriteSheet.CHALLANEESICON_8, new ItemSprite.Glowing(0xac89cc));
			case "champion_enemies":
				return new ItemSprite(ItemSpriteSheet.CHALLANEESICON_9, new ItemSprite.Glowing(0x333333));
			case "rlpt":
				return new ItemSprite(ItemSpriteSheet.CHALLANEESICON_10, new ItemSprite.Glowing(0xc6cc6c));
			case "sbsg":
				return new ItemSprite(ItemSpriteSheet.CHALLANEESICON_11, new ItemSprite.Glowing(0x3b1051));
			case "exsg":
				return new ItemSprite(ItemSpriteSheet.CHALLANEESICON_12, new ItemSprite.Glowing(0xd1ce9f));
			case "stronger_bosses":
				return new ItemSprite(ItemSpriteSheet.CHALLANEESICON_13, new ItemSprite.Glowing(0xff0000));
			case "dhxd":
				return new ItemSprite(ItemSpriteSheet.CHALLANEESICON_15, new ItemSprite.Glowing(0x384976));
			case "morelevel":
				return new ItemSprite(ItemSpriteSheet.CHALLANEESICON_16, new ItemSprite.Glowing(0x98bc76));
			case "cs":
				return new ItemSprite(ItemSpriteSheet.CHALLANEESICON_17, new ItemSprite.Glowing(0x08bed5));
			case "blood_died":
				return new ItemSprite(ItemSpriteSheet.CHALLANEESICON_14, new ItemSprite.Glowing(Window.GDX_COLOR));
			default:
				return Icons.get(Icons.PREFS);
		}
	}

}