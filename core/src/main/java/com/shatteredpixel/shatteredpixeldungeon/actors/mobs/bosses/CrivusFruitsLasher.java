package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.HalomethaneBurning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LockedFloor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Poison;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Bomb;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.Potion;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.levels.ForestBossLevel;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.RotLasherSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

public class CrivusFruitsLasher extends Mob {

    {
        spriteClass = RotLasherSprite.class;

        HP = HT = Statistics.crivusfruitslevel2 ? 30 : 15;
        defenseSkill = 0;

        EXP = 1;

        state = WANDERING = new Waiting();

        properties.add(Property.IMMOVABLE);
        properties.add(Property.MINIBOSS);
    }

    @Override
    protected boolean canAttack( Char enemy ) {
        if(Dungeon.isChallenged(Challenges.STRONGER_BOSSES) && Statistics.crivusfruitslevel2){
            return new Ballistica( pos, enemy.pos, Ballistica.MAGIC_BOLT).collisionPos == enemy.pos;
        }
        return super.canAttack(enemy);
    }

    protected boolean doAttack( Char enemy ) {

        if (Dungeon.level.adjacent( pos, enemy.pos )) {

            return super.doAttack( enemy );

        } else {

            if (sprite != null && (sprite.visible || enemy.sprite.visible) && Dungeon.isChallenged(Challenges.STRONGER_BOSSES)) {
                sprite.zap( enemy.pos );
                return false;
            } else {
                zap();
                return true;
            }
        }
    }



    private void zap() {
        spend( 3f );

        if (hit( this, enemy, true )) {
            //TODO would be nice for this to work on ghost/statues too
            if (enemy == Dungeon.hero && Random.Int( 2 ) == 0) {
                GameScene.add(Blob.seed(enemy.pos, 45, ToxicGas.class));
                Sample.INSTANCE.play( Assets.Sounds.DEBUFF );
            }

            enemy.damage( damageRoll(), enemy );

            if (enemy == Dungeon.hero && !enemy.isAlive()) {
                Dungeon.fail( getClass() );
                GLog.n( Messages.get(this, "frost_kill") );
            }
        } else {
            enemy.sprite.showStatus( CharSprite.NEUTRAL,  enemy.defenseVerb() );
        }
    }

    public void onZapComplete() {
        zap();
        next();
    }

