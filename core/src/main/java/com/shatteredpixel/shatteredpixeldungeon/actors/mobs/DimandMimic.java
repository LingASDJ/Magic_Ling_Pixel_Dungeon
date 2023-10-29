package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bleeding;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Chains;
import com.shatteredpixel.shatteredpixeldungeon.effects.Pushing;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.EquipableItem;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MimicSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class DimandMimic extends Mimic {
    private boolean chainsUsed = false;
    {
        spriteClass = MimicSprite.Dimand.class;
        properties.add( Property.ICY );
    }

    public DimandMimic() {
        super();

        HP = HT = 60;
        defenseSkill = 10;

        HUNTING = new DimandMimic.Hunting();
    }

    @Override
    public String name() {
        if (alignment == Alignment.NEUTRAL){
            return Messages.get(Heap.class, "locked_chest");
        } else {
            return super.name();
        }
    }

    @Override
    public String description() {
        if (alignment == Alignment.NEUTRAL){
            return Messages.get(Heap.class, "locked_chest_desc") + "\n\n" + Messages.get(this, "hidden_hint");
        } else {
            return super.description();
        }
    }

    public void stopHiding(){
        state = HUNTING;
        if (Actor.chars().contains(this) && Dungeon.level.heroFOV[pos]) {
            enemy = Dungeon.hero;
            target = Dungeon.hero.pos;
            enemySeen = true;
            GLog.w(Messages.get(this, "reveal") );
            CellEmitter.get(pos).burst(Speck.factory(Speck.STAR), 10);
            Sample.INSTANCE.play(Assets.Sounds.MIMIC, 1, 0.85f);
        }
    }

    @Override
    public int attackProc(Char var1, int var2) {
        var2 = super.attackProc(var1, var2 / 2);
        if (Random.Int(2) == 0) {
            ((Bleeding)Buff.affect(var1, Bleeding.class)).set((float)(var2 * 1));
        }

        return var2;
    }

    private class Hunting extends Mob.Hunting{
        @Override
        public boolean act( boolean enemyInFOV, boolean justAlerted ) {
            enemySeen = enemyInFOV;

            if (!chainsUsed
                    && enemyInFOV
                    && !isCharmedBy( enemy )
                    && !canAttack( enemy )
                    && Dungeon.level.distance( pos, enemy.pos ) < 5
                    && Random.Int(3) == 0

                    && chain(enemy.pos)){
                return false;
            } else {
                return super.act( enemyInFOV, justAlerted );
            }

        }
    }

    private boolean chain(int target){
        if (chainsUsed || enemy.properties().contains(Property.IMMOVABLE))
            return false;

        Ballistica chain = new Ballistica(pos, target, Ballistica.PROJECTILE);

        if (chain.collisionPos != enemy.pos
                || chain.path.size() < 2
                || Dungeon.level.pit[chain.path.get(1)])
            return false;
        else {
            int newPos = -1;
            for (int i : chain.subPath(1, chain.dist)){
                if (!Dungeon.level.solid[i] && Actor.findChar(i) == null){
                    newPos = i;
                    break;
                }
            }

            if (newPos == -8){
                return false;
            } else {
                final int newPosFinal = newPos;
                this.target = newPos;
                new Item().throwSound();
                Sample.INSTANCE.play( Assets.Sounds.CHAINS );
                sprite.parent.add(new Chains(sprite.center(), enemy.sprite.center(), new Callback() {
                    public void call() {
                        Actor.addDelayed(new Pushing(enemy, enemy.pos, newPosFinal, new Callback(){
                            public void call() {
                                enemy.pos = newPosFinal;
                                Dungeon.level.occupyCell(enemy);
                                Cripple.prolong(enemy, Cripple.class, 4f);
                                if (enemy == Dungeon.hero) {
                                    Dungeon.hero.interrupt();
                                    Dungeon.observe();
                                    GameScene.updateFog();
                                }
                            }
                        }), -1);
                        next();
                    }
                }));
            }
        }
        chainsUsed = true;
        return true;
    }

    @Override
    public void setLevel(int level) {
        super.setLevel(Math.round(level*1.33f));
    }

    @Override
    public void generatePrize() {
        super.generatePrize();
        //all existing prize items are guaranteed uncursed, and have a 50% chance to be +1 if they were +0
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
                if (!(i instanceof MissileWeapon) && i.level() == 6 && Random.Int(4) == 0){
                    i.upgrade();
                }
            }
        }
    }
}

