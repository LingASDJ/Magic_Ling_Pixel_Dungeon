package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.PaswordBadges;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.DriedRose;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Red;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.RedWhiteRose;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfRoseShiled;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.FiveRen;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GhostSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndQuest;
import com.watabou.noosa.Game;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;

import java.util.ArrayList;

public class PinkGhostNPC extends NPC {
    protected ArrayList<String> chat = new ArrayList<>();
    protected ArrayList<String> endChat = new ArrayList<>();

    protected ArrayList<String> sChat = new ArrayList<>();

    protected ArrayList<String> LChat = new ArrayList<>();

    protected ArrayList<String> TheEndChat = new ArrayList<>();
    protected ArrayList<String> MChat = new ArrayList<>();

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

        state = SLEEPING;
        chat = new ArrayList<String>() {
            {
                add(Messages.get(PinkGhost.class, "not_sorry"));
                add(Messages.get(PinkGhost.class, "not_sorry_1"));
                add(Messages.get(PinkGhost.class, "not_sorry_2"));
                add(Messages.get(PinkGhost.class, "not_sorry_3"));
                add(Messages.get(PinkGhost.class, "not_sorry_4"));
            }
        };
        endChat = new ArrayList<String>() {
            {
                add(Messages.get(GhostNPC.class, "love"));
            }
        };
        sChat = new ArrayList<String>() {
            {
                add(Messages.get(PinkGhost.class, "oh_no"));
            }
        };
        LChat = new ArrayList<String>() {
            {
                add(Messages.get(GhostNPC.class, "sorry"));
            }
        };
        TheEndChat = new ArrayList<String>() {
            {
                add(Messages.get(PinkGhost.class, "linklove"));
            }
        };
        MChat = new ArrayList<String>() {
            {
                add(Messages.get(PinkGhost.class, "thanks"));
                add(Messages.get(PinkGhost.class, "thanks_1"));
                add(Messages.get(PinkGhost.class, "thanks_2"));
                add(Messages.get(PinkGhost.class, "thanks_3"));
                add(Messages.get(PinkGhost.class, "thanks_4",hero.name()));
            }
        };
    }

    @Override
    public int defenseSkill( Char enemy ) {
        return INFINITE_EVASION;
    }

    @Override
    public float speed() {
        return 1.5f;
    }

    @Override
    protected Char chooseEnemy() {
        return null;
    }

    @Override
    public void damage( int dmg, Object src ) {
    }

    @Override
    public void add( Buff buff ) {
    }

    @Override
    public boolean reset() {
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

    @Override
    public boolean interact(Char c) {
        NPC questBoss;
        questBoss = new GhostNPC();

        sprite.turnTo( pos, c.pos );

        if (c != Dungeon.hero){
            return super.interact(c);
        }

        Weapon w3;
        w3 = (Weapon) Generator.random(Generator.Category.WEAPON);
        w3.upgrade(3);
        w3.identify();

        Armor a3;
        a3 = (Armor) Generator.randomUsingDefaults( Generator.Category.ARMOR );
        a3.upgrade(3);
        a3.identify();
        if (state == SLEEPING && first) {
            WndQuest.chating(this, chat);
            state = WANDERING;
            first = false;
        } else if(secnod) {
            Game.runOnRenderThread(new Callback() {
                @Override
                public void call() {
                    ShatteredPixelDungeon.scene().add(new WndOptions(new GhostSprite.PinkGhostSprites(),
                            Messages.get(PinkGhost.class, "name"),
                            Messages.get(PinkGhost.class, "talk_desc"),
                            Messages.get(PinkGhost.class, "givego"),
                            Messages.get(PinkGhost.class, "take")) {
                        @Override
                        protected void onSelect(int index) {
                            DriedRose rose = Dungeon.hero.belongings.getItem(DriedRose.class);
                            if (index == 0) {
                                    GLog.p(Messages.get(PinkGhost.class, "goodluck"));
                                Dungeon.level.drop(a3, pos).sprite.drop();
                                Dungeon.level.drop(w3, pos).sprite.drop();
                                Dungeon.level.drop(new ScrollOfRoseShiled().identify(), pos );
                                Dungeon.level.drop(new FiveRen().identify(),pos );
                                Statistics.findMoon = false;
                                die(null);
                            } else if (index == 1) {
                                if(rose == null){
                                    sprite.showLost();
                                    GLog.pink(Messages.get(PinkGhost.class, "whathere"));
                                } else {
                                    tells(Messages.get(PinkGhost.class, "rose"));
                                    secnod = false;
                                }
                            }
                        }
                    });
                }
            });
        } else if(rd) {
            questBoss.pos = pos+1;
            GameScene.add(questBoss, 1f);
            sprite.showAlert();
            WndQuest.chating(this, TheEndChat);
            WndQuest.chating(questBoss,LChat);
            WndQuest.chating(this, sChat);
            WndQuest.chating(questBoss, endChat);
            //GLog.p(Messages.get(PinkGhost.class,"love&love"));
            rd = false;
        } else if (sd) {
            WndQuest.chating(this,MChat);
            sd = false;
        } else {
            DriedRose rose = Dungeon.hero.belongings.getItem(DriedRose.class);
            Red red = hero.belongings.getItem(Red.class);
            GLog.i(Messages.get(PinkGhost.class, "thanks_5"));
            die(null);
            Statistics.deadGo = true;
            Dungeon.level.drop(a3, pos).sprite.drop();
            Dungeon.level.drop(w3, pos).sprite.drop();
            Dungeon.level.drop(new ScrollOfRoseShiled().identify(), pos );
            Dungeon.level.drop(new FiveRen().identify(),pos );
            Dungeon.level.drop( rose.ghostWeapon(), pos ).sprite.drop();
            Dungeon.level.drop( new RedWhiteRose(), pos ).sprite.drop();
            Dungeon.level.drop( rose.ghostArmor(), pos ).sprite.drop();
            rose.detach(hero.belongings.backpack);
            red.detach(hero.belongings.backpack);
            PaswordBadges.ZQJ_FLOWER();
        }
        return true;
    }
}

