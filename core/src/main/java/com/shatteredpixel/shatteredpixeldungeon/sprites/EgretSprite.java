package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.watabou.noosa.TextureFilm;

/**
 * 白鹭（白魔女）精灵。
 *
 * <p>施法读条用 {@code zap} 循环；成功、失败、麻痹、告死之嫁、抵抗敌厄之拒
 * 都是一次性动画，播完自动回 idle。</p>
 */
public class EgretSprite extends MobSprite {

    private Animation zapFail;
    private Animation zapSuccess;
    private Animation zapParalysis;
    private Animation spurnShieldNuptial;
    private Animation baneDeadEnemy;
    private Animation currentAnim;

    /** 告死之嫁动画独占期间，其他动画一律不播放。 */
    private boolean nuptialLock = false;
    private Runnable onNuptialComplete;

    public EgretSprite() {
        super();

        texture( Assets.Sprites.EGRET);

        TextureFilm frames = new TextureFilm( texture, 34, 32 );

        idle = new Animation( 5, true );
        idle.frames( frames, 0,1,2,3 );

        run = idle.clone();
        attack = idle.clone();
        die = idle.clone();

        zap = new Animation( 12, true );
        zap.frames( frames, 9,10,11,12,13,14);

        zapSuccess = new Animation( 12, false );
        zapSuccess.frames( frames, 15,16,17,18,0);

        zapFail = new Animation( 12, true );
        zapFail.frames( frames, 19,20,21,22,23);

        zapParalysis = new Animation(8, true );
        zapParalysis.frames( frames, 24,25,26,27);

        spurnShieldNuptial = new Animation(12, false );
        spurnShieldNuptial.frames( frames, 28,29,30,31,32,33,34,35);

        baneDeadEnemy = new Animation(12, false );
        baneDeadEnemy.frames( frames,  4,5,6,7,8,0);

        play( idle );
    }

    @Override
    public void link(Char ch) {
        super.link(ch);
        // 白鹭使用 BossHealthBar，不显示头顶的普通 HealthBar
        removeHealthIndicator();
    }

    @Override
    public void add(State state) {
        if (state == State.PARALYSED) {
            // 白鹭需要持续播放麻痹循环动画，不能被 paused 冻结
            paused = false;
            return;
        }
        super.add(state);
    }

    @Override
    public void remove(State state) {
        if (state == State.PARALYSED) {
            paused = false;
            return;
        }
        super.remove(state);
    }

    @Override
    public void onComplete(Animation anim) {
        super.onComplete(anim);

        if (anim == baneDeadEnemy) {
            idle();
            return;
        }

        if (anim == spurnShieldNuptial) {
            nuptialLock = false;

            Runnable callback = onNuptialComplete;
            onNuptialComplete = null;
            if (callback != null) {
                callback.run();
            } else {
                idle();
            }
        }
    }

    /** 施法成功/失败/圣光形态的循环动画。 */
    public boolean isCastingResultAnim() {
        return currentAnim == zapSuccess
                || currentAnim == zapFail
                || currentAnim == spurnShieldNuptial;
    }

    @Override
    public void play(Animation anim) {
        // 告死之嫁动画期间，禁止其他动画抢占
        if (nuptialLock && anim != spurnShieldNuptial) {
            return;
        }

        currentAnim = anim;
        super.play(anim);
    }

    /** 让告死之嫁动画独占播放，播完后再执行传入的回调。 */
    public void setOnNuptialComplete(Runnable callback) {
        onNuptialComplete = callback;
    }

    /** 告死之嫁专用的锁定播放。 */
    public void nuptialLocked() {
        nuptialLock = true;
        play(spurnShieldNuptial);
    }

    /** 麻痹动画是否正在循环播放。 */
    public boolean isCastingParalysis() {
        return currentAnim == zapParalysis;
    }

    /** 施法读条（循环）。 */
    public void castStart() {
        play(zap);
    }

    public void castSuccess() {
        play(zapSuccess);
    }

    public void castFail() {
        play(zapFail);
    }

    public void castParalysis() {
        // 麻痹优先打断告死之嫁的独占动画
        nuptialLock = false;
        onNuptialComplete = null;
        play(zapParalysis);
    }

    /** 决绝告死之嫁 / 三圣祝福之礼的圣光形态。 */
    public void nuptial() {
        play(spurnShieldNuptial);
    }

    /** 抵抗敌厄之拒的爆发动画。 */
    public void bane() {
        play(baneDeadEnemy);
    }

    /** 兼容旧调用名。 */
    public void playBaneDeadEnemy() {
        play(baneDeadEnemy);
    }
}
