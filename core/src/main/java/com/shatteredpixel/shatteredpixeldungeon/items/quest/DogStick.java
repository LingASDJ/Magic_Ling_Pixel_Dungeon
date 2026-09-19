package com.shatteredpixel.shatteredpixeldungeon.items.quest;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.pets.MiniCerberus;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.pets.MiniSaka;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfLiquidFlame;
import com.shatteredpixel.shatteredpixeldungeon.journal.Bestiary;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.watabou.noosa.Image;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class DogStick extends Item {

    public static final String AC_SummonDog = "SummonDog";
    public static final String AC_Died_SummonDog = "SummonDogDied";

    public int fireLevel = 0;

    {
        image = ItemSpriteSheet.DOGSTICK;
        stackable = true;
        defaultAction = AC_SummonDog;
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return fireLevel == 1 ? new ItemSprite.Glowing(0xE7971D, 6f) : null;
    }

    @Override
    public String defaultAction() {
        boolean needToSpawn = true;
        boolean needNoFish = true;

        for (Mob mob : Dungeon.level.mobs){
            if (mob instanceof MiniSaka) {
                needNoFish = false;
                break;
            }
        }

        for (Mob mob : Dungeon.level.mobs){
            if (mob instanceof MiniCerberus) {
                needToSpawn = false;
                break;
            }
        }

        if (fireLevel == 1 && !needToSpawn){
            return AC_Died_SummonDog;
        } else if(hero.buff(CoolDownStoneRecharge.class) == null && needNoFish) {
            return AC_SummonDog;
        } else {
            return AC_THROW;
        }
    }

    public ArrayList<String> actions(Hero hero ) {
        ArrayList<String> actions = super.actions( hero );
        boolean needToSpawn = true;
        boolean needNoFish = true;

        for (Mob mob : Dungeon.level.mobs){
            if (mob instanceof MiniSaka) {
                needNoFish = false;
                break;
            }
        }

        for (Mob mob : Dungeon.level.mobs){
            if (mob instanceof MiniCerberus) {
                needToSpawn = false;
                break;
            }
        }

        if (hero.buff(CoolDownStoneRecharge.class) != null && fireLevel == 1){
            fireLevel = 0;
        }

        if (needToSpawn && hero.buff(CoolDownStoneRecharge.class) == null && needNoFish){
            actions.add(AC_SummonDog);
        } else if(hero.buff(CoolDownStoneRecharge.class) == null && needNoFish) {
            actions.add(AC_Died_SummonDog);
        }

        return actions;
    }

    @Override
    public void execute(Hero hero, String action ) {

        super.execute(hero, action);
        PotionOfLiquidFlame potionOfLiquidFlame = hero.belongings.getItem(PotionOfLiquidFlame.class);
        if (action.equals(AC_SummonDog)) {
            if(potionOfLiquidFlame != null && fireLevel < 1){
                GameScene.show(new WndOptions(new ItemSprite(this),
                        Messages.titleCase( Messages.get(this, "dog")),
                        Messages.get(this, "wnd_body"),
                        Messages.get(this, "wnd_set"),
                        Messages.get(this, "wnd_return")){
                    @Override
                    protected void onSelect(int index) {
                        if (index == 0){
                            fireLevel = 1;
                            potionOfLiquidFlame.detach( hero.belongings.backpack );
                            hero.sprite.operate(hero.pos);
                            hero.busy();
                            GLog.p(Messages.get(DogStick.class, "you_active"));
                        }
                    }
                });
            } else if(fireLevel == 1){
                hero.sprite.operate(hero.pos, () ->{
                    ArrayList<Integer> respawnPoints = new ArrayList<>();
                    for (int i = 0; i < PathFinder.NEIGHBOURS8.length; i++) {
                        int p = hero.pos + PathFinder.NEIGHBOURS8[i];
                        if (Actor.findChar(p) == null && Dungeon.level.passable[p]) {
                            respawnPoints.add(p);
                        }
                    }
                    if (!respawnPoints.isEmpty()) {
                        MiniCerberus dogPet = new MiniCerberus();
                        dogPet.pos = respawnPoints.get(Random.index( respawnPoints ));
                        GameScene.add(dogPet);
                        dogPet.state = dogPet.WANDERING;
                        dogPet.sprite.emitter().burst(Speck.factory(Speck.STAR), 10);
                        hero.sprite.idle();
                        Bestiary.setSeen(dogPet.getClass());
                    }
                });
            } else {
                GLog.w(Messages.get(DogStick.class, "you_must_potion"));
            }
        } else if (action.equals(AC_Died_SummonDog)) {
            GameScene.show(new WndOptions(new ItemSprite(this),
                    Messages.titleCase( Messages.get(this, "dog2")),
                    Messages.get(this, "wnd2_body"),
                    Messages.get(this, "wnd2_set"),
                    Messages.get(this, "wnd2_return")){
                @Override
                protected void onSelect(int index) {
                    if (index == 0){
                        for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])){
                            if (mob instanceof MiniCerberus) {
                                mob.die(null);
                            }
                        }
                        fireLevel = 0;
                        Buff.affect(hero, CoolDownStoneRecharge.class, CoolDownStoneRecharge.DURATION);
                        hero.sprite.operate(hero.pos);
                        hero.busy();
                        GLog.w(Messages.get(DogStick.class, "pets_died"));
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
        return quantity * 50;
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
                if (mob instanceof MiniCerberus) {
                    needToSpawn = false;
                    break;
                }
            }
            if (needToSpawn && hero.buff(CoolDownStoneRecharge.class) == null){
                GLog.p( Messages.get(DogStick.class, "charged") );
            }
            return true;
        }

        @Override
        public int icon() {
            return BuffIndicator.TIME;
        }

        @Override
        public void tintIcon(Image icon) {
            icon.hardlight(0xDA6600);
        }

        @Override
        public float iconFadePercent() {
            return Math.max(0, (DURATION - visualcooldown()) / DURATION);
        }

        @Override
        public String toString() {
            return Messages.get(this, "name");
        }
    }

    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        fireLevel = bundle.getInt("firelevel");
    }

    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put("firelevel", fireLevel);
    }
}
