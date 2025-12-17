package com.shatteredpixel.shatteredpixeldungeon.windows;

import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Imp;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.GameAPI;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.CheckBox;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.ScrollPane;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.noosa.ui.Component;

import java.util.ArrayList;

public class WndSelectNPCs extends Window {

    private static final int WIDTH = 120;
    private static final int HEIGHT = 160;
    private static final int GAP = 2;
    private static final int ITEM_HEIGHT = 18;
    private static final int BTN_HEIGHT = 18;

    private GameAPI api;
    private Component content;
    private ScrollPane list;
    private ArrayList<CheckBox> checkBoxes = new ArrayList<>();

    public WndSelectNPCs(GameAPI api) {
        super();
        this.api = api;

        resize(WIDTH, HEIGHT);

        content = new Component();
        list = new ScrollPane(content);
        add(list);

        createUI();
        layoutComponents();
        loadSettings();
    }

    private void createUI() {
        float pos = 0;

        // 标题
        RenderedTextBlock title = PixelScene.renderTextBlock(Messages.get(this, "title"), 9);
        title.hardlight(TITLE_COLOR);
        title.maxWidth(WIDTH);
        title.setPos(0, pos);
        content.add(title);
        pos += title.height() + GAP;

        // 获取所有可用的NPC类
        ArrayList<Class<?>> npcClasses = new ArrayList<>();
        // 这里需要根据你的游戏实现来获取所有NPC类
        // 示例代码，你需要根据实际情况修改
        npcClasses.add(Imp.class);
        // 添加更多NPC类...

        // 为每个NPC创建复选框
        for (Class<?> npcClass : npcClasses) {
            final Class<?> finalClass = npcClass;
            CheckBox cb = new CheckBox(Messages.titleCase(Messages.get(npcClass, "name"))) {
                @Override
                protected void onClick() {
                    super.onClick();
                    if (checked()) {
                        if (!api.getBannedNpcList().contains(finalClass)) {
                            api.getBannedNpcList().add(finalClass);
                        }
                    } else {
                        api.getBannedNpcList().remove(finalClass);
                    }
                }
            };
            cb.checked(api.getBannedNpcList().contains(npcClass));
            cb.setRect(0, pos, WIDTH, ITEM_HEIGHT);
            content.add(cb);
            checkBoxes.add(cb);
            pos += ITEM_HEIGHT + GAP;
        }

        // 保存按钮
        RedButton saveBtn = new RedButton(Messages.get(this, "save")) {
            @Override
            protected void onClick() {
                hide();
            }
        };
        saveBtn.setRect(0, pos, WIDTH, BTN_HEIGHT);
        content.add(saveBtn);
        pos += BTN_HEIGHT + GAP;

        content.setSize(WIDTH, pos);
        list.content();
    }

    private void layoutComponents() {
        list.setRect(0, 0, WIDTH, HEIGHT);
    }

    private void loadSettings() {
        // 设置已经在创建复选框时加载
    }
}
