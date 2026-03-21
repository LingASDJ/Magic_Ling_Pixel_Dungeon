package com.shatteredpixel.shatteredpixeldungeon.custom;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.items.IceCyanBlueSquareCoin;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.TitleScene;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.Reflection;

import net.iharder.Base64;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class Gift implements Bundlable {

    public static final String KEY_ARRAY	= "TUxQRFpFUk8sSEVMTE9aRVJPRUlHSFQ=";
    private static final List<String> Gift_DATA = new ArrayList<>();
    static {
        //【永久兑换码】
        Gift_DATA.add("QmFkTGFudGVyRmlyZS1Hbyw0MDcwOTUxNzc1LGZhbHNl");
        Gift_DATA.add("TUhKSCw0MDcwOTUxNzc1LGZhbHNl");
        Gift_DATA.add("WUxHQiw0MDcwOTUxNzc1LGZhbHNl");
        Gift_DATA.add("TklORU5JTkVaRVJPT05FLDQwNzA5NTE3NzUsZmFsc2U=");
        Gift_DATA.add("SEVMTE8tTUxQRC1WMC45LDQwNzA5NTE3NzUsZmFsc2U=");

        Gift_DATA.add("UHJvcHNGaXhlZCw0MDcwOTUxNzc1LGZhbHNl");

        //KPL 永久赌注兑换码
        Gift_DATA.add("bGl0dGxlIHN1cnByaXNlIG9mIGJ6bWRyLDQwNzA5NTE3NzUsZmFsc2U=");
        Gift_DATA.add("UmVkRmlzaCBCb21iIEdpZnRzLDQwNzA5NTE3NzUsZmFsc2U=");

        Gift_DATA.add("U0hQRC1CSVJUSERBWSwxNzU2MTM3NjIwLGZhbHNl");

        Gift_DATA.add("TkZZSUcsMTc1NDU4MjQwMCxmYWxzZQ==");
        Gift_DATA.add("QVJNWURBWSwxNzU0NTgyNDAwLGZhbHNl");
        Gift_DATA.add("TWVycnlDaHJpc3RtYXMsMTc2NzE5NjgwMCxmYWxzZQ==");

        Gift_DATA.add("WUFNZXJyeUNocmlzdG1hcywxNzY3MTk2ODAwLGZhbHNl");

        //GQJ 国庆节
        Gift_DATA.add("Q2hpbmFCaXJ0aERheSwxNzU5ODU2NDQ5LGZhbHNl");

        //2026
        Gift_DATA.add("UHJlLTVZZWFyc09sZCwxNzcwOTA0NzYwLGZhbHNl");

        Gift_DATA.add("Rml2ZVllYXJzT2xkLDE3NzI1NTM2MDAsZmFsc2U=");
        Gift_DATA.add("U3BlZWRGaXhlZCwxNzcyNTUzNjAwLGZhbHNl");
    }

    private static final HashMap<String, LinkedHashMap<String, Integer>> GIFT_ITEM ;
    static {
        HashMap<String, LinkedHashMap<String, Integer>> tempMap = new HashMap<>();

        LinkedHashMap<String, Integer> code1 = new LinkedHashMap<>();
        code1.put( "com.shatteredpixel.shatteredpixeldungeon.items.IceCyanBlueSquareCoin", 300 );
        code1.put( "com.shatteredpixel.shatteredpixeldungeon.items.quest.LanFireGo", 1 );
        LinkedHashMap<String, Integer> code2 = new LinkedHashMap<>();
        code2.put( "com.shatteredpixel.shatteredpixeldungeon.items.IceCyanBlueSquareCoin", 1200 );
        LinkedHashMap<String, Integer> code3 = new LinkedHashMap<>();
        code3.put( "com.shatteredpixel.shatteredpixeldungeon.items.IceCyanBlueSquareCoin", 400 );
        code3.put( "com.shatteredpixel.shatteredpixeldungeon.items.bags.PropBag", 1 );
        code3.put( "com.shatteredpixel.shatteredpixeldungeon.items.props.LuckyGlove", 1 );
        LinkedHashMap<String, Integer> code4 = new LinkedHashMap<>();
        code4.put( "com.shatteredpixel.shatteredpixeldungeon.items.IceCyanBlueSquareCoin", 1000 );
        code4.put( "com.shatteredpixel.shatteredpixeldungeon.items.bags.PropBag", 1 );
        code4.put( "com.shatteredpixel.shatteredpixeldungeon.items.props.CloakFragmentsOfBzmdr", 1 );
        LinkedHashMap<String, Integer> code5 = new LinkedHashMap<>();
        code5.put( "com.shatteredpixel.shatteredpixeldungeon.items.IceCyanBlueSquareCoin", 600 );
        code5.put( "com.shatteredpixel.shatteredpixeldungeon.items.bags.PropBag", 1 );
        code5.put( "com.shatteredpixel.shatteredpixeldungeon.items.props.NewStem", 1 );
        tempMap.put( "QmFkTGFudGVyRmlyZS1Hbw==",         code1 );
        tempMap.put( "56uv5Y2I5a6J5bq3",                 code2 );
        tempMap.put( "5ZCJ56Wl6ZSm6bKk",                 code3 );
        tempMap.put( "TUhKSA==",                 code4 );
        tempMap.put( "WUxHQg==",                 code5 );

        LinkedHashMap<String, Integer> code6 = new LinkedHashMap<>();
        code6.put( "com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfGolems", 1 );
        code6.put( "com.shatteredpixel.shatteredpixeldungeon.items.IceCyanBlueSquareCoin", 1000 );
        code6.put( "com.shatteredpixel.shatteredpixeldungeon.items.food.fantong.ZakoSoup", 1 );
        tempMap.put( "U0hQRC1CSVJUSERBWQ==",         code6 );

        LinkedHashMap<String, Integer> code9 = new LinkedHashMap<>();
        code9.put( "com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfGolems", 1 );
        code9.put( "com.shatteredpixel.shatteredpixeldungeon.items.IceCyanBlueSquareCoin", 1000 );
        code9.put( "com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfUpgrade", 1 );
        tempMap.put( "UHJvcHNGaXhlZA==",         code9 );

        //KPL
        LinkedHashMap<String, Integer> kpl1 = new LinkedHashMap<>();
        kpl1.put( "com.shatteredpixel.shatteredpixeldungeon.items.JAmulet", 1 );
        kpl1.put( "com.shatteredpixel.shatteredpixeldungeon.items.props.ConfusedMieMieTalisman", 1 );
        kpl1.put( "com.shatteredpixel.shatteredpixeldungeon.items.props.NewStem", 1 );
        kpl1.put( "com.shatteredpixel.shatteredpixeldungeon.items.props.ArmorScalesOfBzmdr", 1 );
        tempMap.put( "bGl0dGxlIHN1cnByaXNlIG9mIGJ6bWRy",         kpl1 );

        LinkedHashMap<String, Integer> kpl2 = new LinkedHashMap<>();
        kpl2.put( "com.shatteredpixel.shatteredpixeldungeon.items.quest.BlessingNecklace$AnkhAlt", 1 );
        kpl2.put( "com.shatteredpixel.shatteredpixeldungeon.items.props.ConfusedMieMieTalisman", 1 );
        kpl2.put( "com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfUpgrade", 1 );
        kpl2.put( "com.shatteredpixel.shatteredpixeldungeon.items.props.TheGriefOfSpeechless", 1 );
        tempMap.put( "UmVkRmlzaCBCb21iIEdpZnRz",         kpl2 );


        LinkedHashMap<String, Integer> code8 = new LinkedHashMap<>();
        code8.put( "com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfGolems", 3 );
        code8.put( "com.shatteredpixel.shatteredpixeldungeon.items.IceCyanBlueSquareCoin", 1010 );
        code8.put( "com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfUpgrade", 2 );
        tempMap.put( "Q2hpbmFCaXJ0aERheQ==",         code8 );

        LinkedHashMap<String, Integer> code7 = new LinkedHashMap<>();
        code7.put( "com.shatteredpixel.shatteredpixeldungeon.items.IceCyanBlueSquareCoin", 900 );
        code7.put( "com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfUpgrade", 2 );
        tempMap.put( "TklORU5JTkVaRVJPT05F",         code7 );

        LinkedHashMap<String, Integer> code10 = new LinkedHashMap<>();
        code10.put( "com.shatteredpixel.shatteredpixeldungeon.items.IceCyanBlueSquareCoin", 900 );
        code10.put( "com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfUpgrade", 1 );
        code10.put( "com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfGolems", 1 );
        code10.put( "com.shatteredpixel.shatteredpixeldungeon.items.food.fantong.ZakoSoup", 1 );
        tempMap.put("SEVMTE8tTUxQRC1WMC45",code10);

        LinkedHashMap<String, Integer> code11 = new LinkedHashMap<>();
        code11.put( "com.shatteredpixel.shatteredpixeldungeon.items.IceCyanBlueSquareCoin", 720 );
        code11.put( "com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfUpgrade", 2 );
        code11.put( "com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfGolems", 2 );
        code11.put( "com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.spdtomlpd.Break", 1 );
        code11.put( "com.shatteredpixel.shatteredpixeldungeon.items.quest.BlessingNecklace$AnkhAlt", 1 );
        tempMap.put("TWVycnlDaHJpc3RtYXM=",code11);

        LinkedHashMap<String, Integer> code12 = new LinkedHashMap<>();
        code12.put( "com.shatteredpixel.shatteredpixeldungeon.items.IceCyanBlueSquareCoin", 400 );
        code12.put( "com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfUpgrade", 1 );
        tempMap.put("WUFNZXJyeUNocmlzdG1hcw==",code12);

        LinkedHashMap<String, Integer> code13 = new LinkedHashMap<>();
        code13.put( "com.shatteredpixel.shatteredpixeldungeon.items.IceCyanBlueSquareCoin", 600 );
        code13.put( "com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfUpgrade", 1 );
        code13.put( "com.shatteredpixel.shatteredpixeldungeon.items.quest.RandomChest", 5 );
        tempMap.put("UHJlLTVZZWFyc09sZA==",code13);

        LinkedHashMap<String, Integer> code14 = new LinkedHashMap<>();
        code14.put( "com.shatteredpixel.shatteredpixeldungeon.items.IceCyanBlueSquareCoin", 520 );
        code14.put( "com.shatteredpixel.shatteredpixeldungeon.items.quest.RandomChest", 3 );
        code14.put( "com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfSun", 1 );
        tempMap.put("Rml2ZVllYXJzT2xk",code14);

        LinkedHashMap<String, Integer> code15 = new LinkedHashMap<>();
        code15.put( "com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHaste", 3 );
        tempMap.put("U3BlZWRGaXhlZA==",code15);

        GIFT_ITEM = new HashMap<>( Collections.unmodifiableMap( tempMap ) );
    }

    private static final LinkedHashMap<String,Integer> giftBuffArray = new LinkedHashMap<>();
    private static final String giftBuffArrayKey = "gift_buff_array_key";
    private static final String giftBuffArrayValue = "gift_buff_array_value";

    private static int GIFT_Code = 0;
    private static int GIFT_Expiration_Date = 1;
    private static int Gift_Used = 2;

    @Override
    public void storeInBundle(Bundle bundle) {
        String[] giftArrayKey = new String[giftBuffArray.size()];
        int[] giftArrayValue = new int[giftBuffArray.size()];

        int i = 0;
        for ( Map.Entry<String, Integer> entry : giftBuffArray.entrySet() ) {
            giftArrayKey[i] = entry.getKey();
            giftArrayValue[i] = entry.getValue();
            i++;
        }

        bundle.put( giftBuffArrayKey, giftArrayKey );
        bundle.put( giftBuffArrayValue, giftArrayValue );
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        ArrayList<String> giftBuffArrayKeys = new ArrayList<>();
        ArrayList<Integer> giftBuffArrayValues = new ArrayList<>();

        for ( String key : bundle.getStringArray( giftBuffArrayKey ) ){
            giftBuffArrayKeys.add( key );
        }
        for ( int value : bundle.getIntArray( giftBuffArrayValue ) ){
            giftBuffArrayValues.add( value );
        }
        for ( int i = 0; i < giftBuffArrayKeys.size(); i++ ){
            giftBuffArray.put( giftBuffArrayKeys.get( i ), giftBuffArrayValues.get( i ) );
        }
    }

    //将兑换码导入本地数据中
    public static void GiftTime() {
        JsonValue redeemCodes = getNetworkedGift();
        if ( redeemCodes != null && redeemCodes.notEmpty()) {
            for (JsonValue codeValue : redeemCodes) {
                LinkedHashMap<String, Integer> reward = new LinkedHashMap<>();
                String giftcode = codeValue.getString("giftcode");
                long timestamp = codeValue.getLong("timestamp");
                long minVersionCode = codeValue.getLong("min_versionCode");

                if (codeValue.has("rewardItems")) {
                    for (JsonValue itemValue : codeValue.get("rewardItems")) {
                        String itemName = itemValue.getString("name");
                        int quantity = itemValue.getInt("quantity");
                        reward.put(itemName, quantity);
                    }
                }

                if (codeValue.has("rewardBuffs")) {
                    for (JsonValue itemValue : codeValue.get("rewardItems")) {
                        String itemName = itemValue.getString("name");
                        int quantity = itemValue.getInt("duration");
                        reward.put(itemName, quantity);
                    }
                }

                String networkedData = giftcode + "," + timestamp + "," + minVersionCode + ",false";
                GIFT_ITEM.put(Base64.encodeBytes(giftcode.getBytes()), reward);
                Gift_DATA.add(Base64.encodeBytes(networkedData.getBytes()));
            }
        }

        try {
            String decodedString;
            byte[] decoded;
            List<String> saveData = new ArrayList<>();
            long currentTime = System.currentTimeMillis() / 1000;
            long expirationDate;

            for( String data : Gift_DATA ) {
                decoded = Base64.decode( data );
                decodedString = new String( decoded) ;

                expirationDate = Long.parseLong( decodedString.split(",")[1] );
                if( currentTime > expirationDate )
                    continue;

                if( SPDSettings.queryGiftExist( decodedString.split(",")[0] ) )
                    continue;

                saveData.add( decodedString );
            }

            if( !saveData.isEmpty() ){
                String[] result = new String[saveData.size()];
                SPDSettings.saveGift( saveData.toArray( result ) );
            }

            SPDSettings.deleteOutdatedGift();
        } catch (Exception ignored) {
        }
    }

    private static JsonValue getNetworkedGift() {
        if( TitleScene.NTP_NOINTER || TitleScene.NTP_ERROR || TitleScene.NTP_NOINTER_VEFY || TitleScene.NTP_ERROR_VEFY )
            return null;

        try {
            TrustManager[] trustAllCerts = new TrustManager[] {
                    new X509TrustManager() {
                        public X509Certificate[] getAcceptedIssuers() {
                            return null;
                        }
                        public void checkClientTrusted(X509Certificate[] certs, String authType) {
                        }
                        public void checkServerTrusted(X509Certificate[] certs, String authType) {
                        }
                    }
            };

            // 安装全信任的TrustManager
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            // 创建不验证主机名的HostnameVerifier
            HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);

            URL url = new URL("https://gameupdate.insrv.mlpd.spldream.com/MLPD/gift.json");
            URLConnection conn = url.openConnection();
            conn.setRequestProperty("User-Agent", "Mozilla/5.0");

            conn.connect();

            InputStream inputStream = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            String jsonContent = sb.toString();
            reader.close();
            inputStream.close();

            JsonReader jsonReader = new JsonReader();
            JsonValue jsonValue = jsonReader.parse(jsonContent);

            return jsonValue.get("redeem_codes");
        } catch (Exception e) {
            return null;
        }
    }

    //玩家使用兑换码
    public static int ActivateGift(String key) {
        if( TitleScene.NTP_NOINTER || TitleScene.NTP_ERROR || TitleScene.NTP_NOINTER_VEFY || TitleScene.NTP_ERROR_VEFY )
            return 0;

        if(Objects.equals(key, "")){
            return 4;
        }

        if( !SPDSettings.queryGiftExist( key ) )
            return 5;

        long currentTime = System.currentTimeMillis() / 1000;
        long expirationDate = Long.parseLong( SPDSettings.queryGiftPart( key, GIFT_Expiration_Date ) );
        if( currentTime > expirationDate )
            return 2;

        String part2 = SPDSettings.queryGiftPart( key, 2 );
        String part3 = SPDSettings.queryGiftPart( key, 3 );
        boolean keyUsed;

        if( !part2.equals( part3 ) ){
            if( Game.versionCode < Long.parseLong( part2 ) )
                return 6;

            keyUsed = Boolean.parseBoolean( part3 );
        }else {
            keyUsed = Boolean.parseBoolean( part2 );
        }

        if( keyUsed )
            return 3;

        String keyCheck = Base64.encodeBytes( key.getBytes() );
        if( GIFT_ITEM.containsKey( keyCheck ) ){
            LinkedHashMap<String, Integer> items = GIFT_ITEM.get( keyCheck );
            for ( Map.Entry<String, Integer> entry : items.entrySet() ) {
                if( entry.getKey().contains("GiftBuff") )
                        GiveBuff( entry.getKey(), entry.getValue() );
                else
                    GiveItem( entry.getKey(), entry.getValue() );
            }
        }

        SPDSettings.modifyGiftPart( key, Gift_Used, String.valueOf(true) );
        return 1;
    }

    //存储Buff
    private static void GiveBuff(String buffKey, int buffValue){
        GLog.i( Messages.get( Gift.class, "buff", Messages.get( Gift.class, buffKey ) ) );
        giftBuffArray.put( buffKey, buffValue);
    }

    //给予物品
    private static void GiveItem(String itemName,int quantity){
        boolean collect = false;
        Item item = null;
        try {
            item = (Item) Reflection.newInstance(Class.forName(itemName));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        if(Challenges.isItemBlocked(item)) return;
        if (item != null) {
            //若为咕币
            if(item instanceof IceCyanBlueSquareCoin) {
                GLog.i( Messages.get( Gift.class, "you_now_have", item.name(), quantity ));
                new IceCyanBlueSquareCoin(quantity).doPickUp(hero);
                return;
            }

            if(item.stackable)
                collect = item.quantity(quantity).collect();
            else
                collect = item.collect();
            item.identify();

            if(collect){
                GLog.i( Messages.get( Gift.class, "you_now_have", item.name(), quantity ));
                Sample.INSTANCE.play( Assets.Sounds.ITEM );
                GameScene.pickUp( item, hero.pos );
            }else{
                item.doDrop(hero);
            }
        }
    }

    //Buff是否存在
    public static boolean IsGiftBuffExist( String key ){
        return giftBuffArray.containsKey( key );
    }

    //Buff状态，-1为不存在或其他
    public static int GetGiftBuffStatus( String key ){
        if( !IsGiftBuffExist( key ) )
            return -1;

        return giftBuffArray.get( key );
    }

    public static String base64() {
        String decodedString = "";
        // 初始为空字符串
        try {
            // 解码Base64字符串
            byte[] decoded = Base64.decode(KEY_ARRAY);
            // 这里的KEY_ARRAY应该是一个Base64编码的字符串
            decodedString = new String(decoded);
            // 解码后的字节数组转换为字符串
        } catch (Exception ignored) {
        }
        return decodedString;  // 返回解码后的字符串
    }

}
