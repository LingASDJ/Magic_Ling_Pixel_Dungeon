package com.shatteredpixel.shatteredpixeldungeon.custom.utils;

import static com.shatteredpixel.shatteredpixeldungeon.levels.RegularLevel.birthday;
import static com.shatteredpixel.shatteredpixeldungeon.levels.RegularLevel.holiday;

import com.nlf.calendar.Lunar;
import com.nlf.calendar.Solar;
import com.shatteredpixel.shatteredpixeldungeon.levels.RegularLevel;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;

import java.util.Calendar;
import java.util.Locale;

public class Gregorian {
    private static long eventEndTime = 0;
    /**
     * 这个代码是基于6Tail的农历Java库<br>
     * 皆在计算中国的传统节日，<br>
     * 使地牢自动计算农历日期成为可能<br>
     * 目前已经实现端午节 中秋节 作者自身的生日等<br>
     * <P></P>
     * 要在地牢中使用该代码，<br>
     * 你需要在core级gradle导入指定的库<br>
     * 然后，使用这个库<br>
     * 最后调用在你需要的位置即可<br>
     * <P></P>
     * 实列参见RegularLevel.java：<br>
     *  Gregorian.LunarCheckDate();<br>
     *   <br>
     *  Gradle Config:<br>
     *  build.gradle (Module:core) <br>
     *  implementation 'cn.6tail:lunar:1.7.4'<br>
     *  <P></P>
     *  <a href="https://mvnrepository.com/artifact/cn.6tail/lunar">Lunar Maven</a><br>
     *  <a href="https://github.com/6tail/lunar-java/releases">Lunar Release</a><br>
     *  <a href="https://github.com/6tail/lunar-java">Lunar Github</a>
     *  2024.1.9 加入NTP验证系统时间
     * */
    public static void LunarCheckDate() {

        Calendar calendar = Calendar.getInstance();
        Solar date = Solar.fromDate(calendar.getTime());
        Lunar lunar = date.getLunar();

        int gregorianMonth = calendar.get(Calendar.MONTH) + 1; // Calendar.MONTH 的范围是 0-11，所以需要加 1
        int gregorianDay = calendar.get(Calendar.DAY_OF_MONTH);

        boolean isZQJ = lunar.getMonth() == 8 && (lunar.getDay() >= 15 - 10 && lunar.getDay() <= 15 + 12);

        boolean isDevBirthday = lunar.getMonth() == 8 && lunar.getDay() >= 22 && lunar.getDay() <= 25;

        boolean isDWJ = lunar.getMonth() == 5 && (lunar.getDay() >= 0 && lunar.getDay() <= 5 + 7);

        boolean isHBJ = false;
        if (gregorianMonth == 7) {
            isHBJ = true;
        } else if (gregorianMonth == 8) {
            isHBJ = gregorianDay <= 15;
        }

        boolean isSF = lunar.getMonth() == 1 && (lunar.getDay() >= 1 && lunar.getDay() <= 1 + 13);

        boolean isYXJ= lunar.getMonth() == 1 && (lunar.getDay() >= 15 && lunar.getDay() <= 15 + 7);

        boolean isZQJ2025 = lunar.getMonth() == 4 && lunar.getDay() >= 6 && lunar.getDay() <= 24;


        if(isYXJ){
            holiday = RegularLevel.Holiday.YX;
        }

        if(isSF){
            holiday = RegularLevel.Holiday.CJ;
        }

        // 判断是否是中秋节前10天到中秋节后12天
        if (isZQJ  || isZQJ2025) {
            holiday = RegularLevel.Holiday.ZQJ;
        }
        // 判断是否是开发组的开发者Ling的当天生日到后续三天-8-22--8.25
        if (isDevBirthday) {
            birthday = RegularLevel.DevBirthday.DEV_BIRTHDAY;
        }
        // 判断是否是端午节前4天到端午节后7天
        if (isDWJ) {
            holiday = RegularLevel.Holiday.DWJ;
            eventEndTime = calculateEventEndTime(lunar, 5, 12);
        }

        if ((gregorianMonth == 8 && gregorianDay >= 30) || (gregorianMonth == 9 && gregorianDay <= 30)) {
            holiday = RegularLevel.Holiday.HWEEN;
            eventEndTime =  calculateSolarEventEnd(2025,9,30);
        }
    }

    public static long calculateSolarEventEnd(int year, int month, int day) {
        Calendar end = Calendar.getInstance();
        end.set(year, month-1, day, 23, 59, 59); // 月份参数适配真实月份
        end.set(Calendar.MILLISECOND, 0);
        return end.getTimeInMillis();
    }


    // 新增计算方法
    private static long calculateEventEndTime(Lunar currentLunar, int month, int endDay) {
        try {
            // 构造结束当天的农历对象（23:59:59）
            Lunar endLunar = new Lunar(
                    currentLunar.getYear(),
                    month,
                    endDay,
                    23, 59, 59
            );

            // 转换为公历
            Solar endSolar = endLunar.getSolar();

            // 生成时间戳
            Calendar endCal = Calendar.getInstance();
            endCal.set(endSolar.getYear(),
                    endSolar.getMonth() - 1, // Calendar月份从0开始
                    endSolar.getDay(),
                    23, 59, 59);
            endCal.set(Calendar.MILLISECOND, 0);
            return endCal.getTimeInMillis();
        } catch (Exception e) {
            // 处理无效日期情况
            return 0;
        }
    }

    // 新增获取剩余时间方法
    public static String getRemainingTime() {
        if (eventEndTime == 0)
            return Messages.get(Gregorian.class,"no_activity");

        long current = System.currentTimeMillis();
        if (current >= eventEndTime)
            return Messages.get(Gregorian.class,"end_activity");

        long diff = eventEndTime - current;

        // 精确计算时间差
        long seconds = diff / 1000;
        long days = seconds / 86400;
        seconds %= 86400;
        long hours = seconds / 3600;
        seconds %= 3600;
        long minutes = seconds / 60;
        seconds %= 60;

        String string;

        if(days > 1){
            string =  String.format(Locale.CHINA, "行动剩余："+"%d天", days);
        } else {
            string =  String.format(Locale.CHINA, "行动剩余："+"%d天 %02d:%02d:%02d", days, hours, minutes, seconds);
        }

        return string;
    }

}
