package com.shatteredpixel.shatteredpixeldungeon.items.food.hollow;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.PaswordBadges;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.hollow.ApprenticeWitch;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Food;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfSirensSong;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Random;

public class Sugar extends Food {

    {
        image = ItemSpriteSheet.TOFFEE;
        energy = 100f;
        defaultAction = AC_THROW;
        usesTargeting = true;
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
            splash(cell);
        }
    }

    protected void splash(int cell) {
        DEM(cell);
    }

    protected void DEM(int cell) {
        Char ch = Actor.findChar(cell);
        if(ch != null){
            if (ch instanceof ApprenticeWitch) {
                if(Random.Int(100) < 20 + ((ApprenticeWitch) ch).Sugar * 20){
                    Buff.affect(ch, ScrollOfSirensSong.Enthralled.class);
                    CellEmitter.get( cell ).burst( Speck.factory( Speck.HEART ), 5 );
                    ((ApprenticeWitch) ch).Sugar = 0;
                    GLog.p(Messages.get(this,"love"));
                    Statistics.LoveMagicGirl++;
                    if(Statistics.LoveMagicGirl>=3){
                        PaswordBadges.SUGAR_FRIENDLY();
                    }
                } else {
                    ((ApprenticeWitch) ch).Sugar++;
                    GLog.w(Messages.get(this,"no_love"));
                }
            }
        }
    }

    public static class Recipe extends com.shatteredpixel.shatteredpixeldungeon.items.Recipe.SimpleRecipe {

        {
            inputs =  new Class[]{Gelatin.class, Sugar_Block.class, WhiteSugar_B.class};
            inQuantity = new int[]{1, 1, 1};

            cost = 16;

            output = Sugar.class;
            outQuantity = 4;
        }

    }

}
