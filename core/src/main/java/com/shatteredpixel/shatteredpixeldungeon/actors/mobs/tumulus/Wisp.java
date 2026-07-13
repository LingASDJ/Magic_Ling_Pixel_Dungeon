package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.tumulus;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.HalomethaneBurning;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ShadowParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfLiquidFlame;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfLiquidFlameX;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.WispSprite;
import com.watabou.noosa.tweeners.AlphaTweener;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

import java.util.ArrayList;

public class Wisp extends Mob {
    private static final float SPAWN_DELAY	= 2f;
    {
        HP = HT = 25;
        defenseSkill = 50;
        EXP = 9;
        maxLvl = 16;

        spriteClass = WispSprite.class;

        properties.add(Property.TUMULUS);
        properties.add(Property.DEMONIC);

        loot = PotionOfLiquidFlame.class;
        lootChance = 0.1428f;
    }

    @Override
    public int attackProc(Char enemy, int damage) {
        Buff.affect( enemy, HalomethaneBurning.class ).reignite( enemy, Random.Int(2,6) );
        return super.attackProc(enemy, damage);
    }

    @Override
    public Item createLoot() {
        Item drop = new PotionOfLiquidFlame();

        int replaceCount = Dungeon.LimitedDrops.WISP_PHANTOM_FIRE.count;
        float replaceChance = (float) (1.0 / Math.pow(3, replaceCount + 1));

        if (Random.Float() < replaceChance) {
            drop = new PotionOfLiquidFlameX();
            Dungeon.LimitedDrops.WISP_PHANTOM_FIRE.count++;
        }

        return drop;
    }

    @Override
    public int attackSkill(Char target) {
        return 21;
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(8, 15);
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(0, 2);
    }

    public static Wisp spawnAt(int pos ) {
        return spawnAt( pos, null );
    }

    public static Wisp spawnAt( int pos, Class<? extends Wisp> wraithClass ) {
        return spawnAt( pos, wraithClass, true );
    }

    private static Wisp spawnAt( int pos, Class<? extends Wisp> wraithClass, boolean allowAdjacent ) {

        //if the position itself is blocked, try to place in an adjacent cell if allowed
        if (Dungeon.level.solid[pos] || Actor.findChar( pos ) != null){
            ArrayList<Integer> candidates = new ArrayList<>();

            for (int i : PathFinder.NEIGHBOURS8){
                if (!Dungeon.level.solid[pos+i] && Actor.findChar( pos+i ) == null){
                    candidates.add(pos+i);
                }
            }

            if (allowAdjacent && !candidates.isEmpty()){
                pos = Random.element(candidates);
            } else {
                pos = -1;
            }

        }

        if (pos != -1) {

            Wisp w;
            //if no wraith type is specified, 1/100 chance for exotic, otherwise normal
            if (wraithClass == null){
                w = new Wisp();
            } else {
                w = Reflection.newInstance(wraithClass);
            }

            w.pos = pos;
            w.state = w.HUNTING;
            GameScene.add( w, SPAWN_DELAY );
            Dungeon.level.occupyCell(w);

            w.sprite.alpha( 0 );
            w.sprite.parent.add( new AlphaTweener( w.sprite, 1, 0.5f ) );

            w.sprite.emitter().burst(ShadowParticle.CURSE, 5);

            return w;
        } else {
            return null;
        }
    }


}
