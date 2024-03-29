package com.shatteredpixel.shatteredpixeldungeon.custom.testmode;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Ankh;
import com.shatteredpixel.shatteredpixeldungeon.items.ArcaneResin;
import com.shatteredpixel.shatteredpixeldungeon.items.Honeypot;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.KingsCrown;
import com.shatteredpixel.shatteredpixeldungeon.items.LiquidMetal;
import com.shatteredpixel.shatteredpixeldungeon.items.Stylus;
import com.shatteredpixel.shatteredpixeldungeon.items.TengusMask;
import com.shatteredpixel.shatteredpixeldungeon.items.Torch;
import com.shatteredpixel.shatteredpixeldungeon.items.Waterskin;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.ArcaneBomb;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Bomb;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Firebomb;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Flashbang;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.FrostBomb;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.HolyBomb;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Noisemaker;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.RegrowthBomb;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.ShockBomb;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.ShrapnelBomb;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.WoollyBomb;
import com.shatteredpixel.shatteredpixeldungeon.items.books.Books;
import com.shatteredpixel.shatteredpixeldungeon.items.books.bookslist.BrokenBooks;
import com.shatteredpixel.shatteredpixeldungeon.items.books.bookslist.DeepBloodBooks;
import com.shatteredpixel.shatteredpixeldungeon.items.books.bookslist.GrassKingBooks;
import com.shatteredpixel.shatteredpixeldungeon.items.books.bookslist.HellFireBooks;
import com.shatteredpixel.shatteredpixeldungeon.items.books.bookslist.IceCityBooks;
import com.shatteredpixel.shatteredpixeldungeon.items.books.bookslist.MagicGirlBooks;
import com.shatteredpixel.shatteredpixeldungeon.items.books.bookslist.NoKingMobBooks;
import com.shatteredpixel.shatteredpixeldungeon.items.books.bookslist.TestBooks;
import com.shatteredpixel.shatteredpixeldungeon.items.books.bookslist.YellowSunBooks;
import com.shatteredpixel.shatteredpixeldungeon.items.books.playbookslist.DeYiZiBooks;
import com.shatteredpixel.shatteredpixeldungeon.items.books.playbookslist.MoneyMoreBooks;
import com.shatteredpixel.shatteredpixeldungeon.items.books.playbookslist.PinkRandomBooks;
import com.shatteredpixel.shatteredpixeldungeon.items.books.questbookslist.DimandBook;
import com.shatteredpixel.shatteredpixeldungeon.items.books.questbookslist.HollowCityBook;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Berry;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Blandfruit;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Cake;
import com.shatteredpixel.shatteredpixeldungeon.items.food.ChargrilledMeat;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Food;
import com.shatteredpixel.shatteredpixeldungeon.items.food.FrozenCarpaccio;
import com.shatteredpixel.shatteredpixeldungeon.items.food.LightFood;
import com.shatteredpixel.shatteredpixeldungeon.items.food.MysteryMeat;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Pasty;
import com.shatteredpixel.shatteredpixeldungeon.items.food.PhantomMeat;
import com.shatteredpixel.shatteredpixeldungeon.items.food.RiceDumplings;
import com.shatteredpixel.shatteredpixeldungeon.items.food.SmallRation;
import com.shatteredpixel.shatteredpixeldungeon.items.food.StewedMeat;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Switch;
import com.shatteredpixel.shatteredpixeldungeon.items.lightblack.OilPotion;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.AlchemicalCatalyst;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.Potion;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfExperience;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfFrost;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHaste;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfInvisibility;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfLevitation;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfLightningShiledX;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfLiquidFlame;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfLiquidFlameX;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfMindVision;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfNoWater;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfParalyticGas;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfPurity;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfStrength;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.brews.BlizzardBrew;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.brews.CausticBrew;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.brews.InfernalBrew;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.brews.ShockingBrew;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.ElixirOfAquaticRejuvenation;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.ElixirOfArcaneArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.ElixirOfDragonsBlood;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.ElixirOfHoneyedHealing;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.ElixirOfIcyTouch;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.ElixirOfMight;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.ElixirOfToxicEssence;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.WaterSoul;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.ExoticPotion;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.CorpseDust;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Embers;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.GooBlob;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.MIME;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.MetalShard;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.Scroll;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfFlameCursed;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfIdentify;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfLullaby;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfMagicMapping;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfMirrorImage;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRage;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRecharging;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRemoveCurse;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRetribution;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTerror;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTransmutation;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ExoticScroll;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfRoseShiled;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Alchemize;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.AquaBlast;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.ArcaneCatalyst;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.BeaconOfReturning;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.CurseInfusion;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.FeatherFall;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.MagicalInfusion;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.PhaseShift;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.ReclaimTrap;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Recycle;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Spell;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.SummonElemental;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.TelekineticGrab;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.WildEnergy;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.Runestone;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfAggression;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfAugmentation;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfBlast;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfBlink;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfClairvoyance;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfDeepSleep;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfDisarming;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfEnchantment;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfFear;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfFlock;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfIntuition;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfShock;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.darts.AdrenalineDart;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.darts.BlindingDart;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.darts.ChillingDart;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.darts.CleansingDart;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.darts.DisplacingDart;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.darts.HealingDart;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.darts.HolyDart;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.darts.IncendiaryDart;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.darts.ParalyticDart;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.darts.PoisonDart;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.darts.RotDart;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.darts.ShockingDart;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.darts.TippedDart;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.AikeLaier;
import com.shatteredpixel.shatteredpixeldungeon.plants.Blindweed;
import com.shatteredpixel.shatteredpixeldungeon.plants.Earthroot;
import com.shatteredpixel.shatteredpixeldungeon.plants.Fadeleaf;
import com.shatteredpixel.shatteredpixeldungeon.plants.Firebloom;
import com.shatteredpixel.shatteredpixeldungeon.plants.Icecap;
import com.shatteredpixel.shatteredpixeldungeon.plants.Mageroyal;
import com.shatteredpixel.shatteredpixeldungeon.plants.Plant;
import com.shatteredpixel.shatteredpixeldungeon.plants.Rotberry;
import com.shatteredpixel.shatteredpixeldungeon.plants.SkyBlueFireBloom;
import com.shatteredpixel.shatteredpixeldungeon.plants.Sorrowmoss;
import com.shatteredpixel.shatteredpixeldungeon.plants.Starflower;
import com.shatteredpixel.shatteredpixeldungeon.plants.Stormvine;
import com.shatteredpixel.shatteredpixeldungeon.plants.Sungrass;
import com.shatteredpixel.shatteredpixeldungeon.plants.Swiftthistle;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.IconButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.utils.WndTextNumberInput;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndError;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Reflection;

