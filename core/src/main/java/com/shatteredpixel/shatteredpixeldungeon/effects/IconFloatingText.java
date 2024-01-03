package com.shatteredpixel.shatteredpixeldungeon.effects;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.RenderedText;
import com.watabou.noosa.TextureFilm;
import com.watabou.utils.Callback;
import com.watabou.utils.SparseArray;

import java.util.ArrayList;

public class IconFloatingText extends RenderedTextBlock {
    private static final float LIFESPAN = 1.0f;
    private static final float DISTANCE = 16.0f;
    public static final int ICON_SIZE = 7;
    private Image icon;
    private boolean iconLeft;
    private float timeLeft;
    private int key;
    public static TextureFilm iconFilm = new TextureFilm(Assets.Effects.TEXT_ICONS, 7, 7);
    public static int NO_ICON = -1;
    public static int PHYS_DMG = 0;
    public static int PHYS_DMG_NO_BLOCK = 1;
    public static int MAGIC_DMG = 2;
    public static int PICK_DMG = 3;
    public static int HUNGER = 5;
    public static int BURNING = 6;
    public static int SHOCKING = 7;
    public static int FROST = 8;
    public static int WATER = 9;
    public static int BLEEDING = 10;
    public static int TOXIC = 11;
    public static int CORROSION = 12;
    public static int POISON = 13;
    public static int OOZE = 14;
    public static int DEFERRED = 15;
    public static int CORRUPTION = 16;
    public static int AMULET = 17;
    public static int HEALING = 18;
    public static int SHIELDING = 19;
    public static int EXPERIENCE = 20;
    public static int STRENGTH = 21;
    public static int GOLD = 23;
    public static int ENERGY = 24;

    public static int HALO = 25;
    public static int HEARTDEMON = 26;

    public static int REDGAS = 27;

    public static int HEARTDEMON_DMG = 28;

    public static int HUNGRY_EXTRA = 29;

    public static int HUNGRY_EXTRA_HEAL = 30;

    private static final SparseArray<ArrayList<IconFloatingText>> stacks = new SparseArray<>();

    public IconFloatingText() {
        super(9 * PixelScene.defaultZoom);
        this.key = -1;
        setHightlighting(false);
    }

    @Override // com.watabou.noosa.Group, com.watabou.noosa.Gizmo
    public void update() {
        super.update();
        if (this.timeLeft >= 0.0f) {
            float f = this.timeLeft - Game.elapsed;
            this.timeLeft = f;
            if (f <= 0.0f) {
                kill();
                return;
            }
            float p = this.timeLeft;
            alpha(p > 0.5f ? 1.0f : p * 2.0f);
            float yMove = 16.0f * Game.elapsed;
            y -= yMove;
            for (RenderedText t : this.words) {
                t.y -= yMove;
            }
            if (this.icon != null) {
                this.icon.alpha(p > 0.5f ? 1.0f : p * 2.0f);
                this.icon.y -= yMove;
            }
        }
    }

    @Override
    // com.shatteredpixel.shatteredpixeldungeon.p002ui.RenderedTextBlock, com.watabou.noosa.p003ui.Component
    public synchronized void layout() {
        super.layout();
        if (this.icon != null) {
            if (this.iconLeft) {
                this.icon.x = left();
            } else {
                this.icon.x = (left() + width()) - this.icon.width();
            }
            this.icon.y = top();
            PixelScene.align(this.icon);
        }
    }

    @Override // com.watabou.noosa.p003ui.Component
    public float width() {
        float width = super.width();
        if (this.icon != null) {
            width += this.icon.width() - 0.5f;
        }
        return width;
    }

    @Override // com.watabou.noosa.Group, com.watabou.noosa.Gizmo
    public void kill() {
        if (this.key != -1) {
            synchronized (stacks) {
                stacks.get(this.key).remove(this);
            }
            this.key = -1;
        }
        super.kill();
    }

    @Override // com.watabou.noosa.Group, com.watabou.noosa.Gizmo
    public void destroy() {
        kill();
        super.destroy();
    }

    public void reset(float x, float y, String text, int color, int iconIdx, boolean left) {
        revive();
        zoom(1.0f / PixelScene.defaultZoom);
        text(text);
        hardlight(color);
        if (iconIdx != NO_ICON) {
            this.icon = new Image(Assets.Effects.TEXT_ICONS);
            this.icon.frame(iconFilm.get(Integer.valueOf(iconIdx)));
            add(this.icon);
            this.iconLeft = left;
            if (this.iconLeft) {
                align(3);
            }
        } else {
            this.icon = null;
        }
        setPos(PixelScene.align(Camera.main, x - (width() / 2.0f)), PixelScene.align(Camera.main, y - height()));
        this.timeLeft = 1.0f;
    }

    public static void show(float x, float y, String text, int color) {
        show(x, y, -1, text, color, -1, false);
    }

    public static void show(float x, float y, int key, String text, int color) {
        show(x, y, key, text, color, -1, false);
    }

    public static void show(final float x, final float y, final int key, final String text, final int color, final int iconIdx, final boolean left) {
        Game.runOnRenderThread(new Callback() {
            // from class: com.shatteredpixel.fshatteredpixeldungeon.effects.FloatingText.1
            @Override
            // com.watabou.utils.Callback
            public void call() {
                IconFloatingText txt = GameScene.iconstatus();
                if (txt != null) {
                    txt.reset(x, y, text, color, iconIdx, left);
                    if (key != -1) {
                        IconFloatingText.push(txt, key);
                    }
                }
            }
        });
    }

    public static void push(IconFloatingText txt, int key) {
        synchronized (stacks) {
            txt.key = key;
            ArrayList<IconFloatingText> stack = stacks.get(key);
            if (stack == null) {
                stack = new ArrayList<>();
                stacks.put(key, stack);
            }
            if (stack.size() > 0) {
                IconFloatingText below = txt;
                int numBelow = 0;
                for (int aboveIndex = stack.size() - 1; aboveIndex >= 0; aboveIndex--) {
                    numBelow++;
                    IconFloatingText above = stack.get(aboveIndex);
                    if (above.bottom() + 4.0f <= below.top()) {
                        break;
                    }
                    above.setPos(above.left(), (below.top() - above.height()) - 4.0f);
                    above.timeLeft = Math.min(above.timeLeft, 1.0f - (numBelow / 5.0f));
                    above.timeLeft = Math.max(above.timeLeft, 0.0f);
                    below = above;
                }
            }
            stack.add(txt);
        }
    }
}
