package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.legend;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
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
import java.util.Collection;
import java.util.List;

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
    public int iceCoinValue() {
        if (Badges.isUnlocked(Badges.Badge.NYZ_SHOP)){
            return (int) ((175 + tier*25) * 0.9f);
        }
        return 175 + tier*25;
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
                // Main arrow towards the primary target
                Ballistica mainBall = new Ballistica(curUser.pos, target, Ballistica.PROJECTILE);
                if (Char.findChar(mainBall.collisionPos) == null || Char.findChar(mainBall.collisionPos).alignment != Char.Alignment.ENEMY) {
                    knockArrow().cast(curUser, target);
                } else {
                    // Proceed with primary target logic
                    if (Char.findChar(mainBall.collisionPos) == Char.findChar(target)) {
                        Collection<Mob> mobs = Dungeon.level.mobs;
                        if (!mobs.isEmpty()) {
                            // Filter mobs within hero's FOV
                            List<Mob> visibleMobs = new ArrayList<>();
                            for (Mob mob : mobs) {
                                if (Dungeon.level.heroFOV[mob.pos] && mob.alignment == Char.Alignment.ENEMY) {
                                    visibleMobs.add(mob);
                                }
                            }
                            if (!visibleMobs.isEmpty() && !(visibleMobs.size() == 1 && visibleMobs.get(0).equals(Char.findChar(target)))) {
                                // Select the first secondary target (different from primary)
                                Mob secondaryTarget1;
                                int randomIndex1;
                                do {
                                    randomIndex1 = (int) (Math.random() * visibleMobs.size());
                                    secondaryTarget1 = visibleMobs.get(randomIndex1);
                                } while (secondaryTarget1.equals(Char.findChar(target)));

                                // Launch the first secondary arrow
                                Ballistica secondaryBall1 = new Ballistica(curUser.pos, secondaryTarget1.pos, Ballistica.PROJECTILE);
                                if (secondaryBall1.collisionPos.equals(secondaryTarget1.pos)) {
                                    knockArrow().cast(curUser, secondaryTarget1.pos);
                                }

                                // Select the second secondary target (different from primary and first secondary)
                                List<Mob> remainingMobs = new ArrayList<>(visibleMobs);
                                remainingMobs.remove(secondaryTarget1); // Remove the first secondary target
                                if (!remainingMobs.isEmpty()) {
                                    Mob secondaryTarget2 = remainingMobs.get((int) (Math.random() * remainingMobs.size()));

                                    // Launch the second secondary arrow
                                    Ballistica secondaryBall2 = new Ballistica(curUser.pos, secondaryTarget2.pos, Ballistica.PROJECTILE);
                                    if (secondaryBall2.collisionPos.equals(secondaryTarget2.pos)) {
                                        knockArrow().cast(curUser, secondaryTarget2.pos);
                                    }
                                }
                            }
                        }
                        // Always shoot the main arrow at the primary target
                        knockArrow().cast(curUser, target);
                    }
                }
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
                                    }
                                });

                user.sprite.zap(cell, () -> {
                    flurryCount--;
                    if (flurryCount > 0){
                        cast(user, dst);
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
        return 4 + lvl;   //scaling unchanged
    }
    //你要是喜欢写成lvl*1也行

    @Override
    public int max(int lvl) {
        return 25 + lvl * 6;   //scaling unchanged
    }
    //最高成长6，写成lvl*6和tm写成lvl*(tier+1)是tm一样的，别被Evan棍进去了，你清醒一点，不要当谜语人了！！！！！
}
