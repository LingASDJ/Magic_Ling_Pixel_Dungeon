package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.plot.BoatPlot;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DeathRongSprite;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndQuest;
import com.watabou.noosa.Game;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;

public class DeathRong extends NTNPC {

    {
        spriteClass = DeathRongSprite.class;
        properties.add(Property.IMMOVABLE);
        flying = true;
    }


    private boolean first=true;
    public boolean secnod=true;
    public boolean rd=true;

    private static final String FIRST = "first";
    private static final String SECNOD = "secnod";
    private static final String RD = "rd";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(FIRST, first);
        bundle.put(SECNOD, secnod);
        bundle.put(RD, rd);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        first = bundle.getBoolean(FIRST);
        secnod = bundle.getBoolean(SECNOD);
        rd = bundle.getBoolean(RD);
    }

    @Override
    protected boolean act() {

        throwItem();

        sprite.turnTo( pos, Dungeon.hero.pos );
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

    @Override
    public boolean interact(Char c) {

        sprite.turnTo(pos, Dungeon.hero.pos);
        BoatPlot plot = new BoatPlot();

       // RenPlot.End endplot = new RenPlot.End();

        if(first){
            Game.runOnRenderThread(new Callback() {
                @Override
                public void call() {
                    GameScene.show(new WndDialog(plot,false));
                }
            });
            first=false;
        } else if(secnod) {
            Game.runOnRenderThread(new Callback() {
                @Override
                public void call() {
                    GameScene.show(new WndOptions(new DeathRongSprite(),
                            Messages.titleCase(Messages.get(DeathRong.class, "name")),
                            Messages.get(DeathRong.class, "quest_start_prompt"),
                            Messages.get(DeathRong.class, "gold_yes"),
                            Messages.get(DeathRong.class, "gold_no")) {
                        @Override
                        protected void onSelect(int index) {
                            if (index == 0) {

                                if(Dungeon.gold >= 450 && secnod){
                                    for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])){
                                        if ( mob instanceof ZeroBoat) {
                                            ((ZeroBoat) mob).rd = true;
                                            secnod = false;
                                        }
                                        Dungeon.gold-=450;
                                    }
                                    tell(Messages.get(DeathRong.class, "good_gold"));
                                } else {
                                    tell(Messages.get(DeathRong.class, "not_money"));
                                    hide();
                                }
                            } else if(index == 1) {
                                tell(Messages.get(DeathRong.class, "can_not_money"));
                                hide();
                            }
                        }
                    });
                }

            });
        } else {
            tell(Messages.get(DeathRong.class, "can_boat"));
        }

        return true;
    }

    public static void tell(String text) {
        Game.runOnRenderThread(new Callback() {
                  @Override
                  public void call() {
                      GameScene.show(new WndQuest(new DeathRong(), text));
                  }
            }
        );
    }


}

