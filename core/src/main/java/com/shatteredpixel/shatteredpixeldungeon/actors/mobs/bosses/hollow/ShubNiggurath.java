package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.hollow;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Boss;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ElementalBuff.BaseBuff.ScaryBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LockedFloor;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.DM100;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.FloatingText;
import com.shatteredpixel.shatteredpixeldungeon.effects.Pushing;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.RainbowParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SnowParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.Trap;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MissileSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ShubNiggurathSprite;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.BArray;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.PointF;

import java.util.ArrayList;

public class ShubNiggurath extends Boss {

    public int summonIndex = 0;

    {
        initProperty();
        initBaseStatus(0, 0, 0, 0, 700, 0, 0);
        initStatus(20);
        spriteClass = ShubNiggurathSprite.class;

        properties.add(Property.BOSS);
        properties.add(Property.ACIDIC);

        noDropIceCoin = true;
    }

    @Override
    public void damage(int dmg, Object src) {
        if(src == Trap.class){
            return;
        }
        BossHealthBar.assignBoss(this);
        LockedFloor lock = hero.buff(LockedFloor.class);
        if (lock != null) {
            int multiple = 1;
            lock.addTime(dmg*multiple);
        }
        super.damage(dmg, src);
    }

    public static class ShubNiggurathClone extends ShubNiggurath {
        @Override
        protected boolean act() {
            if (buff(HeartMagicDamage.class) == null) {
                Buff.affect(this, HeartMagicDamage.class, 10f);
                if(enemy != null){
                    if(Dungeon.level.distance(pos, enemy.pos) <= 5){
                        for (Buff buff : enemy.buffs()) {
                            if (buff instanceof ScaryBuff) {
                                ((ScaryBuff) buff).damgeScary(8);
                            } else {
                                Buff.affect(enemy, ScaryBuff.class).set((100), 5);
                            }
                        }
                    }
                }
            }
            return super.act();
        }
    }

    @Override
    protected boolean act() {
        alerted = false;
        state = PASSIVE;

        if(getClass() == ShubNiggurath.class){
            if(summonIndex>=8){
                for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
                    if (mob instanceof ShubNiggurathClone && summonIndex>=8) {
                        mob.die(true);
                    }
                }
                int finalTargetCell = hero.pos;
                ShubNiggurath.DiedGalaxy.BombItem item = new ShubNiggurath.DiedGalaxy.BombItem();
                sprite.zap(finalTargetCell);
                ((MissileSprite) this.sprite.parent.recycle(MissileSprite.class)).
                        reset(this.sprite,
                                finalTargetCell,
                                item,
                                new Callback() {
                                    @Override
                                    public void call() {
                                        item.onThrow(finalTargetCell);
                                        next();
                                    }
                                });
                summonIndex = 0;
            }

        }

        if (buff(HeartMagicDamage.class) == null && (getClass() == ShubNiggurath.class) && buff(DiedGalaxy.class)== null) {
            Buff.affect(this, HeartMagicDamage.class, 10f);
            if(enemy != null){

                ShubNiggurathClone clone = new ShubNiggurathClone();
                clone.HT = HP;
                HT = HP;
                summonIndex++;
                clone.pos = Dungeon.level.randomDestination(clone);
                GameScene.add(clone, 1f);
                Actor.add( new Pushing( clone, pos, clone.pos ) );
                Dungeon.level.occupyCell(clone);

                if(Dungeon.level.distance(pos, enemy.pos) <= 5){
                    for (Buff buff : enemy.buffs()) {
                        if (buff instanceof ScaryBuff) {
                            ((ScaryBuff) buff).damgeScary(8);
                        } else {
                            Buff.affect(enemy, ScaryBuff.class).set((100), 5);
                        }
                    }
                }
            }
        }