import java.util.ArrayList;
import java.util.Objects;

public class SpawnMisc extends TestItem {
    {
        image = ItemSpriteSheet.POTION_HOLDER;
        defaultAction = AC_SPAWN;
    }

    private int cateSelected;
    private int item_quantity;
    private int selected;

    private static final String AC_SPAWN = "spawn";

    public SpawnMisc(){
        this.cateSelected = 0;
        this.item_quantity = 1;
        this.selected = 0;
    }

    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.add(AC_SPAWN);
        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {
        super.execute(hero, action);
        if (action.equals(AC_SPAWN)) {
            GameScene.show(new SettingsWindow());
        }
    }

    private void createItem(){
        boolean collect = false;
        Item item = Reflection.newInstance(idToItem(selected));
        if(Challenges.isItemBlocked(item)) return;
        if (item != null) {
            if(item.stackable){
                collect = item.quantity(item_quantity).collect();
            }
            else collect = item.collect();
            item.identify();
            if(collect){
                GLog.i(Messages.get(hero, "you_now_have", item.name()));
                Sample.INSTANCE.play( Assets.Sounds.ITEM );
                GameScene.pickUp( item, hero.pos );
            }else{
                item.doDrop(curUser);
            }
        }
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put("item_quantity", item_quantity);
        bundle.put("selected", selected);
        bundle.put("cate_selected", cateSelected);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        item_quantity = bundle.getInt("item_quantity");
        selected = bundle.getInt("selected");
        cateSelected = bundle.getInt("cate_selected");
    }

