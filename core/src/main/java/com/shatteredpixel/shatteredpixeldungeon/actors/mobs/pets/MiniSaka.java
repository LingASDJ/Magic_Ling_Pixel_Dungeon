package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.pets;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.SakaFishSketon;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MiniSakaFishBossSprites;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class MiniSaka extends Pets {

    private static final String[] TXT_RANDOM = {"saka……saka……", "saka!!!","saka?!"};

    private static final int IDLE_DEATH_TURNS = 45;
    private int idleTurns = 0;
    private int lastHeroPos = -1;

    {
        spriteClass = MiniSakaFishBossSprites.class;
        WANDERING = new Wandering();
        defenseSkill = 15;
    }

    @Override
    public void die( Object cause ) {
        super.die(cause);
        this.sprite.showStatus(0xFCE9CC, "saka……T-T");
        Dungeon.level.drop(Generator.random(Generator.Category.STONE),pos).sprite.drop();
        Buff.affect(hero, SakaFishSketon.CoolDownStoneRecharge.class, SakaFishSketon.CoolDownStoneRecharge.DURATION);
    }

    @Override
    protected boolean act() {
        if (lastHeroPos == -1) {
            lastHeroPos = hero.pos;
        } else if (hero.pos != lastHeroPos) {
            lastHeroPos = hero.pos;
            idleTurns = 0;
        } else if (hero.paralysed != 0) {
            //麻痹回合不计入挂机
        } else if (hero.actedThisTurn || hero.curAction != null) {
            idleTurns = 0;
        } else {
            idleTurns++;
            if (idleTurns >= IDLE_DEATH_TURNS) {
                die(null);
                return true;
            }
        }
        return super.act();
    }

    private static final String IDLE_TURNS = "idle_turns";
    private static final String LAST_HERO_POS = "last_hero_pos";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(IDLE_TURNS, idleTurns);
        bundle.put(LAST_HERO_POS, lastHeroPos);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        idleTurns = bundle.getInt(IDLE_TURNS);
        lastHeroPos = bundle.getInt(LAST_HERO_POS);
    }

    private class Wandering extends Mob.Wandering {

        @Override
        public boolean act( boolean enemyInFOV, boolean justAlerted ) {
            if ( enemyInFOV ) {

                enemySeen = true;

                notice();
                alerted = true;
                state = HUNTING;
                target = enemy.pos;

            } else {

                enemySeen = false;

                int oldPos = pos;
                target = hero.pos;

                //always move towards the hero when wandering
                if (getCloser( target )) {
                    spend( 1 / speed() );
                    return moveSprite( oldPos, pos );
                } else {
                    spend( TICK );
                }
            }

            if(Random.Int(10)==1){
                sprite.showStatus(0xFCE9CC, TXT_RANDOM[Random.Int(TXT_RANDOM.length)]);
            }

            return true;
        }

    }

}
