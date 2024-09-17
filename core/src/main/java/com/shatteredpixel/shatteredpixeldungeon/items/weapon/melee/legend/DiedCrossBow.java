package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.legend;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Splash;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BlastParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SmokeParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.SpiritBow;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.darts.TippedDart;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MissileSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.QuickSlotButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class DiedCrossBow extends LegendWeapon {

    {
        image = ItemSpriteSheet.DIEDCROSSBOW;
        tier = 5;
        legend = 3;
        baseMin = 7;
        baseMax = 20;
        min = Lmin();
        max = Lmax();
        usesTargeting = cooldown == 0;
    }

    @Override
    protected LegendWeapon.LegendWeaponBuff passiveBuff() {
        return new DiedCrossBow.Recharge();
    }

    /**
     *
     */

    public class Recharge extends LegendWeapon.LegendWeaponBuff {
        @Override
        public boolean act() {
            if (cooldown < 0 ) {
                cooldown = 0;
            }
            if (cooldown > 0)
                cooldown--;
            updateQuickslot();
            spend(TICK);
            return true;
        }
    }

    @Override
    public String status() {

        //display the current cooldown
        if (cooldown != 0){
            return Messages.format("CD:%d", cooldown);
        } else {
            return null;
        }

    }


    @Override
    public void execute(Hero hero, String action ) {

        super.execute( hero, action );
        if (action.equals( AC_KING )) {

            if (!isEquipped(hero)){
                GLog.i( Messages.get(this, "no_equip") );
            } else if(cooldown == 0) {
                GameScene.selectCell(bomb);
            } else {
                GLog.w(Messages.get(this, "no_cooldown"));
            }
        }

    }

    @Override

    public int targetingPos(Hero user, int dst) {
        return knockArrow().targetingPos(user, dst);
    }

    private CellSelector.Listener bomb = new CellSelector.Listener() {
        @Override
        public void onSelect( Integer target ) {
            if (target != null) {
                final Ballistica shot = new Ballistica( curUser.pos, target, target);
                int cell = shot.collisionPos;

                if (Actor.findChar(target) != null) {
                    QuickSlotButton.target(Actor.findChar(target));
                } else {
                    QuickSlotButton.target(Actor.findChar(cell));
                }
                cooldown = 60-level()*2;
                knockArrow().cast(curUser, target);
                QuickSlotButton.target(Actor.findChar(target));

            }
        }
        @Override
        public String prompt() {
            return Messages.get(SpiritBow.class, "prompt");
        }
    };

    public DiedCrossBow.BombArrow knockArrow(){
        return new BombArrow();
    }




    public static class BombArrow extends TippedDart {
        public boolean sniperSpecial = false;
        public float sniperSpecialBonusDamage = 0f;
        @Override
        public Emitter emitter() {
            Emitter emitter = new Emitter();
            emitter.pos(12.5f, 3.5f);
            emitter.fillTarget = false;
            emitter.pour(SmokeParticle.SPEW, 0.05f);
            return emitter;
        }

        {
            image = ItemSpriteSheet.DISPLACING_DART;
        }

        int flurryCount = -1;
        private int targetPos;

        @Override
        public int proc(Char attacker, Char defender, int damage) {
            DiedCrossBow dartGun = hero.belongings.getItem(DiedCrossBow.class);
            damage = Random.NormalIntRange(dartGun.Lmin(), dartGun.Lmax());
            this.explodeBomb(defender.pos);

            return super.proc(attacker, defender, damage);
        }

        @Override
        public void cast(final Hero user, final int dst) {
            final int cell = throwPos( user, dst );
            targetPos = cell;


            final Char enemy = Actor.findChar( cell );

            QuickSlotButton.target(enemy);


            user.busy();

            throwSound();

            ((MissileSprite) user.sprite.parent.recycle(MissileSprite.class)).
                    reset(user.sprite,
                            cell,
                            this,
                            new Callback() {
                                @Override
                                public void call() {
                                    curUser = user;
                                    onThrow(cell);
                                    onThrow(cell+2);
                                    onThrow(cell-2);
                                }
                            });

            user.sprite.zap(cell, new Callback() {
                @Override
                public void call() {
                    flurryCount--;
                    if (flurryCount > 0){
                        cast(user, dst);
                    }
                }
            });
            super.cast(user, dst);

        }

        @Override
        public ItemSprite.Glowing glowing() {
            return new ItemSprite.Glowing(0x880000, 6f);
        }

        @Override
        protected void onThrow( int cell ) {
            Char enemy = Actor.findChar( cell );
            if (enemy == null || enemy == curUser) {
                parent = null;
                Splash.at( cell, Window.CBLACK, 12 );
                this.explodeBomb(cell);
            } else {
                if (!curUser.shoot(enemy, this)) {
                    Splash.at(cell, Window.CBLACK, 12);
                }
            }

        }

        public void explodeBomb(int cell){
            //We're blowing up, so no need for a fuse anymore.

            Sample.INSTANCE.play( Assets.Sounds.BLAST );

            ArrayList<Char> affected = new ArrayList<>();

            if (Dungeon.level.heroFOV[cell]) {
                CellEmitter.center(cell).burst(BlastParticle.FACTORY, 30);
            }

            boolean terrainAffected = false;
            for (int n : PathFinder.NEIGHBOURS8) {
                int c = cell + n;
                if (c >= 0 && c < Dungeon.level.length()) {
                    if (Dungeon.level.heroFOV[c]) {
                        CellEmitter.get(c).burst(SmokeParticle.FACTORY, 4);
                    }

                    if (Dungeon.level.flamable[c]) {
                        Dungeon.level.destroy(c);
                        GameScene.updateMap(c);
                        terrainAffected = true;
                    }

                    Char ch = Actor.findChar(c);
                    if (ch != null) {
                        affected.add(ch);
                    }
                }

                for (Char ch : affected){

                    //if they have already been killed by another bomb
                    if(!ch.isAlive()){
                        continue;
                    }

                    DiedCrossBow diedCrossBow =  hero.belongings.getItem(DiedCrossBow.class);
                    int dmg = Random.NormalIntRange(diedCrossBow.Lmin(),diedCrossBow.Lmin());

                    //those not at the center of the blast take less damage
                    if (ch.pos != cell){
                        dmg = Math.round(dmg*0.67f);
                    }

                    dmg -= ch.drRoll();

                    if (dmg > 0) {
                        ch.damage(dmg, this);
                    }

                    if (ch == hero && !ch.isAlive()) {
                        //Badges.BOMB();
                        Dungeon.fail( getClass() );
                        GLog.n( Messages.get(DiedCrossBow.class, "ondeath") );
                    }
                }

                if (terrainAffected) {
                    Dungeon.observe();
                }
            }
        }

    }

    @Override
    public int min(int lvl) {
        return 4 + lvl * (tier + 1);   //scaling unchanged
    }


    @Override
    public int max(int lvl) {
        return 25 + lvl * (tier + 5);   //scaling unchanged
    }

}