    private Class<? extends Item> idToItem(int id){
        switch (cateSelected){
            case 0: return idToPotion(id);
            case 1: return idToExoticPotion(id);
            case 2: return idToSeed(id);
            case 3: return idToTippedDart(id);
            case 4: return idToScroll(id);
            case 5: return idToExoticScroll(id);
            case 6: return idToStone(id);
            case 7: return idToBomb(id);
            case 8: return idToSpecialPotion(id);
            case 9: return idToSpell(id);
            case 10: return idToFood(id);
            case 11: return idToBook(id);
            case 12: default: return idToMisc(id);
        }
    }

    private int idToCategoryImage(int selected){
        switch (selected){
            case 0: return ItemSpriteSheet.POTION_AZURE;
            case 1: return ItemSpriteSheet.EXOTIC_AZURE;
            case 2: return ItemSpriteSheet.SEED_ICECAP;
            case 3: return ItemSpriteSheet.CHILLING_DART;
            case 4: return ItemSpriteSheet.SCROLL_LAGUZ;
            case 5: return ItemSpriteSheet.EXOTIC_LAGUZ;
            case 6: return ItemSpriteSheet.STONE_AUGMENTATION;
            case 7: return ItemSpriteSheet.BOMB;
            case 8: return ItemSpriteSheet.BREW_CAUSTIC;
            case 9: return ItemSpriteSheet.PHASE_SHIFT;
            case 10: return ItemSpriteSheet.RATION;
            case 11: return ItemSpriteSheet.MASTERY;
            case 12: default: return ItemSpriteSheet.CHEST;
        }
    }

    private Class<? extends Potion> idToPotion(int id) {
        switch (id) {
            case 0:
                return PotionOfExperience.class;
            case 1:
                return PotionOfFrost.class;
            case 2:
                return PotionOfHaste.class;
            case 3:
                return PotionOfHealing.class;
            case 4:
                return PotionOfInvisibility.class;
            case 5:
                return PotionOfLevitation.class;
            case 6:
                return PotionOfLiquidFlame.class;
            case 7:
                return PotionOfMindVision.class;
            case 8:
                return PotionOfParalyticGas.class;
            case 9:
                return PotionOfPurity.class;
            case 10:
                return PotionOfStrength.class;
            case 11:
                return PotionOfLiquidFlameX.class;
            case 12:
                return PotionOfLightningShiledX.class;
            case 13:
            default:
                return PotionOfToxicGas.class;
        }
    }

    private Class<? extends ExoticPotion> idToExoticPotion(int id) {
        return ExoticPotion.regToExo.get((idToPotion(id)));
    }

    private Class<? extends Plant.Seed> idToSeed(int id) {
        switch (id) {
            case 0:
                return Starflower.Seed.class;
            case 1:
                return Icecap.Seed.class;
            case 2:
                return Swiftthistle.Seed.class;
            case 3:
                return Sungrass.Seed.class;
            case 4:
                return Blindweed.Seed.class;
            case 5:
                return Stormvine.Seed.class;
            case 6:
                return Firebloom.Seed.class;
            case 7:
                return Fadeleaf.Seed.class;
            case 8:
                return Earthroot.Seed.class;
            case 9:
                return Mageroyal.Seed.class;
            case 10:
                return Rotberry.Seed.class;
            case 11:
                return SkyBlueFireBloom.Seed.class;
            case 12:
                return AikeLaier.Seed.class;
            case 13:
            default:
                return Sorrowmoss.Seed.class;
        }
    }

