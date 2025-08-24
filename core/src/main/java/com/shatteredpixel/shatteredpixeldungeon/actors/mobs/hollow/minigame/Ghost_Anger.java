package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.hollow.minigame;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Doom;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.journal.Bestiary;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CrystalGuardianSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MiniGhostSprite;

public class Ghost_Anger extends Mob {

    {
        spriteClass = MiniGhostSprite.class;
        HT = HP = 10;
        baseSpeed = 0.75f;
    }

    @Override
    public boolean isAlive() {
        if (HP <= 0){
            HP = 1;

            for (Buff b : buffs()){
                if (!(b instanceof Doom || b instanceof Cripple)) {
                    b.detach();
                }
            }

            if (HP == 1) {
                Bestiary.setSeen(getClass());
                Bestiary.countEncounter(getClass());
                if (sprite != null) ((MiniGhostSprite) sprite).crumple();
            }
        }
        return super.isAlive();
    }

    @Override
    public int attackSkill( Char target ) {
        return Integer.MAX_VALUE;
    }

    @Override
    public int drRoll() {
        return 0;
    }

    @Override
    protected boolean act() {
        AiState lastState = state;
        if(lastState == SLEEPING){
            ScrollOfTeleportation.appear(this, Dungeon.level.randomDestination(this));
            state = HUNTING;
        }
        beckon(hero.pos);
        return super.act();
    }

}
