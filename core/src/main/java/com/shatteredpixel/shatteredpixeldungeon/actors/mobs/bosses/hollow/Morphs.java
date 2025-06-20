package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.hollow;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Boss;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.DriedRose;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MorpheusSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;
import com.watabou.noosa.Camera;
import com.watabou.utils.Bundle;

public class Morphs extends Boss {

    /**
     * 无敌判定
     * @param effect 无敌效果
     * @return true:无敌
     */
    @Override
    public boolean isInvulnerable(Class effect) {
        return true;
    }

    public boolean FourToneActive = false;

    public float phase;

    {
        initProperty();
        initBaseStatus(0, 0, 0, 0, 1, 0, 0);
        initStatus(0);

        spriteClass = MorpheusSprite.class;

        viewDistance = 100;

        alignment = Alignment.NEUTRAL;

        properties.add(Property.IMMOVABLE);
        properties.add(Property.BOSS);

        immunities.add(Blob.class);
        immunities.add(Buff.class);
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
            GameScene.add(sn);

            yell(Messages.get(this, "four_tone_active"));
        }

        return super.act();
    }

    @Override
    public void notice() {

        if (!BossHealthBar.isAssigned()) {
            BossHealthBar.assignBoss(this);
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

    private static final String FTAV = "FourToneActive";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(FTAV, FourToneActive);
        bundle.put("phase", phase);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        FourToneActive = bundle.getBoolean(FTAV);
        phase = bundle.getFloat("phase");
    }

    public void activate(){
        ((MorpheusSprite) sprite).HatActivate();
    }

}