    private Class<? extends TippedDart> idToTippedDart(int id){
        switch (id){
            case 0: return HolyDart.class;
            case 1: return ChillingDart.class;
            case 2: return AdrenalineDart.class;
            case 3: return HealingDart.class;
            case 4: return BlindingDart.class;
            case 5: return ShockingDart.class;
            case 6: return IncendiaryDart.class;
            case 7: return DisplacingDart.class;
            case 8: return ParalyticDart.class;
            case 9: return CleansingDart.class;
            case 10: return RotDart.class;
            case 11: default: return PoisonDart.class;
        }
    }

    private Class<? extends Scroll> idToScroll(int id) {
        switch (id) {
            case 0:
                return ScrollOfIdentify.class;
            case 1:
                return ScrollOfLullaby.class;
            case 2:
                return ScrollOfMagicMapping.class;
            case 3:
                return ScrollOfMirrorImage.class;
            case 4:
                return ScrollOfRage.class;
            case 5:
                return ScrollOfRecharging.class;
            case 6:
                return ScrollOfRetribution.class;
            case 7:
                return ScrollOfRemoveCurse.class;
            case 8:
                return ScrollOfTeleportation.class;
            case 9:
                return ScrollOfTerror.class;
            case 10:
                return ScrollOfTransmutation.class;
            case 11:
                return ScrollOfUpgrade.class;
            case 12:
                return ScrollOfRoseShiled.class;
            case 13:
                return ScrollOfFlameCursed.class;
            default:
                return null;
        }
    }

    private Class<? extends ExoticScroll> idToExoticScroll(int id) {
            return ExoticScroll.regToExo.get(idToScroll(id));
    }

    private Class<? extends Runestone> idToStone(int id) {
        switch (id) {
            case 0:
                return StoneOfIntuition.class;
            case 1:
                return StoneOfFear.class;
            case 2:
                return StoneOfClairvoyance.class;
            case 3:
                return StoneOfFlock.class;
            case 4:
                return StoneOfAggression.class;
            case 5:
                return StoneOfShock.class;
            case 6:
                return StoneOfBlast.class;
            case 7:
                return StoneOfDisarming.class;
            case 8:
                return StoneOfBlink.class;
            case 9:
                return StoneOfDeepSleep.class;
            case 10:
                return StoneOfAugmentation.class;
            case 11:
            default:
                return StoneOfEnchantment.class;
        }
    }

    private Class<? extends Item> idToBomb(int id){
        switch (id){
            case 0: return Bomb.class;
            case 1: return ArcaneBomb.class;
            case 2: return Firebomb.class;
            case 3: return Flashbang.class;
            case 4: return FrostBomb.class;
            case 5: return HolyBomb.class;
            case 6: return Noisemaker.class;
            case 7: return RegrowthBomb.class;
            case 8: return ShockBomb.class;
            case 9: return ShrapnelBomb.class;
            case 10: default: return WoollyBomb.class;
        }
    }

    private Class<? extends Potion> idToSpecialPotion(int id){
        switch (id){
            case 0: return BlizzardBrew.class;
            case 1: return CausticBrew.class;
            case 2: return InfernalBrew.class;
            case 3: return ShockingBrew.class;
            case 4: return ElixirOfAquaticRejuvenation.class;
            case 5: return ElixirOfArcaneArmor.class;
            case 6: return ElixirOfDragonsBlood.class;
            case 7: return ElixirOfHoneyedHealing.class;
            case 8: return ElixirOfIcyTouch.class;
            case 9: return ElixirOfMight.class;
            case 10: return ElixirOfToxicEssence.class;
            case 11: return AlchemicalCatalyst.class;
            case 12: return WaterSoul.class;
            default: return null;
        }
    }

    private Class<? extends Spell> idToSpell(int id){
        switch (id){
            case 0: return Alchemize.class;
            case 1: return AquaBlast.class;
            case 2: return BeaconOfReturning.class;
            case 3: return CurseInfusion.class;
            case 4: return FeatherFall.class;
            case 5: return MagicalInfusion.class;
            case 6: return TelekineticGrab.class;
            case 7: return PhaseShift.class;
            case 8: return ReclaimTrap.class;
            case 9: return Recycle.class;
            case 10: return WildEnergy.class;
            case 11: return SummonElemental.class;
            case 12: default: return ArcaneCatalyst.class;
        }
    }

