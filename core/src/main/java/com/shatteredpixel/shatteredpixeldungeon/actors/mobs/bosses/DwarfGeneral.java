package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.BGMPlayer;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Boss;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.BrokenArmorFire;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Chill;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.DM100;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.TargetedCell;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ElmoParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.DriedRose;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DwarfGeneralSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class DwarfGeneral extends Boss {
    protected int rangedCooldown = Random.NormalIntRange( 3, 5 );
    private int targetingPos = -1;
    @Override
    public int damageRoll() {
        return Random.NormalIntRange( 20, 55 );
    }


    {
        initProperty();
        initBaseStatus(12, 24, 23, 18, 1000, 9, 16);
        initStatus(40);

        properties.add(Property.DEMONIC);

        spriteClass = DwarfGeneralSprite.class;

        immunities.add(BrokenArmorFire.class);
    }



    @Override
    protected boolean act() {
        rangedCooldown--;
        int heroPos = Dungeon.hero.pos;
        int bossToHeroDistance = Dungeon.level.distance(pos, heroPos);
        if (targetingPos != -1 && bossToHeroDistance < 4){
            if (sprite != null && (sprite.visible || Dungeon.level.heroFOV[targetingPos])) {
                ((DwarfGeneralSprite)sprite).skills( targetingPos );
                return false;
            } else {
                zap();
                return true;
            }
        } else {
            return super.act();
        }
    }

    @Override
    protected boolean canAttack( Char enemy ) {
        if (super.canAttack(enemy)){
            return true;
        } else {
            return rangedCooldown > 30 && new Ballistica(pos, enemy.pos, Ballistica.STOP_SOLID | Ballistica.STOP_TARGET).collisionPos == enemy.pos;
        }
    }

    protected boolean doAttack( Char enemy ) {
        if (rangedCooldown > 0) {

            return super.doAttack(enemy);

        } else if (new Ballistica(pos, enemy.pos, Ballistica.STOP_SOLID | Ballistica.STOP_TARGET).collisionPos == enemy.pos) {
                //set up an attack for next turn
                ArrayList<Integer> candidates = new ArrayList<>();
                for (int i : PathFinder.NEIGHBOURS8) {
                    int target = enemy.pos + i;
                    if (target != pos && new Ballistica(pos, target, Ballistica.STOP_SOLID | Ballistica.STOP_TARGET).collisionPos == target) {
                        candidates.add(target);
                    }
                }

                if (!candidates.isEmpty()) {
                    targetingPos = Random.element(candidates);

                    for (int i : PathFinder.NEIGHBOURS8) {
                        if (!Dungeon.level.solid[targetingPos + i]) {
                            sprite.parent.addToBack(new TargetedCell(targetingPos + i, 0xFF0000));
                        }
                    }

                    spend(2f);
                    yell(Messages.get(this, "charging"));
                    rangedCooldown = 30;
                    return true;
                } else {
                    rangedCooldown = 1;
                    return super.doAttack(enemy);
                }

        } else {
            rangedCooldown = 1;
            return super.doAttack(enemy);
        }
    }



    //技能1.1
    protected void zap() {
        int dmg = damageRoll();
        if (targetingPos != -1) {
            spend(1f);

            Invisibility.dispel(this);

            for (int i : PathFinder.NEIGHBOURS8) {
                if (!Dungeon.level.solid[targetingPos + i]) {
                    CellEmitter.get(targetingPos + i).burst(ElmoParticle.FACTORY, 5);
                    if (Dungeon.level.water[targetingPos + i]) {
                        GameScene.add(Blob.seed(targetingPos + i, 2, BrokenArmorFire.class));
                    } else {
                        GameScene.add(Blob.seed(targetingPos + i, 8, BrokenArmorFire.class));
                    }
                }
            }

            //技能1.1
            final Char leapVictim = Actor.findChar(targetingPos);
            if (leapVictim != null){
                if (hit(this, leapVictim, true)) {
                    enemy.damage(dmg*Random.NormalIntRange(1,3), new DM100.LightningBolt());
                    Buff.affect(enemy, Chill.class, 10f);
                    yell(Messages.get(this, "spear_warn"));
                }
            }


            Sample.INSTANCE.play(Assets.Sounds.BURNING);
        }

        targetingPos = -1;
        rangedCooldown = Random.NormalIntRange( 3, 5 );
    }

    public void onZapComplete() {
        zap();
        next();
    }


    private static final String TARGETING_POS = "targeting_pos";
    private static final String COOLDOWN = "cooldown";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(TARGETING_POS, targetingPos);
        bundle.put( COOLDOWN, rangedCooldown );
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        targetingPos = bundle.getInt(TARGETING_POS);
        if (bundle.contains( COOLDOWN )){
            rangedCooldown = bundle.getInt( COOLDOWN );
        }
    }

    @Override
    public void notice() {
        //super.notice();
        if (!BossHealthBar.isAssigned()) {
            BossHealthBar.assignBoss(this);
            yell(Messages.get(this, "notice"));
            BGMPlayer.playBoss();
            Camera.main.shake(1f,3f);
            GameScene.bossReady();
            for (Char ch : Actor.chars()){
                if (ch instanceof DriedRose.GhostHero){
                    ((DriedRose.GhostHero) ch).sayBoss();
                }
            }
        }
    }

}
