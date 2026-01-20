package com.shatteredpixel.shatteredpixeldungeon.items.quest;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.pets.SmallLight;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfMindVision;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfMagicMapping;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndUseItem;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class SmallLightHeader extends Item implements Item.ThanksItem {

    public static final String AC_SUMMON = "SummonFish";
    public static final String AC_CHOOSE = "CHOOSE";

    {
        image = ItemSpriteSheet.SMTITEM;
        stackable = true;
        defaultAction =  AC_CHOOSE;
    }

    public ArrayList<String> actions(Hero hero ) {
        ArrayList<String> actions = super.actions( hero );
        actions.add(AC_SUMMON);
        return actions;
    }

    @Override
    public void execute(Hero hero, String action ) {
        super.execute(hero, action);
        if (action.equals(AC_CHOOSE)){
            GameScene.show(new WndUseItem(null, this) );
        } else if (action.equals(AC_SUMMON)) {
            detach( hero.belongings.backpack );
            hero.sprite.operate(hero.pos, () -> {
                Buff.affect( hero, SAwareness.class, SAwareness.DURATION );
                ArrayList<Integer> respawnPoints = new ArrayList<>();
                for (int i = 0; i < PathFinder.NEIGHBOURS8.length; i++) {
                    int p = hero.pos + PathFinder.NEIGHBOURS8[i];
                    if (Actor.findChar(p) == null && Dungeon.level.passable[p]) {
                        respawnPoints.add(p);
                    }
                }
                if (!respawnPoints.isEmpty()) {
                    SmallLight smallLight = new SmallLight();
                    smallLight.pos = respawnPoints.get(Random.index( respawnPoints ));
                    GameScene.add(smallLight);
                    smallLight.state = smallLight.WANDERING;
                    smallLight.sprite.emitter().burst(Speck.factory(Speck.STAR), 10);
                    hero.sprite.idle();
                }
            });
        }
    }

    @Override
    public boolean isUpgradable() {
        return false;
    }

    @Override
    public boolean isIdentified() {
        return true;
    }

    @Override
    public int value() {
        return 75;
    }


    public static class Recipe extends com.shatteredpixel.shatteredpixeldungeon.items.Recipe.SimpleRecipe {

        {
            inputs =  new Class[]{PotionOfMindVision.class, ScrollOfMagicMapping.class};
            inQuantity = new int[]{1, 1};

            cost = 14;

            output = SmallLightHeader.class;
            outQuantity = 4;
        }

    }


    public static class SAwareness extends FlavourBuff {
        public int distance = 2;
        {
            type = buffType.POSITIVE;
        }

        public static final float DURATION = 123456789f;

        @Override
        public void detach() {
            super.detach();
            Dungeon.observe();
            GameScene.updateFog();
        }
    }

}


