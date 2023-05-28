package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Wraith;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.WraithSprite;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class WashCrime extends MeleeWeapon {

    private int killmobs;
    private int spawnmobs;

    @Override
    public int min(int lvl) {
        return  tier +  //base
                lvl+killmobs/5;    //level scaling
    }

    @Override
    public int max(int lvl) {
        return  5*(tier+1) +    //base
                lvl*(tier+1)+killmobs/5;   //level scaling
    }

    {
        image = ItemSpriteSheet.DASlade;
        hitSound = Assets.Sounds.HIT;
        hitSoundPitch = 1.1f;

        tier = 5;
        RCH = 3;    //lots of extra reach
        DLY = 2f;
    }

    @Override
    public int STRReq(int lvl) {
        return (9 + tier * 2) - (int)(Math.sqrt(8 * lvl + 1) - 1)/2;
        //19 base strength req, up from 18
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        killmobs = bundle.getInt("lvl");
        spawnmobs = bundle.getInt("vlv");
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put("lvl", killmobs);
        bundle.put("vlv",spawnmobs);
    }

    @Override
    public int proc(Char attacker, Char defender, int damage ) {
        DLY = DLY < 0.3f ? 0.3f : 2-(killmobs/10f)/10f; //Slow Speed
        if(killmobs > 61 ){
            killmobs=60;
        } else if (defender.HP <= damage && killmobs < 61) {
            //目标血量小于实际伤害判定为死亡
            killmobs++;
        }

        if (killmobs>= 2) {
            for (int i : PathFinder.NEIGHBOURS9){

                if (!Dungeon.level.solid[attacker.pos + i]
                        && !Dungeon.level.pit[attacker.pos + i]
                        && Actor.findChar(attacker.pos + i) == null
                        && attacker == Dungeon.hero && spawnmobs < 1 && this.level>1 ) {

                    WashCrime.DiedGhost Mob = new WashCrime.DiedGhost();
                    Mob.pos = defender.pos + i;
                    Mob.aggro(defender);
                    GameScene.add(Mob);
                    Dungeon.level.occupyCell(Mob);

                    spawnmobs++;

                    CellEmitter.get(Mob.pos).burst(Speck.factory(Speck.EVOKE), 4);
                    break;
                } else if(!Dungeon.level.solid[attacker.pos + i]
                        && !Dungeon.level.pit[attacker.pos + i]
                        && Actor.findChar(attacker.pos + i) == null
                        && attacker == Dungeon.hero && spawnmobs<2 && this.level>6 ){
                    WashCrime.DiedGhost Mob = new WashCrime.DiedGhost();
                    Mob.pos = defender.pos + i;
                    Mob.aggro(defender);
                    GameScene.add(Mob);
                    Dungeon.level.occupyCell(Mob);

                    spawnmobs++;

                    CellEmitter.get(Mob.pos).burst(Speck.factory(Speck.EVOKE), 4);
                } else if(!Dungeon.level.solid[attacker.pos + i]) {
                    spawnmobs = 0;
                    return super.proc( attacker, defender, damage );
                }
            }


            if (Random.NormalIntRange(1, 4) == 4) {
                Buff.prolong(defender, Cripple.class, 8f);
            } else {
                return super.proc(attacker, defender, damage);
            }

        }

        return super.proc(attacker, defender, damage);
    }

    public class DiedGhost extends Wraith {

        {
            state = WANDERING;
            spriteClass = WraithSprite.class;
            alignment = Alignment.ALLY;
            WANDERING = new Wandering();
            properties.add(Property.UNDEAD);
        }

        public DiedGhost() {

            HP = HT = 5 + killmobs/10;
            defenseSkill = 4 + killmobs/10;
        }

        @Override
        public int drRoll() {
            return Random.Int(3+killmobs/10, 6+killmobs/10);
        }

        public void die(Object cause) {
            super.die(cause);
            spawnmobs=0;
        }

        private class Wandering extends Mob.Wandering{

            @Override
            public boolean act(boolean enemyInFOV, boolean justAlerted) {
                if (!enemyInFOV){
                    destroy();
                    sprite.die();
                    return true;
                } else {
                    return super.act(true, justAlerted);
                }
            }

        }
    }

}
