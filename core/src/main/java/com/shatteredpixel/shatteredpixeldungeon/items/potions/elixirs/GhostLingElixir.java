package com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.HalomethaneFire;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.MagicFire;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invulnerability;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfLiquidFlameX;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.UnstableSpell;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfEnchantment;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;

import java.util.ArrayList;

public class GhostLingElixir extends Elixir {

    {
        image = ItemSpriteSheet.ELIXIR_GHOSTLING;

        stackable = true;
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        return super.actions(hero);
    }

    @Override
    public void execute(Hero hero, String action) {
        super.execute(hero, action);
    }

    @Override
    protected void onThrow(int cell) {
        if (Dungeon.level.map[cell] == Terrain.WELL || Dungeon.level.pit[cell]) {

            super.onThrow(cell);

        } else {

            Dungeon.level.pressCell(cell);
            shatter(cell);

        }
    }

    public void shatter(int cell) {
        if (Dungeon.level.heroFOV[cell]) {
            Sample.INSTANCE.play(Assets.Sounds.SHATTER);
        }
        for (int offset : PathFinder.NEIGHBOURS9){
            GameScene.add(Blob.seed(cell + offset, 3, MagicFire.class));
            GameScene.add( Blob.seed(cell + offset, 7, HalomethaneFire.class ) );
        }
    }

    public static class Recipe extends com.shatteredpixel.shatteredpixeldungeon.items.Recipe.SimpleRecipe {

        {
            inputs =  new Class[]{StoneOfEnchantment.class, PotionOfLiquidFlameX.class, UnstableSpell.class};
            inQuantity = new int[]{1, 1, 1};

            cost = 8;

            output = GhostLingElixir.class;
            outQuantity = 1;
        }

    }

    @Override
    public boolean isIdentified() {
        return true;
    }

    @Override
    public void apply(Hero hero) {
        Dungeon.level.pressCell(hero.pos);
        shatter(hero.pos);
        Buff.affect(hero, Invulnerability.class, 8f);
    }

    public int value() {
        return 75 * quantity;
    }
}

