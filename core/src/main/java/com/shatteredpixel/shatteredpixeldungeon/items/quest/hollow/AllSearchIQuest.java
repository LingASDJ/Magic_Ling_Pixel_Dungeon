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
        if(hero.buff(ScoreBuff.class)!=null){
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
           if (collect( hero.belongings.backpack )) {
            GameScene.pickUp(this, pos);
            Sample.INSTANCE.play( Assets.Sounds.ITEM );
            hero.spendAndNext( 0f );
            GetScore(hero, 3000);
           }
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
           if (collect( hero.belongings.backpack )) {
            GameScene.pickUp(this, pos);
            Sample.INSTANCE.play( Assets.Sounds.ITEM );
            hero.spendAndNext( 0f );
            GetScore(hero, 4500);
           }
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
           if (collect( hero.belongings.backpack )) {
            GameScene.pickUp(this, pos);
            Sample.INSTANCE.play( Assets.Sounds.ITEM );
            hero.spendAndNext( 0f );
            GetScore(hero, 3000);
           }
            return true;
        }
    }

    //Mid Items
    public static class CrystalHeartChoco extends AllSearchIQuest {

        {
            RXlevel = 2;
            image = ItemSpriteSheet.CRYSTAL_CHOCO;
        }

        @Override
        public boolean doPickUp(Hero hero, int pos) {
           if (collect( hero.belongings.backpack )) {
            GameScene.pickUp(this, pos);
            Sample.INSTANCE.play( Assets.Sounds.ITEM );
            hero.spendAndNext( 0f );
            GetScore(hero, 1000);
           }
            return true;
        }
    }

    public static class CreateWorldHeartModel extends AllSearchIQuest {

        {
            RXlevel = 2;
            image = ItemSpriteSheet.WORLD_HEART_MODEL;
        }

        @Override
        public boolean doPickUp(Hero hero, int pos) {
           if (collect( hero.belongings.backpack )) {
            GameScene.pickUp(this, pos);
            Sample.INSTANCE.play( Assets.Sounds.ITEM );
            hero.spendAndNext( 0f );
            GetScore(hero, 800);
           }
            return true;
        }
    }

    public static class GhostBlueModel extends AllSearchIQuest {

        {
            RXlevel = 2;
            image = ItemSpriteSheet.GHOST_BLUE_MODEL;
        }

        @Override
        public boolean doPickUp(Hero hero, int pos) {
           if (collect( hero.belongings.backpack )) {
            GameScene.pickUp(this, pos);
            Sample.INSTANCE.play( Assets.Sounds.ITEM );
            hero.spendAndNext( 0f );
            GetScore(hero, 850);
           }
            return true;
        }
    }

    public static class GreenDamModel extends AllSearchIQuest {

        {
            RXlevel = 2;
            image = ItemSpriteSheet.GREEN_DAM_MODEL;
        }

        @Override
        public boolean doPickUp(Hero hero, int pos) {
           if (collect( hero.belongings.backpack )) {
            GameScene.pickUp(this, pos);
            Sample.INSTANCE.play( Assets.Sounds.ITEM );
            hero.spendAndNext( 0f );
            GetScore(hero, 700);
           }
            return true;
        }
    }

    public static class GreenStingModel extends AllSearchIQuest {

        {
            RXlevel = 2;
            image = ItemSpriteSheet.GREEN_SLING_SMALL;
        }

        @Override
        public boolean doPickUp(Hero hero, int pos) {
           if (collect( hero.belongings.backpack )) {
            GameScene.pickUp(this, pos);
            Sample.INSTANCE.play( Assets.Sounds.ITEM );
            hero.spendAndNext( 0f );
            GetScore(hero, 900);
           }
            return true;
        }
    }

    //Low Items
    public static class THEATER_CARDS extends AllSearchIQuest {

        {
            RXlevel = 1;
            image = ItemSpriteSheet.THEATER_CARDS;
        }

        @Override
        public boolean doPickUp(Hero hero, int pos) {
           if (collect( hero.belongings.backpack )) {
            GameScene.pickUp(this, pos);
            Sample.INSTANCE.play( Assets.Sounds.ITEM );
            hero.spendAndNext( 0f );
            GetScore(hero, 500);
           }
            return true;
        }
    }

    public static class HOLLOW_SUGARS extends AllSearchIQuest {

        {
            RXlevel = 1;
            image = ItemSpriteSheet.HOLLOW_SUGAR;
        }

        @Override
        public boolean doPickUp(Hero hero, int pos) {
           if (collect( hero.belongings.backpack )) {
            GameScene.pickUp(this, pos);
            Sample.INSTANCE.play( Assets.Sounds.ITEM );
            hero.spendAndNext( 0f );
            GetScore(hero, 500);
           }
            return true;
        }
    }

    public static class GREEN_PRISM extends AllSearchIQuest {

        {
            RXlevel = 1;
            image = ItemSpriteSheet.GREEN_PRISM;
        }

        @Override
        public boolean doPickUp(Hero hero, int pos) {
           if (collect( hero.belongings.backpack )) {
            GameScene.pickUp(this, pos);
            Sample.INSTANCE.play( Assets.Sounds.ITEM );
            hero.spendAndNext( 0f );
            GetScore(hero, 500);
           }
            return true;
        }
    }

    public static class GNOLL_WOOD extends AllSearchIQuest {

        {
            RXlevel = 1;
            image = ItemSpriteSheet.GNOLL_WOOD;
        }

        @Override
        public boolean doPickUp(Hero hero, int pos) {
           if (collect( hero.belongings.backpack )) {
            GameScene.pickUp(this, pos);
            Sample.INSTANCE.play( Assets.Sounds.ITEM );
            hero.spendAndNext( 0f );
            GetScore(hero, 300);
           }
            return true;
        }
    }

    public static class FOUR_KIDS extends AllSearchIQuest {

        {
            RXlevel = 1;
            image = ItemSpriteSheet.FOUR_KIDS;
        }

        @Override
        public boolean doPickUp(Hero hero, int pos) {
           if (collect( hero.belongings.backpack )) {
               GameScene.pickUp(this, pos);
               Sample.INSTANCE.play(Assets.Sounds.ITEM);
               hero.spendAndNext(0f);
               GetScore(hero, 400);
           }
            return true;
        }
    }

}
