package com.shatteredpixel.shatteredpixeldungeon.items.lightblack;
//
// Decompiled by Jadx - 759ms
//

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Fire;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Light;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.FlameParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;

import java.util.ArrayList;

public class OilLantern extends Item {
    private static final String ACTIVE = "active";
    public static final String AC_BURN = "BURN";
    public static final String AC_LIGHT = "LIGHT";
    public static final String AC_REFILL = "REFILL";
    public static final String AC_SNUFF = "SNUFF";
    private static final String CHARGE = "charge";
    private static final String FLASKS = "flasks";
    private static final int MAX_CHARGE = 100;
    private static final float TIME_TO_USE = 1.0f;
    private static final String TXT_DEACTIVATE = "你的灯笼忽隐忽现地熄灭了!";
    private static final String TXT_LIGHT = "你点亮了灯笼。";
    private static final String TXT_NO_FLASKS = "你没油给灯加油了!";
    private static final String TXT_REFILL = "你把灯笼再次加满了。";
    private static final String TXT_SNUFF = "你吹灭了灯笼。";
    private static final String TXT_STATUS = "%d%%";
    private boolean active = false;
    private int charge = MAX_CHARGE;
    public int flasks = 0;

    public OilLantern() {
        this.image = ItemSpriteSheet.LANTERNA;
        this.unique = true;
        updateSprite();
    }

    public void updateSprite() {
        this.image = isActivated() ? ItemSpriteSheet.LANTERNB : ItemSpriteSheet.LANTERNA;
    }

    public int getCharge() {
        return this.charge;
    }

    public void spendCharge() {
        this.charge--;
        updateQuickslot();
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
        actions.add(AC_BURN);
        actions.remove("THROW");
        actions.remove("DROP");
        return actions;
    }

    public void execute(Hero hero, String action) {
        if (action.equals(AC_LIGHT)) {
            if (this.charge > 0) {
                activate(hero, true);
            }
        } else if (action.equals(AC_REFILL)) {
            if (this.flasks > 0) {
                refill(hero);
            } else {
                GLog.w(TXT_NO_FLASKS);
            }
        } else if (action.equals(AC_SNUFF)) {
            if (isActivated()) {
                deactivate(hero, true);
            }
        } else if (!action.equals(AC_BURN)) {
            OilLantern.super.execute(hero, action);
        } else if (this.flasks > 0) {
            curUser = hero;
            curItem = this;
            GameScene.selectCell(burner);
        } else {
            GLog.w("你需要一个备用的油瓶！");
        }
    }

    public void refill(Hero hero) {
        this.flasks--;
        this.charge = MAX_CHARGE;
        hero.spend(TIME_TO_USE);
        hero.busy();
        Sample.INSTANCE.play("sounds/snd_drink.mp3", (float) TIME_TO_USE, (float) TIME_TO_USE, 1.2f);
        hero.sprite.operate(hero.pos);
        GLog.i(TXT_REFILL);
        updateQuickslot();
    }

    public void activate(Hero hero, boolean voluntary) {
        this.active = true;
        updateSprite();
        Buff.affect(hero, Light.class);
        hero.search(false);
        if (voluntary) {
            hero.spend(TIME_TO_USE);
            hero.busy();
            GLog.i(TXT_LIGHT);
            hero.sprite.operate(hero.pos);
        }
        Sample.INSTANCE.play("sounds/snd_click.mp3");
        updateQuickslot();
        Dungeon.observe();
    }

    public void deactivate(Hero hero, boolean voluntary) {
        this.active = false;
        updateSprite();
        Buff.detach(hero, Light.class);
        if (voluntary) {
            hero.spend(TIME_TO_USE);
            hero.busy();
            hero.sprite.operate(hero.pos);
            GLog.i(TXT_SNUFF);
        } else {
            GLog.w(TXT_DEACTIVATE);
        }
        Sample.INSTANCE.play("sounds/snd_puff.mp3");
        updateQuickslot();
        Dungeon.observe();
    }

    public int price() {
        return 0;
    }

    public String status() {
        return Utils.format(TXT_STATUS, this.charge);
    }

    public String info() {
        StringBuilder sb = new StringBuilder();
        sb.append("这盏来自硬化玻璃的灯是暗无天日的地牢中不可缺少的物品。即使是在最黑暗的地牢里，这个简单的油灯也能照亮你的道路，只要你有油瓶来保持它的光亮。");
        sb.append(isActivated() ? "灯火旺盛得燃烧着，照亮了周围的一切与你的内心。 " : "这个小灯笼已经被掐灭了，等待着再次点亮的那一刻。");
        sb.append("你有 ");
        double d = this.charge;
        Double.isNaN(d);
        sb.append(d / 10.0d);
        sb.append("盎司的灯油和 _");
        sb.append(this.flasks);
        sb.append(" 备用瓶");
        sb.append(this.flasks != 1 ? "_" : "_");
        sb.append("余下。");
        return sb.toString();
    }

    protected CellSelector.Listener burner = new  CellSelector.Listener() {

        public void onSelect(Integer target) {
            if (target != null) {
                Ballistica.cast(Item.curUser.pos, target, false, true);
                int cell = Ballistica.trace[0];
                if (Ballistica.distance > 1) {
                    cell = Ballistica.trace[1];
                }
                GameScene.add(Blob.seed(cell, 5, Fire.class));
                Item.curItem.flasks--;
                Invisibility.dispel();
                if (Item.curUser.pos == cell) {
                    GLog.i("你将油瓶中的灯油浇到自己身上然后点上了火。你为什么要这样做？");
                } else {
                    GLog.i("你把灯油倒在了附近的地格并点着了那里。");
                }
                Sample.INSTANCE.play("sounds/snd_burning.mp3", 0.6f, 0.6f, 1.5f);
                CellEmitter.get(cell).burst(FlameParticle.FACTORY, 5);
                Item.curUser.sprite.operate(cell);
                Item.curUser.busy();
                Item.curUser.spend(1.0f);
            }
        }

        public String prompt() {
            return "选择一个身边的地格点燃";
        }

    };



}
