package com.shatteredpixel.shatteredpixeldungeon.windows;

import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.CheckBox;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.OptionSlider;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;

import java.util.ArrayList;

public class BossSettingWindows extends Window {
    private static final int WIDTH = 120;
    private static final int HEIGHT = 100;
    private static final int BOX_HEIGHT = 16;
    private static final int TTL_HEIGHT = 12;
    private static final int GAP= 2;

    private final ArrayList<CheckBox> cbs;
    OptionSlider level3;
    public BossSettingWindows(){
        resize(WIDTH, HEIGHT);

        RenderedTextBlock rtb = PixelScene.renderTextBlock(Messages.get(this, "title"), TTL_HEIGHT - GAP);
        rtb.setPos(WIDTH/2f - rtb.width()/2, GAP);
        PixelScene.align(rtb);
        rtb.hardlight(0xFFFF00);
        add(rtb);
        float pos = TTL_HEIGHT + GAP;
        cbs = new ArrayList<>();

        for(int i = 0; i<5; ++i){
            int finalI = i;
            CheckBox cb = new CheckBox(Messages.get(this, "boss_"+ (finalI + 1))){
                public void checked( boolean value ) {
                    if (checked != value) {
                        checked = value;
                        icon.copy( Icons.get( checked ? Icons.CHECKED : Icons.UNCHECKED ) );
                    }
                }
            };
            cb.setRect(GAP, pos, WIDTH - GAP * 2, BOX_HEIGHT);


            add(cb);
            cbs.add(cb);
            pos += BOX_HEIGHT + GAP;

            if(i == 1 && !Badges.isUnlocked(Badges.Badge.KILL_SM) || i == 0 || i==4){
                cb.alpha(0.4f);
                cb.active=false;
                cb.checked(false);
                cb.text(Messages.get(this, "boss_unselect_"+i));
            } else {
                cb.checked((Statistics.boss_enhance & (1<<i)) >0);
                cb.enable(Statistics.deepestFloor < (5+i*5));
            }

        }

//        level3 = new OptionSlider("", Messages.get(this, "dm300"),
//                Messages.get(this, "ice"), 1, 3) {
//
//            @Override
//            protected void onChange() {
//                    ShatteredPixelDungeon.scene().add(new WndOptions(new ItemSprite(ItemSpriteSheet.GOLD),
//                            Messages.get(BossSettingWindows.class, "bossattack"),
//                            Messages.get(BossSettingWindows.class, "bossgold") + ((2 + 1) * (Math.max(depth / 5,
//                                    1)) * 200),
//                            Messages.get(BossSettingWindows.class, "bug"), Messages.get(BossSettingWindows.class,
//                            "not_yet")) {
//                        @Override
//                        protected void onSelect(int index) {
//                            if (index == 0 && Dungeon.gold >= ((2 + 1) * (Math.max(depth / 5, 1)) * 200)) {
//                                Dungeon.gold -= ((2 + 1) * (Math.max(depth / 5, 1)) * 200);
//                                GLog.w(Messages.get(BossSettingWindows.class, "getboss") + Messages.get(BossSettingWindows.class, "boss_" + (2 + 1)));
//                                SPDSettings.level3boss(getSelectedValue());
//                            } else if (index == 1) {
//                                GLog.w(Messages.get(BossSettingWindows.class, "cancal"));
//                                SPDSettings.level3boss(9);
//                            } else if (Dungeon.gold < ((2 + 1) * (Math.max(depth / 10, 1)) * 200)) {
//                                GLog.w(Messages.get(BossSettingWindows.class, "no_money"));
//                                SPDSettings.level3boss(9);
//                            }
//                        }
//                    });
//                }
//
//        };
//        level3.setRect(GAP, 50, WIDTH - GAP * 2, BOX_HEIGHT);
//        //level3.setSelectedValue(SPDSettings.level3boss());
//        add(level3);

        resize(WIDTH, HEIGHT);
    }



    @Override
    public void onBackPressed() {
        Statistics.boss_enhance = 0;
        for(int i=0, len = cbs.size(); i<len; ++i){
            if(cbs.get(i).checked()) {
                Statistics.boss_enhance += 1<<i;
            }
        }
        super.onBackPressed();
    }

}
