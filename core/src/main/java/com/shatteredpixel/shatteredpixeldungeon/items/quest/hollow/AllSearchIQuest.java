package com.shatteredpixel.shatteredpixeldungeon.items.quest.hollow;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.status.ScoreBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;

abstract public class AllSearchIQuest extends Item {

    protected int RXlevel;

    {
        stackable = false;
    }

    @Override
    public boolean isUpgradable() {
        return false;
    }

    @Override
    public boolean isIdentified() {
        return true;
    }

    public static void GetScore(Hero hero, int score){
        if(hero.buffs(ScoreBuff.class)!=null){
            ScoreBuff buff = hero.buff(ScoreBuff.class);
            buff.addScore(score);
        }
    }

    public static class HollowLantern extends AllSearchIQuest {
        {
            RXlevel = 3;
            image = ItemSpriteSheet.PUMPKM_LANTERN;
        }

        @Override
        public boolean doPickUp(Hero hero, int pos) {
            GameScene.pickUp(this, pos);
            Sample.INSTANCE.play( Assets.Sounds.ITEM );
            hero.spendAndNext( 0f );
            GetScore(hero, 3000);
            return true;
        }
    }

    public static class HollowCityProps extends AllSearchIQuest {
        {
            RXlevel = 3;
            image = ItemSpriteSheet.CASTLE_AIRPORT;
        }

        @Override
        public boolean doPickUp(Hero hero, int pos) {
            GameScene.pickUp(this, pos);
            Sample.INSTANCE.play( Assets.Sounds.ITEM );
            hero.spendAndNext( 0f );
            GetScore(hero, 4500);
            return true;
        }
    }

    public static class HollowGoldCards extends AllSearchIQuest {
        {
            RXlevel = 3;
            image = ItemSpriteSheet.GOLD_CARDS;
        }

        @Override
        public boolean doPickUp(Hero hero, int pos) {
            GameScene.pickUp(this, pos);
            Sample.INSTANCE.play( Assets.Sounds.ITEM );
            hero.spendAndNext( 0f );
            GetScore(hero, 3000);
            return true;
        }
    }



}
