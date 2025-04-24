package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.HalomethaneFire;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Chill;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Corruption;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FrostBurning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.HalomethaneBurning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ElmoParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Dewdrop;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.food.MeatPie;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRetribution;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfPsionicBlast;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Grim;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.AoReadyDragonSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class AoReadyDragon extends Mob implements Callback {

    private static final float TIME_TO_ZAP = 4f;

    {
        spriteClass = AoReadyDragonSprite.class;
        baseSpeed = 2f;
        HP = HT = 40 - Challenges.activeChallenges()/3;
        EXP = 20;

        flying = true;

        defenseSkill = 8;
        immunities.add(Burning.class);
        immunities.add(HalomethaneBurning.class);
        immunities.add(FrostBurning.class);
        immunities.add(Chill.class);
        immunities.add(Vertigo.class);
        immunities.add(ToxicGas.class);
        properties.add(Property.MINIBOSS);
        immunities.add(Grim.class);
        immunities.add(ScrollOfPsionicBlast.class);
        immunities.add(ScrollOfRetribution.class);
        immunities.add(Corruption.class);
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(4, 7);
    }

    @Override
    public int attackSkill(Char target) {
        return defenseSkill(this) + 10;
    }

    @Override
    public int drRoll() {
        return 4;
    }


    @Override
    public void die(Object cause) {
        super.die(cause);
        yell(Messages.get(this,"flower"));

        Dungeon.level.drop(Generator.randomUsingDefaults(Generator.Category.WAND),pos);
        Dungeon.level.drop(new MeatPie(),pos);
        Dungeon.level.drop(Generator.randomUsingDefaults(Generator.Category.POTION),pos);
        Dungeon.level.drop(Generator.randomUsingDefaults(Generator.Category.STONE),pos);

        Dewdrop dewdrop = new Dewdrop();
        dewdrop.quantity = 8;
        Dungeon.level.drop(dewdrop,pos);
    }

    @Override
    protected boolean canAttack( Char enemy ) {
        return new Ballistica( pos, enemy.pos, Ballistica.MAGIC_BOLT).collisionPos == enemy.pos;
    }

    protected boolean doAttack( Char enemy ) {

        if (Dungeon.level.adjacent( pos, enemy.pos )) {

            return super.doAttack( enemy );

        } else {

            if (sprite != null && (sprite.visible || enemy.sprite.visible)) {
                sprite.zap( enemy.pos );
                return false;
            } else {
                zap();
                return true;
            }
        }
    }


    private void zap() {
        spend(TIME_TO_ZAP);

        yell(Messages.get(this,"leave"));

        if (hit(this, enemy, true)) {

            int dmg = damageRoll()*2;
            enemy.damage(dmg, this);
            for (int i : PathFinder.NEIGHBOURS4) {
                int targetingPos = enemy.pos + i;
                if (!Dungeon.level.solid[targetingPos]) {
                    CellEmitter.get(targetingPos).burst(ElmoParticle.FACTORY, 5);
                    GameScene.add(Blob.seed(targetingPos, 5, HalomethaneFire.class));
                }
            }
        } else {
            enemy.sprite.showStatus(CharSprite.NEUTRAL, enemy.defenseVerb());
        }

    }

    public void onZapComplete() {
        zap();
        next();
    }

    @Override
    public void call() {
        next();
    }
}
