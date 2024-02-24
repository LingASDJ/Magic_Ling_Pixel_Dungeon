package com.shatteredpixel.shatteredpixeldungeon.items.wands.hightwand;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Electricity;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Fire;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Regrowth;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.StormCloud;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Chill;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Frost;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Lightning;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BlastParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.FlameParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SmokeParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfEnchantment;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.DamageWand;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfGodIce;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Unstable;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MagesStaff;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.ColorMath;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

import java.util.ArrayList;

public class WandOfVenom extends DamageWand {

    {
        image = ItemSpriteSheet.HIGHTWAND_1;
    }

    public static class Recipe extends com.shatteredpixel.shatteredpixeldungeon.items.Recipe.SimpleRecipe {

        {
            inputs =  new Class[]{WandOfBlueFuck.class, ScrollOfEnchantment.class , WandOfGodIce.class};
            inQuantity = new int[]{1, 1, 1};

            cost = 20;

            output = WandOfVenom.class;
            outQuantity = 1;
        }

        public final Item sampleOutput(ArrayList<Item> ingredients){
            try {
                Item result = Reflection.newInstance(output);
                result.quantity(outQuantity).level(Random.NormalIntRange(2,3));
                result.upgrade();
                return result;
            } catch (Exception e) {
                ShatteredPixelDungeon.reportException( e );
                return null;
            }
        }

    }

    private int randomCast = Random.Int(5);

    @Override
    public void onZap( Ballistica bolt ) {
        this.chaosZap( bolt,
                randomCast,
                level());
        randomCast = Random.Int(5);
    }

    private void chaosZap( Ballistica bolt, int castType, int lvl ) {
        int damage;
        Char ch;
        switch(castType) {
            default:
            case 0: //magic missile
                ch = Actor.findChar(bolt.collisionPos);
                if (ch != null) {
                    damage = Random.IntRange(
                            2+lvl+Dungeon.hero.lvl/4,
                            8+2*lvl+Dungeon.hero.lvl/2);
                    ch.damage(damage, this);
                    ch.sprite.burst(0xFFFFFFFF, lvl / 2 + 2);
                }
                break;
            case 1: //thunderbolt
                ch = Actor.findChar(bolt.collisionPos);
                if (ch != null) {
                    damage = Random.IntRange(
                            3+lvl,
                            10+4*lvl);
                    ch.damage(damage, this);
                }
                if (Dungeon.level.heroFOV[bolt.collisionPos]) {
                    CellEmitter.center(bolt.collisionPos).burst(BlastParticle.FACTORY, 30);
                    Sample.INSTANCE.play( Assets.Sounds.BLAST );
                }
                for (int i : PathFinder.NEIGHBOURS8) {
                    int AOE = bolt.collisionPos + i;
                    if (AOE >= 0 && AOE < Dungeon.level.length()) {
                        ch = Actor.findChar(AOE);
                        if (ch != null) {
                            damage = Random.IntRange(
                                    1+lvl,
                                    6+2*lvl);
                          //  processSoulMark(ch, chargesPerCast());
                            ch.damage(damage, this);
                        }
                        if (Dungeon.level.heroFOV[AOE]) {
                            CellEmitter.get(AOE).burst(SmokeParticle.FACTORY, 4);
                        }
                    }
                }
                break;
            case 2: //frostfire
                ch = Actor.findChar(bolt.collisionPos);
                if (ch != null) {
                    damage = Random.IntRange(
                            4+2*lvl,
                            16+6*lvl);
                  //  processSoulMark(ch, chargesPerCast());
                    ch.damage(damage, this); //second hit
                    if (ch.isAlive() && ch.buff(Frost.class) == null) {
                        if (Dungeon.level.water[ch.pos])
                            Buff.prolong(ch, Chill.class, 4+lvl);
                        else
                            Buff.prolong(ch, Chill.class, 2+lvl);
                    }
                }
                CellEmitter.get(bolt.collisionPos).burst(FlameParticle.FACTORY, 6);
                break;
            case 3: //flaming regrowth
                ch = Actor.findChar(bolt.collisionPos);
                if (ch != null) {
                    damage = Random.IntRange(
                            1+lvl,
                            6+2*lvl);
                  //  processSoulMark(ch, chargesPerCast());
                    ch.damage(damage, this);
                }
                GameScene.add(Blob.seed(bolt.collisionPos, 30, Regrowth.class));
                GameScene.add(Blob.seed(bolt.collisionPos, 2, Fire.class));
                break;
            case 4: //electric storm cloud
                ch = Actor.findChar(bolt.collisionPos);
                if (ch != null) {
                    damage = Random.IntRange(
                            3+lvl,
                            10+4*lvl);
                  //  processSoulMark(ch, chargesPerCast());
                    ch.damage(damage, this);
                }
                GameScene.add(Blob.seed(bolt.collisionPos, 50, StormCloud.class));
                GameScene.add(Blob.seed(bolt.collisionPos, 10, Electricity.class));
                break;
        }
    }

