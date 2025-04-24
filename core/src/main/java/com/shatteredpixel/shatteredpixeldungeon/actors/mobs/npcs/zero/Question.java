/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2022 Evan Debenham
 *
 * Mund Pixel Dungeon
 * Copyright (C) 2018-2023 Thliey Pen
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NTNPC;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.plot.QuestionPlot;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.BlessingNecklace;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.QuestionSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Question extends NTNPC {

    {
        spriteClass = QuestionSprite.class;
    }

    private int first=0;
    private int second=0;
    private int tired=0;
    public ArrayList<Integer> another = new ArrayList<>();
    public ArrayList<Integer> player = new ArrayList<>();
    public ArrayList<Boolean> rule = new ArrayList<>();


    private static final String ANOTHER = "another";
    private static final String PLAYER = "player";
    private static final String RULE = "rule";
    private static final String FIRST = "first";
    private static final String SECOND = "second";
    private static final String TIRED = "tried";

//    @Override
//    public void restoreFromBundle( Bundle bundle ) {
//        super.restoreFromBundle(bundle);
//        bundle.put(FIRST, first);
//        bundle.put(SECOND, second);
//        bundle.put(TIRED, tired);
//        for (int i=0; i<bundle.getIntArray(ANOTHER).length; i++)
//            another.add(bundle.getIntArray(ANOTHER)[i]);
//        for (int i=0; i<bundle.getIntArray(PLAYER).length; i++)
//            player.add(bundle.getIntArray(PLAYER)[i]);
//        for (int i=0; i<bundle.getBooleanArray(RULE).length; i++)
//            rule.add(bundle.getBooleanArray(RULE)[i]);
//    }
//
//    @Override
//    public void storeInBundle( Bundle bundle ) {
//        super.storeInBundle(bundle);
//        first = bundle.getInt(FIRST);
//        second = bundle.getInt(SECOND);
//        tired = bundle.getInt(TIRED);
//        int[] Another = new int[another.size()];
//        for (int i=0; i< another.size(); i++)
//            Another[i] = another.get(i);
//        bundle.put(ANOTHER, Another);
//        int[] Player = new int[player.size()];
//        for (int i=0; i< player.size(); i++)
//            Another[i] = player.get(i);
//        bundle.put(PLAYER, Player);
//        boolean[] Rule = new boolean[rule.size()];
//        for (int i=0; i< rule.size(); i++)
//            Rule[i] = rule.get(i);
//        bundle.put(RULE, Rule);
//    }

    @Override
    public boolean interact(Char c) {
        title_1();
        return true;
    }

    private void title_1() {
        Game.runOnRenderThread(new Callback() {
            @Override
            public void call() {
                GameScene.show(new WndOptions(
                                       sprite(),
                                       Messages.titleCase(name()),
                                       Messages.get(Question.class, "title"),
                                       Messages.get(Question.class, "option_nothing"),
                                       Messages.get(Question.class, "option_chat"),
                                       Messages.get(Question.class, "option_enquire"),
                                       Messages.get(Question.class, "option_request"),
                                       Messages.get(Question.class, "option_duel"),
                                       Messages.get(Question.class, "option_game")
                               ) {
                                   @Override
                                   protected void onSelect(int index) {
                                       if (index==1){
                                           QuestionPlot P = new QuestionPlot();
                                           Game.runOnRenderThread(new Callback() {
                                               @Override
                                               public void call() {
                                                   GameScene.show(new WndDialog(P,false));
                                               }
                                           });
                                       } else if (index==2){
                                           QuestionPlot.Plot_3 P = new QuestionPlot.Plot_3();
                                           Game.runOnRenderThread(new Callback() {
                                               @Override
                                               public void call() {
                                                   GameScene.show(new WndDialog(P,false));
                                               }
                                           });
                                       } else if (index==3){
                                           QuestionPlot.Plot_1 P = new QuestionPlot.Plot_1();
                                           Game.runOnRenderThread(new Callback() {
                                               @Override
                                               public void call() {
                                                   GameScene.show(new WndDialog(P,false));
                                               }
                                           });
                                       } else if (index==4){
                                           QuestionPlot.Plot_2 P = new QuestionPlot.Plot_2();
                                           Game.runOnRenderThread(new Callback() {
                                               @Override
                                               public void call() {
                                                   GameScene.show(new WndDialog(P,false));
                                               }
                                           });
                                       } else if (index==5){
                                           title_2();
                                       }
                                   }
                               }
                );
            }});
    }

    private void title_2(){
        Game.runOnRenderThread(new Callback() {
            @Override
            public void call(){
                Question qes = new Question();
                GameScene.show(new WndOptions(
                        qes.sprite(),
                        Messages.titleCase(qes.name()),
                        Messages.get(Question.class, "title_4"),
                        Messages.get(Question.class, "Fgame"),
                        Messages.get(Question.class, "Sgame"),
                        Messages.get(Question.class, "Tgame")
                ){
                    @Override
                    protected void onSelect(int index) {
                        if (index==0){
                            if (first<2) {
                                if (Dungeon.gold >= 50) {
                                    Dungeon.gold -= 50;
                                    for (int i = 1; i < 6; i++)
                                        another.add(Random.Int(11 - i) + i);
                                    for (int i = 1; i < 6; i++)
                                        player.add(Random.Int(10) + 1);
                                    Fgame();
                                    if (Random.Int(3) == 0) {
                                        GLog.w(Messages.get(Question.class, "warning_1"));
                                    }
                                } else {
                                    GLog.i(Messages.get(Question.class, "no_money"));
                                }
                            } else {
                                QuestionPlot.Plot_4 P = new QuestionPlot.Plot_4();
                                Game.runOnRenderThread(new Callback() {
                                    @Override
                                    public void call() {
                                        GameScene.show(new WndDialog(P,false));
                                    }
                                });
                            }
                        } else if (index==1){
                            if (second<1) {
                                int T = 0;
                                int F = 0;
                                for (int i = 0; i < 18; i++) {
                                    boolean j = Random.Int(2) == 0 ? true : false;
                                    if (T >= 6) {
                                        j = false;
                                    } else if (F >= 15) {
                                        j = true;
                                    }
                                    rule.add(j);
                                }
                                for (int i = 0; i < 18; i++) {
                                    if (Random.Int(5) <= 1)
                                        another.add(1);
                                    else
                                        another.add(0);
                                }
                                player.add(1);
                                player.add(0);
                                player.add(0);
                                player.add(1);
                                player.add(1);
                                Sgame();
                                if (Random.Int(3) == 0) {
                                    if (Random.Int(3) == 0)
                                        GLog.w(Messages.get(Question.class, "warning_2"));
                                    else
                                        GLog.w(Messages.get(Question.class, "warning_3"));
                                }
                            } else {
                                QuestionPlot.Plot_4 P = new QuestionPlot.Plot_4();
                                Game.runOnRenderThread(new Callback() {
                                    @Override
                                    public void call() {
                                        GameScene.show(new WndDialog(P,false));
                                    }
                                });
                            }
                        } else if (index==2){
                            if (-500<=tired && tired<=500) {
                                for (int i = 0; i < 4; i++)
                                    player.add(0);
                                another.add(Random.Int(6) + 1);
                                title_3();
                            } else {
                                QuestionPlot.Plot_4 P = new QuestionPlot.Plot_4();
                                Game.runOnRenderThread(new Callback() {
                                    @Override
                                    public void call() {
                                        GameScene.show(new WndDialog(P,false));
                                    }
                                });
                            }
                        }
                    }
                });
            }
        });
    }

    private void title_3() {
        int[] money = new int[]{50, 100, 200, 500, 1000};
        String[] moneySTR = new String[]{"50", "100", "200", "500", "1000"};
        Game.runOnRenderThread(new Callback() {
            @Override
            public void call(){
                Question qes = new Question();
                GameScene.show(new WndOptions(
                        qes.sprite(),
                        Messages.titleCase(qes.name()),
                        Messages.get(Question.class, "title_4_4"),
                        moneySTR
                ){
                    @Override
                    protected void onSelect(int index) {
                        if (Dungeon.gold >= money[index]){
                            Tgame(money[index]);
                            if (Random.Int(3) == 0) {
                                GLog.w(Messages.get(Question.class, "warning_4"));
                            }
                        } else {
                            GLog.w(Messages.get(Question.class, "no_money"));
                        }
                    }
                });
            }
        });
    }

    private void Fgame() {
        String[] playerSTR = new String[player.size()];
        for (int i=0; i < player.size(); i++)
            playerSTR[i] = String.valueOf(player.get(i));
        int i = Random.Int(another.size());
        final int GameI = i;
        Game.runOnRenderThread(new Callback() {
            @Override
            public void call() {
                GameScene.show(new WndOptions(
                        sprite(),
                        Messages.titleCase(name()),
                        Messages.get(Question.class, "title_4_1", another.get(GameI)),
                        playerSTR
                ) {
                    @Override
                    protected void onSelect(int index) {
                        if (another.get(GameI) <= player.get(index))
                            rule.add(true);
                        another.remove(GameI);
                        player.remove(index);
                        if (!player.isEmpty()) {
                            Fgame();
                        } else {
                            if (rule.size()>=3){
                                first++;
                                player.clear();
                                another.clear();
                                rule.clear();
                                Dungeon.level.drop(new Gold(150), hero.pos);
                                Sample.INSTANCE.play( Assets.Sounds.GOLD, 1, 1, Random.Float( 0.9f, 1.1f ) );
                                ArrayList<Item> items = new ArrayList<>(hero.belongings.backpack.items);
                                boolean having = false;
                                for (Item j : items)
                                    if (j instanceof BlessingNecklace)
                                        having = true;
                                if (!having)
                                    Dungeon.level.drop(new BlessingNecklace(), hero.pos);
                                QuestionPlot.Plot_WIN P = new QuestionPlot.Plot_WIN();
                                Game.runOnRenderThread(new Callback() {
                                    @Override
                                    public void call() {
                                        GameScene.show(new WndDialog(P,false));
                                    }
                                });
                            } else {
                                QuestionPlot.Plot_LOSE P = new QuestionPlot.Plot_LOSE();
                                Game.runOnRenderThread(new Callback() {
                                    @Override
                                    public void call() {
                                        GameScene.show(new WndDialog(P,false));
                                    }
                                });
                            }
                        }
                    }
                });
            }
        });
    }

    private void Sgame() {
        if (rule.size()>0 && player.get(2)<4) {
            Game.runOnRenderThread(new Callback() {
                @Override
                public void call() {
                    if (player.get(0) == 1) {
                        GameScene.show(new WndOptions(
                                sprite(),
                                Messages.titleCase(name()),
                                Messages.get(Question.class, "title_4_2"),
                                Messages.get(Question.class, "he"),
                                Messages.get(Question.class, "you"),
                                Messages.get(Question.class, "give_up")
                        ) {
                            @Override
                            protected void onSelect(int index) {
                                if (index == 0) {
                                    if (rule.get(0)) {
                                        Sample.INSTANCE.play( Assets.Sounds.HIT_CRUSH, 1, 1, Random.Float( 0.9f, 1.1f ) );
                                        player.set(2, player.get(2) + 1);
                                    } else
                                        player.set(0, 0);
                                    player.set(1, player.get(1) + 1);
                                    rule.remove(0);
                                } else if (index == 1) {
                                    if (rule.get(0)) {
                                        Sample.INSTANCE.play( Assets.Sounds.HIT_CRUSH, 1, 1, Random.Float( 0.9f, 1.1f ) );
                                        Dungeon.hero.damage(10000, Dungeon.hero);
                                        player.set(0, 0);
                                    }
                                    player.set(1, player.get(1) + 1);
                                    rule.remove(0);
                                } else if (index == 2) {
                                    rule.clear();
                                    player.set(3, 0);
                                }
                                Sgame();
                            }
                        });
                    } else {
                        Question qes = new Question();
                        GameScene.show(new WndOptions(
                                qes.sprite(),
                                Messages.titleCase(qes.name()),
                                Messages.get(Question.class, "title_4_3"),
                                Messages.get(Question.class, "wait"),
                                Messages.get(Question.class, "give_up")
                        ) {
                            @Override
                            protected void onSelect(int index) {
                                if (index == 1) {
                                    rule.clear();
                                    player.set(3, 0);
                                } else {
                                    if (another.get(player.get(1)) == 1) {
                                        if (rule.get(0)) {
                                            if (player.get(4) == 1) {
                                                player.set(4, 0);
                                                GLog.w(Messages.get(Question.class, "chance"));
                                            } else
                                                Dungeon.hero.damage(10000, new Question());
                                            Sample.INSTANCE.play( Assets.Sounds.HIT_CRUSH, 1, 1, Random.Float( 0.9f, 1.1f ) );
                                        }
                                    } else {
                                        int nowT = 0;
                                        int nowF = 0;
                                        for (boolean i : rule) {
                                            if (i)
                                                nowT++;
                                            else
                                                nowF++;
                                        }
                                        if (nowF <= nowT) {
                                            if (rule.get(0)) {
                                                Sample.INSTANCE.play( Assets.Sounds.HIT_CRUSH, 1, 1, Random.Float( 0.9f, 1.1f ) );
                                                Dungeon.hero.damage(10000, new Question());
                                            } else
                                                player.set(0, 1);
                                        } else {
                                            if (rule.get(0)) {
                                                Sample.INSTANCE.play( Assets.Sounds.HIT_CRUSH, 1, 1, Random.Float( 0.9f, 1.1f ) );
                                                player.set(2, player.get(2) + 1);
                                                player.set(0, 1);
                                            }
                                        }
                                    }
                                    player.set(1, player.get(1) + 1);
                                    rule.remove(0);
                                }
                                Sgame();
                            }
                        });
                    }
                }
            });
        } else {
            if (player.get(3)==1) {
                ArrayList<Item> items = new ArrayList<>(hero.belongings.backpack.items);
                boolean having = false;
                for (Item i : items)
                    if (i instanceof BlessingNecklace)
                        having = true;
                if (!having)
                    Dungeon.level.drop(new BlessingNecklace(), hero.pos);
                Sample.INSTANCE.play( Assets.Sounds.GOLD, 1, 1, Random.Float( 0.9f, 1.1f ) );
                Dungeon.level.drop(new Gold(500), hero.pos);
                QuestionPlot.Plot_WIN P = new QuestionPlot.Plot_WIN();
                Game.runOnRenderThread(new Callback() {
                    @Override
                    public void call() {
                        GameScene.show(new WndDialog(P,false));
                    }
                });
            } else {
                QuestionPlot.Plot_LOSE P = new QuestionPlot.Plot_LOSE();
                Game.runOnRenderThread(new Callback() {
                    @Override
                    public void call() {
                        GameScene.show(new WndDialog(P,false));
                    }
                });
            }
            second++;
            player.clear();
            another.clear();
            rule.clear();
        }
    }

    private void Tgame(int Gold) {
        if (player.get(3)<3) {
            for (int i = 0; i < 3; i++)
                player.set(i, Random.Int(6) + 1);
            Game.runOnRenderThread(new Callback() {
                @Override
                public void call() {
                    Question qes = new Question();
                    GameScene.show(new WndOptions(
                            qes.sprite(),
                            Messages.titleCase(qes.name()),
                            Messages.get(Question.class, "title_4_5", String.valueOf(player.get(0)), String.valueOf(player.get(1)), String.valueOf(player.get(2))),
                            Messages.get(Question.class, "throw"),
                            Messages.get(Question.class, "stop")
                    ) {
                        @Override
                        protected void onSelect(int index) {
                            if (index == 0) {
                                player.add(3, player.get(3)+1);
                                Tgame(Gold);
                            } else {
                                player.add(3, 3);
                            }
                        }
                    });
                }
            });
        } else {
            int anotherPoint = 0;
            if (another.get(0)==1){
                another.add(1, Random.Int(2)==0 ? 1 : 5);
            } else if(another.get(0)==3){
                another.add(1, Random.Int(4)+3);
            } else {
                another.add(1, Random.Int(3)+4);
            }
            for (int i=1; i<7; i++){
                int j = points(another.get(0), another.get(1), i);
                if (j>anotherPoint){
                    anotherPoint = j;
                    another.add(2, i);
                }
            }
            int playerPoint = points(player.get(0), player.get(1), player.get(2));

            int collectPoint = 0;
            if (playerPoint>10)
                collectPoint = playerPoint - 10;
            else if (playerPoint==0)
                collectPoint = 0;
            else
                collectPoint = 1;

            int losePoint = 0;
            if (anotherPoint>10)
                losePoint = anotherPoint - 10;
            else if (anotherPoint==0)
                losePoint = 0;
            else
                losePoint = 1;

            if (playerPoint>anotherPoint){
                tired -= Gold*losePoint;
                Sample.INSTANCE.play( Assets.Sounds.GOLD, 1, 1, Random.Float( 0.9f, 1.1f ) );
                Dungeon.level.drop(new Gold(Gold*collectPoint), hero.pos);
                QuestionPlot.Plot_WIN P = new QuestionPlot.Plot_WIN();
                Game.runOnRenderThread(new Callback() {
                    @Override
                    public void call() {
                        GameScene.show(new WndDialog(P,false));
                    }
                });
            } else if (playerPoint<anotherPoint){
                tired += Gold*losePoint;
                Dungeon.gold -= Gold*losePoint;
                QuestionPlot.Plot_LOSE P = new QuestionPlot.Plot_LOSE();
                Game.runOnRenderThread(new Callback() {
                    @Override
                    public void call() {
                        GameScene.show(new WndDialog(P,false));
                    }
                });
            } else {
                QuestionPlot.Plot_DRAW P = new QuestionPlot.Plot_DRAW();
                Game.runOnRenderThread(new Callback() {
                    @Override
                    public void call() {
                        GameScene.show(new WndDialog(P,false));
                    }
                });
            }
            player.clear();
            another.clear();
            rule.clear();
        }
    }

    private int points(int a, int b, int c){
        boolean[] conditions = new boolean[]{false, false, false};
        if (a==b) conditions[0] = true;
        if (a==c) conditions[0] = true;
        if (b==c) conditions[0] = true;
        int count = 0;
        for (boolean i : conditions)
            if (i) count++;
        if (count==0){
            if ((a==1 && b==3 && c==5) || (a==2 && b==4 && c==6))
                return 12;
        }
        if (count==1){
            if (a==b) return c;
            if (a==c) return b;
            if (b==c) return a;
        }
        if (count==3){
            if ((a==1) || (a==6))
                return 15;
            else
                return 13;
        }
        return 0;
    }
}
