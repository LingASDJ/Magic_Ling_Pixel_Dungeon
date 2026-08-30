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
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ElementalBuff.BaseBuff.DeathBuff;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndInfoBuff;
import com.watabou.gltextures.TextureCache;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.tweeners.AlphaTweener;
import com.watabou.noosa.ui.Component;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class BuffIndicator extends Component {
	public static int SIZE = 16;
	private static BuffIndicator[] bossInstances = new BuffIndicator[4];
	private static BuffIndicator heroInstance;
	private static final int SCROLL_THRESHOLD = 12;

	// 图标常量定义
	public static final int NONE	= 68;
	public static final int MIND_VISION	= 0;
	public static final int LEVITATION	= 1;
	public static final int FIRE		= 2;
	public static final int POISON		= 3;
	public static final int PARALYSIS	= 4;
	public static final int HUNGER		= 5;
	public static final int STARVATION	= 6;
	public static final int TIME		= 7;
	public static final int OOZE		= 8;
	public static final int AMOK		= 9;
	public static final int TERROR		= 10;
	public static final int ROOTS		= 11;
	public static final int INVISIBLE	= 12;
	public static final int SHADOWS	= 13;
	public static final int WEAKNESS	= 14;
	public static final int FROST		= 15;
	public static final int BLINDNESS	= 16;
	public static final int COMBO		= 17;
	public static final int FURY		= 18;
	public static final int HERB_HEALING= 19;
	public static final int ARMOR		= 20;
	public static final int HEART		= 21;
	public static final int LIGHT		= 22;
	public static final int CRIPPLE		= 23;
	public static final int BARKSKIN	= 24;
	public static final int IMMUNITY	= 25;
	public static final int BLEEDING	= 26;
	public static final int MARK		= 27;
	public static final int DEFERRED	= 28;
	public static final int DROWSY      = 29;
	public static final int MAGIC_SLEEP = 30;
	public static final int THORNS      = 31;
	public static final int FORESIGHT   = 32;
	public static final int VERTIGO     = 33;
	public static final int RECHARGING 	= 34;
	public static final int LOCKED_FLOOR= 35;
	public static final int CORRUPT     = 36;
	public static final int BLESS       = 37;
	public static final int RAGE		= 38;
	public static final int SACRIFICE	= 39;
	public static final int BERSERK     = 40;
	public static final int HASTE       = 41;
	public static final int PREPARATION = 42;
	public static final int WELL_FED    = 43;
	public static final int HEALING     = 44;
	public static final int WEAPON      = 45;
	public static final int VULNERABLE  = 46;
	public static final int HEX         = 47;
	public static final int DEGRADE     = 48;
	public static final int PINCUSHION  = 49;
	public static final int UPGRADE     = 50;
	public static final int MOMENTUM    = 51;
	public static final int ANKH        = 52;
	public static final int NOINV       = 53;
	public static final int DISGUISE = 54;
	public static final int FIREDIED = 55;
	public static final int  ROSEBARRIER= 56;
	public static final int HALOMETHANEBURNING    = 57;
	public static final int LANTERFIRE_ONE    = 58;
	public static final int LANTERFIRE_TWO    = 59;
	public static final int LANTERFIRE_THREE    = 60;
	public static final int LANTERFIRE_FOUR    = 61;
	public static final int LANTERFIRE_FIVE = 62;
	public static final int LANTERFIRE_SIX = 63;
	public static final int DEBUFF_DOWN = 64;
	public static final int GOBUFF_UPRD = 65;
	public static final int ICE_SWORDDOWN = 66;
	public static final int LIGHT_DIED = 67;
	public static final int TARGETED    = 68;
	public static final int IMBUE       = 69;
	public static final int WAND      = 70;
	public static final int INVERT_MARK = 71;
	public static final int NATURE_POWER= 72;
	public static final int AMULET      = 73;
	public static final int DUEL_CLEAVE = 74;
	public static final int DUEL_GUARD  = 75;
	public static final int DUEL_SPIN   = 76;
	public static final int DUEL_EVASIVE= 77;
	public static final int DUEL_DANCE  = 78;
	public static final int DUEL_BRAWL  = 79;
	public static final int DUEL_XBOW   = 80;
	public static final int CHALLENGE   = 81;
	public static final int MONK_ENERGY = 82;
	public static final int DUEL_COMBO  = 83;
	public static final int DAZE        = 84;
	public static final int IMELSAZE        = 85;
	public static final int WHITE_DAY        = 86;
	public static final int MID_DAY        = 87;
	public static final int EVEN_DAY        = 88;
	public static final int NIGHT_DAY        = 89;
	public static final int NIGHT_CAT        = 90;
	public static final int STORM_SNOW        = 91;
	public static final int SNOW_SHILED       = 92;
	public static final int SNOW_RAIN     	  = 93;
	public static final int SNOW_EYE          = 94;
	public static final int TERR_LIST          = 95;
	public static final int SCARY        = 112;
	public static final int SCARY_RED        = 113;
	public static final int SCARY_PINK        = 114;
	public static final int SMOKING        = 115;
	public static final int DK        = 116;
	public static final int QUEST       = 117;
	public static final int KILLER        = 118;
	public static final int GOODLUCK        = 119;
	public static final int PROP_SHADOW = 120;
	public static final int WAND_MAGIC = 121;
	public static final int SLICE_BLESS = 122;
	public static final int GHOST_SCARY = 123;
	public static final int PACMAN_GAME = 124;
	public static final int BOX_GAME = 125;


	public static final int ALL_SEARCH = 144;
	public static final int BASE_STATUS = 145;
	public static final int INVISIBLE_ACTION = 146;
	public static final int HELLBURING = 148;
	public static final int UNLESS = 149;
	public static final int ANCIENT_SURVEY = 150;

	public static final int UPGRADE_SOUL = 151;

	public static final int LOST_SOUL = 153;
	public static final int DEATH = 154;
	public static final int ARROW_NORMAL = 155;
	public static final int ARROW_PARTY = 156;

	public static final int WICKBONE = 157;

	public static final int FIRE_DEH = 158;
	public static final int FIRE_DEM = 159;

	public static final int BREAK_DMG = 160;

	public static final int SIZE_SMALL = 7;
	public static final int SIZE_LARGE = 16;
	private static BuffIndicator bossInstance;

	private LinkedHashMap<Buff, BuffButton> buffButtons = new LinkedHashMap<>();
	private boolean needsRefresh;
	private Char ch;
	public int maxBuffs = 14;
	private boolean large;

	private boolean noScroll = false;
	private boolean oneLing = false;
	private ScrollPane scrollPane;
	private Component scrollContent;

	public int resizeWidth = 0;

	public BuffIndicator( Char ch, boolean large ) {
		super();

		this.ch = ch;
		this.large = large;
		if (ch == Dungeon.hero) {
			heroInstance = this;
		}
	}

	public BuffIndicator( Char ch, boolean large ,boolean noScroll,boolean oneLing) {
		super();

		this.ch = ch;
		this.large = large;
		this.noScroll = noScroll;
		this.oneLing = oneLing;
		if (ch == Dungeon.hero) {
			heroInstance = this;
		}
	}

	public BuffIndicator( Char ch, boolean large,boolean noScroll, int resizeWidth) {
		super();
		this.ch = ch;
		this.large = large;
		this.noScroll = noScroll;
		this.resizeWidth = resizeWidth;
		if (ch == Dungeon.hero) {
			heroInstance = this;
		}
	}

	@Override
	public void destroy() {
		super.destroy();

		if (this == heroInstance) {
			heroInstance = null;
		}
	}

	@Override
	public synchronized void update() {
		super.update();
		if (needsRefresh){
			needsRefresh = false;
			layout();
		}
	}

	@Override
	protected void layout() {

		ArrayList<Buff> newBuffs = new ArrayList<>();
		for (Buff buff : ch.buffs()) {
			if (buff.icon() != NONE) {
				newBuffs.add(buff);
			}
		}
		int totalBuffCount = newBuffs.size();
		int size = large ? SIZE_LARGE : SIZE_SMALL;

		if(noScroll){
			//remove any icons no longer present
			for (Buff buff : buffButtons.keySet().toArray(new Buff[0])){
				if (!newBuffs.contains(buff)){
					Image icon = buffButtons.get( buff ).icon;
					icon.originToCenter();
					icon.alpha(0.6f);
					add( icon );
					add( new AlphaTweener( icon, 0, 0.6f ) {
						@Override
						protected void updateValues( float progress ) {
							super.updateValues( progress );
							image.scale.set( 1 + 5 * progress );
						}

						@Override
						protected void onComplete() {
							image.killAndErase();
						}
					} );

					buffButtons.get( buff ).destroy();
					remove(buffButtons.get( buff ));
					buffButtons.remove( buff );
				}
			}

			//add new icons
			for (Buff buff : newBuffs) {
				if (!buffButtons.containsKey(buff)) {
					BuffButton icon = new BuffButton(buff, large);
					add(icon);
					buffButtons.put( buff, icon );
				}
			}

			//layout
			// 怪物窗口布局：每行6个、左对齐平铺
			int pos = 0;
			int row = 0;
			int maxIconsPerRow = large && oneLing ? 9 : large ? 7 : 6;
			int horizontalSpacing = 0;
			int verticalSpacing = large ? 1 : -3;
			int iconWidth = size + (large ? 1 : 2);
			int iconHeight = size + (large ? 1 : 5);
			if (resizeWidth != 0) {
				maxIconsPerRow = resizeWidth / (iconWidth);
			}

			for (BuffButton icon : buffButtons.values()){
				icon.updateIcon();
				if (pos % maxIconsPerRow == 0 && pos != 0) {
					row++;
					pos = 0;
				}
				float posX = x + pos * (iconWidth + horizontalSpacing);
				float posY = y + row * (iconHeight + verticalSpacing);
				icon.setRect(posX, posY, size, size);
				PixelScene.align(icon);
				pos++;
			}
			if (!buffButtons.isEmpty()) {
				height = (row + 1) * (iconHeight + verticalSpacing);
			}
		} else {
			if (scrollPane == null || scrollContent == null) {
				scrollContent = new Component();
				scrollPane = new ScrollPane(scrollContent);
				add(scrollPane);
			}

			int maxIconsPerRow = large ? 5 : 4;
			int horizontalSpacing = 0;
			int verticalSpacing = -3;
			int iconWidth = size + (large ? 1 : 2);
			int iconHeight = size + (large ? 4 : 5);

			// 清理消失的buff
			for (Buff buff : buffButtons.keySet().toArray(new Buff[0])) {
				if (!newBuffs.contains(buff)) {
					BuffButton button = buffButtons.get(buff);
					if (button == null) {
						buffButtons.remove(buff);
						continue;
					}
					Image icon = button.icon;
					icon.originToCenter();
					icon.alpha(0.6f);
					scrollContent.add(icon);
					scrollContent.add(new AlphaTweener(icon, 0, 0.6f) {
						@Override
						protected void updateValues(float progress) {
							super.updateValues(progress);
							image.scale.set(1 + 5 * progress);
						}
						@Override
						protected void onComplete() {
							image.killAndErase();
						}
					});
					button.destroy();
					scrollContent.remove(button);
					buffButtons.remove(buff);
				}
			}

			// 添加新buff
			for (Buff buff : newBuffs) {
				if (!buffButtons.containsKey(buff)) {
					BuffButton icon = new BuffButton(buff, large);
					scrollContent.add(icon);
					buffButtons.put(buff, icon);
				}
			}

			// 图标整体水平居中排布
			int pos = 0;
			int row = 0;
			float contentW = 0f;
			float contentH = 0f;
			float rowTotalWidth = maxIconsPerRow * iconWidth + (maxIconsPerRow - 1) * horizontalSpacing;
			float centerOffset = (this.width - rowTotalWidth) / 2f;
			if (centerOffset < 0) centerOffset = 0;

			for (BuffButton icon : buffButtons.values()) {
				if (icon == null) continue;
				icon.updateIcon();

				if (pos % maxIconsPerRow == 0 && pos != 0) {
					row++;
					pos = 0;
				}

				float posX = centerOffset + pos * (iconWidth + horizontalSpacing);
				float posY = row * (iconHeight + verticalSpacing);
				icon.setRect(posX, posY, size, size);
				PixelScene.align(icon);

				contentW = Math.max(contentW, posX + size);
				contentH = Math.max(contentH, posY + size);
				pos++;
			}

			scrollContent.setWidth(contentW);
			scrollContent.setHeight(contentH);
			scrollPane.setRect(x, y, width, height+4);

			// 滚动开关：超过阈值开启滚动
			if (totalBuffCount > SCROLL_THRESHOLD) {
				scrollPane.controller.active = true;
			} else {
				scrollPane.controller.active = false;
				scrollPane.scrollTo(0, 0);
			}
			scrollPane.disableThumb();
		}

	}

	private static class BuffButton extends IconButton {

		private Buff buff;

		private boolean large;

		public Image grey; //only for small
		public BitmapText text; //only for large

		//TODO for large buffs there is room to have text instead of fading
		public BuffButton( Buff buff, boolean large ){
			super( new BuffIcon(buff, large));
			this.buff = buff;
			this.large = large;

			bringToFront(grey);
			bringToFront(text);
		}

		@Override
		protected void createChildren() {
			super.createChildren();
			grey = new Image( TextureCache.createSolid(0xCC666666));
			add( grey );

			text = new BitmapText(PixelScene.pixelFont);
			add( text );
		}

		public void updateIcon(){
			((BuffIcon)icon).refresh(buff);
			//round up to the nearest pixel if <50% faded, otherwise round down
			if (!large || buff.iconTextDisplay().isEmpty()) {
				text.visible = false;
				float fadeHeight = buff.iconFadePercent() * icon.height();
				float zoom = (camera() != null) ? camera().zoom : 1;
				if (fadeHeight < icon.height() / 2f) {
					grey.scale.set(icon.width(), (float) Math.ceil(zoom * fadeHeight) / zoom);
				} else {
					grey.scale.set(icon.width(), (float) Math.floor(zoom * fadeHeight) / zoom);
				}
			} else if (!buff.iconTextDisplay().isEmpty()) {
				grey.visible = false;
				if(buff instanceof DeathBuff){
					text.hardlight(Window.ANSDO_COLOR);
				} else {
					if (buff.type == Buff.buffType.POSITIVE)        text.hardlight(CharSprite.POSITIVE);
					else if (buff.type == Buff.buffType.NEGATIVE)   text.hardlight(CharSprite.NEGATIVE);
				}

				text.alpha(0.7f);

				text.text(buff.iconTextDisplay());
				text.measure();
			}
		}

		@Override
		protected void layout() {
			super.layout();
			grey.x = icon.x = this.x + (large ? 0 : 1);
			grey.y = icon.y = this.y + (large ? 0 : 2);

			if (text.width > width()){
				text.scale.set(PixelScene.align(0.5f));
			} else {
				text.scale.set(1f);
			}
			text.x = this.x + width() - text.width() - 1;
			text.y = this.y + width() - text.baseLine() - 2;
		}

		@Override
		protected void onClick() {
			if (buff.icon() != NONE) GameScene.show(new WndInfoBuff(buff));
		}

		@Override
		protected void onPointerDown() {
			//don't affect buff color
			Sample.INSTANCE.play( Assets.Sounds.CLICK );
		}

		@Override
		protected void onPointerUp() {
			//don't affect buff color
		}

		@Override
		protected String hoverText() {
			return Messages.titleCase(buff.name());
		}
	}

	public static void refreshHero() {
		if (heroInstance != null) {
			heroInstance.needsRefresh = true;
		}
	}

	public static void refreshBoss(){
		if (bossInstance != null) {
			bossInstance.needsRefresh = true;
		}
	}

	public static void setBossInstance(BuffIndicator boss){
		bossInstance = boss;
	}
}