    public void fx(Ballistica bolt, Char caster, Callback callback) {
        switch(randomCast) {
            default:
            case 0: //magic missile
                MagicMissile.boltFromChar( caster.sprite.parent,
                        MagicMissile.MAGIC_MISSILE,
                        caster.sprite,
                        bolt.collisionPos,
                        callback);
                Sample.INSTANCE.play( Assets.Sounds.ZAP );
                break;
            case 1: //thunderbolt
                caster.sprite.parent.addToFront( new Lightning( caster.pos, bolt.collisionPos, null ) );
                callback.call();
                break;
            case 2: //frostfire
                MagicMissile.boltFromChar(caster.sprite.parent,
                        MagicMissile.FIRE,
                        caster.sprite,
                        bolt.collisionPos,
                        null);
                MagicMissile.boltFromChar(caster.sprite.parent,
                        MagicMissile.FROST,
                        caster.sprite,
                        bolt.collisionPos,
                        callback);
                Sample.INSTANCE.play( Assets.Sounds.ZAP );
                break;
            case 3: //flaming regrowth
                MagicMissile.boltFromChar(caster.sprite.parent,
                        MagicMissile.FIRE,
                        caster.sprite,
                        bolt.collisionPos,
                        null);
                MagicMissile.boltFromChar(caster.sprite.parent,
                        MagicMissile.FOLIAGE,
                        caster.sprite,
                        bolt.collisionPos,
                        callback);
                Sample.INSTANCE.play( Assets.Sounds.ZAP );
                break;
            case 4: //electric storm cloud
                MagicMissile.boltFromChar(caster.sprite.parent,
                        MagicMissile.SWORDLING,
                        caster.sprite,
                        bolt.collisionPos,
                        callback);
                MagicMissile.boltFromChar(caster.sprite.parent,
                        MagicMissile.SPARK_CONE,
                        caster.sprite,
                        bolt.collisionPos,
                        callback);
                Sample.INSTANCE.play( Assets.Sounds.ZAP );
                break;
        }
    }

    @Override
    public void onHit(MagesStaff staff, Char attacker, Char defender, int damage) {
        new Unstable().proc( staff, attacker, defender, damage);
    }

    @Override
    public void staffFx(MagesStaff.StaffParticle particle) {
        particle.color(ColorMath.random(Window.SHPX_COLOR, Window.GDX_COLOR));
        particle.am = 0.6f;
        particle.setLifespan(0.6f);
        particle.alpha(1f);
        particle.acc.set(0, +10);
        particle.speed.polar(-Random.Float(3.1415926f), 6f);
        particle.setSize(1f, 1.5f);
        particle.sizeJitter = 1f;
        particle.angularSpeed = 25;
        particle.shuffleXY(1f);
        float dst = Random.Float(1f);
        particle.x -= dst;
        particle.y += dst;
    }

    protected int initialCharges() {
        return 2;
    }

    @Override
    public int min(int lvl) {
        return 3+4*lvl;
    }

    @Override
    public int max(int lvl) {
        return 8+3*lvl;
    }
}