package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.PaswordBadges;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.effects.FloatingText;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.OldSunShadow;
import com.shatteredpixel.shatteredpixeldungeon.items.TengusMask;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.TimeFlower;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.ConeAOE;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.HiroSprites;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
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

    public boolean flower = false;
    public boolean getFlower = false;

    protected ArrayList<String> helloChat;
    protected ArrayList<String> aTalkChat;
    protected ArrayList<String> bTalkChat;
    protected ArrayList<String> btkTalkChat;
    protected ArrayList<String> btkMageTalkChat;
    protected ArrayList<String> cTalkChat;
    protected ArrayList<String> ctkMageTalkChat;

    {
        spriteClass = HiroSprites.class;
        helloChat = new ArrayList<String>() {
            {
                add(Messages.get(Hiro.class, "hello", hero.name()));
            }
        };

        aTalkChat = new ArrayList<String>() {
            {
                if(!SPDSettings.HiroFirstDialog()) {
                    add(Messages.get(Hiro.class, "talk_hello"));
                } else {
                    add(Messages.get(Hiro.class, "talk_hello_no",SPDSettings.see_Hiros()));
                }
            }
        };

        bTalkChat = new ArrayList<String>() {
            {
                if(!SPDSettings.HiroFirstDialog()) {
                    add(Messages.get(Hiro.class, "talk_ano_1"));
                    add(Messages.get(Hiro.class, "talk_ano_2"));
                    add(Messages.get(Hiro.class, "talk_ano_3"));
                    add(Messages.get(Hiro.class, "talk_ano_4"));
                } else {
                    add(Messages.get(Hiro.class, "talk_ano_5"));
                }
            }
        };

        btkTalkChat = new ArrayList<String>() {
            {
                add(Messages.get(Hiro.class, "talk_btk_1"));
                add(Messages.get(Hiro.class, "talk_btk_2"));
                add(Messages.get(Hiro.class, "talk_btk_3"));
            }
        };

        btkMageTalkChat = new ArrayList<String>() {
            {
                add(Messages.get(Hiro.class, "talk_btk_1"));
                add(Messages.get(Hiro.class, "talk_btk_2"));
                add(Messages.get(Hiro.class, "talk_btk_3"));
                add(Messages.get(Hiro.class, "talk_btk_4"));
                add(Messages.get(Hiro.class, "talk_btk_5"));
            }
        };

        cTalkChat = new ArrayList<String>() {
            {
                add(Messages.get(Hiro.class, "talk_cno_1"));
                add(Messages.get(Hiro.class, "talk_cno_2"));
            }
        };

        ctkMageTalkChat = new ArrayList<String>() {
            {
                add(Messages.get(Hiro.class, "talk_ctk_1"));
            }
        };

        endChat = new ArrayList<String>() {
            {
                add("…………");
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
            if(!SPDSettings.HiroFirstDialog()){
                SPDSettings.HiroFirstDialog(true);
            }
            SPDSettings.seeHiros(1);
            secnod = false;
        } else if(rd) {
            WndQuest.chating(this,bTalkChat);
            Dungeon.level.drop(new TimeFlower(), hero.pos).sprite.drop();
            rd = false;
        } else if(sd && flower) {
            if (hero.heroClass == HeroClass.MAGE) {
                WndQuest.chating(this, btkMageTalkChat);
            } else {
                WndQuest.chating(this, btkTalkChat);
            }
            GLog.p(Messages.get(Hiro.class, "talk_hiro"));
            TimeFlower timeFlower = hero.belongings.getItem(TimeFlower.class);
            if(timeFlower!=null){
                timeFlower.powerFlower = true;
                Item.updateQuickslot();
                PaswordBadges.HIRO();
            }
            sd = false;
        } else if(kd && hero.heroClass == HeroClass.MAGE){
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
                            LilyBloomEffect(Hiro.this);
                        }
                        kd = false;
                    }
                }
            }));
        } else if(td) {
            if(hero.heroClass == HeroClass.MAGE){
                WndQuest.chating(this, ctkMageTalkChat);
                GLog.p(Messages.get(Hiro.class, "talk_hiro"));
                TimeFlower timeFlower = hero.belongings.getItem(TimeFlower.class);
                if(timeFlower!=null){
                    timeFlower.powerFlower = true;
                    Item.updateQuickslot();
                }
            } else {
                WndQuest.chating(this, cTalkChat);
            }
            td = false;
        } else {
            WndQuest.chating(this, endChat);
        }
        return true;
    }


    private static final String FIRST = "first";
    private static final String SECNOD = "secnod";
    private static final String RD = "rd";
    private static final String SD = "sd";
    private static final String KD = "kd";
    private static final String TD = "td";
    private static final String KRS = "krs";
    private static final String KTS = "kts";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(FIRST, first);
        bundle.put(SECNOD,secnod);
        bundle.put(RD,rd);
        bundle.put(SD,sd);
        bundle.put(KD,kd);
        bundle.put(TD,td);
        bundle.put(KRS,flower);
        bundle.put(KTS,getFlower);
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
        flower = bundle.getBoolean(KRS);
        getFlower = bundle.getBoolean(KTS);
    }

    public static void LilyBloomEffect(Char ch) {
        Ballistica aim;
        aim = new Ballistica(ch.pos, ch.pos - 1, Ballistica.STOP_TARGET);
        int projectileProps = 12;
        int aoeSize = 12;
        ConeAOE aoe = new ConeAOE(aim, aoeSize, 360, projectileProps);
        GameScene.flash(Window.GDX_COLOR);
        for (Ballistica ray : aoe.outerRays){
            ((MagicMissile)ch.sprite.parent.recycle( MagicMissile.class )).reset(
                    MagicMissile.WARD,
                    ch.sprite,
                    ray.path.get(ray.dist),
                    null
            );
        }
    }


    private String def_verb(){
        FloatingText.show(sprite.x+10, sprite.y, pos, Messages.get(this, "def_verb_3"), CharSprite.NEGATIVE);
        return Messages.get(this, "def_verb");
    }

    @Override
    public String defenseVerb() {
        return def_verb();
    }

}
