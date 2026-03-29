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

package com.shatteredpixel.shatteredpixeldungeon.items.wands.hightwand;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.HalomethaneFire;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.HalomethaneBurning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hex;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.gold.SkullSprite;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BlastParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SmokeParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfLiquidFlameX;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.CrivusFruitsFlake;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfSirensSong;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.DamageWand;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfFireblast;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MagesStaff;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.ConeAOE;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

import java.util.ArrayList;

public class WandOfBlueFuck extends DamageWand {

    {
        image = ItemSpriteSheet.HIGHTWAND_3;

        collisionProperties = Ballistica.WONT_STOP;
    }

    public static class Recipe extends com.shatteredpixel.shatteredpixeldungeon.items.Recipe.SimpleRecipe {

        {
            inputs =  new Class[]{PotionOfLiquidFlameX.class, WandOfFireblast.class, CrivusFruitsFlake.class};
            inQuantity = new int[]{1, 1, 1};

            cost = 15+Dungeon.depth/2;

            output = WandOfBlueFuck.class;
            outQuantity = 1;
        }

        public final Item sampleOutput(ArrayList<Item> ingredients){
            try {
                Item result = Reflection.newInstance(output);
                result.quantity(outQuantity).level(Random.NormalIntRange(2,4));
                return result;
            } catch (Exception e) {
                ShatteredPixelDungeon.reportException( e );
                return null;
            }
        }

    }


    //1x/2x/3x damage
    public int min(int lvl){
        return (2+lvl+Dungeon.depth/5) * chargesPerCast();
    }

    //1x/2x/3x damage
    public int max(int lvl){
        return (3*lvl+Dungeon.depth/5) * chargesPerCast();
    }

    ConeAOE cone;

    @Override
    public void onZap(Ballistica bolt) {

        ArrayList<Char> affectedChars = new ArrayList<>();
        ArrayList<Integer> adjacentCells = new ArrayList<>();
        for( int cell : cone.cells ){

            //ignore caster cell
            if (cell == bolt.sourcePos){
                continue;
            }

            //knock doors open
            if (Dungeon.level.map[cell] == Terrain.DOOR){
                Level.set(cell, Terrain.OPEN_DOOR);
                GameScene.updateMap(cell);
            }

            //only ignite cells directly near caster if they are flammable or solid
            if (Dungeon.level.adjacent(bolt.sourcePos, cell)
                    && !(Dungeon.level.flamable[cell] || Dungeon.level.solid[cell])){
                adjacentCells.add(cell);
                //do burn any heaps located here though
                if (Dungeon.level.heaps.get(cell) != null){
                    Dungeon.level.heaps.get(cell).burn();
                }
            } else {
                GameScene.add( Blob.seed( cell, 6+chargesPerCast(), HalomethaneFire.class ) );
            }

            Char ch = Actor.findChar( cell );
            if (ch != null) {
                affectedChars.add(ch);
            }
        }

        //if wand was shot right at a wall
        if (cone.cells.isEmpty()){
            adjacentCells.add(bolt.sourcePos);
        }

        //ignite cells that share a side with an adjacent cell, are flammable, and are closer to the collision pos
        //This prevents short-range casts not igniting barricades or bookshelves
        for (int cell : adjacentCells){
            for (int i : PathFinder.NEIGHBOURS8){
                if (Dungeon.level.trueDistance(cell+i, bolt.collisionPos) < Dungeon.level.trueDistance(cell, bolt.collisionPos)
                        && Dungeon.level.flamable[cell+i]
                        && HalomethaneFire.volumeAt(cell+i, HalomethaneFire.class) == 0){
                    GameScene.add( Blob.seed( cell+i, 1+chargesPerCast(), HalomethaneFire.class ) );
                }
            }
        }

        for ( Char ch : affectedChars ){
            wandProc(ch, chargesPerCast());
            ch.damage(damageRoll(), this);
            if (ch.isAlive()) {
                Buff.affect(ch, HalomethaneBurning.class).reignite(ch);
                switch (chargesPerCast()) {
                    case 1:
                        break;
                    case 2:
                        Buff.affect(ch, Blindness.class, 4f);
                        break;
                    case 3:
                        Buff.affect(ch, Paralysis.class, 4f);
                        break;
                }
            }
        }
    }


