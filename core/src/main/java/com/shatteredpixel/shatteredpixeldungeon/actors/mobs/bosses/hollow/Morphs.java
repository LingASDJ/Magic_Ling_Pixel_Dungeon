package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.hollow;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Boss;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MindVision;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.status.SliceDeadBless;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.hollow.bad.TowerGodsBad;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.hollow.bad.TowerMachineBad;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.hollow.bad.TowerMindBad;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.hollow.bad.TowerTimeBad;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.plot.hollow.HollowEndStoryPlot;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.DriedRose;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.AlarmTrap;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MorpheusSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialog;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;

public class Morphs extends Boss {

    /**
     * 无敌判定
     * @param effect 无敌效果
     * @return true:无敌
     */
    @Override
    public boolean isInvulnerable(Class effect) {
        return !EndStory || super.isInvulnerable(effect);
    }

    public boolean FourToneActive = false;

    public boolean ThreePhaseActive = false;

    public float phase;

    public boolean EndStory = false;

    {
        initProperty();
        initBaseStatus(0, 0, 0, 0, 1, 0, 0);
        initStatus(0);

        spriteClass = MorpheusSprite.class;

        viewDistance = 100;

        flying = true;

        alignment = EndStory ? Alignment.ENEMY : Alignment.NEUTRAL;

        properties.add(Property.IMMOVABLE);
        properties.add(Property.BOSS);
        noDropIceCoin = true;
        immunities.add(Blob.class);
        immunities.add(Buff.class);
    }

    @Override
    public void damage(int dmg, Object src, DamageType type) {
        if(src == hero){
            interact(hero);
        }
    }


    @Override
    public boolean interact(Char c) {
        sprite.turnTo(pos, hero.pos);
        if(EndStory){
            HollowEndStoryPlot plot = new HollowEndStoryPlot();
            Game.runOnRenderThread(new Callback() {
                @Override
                public void call() {
                    GameScene.show(new WndDialog(plot,false));
                }
            });
        }
        return true;
    }

    @Override
    public boolean act() {
        activate();
        alerted = false;
        state = PASSIVE;

        if (!FourToneActive && phase == 1) {
            FourToneActive = true;

            ShubNiggurath sn = new ShubNiggurath();
            sn.pos = 358;
            BossHealthBar.assignBoss(sn);
            GameScene.add(sn);

            Nyarlathotep nyarlathotep = new Nyarlathotep();
            nyarlathotep.pos = 367;
            BossHealthBar.assignBoss(nyarlathotep);
            GameScene.add(nyarlathotep);

            YogSoul yogSoul = new YogSoul();
            yogSoul.pos = 187;
            BossHealthBar.assignBoss(yogSoul);
            GameScene.add(yogSoul);

            AlarmTrap alarmTrap = new AlarmTrap();
            alarmTrap.pos = pos;
            alarmTrap.activate();

            yell(Messages.get(this, "four_tone_active"));
            phase++;
        } else if(phase >= 1 && !ThreePhaseActive) {
            // 检查三个boss是否都死亡
            boolean allDead = true;
            for (Char ch : Actor.chars()) {
                if ((ch instanceof ShubNiggurath || ch instanceof Nyarlathotep || ch instanceof YogSoul) && ch.isAlive()) {
                    allDead = false;
                    break;
                }
            }

            // 如果三个boss都已死亡，则进入第三阶段
            if (allDead) {
                Buff.detach(hero, MindVision.class);

                yell(Messages.get(this, "phase_tone_active"));

                MyCoreHeart myCoreHeart = new MyCoreHeart();
                myCoreHeart.pos = 312;
                GameScene.add(myCoreHeart);
                BossHealthBar.assignBoss(myCoreHeart);

                Buff.detach(hero, SliceDeadBless.class);

                TowerGodsBad towerGodsBad = new TowerGodsBad();
                towerGodsBad.pos = 304;
                GameScene.add(towerGodsBad);

                TowerTimeBad towerTimeBad = new TowerTimeBad();
                towerTimeBad.pos = 512;
                GameScene.add(towerTimeBad);

                TowerMachineBad towerMachineBad = new TowerMachineBad();
                towerMachineBad.pos = 112;
                GameScene.add(towerMachineBad);

                TowerMindBad towerMindBad = new TowerMindBad();
                towerMindBad.pos = 320;
                GameScene.add(towerMindBad);

                ScrollOfTeleportation.appear(this,562);
                ThreePhaseActive = true;
                phase = 2;
            }
        }

        return super.act();
    }


    @Override
    public void notice() {

        if (!BossHealthBar.isAssigned()) {
            Camera.main.shake(1f,3f);
            GameScene.bossReady();

            yell(Messages.get(this, "notice",Dungeon.hero.name()));

            for (Char ch : Actor.chars()){
                if (ch instanceof DriedRose.GhostHero){
                    ((DriedRose.GhostHero) ch).sayBoss();
                }
            }
        }
    }

    @Override
    public CharSprite sprite() {
        MorpheusSprite sprite = (MorpheusSprite) super.sprite();
        sprite.HatActivate();
        return sprite;
    }

    @Override
    public void die( Object cause ) {
        super.die(cause);
        GameScene.bossSlain();
    }

    private static final String FTAV = "FourToneActive";

    private static final String XDFR = "XDFR";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(FTAV, FourToneActive);
        bundle.put("phase", phase);
        bundle.put(XDFR, EndStory);
        bundle.put("ThreePhaseActive", ThreePhaseActive);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        FourToneActive = bundle.getBoolean(FTAV);
        phase = bundle.getFloat("phase");
        EndStory = bundle.getBoolean(XDFR);
        ThreePhaseActive = bundle.getBoolean("ThreePhaseActive");
    }

    public void activate(){
        ((MorpheusSprite) sprite).HatActivate();
    }

}
