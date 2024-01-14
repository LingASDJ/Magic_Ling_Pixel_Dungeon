package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.BruteBot;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.Cerberus;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.TyphonPlot;
import com.shatteredpixel.shatteredpixeldungeon.effects.Chains;
import com.shatteredpixel.shatteredpixeldungeon.effects.Effects;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.ConeAOE;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.TyphonSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndQuest;
import com.watabou.noosa.Game;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class Typhon extends NTNPC {

    {
        spriteClass = TyphonSprite.class;
        properties.add(Property.IMMOVABLE);
        flying = true;
    }

    private boolean first=true;
    public boolean secnod=true;
    public boolean rd=true;
    private int look = 0;

    private static final String FIRST = "first";
    private static final String SECNOD = "secnod";
    private static final String RD = "rd";

    private static final String LOOK = "look";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(FIRST, first);
        bundle.put(SECNOD, secnod);
        bundle.put(RD, rd);
        bundle.put(LOOK, look);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        first = bundle.getBoolean(FIRST);
        secnod = bundle.getBoolean(SECNOD);
        rd = bundle.getBoolean(RD);
        look = bundle.getInt(LOOK);
    }

    public static void tell(String text) {
        Game.runOnRenderThread(new Callback() {
                                   @Override
                                   public void call() {
                                       GameScene.show(new WndQuest(new Typhon(), text));
                                   }
                               }
        );
    }

    @Override
    public boolean interact(Char c) {

        sprite.turnTo(pos, hero.pos);


        TyphonPlot typhonPlot = new TyphonPlot();


        if(!secnod && first){
            Game.runOnRenderThread(new Callback() {
                @Override
                public void call() {
                    GameScene.show(new WndDialog(typhonPlot,false));
                }
            });
            first = false;
            return true;
        } else {
            tell(Messages.get(Typhon.class,"now_letgo"));
        }
        return true;
    }
    private static int[] FirstPos = new int[]{259,453,235,477};
    private static int[] EndPos = new int[]{386,326,324,388};
    @Override
    protected boolean act() {

        throwItem();

        sprite.turnTo( pos, hero.pos );

        final int chance = Random.NormalIntRange(50,120);

        if(look>chance && rd) {
            for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
                if (mob instanceof Cerberus) {
                    ScrollOfTeleportation.appear(mob, 356);
                    mob.sprite.idle();
                    mob.sprite.jump(46, 356, 95, 12f,new Callback() {
                        @Override
                        public void call() {
                            Game.runOnRenderThread(new Callback() {
                                @Override
                                public void call() {
                                    // This is Project,Shuold May be Can Work...
                                    mob.sprite.parent.add(new Chains(FirstPos[0], EndPos[0], Effects.Type.RED_CHAIN,null));

                                    mob.sprite.parent.add(new Chains(FirstPos[1], EndPos[1], Effects.Type.RED_CHAIN,null));

                                    mob.sprite.parent.add(new Chains(FirstPos[2], EndPos[2], Effects.Type.RED_CHAIN, null));

                                    mob.sprite.parent.add(new Chains(FirstPos[3], EndPos[3], Effects.Type.RED_CHAIN, null));

                                    mob.sprite.parent.add(new Chains(FirstPos[0], EndPos[1], Effects.Type.RED_CHAIN, null));

                                    mob.sprite.parent.add(new Chains(FirstPos[1], EndPos[2], Effects.Type.RED_CHAIN, null));

                                    mob.sprite.parent.add(new Chains(FirstPos[2], EndPos[0], Effects.Type.RED_CHAIN,null));

                                    mob.sprite.parent.add(new Chains(FirstPos[3], EndPos[0], Effects.Type.RED_CHAIN,new Callback() {
                                        @Override
                                        public void call() {
                                            GameScene.flash(Window.Pink_COLOR);
                                            DiedStorm(mob);
                                            mob.die(null);
                                            yell(Messages.get(Typhon.class,"dog_home"));
                                            Buff.detach(mob, BruteBot.BruteBotRage.class);
                                        }
                                    }));
                                }
                            });
                        }
                    });
                }
            }
            rd = false;
            secnod = false;
        } else {
            look++;
        }

        spend( TICK );
        return true;
    }

    @Override
    public void damage( int dmg, Object src ) {
    }

    @Override
    public int defenseSkill( Char enemy ) {
        return INFINITE_EVASION;
    }

    @Override
    public boolean reset() {
        return true;
    }


    public void DiedStorm(Char ch){
        Ballistica aim;
        aim = new Ballistica(ch.pos, ch.pos - 1, Ballistica.WONT_STOP);
        int projectileProps = Ballistica.STOP_SOLID | Ballistica.STOP_TARGET;
        int aoeSize = 8;
        ConeAOE aoe = new ConeAOE(aim, aoeSize, 360, projectileProps);

        for (Ballistica ray : aoe.outerRays){
            ((MagicMissile)ch.sprite.parent.recycle( MagicMissile.class )).reset(
                    MagicMissile.HALOFIRE,
                    ch.sprite,
                    ray.path.get(ray.dist),
                    null
            );
        }
    }
}
