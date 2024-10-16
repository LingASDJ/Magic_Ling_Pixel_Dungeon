package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.hollow;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.HalomethaneFire;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.HalomethaneBurning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.TargetedCell;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BlastParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ElmoParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Bomb;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ApprenticeWitchSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class ApprenticeWitch extends Mob {

    private boolean targeting = false;
    private boolean shot = true;
    public boolean isTargetingTeleport = true;

    private int cellToFire = 0;

    @Override
    protected boolean canAttack( Char enemy ) {
        return new Ballistica( pos, enemy.pos, Ballistica.PROJECTILE).collisionPos == enemy.pos;
    }

    {
        spriteClass = ApprenticeWitchSprite.class;

        baseSpeed = 1.75f;
        HP = HT = 140;

        defenseSkill = 24;

        loot = Generator.Category.SCROLL;
        lootChance = 1f;

        maxLvl = 35;
        immunities.add(HalomethaneBurning.class);
        EXP = 18;

        properties.add(Property.HOLLOW);

        properties.add(Property.ICY);
        properties.add(Property.FIERY);
        properties.add(Property.ELECTRIC);
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(40, 52);
    }

    @Override
    public int attackSkill( Char target ) {
        return 40;
    }

    @Override
    public int drRoll() {
        return 0;
    }


    public void onZapComplete(int cell) {
        zap(cell);
        next();
    }

    private void zap(int cell) {
        spend(2f);
        int dmg = Random.NormalIntRange(10,24);
        if(isTargetingTeleport){
            Invisibility.dispel(this);
            for (int i : PathFinder.NEIGHBOURS9) {
                if (!Dungeon.level.solid[cell + i]) {
                    CellEmitter.get(cell + i).burst(ElmoParticle.FACTORY, 5);
                    if (Dungeon.level.water[cell + i]) {
                        GameScene.add(Blob.seed(cell + i, 2, HalomethaneFire.class));
                    } else {
                        GameScene.add(Blob.seed(cell + i, 2, HalomethaneFire.class));
                    }
                }
            }

            for(int c: PathFinder.NEIGHBOURS9){
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
        } else {
            target = Dungeon.level.randomDestination(this);
            final Char leapVictim = Actor.findChar(cell);
            switch (Random.NormalIntRange(0,3)){
                case 0:
                    if (leapVictim != null){
                        ScrollOfTeleportation.appear(this, target);
                        yell(Messages.get(this, "bye"));
                    }
                    break;
                case 1:
                    if (leapVictim != null){
                        ScrollOfTeleportation.appear(enemy, target);
                        yell(Messages.get(this,"bye"));
                    }
                    break;
                case 2:
                    int T_dmg = dmg*2;
                    if (leapVictim != null){
                        enemy.damage(T_dmg,this);
                        yell(Messages.get(this,"die"));
                    }
                    break;
                case 3:
                    yell(Messages.get(this,"oh_no"));
                    damage(dmg,this);
                    break;
            }
            spend(TICK);
        }



    }

    protected boolean doAttack(Char enemy ) {

        if (Dungeon.level.adjacent( pos, enemy.pos )) {
            shot = true;
            targeting = false;

            return super.doAttack( enemy );

        }else if (shot){
            shot = false;
            targeting = true;
            sprite.parent.add(new TargetedCell(enemy.pos, 0xFF0000));
            for(int c: PathFinder.NEIGHBOURS9){
                if(isTargetingTeleport){
                    sprite.parent.add(new TargetedCell(enemy.pos + c, 0xFF0000));
                }
                sprite.parent.add(new TargetedCell(enemy.pos, Window.CYELLOW));
            }
            cellToFire = enemy.pos;
            ((ApprenticeWitchSprite)sprite).targeting(cellToFire);
            spend( attackDelay()*2);
            return true;
        } else {
            shot = true;
            targeting = false;
            if (sprite != null && (sprite.visible || enemy.sprite.visible)) {
                sprite.zap( cellToFire );
                return false;
            } else {
                zap(cellToFire);
                return true;
            }
        }
    }


    private static final String ISTR = "ISTR";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(ISTR, isTargetingTeleport);
    }


    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        isTargetingTeleport = bundle.getBoolean(ISTR);
    }

}

