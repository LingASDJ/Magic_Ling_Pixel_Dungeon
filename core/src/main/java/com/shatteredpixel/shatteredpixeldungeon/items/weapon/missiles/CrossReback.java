package com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.BlobEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SparkParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MagesStaff;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MissileSprite;
import com.watabou.noosa.tweeners.AlphaTweener;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;

public class CrossReback extends MissileWeapon {

    {
        image = ItemSpriteSheet.SZJ_REBACK;
        hitSound = Assets.Sounds.HIT_CRUSH;
        hitSoundPitch = 1f;

        tier = 2;
        sticky = false;
        baseUses = 10;
    }

    public int STRReq(int lvl){
        return STRReq(tier, lvl); //1 less str than normal for their tier
    }

    @Override
    public int min(int level) {
        return 4 + level;
    }

    @Override
    public String desc() {
        if(Dungeon.hero != null){
            return Messages.get(this, "desc", 10+5*level(),4 + Dungeon.hero.lvl/5);
        } else {
            return Messages.get(this, "desc", 10+5*level(),4);
        }

    }


    public int max(int level) {
        return 12 + level * 3;
    }
    
    boolean circleBackhit = false;

    @Override
    protected float adjacentAccFactor(Char owner, Char target) {
        if (circleBackhit){
            circleBackhit = false;
            return 1.5f;
        }
        return super.adjacentAccFactor(owner, target);
    }

    @Override
    protected void rangedHit(Char enemy, int cell) {
        decrementDurability();
        if (durability > 0){
            Buff.append(Dungeon.hero, CircleBack.class).setup(this, cell, Dungeon.hero.pos, Dungeon.depth, Dungeon.branch);
            GameScene.add( Blob.seed( cell, 10+5*level(), Electricity.class ) );
        }
    }

    @Override
    protected void rangedMiss(int cell) {
        parent = null;
        Buff.append(Dungeon.hero, CircleBack.class).setup(this, cell, Dungeon.hero.pos, Dungeon.depth, Dungeon.branch);
    }

    public static class Electricity extends Blob {

        {
            //acts after mobs, to give them a chance to resist paralysis
            actPriority = MOB_PRIO - 1;
        }

        private boolean[] water;

        @Override
        protected void evolve() {

            water = Dungeon.level.water;
            int cell;

            //spread first..
            for (int i = area.left-1; i <= area.right; i++) {
                for (int j = area.top-1; j <= area.bottom; j++) {
                    cell = i + j*Dungeon.level.width();

                    if (cur[cell] > 0) {
                        spreadFromCell(cell, cur[cell]);
                    }
                }
            }

            //..then decrement/shock
            for (int i = area.left-1; i <= area.right; i++) {
                for (int j = area.top-1; j <= area.bottom; j++) {
                    cell = i + j*Dungeon.level.width();
                    if (cur[cell] > 0) {
                        Char ch = Actor.findChar( cell );
                        if (ch != null && !ch.isImmune(this.getClass())) {
                            if (cur[cell] % 2 == 1) {
                                if(ch.alignment == Char.Alignment.ENEMY){
                                    ch.damage(4 + Dungeon.hero.lvl/5, this, Char.DamageType.Element);
                                }
                            }
                        }

                        Heap h = Dungeon.level.heaps.get( cell );
                        if (h != null){
                            Item toShock = h.peek();
                            if (toShock instanceof Wand){
                                ((Wand) toShock).gainCharge(0.333f);
                            } else if (toShock instanceof MagesStaff){
                                ((MagesStaff) toShock).gainCharge(0.333f);
                            }
                        }

                        off[cell] = cur[cell] - 1;
                        volume += off[cell];
                    } else {
                        off[cell] = 0;
                    }
                }
            }

        }

        private void spreadFromCell( int cell, int power ){
            if (cur[cell] == 0) {
                area.union(cell % Dungeon.level.width(), cell / Dungeon.level.width());
            }
            cur[cell] = Math.max(cur[cell], power);

            for (int c : PathFinder.NEIGHBOURS4){
                if (water[cell + c] && cur[cell + c] < power){
                    spreadFromCell(cell + c, power);
                }
            }
        }

