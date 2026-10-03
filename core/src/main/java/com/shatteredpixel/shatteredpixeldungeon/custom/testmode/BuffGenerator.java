package com.shatteredpixel.shatteredpixeldungeon.custom.testmode;

import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.*;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.custom.messages.M;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.BuffScanner;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIcon;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.ScrollPane;
import com.shatteredpixel.shatteredpixeldungeon.ui.ScrollingGridPane;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.utils.WndTextNumberInput;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.utils.Bundle;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;

public class BuffGenerator extends TestItem{
    {
        image = ItemSpriteSheet.DEV_3;
        defaultAction = AC_BUFF_TARGET;
    }

    private static final String AC_BUFF_SET = "buff_set";
    private static final String AC_BUFF_TARGET = "buff_target";
    private static final String AC_BUFF_CLEAN = "buff_clean";

    private BitSet buffsStatus = new BitSet();
    private int duration = 1;

    @FunctionalInterface
    private interface Function<T>{
        void func(Char obj, float duration);
    }

    private static final Map<Class<?>,Function<?>> functions = new HashMap<>();

    
    {
        functions.put( AdrenalineSurge.class, (Char ch,float duration) -> Buff.affect( ch, AdrenalineSurge.class ).reset(1, duration ) );
    }

    //数据源：BuffScanner 全量扫描 + 效果百科同款过滤
    private final ArrayList<BuffEntry> availableBuffs = new ArrayList<>();

    private static class BuffEntry {
        Class<? extends Buff> clazz;
        Buff instance;
        int iconID;
        String title;
        String desc;
    }

    private void buildBuffList() {
        availableBuffs.clear();
        ArrayList<Class<? extends Buff>> scanned = BuffScanner.getTestBuffClasses();
        for (Class<? extends Buff> buffClass : scanned) {
            Buff buff;
            try {
                buff = buffClass.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                continue;
            }

            int iconID;
            String title, desc;
            try {
                iconID = buff.icon();
                title = Messages.titleCase(buff.name());
                desc = buff.desc();
            } catch (Exception e) {
                continue;
            }

            //效果百科同款过滤：无图标占位、缺翻译的跳过
            if (iconID == 68) continue;
            if (title.contains("Ms") || desc.contains("Ms")) continue;

            BuffEntry entry = new BuffEntry();
            entry.clazz = buffClass;
            entry.instance = buff;
            entry.iconID = iconID;
            entry.title = title;
            entry.desc = desc;
            availableBuffs.add(entry);
        }
    }

    private CellSelector.Listener buff_target_selector = new CellSelector.Listener() {
        @Override
        public void onSelect( Integer cell ) {
            if( cell == null ) return;

            Char ch = Actor.findChar( cell );
            if( ch == null ) {
                GLog.w(M.L(WndSetBuff.class, "no_char"));
            }else {
                for (int i = buffsStatus.nextSetBit(0 ); i >= 0; i = buffsStatus.nextSetBit(i + 1 ) ) {
                    if (i >= availableBuffs.size()) continue;
                    Class buffClass = availableBuffs.get(i).clazz;
                    AffectBuff( ch, buffClass, duration);
                }
            }
        }

        @Override
        public String prompt() {
            return M.L( WndSetBuff.class, "select" );
        }
    };

    private CellSelector.Listener buff_clean_selector = new CellSelector.Listener() {
        @Override
        public void onSelect( Integer cell ) {
            if( cell == null ) return;

            Char ch = Actor.findChar( cell );
            if( ch == null )
                GLog.w( M.L( WndSetBuff.class, "no_char" ) );
            else
                CleanBuff(ch);
        }

        @Override
        public String prompt() {
            return M.L( WndSetBuff.class, "select" );
        }
    };

    public void CleanBuff( Char ch ){
        for ( Buff b : ch.buffs() ){
            if ( !( b instanceof AllyBuff )
                    && !( b instanceof LostInventory ) ){
                b.detach();
            }
            if ( b instanceof Hunger ){
                ( ( Hunger ) b ).satisfy( Hunger.STARVING );
            }
        }
        ch.venodamage = 0;
    }

    @SuppressWarnings("unchecked")
    public  <T> void AffectBuff( Char ch, Class buffClass, float duration ) {
        Function<T> function = (Function<T>) functions.get( buffClass );
        if( function != null ) {
            function.func( ch, duration );
        }else if ( FlavourBuff.class.isAssignableFrom( buffClass ) ) {
            Buff.affect( ch, buffClass, duration );
        } else {
            Buff.affect( ch, buffClass );
        }
    }

