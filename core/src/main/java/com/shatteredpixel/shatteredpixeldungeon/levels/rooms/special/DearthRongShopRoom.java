package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special;

import static com.shatteredpixel.shatteredpixeldungeon.Challenges.DHXD;
import static com.shatteredpixel.shatteredpixeldungeon.Statistics.lanterfireactive;

import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Conducts;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Belongings;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.hollow.DeathRongShop;
import com.shatteredpixel.shatteredpixeldungeon.items.Ankh;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Honeypot;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TimekeepersHourglass;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.Bag;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.MagicalHolster;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.PotionBandolier;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.ScrollHolder;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.VelvetPouch;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Firebomb;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Flashbang;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.FrostBomb;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.HolyBomb;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Noisemaker;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.RegrowthBomb;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Food;
import com.shatteredpixel.shatteredpixeldungeon.items.food.SmallRation;
import com.shatteredpixel.shatteredpixeldungeon.items.food.hollow.Sugar;
import com.shatteredpixel.shatteredpixeldungeon.items.lightblack.OilPotion;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.brews.BlizzardBrew;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.brews.CausticBrew;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.brews.InfernalBrew;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.brews.ShockingBrew;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.brews.UnstableBrew;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.ElixirOfMight;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.WaterSoul;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.*;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.hollow.StarCrystal;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfIdentify;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfMagicMapping;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRemoveCurse;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTransmutation;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfAntiMagic;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfChallenge;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfMetamorphosis;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfPsionicBlast;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfSirensSong;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.CurseInfusion;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.MagicalInfusion;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.PhaseShift;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.WildEnergy;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfAugmentation;
import com.shatteredpixel.shatteredpixeldungeon.items.thanks.DistressSignalNesting;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.darts.TippedDart;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.HashMap;

public class DearthRongShopRoom extends SpecialRoom {
    private ArrayList<Item> itemsToSpawn;

    public int width = Math.max(11, (int) (Math.sqrt(itemCount()) + 3.0d));;
    public int height = Math.max(11, (int) (Math.sqrt(itemCount()) + 3.0d));;

    @Override
    public int minWidth() {
        return width;
    }
    @Override
    public int minHeight() {
        return height;
    }
    @Override
    public int maxWidth() {
        return width;
    }
    @Override
    public int maxHeight() {
        return height;
    }

    public int itemCount() {
        if (this.itemsToSpawn == null) {
            this.itemsToSpawn = generateItems();
        }
        return this.itemsToSpawn.size();
    }

    public void paint(Level level) {
        Painter.fill(level, this, 4);
        Painter.fill(level, this, 1, 14);
        placeShopkeeper(level);
        placeItems(level);
        for (Room.Door door : this.connected.values()) {
            door.set(Room.Door.Type.REGULAR);
        }
    }

    protected void placeShopkeeper(Level level) {
        int pos = level.pointToCell(center());

        Mob d = new DeathRongShop();
        d.pos = pos;
        level.mobs.add(d);
    }

    protected void placeItems(Level level) {
        if (this.itemsToSpawn == null) {
            this.itemsToSpawn = generateItems();
        }
        Point itemPlacement = new Point(entrance());
        if (itemPlacement.y == this.top) {
            itemPlacement.y++;
        } else if (itemPlacement.y == this.bottom) {
            itemPlacement.y--;
        } else if (itemPlacement.x == this.left) {
            itemPlacement.x++;
        } else {
            itemPlacement.x--;
        }
        for (Item item : this.itemsToSpawn) {
            if (itemPlacement.x == this.left + 1 && itemPlacement.y != this.top + 1) {
                itemPlacement.y--;
            } else if (itemPlacement.y == this.top + 1 && itemPlacement.x != this.right - 1) {
                itemPlacement.x++;
            } else if (itemPlacement.x != this.right - 1 || itemPlacement.y == this.bottom - 1) {
                itemPlacement.x--;
            } else {
                itemPlacement.y++;
            }
            int cell = level.pointToCell(itemPlacement);
            if (level.heaps.get(cell) != null) {
                do {
                    cell = level.pointToCell(random());
                } while (level.heaps.get(cell) != null || level.findMob(cell) != null);
            }
            level.drop(item, cell).type = Heap.Type.FOR_SALE;
        }
    }

    public static Item HighPotion() {
        Item w;
        switch (Random.Int(6)){
            default:
            case 1: w = new WaterSoul();   break;
            case 2: w = new BlizzardBrew(); break;
            case 3: w = new CausticBrew();    break;
            case 4: w = new InfernalBrew();   break;
            case 5: w = new ShockingBrew();   break;
        }
        return w;
    }

