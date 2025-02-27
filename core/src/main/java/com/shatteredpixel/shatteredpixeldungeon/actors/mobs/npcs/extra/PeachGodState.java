package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.extra;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;
import static com.shatteredpixel.shatteredpixeldungeon.items.Generator.randomUsingDefaults;
import static com.shatteredpixel.shatteredpixeldungeon.items.Generator.wepTiers;

import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NTNPC;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.SmallLeafHardDungeon;
import com.shatteredpixel.shatteredpixeldungeon.items.EnergyCrystal;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.items.IceCyanBlueSquareCoin;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.LeatherArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.MailArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.PlateArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.ScaleArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.ElectricalSmoke;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Food;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Pasty;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfExperience;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfDivineInspiration;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.SmallLightHeader;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.SelectableRing;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfIdentify;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRemoveCurse;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTransmutation;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfMetamorphosis;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.SelectableWand;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfSun;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.PeachGodStateSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SmallLeafSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.watabou.noosa.Game;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class PeachGodState extends NTNPC {
    {
        //TODO 完善祈愿阶段性变化 如果一次10连就直接渲染最终效果
        spriteClass = PeachGodStateSprite.class;
    }

    public int count = 0;

    @Override
    public boolean interact(Char c) {

        sprite.turnTo(pos, hero.pos);

        Game.runOnRenderThread(new Callback() {
            @Override
            public void call() {
                GameScene.show(new WndOptions(new PeachGodStateSprite(),
                        Messages.get(PeachGodState.class, "title"),
                        Messages.get(PeachGodState.class, "lucky_desc"),
                        Messages.get(PeachGodState.class, "single"),
                        Messages.get(PeachGodState.class, "ten_shots"),
                        Messages.get(PeachGodState.class, "cancel")) {
                    @Override
                    protected void onSelect(int index) {
                        super.onSelect(index);
                        if (index == 0) {
                            Game.runOnRenderThread(new Callback() {
                                @Override
                                public void call() {
                                    if(count<9){
                                        pray(0);
                                    }else {
                                        pray(0);
                                    }
                                }
                            });
                        } else if (index == 1) {
                            Game.runOnRenderThread(new Callback() {
                                @Override
                                public void call() {
                                    if(count<9){
                                        pray(1);
                                    }else {
                                        pray(1);
                                    }
                                }
                            });
                        } else if (index == 2){
                            GLog.b(Messages.get(SmallLeafHardDungeon.class,"cancelselect"));
                        }
                    }
                });
            }
        });

        //TODO 完善抽卡逻辑
        return true;
    }

    public void pray(int mode){
        if(mode == 0){
            //单抽
            int p = randomPray();
            count(p);

        } else if (mode == 1) {
            //十连抽
            int[] p = {randomPray(),randomPray(),randomPray(),randomPray(),randomPray(),randomPray(),randomPray(),randomPray(),randomPray(),randomPray()};
            for(int i : p){
                count(i);
            }
        }
    }

    public int randomPray(){

        float f = Random.Int(1,100);

        int type = 0;

        if(count == 10 ) type = 1;

        if(Statistics.prayCount == 41) type = 2;

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
        if(rare<=1) {
            count ++;
        } else {
            count =0;
        }

        if(rare<=2) {
            Statistics.prayCount++;
        } else {
            Statistics.prayCount = 0;
        }

    }

    public Item[] reward0 = {
            new Gold().quantity(555),
            new LeatherArmor().identify(false),
            randomUsingDefaults( Generator.Category.POTION ),
            randomUsingDefaults( Generator.Category.SCROLL ),
            new EnergyCrystal().quantity(5),
            new IceCyanBlueSquareCoin().quantity(10)
    };

    public Item[] reward1 = {
            new Gold().quantity(666),
            new MailArmor().identify(false),
            randomUsingDefaults( Generator.Category.POTION ),
            randomUsingDefaults( Generator.Category.SCROLL ),
            new EnergyCrystal().quantity(10),
            new IceCyanBlueSquareCoin().quantity(20),
            new Pasty(),
    };

    public Item[] reward2 = {
            new Gold().quantity(1145),
            new ScaleArmor().identify(false),
            new PotionOfExperience(),
            new ScrollOfTransmutation(),
            new EnergyCrystal().quantity(20),
            new ScrollOfIdentify().quantity(8),
            new ScrollOfRemoveCurse().quantity(8),
            new Food().quantity(3),
    };

    public Item[] reward3 = {
            new PlateArmor().identify(false),
            new PotionOfDivineInspiration().quantity(2),
            new EnergyCrystal().quantity(20),
            new ElectricalSmoke(),
            new WandOfSun(),
            new SmallLightHeader(),
            new IceCyanBlueSquareCoin().quantity(500),
            Generator.randomArtifact(),
    };

    public Item[] reward4 = {
            new ScrollOfUpgrade().quantity(2),
            new SelectableWand(),
            new SelectableRing(),
            new IceCyanBlueSquareCoin().quantity(2500)
    };

    public void reward(int rare){
        switch (rare){
            case 0:
                if(Random.Int(1,10)>8){

                }else{
                    reward0[Random.Int(1,8)].collect();
                }
                break;
            case 1:
                if(Random.Int(1,12)>11){

                }else{
                    reward1[Random.Int(1,11)].collect();
                }
                break;
            case 2:
                if(Random.Int(1,15)>14){

                }else{
                    reward2[Random.Int(1,14)].collect();
                }
                break;
            case 3:
                if(Random.Int(1,14)>13){

                }else{
                    reward3[Random.Int(1,13)].collect();
                }
                break;
            case 4:
                reward4[Random.Int(1,4)].collect();
        }
    }
}
