package com.shatteredpixel.shatteredpixeldungeon.custom.testmode;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ChampionEnemy;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.custom.messages.M;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfAnmy;
import com.shatteredpixel.shatteredpixeldungeon.journal.Bestiary;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.CheckBox;
import com.shatteredpixel.shatteredpixeldungeon.ui.IconButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.utils.WndTextNumberInput;
import com.watabou.noosa.Game;
import com.watabou.utils.Bundle;
import com.watabou.utils.PointF;
import com.watabou.utils.Reflection;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

public class MobPlacer extends TestItem{
    {
        image = ItemSpriteSheet.CANDLE;
        defaultAction = AC_PLACE;
    }

    private static final String AC_PLACE = "place";
    private static final String AC_SET = "set";

    private int mobTier = 1;
    private int mobIndex = 0;
    private int elite = 0;

    private boolean shouldOverride = false;
    private int HT = 1;
    private int ST = 1;
    private int elite_op = 0;

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

        eliteBuffs.add(WandOfAnmy.AllyToRestartOK.class);
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
                                GameScene.add(m);

                                if( HT > 0 && shouldOverride ){
                                    m.HT = m.HP = HT;
                                }

                                if(elite_op>0){
                                    for(int i=0;i<16;++i){
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
    }


    private class WndSetMob extends Window{

        private static final int WIDTH = 150;
        private static final int HEIGHT = 180;
        private static final int BTN_SIZE = 18;
        private static final int GAP = 2;

        private RenderedTextBlock selectedPage;
        private ArrayList<IconButton> mobButtons = new ArrayList<>();
        private RenderedTextBlock selectedMob;
        private ArrayList<CheckBox> eliteOptions = new ArrayList<>(16);
        private RedButton modifyHealth;

        public WndSetMob(){
            super();

            resize(WIDTH, HEIGHT);

            RedButton lhs = new RedButton(Messages.get(WndSetMob.class,"last_page"), 6){
                @Override
                public void onClick(){
                    mobTier--;
                    if(mobTier < 1 || mobTier>10){
                        mobTier = 10;
                    }
                    mobIndex = Math.min(mobIndex, maxMobIndex(mobTier) - 1 );
                    refreshImage();
                    updateSelectedMob();
                }
            };
            lhs.setRect(GAP, GAP, 24, 18);
            add(lhs);

            RedButton rhs = new RedButton(Messages.get(WndSetMob.class,"next_page"), 6){
                @Override
                public void onClick(){
                    mobTier++;
                    if(mobTier < 1 || mobTier >10){
                        mobTier = 1;
                    }
                    mobIndex = Math.min(mobIndex, maxMobIndex(mobTier) - 1 );
                    refreshImage();
                    updateSelectedMob();
                }
            };
            rhs.setRect(WIDTH - 24 - GAP,  GAP, 24, 18);
            add(rhs);

            selectedPage = PixelScene.renderTextBlock("", 9);
            PixelScene.align(selectedPage);
            add(selectedPage);

            selectedMob = PixelScene.renderTextBlock("", 9);
            selectedMob.hardlight(0xFFFF44);
            PixelScene.align(selectedMob);
            add(selectedMob);

            float pos = 96;
            int column = 0;
            for (int i = 0; i < 16 && column < 3; ++i) {
                CheckBox cb = new CheckBox(M.L(MobPlacer.class, "elite_name" + i));
                cb.active = true;
                cb.checked((elite_op & (1<<i))>0);
                add(cb);
                eliteOptions.add(cb);

                if (column == 0) {
                    cb.setRect((WIDTH/3f - GAP)/3f * column, pos, (WIDTH/3f - GAP), 16);
                } else if (column == 1) {
                    cb.setRect((WIDTH/3f - GAP)/3f * column+35, pos, (WIDTH/3f - GAP), 16);
                } else {
                    cb.setRect((WIDTH/3f - GAP)/3f * column+70, pos, (WIDTH/3f - GAP), 16);
                    column = -1; // 重置column的值，使其在下一次循环时为0（即第一列）
                    pos += 16 + GAP; // 换行
                }

                if(i==15){
                    cb.setRect((WIDTH/3f - GAP)/3f * 2+70, 78, (WIDTH/3f - GAP), 16);
                }

                modifyHealth = new RedButton(Messages.get(MobPlacer.class, "modify_health"), 7) {
                    @Override
                    protected void onClick() {
                        Game.runOnRenderThread(() ->GameScene.show(new WndTextNumberInput(
                                Messages.get(MobPlacer.class, "custom_title"),
                                Messages.get(MobPlacer.class, "health_desc"),
                                Integer.toString(HT),
                                6, false, Messages.get(MobPlacer.class, "confirm"),
                                Messages.get(MobPlacer.class, "cancel"),false) {
                            @Override
                            public void onSelect(boolean check, String text) {
                                if ( check && text.matches("\\d+") ) {
                                    int value = Integer.parseInt( text );
                                    if( value >= 0 ) {
                                        HT = Math.min( value, 666666 );
                                    }
                                }
                            }
                        }));
                    }
                };
                modifyHealth.setRect((WIDTH/3f)/6f-8, 78, (WIDTH/3f - GAP), 16);
                modifyHealth.enable( shouldOverride );
                modifyHealth.active = shouldOverride;
                add(modifyHealth);

                CheckBox overrideResultButton = new CheckBox(Messages.get(MobPlacer.class, "override")) {
                    @Override
                    public void checked(boolean value) {
                        if (checked != value) {
                            checked = value;
                            icon.copy( Icons.get( checked ? Icons.CHECKED : Icons.UNCHECKED ) );
                            shouldOverride = checked;
                            modifyHealth.enable( shouldOverride );
                            modifyHealth.active = shouldOverride;
                        }
                    }
                };
                overrideResultButton.setRect(modifyHealth.right() + GAP, 78, (WIDTH/3f - GAP), 16);
                overrideResultButton.checked( shouldOverride );
                add(overrideResultButton);



                column++;
            }

            createMobImage();

            updateSelectedMob();
            layout();
        }

        private void updateEliteSettings(){
            int el = 0;
            for(int i=0;i<16;++i){
                el += eliteOptions.get(i).checked() ? (1<<i) : 0;
            }
            elite_op = el;
        }

        private void updateSelectedMob(){
            int selected = mobTier;
            StringBuilder sb = new StringBuilder();
            for(int i=1;i<=10;++i){
                sb.append((i==selected? "* ":"- "));
            }
            selectedPage.text(sb.toString());
            selectedPage.maxWidth(WIDTH / 2);
            selectedPage.setPos((WIDTH - selectedPage.width())/2, 5);
            updateMobText();
        }

        private void updateMobText(){
            selectedMob.text( M.L( getMobClass(), "name") );
        }

        private void layout(){
            selectedPage.maxWidth(WIDTH / 2);
            selectedPage.setPos((WIDTH - selectedPage.width())/2, 5);
            selectedMob.maxWidth(WIDTH);
            selectedMob.setPos((WIDTH - selectedMob.width())/2, 16);
            resize(WIDTH, (int)eliteOptions.get(14).bottom() + 1);
        }

        private void createMobImage() {
            int maxNum = maxMobIndex( mobTier );
            //(N+1)/2
            int firstLine = (maxNum >> 1) + (maxNum & 1);
            float left1 = (WIDTH - (GAP + BTN_SIZE) * firstLine + GAP)/2f;
            float left2 = (WIDTH - (GAP + BTN_SIZE) * (maxNum - firstLine) + GAP)/2f;
            for (int i = 0; i < maxNum; ++i) {
                final int j = i;
                IconButton btn = new IconButton() {
                    @Override
                    public void onClick() {
                        super.onClick();
                        mobIndex = j;
                        updateMobText();
                    }
                };
                btn.icon( Reflection.newInstance( getMobClass( i ) ).sprite());
                float max = Math.max(btn.icon().width(), btn.icon().height());
                btn.icon().scale = new PointF(BTN_SIZE/max, BTN_SIZE/max);
                if(i<firstLine){
                    btn.setRect(left1, 30f, BTN_SIZE, BTN_SIZE );
                    left1 += GAP + BTN_SIZE;
                }else{
                    btn.setRect(left2, 56f, BTN_SIZE, BTN_SIZE);
                    left2 += GAP + BTN_SIZE;
                }
                add(btn);
                mobButtons.add(btn);
            }
        }

        private void clearImage(){
            for(int i=0, len = mobButtons.size();i<len;++i){
                mobButtons.get(i).destroy();
            }
        }

        private void refreshImage(){
            clearImage();
            createMobImage();
        }

        @Override
        public void onBackPressed() {
            updateEliteSettings();
            super.onBackPressed();
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

        for(Bestiary bestiary : Bestiary.values()){
            if( !excludedTypes.contains( bestiary ) ) {
                List< Class< ? extends Mob > > mobClasses = new ArrayList<>();
                for ( Class<?> cls : bestiary.entities() ) {
                    if ( Mob.class.isAssignableFrom( cls ) ) {
                        mobClasses.add( ( Class< ? extends Mob >) cls );
                    }
                }

                allData.put( bestiary.ordinal(), new ArrayList<>( mobClasses ) );
            }
        }
    }
}