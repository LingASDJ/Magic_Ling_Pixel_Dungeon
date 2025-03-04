package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.extra;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;
import static com.shatteredpixel.shatteredpixeldungeon.items.Generator.randomUsingDefaults;

import com.shatteredpixel.shatteredpixeldungeon.Conducts;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.PaswordBadges;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Adrenaline;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.GoodLuck;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Haste;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Killer;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NTNPC;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.SmallLeafHardDungeon;
import com.shatteredpixel.shatteredpixeldungeon.effects.Flare;
import com.shatteredpixel.shatteredpixeldungeon.effects.FloatingText;
import com.shatteredpixel.shatteredpixeldungeon.items.EnergyCrystal;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.items.IceCyanBlueSquareCoin;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.LeatherArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.MailArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.PlateArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.ScaleArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.ElectricalSmoke;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Food;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Pasty;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.Potion;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfExperience;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.ExoticPotion;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfDivineInspiration;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.SmallLightHeader;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.Ring;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.SelectableRing;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.Scroll;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfIdentify;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRemoveCurse;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTransmutation;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ExoticScroll;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.SelectableWand;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfSun;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.PeachGodStateSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.watabou.noosa.Game;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

public class PeachGodState extends NTNPC {
    {
        spriteClass = PeachGodStateSprite.class;
    }

    public int anocount = 0;
    public int count = 0;
    private boolean seenBefore = false;
    @Override
    protected boolean act() {

        if (!seenBefore && Dungeon.level.heroFOV[pos] && Dungeon.level.distance(pos, hero.pos) <= 3) {
            seenBefore = true;
            GLog.p(Messages.get(this,"hello"));
        }  else if(seenBefore && !Dungeon.level.heroFOV[pos]) {
            seenBefore = false;
        }

        return super.act();
    }

    @Override
    public boolean interact(Char c) {

        sprite.turnTo(pos, hero.pos);

        Game.runOnRenderThread(new Callback() {
            @Override
            public void call() {
                GameScene.show(new WndOptions(new PeachGodStateSprite(),
                        Messages.get(PeachGodState.class, "title"),
                        Messages.get(PeachGodState.class, "lucky_desc",anocount),
                        Messages.get(PeachGodState.class, "single"),
                        Messages.get(PeachGodState.class, "ten_shots"),
                        Messages.get(PeachGodState.class, "cancel")) {
                    @Override
                    protected void onSelect(int index) {
                        super.onSelect(index);
                        if (index == 0) {
                            Game.runOnRenderThread(() -> {

                                if(!Dungeon.isDLC(Conducts.Conduct.DEV) && anocount>=50){
                                    GLog.i(Messages.get(PeachGodState.class,"touchTop"));
                                    hide();
                                    return;
                                }

                                if(Dungeon.isDLC(Conducts.Conduct.DEV) || ( c instanceof Hero && SPDSettings.iceCoin() >50 )) {
                                    pray(0);
                                }else{
                                    GLog.i(Messages.get(PeachGodState.class,"notenough"));
                                    hide();
                                }
                            });
                        } else if (index == 1) {
                            Game.runOnRenderThread(() -> {

                                if(!Dungeon.isDLC(Conducts.Conduct.DEV) && anocount>=50){
                                    GLog.i(Messages.get(PeachGodState.class,"touchTop"));
                                    hide();
                                    return;
                                }

                                if(Dungeon.isDLC(Conducts.Conduct.DEV) || ( c instanceof Hero && SPDSettings.iceCoin() >500 )) {
                                    pray(1);
                                }else{
                                    GLog.i(Messages.get(PeachGodState.class,"notenough"));
                                    hide();
                                }

                            });
                        } else if (index == 2){
                            GLog.b(Messages.get(SmallLeafHardDungeon.class,"cancelselect"));
                        }
                    }
                });
            }
        });
        return true;
    }

    public void pray(int mode){

        String reward = Messages.get(PeachGodState.class,"you_now_have");

        if(mode == 0){

            //单抽
            int p = randomPray();
            count(p);
            Item m = reward(p);
            reward += m.name();

            if(!m.doPickUp(hero,hero.pos)){
                m.doDrop(hero);
            }


        } else if (mode == 1) {
            //十连抽
            int[] p = {randomPray(),randomPray(),randomPray(),randomPray(),randomPray(),randomPray(),randomPray(),randomPray(),randomPray(),randomPray()};
            for(int i : p){

                if(anocount>=50 && !Dungeon.isDLC(Conducts.Conduct.DEV)){
                    GLog.i(Messages.get(PeachGodState.class,"surplus"));
                    return;
                }

                count(i);

                Item m = reward(i);
                reward += m.name() + "  ";

                if(!m.doPickUp(hero,hero.pos)){
                    m.doDrop(hero);
                }
            }
        }

        GLog.i(reward);
    }

