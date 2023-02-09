package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses;

import static com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.CrivusFruits.GoTwoBoss;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.HalomethaneBurning;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.levels.ForestBossLevel;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.RotLasherSprite;
import com.watabou.utils.Random;

public class CrivusFruitsLasher extends Mob {

    {
        spriteClass = RotLasherSprite.class;

        HP = HT = GoTwoBoss ? 40 : 20;
        defenseSkill = 0;

        EXP = 1;

        loot = Generator.Category.SEED;
        lootChance = 0.35f;

        state = WANDERING = new Waiting();

        properties.add(Property.IMMOVABLE);
        properties.add(Property.MINIBOSS);
    }

    @Override
    protected boolean act() {
        GameScene.add(Blob.seed(pos, GoTwoBoss ? 60 : 35, ToxicGas.class));

        if(Dungeon.hero.buff(CrivusFruits.DiedDamager.class) == null){
            Buff.affect(this,CrivusFruits.DiedDamager.class);
        }

        //二阶段开始瞬移，地狱绘图
        if(GoTwoBoss){
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

        if(this.HT!=HP) {
            HP = Math.min(HT, HP + 1);
            this.sprite.showStatus(CharSprite.POSITIVE, "+2");
        }

        return super.act();
    }

    @Override
    public void damage(int dmg, Object src) {
        if (src instanceof Burning && !GoTwoBoss) {
            Buff.affect( this, HalomethaneBurning.class ).reignite( this, 100f );
        } else {
            super.damage(dmg, src);
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
        return true;
    }

    @Override
    protected boolean getFurther(int target) {
        return true;
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(8, 15);
    }

    @Override
    public int attackSkill( Char target ) {
        return 15;
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(0, 8);
    }

    {
        immunities.add( ToxicGas.class );
    }

    private class Waiting extends Mob.Wandering{}
}

