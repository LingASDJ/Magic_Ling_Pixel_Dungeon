package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.gold;

import static com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfBlastWave.throwChar;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TimekeepersHourglass;
import com.shatteredpixel.shatteredpixeldungeon.items.food.MysteryMeat;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfBlastWave;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Swiftthistle;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class Drake extends GoldMob {
    {
        spriteClass = DrakeSprite.class;

        HP = HT = 80;
        defenseSkill = 10;

        baseSpeed = 0.8f;

        EXP = 9;
        maxLvl = 19;

        alignment = Alignment.NEUTRAL;
        state = PASSIVE;

        loot = new MysteryMeat();
        lootChance = 0.25f;
    }

    private boolean gotClose = false;
    private int ultimatum = 2;

    private boolean isNotice = false;
    private boolean isStopHiding = false;


    public int damageRoll() {
        return Random.NormalIntRange( 8, 15 );
    }

    @Override
    public int attackSkill( Char target ) {
        return 20;
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(2,4);
    }


    @Override
    public boolean interact(Char c) {
        if (alignment != Alignment.NEUTRAL || c != Dungeon.hero) {
            return super.interact(c);
        }
        if(!isStopHiding){
            stopHiding();
            isStopHiding = true;
        }
        if (Dungeon.hero.invisible <= 0
                && Dungeon.hero.buff(Swiftthistle.TimeBubble.class) == null
                && Dungeon.hero.buff(TimekeepersHourglass.timeFreeze.class) == null){
            return doAttack(Dungeon.hero);
        } else {
            sprite.idle();
            alignment = Alignment.ENEMY;
            Dungeon.hero.spendAndNext(1f);
            return true;
        }
    }

    @Override
    protected boolean act() {
        if (alignment == Alignment.NEUTRAL && state != PASSIVE){
            alignment = Alignment.ENEMY;
        }
        if(Dungeon.hero != null){
            if (alignment == Alignment.NEUTRAL && Dungeon.level.distance(pos,Dungeon.hero.pos)<=2){
//                if(!isNotice){
//                    GLog.n('\n'+ Messages.get(this, "get_close"));
//                    Camera.main.shake(5, 1f);
//                    isNotice = true;
//                }
//                Dungeon.hero.interrupt();
//                Buff.append(this, DM300.FallingRockBuff.class, 0f).setRockPositions(new ArrayList<>(Dungeon.hero.pos));
                if(Dungeon.level.adjacent(pos,Dungeon.hero.pos)){
//                    stopHiding();
                    GLog.n('\n'+ Messages.get(this, "revealing"));
                    gotClose = true;
                }
            }
            if(gotClose){
                ultimatum --;
            }
            if(ultimatum < 0 && !isStopHiding){
                stopHiding();
                isStopHiding = true;
            }
        }
        return super.act();
    }
    @Override
    public CharSprite sprite() {
        DrakeSprite sprite = (DrakeSprite) super.sprite();
        if (alignment == Alignment.NEUTRAL) sprite.hideDrake();
        return sprite;
    }
    @Override
    public void onAttackComplete() {
        super.onAttackComplete();
        if (alignment == Alignment.NEUTRAL){
            alignment = Alignment.ENEMY;
        }
    }

    @Override
    public void damage(int dmg, Object src) {
        if (state == PASSIVE){
            alignment = Alignment.ENEMY;
            stopHiding();
        }
        super.damage(dmg, src);
    }
//    @Override
//    public boolean interact(Char c) {
//        if (alignment != Alignment.NEUTRAL || c != Dungeon.hero){
//            return super.interact(c);
//        }
//        stopHiding();
//
////        Dungeon.hero.busy();
//        if (Dungeon.hero.invisible <= 0
//                && Dungeon.hero.buff(Swiftthistle.TimeBubble.class) == null
//                && Dungeon.hero.buff(TimekeepersHourglass.timeFreeze.class) == null){
////            return doAttack(Dungeon.hero);
//            sprite.idle();
//            alignment = Alignment.ENEMY;
//            return true;
//        } else {
//            sprite.idle();
//            alignment = Alignment.ENEMY;
//            return true;
//        }
//    }

    @Override
    public boolean add(Buff buff) {
        boolean buffAdd = super.add(buff);
        if (buff.type == Buff.buffType.NEGATIVE && alignment == Alignment.NEUTRAL){
            alignment = Alignment.ENEMY;
            stopHiding();
            if (sprite != null) sprite.idle();
        }
        return buffAdd;
    }
    public void stopHiding(){
        state = HUNTING;
        if (sprite != null) sprite.idle();
        if (Actor.chars().contains(this) && Dungeon.level.heroFOV[pos]) {
//            enemy = Dungeon.hero;
//            target = pos;
//            enemySeen = true;
            GLog.w("\n"+Messages.get(this, "reveal") );
//            CellEmitter.get(pos).burst(Speck.factory(Speck.ROCK), 10);
            Mob drake = this;
            sprite.jump(pos, pos, new Callback() {
                @Override
                public void call() {
//                    move(target);
//                    Dungeon.level.occupyCell(drake);
                    WandOfBlastWave.BlastWave.blast(pos);
                    Sample.INSTANCE.play(Assets.Sounds.BLAST);
//                    Camera.main.shake(100, 0.5f);
                    Dungeon.observe();
//                    GameScene.updateFog();


                    //throws other chars around the center.
                    for (int i  : PathFinder.NEIGHBOURS13){
                        Char ch = Actor.findChar(pos + i);

                        CellEmitter.get(pos+i).burst(Speck.factory(Speck.ROCK), 10);
                        WandOfBlastWave.BlastWave.blast(pos+i);
                        WandOfBlastWave.BlastWave.blast(pos+2*i);
                        WandOfBlastWave.BlastWave.blast(pos+3*i);
                        if (ch != null && ch != drake){
                            if (ch.alignment != Char.Alignment.ALLY) ch.damage(damageRoll(), this);

                            if (ch.pos == pos + i) {
                                Ballistica trajectory = new Ballistica(ch.pos, ch.pos + i, Ballistica.MAGIC_BOLT);
                                int strength = 2;
                                throwChar(ch, trajectory, strength, false, true, getClass());
                                ch.damage(Random.Int(10,25),this);
                                GLog.w("\n"+Messages.get(Drake.class, "shock",ch.name()) );
                                if(Dungeon.hero != null){
                                    if(!Dungeon.hero.isAlive()){
                                        Dungeon.fail(getClass());
                                        GLog.n('\n'+ Messages.get(Drake.class, "rock_punk_kill"));
                                    }
                                }
                            }

                        }
                    }
                    spend(2f*TICK);
                    Dungeon.hero.spendAndNext(1f);
                }
            });
        }
    }
    @Override
    public boolean reset() {
        if (state != PASSIVE) state = WANDERING;
        return true;
    }


    private static final String STOPHIDING	= "weapon";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( STOPHIDING, isStopHiding );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        isStopHiding = bundle.getBoolean(STOPHIDING);
        if(isStopHiding){
            state = WANDERING;
            alignment = Alignment.ENEMY;
        }
    }
}