    public int randomPray(){

        float f = Random.Int(1,101);

        int type = 0;

        if(count >= 10 ) type = 1;

        if(SPDSettings.prayCount() >= 41){
            type = 2;
        }

        if(type == 0) {
            //普通
            if (f <= 40) {
                return 0;
            } else if (f <= 75) {
                return 1;
            } else if (f <= 95) {
                return 2;
            } else if (f <= 99) {
                return 3;
            } else {
                return 4;
            }
        } else if (type == 1) {
            //小保底
            if (f <= 70) {
                return 2;
            } else if (f <= 98) {
                return 3;
            } else{
                return 4;
            }
        } else if (type == 2) {
            //大保底
            if (f <= 95 ){
                return 3;
            }else {
                return 4;
            }
        }
        return 0;
    }

    public void count(int rare){

        switch (rare){
            case 3:
                sprite.showStatusWithIcon(0xFFFF00, Messages.get(this,"gold"), FloatingText.GOLD);
                new Flare( 12, 70 ).color(0xFFFF00, true).show( sprite, 5f );
                PaswordBadges.LEGEND();
            break;
            case 4:
                sprite.showStatusWithIcon(Window.Pink_COLOR, Messages.get(this,"lends"), FloatingText.GOLD);
                new Flare( 16, 70 ).color(Window.Pink_COLOR, true).show( sprite, 5f );
                PaswordBadges.LEGEND();
                PaswordBadges.MYTHOLOGY();
            break;
        }

        anocount++;

        if(!Dungeon.isDLC(Conducts.Conduct.DEV)) SPDSettings.iceCoin(-50);

        if(rare<=1) {
            count ++;
        } else {
            count =0;
        }

        if(rare<=2 && !Dungeon.isDLC(Conducts.Conduct.DEV)) {
            SPDSettings.addPrayCount();
        } else {
            SPDSettings.resetPrayCount();
        }

        if(anocount>2){
            PeachGodStateSprite s = (PeachGodStateSprite) sprite;
            if(anocount >8){
                s.idle4();
            } else if (anocount>5) {
                s.idle3();
            }else{
                s.idle2();
            }
        }

    }

    public Item[] reward0 = {
            new Gold().quantity(555),
            Generator.random(Generator.Category.WEP_T2),
            Generator.random(Generator.Category.MIS_T2).quantity(Random.Int(1,5)),
            new LeatherArmor().identify(false),
            randomUsingDefaults( Generator.Category.POTION ),
            randomUsingDefaults( Generator.Category.SCROLL ),
            new EnergyCrystal().quantity(5),
            new IceCyanBlueSquareCoin().quantity(10)
    };

    public Item[] reward1 = {
            new Gold().quantity(666),
            Generator.random(Generator.Category.WEP_T3),
            Generator.random(Generator.Category.MIS_T3).quantity(Random.Int(1,5)),
            new MailArmor().identify(false),
            randomUsingDefaults( Generator.Category.POTION ).quantity(2),
            randomUsingDefaults( Generator.Category.POTION ),
            randomUsingDefaults( Generator.Category.SCROLL ).quantity(2),
            randomUsingDefaults( Generator.Category.SCROLL ),
            new EnergyCrystal().quantity(10),
            new IceCyanBlueSquareCoin().quantity(20),
            new Pasty(),
    };

    public Item[] reward2 = {
            new Gold().quantity(1145),
            Generator.random(Generator.Category.WEP_T4),
            Generator.random(Generator.Category.MIS_T4).quantity(Random.Int(1,5)),
            new ScaleArmor().identify(false),
            new PotionOfExperience(),
            new ScrollOfTransmutation(),
            new EnergyCrystal().quantity(20),
            new ScrollOfIdentify().quantity(8),
            new ScrollOfRemoveCurse().quantity(8),
            new Food().quantity(3),
            Generator.random(Generator.Category.WAND),
            Generator.random(Generator.Category.RING),
            Generator.random(Generator.Category.POTION).quantity(2),
            Generator.random(Generator.Category.SCROLL).quantity(2),
    };

    public Item[] reward3 = {
            Generator.random(Generator.Category.WEP_T5),
            Generator.random(Generator.Category.MIS_T5).quantity(Random.Int(2,8)),
            new PlateArmor().identify(false),
            new PotionOfDivineInspiration().quantity(2),
            new EnergyCrystal().quantity(20),
            new ElectricalSmoke(),
            new WandOfSun(),
            new SmallLightHeader(),
            new IceCyanBlueSquareCoin().quantity(500),
            Generator.randomArtifact(),
            Generator.random(Generator.Category.WAND),
            Generator.random(Generator.Category.RING)
    };

    public Item[] reward4 = {
            new ScrollOfUpgrade().quantity(2),
            new SelectableWand(),
            new SelectableRing(),
            new IceCyanBlueSquareCoin().quantity(2500)
    };

