package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.tumulus;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Boss;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.DriedRose;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.RogerSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;

public class Roger extends Boss {

    public static int damageREF = 5 + Statistics.spawnersTombTownAlive;

    {
        initStatus(30);
        initProperty();
        spriteClass = RogerSprite.class;
        initBaseStatus(15, 30, 30, 15, 400, 0, 10);
    }

    @Override
    protected boolean act() {
        if(state != SLEEPING){
            damage_reflection();
        }
        return super.act();
    }

    /**
     * 不屈技能
     */
    public void damage_reflection(){
        if(buff(DamageREFCD.class) == null){
            Buff.affect(this,DamageREFCD.class,28f);
            Buff.affect(this,DamageREF.class,8f);
            sprite.centerEmitter().start( Speck.factory( Speck.SCREAM ), 0.3f, 3 );
            Sample.INSTANCE.play( Assets.Sounds.CHALLENGE );
        }
    }

    /**
     * 净化诅咒
     */
    public void cleanseCursed(){
        Sample.INSTANCE.play( Assets.Sounds.GHOST );
        damage((int) (HT*0.05f),this,DamageType.REAL);
        Buff.affect(this, Paralysis.class,8f);
    }

    public static class DamageREFCD extends FlavourBuff{}

    public static class DamageREF extends FlavourBuff{
        @Override
        public void tintIcon(Image icon) {
            icon.hardlight(0xA1887F);
        }

        @Override
        public int icon() {
            return BuffIndicator.ARMOR;
        }

        @Override
        public String desc() {
            return Messages.get(this,"desc",damageREF,(int)visualcooldown());
        }
    }

    @Override
    public float attackDelay() {
        if(enemy != null){
            return enemy.buff(BreakDamage.class) != null ? 0.5f : super.attackDelay();
        } else {
            return super.attackDelay();
        }
    }

    @Override
    public int attackProc(Char enemy, int damage) {
        if (enemy != null && enemy == Dungeon.hero) {
            int X = Statistics.spawnersTombTownAlive;
            float ratio = (float) damage / enemy.HT;
            float rawTurns = (50f + 5f * X) * ratio;
            int duration = (int) Math.ceil(rawTurns);
            if (duration < 1) duration = 1;
            Buff.affect(enemy, BreakDamage.class).set(duration, 1);
        }
        return super.attackProc(enemy, damage);
    }


    @Override
    public void damage(int dmg, Object src, DamageType type) {

        if(buff(DamageREF.class) != null){
            dmg -= damageREF;
        }

        super.damage(dmg, src, type);
    }

    public static class BreakDamage extends Buff {

        {
            type = buffType.POSITIVE;
        }

        public int level = 0;
        private int interval = 1;

        @Override
        public boolean act() {
            if (target.isAlive()) {

                spend( interval );
                if (--level <= 0) {
                    detach();
                }

            } else {

                detach();

            }

            return true;
        }

        public int level() {
            return level;
        }

        public void set( int value, int time ) {
            if (level <= value) {
                level = value;
                interval = time;
                spend(time - cooldown() - 1);
            }
        }

        @Override
        public int icon() {
            return BuffIndicator.BREAK_DMG;
        }

        @Override
        public float iconFadePercent() {
            if (target instanceof Hero){
                float max = ((Hero) target).lvl;
                return Math.max(0, (max-level)/max);
            }
            return 0;
        }

        @Override
        public String iconTextDisplay() {
            return Integer.toString(level);
        }

        @Override
        public String desc() {
            return Messages.get(this, "desc", level, dispTurns(visualcooldown()));
        }

        private static final String LEVEL	    = "level";
        private static final String INTERVAL    = "interval";

        @Override
        public void storeInBundle( Bundle bundle ) {
            super.storeInBundle( bundle );
            bundle.put( INTERVAL, interval );
            bundle.put( LEVEL, level );
        }

        @Override
        public void restoreFromBundle( Bundle bundle ) {
            super.restoreFromBundle( bundle );
            interval = bundle.getInt( INTERVAL );
            level = bundle.getInt( LEVEL );
        }
    }

    @Override
    public void notice() {
        super.notice();
        if (!BossHealthBar.isAssigned()) {
            BossHealthBar.assignBoss(this);
            Dungeon.level.seal();
            Camera.main.shake(1f,3f);
            GameScene.bossReady();
            yell(Messages.get(this, "notice"));
            for (Char ch : Actor.chars()){
                if (ch instanceof DriedRose.GhostHero){
                    ((DriedRose.GhostHero) ch).sayBoss();
                }
            }
        }
    }

}
