package com.shatteredpixel.shatteredpixeldungeon.custom;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.Null;
import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.custom.testmode.BuffGenerator;
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
import com.watabou.utils.DeviceCompat;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class Gift implements Bundlable {
    private static final String Local_Gift = "";
    private static final String Local_File_Gift_Path = "";
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

    private static final class ItemInfo {
        final String itemName;
        final int quantity;
        final boolean shouldDoPickUp;

        ItemInfo( String itemName ) {
            this.itemName = itemName;
            this.quantity = 1;
            this.shouldDoPickUp = false;
        }

        ItemInfo( String itemName, int quantity ) {
            this.itemName = itemName;
            this.quantity = quantity;
            this.shouldDoPickUp = false;
        }

        ItemInfo( String itemName, boolean shouldDoPickUp) {
            this.itemName = itemName;
            this.quantity = 1;
            this.shouldDoPickUp = shouldDoPickUp;
        }

        ItemInfo( String itemName, int quantity, boolean shouldDoPickUp ) {
            this.itemName = itemName;
            this.quantity = quantity;
            this.shouldDoPickUp = shouldDoPickUp;
        }
    }

    private static final class BuffInfo {
        final String buffName;
        final float duration;

        BuffInfo( String buffName ) {
            this.buffName = buffName;
            this.duration = 1f;
        }

        BuffInfo( String buffName, float duration) {
            this.buffName = buffName;
            this.duration = duration;
        }
    }

    private static final HashMap<String, ArrayList<ItemInfo>> GIFT_ITEM = new HashMap<>();
    private static final HashMap<String, ArrayList<BuffInfo>> GIFT_BUFF = new HashMap<>();
    static {
        ArrayList<ItemInfo> code1 = new ArrayList<>();
        code1.add( new ItemInfo("com.shatteredpixel.shatteredpixeldungeon.items.IceCyanBlueSquareCoin", 300,true) );
        code1.add( new ItemInfo("com.shatteredpixel.shatteredpixeldungeon.items.quest.LanFireGo", 1) );
        ArrayList<ItemInfo> code2 = new ArrayList<>();
        code2.add( new ItemInfo("com.shatteredpixel.shatteredpixeldungeon.items.IceCyanBlueSquareCoin", 1200,true) );
        ArrayList<ItemInfo> code3 = new ArrayList<>();
        code3.add( new ItemInfo("com.shatteredpixel.shatteredpixeldungeon.items.IceCyanBlueSquareCoin", 400,true) );
        code3.add( new ItemInfo("com.shatteredpixel.shatteredpixeldungeon.items.bags.PropBag", 1) );
        code3.add( new ItemInfo("com.shatteredpixel.shatteredpixeldungeon.items.props.LuckyGlove", 1) );
        ArrayList<ItemInfo> code4 = new ArrayList<>();
        code4.add( new ItemInfo("com.shatteredpixel.shatteredpixeldungeon.items.IceCyanBlueSquareCoin", 1000,true) );
        code4.add( new ItemInfo("com.shatteredpixel.shatteredpixeldungeon.items.bags.PropBag", 1) );
        code4.add( new ItemInfo("com.shatteredpixel.shatteredpixeldungeon.items.props.CloakFragmentsOfBzmdr", 1) );
        ArrayList<ItemInfo> code5 = new ArrayList<>();
        code5.add( new ItemInfo("com.shatteredpixel.shatteredpixeldungeon.items.IceCyanBlueSquareCoin", 600,true) );
        code5.add( new ItemInfo("com.shatteredpixel.shatteredpixeldungeon.items.bags.PropBag", 1) );
        code5.add( new ItemInfo("com.shatteredpixel.shatteredpixeldungeon.items.props.NewStem", 1) );
        GIFT_ITEM.put( "QmFkTGFudGVyRmlyZS1Hbw==",         code1 );
        GIFT_ITEM.put( "56uv5Y2I5a6J5bq3",                 code2 );
        GIFT_ITEM.put( "5ZCJ56Wl6ZSm6bKk",                 code3 );
        GIFT_ITEM.put( "TUhKSA==",                 code4 );
        GIFT_ITEM.put( "WUxHQg==",                 code5 );

        ArrayList<ItemInfo> code6 = new ArrayList<>();
        code6.add( new ItemInfo("com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfGolems", 1) );
        code6.add( new ItemInfo("com.shatteredpixel.shatteredpixeldungeon.items.IceCyanBlueSquareCoin", 1000,true) );
        code6.add( new ItemInfo("com.shatteredpixel.shatteredpixeldungeon.items.food.fantong.ZakoSoup", 1) );
        GIFT_ITEM.put( "U0hQRC1CSVJUSERBWQ==",         code6 );

        ArrayList<ItemInfo> code9 = new ArrayList<>();
        code9.add( new ItemInfo("com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfGolems", 1) );
        code9.add( new ItemInfo("com.shatteredpixel.shatteredpixeldungeon.items.IceCyanBlueSquareCoin", 1000,true) );
        code9.add( new ItemInfo("com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfUpgrade", 1) );
        GIFT_ITEM.put( "UHJvcHNGaXhlZA==",         code9 );

        //KPL
        ArrayList<ItemInfo> kpl1 = new ArrayList<>();
        kpl1.add( new ItemInfo("com.shatteredpixel.shatteredpixeldungeon.items.JAmulet", 1) );
        kpl1.add( new ItemInfo("com.shatteredpixel.shatteredpixeldungeon.items.props.ConfusedMieMieTalisman", 1) );
        kpl1.add( new ItemInfo("com.shatteredpixel.shatteredpixeldungeon.items.props.NewStem", 1) );
        kpl1.add( new ItemInfo("com.shatteredpixel.shatteredpixeldungeon.items.props.ArmorScalesOfBzmdr", 1) );
        GIFT_ITEM.put( "bGl0dGxlIHN1cnByaXNlIG9mIGJ6bWRy",         kpl1 );

        ArrayList<ItemInfo> kpl2 = new ArrayList<>();
        kpl2.add( new ItemInfo("com.shatteredpixel.shatteredpixeldungeon.items.quest.BlessingNecklace$AnkhAlt", 1) );
        kpl2.add( new ItemInfo("com.shatteredpixel.shatteredpixeldungeon.items.props.ConfusedMieMieTalisman", 1) );
        kpl2.add( new ItemInfo("com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfUpgrade", 1) );
        kpl2.add( new ItemInfo("com.shatteredpixel.shatteredpixeldungeon.items.props.TheGriefOfSpeechless", 1) );
        GIFT_ITEM.put( "UmVkRmlzaCBCb21iIEdpZnRz",         kpl2 );


        ArrayList<ItemInfo> code8 = new ArrayList<>();
        code8.add( new ItemInfo("com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfGolems", 3) );
        code8.add( new ItemInfo("com.shatteredpixel.shatteredpixeldungeon.items.IceCyanBlueSquareCoin", 1010,true) );
        code8.add( new ItemInfo("com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfUpgrade", 2) );
        GIFT_ITEM.put( "Q2hpbmFCaXJ0aERheQ==",         code8 );

        ArrayList<ItemInfo> code7 = new ArrayList<>();
        code7.add( new ItemInfo("com.shatteredpixel.shatteredpixeldungeon.items.IceCyanBlueSquareCoin", 900,true) );
        code7.add( new ItemInfo("com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfUpgrade", 2) );
        GIFT_ITEM.put( "TklORU5JTkVaRVJPT05F",         code7 );

        ArrayList<ItemInfo> code10 = new ArrayList<>();
        code10.add( new ItemInfo("com.shatteredpixel.shatteredpixeldungeon.items.IceCyanBlueSquareCoin", 900,true) );
        code10.add( new ItemInfo("com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfUpgrade", 1) );
        code10.add( new ItemInfo("com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfGolems", 1) );
        code10.add( new ItemInfo("com.shatteredpixel.shatteredpixeldungeon.items.food.fantong.ZakoSoup", 1) );
        GIFT_ITEM.put("SEVMTE8tTUxQRC1WMC45",code10);

        ArrayList<ItemInfo> code11 = new ArrayList<>();
        code11.add( new ItemInfo("com.shatteredpixel.shatteredpixeldungeon.items.IceCyanBlueSquareCoin", 720,true) );
        code11.add( new ItemInfo("com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfUpgrade", 2) );
        code11.add( new ItemInfo("com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfGolems", 2) );
        code11.add( new ItemInfo("com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.spdtomlpd.Break", 1) );
        code11.add( new ItemInfo("com.shatteredpixel.shatteredpixeldungeon.items.quest.BlessingNecklace$AnkhAlt", 1) );
        GIFT_ITEM.put("TWVycnlDaHJpc3RtYXM=",code11);

        ArrayList<ItemInfo> code12 = new ArrayList<>();
        code12.add( new ItemInfo("com.shatteredpixel.shatteredpixeldungeon.items.IceCyanBlueSquareCoin", 400,true) );
        code12.add( new ItemInfo("com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfUpgrade", 1) );
        GIFT_ITEM.put("WUFNZXJyeUNocmlzdG1hcw==",code12);

        ArrayList<ItemInfo> code13 = new ArrayList<>();
        code13.add( new ItemInfo("com.shatteredpixel.shatteredpixeldungeon.items.IceCyanBlueSquareCoin", 600,true) );
        code13.add( new ItemInfo("com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfUpgrade", 1) );
        code13.add( new ItemInfo("com.shatteredpixel.shatteredpixeldungeon.items.quest.RandomChest", 5) );
        GIFT_ITEM.put("UHJlLTVZZWFyc09sZA==",code13);

        ArrayList<ItemInfo> code14 = new ArrayList<>();
        code14.add( new ItemInfo("com.shatteredpixel.shatteredpixeldungeon.items.IceCyanBlueSquareCoin", 520,true) );
        code14.add( new ItemInfo("com.shatteredpixel.shatteredpixeldungeon.items.quest.RandomChest", 3) );
        code14.add( new ItemInfo("com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfSun", 1) );
        GIFT_ITEM.put("Rml2ZVllYXJzT2xk",code14);

        ArrayList<ItemInfo> code15 = new ArrayList<>();
        code15.add( new ItemInfo("com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHaste", 3) );
        GIFT_ITEM.put("U3BlZWRGaXhlZA==",code15);
    }

    private static int GIFT_Code = 0;
    private static int GIFT_Expiration_Date = 1;
    private static int Gift_Min_Version_Code = 2;
    private static int Gift_Used = 3;

    @Override
    public void storeInBundle(Bundle bundle) {

    }

    @Override
    public void restoreFromBundle(Bundle bundle) {

    }

    //将兑换码导入本地数据中
    public static void GiftTime() {
        saveJsonGift( getLocalGift() );
        saveJsonGift( getLocalFileGift() );
        saveJsonGift( getNetworkedGift() );

        try {
            String decodedString;
            byte[] decoded;
            List<String> saveData = new ArrayList<>();
            long currentTime = System.currentTimeMillis() / 1000;
            long expirationDate;
            long minVersionCode;
            String[] keyStruct;

            for( String data : Gift_DATA ) {
                decoded = Base64.decode( data );
                decodedString = new String( decoded) ;
                keyStruct = decodedString.split(",");

                minVersionCode = keyStruct.length > 3 ? Long.parseLong( keyStruct[2] ) : 0;
                if( Game.versionCode < minVersionCode )
                    continue;

                expirationDate = Long.parseLong( keyStruct[1] );
                if( currentTime > expirationDate )
                    continue;

                if( SPDSettings.queryGiftExist( keyStruct[0] ) )
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

    private static JsonValue getLocalFileGift() {
        try{
            FileHandle fileHandle = Gdx.files.internal( Local_File_Gift_Path );
            if( !fileHandle.exists() )
                return null;

            String jsonContent = new String( Base64.decode( fileHandle.readString("UTF-8") ) );
            JsonReader jsonReader = new JsonReader();

            return jsonReader.parse(jsonContent);
        } catch (Exception e) {
            return null;
        }
    }

    private static JsonValue getLocalGift() {
        try{
            if( Local_Gift.isEmpty() )
                return null;

            String jsonContent = new String( Base64.decode( Local_Gift ) );
            JsonReader jsonReader = new JsonReader();

            return jsonReader.parse(jsonContent);
        } catch (Exception e) {
            return null;
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

    private static void saveJsonGift( JsonValue jsonValue ) {
        try{
            if( jsonValue == null || jsonValue.isEmpty() )
                return;

            for (JsonValue codeValue : jsonValue) {
                ArrayList<ItemInfo> itemReward = new ArrayList<>();
                ArrayList<BuffInfo> buffReward = new ArrayList<>();
                String giftcode = codeValue.getString("giftcode");
                long timestamp = codeValue.getLong("timestamp");
                long minVersionCode = codeValue.getLong("min_versionCode");

                if (codeValue.has("rewardItems")) {
                    for (JsonValue itemValue : codeValue.get("rewardItems")) {
                        String itemName = itemValue.getString("name");
                        int quantity = itemValue.getInt("quantity");
                        if (itemValue.has("shouldDoPickUp")) {
                            itemReward.add( new ItemInfo( itemName, quantity, itemValue.getBoolean("shouldDoPickUp") ) );
                        } else {
                            itemReward.add( new ItemInfo( itemName, quantity ) );
                        }
                    }
                }

                if (codeValue.has("rewardBuffs")) {
                    for (JsonValue itemValue : codeValue.get("rewardBuffs")) {
                        String buffName = itemValue.getString("name");
                        int duration = itemValue.getInt("duration");
                        buffReward.add( new BuffInfo( buffName, duration ) );
                    }
                }

                String jsonData = giftcode + "," + timestamp + "," + minVersionCode + ",false";
                GIFT_ITEM.put( Base64.encodeBytes( giftcode.getBytes() ), itemReward );
                GIFT_BUFF.put( Base64.encodeBytes( giftcode.getBytes() ), buffReward );
                Gift_DATA.add( Base64.encodeBytes( jsonData.getBytes() ) );
            }
        } catch (Exception ignored) {
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
        boolean shouldCheckGameVersion = ( !part2.equals( part3 ) ) && ( part2.matches("-?\\d+") );
        boolean keyUsed = shouldCheckGameVersion ? Boolean.parseBoolean( part3 ) : Boolean.parseBoolean( part2 );

        if( shouldCheckGameVersion )
            if( Game.versionCode < Long.parseLong( part2 ) )
                return 6;

        if( keyUsed )
            return 3;

        String keyCheck = Base64.encodeBytes( key.getBytes() );
        if( GIFT_ITEM.containsKey( keyCheck ) ){
            ArrayList<ItemInfo> itemInfo = GIFT_ITEM.get( keyCheck );
            for ( ItemInfo entry : itemInfo ) {
                GiveItem( entry.itemName, entry.quantity, entry.shouldDoPickUp );
            }
        }
        if( GIFT_BUFF.containsKey( keyCheck ) ){
            ArrayList<BuffInfo> buffInfos = GIFT_BUFF.get( keyCheck );
            for ( BuffInfo entry : buffInfos ) {
                GiveBuff( entry.buffName, entry.duration );
            }
        }

        if( !DeviceCompat.isDebug() && !DeviceCompat.isMidTest() )
            SPDSettings.modifyGiftPart( key, shouldCheckGameVersion ? 3 : 2, String.valueOf(true) );

        return 1;
    }

    //存储Buff
    private static void GiveBuff( String buffName, float buffDuration ){
        GLog.i( Messages.get( Gift.class, "buff", Messages.get( Gift.class, buffName ) ) );
        try {
            new BuffGenerator().AffectBuff( hero, Class.forName( buffName ), buffDuration );
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    //给予物品
    private static void GiveItem( String itemName, int quantity, boolean shouldDoPickUp ){
        boolean collect = false;
        Item item = null;
        try {
            item = (Item) Reflection.newInstance(Class.forName(itemName));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        if(Challenges.isItemBlocked(item)) return;
        if (item != null) {
            if( shouldDoPickUp ) {
                GLog.i( Messages.get( Gift.class, "you_now_have", item.name(), quantity ));
                item.quantity(quantity).doPickUp(hero);
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
}
