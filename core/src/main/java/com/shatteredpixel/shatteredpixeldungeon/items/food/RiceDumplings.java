package com.shatteredpixel.shatteredpixeldungeon.items.food;

import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AdrenalineSurge;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ArcaneArmor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ArtifactRecharge;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bleeding;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Doom;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Drowsy;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FrostBurning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Fury;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.HaloFireImBlue;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.HalomethaneBurning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Healing;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hex;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Poison;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Slow;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vulnerable;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Weakness;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfStrength;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;

public class RiceDumplings extends Food {

    {
        image = ItemSpriteSheet.RiceDumplingsRed;
        energy = 150f;
    }

    public static void cure( Char ch ) {
        Buff.detach( ch, Poison.class );
        Buff.detach( ch, Cripple.class );
        Buff.detach( ch, Weakness.class );
        Buff.detach( ch, Vulnerable.class );
        Buff.detach( ch, Bleeding.class );
        Buff.detach( ch, Blindness.class );
        Buff.detach( ch, Drowsy.class );
        Buff.detach( ch, Slow.class );
        Buff.detach( ch, Vertigo.class);
        Buff.detach( ch, Doom.class );
        Buff.detach( ch, HalomethaneBurning.class );
        Buff.detach( ch, FrostBurning.class );
        Buff.detach( ch, Burning.class );
        Buff.detach( ch, Hex.class );
        GLog.p(Messages.get(RiceDumplings.class, "eat_msg_good"));
    }

    @Override
    protected void satisfy(Hero hero) {
        super.satisfy(hero);
        cure(hero);
    }

    public static class RiceDumplingsRed extends RiceDumplings {

        {
            image = ItemSpriteSheet.RiceDumplingsRed;
        }

        @Override
        protected void satisfy(Hero hero) {
            super.satisfy(hero);
            if (Dungeon.isChallenged(Challenges.NO_HEALING)){
                Buff.affect(hero, Healing.class).setHeal((int) (0.4f * hero.HT + 8), 0.25f, 0);
            } else {
                Buff.affect(hero, Healing.class).setHeal((int) (0.8f * hero.HT + 14), 0.25f, 0);
            }
        }
    }

    public static class RiceDumplingsPink extends RiceDumplings {

        {
            image = ItemSpriteSheet.RiceDumplingsPink;
        }

        @Override
        protected void satisfy(Hero hero) {
            super.satisfy(hero);
            Buff.affect(hero,ArcaneArmor.class).set(Dungeon.hero.lvl + 10, 10);
        }
    }

    public static class RiceDumplingsOrange extends RiceDumplings {

        {
            image = ItemSpriteSheet.RiceDumplingsOrange;
        }

        @Override
        protected void satisfy(Hero hero) {
            super.satisfy(hero);
            Buff.affect(curUser, ArtifactRecharge.class).set( 30 ).ignoreHornOfPlenty = false;
        }
    }

    public static class RiceDumplingsLink extends RiceDumplings {

        {
            image = ItemSpriteSheet.RiceDumplingsLink;
        }

        @Override
        protected void satisfy(Hero hero) {
            super.satisfy(hero);
            Buff.affect(hero, AdrenalineSurge.class).reset(1, AdrenalineSurge.DURATION/2);
        }
    }

    public static class RiceDumplingsBottle extends RiceDumplings {

        {
            image = ItemSpriteSheet.RiceDumplingsBottle;
        }

        @Override
        protected void satisfy(Hero hero) {
            super.satisfy(hero);
            Buff.affect(hero, Fury.class).set( (100), 1 );
            Buff.affect(hero, HaloFireImBlue.class).set(HaloFireImBlue.DURATION);
            hero.STR++;
            hero.sprite.showStatus(CharSprite.POSITIVE, Messages.get(PotionOfStrength.class, "msg_1"));
            GLog.p(Messages.get(PotionOfStrength.class, "msg_2"));
        }
    }



}
