package com.shatteredpixel.shatteredpixeldungeon.actors.buffs.BuffsOringinForWeapon;

import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;

// 有小怨灵结界时，开启坟墓不生成怨灵
// 在坟墓生成怨灵处阻断，具体位于Heap.java的open函数的TOMB分支
// tips: 怨灵结界是我起的名字

public class PreventTombWraithSpawn extends Buff {
    // 不设置 revivePersists：死亡后若铲子随背包丢失，结界也应随之消失；
    // 拾回遗失的背包时 LostBackpack 会重新调用 activate，结界会自动恢复。
}
