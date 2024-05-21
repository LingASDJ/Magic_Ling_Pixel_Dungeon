package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.PaswordBadges;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NTNPC;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.plot.DwarfGrenEndPlot;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DwarfGeneralSprite;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndQuest;
import com.watabou.noosa.Game;
import com.watabou.noosa.tweeners.Delayer;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;

public class DwarfGeneralNTNPC extends NTNPC {

    {
        spriteClass = DwarfGeneralSprite.class;
    }

    private boolean first=true;
    private static final String FIRST = "first";
    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(FIRST, first);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        first = bundle.getBoolean(FIRST);
    }

    @Override
    public boolean interact(Char c) {

        sprite.turnTo(pos, hero.pos);
        if(first){
            DwarfGrenEndPlot plot = new DwarfGrenEndPlot();
            Game.runOnRenderThread(new Callback() {
                @Override
                public void call() {
                    GameScene.show(new WndDialog(plot,false));
                }
            });
            first = false;
        } else if(!Statistics.dwarfKill){
           Statistics.dwarfKill = true;
           yell(Messages.get(this,"goodbye",hero.name()));
            GameScene.scene.add(new Delayer(3f){
                @Override
                protected void onComplete() {
                    GameScene.scene.add(new Delayer(1f){
                        @Override
                        protected void onComplete() {
                            ShatteredPixelDungeon.seamlessResetScene();
                        }
                    });
                    ShatteredPixelDungeon.resetScene();
                    SPDSettings.KillDwarf(true);
                    PaswordBadges.KILLDWG();
                }
            });
        } else {
           tell(Messages.get(DwarfGeneralNTNPC.class, "xms"));
        }
        return true;
    }

    public static void tell(String text) {
        Game.runOnRenderThread(new Callback() {
                @Override
                public void call() {
                    GameScene.show(new WndQuest(new DwarfGeneralNTNPC(), text));
                }
            }
        );
    }


}
