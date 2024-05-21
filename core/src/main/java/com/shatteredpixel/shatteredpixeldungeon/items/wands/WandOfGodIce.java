package com.shatteredpixel.shatteredpixeldungeon.items.wands;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blizzard;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Freezing;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.WorstBlizzardFx;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Chill;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Frost;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.WorstBlizzard;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.RainbowParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ShadowParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfMagicMapping;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Lucky;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MagesStaff;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.ConeAOE;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.ShadowCaster;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Point;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

public class WandOfGodIce extends DamageWand {

    {
        image = ItemSpriteSheet.HIGHTWAND_4;

        collisionProperties = Ballistica.MAGIC_BOLT;
    }

    //1x/2x/3x damage
    public int min(int lvl){
        return (5+lvl/2) * chargesPerCast();
    }

    //1x/2x/3x damage
    public int max(int lvl){
        return (10*lvl/2) * chargesPerCast();
    }

    @Override
    public void onZap(Ballistica beam) {
        affectMap(beam);

        if (Dungeon.level.viewDistance < 6 ){
            if (Dungeon.isChallenged(Challenges.DARKNESS)){
                Buff.prolong( curUser, Chill.class, 2f + buffedLvl());
            } else {
                Buff.prolong( curUser, Chill.class, 10f+buffedLvl()*5);
            }
        }

        Char ch = Actor.findChar(beam.collisionPos);
        if (ch != null){
            processSoulMark(ch, chargesPerCast());
            ch.damage(affectTarget(ch),this);
        }
        Point c = Dungeon.level.cellToPoint(beam.collisionPos);
        int dist = Math.min(2+level(),6);
        int[] rounding = ShadowCaster.rounding[dist];

        int left, right;
        int curr;
        for (int y = Math.max(0, c.y - dist); y <= Math.min(Dungeon.level.height()-1, c.y + dist); y++) {
            if (rounding[Math.abs(c.y - y)] < Math.abs(c.y - y)) {
                left = c.x - rounding[Math.abs(c.y - y)];
            } else {
                left = dist;
                while (rounding[left] < rounding[Math.abs(c.y - y)]) {
                    left--;
                }
                left = c.x - left;
            }
            right = Math.min(Dungeon.level.width() - 1, c.x + c.x - left);
            left = Math.max(0, left);
            for (curr = left + y * Dungeon.level.width(); curr <= right + y * Dungeon.level.width(); curr++) {
                if (!Dungeon.level.solid[curr]) {
                    WorstBlizzardFx.keepTime = Math.min(2+level(),14);
                    WorstBlizzardFx.damageTarget = affectTarget(null);
                    GameScene.add(Blob.seed(curr, 10, WorstBlizzardFx.class));
                    Char charAt= Actor.findChar(curr);
                    if(charAt!=null ){
                        Buff.affect(charAt, WorstBlizzard.class,1f);
                    }
                }
            }
        }
    }

    private int affectTarget(Char ch){
        int dmg = damageRoll();

        //three in (5+lvl) chance of failing
        if (Random.Int(5+buffedLvl()) >= 3) {
            Buff.prolong(ch, Chill.class, 2f + (buffedLvl() * 0.333f));
            ch.sprite.emitter().burst(Speck.factory(Speck.STAR), 12 );
        }
        dmg += level() > 4 ? level() - 4 : 0;

        if (ch.properties().contains(Char.Property.BOSS)){
            ch.sprite.emitter().start( ShadowParticle.CURSE, 0.05f, 10+buffedLvl() );
            Sample.INSTANCE.play(Assets.Sounds.BURNING);
            dmg = Math.round(dmg*2f);
        } else {
            ch.sprite.centerEmitter().burst( RainbowParticle.BURST, 10+buffedLvl() );
        }
        return dmg;
    }

    private void affectMap(Ballistica beam){
        boolean noticed = false;
        for (int c : beam.subPath(0, beam.dist)){
            if (!Dungeon.level.insideMap(c)){
                continue;
            }
            for (int n : PathFinder.NEIGHBOURS9){
                int cell = c+n;

                if (Dungeon.level.discoverable[cell])
                    Dungeon.level.mapped[cell] = true;

                int terr = Dungeon.level.map[cell];
                if ((Terrain.flags[terr] & Terrain.SECRET) != 0) {

                    Dungeon.level.discover( cell );

                    GameScene.discoverTile( cell, terr );
                    ScrollOfMagicMapping.discover(cell);

                    noticed = true;
                }
            }

            CellEmitter.center(c).burst( RainbowParticle.BURST, Random.IntRange( 12, 25 ) );
        }
        if (noticed)
            Sample.INSTANCE.play( Assets.Sounds.SECRET );

        GameScene.updateFog();
    }

    @Override
    public void fx( Ballistica beam, Callback callback ) {
        MagicMissile.boltFromChar( curUser.sprite.parent,
                MagicMissile.SHAMAN_PURPLE,
                curUser.sprite,
                beam.collisionPos,
                callback);
        Sample.INSTANCE.play( Assets.Sounds.ZAP );
    }

    @Override
    public void onHit(MagesStaff staff, Char attacker, Char defender, int damage) {
        new Lucky().proc( staff, attacker, defender, damage);
    }

    @Override
    public void staffFx(MagesStaff.StaffParticle particle) {
        particle.color( Random.Int( 0x00fffff ) );
        particle.am = 0.5f;
        particle.setLifespan(1f);
        particle.speed.polar(Random.Float(PointF.G2R), 2f);
        particle.setSize( 1f, 2f);
        particle.radiateXY( 0.5f);
    }

}