package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.extra;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.PaswordBadges;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bleeding;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ChampionHero;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.FiveYearsNPC;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.plot.fiveyears.FiveYearsFourPlot;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Flare;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ShaftParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.OldSunShadow;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.Bag;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.HerbBag;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Food;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfCleansing;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.BloodRedFlower;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.YuanTaStoneScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.KusumiSprites;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndBag;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.watabou.noosa.Game;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class KuzumiNewYears extends FiveYearsNPC {

    public boolean flower = false;
    public boolean getFlower = false;

    public boolean angry = false;

    public boolean td = true;

    {
        spriteClass = KusumiSprites.class;
        plot1 = new FiveYearsFourPlot.KuzumiFiveYearsPlot();
        plot2 = new FiveYearsFourPlot.KuzumiFiveYearsBPlot();
        properties.add(Property.UNKNOWN);
    }

    @Override
    public boolean interact(Char c) {
        sprite.turnTo( pos, c.pos );
        if(flower && rd) {
            NeedFlowers();
            rd = false;
        } else if(angry && rd) {
            int dmg = 20;
            GLog.n(Messages.get(KuzumiNewYears.class,"anary"));
            Buff.affect(hero, Bleeding.class).set(5f);
            hero.damage(dmg,this,DamageType.REAL);
            if (!hero.isAlive()) {
                Dungeon.fail( getClass() );
                GLog.n( Messages.capitalize(Messages.get(KuzumiNewYears.class, "kill", name())) );
            }
        } else if(first){
            Game.runOnRenderThread(() -> GameScene.show(new WndDialog(plot1,false)));
            first = false;
        } else if(secnod){
            NeedFood();
        } else {
            Game.runOnRenderThread(() -> GameScene.show(new WndDialog(plot2,false)));
        }
        return true;
    }

    private void NeedFood(){
        Game.runOnRenderThread(new Callback() {
            @Override
            public void call() {
                GameScene.show(new WndOptions(
                                       sprite(),
                                       Messages.titleCase(name()),
                                       Messages.get(KuzumiNewYears.class, "messages3"),
                                       Messages.get(KuzumiNewYears.class, "yes"),
                                       Messages.get(KuzumiNewYears.class, "no")
                               ) {
                                   @Override
                                   protected void onSelect(int index) {
                                       if (index==0){
                                           GameScene.selectItem(FooditemSelector);
                                       }
                                   }
                               }
                );
            }});
    }

    private void NeedFlowers(){
        Game.runOnRenderThread(new Callback() {
            @Override
            public void call() {
                GameScene.show(new WndOptions(
                                       sprite(),
                                       Messages.titleCase(name()),
                                       Messages.get(KuzumiNewYears.class, "messages4"),
                                       Messages.get(KuzumiNewYears.class, "yes"),
                                       Messages.get(KuzumiNewYears.class, "no")
                               ) {
                                   @Override
                                   protected void onSelect(int index) {
                                       if (index==0){
                                           GameScene.flash(Window.GDX_COLOR);
                                           hero.HP = 1;
                                           BloodRedFlower bf = new BloodRedFlower();
                                           bf.Charge = hero.HT/2;
                                           Dungeon.level.drop(bf, hero.pos).sprite.drop();
                                           PaswordBadges.HIRO();
                                       }
                                   }
                               }
                );
            }});
    }

    public static void Bless(){
        PotionOfHealing.cure( hero );
        hero.belongings.uncurseEquipped();
        hero.buff( Hunger.class ).satisfy( Hunger.STARVING );

        hero.HP = hero.HT;
        hero.sprite.emitter().start( Speck.factory( Speck.HEALING ), 0.4f, 4 );

        CellEmitter.get( hero.pos ).start( ShaftParticle.FACTORY, 0.2f, 3 );

        Dungeon.hero.interrupt();
        new Flare( 6, 32 ).show( hero.sprite, 2f );
    }

    private static final String KRS = "krs";
    private static final String KTS = "kts";
    private static final String ARE = "are5";
    private static final String TDG = "tdg";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(KRS,flower);
        bundle.put(KTS,getFlower);
        bundle.put(ARE,angry);
        bundle.put(TDG,td);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        flower = bundle.getBoolean(KRS);
        getFlower = bundle.getBoolean(KTS);
        angry = bundle.getBoolean(ARE);
        td = bundle.getBoolean(TDG);
    }

    protected WndBag.ItemSelector FooditemSelector = new WndBag.ItemSelector() {

        @Override
        public String textPrompt() {
            return Messages.get(KuzumiNewYears.class, "inv_title");
        }

        @Override
        public Class<? extends Bag> preferredBag() {
            return HerbBag.class;
        }

        @Override
        public boolean itemSelectable(Item item) {
            return item instanceof Food;
        }

        @Override
        public void onSelect(Item item) {
            Food w = (Food) item;
            if(w != null){
                if(w.energy == 450){
                    Bless();
                    w.detach(hero.belongings.backpack);
                } else if(w.energy <= 300){
                    PotionOfCleansing.cleanse(hero);
                    new Flare( 6, 32 ).show( hero.sprite, 2f );
                    w.detach(hero.belongings.backpack);
                } else if(w.energy > 450) {
                    Bless();
                    switch (Random.Int(7)){
                        case 0:
                            Buff.affect(hero,ChampionHero.AntiMagic.class,300f);
                            break;
                        case 1:
                            Buff.affect(hero,ChampionHero.Giant.class,300f);
                        break;
                        case 2:
                            Buff.affect(hero,ChampionHero.Growing.class,300f);
                        break;
                        case 3:
                            Buff.affect(hero,ChampionHero.Halo.class,300f);
                        break;
                        case 4:
                            Buff.affect(hero,ChampionHero.Blazing.class,300f);
                        break;
                        case 5:
                            Buff.affect(hero,ChampionHero.Projecting.class,300f);
                        break;
                        case 6:
                            Buff.affect(hero,ChampionHero.Blessed.class,300f);
                        break;
                    }
                    w.detach(hero.belongings.backpack);
                }

                if(hero.heroClass == HeroClass.MAGE){
                    Dungeon.level.drop(new OldSunShadow(), hero.pos).sprite.drop();
                }
                yell(Messages.get(KuzumiNewYears.class,"thanks"));
                secnod = false;

                YuanTaStoneScene.YuanTaStoryManager.unlockStory(
                        "story_icetown",
                        Messages.get("icetown_title"),
                        Messages.get("icetown_content"),
                        ItemSpriteSheet.ICEBOOK
                );
            } else {
                angry = true;
                int dmg = 20;

                GLog.p(Messages.get(KuzumiNewYears.class,"empty_1"));
                GLog.i(Messages.get(KuzumiNewYears.class,"empty_2"));
                GLog.n(Messages.get(KuzumiNewYears.class,"empty_3"));
                Buff.affect(hero, Bleeding.class).set(5f);
                hero.damage(dmg,this,DamageType.REAL);
                if (!hero.isAlive()) {
                    Dungeon.fail( getClass() );
                    GLog.n( Messages.capitalize(Messages.get(KuzumiNewYears.class, "kill", name())) );
                }
            }

        }
    };
}
