package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.HalomethaneBurning;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.FireCrystalSprites;

public class TurbidFlameCore extends Mob implements Mob.NoMobSpawn {

    {
        spriteClass = FireCrystalSprites.class;

        HP = HT = 10;

        state = PASSIVE;

        properties.add(Property.MINIBOSS);
        properties.add(Property.INORGANIC);
        properties.add(Property.IMMOVABLE);

        immunities.add(Burning.class);
        immunities.add(HalomethaneBurning.class);

        lootChance = 0f;
    }

    @Override
    public boolean interact(Char c) {
        return true;
    }

    @Override
    public boolean add(Buff buff) {
        return false;
    }

    @Override
    public String name() {
        return Messages.get(FireMagicDied.class, "core_name");
    }

    @Override
    public String description() {
        return Messages.get(FireMagicDied.class, "core_desc");
    }

    @Override
    public void die(Object cause) {
        //通知莲娜：一个浊焰核心被摧毁
        for (Mob m : Dungeon.level.mobs.toArray(new Mob[0])){
            if (m instanceof FireMagicDied){
                ((FireMagicDied) m).onCoreDestroyed(this);
            }
        }
        super.die(cause);
        Dungeon.level.mobs.remove(this);
    }
}

