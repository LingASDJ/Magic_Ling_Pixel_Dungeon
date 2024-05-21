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

import static com.shatteredpixel.shatteredpixeldungeon.Challenges.MOREROOM;
import static com.shatteredpixel.shatteredpixeldungeon.Challenges.PRO;
import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;
import static com.shatteredpixel.shatteredpixeldungeon.SPDSettings.HelpSettings;
import static com.shatteredpixel.shatteredpixeldungeon.Statistics.lanterfireactive;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Clipboard;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ClearBleesdGoodBuff.ClearLanterBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ElementalBuff.ElementalBaseBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ElementalBuff.ElementalBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ElementalBuff.ElementalFABuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicGirlDebuff.MagicGirlDebuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.custom.ch.GameTracker;
import com.shatteredpixel.shatteredpixeldungeon.custom.messages.M;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.HeroSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.text.HeroStat;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIcon;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.IconButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.ScrollPane;
import com.shatteredpixel.shatteredpixeldungeon.ui.StatusPane;
import com.shatteredpixel.shatteredpixeldungeon.ui.TalentButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.TalentsPane;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.utils.DungeonSeed;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Gizmo;
import com.watabou.noosa.Group;
import com.watabou.noosa.Image;
import com.watabou.noosa.ui.Component;

import java.util.ArrayList;
import java.util.Locale;
import java.util.regex.Pattern;

public class WndHero extends WndTabbed {

	private static final int WIDTH		= 130;
	private static final int HEIGHT		= 130;

	private StatsTab stats;
	private TalentsTab talents;
	private BuffsTab buffs;

	private ElementalBuffsTab ebuffs;
	private LRBuffsTab rbuffs;
	Clipboard clipboard;

	public static int lastIdx = 0;

	public WndHero() {

		super();

		resize( WIDTH, HEIGHT );

		clipboard = Gdx.app.getClipboard();

		stats = new StatsTab();
		add( stats );

		talents = new TalentsTab();
		add(talents);
		talents.setRect(0, 0, WIDTH, HEIGHT);

		buffs = new BuffsTab();
		add( buffs );
		buffs.setRect(0, 0, WIDTH, HEIGHT);
		buffs.setupList();

		ebuffs = new ElementalBuffsTab();
		add( ebuffs );
		ebuffs.setRect(0, 0, WIDTH, HEIGHT);
		ebuffs.setupList();

		rbuffs = new LRBuffsTab();
		add( rbuffs );
		rbuffs.setRect(0, 0, WIDTH, HEIGHT);
		rbuffs.setupList();

		add( new IconTab( Icons.get(Icons.RANKINGS) ) {
			protected void select( boolean value ) {
				super.select( value );
				if (selected) {
					lastIdx = 0;
					if (!stats.visible) {
						stats.initialize();
					}
				}
				stats.visible = stats.active = selected;
			}
		} );
		add( new IconTab( Icons.get(Icons.TALENT) ) {
			protected void select( boolean value ) {
				super.select( value );
				if (selected) lastIdx = 1;
				if (selected) StatusPane.talentBlink = 0;
				talents.visible = talents.active = selected;
			}
		} );
		add( new IconTab( Icons.get(Icons.BUFFS) ) {
			protected void select( boolean value ) {
				super.select( value );
				if (selected) lastIdx = 2;
				buffs.visible = buffs.active = selected;
			}
		} );
		add( new IconTab( Icons.get(Icons.CHANGES) ) {
			protected void select( boolean value ) {
				super.select( value );
				if (selected) lastIdx = 3;
				ebuffs.visible = ebuffs.active = selected;
			}
		} );

		add( new IconTab( Icons.get(Icons.PLUS) ) {
			protected void select( boolean value ) {
				super.select( value );
				if (selected) lastIdx = 4;
				rbuffs.visible = rbuffs.active = selected;
			}
		} );

		layoutTabs();

		talents.setRect(0, 0, WIDTH, HEIGHT);
		talents.pane.scrollTo(0, talents.pane.content().height() - talents.pane.height());
		talents.layout();

		select( lastIdx );
	}

	@Override
	public void offset(int xOffset, int yOffset) {
		super.offset(xOffset, yOffset);
		talents.layout();
		buffs.layout();
		ebuffs.layout();
		rbuffs.layout();
	}

	private class StatsTab extends Group {

		private static final int GAP = 4;

		private float pos;

		public StatsTab() {
			initialize();
		}

