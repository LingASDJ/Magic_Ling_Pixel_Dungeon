package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Healing;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.FloatingText;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ShaftParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.items.trinkets.VialOfBlood;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.HealLanFireSprites;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;

public class HealLanFire extends NPC {
    {
        spriteClass = HealLanFireSprites.class;
        properties.add(Char.Property.BOSS);
        properties.add(Char.Property.IMMOVABLE);
        properties.add(Char.Property.UNKNOWN);
    }

    @Override
    public void damage( int dmg, Object src ) {
    }

    @Override
    public int defenseSkill( Char enemy ) {
        return INFINITE_EVASION;
    }

    @Override
    public boolean interact(Char c) {
        if (!hero.isAlive()) return false;

        Sample.INSTANCE.play( Assets.Sounds.DRINK );

        PotionOfHealing.cure( hero );
        hero.belongings.uncurseEquipped();
        hero.buff( Hunger.class ).satisfy( Hunger.STARVING );

        if (VialOfBlood.delayBurstHealing()){
            Healing healing = Buff.affect(hero, Healing.class);
            healing.setHeal(hero.HT, 0, VialOfBlood.maxHealPerTurn());
            healing.applyVialEffect();
        } else {
            hero.HP = hero.HT;
            hero.sprite.emitter().start(Speck.factory(Speck.HEALING), 0.4f, 4);
            hero.sprite.showStatusWithIcon(CharSprite.POSITIVE, Integer.toString(hero.HT), FloatingText.HEALING);
        }

        CellEmitter.get( hero.pos ).start( ShaftParticle.FACTORY, 0.2f, 3 );

        hero.interrupt();

        GLog.p( Messages.get(this, "ssss") );

        die(true);
        return false;
    }
}