    public void onAIZap(Ballistica bolt) {
        ConeAOE conex;
        ArrayList<Char> affectedChars = new ArrayList<>();
        ArrayList<Integer> adjacentCells = new ArrayList<>();
        int maxDist = 3 + 2*chargesPerCast();

        conex = new ConeAOE( bolt,
                maxDist,
                30 + 20*chargesPerCast(),
                Ballistica.STOP_TARGET | Ballistica.STOP_SOLID | Ballistica.IGNORE_SOFT_SOLID);
        for( int cell : conex.cells ){

            //ignore caster cell
            if (cell == bolt.sourcePos){
                continue;
            }

            //knock doors open
            if (Dungeon.level.map[cell] == Terrain.DOOR){
                Level.set(cell, Terrain.OPEN_DOOR);
                GameScene.updateMap(cell);
            }

            //only ignite cells directly near caster if they are flammable or solid
            if (Dungeon.level.adjacent(bolt.sourcePos, cell)
                    && !(Dungeon.level.flamable[cell] || Dungeon.level.solid[cell])){
                adjacentCells.add(cell);
                //do burn any heaps located here though
                if (Dungeon.level.heaps.get(cell) != null){
                    Dungeon.level.heaps.get(cell).burn();
                }
            } else {
                GameScene.add( Blob.seed( cell, 1+chargesPerCast(), HalomethaneFire.class ) );
            }

            Char ch = Actor.findChar( cell );
            if (ch != null) {
                affectedChars.add(ch);
            }
        }

        //if wand was shot right at a wall
        if (conex.cells.isEmpty()){
            adjacentCells.add(bolt.sourcePos);
        }

        //ignite cells that share a side with an adjacent cell, are flammable, and are closer to the collision pos
        //This prevents short-range casts not igniting barricades or bookshelves
        for (int cell : adjacentCells){
            for (int i : PathFinder.NEIGHBOURS8){
                if (Dungeon.level.trueDistance(cell+i, bolt.collisionPos) < Dungeon.level.trueDistance(cell, bolt.collisionPos)
                        && Dungeon.level.flamable[cell+i]
                        && HalomethaneFire.volumeAt(cell+i, HalomethaneFire.class) == 0){
                    GameScene.add( Blob.seed( cell+i, 1+chargesPerCast(),HalomethaneFire.class ) );
                }
            }
        }

        for ( Char ch : affectedChars ){
            wandProc(ch, chargesPerCast());
            ch.damage(damageRoll(), this);
            if (ch.isAlive()) {
                Buff.affect(ch, HalomethaneBurning.class).reignite(ch);
                switch (chargesPerCast()) {
                    case 1:
                        break; //no effects
                    case 2:
                        Buff.affect(ch, Blindness.class, 5f);
                        break;
                    case 3:
                        Buff.affect(ch, Paralysis.class, 4f);
                        break;
                }
            }
        }
    }

    @Override
    public void onHit(MagesStaff staff, Char attacker, Char defender, int damage) {

        //proc chance is initially 0..
        float procChance = 0;
        for (int i : PathFinder.CIRCLE7) {

            //+25% proc chance per burning char within 8x8 of target
            // this includes the attacker and defender
            if (Actor.findChar(defender.pos + i) != null
                    && Actor.findChar(defender.pos + i).buff(Burning.class) != null) {
                procChance += 0.25f;

                //otherwise +5% proc chance per burning tile within 3x3 of target
            } else if (HalomethaneFire.volumeAt(defender.pos+i, HalomethaneFire.class) > 0){
                procChance += 0.05f;
            }
        }

        procChance = Math.min(1f, procChance);
        procChance *= Wand.procChanceMultiplier(attacker);

        if(Random.Float() < Math.min(0.05f + (level() * 0.1f), 1) && damage > defender.HP && hero.buff(FireBoomSkullSpawnCooldown.class) == null){
            FireBoomSkull fireBoomSkull = new FireBoomSkull();
            fireBoomSkull.HT = fireBoomSkull.HP = defender.HT/2;
            fireBoomSkull.pos = defender.pos;
            fireBoomSkull.state = fireBoomSkull.WANDERING;
            Buff.affect(fireBoomSkull, ScrollOfSirensSong.Enthralled.class);
            GameScene.add(fireBoomSkull);
            Buff.affect(hero, FireBoomSkullSpawnCooldown.class, 50f);
        }

        if (Random.Float() < procChance){

            float powerMulti = Math.max(1f, procChance);

            Blob fire = Dungeon.level.blobs.get(HalomethaneFire.class);

            //explode, dealing damage to enemies in 3x3, and clearing all fire
            CellEmitter.center(defender.pos).burst(BlastParticle.FACTORY, 30);
            if (fire != null) {
                for (int i : PathFinder.CIRCLE7) {
                    CellEmitter.get(defender.pos + i).burst(SmokeParticle.FACTORY, 4);
                    if (HalomethaneFire.volumeAt(defender.pos+i, HalomethaneFire.class) > 0){
                        Dungeon.level.destroy(defender.pos + i);
                        GameScene.updateMap(defender.pos + i);
                        fire.clear(defender.pos + i);
                    }

                    Char ch = Actor.findChar(defender.pos + i);
                    if (ch != null) {
                        if (ch.buff(HalomethaneBurning.class) != null) {
                            ch.buff(HalomethaneBurning.class).detach();
                        }
                        if (ch.alignment == Char.Alignment.ENEMY) {
                            //damage of a 2-charge zap
                            Buff.affect(ch, Blindness.class, 4f);
                            Buff.affect(ch, Hex.class, 4f);
                            ch.damage(Math.round(powerMulti* Hero.heroDamageIntRange(2 + 2*buffedLvl(), 8 + 4*buffedLvl())), this);
                        }
                    }
                }
            }

            Sample.INSTANCE.play( Assets.Sounds.BLAST );
        }
    }

