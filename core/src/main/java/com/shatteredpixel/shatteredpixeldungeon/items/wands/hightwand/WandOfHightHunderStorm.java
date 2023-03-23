package com.shatteredpixel.shatteredpixeldungeon.items.wands.hightwand;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.StormCloud;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Lightning;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SparkParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfLightningShiledX;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.CrivusFruitsFlake;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.DamageWand;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfLightning;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Shocking;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MagesStaff;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.ConeAOE;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.shatteredpixel.shatteredpixeldungeon.utils.BArray;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

import java.util.ArrayList;

public class WandOfHightHunderStorm extends DamageWand {

    {
        image = ItemSpriteSheet.WAND_HTR;
    }

    public static class Recipe extends com.shatteredpixel.shatteredpixeldungeon.items.Recipe.SimpleRecipe {

        {
            inputs =  new Class[]{PotionOfLightningShiledX.class, WandOfLightning.class, CrivusFruitsFlake.class};
            inQuantity = new int[]{1, 1, 1};

            cost = 15+Dungeon.depth/2;

            output = WandOfHightHunderStorm.class;
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

    private ArrayList<Char> affected = new ArrayList<>();

    private ArrayList<Lightning.Arc> arcs = new ArrayList<>();

    public int min(int lvl){
        return 4+lvl;
    }

    public int max(int lvl){
        return 4+5*lvl;
    }
    ConeAOE cone;
    @Override
    public void onZap(Ballistica bolt) {

        ArrayList<Char> affectedChars = new ArrayList<>();
        for( int cell : cone.cells ){

            //ignore caster cell
            if (cell == bolt.sourcePos){
                continue;
            }

            //only ignite cells directly near caster if they are flammable
            if (!Dungeon.level.adjacent(bolt.collisionPos, cell) || Dungeon.level.flamable[cell]){
                GameScene.add( Blob.seed( cell, 6+chargesPerCast(), StormCloud.class ) );
            }

            Char ch = Actor.findChar( cell );
            if (ch != null) {
                affectedChars.add(ch);
            }
        }

        //lightning deals less damage per-target, the more targets that are hit.
        float multipler = 0.4f + (0.75f/affected.size());
        //if the main target is in water, all affected take full damage
        if (Dungeon.level.water[bolt.collisionPos]) multipler = 1f;

        for (Char ch : affected){
            if (ch == Dungeon.hero) Camera.main.shake( 2, 0.3f );
            ch.sprite.centerEmitter().burst( SparkParticle.FACTORY, 3 );
            ch.sprite.flash();

            if (ch != curUser && ch.alignment == curUser.alignment && ch.pos != bolt.collisionPos){
                continue;
            }
            wandProc(ch, chargesPerCast());
            if (ch == curUser) {
                ch.damage(Math.round(damageRoll() * multipler * 0.8f), this);
            } else {
                ch.damage(Math.round(damageRoll() * multipler), this);
            }
        }

        if (!curUser.isAlive()) {
            Dungeon.fail( getClass() );
            GLog.n(Messages.get(this, "ondeath"));
        }
    }

    @Override
    public void onHit(MagesStaff staff, Char attacker, Char defender, int damage) {
        //acts like shocking enchantment
        new Shocking().proc(staff, attacker, defender, damage);
    }

    private void arc( Char ch ) {

        int dist = (Dungeon.level.water[ch.pos] && !ch.flying) ? 2 : 1;

        ArrayList<Char> hitThisArc = new ArrayList<>();
        PathFinder.buildDistanceMap( ch.pos, BArray.not( Dungeon.level.solid, null ), dist );
        for (int i = 0; i < PathFinder.distance.length; i++) {
            if (PathFinder.distance[i] < Integer.MAX_VALUE){
                Char n = Actor.findChar( i );
                if (n == Dungeon.hero && PathFinder.distance[i] > 1)
                    //the hero is only zapped if they are adjacent
                    continue;
                else if (n != null && !affected.contains( n )) {
                    hitThisArc.add(n);
                }
            }
        }

        affected.addAll(hitThisArc);
        for (Char hit : hitThisArc){
            arcs.add(new Lightning.Arc(ch.sprite.center(), hit.sprite.center()));
            arc(hit);
        }
    }

    @Override
    public void fx(Ballistica bolt, Callback callback) {

        affected.clear();
        arcs.clear();

        // 4/6/8 distance
        int maxDist = (1 + 2*chargesPerCast())*level/5+2;
        int dist = Math.min(bolt.dist, maxDist);

        cone = new ConeAOE( bolt.sourcePos, bolt.path.get(dist),
                maxDist,
                5 + 10*chargesPerCast(),
                collisionProperties | Ballistica.MAGIC_BOLT);
        int cell = bolt.collisionPos;


        //cast to cells at the tip, rather than all cells, better performance.
        for (Ballistica ray : cone.rays){
            ((MagicMissile)curUser.sprite.parent.recycle( MagicMissile.class )).reset(
                    MagicMissile.ELMO,
                    curUser.sprite,
                    ray.path.get(ray.dist),
                    null
            );
        }

        Char ch = Actor.findChar( cell );
        if (ch != null) {
            affected.add( ch );
            arcs.add( new Lightning.Arc(curUser.sprite.center(), ch.sprite.center()));
            arc(ch);
        } else {
            arcs.add( new Lightning.Arc(curUser.sprite.center(), DungeonTilemap.raisedTileCenterToWorld(bolt.collisionPos)));
            CellEmitter.center( cell ).burst( SparkParticle.FACTORY, 3 );
        }

        //don't want to wait for the effect before processing damage.
        curUser.sprite.parent.addToFront( new Lightning( arcs, null ) );
        Sample.INSTANCE.play( Assets.Sounds.LIGHTNING );
        callback.call();
    }

    @Override
    public void staffFx(MagesStaff.StaffParticle particle) {
        particle.color(0xFFFFFF);
        particle.am = 0.6f;
        particle.setLifespan(0.6f);
        particle.acc.set(0, +10);
        particle.speed.polar(-Random.Float(3.1415926f), 6f);
        particle.setSize(0f, 1.5f);
        particle.sizeJitter = 1f;
        particle.shuffleXY(1f);
        float dst = Random.Float(1f);
        particle.x -= dst;
        particle.y += dst;
    }

}