    private Class<? extends Food> idToFood(int id){
        switch (id){
            case 0: return Food.class;
            case 1: return SmallRation.class;
            case 2: return Pasty.class;
            case 3: return Blandfruit.class;
            case 4: return MysteryMeat.class;
            case 5: return StewedMeat.class;
            case 6: return FrozenCarpaccio.class;
            case 7: return ChargrilledMeat.class;
            case 8: return Berry.class;
            case 9: return LightFood.class;
            case 10: return Cake.class;
            case 11: return Switch.class;
            case 12: return RiceDumplings.RiceDumplingsRed.class;
            case 13: return RiceDumplings.RiceDumplingsPink.class;
            case 14: return RiceDumplings.RiceDumplingsOrange.class;
            case 15: return RiceDumplings.RiceDumplingsLink.class;
            case 16: return RiceDumplings.RiceDumplingsBottle.class;
            default:
            case 17: return PhantomMeat.class;
        }
    }

    private Class<? extends Item> idToMisc(int id){
        switch (id){
            case 0: return Torch.class;
            case 1: return GooBlob.class;
            case 2: return MetalShard.class;
            case 3: return Honeypot.class;
            case 4: return Ankh.class;
            case 5: return Waterskin.class;
            case 6: return Stylus.class;
            case 7: default: return KingsCrown.class;
            case 8: return TengusMask.class;
            case 9: return LiquidMetal.class;
            case 10: return ArcaneResin.class;
            case 11: return Embers.class;
            case 12: return CorpseDust.class;
            case 13: return PotionOfNoWater.class;
            case 14: return MIME.GOLD_ONE.class;
            case 15: return MIME.GOLD_TWO.class;
            case 16: return MIME.GOLD_THREE.class;
            case 17: return MIME.GOLD_FOUR.class;
            case 18: return MIME.GOLD_FIVE.class;
            case 19: return OilPotion.class;

            case 20: return TestBooks.class;
        }
    }

    private Class<? extends Books> idToBook(int id){
        switch (id){
            case 0: return MagicGirlBooks.class;
            case 1: return BrokenBooks.class;
            case 2: return GrassKingBooks.class;
            case 3: return IceCityBooks.class;
            case 4: return NoKingMobBooks.class;
            case 5: return HollowCityBook.class;
            case 6: return DeepBloodBooks.class;
            case 7: return DimandBook.class;
            case 8: return DeYiZiBooks.class;
            case 9: return MoneyMoreBooks.class;
            case 10: return PinkRandomBooks.class;
            case 11: return  HellFireBooks.class;
            default:
            case 12: return YellowSunBooks.class;
        }
    }

    private int maxIndex(int cate){
        switch (cate){
            case 0:
                return 13;
            case 1:
                return 13;
            case 2:
                return 13;
            case 4:
                return 13;
            case 7:
                return 10;
            case 8:
                return 12;
            case 9:
                return 12;
            case 10:
                return 17;
            case 11:
                return 12;
            case 12:
                return 20;
            default:
                return 11;
        }
    }

    private int maxCategory(){
        return 12;
    }

    private static ArrayList<Class<? extends Potion>> potionList = new ArrayList<>();
    private static ArrayList<Class<? extends ExoticPotion>> exoticPotionList = new ArrayList<>();
    private static ArrayList<Class<? extends Plant.Seed>> seedList = new ArrayList<>();
    private static ArrayList<Class<? extends Scroll>> scrollList = new ArrayList<>();
    private static ArrayList<Class<? extends ExoticScroll>> exoticScrollList = new ArrayList<>();
    private static ArrayList<Class<? extends Runestone>> stoneList = new ArrayList<>();
    private static ArrayList<Class<? extends TippedDart>> dartList = new ArrayList<>();
    private static ArrayList<Class<? extends Item>> bombList = new ArrayList<>();
    private static ArrayList<Class<? extends Potion>> brewList = new ArrayList<>();
    private static ArrayList<Class<? extends Spell>> spellList = new ArrayList<>();
    private static ArrayList<Class<? extends Food>> foodList = new ArrayList<>();
    private static ArrayList<Class<? extends Books>> bookList = new ArrayList<>();
    private static ArrayList<Class<? extends Item>> miscList = new ArrayList<>();

