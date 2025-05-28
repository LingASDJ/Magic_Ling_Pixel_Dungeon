package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.spical;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;
import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.level;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.PaswordBadges;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Fire;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Adrenaline;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bleeding;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.HalomethaneBurning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LockedFloor;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.CrystalKey;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.IronKey;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfLiquidFlameX;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfDragonKingBreath;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SkyDeadSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Camera;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class SkyDead extends Mob {

    public boolean firstBurning = false;
    public int scytheCooldown;
    public boolean adrenalineSurge = false;

    {
        spriteClass = SkyDeadSprite.class;

        HP = HT = 120;
        defenseSkill = 10;

        scytheCooldown = Random.IntRange(7,9);

        EXP  = 20;

        viewDistance = 8;

        flying = true;

        maxLvl = 18;

        properties.add(Property.BOSS);
        properties.add(Property.DEMONIC);
        properties.add(Property.IMMOVABLE);
        immunities.add(Burning.class);

        immunities.add(ScrollOfTeleportation.class);
    }

    private static final String SCYTHE_CD = "scythe_cd";
    private static final String ADRENALINE = "adrenaline";
    private static final String FIRST_BURNING = "first_burning";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(SCYTHE_CD, scytheCooldown);
        bundle.put(ADRENALINE, adrenalineSurge);
        bundle.put(FIRST_BURNING, firstBurning);
    }

    @Override
    public boolean act() {

        if(level.distance(pos,hero.pos)>=11 && level.locked){
            ScrollOfTeleportation.appear(hero, pos);
            yell(Messages.get(this, "tp"));
        }

        if(enemy != null && !level.locked){
            if(Dungeon.level.distance(pos,enemy.pos)<=4){
                notice();
            }
        } else if(Dungeon.level.distance(pos,hero.pos)<=4){
           for (Buff buff : hero.buffs()){
               if(buff instanceof Invisibility){
                   buff.detach();
               }
           }
        }

        return super.act();
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        scytheCooldown = bundle.getInt(SCYTHE_CD);
        adrenalineSurge = bundle.getBoolean(ADRENALINE);
        firstBurning = bundle.getBoolean(FIRST_BURNING);

        if (state != SLEEPING) BossHealthBar.assignBoss(this);
        if ((HP*2 <= HT)) BossHealthBar.bleed(true);
    }

    @Override
    public void notice() {
        super.notice();
        if (!BossHealthBar.isAssigned()) {
            BossHealthBar.assignBoss(this);
            Dungeon.level.seal();
            Camera.main.shake(1f,3f);
            GameScene.bossReady();
            yell(Messages.get(this, "notice"));
        }
    }

    private void zap() {
        spend( 1f );
        int dmg = damageRoll();
        if(enemy == null){
            return;
        }

        if (enemy == Dungeon.hero && !enemy.isAlive()) {
            Dungeon.fail( getClass() );
            GLog.n( Messages.capitalize(Messages.get(Char.class, "kill", name())) );
        }

        if(!firstBurning && enemy != null){
            for (int i : PathFinder.CIRCLE8) {
                int cell = enemy.pos + i;
                GameScene.add(Blob.seed(cell, 6, Fire.class));
            }
            firstBurning = true;
            adrenalineSurge = true;
            Buff.affect(this, Adrenaline.class, 20f);
        } else if (scytheCooldown <= 0 && enemy != null && level.distance(pos,enemy.pos) ==2){
            Buff.affect(enemy, Burning.class).reignite(enemy);
            Buff.affect(enemy, Bleeding.class).set((float) dmg /10);
            scytheCooldown = Random.IntRange(7,9);
            if(enemy.buff(Burning.class) != null){
                Buff.affect(enemy, HalomethaneBurning.class).reignite(enemy);
            }
        }

    }

    public void onZapComplete() {
        zap();
        next();
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(6,12);
    }

    @Override
    public void move(int step) {
        super.move(step);
        scytheCooldown--;
    }


    @Override
    public void damage(int damage, Object src){
        LockedFloor lock = hero.buff(LockedFloor.class);
        if (lock != null) lock.addTime(damage*2);
        int dmg = Math.min(damage, 15);
        super.damage(dmg, src);
    }

    @Override
    public int attackSkill( Char target ) {
        if (Dungeon.level.adjacent(pos, target.pos)){
            return 10;
        } else {
            return 20;
        }
    }

    @Override
    protected boolean canAttack(Char enemy) {
        // 第一次触发火焰
        if (scytheCooldown <= 0 && Dungeon.level.distance(pos, enemy.pos) == 2){
            return new Ballistica(pos, enemy.pos, Ballistica.PROJECTILE).collisionPos == enemy.pos;
        }

        return super.canAttack(enemy);
    }

    @Override
    public void die( Object cause ) {

        super.die( cause );
        Statistics.skydeadFight = true;
        Dungeon.level.unseal();

        GameScene.bossSlain();

        //Dungeon.level.drop( Generator.random( Generator.Category.WAND), pos) .sprite.drop();

        if(Dungeon.branch == 2) {
            Dungeon.level.drop(new IronKey(Dungeon.depth), hero.pos).sprite.drop();
            Dungeon.level.drop(new CrystalKey(Dungeon.depth), pos).sprite.drop();
            Dungeon.level.drop(new PotionOfLiquidFlameX(), pos).sprite.drop();

            if(Random.Int(4) == 0){
                Dungeon.level.drop(new PotionOfDragonKingBreath(), pos).sprite.drop();
            }
        }

        //60% chance of 2 blobs, 30% chance of 3, 10% chance for 4. Average of 2.5
        int blobs = Random.chances(new float[]{0, 0, 6, 3, 1});
        for (int i = 0; i < blobs; i++){
            int ofs;
            do {
                ofs = PathFinder.NEIGHBOURS8[Random.Int(8)];
            } while (!Dungeon.level.passable[level.map[pos] == Terrain.CHASM ? level.entrance() : pos + ofs]);
            if(level.map[pos] == Terrain.CHASM){
                Dungeon.level.drop( Generator.random( Generator.Category.STONE), level.entrance() + ofs ).sprite.drop();
            } else {
                Dungeon.level.drop( Generator.random( Generator.Category.STONE), pos + ofs ).sprite.drop( pos );
            }
        }

        PaswordBadges.SKY_DEAD();
        Statistics.bossScores[1] += 1000;

        yell( Messages.get(this, "defeated") );
    }

    @Override
    protected boolean getCloser(int target) {
        if (state == HUNTING) {
            if(scytheCooldown <= 0 && Dungeon.level.distance(pos, enemy.pos) >= 2)
                return super.getCloser( target );
            return enemySeen && getFurther( target );
        } else {
            return super.getCloser(target);
        }
        //return false;
    }


    @Override
    public int drRoll() {
        return Random.NormalIntRange(0, 4);
    }


}

