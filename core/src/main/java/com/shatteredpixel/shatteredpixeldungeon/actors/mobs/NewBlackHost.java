package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.CursedDiedBlos;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.TargetedCell;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.MagicFlameParticle;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.PoltergeistSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.GameMath;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class NewBlackHost extends Elemental.FireElemental {
    public NewBlackHost() {
        this.spriteClass = PoltergeistSprite.class;

        this.HT = 27;

        this.HP = 27;

        this.defenseSkill = 5;

        this.EXP = 8;

        this.maxLvl = 15;

        this.flying = true;

        properties.add(Property.UNDEAD);
    }

    private int targetingPos = -1;

    @Override
    protected boolean act() {
        if (targetingPos != -1){
            if (sprite != null && (sprite.visible || Dungeon.level.heroFOV[targetingPos])) {
                sprite.zap( targetingPos );
                return false;
            } else {
                zap();
                return true;
            }
        } else {
            return super.act();
        }
    }

    @Override
    protected boolean canAttack( Char enemy ) {
        if (super.canAttack(enemy)){
            return true;
        } else {
            return rangedCooldown < 0 && new Ballistica( pos, enemy.pos, Ballistica.MAGIC_BOLT | Ballistica.STOP_TARGET ).collisionPos == enemy.pos;
        }
    }

    protected boolean doAttack( Char enemy ) {

        if (rangedCooldown > 0) {

            return super.doAttack( enemy );

        } else if (new Ballistica( pos, enemy.pos, Ballistica.STOP_SOLID | Ballistica.STOP_TARGET ).collisionPos == enemy.pos) {

            //set up an attack for next turn
            ArrayList<Integer> candidates = new ArrayList<>();
            for (int i : PathFinder.NEIGHBOURS8){
                int target = enemy.pos + i;
                if (target != pos && new Ballistica(pos, target, Ballistica.STOP_SOLID | Ballistica.STOP_TARGET).collisionPos == target){
                    candidates.add(target);
                }
            }

            if (!candidates.isEmpty()){
                targetingPos = Random.element(candidates);

                for (int i : PathFinder.NEIGHBOURS9){
                    if (!Dungeon.level.solid[targetingPos + i]) {
                        sprite.parent.addToBack(new TargetedCell(targetingPos + i, 0xFF0000));
                    }
                }

                GLog.n(Messages.get(this, "charging"));
                spend(GameMath.gate(TICK, (int)Math.ceil(Dungeon.hero.cooldown()), 3*TICK));
                Dungeon.hero.interrupt();
                return true;
            } else {
                rangedCooldown = 1;
                return super.doAttack(enemy);
            }


        } else {
            rangedCooldown = 1;
            return super.doAttack(enemy);
        }
    }

    @Override
    protected void zap() {
        if (targetingPos != -1) {
            spend(1f);

            Invisibility.dispel(this);

            for (int i : PathFinder.NEIGHBOURS9) {
                if (!Dungeon.level.solid[targetingPos + i]) {
                    CellEmitter.get(targetingPos + i).burst(MagicFlameParticle.FACTORY, 5);
                    if (Dungeon.level.water[targetingPos + i]) {
                        GameScene.add(Blob.seed(targetingPos + i, 2, CursedDiedBlos.class));
                    } else {
                        GameScene.add(Blob.seed(targetingPos + i, 8, CursedDiedBlos.class));
                    }
                }
            }
            Sample.INSTANCE.play(Assets.Sounds.BURNING);
        }

        targetingPos = -1;
        rangedCooldown = Random.NormalIntRange( 3, 5 );
    }

    @Override
    public int attackSkill(Char target) {
        if (!summonedALly) {
            return 15;
        } else {
            return super.attackSkill(target);
        }
    }

    @Override
    public int damageRoll() {
        if (!summonedALly) {
            return Random.NormalIntRange(10, 12);
        } else {
            return super.damageRoll();
        }
    }

    @Override
    protected void meleeProc(Char enemy, int damage) {
        //no fiery on-hit unless it is an ally summon
        if (summonedALly) {
            super.meleeProc(enemy, damage);
        }
    }

    @Override
    public boolean reset() {
        return !summonedALly;
    }

    private static final String TARGETING_POS = "targeting_pos";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(TARGETING_POS, targetingPos);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        targetingPos = bundle.getInt(TARGETING_POS);
    }
}