    public static Item HighScroll() {
        Item w;
        switch (Random.Int(5)){
            default:
            case 0: w = new ScrollOfSirensSong(); break;
            case 1: w = new ScrollOfChallenge(); break;
            case 2: w = new ScrollOfMetamorphosis(); break;
            case 3: w = new ScrollOfAntiMagic();    break;
            case 4: w = new ScrollOfPsionicBlast();   break;
        }
        return w;
    }

    public static Item HighEXPotion() {
        Item w;
        switch (Random.Int(14)) {
            default:
            case 0: w = new PotionOfShielding(); break;
            case 1: w = new PotionOfCorrosiveGas(); break;
            case 2: w = new PotionOfMastery(); break;
            case 3: w = new PotionOfSnapFreeze(); break;
            case 4: w = new PotionOfStamina(); break;
            case 5: w = new PotionOfDragonsBreath(); break;
            case 6: w = new PotionOfLightStromCloud(); break;
            case 7: w = new PotionOfDragonKingBreath(); break;
            case 8: w = new PotionOfShroudingFog(); break;
            case 9: w = new PotionOfMagicalSight(); break;
            case 10: w = new PotionOfStormClouds(); break;
            case 11: w = new PotionOfDivineInspiration(); break;
            case 12: w = new PotionOfCleansing(); break;
            case 13: w = new PotionOfEarthenArmor(); break;
        }
        return w;
    }

    protected static ArrayList<Item> generateItems() {
        ArrayList<Item> itemsToSpawn = new ArrayList<>();

        itemsToSpawn.add(new Ankh());
        TippedDart dart = TippedDart.randomTipped(Random.NormalIntRange(2,4));
        dart.enchantment = Weapon.Enchantment.random();
        itemsToSpawn.add(dart);

        itemsToSpawn.add(TippedDart.randomTipped(2));


        itemsToSpawn.add(new ScrollOfTransmutation());
        itemsToSpawn.add(Generator.randomUsingDefaults(Generator.Category.WAND));

        // 随机模式下添加特定物品
        if (Statistics.RandMode) {
            itemsToSpawn.add(new ElixirOfMight());
            itemsToSpawn.add(new PotionOfMastery());
        }

        // 挑战模式下添加特定物品
        if (Dungeon.isChallenged(DHXD) || lanterfireactive) {
            int oilPotionCount = Challenges.activeChallenges() > 6 && lanterfireactive ? 2 : 1;
            for (int i = 0; i < oilPotionCount; i++) {
                itemsToSpawn.add(new OilPotion());
                itemsToSpawn.add(new OilPotion());
                itemsToSpawn.add(new OilPotion());
            }
        }

        if(Badges.isUnlocked(Badges.Badge.KILL_MORES)){
            itemsToSpawn.add(new StarCrystal());
        }

        // 添加通用物品
        itemsToSpawn.add(new ScrollOfIdentify());
        itemsToSpawn.add(new ScrollOfMagicMapping());
        itemsToSpawn.add(new ScrollOfRemoveCurse());

        itemsToSpawn.add(new Food());
        itemsToSpawn.add(new Food());

        // 添加其他通用物品
        itemsToSpawn.add(new PotionOfHealing());
        itemsToSpawn.add(new PotionOfHealing());
        itemsToSpawn.add(new SmallRation());

        itemsToSpawn.add(new Honeypot());
        itemsToSpawn.add(new StoneOfAugmentation());

        itemsToSpawn.add(Generator.random(Generator.Category.STONE));

        // 沙漏模式添加特定物品
        TimekeepersHourglass hourglass = Dungeon.hero.belongings.getItem(TimekeepersHourglass.class);
        if (hourglass != null) {
            int bagsToAdd = 0;
            switch (Dungeon.depth) {
                case 6:
                    bagsToAdd = (int) Math.ceil((5 - hourglass.sandBags) * 0.2f);
                    break;
                case 11:
                    bagsToAdd = (int) Math.ceil((3 - hourglass.sandBags) * 0.25f);
                    break;
                case 16:
                    bagsToAdd = (int) Math.ceil((5 - hourglass.sandBags) * 0.5f);
                    break;
                case 20:
                case 21:
                    bagsToAdd = (int) Math.ceil((5 - hourglass.sandBags) * 0.8f);
                    break;
            }
            for (int i = 0; i < bagsToAdd; i++) {
                itemsToSpawn.add(new TimekeepersHourglass.sandBag());
                hourglass.sandBags++;
            }
        }

        // 根据随机值添加罕见物品
        Item rare;
        rare = Generator.random(Generator.Category.RING);
        rare.level(Random.Int(2,5));
        rare.upgrade();
        rare.cursed = false;
        rare.cursedKnown = true;
        itemsToSpawn.add(rare);

        // 求救信号套组
        if (!Statistics.SOSisAlreadySpawn && Random.Float() < 0.3f){
            itemsToSpawn.add(new DistressSignalNesting());
            Statistics.SOSisAlreadySpawn = true;
        }
        itemsToSpawn.add (new Flashbang().quantity(1));
        itemsToSpawn.add (new Flashbang().quantity(1));
        itemsToSpawn.add (new Noisemaker().quantity(1));
        itemsToSpawn.add (new RegrowthBomb().quantity(1));
        itemsToSpawn.add (new HolyBomb().quantity(1));
        itemsToSpawn.add (new Firebomb().quantity(1));
        itemsToSpawn.add (new FrostBomb().quantity(1));

        itemsToSpawn.add(HighEXPotion());
        itemsToSpawn.add(HighEXPotion());

        itemsToSpawn.add (Generator.random( Generator.Category.ARTIFACT ));

        Bag bag = ChooseBag(Dungeon.hero.belongings);
        if (bag != null) {
            itemsToSpawn.add(bag);
        }

        for (int i = 0; i < 4; i++) {
            itemsToSpawn.add(Random.Int(2) == 0 ?
                    Generator.randomUsingDefaults(Generator.Category.POTION) :
                    Generator.randomUsingDefaults(Generator.Category.SCROLL));
        }

        itemsToSpawn.add(HighScroll());
        itemsToSpawn.add(HighPotion());
        itemsToSpawn.add(HighScroll());
        itemsToSpawn.add(HighPotion());
        itemsToSpawn.add(new UnstableBrew());
        itemsToSpawn.add(new CurseInfusion());
        itemsToSpawn.add(new MagicalInfusion());

        itemsToSpawn.add(new WildEnergy());
        itemsToSpawn.add(new PhaseShift());

        itemsToSpawn.add(new Sugar());
        itemsToSpawn.add(new Sugar());

        // 打乱物品顺序
        //hard limit is 63 items + 1 shopkeeper, as shops can't be bigger than 8x8=64 internally
        if (itemsToSpawn.size() > 63) {
            throw new RuntimeException("Shop attempted to carry more than 63 items!");
        }

        //use a new generator here to prevent items in shop stock affecting levelgen RNG (e.g. sandbags)
        //we can use a random long for the seed as it will be the same long every time
        Random.pushGenerator(Random.Long());
        Random.shuffle(itemsToSpawn);
        Random.popGenerator();

        // 输出所有物品名称
        for (Item item : itemsToSpawn) {
            item.identify();
        }

        return itemsToSpawn;
    }



