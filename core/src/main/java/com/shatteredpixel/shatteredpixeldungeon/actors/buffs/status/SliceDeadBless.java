package com.shatteredpixel.shatteredpixeldungeon.actors.buffs.status;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.StormCloudDied;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.hollow.minigame.Ghost_Anger;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.hollow.minigame.Ghost_Junko;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.hollow.minigame.Ghost_Pink;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.hollow.minigame.Ghost_Smart;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Lightning;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SparkParticle;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.utils.Bundle;

import java.util.ArrayList;

public class SliceDeadBless extends Buff {

    {
        type = buffType.POSITIVE;
    }

    private int level = 0;
    private int interval = 1;
    private int cooldown = 0;

    protected float left;
    ArrayList<Lightning.Arc> arcs = new ArrayList<>();
    private static final String LEFT = "left";
    private static final String COOLDOWN = "cooldown";


    private void LightAttackLimitEnemy(boolean isActive){
        if(isActive) {
            if (cooldown > 0) {
                cooldown--;
            }

            if (cooldown == 0) {
                // 寻找最近的敌人
                Char nearestEnemy = null;
                float minDistance = Float.MAX_VALUE;

                for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
                    if (mob != null && mob.isAlive() && Dungeon.level.heroFOV[mob.pos] && mob.alignment == Char.Alignment.ENEMY) {
                        if (!(mob instanceof Ghost_Junko || mob instanceof Ghost_Anger || mob instanceof Ghost_Smart || mob instanceof Ghost_Pink)) {
                            float distance = Dungeon.level.distance(hero.pos, mob.pos);
                            if (distance < minDistance) {
                                minDistance = distance;
                                nearestEnemy = mob;
                            }
                        }
                    }
                }

                if (nearestEnemy != null) {
                    int totalDamage = 10 + Dungeon.hero.lvl / 5 + Dungeon.depth / 5;
                    arcs.add(new Lightning.Arc(hero.sprite.center(), nearestEnemy.sprite.center()));
                    nearestEnemy.damage(totalDamage, new StormCloudDied.LightningBolt());

                    CellEmitter.center(nearestEnemy.pos).burst(SparkParticle.FACTORY, 3);
                    hero.sprite.parent.addToFront(new Lightning(arcs, null));
                    cooldown = 10;
                }
            }
        }
    }

    @Override
    public boolean act() {
        if (target.isAlive()) {
            spend(interval);

            LightAttackLimitEnemy(Dungeon.depth>27 && Dungeon.branch == 0);

        } else {
            detach();
        }

        return true;
    }

    public int level() {
        return level;
    }

    public void set(int value, int time) {
        if (level <= value) {
            level = value;
            interval = time;
            spend(time - cooldown() - 1);
        }
    }

    @Override
    public int icon() {
        return BuffIndicator.SLICE_BLESS;
    }

    @Override
    public String iconTextDisplay() {
        return Integer.toString(level);
    }

    public String desc() {
        String string = super.desc();
        if(Dungeon.depth>27){
            string = Messages.get(this, "desc_plus1",(10 + Dungeon.hero.lvl / 5 + Dungeon.depth / 5) );
            if(cooldown>0){
                string += "\n\n" + Messages.get(this, "desc_cooldown", cooldown);
            }
        }
        if(Dungeon.depth>28){
            string += "\n\n" + Messages.get(this, "desc_plus2" );
        }
        if(Dungeon.depth>29){
            string += "\n\n" + Messages.get(this, "desc_plus3" );
        }
        if(Dungeon.depth>30){
            string += "\n\n" + Messages.get(this, "desc_plus4" );
        }

        string += "\n\n" + Messages.get(this, "bless",Dungeon.depth-27);
        return string;
    }

    private static final String LEVEL = "level";
    private static final String INTERVAL = "interval";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(INTERVAL, interval);
        bundle.put(LEVEL, level);
        bundle.put(LEFT, left);
        bundle.put(COOLDOWN, cooldown);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        interval = bundle.getInt(INTERVAL);
        level = bundle.getInt(LEVEL);
        left = bundle.getFloat(LEFT);
        cooldown = bundle.getInt(COOLDOWN);
    }
}
