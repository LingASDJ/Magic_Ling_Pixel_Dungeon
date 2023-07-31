package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Fire;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Adrenaline;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bleeding;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Corruption;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LockedFloor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.RoseShiled;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.Pushing;
import com.shatteredpixel.shatteredpixeldungeon.effects.TargetedCell;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRetribution;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfPsionicBlast;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfBlastWave;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Grim;
import com.shatteredpixel.shatteredpixeldungeon.levels.AncientMysteryCityBossLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.CaveTwoBossLevel;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.RoomStoneSprites;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.GameMath;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class RoomStone extends Mob {

    {
        spriteClass = RoomStoneSprites.class;

        HP = HT = 100;
        defenseSkill = 16;

        EXP = 9; //for corrupting
        maxLvl = -2;

        HUNTING = new Hunting();

        baseSpeed = 1.5f;

        properties.add(Property.BOSS); //添加BOSS属性
        immunities.add(Grim.class); //添加Grim类
        immunities.add(ScrollOfPsionicBlast.class); //添加ScrollOfPsionicBlast类
        immunities.add(ScrollOfRetribution.class); //添加ScrollOfRetribution类
        immunities.add(Corruption.class);
    }

    @Override
    public void damage(int dmg, Object src) {
        LockedFloor lock = Dungeon.hero.buff(LockedFloor.class);
        if (lock != null) lock.addTime(dmg*0.8f);
        super.damage(dmg, src);
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange( 5, 12 );
    }

    @Override
    public int attackSkill( Char target ) {
        return 5;
    }

    @Override
    public int drRoll() {
        return 3;
    }

    private static final String LAST_ENEMY_POS = "last_enemy_pos";
    private static final String LEAP_POS = "leap_pos";
    private static final String LEAP_CD = "leap_cd";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(LAST_ENEMY_POS, lastEnemyPos);
        bundle.put(LEAP_POS, leapPos);
        bundle.put(LEAP_CD, leapCooldown);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        lastEnemyPos = bundle.getInt(LAST_ENEMY_POS);
        leapPos = bundle.getInt(LEAP_POS);
        leapCooldown = bundle.getFloat(LEAP_CD);
    }

    private int lastEnemyPos = -1;

    @Override
    protected boolean act() {

        if(Statistics.sakaBackStage >= 2){
            ((AncientMysteryCityBossLevel)Dungeon.level).progress();
        }

        AiState lastState = state;
        boolean result = super.act();
        if (paralysed <= 0) leapCooldown --;

        //if state changed from wandering to hunting, we haven't acted yet, don't update.
        if (!(lastState == WANDERING && state == HUNTING)) {
            if (enemy != null) {
                lastEnemyPos = enemy.pos;
            } else {
                lastEnemyPos = hero.pos;
            }
        }

        return result;
    }

    private int leapPos = -1;
    private float leapCooldown = 0;

    @Override
    public void die( Object cause ) {
        Statistics.sakaBackStage++;
        GLog.n(Messages.get( DictFish.class, "angry" ));
        if (Statistics.sakaBackStage >= 2) {
            ((AncientMysteryCityBossLevel) Dungeon.level).progress();
        }

        for (Mob boss : Dungeon.level.mobs.toArray(new Mob[0])) {
            if (boss instanceof DictFish) {
                Buff.affect(boss, Adrenaline.class, 35f);
                Buff.affect(boss, RoseShiled.class, 10f);
            }
        }
        super.die( cause );
    }

    public class Hunting extends Mob.Hunting {


        public void dropRocks( Char target ) {

            hero.interrupt();
            final int rockCenter;
            if (Dungeon.level.adjacent(pos, target.pos)){
                int oppositeAdjacent = target.pos + (target.pos - pos);
                Ballistica trajectory = new Ballistica(target.pos, oppositeAdjacent, Ballistica.MAGIC_BOLT);
                WandOfBlastWave.throwChar(target, trajectory, 20, false, false, getClass());
                if (target == hero){
                    hero.interrupt();
                }
                rockCenter = trajectory.path.get(Math.min(trajectory.dist, 200));
            } else {
                rockCenter = target.pos;
            }

            Actor a = new Actor() {

                {
                    actPriority = HERO_PRIO+1;
                }
                private static final float TIME_TO_ZAP	= 1f;

                @Override
                protected boolean act() {

                    //pick an adjacent cell to the hero as a safe cell. This cell is less likely to be in a wall or containing hazards
                    int safeCell;
                    do {
                        safeCell = rockCenter + PathFinder.NEIGHBOURS8[Random.Int(8)];
                    } while (safeCell == pos
                            || (Dungeon.level.solid[safeCell] && Random.Int(2) == 0)
                            || (Blob.volumeAt(safeCell, CaveTwoBossLevel.PylonEnergy.class) > 0 && Random.Int(2) == 0));

                    int start = rockCenter - Dungeon.level.width() * 3 - 3;
                    int pos;
                    for (int y = 0; y < 7; y++) {
                        pos = start + Dungeon.level.width() * y;
                        for (int x = 0; x < 7; x++) {
                            if (!Dungeon.level.insideMap(pos)) {
                                pos++;
                                continue;
                            }
                            //add rock cell to pos, if it is not solid, and isn't the safecell
                            if (!Dungeon.level.solid[pos] && pos != safeCell && Random.Int(Dungeon.level.distance(rockCenter, pos)) == 0) {
                                //don't want to overly punish players with slow move or attack speed
                                GameScene.add(Blob.seed(pos, 4, Fire.class));
                            }
                            pos++;
                        }
                    }
                    Actor.remove(this);
                    return true;
                }
            };
            Actor.addDelayed(a, Math.min(target.cooldown(), 3*TICK));

        }

        @Override
        public boolean act( boolean enemyInFOV, boolean justAlerted ) {

            if (leapPos != -1){

                leapCooldown = Random.NormalIntRange(2, 4);
                Ballistica b = new Ballistica(pos, leapPos, Ballistica.STOP_TARGET | Ballistica.STOP_SOLID);

                //check if leap pos is not obstructed by terrain
                if (rooted || b.collisionPos != leapPos){
                    leapPos = -1;
                    return true;
                }

                final Char leapVictim = Actor.findChar(leapPos);
                final int endPos;

                //ensure there is somewhere to land after leaping
                if (leapVictim != null){
                    int bouncepos = -1;
                    for (int i : PathFinder.NEIGHBOURS8){
                        if ((bouncepos == -1 || Dungeon.level.trueDistance(pos, leapPos+i) < Dungeon.level.trueDistance(pos, bouncepos))
                                && Actor.findChar(leapPos+i) == null && Dungeon.level.passable[leapPos+i]){
                            bouncepos = leapPos+i;
                        }
                    }
                    if (bouncepos == -1) {
                        leapPos = -1;
                        return true;
                    } else {
                        endPos = bouncepos;
                    }
                } else {
                    endPos = leapPos;
                }

                //do leap
                sprite.visible = Dungeon.level.heroFOV[pos] || Dungeon.level.heroFOV[leapPos] || Dungeon.level.heroFOV[endPos];
                Ballistica route = new Ballistica(leapPos, target, Ballistica.STOP_TARGET | Ballistica.STOP_SOLID);
                int cell = route.collisionPos;

                //can't occupy the same cell as another char, so move back one.
                int backTrace = route.dist-1;
                while (Actor.findChar( cell ) != null && cell != leapPos) {
                    cell = route.path.get(backTrace);
                    backTrace--;
                }
                sprite.jump(pos, leapPos, new Callback() {
                    @Override
                    public void call() {

                        if (leapVictim != null && alignment != leapVictim.alignment){
                            Buff.affect(leapVictim, Bleeding.class).set(0.75f*damageRoll());
                            leapVictim.sprite.flash();
                            dropRocks(enemy);
                            Sample.INSTANCE.play(Assets.Sounds.HIT);
                        }

                        if (endPos != leapPos){
                            Actor.addDelayed(new Pushing(RoomStone.this, leapPos, endPos), -1);
                        }

                        pos = endPos;
                        leapPos = -1;
                        sprite.idle();
                        Dungeon.level.occupyCell(RoomStone.this);
                        next();
                    }
                });
                return false;
            }

            enemySeen = enemyInFOV;
            if (enemyInFOV && !isCharmedBy( enemy ) && canAttack( enemy )) {

                return doAttack( enemy );

            } else {

                if (enemyInFOV) {
                    target = enemy.pos;
                } else if (enemy == null) {
                    state = WANDERING;
                    target = Dungeon.level.randomDestination( RoomStone.this );
                    return true;
                }


                        if (leapCooldown <= 0 && enemyInFOV && !rooted
                                && Dungeon.level.distance(pos, enemy.pos) >= 3 && Statistics.sakaBackStage>=1) {

                            int targetPos = enemy.pos;
                            if (lastEnemyPos != enemy.pos) {
                                int closestIdx = 0;
                                for (int i = 1; i < PathFinder.CIRCLE8.length; i++) {
                                    if (Dungeon.level.trueDistance(lastEnemyPos, enemy.pos + PathFinder.CIRCLE8[i])
                                            < Dungeon.level.trueDistance(lastEnemyPos, enemy.pos + PathFinder.CIRCLE8[closestIdx])) {
                                        closestIdx = i;
                                    }
                                }
                                targetPos = enemy.pos + PathFinder.CIRCLE8[(closestIdx + 4) % 8];
                            }

                            Ballistica b = new Ballistica(pos, targetPos, Ballistica.STOP_TARGET | Ballistica.STOP_SOLID);
                            //try aiming directly at hero if aiming near them doesn't work
                            if (b.collisionPos != targetPos && targetPos != enemy.pos) {
                                targetPos = enemy.pos;
                                b = new Ballistica(pos, targetPos, Ballistica.STOP_TARGET | Ballistica.STOP_SOLID);
                            }
                            if (b.collisionPos == targetPos) {
                                //get ready to leap
                                leapPos = targetPos;
                                //don't want to overly punish players with slow move or attack speed
                                spend(GameMath.gate(TICK, enemy.cooldown(), 3 * TICK));
                                if (Dungeon.level.heroFOV[pos] || Dungeon.level.heroFOV[leapPos]) {
                                    GLog.w(Messages.get(RoomStone.this, "leap"));
                                    sprite.parent.addToBack(new TargetedCell(leapPos, 0xFF0000));
                                    ((RoomStoneSprites) sprite).leapPrep(leapPos);
                                    hero.interrupt();
                                }
                                return true;
                            }
                        }




                int oldPos = pos;
                if (target != -1 && getCloser( target )) {

                    spend( 1 / speed() );
                    return moveSprite( oldPos,  pos );

                } else {
                    spend( TICK );
                    if (!enemyInFOV) {
                        sprite.showLost();
                        state = WANDERING;
                        target = Dungeon.level.randomDestination( RoomStone.this );
                    }
                    return true;
                }
            }
        }

    }

}