		public void initialize(){

			for (Gizmo g : members){
				if (g != null) g.destroy();
			}
			clear();

			Hero hero = Dungeon.hero;

			IconTitle title = new IconTitle();
			title.icon( HeroSprite.avatar(hero.heroClass, hero.tier()) );
			if (hero.name().equals(hero.className()))
				title.label( Messages.get(this, "title", hero.lvl, hero.className() ).toUpperCase( Locale.ENGLISH ) );
			else
				title.label((hero.name() + "\n" + Messages.get(this, "title", hero.lvl, hero.className())).toUpperCase(Locale.ENGLISH));
			title.color(Window.TITLE_COLOR);
			title.setRect( 0, 0, WIDTH-16, 0 );
			add(title);

			IconButton infoButton = new IconButton(Icons.get(Icons.INFO)){
				@Override
				protected void onClick() {
					super.onClick();
					if (ShatteredPixelDungeon.scene() instanceof GameScene){
						GameScene.show(new WndHeroInfo(hero.heroClass));
					} else {
						ShatteredPixelDungeon.scene().addToFront(new WndHeroInfo(hero.heroClass));
					}
				}

				@Override
				protected String hoverText() {
					return Messages.titleCase(Messages.get(WndKeyBindings.class, "hero_info"));
				}

			};
			infoButton.setRect(title.right(), 0, 16, 16);
			add(infoButton);

			IconButton seedButton = new IconButton(new Image(new ItemSprite(ItemSpriteSheet.SEED_SKYBLUEFIRE))){
				@Override
				protected void onClick() {
					super.onClick();
					clipboard.setContents(DungeonSeed.convertToCode(Dungeon.seed));
					GLog.i(Messages.get(HeroStat.class, "copy_success"));
				}

				@Override
				protected String hoverText() {
					return Messages.titleCase(Messages.get(HeroStat.class, "item_seed"));
				}

			};
			seedButton.setRect(title.right(), infoButton.bottom()+seedButton.height()+2, 16, 16);
			add(seedButton);

			IconButton itemButton = new IconButton(Icons.get(Icons.MAGNIFY)){
				@Override
				protected void onClick() {
					super.onClick();
					GameScene.show(new StatsTab.WndTreasureGenerated());
				}

				@Override
				protected String hoverText() {
					return Messages.titleCase(Messages.get(HeroStat.class, "item_enter"));
				}

			};
			itemButton.setRect(title.right(), seedButton.bottom()+itemButton.height()+2, 16, 16);
			add(itemButton);

			if(HelpSettings() && Dungeon.isChallenged(PRO)){
				itemButton.active = true;
			} else {
				itemButton.active = false;
				itemButton.visible = false;
			}



//			IconButton scoreButton = new IconButton(Icons.get(Icons.BUFFS)){
//				@Override
//				protected void onClick() {
//					super.onClick();
//					ShatteredPixelDungeon.scene().addToFront(new WndScoreBreakdown());
//				}
//
//				@Override
//				protected String hoverText() {
//					return Messages.titleCase(Messages.get(WndKeyBindings.class, "score_info"));
//				}
//
//			};
//			scoreButton.setRect(title.right()-16, 0, 16, 16);
//			add(scoreButton);

			pos = title.bottom() + GAP;

			int strBonus = hero.STR() - hero.STR;
			if (strBonus > 0)           statSlot( Messages.get(this, "str"), hero.STR + " + " + strBonus );
			else if (strBonus < 0)      statSlot( Messages.get(this, "str"), hero.STR + " - " + -strBonus );
			else                        statSlot( Messages.get(this, "str"), hero.STR() );
			if (hero.shielding() > 0)   statSlot( Messages.get(this, "health"), hero.HP + "+" + hero.shielding() + "/" + hero.HT );
			else                        statSlot( Messages.get(this, "health"), (hero.HP) + "/" + hero.HT );
			statSlot( Messages.get(this, "exp"), hero.exp + "/" + hero.maxExp() );

			pos += GAP;

			statSlot( Messages.get(this, "gold"), Statistics.goldCollected );
			statSlot( Messages.get(this, "depth"), Statistics.deepestFloor );
			statSlot( Messages.get(HeroStat.class, "seed_dungeon"), DungeonSeed.convertToCode(Dungeon.seed));


			statSlot(Messages.get(HeroStat.class, "seed_type"), seedType());


			if(lanterfireactive){
				RestatSlot( Messages.get(this, "lanterfire"), (double)(hero.lanterfire) + "/" + 100 );
			}

//			IcestatSlot( Messages.get(this, "icehp"), (hero.icehp) + "/" + 100 );

			pos += GAP;

			Hunger hunger = Dungeon.hero.buff(Hunger.class);
			String hunger_str = "null";
			if(hunger != null){
				hunger_str = hunger.hunger() + "/" + Hunger.STARVING;
			}
			statSlot( M.L(HeroStat.class, "hunger"), hunger_str);

			pos += GAP;
		}

