package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.hollow.minigame;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.PaswordBadges;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicalSight;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.status.ScoreBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.hollow.PacManQuest;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MiniGhostSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.List;

public class Ghost_Pink extends GhostTemplate {

    {
        spriteClass = MiniGhostSprite.PinkSadlyGhost.class;
        HT = HP = 10;
    }

    public int AI_state;
    private static final String RecordOtherGhost_STATE   = "RECORDOTHERGHOST_STATE";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(RecordOtherGhost_STATE, AI_state);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        AI_state = bundle.getInt(RecordOtherGhost_STATE);
    }

    @Override
    public boolean isAlive() {
        return true;
    }

    @Override
    protected boolean canAttack(Char enemy) {
        if (enemy != null) {
            if (enemy.buff(PacManQuest.AntiAttack.class) != null) {
                return false;
            }
        }
        return Dungeon.level.adjacent(pos, enemy.pos);
    }

    @Override
    protected boolean getCloser(int target) {
        if (state == HUNTING) {
            if (enemy != null && enemy.buff(PacManQuest.AntiAttack.class) != null) {
                return getFurther(target);
            } else if (enemy != null && enemy.buff(PacManQuest.AntiAttack.class) == null) {
                return super.getCloser(target);
            }
        }
        return super.getCloser(target);
    }

    public static class RecordOtherGhost extends FlavourBuff { }

    @Override
    protected boolean act() {
        AiState lastState = state;

        for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
            if(mob.alignment != Alignment.ENEMY) {
                mob.damage(mob.HP, this,DamageType.MAGIC);
            }
        }

        if(HP < 1){
            ScrollOfTeleportation.appear(this, 274);
            Buff.affect(this, Paralysis.class, Paralysis.DURATION);
            HP = 10;
            PacManQuest.AntiAttack buff = hero.buff(PacManQuest.AntiAttack.class);
            if(buff != null){
                int powerOfTwo = 1 << buff.Plus;
                PaswordBadges.loadGlobal();
                List<PaswordBadges.Badge> passwordbadges = PaswordBadges.filtered(true);
                if(200 * powerOfTwo > 800 && !(passwordbadges.contains(PaswordBadges.Badge.GHOST_HUNTER))){
                    PaswordBadges.GHOST_HUNTER();
                } else if(200 * powerOfTwo <= 1600) {
                    hero.sprite.showStatus(Window.Pink_COLOR, "+"+200 * powerOfTwo);
                    PacManQuest.GetScore(hero, 200 * powerOfTwo);
                    buff.Plus++;
                }
            }
        }
        if(lastState == SLEEPING || (buff(Paralysis.class)==null && hero.buff(PacManQuest.AntiAttack.class)==null &&
                (pos == 255 || pos == 256 || pos == 257 || pos == 274 || pos == 275 || pos == 276))) {
            ScrollOfTeleportation.appear(this, 215);
            Buff.affect(hero, MagicalSight.class, MagicalSight.DURATION*200);
            state = HUNTING;
        }

        if(buff(RecordOtherGhost.class) == null){
            Buff.prolong(this, RecordOtherGhost.class,40f);
            AI_state = Random.Int(3);
        }

        for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])){
            switch (AI_state){
                case 1:
                    if(mob instanceof Ghost_Junko){
                        beckon(mob.pos);
                    }
                    break;
                case 2:
                    if(mob instanceof Ghost_Pink){
                        beckon(mob.pos);
                    }
                    break;
                default:
                    if(mob instanceof Ghost_Anger){
                        beckon(mob.pos);
                    }
                    break;
            }
        }
        return super.act();
    }

    @Override
    public int attackProc(Char enemy, int damage) {
        if(enemy == hero) {
            if (hero.buff(ScoreBuff.class) != null) {
                ScoreBuff buff = hero.buff(ScoreBuff.class);
                buff.downScore(200);
                hero.sprite.showStatus(Window.R_COLOR, "-" + 200);
                ScrollOfTeleportation.appear(this, 274);
                ScrollOfTeleportation.appear(hero, 389);
                Buff.affect(hero, Invisibility.class, 3f);
            }
        } else {
            enemy.damage(10000,this,DamageType.MAGIC);
        }
        return super.attackProc(enemy, damage);
    }

    @Override
    public boolean isInvulnerable(Class effect) {
        if (effect == PacManQuest.SugarBomb.class) {
            return false;
        }
        return hero.buff(PacManQuest.AntiAttack.class) == null || super.isInvulnerable(effect);
    }

    @Override
    public int attackSkill( Char target ) {
        return Integer.MAX_VALUE;
    }

    @Override
    public int drRoll() {
        return 0;
    }

}

