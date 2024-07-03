package com.shatteredpixel.shatteredpixeldungeon.custom.testmode;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Ankh;
import com.shatteredpixel.shatteredpixeldungeon.items.ArcaneResin;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
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
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.LaserPython;
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
import com.shatteredpixel.shatteredpixeldungeon.items.food.CrivusFruitsFood;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Food;
import com.shatteredpixel.shatteredpixeldungeon.items.food.FrozenCarpaccio;
import com.shatteredpixel.shatteredpixeldungeon.items.food.LightFood;
import com.shatteredpixel.shatteredpixeldungeon.items.food.MeatPie;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Pasty;
import com.shatteredpixel.shatteredpixeldungeon.items.food.PhantomMeat;
import com.shatteredpixel.shatteredpixeldungeon.items.food.PotionOfLightningShiled;
import com.shatteredpixel.shatteredpixeldungeon.items.food.RedCrab;
import com.shatteredpixel.shatteredpixeldungeon.items.food.SakaMeat;
import com.shatteredpixel.shatteredpixeldungeon.items.food.SmallRation;
import com.shatteredpixel.shatteredpixeldungeon.items.food.StewedMeat;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Switch;
import com.shatteredpixel.shatteredpixeldungeon.items.lightblack.OilLantern;
import com.shatteredpixel.shatteredpixeldungeon.items.lightblack.OilPotion;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.Potion;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfNoWater;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfPurity;
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
import com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.ElixirOfNukeCole;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.ElixirOfToxicEssence;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.WaterSoul;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.ExoticPotion;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.BlessingNecklace;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.CorpseDust;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.CrivusFruitsFlake;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.DevItem.CrystalLing;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Embers;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.GooBlob;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.MIME;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.MetalShard;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.RandomChest;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Red;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.RedWhiteRose;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.SakaFishSketon;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.Scroll;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfFlameCursed;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ExoticScroll;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfRoseShiled;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Alchemize;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.BeaconOfReturning;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.CurseInfusion;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.MagicalInfusion;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.PhaseShift;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.ReclaimTrap;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Recycle;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Spell;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.SummonElemental;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.TelekineticGrab;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.WildEnergy;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.Runestone;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfFear;
import com.shatteredpixel.shatteredpixeldungeon.items.trinkets.TrinketCatalyst;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.darts.TippedDart;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Plant;
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

    public SpawnMisc(){
        this.cateSelected = 0;
        this.item_quantity = 1;
        this.selected = 0;

        buildList();
    }

    private void buildList(){
        if(potionList.isEmpty()) {
            for (int i = 0; i < Generator.Category.POTION.classes.length; i++) {
                potionList.add((Class<? extends Potion>) Generator.Category.POTION.classes[i]);
            }
            potionList.add(PotionOfPurity.PotionOfPurityLing.class);
        }

        if(exoticPotionList.isEmpty()) {
            for (int i = 0; i < Generator.Category.POTION.classes.length; i++) {
                exoticPotionList.add(ExoticPotion.regToExo.get(potionList.get(i)));
            }
        }

        if(seedList.isEmpty()) {
            for (int i = 0; i < Generator.Category.SEED.classes.length; i++) {
                seedList.add((Class<? extends Plant.Seed>) Generator.Category.SEED.classes[i]);
            }
        }

        if (dartList.isEmpty()) {
            for (int i = 0; i < Generator.Category.SEED.classes.length; i++) {
                dartList.add(TippedDart.types.get(seedList.get(i)));
            }
        }

        if(scrollList.isEmpty()) {
            for (int i = 0; i < Generator.Category.SCROLL.classes.length; i++) {
                scrollList.add((Class<? extends Scroll>) Generator.Category.SCROLL.classes[i]);
            }
            scrollList.add(ScrollOfRoseShiled.class);
            scrollList.add(ScrollOfFlameCursed.class);
        }

        if(exoticScrollList.isEmpty()) {
            for (int i = 0; i < Generator.Category.SCROLL.classes.length; i++) {
                exoticScrollList.add(ExoticScroll.regToExo.get(scrollList.get(i)));
            }
        }


        if(stoneList.isEmpty()) {
            for (int i = 0; i < Generator.Category.STONE.classes.length; i++) {
                stoneList.add((Class<? extends Runestone>) Generator.Category.STONE.classes[i]);
            }
            stoneList.add(StoneOfFear.class);
        }

        //Bomb
        if(bombList.isEmpty()) {
            bombList.add(Bomb.class);
            bombList.add(ArcaneBomb.class);
            bombList.add(Firebomb.class);
            bombList.add(Flashbang.class);
            bombList.add(FrostBomb.class);
            bombList.add(HolyBomb.class);
            bombList.add(Noisemaker.class);
            bombList.add(RegrowthBomb.class);
            bombList.add(ShockBomb.class);
            bombList.add(ShrapnelBomb.class);
            bombList.add(WoollyBomb.class);
            bombList.add(LaserPython.class);
        }

        //Brew
        if(brewList.isEmpty()) {
            brewList.add(BlizzardBrew.class);
            brewList.add(CausticBrew.class);
            brewList.add(InfernalBrew.class);
            brewList.add(ShockingBrew.class);
            brewList.add(ElixirOfAquaticRejuvenation.class);
            brewList.add(ElixirOfArcaneArmor.class);
            brewList.add(ElixirOfDragonsBlood.class);
            brewList.add(ElixirOfHoneyedHealing.class);
            brewList.add(ElixirOfIcyTouch.class);
            brewList.add(ElixirOfMight.class);
            brewList.add(ElixirOfToxicEssence.class);

            brewList.add(WaterSoul.class);
            brewList.add(ElixirOfNukeCole.class);
        }

        //Spell
        if(spellList.isEmpty()) {
            spellList.add(Alchemize.class);
            //spellList.add(AquaBlast.class);
            spellList.add(BeaconOfReturning.class);
            spellList.add(CurseInfusion.class);
            //spellList.add(FeatherFall.class);
            spellList.add(MagicalInfusion.class);
            spellList.add(TelekineticGrab.class);
            spellList.add(PhaseShift.class);
            spellList.add(ReclaimTrap.class);
            spellList.add(Recycle.class);
            spellList.add(WildEnergy.class);
            spellList.add(SummonElemental.class);
            //spellList.add(ArcaneCatalyst.class);
        }

        if(foodList.isEmpty()) {
            for (int i = 0; i < Generator.Category.FOOD.classes.length; i++) {
                foodList.add((Class<? extends Food>) Generator.Category.FOOD.classes[i]);
            }
            foodList.add(SmallRation.class);
            foodList.add(MeatPie.class);
            foodList.add(Blandfruit.class);
            foodList.add(StewedMeat.class);
            foodList.add(FrozenCarpaccio.class);
            foodList.add(ChargrilledMeat.class);
            foodList.add(Berry.class);
            foodList.add(LightFood.class);
            foodList.add(Cake.class);
            foodList.add(Switch.class);
            foodList.add(RedCrab.class);
            foodList.add(Pasty.FishLeftover.class);
            foodList.add(PhantomMeat.class);
            foodList.add(CrivusFruitsFood.class);
            foodList.add(SakaMeat.class);
        }

        //Book
        if(bookList.isEmpty()) {
            bookList.add(MagicGirlBooks.class);
            bookList.add(BrokenBooks.class);
            bookList.add(GrassKingBooks.class);
            bookList.add(IceCityBooks.class);
            bookList.add(NoKingMobBooks.class);
            bookList.add(HollowCityBook.class);
            bookList.add(DeepBloodBooks.class);
            bookList.add(DimandBook.class);
            bookList.add(DeYiZiBooks.class);
            bookList.add(MoneyMoreBooks.class);
            bookList.add(PinkRandomBooks.class);
            bookList.add(HellFireBooks.class);
            bookList.add(YellowSunBooks.class);
        }

        //Misc
        if(miscList.isEmpty()) {
            miscList.add(TrinketCatalyst.class);
            miscList.add(Torch.class);
            miscList.add(GooBlob.class);
            miscList.add(MetalShard.class);
            miscList.add(Honeypot.class);
            miscList.add(Ankh.class);
            miscList.add(Waterskin.class);
            miscList.add(Stylus.class);
            miscList.add(KingsCrown.class);
            miscList.add(TengusMask.class);
            miscList.add(LiquidMetal.class);
            miscList.add(ArcaneResin.class);
            miscList.add(Embers.class);
            miscList.add(CorpseDust.class);
            miscList.add(PotionOfNoWater.class);
            miscList.add(PotionOfLightningShiled.class);
            miscList.add(MIME.GOLD_ONE.class);
            miscList.add(MIME.GOLD_TWO.class);
            miscList.add(MIME.GOLD_THREE.class);
            miscList.add(MIME.GOLD_FOUR.class);
            miscList.add(MIME.GOLD_FIVE.class);
            miscList.add(TestBooks.class);
            miscList.add(BlessingNecklace.class);
            miscList.add(OilPotion.class);
            miscList.add(OilLantern.class);
            miscList.add(CrivusFruitsFlake.class);
            miscList.add(SakaFishSketon.class);
            miscList.add(RandomChest.class);
            miscList.add(Red.class);
            miscList.add(CrystalLing.class);
            miscList.add(RedWhiteRose.class);
        }
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
            case 0: return potionList.get(id);
            case 1: return exoticPotionList.get(id);
            case 2: return seedList.get(id);
            case 3: return dartList.get(id);
            case 4: return scrollList.get(id);
            case 5: return exoticScrollList.get(id);
            case 6: return stoneList.get(id);
            case 7: return bombList.get(id);
            case 8: return brewList.get(id);
            case 9: return spellList.get(id);
            case 10: return foodList.get(id);
            case 11: return bookList.get(id);
            case 12: default: return miscList.get(id);
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

    private int maxIndex(int cate){
        switch (cate){
            case 0:
                return potionList.size() - 1;
            case 1:
                return exoticPotionList.size() - 1;
            case 2:
                return seedList.size() - 1;
            case 3:
                return dartList.size() - 1;
            case 4:
                return scrollList.size() - 1;
            case 5:
                return exoticScrollList.size() - 1;
            case 6:
                return stoneList.size() - 1;
            case 7:
                return bombList.size() - 1;
            case 8:
                return brewList.size() - 1;
            case 9:
                return spellList.size() - 1;
            case 10:
                return foodList.size() - 1;
            case 11:
                return bookList.size() - 1;
            case 12:
                return miscList.size() - 1;
            default:
                return 11;
        }
    }

    private int maxCategory(){
        return 12;
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
                                Messages.get(SpawnMisc.SettingsWindow.class, "cancel"),false) {
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
            RedButton_quantity.text(Messages.get(this, "item_quantity",item == Pasty.class?new Pasty().name():Messages.get(item, "name"),item_quantity));
            //layout();
        }
    }
}
