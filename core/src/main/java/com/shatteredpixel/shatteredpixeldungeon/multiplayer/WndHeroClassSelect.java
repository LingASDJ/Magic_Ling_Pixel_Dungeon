package com.shatteredpixel.shatteredpixeldungeon.multiplayer;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.noosa.Image;

import java.util.function.Consumer;

/**
 * 访客首次加入时选择英雄职业的简易窗口：只有职业按钮，不含种子/挑战/难度等原版选项。
 * 点击职业 → onSelect.accept(职业)；关闭/取消 → onSelect.accept(null)。
 */
/** 访客首次加入时的职业选择窗口。 */
public class WndHeroClassSelect extends Window {

    private static final int WIDTH = 150;
    private static final int BTN_HEIGHT = 20;
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

        RenderedTextBlock title = PixelScene.renderTextBlock("选择英雄职业", 9);
        title.hardlight(TITLE_COLOR);
        title.setPos(0f, pos);
        title.maxWidth(WIDTH);
        add(title);
        pos = title.bottom() + 4f;

        for (HeroClass cls : HeroClass.values()) {
            RedButton btn = new RedButton(Messages.titleCase(cls.title())) {
                @Override
                protected void onClick() {
                    hide();
                    WndHeroClassSelect.this.onSelect.accept(cls);
                }
            };
            btn.icon(new Image(cls.spritesheet(), 0, 0, 12, 15));
            add(btn);
            btn.setRect(0f, pos, WIDTH, BTN_HEIGHT);
            pos += BTN_HEIGHT + GAP;
        }

        resize(WIDTH, (int) (pos - GAP));
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
