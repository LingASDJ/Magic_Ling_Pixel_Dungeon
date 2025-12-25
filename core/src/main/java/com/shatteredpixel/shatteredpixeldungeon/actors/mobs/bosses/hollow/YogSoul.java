package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.hollow;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Boss;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ElementalBuff.BaseBuff.ScaryBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ElementalBuff.DamageBuff.ScaryDamageBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ElementalBuff.Immunities.ScaryImmunitiesBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LockedFloor;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.DM100;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.sprites.YogSoulSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.noosa.Image;
import com.watabou.utils.Random;

public class YogSoul extends Boss {

    {
        initProperty();
        initBaseStatus(0, 0, 40, 0, 1500, 10, 25);
        initStatus(20);
        spriteClass = YogSoulSprite.class;

        properties.add(Property.BOSS);
        properties.add(Property.ACIDIC);
        properties.add(Property.INORGANIC);
        properties.add(Property.ELECTRIC);

        state = WANDERING = new Waiting();

        noDropIceCoin = true;
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange( 10, 25 );
    }
    @Override
    public int attackSkill( Char target ) {
        return 40;
    }


    @Override
    public boolean reset() {
        return true;
    }

    @Override
    protected boolean getCloser(int target) {
        return false;
    }

    @Override
    protected boolean getFurther(int target) {
        return false;
    }

    private class Waiting extends Mob.Wandering{

        @Override
        protected boolean noticeEnemy() {
            spend(TICK);
            return super.noticeEnemy();
        }
    }

    @Override
    public int attackProc(Char enemy, int damage) {
        damage = super.attackProc(enemy, 0);
        enemy.damage( damageRoll(), new DM100.LightningBolt() );
        return damage;
    }

    @Override
    protected boolean act() {

        if(Dungeon.level.distance(pos, hero.pos) >= 2){

            if (buff(FriendBuffGet.class) == null) {
                Buff.affect(this, FriendBuffGet.class, 5f);
                for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])){
                    if (mob instanceof ShubNiggurath) {
                        mob.HT += 200;
                        mob.HP += Math.min( (int) (mob.HT * 0.05f), mob.HT - mob.HP);
                    }
                    if (mob instanceof Nyarlathotep) {
                        mob.HT += 100;
                        mob.HP += Math.min( (int) (mob.HT * 0.05f), mob.HT - mob.HP);
                    }
                }
                HT += 100;
                HP += Math.min( (int) (HT * 0.05f), HT - HP);
            }
            
            if(buff(ReHealHP.class)==null){
                Buff.affect(this, ReHealHP.class, 15f);
                int heartDamage = (int) (HT * Random.NormalFloat(0.1f, 0.2f));
                HP += Math.min(heartDamage,HT - HP);
            }

            if (buff(DeadHeartMagic.class) == null) {
                Buff.affect(this, DeadHeartMagic.class, 10f);
                Buff.affect(this, AttackDamageMagic.class, 5f);
                for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])){
                    if(mob.buff(AttackDamageMagic.class)== null){
                        if (mob instanceof ShubNiggurath || mob instanceof Nyarlathotep) {
                            Buff.affect(mob, AttackDamageMagic.class, 5f);
                        }
                    }
                }
            }

            if(buff(AttackDamageMagic.class)!=null && Dungeon.level.distance(pos, hero.pos) <= 7){
                if (enemy != null && enemy == hero && enemySeen) {
                    boolean isNyzAlive = false;
                    for (Mob mob : Dungeon.level.mobs) {
                        if (mob instanceof Nyarlathotep) {
                            isNyzAlive = true;
                            break;
                        }
                    }
                    boolean hasScaryBuff = false;
                    for (Buff buff : enemy.buffs()) {
                        if(buff instanceof ScaryDamageBuff || buff instanceof ScaryImmunitiesBuff){
                            if (isNyzAlive) {
                                int heartDamage = (int) (8 * Random.NormalFloat(0.5f, 1));
                                enemy.damage(heartDamage, new DM100.LightningBolt());
                                for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])){
                                    mob.HP += Math.min(heartDamage, mob.HT - mob.HP);
                                }
                            }
                        } else if (buff instanceof ScaryBuff) {
                            hasScaryBuff = true;
                            if (isNyzAlive) {
                                int heartDamage = (int) (8 * Random.NormalFloat(0.5f, 1));
                                enemy.damage(heartDamage, new DM100.LightningBolt());
                                for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])){
                                    mob.HP += Math.min(heartDamage, mob.HT - mob.HP);
                                }
                            }
                            ((ScaryBuff) buff).damgeScary(4 * (isNyzAlive ? 2 : 1));
                        }
                    }

                    if (!hasScaryBuff) {
                        Buff.affect(enemy, ScaryBuff.class).set(100, 5);
                    }
                }
            }
        }


        return super.act();
    }

    public static class FriendBuffGet extends FlavourBuff {
        {
            type = buffType.POSITIVE;
        }
    }

    public static class DeadHeartMagic extends FlavourBuff {
        {
            type = buffType.POSITIVE;
        }
    }

    public static class ReHealHP extends FlavourBuff {
        {
            type = buffType.POSITIVE;
        }
    }

    public static class AttackDamageMagic extends FlavourBuff {


        @Override
        public void tintIcon(Image icon) {
            icon.hardlight(Window.GDX_COLOR);
        }

        @Override
        public int icon() {
            return BuffIndicator.LANTERFIRE_FIVE;
        }

        @Override
        public void fx(boolean on) {
            if (on) target.sprite.aura(Window.ANSDO_COLOR);
            else target.sprite.clearAura();
        }
    }

    @Override
    public void damage(int dmg, Object src, DamageType type) {
        BossHealthBar.assignBoss(this);
        LockedFloor lock = hero.buff(LockedFloor.class);
        if (lock != null) {
            int multiple = 1;
            lock.addTime(dmg*multiple);
        }
        super.damage(dmg, src, type);
    }

}