    public Item reward(int rare){

        Item m = new Food();

        switch (rare){
            case 1:

                if(Random.Int(1,13)>11){
                    Buff.affect(hero, Adrenaline.class,30);
                }else{
                    m = reward1[Random.Int(1,11)];
                    if(m instanceof Armor || m instanceof Weapon){
                        randomLevel(m,1,4);
                        if(Random.Int(1,100)<=35){
                            if(m instanceof Armor){
                                ((Armor) m).inscribe();
                            }
                            if(m instanceof Weapon){
                                ((Weapon) m).enchant();
                            }
                        }
                    }

                    if(m instanceof Potion && m.quantity ==1){
                        if (ExoticPotion.regToExo.containsKey(m.getClass())){
                            m = Reflection.newInstance(ExoticPotion.regToExo.get(m.getClass()));
                        }
                    }

                    if(m instanceof Scroll && m.quantity ==1){
                        if (ExoticScroll.regToExo.containsKey(m.getClass())){
                            m = Reflection.newInstance(ExoticScroll.regToExo.get(m.getClass()));
                        }
                    }

                }
                break;
            case 2:
                if(Random.Int(1,16)>14){
                    Buff.affect(hero, GoodLuck.class);
                }else{
                    m = reward2[Random.Int(1,14)];
                    if(m instanceof Armor || m instanceof Weapon){
                        randomLevel(m,2,4);

                        if(Random.Int(1,100)<=75){
                            if(m instanceof Armor){
                                ((Armor) m).inscribe();
                            }
                            if(m instanceof Weapon){
                                ((Weapon) m).enchant();
                            }
                        }
                    }

                    if(m instanceof Wand){
                        randomLevel(m,1,4);
                    }

                    if(m instanceof Ring){
                        randomLevel(m,0,3);
                    }

                    if(m instanceof Potion && m.quantity ==2){
                        if (ExoticPotion.regToExo.containsKey(m.getClass())){
                            m = Reflection.newInstance(ExoticPotion.regToExo.get(m.getClass()));
                        }
                    }

                    if(m instanceof Scroll && m.quantity ==2){
                        if (ExoticScroll.regToExo.containsKey(m.getClass())){
                            m = Reflection.newInstance(ExoticScroll.regToExo.get(m.getClass()));
                        }
                    }

                }
                break;
            case 3:
                if(Random.Int(1,14)>12){
                    Buff.affect(hero, Killer.class);
                }else{
                    m = reward3[Random.Int(1,12)];
                    if(m instanceof Armor || m instanceof Weapon){
                        randomLevel(m,2,4);
                        if(m instanceof Armor){
                            ((Armor) m).inscribe();
                        }
                        if(m instanceof Weapon){
                            ((Weapon) m).enchant();
                        }
                    }

                    if(m instanceof Wand){
                        randomLevel(m,3,5);
                    }

                    if(m instanceof Ring){
                        randomLevel(m,1,4);
                    }

                }
                break;
            case 4:
                m  = reward4[Random.Int(1,4)];
                break;
            case 0:
            default:

                if(Random.Int(1,11)>8){
                    if(Random.Int(1,2)>1){
                        Buff.affect(hero, Haste.class,30);
                    }else{
                        Buff.affect(hero, Invisibility.class,30);
                    }
                }else{
                    m = reward0[Random.Int(1,8)];
                    if(m instanceof Armor || m instanceof Weapon){
                        randomLevel(m,1,3);
                    }
                }
                break;
        }
        return m;
    }

    public void randomLevel(Item item ,int min,int max){
        int chance = Random.Int(1,100);
        switch (max-min+1){
            case 3:
                if(chance<=50){
                    item.level(min);
                } else if (chance <= 80) {
                    item.level(min+1);
                } else {
                    item.level(max);
                }
                break;
            case 4:
                if(chance<=50){
                    item.level(min);
                } else if (chance <= 80) {
                    item.level(min+1);
                } else if (chance <= 95) {
                    item.level(max-1);
                } else {
                    item.level(max);
                }
                break;
        }
    }

    private final String COUNT = "count";
    private final String ANOCOUNT = "anocount";


    @Override
    public void storeInBundle(Bundle bundle) {

        bundle.put(COUNT,count);
        bundle.put(ANOCOUNT,anocount);

        super.storeInBundle(bundle);
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {

        count = bundle.getInt(COUNT);
        anocount = bundle.getInt(ANOCOUNT);

        super.restoreFromBundle(bundle);
    }

    @Override
    public CharSprite sprite() {

        PeachGodStateSprite sprite = (PeachGodStateSprite) super.sprite();

        if(anocount>2){
            if(anocount >8){
                sprite.idle4();
            } else if (anocount>5) {
                sprite.idle3();
            }else{
                sprite.idle2();
            }
        }

        return sprite;
    }

}
