package com.shatteredpixel.shatteredpixeldungeon.ui.changelist.mlpd;

import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.ui.changelist.ChangeButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.changelist.ChangeInfo;
import com.watabou.noosa.Image;

import java.util.ArrayList;

public class vM0_6_7_X_Changes {

    public static void addAllChanges(ArrayList<ChangeInfo> changeInfos) {
        add_v0_6_0_Changes(changeInfos);
    }

    public static void add_v0_6_0_Changes( ArrayList<ChangeInfo> changeInfos ) {
        ChangeInfo changes = new ChangeInfo("v0.6.0.0-BetaX", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes = new ChangeInfo("改动", false, null);
        changes.hardlight(Window.SKYBULE_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(Icons.get(Icons.CHANGES), ("重大更新"),
                ("魔绫现已更新底层到破碎123版本！")));

        changes.addButton(new ChangeButton(Icons.get(Icons.CHALLENGE_ON), ("新挑战和部分挑战改动"),
                ("部分挑战进行重新调整，同时追加全新挑战，欢迎前来探索\n\n注意：部分挑战尚未完成，请等待后续版本")));

        changes.addButton(new ChangeButton(Icons.get(Icons.LANGS), ("本地化模块升级"),
                ("魔绫已经对本地化语言模块优化，使部分低配设备性能更好")));

        changes.addButton(new ChangeButton(Icons.get(Icons.DISPLAY), ("UI优化改动"),
                ("魔绫已经对UI优化改动进行大规模调整，欢迎前来体验")));

        changes.addButton(new ChangeButton((new Image("Ling.png", 0, 0, 16, 16)), ("开发者的话"),
                ("你好，这里是绫。如你所见，这是全新的魔绫像素地牢，她已经步入了破碎1.2" +
                        ".3的版本。\n\n至此，魔绫像素地牢以后将针对于此破碎底层进行更新。今后不会继续同步底层破碎版本。\n\n" +
                        "同时，本次更新后，后续应该还有几个补丁版。很高兴一路以来有那么多的朋友，非常谢谢你们的支持。\n\n现在，旅途才刚刚开始，魔绫下半段，将会更加精彩。\n" +
                        "在这之前，就让我们继续在上半段的魔绫里面探索前进吧。")));

        changes = new ChangeInfo("调整", false, null);
        changes.hardlight(Window.GREEN_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.FIREFISHSWORD), ("尚方宝剑特效重写"),
                ("优化了尚方宝剑特效，并最大程度上进行了处理")));



    }
}
