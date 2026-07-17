package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.tumulus;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.level;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bleeding;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MissileSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.NecroPioneerSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class NecroPioneer extends Mob {

    {
        HT = 55;
        HP = HT;
        defenseSkill = 22;
        EXP = 12;
        maxLvl = 25;
        spriteClass = NecroPioneerSprite.class;
        alignment = Alignment.ENEMY;
        properties.add(Property.UNDEAD);
        properties.add(Property.NECRO);
    }

    @Override
    public int attackSkill(Char target) {
        return 22;
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(10, 26);
    }

    @Override
    public float speed() {
        return 2f;
    }

    @Override
    public int attackProc(Char enemy, int damage) {
        damage = super.attackProc(enemy, damage);

        if (enemy != null && enemy.isAlive() && enemy == this.enemy) {
            int x = pos % level.width();
            int y = pos / level.width();
            int ex = enemy.pos % level.width();
            int ey = enemy.pos / level.width();

            int stepX = Integer.compare(x, ex);
            int stepY = Integer.compare(y, ey);

            int newPos = pos;
            for (int i = 0; i < 2; i++) {
                int nextPos = newPos + stepX + stepY * level.width();
                if (level.passable[nextPos] && Actor.findChar(nextPos) == null) {
                    newPos = nextPos;
                } else {
                    break;
                }
            }

            if (newPos != pos) {
                ScrollOfTeleportation.appear(this, newPos);
                level.updateFieldOfView(this, fieldOfView);
            }

            if (sprite != null) {
                sprite.zap(enemy.pos);
                final BoneKnife knife = new BoneKnife();
                final int throwDamage = Math.max(0, damage / 2);
                final Char attacker = this;
                final Char defender = enemy;

                ((MissileSprite) ((NecroPioneerSprite) sprite).parent.recycle(MissileSprite.class))
                        .reset(sprite, enemy.pos, knife, new Callback() {
                            @Override
                            public void call() {
                                knife.proc(attacker, defender, throwDamage);
                            }
                        });
            }
        }
        return damage;
    }

    @Override
    public void die(Object cause) {
        super.die(cause);
        level.drop(new BoneKnife(),pos);
    }


    public static class BoneKnife extends MissileWeapon {


        int MAX;
        int MIN;


        {
            image = ItemSpriteSheet.BONE_KNIFE;
            hitSound = Assets.Sounds.HIT_SLASH;
            hitSoundPitch = 1.2f;
            tier = 2;

            MIN = 4;
            MAX = 8;

            durability = 5;
        }

        @Override
        public int damageRoll(Char owner) {
            return Random.NormalIntRange(MIN, MAX);
        }

        @Override
        public int proc(Char attacker, Char defender, int damage) {
            int bleedAmt = Random.NormalIntRange(8, 20);
            int crippleTurns = 4;
            int level = level();
            bleedAmt += Random.Int(level * 6 + 1);
            crippleTurns += level / 2;

            Buff.affect(defender, Bleeding.class).set(bleedAmt);
            Buff.affect(defender, Cripple.class, crippleTurns);

            if (durability > 0) {
                durability--;
                if (durability == 0 && attacker instanceof Hero) {
                    detach(((Hero) attacker).belongings.backpack);
                    GLog.w(Messages.get(BoneKnife.class, "shattered"));
                }
            }

            return super.proc(attacker, defender, damage);
        }
    }
}