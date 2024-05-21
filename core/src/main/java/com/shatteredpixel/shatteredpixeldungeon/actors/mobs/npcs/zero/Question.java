/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2022 Evan Debenham
 *
 * Mund Pixel Dungeon
 * Copyright (C) 2018-2023 Thliey Pen
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

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NTNPC;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.BrokenSeal;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.CloakOfShadows;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Food;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.BlessingNecklace;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.SpiritBow;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Dagger;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MagesStaff;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.QuestionSprite;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndQuest;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Question extends NTNPC {

    {
        spriteClass = QuestionSprite.class;
    }
    private boolean first=true;
    private boolean secnod=true;
    private boolean rd=true;

    private static final String FIRST = "first";
    private static final String SECNOD = "secnod";
    private static final String RD = "rd";
    private int fight = 0;
    private int daily = 0;
    private int eat = 0;

    private static final String FIGHT	= "fight";
    private static final String DAILY	= "daily";
    private static final String EAT	= "eat";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( FIGHT, fight );
        bundle.put( DAILY, daily );
        bundle.put( EAT, eat );
        bundle.put(FIRST, first);
        bundle.put(SECNOD, secnod);
        bundle.put(RD, rd);
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle(bundle);
        fight = bundle.getInt( FIGHT );
        daily = bundle.getInt( DAILY );
        eat = bundle.getInt( EAT );
        first = bundle.getBoolean(FIRST);
        secnod = bundle.getBoolean(SECNOD);
        rd = bundle.getBoolean(RD);
    }

    @Override
    public boolean interact(Char c) {
        if (c != hero) return true;

        ArrayList<Item> items = new ArrayList<>(hero.belongings.backpack.items);
        String eat_msg = eat<1 ? Messages.get(Question.class,"eat"):Messages.get(Question.class,"steal");
        Game.runOnRenderThread(new Callback() {
            @Override
            public void call() {
                GameScene.show(new WndOptions(sprite(), Messages.titleCase(name()), Messages.get(Question.class, "title"),Messages.get(Question.class, "option_fight"),
                                       Messages.get(Question.class, "option_daily"), eat_msg) {
                                   @Override
                                   protected void onSelect(int index) {
                                       if (eat==0){
                                           if (index==0||index==1||index==2){
                                               boolean act = false;
                                               ArrayList<Food> food = hero.belongings.getAllItems(Food.class);
                                               for (Food w : food.toArray(new Food[0])){
                                                   act = true;
                                                   w.detachAll(hero.belongings.backpack);
                                               }
                                               if (act){
                                                   Sample.INSTANCE.play(Assets.Sounds.MISS, 1f, 0.8f);
                                                   CellEmitter.get( hero.pos ).burst( Speck.factory( Speck.WOOL ), 10 );
                                                   int pos;
                                                   if (Random.Int(4)==0){
                                                       pos = 3157;
                                                   } else if (Random.Int(3)==0){
                                                       pos = 1532;
                                                   } else if (Random.Int(2)==0){
                                                       pos = 941;
                                                   } else {
                                                       pos = 267;
                                                   }
                                                   ScrollOfTeleportation.appear(Question.this, pos);
                                                   eat++;
                                               } else {
                                                   GameScene.show( new WndQuest( Question.this, "……") );
                                               }
                                           }
                                       } else if (eat==1){
                                           Game.runOnRenderThread(new Callback() {
                                               @Override
                                               public void call() {
                                                   GameScene.show( new WndQuest( Question.this, Messages.get(Question.class, "thanks_for_food") ) );
                                                   eat++;
                                               }
                                           });
                                       } else if (eat>=2){
                                           if (index==0){
                                               if (fight==0){
                                                   GameScene.show( new WndQuest( Question.this, Messages.get(Question.class, "fight_1",hero.name()) ) );
                                                   fight++;
                                               } else if (fight==1){
                                                   GameScene.show( new WndQuest( Question.this, Messages.get(Question.class, "fight_2") ) );
                                                   fight++;
                                               } else if (fight==2){
                                                   GameScene.show( new WndQuest( Question.this, Messages.get(Question.class, "fight_3") ) );
                                                   fight++;
                                               } else if (fight==3){
                                                   if (eat==2){
                                                       GameScene.show( new WndQuest( Question.this, Messages.get(Question.class, "weapon") ) );
                                                       Dungeon.level.drop(new Dagger(), hero.pos).sprite.drop();
                                                   } else {
                                                       GameScene.show( new WndQuest( Question.this, Messages.get(Question.class, "food") ) );
                                                       Dungeon.level.drop(new Food(), hero.pos).sprite.drop();
                                                   }
                                                   fight++;
                                               } else {
                                                   GameScene.show( new WndQuest( Question.this, "……") );
                                               }
                                           } else if (index==1){
                                               if (daily==0){
                                                   daily++;
                                                   GameScene.show( new WndQuest( Question.this, Messages.get(Question.class, "daily_1") ) );
                                               } else if (daily==1){
                                                   daily++;
                                                   GameScene.show( new WndQuest( Question.this, Messages.get(Question.class, "daily_2") ) );
                                               } else if (daily==2&&eat<=3 && first){
                                                   for (Item i : items){
                                                       if (i instanceof BrokenSeal || i instanceof MagesStaff || i instanceof CloakOfShadows || i instanceof SpiritBow){
                                                           GameScene.show( new WndQuest( Question.this, Messages.get(Question.class, "wait",i.name(),hero.name()) ) );
                                                           Dungeon.level.drop(new BlessingNecklace(), hero.pos).sprite.drop();
                                                           destroy();
                                                           CellEmitter.get( pos ).burst( Speck.factory( Speck.WOOL ), 10 );
                                                           first = false;
                                                       }
                                                   }
                                               } else {
                                                   GameScene.show( new WndQuest( Question.this, "……") );
                                               }
                                           } else if (index==2){
                                               if (eat==2){
                                                   eat++;
                                                   GameScene.show( new WndQuest( Question.this, Messages.get(Question.class, "eat_1") ) );
                                               } else if (eat==3){
                                                   eat++;
                                                   GameScene.show( new WndQuest( Question.this, Messages.get(Question.class, "eat_2") ) );
                                               } else {
                                                   GameScene.show( new WndQuest( Question.this, "……") );
                                               }
                                           }
                                       }
                                   }
                               }
                );
            }});
        return true;
    }
}