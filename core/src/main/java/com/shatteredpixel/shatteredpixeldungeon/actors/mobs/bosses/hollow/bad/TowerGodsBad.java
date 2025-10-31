package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.hollow.bad;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ArtifactRecharge;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Healing;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.hollow.MyCoreHeart;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MobSprite;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.watabou.noosa.Game;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;

public class TowerGodsBad extends Mob {

    public boolean repiaer =false;

    private static final String REPIAER = "repiaer";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(REPIAER, repiaer);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        repiaer = bundle.getBoolean(REPIAER);
    }

    {
        HP = HT = 1;
        spriteClass = TowerBadGodSprite.class;
    }

    @Override
    public String name(){
        String s = super.name();
        if(repiaer){
            s = Messages.get(this, "name_x");
        }
        return s;
    }

    @Override
    public String description(){
        String s = super.description();
        if(repiaer){
            s = Messages.get(this, "desc_x");
        }
        return s;
    }

    public int slowCoolDown;
    public int blobCoolDown;

    @Override
    protected boolean act() {
        alerted = false;
        state = PASSIVE;
        if(repiaer && buff(MyCoreHeart.RepaierDown.class)==null && slowCoolDown <= 0){
            ((TowerBadGodSprite) sprite).ReActivate();
            for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
                if(mob instanceof MyCoreHeart ){
                    Buff.affect(mob, Healing.class).setHeal(5, 0f, 6);
                }
            }
            slowCoolDown = 30;
        }

        if(buff(MyCoreHeart.RepaierDown.class)==null && repiaer && Dungeon.hero.buff(ArtifactRecharge.class) == null && blobCoolDown <= 0){
            Buff.affect(Dungeon.hero, ArtifactRecharge.class).prolong(10f);
            blobCoolDown = 40;
        }
        if(blobCoolDown >= 0){
            blobCoolDown--;
        }
        if(slowCoolDown >= 0){
            slowCoolDown--;
        }

        return super.act();
    }

    @Override
    public int defenseSkill( Char enemy ) {
        return 0;
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

        if(buff(MyCoreHeart.RepaierDown.class)==null){
            if(Statistics.RepaierTowerCount>0 && !repiaer){
                Game.runOnRenderThread(new Callback() {
                    @Override
                    public void call() {
                        GameScene.show(new WndOptions(new TowerGodsBad.TowerBadGodSprite(),
                                Messages.titleCase(Messages.get(TowerGodsBad.class, "name")),
                                Messages.get(MyCoreHeart.class, "repaier"),
                                Messages.get(MyCoreHeart.class, "enter_yes"),
                                Messages.get(MyCoreHeart.class, "enter_no")) {
                            @Override
                            protected void onSelect(int index) {
                                if (index == 0) {
                                    Buff.affect(TowerGodsBad.this,
                                            MyCoreHeart.RepaierDown.class,
                                            MyCoreHeart.RepaierDown.DURATION);
                                    Statistics.RepaierTowerCount--;
                                    repiaer = true;
                                }
                            }
                        });
                    }
                });
            }
        }


        return true;
    }

    public static class TowerBadGodSprite extends MobSprite {
        Animation activeIdle;
        public TowerBadGodSprite() {
            super();

            texture( Assets.Sprites.TowerGods );

            TextureFilm frames = new TextureFilm( texture, 32, 32 );

            idle = new MovieClip.Animation( 10, true );
            idle.frames( frames, Rules() );

            activeIdle = new MovieClip.Animation( 10, true );
            activeIdle.frames( frames, 0 );

            run = new MovieClip.Animation( 10, true );
            run.frames( frames, 5);

            attack = new MovieClip.Animation( 10, false );
            attack.frames( frames, 5 );

            die = new MovieClip.Animation( 9, false );
            die.frames( frames, 1,2,3,4,5,6 );

            play( idle );
        }

        private int Rules() {
            int get = 0;
            for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
                if (mob instanceof TowerGodsBad) {
                    get = ((TowerGodsBad) mob).repiaer ? 0 : 5;
                }
            }
            return get;
        }

        public void ReActivate(){
            idle = activeIdle.clone();
            idle();
        }

    }
}
