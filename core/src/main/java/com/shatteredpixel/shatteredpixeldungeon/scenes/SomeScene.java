package com.shatteredpixel.shatteredpixeldungeon.scenes;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.TextureFilm;

import java.util.ArrayList;
import java.util.List;

public class SomeScene extends PixelScene {


    @Override
    public void create() {
        super.create();
        SomeWindow window = new SomeWindow();
        add(window);
    }

    @Override
    public void update() {
        super.update();
    }

    public static class SomeWindow extends Window {
        public SomeWindow() {
            super( 200, 200);

            SomeSprite someSprite = new SomeSprite(2,4);
            someSprite.x = 100 - 8;
            someSprite.y = 100 - 8;
            add(someSprite);
        }
    }

    public static class SomeSprite extends CharSprite {

        public Animation sabc;

        public SomeSprite(int finalFrame,int times) {
            super();

            texture(Assets.Interfaces.BADGES);
            TextureFilm frames = new TextureFilm(texture,16,16);

            sabc = new Animation(10,false);

            //finalFrame是最后一帧（去掉尾巴），times是第一轮的循环次数，frames是需要循环的帧
            List<Integer> slowFrames = generateSlowdownFrames(finalFrame,times,0,1,2,3);
            Integer[] framesArray = slowFrames.toArray(new Integer[0]);
            sabc.frames(frames,(Object[]) framesArray);

//            sabc.frames(frames,
//                    0, 1, 2, 3, 4,
//                    0, 1, 2, 3, 4,
//                    0, 1, 2, 3, 4,
//                    0, 1, 2, 3, 4,//四次，也就是times=4
//                    0, 0, 1, 1, 2, 2, 3, 3, 4, 4,
//                    0, 0, 1, 1, 2, 2, 3, 3, 4, 4,
//                    0, 0, 1, 1, 2, 2, 3, 3, 4, 4,//少一次
//                    0, 0, 0, 1, 1, 1, 2, 2, 2, 3, 3, 3, 4, 4, 4,
//                    0, 0, 0, 1, 1, 1, 2, 2, 2, 3, 3, 3, 4, 4, 4,
//                    0, 0, 0, 0, 1, 1, 1, 2, 2, 2, 2//剩一次时结束，停在finalFrame
//            );

            play(sabc);



        }


        public static List<Integer> generateSlowdownFrames(int finalFrame,int times,int... frames) {
            List<Integer> result = new ArrayList<>();

            for (int repeat = 1; repeat <= times; repeat++) {
                int loops = times - repeat + 1;
                for (int l = 0; l < loops; l++) {
                    for (int frame : frames) {
                        for (int r = 0; r < repeat; r++) {
                            result.add(frame);
                        }
                    }
                }
            }

            // 剪裁尾部，保留最后一个等于 finalFrame 的位置为止
            int lastIndex = result.lastIndexOf(finalFrame);
            if (lastIndex != -1 && lastIndex < result.size() - 1) {
                result = result.subList(0, lastIndex + 1);
            }

            return result;
        }
    }
}
