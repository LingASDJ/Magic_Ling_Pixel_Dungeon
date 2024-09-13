//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.HalomethaneFire;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BlastParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfLiquidFlameX;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.Chasm;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.FireBallMobSpriteKB;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class RandomBlueFire extends Mob {
    private static final String COMBO = "combo";
    private int combo;
    public float lifespan;
    public RandomBlueFire() {
        this.spriteClass = FireBallMobSpriteKB.class;
        this.HT = 75;
        this.HP = 75;
        this.defenseSkill = 10;
        this.EXP = 15;
        this.loot = new PotionOfLiquidFlameX();
        this.lootChance =1;
        this.state = this.SLEEPING;
        this.baseSpeed = 0.8f;
        this.combo = 0;
        properties.add(Property.FIERY);
    }

    @Override
    public void notice() {
        super.notice();
        yell( Messages.get(this, "notice") );
    }

    public int attackProc(Char var1, int var2) {
        byte var3 = 0;
        int var4;
        if (Random.Int(0, 10) > 7) {
            var4 = Random.IntRange(1, 3);
            this.sprite.showStatus(16711680, Messages.get(this,"attack_msg_"+var4), new Object[0]);
        }

        int var5 = super.attackProc(var1, var2);
        var4 = var1.pos;
        CellEmitter.center(var4).burst(BlastParticle.FACTORY, 30);
        GameScene.add(Blob.seed(var4, 2, HalomethaneFire.class));
        int reg = Math.min( Random.Int(8) - 4, HT - HP );

        if (reg > 0) {
            HP += reg;
            sprite.emitter().burst( Speck.factory( Speck.HEALING ), 1 );
        }
        int[] var7 = PathFinder.CIRCLE8;
        int var6 = var7.length;

        for(var2 = var3; var2 < var6; ++var2) {
            int var8 = var7[var2];
            if (!Dungeon.level.solid[var4 + var8]) {
                GameScene.add(Blob.seed(var4 + var8, 2, HalomethaneFire.class));
            }
        }

        ++this.combo;
        return var5;
    }

    public int attackSkill(Char var1) {
        return 56;
    }

    protected boolean canAttack(Char var1) {
        Ballistica var2 = new Ballistica(this.pos, var1.pos, 7);
        boolean var3;
        if (!Dungeon.level.adjacent(this.pos, var1.pos) && var2.collisionPos == var1.pos) {
            var3 = true;
        } else {
            var3 = false;
        }

        return var3;
    }

    @Override
    public int damageRoll() {
        return Char.combatRoll( 10, 20 );
    }

    public void die(Object var1) {
        Dungeon.level.drop( new PotionOfLiquidFlameX(), pos ).sprite.drop();
        Dungeon.level.drop( new PotionOfLiquidFlameX(), pos ).sprite.drop();
        Dungeon.level.drop( new PotionOfHealing(), pos ).sprite.drop();
        Dungeon.level.drop( new PotionOfHealing(), pos ).sprite.drop();
        super.die(var1);
        if (var1 != Chasm.class) {
            //如果新增临死前的话，请将1改为Random.IntRange
            this.sprite.showStatus(16711680, Messages.get(this,"death_msg_"+1));
        }
       for (Mob mob : Dungeon.level.mobs.toArray( new Mob[0] )) {
            if (	mob instanceof FlameC01) {
                mob.die( var1 );
            }
        }
        yell( Messages.get(this, "died") );
    }

    protected boolean getCloser(int var1) {
        boolean var2 = false;
        this.combo = 0;
        if (this.state == this.HUNTING) {
            boolean var3 = var2;
            if (this.enemySeen) {
                var3 = var2;
                if (this.getFurther(var1)) {
                    var3 = true;
                }
            }

            return var3;
        } else {
            return super.getCloser(var1);
        }
    }

    public void restoreFromBundle(Bundle var1) {
        super.restoreFromBundle(var1);
        this.combo = var1.getInt("combo");
    }

    public void storeInBundle(Bundle var1) {
        super.storeInBundle(var1);
        var1.put("combo", this.combo);
    }
}