    @Override
    protected boolean act() {
        //毒雾 都得死 啊哈哈哈哈
        GameScene.add(Blob.seed(pos, Statistics.crivusfruitslevel2 ? 60 : 10, ToxicGas.class));

        //无敌也要扣减本体 NND
        if(hero.buff(CrivusFruits.DiedDamager.class) == null){
            Buff.affect(this,CrivusFruits.DiedDamager.class);
        }

        //二阶段开始瞬移，地狱绘图
        if(Statistics.crivusfruitslevel2){
            if(Random.Float() < 0.4f && (enemy == null || !Dungeon.level.adjacent(pos, enemy.pos))){
               switch (Random.Int(14)){
                   case 0: default:
                      if(Dungeon.level.avoid[pos] || !Dungeon.level.passable[pos]
                              || Actor.findChar(pos) != null){
                          ScrollOfTeleportation.appear(this, ForestBossLevel.WIDTH * 17 + 11);
                      }
                    break;
                   case 1:
                       if(Dungeon.level.avoid[pos] || !Dungeon.level.passable[pos]
                               || Actor.findChar(pos) != null) {
                           ScrollOfTeleportation.appear(this, ForestBossLevel.WIDTH * 15 + 13);
                       }
                       break;
                   case 2:
                       if(Dungeon.level.avoid[pos] || !Dungeon.level.passable[pos]
                               || Actor.findChar(pos) != null) {
                           ScrollOfTeleportation.appear(this, ForestBossLevel.WIDTH * 23 + 13);
                       }
                       break;
                   case 3:
                       if(Dungeon.level.avoid[pos] || !Dungeon.level.passable[pos]
                               || Actor.findChar(pos) != null) {
                           ScrollOfTeleportation.appear(this, ForestBossLevel.WIDTH * 21 + 11);
                       }
                       break;
                   case 4:
                       if(Dungeon.level.avoid[pos] || !Dungeon.level.passable[pos]
                               || Actor.findChar(pos) != null) {
                           ScrollOfTeleportation.appear(this, ForestBossLevel.WIDTH * 15 + 19);
                       }
                       break;
                   case 5:
                       if(Dungeon.level.avoid[pos] || !Dungeon.level.passable[pos]
                               || Actor.findChar(pos) != null) {
                           ScrollOfTeleportation.appear(this, ForestBossLevel.WIDTH * 17 + 21);
                       }
                       break;
                   case 6:
                       if(Dungeon.level.avoid[pos] || !Dungeon.level.passable[pos]
                               || Actor.findChar(pos) != null) {
                           ScrollOfTeleportation.appear(this, ForestBossLevel.WIDTH * 21 + 21);
                       }
                       break;
                   case 7:
                       if(Dungeon.level.avoid[pos] || !Dungeon.level.passable[pos]
                               || Actor.findChar(pos) != null) {
                           ScrollOfTeleportation.appear(this, ForestBossLevel.WIDTH * 23 + 19);
                       }
                       break;
                   case 8:
                       if(Dungeon.level.avoid[pos] || !Dungeon.level.passable[pos]
                               || Actor.findChar(pos) != null) {
                           ScrollOfTeleportation.appear(this, ForestBossLevel.WIDTH * 24 + 7);
                       }
                       break;
                   case 9:
                       if(Dungeon.level.avoid[pos] || !Dungeon.level.passable[pos]
                               || Actor.findChar(pos) != null) {
                           ScrollOfTeleportation.appear(this, ForestBossLevel.WIDTH * 23 + 3);
                       }
                       break;
                   case 10:
                       if(Dungeon.level.avoid[pos] || !Dungeon.level.passable[pos]
                               || Actor.findChar(pos) != null) {
                           ScrollOfTeleportation.appear(this, ForestBossLevel.WIDTH * 27 + 4);
                       }
                       break;
                   case 11:
                       if(Dungeon.level.avoid[pos] || !Dungeon.level.passable[pos]
                               || Actor.findChar(pos) != null) {
                           ScrollOfTeleportation.appear(this, ForestBossLevel.WIDTH * 24 + 27);
                       }
                       break;
                   case 12:
                       if(Dungeon.level.avoid[pos] || !Dungeon.level.passable[pos]
                               || Actor.findChar(pos) != null) {
                           ScrollOfTeleportation.appear(this, ForestBossLevel.WIDTH * 29 + 27);
                       }
                       break;
                   case 13: case 14:
                       if(Dungeon.level.avoid[pos] || !Dungeon.level.passable[pos]
                               || Actor.findChar(pos) != null) {
                           ScrollOfTeleportation.appear(this, ForestBossLevel.WIDTH * 25 + 23);
                       }
                       break;
               }
                spend( TICK*6 );
            }

        }

        return super.act();
    }

    @Override
    public void damage(int dmg, Object src) {
        LockedFloor lock = Dungeon.hero.buff(LockedFloor.class);
        if (lock != null) lock.addTime(dmg*2);
        int damage = Random.Int(1,3);
        if (src instanceof Burning && ! Statistics.crivusfruitslevel2) {
            //一阶段如果触手着火会给予它100回合磷火燃烧（永久燃烧）
            Buff.affect( this, HalomethaneBurning.class ).reignite( this, 100f );
        } else if (src instanceof Hero || src instanceof Wand || src instanceof Potion || src instanceof Bomb){
            //反伤物理伤害4点,护甲超过+3就不再反伤，
            if(hero.belongings.armor != null){
                if(hero.belongings.armor.level < 3){
                    damage -= hero.drRoll();
                    hero.damage(damage, this);
                } else {
                    Buff.affect( hero, Poison.class ).set(1);
                }
            } else {
                damage -= hero.drRoll();
                hero.damage(damage, this);
            }
            super.damage(dmg, src);
        } else {
            super.damage(dmg, src);
        }
    }

    @Override
    public int attackProc(Char enemy, int damage) {
        damage = super.attackProc( enemy, damage );
        Buff.affect( enemy, Cripple.class, 2f );

        if(this.HT!=HP && enemy instanceof Hero ) {
            HP = Math.min(HT, HP + 1);
            this.sprite.showStatus(CharSprite.POSITIVE, "+2");
        }

        return super.attackProc(enemy, damage);
    }

    @Override
    public boolean reset() {
        return true;
    }

    @Override
    protected boolean getCloser(int target) {
        return true;
    }

    @Override
    protected boolean getFurther(int target) {
        return true;
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange( 1, 7 );
    }

    @Override
    public int attackSkill( Char target ) {
        return 6;
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(0, 4);
    }

    {
        immunities.add( ToxicGas.class );
    }

    private class Waiting extends Mob.Wandering{}
}

