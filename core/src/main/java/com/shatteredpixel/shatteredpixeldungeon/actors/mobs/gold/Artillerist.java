package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.gold;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AscensionChallenge;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Degrade;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.TargetedCell;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BlastParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SmokeParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Bomb;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class Artillerist extends GoldMob {
    {
        spriteClass = ArtilleristSprite.class;

        HP = HT = 100;
        defenseSkill = 22;

        EXP = 9;
        maxLvl = 20;

        properties.add(Property.UNDEAD);

        loot = Generator.Category.SCROLL;
        lootChance = 0.1f;
    }

    private boolean targeting = false;
    private boolean shot = true;

    private int targetingPos = -1;

    private int cellToFire = 0;

    @Override
    protected boolean canAttack( Char enemy ) {
        Ballistica ballistica = new Ballistica( pos, enemy.pos, Ballistica.PROJECTILE);
        boolean isCanAttack = ballistica.collisionPos == enemy.pos;
        if(targetingPos != pos) shot = true;
        return isCanAttack;
    }

    public int damageRoll() {
        return Random.NormalIntRange( 25, 45 );
    }

    @Override
    public int attackSkill( Char target ) {
        return 26;
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(4, 8);
    }

    public void onZapComplete(int cell) {
        zap(cell);
        next();
    }

    private void zap(int cell) {
        spend(1f);
        Invisibility.dispel(this);
        int dmg = Random.NormalIntRange(25, 35);
        dmg = Math.round(dmg * AscensionChallenge.statModifier(this));
        CellEmitter.get(cell).burst(SmokeParticle.FACTORY, 4);
        if(Dungeon.hero != null){
            if(Dungeon.hero.pos == cell){
                Dungeon.hero.damage(dmg,new Bomb());
            }
        }

        if(Random.Int(10)<=3) {
            if(Statistics.bossRushMode){
                Buff.affect(enemy, Degrade.class, 6f);
            }
        }

        for(int c: PathFinder.NEIGHBOURS4){
            CellEmitter.get(cell+c).burst(BlastParticle.FACTORY, 20);
            Mob mob = Dungeon.level.findMob(cell+c);
            if(mob != null){
                mob.damage(dmg,new Bomb());
            }
            if(Dungeon.hero != null){
                if(Dungeon.hero.pos == cell + c){
                    Dungeon.hero.damage(dmg,new Bomb());
                }
            }
        }


        if (!enemy.isAlive() && enemy == Dungeon.hero) {
            Dungeon.fail(getClass());
            GLog.n(Messages.get(this, "bomb_party_kill"));
        }
    }
    protected boolean doAttack(Char enemy ) {

        if (Dungeon.level.adjacent( pos, enemy.pos )) {
            shot = true;
            targeting = false;

            return super.doAttack( enemy );

        }else if (shot){
            targeting = true;
            shot = false;
            targetingPos = pos;
            sprite.parent.add(new TargetedCell(enemy.pos, 0xFF0000));
            for(int c: PathFinder.NEIGHBOURS4){
                sprite.parent.add(new TargetedCell(enemy.pos + c, 0xFF0000));
            }
            cellToFire = enemy.pos;
            ((ArtilleristSprite)sprite).targeting(cellToFire);
            spend( attackDelay());
            return true;
        }
        else{
            shot = true;
            if (sprite != null && (sprite.visible || enemy.sprite.visible)) {
                if(targeting)
                    sprite.zap( cellToFire );
                targeting = false;
                return false;
            } else {
                if(targeting)
                    zap(cellToFire);
                targeting = false;
                return true;
            }
        }
    }
}
