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

package com.shatteredpixel.shatteredpixeldungeon.items;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.status.ScoreBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.GameAPI;
import com.shatteredpixel.shatteredpixeldungeon.effects.FloatingText;
import com.shatteredpixel.shatteredpixeldungeon.items.props.RustedGoldCoin;
import com.shatteredpixel.shatteredpixeldungeon.journal.Catalog;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Gold extends Item {

	{
		image = ItemSpriteSheet.GOLD;
		stackable = true;
	}
	
	public Gold() {
		this( 1 );
	}
	
	public Gold( int value ) {
		this.quantity = value;
	}
	
	@Override
	public ArrayList<String> actions( Hero hero ) {
		return new ArrayList<>();
	}
	
	@Override
	public boolean doPickUp(Hero hero, int pos) {
		if(hero.buff(ScoreBuff.class)!=null && Dungeon.hero != null) {
			GameScene.pickUp(this, pos);
			Sample.INSTANCE.play(Assets.Sounds.ITEM);
			hero.spendAndNext(TIME_TO_PICK_UP);
			ScoreBuff buff = hero.buff(ScoreBuff.class);
			OnlyAllSearch = false;
			int score = quantity / 4;
			hero.sprite.showStatus(Window.TITLE_COLOR, "+" + score);
			buff.addScore(score);
		} else if(quantity > 49 && Statistics.bossRushMode ){
			Dungeon.rushgold += quantity/50;
			GameScene.pickUp( this, pos );
			hero.spendAndNext( TIME_TO_PICK_UP );
			hero.sprite.showStatus(CharSprite.NEGATIVE, Messages.get(this, "ling",quantity/50));
        } else {
			if(hero.belongings.getItem(RustedGoldCoin.class)!=null){
				quantity *= 0.85f;
				Dungeon.gold += quantity == 0 ? 1 : quantity;
				Statistics.goldCollected += quantity == 0 ? 1 : quantity;
				Badges.validateGoldCollected();
				GameScene.pickUp( this, pos );
				hero.sprite.showStatusWithIcon( CharSprite.NEUTRAL, Integer.toString( quantity == 0 ? 1 : quantity), FloatingText.GOLD );
			} else {
				Dungeon.gold += quantity;
				Statistics.goldCollected += quantity;
				Badges.validateGoldCollected();
				GameScene.pickUp( this, pos );
				hero.sprite.showStatusWithIcon( CharSprite.NEUTRAL, Integer.toString(quantity), FloatingText.GOLD );
			}

			hero.spendAndNext( TIME_TO_PICK_UP );
        }
        Sample.INSTANCE.play( Assets.Sounds.GOLD, 1, 1, Random.Float( 0.9f, 1.1f ) );
        updateQuickslot();
		Catalog.setSeen(getClass());
        return true;
	}
	
	@Override
	public boolean isUpgradable() {
		return false;
	}
	
	@Override
	public boolean isIdentified() {
		return true;
	}
	
	@Override
	public Item random() {
		Gold item = this;
		quantity = Random.IntRange( 30 + Dungeon.depth * 10, 60 + Dungeon.depth * 20 );
		GameAPI.CodeCallback_OnItemCreation( item );
		return item;
	}


	public int random_4X() {
		return quantity = (Random.IntRange( 30 + Dungeon.depth * 10, 60 + Dungeon.depth * 20 ) )*4;
	}

}
