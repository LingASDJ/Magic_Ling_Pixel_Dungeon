package com.shatteredpixel.shatteredpixeldungeon.multiplayer;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndMessage;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;

import java.util.function.Consumer;

/**
 * 访客首次加入时选择英雄职业的简易窗口：只有职业按钮，不含种子/挑战/难度等原版选项。
 * 点击职业 → onSelect.accept(职业)；关闭/取消 → onSelect.accept(null)。
 */
/** 访客首次加入时的职业选择窗口。 */
public class WndHeroClassSelect extends Window {

    private static final int WIDTH = 150;
    private static final int BTN_HEIGHT = 32;
    private static final int GAP = 2;

    private final Consumer<HeroClass> onSelect;

    /**
     * 访客首次加入时的职业选择窗口。
     *
     * @param onSelect 参数。
     */
    public WndHeroClassSelect(Consumer<HeroClass> onSelect) {
        this.onSelect = onSelect;

        float pos = 0f;

        float width = 80;
        float btnHeight = MpUi.px(BTN_HEIGHT);
        float gap = MpUi.px(GAP);

        RenderedTextBlock title = PixelScene.renderTextBlock( 6);
        title.hardlight(TITLE_COLOR);
        title.setPos(0f, pos);
        title.maxWidth((int) width);
        title.text( Messages.get(WndHeroClassSelect.class, "title"));
        add(title);
        pos = title.bottom() + MpUi.px(4f);

        for (HeroClass cls : HeroClass.values()) {
            RedButton btn = new RedButton(Messages.titleCase(cls.title()),6) {
                @Override
                protected void onClick() {
                    // 法尔塔娅（剑士）是 v1.0.0 的预载资源：正式上线前不可选。
                    // 判定委托原版 HeroClass.isUnlocked()：正式版为 false 弹提示，
                    // 桌面开发版(INDEV)可玩；作者在 v1.0.0 放开原版判定后这里自动解锁。
                    if (cls == HeroClass.SPELLSWORD && !cls.isUnlocked()) {
                        Game.scene().add(new WndMessage(
                                Messages.get(WndHeroClassSelect.class, "spellsword_locked")));
                        return;
                    }
                    hide();
                    WndHeroClassSelect.this.onSelect.accept(cls);
                }
            };
            Image icon = new Image(cls.spritesheet(), 0, 0, 12, 15);
            icon.scale.set(MpUi.scale());
            btn.icon(icon);
            add(btn);
            btn.setRect(0f, pos, width, btnHeight);
            pos += btnHeight + gap;
        }

        resize((int) width, (int) (pos - gap));
    }

    @Override
    /**
     * 取消选择 = 取消本次加入。
     */
    public void onBackPressed() {
        hide();
        onSelect.accept(null);   // 取消选择 = 取消本次加入
    }
}
