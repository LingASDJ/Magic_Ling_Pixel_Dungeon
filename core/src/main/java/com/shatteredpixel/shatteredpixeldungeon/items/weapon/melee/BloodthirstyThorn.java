package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
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

        /**
         * cost 炼金花费
         * @param ingredients
         * @return
         */
        @Override
        public int cost(ArrayList<Item> ingredients) {
            return 18;
        }
        /**
         * @param bloodthirstyThorn 读取嗜血荆棘的对象
         * @param chaliceOfBlood 读取自己背包中的圣杯
         */


        /**
         * @param message 消息弹框控件
         */
        public void message(String message) {
            Game.runOnRenderThread(() -> ShatteredPixelDungeon.scene().add(new WndMessage(message)));
        }

        /*
         * @param ingredients 数组遍历物品
         * @param Method brew 输出最终的炼金结果物品
         */
        @Override
        public Item brew(ArrayList<Item> ingredients) {
            ChaliceOfBlood chaliceOfBlood = Dungeon.hero.belongings.getItem(ChaliceOfBlood.class);
            BloodthirstyThorn bloodthirstyThorn = new BloodthirstyThorn();

            if (!testIngredients(ingredients)) return null;

            for (Item ingredient : ingredients){
                ingredient.quantity(ingredient.quantity() - 1);
            }

            //返回圣杯一致的等级
            if(chaliceOfBlood!=null){
                bloodthirstyThorn.level=chaliceOfBlood.level();
            } else {
                //如果圣杯物品为空，则读取本局全局玩家献祭圣杯的次数
                bloodthirstyThorn.level=Statistics.ChaicBlood;
            }

            if(chaliceOfBlood!=null) {
                bloodthirstyThorn.quantity(1).identify();

                chaliceOfBlood.detach(hero.belongings.backpack);

                if (chaliceOfBlood.level() == 10) {
                    ChaliceOfBlood chaliceOfBlood1 = new ChaliceOfBlood();
                    chaliceOfBlood1.cursed = true;
                    chaliceOfBlood1.level = Random.NormalIntRange(1, 3);
                    chaliceOfBlood1.identify().quantity(1).collect();
                }
            }

            return sampleOutput(null);
        }

        /**
         * @param ingredients 输出炼金结果物品前的预览方法
         */
        @Override
        public Item sampleOutput(ArrayList<Item> ingredients) {
            ChaliceOfBlood chaliceOfBlood = Dungeon.hero.belongings.getItem(ChaliceOfBlood.class);
            BloodthirstyThorn bloodthirstyThorn = new BloodthirstyThorn();

            bloodthirstyThorn.identify();

            if(chaliceOfBlood!=null){
                bloodthirstyThorn.level=chaliceOfBlood.level();
            } else {
                bloodthirstyThorn.level= Statistics.ChaicBlood;
            }

            return bloodthirstyThorn;
        }
    }

    @Override
    public int min(int lvl) {
        if (lvl > 10) {
            return 0;
        }
        return 2 + lvl * 2;
    }

    @Override
    public int max(int lvl) {
        if (lvl > 10) {
            return 0;
        }
        return 6 + lvl * 4;
    }


    @Override
    public int STRReq(int lvl) {
        if (lvl > 10) {
            return lvl * 5;
        }
        return 14;
    }

    @Override
    public int proc(Char attacker, Char defender, int damage ) {

        if (level() >= 10) {
            int healAmt = Math.min( attacker.HT, damage/5);
            healAmt = Math.min( healAmt, attacker.HT - attacker.HP );

            if (healAmt > 0 && attacker.isAlive() && Random.Float()<=0.7f) {
                attacker.HP += healAmt;
                hero.sprite.showStatus(CharSprite.POSITIVE, ("+" +Math.min( attacker.HT, damage/5) + "HP"));
            }

        }

        //恐惧和流血
        if(level()>=0 && Random.Float()>0.45f){
            Buff.affect(defender, Bleeding.class).set(level());
            Buff.affect(defender, Terror.class, level() );
        }
        return super.proc(attacker, defender, damage);

    }

    @Override
    public int image() {
        super.image = ItemSpriteSheet.BloodDir;
        if (level() >= 5) {
            super.image = ItemSpriteSheet.BloodDied;
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
