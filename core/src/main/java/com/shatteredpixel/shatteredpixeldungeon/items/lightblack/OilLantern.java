package com.shatteredpixel.shatteredpixeldungeon.items.lightblack;
//
// Decompiled by Jadx - 759ms
//

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LighS;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Light;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LostInventory;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MagicTorch;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;

import java.util.ArrayList;

public class OilLantern extends Item {

    private static final String ACTIVE = "active";

    public static final String AC_LIGHT = "LIGHT";
    public static final String AC_REFILL = "REFILL";
    public static final String AC_SNUFF = "SNUFF";
    private static final String CHARGE = "charge";
    private static final String FLASKS = "flasks";

    private static final int MAX_CHARGE = 60;
    private static final float TIME_TO_USE = 2.0f;

    private static final String TXT_STATUS = "%d%%";
    private boolean active = false;
    private int charge = 100+Challenges.activeChallenges()/5*50;
    public int flasks = 0;

    public OilLantern() {
        this.image = ItemSpriteSheet.LANTERNA;
        this.unique = true;
        updateSprite();
        defaultAction = AC_LIGHT;
    }

    public void updateSprite() {
        this.image = isActivated() ? ItemSpriteSheet.LANTERNB : ItemSpriteSheet.LANTERNA;
        defaultAction = isActivated() ? AC_SNUFF : AC_LIGHT;
    }

    public int getCharge() {
        return this.charge;
    }

    public void spendCharge() {
        this.charge--;
        updateQuickslot();
        hero.healLantern(1);
    }

    public boolean isActivated() {
        return this.active;
    }

    public void storeInBundle(Bundle bundle) {
        OilLantern.super.storeInBundle(bundle);
        bundle.put(ACTIVE, this.active);
        bundle.put(CHARGE, this.charge);
        bundle.put(FLASKS, this.flasks);
    }

    public void restoreFromBundle(Bundle bundle) {
        OilLantern.super.restoreFromBundle(bundle);
        this.active = bundle.getBoolean(ACTIVE);
        this.charge = bundle.getInt(CHARGE);
        this.flasks = bundle.getInt(FLASKS);
        updateSprite();
    }

    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = OilLantern.super.actions(hero);
        actions.add(isActivated() ? AC_SNUFF : AC_LIGHT);
        actions.add(AC_REFILL);

        actions.remove("THROW");

        actions.remove("DROP");
        return actions;
    }

    public void execute(Hero hero, String action) {
        if (action.equals(AC_LIGHT)) {
            if (hero.buff(LostInventory.class) == null) {
                if (this.charge > 0) {
                    activate(hero, true);
                } else if (this.flasks > 0) {
                    GLog.w(Messages.get(OilLantern.class, "lanterneedsxs"));
                } else {
                    GLog.w(Messages.get(OilLantern.class, "lanterneedsx"));
                }
            } else {
                GLog.n("你陷入灵魂残缺的迷茫当中 无法引燃提灯");
            }
        } else if (action.equals(AC_REFILL)) {
            if (this.flasks > 0) {
                refill(hero);
            } else {
                GLog.w(Messages.get(OilLantern.class, "lanterneed"));
            }
        } else if (action.equals(AC_SNUFF)) {
            if (isActivated()) {
                deactivate(hero, true);
            }
        } else {
            GLog.w(Messages.get(OilLantern.class, "lanterneeds"));
        }
    }

    public void refill(Hero hero) {
        this.flasks--;
        this.charge += Math.min(MAX_CHARGE,100);
        hero.spend(TIME_TO_USE);
        hero.busy();
        Sample.INSTANCE.play(Assets.Sounds.DRINK, TIME_TO_USE, TIME_TO_USE, 1.2f);
        hero.sprite.operate(hero.pos);
        GLog.i(Messages.get(OilLantern.class, "lanterreload"));
        updateQuickslot();
    }

    public void activate(Hero hero, boolean voluntary) {
        if (voluntary) {
            if (Dungeon.hero.buff(Light.class) != null || Dungeon.hero.buff(MagicTorch.MagicLight.class) != null) {
                GLog.n("你已有其他光芒效果，在这些效果取消或主动失效前，暂时无法使用提灯的效果。");
            } else {
                hero.spend(TIME_TO_USE);
                hero.busy();
                GLog.i(Messages.get(OilLantern.class, "lanteron"));
                hero.sprite.operate(hero.pos);
                this.active = true;
                updateSprite();
                Buff.affect(hero, LighS.class);
                hero.search(false);
                Sample.INSTANCE.play("sounds/snd_click.mp3");
                updateQuickslot();
                Dungeon.observe();
            }

        }

//        if (voluntary && hero.lanterfire > 0) {
//            hero.spend(TIME_TO_USE);
//            hero.busy();
//            GLog.i(Messages.get(OilLantern.class, "lanteron"));
//            hero.sprite.operate(hero.pos);
//            this.active = true;
//            updateSprite();
//            Buff.affect(hero, LighS.class);
//            hero.search(false);
//            Sample.INSTANCE.play("sounds/snd_click.mp3");
//            updateQuickslot();
//            Dungeon.observe();
//        } else {
//            GameScene.flash(0x880000);
//            GLog.n(Messages.get(OilLantern.class, "black"));
//        }

    }

    public void deactivate(Hero hero, boolean voluntary) {
        this.active = false;
        updateSprite();
        Buff.detach(hero, LighS.class);
        if (voluntary) {
            hero.spend(TIME_TO_USE);
            hero.busy();
            hero.sprite.operate(hero.pos);
            GLog.i(Messages.get(OilLantern.class, "lanteroff"));
        } else {
            GLog.w(Messages.get(OilLantern.class, "lanterdied"));
        }
        Sample.INSTANCE.play("sounds/snd_puff.mp3");
        updateQuickslot();
        Dungeon.observe();
    }

    public int price() {
        return 0;
    }

    @Override
    public boolean isUpgradable() {
        return false;
    }

    public String status() {
        return Utils.format(TXT_STATUS, this.charge);
    }
}
