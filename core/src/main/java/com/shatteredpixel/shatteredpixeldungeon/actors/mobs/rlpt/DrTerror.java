package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.rlpt;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.Pushing;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ShaftParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Bomb;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DrTerrorSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MurdererSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

//恐怖博士
public class DrTerror extends Mob {

    public static class BombTech extends Mob {
        @Override
        public int attackProc(Char enemy, int damage) {
            die(null);
            new Bomb().explodeMobs(target);
            return damage;
        }

        @Override
        public int attackSkill(Char target) {
            return 30;
        }

        public BombTech() {
            this.spriteClass = MurdererSprite.class;
            this.HT = 12;
            this.HP = 12;
            this.defenseSkill = 9;
            this.EXP = 0;
            this.state = this.HUNTING;
            this.properties.add(Char.Property.DEMONIC);
        }

        @Override
        public int damageRoll() {
            return Random.NormalIntRange(2, 8);
        }

        @Override
        public int drRoll() {
            return Random.NormalIntRange(0, 8);
        }
    }

    public DrTerror() {
        spriteClass = DrTerrorSprite.class;
        defenseSkill = 9;
        EXP = 5;

        loot = Gold.class;
        lootChance = 1.0f;

        maxLvl = 30;
        HP = HT = 80;

        properties.add(Property.UNDEAD);
    }

    @Override
    public int damageRoll() {
        return Char.combatRoll(26, 34);
    }

    @Override
    public int attackProc(Char enemy, int damage) {
        int newDamage = super.attackProc(enemy, damage);
        int reg = Math.min(newDamage/3, this.HT - this.HP);
        if (reg > 0) {
            this.HP += reg;
            this.sprite.emitter().burst(Speck.factory(0), 1);
        }
        return newDamage;
    }

    @Override
    public int attackSkill(Char target) {
        return 23;
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(4, 7);
    }

    @Override
    public int defenseProc(Char enemy, int damage) {
        ArrayList<Integer> spawnPoints = new ArrayList<>();
        for (int i = 0; i < PathFinder.NEIGHBOURS8.length; i++) {
            int p = this.pos + PathFinder.NEIGHBOURS8[i];
            if (Actor.findChar(p) == null && (Dungeon.level.passable[p] || Dungeon.level.avoid[p])) {
                spawnPoints.add(Integer.valueOf(p));
            }
        }
        int size = spawnPoints.size();
        if (size > 0) {
            BombTech larva = new BombTech();
            larva.pos = Random.element(spawnPoints).intValue();
            GameScene.add(larva);
            Actor.addDelayed(new Pushing(larva, this.pos, larva.pos), -1.0f);
        }
        for (Mob mob : Dungeon.level.mobs) {
            if (mob instanceof BombTech) {
                mob.aggro(enemy);
            }
        }
        return super.defenseProc(enemy, damage);
    }

    public void cleanse(){
        Sample.INSTANCE.play( Assets.Sounds.GHOST );
        yell(Messages.get(this, "thanks"));

        //50/50 between weapon or armor, always uncursed
        Item prize;
        if (Random.Int(2) == 0){
            prize = Generator.random(Generator.Category.POTION);

        } else {
            prize = Generator.random(Generator.Category.SCROLL);
        }
        prize.cursed = false;
        prize.cursedKnown = true;

        Dungeon.level.drop(prize, pos).sprite.drop();

        destroy();
        sprite.die();
        sprite.tint(1, 1, 1, 1);
        sprite.emitter().start( ShaftParticle.FACTORY, 0.3f, 4 );
        sprite.emitter().start( Speck.factory( Speck.LIGHT ), 0.2f, 3 );

    }

}
