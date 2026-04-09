package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Sheep;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.Transmuting;
import com.shatteredpixel.shatteredpixeldungeon.items.props.ConfusedMieMieTalisman;
import com.shatteredpixel.shatteredpixeldungeon.items.props.FaintGlimmer;
import com.shatteredpixel.shatteredpixeldungeon.items.props.FreeCrack;
import com.shatteredpixel.shatteredpixeldungeon.items.props.KillEye;
import com.shatteredpixel.shatteredpixeldungeon.items.props.NewStem;
import com.shatteredpixel.shatteredpixeldungeon.items.props.RapidEarthRoot;
import com.shatteredpixel.shatteredpixeldungeon.items.props.WenStudyingPaperOne;
import com.shatteredpixel.shatteredpixeldungeon.items.props.WenStudyingPaperTwo;
import com.shatteredpixel.shatteredpixeldungeon.items.props.YanStudyingPaperOne;
import com.shatteredpixel.shatteredpixeldungeon.items.props.YanStudyingPaperTwo;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.RandomChest;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.TeleportationTrap;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.BArray;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class PropBuff extends Buff{

    {
        type = buffType.POSITIVE;
    }

    public int timeA = 0,timeB = 0,timeC = 0, timeD = 0, timeE =0, timeF = 0, timeG = 0, timeH = 0, timeI = 0;
    public int levelA = 0;

    public boolean potionLost = false;
    public int warningTime = 0;

    @Override
    public boolean act() {

        spend(1);

        if(Dungeon.depth>0){

            Hero hero = Dungeon.hero;

            if(Dungeon.hero.belongings.getItem(RapidEarthRoot.class)!=null) {
                timeA ++;
                if(timeA >= 100) {
                    Buff.affect(hero, Barkskin.class).set(Dungeon.depth, 10);
                    timeA = 0;
                }
            }
            if(Dungeon.hero.belongings.getItem(WenStudyingPaperOne.class)!=null) {
                if(timeB < 7){
                    timeB ++;
                }
            }
            if(Dungeon.hero.belongings.getItem(YanStudyingPaperTwo.class)!=null) {
                timeC ++;
                if(timeC >= 30) {
                    Buff.affect(hero, Haste.class, 5f);
                    timeC = 0;
                }
            }

            if(Dungeon.hero.belongings.getItem(FreeCrack.class)!=null) {
                timeI ++;
                if(timeI >= 50) {
                    TeleportationTrap t = new TeleportationTrap();
                    t.pos = hero.pos;
                    t.activate();
                    timeI = 0;
                    Buff.affect(hero,Vertigo.class,2f);
                    target.damage(Math.max((int) (target.HP * Random.NormalFloat(0.08f,0.16f)), 1),this, Char.DamageType.REAL);
                }
            }

            if(Dungeon.hero.belongings.getItem(ConfusedMieMieTalisman.class)!=null) {
                if(timeD>0) timeD--;

                // 预警计时器
                if(warningTime > 1) {
                    warningTime--;
                    if(warningTime == 4) {
                        GLog.n(Messages.get(ConfusedMieMieTalisman.class, "warning", warningTime-1));
                    }
                    if(warningTime == 1){
                        GLog.n(Messages.get(ConfusedMieMieTalisman.class,"sheep"));
                        timeD = 135;
                        warningTime = 0;

                        int cell = hero.pos;
                        PathFinder.buildDistanceMap( cell, BArray.not( Dungeon.level.solid, null ), 2 );
                        ArrayList<Integer> spawnPoints = new ArrayList<>();
                        for (int i = 0; i < PathFinder.distance.length; i++) {
                            if (PathFinder.distance[i] < Integer.MAX_VALUE) {
                                spawnPoints.add(i);
                            }
                        }

                        for (int i : spawnPoints){
                            if (Dungeon.level.insideMap(i)
                                    && Actor.findChar(i) == null
                                    && !(Dungeon.level.pit[i])) {
                                Sheep sheep = new Sheep();
                                sheep.lifespan = 8;
                                sheep.pos = i;
                                GameScene.add(sheep);
                                Dungeon.level.occupyCell(sheep);
                                CellEmitter.get(i).burst(Speck.factory(Speck.WOOL), 4);
                            }
                        }

                        CellEmitter.get(cell).burst(Speck.factory(Speck.WOOL), 4);
                        Sample.INSTANCE.play(Assets.Sounds.PUFF);
                        Sample.INSTANCE.play(Assets.Sounds.SHEEP);
                    }
                }

                if(timeD==0){
                    if(warningTime <= 0) {
                        Transmuting.show(Dungeon.hero, new RandomChest(), new ConfusedMieMieTalisman());
                        Dungeon.hero.sprite.emitter().start(Speck.factory(Speck.STAR), 0.2f, 10);
                        warningTime = 11;
                        Sample.INSTANCE.play(Assets.Sounds.SHEEP);
                        GLog.w(Messages.get(ConfusedMieMieTalisman.class, "warning_start"));
                    }
                }
            }

            if(Dungeon.hero.belongings.getItem(WenStudyingPaperTwo.class)!=null) {
                timeE++;
                if (timeE >= 30) {
                    Buff.affect(hero, Hex.class, 25);
                    timeE = 0;
                }
            }
            if (target.HP < target.HT && !((Hero)target).isStarving()) {
                if (Dungeon.hero.belongings.getItem(NewStem.class) != null) {
                    timeF++;
                    if (timeF >= 10) {
                        target.HP += 1;
                        timeF = 0;
                    }
                }
            }

            if(Dungeon.hero.belongings.getItem(KillEye.class)!=null) {
                timeG++;
                int s = Dungeon.depth/5;
                if(timeG >= 13-s){
                    timeG = 13-s;
                }
            }

            if(Dungeon.hero.belongings.getItem(FaintGlimmer.class)!=null) {
                timeH++;
                if(timeH >= 5 && levelA <= 9){
                    levelA++;
                    timeH = 0;
                }
                if(levelA < 0){
                    levelA = 0;
                }
            }

        }

        if(potionLost){
            GLog.n(Messages.get(YanStudyingPaperOne.class,"lost"));
            potionLost = false;
        }

        return true;
    }

    @Override
    public int icon() {
        return BuffIndicator.PROP_SHADOW;
    }

    private static final String TIMEA = "timeA";
    private static final String TIMEB = "timeB";
    private static final String TIMEC = "timeC";
    private static final String TIMED = "timeD";
    private static final String TIMEE = "timeE";
    private static final String TIMEF = "timeF";
    private static final String TIMEG = "timeG";
    private static final String TIMEH = "timeH";
    private static final String TIMEI = "timeI";
    private static final String WRTME = "wrtme";

    private static final String LEVELA = "levelA";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( TIMEA, timeA );
        bundle.put( TIMEB, timeB );
        bundle.put( TIMEC, timeC );
        bundle.put( TIMED, timeD );
        bundle.put( TIMEE, timeE );
        bundle.put( TIMEF, timeF );
        bundle.put( TIMEG, timeG );
        bundle.put( TIMEH, timeH);
        bundle.put( TIMEI, timeI);

        bundle.put( WRTME,warningTime );

        bundle.put( LEVELA,levelA);
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        timeA = bundle.getInt( TIMEA );
        timeB = bundle.getInt( TIMEB );
        timeC = bundle.getInt( TIMEC );
        timeD = bundle.getInt( TIMED );
        timeE = bundle.getInt( TIMEE );
        timeF = bundle.getInt( TIMEF );
        timeG = bundle.getInt( TIMEG );
        timeH = bundle.getInt( TIMEH );
        timeI = bundle.getInt( TIMEI );

        warningTime = bundle.getInt( WRTME );

        levelA = bundle.getInt(LEVELA);
    }

}