    protected static Bag ChooseBag(Belongings pack){
        if(Dungeon.isDLC(Conducts.Conduct.DEV)) return null;
        //generate a hashmap of all valid bags.
        HashMap<Bag, Integer> bags = new HashMap<>();
        if (!Dungeon.LimitedDrops.VELVET_POUCH.dropped()) bags.put(new VelvetPouch(), 1);
        if (!Dungeon.LimitedDrops.SCROLL_HOLDER.dropped()) bags.put(new ScrollHolder(), 0);
        if (!Dungeon.LimitedDrops.POTION_BANDOLIER.dropped()) bags.put(new PotionBandolier(), 0);
        if (!Dungeon.LimitedDrops.MAGICAL_HOLSTER.dropped()) bags.put(new MagicalHolster(), 0);

        if (bags.isEmpty()) return null;

        //count up items in the main bag
        for (Item item : pack.backpack.items) {
            for (Bag bag : bags.keySet()){
                if (bag.canHold(item)){
                    bags.put(bag, bags.get(bag)+1);
                }
            }
        }

        //find which bag will result in most inventory savings, drop that.
        Bag bestBag = null;
        for (Bag bag : bags.keySet()){
            if (bestBag == null){
                bestBag = bag;
            } else if (bags.get(bag) > bags.get(bestBag)){
                bestBag = bag;
            }
        }

        if (bestBag instanceof VelvetPouch){
            Dungeon.LimitedDrops.VELVET_POUCH.drop();
        } else if (bestBag instanceof ScrollHolder){
            Dungeon.LimitedDrops.SCROLL_HOLDER.drop();
        } else if (bestBag instanceof PotionBandolier){
            Dungeon.LimitedDrops.POTION_BANDOLIER.drop();
        } else if (bestBag instanceof MagicalHolster){
            Dungeon.LimitedDrops.MAGICAL_HOLSTER.drop();
        }

        return bestBag;

    }
}