    private void buildList() {
        if (potionList.isEmpty()) {
            for (int i = 0; i < maxIndex(0)+1; ++i) {
                potionList.add(idToPotion(i));
            }
        }
        if (exoticPotionList.isEmpty()) {
            for (int i = 0; i < maxIndex(1)+1; ++i) {
                exoticPotionList.add(idToExoticPotion(i));
            }
        }

        if (seedList.isEmpty()) {
            for (int i = 0; i < maxIndex(2)+1; ++i) {
                seedList.add(idToSeed(i));
            }
        }

        if(dartList.isEmpty()){
            for(int i=0; i<maxIndex(3)+1; ++i){
                dartList.add(idToTippedDart(i));
            }
        }

        if (scrollList.isEmpty()) {
            for (int i = 0; i < maxIndex(4)+1; ++i) {
                scrollList.add(idToScroll(i));
            }
        }

        if (exoticScrollList.isEmpty()) {
            for (int i = 0; i < maxIndex(5)+1; ++i) {
                exoticScrollList.add(idToExoticScroll(i));
            }
        }

        if (stoneList.isEmpty()) {
            for (int i = 0; i < maxIndex(6)+1; ++i) {
                stoneList.add(idToStone(i));
            }
        }

        if(bombList.isEmpty()){
            for(int i=0; i<maxIndex(7)+1; ++i){
                bombList.add(idToBomb(i));
            }
        }

        if(brewList.isEmpty()){
            for(int i=0; i<maxIndex(8)+1; ++i){
                brewList.add(idToSpecialPotion(i));
            }
        }

        if(spellList.isEmpty()){
            for(int i=0; i<maxIndex(9)+1; ++i){
                spellList.add(idToSpell(i));
            }
        }

        if(foodList.isEmpty()){
            for(int i=0; i<maxIndex(10)+1; ++i){
                foodList.add(idToFood(i));
            }
        }

        if(bookList.isEmpty()){
            for(int i=0; i<maxIndex(11)+1; ++i){
                bookList.add(idToBook(i));
            }
        }

        if(miscList.isEmpty()){
            for(int i=0; i<maxIndex(12)+1; ++i){
                miscList.add(idToMisc(i));
            }
        }
    }

    private class SettingsWindow extends Window {
        private final RedButton RedButton_quantity;
        private RedButton RedButton_create;
        private ArrayList<IconButton> buttonList = new ArrayList<>();
        private ArrayList<IconButton> cateButtonList = new ArrayList<>();
        private static final int WIDTH = 170;
        private static final int BTN_SIZE = 16;
        private static final int GAP = 2;
        private static final int TITLE_BTM = 8;

        public SettingsWindow() {
            buildList();

            createCategoryImage();

            createImage();

            RedButton_quantity = new RedButton(Messages.get(this, "select_item")) {
                @Override
                protected void onClick() {
                    if(!RedButton_quantity.text().equals(Messages.get(SpawnMisc.SettingsWindow.class, "select_item"))){ // 修改此行代码
                        Game.runOnRenderThread(() ->GameScene.show(new WndTextNumberInput(
                                Messages.get(SpawnMisc.SettingsWindow.class, "item_level"), Messages.get(SpawnMisc.SettingsWindow.class, "item_level_desc"),
                                Integer.toString(item_quantity),
                                4, false, Messages.get(SpawnMisc.SettingsWindow.class, "confirm"),
                                Messages.get(SpawnMisc.SettingsWindow.class, "cancel")) {
                            @Override
                            public void onSelect(boolean check, String text) {
                                if (check && item_quantity > 0 &&text.matches("^[1-9]\\d*$")) {
                                    int quantity = Integer.parseInt(text);
                                    if(Reflection.newInstance(idToItem(selected)).stackable)
                                        item_quantity = Math.min(quantity, 6666);
                                    else
                                        item_quantity = 1;
                                }
                                updateText();
                            }
                        }));
                    } else {
                        Game.scene().add( new WndError( Messages.get(SpawnMisc.SettingsWindow.class, "item_level_error") ) );
                    }
                }
            };
            updateText();
            add(RedButton_quantity);

            RedButton_create = new RedButton(Messages.get(this, "create_button")) {
                @Override
                protected void onClick() {
                    createItem();
                    updateText();
                }
            };
            add(RedButton_create);

            WindowLayout();
        }