        return super.act();
    }

    int generation	= 0;

    private static final String GENERATION	= "generation";
    private static final String SUMMON_INDEX	= "summon_index";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( GENERATION, generation );
        bundle.put( SUMMON_INDEX, summonIndex );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        generation = bundle.getInt( GENERATION );
        summonIndex = bundle.getInt( SUMMON_INDEX );
        if (generation > 0) EXP = 0;
    }

    @Override
    public void die( Object cause ) {
        super.die(cause);
        if(getClass() == ShubNiggurath.class) {
            for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
                if (mob instanceof Morphs) {
                    ((Morphs) mob).phase += 0.30f;
                }
                if(mob instanceof ShubNiggurathClone){
                    mob.die(true);
                }
            }
        }
    }

    public static class DiedGalaxy extends Buff {

        public int bombPos = -1;
        private int timer = 10;

        private ArrayList<Emitter> smokeEmitters = new ArrayList<>();

        @Override
        public boolean act() {

            if (smokeEmitters.isEmpty()){
                fx(true);
            }

            PointF p = DungeonTilemap.raisedTileCenterToWorld(bombPos);
            if (timer == 10) {
                FloatingText.show(p.x, p.y, bombPos, "10", CharSprite.POSITIVE);
            } else if (timer == 5){
                FloatingText.show(p.x, p.y, bombPos, "5", CharSprite.WARNING);
            } else if (timer == 3){
                FloatingText.show(p.x, p.y, bombPos, "3", CharSprite.NEGATIVE);
            } else if(timer <= 0) {
                FloatingText.show(p.x, p.y, bombPos, "Bomb!!!", CharSprite.NEGATIVE);
                PathFinder.buildDistanceMap( bombPos, BArray.not( Dungeon.level.solid, null ), 7 );
                for (int cell = 0; cell < PathFinder.distance.length; cell++) {
                    if (PathFinder.distance[cell] < Integer.MAX_VALUE) {
                        Char ch = Actor.findChar(cell);
                        if (ch != null && !(ch instanceof ShubNiggurath)) {
                            int dmg = ch.HT / 4;
                            ch.damage(dmg, new DM100.LightningBolt());
                        }
                    }
                }

                for (Heap heap : Dungeon.level.heaps.valueList()) {
                    for (Item item : heap.items) {
                        if((item instanceof BombItem)){
                            heap.destroy();
                        }
                    }
                }
                Sample.INSTANCE.play(Assets.Sounds.BLAST);
                detach();

                return true;
            }

            timer--;
            spend(TICK);
            return true;
        }

        @Override
        public void fx(boolean on) {

            if (on && bombPos != -1){
                PathFinder.buildDistanceMap( bombPos, BArray.not( Dungeon.level.solid, null ), 7 );
                for (int i = 0; i < PathFinder.distance.length; i++) {
                    if (PathFinder.distance[i] < Integer.MAX_VALUE) {
                        Emitter e = CellEmitter.get(i);
                        e.pour(SnowParticle.FACTORY, 0.25f );
                        smokeEmitters.add(e);
                    }
                }
            } else if (!on) {
                for (Emitter e : smokeEmitters){
                    e.burst(SnowParticle.FACTORY, 4);
                }
            }
        }

        private static final String BOMB_POS = "bomb_pos";

        private static final String TIMER = "timer";

        @Override
        public void storeInBundle(Bundle bundle) {
            super.storeInBundle(bundle);
            bundle.put( BOMB_POS, bombPos );
            bundle.put( TIMER, timer );
        }

        @Override
        public void restoreFromBundle(Bundle bundle) {
            super.restoreFromBundle(bundle);
            bombPos = bundle.getInt( BOMB_POS );
            timer = bundle.getInt( TIMER );
        }

        public static class BombItem extends Item {
            private float time;
            {
                dropsDownHeap = true;
                unique = true;

                image = ItemSpriteSheet.RECLAIM_TRAP;
            }

            @Override
            public ItemSprite.Glowing glowing() {
                return new ItemSprite.Glowing(Window.GDX_COLOR, 6f);
            }

            @Override
            public boolean doPickUp(Hero hero, int pos) {
                GLog.w( Messages.get(this, "cant_pickup") );
                return false;
            }

            @Override
            protected void onThrow(int cell) {
                super.onThrow(cell);

                for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])){
                    if (mob instanceof ShubNiggurath) {
                        Buff.append(mob, ShubNiggurath.DiedGalaxy.class).bombPos = hero.pos;
                    }
                }
            }

            @Override
            public Emitter emitter() {
                Emitter emitter = new Emitter();
                emitter.pos(7.5f, 3.5f);
                emitter.fillTarget = false;
                emitter.pour(RainbowParticle.BURST, 0.05f);
                return emitter;
            }
        }
    }

    public static class HeartMagicDamage extends FlavourBuff {
        {
            type = buffType.POSITIVE;
        }
    }

}
