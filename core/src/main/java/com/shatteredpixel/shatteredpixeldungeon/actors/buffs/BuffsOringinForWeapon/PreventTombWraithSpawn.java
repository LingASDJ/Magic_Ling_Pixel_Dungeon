package com.shatteredpixel.shatteredpixeldungeon.actors.buffs.BuffsOringinForWeapon;

import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;

// 有小怨灵结界时，开启坟墓不生成怨灵
// 在坟墓生成怨灵处阻断，具体位于Heap.java的open函数的TOMB分支
// tips: 怨灵结界是我起的名字

public class PreventTombWraithSpawn extends Buff {
    {
        revivePersists = true;
    }
}
