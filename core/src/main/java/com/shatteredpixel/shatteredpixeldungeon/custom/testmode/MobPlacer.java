/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2022 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
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
        private boolean isClosed = false;

        private static final int WND_WIDTH_LANDSCAPE = 200;
        private static final int WND_WIDTH_PORTRAIT = 140;
        private static final int WND_HEIGHT_LANDSCAPE = 180;
        private static final int WND_HEIGHT_PORTRAIT = 190;
        private static final int LEFT_PANEL_WIDTH_LANDSCAPE = 55;
        private static final int LEFT_PANEL_WIDTH_PORTRAIT = 40;

        private boolean isLandscape;
        private int wndWidth, wndHeight, leftPanelWidth;

        private static final float MOB_GRID_RATIO_LANDSCAPE = 0.35f;
        private static final float MOB_GRID_RATIO_PORTRAIT = 0.30f;
        private static final float ELITE_GRID_RATIO_LANDSCAPE = 0.45f;
        private static final float ELITE_GRID_RATIO_PORTRAIT = 0.50f;
        private static final float SETTING_GRID_RATIO = 0.20f;

        private static final int FUNCTION_GRID_WIDTH_LANDSCAPE = 50;
        private static final int FUNCTION_GRID_WIDTH_PORTRAIT = 40;
        private static final int FUNCTION_GRID_HEIGHT = 26;

        private RedButton btnPrevPage, btnNextPage;
        private RenderedTextBlock txtPageIndicator;
        private RenderedTextBlock txtSelectedMobName;

        private ScrollingGridPane mobGridPane;
        private ScrollingGridPane eliteGridPane;
        private ScrollingGridPane settingGridPane;

        private ArrayList<MobGridItem> mobIconItems = new ArrayList<>();
        private ArrayList<EliteCheckItem> eliteCheckItems = new ArrayList<>();
        private RedButton btnModifyHealth;
        private CheckBox cbOverrideHP;

        private MobSprite previewSprite;
        private static final int PREVIEW_SIZE_LANDSCAPE = 36;
        private static final int PREVIEW_SIZE_PORTRAIT = 24;

        private CheckBox cbEnemy, cbNeutral, cbAlly;
        private RedButton cbInfo;

        private final Image EMPTY_ICON = new Image(new ItemSprite(ItemSpriteSheet.CHALLANEESICON_17+6));

        public WndSetMob(){
            super();
            isLandscape = Game.width > Game.height;
            wndWidth = isLandscape ? WND_WIDTH_LANDSCAPE : WND_WIDTH_PORTRAIT;
            wndHeight = isLandscape ? WND_HEIGHT_LANDSCAPE : WND_HEIGHT_PORTRAIT;
            leftPanelWidth = isLandscape ? LEFT_PANEL_WIDTH_LANDSCAPE : LEFT_PANEL_WIDTH_PORTRAIT;
            resize(wndWidth, wndHeight);
            initLeftPanel();
            initTripleGridLayout();
            refreshAllGridContent();
            layout();
            refreshPreviewSprite();
        }

        @Override
        public void hide() {
            syncEliteFlags();
            isClosed = true;

            if (mobGridPane != null) {
                mobGridPane.setSize(0, 0);
                mobGridPane.clear();
            }
            if (eliteGridPane != null) {
                eliteGridPane.setSize(0, 0);
                eliteGridPane.clear();
            }
            if (settingGridPane != null) {
                settingGridPane.setSize(0, 0);
                settingGridPane.clear();
            }

            if (btnPrevPage != null) btnPrevPage.setSize(0, 0);
            if (btnNextPage != null) btnNextPage.setSize(0, 0);
            if (cbEnemy != null) cbEnemy.setSize(0, 0);
            if (cbNeutral != null) cbNeutral.setSize(0, 0);
            if (cbAlly != null) cbAlly.setSize(0, 0);
            if (cbInfo != null) cbInfo.setSize(0, 0);
            if (cbOverrideHP != null) cbOverrideHP.setSize(0, 0);
            if (btnModifyHealth != null) btnModifyHealth.setSize(0, 0);

            mobIconItems.clear();
            eliteCheckItems.clear();

            if (parent != null) parent.remove(this);
            super.hide();
        }

        private void initLeftPanel(){
            int btnWidth = leftPanelWidth - 10;
            int btnHeight = isLandscape ? 24 : 18;

            btnPrevPage = new RedButton("<", 7){
                @Override
                public void onClick(){
                    if (isClosed) return;
                    mobTier--;
                    if(mobTier < 1) mobTier = maxPage;
                    mobIndex = Math.min(mobIndex, maxMobIndex(mobTier)-1);
                    refreshAllGridContent();
                    updatePageText();
                    refreshPreviewSprite();
                }
            };
            btnPrevPage.setRect(5, 10, btnWidth, btnHeight);
            add(btnPrevPage);

            txtPageIndicator = PixelScene.renderTextBlock("", isLandscape ? 9 : 8);
            txtPageIndicator.hardlight(0xFFFFFF);
            add(txtPageIndicator);

            txtSelectedMobName = PixelScene.renderTextBlock("", 5);
            txtSelectedMobName.hardlight(0xFFFF44);
            txtSelectedMobName.maxWidth(leftPanelWidth);
            add(txtSelectedMobName);

            btnNextPage = new RedButton(">", 7){
                @Override
                public void onClick(){
                    if (isClosed) return;
                    mobTier++;
                    if(mobTier > maxPage) mobTier = 1;
                    mobIndex = Math.min(mobIndex, maxMobIndex(mobTier)-1);
                    refreshAllGridContent();
                    updatePageText();
                    refreshPreviewSprite();
                }
            };
            btnNextPage.setRect(5, txtSelectedMobName.bottom()+3, btnWidth, btnHeight);
            add(btnNextPage);

            previewSprite = new RatSprite();
            add(previewSprite);

            int cbSize = isLandscape ? 16 : 14;

            cbEnemy = new CheckBox("E"){
                @Override
                public void checked(boolean value) {
                    if (isClosed) return;
                    super.checked(value);
                    if (value) {
                        mobAlign = Char.Alignment.ENEMY;
                        if (cbNeutral != null) cbNeutral.checked(false);
                        if (cbAlly != null) cbAlly.checked(false);
                    }
                }
            };

            cbNeutral = new CheckBox("R"){
                @Override
                public void checked(boolean value) {
                    if (isClosed) return;
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
                    if (isClosed) return;
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
                    if (isClosed) return;
                    GameScene.show(new WndMessage(Messages.get(MobPlacer.class, "era_desc")));
                }
            };
            add(cbInfo);

            updatePageText();
        }

        private void refreshPreviewSprite(){
            remove(previewSprite);
            Mob mobProto = Reflection.newInstance(getMobClass());
            previewSprite = (MobSprite) mobProto.sprite();
            previewSprite.idle();
            add(previewSprite);

            int previewSize = isLandscape ? PREVIEW_SIZE_LANDSCAPE : PREVIEW_SIZE_PORTRAIT;
            float scale = previewSize / Math.max(previewSprite.width(), previewSprite.height());
            previewSprite.scale.set(scale, scale);
            previewSprite.x = (leftPanelWidth - previewSprite.width() * scale) / 2f;
            previewSprite.y = btnNextPage.bottom() + (isLandscape ? 5 : 2);
            PixelScene.align(previewSprite);
        }

        private void initTripleGridLayout(){
            float rightX = leftPanelWidth + 2;
            float rightW = wndWidth - rightX - 2;
            float mobRatio = isLandscape ? MOB_GRID_RATIO_LANDSCAPE : MOB_GRID_RATIO_PORTRAIT;
            float eliteRatio = isLandscape ? ELITE_GRID_RATIO_LANDSCAPE : ELITE_GRID_RATIO_PORTRAIT;

            mobGridPane = new ScrollingGridPane();
            mobGridPane.setCellSize(isLandscape ? 24 : 20, isLandscape ? 24 : 20);
            add(mobGridPane);
            mobGridPane.setRect(rightX, 0, rightW, wndHeight * mobRatio);

            eliteGridPane = new ScrollingGridPane();
            eliteGridPane.setCellSize(
                    isLandscape ? FUNCTION_GRID_WIDTH_LANDSCAPE : FUNCTION_GRID_WIDTH_PORTRAIT,
                    FUNCTION_GRID_HEIGHT
            );
            add(eliteGridPane);
            eliteGridPane.setRect(
                    rightX,
                    wndHeight * mobRatio,
                    rightW,
                    wndHeight * eliteRatio
            );

            settingGridPane = new ScrollingGridPane();
            settingGridPane.setCellSize(isLandscape ? 65 : 55, FUNCTION_GRID_HEIGHT);
            add(settingGridPane);
            settingGridPane.setRect(
                    rightX,
                    wndHeight * (mobRatio + eliteRatio),
                    rightW,
                    wndHeight * SETTING_GRID_RATIO
            );
        }

        private void updatePageText(){
            txtPageIndicator.text(mobTier + "/" + maxPage);
            txtPageIndicator.setPos(
                    leftPanelWidth / 2f - txtPageIndicator.width() / 2f,
                    btnPrevPage.bottom() + (isLandscape ? 10 : 6)
            );
            PixelScene.align(txtPageIndicator);

            txtSelectedMobName.text(M.L(getMobClass(), "name"));
            txtSelectedMobName.setPos(
                    leftPanelWidth / 2f - txtSelectedMobName.width() / 2f,
                    txtPageIndicator.bottom() + (isLandscape ? 8 : 4)
            );
            PixelScene.align(txtSelectedMobName);
        }

        private void refreshAllGridContent(){
            mobGridPane.clear();
            eliteGridPane.clear();
            settingGridPane.clear();
            mobIconItems.clear();
            eliteCheckItems.clear();

            mobGridPane.addHeader(Messages.get(WndSetMob.class, "mob_list"), isLandscape ? 9 : 7, false);
            int mobCount = maxMobIndex(mobTier);
            for(int i = 0; i < mobCount; i++){
                Mob mobProto = Reflection.newInstance(getMobClass(i));
                Image mobSprite = mobProto.sprite();
                int finalI = i;
                MobGridItem gridItem = new MobGridItem(mobSprite, i, () -> {
                    mobIndex = finalI;
                    updatePageText();
                    refreshPreviewSprite();
                });
                mobGridPane.addItem(gridItem);
                mobIconItems.add(gridItem);
            }

            eliteGridPane.addHeader(Messages.get(WndSetMob.class, "elite_modifier"), isLandscape ? 9 : 7, false);
            for(int i = 0; i < eliteBuffs.size(); i++){
                Class<? extends ChampionEnemy> buffCls = eliteBuffs.get(i);
                boolean checked = (elite_op & (1 << i)) > 0;
                EliteCheckItem checkItem = new EliteCheckItem(buffCls, checked, i);
                eliteGridPane.addItem(checkItem);
                eliteCheckItems.add(checkItem);
            }

            settingGridPane.addHeader(Messages.get(WndSetMob.class, "health_setting"), isLandscape ? 9 : 7, false);
            btnModifyHealth = new RedButton(Messages.get(MobPlacer.class, "modify_health"), isLandscape ? 8 : 7){
                @Override
                protected void onClick() {
                    if (isClosed) return;
                    Game.runOnRenderThread(() -> {
                        if (isClosed) return;
                        GameScene.show(new WndTextNumberInput(
                                Messages.get(MobPlacer.class, "custom_title"),
                                Messages.get(MobPlacer.class, "health_desc"),
                                Integer.toString(HT),
                                6, false,
                                Messages.get(MobPlacer.class, "confirm"),
                                Messages.get(MobPlacer.class, "cancel"), false
                        ) {
                            @Override
                            public void onSelect(boolean check, String text) {
                                if (isClosed) return;
                                if (check && text.matches("\\d+")) {
                                    int value = Integer.parseInt(text);
                                    if (value >= 0) HT = Math.min(value, 666666);
                                }
                            }
                        });
                    });
                }
            };

            cbOverrideHP = new CheckBox(Messages.get(MobPlacer.class, "override")){
                @Override
                public void checked(boolean value) {
                    if (isClosed) return;
                    super.checked(value);
                    shouldOverride = value;
                    if (btnModifyHealth != null) {
                        btnModifyHealth.enable(value);
                        btnModifyHealth.active = value;
                    }
                }
            };
            cbOverrideHP.checked(shouldOverride);

            settingGridPane.addItem(new ComponentWrapperItem(cbOverrideHP));
            btnModifyHealth.enable(shouldOverride);
            btnModifyHealth.active = shouldOverride;
            settingGridPane.addItem(new ComponentWrapperItem(btnModifyHealth));

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
            btnPrevPage.setY(10);
            btnNextPage.setY(txtSelectedMobName.bottom() + 3);
            updatePageText();
            refreshPreviewSprite();

            float baseY = previewSprite.y + previewSprite.height() + (isLandscape ? 4 : 2);
            int cbW = (leftPanelWidth - 8) / 3;
            cbEnemy.setRect(isLandscape ? 2 : 4, baseY,  isLandscape ? cbW : 32, 16);
            cbNeutral.setRect(isLandscape ? cbEnemy.right() + 2 : 4, isLandscape ? baseY : baseY + 18,  isLandscape ? cbW : 32, 16);
            cbAlly.setRect(isLandscape ? cbNeutral.right() + 2 : 4,isLandscape ? baseY : baseY + 36, isLandscape ? cbW : 32, 16);
            cbInfo.setRect(2, cbAlly.bottom() + 1, leftPanelWidth - 6, 16);
            PixelScene.align(cbEnemy);
            PixelScene.align(cbNeutral);
            PixelScene.align(cbAlly);

            mobGridPane.layout();
            eliteGridPane.layout();
            settingGridPane.layout();
        }

        private class MobGridItem extends ScrollingGridPane.GridItem {
            private Runnable clickCallback;
            private int mobIdx;
            public MobGridItem(Image sprite, int index, Runnable onClick) {
                super(sprite);
                mobIdx = index;
                clickCallback = onClick;
                float maxDim = Math.max(icon.width(), icon.height());
                float cellSize = isLandscape ? 24 : 20;
                icon.scale.set(cellSize / maxDim * 0.85f, cellSize / maxDim * 0.85f);
            }

            @Override
            public boolean onClick(float globalX, float globalY) {
                if (isClosed) return false;
                if (globalX >= this.x && globalX <= this.x + width()
                        && globalY >= this.y && globalY <= this.y + height()) {
                    clickCallback.run();
                    return true;
                }
                return false;
            }
        }

        private class EliteCheckItem extends ScrollingGridPane.GridItem {
            private Image icon;
            private IconButton infoBtn;
            private CheckBox checkBtn;

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

                icon = new BuffIcon(buff.icon(), true);
                buff.tintIcon(icon);
                add(icon);

                infoBtn = new IconButton(Icons.get(Icons.INFO)){
                    @Override
                    protected void onClick() {
                        if (isClosed) return;
                        int winW = isLandscape ? 160 : 130;
                        int scrollW = isLandscape ? 144 : 118;

                        RenderedTextBlock titleTxt = PixelScene.renderTextBlock(buffName, isLandscape ? 10 : 9);
                        titleTxt.hardlight(buffColor);

                        RenderedTextBlock descTxt = PixelScene.renderTextBlock(buffDesc, 6);
                        descTxt.maxWidth(scrollW);

                        int paddingTop = 6;
                        int titleGap = 4;
                        int scrollPaddingTop = 2;
                        int scrollPaddingBottom = 4;
                        int bottomBtnHeight = 26;
                        int scrollH = (int)(descTxt.height() + scrollPaddingTop + scrollPaddingBottom);
                        int winH = paddingTop + (int)titleTxt.height() + titleGap + scrollH + bottomBtnHeight;
                        winH = Math.max(110, Math.min(winH, 280));

                        Window infoWnd = new Window(winW, winH);
                        titleTxt.setRect(8, paddingTop, scrollW, titleTxt.height());
                        infoWnd.add(titleTxt);

                        Component scrollContent = new Component();
                        ScrollPane scrollPane = new ScrollPane(scrollContent);
                        infoWnd.add(scrollPane);
                        scrollPane.setRect(8, titleTxt.bottom() + titleGap, scrollW, scrollH);

                        descTxt.setPos(0, scrollPaddingTop);
                        scrollContent.add(descTxt);
                        scrollContent.setSize(scrollW, descTxt.height() + scrollPaddingTop + scrollPaddingBottom);

                        RedButton closeBtn = new RedButton(Messages.get(WndSetMob.class, "close"), 8){
                            @Override
                            protected void onClick(){
                                infoWnd.hide();
                                if (infoWnd.parent != null) infoWnd.parent.remove(infoWnd);
                            }
                        };
                        closeBtn.setRect(10, winH - bottomBtnHeight, winW - 20, 20);
                        infoWnd.add(closeBtn);

                        GameScene.show(infoWnd);
                    }
                };
                add(infoBtn);

                checkBtn = new CheckBox(""){
                    @Override
                    public void checked(boolean value) {
                        if (isClosed) return;
                        super.checked(value);
                        EliteCheckItem.this.checked = value;
                    }
                };
                checkBtn.checked(isChecked);
                checked = isChecked;
                add(checkBtn);
            }

            @Override
            protected void layout() {
                float iconSize = isLandscape ? BuffIndicator.SIZE_LARGE : BuffIndicator.SIZE;
                icon.x = x + (width() - iconSize) / 4f;
                icon.y = y + 2;
                PixelScene.align(icon);

                float iconRight = icon.x + icon.width();
                infoBtn.setRect(iconRight + 2, icon.y, isLandscape ? 16 : 14, isLandscape ? 16 : 14);
                PixelScene.align(infoBtn);

                checkBtn.setRect(x + (width() - 16) / 2f, y + height() - 18, 16, 16);
                PixelScene.align(checkBtn);
            }
        }

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