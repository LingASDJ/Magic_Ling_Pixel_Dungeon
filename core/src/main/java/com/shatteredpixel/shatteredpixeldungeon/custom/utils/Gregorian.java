package com.shatteredpixel.shatteredpixeldungeon.custom.utils;

import static com.shatteredpixel.shatteredpixeldungeon.levels.RegularLevel.birthday;
import static com.shatteredpixel.shatteredpixeldungeon.levels.RegularLevel.chinaHoliday;

import com.nlf.calendar.Lunar;
import com.nlf.calendar.Solar;
import com.shatteredpixel.shatteredpixeldungeon.levels.RegularLevel;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;

import java.util.Calendar;

/**
 * 农历节日工具类，基于6Tail的农历Java库实现
 * 功能：计算中国传统节日，使地牢能够自动根据农历日期调整节日状态
 *
 * 已实现的节日：
 * - 端午节
 * - 中秋节
 * - 春节
 * - 元宵节
 * - 开发者生日
 * - 万圣节等
 *
 * 使用前需在core级gradle中导入依赖：
 * implementation 'cn.6tail:lunar:1.7.4'
 *
 * 示例用法：在RegularLevel.java中调用 Gregorian.checkLunarDates();
 *
 * @see <a href="https://mvnrepository.com/artifact/cn.6tail/lunar">Lunar Maven</a>
 * @see <a href="https://github.com/6tail/lunar-java">Lunar Github</a>
 * @since 2024.1.9 加入NTP验证系统时间
 */
public class Gregorian {
    // 事件结束时间戳（毫秒）
    private static long eventEndTime = 0;

    private static final int MID_AUTUMN_PRE_DAYS = 10;    // 中秋节前天数
    private static final int MID_AUTUMN_POST_DAYS = 7;   // 中秋节后天数
    private static final int DEV_BIRTHDAY_START = 22;     // 开发者生日开始日
    private static final int DEV_BIRTHDAY_END = 28;       // 开发者生日结束日
    private static final int DRAGON_BOAT_POST_DAYS = 7;    // 端午节后天数
    private static final int SPRING_FESTIVAL_POST_DAYS = 13; // 春节后天数
    private static final int LANTERN_FESTIVAL_POST_DAYS = 7; // 元宵节后天数

    public static void LunarCheckDate() {
        Calendar calendar = Calendar.getInstance();
        Solar solarDate = Solar.fromDate(calendar.getTime());
        Lunar lunarDate = solarDate.getLunar();
        int gregorianMonth = calendar.get(Calendar.MONTH) + 1; // 转换为1-12月
        int gregorianDay = calendar.get(Calendar.DAY_OF_MONTH);
        eventEndTime = 0;

        checkLanternFestival(lunarDate);
        checkSpringFestival(lunarDate);
        checkMidAutumnFestival(lunarDate);
        checkDeveloperBirthday(lunarDate);
        checkDragonBoatFestival(lunarDate);
        //checkChongYang(lunarDate);
        checkFukePQJ(gregorianMonth, gregorianDay);
        checkChinaBirthday(gregorianMonth, gregorianDay);
        checkMageSkin(gregorianMonth, gregorianDay);
    }

    /**
     * 检查是否为元宵节期间（农历1月15日至15+7天）
     */
    private static void checkLanternFestival(Lunar lunar) {
        if (lunar.getMonth() == 1 &&
                lunar.getDay() >= 15 &&
                lunar.getDay() <= 15 + LANTERN_FESTIVAL_POST_DAYS) {
            chinaHoliday = RegularLevel.ChinaHoliday.YX;
        }
    }

    /**
     * 检查是否为春节期间（农历1月1日至1+13天）
     */
    private static void checkSpringFestival(Lunar lunar) {
        if (lunar.getMonth() == 1 &&
                lunar.getDay() >= 1 &&
                lunar.getDay() <= 1 + SPRING_FESTIVAL_POST_DAYS) {
            chinaHoliday = RegularLevel.ChinaHoliday.CJ;
        }
    }

    /**
     * 检查是否为中秋节期间
     */
    private static void checkMidAutumnFestival(Lunar lunar) {
        boolean isRegularMidAutumn = lunar.getMonth() == 8 &&
                (lunar.getDay() >= 15 &&
                        lunar.getDay() < 15 + MID_AUTUMN_POST_DAYS);

        if (isRegularMidAutumn) {
            chinaHoliday = RegularLevel.ChinaHoliday.ZQJ;
            eventEndTime = calculateLunarEventEndTime(lunar, 8, 21);
        }
    }

    /**
     * 检查是否为开发者生日期间（农历8月22日至28日）
     */
    private static void checkDeveloperBirthday(Lunar lunar) {
        if (lunar.getMonth() == 8 &&
                lunar.getDay() >= DEV_BIRTHDAY_START &&
                lunar.getDay() <= DEV_BIRTHDAY_END) {
            birthday = RegularLevel.DevBirthday.DEV_BIRTHDAY;
            eventEndTime = calculateLunarEventEndTime(lunar, 8, 28);
        }
    }

