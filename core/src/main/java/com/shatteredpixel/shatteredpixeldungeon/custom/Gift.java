package com.shatteredpixel.shatteredpixeldungeon.custom;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Gift {
    // 定义礼包码和其状态
    private static final Map<String, Boolean> giftCodes = new HashMap<>();

    static {
        // 初始化礼包码和状态，false表示未兑换
        giftCodes.put("UPDATE1", false);
        giftCodes.put("UPDATE2", false);
        giftCodes.put("UPDATE3", false);
        giftCodes.put("UPDATE4", false);
        giftCodes.put("UPDATE5", false);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("可用礼包码: " + giftCodes.keySet());
            System.out.print("请输入兑换码，或输入'退出'以结束: ");

            String userInput = scanner.nextLine().trim();

            if (userInput.equalsIgnoreCase("退出")) {
                System.out.println("退出程序.");
                break;
            }

            String result = redeemGiftCode(userInput);
            System.out.println(result);
        }

        scanner.close();
    }

    private static String redeemGiftCode(String code) {
        if (!giftCodes.containsKey(code)) {
            return "无效的礼包码";
        }

        if (giftCodes.get(code)) {
            return "该礼包码已被兑换";
        }

        // 标记为已兑换
        giftCodes.put(code, true);
        return "礼包码 " + code + " 兑换成功";
    }
}