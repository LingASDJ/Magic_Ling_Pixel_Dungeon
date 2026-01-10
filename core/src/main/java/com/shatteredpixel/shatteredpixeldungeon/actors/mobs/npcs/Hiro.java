package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.effects.FloatingText;
import com.shatteredpixel.shatteredpixeldungeon.items.OldSunShadow;
import com.shatteredpixel.shatteredpixeldungeon.items.TengusMask;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRemoveCurse;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.HiroSprites;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndQuest;
import com.watabou.noosa.Game;
import com.watabou.utils.Bundle;

import java.util.ArrayList;

public class Hiro extends NTNPC {

    private boolean first=true;
    private boolean secnod=true;
    private boolean rd=true;
    private boolean sd=true;
    private boolean kd=true;
    private boolean td=true;

    protected ArrayList<String> helloChat;
    protected ArrayList<String> aTalkChat;
    protected ArrayList<String> anoMageTalkChat;
    protected ArrayList<String> bnoMageTalkChat;
    protected ArrayList<String> bTalkChat;
    protected ArrayList<String> endChat;
    protected ArrayList<String> noMarkChat;

    {
        spriteClass = HiroSprites.class;
        helloChat = new ArrayList<String>() {
            {
                add(Messages.get(Hiro.class, "hello", hero.name()));
            }
        };

        aTalkChat = new ArrayList<String>() {
            {
                if(Dungeon.hero.HP > Dungeon.hero.HT*0.2f) {
                    add(Messages.get(Hiro.class, "talk_scroll"));
                } else {
                    add(Messages.get(Hiro.class, "talk_heal"));
                }
            }
        };

        anoMageTalkChat = new ArrayList<String>() {
            {
                add(Messages.get(Hiro.class, "talk_go"));
            }
        };

        bnoMageTalkChat = new ArrayList<String>() {
            {
                add("......");
            }
        };

        bTalkChat = new ArrayList<String>() {
            {
                add(Messages.get(Hiro.class, "talk_mark"));
            }
        };

        endChat = new ArrayList<String>() {
            {
                add(Messages.get(Hiro.class, "talk_mark_yes"));
            }
        };

        noMarkChat = new ArrayList<String>() {
            {
                add(Messages.get(Hiro.class, "talk_nomark"));
            }
        };
    }


    @Override
    public boolean interact(Char c) {
        sprite.turnTo(pos, hero.pos);
        if(first){
            WndQuest.chating(this,helloChat);
            first = false;
        } else if(secnod) {
            WndQuest.chating(this,aTalkChat);
            if(Dungeon.hero.HP > Dungeon.hero.HT*0.2f) {
                Dungeon.level.drop(new ScrollOfRemoveCurse(), hero.pos).sprite.drop();
            } else {
                Dungeon.level.drop(new PotionOfHealing(), hero.pos).sprite.drop();
            }
            secnod = false;
        } else if(rd) {
           if(hero.heroClass != HeroClass.MAGE){
               WndQuest.chating(this,anoMageTalkChat);
           } else  {
               WndQuest.chating(this,bTalkChat);
           }
            rd = false;
        } else if(sd) {
            if(hero.heroClass != HeroClass.MAGE){
                WndQuest.chating(this,bnoMageTalkChat);
            } else {
                Game.runOnRenderThread(() -> GameScene.show(new WndOptions(new HiroSprites(),
                        Messages.titleCase(Messages.get(Hiro.class, "name")),
                        Messages.get(Hiro.class, "quest_start_prompt"),
                        Messages.get(Hiro.class, "enter_yes"),
                        Messages.get(Hiro.class, "enter_no")) {
                    @Override
                    protected void onSelect(int index) {
                        if (index == 0) {
                            TengusMask tengusMask = hero.belongings.getItem(TengusMask.class);
                            if(tengusMask!=null){
                                tengusMask.detach( hero.belongings.backpack );
                                Dungeon.level.drop(new OldSunShadow(), hero.pos).sprite.drop();
                                sd = false;
                            } else {
                                sd = false;
                                WndQuest.chating(Hiro.this,noMarkChat);
                            }
                        }
                    }
                }));
            }
        } else if(kd) {
            WndQuest.chating(this,endChat);
            kd = false;
        } else if(td){
            WndQuest.chating(this, bnoMageTalkChat);
        }
        return true;
    }


    private static final String FIRST = "first";
    private static final String SECNOD = "secnod";
    private static final String RD = "rd";
    private static final String SD = "sd";
    private static final String KD = "kd";
    private static final String TD = "td";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(FIRST, first);
        bundle.put(SECNOD,secnod);
        bundle.put(RD,rd);
        bundle.put(SD,sd);
        bundle.put(KD,kd);
        bundle.put(TD,td);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        first = bundle.getBoolean(FIRST);
        secnod = bundle.getBoolean(SECNOD);
        rd = bundle.getBoolean(RD);
        sd = bundle.getBoolean(SD);
        kd = bundle.getBoolean(KD);
        td = bundle.getBoolean(TD);
    }

    private String def_verb(){
        FloatingText.show(sprite.x, sprite.y, pos, Messages.get(this, "def_verb_3"), CharSprite.NEGATIVE);
        return Messages.get(this, "def_verb");
    }

    @Override
    public String defenseVerb() {
        return def_verb();
    }

}
