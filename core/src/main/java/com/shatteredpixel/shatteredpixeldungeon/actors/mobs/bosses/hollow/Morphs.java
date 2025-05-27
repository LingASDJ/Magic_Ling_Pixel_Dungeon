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

    {
        initProperty();
        initBaseStatus(0, 0, 0, 0, 50000, 0, 0);
        initStatus(0);

        spriteClass = MorpheusSprite.class;

        viewDistance = 100;

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
        if(!Dungeon.level.locked){
            Dungeon.level.seal();
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
        return super.act();
    }

    @Override
    public CharSprite sprite() {
        MorpheusSprite sprite = (MorpheusSprite) super.sprite();
        sprite.HatActivate();
        return sprite;
    }

    public void activate(){
        ((MorpheusSprite) sprite).HatActivate();
    }

}
