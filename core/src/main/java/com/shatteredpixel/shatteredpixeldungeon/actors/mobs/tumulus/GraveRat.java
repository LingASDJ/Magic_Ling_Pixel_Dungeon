package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.tumulus;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.RotGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.FloatingText;
import com.shatteredpixel.shatteredpixeldungeon.items.food.tomb.GraveMeat;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GraveRatSprite;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class GraveRat extends Mob {

    {
        HP = HT = 40;
        defenseSkill = 16;
        EXP = 8;
        maxLvl = 15;
        spriteClass = GraveRatSprite.class;
        properties.add(Property.TUMULUS);
        lootChance = 0.16f;

        loot = GraveMeat.class;

        immunities.add(RotGas.class);
    }

    private ArrayList<Mob> lastMobs = new ArrayList<>();

    @Override
    protected boolean act() {
        if (!lastMobs.isEmpty()) {
            for (Mob mob : lastMobs) {
                if (mob == this) continue;
                if (!Dungeon.level.mobs.contains(mob)) {
                    int distance = Dungeon.level.distance(pos, mob.pos);
                    if (distance <= viewDistance) {
                        HP = Math.min(HP + 8, HT);
                        sprite.showStatusWithIcon(CharSprite.POSITIVE, Integer.toString(8), FloatingText.HEALING);
                        break;
                    }
                }
            }
        }

        lastMobs.clear();
        lastMobs.addAll(Dungeon.level.mobs);

        return super.act();
    }

    @Override
    public void die(Object cause) {
        super.die(cause);
        GameScene.add( Blob.seed( pos, 350, RotGas.class ).setStrength( 4));
    }

    @Override
    public int attackSkill(Char target) {
        return 16;
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(8, 19);
    }

    @Override
    public int drRoll() {
        return super.drRoll() + Random.NormalIntRange(0, 6);
    }
}