package com.shatteredpixel.shatteredpixeldungeon.actors;

/**
 * 场上单位死亡事件监听器。
 *
 * <p>由 {@link Char#die(Object)} 在任意单位死亡时广播，供依赖“任意单位死亡”的
 * Boss 机制使用（灵肉桎梏之界、千面殁亡之果、希声奔赴之花）。</p>
 */
public interface DeathListener {

    /**
     * @param dead 刚刚死亡的单位，此时 {@code dead.pos} 仍然有效。
     */
    void onCharDeath(Char dead);
}