		private void statSlot( String label, String value ) {

			RenderedTextBlock txt = PixelScene.renderTextBlock( label, 7 );
			txt.setPos(0, pos);
			add( txt );

			txt = PixelScene.renderTextBlock( value, 7 );
			txt.setPos(WIDTH * 0.5f, pos);
			PixelScene.align(txt);
			add( txt );

			pos += GAP + txt.height();
		}

		private void RestatSlot( String label, String value ) {

			RenderedTextBlock txt = PixelScene.renderTextBlock( label, 7 );
			txt.setPos(0, pos);
			add( txt );

			txt = PixelScene.renderTextBlock( value, 7 );
			txt.setPos(WIDTH * 0.5f, pos);
			PixelScene.align(txt);

			txt.hardlight(hero.lanterfire<=0 ? 0x808080 : Window.WHITE );

			add( txt );

			pos += GAP + txt.height();
		}

		private String seedType(){
			String seed;
			if(Dungeon.isChallenged(MOREROOM)){
				seed = "B";
			} else {
				seed = "A";
			}
			return seed;
		}

		private void IcestatSlot( String label, String value ) {

			RenderedTextBlock txt = PixelScene.renderTextBlock( label, 7 );
			txt.setPos(0, pos);
			add( txt );

			txt = PixelScene.renderTextBlock( value, 7 );
			txt.setPos(WIDTH * 0.5f, pos);
			PixelScene.align(txt);

			txt.hardlight(Window.TITLE_COLOR);

			add( txt );

			pos += GAP + txt.height();
		}

		private void statSlot( String label, int value ) {
			statSlot( label, Integer.toString( value ) );
		}

		public float height() {
			return pos;
		}

		private class WndTreasureGenerated extends Window{
			private static final int WIDTH = 120;
			private static final int HEIGHT = 144;

			public WndTreasureGenerated(){
				super();
				resize(WIDTH, HEIGHT);
				ScrollPane pane = new ScrollPane(new Component());
				Component content = pane.content();
				this.add(pane);
				pane.setRect(0,0,WIDTH, HEIGHT);

				GameTracker gmt = hero.buff(GameTracker.class);
				if(gmt != null){
					String allInfo = gmt.itemInfo();
					String[] result = allInfo.split("\n");
					float pos = 2;
					for(String info: result){
						if(info.contains("dungeon_depth")){
							pos += 4;
							RenderedTextBlock depthText = PixelScene.renderTextBlock(info.replace("dungeon_depth: ", M.L(HeroStat.class, "item_wnd_depth")), 8);
							depthText.maxWidth(WIDTH);
							depthText.hardlight(0xFFFF00);
							content.add(depthText);
							depthText.setPos(0, pos);
							pos += 8;
						}else{
							pos += 1;
							info = info.replace("MIMIC_HOLD", M.L(HeroStat.class, "item_wnd_mimic"));
							info = info.replace("QUEST_REWARD", M.L(HeroStat.class, "item_wnd_reward"));
							info = info.replace("CURSED", M.L(HeroStat.class, "item_wnd_cursed"));
							RenderedTextBlock itemText = PixelScene.renderTextBlock(info, 6);
							itemText.maxWidth(WIDTH);
							content.add(itemText);
							itemText.setPos(0, pos);
							pos += 6;
							String level = Pattern.compile("[^0-9]").matcher(info).replaceAll("").trim();
							try{
								int lvl = Integer.parseInt(level);
								if(lvl == 1){
									itemText.hardlight(0x57FAFF);
								}else if(lvl == 2){
									itemText.hardlight(0xA000A0);
								}else if(lvl == 3){
									itemText.hardlight(0xFFB700);
								}else if(lvl >= 4) {
									itemText.hardlight(Window.Pink_COLOR);
								}
							}catch (Exception e){

							}


						}
					}
					content.setSize(WIDTH, pos + 2);
				}
				pane.scrollTo(0, 0);
			}
		}
	}

	public class TalentsTab extends Component {

		TalentsPane pane;

