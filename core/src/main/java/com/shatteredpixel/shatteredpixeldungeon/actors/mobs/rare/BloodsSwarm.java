package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.rare;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.RedSwarm;
import com.shatteredpixel.shatteredpixeldungeon.effects.Pushing;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfLiquidFlameX;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.BloodsSwarmSprite;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class BloodsSwarm extends Mob {

    {
        spriteClass = BloodsSwarmSprite.class;

        HP = HT = 80;
        defenseSkill = 15;

        EXP = 15;

        maxLvl = 18;

        flying = true;

        loot = new PotionOfLiquidFlameX();
        lootChance = 1f;

        properties.add(Property.UNDEAD);
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange( 5, 10 );
    }

    @Override
    public void damage( int damage, Object src, DamageType type){

        ArrayList<Integer> candidates = new ArrayList<>();

        int[] neighbours = {pos + 1, pos - 1, pos + Dungeon.level.width(), pos - Dungeon.level.width()};
        for (int n : neighbours) {
            if (!Dungeon.level.solid[n]
                    && Actor.findChar( n ) == null
                    && (Dungeon.level.passable[n] || Dungeon.level.avoid[n])
                    && (!properties().contains(Property.LARGE) || Dungeon.level.openSpace[n])) {
                candidates.add( n );
            }
        }
        if (!candidates.isEmpty()) {
            RedSwarm clone = new RedSwarm();
            clone.split();
            clone.pos = Random.element( candidates );
            clone.spawnBloods = true;
            clone.state = clone.HUNTING;
            clone.maxLvl = -1;

            GameScene.add( clone, clone.SPLIT_DELAY );

            int splitHP = Math.max(10, (int) ((HP - damage) * 0.35f));
            clone.HP = splitHP;
            Actor.add( new Pushing( clone, pos, clone.pos ) );

            Dungeon.level.occupyCell(clone);
            HP = Math.max(1, HP - splitHP);
        }
        super.damage(damage,src,type);
    }

    @Override
    public int attackSkill( Char target ) {
        return 20;
    }

    @Override
    public int drRoll() {
        return super.drRoll() + Random.NormalIntRange(0, 2);
    }

}

