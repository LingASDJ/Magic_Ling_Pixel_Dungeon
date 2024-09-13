package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ArtifactRecharge;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Degrade;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.TestBatLock;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.BlueBatSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class BloodBat extends Mob implements Callback {

    {
        HP = HT = 20;
        defenseSkill = 7;
        baseSpeed = 2.5f;
        spriteClass = BlueBatSprite.class;
        alignment = Alignment.ALLY;
        WANDERING = new Wandering();
        intelligentAlly = true;
        maxLvl = 0;
        flying = true;
    }

    protected int defendingPos = -1;
    protected boolean movingToDefendPos = false;

    public void defendPos( int cell ){
        defendingPos = cell;
        movingToDefendPos = true;
        aggro(null);
        state = WANDERING;
    }

    public void clearDefensingPos(){
        defendingPos = -1;
        movingToDefendPos = false;
    }

    private static final String DEFEND_POS = "defend_pos_pets";
    private static final String MOVING_TO_DEFEND = "moving_to_defend_pets";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(DEFEND_POS, defendingPos);
        bundle.put(MOVING_TO_DEFEND, movingToDefendPos);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        if (bundle.contains(DEFEND_POS)) defendingPos = bundle.getInt(DEFEND_POS);
        movingToDefendPos = bundle.getBoolean(MOVING_TO_DEFEND);
    }

    @Override
    public boolean act() {
        if(HT>850) die(true);

        return super.act();
    }

    public void onZapComplete() {
        zap();
        next();
    }

    @Override
    protected boolean canAttack( Char enemy ) {
        if(Dungeon.hero.lvl >= 15) {
            return new Ballistica(pos, enemy.pos, MagicMissile.WARD).collisionPos == enemy.pos;
        } else {
            return super.canAttack(enemy);
        }
    }

    @Override
    public void call() {
        next();
    }

    public static class DarkBolt{}
    public static int level = 1;
    private static final float TIME_TO_ZAP	= 2f;
    private void zap() {
        spend( TIME_TO_ZAP );

        if (hit( this, enemy, true )) {
            //TODO would be nice for this to work on ghost/statues too
            if (enemy == Dungeon.hero && Random.Int( 2 ) == 0) {
                Buff.prolong( enemy, Blindness.class, Degrade.DURATION );
                Sample.INSTANCE.play( Assets.Sounds.DEBUFF );
            }

            int dmg = Random.NormalIntRange( (2*Dungeon.depth/5), 4*Dungeon.depth/5 );
            enemy.damage( dmg, new BloodBat.DarkBolt() );

            if (enemy == Dungeon.hero && !enemy.isAlive()) {
                Dungeon.fail( getClass() );
                GLog.n( Messages.get(this, "frost_kill") );
            }
        } else {
            enemy.sprite.showStatus( CharSprite.NEUTRAL,  enemy.defenseVerb() );
        }
    }

    public static void saveLevel(Bundle bundle){
        bundle.put("BloodBatLevel", level);
    }

    public static void loadLevel(Bundle bundle){
        level = bundle.getInt("BloodBatLevel");
    }

    @Override
    public int damageRoll() {
        return Char.combatRoll( (2*Dungeon.depth/5)+3, 4*Dungeon.depth/5 );
    }

    @Override
    public float attackDelay() {
        return super.attackDelay() * (Dungeon.hero.isSubclass(HeroSubClass.ASSASSIN) ? 0.33f : 0.5f);
    }

    @Override
    public int attackSkill(Char targetd) {
        return 4 + level * 2;
    }


    @Override
    public int attackProc(Char enemy, int damage) {
        if(Dungeon.hero.lvl == 15) {
            zap();
        }
        return super.attackProc(enemy, damage);

    }

    @Override
    protected Char chooseEnemy() {
        Char enemy = super.chooseEnemy();

        int targetPos = Dungeon.hero.pos;
        int distance = Dungeon.hero.isSubclass(HeroSubClass.ASSASSIN) ? 99999 : 8;

        //will never attack something far from their target
        if (enemy != null
                && Dungeon.level.mobs.contains(enemy)
                && (Dungeon.level.distance(enemy.pos, targetPos) <= distance)){
            ((Mob)enemy).aggro(this);
            return enemy;
        }

        return null;
    }

    public static void updateHP(){
        level += 1;
        if (Dungeon.level != null) {
            for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
                if (mob instanceof BloodBat) {
                    mob.HP = mob.HT = 14 + level * 2;
                    ((BloodBat) mob).defenseSkill = 3 + level * 4;
                }
            }
        }
    }

    @Override
    public void damage(int dmg, Object src) {
        super.damage(dmg, src);
        if(Random.Int(10)==5 && hero.lvl>=10){
            Buff.affect(Dungeon.hero, ArtifactRecharge.class).prolong(dmg/2f*2);
        }

    }

    @Override
    public void die(Object cause) {
        super.die(cause);
        if(Dungeon.hero.lvl >= 25) {
            Buff.affect(Dungeon.hero, BloodBatRecharge.class, 300f);
        } else if(Dungeon.hero.lvl >= 20) {
            Buff.affect(Dungeon.hero, BloodBatRecharge.class, 550f);
        } else if(Dungeon.hero.lvl >= 15) {
            Buff.affect(Dungeon.hero, BloodBatRecharge.class, 600f);
        } else if(Dungeon.hero.lvl >= 10) {
            Buff.affect(Dungeon.hero, BloodBatRecharge.class, 700f);
        } else {
            Buff.affect(Dungeon.hero, BloodBatRecharge.class, 800f);
        }
        for (Buff buff : hero.buffs()) {
            if (buff instanceof TestBatLock) {
                buff.detach();
            }
        }
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
                target = Dungeon.hero.pos;
                //always move towards the hero when wandering
                if (getCloser( target )) {
                    spend( 1 / speed() );
                    return moveSprite( oldPos, pos );
                } else {
                    spend( TICK );
                }

            }
            return true;
        }

    }

    protected boolean doAttack( Char enemy ) {

        if (Dungeon.level.adjacent( pos, enemy.pos )) {

            return super.doAttack( enemy );

        } else {

            if (sprite != null && (sprite.visible || enemy.sprite.visible)) {
                sprite.zap( enemy.pos );
                return false;
            } else {
                zap();
                return true;
            }
        }
    }

    public static class BloodBatRecharge extends FlavourBuff {

        public static final float DURATION = 800f;

        {
            type = buffType.NEGATIVE;
            announced = true;
        }

        @Override
        public boolean act() {

            detach();
            return true;
        }

        @Override
        public int icon() {
            return BuffIndicator.TIME;
        }

        @Override
        public void tintIcon(Image icon) {
            icon.hardlight(Window.Pink_COLOR);
        }

        @Override
        public float iconFadePercent() {
            return Math.max(0, (DURATION - visualcooldown()) / DURATION);
        }

        @Override
        public String toString() {
            return Messages.get(this, "name");
        }

        @Override
        public String desc() {
            return Messages.get(this, "desc", dispTurns());
        }

    }
}