		@Override
		protected void createChildren() {
			super.createChildren();
			pane = new TalentsPane(TalentButton.Mode.UPGRADE);
			add(pane);
		}

		@Override
		protected void layout() {
			super.layout();
			pane.setRect(x, y, width, height);
		}

	}
	
	private class BuffsTab extends Component {
		
		private static final int GAP = 2;
		
		private float pos;
		private ScrollPane buffList;
		private ArrayList<BuffSlot> slots = new ArrayList<>();

		@Override
		protected void createChildren() {

			super.createChildren();

			buffList = new ScrollPane( new Component() ){
				@Override
				public void onClick( float x, float y ) {
					int size = slots.size();
					for (int i=0; i < size; i++) {
						if (slots.get( i ).onClick( x, y )) {
							break;
						}
					}
				}
			};
			add(buffList);
		}
		
		@Override
		protected void layout() {
			super.layout();
			buffList.setRect(0, 0, width, height);
		}
		
		private void setupList() {
			Component content = buffList.content();


			content.setSize(buffList.width(), pos);
			buffList.setSize(buffList.width(), buffList.height());

			RenderedTextBlock title = PixelScene.renderTextBlock(Messages.get(WndHero.class,"blist"), 9);
			title.hardlight(TITLE_COLOR);
			title.maxWidth( (int)width() - 2 );
			title.setPos( (width() - title.width())/2f, pos + 1 + ((18) - title.height())/2f);
			PixelScene.align(title);
			content.add(title);

			pos += Math.max(18, title.height());

			for (Buff buff : hero.buffs()) {
				if (buff.icon() != BuffIndicator.NONE && !(buff instanceof ElementalBuff || buff instanceof ElementalFABuff|| buff instanceof ClearLanterBuff || buff instanceof MagicGirlDebuff)) {
					BuffSlot slot = new BuffSlot(buff);
					slot.setRect(0, pos, WIDTH, slot.icon.height());
					content.add(slot);
					slots.add(slot);
					pos += GAP + slot.height();
				}
			}

			content.setSize(buffList.width(), pos);
			buffList.setSize(buffList.width(), buffList.height());


		}

		private class BuffSlot extends Component {

			private Buff buff;

			Image icon;
			RenderedTextBlock txt;

			public BuffSlot( Buff buff ){
				super();
				this.buff = buff;

				icon = new BuffIcon(buff, true);
				icon.y = this.y;
				add( icon );

				txt = PixelScene.renderTextBlock( buff.name(), 8 );
				txt.setPos(
						icon.width + GAP,
						this.y + (icon.height - txt.height()) / 2
				);
				PixelScene.align(txt);
				add( txt );

			}

			@Override
			protected void layout() {
				super.layout();
				icon.y = this.y;
				txt.maxWidth((int)(width - icon.width()));
				txt.setPos(
						icon.width + GAP,
						this.y + (icon.height - txt.height()) / 2
				);
				PixelScene.align(txt);
			}
			
			protected boolean onClick ( float x, float y ) {
				if (inside( x, y )) {
					GameScene.show(new WndInfoBuff(buff));
					return true;
				} else {
					return false;
				}
			}
		}
	}

	private class ElementalBuffsTab extends Component {

		private static final int GAP = 2;

		private float pos;
		private ScrollPane buffList;
		private ArrayList<EBuffSlot> slots = new ArrayList<>();

		@Override
		protected void createChildren() {

			super.createChildren();



			buffList = new ScrollPane( new Component() ){
				@Override
				public void onClick( float x, float y ) {
					int size = slots.size();
					for (int i=0; i < size; i++) {
						if (slots.get( i ).onClick( x, y )) {
							break;
						}
					}
				}
			};
			add(buffList);
		}

		@Override
		protected void layout() {
			super.layout();
			buffList.setRect(0, 0, width, height);
		}

		private void setupList() {
			Component content = buffList.content();


			RenderedTextBlock title = PixelScene.renderTextBlock(Messages.get(WndHero.class,"elist"), 9);
			title.hardlight(SPDSettings.ClassUI() ? 0x8f8f8f : Window.R_COLOR);
			title.maxWidth( (int)width() - 2 );
			title.setPos( (width() - title.width())/2f, pos + 1 + ((18) - title.height())/2f);
			PixelScene.align(title);
			content.add(title);

			pos += Math.max(18, title.height());

			content.setSize(buffList.width(), pos);
			buffList.setSize(buffList.width(), buffList.height());

			//遍历buff 既不是None 但是ElementalBuff ElementalFABuff ElementalBaseBuff
			for (Buff ebuff : hero.buffs()) {
				if (ebuff.icon() != BuffIndicator.NONE && (ebuff instanceof ElementalBuff || ebuff instanceof ElementalFABuff || ebuff instanceof ElementalBaseBuff)) {
					EBuffSlot slot = new EBuffSlot(ebuff);
					slot.setRect(0, pos, WIDTH, slot.icon.height());
					content.add(slot);
					slots.add(slot);
					pos += GAP + slot.height();
				}
			}

			content.setSize(buffList.width(), pos);
			buffList.setSize(buffList.width(), buffList.height());

		}

