package com.shatteredpixel.shatteredpixeldungeon.windows;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Ghost;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Imp;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Wandmaker;
import com.shatteredpixel.shatteredpixeldungeon.items.Dewdrop;
import com.shatteredpixel.shatteredpixeldungeon.items.EnergyCrystal;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.Artifact;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.CrystalKey;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.GoldenKey;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.IronKey;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.Potion;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.CeremonialCandle;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.CorpseDust;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Embers;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Pickaxe;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.Ring;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.Scroll;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ScrollOfAwareness extends Scroll {

    {
        icon = ItemSpriteSheet.Icons.SCROLL_FORESIGHT;
        image = ItemSpriteSheet.SCROLL_YNGVI;
        unique = true;
    }

    List<Class<? extends Item>> blacklist;

    private void addTextItems(String caption, ArrayList<Heap> items, StringBuilder builder) {
        if (!items.isEmpty()) {
            builder.append(caption + ":\n");

            for (Heap h : items) {
                Item i = h.peek();

                if (((i instanceof Armor && ((Armor) i).hasGoodGlyph()) ||
                        (i instanceof Weapon && ((Weapon) i).hasGoodEnchant()) ||
                        (i instanceof Ring) || (i instanceof Wand) || (i instanceof Artifact)) && i.cursed)
                    builder.append("- cursed " + i.toString().toLowerCase());

                else
                    builder.append("- " + i.toString().toLowerCase());

                if (h.type != Heap.Type.HEAP)
                    builder.append(" (" + h.toString().toLowerCase() + ")");

                builder.append("\n");
            }

            builder.append("\n");
        }
    }

    private void addTextQuest(String caption, ArrayList<Item> items, StringBuilder builder) {
        if (!items.isEmpty()) {
            builder.append(caption + ":\n");

            for (Item i : items) {
                if (i.cursed)
                    builder.append("- cursed " + i.toString().toLowerCase() + "\n");

                else
                    builder.append("- " + i.toString().toLowerCase() + "\n");
            }

            builder.append("\n");
        }
    }

    @Override
    public void doRead() {
        blacklist = Arrays.asList(Gold.class, Dewdrop.class, IronKey.class, GoldenKey.class, CrystalKey.class, EnergyCrystal.class,
                CorpseDust.class, Embers.class, CeremonialCandle.class, Pickaxe.class);

        List<Heap> heaps = Dungeon.level.heaps.valueList();
        StringBuilder builder = new StringBuilder();
        ArrayList<Heap> scrolls = new ArrayList<>();
        ArrayList<Heap> potions = new ArrayList<>();
        ArrayList<Heap> equipment = new ArrayList<>();
        ArrayList<Heap> rings = new ArrayList<>();
        ArrayList<Heap> artifacts = new ArrayList<>();
        ArrayList<Heap> wands = new ArrayList<>();
        ArrayList<Heap> others = new ArrayList<>();

        // list quest rewards
        if (Ghost.Quest.armor != null && Ghost.Quest.depth == Dungeon.depth) {
            ArrayList<Item> rewards = new ArrayList<>();
            rewards.add(Ghost.Quest.armor);
            rewards.add(Ghost.Quest.weapon);

            addTextQuest("_Ghost quest rewards_", rewards, builder);
        }

        if (Wandmaker.Quest.wand1 != null && Wandmaker.Quest.depth == Dungeon.depth) {
            ArrayList<Item> rewards = new ArrayList<>();
            rewards.add(Wandmaker.Quest.wand1);
            rewards.add(Wandmaker.Quest.wand2);

            builder.append("_Wandmaker quest item_: ");

            switch (Wandmaker.Quest.type) {
                case 1: default:
                    builder.append("corpse dust\n\n");
                    break;
                case 2:
                    builder.append("fresh embers\n\n");
                    break;
                case 3:
                    builder.append("rotberry seed\n\n");
            }

            addTextQuest("_Wandmaker quest rewards_", rewards, builder);
        }

        if (Imp.Quest.reward != null && Imp.Quest.depth == Dungeon.depth) {
            ArrayList<Item> rewards = new ArrayList<>();
            rewards.add(Imp.Quest.reward.identify());

            addTextQuest("_Imp quest reward_", rewards, builder);
        }

        // list items
        for (Heap h : heaps) {
            Item item = h.peek();

            if (h.type == Heap.Type.FOR_SALE) continue;
            else if (blacklist.contains(item.getClass())) continue;
            else if (item instanceof Scroll) scrolls.add(h);
            else if (item instanceof Potion) potions.add(h);
            else if (item instanceof MeleeWeapon || item instanceof Armor) equipment.add(h);
            else if (item instanceof Ring) rings.add(h);
            else if (item instanceof Artifact) artifacts.add( h);
            else if (item instanceof Wand) wands.add(h);
            else others.add(h);
        }

        addTextItems("_Scrolls_", scrolls, builder);
        addTextItems("_Potions_", potions, builder);
        addTextItems("_Equipment_", equipment, builder);
        addTextItems("_Rings_", rings, builder);
        addTextItems("_Artifacts_", artifacts, builder);
        addTextItems("_Wands_", wands, builder);
        addTextItems("_Other_", others, builder);

        if (Dungeon.depth % 5 == 0) {
            GLog.i("No items found on this level");
        } else {
            builder.setLength(builder.length()-2); // remove trailing newlines
            GameScene.show(new ScrollableWindow(builder.toString()));
        }

        collect();

        Sample.INSTANCE.play( Assets.Sounds.READ );
    }

    @Override
    public String name() {
        return "Scroll of Awareness";
    }

    @Override
    public String desc() {
        StringBuilder builder = new StringBuilder();

        builder.append("When this scroll is read, it will list all the generated items in the current floor ");
        builder.append("and any quest rewards. ");
        builder.append("This scroll won't be consumed after being read.");

        return builder.toString();
    }

    @Override public boolean isIdentified() {
        return true;
    }

    @Override public boolean isKnown() {
        return true;
    }

}