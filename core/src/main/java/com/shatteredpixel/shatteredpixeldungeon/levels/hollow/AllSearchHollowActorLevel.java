package com.shatteredpixel.shatteredpixeldungeon.levels.hollow;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;
import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.level;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicalSight;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.status.ActivePoint;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.status.ScoreBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.MobSpawner;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NPC;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.levels.RegularLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.PrisonPainter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.hollow.crystal.CrystalOneRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.hollow.crystal.CrystalThreeRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.hollow.crystal.CrystalTwoRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.hollow.escape.MagicCircleRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.hollow.escape.MainTowerRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.hollow.gold.GoldFiveRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.hollow.gold.GoldFourRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.hollow.gold.GoldOneRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.hollow.gold.GoldThreeRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.hollow.gold.GoldTwoRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.hollow.guard.GuardFiveRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.hollow.guard.GuardFourRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.hollow.guard.GuardOneRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.hollow.guard.GuardThreeRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.hollow.guard.GuardTwoRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.hollow.locked.FourLockedRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.hollow.locked.OneLockedRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.hollow.locked.ThreeLockedRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.hollow.locked.TwoLockedRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special.PitRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special.SpecialRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.EntranceRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.StandardRoom;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MobSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.ScoreBar;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WinAllSearchStatus;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

import java.util.ArrayList;

public class AllSearchHollowActorLevel extends RegularLevel {

    public int readscore;

    {
        extraGlass = false;
    }

    @Override
    protected ArrayList<Room> initRooms() {
        ArrayList<Room> initRooms = new ArrayList<>();

        initRooms.add(roomEntrance = EntranceRoom.createEntrance());
        initRooms.add(roomExit = new MagicCircleRoom());

        int specials = specialRooms(feeling == Feeling.LARGE || feeling == Feeling.BIGROOMS);
        if (feeling == Feeling.LARGE || feeling == Feeling.BIGROOMS){
            specials++;
        }
        SpecialRoom.initForFloor();
        for (int i = 0; i < specials; i++) {
            SpecialRoom s = SpecialRoom.createRoom();
            if (s instanceof PitRoom) specials++;
            initRooms.add(s);
        }

        int standards = standardRooms(feeling == Feeling.LARGE);
        if (feeling == Feeling.BIGROOMS){
            standards = (int)Math.ceil(standards * 2.75f);
        } else if (feeling == Feeling.LARGE){
            standards = (int)Math.ceil(standards * 1.5f);
        }
        for (int i = 0; i < standards; i++) {
            StandardRoom s;
            do {
                s = StandardRoom.createRoom();
            } while (!s.setSizeCat( standards-i ));
            i += s.sizeFactor()-1;
            initRooms.add(s);
        }

        for (int i = 0; i < 2; i++) {
            initRooms.add(new MainTowerRoom());
        }

        initRooms.add(new OneLockedRoom());
        initRooms.add(new TwoLockedRoom());
        initRooms.add(new ThreeLockedRoom());
        initRooms.add(new FourLockedRoom());

        initRooms.add(new GoldOneRoom());
        initRooms.add(new GoldTwoRoom());
        initRooms.add(new GoldThreeRoom());
        initRooms.add(new GoldFourRoom());
        initRooms.add(new GoldFiveRoom());

        initRooms.add(new CrystalOneRoom());
        initRooms.add(new CrystalTwoRoom());
        initRooms.add(new CrystalThreeRoom());

        initRooms.add(new GuardOneRoom());
        initRooms.add(new GuardTwoRoom());
        initRooms.add(new GuardThreeRoom());
        initRooms.add(new GuardFourRoom());
        initRooms.add(new GuardFiveRoom());

        return initRooms;
    }

    @Override
    protected void createItems() {
        super.createItems();
        for (Heap heap : heaps.valueList()) {
            for (Item item : heap.items) {
              item.OnlyAllSearch = true;
            }
        }
    }

    @Override
    public void playLevelMusic(){
        Music.playModeBGM(Assets.Music.SEACH,true);
    }

    @Override
    protected int standardRooms(boolean forceMax) {
        if (forceMax) return 1;
        return 3 + Dungeon.depth/5 + Random.chances(new float[]{1,1,1});
    }

    @Override
    protected int specialRooms(boolean forceMax) {
        if (forceMax) return 1;
        return 3 + Dungeon.depth/5 + Random.chances(new float[]{1,1,1});
    }

    @Override
    public String tilesTex() {
        return Assets.Environment.TILES_GHOST;
    }

    @Override
    public String waterTex() {
        return Assets.Environment.WATER_GHOST;
    }