    @Override
    public ArrayList<String> actions( Hero hero ) {
        ArrayList<String> actions = super.actions( hero );
        actions.add( AC_BUFF_SET );
        actions.add( AC_BUFF_TARGET );
        actions.add( AC_BUFF_CLEAN );
        return actions;
    }

    @Override
    public void execute( Hero hero, String action ) {
        super.execute( hero, action );
        if( action.equals( AC_BUFF_TARGET ) ){
            GameScene.selectCell( buff_target_selector );
        }else if( action.equals(AC_BUFF_SET)){
            GameScene.show( new WndSetBuff() );
        }else if( action.equals( AC_BUFF_CLEAN ) ){
            GameScene.selectCell( buff_clean_selector );
        }
    }

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( "buffDuration", duration );

        int[] storeStatus = new int[ buffsStatus.cardinality() ];
        for( int i = buffsStatus.nextSetBit(0), j = 0;i >= 0; i = buffsStatus.nextSetBit(i + 1 ), j++ )
            storeStatus[ j ] = i;
        bundle.put("storeStatus", storeStatus );

    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        duration = bundle.getInt("buffDuration" );

        int[] storeStatus = bundle.getIntArray("storeStatus" );
        for(int i = 0;i < storeStatus.length; i++)
            buffsStatus.set( storeStatus[ i ] );
    }

    private class WndSetBuff extends Window {

        private static final int WIDTH = 150;
        private static final int HEIGHT = 180;
        private static final int GAP = 2;
        private static final int TOP_BAR_H = 21;
        private static final int DESC_AREA_H = 32;

        //选中高亮与默认底色（效果百科网格默认底 0x9953564D）
        private static final float SEL_R = 0.35f, SEL_G = 0.85f, SEL_B = 0.45f;
        private static final float DEF_R = 0.325f, DEF_G = 0.337f, DEF_B = 0.302f;

        private final ScrollingGridPane grid = new ScrollingGridPane();
        private final Map<Integer, ScrollingGridPane.GridItem> itemByIndex = new HashMap<>();

        private final ArrayList<Integer> positiveBuffs = new ArrayList<>();
        private final ArrayList<Integer> negativeBuffs = new ArrayList<>();
        private final ArrayList<Integer> neutralBuffs = new ArrayList<>();

        private final RedButton modifyDuration;
        private final RedButton clearButton;
        private final RenderedTextBlock selectedCount;
        private final RenderedTextBlock descText;
        private final ScrollPane descPane;

        public WndSetBuff(){
            super();
            resize(WIDTH, HEIGHT);

            //数据源：BuffScanner 全量扫描（效果百科同款过滤规则）
            buildBuffList();
            classifyBuffs();

            modifyDuration = new RedButton(Messages.get(WndSetBuff.class, "modify_duration",duration), 7) {
                @Override
                protected void onClick() {
                    Game.runOnRenderThread(() ->GameScene.show(new WndTextNumberInput(
                            Messages.get(WndSetBuff.class, "custom_title"),
                            Messages.get(WndSetBuff.class, "duration_desc"),
                            Integer.toString(duration),
                            2, false, Messages.get(WndSetBuff.class, "confirm"),
                            Messages.get(WndSetBuff.class, "cancel"),false) {
                        @Override
                        public void onSelect(boolean check, String text) {
                            if ( check && text.matches("\\d+") ) {
                                int value = Integer.parseInt( text );
                                if( value > 0 ) {
                                    duration = Math.min( value, 99 );
                                    modifyDuration.text(Messages.get(WndSetBuff.class, "modify_duration",duration));
                                }
                            }
                        }
                    }));
                }
            };
            add(modifyDuration);

            clearButton = new RedButton(Messages.get(WndSetBuff.class, "clear_all"), 7) {
                @Override
                protected void onClick() {
                    super.onClick();
                    buffsStatus.clear();
                    for (ScrollingGridPane.GridItem item : itemByIndex.values()) {
                        item.hardLightBG(DEF_R, DEF_G, DEF_B);
                    }
                    updateCount();
                    descText.text("");
                    descPane.scrollTo(0, 0);
                }
            };
            add(clearButton);

            selectedCount = PixelScene.renderTextBlock("", 7);
            PixelScene.align(selectedCount);
            add(selectedCount);

            descText = PixelScene.renderTextBlock("", 4);
            descText.maxWidth(WIDTH - 2 * GAP);
            descPane = new ScrollPane(descText) {
                //放宽访问权限：切换描述后需要主动重新布局以更新滚动范围与滑块
                @Override
                public void layout() {
                    super.layout();
                }
            };
            add(descPane);

            buildGrid();
            layout();
            updateCount();
        }

        private void buildGrid(){
            grid.clear();
            itemByIndex.clear();

            grid.addHeader(Messages.get(this, "total_buffs", availableBuffs.size()), 9, true);

            if (!positiveBuffs.isEmpty()) {
                grid.addHeader("_" + Messages.get(this, "title_positive") + "_ (" + positiveBuffs.size() + ")", 7, false);
                for (int idx : positiveBuffs) addBuffItem(idx);
            }
            if (!negativeBuffs.isEmpty()) {
                grid.addHeader("_" + Messages.get(this, "title_negative") + "_ (" + negativeBuffs.size() + ")", 7, false);
                for (int idx : negativeBuffs) addBuffItem(idx);
            }
            if (!neutralBuffs.isEmpty()) {
                grid.addHeader("_" + Messages.get(this, "title_neutral") + "_ (" + neutralBuffs.size() + ")", 7, false);
                for (int idx : neutralBuffs) addBuffItem(idx);
            }
            add(grid);
        }

        private void addBuffItem(final int index){
            BuffEntry entry = availableBuffs.get(index);

            //效果百科同款：按图标ID构建 BuffIcon，再用反射做 tint（异常忽略，避免个别buff导致窗口打不开）
            BuffIcon icons = new BuffIcon(entry.iconID, false);
            try {
                Method tintMethod = entry.clazz.getMethod("tintIcon", Image.class);
                tintMethod.invoke(entry.instance, icons);
            } catch (Exception ignored) {
            }

            ScrollingGridPane.GridItem item = new ScrollingGridPane.GridItem(icons) {
                @Override
                public boolean onClick(float x, float y) {
                    if (inside(x, y)) {
                        toggleBuff(index);
                        return true;
                    }
                    return false;
                }
            };
            if (buffsStatus.get(index)) {
                item.hardLightBG(SEL_R, SEL_G, SEL_B);
            }
            itemByIndex.put(index, item);
            grid.addItem(item);
        }

        private void toggleBuff(int index){
            BuffEntry entry = availableBuffs.get(index);
            ScrollingGridPane.GridItem item = itemByIndex.get(index);
            if (buffsStatus.get(index)) {
                buffsStatus.clear(index);
                item.hardLightBG(DEF_R, DEF_G, DEF_B);
            } else {
                buffsStatus.set(index);
                item.hardLightBG(SEL_R, SEL_G, SEL_B);
            }
            descText.text(entry.title + "\n\n" + entry.desc);
            descPane.scrollTo(0, 0);
            updateCount();
        }

        private void updateCount(){
            selectedCount.text(Messages.get(this, "selected_buffs", buffsStatus.cardinality(), availableBuffs.size()));
            selectedCount.setPos((WIDTH - selectedCount.width())/2f, modifyDuration.bottom() + GAP);
        }

        private void layout(){
            modifyDuration.setRect(0, GAP, WIDTH / 2f - GAP / 2f, TOP_BAR_H);
            clearButton.setRect(WIDTH / 2f + GAP / 2f, GAP, WIDTH / 2f - GAP / 2f, TOP_BAR_H);

            float countBottom = modifyDuration.bottom() + GAP + 12;
            descPane.setRect(GAP, HEIGHT - DESC_AREA_H, WIDTH - 2 * GAP, DESC_AREA_H + GAP);
            grid.setRect(0, countBottom, WIDTH, descPane.top() - countBottom);
        }

        //效果百科同款分类统计
        private void classifyBuffs(){
            positiveBuffs.clear();
            negativeBuffs.clear();
            neutralBuffs.clear();
            for (int i = 0; i < availableBuffs.size(); i++){
                Buff buff = availableBuffs.get(i).instance;
                if (buff.type == Buff.buffType.POSITIVE) positiveBuffs.add(i);
                else if (buff.type == Buff.buffType.NEGATIVE) negativeBuffs.add(i);
                else neutralBuffs.add(i);
            }
        }
    }
}
