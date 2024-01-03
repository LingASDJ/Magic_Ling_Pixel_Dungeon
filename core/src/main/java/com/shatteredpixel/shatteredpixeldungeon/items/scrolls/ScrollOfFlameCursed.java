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

package com.shatteredpixel.shatteredpixeldungeon.items.scrolls;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FrostBurning;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.Flare;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfFrost;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfLiquidFlame;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;

public class ScrollOfFlameCursed extends Scroll {

    {
        image = ItemSpriteSheet.DG24;
        unique = true;
    }

    public void bolt(Integer target, final Mob mob){
        if (target != null) {

            final Ballistica shot = new Ballistica( Dungeon.hero.pos, target, Ballistica.PROJECTILE);

            fx(shot, () -> onHit(shot, mob));
        }
    }
    protected void fx(Ballistica bolt, Callback callback) {
        MagicMissile.boltFromChar( Dungeon.hero.sprite.emitter(), MagicMissile.WARD, Dungeon.hero.sprite,
                bolt.collisionPos,
                callback);
    }

    protected void onHit(Ballistica bolt, Mob mob) {

        //presses all tiles in the AOE first

        if (mob != null){
            if (mob.isAlive() && bolt.path.size() > bolt.dist+1) {
                Buff.affect( mob, FrostBurning.class ).reignite( mob, 25f );
            }
        }

    }

    @Override
    public void doRead() {

        detach(curUser.belongings.backpack);
        new Flare( 5, 32 ).color( 0x00FFFF, true ).show( curUser.sprite, 2f );
        Sample.INSTANCE.play( Assets.Sounds.READ );

        int count = 0;
        Mob affected = null;
        for (Mob mob : Dungeon.level.mobs.toArray( new Mob[0] )) {
            if (mob.alignment != Char.Alignment.ALLY && Dungeon.level.heroFOV[mob.pos]) {
                bolt(mob.pos, mob);
            }
        }

        switch (count) {
            case 0:
                GLog.i( Messages.get(this, "none") );
                break;
            case 1:
                GLog.i( Messages.get(this, "one", affected.name()) );
                break;
            default:
                GLog.i( Messages.get(this, "many") );
        }
        identify();
        readAnimation();
    }

    public static class Recipe extends com.shatteredpixel.shatteredpixeldungeon.items.Recipe.SimpleRecipe {

        {
            inputs =  new Class[]{PotionOfLiquidFlame.class, ScrollOfTerror.class, PotionOfFrost.class};
            inQuantity = new int[]{1, 1, 1};

            cost = 24;

            output = ScrollOfFlameCursed.class;
            outQuantity = 2;
        }

    }

    @Override
    public boolean isIdentified() {
        return true;
    }

    @Override
    public boolean isKnown() {
        return true;
    }


//    @Override
//    public int value() {
//        return isKnown() ? 40 * quantity : super.value();
//    }
}
