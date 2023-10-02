package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bleeding;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.HalomethaneBurning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ShieldBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Sleep;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.Chasm;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.AlarmTrap;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.BruteBotSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class BruteBot extends Mob implements Callback,Hero.Doom {
    protected float focusCooldown = 0;
    public int count = Random.NormalIntRange(1,2);
    protected boolean hasRaged = false;
    {
        spriteClass = BruteBotSprite.class;

        HP = HT = 120;
        defenseSkill = 12;

        baseSpeed=0.75f;

        EXP = 15;
        maxLvl = 24;

        loot = Generator.Category.WEAPON;
        lootChance = 0.25f;
        properties.add(Property.NOBIG);
        properties.add(Property.MINIBOSS);
    }

    protected void triggerEnrage(){
        Buff.affect(this, BruteBot.BruteBotRage.class).setShield(HT/2 + 40);
        if (Dungeon.level.heroFOV[pos]) {
            sprite.showStatus( CharSprite.NEGATIVE, Messages.get(this, "enraged") );
            AlarmTrap trap = new AlarmTrap();
            trap.pos = super.pos;
            trap.activate();
            GLog.n(Messages.get(this, "died"));
        }
        spend( TICK );
        hasRaged = true;
    }

    @Override
    protected boolean act() {
        boolean result = super.act();
        if (buff(BruteBot.Focus.class) == null && state == HUNTING && focusCooldown <= 0) {
            Buff.affect( this, BruteBot.Focus.class );
        }
        return result;
    }

    @Override
    public synchronized boolean isAlive() {
        if (super.isAlive()){
            return true;
        } else {
            if (!hasRaged){
                triggerEnrage();
            }
            return !buffs(BruteBotRage.class).isEmpty();
        }
    }

    @Override
    public int damageRoll() {
        return buff(BruteBot.BruteBotRage.class) != null ?
                Random.NormalIntRange( 20, 50 ) :
                Random.NormalIntRange( 15, 20 );
    }


    @Override
    public int attackSkill( Char target ) {
        return 16;
    }

    @Override
    public int defenseSkill( Char enemy ) {
        if (buff(BruteBot.Focus.class) != null && paralysed == 0 && state != SLEEPING){
            return INFINITE_EVASION;
        }
        return super.defenseSkill( enemy );
    }

    @Override
    protected void spend( float time ) {
        focusCooldown -= time;
        super.spend( time );
    }

    @Override
    public void move( int step, boolean travelling) {
        // moving reduces cooldown by an additional 0.67, giving a total reduction of 1.67f.
        // basically monks will become focused notably faster if you kite them.
        if (travelling) focusCooldown -= 0.67f;
        super.move( step, travelling);
    }

    @Override
    public String defenseVerb() {
        BruteBot.Focus f = buff(BruteBot.Focus.class);
        if (f == null) {
            return super.defenseVerb();
        } else {
            f.detach();
            if (sprite != null && sprite.visible) {
                Sample.INSTANCE.play(Assets.Sounds.HIT_PARRY, 1, Random.Float(0.96f, 1.05f));
            }
            focusCooldown = Random.NormalFloat( 6, 7 );
            return Messages.get(this, "parried");
        }
    }

    @Override
    protected boolean canAttack( Char enemy ) {
        return new Ballistica( pos, enemy.pos, Ballistica.MAGIC_BOLT).collisionPos == enemy.pos;
    }

    @Override
    public void aggro(Char ch) {
        //cannot be aggroed to something it can't see
        if (ch == null || fieldOfView == null || fieldOfView[ch.pos]) {
            super.aggro(ch);
        }
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(8, 12);
    }

    @Override
    public int attackProc(Char enemy, int damage) {

        switch (count) {
            case 1:
                if(Random.Int(16) == 0){
                    Buff.prolong(enemy, Cripple.class, Cripple.DURATION);
                }

                break;
            case 2:
                if(Random.Int(16) == 0) {
                    Buff.affect(enemy, Bleeding.class).set(5f);
                }
                break;
            default:
                super.attackProc(enemy, damage);
                break;
        }
        return damage;
    }

    @Override
    protected boolean doAttack( Char enemy ) {

        if (Dungeon.level.distance( pos, enemy.pos ) <= 1) {


            return super.doAttack( enemy );

        } else {
            spend( buff(BruteBot.BruteBotRage.class) != null? 1f : 3f );
            if (sprite != null && (sprite.visible || enemy.sprite.visible)) {
                sprite.attack( enemy.pos );
                return false;
            } else {
                return true;
            }
        }
    }

    @Override
    public void call() {
        next();
    }

    @Override
    public void die(Object cause) {
        super.die(cause);

        if (cause == Chasm.class){
            hasRaged = true; //don't let enrage trigger for chasm deaths
        }

        if(Random.Float()<0.25f){
            Dungeon.level.drop(Generator.randomArmor(), pos ).sprite.drop();
        }

        Dungeon.level.drop( new Gold( Random.IntRange( 80, 120 ) ), pos ).sprite.drop();;
    }

    public static class BruteBotRage extends ShieldBuff {

        {
            type = buffType.POSITIVE;
        }

        @Override
        public boolean act() {

            if (target.HP > 0){
                detach();
                return true;
            }

            absorbDamage( 2);

            if (shielding() <= 0){
                target.die(null);
            }

            spend( TICK );

            return true;
        }

        @Override
        public int icon () {
            return BuffIndicator.FURY;
        }

        @Override
        public String toString () {
            return Messages.get(this, "name");
        }

        @Override
        public String desc () {
            return Messages.get(this, "desc", shielding());
        }

        {
            immunities.add(Terror.class);
        }
    }

    public static class Focus extends Buff {

        {
            type = buffType.POSITIVE;
            announced = true;
        }

        @Override
        public int icon() {
            return BuffIndicator.MIND_VISION;
        }

        @Override
        public void tintIcon(Image icon) {
            icon.hardlight(0x00ff0000);
        }

        @Override
        public String toString() {
            return Messages.get(this, "name");
        }

        @Override
        public String desc() {
            return Messages.get(this, "desc");
        }
    }

    private static final String NUM_CONTS = "numConts";
    private static final String HAS_RAGED = "has_raged";

    private static String FOCUS_COOLDOWN = "focus_cooldown";
    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(NUM_CONTS, count);
        bundle.put(HAS_RAGED, hasRaged);
        bundle.put( FOCUS_COOLDOWN, focusCooldown );
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        count = bundle.getInt(NUM_CONTS);
        hasRaged = bundle.getBoolean(HAS_RAGED);
        focusCooldown = bundle.getInt( FOCUS_COOLDOWN );
    }

    @Override
    public void onDeath() {

        if( hasRaged ) {
            Badges.BRUTE_DIED();
        }

        Dungeon.fail( getClass() );
        GLog.n( Messages.get(this, "ondeath") );
    }


    {
        immunities.add( Sleep.class );
        immunities.add( HalomethaneBurning.class );
        immunities.add( Burning.class );
        immunities.add( ToxicGas.class );
    }

}