        @Override
        public void use( BlobEmitter emitter ) {
            super.use( emitter );
            emitter.start( SparkParticle.FACTORY, 0.05f, 0 );
        }

        @Override
        public String tileDesc() {
            return Messages.get(this, "desc");
        }

    }

    public static class CircleBack extends Buff {

        {
            revivePersists = true;
        }

        public CrossReback boomerang;
        private int thrownPos;
        private int returnPos;
        private int returnDepth;
        private int returnBranch;

        private int left;

        public void setup( CrossReback boomerang, int thrownPos, int returnPos, int returnDepth, int returnBranch){
            this.boomerang = boomerang;
            this.thrownPos = thrownPos;
            this.returnPos = returnPos;
            this.returnDepth = returnDepth;
            this.returnBranch = returnBranch;
            left = 3;
        }

        public int returnPos(){
            return returnPos;
        }

        public MissileWeapon cancel(){
            detach();
            return boomerang;
        }

        @Override
        public boolean act() {
            if (returnDepth == Dungeon.depth && returnBranch == Dungeon.branch){
                left--;
                if (left <= 0){
                    final Char returnTarget = Actor.findChar(returnPos);
                    final Char target = this.target;
                    MissileSprite visual = ((MissileSprite) Dungeon.hero.sprite.parent.recycle(MissileSprite.class));
                    visual.reset( thrownPos,
                            returnPos,
                            boomerang,
                            new Callback() {
                                @Override
                                public void call() {
                                    if (returnTarget == target){
                                        if (target instanceof Hero && boomerang.doPickUp((Hero) target)) {
                                            //grabbing the boomerang takes no time
                                            ((Hero) target).spend(-TIME_TO_PICK_UP);
                                        } else {
                                            Dungeon.level.drop(boomerang, returnPos).sprite.drop();
                                        }

                                    } else if (returnTarget != null){
                                        boomerang.circleBackhit = true;
                                        if (((Hero)target).shoot( returnTarget, boomerang )) {
                                            boomerang.decrementDurability();
                                        }
                                        if (boomerang.durability > 0) {
                                            Dungeon.level.drop(boomerang, returnPos).sprite.drop();
                                        }

                                    } else {
                                        Dungeon.level.drop(boomerang, returnPos).sprite.drop();
                                    }
                                    CircleBack.this.next();
                                }
                            });
                    visual.alpha(0f);
                    float duration = Dungeon.level.trueDistance(thrownPos, returnPos) / 20f;
                    target.sprite.parent.add(new AlphaTweener(visual, 1f, duration));
                    detach();
                    return false;
                }
            }
            spend( TICK );
            return true;
        }

        private static final String BOOMERANG = "boomerang";
        private static final String THROWN_POS = "thrown_pos";
        private static final String RETURN_POS = "return_pos";
        private static final String RETURN_DEPTH = "return_depth";
        private static final String RETURN_BRANCH = "return_branch";

        @Override
        public void storeInBundle(Bundle bundle) {
            super.storeInBundle(bundle);
            bundle.put(BOOMERANG, boomerang);
            bundle.put(THROWN_POS, thrownPos);
            bundle.put(RETURN_POS, returnPos);
            bundle.put(RETURN_DEPTH, returnDepth);
            bundle.put(RETURN_BRANCH, returnBranch);
        }

        @Override
        public void restoreFromBundle(Bundle bundle) {
            super.restoreFromBundle(bundle);
            boomerang = (CrossReback) bundle.get(BOOMERANG);
            thrownPos = bundle.getInt(THROWN_POS);
            returnPos = bundle.getInt(RETURN_POS);
            returnDepth = bundle.getInt(RETURN_DEPTH);
            returnBranch = bundle.contains(RETURN_BRANCH) ? bundle.getInt(RETURN_BRANCH) : 0;
        }
    }

}

