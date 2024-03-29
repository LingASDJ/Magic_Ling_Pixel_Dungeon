package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.notsync;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Statue;
import com.shatteredpixel.shatteredpixeldungeon.effects.Flare;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ShadowParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ShaftParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRemoveCurse;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ClearGuardSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.tweeners.Delayer;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;

public class ClearElemtGuard extends Statue {

    private int clearCount;
    private boolean recovering = false;
    public static final String RECOVERING = "recovering";


    {
        spriteClass = ClearGuardSprite.class;

        HP = HT = 100;

        defenseSkill = 14;
        state = WANDERING;
        maxLvl = -1;
        properties.add(Property.FIERY);
        properties.add(Property.INORGANIC);
    }

    protected Armor armor;

    @Override
    public void createWeapon(boolean useDecks) {
        super.createWeapon(useDecks);

        armor = Generator.randomArmor();
        armor.cursed = false;
        armor.inscribe(Armor.Glyph.random());
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put("count",clearCount);
        bundle.put(RECOVERING, recovering);

    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        clearCount = bundle.getInt("count");
        recovering = bundle.getBoolean(RECOVERING);
    }
    @Override
    protected boolean act() {

        if (clearCount>=2) {
            state = HUNTING;
            Music.INSTANCE.play(Assets.Music.PRISON_TENSE, true);
        } else {
            state = PASSIVE;
            sprite.emitter().burst( ShadowParticle.CURSE, 6 );
        }
        return super.act();
    }
    @Override
    public boolean add(Buff buff ) {
        if (clearCount>=2) {
           return super.add(buff);
        }  else {
            return false;
        }

    }


    @Override
    public void die( Object cause ) {
        super.die( cause );
        ((ClearGuardSprite)sprite).What_UP();
        //5s延迟后，恢复SANS状态
        yell(Messages.get(ClearElemtGuard.class,"kill"));
        Mob mob = new ClearElemtGuardNPC();
        mob.pos = pos;
        GameScene.add(mob);
    }

    @Override
    public void damage( int dmg, Object src ) {

        if (clearCount>=2) {

            super.damage( dmg, src );
        } else {
            Game.runOnRenderThread(new Callback() {
                @Override
                public void call() {
                    GameScene.show(new WndOptions(
                            new ClearGuardSprite(),
                            Messages.get(ClearElemtGuard.class, "namex" ),
                            Messages.get(ClearElemtGuard.class, "cashout_verify"),
                            Messages.get(ClearElemtGuard.class, "cashout_yes"),
                            Messages.get(ClearElemtGuard.class, "cashout_no")
                    ){
                        @Override
                        protected void onSelect(int index) {
                            if (index == 0){

                                ScrollOfRemoveCurse sc = hero.belongings.getItem(ScrollOfRemoveCurse.class);
                                if(sc != null){
                                    sc.detach(hero.belongings.backpack);
                                    clearCount++;
                                    cleanse();
                                } else {
                                    GLog.w(Messages.get(ClearElemtGuard.class, "cashout_none"));
                                }

                            }
                        }
                    });
                }
            } );
        }


    }

    public void cleanse(){
        if(clearCount>=2){
            sprite.emitter().start( ShaftParticle.FACTORY, 0.3f, 4 );
            Sample.INSTANCE.play( Assets.Sounds.RAY );
            GLog.w(Messages.get(this, "clear_"+clearCount));
            GameScene.scene.add(new Delayer(1f){
                @Override
                protected void onComplete() {
                    new Flare(6, 32).show(sprite, 2f);
                    state = HUNTING;
                    Music.INSTANCE.play(Assets.Music.PRISON_TENSE, true);
                    yell(Messages.get(ClearElemtGuard.class, "attack"));
                }
            });
        } else {
            Sample.INSTANCE.play( Assets.Sounds.CURSED );
            GLog.w(Messages.get(this, "clear_"+clearCount));
        }
    }

}
