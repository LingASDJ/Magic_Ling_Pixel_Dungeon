package com.shatteredpixel.shatteredpixeldungeon.items.quest;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.pets.MiniSaka;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.watabou.noosa.Image;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class SakaFishSketon extends Item {

    public static final String AC_SummonFish = "SummonFish";
    public static final String AC_Died_SummonFish = "SummonFishDied";

    public int waterlevel = 0;

    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        waterlevel = bundle.getInt("waterlevel");
    }

    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put("waterlevel", waterlevel);
    }

    {
        image = ItemSpriteSheet.FISHSKELETON;
        stackable = true;
        defaultAction = AC_SummonFish;
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return waterlevel == 1 ? new ItemSprite.Glowing(0xFCE9CC, 6f) : null;
    }

    @Override
    public String defaultAction() {
        boolean needToSpawn = true;
        for (Mob mob : Dungeon.level.mobs){
            if (mob instanceof MiniSaka) {
                needToSpawn = false;
                break;
            }
        }

        if (waterlevel == 1 && !needToSpawn){
            return AC_Died_SummonFish;
        } else if(hero.buff(CoolDownStoneRecharge.class) == null) {
            return AC_SummonFish;
        } else {
            return AC_THROW;
        }
    }

    public ArrayList<String> actions( Hero hero ) {
        ArrayList<String> actions = super.actions( hero );
        boolean needToSpawn = true;

        for (Mob mob : Dungeon.level.mobs){
            if (mob instanceof MiniSaka) {
                needToSpawn = false;
                break;
            }
        }

        if (hero.buff(CoolDownStoneRecharge.class) != null && waterlevel == 1){
            waterlevel = 0;
        }

        if (needToSpawn && hero.buff(CoolDownStoneRecharge.class) == null){
            actions.add(AC_SummonFish);
        } else if(hero.buff(CoolDownStoneRecharge.class) == null) {
            actions.add(AC_Died_SummonFish);
        }

        return actions;
    }

    @Override
    public void execute(Hero hero, String action ) {

        super.execute(hero, action);
        PotionOfHealing potionOfHealing= hero.belongings.getItem(PotionOfHealing.class);
        if (action.equals(AC_SummonFish)) {
            if(potionOfHealing != null && waterlevel < 1){
                GameScene.show(new WndOptions(new ItemSprite(this),
                        Messages.titleCase( Messages.get(this, "saka")),
                        Messages.get(this, "wnd_body"),
                        Messages.get(this, "wnd_set"),
                        Messages.get(this, "wnd_return")){
                    @Override
                    protected void onSelect(int index) {
                        if (index == 0){
                            waterlevel = 1;
                            potionOfHealing.detach( hero.belongings.backpack );
                            hero.sprite.operate(hero.pos);
                            hero.busy();
                            GLog.p(Messages.get(SakaFishSketon.class, "you_active"));
                        }
                    }
                });
            } else if(waterlevel == 1){
                hero.sprite.operate(hero.pos, new Callback() {
                    @Override
                    public void call() {
                        ArrayList<Integer> respawnPoints = new ArrayList<>();
                        for (int i = 0; i < PathFinder.NEIGHBOURS8.length; i++) {
                            int p = hero.pos + PathFinder.NEIGHBOURS8[i];
                            if (Actor.findChar(p) == null && Dungeon.level.passable[p]) {
                                respawnPoints.add(p);
                            }
                        }
                        if (respawnPoints.size() > 0) {
                            MiniSaka fish = new MiniSaka();
                            fish.pos = respawnPoints.get(Random.index( respawnPoints ));
                            GameScene.add(fish);
                            fish.state = fish.WANDERING;
                            fish.sprite.emitter().burst(Speck.factory(Speck.STAR), 10);
                            hero.sprite.idle();
                        }
                    }
                });
            } else {
                GLog.w(Messages.get(SakaFishSketon.class, "you_must_potion"));
            }
        } else if (action.equals(AC_Died_SummonFish)) {
            GameScene.show(new WndOptions(new ItemSprite(this),
                    Messages.titleCase( Messages.get(this, "saka2")),
                    Messages.get(this, "wnd2_body"),
                    Messages.get(this, "wnd2_set"),
                    Messages.get(this, "wnd2_return")){
                @Override
                protected void onSelect(int index) {
                    if (index == 0){
                        for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])){
                            if (mob instanceof MiniSaka) {
                                mob.die(null);
                            }
                        }
                        waterlevel = 0;
                        Buff.affect(hero, CoolDownStoneRecharge.class, CoolDownStoneRecharge.DURATION);
                        hero.sprite.operate(hero.pos);
                        hero.busy();
                        GLog.w(Messages.get(SakaFishSketon.class, "pets_died"));
                    }
                }
            });


        }
    }

    @Override
    public boolean isUpgradable() {
        return false;
    }

    @Override
    public boolean isIdentified() {
        return true;
    }

    @Override
    public int value() {
        if(Statistics.sakaBackStage>=2){
            return quantity * 1250;
        } else {
            return quantity * 50;
        }
    }

    public static class CoolDownStoneRecharge extends FlavourBuff {

        public static final float DURATION = 300f;

        {
            type = buffType.NEGATIVE;
            announced = true;
        }

        @Override
        public boolean act() {

            detach();
            boolean needToSpawn = true;

            for (Mob mob : Dungeon.level.mobs){
                if (mob instanceof MiniSaka) {
                    needToSpawn = false;
                    break;
                }
            }
            if (needToSpawn && hero.buff(CoolDownStoneRecharge.class) == null){
                GLog.p( Messages.get(SakaFishSketon.class, "charged") );
            }
            return true;
        }

        @Override
        public int icon() {
            return BuffIndicator.TIME;
        }

        @Override
        public void tintIcon(Image icon) {
            icon.hardlight(0xFCE9CC);
        }

        @Override
        public float iconFadePercent() {
            return Math.max(0, (DURATION - visualcooldown()) / DURATION);
        }

        @Override
        public String toString() {
            return Messages.get(this, "name");
        }

        @Override
        public String desc() {
            return Messages.get(this, "desc", dispTurns());
        }

    }

}

