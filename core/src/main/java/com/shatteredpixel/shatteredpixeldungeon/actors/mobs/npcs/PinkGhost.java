package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.food.SmallRation;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Red;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfRoseShiled;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.FiveRen;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.InterlevelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GhostSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndQuest;
import com.watabou.noosa.Game;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;

import java.util.ArrayList;

public class PinkGhost extends NTNPC {

    private boolean first=true;
    private boolean secnod=true;
    private boolean rd=true;

    private boolean sd=true;

    private static final String FIRST = "first";
    private static final String SECNOD = "secnod";
    private static final String RD = "rd";

    private static final String SD = "sd";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(FIRST, first);
        bundle.put(SECNOD,secnod);
        bundle.put(RD,rd);
        bundle.put(SD,sd);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        first = bundle.getBoolean(FIRST);
        secnod = bundle.getBoolean(SECNOD);
        rd = bundle.getBoolean(RD);
        sd = bundle.getBoolean(SD);
    }

    {
        spriteClass = GhostSprite.PinkGhostSprites.class;
        chat = new ArrayList<String>() {
            {
                add(Messages.get(PinkGhost.class, "hello"));
                add(Messages.get(PinkGhost.class, "hello_1"));
            }
        };
        endChat = new ArrayList<String>() {
            {
                add(Messages.get(PinkGhost.class, "hello_2"));
                add(Messages.get(PinkGhost.class, "hello_3"));
            }
        };
    }

    @Override
    public boolean interact(Char c) {
        Red red = hero.belongings.getItem(Red.class);
        sprite.turnTo(pos, hero.pos);
        if(first){
            WndQuest.chating(this,chat);
            first = false;
        } else if(secnod){
            WndQuest.chating(this,endChat);
            secnod= false;
        } else if(red == null && rd) {
            rd = false;
            InterlevelScene.mode = InterlevelScene.Mode.GARDEN;
            Game.switchScene(InterlevelScene.class);
        } else if(red == null) {
            GLog.pink(Messages.get(PinkGhost.class,"why_red"));
        } else {
            tell(this);
        }
        return true;
    }

    private void tells(String text) {
        Game.runOnRenderThread(new Callback() {
            @Override
            public void call() {
                GameScene.show(new WndQuest(new PinkGhost(), text));
            }
        }
        );
    }

    private void tell(Char ghost) {
        Weapon w3;
        w3 = (Weapon) Generator.random(Generator.Category.WEAPON);
        w3.upgrade(3);
        w3.identify();

        Armor a3;
        a3 = (Armor) Generator.randomUsingDefaults( Generator.Category.ARMOR );
        a3.upgrade(3);
        a3.identify();

        Game.runOnRenderThread(new Callback() {
            @Override
            public void call() {

                ShatteredPixelDungeon.scene().add(new WndOptions(new GhostSprite.PinkGhostSprites(),
                        Messages.get(PinkGhost.class, "name"),
                        Messages.get(PinkGhost.class, "good_desc"),
                        Messages.get(PinkGhost.class, "giverose"),
                        Messages.get(PinkGhost.class, "givemoon")) {
                    @Override
                    protected void onSelect(int index) {
                        SmallRation.BlackMoon moon = hero.belongings.getItem( SmallRation.BlackMoon.class);
                        Red red = hero.belongings.getItem(Red.class);
                        if (index == 0) {
                            if(red != null){
                                GLog.p(Messages.get(PinkGhost.class, "goodluck"));
                                red.detach(hero.belongings.backpack);

                                Dungeon.level.drop(a3, ghost.pos).sprite.drop();
                                Dungeon.level.drop(w3, ghost.pos).sprite.drop();
                                Dungeon.level.drop(new ScrollOfRoseShiled().identify(), ghost.pos );
                                Dungeon.level.drop(new FiveRen().identify(), ghost.pos );
                                die(null);
                            }
                        } else if (index == 1) {
                            if(moon == null){
                                sprite.showLost();
                                GLog.pink(Messages.get(PinkGhost.class, "whatmoon"));
                            } else {
                                tells(Messages.get(PinkGhost.class, "moonname"));
                                GLog.n(Messages.get(PinkGhost.class, "moonlost"));
                                moon.detach(hero.belongings.backpack);
                                Statistics.findMoon = true;
                                die( null );
                            }
                        }
                    }
                });
            }
        });
    }
}
