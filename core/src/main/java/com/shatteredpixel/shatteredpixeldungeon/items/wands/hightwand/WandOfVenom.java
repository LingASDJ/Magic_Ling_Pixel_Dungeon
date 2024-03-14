package com.shatteredpixel.shatteredpixeldungeon.items.wands.hightwand;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.VenomGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.DwarfKing;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfCorrosiveGas;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfDeepSleep;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.DamageWand;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfCorrosion;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Unstable;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MagesStaff;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.ColorMath;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

import java.util.ArrayList;

public class WandOfVenom extends DamageWand {

    {
        image = ItemSpriteSheet.HIGHTWAND_1;
    }

    public static class Recipe extends com.shatteredpixel.shatteredpixeldungeon.items.Recipe.SimpleRecipe {

        {
            inputs =  new Class[]{WandOfCorrosion.class, PotionOfCorrosiveGas.class , StoneOfDeepSleep.class};
            inQuantity = new int[]{1, 1, 1};

            cost = 20;

            output = WandOfVenom.class;
            outQuantity = 1;
        }

        public final Item sampleOutput(ArrayList<Item> ingredients){
            try {
                Item result = Reflection.newInstance(output);
                result.quantity(outQuantity).level(Random.NormalIntRange(2,3));
                result.upgrade();
                return result;
            } catch (Exception e) {
                ShatteredPixelDungeon.reportException( e );
                return null;
            }
        }

    }

    @Override
    public void onZap(Ballistica bolt) {
        VenomGas gas = Blob.seed(bolt.collisionPos, 500 + 10 * buffedLvl(),  VenomGas.class);
        CellEmitter.get(bolt.collisionPos).burst(Speck.factory(Speck.CORROSION), 10 );
        gas.setStrength(4 + buffedLvl(), getClass());
        GameScene.add(gas);
        Sample.INSTANCE.play(Assets.Sounds.GAS);

        for (int i : PathFinder.NEIGHBOURS9) {
            Char ch = Actor.findChar(bolt.collisionPos + i);
            if (ch != null) {
                wandProc(ch, chargesPerCast());

                if (i == 0 && ch instanceof DwarfKing){
                    Statistics.qualifiedForBossChallengeBadge = false;
                }
            }
        }

        if (Actor.findChar(bolt.collisionPos) == null){
            Dungeon.level.pressCell(bolt.collisionPos);
        }
    }

    public void fx(Ballistica bolt, Char caster, Callback callback) {
        MagicMissile.boltFromChar(caster.sprite.parent,
                MagicMissile.TOXIC_VENT,
                caster.sprite,
                bolt.collisionPos,
                null);
    }

    @Override
    public void onHit(MagesStaff staff, Char attacker, Char defender, int damage) {
        new Unstable().proc( staff, attacker, defender, damage);
    }

    @Override
    public void staffFx(MagesStaff.StaffParticle particle) {
        particle.color(ColorMath.random(Window.SHPX_COLOR, Window.GDX_COLOR));
        particle.am = 0.6f;
        particle.setLifespan(0.6f);
        particle.alpha(1f);
        particle.acc.set(0, +10);
        particle.speed.polar(-Random.Float(3.1415926f), 6f);
        particle.setSize(1f, 1.5f);
        particle.sizeJitter = 1f;
        particle.angularSpeed = 25;
        particle.shuffleXY(1f);
        float dst = Random.Float(1f);
        particle.x -= dst;
        particle.y += dst;
    }

    @Override
    public int min(int lvl) {
        return 2+4*lvl;
    }

    @Override
    public int max(int lvl) {
        return 3+5*lvl;
    }

    @Override
    public String statsDesc() {
        if (levelKnown)
            return Messages.get(this, "stats_desc", 4+buffedLvl());
        else
            return Messages.get(this, "stats_desc", 4);
    }
}