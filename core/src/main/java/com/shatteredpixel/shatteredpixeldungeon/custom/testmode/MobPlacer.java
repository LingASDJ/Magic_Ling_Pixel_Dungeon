package com.shatteredpixel.shatteredpixeldungeon.custom.testmode;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Boss;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ChampionEnemy;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.GnollGeomancer;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.DwarfGeneral;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.hollow.DeadDogCerberus;
import com.shatteredpixel.shatteredpixeldungeon.custom.messages.M;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.journal.Bestiary;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MobSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.RatSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIcon;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.CheckBox;
import com.shatteredpixel.shatteredpixeldungeon.ui.IconButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.ScrollPane;
import com.shatteredpixel.shatteredpixeldungeon.ui.ScrollingGridPane;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.utils.WndTextNumberInput;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndMessage;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.ui.Component;
import com.watabou.utils.Bundle;
import com.watabou.utils.Reflection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

public class MobPlacer extends TestItem{
    {
        image = ItemSpriteSheet.DEV_5;
        defaultAction = AC_PLACE;
    }

    private static final String AC_PLACE = "place";
    private static final String AC_SET = "set";

    private int mobTier = 1;
    private int mobIndex = 0;
    private int elite = 0;

    private boolean shouldOverride = false;
    private int HT = 1;
    private int maxPage = 24;
    private int ST = 1;
    private int elite_op = 0;
    // 怪物阵营变量，默认敌人
    private Char.Alignment mobAlign = Char.Alignment.ENEMY;

