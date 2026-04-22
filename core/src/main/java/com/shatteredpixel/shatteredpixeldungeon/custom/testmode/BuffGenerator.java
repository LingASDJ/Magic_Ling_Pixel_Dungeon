package com.shatteredpixel.shatteredpixeldungeon.custom.testmode;

import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.*;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ClearBleesdGoodBuff.BlessAnmy;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ClearBleesdGoodBuff.BlessBossRushLow;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ClearBleesdGoodBuff.BlessGoRead;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ClearBleesdGoodBuff.BlessGoodSTR;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ClearBleesdGoodBuff.BlessImmune;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ClearBleesdGoodBuff.BlessLing;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ClearBleesdGoodBuff.BlessLingJing;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ClearBleesdGoodBuff.BlessMixShiled;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ClearBleesdGoodBuff.BlessMobDied;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ClearBleesdGoodBuff.BlessNoDied;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ClearBleesdGoodBuff.BlessNoMoney;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ClearBleesdGoodBuff.BlessQinyue;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ClearBleesdGoodBuff.BlessRedWhite;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ClearBleesdGoodBuff.BlessUnlock;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ClearBleesdGoodBuff.ClearLanterBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ElementalBuff.BaseBuff.ScaryBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ElementalBuff.DamageBuff.ScaryDamageBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ElementalBuff.Immunities.ScaryImmunitiesBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicGirlDebuff.MagicGirlSayCursed;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicGirlDebuff.MagicGirlSayKill;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicGirlDebuff.MagicGirlSayMoneyMore;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicGirlDebuff.MagicGirlSayNoSTR;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicGirlDebuff.MagicGirlSaySlowy;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicGirlDebuff.MagicGirlSayTimeLast;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.status.DragonWall;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.status.FoundChest;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.status.NightorDay;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.status.OozeStatueDead;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.status.QuestGold;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.custom.messages.M;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfSirensSong;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.CheckBox;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.utils.WndTextNumberInput;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndMessage;
import com.watabou.noosa.Game;
import com.watabou.utils.Bundle;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
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
    private int currentPage = 1;

    @FunctionalInterface
    private interface Function<T>{
        void func(Char obj, float duration);
    }

    private static final Map<Class<?>,Function<?>> functions = new HashMap<>();

    //带有自定义回合函数/继承自Buff类的方法应写在这里
    {
        functions.put( AdrenalineSurge.class, (Char ch,float duration) -> Buff.affect( ch, AdrenalineSurge.class ).reset(1, duration ) );
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
                    Class buffClass = allData.get(i);
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
        bundle.put( "currentPage", currentPage );

        int[] storeStatus = new int[ buffsStatus.cardinality() ];
        for( int i = buffsStatus.nextSetBit(0), j = 0;i >= 0; i = buffsStatus.nextSetBit(i + 1 ), j++ )
            storeStatus[ j ] = i;
        bundle.put("storeStatus", storeStatus );

    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        duration = bundle.getInt("buffDuration" );
        currentPage = bundle.getInt("currentPage" );

        int[] storeStatus = bundle.getIntArray("storeStatus" );
        for(int i = 0;i < storeStatus.length; i++)
            buffsStatus.set( storeStatus[ i ] );
    }

    private class WndSetBuff extends Window {

        private static final int WIDTH = 180;
        private static final int HEIGHT = 280;
        private static final int BTN_SIZE = 18;
        private static final int GAP = 2;

        private int columPerPage = 9;
        private int maxPage = allData.size() / columPerPage;
        private ArrayList<CheckBox> buffButtons = new ArrayList<>(columPerPage + 1 );
        private RenderedTextBlock selectedPage;
        private RedButton modifyDuration;
        private RenderedTextBlock buffText = PixelScene.renderTextBlock(8);
        private RenderedTextBlock descText = PixelScene.renderTextBlock(6);

        // 新增过滤后的数据缓存
        private ArrayList<Class<?>> filteredData = new ArrayList<>();
        private ArrayList<Class<?>> filteredBuffs = new ArrayList<>();
        // 核心过滤方法
        private void filterBuffs() {
            filteredBuffs.clear();
            for (Class<?> buffClass : allData) {
                String buffName = M.L(buffClass, "name");
                if (!buffName.startsWith("Ms:")) {
                    filteredBuffs.add(buffClass);
                }
            }
        }

        public WndSetBuff(){
            super();

            //过略Ms:开头的buff
            filterBuffs();
            //计算过略的分页参数
            maxPage = (int) Math.ceil((double) filteredBuffs.size() / columPerPage);

            resize(WIDTH, HEIGHT);

            int pos = 110;

            RedButton lhs = new RedButton(Messages.get(WndSetBuff.class,"last_page"), 6){
                @Override
                public void onClick(){
                    updateFilteredData();
                    currentPage--;
                    if(currentPage < 1 || currentPage>maxPage){
                        currentPage = maxPage;
                    }
                    updateBuffButtons();
                    updateSelectedPage();
                }
            };
            lhs.setRect(GAP, GAP, 24, 18);
            add(lhs);

            RedButton rhs = new RedButton(Messages.get(WndSetBuff.class,"next_page"), 6){
                @Override
                public void onClick(){
                    currentPage++;
                    updateFilteredData();
                    if(currentPage < 1 || currentPage >maxPage){
                        currentPage = 1;
                    }
                    updateBuffButtons();
                    updateSelectedPage();
                }
            };
            rhs.setRect(WIDTH - 24 - GAP,  GAP, 24, 18);
            add(rhs);

            buffText.setPos(lhs.left(), lhs.bottom() + 2 * GAP);
            buffText.maxWidth(WIDTH - 2 * GAP);  // 添加自动换行
            buffText.hardlight(Window.TITLE_COLOR);
            add(buffText);

            // 修改后的代码段
            descText.setPos(0, 0);
            descText.maxWidth(WIDTH - 2 * GAP - 4); // 为滚动条保留空间
            descText.setPos(lhs.left(), buffText.bottom() + 6 * GAP);
            add(descText);

            selectedPage = PixelScene.renderTextBlock("", 9);
            PixelScene.align(selectedPage);
            add(selectedPage);

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
            modifyDuration.setRect((WIDTH/3f)/6f-8, 90, (WIDTH/3f - GAP), 16);
            add(modifyDuration);

            RedButton clearButton = new RedButton(Messages.get(WndSetBuff.class, "clear_all",duration)) {
                @Override
                protected void onClick() {
                    super.onClick();
                    buffsStatus.clear();
                    updateBuffButtons();
                }
            };
            clearButton.setRect(modifyDuration.right() + GAP+5, 90, 109, 16);
            add(clearButton);

            int column = 0;
            for (int i = 0; i < columPerPage && column < 3; ++i) {
                int temp = i;
                CheckBox cb = new CheckBox(M.L(allData.get(temp + ( currentPage - 1 ) * columPerPage), "name")){
                    @Override
                    protected void onClick() {
                        super.onClick();
                        int finalI = temp + ( currentPage - 1 ) * columPerPage;
                        descText.text(M.L(allData.get(finalI), "desc"));
                        buffText.text(M.L(allData.get(finalI), "name"));
                        if (checked) {
                            buffsStatus.set(finalI);
                        } else {
                            buffsStatus.clear(finalI);
                        }
                    }

                    @Override
                    protected boolean onLongClick() {
                        int finalI = temp + ( currentPage - 1 ) * columPerPage;
                        GameScene.show( new WndMessage( M.L(allData.get(finalI), "desc") ) ) ;
                        return super.onLongClick();
                    }
                };
                cb.active = true;
                add(cb);
                buffButtons.add(cb);

                float Radius = 2.8f;

                if (column == 0) {
                    cb.setRect(GAP, pos, 58, 16);
                } else if (column == 1) {
                    cb.setRect(column * 58 + GAP, pos, 58, 16);
                }else{
                    cb.setRect(column * 58 + GAP, pos, 58, 16);
                    column = -1; // 重置column的值，使其在下一次循环时为0（即第一列）
                    pos += 16 + GAP; // 换行
                }

                if(i==16){
                    cb.setRect((WIDTH/Radius - GAP)/Radius * 2+81, 90, (WIDTH/3f - GAP), 16);
                }

                column++;
            }
            resize(WIDTH, (int) (buffButtons.get(buffButtons.size()-1).bottom()+GAP));

            updateBuffButtons();
            updateSelectedPage();
            updateFilteredData();
        }

        private void updateBuffButtons(){
            // 清空现有UI元素
            for (CheckBox cb : buffButtons) {
                remove(cb);
            }
            buffButtons.clear();

            // 计算分页参数

            // 基于过滤后列表创建按钮
            int startIdx = (currentPage - 1) * columPerPage;
            int endIdx = Math.min(startIdx + columPerPage, filteredBuffs.size());

            int pos = 110;
            int column = 0;

            // 动态生成CheckBox
            for (int i = startIdx; i < endIdx; i++) {
                Class<?> buffClass = filteredBuffs.get(i);
                CheckBox cb = createCheckBox(buffClass, i);

                descText.text(M.L(buffClass, "desc"));
                buffText.text(M.L(buffClass, "name"));


                // 布局逻辑
                int Base = 58;
                if (column == 0) {
                    cb.setRect(GAP, pos, Base, 16);
                } else if (column == 1) {
                    cb.setRect(Base + GAP, pos, Base, 16);
                } else {
                    cb.setRect(Base*2 + GAP, pos, Base, 16);
                    column = -1;
                    pos += 16 + GAP;
                }
                column++;

                add(cb);
                buffButtons.add(cb);
            }

            // 调整窗口高度
            if (!buffButtons.isEmpty()) {
                resize(WIDTH, (int) (buffButtons.get(buffButtons.size()-1).bottom() + GAP));
            } else {
                resize(WIDTH, HEIGHT);
            }
        }
        private void updateFilteredData() {
            filteredData.clear();
            for (Class<?> buffClass : allData) {
                String name = M.L(buffClass, "name");
                if (!name.startsWith("Ms:")) {
                    filteredData.add(buffClass);
                }
            }
        }

        private CheckBox createCheckBox(Class<?> buffClass, int filteredIndex) {
            CheckBox cb = new CheckBox(M.L(buffClass, "name"),6) {
                @Override
                protected void onClick() {
                    super.onClick();
                    int originalIndex = allData.indexOf(buffClass);
                    buffsStatus.set(originalIndex, checked);
                }

                @Override
                protected boolean onLongClick() {
                    int originalIndex = allData.indexOf(buffClass);
                    GameScene.show( new WndMessage( M.L(allData.get(originalIndex), "desc") ) ) ;
                    return super.onLongClick();
                }

            };
            cb.checked(buffsStatus.get(allData.indexOf(buffClass)));
            return cb;
        }

        private void updateSelectedPage(){
            maxPage = (int) Math.ceil(filteredBuffs.size() / (double) columPerPage);
            selectedPage.text(Messages.get(WndSetBuff.class, "selected_page",currentPage,maxPage));
            selectedPage.maxWidth(WIDTH / 2);
            selectedPage.setPos((WIDTH - selectedPage.width())/2, 5);
        }
    }

    private static final List<Class> allData = new ArrayList<>();
    static {
        allData.add(Adrenaline.class);
        allData.add(AdrenalineSurge.class);
        allData.add(Amok.class);
        allData.add(AntiLightShiled.class);
        allData.add(ArcaneArmor.class);
        allData.add(ArtifactRecharge.class);
        allData.add(AscensionChallenge.class);
        allData.add(AutoRandomBuff.class);

        allData.add(Barkskin.class);
        allData.add(Barrier.class);
        allData.add(BeamTowerAdbility.class);

        allData.add(Bleeding.class);
        allData.add(Bless.class);
        allData.add(Blindness.class);
        allData.add(BlobImmunity.class);
        allData.add(BrokenArmor.class);
        allData.add(Burning.class);

        allData.add(Charm.class);
        allData.add(Chill.class);
        allData.add(Combo.class);
        allData.add(Corrosion.class);
        allData.add(Corruption.class);


        allData.add(Cripple.class);

        allData.add(Daze.class);
        allData.add(DeadSoul.class);
        allData.add(Degrade.class);
        allData.add(Doom.class);
        allData.add(Dread.class);
        allData.add(Drowsy.class);
        allData.add(EnhancedRings.class);
        allData.add(FireImbue.class);
        allData.add(FlavourBuff.class);
        allData.add(Foresight.class);
        allData.add(Frost.class);
        allData.add(FrostBurning.class);
        allData.add(FrostImbue.class);
        allData.add(FrostImbueEX.class);
        allData.add(Fury.class);
        allData.add(GoodLuck.class);
        allData.add(GravityChaosTracker.class);
        allData.add(GreaterHaste.class);
        allData.add(HaloFireImBlue.class);
        allData.add(HalomethaneBurning.class);
        allData.add(Haste.class);
        allData.add(HasteLing.class);
        allData.add(Healing.class);
        allData.add(HeroDisguise.class);
        allData.add(Hex.class);
        allData.add(HoldFast.class);
        allData.add(Hunger.class);
        allData.add(Invisibility.class);
        allData.add(InvisibilityRing.class);
        allData.add(Invulnerability.class);
        allData.add(Killer.class);

        allData.add(LethalDefense.class);
        allData.add(Levitation.class);
        allData.add(LifeLink.class);
        allData.add(LighS.class);
        allData.add(Light.class);

        allData.add(LockedFloor.class);
        allData.add(LostInventory.class);
        allData.add(MagicalSight.class);
        allData.add(MagicalSleep.class);
        allData.add(MagicImmune.class);
        //allData.add(Marked.class);
        allData.add(MindVision.class);

        allData.add(MonkEnergy.class);
        allData.add(Nyctophobia.class);
        allData.add(Ooze.class);
        allData.add(Paralysis.class);
        allData.add(PhysicalEmpower.class);
        allData.add(PinCushion.class);
        allData.add(Poison.class);
        allData.add(Preparation.class);
        allData.add(PrismaticGuard.class);
        allData.add(PropBuff.class);

        allData.add(Recharging.class);

        allData.add(ReloadShop.class);
        allData.add(ReloadShopTwo.class);
        allData.add(RevealedArea.class);
        allData.add(Roots.class);
        allData.add(RoseShiled.class);

        allData.add(ScrollEmpower.class);

        allData.add(Shadows.class);


        allData.add(Slow.class);
        allData.add(SmokeAlly.class);
        allData.add(Smoking.class);
        allData.add(SnipersMark.class);
        allData.add(SoulMark.class);

        allData.add(Stamina.class);
        allData.add(StormCloudDied.class);
        allData.add(SunFire.class);

        allData.add(Terror.class);

        allData.add(ToxicImbue.class);
        allData.add(TrueInvisibiity.class);
        allData.add(Venom.class);
        allData.add(Vertigo.class);
        allData.add(Vulnerable.class);
        allData.add(WandEmpower.class);
        allData.add(WaterSoulX.class);
        allData.add(Weakness.class);
        allData.add(WellFed.class);
        allData.add(WorstBlizzard.class);

        //ClearBleesdGoodBuff
        allData.add(BlessAnmy.class);
        allData.add(BlessBossRushLow.class);
        allData.add(BlessGoodSTR.class);
        allData.add(BlessGoRead.class);
        allData.add(BlessImmune.class);
        allData.add(BlessLing.class);
        allData.add(BlessLingJing.class);
        allData.add(BlessMixShiled.class);
        allData.add(BlessMobDied.class);
        allData.add(BlessNoDied.class);
        allData.add(BlessNoMoney.class);
        allData.add(BlessQinyue.class);
        allData.add(BlessRedWhite.class);
        allData.add(BlessUnlock.class);
        allData.add(ClearLanterBuff.class);

        //ElementalBuff
        allData.add(ScaryBuff.class);
        allData.add(ScaryDamageBuff.class);
        allData.add(ScaryImmunitiesBuff.class);

        //MagicGirlDebuff
        //allData.add((NO)MagicGirlSaySoftDied.class);
        allData.add(MagicGirlSayCursed.class);
        allData.add(MagicGirlSayKill.class);
        allData.add(MagicGirlSayMoneyMore.class);
        allData.add(MagicGirlSayNoSTR.class);
        allData.add(MagicGirlSaySlowy.class);
        allData.add(MagicGirlSayTimeLast.class);

        allData.add(DragonWall.class);
        allData.add(FoundChest.class);
        allData.add(NightorDay.class);
        allData.add(OozeStatueDead.class);
        allData.add(QuestGold.class);

        //SP
        allData.add(ScrollOfSirensSong.Enthralled.class);
    }
}
