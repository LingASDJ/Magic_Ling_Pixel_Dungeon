package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard;

import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.EMPTY;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.EMPTY_SP;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.PEDESTAL;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.WATER;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AscensionChallenge;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FireImbue;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FrostImbue;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Fury;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.HaloFireImBlue;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicImmune;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.notsync.ClearElemtGuard;
import com.shatteredpixel.shatteredpixeldungeon.effects.Beam;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special.SpecialRoom;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MobSprite;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

public class BloodCrystalRoom extends SpecialRoom {

	@Override
	public boolean canMerge(Level l, Room other, Point p, int mergeTerrain) {
		return false;
	}

    @Override
    public int minWidth() {
        return 17;
    }

    @Override
    public int minHeight() {
        return 17;
    }

    @Override
    public int maxWidth() {
        return 17;
    }

    @Override
    public int maxHeight() {
        return 17;
    }

    @Override
    public void paint(Level level) {
        Painter.fill( level, this, Terrain.WALL );

        Painter.fillEllipse( level, this, 1 , EMPTY );

        for (Door door : connected.values()) {
            door.set( Door.Type.REGULAR);
            if (door.x == left || door.x == right){
                Painter.drawInside(level, this, door, width()/2, WATER);
            } else {
                Painter.drawInside(level, this, door, height()/2, WATER);
            }
        }
        Painter.fillEllipse( level, this, 3, Terrain.CHASM);

        int centerX = left + width() / 2;
        int centerY = top + height() / 2;

        Painter.set(level, centerX, centerY, PEDESTAL);
        Painter.drawRectangle(level, new Point(centerX, centerY),3,3, WATER,false,0);
        Painter.drawRectangle(level, new Point(centerX, centerY),5,5, EMPTY_SP,true,Terrain.CHASM);

        Painter.drawCrossWithOuterFill(level, new Point(centerX, centerY),4, WATER,false,0);

        Painter.drawHorizontalLine(level, new Point(centerX-6, centerY),3,EMPTY_SP);

        Painter.drawHorizontalLine(level, new Point(centerX+3, centerY),3,EMPTY_SP);

        Painter.drawVerticalLine(level, new Point(centerX, centerY+3),3,EMPTY_SP);
        Painter.drawVerticalLine(level, new Point(centerX, centerY-6),3,EMPTY_SP);

        Point e = new Point(centerX, centerY);
        int LXDPos = (left + right) - e.x + e.y * level.width();

        BloodCRT n = new BloodCRT();
        n.pos = LXDPos;
        level.mobs.add(n);
    }

    public static class BloodCRT extends Mob {

        {
            spriteClass = SentrySprite.class;
            HP = HT = 50 + Dungeon.depth/5 * 2;
            properties.add(Property.IMMOVABLE);
        }

        @Override
        protected boolean act() {
            alerted = false;
            for (Mob boss : Dungeon.level.mobs.toArray(new Mob[0])) {
                if (boss.alignment == Alignment.ENEMY && !(boss instanceof ClearElemtGuard || boss instanceof BloodCRT)) {
                   switch (Random.Int(6)){
                       default:
                       case 0:
                           if(boss.buff(Fury.class) == null) {
                               Buff.affect(boss, Fury.class).set((16), 1);
                           }
                           break;
                       case 1:
                           if(boss.buff(Barrier.class) == null){
                               Buff.affect(boss, Barrier.class).setShield(20);
                           }
                           break;
                       case 2:
                           Buff.affect(boss, HaloFireImBlue.class).set( 10f );
                           break;
                       case 3:
                           Buff.affect(boss, MagicImmune.class, 10f);
                           break;
                       case 4:
                           Buff.affect(boss, FireImbue.class).set(15f);
                           break;
                       case 5:
                           Buff.affect(boss, FrostImbue.class, 15f);
                           break;
                   }
                }
            }
            spend(30f + 5f * Dungeon.depth/5);
            state = PASSIVE;
            return super.act();
        }

        @Override
        public String description() {
            String desc = super.description();

            desc += "\n\n" + Messages.get(this, "desc_info",(int)(30f + 5f * Dungeon.depth/5));

            return desc;
        }

        @Override
        public void damage(int dmg, Object src) {
            float scaleFactor = AscensionChallenge.statModifier(this);
            int scaledDmg = Math.round(dmg/scaleFactor);
            if (scaledDmg >= 5){
                //takes 5/6/7/8/9/10 dmg at 5/7/10/14/19/25 incoming dmg
                scaledDmg = 4 + (int)(Math.sqrt(8*(scaledDmg - 4) + 1) - 1)/2;
            }
            dmg = (int)(scaledDmg*AscensionChallenge.statModifier(this));
            super.damage(dmg, src);
        }

    }

    public static class SentrySprite extends MobSprite {

        private Animation charging;
        private Emitter chargeParticles;

        public SentrySprite(){
            texture( Assets.Sprites.RED_SENTRY );

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
        public void zap( int pos ) {
            idle();
            flash();
            emitter().burst(MagicMissile.WardParticle.UP, 2);
            if (Actor.findChar(pos) != null){
                parent.add(new Beam.DeathRay(center(), Actor.findChar(pos).sprite.center()));
            } else {
                parent.add(new Beam.DeathRay(center(), DungeonTilemap.raisedTileCenterToWorld(pos)));
            }
            ((BloodCRT)ch).onZapComplete();
        }

        @Override
        public void link(Char ch) {
            super.link(ch);

            chargeParticles = centerEmitter();
            chargeParticles.autoKill = false;
            chargeParticles.pour(MagicMissile.MagicParticle.ATTRACTING, 0.05f);
            chargeParticles.on = false;
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

        @Override
        public void update() {
            super.update();
            if (chargeParticles != null){
                chargeParticles.pos( center() );
                chargeParticles.visible = visible;
            }

            if (!paused){
                if (Float.isNaN(baseY)) baseY = y;
                y = baseY + (float) Math.sin(Game.timeTotal);
                shadowOffset = 0.25f - 0.8f*(float) Math.sin(Game.timeTotal);
            }
        }

    }

}

