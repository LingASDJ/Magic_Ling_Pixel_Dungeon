/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2019 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Degrade;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfFrost;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHaste;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfInvisibility;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfLevitation;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfLightningShiledX;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfLiquidFlame;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfLiquidFlameX;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfMindVision;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfParalyticGas;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfPurity;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ColdRatSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class ColdMagicRat extends Mob implements Callback {

    private static final float TIME_TO_ZAP	= 3f;

    {
        spriteClass = ColdRatSprite.class;

        HP = HT = 30;
        defenseSkill = 4;

        EXP = 9;
        maxLvl = 15;

        loot = Generator.Category.GOLD;
        lootChance = 0.6f;

        properties.add(Property.UNDEAD);
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange( 10, 12 );
    }

    @Override
    public int attackSkill( Char target ) {
        return 12;
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(1, 3);
    }

    @Override
    protected boolean canAttack( Char enemy ) {
        return new Ballistica( pos, enemy.pos, Ballistica.MAGIC_BOLT).collisionPos == enemy.pos;
    }

    protected boolean doAttack( Char enemy ) {

        if (Dungeon.level.adjacent( pos, enemy.pos )) {

            return super.doAttack( enemy );

        } else {

            if (sprite != null && (sprite.visible || enemy.sprite.visible)) {
                sprite.zap( enemy.pos );
                return false;
            } else {
                zap();
                return true;
            }
        }
    }

    //used so resistances can differentiate between melee and magical attacks
    public static class DarkBolt{}

    private void zap() {
        spend( TIME_TO_ZAP );

        if (hit( this, enemy, true )) {

            if (enemy == Dungeon.hero && Random.Int( 2 ) == 0) {
                Buff.prolong( enemy, Blindness.class, Degrade.DURATION/5 );
                Sample.INSTANCE.play( Assets.Sounds.DEBUFF );
            }

            int dmg = Random.NormalIntRange( 10, 12 );
            enemy.damage( dmg, new DarkBolt() );

            if (enemy == Dungeon.hero && !enemy.isAlive()) {
                Dungeon.fail( getClass() );
                GLog.n( Messages.get(this, "frost_kill") );
            }
        } else {
            enemy.sprite.showStatus( CharSprite.NEUTRAL,  enemy.defenseVerb() );
        }
    }

    public void onZapComplete() {
        zap();
        next();
    }

    @Override
    public void call() {
        next();
    }

    @Override
    public Item createLoot(){
        Item w;
        if (Random.Int(3) == 0 && Random.Int(8) > Dungeon.LimitedDrops.ICERAT_HP.count ){
            Dungeon.LimitedDrops.ICERAT_HP.count++;
            switch (Random.Int(11)){
                case 1:
                    w = new PotionOfFrost();   break;
                case 2:
                    w = new PotionOfLiquidFlame();   break;
                case 3:
                    w = new PotionOfToxicGas();   break;
                case 4:
                    w = new PotionOfHaste();   break;
                case 5:
                    w = new PotionOfInvisibility();   break;
                case 6:
                    w = new PotionOfLevitation();   break;
                case 7:
                    w = new PotionOfParalyticGas();   break;
                case 8:
                    w = new PotionOfPurity();   break;
                case 9:
                    w = new PotionOfLiquidFlameX();   break;
                case 10:
                    w = new PotionOfLightningShiledX();   break;
                default:
                    w = new PotionOfMindVision();
                    break;
            }
            return w;
        } else {
            return Generator.randomUsingDefaults(Generator.Category.SEED);
        }
    }
}