		private class EBuffSlot extends Component {

			private Buff buff;

			Image icon;
			RenderedTextBlock txt;

			public EBuffSlot( Buff buff ){
				super();
				this.buff = buff;

				icon = new BuffIcon(buff, true);
				icon.y = this.y;
				add( icon );

				txt = PixelScene.renderTextBlock( buff.name(), 8 );
				txt.setPos(
						icon.width + GAP,
						this.y + (icon.height - txt.height()) / 2
				);
				PixelScene.align(txt);
				add( txt );

			}

			@Override
			protected void layout() {
				super.layout();
				icon.y = this.y;
				txt.maxWidth((int)(width - icon.width()));
				txt.setPos(
						icon.width + GAP,
						this.y + (icon.height - txt.height()) / 2
				);
				PixelScene.align(txt);
			}

			protected boolean onClick ( float x, float y ) {
				if (inside( x, y )) {
					GameScene.show(new WndInfoBuff(buff));
					return true;
				} else {
					return false;
				}
			}
		}
	}

	private class LRBuffsTab extends Component {

		private static final int GAP = 2;

		private float pos;
		private float pos2;
		private ScrollPane buffList;
		private ArrayList<LRBuffSlot> slots = new ArrayList<>();

		@Override
		protected void createChildren() {

			super.createChildren();
			buffList = new ScrollPane( new Component() ){
				@Override
				public void onClick( float x, float y ) {
					int size = slots.size();
					for (int i=0; i < size; i++) {
						if (slots.get( i ).onClick( x, y )) {
							break;
						}
					}
				}
			};
			add(buffList);
		}

		@Override
		protected void layout() {
			super.layout();
			buffList.setRect(0, 0, width, height);
		}

		private void setupList() {
			Component content = buffList.content();

			RenderedTextBlock title = PixelScene.renderTextBlock(Messages.get(WndHero.class,"gdlist"), 9);
			title.hardlight(SPDSettings.ClassUI() ? Window.CPINK : Window.CYELLOW);
			title.maxWidth( (int)width() - 2 );
			title.setPos( (width() - title.width())/2f, pos + 1 + ((18) - title.height())/2f);
			PixelScene.align(title);
			content.add(title);

			pos += Math.max(18, title.height());

			for (Buff ebuff : hero.buffs()) {
				if (ebuff.icon() != BuffIndicator.NONE && (ebuff instanceof ClearLanterBuff || ebuff instanceof MagicGirlDebuff) ) {
					LRBuffSlot slot = new LRBuffSlot(ebuff);
					slot.setRect(0, pos, WIDTH, slot.icon.height());
					content.add(slot);
					slots.add(slot);
					pos += GAP + slot.height();
				}
			}

			content.setSize(buffList.width(), pos);
			buffList.setSize(buffList.width(), buffList.height());

		}

		private class LRBuffSlot extends Component {

			private Buff buff;

			Image icon;
			RenderedTextBlock txt;

			public LRBuffSlot( Buff buff ){
				super();
				this.buff = buff;

				icon = new BuffIcon(buff, true);
				icon.y = this.y;
				add( icon );

				txt = PixelScene.renderTextBlock( buff.name(), 8 );
				txt.setPos(
						icon.width + GAP,
						this.y + (icon.height - txt.height()) / 2
				);
				PixelScene.align(txt);
				add( txt );

			}

			@Override
			protected void layout() {
				super.layout();
				icon.y = this.y;
				txt.maxWidth((int)(width - icon.width()));
				txt.setPos(
						icon.width + GAP,
						this.y + (icon.height - txt.height()) / 2
				);
				PixelScene.align(txt);
			}

			protected boolean onClick ( float x, float y ) {
				if (inside( x, y )) {
					GameScene.show(new WndInfoBuff(buff));
					return true;
				} else {
					return false;
				}
			}
		}
	}

}