    @Override
    public int getMissileType() {
        return MagicMissile.HALOFIRE;
    }

    @Override
    public void fx(Ballistica bolt, Callback callback) {
        //need to perform flame spread logic here so we can determine what cells to put flames in.

        // 5/7/9 distance
        int maxDist = 3 + 2*chargesPerCast();

        cone = new ConeAOE( bolt,
                maxDist,
                30 + 20*chargesPerCast(),
                Ballistica.STOP_TARGET | Ballistica.STOP_SOLID | Ballistica.IGNORE_SOFT_SOLID);

        //cast to cells at the tip, rather than all cells, better performance.
        Ballistica longestRay = null;
        for (Ballistica ray : cone.outerRays){
            if (longestRay == null || ray.dist > longestRay.dist){
                longestRay = ray;
            }
            ((MagicMissile)curUser.sprite.parent.recycle( MagicMissile.class )).reset(
                    MagicMissile.HALOFIRE,
                    curUser.sprite,
                    ray.path.get(ray.dist),
                    null
            );
        }

        //final zap at half distance of the longest ray, for timing of the actual wand effect
        MagicMissile.boltFromChar( curUser.sprite.parent,
                MagicMissile.HALOFIRE,
                curUser.sprite,
                longestRay.path.get(longestRay.dist/2),
                callback );
        Sample.INSTANCE.play( Assets.Sounds.ZAP );
        Sample.INSTANCE.play( Assets.Sounds.BURNING );
    }

    @Override
    public int chargesPerCast() {
        //consumes 30% of current charges, rounded up, with
        // a minimum of one.
        return Math.max(1, (int)Math.ceil(curCharges*0.3f));
    }

    @Override
    public String statsDesc() {
        if (levelKnown)
            return Messages.get(this, "stats_desc", chargesPerCast(), min(), max());
        else
            return Messages.get(this, "stats_desc", chargesPerCast(), min(0), max(0));
    }

    @Override
    public void staffFx(MagesStaff.StaffParticle particle) {
        particle.color( 0xEE7722 );
        particle.am = 0.5f;
        particle.setLifespan(0.6f);
        particle.acc.set(0, -20);
        particle.setSize( 0f, 1.5f);
        particle.shuffleXY( 0.4f );
    }

    public static class SkySkullSprite extends SkullSprite {

        @Override
        public void update() {
            super.update();
            if (flashTime <= 0) {
                float interval = (Game.timeTotal % 9) / 3f;
                tint(0,
                        interval > 1 ? Math.max(0.9f, 1 - interval) : interval,
                        interval < 1 ? Math.min(153 + interval * 72, 255) : Math.max(153 - (interval - 1) * 72, 128),
                        0.5f);
            }
        }
    }

    public static class FireBoomSkullSpawnCooldown extends FlavourBuff {
        @Override
        public int icon() {
            return BuffIndicator.TIME;
        }

        @Override
        public void tintIcon(Image icon) {
            icon.hardlight(Window.BLUE_COLOR);
        }
    };

    public static class FireBoomSkull extends Mob {
        {
            HP = HT = 5;

            spriteClass = SkySkullSprite.class;

            defenseSkill = 0;

            viewDistance = 30;

            flying = true;
            alignment = Alignment.ALLY;
        }

        @Override
        public boolean add(Buff buff) {
            return false;
        }

        @Override
        public boolean attack(Char enemy, float dmgMulti, float dmgBonus, float accMulti,DamageType type) {
            die(enemy);
            return super.attack(enemy, dmgMulti, dmgBonus, accMulti, type);
        }

        @Override
        public void die(Object cause) {
            super.die(cause);
            int cell;

            for (int i : PathFinder.NEIGHBOURS9){
                cell = pos + i;

                if (!Dungeon.level.solid[cell] && !Dungeon.level.water[cell + i] && cell != hero.pos) {
                    GameScene.add( Blob.seed( cell, 3, HalomethaneFire.class ) );
                }

                Blob fire = Dungeon.level.blobs.get(HalomethaneFire.class);
                if(hero.pos == cell){
                    fire.clear(cell);
                }

                if (Dungeon.level.heroFOV[cell]) {
                    CellEmitter.center(cell).burst(BlastParticle.FACTORY, 5);
                }
            }
        }

        @Override
        public int attackSkill(Char target) {
            return 1000;
        }

        @Override
        public int damageRoll() {
            return Random.Int(hero.lvl*2 + 4, hero.lvl*3 + 5);
        }
    }

}