        private void WindowLayout() {
            RedButton_quantity.setRect(0, buttonList.get(buttonList.size() - 1).bottom() + GAP + 2 * GAP, WIDTH, 24);
            RedButton_create.setRect(0, RedButton_quantity.bottom() + GAP, WIDTH, 16);
            resize(WIDTH, (int) RedButton_create.bottom());
        }

        private void createCategoryImage(){
            float left;
            float top = GAP + TITLE_BTM;
            int length = 13;
            int maxImageCount = 6;
            for (int i = 0; i < length; ++i) {
                final int j = i;
                IconButton btn = new IconButton() {
                    @Override
                    protected void onClick() {
                        cateButtonList.get(cateSelected).icon().resetColor();
                        cateSelected = Math.min(j, maxCategory());
                        if(selected > maxIndex(cateSelected)) selected = maxIndex(cateSelected);
                        cateButtonList.get(cateSelected).icon().hardlight(0xffbf00);
                        updateImage();
                        updateText();
                        WindowLayout();
                        super.onClick();
                    }
                };
                Image im =  new Image(Assets.Sprites.ITEMS);
                im.frame(ItemSpriteSheet.film.get(idToCategoryImage(i)));
                im.scale.set(1.0f);
                btn.icon(im);

                int line = i / maxImageCount;
                left = (WIDTH - BTN_SIZE * maxImageCount) / 2f;
                btn.setRect(left + (i - maxImageCount * line) * BTN_SIZE, top + GAP * line + BTN_SIZE * line, BTN_SIZE, BTN_SIZE);

                add(btn);
                cateButtonList.add(btn);
            }
        }

