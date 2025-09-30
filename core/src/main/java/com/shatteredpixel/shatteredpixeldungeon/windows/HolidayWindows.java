package com.shatteredpixel.shatteredpixeldungeon.windows;

import static com.shatteredpixel.shatteredpixeldungeon.levels.RegularLevel.birthday;
import static com.shatteredpixel.shatteredpixeldungeon.levels.RegularLevel.chinaHoliday;
import static com.shatteredpixel.shatteredpixeldungeon.levels.RegularLevel.holiday;

import com.shatteredpixel.shatteredpixeldungeon.Chrome;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.Gregorian;
import com.shatteredpixel.shatteredpixeldungeon.levels.RegularLevel;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.StyledButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;

public class HolidayWindows extends Window {
    private static final int WIDTH = 120;
    private static final int HEIGHT = 100;
    private static final int BOX_HEIGHT = 20;
    private static final int TTL_HEIGHT = 12;
    private static final int GAP= 2;

    StyledButton button2;
    RenderedTextBlock rtx;

    public HolidayWindows(){
        resize(WIDTH, HEIGHT);

        RenderedTextBlock rtb = PixelScene.renderTextBlock(Messages.get(Gregorian.class, Rules()), TTL_HEIGHT - GAP);
        rtb.setPos(WIDTH/2f - rtb.width()/2, GAP);
        PixelScene.align(rtb);        rtb.hardlight(0xFFFF00);
        add(rtb);


        button2 = new StyledButton(Chrome.Type.WINDOW_SILVER, Gregorian.getRemainingTime()) {
            @Override
            protected void onClick() {

            }
        };
        button2.setRect(GAP, rtb.bottom() + 5, WIDTH - GAP * 2, BOX_HEIGHT);
        add(button2);

        rtx = PixelScene.renderTextBlock(Messages.get(Gregorian.class, Rules() + "_desc"), 6);
        rtx.setPos(WIDTH/2f - rtx.width()/2, GAP);
        PixelScene.align(rtx);
        rtx.maxWidth(WIDTH - GAP * 2);
        rtx.setRect(GAP, button2.bottom() + 5, WIDTH - GAP * 2, BOX_HEIGHT);
        add(rtx);

        resize(WIDTH, (int) (rtx.bottom()+3));
    }

    private String Rules() {
        if (chinaHoliday != RegularLevel.ChinaHoliday.NONE) {
            return String.valueOf(chinaHoliday);
        } else if (holiday != RegularLevel.WestHoliday.NONE) {
            return String.valueOf(holiday);
        } else if (birthday != RegularLevel.DevBirthday.NONE) {
            return String.valueOf(birthday);
        } else {
            return "none";
        }
    }


    @Override
    public void update() {
        super.update();
        boolean checkList = chinaHoliday != RegularLevel.ChinaHoliday.NONE || holiday != RegularLevel.WestHoliday.NONE  || birthday != RegularLevel.DevBirthday.NONE;
        button2.text(checkList ? Gregorian.getRemainingTime() : "N / A");
    }

}

