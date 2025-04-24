package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Fire;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.StormCloud;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LockedFloor;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.DM100;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.Beam;
import com.shatteredpixel.shatteredpixeldungeon.effects.FloatingText;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MissileSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.QliphothLasherSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class QliphothLasher extends Mob implements Hero.Doom {

    public int state_lasher_boss;

    public int state_Phase;

    {
        spriteClass = QliphothLasherSprite.class;

        HP = HT = 40;
        defenseSkill = 0;

        EXP = 1;

        loot = Generator.Category.SEED;
        lootChance = 0.65f;

        HUNTING = new Hunting();

        properties.add(Property.ACIDIC);
        properties.add(Property.IMMOVABLE);
        properties.add(Property.MINIBOSS);
    }

    {
        immunities.add( ToxicGas.class );
    }

    @Override
    public void die( Object cause ) {
            super.die(cause);
        if(state_lasher_boss >= 1){
            GameScene.add(Blob.seed(pos, 16, StormCloud.class));
        }

        Mob[] mobsx = Dungeon.level.mobs.toArray(new Mob[0]);
        for(Mob m : mobsx){
            if(m != null && m instanceof Qliphoth){
                Qliphoth q = (Qliphoth) m;
                q.amount++;
            }
        }
        /*
            // 获取当前Dungeon.level上的Mob数组
            Mob[] mobsx = Dungeon.level.mobs.toArray(new Mob[0]);
            ArrayList<Mob> enemys = new ArrayList<>();
            for(Mob mob : mobsx){
                if(mob instanceof Qliphoth) enemys.add(mob);
                if(mob.alignment == Alignment.ENEMY) enemys.add(mob);
            }

            if (!enemys.isEmpty()) {
                boolean containsBoss = false;
                boolean onlyCRWithBoss = true;
                for (Mob mob : enemys) {
                    if (mob instanceof Qliphoth) {
                        containsBoss = true;
                    } else if (!(mob instanceof ClearElemental)) {
                        onlyCRWithBoss = false;
                    }
                }
                // 如果场景中包含Boss并且除了Boss外只含有CR怪物
                if (containsBoss && onlyCRWithBoss) {
                    for (Mob mob : enemys) {
                        if (mob instanceof ClearElemental) {
                            mob.die(true ); // CR怪物死亡
                        } else if (mob instanceof Qliphoth) {
                            if((mob.HP>=60 && mob.HP<=100)){
                                mob.HP = 60;
                            }
                        }
                    }
                }
           }
         */
    }


    @Override
    public void damage(int dmg, Object src) {
        LockedFloor lock = Dungeon.hero.buff(LockedFloor.class);
        if (lock != null) lock.addTime(dmg*2+1);
        super.damage(dmg, src);
    }


    @Override
    protected boolean act() {
        //Regen HP...
        if (HP < HT) {
            sprite.showStatusWithIcon(CharSprite.POSITIVE, Integer.toString(Math.min(2, HT - HP)), FloatingText.HEALING);
            HP = Math.min(HT, HP + 2);
        }

        //Boss level 2
        if(state_lasher_boss == 1){
            for (int i : PathFinder.NEIGHBOURS8) {
                GameScene.add(Blob.seed(pos + i, 6, Fire.class));
            }
            immunities.add( Fire.class );
        } else {
            //Boss level 1
            GameScene.add(Blob.seed(pos, 20, ToxicGas.class));
        }

        onZapComplete();
        spend(Dungeon.isChallenged(Challenges.STRONGER_BOSSES) ? 4f : 3f);

        return super.act();
    }

    public void onZapComplete() {
        if (Dungeon.level.distance(pos, hero.pos) <= 4 && Dungeon.level.heroFOV[pos] && state != SLEEPING && hero.invisible != 1) {
            Hero enemy = hero;
            sprite.zap(enemy.pos);
            if (sprite.visible || enemy.sprite.visible) {
                ((MissileSprite)sprite.parent.recycle( MissileSprite.class )).
                        reset(sprite, enemy.pos, new Forest_Kill(), new Callback() {
                            @Override
                            public void call() {
                                if(Dungeon.isChallenged(Challenges.STRONGER_BOSSES)){
                                    Sample.INSTANCE.play( Assets.Sounds.ZAP );
                                    enemy.damage(damageRoll(), new DM100.LightningBolt());
                                } else {
                                    onAttackComplete();
                                }
                                next();
                            }
                        }
                );
            }
        } else if (enemy != null && enemy != hero) {
            if (Dungeon.level.distance(pos, enemy.pos) <= 4) {
                if (sprite.visible || enemy.sprite.visible) {
                    sprite.parent.add(new Beam.GlassRayS(sprite.center(), enemy.sprite.center()));
                    Sample.INSTANCE.play( Assets.Sounds.ZAP );
                }

                int dmg = Random.NormalIntRange(5, 12);
                enemy.damage(dmg, this);
                next();
            }
        }
    }

    @Override
    public int attackProc(Char enemy, int damage) {
        damage = super.attackProc( enemy, damage );
        Buff.affect( enemy, Cripple.class, 2f );
        return super.attackProc(enemy, damage);
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

    @Override
    public int damageRoll() {
        return state_lasher_boss >=1 ? Random.NormalIntRange(4, 10) : Random.NormalIntRange(2, 5);
    }

    @Override
    public int attackSkill( Char target ) {
        return 15;
    }

    @Override
    public int drRoll() {
        return super.drRoll() + (Dungeon.isChallenged(Challenges.STRONGER_BOSSES) ? Random.NormalIntRange(2,5) : Random.NormalIntRange(0, 2));
    }

    private static final String STATE_LASHER_BOSS   = "state_lasher_boss";
    private static final String STATE_PHASE         = "state_Phase";

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        state_lasher_boss = bundle.getInt(STATE_LASHER_BOSS);
        state_Phase = bundle.getInt(STATE_PHASE);
        if(state_lasher_boss >= 1){
            immunities.add( Fire.class );
        }
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(STATE_LASHER_BOSS, state_lasher_boss);
        bundle.put(STATE_PHASE, state_Phase);
    }

    @Override
    public void onDeath() {
        Dungeon.fail( getClass() );
    }

    private class Hunting extends Mob.Hunting{
        @Override
        public boolean act( boolean enemyInFOV, boolean justAlerted ) {
            return true;
        }
    }

    public static class Forest_Kill extends MissileWeapon {

        {
            image = ItemSpriteSheet.GLASS_CI;
            hitSound = Assets.Sounds.HIT_STAB;
            hitSoundPitch = 1.2f;

            bones = false;

            tier = 1;
        }

    }

}
