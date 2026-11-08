package com.shatteredpixel.shatteredpixeldungeon.actors.buffs.status;

import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;

/**
 * 魔力汲取的buff实现
 * 现在已废弃，仅用于兼容旧存档。
 */
@Deprecated
public class MagicAbsorb extends Buff {

    @Override
    public boolean act() {
        detach();
        return true;
    }
}
