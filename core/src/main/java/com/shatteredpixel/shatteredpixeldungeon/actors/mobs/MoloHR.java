//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Boss;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BlastParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.DriedRose;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.brews.Brew;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ExoticScroll;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.Chasm;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MolotovHuntsmanSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class MoloHR extends Boss {

    private int combo;

    public MoloHR() {
        this.spriteClass = MolotovHuntsmanSprite.class;
        this.HT = 180;
        this.HP = 180;
        HUNTING = new Hunting();
        state = HUNTING;
        this.defenseSkill = 10;
        flying = true;
        this.EXP = 15;
        this.baseSpeed = 2F;
        this.combo = 0;
        properties.add(Property.BOSS);
        properties.add(Property.FIERY);
    }

    public int attackProc(Char var1, int var2) {

        int var4;
        if (Random.Int(0, 8) > 7) {
            var4 = Random.IntRange(1, 8);
            this.sprite.showStatus(16711680, Messages.get(this,"attack_msg_"+var4));
        }

        int var5 = super.attackProc(var1, var2);
        var4 = var1.pos;
        CellEmitter.center(var4).burst(BlastParticle.FACTORY, 30);
        //GameScene.add(Blob.seed(var4, 4, GooWarn.class));

        ++this.combo;
        return var5;
    }

    public int attackSkill(Char var1) {
        return 56;
    }

    protected boolean canAttack(Char var1) {
        Ballistica var2 = new Ballistica(this.pos, var1.pos, 7);
        boolean var3;
        var3 = !Dungeon.level.adjacent(this.pos, var1.pos) && var2.collisionPos == var1.pos;

        return var3;
    }

    public int damageRoll() {
        return Random.NormalIntRange(30, 40);
    }
    public static Brew food;
    public static ExoticScroll scrolls;
    @Override
    public void die( Object cause ) {
        super.die( cause );
        Badges.validateBossSlain();
        Badges.KILLSDM720();
        GameScene.bossSlain();
        Dungeon.level.unseal();
        if (cause != Chasm.class) {
            //如果新增临死前的话，请将1改为Random.IntRange
            this.sprite.showStatus(16711680, Messages.get(this,"death_msg_"+1));
        }
        //酸液体清0
        Statistics.SiderLing = 0;
        GetBossLoot();
        Statistics.bossScores[2] += 4000;
        Dungeon.level.drop( ( Generator.randomUsingDefaults( Generator.Category.SCROLL ) ), this.pos );
        Dungeon.level.drop( ( Generator.randomUsingDefaults( Generator.Category.POTION ) ), this.pos );
        Dungeon.level.drop( ( Generator.randomUsingDefaults( Generator.Category.WAND ) ), this.pos );
    }

    protected boolean getCloser(int var1) {
        boolean var2 = false;
        this.combo = 0;
        if (this.state == this.HUNTING) {
            boolean var3 = var2;
            if (this.enemySeen) {
                if (this.getFurther(var1)) {
                    var3 = true;
                }
            }

            return var3;
        } else {
            return super.getCloser(var1);
        }
    }

    public void storeInBundle(Bundle var1) {
        super.storeInBundle(var1);
        var1.put("combo", this.combo);
    }

    @Override
    public void notice() {
        if (!BossHealthBar.isAssigned()) {
            BossHealthBar.assignBoss(this);
            yell(Messages.get(this, "notice"));
            for (Char ch : Actor.chars()){
                if (ch instanceof DriedRose.GhostHero){
                    ((DriedRose.GhostHero) ch).sayBoss();
                }
            }
        }
        super.notice();
    }

    {
        immunities.add( ToxicGas.class );
        immunities.add( Terror.class );
    }
}
