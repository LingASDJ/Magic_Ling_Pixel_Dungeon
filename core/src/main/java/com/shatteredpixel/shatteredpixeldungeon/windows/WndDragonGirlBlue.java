package com.shatteredpixel.shatteredpixeldungeon.windows;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Belongings;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.DragonGirlBlue;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Flare;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ShaftParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.Bag;
import com.shatteredpixel.shatteredpixeldungeon.items.books.bookslist.BrokenBooks;
import com.shatteredpixel.shatteredpixeldungeon.items.books.bookslist.DeepBloodBooks;
import com.shatteredpixel.shatteredpixeldungeon.items.books.bookslist.GrassKingBooks;
import com.shatteredpixel.shatteredpixeldungeon.items.books.bookslist.IceCityBooks;
import com.shatteredpixel.shatteredpixeldungeon.items.books.bookslist.MagicGirlBooks;
import com.shatteredpixel.shatteredpixeldungeon.items.books.bookslist.NoKingMobBooks;
import com.shatteredpixel.shatteredpixeldungeon.items.books.bookslist.YellowSunBooks;
import com.shatteredpixel.shatteredpixeldungeon.items.food.PhantomMeat;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfDivineInspiration;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.Ring;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfMetamorphosis;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class WndDragonGirlBlue extends Window {
    private static final int WIDTH_P = 120;
    private static final int WIDTH_L = 160;

    private static final int GAP  = 2;


    public WndDragonGirlBlue(DragonGirlBlue dragon, Hero hero ) {
        super();

        int width = PixelScene.landscape() ? WIDTH_L : WIDTH_P;

        IconTitle titlebar = new IconTitle();
        titlebar.icon( dragon.sprite() );
        titlebar.label( Messages.titleCase( dragon.name() ) );
        titlebar.setRect( 0, 0, width, 0 );
        add( titlebar );

        RenderedTextBlock message = PixelScene.renderTextBlock( Messages.get(this, "prompt", DragonGirlBlue.Quest.survey_research_points), 6 );
        message.maxWidth( width );
        message.setPos(0, titlebar.bottom() + GAP);
        add( message );

        ArrayList<RedButton> buttons = new ArrayList<>();

        int T1Point = 400;

        if(DragonGirlBlue.Quest.one_used_points<3) {
            RedButton books = new RedButton(Messages.get(this, "random_book", T1Point), 6) {
                @Override
                protected void onClick() {
                    Dungeon.level.drop(RandomBooks(), Dungeon.hero.pos).sprite.drop();
                    DragonGirlBlue.Quest.survey_research_points -= T1Point;
                    GLog.p(Messages.get(WndDragonGirlBlue.class, "random_book_yes"), hero.name());
                    DragonGirlBlue.Quest.one_used_points++;
                    hide();
                }
            };
            books.enable(DragonGirlBlue.Quest.one_used_points == 0 && DragonGirlBlue.Quest.survey_research_points >= 400);
            buttons.add(books);

            RedButton randomMiss = new RedButton(Messages.get(this, "random_misp", T1Point), 6) {
                @Override
                protected void onClick() {
                    MissileWeapon w4;
                    w4 = (MissileWeapon) Generator.random(Generator.Category.MISSILE);
                    w4.level(Random.Int(1, 4));
                    w4.quantity(Random.Int(1, 4));
                    w4.cursed = false;
                    w4.identify();
                    Dungeon.level.drop(w4, Dungeon.hero.pos).sprite.drop();
                    DragonGirlBlue.Quest.survey_research_points -= T1Point;
                    GLog.p(Messages.get(WndDragonGirlBlue.class, "random_misp_yes"), hero.name());
                    DragonGirlBlue.Quest.one_used_points++;
                    hide();
                }
            };

            randomMiss.enable(DragonGirlBlue.Quest.one_used_points == 1 && DragonGirlBlue.Quest.survey_research_points >= 400);
            buttons.add(randomMiss);

            RedButton randomMeat = new RedButton(Messages.get(this, "random_meat", T1Point), 6) {
                @Override
                protected void onClick() {
                    Dungeon.level.drop(new PhantomMeat().quantity(2), Dungeon.hero.pos).sprite.drop();
                    DragonGirlBlue.Quest.survey_research_points -= T1Point;
                    GLog.p(Messages.get(WndDragonGirlBlue.class, "random_meat_yes"), hero.name());
                    DragonGirlBlue.Quest.one_used_points++;
                    hide();
                }
            };

            randomMeat.enable(DragonGirlBlue.Quest.one_used_points == 2 && DragonGirlBlue.Quest.survey_research_points >= 400);
            buttons.add(randomMeat);
        }

        if(DragonGirlBlue.Quest.one_used_points == 3 && DragonGirlBlue.Quest.two_used_points<3) {

            int T2Point = 500;
            RedButton bleesed = new RedButton(Messages.get(this, "bleesed", T2Point), 6) {
                @Override
                protected void onClick() {
                    PotionOfHealing.cure( hero );
                    hero.belongings.uncurseEquipped();
                    hero.buff( Hunger.class ).satisfy( Hunger.STARVING );

                    hero.HP = hero.HT;
                    hero.sprite.emitter().start( Speck.factory( Speck.HEALING ), 0.4f, 4 );

                    CellEmitter.get( hero.pos ).start( ShaftParticle.FACTORY, 0.2f, 3 );

                    Dungeon.hero.interrupt();
                    new Flare( 6, 32 ).show( hero.sprite, 2f );
                    DragonGirlBlue.Quest.survey_research_points -= T2Point;
                    GLog.p(Messages.get(WndDragonGirlBlue.class, "bleesed_yes"), hero.name());
                    DragonGirlBlue.Quest.two_used_points++;
                    hide();
                }
            };
            bleesed.enable(DragonGirlBlue.Quest.two_used_points == 0 && DragonGirlBlue.Quest.survey_research_points >= T2Point);
            buttons.add(bleesed);

            RedButton maxTier = new RedButton(Messages.get(this, "maxtier", T2Point), 6) {
                @Override
                protected void onClick() {
                    Dungeon.level.drop(new ScrollOfMetamorphosis(), Dungeon.hero.pos).sprite.drop();
                    Dungeon.level.drop(new PotionOfDivineInspiration(), Dungeon.hero.pos).sprite.drop();
                    DragonGirlBlue.Quest.survey_research_points -= T2Point;
                    GLog.p(Messages.get(WndDragonGirlBlue.class, "maxtier_yes"), hero.name());
                    DragonGirlBlue.Quest.two_used_points++;
                    hide();
                }
            };
            maxTier.enable(DragonGirlBlue.Quest.two_used_points == 1 && DragonGirlBlue.Quest.survey_research_points >= T2Point);
            buttons.add(maxTier);

            RedButton maxring = new RedButton(Messages.get(this, "maxring", T2Point), 6) {
                @Override
                protected void onClick() {
                    Ring reward;
                    do {
                        reward = (Ring)Generator.random( Generator.Category.RING );
                    } while (reward.cursed);
                    reward.level(1);
                    reward.identify();
                    reward.cursed = true;
                    Dungeon.level.drop(reward, Dungeon.hero.pos).sprite.drop();
                    DragonGirlBlue.Quest.survey_research_points -= T2Point;
                    GLog.p(Messages.get(WndDragonGirlBlue.class, "maxring_yes"), hero.name());
                    DragonGirlBlue.Quest.two_used_points++;
                    hide();
                }
            };
            maxring.enable(DragonGirlBlue.Quest.two_used_points == 2 && DragonGirlBlue.Quest.survey_research_points >= T2Point);
            buttons.add(maxring);
        }

        if(DragonGirlBlue.Quest.one_used_points == 3 && DragonGirlBlue.Quest.two_used_points ==3) {
            int UpgradePoint = 1000;
            int OtherPoint = 400;

            RedButton pro = new RedButton(Messages.get(this, "pro", OtherPoint), 6){
                @Override
                protected void onClick() {
                    hide();
                    Statistics.questScores[2] += 1000;
                    DragonGirlBlue.Quest.survey_research_points -= OtherPoint;
                    DragonGirlBlue.Quest.three_used_points++;
                    GLog.p(Messages.get(WndDragonGirlBlue.class, "pro_yes"));
                }
            };
            pro.enable(DragonGirlBlue.Quest.three_used_points == 0 && DragonGirlBlue.Quest.survey_research_points>=OtherPoint);
            buttons.add(pro);

            RedButton gold = new RedButton(Messages.get(this, "gold", OtherPoint), 6){
                @Override
                protected void onClick() {
                    hide();
                    new Gold(800).doPickUp(Dungeon.hero, Dungeon.hero.pos);
                    DragonGirlBlue.Quest.three_used_points++;
                    DragonGirlBlue.Quest.survey_research_points -= OtherPoint;
                    GLog.p(Messages.get(WndDragonGirlBlue.class, "gold_yes"));
                }
            };
            gold.enable(DragonGirlBlue.Quest.three_used_points == 1 && DragonGirlBlue.Quest.survey_research_points>=OtherPoint);
            buttons.add(gold);

            RedButton upgrade = new RedButton(Messages.get(this, "up", UpgradePoint), 6){
                @Override
                protected void onClick() {
                    GameScene.selectItem(new UpgradeSelector());
                }
            };
            upgrade.enable( DragonGirlBlue.Quest.survey_research_points>=UpgradePoint);
            buttons.add(upgrade);

        }

        float pos = message.bottom() + 3*GAP;
        for (RedButton b : buttons){
            b.leftJustify = true;
            b.multiline = true;
            b.setSize(width, b.reqHeight());
            b.setRect(0, pos, width, b.reqHeight());
            b.enable(b.active); //so that it's visually reflected
            add(b);
            pos = b.bottom() + GAP;
        }

        resize(width, (int)pos);
    }

    private class UpgradeSelector extends WndBag.ItemSelector {

        @Override
        public String textPrompt() {
            return Messages.get(this, "prompt");
        }

        @Override
        public Class<?extends Bag> preferredBag(){
            return Belongings.Backpack.class;
        }

        @Override
        public boolean itemSelectable(Item item) {
            return item.isUpgradable();
        }

        @Override
        public void onSelect(Item item) {
            if (item != null) {
                item.upgrade(Random.Int(1,4));
                int upgradeCost = 1000;
                DragonGirlBlue.Quest.survey_research_points -= upgradeCost;
                DragonGirlBlue.Quest.three_used_points++;

                hide();
                GLog.p(Messages.get(WndDragonGirlBlue.class, "up_yes"));
                Sample.INSTANCE.play(Assets.Sounds.READ);
                ScrollOfUpgrade.upgrade( Dungeon.hero );
                Item.evoke( Dungeon.hero );

                Badges.validateItemLevelAquired( item );
            }
        }
    }

    private Item RandomBooks() {
        switch (Random.Int(8)){
            case 1: default:
                return new YellowSunBooks().quantity(Random.Int(1,3));
            case 2:
                return new BrokenBooks().quantity(Random.Int(1,3));
            case 3:
                return new IceCityBooks().quantity(Random.Int(1,3));
            case 4:
                return new NoKingMobBooks().quantity(Random.Int(1,3));
            case 5:
                return new DeepBloodBooks().quantity(Random.Int(1,3));
            case 6:
                return new MagicGirlBooks().quantity(Random.Int(1,3));
            case 7:
                return new GrassKingBooks().quantity(Random.Int(1,3));
        }
    }

}