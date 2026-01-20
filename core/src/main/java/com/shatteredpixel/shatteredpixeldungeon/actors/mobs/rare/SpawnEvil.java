package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.rare;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AscensionChallenge;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.Pushing;
import com.shatteredpixel.shatteredpixeldungeon.items.food.MeatPie;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.journal.Notes;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SpawnereEvilSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class SpawnEvil extends Mob {

    {
        spriteClass = SpawnereEvilSprite.class;

        HP = HT = 140;
        defenseSkill = 0;

        EXP = 15;
        maxLvl = 29;

        state = PASSIVE;

        loot = PotionOfHealing.class;
        lootChance = 1f;

        properties.add(Property.IMMOVABLE);
        properties.add(Property.MINIBOSS);
        properties.add(Property.DEMONIC);
        properties.add(Property.STATIC);
    }

    @Override
    public int drRoll() {
        return super.drRoll() + Random.NormalIntRange(0, 12);
    }

    @Override
    public void beckon(int cell) {
        //do nothing
    }

    @Override
    public boolean reset() {
        return true;
    }

    private float spawnCooldown = 0;

    public boolean spawnRecorded = false;

    @Override
    protected boolean act() {
        if (!spawnRecorded){
            Statistics.spawnersAlive++;
            spawnRecorded = true;
        }

        if (Dungeon.hero.buff(AscensionChallenge.class) != null && spawnCooldown > 20){
            spawnCooldown = 20;
        }

        spawnCooldown--;
        if (spawnCooldown <= 0){

            //we don't want spawners to store multiple ripper demons
            if (spawnCooldown < -20){
                spawnCooldown = -20;
            }

            ArrayList<Integer> candidates = new ArrayList<>();
            for (int n : PathFinder.NEIGHBOURS8) {
                if (Dungeon.level.passable[pos+n] && Actor.findChar( pos+n ) == null) {
                    candidates.add( pos+n );
                }
            }

            if (!candidates.isEmpty()) {
                DemonFodder spawn = new DemonFodder();

                spawn.pos = Random.element( candidates );
                spawn.state = spawn.HUNTING;

                GameScene.add( spawn, 1 );
                Dungeon.level.occupyCell(spawn);

                if (sprite.visible) {
                    Actor.add(new Pushing(spawn, pos, spawn.pos));
                }

                // 根据深度设置不同的冷却时间，从21层开始
                if (Dungeon.depth < 21) {
                    spawnCooldown += 50; // 21层以下：50回合
                } else if (Dungeon.depth < 24) {
                    spawnCooldown += 40; // 21-23层：40回合
                } else if (Dungeon.depth < 27) {
                    spawnCooldown += 35; // 24-26层：35回合
                } else {
                    spawnCooldown += 30; // 27层及以上：30回合
                }
            }
        }
        alerted = false;
        return super.act();
    }

    @Override
    public void rollToDropLoot(){
        super.rollToDropLoot();
        Dungeon.level.drop(new MeatPie(), pos-1);
    }

    @Override
    public void damage(int dmg, Object src, DamageType type) {
        if (dmg >= 20){
            dmg = 19 + (int)(Math.sqrt(8*(dmg - 19) + 1) - 1)/2;
        }
        spawnCooldown -= dmg;
        super.damage(dmg, src, type);
    }

    @Override
    public Notes.Landmark landmark() {
        return Notes.Landmark.DEMON_SPAWNER;
    }

    @Override
    public void die(Object cause) {
        if (spawnRecorded){
            Statistics.spawnersAlive--;
            Notes.remove(landmark());
        }
        GLog.h(Messages.get(this, "on_death"));
        super.die(cause);
    }

    public static final String SPAWN_COOLDOWN = "spawn_cooldown";
    public static final String SPAWN_RECORDED = "spawn_recorded";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(SPAWN_COOLDOWN, spawnCooldown);
        bundle.put(SPAWN_RECORDED, spawnRecorded);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        spawnCooldown = bundle.getFloat(SPAWN_COOLDOWN);
        spawnRecorded = bundle.getBoolean(SPAWN_RECORDED);
    }

}
