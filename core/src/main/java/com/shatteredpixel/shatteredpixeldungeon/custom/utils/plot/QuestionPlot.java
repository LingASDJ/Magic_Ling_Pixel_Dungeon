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

package com.shatteredpixel.shatteredpixeldungeon.custom.utils.plot;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Conducts;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Rankings;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.Question;
import com.shatteredpixel.shatteredpixeldungeon.items.Amulet;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.watabou.noosa.Game;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class QuestionPlot extends Plot {

    private final static int maxprocess = 1;

    {
        process = 1;
    }

    protected String getPlotName() {
        return SEWER_NAME;
    }

    @Override
    public void reachProcess(WndDialog wndDialog) {
        diagulewindow = wndDialog;

        while(this.process < needed_process )
        {
            this.process();
        }
    }

    @Override
    public void process() {
        if(diagulewindow!=null) {
            switch (process) {
                default:
                case 1:
                    process_to_1();
                    break;
            }
            diagulewindow.update();
            process ++;
        }
    }

    @Override
    public void initial(WndDialog wndDialog) {
        diagulewindow = wndDialog;
        process = 2;
        process_to_1();
    }

    @Override
    public boolean end() {
        return process > maxprocess;
    }

    @Override
    public void skip() {
        diagulewindow.cancel();
        WndDialog.settedPlot = null;
    }

    private void process_to_1()
    {
        diagulewindow.hideAll();
        diagulewindow.setLeftName(Messages.get(Question.class,"name"));
        if (Random.Int(2)==0 || Rankings.INSTANCE.records.isEmpty()){
            diagulewindow.changeText(Messages.get(Question.class,"message1_1"));
        } else {
            for (Rankings.Record rec : Rankings.INSTANCE.records){
                if (Rankings.INSTANCE.lastRecord==0){
                    diagulewindow.changeText(Messages.get(Question.class,rec.win ? "message1_2" : "message1_3"));
                    break;
                }
            }
        }
    }

    public static class Plot_1 extends Plot {
        private final static int maxprocess = 2;

        {
            process = 1;
        }

        protected String getPlotName() {
            return SEWER_NAME;
        }

        @Override
        public void reachProcess(WndDialog wndDialog) {
            diagulewindow = wndDialog;

            while(this.process < needed_process )
            {
                this.process();
            }
        }

        @Override
        public void process() {
            if(diagulewindow!=null) {
                switch (process) {
                    default:
                    case 1:
                        process_to_1();
                        break;
                    case 2:
                        process_to_2();
                        break;
                }
                diagulewindow.update();
                process ++;
            }
        }

        @Override
        public void initial(WndDialog wndDialog) {
            diagulewindow = wndDialog;
            process = 2;
            process_to_1();
        }

        @Override
        public boolean end() {
            return process > maxprocess;
        }

        @Override
        public void skip() {
            diagulewindow.cancel();
            WndDialog.settedPlot = null;
        }

        private void process_to_1()
        {
            diagulewindow.hideAll();
            diagulewindow.setLeftName(Messages.get(Question.class,"name"));
            diagulewindow.changeText(Messages.get(Question.class,"message2_1"));
        }

        private void process_to_2()
        {
            diagulewindow.changeText(Messages.get(Question.class,"message2_2"));
            Game.runOnRenderThread(new Callback() {
                @Override
                public void call(){
                    Question qes = new Question();
                    GameScene.show(new WndOptions(
                            qes.sprite(),
                            Messages.titleCase(qes.name()),
                            Messages.get(Question.class, "title_5"),
                            Messages.get(Question.class, "true"),
                            Messages.get(Question.class, "false")
                    ){
                        @Override
                        protected void onSelect(int index) {
                            if (index==0){
                                diagulewindow.changeText(Messages.get(Question.class, "message2_3"));
                                process_to_3();
                            }
                        }
                    });
                }
            });
        }

        private void process_to_3() {}
    }

    public static class Plot_2 extends Plot {
        private final static int maxprocess = 3;

        {
            process = 1;
        }

        protected String getPlotName() {
            return SEWER_NAME;
        }

        @Override
        public void reachProcess(WndDialog wndDialog) {
            diagulewindow = wndDialog;

            while(this.process < needed_process )
            {
                this.process();
            }
        }

        @Override
        public void process() {
            if(diagulewindow!=null) {
                switch (process) {
                    default:
                    case 1:
                        process_to_1();
                        break;
                    case 2:
                        process_to_2();
                        break;
                    case 3:
                        process_to_3();
                        break;
                }
                diagulewindow.update();
                process ++;
            }
        }

        @Override
        public void initial(WndDialog wndDialog) {
            diagulewindow = wndDialog;
            process = 2;
            process_to_1();
        }

        @Override
        public boolean end() {
            return ending() ? process > maxprocess : process > 1;
        }

        @Override
        public void skip() {
            diagulewindow.cancel();
            WndDialog.settedPlot = null;
        }

        private void process_to_1()
        {
            diagulewindow.hideAll();
            diagulewindow.setLeftName(Messages.get(Question.class,"name"));
            ArrayList<Item> items = new ArrayList<>(hero.belongings.backpack.items);
            boolean win = false;
            for (Item i : items)
                if (i instanceof Amulet)
                    win = true;
            if (Dungeon.isDLC(Conducts.Conduct.DEV)){
                diagulewindow.changeText(Messages.get(Question.class,"message4_1"));
            } else if (win){
                diagulewindow.changeText(Messages.get(Question.class,"message4_2"));
            } else {
                diagulewindow.changeText(Messages.get(Question.class,"message4_3"));
            }
        }

        private void process_to_2()
        {
            diagulewindow.changeText(Messages.get(Question.class,"message4_4"));
        }

        private void process_to_3()
        {
            diagulewindow.changeText(Messages.get(Question.class,"message4_5"));
        }

        private boolean ending(){
            ArrayList<Item> items = new ArrayList<>(hero.belongings.backpack.items);
            boolean win = false;
            for (Item i : items)
                if (i instanceof Amulet)
                    win = true;
            if (Dungeon.isDLC(Conducts.Conduct.DEV)){
                return false;
            } else if (win){
                return true;
            } else {
                return false;
            }
        }
    }

    public static class Plot_3 extends Plot {
        private final static int maxprocess = 2;

        {
            process = 1;
        }

        protected String getPlotName() {
            return SEWER_NAME;
        }

        @Override
        public void reachProcess(WndDialog wndDialog) {
            diagulewindow = wndDialog;

            while(this.process < needed_process )
            {
                this.process();
            }
        }

        @Override
        public void process() {
            if(diagulewindow!=null) {
                switch (process) {
                    default:
                    case 1:
                        process_to_1();
                        break;
                    case 2:
                        process_to_2();
                        break;
                }
                diagulewindow.update();
                process ++;
            }
        }

        @Override
        public void initial(WndDialog wndDialog) {
            diagulewindow = wndDialog;
            process = 2;
            process_to_1();
        }

        @Override
        public boolean end() {
            return process > maxprocess;
        }

        @Override
        public void skip() {
            diagulewindow.cancel();
            WndDialog.settedPlot = null;
        }

        private void process_to_1()
        {
            diagulewindow.hideAll();
            diagulewindow.setLeftName(Messages.get(Question.class,"name"));
            diagulewindow.changeText(Messages.get(Question.class,"message5_1"));
            Game.runOnRenderThread(new Callback() {
                @Override
                public void call(){
                    Question qes = new Question();
                    GameScene.show(new WndOptions(
                            qes.sprite(),
                            Messages.titleCase(qes.name()),
                            Messages.get(Question.class, "title_1"),
                            Messages.get(Question.class, "ling"),
                            Messages.get(Question.class, "birth"),
                            Messages.get(Question.class, "dungeon")
                    ){
                        @Override
                        protected void onSelect(int index) {
                            if (index==0){
                                diagulewindow.changeText(Messages.get(Question.class,"message5_2"));
                            } else if (index==1){
                                diagulewindow.changeText(Messages.get(Question.class,"message5_3"));
                            } else if (index==2){
                                diagulewindow.changeText(Messages.get(Question.class,"message5_4"));
                            }
                        }
                    });
                }
            });
        }

        private void process_to_2(){}
    }

    public static class Plot_4 extends Plot {
        private final static int maxprocess = 2;

        {
            process = 1;
        }

        protected String getPlotName() {
            return SEWER_NAME;
        }

        @Override
        public void reachProcess(WndDialog wndDialog) {
            diagulewindow = wndDialog;

            while(this.process < needed_process )
            {
                this.process();
            }
        }

        @Override
        public void process() {
            if(diagulewindow!=null) {
                switch (process) {
                    default:
                    case 1:
                        process_to_1();
                        break;
                    case 2:
                        process_to_2();
                        break;
                }
                diagulewindow.update();
                process ++;
            }
        }

        @Override
        public void initial(WndDialog wndDialog) {
            diagulewindow = wndDialog;
            process = 2;
            process_to_1();
        }

        @Override
        public boolean end() {
            return process > maxprocess;
        }

        @Override
        public void skip() {
            diagulewindow.cancel();
            WndDialog.settedPlot = null;
        }

        private void process_to_1()
        {
            diagulewindow.hideAll();
            diagulewindow.setLeftName(Messages.get(Question.class,"name"));
            diagulewindow.changeText(Messages.get(Question.class,"no_more_1"));
        }

        private void process_to_2()
        {
            diagulewindow.changeText(Messages.get(Question.class,"no_more_2"));
        }
    }

    public static class Plot_WIN extends Plot {
        private final static int maxprocess = 1;

        {
            process = 1;
        }

        protected String getPlotName() {
            return SEWER_NAME;
        }

        @Override
        public void reachProcess(WndDialog wndDialog) {
            diagulewindow = wndDialog;

            while(this.process < needed_process )
            {
                this.process();
            }
        }

        @Override
        public void process() {
            if(diagulewindow!=null) {
                switch (process) {
                    default:
                    case 1:
                        process_to_1();
                        break;
                }
                diagulewindow.update();
                process ++;
            }
        }

        @Override
        public void initial(WndDialog wndDialog) {
            diagulewindow = wndDialog;
            process = 2;
            process_to_1();
        }

        @Override
        public boolean end() {
            return process > maxprocess;
        }

        @Override
        public void skip() {
            diagulewindow.cancel();
            WndDialog.settedPlot = null;
        }

        private void process_to_1()
        {
            diagulewindow.hideAll();
            diagulewindow.setLeftName(Messages.get(Question.class,"name"));
            diagulewindow.changeText(Messages.get(Question.class,"win"));
        }
    }

    public static class Plot_LOSE extends Plot {
        private final static int maxprocess = 1;

        {
            process = 1;
        }

        protected String getPlotName() {
            return SEWER_NAME;
        }

        @Override
        public void reachProcess(WndDialog wndDialog) {
            diagulewindow = wndDialog;

            while(this.process < needed_process )
            {
                this.process();
            }
        }

        @Override
        public void process() {
            if(diagulewindow!=null) {
                switch (process) {
                    default:
                    case 1:
                        process_to_1();
                        break;
                }
                diagulewindow.update();
                process ++;
            }
        }

        @Override
        public void initial(WndDialog wndDialog) {
            diagulewindow = wndDialog;
            process = 2;
            process_to_1();
        }

        @Override
        public boolean end() {
            return process > maxprocess;
        }

        @Override
        public void skip() {
            diagulewindow.cancel();
            WndDialog.settedPlot = null;
        }

        private void process_to_1()
        {
            diagulewindow.hideAll();
            diagulewindow.setLeftName(Messages.get(Question.class,"name"));
            diagulewindow.changeText(Messages.get(Question.class,"lose"));
        }
    }

    public static class Plot_DRAW extends Plot {
        private final static int maxprocess = 1;

        {
            process = 1;
        }

        protected String getPlotName() {
            return SEWER_NAME;
        }

        @Override
        public void reachProcess(WndDialog wndDialog) {
            diagulewindow = wndDialog;

            while(this.process < needed_process )
            {
                this.process();
            }
        }

        @Override
        public void process() {
            if(diagulewindow!=null) {
                switch (process) {
                    default:
                    case 1:
                        process_to_1();
                        break;
                }
                diagulewindow.update();
                process ++;
            }
        }

        @Override
        public void initial(WndDialog wndDialog) {
            diagulewindow = wndDialog;
            process = 2;
            process_to_1();
        }

        @Override
        public boolean end() {
            return process > maxprocess;
        }

        @Override
        public void skip() {
            diagulewindow.cancel();
            WndDialog.settedPlot = null;
        }

        private void process_to_1()
        {
            diagulewindow.hideAll();
            diagulewindow.setLeftName(Messages.get(Question.class,"name"));
            diagulewindow.changeText(Messages.get(Question.class,"draw"));
        }
    }
}