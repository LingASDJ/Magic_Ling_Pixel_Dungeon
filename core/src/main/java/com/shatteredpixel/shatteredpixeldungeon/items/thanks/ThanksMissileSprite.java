package com.shatteredpixel.shatteredpixeldungeon.items.thanks;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.watabou.noosa.Game;
import com.watabou.noosa.Visual;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Callback;
import com.watabou.utils.PointF;

public class ThanksMissileSprite extends ItemSprite {

    private static final float SPEED = 240f;  // 这里暂时写死240速度，后续要拓展后续再说吧
    private static final float MAX_DURATION = 3.0f;
    private static final float ARRIVAL_DIST = 2.0f;

    private float speed = SPEED;  // 可变速度
    private float max_duration = MAX_DURATION;

    private Char targetChar;
    private boolean tracking;
    private float elapsed;
    private Callback callback;
    private Emitter emitter;               // 新增：粒子发射器
    private Item missileItem;              // 保存Item引用，

    private void set(Item item) {
        if (item instanceof SniperSupport.FrostSnipeArrow
                || item instanceof SniperSupport.ShockSnipeArrow
                || item instanceof SniperSupport.BurnSnipeArrow) {
            speed *= 3f;
            max_duration /= 3f;
        }
    }

    public ThanksMissileSprite() {
        super();
    }

    public void reset(PointF fromPos, Char target, Item item, Callback listener) {
        set(item);
        revive();
        view(item, true);
        this.targetChar = target;
        this.tracking = true;
        this.callback = listener;
        this.elapsed = 0;
        this.missileItem = item;

        originToCenter();
        x = fromPos.x - width / 2f;
        y = fromPos.y - height / 2f;

        PointF toP = target.sprite.center();
        float dx = toP.x - (x + width / 2f);
        float dy = toP.y - (y + height / 2f);
        updateAngle(dx, dy);
        updateFrame();
        angularSpeed = 0;

        if (emitter != null) {
            emitter.killAndErase();   // 清理旧的发射器
            emitter = null;
        }
        if (item != null && item.emitter() != null) {
            emitter = item.emitter();
            if (parent != null) parent.add(emitter);
            emitter.pos(center());    // 传入 PointF
            emitter.fillTarget = false;
        }
    }

    /**
     * 重置并发射追踪导弹，同时附加粒子特效（如果Item提供）。
     */
    public void reset(Visual from, Char target, Item item, Callback listener) {
        set(item);
        revive();
        view(item, true);
        this.targetChar = target;
        this.tracking = true;
        this.callback = listener;
        this.elapsed = 0;
        this.missileItem = item;

        originToCenter();

        PointF fromP = from.center();
        x = fromP.x - width / 2f;
        y = fromP.y - height / 2f;

        PointF toP = target.sprite.center();
        float dx = toP.x - fromP.x;
        float dy = toP.y - fromP.y;
        updateAngle(dx, dy);
        updateFrame();
        angularSpeed = 0;

        // ----- 新增：初始化粒子发射器 -----
        if (emitter != null) {
            emitter.killAndErase();   // 清理旧的发射器
            emitter = null;
        }
        if (item != null && item.emitter() != null) {
            emitter = item.emitter();
            // 将发射器添加到场景中，并定位到当前精灵中心
            if (parent != null) parent.add(emitter);
            emitter.pos(center());    // 传入 PointF
            emitter.fillTarget = false;
        }
    }

    private void updateAngle(float dx, float dy) {
        float angleDeg = 135 - (float) (Math.atan2(dx, dy) / Math.PI * 180);
        if (dx >= 0) {
            angle = angleDeg;
            flipHorizontal = false;
        } else {
            angle = angleDeg + 90;
            flipHorizontal = true;
        }
    }

    @Override
    public void update() {
        if (tracking) {
            if (targetChar == null || !targetChar.isAlive()) {
                kill();
                if (callback != null) callback.call();
                return;
            }

            elapsed += Game.elapsed;
            if (elapsed > max_duration) {
                kill();
                if (callback != null) callback.call();
                return;
            }

            PointF targetPos = targetChar.sprite.center();
            float cx = x + width / 2f;
            float cy = y + height / 2f;
            float dx = targetPos.x - cx;
            float dy = targetPos.y - cy;
            float dist = (float) Math.sqrt(dx * dx + dy * dy);

            if (dist < ARRIVAL_DIST) {
                kill();
                if (callback != null) callback.call();
                return;
            }

            float step = Math.min(speed * Game.elapsed, dist);
            if (dist > 0.001f) {
                x += (dx / dist) * step;
                y += (dy / dist) * step;
            }

            updateAngle(dx, dy);
            updateFrame();

            // ----- 更新粒子发射器的位置 -----
            if (emitter != null && emitter.parent != null) {
                emitter.pos(center());
            }

            super.update();
        } else {
            super.update();
        }
    }

    @Override
    public void revive() {
        super.revive();
        tracking = false;
        targetChar = null;
        elapsed = 0;
        callback = null;
        missileItem = null;
        // emitter 会在 reset 中处理
    }

    @Override
    public void kill() {
        // 移除粒子发射器
        if (emitter != null) {
            emitter.killAndErase();
            emitter = null;
        }
        super.kill();
    }
}