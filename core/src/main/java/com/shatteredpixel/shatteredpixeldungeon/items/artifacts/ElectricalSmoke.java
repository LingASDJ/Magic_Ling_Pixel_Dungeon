package com.shatteredpixel.shatteredpixeldungeon.items.artifacts;


import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ElectricalSmokeBlob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicImmune;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Smoking;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ElectricalSmokeParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.Bag;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.PotionBandolier;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.Potion;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfFrost;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfLevitation;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfLiquidFlame;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfLiquidFlameX;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfParalyticGas;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.brews.BlizzardBrew;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.brews.InfernalBrew;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.brews.ShockingBrew;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfCorrosiveGas;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRecharging;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndBag;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class ElectricalSmoke extends Artifact {
    {
        image = ItemSpriteSheet.RUIKE;

        levelCap = 10;

        charge = 0;
        chargeCap = 100;

    }

    public float process;
    public ElectricalSmoke artifact = this;
    public int decrease = 10;
    public Hero hero = Dungeon.hero;

    public static final String AC_CHARGE = "CHARGE";
    public static final String AC_SMOKE = "SMOKE";

    public ArrayList<Class> potions = new ArrayList<>();

    @Override
    public Emitter emitter() {
        Emitter emitter = new Emitter();
        emitter.pos(13f, 4);
        emitter.fillTarget = false;
        emitter.pour(StaffParticleFactory, 0.1f);
        return emitter;
    }

    private final Emitter.Factory StaffParticleFactory = new Emitter.Factory() {
        /**
         * @param emitter 目标来源
         * @param index 特效来源
         * @param x,y 位置
         */
        @Override
        public void emit( Emitter emitter, int index, float x, float y ) {
            ((ElectricalSmokeParticle)emitter.recycle( ElectricalSmokeParticle.class )).reset( x, y+3 );
        }
        @Override
        public boolean lightMode() {
            return true;
        }
    };

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        if (hero.buff(MagicImmune.class) != null) {
            return actions;
        }
        if (isEquipped(hero) && !cursed) {
            actions.add(AC_CHARGE);
            actions.add(AC_SMOKE);
        }
        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {
        super.execute(hero, action);

        if (hero.buff(MagicImmune.class) != null) return;

        if (action.equals(AC_CHARGE)) {

            GameScene.selectItem(itemSelector);

        } else if (action.equals(AC_SMOKE) && !cursed) {

            if (!isEquipped(hero)) GLog.i(Messages.get(Artifact.class, "need_to_equip"));
            else if (charge < 100 / (10 + level()))
                GLog.i(Messages.get(this, "low_charge"));
            else {
                if(hero.buff(Smoking.class)==null) {

                    Buff.affect(hero, Smoking.class);
                    hero.buff(Smoking.class).setArtifact(this);

                    hero.sprite.operate( hero.pos );
                    hero.busy();
                    hero.spend(Actor.TICK);

                }else{
                    hero.sprite.operate( hero.pos );
                    hero.buff(Smoking.class).detach();
                }
            }
        }
    }

    public static ArrayList<Class> gasPotion = new ArrayList<>(Arrays.asList(
            PotionOfFrost.class,
            PotionOfLiquidFlame.class,
            PotionOfToxicGas.class,
            PotionOfLevitation.class,
            PotionOfParalyticGas.class,
            PotionOfLiquidFlameX.class,
            PotionOfCorrosiveGas.class,
            BlizzardBrew.class,
            InfernalBrew.class,
            ShockingBrew.class
    ));

    public HashMap<Class<? extends Potion>, Integer> potionCate = new HashMap<>();

    {
        potionCate.put(PotionOfFrost.class, 0);
        potionCate.put(PotionOfLiquidFlame.class, 0);
        potionCate.put(PotionOfToxicGas.class, 0);
        potionCate.put(PotionOfLevitation.class, 0);
        potionCate.put(PotionOfParalyticGas.class, 0);
        potionCate.put(PotionOfLiquidFlameX.class, 0);
        potionCate.put(PotionOfCorrosiveGas.class, 0);
        potionCate.put(BlizzardBrew.class, 0);
        potionCate.put(InfernalBrew.class, 0);
        potionCate.put(ShockingBrew.class, 0);
    }

    public boolean canCharge(Item item) {
        return gasPotion.contains(item.getClass()) || (item.getClass() == ScrollOfRecharging.class && level < 10);
    }

    @Override
    protected ArtifactBuff passiveBuff() {
        return new SmokingAlloy();
    }

    public int getCharge(){
        return charge;
    }

    public void reduceCharge(int amount){
        charge -= amount;
    }

    @Override
    public Item upgrade(){
        decrease = 100/(10+level());
        if(levelCap > level) return super.upgrade();
        else return this;
    }

    protected WndBag.ItemSelector itemSelector = new WndBag.ItemSelector() {

        @Override
        public String textPrompt() {
            return Messages.get(ElectricalSmoke.class, "prompt");
        }

        @Override
        public Class<? extends Bag> preferredBag() {
            return PotionBandolier.class;
        }

        @Override
        public boolean itemSelectable(Item item) {
            return canCharge(item);
        }

        @Override
        public void onSelect(Item item) {
            Hero hero = Dungeon.hero;
            if (item != null && gasPotion.contains(item.getClass())) {
                hero.sprite.operate( hero.pos );
                add(item.getClass(), 100);

                if(item instanceof ShockingBrew) {
                    if(level<levelCap){
                        GLog.p(Messages.get(ElectricalSmoke.class, "levelup"));
                        upgrade();
                    }
                    if(level<levelCap) upgrade();
                }

                item.detach(hero.belongings.backpack);

            } else if(item != null){
                if(level<levelCap) upgrade();
                hero.sprite.operate( hero.pos );
                GLog.p(Messages.get(ElectricalSmoke.class, "levelup"));
                item.detach(hero.belongings.backpack);
            }
            hero.busy();
            hero.spend(Actor.TICK);
        }
    };

    @Override
    public boolean doEquip( final Hero hero ) {
        if(!cursed) hero.withElectricalSmoke = true;
        return super.doEquip(hero);
    }

    @Override
    public boolean doUnequip(Hero hero,boolean collect, boolean single) {
        if(!cursed && super.doUnequip(hero,collect,single)) hero.withElectricalSmoke = false;
        return super.doUnequip(hero,collect,single);
    }

    @Override
    public String desc() {
        String desc = Messages.get(this, "desc");
        if ( isEquipped ( Dungeon.hero ) ) {
            desc += "\n\n";

            if (!cursed) {
                desc += Messages.get(this, "desc_normal");
            } else {
                desc += Messages.get(this, "desc_cursed");
            }

        }

        if (cursed && !isEquipped(Dungeon.hero) && isIdentified()){
            desc += "\n\n" + Messages.get(this, "desc_noequip_cursed");
        }else if(!isEquipped(Dungeon.hero) && isIdentified()){
            desc += "\n\n" + Messages.get(this, "desc_noequip_normal");
        }

        if( !isIdentified()){
            desc += "\n\n" + Messages.get(this, "desc_noidentify");
        }

        if(has(PotionOfFrost.class) || has(PotionOfLiquidFlame.class) ||has(PotionOfToxicGas.class) || has(PotionOfLevitation.class)
                || has(PotionOfParalyticGas.class) || has(PotionOfLiquidFlameX.class) || has(PotionOfCorrosiveGas.class)
                || has(BlizzardBrew.class) || has(InfernalBrew.class) || has(ShockingBrew.class)) {
            desc += "\n\n" + Messages.get(this,"withpotiona");
            if (has(PotionOfFrost.class)) {
                desc += "\n\n" + new PotionOfFrost().name();
            }

            if (has(PotionOfLiquidFlame.class)) {
                desc += "\n\n" +new PotionOfLiquidFlame().name();
            }

            if (has(PotionOfToxicGas.class)) {
                desc += "\n\n" +new PotionOfToxicGas().name();
            }

            if (has(PotionOfLevitation.class)) {
                desc += "\n\n" +new PotionOfLevitation().name();
            }

            if (has(PotionOfParalyticGas.class)) {
                desc += "\n\n" +new PotionOfParalyticGas().name();
            }

            if (has(PotionOfLiquidFlameX.class)) {
                desc += "\n\n" +new PotionOfLiquidFlameX().name();
            }

            if (has(PotionOfCorrosiveGas.class)) {
                desc += "\n\n" +new PotionOfCorrosiveGas().name();
            }

            if (has(BlizzardBrew.class)) {
                desc += "\n\n" +new BlizzardBrew().name();
            }

            if (has(InfernalBrew.class)) {
                desc += "\n\n" +new InfernalBrew().name();
            }

            if (has(ShockingBrew.class)) {
                desc += "\n\n" +new ShockingBrew().name();
            }
        }

        return desc;
    }

    public boolean has(Class c){
        return potionCate.get(c) > 0;
    }

    public class SmokingAlloy extends ArtifactBuff{

        public ElectricalSmoke smoke = artifact;

        @Override
        public boolean act() {
            if(isCursed()){
                if(Random.Float()<0.1f){
                    GLog.n(Messages.get(ElectricalSmoke.class,"cursedEffect"));
                    Buff.affect(Dungeon.hero, Blindness.class,2);
                    GameScene.add(Blob.seed(Dungeon.hero.pos, 50, ToxicGas.class));
                }
            }
            process += 0.1f+(0.01f*level());
            if(process>=1){
                process--;
                charge++;
                if(charge>chargeCap)charge = chargeCap;
            }

            smoke = artifact;

            spend(TICK);
            return true;
        }

        public HashMap<Class<? extends Potion>, Integer> getMap(){
            return potionCate;
        }

    }

    private static final String FROST = "frost";
    private static final String FLAME = "flame";
    private static final String TOXIC = "toxic";
    private static final String LEVITATION = "levitation";
    private static final String PARALYTIC = "paralytic";
    private static final String FLAMEX = "flamex";
    private static final String CORROSIVE = "corrosive";
    private static final String BLIZZARD = "blizzard";
    private static final String INFERNAL = "infernal";
    private static final String SHOCKING = "shocking";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle(bundle);

        bundle.put(FROST,potionCate.get(PotionOfFrost.class));
        bundle.put(FLAME,potionCate.get(PotionOfLiquidFlame.class));
        bundle.put(TOXIC,potionCate.get(PotionOfToxicGas.class));
        bundle.put(LEVITATION,potionCate.get(PotionOfLevitation.class));
        bundle.put(PARALYTIC,potionCate.get(PotionOfParalyticGas.class));
        bundle.put(FLAMEX,potionCate.get(PotionOfLiquidFlameX.class));
        bundle.put(CORROSIVE,potionCate.get(PotionOfCorrosiveGas.class));
        bundle.put(BLIZZARD,potionCate.get(BlizzardBrew.class));
        bundle.put(INFERNAL,potionCate.get(InfernalBrew.class));
        bundle.put(SHOCKING,potionCate.get(ShockingBrew.class));

    }

    @Override
    public void restoreFromBundle( Bundle bundle){
        super.restoreFromBundle(bundle);

        add(PotionOfFrost.class,bundle.getInt(FROST));
        add(PotionOfLiquidFlame.class,bundle.getInt(FLAME));
        add(PotionOfToxicGas.class,bundle.getInt(TOXIC));
        add(PotionOfLevitation.class,bundle.getInt(LEVITATION));
        add(PotionOfParalyticGas.class,bundle.getInt(PARALYTIC));
        add(PotionOfLiquidFlameX.class,bundle.getInt(FLAMEX));
        add(PotionOfCorrosiveGas.class,bundle.getInt(CORROSIVE));
        add(BlizzardBrew.class,bundle.getInt(BLIZZARD));
        add(InfernalBrew.class,bundle.getInt(INFERNAL));
        add(ShockingBrew.class,bundle.getInt(SHOCKING));

    }

    public void add(Class c,int i){
        int old = potionCate.get(c);
        potionCate.remove(c);
        potionCate.put(c,old+i);
    }
}
