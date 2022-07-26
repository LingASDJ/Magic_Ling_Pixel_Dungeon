package com.shatteredpixel.shatteredpixeldungeon.ui.changelist.mlpd;

import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.changelist.ChangeButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.changelist.ChangeInfo;

import java.util.ArrayList;

public class vM0_6_7_X_Changes {

    public static void addAllChanges(ArrayList<ChangeInfo> changeInfos) {
        add_v0_6_0_Changes(changeInfos);
    }

    public static void add_v0_6_0_Changes( ArrayList<ChangeInfo> changeInfos ) {
        ChangeInfo changes = new ChangeInfo("敬请期待-MLPD最终更新(P1)", true, "");
        changes.hardlight(0x808080);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(Icons.get(Icons.SHPX), ("更新底层到破碎123版本"),
                ("魔绫即将进行大更新到破碎123版本，敬请期待")));

        changes.addButton(new ChangeButton(Icons.get(Icons.CHALLENGE_ON), ("新挑战和部分挑战改动"),
                ("魔绫即将对部分挑战进行重新调整，同时追加全新挑战，敬请期待")));

        changes.addButton(new ChangeButton(Icons.get(Icons.LANGS), ("本地化模块升级"),
                ("魔绫即将对本地化语言模块优化，使部分渣机性能更好")));

        changes.addButton(new ChangeButton(Icons.get(Icons.DISPLAY), ("UI优化改动"),
                ("魔绫即将对UI优化改动进行大规模调整，敬请期待")));

        changes.addButton(new ChangeButton(Icons.get(Icons.STAIRS), ("开发者的话"),
                ("魔绫地牢已经更新了接近一年，接下来，第一部分即将完结。敬请期待第二部分的更新！")));

        changes.addButton(new ChangeButton(Icons.get(Icons.NEWS), ("不止于此"),
                ("更多第一部分最终版更新情报，请锁定魔绫总群!")));

    }
}
