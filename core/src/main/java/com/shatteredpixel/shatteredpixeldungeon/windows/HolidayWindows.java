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

import java.util.ArrayList;
import java.util.List;

public class HolidayWindows extends Window {
    private static final int WIDTH = 120;
    private static final int BOX_HEIGHT = 20;
    private static final int TTL_HEIGHT = 12;
    private static final int GAP = 2;

    private StyledButton prevBtn;
    private StyledButton nextBtn;
    private StyledButton timeBtn;
    private RenderedTextBlock titleText;
    private RenderedTextBlock descText;

    private List<HolidayItem> activeHolidays = new ArrayList<>();
    private int currentIndex = 0;

    // 封装节日：名称 + 枚举类型，区分三种节日
    private static class HolidayItem {
        String holidayName;
        RegularLevel.ChinaHoliday china;
        RegularLevel.WestHoliday west;
        RegularLevel.DevBirthday dev;

        public HolidayItem(RegularLevel.ChinaHoliday h){
            china = h;
            west = null;
            dev = null;
            holidayName = String.valueOf(h);
        }
        public HolidayItem(RegularLevel.WestHoliday h){
            china = null;
            west = h;
            dev = null;
            holidayName = String.valueOf(h);
        }
        public HolidayItem(RegularLevel.DevBirthday h){
            china = null;
            west = null;
            dev = h;
            holidayName = String.valueOf(h);
        }

        // 获取当前节日独立倒计时
        public String getTimeText(){
            if(china != null){
                return Gregorian.getRemainingTime(china);
            }else if(west != null){
                return Gregorian.getRemainingTime(west);
            }else if(dev != null){
                return Gregorian.getRemainingTime(dev);
            }
            return "N / A";
        }
    }

    public HolidayWindows(){
        resize(WIDTH, 100);
        refreshHolidayList();

        titleText = PixelScene.renderTextBlock("", TTL_HEIGHT - GAP);
        titleText.hardlight(0xFFFF00);
        add(titleText);

        prevBtn = new StyledButton(Chrome.Type.WINDOW_SILVER, "<"){
            @Override
            protected void onClick() {
                currentIndex--;
                refreshUI();
            }
        };
        prevBtn.setRect(GAP, 0, 10, BOX_HEIGHT);
        add(prevBtn);

        nextBtn = new StyledButton(Chrome.Type.WINDOW_SILVER, ">"){
            @Override
            protected void onClick() {
                currentIndex++;
                refreshUI();
            }
        };
        nextBtn.setRect(WIDTH - 28 - GAP,0, 10, BOX_HEIGHT);
        add(nextBtn);

        timeBtn = new StyledButton(Chrome.Type.WINDOW_SILVER, "") {
            @Override
            protected void onClick() {}
        };
        add(timeBtn);

        descText = PixelScene.renderTextBlock("", 6);
        descText.maxWidth(WIDTH - GAP * 2);
        add(descText);

        refreshUI();
    }

    // 收集所有激活节日
    private void refreshHolidayList(){
        activeHolidays.clear();
        if (chinaHoliday != RegularLevel.ChinaHoliday.NONE){
            activeHolidays.add(new HolidayItem(chinaHoliday));
        }
        if (holiday != RegularLevel.WestHoliday.NONE){
            activeHolidays.add(new HolidayItem(holiday));
        }
        if (birthday != RegularLevel.DevBirthday.NONE){
            activeHolidays.add(new HolidayItem(birthday));
        }
        if (!activeHolidays.isEmpty() && currentIndex >= activeHolidays.size()){
            currentIndex = activeHolidays.size() - 1;
        }
    }

    // 获取标题（附带页码：节日名 (1/3)）
    private String getCurrentTitle(){
        if (activeHolidays.isEmpty()) return "none";
        HolidayItem item = activeHolidays.get(currentIndex);
        return Messages.get(Gregorian.class,item.holidayName) + " (" + (currentIndex+1) + "/" + activeHolidays.size() + ")";
    }

    private String getCurrentDesc(){
        if (activeHolidays.isEmpty()) return "";
        HolidayItem item = activeHolidays.get(currentIndex);
        String key = item.holidayName + "_desc";
        return Messages.get(Gregorian.class, key);
    }

    // 刷新界面 + 自动重算窗口高度
    private void refreshUI(){
        if(activeHolidays.isEmpty()){
            titleText.text("none");
            descText.text("");
            timeBtn.text("N / A");
            prevBtn.enable(false);
            nextBtn.enable(false);

            titleText.setPos(WIDTH/2f - titleText.width()/2, GAP);
            PixelScene.align(titleText);
            prevBtn.setRect(GAP, titleText.bottom()+3, 15, BOX_HEIGHT);
            nextBtn.setRect(WIDTH - 15 - GAP, titleText.bottom()+3,15, BOX_HEIGHT);
            timeBtn.setRect(prevBtn.right()+GAP, titleText.bottom()+3, nextBtn.left() - prevBtn.right() - GAP*2, BOX_HEIGHT);
            descText.setRect(GAP, timeBtn.bottom() +5, WIDTH - GAP*2, 40);
            resize(WIDTH, (int)(descText.bottom()+3));
            return;
        }

        HolidayItem cur = activeHolidays.get(currentIndex);
        titleText.text(getCurrentTitle());
        descText.text(getCurrentDesc());
        timeBtn.text(cur.getTimeText());

        prevBtn.enable(currentIndex > 0);
        nextBtn.enable(currentIndex < activeHolidays.size()-1);

        //布局排版
        titleText.setPos(WIDTH/2f - titleText.width()/2, GAP);
        PixelScene.align(titleText);

        float btnY = titleText.bottom() + 3;
        prevBtn.setRect(GAP, btnY, 15, BOX_HEIGHT);
        nextBtn.setRect(WIDTH - 15 - GAP, btnY, 15, BOX_HEIGHT);
        timeBtn.setRect(prevBtn.right()+GAP, btnY, nextBtn.left() - prevBtn.right() - GAP*2, BOX_HEIGHT);

        descText.setRect(GAP, timeBtn.bottom() +5, WIDTH - GAP*2, 100);
        PixelScene.align(descText);

        resize(WIDTH, (int) (descText.bottom()+3));
    }

    @Override
    public void update() {
        super.update();
        refreshHolidayList();
        refreshUI();
    }
}
