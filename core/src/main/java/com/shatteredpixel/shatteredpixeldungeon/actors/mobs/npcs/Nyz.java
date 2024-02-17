package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Conducts;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AscensionChallenge;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Bomb;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Firebomb;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Flashbang;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.FrostBomb;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.HolyBomb;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Noisemaker;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.RegrowthBomb;
import com.shatteredpixel.shatteredpixeldungeon.items.books.Books;
import com.shatteredpixel.shatteredpixeldungeon.items.books.bookslist.BrokenBooks;
import com.shatteredpixel.shatteredpixeldungeon.items.books.bookslist.DeepBloodBooks;
import com.shatteredpixel.shatteredpixeldungeon.items.books.bookslist.IceCityBooks;
import com.shatteredpixel.shatteredpixeldungeon.items.books.bookslist.MagicGirlBooks;
import com.shatteredpixel.shatteredpixeldungeon.items.books.bookslist.NoKingMobBooks;
import com.shatteredpixel.shatteredpixeldungeon.items.books.bookslist.YellowSunBooks;
import com.shatteredpixel.shatteredpixeldungeon.levels.RegularLevel;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.NyzSprites;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndNyzShop;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndQuest;
import com.watabou.noosa.Game;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Nyz extends NTNPC {

    {

        spriteClass = NyzSprites.class;
        properties.add(Property.UNKNOWN);
        maxLvl = -1;
        properties.add(Property.IMMOVABLE);

        chat = new ArrayList<String>() {
            {
                if(RegularLevel.holiday == RegularLevel.Holiday.CJ) {
                    add(Messages.get(Nyz.class, "dhat_1",hero.name()));
                    add(Messages.get(Nyz.class, "dhat_2",hero.name()));
                    add(Messages.get(Nyz.class, "dhat_3"));
                } else {
                    add(Messages.get(Nyz.class, "chat_1"));
                    add(Messages.get(Nyz.class, "chat_2"));
                    add(Messages.get(Nyz.class, "chat_3"));
                }
            }
        };
    }
    private boolean seenBefore = false;

    protected boolean act() {

        Game.runOnRenderThread(new Callback() {
            @Override
            public void call() {
                if (!seenBefore && Dungeon.level.heroFOV[pos]) {
                    if (Dungeon.hero.buff(AscensionChallenge.class) != null) {
                        yell(Messages.get(this, "talk_ascent", Messages.titleCase(Dungeon.hero.name())));
                    } else {
                        GLog.p(Messages.get(Nyz.class, "greetings", hero.name()));
                    }

                    seenBefore = true;
                } else if(seenBefore && !Dungeon.level.heroFOV[pos]) {
                    seenBefore = false;
                }
            }
        });

        throwItem();

        sprite.turnTo( pos, hero.pos );
        spend( TICK );

        shop6 = (Books) new YellowSunBooks().quantity(Random.Int(1,2));
        shop5 = (Books) new BrokenBooks().quantity(Random.Int(1,2));
        shop4 = (Books) new IceCityBooks().quantity(Random.Int(1,2));
        shop3 = (Books) new NoKingMobBooks().quantity(Random.Int(1,2));
        shop2 = (Books) new DeepBloodBooks().quantity(Random.Int(1,2));
        shop1 = (Books) new MagicGirlBooks().quantity(Random.Int(1,2));
        bomb1 = (Bomb) new Flashbang().quantity(1);
        bomb2 = (Bomb) new Noisemaker().quantity(1);
        bomb3 = (Bomb) new RegrowthBomb().quantity(1);
        bomb4 = (Bomb) new HolyBomb().quantity(1);
        bomb5 = (Bomb) new Firebomb().quantity(1);
        bomb6 = (Bomb) new FrostBomb().quantity(1);
        throwItem();
        return Nyz.super.act();
    }

    public static Books shop1;
    public static Books shop2;
    public static Books shop3;
    public static Books shop4;
    public static Books shop5;
    public static Books shop6;

    public static Bomb bomb1;
    public static Bomb bomb2;
    public static Bomb bomb3;
    public static Bomb bomb4;
    public static Bomb bomb5;
    public static Bomb bomb6;
    public int defenseSkill(Char enemy) {
        return 1000;
    }

    private void tell(String text) {
        Game.runOnRenderThread(new Callback() {
            @Override
                public void call() {
                    GameScene.show(new WndQuest(new Nyz(), text));
                }
            }
        );
    }

    public boolean reset() {
        return true;
    }

    public boolean interact(Char c) {
        this.sprite.turnTo(this.pos, hero.pos);
        if (seenBefore && Dungeon.level.heroFOV[pos] && Dungeon.depth != 0 || Dungeon.isDLC(Conducts.Conduct.BOSSRUSH)) {
            Game.runOnRenderThread(new Callback() {
                @Override
                public void call() {
                    GameScene.show(new WndNyzShop(this));
                }
            });
        } else {
            WndQuest.chating(this,chat);
        }
        return true;
    }
}
