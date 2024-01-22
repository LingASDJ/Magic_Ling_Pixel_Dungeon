package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.spical;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Charm;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Succubus;
import com.shatteredpixel.shatteredpixeldungeon.effects.FloatingText;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SuccubusQueenSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

public class SuccubusQueen extends Succubus {

    @Override
    public boolean isInvulnerable(Class effect) {
        return ( enemy.buff( Charm.CharmLing.class ) != null );
    }

    {
        HP = HT = 200;

        defenseSkill = 30;

        spriteClass = SuccubusQueenSprite.class;

        viewDistance = 20;

        flying = true;

        EXP = 17;
        maxLvl = 30;

        loot = Generator.Category.RING;
        lootChance = 0.48f;
    }

    public int attackProc(Char enemy, int damage ) {

        if (enemy.buff(Charm.CharmLing.class) != null ){
            int shield = (HP - HT) + (5 + damage);
            if (shield > 0){
                HP = HT;
                if (shield < 5){
                    sprite.showStatusWithIcon(CharSprite.POSITIVE, Integer.toString(5-shield), FloatingText.HEALING);
                }

                Buff.affect(this, Barrier.class).setShield(shield);
                sprite.showStatusWithIcon(CharSprite.POSITIVE, Integer.toString(shield), FloatingText.SHIELDING);
            } else {
                HP += 5 + damage;
                sprite.showStatusWithIcon(CharSprite.POSITIVE, "5", FloatingText.HEALING);
            }
            if (Dungeon.level.heroFOV[pos]) {
                Sample.INSTANCE.play( Assets.Sounds.CHARMS );
            }
        } else if (Random.Int( 3 ) == 0) {
            Charm.CharmLing c = Buff.affect( enemy, Charm.CharmLing.class, Charm.CharmLing.DURATION/2f );
            c.object = id();
            c.ignoreNextHit = true; //so that the -5 duration from succubus hit is ignored
            if (Dungeon.level.heroFOV[enemy.pos]) {
                enemy.sprite.centerEmitter().start(Speck.factory(Speck.HEART), 0.2f, 5);
                Sample.INSTANCE.play(Assets.Sounds.CHARMS);
            }
        }

        return damage;
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange( 30, 40 );
    }

}
