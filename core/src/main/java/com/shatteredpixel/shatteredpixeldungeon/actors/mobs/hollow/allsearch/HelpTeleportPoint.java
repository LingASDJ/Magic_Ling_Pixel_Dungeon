package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.hollow.allsearch;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;
import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.level;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.status.ActivePoint;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.MobSpawner;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NTNPC;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.HelpTeleportPointSprites;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

public class HelpTeleportPoint extends NTNPC {

    public boolean activeX = false;

    private int MaxActive = 0;

    {
        spriteClass = HelpTeleportPointSprites.class;
        properties.add(Property.SEARCH);
    }


    public void activate(){
        ((HelpTeleportPointSprites) sprite).Activate();
    }

    public void deactivate(){
        ((HelpTeleportPointSprites) sprite).UnActivate();
    }

    @Override
    public boolean act() {
        if(activeX){
            activate();
        } else {
            deactivate();
        }
        return super.act();
    }

    @Override
    public boolean interact(Char c) {
        sprite.turnTo(pos, hero.pos);
        Game.runOnRenderThread(new Callback() {
            @Override
            public void call() {
                GameScene.show(new WndOptions(new HelpTeleportPointSprites(),
                        Messages.titleCase(Messages.get(this, "name")),
                        Messages.get(this, "quest_start_prompt",MaxActive),
                        Messages.get(this, "enter_yes"),
                        Messages.get(this, "enter_no")) {
                    @Override
                    protected void onSelect(int index) {
                        if (index == 0 && MaxActive < 2 && !activeX) {
                            activeX = true;
                            ActivePoint buff = hero.buff(ActivePoint.class);
                            if(buff != null){
                                buff.Active(1);
                            }
                            MaxActive++;
                            hero.sprite.showStatus(CharSprite.POSITIVE, Messages.get(HelpTeleportPointSprites.class, "active"));

                            for (int i = 0; i < Random.Int(7,14); i++) {
                                Mob mob = Reflection.newInstance(MobSpawner.getMobRotation(31).get(0));
                                GameScene.add(mob);
                                mob.pos = level.randomDestination(mob);
                                mob.state = mob.HUNTING;
                                mob.beckon( pos );
                                CellEmitter.center( pos ).start( Speck.factory( Speck.SCREAM ), 0.3f, 3 );
                            }

                            Sample.INSTANCE.play( Assets.Sounds.ALERT );
                            yell(Messages.get(HelpTeleportPoint.class, "alert_warning"));
                        } else if( MaxActive>=2 ){
                            yell(Messages.get(HelpTeleportPoint.class, "used_max_limit"));
                        } else if(activeX){
                            yell(Messages.get(HelpTeleportPoint.class, "used_active"));
                        }
                    }
                });
            }
        });
        return true;
    }

    private static final String ACTIVEX = "activeX";
    private static final String MAXACTIVE = "MaxActive";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(ACTIVEX, activeX);
        bundle.put(MAXACTIVE, MaxActive);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        activeX = bundle.getBoolean(ACTIVEX);
        MaxActive = bundle.getInt(MAXACTIVE);
    }

}
