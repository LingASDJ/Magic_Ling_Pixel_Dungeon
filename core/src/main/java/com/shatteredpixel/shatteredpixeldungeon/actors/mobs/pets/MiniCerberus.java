package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.pets;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfLiquidFlame;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.DogStick;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MiniCerberusBossSprites;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;

public class MiniCerberus extends Pets {

    /** 狗叫冷却（逻辑秒） */
    private static final float WOOF_COOLDOWN = 1.2f;
    /** 狗叫动作持续时间，必须 ≥ 狗叫动画时长(11帧@12fps≈0.92s)，确保动画播完才允许移动 */
    private static final float WOOF_ACTION_TIME = 1.0f;

    {
        spriteClass = MiniCerberusBossSprites.class;
        WANDERING = new Wandering();
        defenseSkill = 15;
    }

    private float lastWoofTime = -999f;

    public Mob closest = null;

    @Override
    protected boolean act() {
        boolean didWoof = false;
        closest = null;

        for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])){
            if (mob != this && mob.isAlive()
                    && mob.state != mob.SLEEPING  // 这里原来是SLEEPING,而不是mob.SLEEPING，猜测意图是不对睡觉的怪狗叫，故改为后者
                    && hero.fieldOfView[mob.pos]
                    && mob.alignment == Alignment.ENEMY
                    && !mob.properties().contains(Char.Property.PETS)){
                closest = mob;
                // 换层时，时钟重置带来的大数CD问题
                if (lastWoofTime > Actor.now()) lastWoofTime = -999f;
                if (Actor.now() - lastWoofTime >= WOOF_COOLDOWN
                        && Dungeon.level.distance(pos, mob.pos) < 3){

                    ((MiniCerberusBossSprites)sprite).dogWoof( closest.pos );
                    Sample.INSTANCE.play( Assets.Sounds.DOGWOOF );

                    lastWoofTime = Actor.now();
                    didWoof = true;
                    break;
                }
            }
        }

        if (didWoof){
            // 叫的时候站住，动作时长覆盖整个狗叫动画，不进入 Wandering 移动
            spend( WOOF_ACTION_TIME );
            return true;
        }

        return super.act();
    }

    @Override
    public void die( Object cause ) {
        super.die(cause);
        Dungeon.level.drop(new PotionOfLiquidFlame().identify(),pos).sprite.drop();
        Buff.affect(hero, DogStick.CoolDownStoneRecharge.class, DogStick.CoolDownStoneRecharge.DURATION);
    }

    private class Wandering extends Mob.Wandering {

        @Override
        public boolean act( boolean enemyInFOV, boolean justAlerted ) {
            if ( enemyInFOV ) {
                enemySeen = true;
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
            return true;
        }
    }
}
