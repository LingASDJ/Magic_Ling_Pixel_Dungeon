package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.depth;
import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;
import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.level;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.WALL;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.BruteBot;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.DM100;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.MolotovHuntsman;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Salamander;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Scorpio;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NPC;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.GoldenKey;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfLiquidFlame;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.AlarmTrap;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MobSprite;
import com.watabou.noosa.Game;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Point;

public class SkeletonFishRoom extends SpecialRoom {
    @Override
    public int minWidth() {
        return 13;
    }
    @Override
    public int minHeight() {
        return 13;
    }
    @Override
    public int maxWidth() {
        return 13;
    }
    @Override
    public int maxHeight() {
        return 13;
    }

    @Override
    public boolean canConnect(Point p) {
        if (!super.canConnect(p)){
            return false;
        }
        if (Math.abs(p.x - (right - (width()-1)/4f)) < 1f){
            return true;
        }
        return Math.abs(p.y - (bottom - (height() - 1) / 4f)) < 1f;
    }

    @Override
    public boolean canPlaceTrap(Point p) {
        return false;
    }

    private static final int[] pre_map = {
            0,5,5,0,0,144,20,80,0,0,144,
            5,144,144,144,144,144,0,48,144,144,144,
            5,144,5,5,5,48,48,92,48,48,48,
            0,144,5,0,0,0,0,0,0,20,48,
            0,144,5,0,48,92,92,92,80,144,48,
            144,144,80,0,92,48,48,92,92,92,48,
            20,0,80,0,92,48,48,48,48,115,48,
            80,80,0,0,92,92,48,48,92,0,48,
            0,144,80,0,80,92,48,92,0,0,48,
            0,144,48,20,144,92,115,0,0,76,48,
            144,144,48,48,48,48,48,48,48,48,48,
    };

    private int codeToTerrain(int code){
        switch (code){
            case 5:
                return Terrain.WATER;
            case 20: case 76:
                return Terrain.PEDESTAL;
            case 48: case 80:
                return WALL;
            case 92:
                return Terrain.BOOKSHELF;
            case 115:
                return Terrain.SECRET_DOOR;
            case 144:
                return Terrain.CHASM;
            default:
                return Terrain.EMPTY;
        }
    }

    @Override
    public void paint(Level level) {

        Painter.fill(level,this, 0, WALL);

        for (int i = left + 1; i <= right-1; i++) {
            for (int j = top + 1; j <= bottom-1; j++) {
                int dx = i - (left + 1);
                int dy = j - (top + 1);
                int index = dy * (minWidth()-2) + dx;

                if(index >= 0 && index < pre_map.length){
                    set(level, i, j, codeToTerrain(pre_map[index]));
                } else {
                    set(level, i, j, Terrain.EMPTY);
                }
            }
        }

        entrance().set(Door.Type.WALL);

        int centerX = left + width() / 2;
        int centerY = top + height() /2;
        Point xpos = new Point(centerX-4, centerY+4);
        int RPos = left + right - xpos.x + xpos.y * level.width();

        BlueStoneDoor ncx = new BlueStoneDoor();
        ncx.pos = RPos;
        level.mobs.add(ncx);
        level.map[RPos] = Terrain.TRAP;
        level.setTrap(new AlarmTrap(), RPos);

        level.drop( new PotionOfLiquidFlame(), RPos-1 );
        level.drop( new GoldenKey(depth), RPos-2 );

        Point apos = new Point(centerX+5, centerY+1);
        int aPos = left + right - apos.x + apos.y * level.width();
        level.drop(Generator.random(), aPos).type = Heap.Type.CHEST;

        Point bpos = new Point(centerX+2, centerY+4);
        int bPos = left + right - bpos.x + bpos.y * level.width();
        level.drop(Generator.random(), bPos).type = Heap.Type.TOMB;

        Point cpos = new Point(centerX-4, centerY-2);
        int cPos = left + right - cpos.x + cpos.y * level.width();
        level.drop(Generator.random(), cPos).type = Heap.Type.LOCKED_CHEST;

        Point dpos = new Point(centerX-1, centerY-5);
        int dPos = left + right - dpos.x + dpos.y * level.width();
        level.drop(Generator.random(), dPos).type = Heap.Type.SKELETON;

        Point zpos = new Point(centerX+5, centerY+3);
        int zPos = left + right - zpos.x + zpos.y * level.width();

        Point gpos = new Point(centerX+3, centerY-5);
        int gPos = left + right - gpos.x + gpos.y * level.width();

        Point ppos = new Point(centerX-4, centerY-5);
        int pPos = left + right - ppos.x + ppos.y * level.width();

        int[] MBTPOS = new int[]{
               zPos,gPos,pPos
        };

        for (int i : MBTPOS) {
            Mob n = new Salamander();
            if (depth >= 20) {
                n = new Scorpio();
            } else if (depth >= 15) {
                n = new BruteBot();
            } else if (depth >= 10) {
                n = new MolotovHuntsman();
            } else if (depth >= 6) {
                n = new DM100();
            }
            n.pos = i;
            level.mobs.add(n);
        }
    }

    private void set(Level level, int x, int y, int value) {
        level.map[x + y * level.width()] = value;
    }

    public static class BlueAltStoneDoor extends NPC {

        {
            spriteClass = BlueStoneDoorSprite.class;
            properties.add(Property.IMMOVABLE);
        }

        @Override
        public boolean interact(Char c) {
            for (Mob mob : level.mobs.toArray(new Mob[0])){
                if (mob instanceof BlueStoneDoor){
                    ScrollOfTeleportation.appear(c, mob.pos);
                    //传送目标区域
                    hero.interrupt();
                    Dungeon.observe();
                    GameScene.updateFog();
                }
            }
            return true;
        }

        @Override
        public int defenseSkill(Char enemy) {
            return INFINITE_EVASION;
        }

        @Override
        public void damage(int dmg, Object src, DamageType type) {
        }

        @Override
        public boolean add(Buff buff) {
            return false;
        }

        @Override
        public boolean reset() {
            return true;
        }

    }

    public static class BlueStoneDoor extends NPC {

        {
            spriteClass = BlueStoneDoorSprite.class;
            properties.add(Property.IMMOVABLE);
        }

        @Override
        public int defenseSkill(Char enemy) {
            return INFINITE_EVASION;
        }

        @Override
        public boolean interact(Char c) {
            for (Mob mob : level.mobs.toArray(new Mob[0])){
                if (mob instanceof BlueAltStoneDoor){
                    ScrollOfTeleportation.appear(c, mob.pos);
                    //传送目标区域
                    hero.interrupt();
                    Dungeon.observe();
                    GameScene.updateFog();
                }
            }
            return true;
        }

        @Override
        public void damage(int dmg, Object src, DamageType type) {
        }

        @Override
        public boolean reset() {
            return true;
        }

    }

    public static class BlueStoneDoorSprite extends MobSprite {

        private Animation charging;
        private Emitter chargeParticles;

        public BlueStoneDoorSprite(){
            texture( Assets.Sprites.BUE_SENTRY );

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
            chargeParticles.pour(MagicMissile.MagicParticle.ATTRACTING, 0.04f);

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
