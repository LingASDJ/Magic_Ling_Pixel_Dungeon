package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bleeding;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.RoseShiled;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ShadowParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.ChaliceOfBlood;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.MetalShard;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfLivingEarth;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Earthroot;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndMessage;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class BloodthirstyThorn extends MeleeWeapon {

    {
        image = ItemSpriteSheet.BloodDir;
        hitSound = Assets.Sounds.HIT_SLASH;
        hitSoundPitch = 0.9f;
        ACC = 1.75f;
        RCH=1;
        tier=6;

    }
    public int levelCap = 10;
    @Override
    public boolean isUpgradable() {
        return false;
    }

    public static final String AC_PRICK = "PRICK";

    @Override
    public ArrayList<String> actions( Hero hero ) {
        ArrayList<String> actions = super.actions( hero );
        if (isEquipped( hero ) && level() < levelCap && !cursed && !hero.isInvulnerable(getClass())&& hero.buff(RoseShiled.class) == null)
            actions.add(AC_PRICK);
        return actions;
    }



    @Override
    public void execute(Hero hero, String action ) {
        super.execute(hero, action);

        if (action.equals(AC_PRICK)){

            int damage = 3*(level()*level());

            if (damage > hero.HP*0.75) {

                GameScene.show(
                        new WndOptions(new ItemSprite(this),
                                Messages.titleCase(name()),
                                Messages.get(this, "prick_warn"),
                                Messages.get(this, "yes"),
                                Messages.get(this, "no")) {
                            @Override
                            protected void onSelect(int index) {
                                if (index == 0)
                                    prick(Dungeon.hero);
                            }
                        }
                );

            } else {
                prick(hero);
            }
        }
    }

    private void prick(Hero hero){
        int damage = 3*(level()*level());

        Earthroot.Armor armor = hero.buff(Earthroot.Armor.class);
        if (armor != null) {
            damage = armor.absorb(damage);
        }

        WandOfLivingEarth.RockArmor rockArmor = hero.buff(WandOfLivingEarth.RockArmor.class);
        if (rockArmor != null) {
            damage = rockArmor.absorb(damage);
        }

        damage -= hero.drRoll();

        hero.sprite.operate( hero.pos );
        hero.busy();
        hero.spend(3f);
        GLog.w( Messages.get(this, "onprick") );
        if (damage <= 0){
            damage = 1;
        } else {
            Sample.INSTANCE.play(Assets.Sounds.CURSED);
            hero.sprite.emitter().burst( ShadowParticle.CURSE, 4+(damage/10) );
        }

        hero.damage(damage, this);

        if (!hero.isAlive()) {
            Dungeon.fail( getClass() );
            GLog.n( Messages.get(this, "ondeath") );
        } else {
            upgrade();
        }
    }


    /**
     *复杂的炼金合成不能使用SimpleRecipe,
     * 需要使用Recipe进行完全覆盖写入
     */
    public static class Recipe extends com.shatteredpixel.shatteredpixeldungeon.items.Recipe {

        /**
         * @param ingredients 遍历物品
         */
        @Override
        public boolean testIngredients(ArrayList<Item> ingredients) {
            boolean chice = false;
            boolean demoncrystal = false;
            boolean dm300ts = false;

            for (Item ingredient : ingredients){
                if (ingredient.quantity() > 0) {
                    if (ingredient instanceof ChaliceOfBlood) {
                        chice = true;
                    } else if (ingredient instanceof MetalShard) {
                        demoncrystal = true;
                    } else if (ingredient instanceof LifeTreeSword) {
                        dm300ts = true;
                    }
                }
            }

            return chice && demoncrystal && dm300ts;
        }

        @Override
        public int cost(ArrayList<Item> ingredients) {
            return 18;
        }
        /**
         * @param bloodthirstyThorn 读取嗜血荆棘的对象
         * @param chaliceOfBlood 读取自己背包中的圣杯
         */
        BloodthirstyThorn bloodthirstyThorn = new BloodthirstyThorn();
        ChaliceOfBlood chaliceOfBlood = Dungeon.hero.belongings.getItem(ChaliceOfBlood.class);
        /**
         * @param message 消息弹框控件
         */
        public static void message(String message) {
            Game.runOnRenderThread(() -> ShatteredPixelDungeon.scene().add(new WndMessage(message)));
        }

        /**
         * @param ingredients 数组遍历物品
         */
        @Override
        public Item brew(ArrayList<Item> ingredients) {
            /**
             * @param Method brew 输出最终的炼金结果物品
             */
            if (!testIngredients(ingredients)) return null;

            for (Item ingredient : ingredients){
                ingredient.quantity(ingredient.quantity() - 1);
            }

            //返回圣杯一致的等级
            bloodthirstyThorn.level=chaliceOfBlood.level();

            bloodthirstyThorn.quantity(1).identify();

            chaliceOfBlood.detachAll( hero.belongings.backpack );

            if(chaliceOfBlood.level()==10){
                ChaliceOfBlood chaliceOfBlood1 = new ChaliceOfBlood();
                chaliceOfBlood1.cursed = true;
                chaliceOfBlood1.level=Random.NormalIntRange(1,3);
                chaliceOfBlood1.identify().quantity(1).collect();
                message(Messages.get(BloodthirstyThorn.class,"good"));
            }

            return sampleOutput(null);
        }

        @Override
        public Item sampleOutput(ArrayList<Item> ingredients) {
            /**
             * @param sampleOutput 输出炼金结果物品前的预览方法
             */
            bloodthirstyThorn.identify();

            bloodthirstyThorn.level=chaliceOfBlood.level();
            return bloodthirstyThorn;
        }
    }

    @Override
    public int min(int lvl) {
        return 4 +lvl*2;
    }

    @Override
    public int max(int lvl) {
        return  14 + lvl*7;
    }

    @Override
    public int STRReq(int lvl){
        return 8;
    }

    private void getHerodamageHp(Hero hero) {
        int damage = 2;
        hero.damage(damage,this);
    }

    @Override
    public int proc(Char attacker, Char defender, int damage ) {
        // 等级+3前每次攻击有概率扣除使用者2点生命值，并施加流血效果。
        // 等级+5及以后时攻击距离增加一格，并移除其负面影响
        if (level() < 4 && Random.Float() > 0.35f) {
            getHerodamageHp(hero);
            Buff.affect(attacker, Bleeding.class).set(7);
        } else if (level() >= 10) {
            //吸血为每次伤害/5 例如=50/5=10 Math.min()不超出。

            int healAmt = Math.min( attacker.HT, damage/5);
            healAmt = Math.min( healAmt, attacker.HT - attacker.HP );

            if (healAmt > 0 && attacker.isAlive()) {
                attacker.HP += healAmt;
                hero.sprite.showStatus(CharSprite.POSITIVE, ("+" +Math.min( attacker.HT, damage/5) + "HP"));
            }

        }

        //恐惧和流血
        if(level()>=0 && Random.Float()>0.45f){
            Buff.affect(defender, Bleeding.class).set(15+level());
            Buff.affect(defender, Terror.class, Terror.DURATION );
        }
        return super.proc(attacker, defender, damage);

    }

    //动态改变图标1
    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        super.image = ItemSpriteSheet.BloodDir;
        if (level() >= 5) {
            super.image = ItemSpriteSheet.BloodDied;
            //在载入存档更新图标的同时更新攻击范围
            RCH=2;
            ACC = 1.95f;
        } else {
            super.image = ItemSpriteSheet.BloodDir;
        }
    }

    //动态改变图标2
    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        super.image = ItemSpriteSheet.BloodDir;
        if (level() >= 5) {
            //在保存存档更新图标的同时更新攻击范围
            super.image = ItemSpriteSheet.BloodDied;
            RCH=2;
            ACC = 1.95f;
        } else {
            super.image = ItemSpriteSheet.BloodDir;
        }
    }

    //动态改变图标3
    @Override
    public int image() {
        super.image = ItemSpriteSheet.BloodDir;
        if (level() >= 5) {
            super.image = ItemSpriteSheet.BloodDied;
            //在更新图标的同时更新攻击范围
            RCH=2;
            ACC = 1.95f;
        } else {
            super.image = ItemSpriteSheet.BloodDir;
        }
        return image;
    }

    @Override
    public String statsInfo(){
        if (isIdentified() && level() >= 10 ) {
            return Messages.get(this, "stats_desc")+Messages.get(this, "stats_descx");
        } else if (isIdentified() && level() >= 5) {
            return Messages.get(this, "stats_desc");
        } else {
            return "";
        }
    }

}
