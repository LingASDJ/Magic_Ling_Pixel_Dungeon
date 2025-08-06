package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AscensionChallenge;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Poison;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.CrivusFruits;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.EquipableItem;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.IceCyanBlueSquareCoin;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.Torch;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.Artifact;
import com.shatteredpixel.shatteredpixeldungeon.items.food.CrivusFruitsFood;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Food;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.CrystalKey;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfPurity;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.CrivusFruitsFlake;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.LifeTreeSword;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.shatteredpixeldungeon.levels.RegularLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.Door;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.AlarmTrap;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MimicSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class GreenDiamndMimic extends Mimic {

    {
        spriteClass = MimicSprite.Green.class;
    }

    @Override
    public String name() {
        if (alignment == Char.Alignment.NEUTRAL){
            return Messages.get(this, "hidden_name");
        } else {
            return super.name();
        }
    }

    @Override
    public String description() {
        if (alignment == Char.Alignment.NEUTRAL){
            return Messages.get(this, "hidden_desc");
        } else {
            return Messages.get(this, "desc");
        }
    }

    @Override
    public void die(Object cause) {
        super.die(cause);
        boolean heroKilled = false;

        for (int i = 0; i < PathFinder.NEIGHBOURS9.length; i++) {
            Char ch = findChar( pos + PathFinder.NEIGHBOURS9[i] );
            if (ch != null && ch.isAlive()) {
                int damage = Math.round(Random.NormalIntRange(8, 14));
                damage = Math.round( damage * AscensionChallenge.statModifier(this));
                damage = Math.max( 0,  damage - (ch.drRoll() + ch.drRoll()) );
                ch.damage( damage, this );
                if (ch == Dungeon.hero && !ch.isAlive()) {
                    heroKilled = true;
                }
            }
        }

        if (heroKilled) {
            Dungeon.fail( this );
            GLog.n( Messages.get(this, "explo_kill") );
        }
    }

    @Override
    public boolean stealthy() {
        return true;
    }

    public void stopHiding(){
        state = HUNTING;
        if (sprite != null) sprite.idle();
        if (Actor.chars().contains(this) && Dungeon.level.heroFOV[pos]) {
            enemy = Dungeon.hero;
            target = Dungeon.hero.pos;
            GLog.w(Messages.get(this, "reveal") );
            CellEmitter.get(pos).burst(Speck.factory(Speck.STAR), 10);
            Sample.INSTANCE.play(Assets.Sounds.CHALLENGE, 1, 0.55f);
            AlarmTrap alarmTrap = new AlarmTrap();
            alarmTrap.activate();
        }
        if (Actor.chars().contains(this) && Dungeon.level.map[pos] == Terrain.DOOR){
            Door.enter( pos );
        }
    }


    @Override
    public int damageRoll() {
        if (alignment == Char.Alignment.NEUTRAL){
            int damage = Math.round(super.damageRoll()*1.75f);
            if (enemy != null && enemy.isAlive()) {
                int effect = Random.Int(3);
                switch (effect) {
                    case 0:
                        Buff.affect(enemy, Poison.class).set(4 + Dungeon.depth);
                        break;
                    case 1:
                        Buff.prolong(enemy, Cripple.class, 15f);
                        break;
                    case 2:
                        Buff.prolong(enemy, Blindness.class, 15f);
                        break;
                }
            }
            return damage;
        } else {
            return super.damageRoll();
        }
    }


    @Override
    public void setLevel(int level) {
        super.setLevel(Math.round(level*1.25f));
    }

    @Override
    protected void generatePrize( boolean useDecks ) {
        super.generatePrize( useDecks );
        //add one extra random loot item, on top of the one granted by mimic tooth
        items.add(Generator.randomUsingDefaults());

        //all existing prize items are guaranteed uncursed, and are always at least +1
        for (Item i : items){
            if (i instanceof EquipableItem || i instanceof Wand){
                i.cursed = false;
                i.cursedKnown = true;
                if (i instanceof Weapon && ((Weapon) i).hasCurseEnchant()){
                    ((Weapon) i).enchant(null);
                }
                if (i instanceof Armor && ((Armor) i).hasCurseGlyph()){
                    ((Armor) i).inscribe(null);
                }
                if (!(i instanceof MissileWeapon || i instanceof Artifact) && i.level() == 0){
                    i.upgrade();
                }
            }
        }
    }

}