    /**
     * 检查是否为国庆节期间（阳历10月1日至10.5）
     */
    private static void checkChinaBirthday(int month, int day) {
        if (month == 10) {
            if(day >= 1 &&  day < 6){
                chinaHoliday = RegularLevel.ChinaHoliday.GQJ;
                eventEndTime = calculateSolarEventEndTime(2025, 10, 6);
            }
        }
    }

    private static void checkMageSkin(int month, int day) {
        if (month == 12) {
            if(day >= 24 &&  day < 32){
                chinaHoliday = RegularLevel.ChinaHoliday.MEJ;
                eventEndTime = calculateSolarEventEndTime(2025, 12, 32);
            }
        }
    }

    private static void checkFukePQJ(int month, int day) {
        if (month == 11) {
            if(day >= 1 &&  day < 31){
                chinaHoliday = RegularLevel.ChinaHoliday.PQJ;
                eventEndTime = calculateSolarEventEndTime(2025, 12, 1);
            }
        }
    }

    /**
     * 检查是否为端午节期间（农历5月0日至5+7天）
     */
    private static void checkDragonBoatFestival(Lunar lunar) {
        if (lunar.getMonth() == 5 &&
                (lunar.getDay() >= 0 &&
                        lunar.getDay() <= 5 + DRAGON_BOAT_POST_DAYS)) {
            chinaHoliday = RegularLevel.ChinaHoliday.DWJ;
            eventEndTime = calculateLunarEventEndTime(lunar, 5, 12);
        }
    }

    private static void checkChongYang(Lunar lunar) {
        if (lunar.getMonth() == 9 &&
                (lunar.getDay() >= 9 &&
                        lunar.getDay() <= 16)){
            chinaHoliday = RegularLevel.ChinaHoliday.CYJ;
            eventEndTime = calculateLunarEventEndTime(lunar, 9, 16);
        }
    }

    /**
     * 计算农历事件结束时间戳
     * @param currentLunar 当前农历日期
     * @param endMonth 结束月份（农历）
     * @param endDay 结束日期（农历）
     * @return 结束时间戳（毫秒），失败返回0
     */
    private static long calculateLunarEventEndTime(Lunar currentLunar, int endMonth, int endDay) {
        try {
            // 构造结束当天的农历对象（23:59:59）
            Lunar endLunar = new Lunar(
                    currentLunar.getYear(),
                    endMonth,
                    endDay,
                    23, 59, 59
            );

            // 转换为公历并生成时间戳
            Solar endSolar = endLunar.getSolar();
            return getCalendarTimeInMillis(
                    endSolar.getYear(),
                    endSolar.getMonth() - 1, // 转换为Calendar的0-11月
                    endSolar.getDay()
            );
        } catch (IllegalArgumentException e) {
            // 捕获无效日期异常
            return 0;
        }
    }

    /**
     * 计算公历事件结束时间戳
     * @param year 年份
     * @param month 月份（1-12）
     * @param day 日期
     * @return 结束时间戳（毫秒）
     */
    public static long calculateSolarEventEndTime(int year, int month, int day) {
        return getCalendarTimeInMillis(year, month - 1, day);
    }

    /**
     * 生成指定日期时间的时间戳
     * 该方法用于创建一个指定年月日的时间戳，并将时间设置为当天的最后一刻（23:59:59）

     *
     * @param year  年份
     * @param month 月份（0-11），0代表一月，11代表十二月
     * @param day   日期，例如：1-31之间（根据月份不同会有差异）
     * @return 时间戳（毫秒）
     */
    private static long getCalendarTimeInMillis(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, 23, 59, 59);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    /**
     * 获取活动剩余时间的本地化字符串
     * @return 剩余时间字符串
     */
    public static String getRemainingTime() {
        if (eventEndTime == 0) {
            return Messages.get(Gregorian.class, "no_activity");
        }

        long currentTime = System.currentTimeMillis();
        if (currentTime >= eventEndTime) {
            return Messages.get(Gregorian.class, "end_activity");
        }

        long timeDiff = eventEndTime - currentTime;
        return formatTimeDiff(timeDiff);
    }

    /**
     * 格式化时间差为易读的字符串
     * @param timeDiff 时间差（毫秒）
     * @return 格式化后的时间字符串
     */
    private static String formatTimeDiff(long timeDiff) {
        long totalSeconds = timeDiff / 1000;
        long days = totalSeconds / 86400;
        long remainingSeconds = totalSeconds % 86400;

        long hours = remainingSeconds / 3600;
        remainingSeconds %= 3600;

        long minutes = remainingSeconds / 60;
        long seconds = remainingSeconds % 60;

        if (days > 1) {
            return String.format(
                    Messages.get(Gregorian.class, "remaining_days"),
                    days
            );
        } else {
            return String.format(
                    Messages.get(Gregorian.class, "remaining_full"),
                    hours, minutes, seconds
            );
        }
    }
}
