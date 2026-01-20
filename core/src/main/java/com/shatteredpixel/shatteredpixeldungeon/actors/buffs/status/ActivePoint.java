package com.shatteredpixel.shatteredpixeldungeon.actors.buffs.status;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.hollow.allsearch.HelpTeleportPoint;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Camera;
import com.watabou.noosa.tweeners.Delayer;
import com.watabou.utils.Bundle;

public class ActivePoint extends Buff {

    private int level = 0;

    public int escLimit = 201;

    public int active = 0;

    public boolean escActive = false;

    private int interval = 1;

    @Override
    public boolean act() {
        if (target.isAlive()) {

            spend( interval );

            if (level <= 0) {
                detach();
            }

            if(escLimit>0 && escLimit != 201){
                escLimit--;
            } else  if(escLimit <= 0 && escActive){
                escLimit = 201;
                escActive = false;
                active = 0;
                for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])){
                    if (mob instanceof HelpTeleportPoint) {
                        ((HelpTeleportPoint) mob).activeX = false;
                    }
                }
            }
        } else {
            detach();
        }

        return true;
    }

    public int level() {
        return level;
    }

    public void set( int value, int time ) {
        if (level <= value) {
            level = value;
            interval = time;
            spend(time - cooldown() - 1);
        }
    }

    public void Active( int value) {
        active += value;
        if(active>=2){
            escLimit = 200;
            escActive = true;
            Camera.main.panTo(DungeonTilemap.tileCenterToWorld(Dungeon.level.exit()), 5f);
            GLog.w(Messages.get(HelpTeleportPoint.class,"esc"));
            hero.busy();
            GameScene.scene.add(new Delayer(2f){
                @Override
                protected void onComplete() {
                    Camera.main.panTo(DungeonTilemap.tileCenterToWorld(hero.pos), 5f);
                    hero.ready();
                }
            });
        }
    }

    @Override
    public int icon() {
        return BuffIndicator.BASE_STATUS;
    }

    @Override
    public String iconTextDisplay() {
        return Integer.toString(escLimit);
    }

    @Override
    public String desc() {
        return Messages.get(this, "desc", active, escLimit);
    }

    private static final String LEVEL	    = "level";
    private static final String INTERVAL    = "interval";
    private static final String ESCLIMIT = "esclimit";
    private static final String ACTIVE_COUNT = "active_count";

    private static final String ACTIVE_ESC = "active_esc";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( INTERVAL, interval );
        bundle.put( LEVEL, level );
        bundle.put( ESCLIMIT, escLimit );
        bundle.put( ACTIVE_COUNT, active );
        bundle.put( ACTIVE_ESC, escActive );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        interval = bundle.getInt( INTERVAL );
        level = bundle.getInt( LEVEL );
        escLimit = bundle.getInt( ESCLIMIT );
        active = bundle.getInt( ACTIVE_COUNT );
        escActive = bundle.getBoolean( ACTIVE_ESC );
    }
}

