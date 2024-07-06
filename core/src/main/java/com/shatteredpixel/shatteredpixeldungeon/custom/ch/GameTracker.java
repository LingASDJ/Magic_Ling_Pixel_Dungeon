package com.shatteredpixel.shatteredpixeldungeon.custom.ch;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mimic;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Ghost;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Imp;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.RedDragon;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Wandmaker;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.Artifact;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.brews.Brew;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.Ring;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ExoticScroll;
import com.shatteredpixel.shatteredpixeldungeon.items.trinkets.TrinketCatalyst;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.levels.RegularLevel;
import com.watabou.noosa.Game;
import com.watabou.noosa.Visual;
import com.watabou.utils.Bundle;

public class GameTracker extends Buff {
    {
        actPriority = VFX_PRIO - 1;
        revivePersists = true;
    }

    private VirtualVisualTimer vvt;
    private int maxDepth = -1;
    private String allItemInfo = "";

    @Override
    public boolean act() {
        spend(TICK/2f);
        if(vvt==null) {
            vvt = new VirtualVisualTimer();
            Dungeon.hero.sprite.parent.add(vvt);
        }
        if(!vvt.alive || !vvt.active || vvt.parent == null){
            vvt.revive();
            vvt.active = true;
            Dungeon.hero.sprite.parent.add(vvt);
            vvt.parent = Dungeon.hero.sprite.parent;
        }
        if(maxDepth < Dungeon.depth){
            spend(-TICK);
            maxDepth = Dungeon.depth;
            updateItemInfo();
        }
        return true;
    }

    public void onNewLevel(){
        if(maxDepth < Dungeon.depth){
            spend(-TICK);
            maxDepth = Dungeon.depth;
            updateItemInfo();
        }
    }

    private void updateItemInfo(){
        if(Dungeon.level instanceof RegularLevel){
            StringBuilder info = new StringBuilder("");
            info.append("dungeon_depth: ").append(maxDepth).append("\n");
            for(Heap heap: Dungeon.level.heaps.valueList()){
                if((!heap.autoExplored) || heap.type == Heap.Type.CHEST || heap.type == Heap.Type.LOCKED_CHEST || heap.type == Heap.Type.CRYSTAL_CHEST){
                    for(Item item : heap.items){
                        appendDesc(item, info, "");
                    }
                }
            }
            for(Mob m : Dungeon.level.mobs.toArray(new Mob[0])){
                if(m instanceof Mimic){
                    for(Item item: ((Mimic) m).items){
                        appendDesc(item, info, "MIMIC_HOLD");
                    }
                }
                if(m instanceof Ghost){
                    appendDesc(Ghost.Quest.weapon, info, "QUEST_REWARD");
                    appendDesc(Ghost.Quest.armor, info, "QUEST_REWARD");
                }
                if(m instanceof Wandmaker){
                    appendDesc(Wandmaker.Quest.wand1, info, "QUEST_REWARD");
                    appendDesc(Wandmaker.Quest.wand2, info, "QUEST_REWARD");
                }
                if(m instanceof RedDragon){
                    appendDesc(RedDragon.Quest.weapon, info, "QUEST_REWARD");
                    appendDesc(RedDragon.Quest.armor, info, "QUEST_REWARD");
                    appendDesc(RedDragon.Quest.food, info, "QUEST_REWARD");
                    appendDesc(RedDragon.Quest.scrolls, info, "QUEST_REWARD");
                }
                if(m instanceof Imp){
                    appendDesc(Imp.Quest.reward, info, "QUEST_REWARD");
                }
            }
            allItemInfo += info.toString();
        }
    }

    private void appendDesc(Item item, StringBuilder info, String prefix){
        if(item != null) {
            if (
                    ((item instanceof Weapon || item instanceof Armor) && item.level() > 0)
                            || (item instanceof Ring || item instanceof Wand || item instanceof Artifact || item instanceof ExoticScroll|| item instanceof Brew || item instanceof TrinketCatalyst)
            ) {
                String name = item.trueName();
                int index = name.indexOf('+');
                if(index > 0){
                    name = name.substring(0, index - 1);
                }
                info.append(prefix).append(name).append('+').append(item.level()).append(item.cursed ? "  CURSED\n" : "\n");
            }
        }
    }

    public String itemInfo(){
        return allItemInfo;
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put("allItemInfo", allItemInfo);
        bundle.put("recordedMaxDepth", maxDepth);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        allItemInfo = bundle.getString("allItemInfo");
        maxDepth = bundle.getInt("recordedMaxDepth");
    }

    public static class VirtualVisualTimer extends Visual {
        public VirtualVisualTimer(){
            super(0, 0, 0, 0);
        }

        @Override
        public void update() {
            super.update();
            Statistics.second_elapsed += Game.elapsed;
            if(Statistics.second_elapsed > 1f){
                Statistics.real_seconds += Math.floor(Statistics.second_elapsed);
                Statistics.second_elapsed -= Math.floor(Statistics.second_elapsed);
            }
            if(Statistics.turnsPassed < Statistics.duration){
                Statistics.turnsPassed = Statistics.duration;
            }else{
                Statistics.turnsPassed = Statistics.duration + Actor.now();
            }
        }
    }
}
