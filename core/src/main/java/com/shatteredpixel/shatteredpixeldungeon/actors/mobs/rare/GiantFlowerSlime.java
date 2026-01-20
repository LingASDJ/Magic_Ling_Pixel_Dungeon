package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.rare;

import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bleeding;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Chill;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Roots;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Crab;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Rat;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Slime;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Swarm;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.SkeletonKey;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.GooBlob;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.MetalShard;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Plant;
import com.shatteredpixel.shatteredpixeldungeon.plants.Sungrass;
import com.shatteredpixel.shatteredpixeldungeon.plants.Swiftthistle;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.FlowerSlimeSprites;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GiantFlowerSlimeSprites;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class GiantFlowerSlime extends Mob {

    {
        spriteClass = GiantFlowerSlimeSprites.class;

        HP = HT = 14;

        defenseSkill = 2;

        EXP = 2;

        maxLvl = 5;
    }

    @Override
    public int attackProc( Char enemy, int damage ) {
        damage = super.attackProc( enemy, damage );
        if (damage > 0) {
            if(Random.Int( 3 ) == 0){
                Buff.affect( enemy, Burning.class ).reignite( enemy, 2f );
            } else if(Random.Int( 3 ) == 0){
                Buff.affect( enemy, Chill.class, 3f );
            } else if(Random.Int( 3 ) == 0){
                Buff.affect( enemy, Roots.class, 5f );
            } else if(Random.Int( 5 ) == 0) {
                Buff.affect(this, Sungrass.Health.class).boost(5);
            } else {
                Buff.affect(this, Swiftthistle.TimeBubble.class).setLeft(5f);
            }
        }

        return damage;
    }

    @Override
    public void rollToDropLoot() {
        super.rollToDropLoot();

        int ofs;
        do {
            ofs = PathFinder.NEIGHBOURS8[Random.Int(8)];
        } while (Dungeon.level.solid[pos + ofs] && !Dungeon.level.passable[pos + ofs]);

        Dungeon.level.drop(Generator.randomUsingDefaults( Generator.Category.SEED ), pos + ofs ).sprite.drop( pos );
        Dungeon.level.drop(Generator.randomUsingDefaults( Generator.Category.SEED ), pos + ofs ).sprite.drop( pos );
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange( 1, 5 );
    }

    @Override
    public int attackSkill( Char target ) {
        return 8;
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(0, 1);
    }

}
