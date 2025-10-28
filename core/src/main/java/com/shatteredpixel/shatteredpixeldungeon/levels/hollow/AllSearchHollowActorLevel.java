package com.shatteredpixel.shatteredpixeldungeon.levels.hollow;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicalSight;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.status.ActivePoint;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.status.ScoreBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NPC;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.levels.RegularLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.PrisonPainter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.hollow.crystal.*;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.hollow.gold.*;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.hollow.guard.*;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.hollow.escape.*;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.hollow.locked.*;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special.PitRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special.SpecialRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.EntranceRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.StandardRoom;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MobSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.ScoreBar;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class AllSearchHollowActorLevel extends RegularLevel {

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

        int goldRooms = Random.Int(2) + 1;
        ArrayList<Integer> goldRoomTypes = new ArrayList<>();
        for (int i = 0; i < 5; i++) goldRoomTypes.add(i);
        Random.shuffle(goldRoomTypes);
        for (int i = 0; i < goldRooms; i++) {
            switch (goldRoomTypes.get(i)) {
                case 0: initRooms.add(new GoldOneRoom()); break;
                case 1: initRooms.add(new GoldTwoRoom()); break;
                case 2: initRooms.add(new GoldThreeRoom()); break;
                case 3: initRooms.add(new GoldFourRoom()); break;
                case 4: initRooms.add(new GoldFiveRoom()); break;
            }
        }

        int crystalRooms = Random.Int(2) + 2;
        ArrayList<Integer> crystalRoomTypes = new ArrayList<>();
        for (int i = 0; i < 3; i++) crystalRoomTypes.add(i);
        Random.shuffle(crystalRoomTypes);
        for (int i = 0; i < crystalRooms; i++) {
            switch (crystalRoomTypes.get(i)) {
                case 0: initRooms.add(new CrystalOneRoom()); break;
                case 1: initRooms.add(new CrystalTwoRoom()); break;
                case 2: initRooms.add(new CrystalThreeRoom()); break;
            }
        }


        // 随机生成1-3个守卫房间（不重复）
        int guardRooms = Random.Int(3) + 1; // 生成1-3个
        ArrayList<Integer> guardRoomTypes = new ArrayList<>();
        for (int i = 0; i < 5; i++) guardRoomTypes.add(i);
        Random.shuffle(guardRoomTypes);
        for (int i = 0; i < guardRooms; i++) {
            switch (guardRoomTypes.get(i)) {
                case 0: initRooms.add(new GuardOneRoom()); break;
                case 1: initRooms.add(new GuardTwoRoom()); break;
                case 2: initRooms.add(new GuardThreeRoom()); break;
                case 3: initRooms.add(new GuardFourRoom()); break;
                case 4: initRooms.add(new GuardFiveRoom()); break;
            }
        }


        return initRooms;
    }

    @Override
    public void playLevelMusic(){
        Music.playModeBGM(Assets.Music.SEACH,true);
    }

    @Override
    protected int standardRooms(boolean forceMax) {
        if (forceMax) return 1;
        return 1 + Dungeon.depth/5 + Random.chances(new float[]{1,1,1});
    }

    @Override
    protected int specialRooms(boolean forceMax) {
        if (forceMax) return 1;
        return 2 + Dungeon.depth/5 + Random.chances(new float[]{1,1,1});
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

        Buff.affect(hero, ScoreBuff.class);
        Buff.affect(hero, ActivePoint.class).set(100, 1);

        ScoreBar.updateScoreFromBuff(hero.buff(ScoreBuff.class));
        ScoreBar.setRules(3);
        Buff.affect(hero, MagicalSight.class, MagicalSight.DURATION*200);
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

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle(bundle);
        ScoreBar.setRules(3);
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
        public boolean reset() {
            return true;
        }

        @Override
        public boolean interact(Char c) {
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

}