    @Override
    protected void createMobs() {
        super.createMobs();
        Buff.detach(hero, ScoreBuff.class);
        Buff.detach(hero, ActivePoint.class);
        Buff.detach(hero, RecordTimeDead.class);

        Buff.affect(hero, ScoreBuff.class);
        Buff.affect(hero, ActivePoint.class).set(100, 1);
        Buff.affect(hero, RecordTimeDead.class, RecordTimeDead.DURATION);

        ScoreBar.updateScoreFromBuff(hero.buff(ScoreBuff.class));
        ScoreBar.setRules(3);
        Buff.detach(hero, MagicalSight.class);
        ScoreBar.assignScore(0,20000);

        PinkPrism pinkPrism = new PinkPrism();
        pinkPrism.pos = exit();
        mobs.add(pinkPrism);
    }

    @Override
    protected Painter painter() {
        return new PrisonPainter()
                .setWater(feeling == Feeling.WATER ? 0.85f : 0.30f, 6)
                .setGrass(feeling == Feeling.GRASS ? 0.65f : 0.15f, 3)
                .setTraps(nTraps(), trapClasses(), trapChances());
    }

    public final String READSCORE = "readscore";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle(bundle);
        bundle.put(READSCORE, readscore);
    }


    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle(bundle);
        ScoreBar.setRules(3);
        if(readscore != 0){
            ScoreBar.assignScore(readscore,20000);
        }
    }

    public static class PinkPrism extends NPC {

        {
            spriteClass = PinkPrismSprite.class;
            properties.add(Property.IMMOVABLE);
            properties.add(Property.SEARCH);
        }

        @Override
        public int defenseSkill( Char enemy ) {
            return INFINITE_EVASION;
        }

        @Override
        public void damage(int dmg, Object src, DamageType type) {
        }

        @Override
        public boolean add(Buff buff ) {
            return false;
        }

        @Override
        public boolean act() {

            if(hero.buff(BackTimeGoHome.class)!=null){
                if(Dungeon.level.distance(pos, hero.pos) > 5){
                    yell(Messages.get(PinkPrism.class,"failed"));
                    Buff.detach(hero,BackTimeGoHome.class);
                }
            }

            return super.act();
        }
        @Override
        public boolean interact(Char c) {
            ActivePoint buff = hero.buff(ActivePoint.class);
            if(HT == 100){
                Statistics.AllSearchSuccessEsc = true;
                Game.runOnRenderThread(() -> GameScene.show(new WinAllSearchStatus()));
                if(hero.buff(ScoreBuff.class)!=null) {
                    ScoreBuff buffs = hero.buff(ScoreBuff.class);
                    SPDSettings.AllSearchScore(buffs.score);
                    Statistics.getAlLSearchScore = buffs.score;
                }
            } else if(buff != null){
                if(buff.escActive){
                    Game.runOnRenderThread(new Callback() {
                        @Override
                        public void call() {
                            GameScene.show(new WndOptions(new PinkPrismSprite(),
                                    Messages.titleCase(Messages.get(this, "name")),
                                    Messages.get(this, "quest_start_prompt"),
                                    Messages.get(this, "enter_yes"),
                                    Messages.get(this, "enter_no")) {
                                @Override
                                protected void onSelect(int index) {
                                    if (index == 0) {
                                        Buff.affect(hero, BackTimeGoHome.class).set(100, 1);
                                        yell(Messages.get(PinkPrism.class, "warning"));
                                        for (int i = 0; i < 12; i++) {
                                            Mob mob = Reflection.newInstance(MobSpawner.getMobRotation(31).get(0));
                                            GameScene.add(mob);
                                            mob.pos = level.randomDestination(mob);
                                            mob.state = mob.HUNTING;
                                            mob.beckon( pos );
                                            CellEmitter.center( pos ).start( Speck.factory( Speck.SCREAM ), 0.3f, 3 );
                                            Sample.INSTANCE.play( Assets.Sounds.ALERT );
                                        }
                                    }
                                }
                            });
                        }
                    });
                }
            }
            return true;
        }

    }



    public static class PinkPrismSprite extends MobSprite {

        private Animation charging;
        private Emitter chargeParticles;

        public PinkPrismSprite(){
            texture( Assets.Sprites.PINK_SENTRY);

            idle = new Animation(1, true);
            idle.frames(texture.uvRect(0, 0, 8, 15));

            run = idle.clone();
            attack = idle.clone();
            charging = idle.clone();
            die = idle.clone();
            zap = idle.clone();

            play( idle );
        }

        @Override
        public void link(Char ch) {
            super.link(ch);

            chargeParticles = centerEmitter();
            chargeParticles.autoKill = false;
            chargeParticles.pour(MagicMissile.WardParticle.UP, 0.04f);

            chargeParticles.on = false;

            play(charging);
        }

        @Override
        public void die() {
            super.die();
            if (chargeParticles != null){
                chargeParticles.on = false;
            }
        }

        @Override
        public void kill() {
            super.kill();
            if (chargeParticles != null){
                chargeParticles.killAndErase();
            }
        }

        public void charge(){
            play(charging);
            if (visible) Sample.INSTANCE.play( Assets.Sounds.CHARGEUP );
        }

        @Override
        public void play(Animation anim) {
            if (chargeParticles != null) chargeParticles.on = anim == charging;
            super.play(anim);
        }

        private float baseY = Float.NaN;

        @Override
        public void place(int cell) {
            super.place(cell);
            baseY = y;
        }

        @Override
        public void turnTo(int from, int to) {
            //do nothing
        }
        private float time;
        @Override
        public void update() {
            super.update();
            if (chargeParticles != null){
                chargeParticles.pos( center() );
                chargeParticles.visible = visible;
            }

            if (flashTime <= 0) {
                time += Game.elapsed / 3.5f;
                float r = 0.33f+0.57f*Math.max(0f, (float)Math.sin( time));
                float g = 0.53f+0.57f*Math.max(0f, (float)Math.sin( time + 2*Math.PI/3 ));
                float b = 0.63f+0.57f*Math.max(0f, (float)Math.sin( time + 4*Math.PI/3 ));
                tint( r,g,b, 0.3f);
            }

            if (!paused){
                if (Float.isNaN(baseY)) baseY = y;
                y = baseY + (float) Math.sin(Game.timeTotal);
                shadowOffset = 0.25f - 0.8f*(float) Math.sin(Game.timeTotal);
            }
        }
    }

    public static class RecordTimeDead extends FlavourBuff {

        public static final float DURATION	= 1600f;

        {
            announced = true;
        }

        public void detach() {
           super.detach();
            if(hero.buff(ScoreBuff.class)!=null && !Statistics.AllSearchSuccessEsc) {
                Statistics.AllSearchFailedEsc = true;
                ScoreBuff buffs = hero.buff(ScoreBuff.class);
                SPDSettings.AllSearchScore(buffs.score/2);
                Statistics.getAlLSearchScore = buffs.score/2;
                Game.runOnRenderThread(() -> GameScene.show(new WinAllSearchStatus()));
            }
        }



        @Override
        public int icon() {
            return BuffIndicator.INVISIBLE_ACTION;
        }


        @Override
        public float iconFadePercent() {
            return Math.max(0, (DURATION - visualcooldown()) / DURATION);
        }

    }

    public static class BackTimeGoHome extends Buff {

        {
            type = buffType.POSITIVE;
        }

        private int level = 0;

        public int timeRecords =45;

        private int interval = 1;

        @Override
        public boolean act() {
            if (target.isAlive()) {

                spend( interval );

                if (level <= 0) {
                    detach();
                }

                for (Mob m : Dungeon.level.mobs){
                    if (m.alignment == Char.Alignment.ENEMY && m.distance(hero) > 5) {
                        m.beckon(hero.pos);
                    }
                }

                if(timeRecords>=0){
                    timeRecords--;
                    if(timeRecords==10){
                        GLog.w(Messages.get(PinkPrism.class,"end"));
                    } else if(timeRecords<=0) {
                        GLog.w(Messages.get(PinkPrism.class, "go"));
                        for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])){
                            if (mob instanceof PinkPrism ){
                                mob.HT = mob.HP = 100;
                            }
                        }
                        detach();
                    }
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
            return BuffIndicator.SNOW_SHILED;
        }

        @Override
        public void tintIcon(Image icon) {
            icon.hardlight(Window.SHPX_COLOR);
        }


        @Override
        public String iconTextDisplay() {
            return Integer.toString(timeRecords);
        }

        @Override
        public String desc() {
            return Messages.get(this, "desc", timeRecords);
        }

        private static final String LEVEL	    = "level";
        private static final String INTERVAL    = "interval";
        private static final String TIMERECORDS = "timerecords";

        @Override
        public void storeInBundle( Bundle bundle ) {
            super.storeInBundle( bundle );
            bundle.put( INTERVAL, interval );
            bundle.put( LEVEL, level );
            bundle.put( TIMERECORDS, timeRecords );
        }

        @Override
        public void restoreFromBundle( Bundle bundle ) {
            super.restoreFromBundle( bundle );
            interval = bundle.getInt( INTERVAL );
            level = bundle.getInt( LEVEL );
            timeRecords = bundle.getInt( TIMERECORDS );
        }
    }

    @Override
    public boolean activateTransition(Hero hero, LevelTransition transition) {
        return false;
    }

}