    private final ArrayList<Class<? extends ChampionEnemy>> eliteBuffs = new ArrayList<>();
    {
        eliteBuffs.add(ChampionEnemy.Blazing.class);
        eliteBuffs.add(ChampionEnemy.AntiMagic.class);
        eliteBuffs.add(ChampionEnemy.Blessed.class);
        eliteBuffs.add(ChampionEnemy.Giant.class);
        eliteBuffs.add(ChampionEnemy.Growing.class);
        eliteBuffs.add(ChampionEnemy.Projecting.class);
        eliteBuffs.add(ChampionEnemy.Halo.class);
        eliteBuffs.add(ChampionEnemy.DelayMob.class);
        eliteBuffs.add(ChampionEnemy.King.class);

        eliteBuffs.add(ChampionEnemy.Small.class);
        eliteBuffs.add(ChampionEnemy.Bomber.class);
        eliteBuffs.add(ChampionEnemy.Middle.class);
        eliteBuffs.add(ChampionEnemy.Big.class);
        eliteBuffs.add(ChampionEnemy.Sider.class);
        eliteBuffs.add(ChampionEnemy.LongSider.class);
        eliteBuffs.add(ChampionEnemy.HealRight.class);
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.add(AC_PLACE);
        actions.add(AC_SET);
        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {
        super.execute(hero, action);
        if (action.equals(AC_PLACE)) {
            GameScene.selectCell(new CellSelector.Listener() {
                @Override
                public void onSelect(final Integer cell) {
                    if(cell != null){
                        if (canPlaceMob(cell)) {
                            try {
                                Mob m = Reflection.newInstance(getMobClass());
                                m.pos = cell;
                                m.state = m.HUNTING;
                                // 赋值阵营
                                m.alignment = mobAlign;
                                GameScene.add(m);

                                if( HT > 0 && shouldOverride ){
                                    m.HT = m.HP = HT;
                                }

                                if(elite_op>0){
                                    for(int i=0;i<17;++i){
                                        if((elite_op & (1<<i))>0){
                                            Buff.affect(m, eliteBuffs.get(i));
                                        }
                                    }
                                }
                                ScrollOfTeleportation.appear(m, cell);
                                Dungeon.level.occupyCell(m);
                            } catch (Exception e) {
                                ShatteredPixelDungeon.reportException(e);
                            }
                        }else{
                            GLog.w(M.L(MobPlacer.class, "forbidden"));
                        }
                    }
                    curUser.next();
                }
                @Override
                public String prompt() {
                    return M.L(MobPlacer.class, "prompt");
                }
            });

        } else if (action.equals(AC_SET)) {
            GameScene.show(new WndSetMob());
        }
    }

    private boolean canPlaceMob(int cell){
        return Actor.findChar(cell) == null && (!Dungeon.level.solid[cell] || Dungeon.level.map[cell] == Terrain.DOOR || Dungeon.level.map[cell] == Terrain.OPEN_DOOR);
    }

    protected int maxMobIndex(int tier){
        return allData.get( tier - 1 ).size();
    }

    private Class<? extends Mob> getMobClass(){
        return allData.get( mobTier - 1 ).get( mobIndex );
    }

    private Class<? extends Mob> getMobClass( int index ){
        return allData.get( mobTier - 1 ).get( index );
    }

    @Override
    public void storeInBundle(Bundle b){
        super.storeInBundle(b);
        b.put("mobTier", mobTier);
        b.put("mobIndex", mobIndex);
        b.put("eliteTags", elite);
        b.put("htTags", HT);
        b.put("stTags", ST);
        b.put("elite_ops", elite_op);
        b.put("mob_shouldOverride",shouldOverride);
        // 保存阵营
        b.put("mobAlign", mobAlign.name());
    }

    @Override
    public void restoreFromBundle(Bundle b){
        super.restoreFromBundle(b);
        mobTier = b.getInt("mobTier");
        mobIndex = b.getInt("mobIndex");
        elite = b.getInt("eliteTags");
        HT = b.getInt("htTags");
        ST = b.getInt("stTags");
        elite_op = b.getInt("elite_ops");
        shouldOverride = b.getBoolean("mob_shouldOverride");
        if (b.contains("mobAlign")){
            String alignStr = b.getString("mobAlign");
            if (alignStr != null && !alignStr.isEmpty()){
                mobAlign = Char.Alignment.valueOf(alignStr);
            } else {
                mobAlign = Char.Alignment.ENEMY;
            }
        } else {
            mobAlign = Char.Alignment.ENEMY;
        }
    }

    private class WndSetMob extends Window{
        @Override
        public void hide() {
            super.hide();
            if (parent != null) parent.remove(this);
        }

        // 窗口自适应尺寸，加宽适配大控件
        private static final int WND_WIDTH = 200;
        private static final int WND_HEIGHT = 180;
        private static final int LEFT_PANEL_WIDTH = 55;

        // 三区分割高度比例
        private static final float MOB_GRID_RATIO = 0.35f;
        private static final float ELITE_GRID_RATIO = 0.45f;
        private static final float SETTING_GRID_RATIO = 0.2f;
        private static final int FUNCTION_GRID_WIDTH = 50;
        private static final int FUNCTION_GRID_HEIGHT = 26;

        private RedButton btnPrevPage, btnNextPage;
        private RenderedTextBlock txtPageIndicator;
        private RenderedTextBlock txtSelectedMobName;

        // 三个独立网格面板
        private ScrollingGridPane mobGridPane;
        private ScrollingGridPane eliteGridPane;    // 精英词条独立滚动区
        private ScrollingGridPane settingGridPane;  // 血量调整独立滚动区

        private ArrayList<MobGridItem> mobIconItems = new ArrayList<>();
        private ArrayList<EliteCheckItem> eliteCheckItems = new ArrayList<>();
        private RedButton btnModifyHealth;
        private CheckBox cbOverrideHP;

        // ========== 左侧预览精灵 ==========
        private MobSprite previewSprite;
        private static final int PREVIEW_SIZE = 42;
        // 阵营单选框
        private CheckBox cbEnemy, cbNeutral, cbAlly;

        private RedButton cbInfo;

        private final Image EMPTY_ICON = new Image(new ItemSprite(ItemSpriteSheet.CHALLANEESICON_17+6));

        public WndSetMob(){
            super();
            resize(Game.width > Game.height ? WND_WIDTH : 180, WND_HEIGHT);
            initLeftPanel();
            initTripleGridLayout();
            refreshAllGridContent();
            layout();
            refreshPreviewSprite();
        }

        // 左侧固定面板
        private void initLeftPanel(){
            btnPrevPage = new RedButton("<", 7){
                @Override
                public void onClick(){
                    mobTier--;
                    if(mobTier < 1) mobTier = maxPage;
                    mobIndex = Math.min(mobIndex, maxMobIndex(mobTier)-1);
                    refreshAllGridContent();
                    updatePageText();
                    refreshPreviewSprite();
                }
            };
            btnPrevPage.setRect(5, 15, LEFT_PANEL_WIDTH-10, 24);
            add(btnPrevPage);

            txtPageIndicator = PixelScene.renderTextBlock("", 9);
            txtPageIndicator.hardlight(0xFFFFFF);
            add(txtPageIndicator);

            txtSelectedMobName = PixelScene.renderTextBlock("", 7);
            txtSelectedMobName.hardlight(0xFFFF44);
            txtSelectedMobName.maxWidth(LEFT_PANEL_WIDTH);
            add(txtSelectedMobName);

            btnNextPage = new RedButton(">", 7){
                @Override
                public void onClick(){
                    mobTier++;
                    if(mobTier > maxPage) mobTier = 1;
                    mobIndex = Math.min(mobIndex, maxMobIndex(mobTier)-1);
                    refreshAllGridContent();
                    updatePageText();
                    refreshPreviewSprite();
                }
            };
            btnNextPage.setRect(5, txtSelectedMobName.bottom()+5, LEFT_PANEL_WIDTH-10, 24);
            add(btnNextPage);

            previewSprite = new RatSprite();
            add(previewSprite);

            // 阵营单选框 先全部实例化（不执行checked赋值）
            cbEnemy = new CheckBox("E"){
                @Override
                public void checked(boolean value) {
                    super.checked(value);
                    if (value) {
                        mobAlign = Char.Alignment.ENEMY;
                        if (cbNeutral != null) {
                            cbNeutral.checked(false);
                        }
                        if (cbAlly != null) {
                            cbAlly.checked(false);
                        }
                    }
                }
            };

            cbNeutral = new CheckBox("R"){
                @Override
                public void checked(boolean value) {
                    super.checked(value);
                    if (value) {
                        mobAlign = Char.Alignment.NEUTRAL;
                        if (cbEnemy != null) cbEnemy.checked(false);
                        if (cbAlly != null) cbAlly.checked(false);
                    }
                }
            };

            cbAlly = new CheckBox("A"){
                @Override
                public void checked(boolean value) {
                    super.checked(value);
                    if (value) {
                        mobAlign = Char.Alignment.ALLY;
                        if (cbEnemy != null) cbEnemy.checked(false);
                        if (cbNeutral != null) cbNeutral.checked(false);
                    }
                }
            };


            add(cbEnemy);
            add(cbNeutral);
            add(cbAlly);

            switch (mobAlign) {
                case NEUTRAL:
                    cbNeutral.checked(true);
                    break;
                case ALLY:
                    cbAlly.checked(true);
                    break;
                default:
                    cbEnemy.checked(true);
                    break;
            }

            cbInfo = new RedButton(Messages.get(MobPlacer.class, "era")) {
                @Override
                protected void onClick() {
                    GameScene.show( new WndMessage( Messages.get(MobPlacer.class, "era_desc") ) ) ;
                }
            };
            add(cbInfo);

            updatePageText();
        }

        // 刷新预览怪物精灵
        private void refreshPreviewSprite(){
            remove(previewSprite);
            Mob mobProto = Reflection.newInstance(getMobClass());
            previewSprite = (MobSprite) mobProto.sprite();
            previewSprite.idle();
            add(previewSprite);

            float scale = PREVIEW_SIZE / Math.max(previewSprite.width(), previewSprite.height());
            previewSprite.scale.set(scale, scale);
            previewSprite.x = 5;
            previewSprite.y = btnNextPage.bottom()+5;
            PixelScene.align(previewSprite);
        }

        // 初始化三区域独立网格（怪物区 / 精英词条区 / 血量设置区）
        private void initTripleGridLayout(){
            float rightX = LEFT_PANEL_WIDTH;
            float rightW = 190 - LEFT_PANEL_WIDTH;

            // 1. 上方怪物选择网格
            mobGridPane = new ScrollingGridPane();
            mobGridPane.setCellSize(24,24);
            add(mobGridPane);
            mobGridPane.setRect(rightX, 0, rightW, WND_HEIGHT * MOB_GRID_RATIO);

            // 2. 中间精英词条独立滚动网格
            eliteGridPane = new ScrollingGridPane();
            eliteGridPane.setCellSize(FUNCTION_GRID_WIDTH, FUNCTION_GRID_HEIGHT);
            add(eliteGridPane);
            eliteGridPane.setRect(
                    rightX-15,
                    WND_HEIGHT * MOB_GRID_RATIO,
                    rightW+20,
                    WND_HEIGHT * ELITE_GRID_RATIO
            );

            // 3. 下方血量调整独立滚动网格（第三区域，和精英完全分离）
            settingGridPane = new ScrollingGridPane();
            settingGridPane.setCellSize(65, FUNCTION_GRID_HEIGHT);
            add(settingGridPane);
            settingGridPane.setRect(
                    rightX,
                    WND_HEIGHT * (MOB_GRID_RATIO + ELITE_GRID_RATIO),
                    rightW,
                    WND_HEIGHT * SETTING_GRID_RATIO
            );
        }

        private void updatePageText(){
            txtPageIndicator.text(mobTier + "/" + maxPage);
            txtPageIndicator.setPos(
                    LEFT_PANEL_WIDTH/2f - txtPageIndicator.width()/2f,
                    btnPrevPage.bottom() + 10
            );
            PixelScene.align(txtPageIndicator);

            txtSelectedMobName.text(M.L(getMobClass(), "name"));
            txtSelectedMobName.setPos(
                    LEFT_PANEL_WIDTH/2f - txtSelectedMobName.width()/2f,
                    txtPageIndicator.bottom() + 8
            );
            PixelScene.align(txtSelectedMobName);
        }

        private void refreshAllGridContent(){
            // 清空全部三个网格
            mobGridPane.clear();
            eliteGridPane.clear();
            settingGridPane.clear();
            mobIconItems.clear();
            eliteCheckItems.clear();

            // 1. 上侧怪物图标网格
            mobGridPane.addHeader(Messages.get(WndSetMob.class, "mob_list"), 9, false);
            int mobCount = maxMobIndex(mobTier);
            for(int i=0; i<mobCount; i++){
                Mob mobProto = Reflection.newInstance(getMobClass(i));
                Image mobSprite = mobProto.sprite();
                int finalI = i;
                MobGridItem gridItem = new MobGridItem(mobSprite, i, ()->{
                    mobIndex = finalI;
                    updatePageText();
                    refreshPreviewSprite();
                });
                mobGridPane.addItem(gridItem);
                mobIconItems.add(gridItem);
            }

            // 2. 中间精英词条网格（独立区域，只放精英）
            eliteGridPane.addHeader(Messages.get(WndSetMob.class, "elite_modifier"), 9, false);
            for(int i=0; i<eliteBuffs.size(); i++){
                Class<? extends ChampionEnemy> buffCls = eliteBuffs.get(i);
                boolean checked = (elite_op & (1 << i)) > 0;
                EliteCheckItem checkItem = new EliteCheckItem(buffCls, checked, i);
                eliteGridPane.addItem(checkItem);
                eliteCheckItems.add(checkItem);
            }

            // 3. 下方血量调整网格（第三独立区域，和精英彻底分开滚动）
            settingGridPane.addHeader(Messages.get(WndSetMob.class, "health_setting"), 9, false);
            btnModifyHealth = new RedButton(Messages.get(MobPlacer.class, "modify_health"),8){
                @Override
                protected void onClick() {
                    Game.runOnRenderThread(() -> GameScene.show(new WndTextNumberInput(
                            Messages.get(MobPlacer.class, "custom_title"),
                            Messages.get(MobPlacer.class, "health_desc"),
                            Integer.toString(HT),
                            6, false, Messages.get(MobPlacer.class, "confirm"),
                            Messages.get(MobPlacer.class, "cancel"),false) {
                        @Override
                        public void onSelect(boolean check, String text) {
                            if ( check && text.matches("\\d+") ) {
                                int value = Integer.parseInt( text );
                                if( value >= 0 ) HT = Math.min( value, 666666 );
                            }
                        }
                    }));
                }
            };

            cbOverrideHP = new CheckBox(Messages.get(MobPlacer.class, "override")){
                @Override
                public void checked(boolean value) {
                    super.checked(value);
                    shouldOverride = value;
                    if (btnModifyHealth != null) {
                        btnModifyHealth.enable(value);
                        btnModifyHealth.active = value;
                    }
                }
            };
            cbOverrideHP.checked(shouldOverride);

            // 血量控件全部添加到 settingGridPane（第三区域）
            settingGridPane.addItem(new ComponentWrapperItem(cbOverrideHP));
            btnModifyHealth.enable(shouldOverride);
            btnModifyHealth.active = shouldOverride;
            settingGridPane.addItem(new ComponentWrapperItem(btnModifyHealth));

            // 三个网格分别布局，互不干扰
            mobGridPane.layout();
            eliteGridPane.layout();
            settingGridPane.layout();
        }

        private void syncEliteFlags(){
            int flag = 0;
            for(EliteCheckItem item : eliteCheckItems){
                if(item.checked) flag |= (1 << item.index);
            }
            elite_op = flag;
        }

        @Override
        public void onBackPressed() {
            syncEliteFlags();
            hide();
        }

        private void layout(){
            btnPrevPage.setY(15);
            btnNextPage.setY(txtSelectedMobName.bottom()+5);
            updatePageText();
            refreshPreviewSprite();

            float baseY = previewSprite.y + previewSprite.height() + 3;
            cbEnemy.setRect(5, baseY, LEFT_PANEL_WIDTH - 40, 16);
            cbNeutral.setRect(cbEnemy.right()+2, baseY, LEFT_PANEL_WIDTH - 40, 16);
            cbAlly.setRect(cbNeutral.right()+2, baseY, LEFT_PANEL_WIDTH - 40, 16);
            cbInfo.setRect(5, cbAlly.bottom()+1, LEFT_PANEL_WIDTH-5, 16);
            PixelScene.align(cbEnemy);
            PixelScene.align(cbNeutral);
            PixelScene.align(cbAlly);

            mobGridPane.layout();
            eliteGridPane.layout();
            settingGridPane.layout();
        }

        // 怪物格子Item（无改动）
        private class MobGridItem extends ScrollingGridPane.GridItem {
            private Runnable clickCallback;
            private int mobIdx;
            public MobGridItem(Image sprite, int index, Runnable onClick) {
                super(sprite);
                mobIdx = index;
                clickCallback = onClick;
                float maxDim = Math.max(icon.width(), icon.height());
                if(Game.width > Game.height){
                    icon.scale.set(24 / maxDim * 0.85f, 24 / maxDim * 0.85f);
                } else {
                    icon.scale.set(16 / maxDim * 0.85f, 16 / maxDim * 0.85f);
                }

            }

            @Override
            public boolean onClick(float globalX, float globalY) {
                if (globalX >= this.x && globalX <= this.x + width()
                        && globalY >= this.y && globalY <= this.y + height()) {
                    clickCallback.run();
                    return true;
                }
                return false;
            }
        }

        // 精英词条Item（无改动）
        private class EliteCheckItem extends ScrollingGridPane.GridItem {
            Image icon;
            IconButton infoBtn;
            CheckBox checkBtn;

            public Class<? extends ChampionEnemy> buffClass;
            public boolean checked;
            public int index;
            private int buffColor;

            private String buffName;
            private String buffDesc;

            public EliteCheckItem(Class<? extends ChampionEnemy> cls, boolean isChecked, int idx){
                super(EMPTY_ICON);
                this.buffClass = cls;
                this.index = idx;

                ChampionEnemy buff = Reflection.newInstance(cls);
                buffColor = buff.getColor();
                buffName = buff.toString();
                buffDesc = buff.desc();

                icon = new BuffIcon(buff.icon(),true);
                buff.tintIcon(icon);
                add(icon);

                infoBtn = new IconButton(Icons.get(Icons.INFO)){
                    @Override
                    protected void onClick() {
                        // 移除 measure()，直接创建文本后读取宽高
                        RenderedTextBlock titleTxt = PixelScene.renderTextBlock(buffName, 10);
                        titleTxt.hardlight(buffColor);

                        RenderedTextBlock descTxt = PixelScene.renderTextBlock(buffDesc, 6);
                        descTxt.maxWidth(144);

                        // 固定边距常量
                        int paddingTop = 6;
                        int titleGap = 4;
                        int scrollPaddingTop = 2;
                        int scrollPaddingBottom = 4;
                        int bottomBtnHeight = 26;
                        int winW = 160;
                        int scrollW = 144;
                        int scrollH = (int)(descTxt.height() + scrollPaddingTop + scrollPaddingBottom);
                        int winH = paddingTop + (int)titleTxt.height() + titleGap + scrollH + bottomBtnHeight;
                        // 限制窗口最小/最大高度
                        winH = Math.max(110, Math.min(winH, 280));

                        Window infoWnd = new Window(winW, winH);
                        // 标题位置
                        titleTxt.setRect(8, paddingTop, scrollW, titleTxt.height());
                        infoWnd.add(titleTxt);

                        // 滚动容器
                        Component scrollContent = new Component();
                        ScrollPane scrollPane = new ScrollPane(scrollContent);
                        infoWnd.add(scrollPane);
                        scrollPane.setRect(8, titleTxt.bottom() + titleGap, scrollW, scrollH);


                        descTxt.setPos(0, scrollPaddingTop);
                        scrollContent.add(descTxt);
                        scrollContent.setSize(scrollW, descTxt.height() + scrollPaddingTop + scrollPaddingBottom);

                        // 底部关闭按钮
                        RedButton closeBtn = new RedButton(Messages.get(WndSetMob.class,"close"),8){
                            @Override protected void onClick(){ infoWnd.hide(); }
                        };
                        closeBtn.setRect(10, winH - bottomBtnHeight, winW - 20, 20);
                        infoWnd.add(closeBtn);

                        GameScene.show(infoWnd);
                    }
                };
                add(infoBtn);

                checkBtn = new CheckBox("");
                checkBtn.checked(isChecked);
                checked = isChecked;
                checkBtn = new CheckBox(""){
                    @Override
                    public void checked(boolean value) {
                        super.checked(value);
                        EliteCheckItem.this.checked = value;
                    }
                };
                checkBtn.checked(isChecked);
                add(checkBtn);
            }

            @Override
            protected void layout() {
                float iconSize = BuffIndicator.SIZE_LARGE;
                icon.x = x + (width() - iconSize) / 4f;
                icon.y = y + 2;
                PixelScene.align(icon);

                float iconRight = icon.x + icon.width();
                infoBtn.setRect(iconRight + 2, icon.y, 16, 16);
                PixelScene.align(infoBtn);

                checkBtn.setRect(x + (width() - 16)/2f, y + height() - 18, 16, 16);
                PixelScene.align(checkBtn);
            }
        }

        // 控件包装Item（无改动）
        private class ComponentWrapperItem extends ScrollingGridPane.GridItem {
            private Component inner;
            public ComponentWrapperItem(Component comp){
                super(EMPTY_ICON);
                inner = comp;
                add(inner);
            }

            @Override
            protected void layout() {
                super.layout();
                inner.setRect(x + 4, y + 4, width() - 8, height() - 8);
                PixelScene.align(inner);
            }
        }
    }

    // 怪物数据池（原样保留）
    private static LinkedHashMap<Integer, List<Class<? extends Mob>>> allData = new LinkedHashMap<>();
    static {
        Set<Bestiary> excludedTypes = EnumSet.of(
                Bestiary.TRAP,
                Bestiary.PLANT,
                Bestiary.ALLY,
                Bestiary.NEUTRAL
        );
        List<Class<?>> includedBosses = Arrays.asList(DeadDogCerberus.class, DwarfGeneral.class, GnollGeomancer.class);
        for(Bestiary bestiary : Bestiary.values()){
            if( !excludedTypes.contains( bestiary ) ) {
                List< Class< ? extends Mob > > mobClasses = new ArrayList<>();
                for ( Class<?> cls : bestiary.entities() ) {
                    if ( Mob.class.isAssignableFrom( cls ) &&
                            (!Boss.class.isAssignableFrom( cls ) || includedBosses.contains( cls )) &&
                            !Mob.NoMobSpawn.class.isAssignableFrom( cls )) {
                        mobClasses.add( ( Class< ? extends Mob >) cls );
                    }
                }
                allData.put( bestiary.ordinal(), new ArrayList<>( mobClasses ) );
            }
        }
    }
}