        private void createImage() {
            float left;
            float top = GAP + cateButtonList.get(cateButtonList.size() - 1 ).bottom() + 2 * GAP;
            int length = maxIndex(cateSelected)+1;
            int maxImageCount = 6;
            for (int i = 0; i < length; ++i) {
                final int j = i;
                IconButton btn = new IconButton() {
                    @Override
                    protected void onClick() {
                        selected = Math.min(j, maxIndex(cateSelected));
                        updateText();
                        super.onClick();
                    }
                };
                switch (cateSelected){
                    case 0 :{
                        Image im = new Image(Assets.Sprites.ITEM_ICONS);
                        im.frame(ItemSpriteSheet.Icons.film.get(Objects.requireNonNull(Reflection.newInstance(potionList.get(i))).icon));
                        im.scale.set(1.6f);
                        btn.icon(im);
                    } break;
                    case 1:{
                        Image im = new Image(Assets.Sprites.ITEM_ICONS);
                        im.frame(ItemSpriteSheet.Icons.film.get(Objects.requireNonNull(Reflection.newInstance(exoticPotionList.get(i))).icon));
                        im.scale.set(1.6f);
                        btn.icon(im);
                    } break;
                    case 2:{
                        Image im = new Image(Assets.Sprites.ITEMS);
                        im.frame(ItemSpriteSheet.film.get(Objects.requireNonNull(Reflection.newInstance(seedList.get(i))).image));
                        im.scale.set(1.0f);
                        btn.icon(im);
                    } break;
                    case 3:{
                        Image im = new Image(Assets.Sprites.ITEMS);
                        im.frame(ItemSpriteSheet.film.get(Objects.requireNonNull(Reflection.newInstance(dartList.get(i))).image));
                        im.scale.set(1.0f);
                        btn.icon(im);
                    } break;
                    case 4:{
                        Image im;
                        if(i<=11) {
                            im = new Image(Assets.Sprites.ITEM_ICONS);
                            im.frame(ItemSpriteSheet.Icons.film.get(Objects.requireNonNull(Reflection.newInstance(scrollList.get(i))).icon));
                            im.scale.set(1.6f);
                        }
                        else {
                            im = new Image(Assets.Sprites.ITEMS);
                            im.frame(ItemSpriteSheet.film.get(Objects.requireNonNull(Reflection.newInstance(scrollList.get(i))).image));
                            im.scale.set(1.0f);
                        }
                        btn.icon(im);
                    }break;
                    case 5:{
                        Image im = new Image(Assets.Sprites.ITEM_ICONS);
                        im.frame(ItemSpriteSheet.Icons.film.get(Objects.requireNonNull(Reflection.newInstance(exoticScrollList.get(i))).icon));
                        im.scale.set(1.6f);
                        btn.icon(im);
                    } break;
                    case 6:{
                        Image im = new Image(Assets.Sprites.ITEMS);
                        im.frame(ItemSpriteSheet.film.get(Objects.requireNonNull(Reflection.newInstance(stoneList.get(i))).image));
                        im.scale.set(1.0f);
                        btn.icon(im);
                    } break;
                    case 7:{
                        Image im = new Image(Assets.Sprites.ITEMS);
                        im.frame(ItemSpriteSheet.film.get(Objects.requireNonNull(Reflection.newInstance(bombList.get(i))).image));
                        im.scale.set(1.0f);
                        btn.icon(im);
                    } break;
                    case 8:{
                        Image im = new Image(Assets.Sprites.ITEMS);
                        im.frame(ItemSpriteSheet.film.get(Objects.requireNonNull(Reflection.newInstance(brewList.get(i))).image));
                        im.scale.set(1.0f);
                        btn.icon(im);
                    } break;
                    case 9: {
                        Image im = new Image(Assets.Sprites.ITEMS);
                        im.frame(ItemSpriteSheet.film.get(Objects.requireNonNull(Reflection.newInstance(spellList.get(i))).image));
                        im.scale.set(1.0f);
                        btn.icon(im);
                    } break;
                    case 10: {
                        Image im = new Image(Assets.Sprites.ITEMS);
                        im.frame(ItemSpriteSheet.film.get(Objects.requireNonNull(Reflection.newInstance(foodList.get(i))).image));
                        im.scale.set(1.0f);
                        btn.icon(im);
                    } break;
                    case 11:{
                        Image im = new Image(Assets.Sprites.ITEMS);
                        im.frame(ItemSpriteSheet.film.get(Objects.requireNonNull(Reflection.newInstance(bookList.get(i))).image));
                        im.scale.set(1.0f);
                        btn.icon(im);
                    } break;
                    case 12: default:{
                        Image im = new Image(Assets.Sprites.ITEMS);
                        im.frame(ItemSpriteSheet.film.get(Objects.requireNonNull(Reflection.newInstance(miscList.get(i))).image));
                        im.scale.set(1.0f);
                        btn.icon(im);
                    }
                }

                left = (WIDTH - BTN_SIZE * maxImageCount) / 2f;
                int line = i / maxImageCount;
                btn.setRect(left + (i - maxImageCount * line) * BTN_SIZE, top + GAP * line + BTN_SIZE * line, BTN_SIZE, BTN_SIZE);

                add(btn);
                buttonList.add(btn);
            }
        }

        private void clearImage() {
            for (IconButton button : buttonList.toArray(new IconButton[0])) {
                button.destroy();
            }
        }

        private void updateImage() {
            clearImage();
            createImage();
        }

        private void updateText() {
            Class<? extends Item> item = idToItem(selected);
            if(!Reflection.newInstance(item).stackable)
                item_quantity = 1;
            RedButton_quantity.text(Messages.get(this, "item_quantity",Messages.get(item, "name"),item_quantity));
            //layout();
        }
    }
}
