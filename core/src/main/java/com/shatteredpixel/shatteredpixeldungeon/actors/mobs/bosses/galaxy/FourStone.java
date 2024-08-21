package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.galaxy;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FireImbue;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FrostImbue;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicImmune;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ToxicImbue;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NPC;
import com.shatteredpixel.shatteredpixeldungeon.sprites.FourStoneSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;

public class FourStone extends NPC {

    {
        spriteClass = FourStoneSprite.class;
    }

    @Override
    public boolean interact(Char c) {
        float left = 50f;
        Buff.affect(hero, FireImbue.class).set(left);
        sprite.turnTo(pos, hero.pos);
        GLog.w("石碑中的神奇力量已被你吸收！");
        die(true);
        return true;
    }


    public static class FrostFourStone extends NPC {

        {
            spriteClass = FourStoneSprite.FrostFourStoneSprite.class;
        }

        @Override
        public boolean interact(Char c) {
            float left = 50f;
            Buff.affect(hero, FrostImbue.class,left);
            sprite.turnTo(pos, hero.pos);
            GLog.w("石碑中的神奇力量已被你吸收！");
            die(true);
            return true;
        }

    }

    public static class PoisonFourStone extends NPC {

        {
            spriteClass = FourStoneSprite.PoisonFourStoneSprite.class;
        }

        @Override
        public boolean interact(Char c) {
            float left = 50f;
            Buff.affect(hero, ToxicImbue.class).set(left);
            sprite.turnTo(pos, hero.pos);
            GLog.w("石碑中的神奇力量已被你吸收！");
            die(true);
            return true;
        }

    }

    public static class MagicFourStone extends NPC {

        {
            spriteClass = FourStoneSprite.MagicFourStoneSprite.class;
        }

        @Override
        public boolean interact(Char c) {
            float left = 50f;
            Buff.affect(hero, MagicImmune.class,left);
            sprite.turnTo(pos, hero.pos);
            GLog.w("石碑中的神奇力量已被你吸收！");
            die(true);
            return true;
        }

    }


}
