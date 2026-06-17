package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.extra;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.PaswordBadges;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.WaterOfAwareness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Awareness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicalSight;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.FiveYearsNPC;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.plot.fiveyears.ArchettoWeightLessPlot;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.plot.fiveyears.ArchettoWeightLessTalkMask;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Flare;
import com.shatteredpixel.shatteredpixeldungeon.effects.Identification;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ShaftParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.TengusMask;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.Bag;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.ScrollHolder;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.BloodRedFlower;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.DeepRedFlower;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.TimeFlower;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.Scroll;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfFlameCursed;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfDivination;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfEnchantment;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfGolems;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfRoseShiled;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.extra.ScrollOfSoul;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfBlastWave;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.UnlessEndFlowerLevel;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ArchettoSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndBag;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;

import java.util.List;

public class ArchettoWeightLess extends FiveYearsNPC {

    public List<PaswordBadges.Badge> passwordbadges = PaswordBadges.filtered(true);


    public boolean getFlower = false;

    private static final String KTS = "kts";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(KTS,getFlower);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        getFlower = bundle.getBoolean(KTS);
    }

    {
        spriteClass = ArchettoSprite.class;
        plot1 = new ArchettoWeightLessPlot();
        plot2 = new ArchettoWeightLessPlot.TalkOne();
        plot3 = new ArchettoWeightLessTalkMask();
        plot4 = new ArchettoWeightLessPlot.TalkTwo();
        plot5 = new ArchettoWeightLessPlot.TalkCrash();
        plot6 = new ArchettoWeightLessPlot.TalkEnd();
        properties.add(Property.UNKNOWN);
        PaswordBadges.loadGlobal();
    }

    @Override
    protected boolean act() {
        Buff.affect(hero, MagicalSight.class, 10f);
        return super.act();
    }

    private void GetGift(){
        Game.runOnRenderThread(new Callback() {
            @Override
            public void call() {
                GameScene.show(new WndOptions(
                                       sprite(),
                                       Messages.titleCase(name()),
                                       Messages.get(ArchettoWeightLess.class, "gift"),
                                       Messages.get(ArchettoWeightLess.class, "yes"),
                                       Messages.get(ArchettoWeightLess.class, "no")
                               ) {
                                   @Override
                                   protected void onSelect(int index) {
                                       if (index==0){
                                           GameScene.selectItem(giftSelect);
                                       }
                                   }

                                   @Override
                                   public void onBackPressed() {

                                   }
                               }
                );
            }});
    }

    @Override
    public boolean interact(Char c) {
        sprite.turnTo(pos, c.pos);

        TengusMask tengusMask = hero.belongings.getItem(TengusMask.class);
        UnlessEndFlowerLevel.UnlessAbyss unlessAbyss = hero.buff(UnlessEndFlowerLevel.UnlessAbyss.class);

        /**@param 全局首次对话 */
        if(!SPDSettings.ArchettoSeeFirst() && first) {
            Game.runOnRenderThread(() -> GameScene.show(new WndDialog(plot1, false)));
            SPDSettings.ArchettoSeeFirst(true);
            first = false;
        /**@param 常规对话 */
        } else if(first){
            Game.runOnRenderThread(() -> GameScene.show(new WndDialog(plot2, false)));
            first = false;
        /**@param 法师对话 */
        } else if(secnod && tengusMask != null && hero.heroClass == HeroClass.MAGE) {
            Game.runOnRenderThread(() -> GameScene.show(new WndDialog(plot3, false)));
            secnod = false;
        /**@param 未崩坏前礼物交易对话 */
        } else if(passwordbadges.contains(PaswordBadges.Badge.TIME_CIRCLE) && rd && unlessAbyss != null && unlessAbyss.Time < 300){
            GetGift();
        /**@param 未崩坏前与崩坏后循环对话 */
        } else  {
            if(unlessAbyss != null && unlessAbyss.Time > 500 && sd) {
                Game.runOnRenderThread(() -> GameScene.show(new WndDialog(plot5, false)));
                sd = false;
            } else if((unlessAbyss != null && unlessAbyss.Time > 500)) {
                Game.runOnRenderThread(() -> GameScene.show(new WndDialog(plot6, false)));
            } else {
                TimeFlower timeFlower = hero.belongings.getItem(TimeFlower.class);
                DeepRedFlower deepRedFlower =  hero.belongings.getItem(DeepRedFlower.class);
                BloodRedFlower bloodRedFlower = hero.belongings.getItem(BloodRedFlower.class);
                if(timeFlower != null && deepRedFlower != null && bloodRedFlower != null){
                    PaswordBadges.HIRO();
                }
                Game.runOnRenderThread(() -> GameScene.show(new WndDialog(plot4, false)));
            }
        }

        return true;
    }

    protected WndBag.ItemSelector giftSelect = new WndBag.ItemSelector() {

        @Override
        public String textPrompt() {
            return Messages.get(ArchettoWeightLess.class, "inv_title");
        }

        @Override
        public Class<? extends Bag> preferredBag() {
            return ScrollHolder.class;
        }

        @Override
        public boolean itemSelectable(Item item) {
            return item instanceof Scroll;
        }

        @Override
        public void onSelect(Item item) {
            if(item instanceof ScrollOfRoseShiled || item instanceof ScrollOfFlameCursed || item instanceof ScrollOfGolems){
                PotionOfHealing.cure( hero );
                hero.belongings.uncurseEquipped();
                hero.buff( Hunger.class ).satisfy( Hunger.STARVING );
                hero.HP = hero.HT;
                hero.sprite.emitter().start( Speck.factory( Speck.HEALING ), 0.4f, 4 );
                CellEmitter.get( hero.pos ).start( ShaftParticle.FACTORY, 0.2f, 3 );
                Dungeon.hero.interrupt();
                new Flare( 6, 32 ).show( hero.sprite, 2f );
                yell(Messages.get(ArchettoWeightLess.class,"heal"));
                rd = false;
            } else if(item instanceof Scroll && !(item instanceof ScrollOfUpgrade) && !(item instanceof ScrollOfEnchantment)) {
                Sample.INSTANCE.play( Assets.Sounds.DRINK );
                hero.sprite.emitter().parent.add( new Identification( hero.sprite.center() ) );
                hero.belongings.observe();
                for (int i=0; i < Dungeon.level.length(); i++) {

                    int terr = Dungeon.level.map[i];
                    if ((Terrain.flags[terr] & Terrain.SECRET) != 0) {

                        Dungeon.level.discover( i );

                        if (Dungeon.level.heroFOV[i]) {
                            GameScene.discoverTile( i, terr );
                        }
                    }
                }
                Buff.affect( hero, Awareness.class, Awareness.DURATION );
                Dungeon.observe();
                hero.interrupt();
                GLog.p( Messages.get(WaterOfAwareness.class, "procced") );

                Dungeon.level.drop(new ScrollOfDivination(),hero.pos).sprite.drop();

                yell(Messages.get(ArchettoWeightLess.class,"scroll"));
                rd = false;
            } else if(item instanceof ScrollOfUpgrade || item instanceof ScrollOfEnchantment){
                Game.runOnRenderThread(new Callback() {
                    @Override
                    public void call() {
                        GameScene.show(new WndOptions(
                                               sprite(),
                                               Messages.titleCase(name()),
                                               Messages.get(ArchettoWeightLess.class, "gift_sc"),
                                               Messages.get(ArchettoWeightLess.class, "yesa"),
                                               Messages.get(ArchettoWeightLess.class, "not"),
                                               Messages.get(ArchettoWeightLess.class, "nob")
                                       ) {
                                           @Override
                                           protected void onSelect(int index) {
                                               if (index==0){
                                                   Dungeon.level.drop(new ScrollOfSoul(),hero.pos);
                                                   yell(Messages.get(ArchettoWeightLess.class,"props"));
                                                   item.detach(hero.belongings.backpack);
                                                   rd = false;
                                               } else if(index == 1){
                                                   yell("………………");
                                                   int oppositeAdjacent = hero.pos + (hero.pos - pos);
                                                   Ballistica trajectory = new Ballistica( hero.pos, oppositeAdjacent, Ballistica.MAGIC_BOLT);
                                                   WandOfBlastWave.throwChar(hero, trajectory, 10000, true, false, this);
                                                   hero.spend( Actor.TICK );
                                                   hero.busy();
                                                   hero.sprite.operate( hero.pos );
                                                   Buff.affect(hero,Paralysis.class,100f);
                                                   rd = false;
                                               } else if(index == 2){
                                                   hide();
                                                   rd = false;
                                               }
                                           }
                                       }
                        );
                    }});
            }

        }
    };

}
