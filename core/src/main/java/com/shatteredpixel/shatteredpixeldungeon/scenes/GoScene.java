package com.shatteredpixel.shatteredpixeldungeon.scenes;

import static com.shatteredpixel.shatteredpixeldungeon.BGMPlayer.playBGM;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.TPDoor;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.utils.GameMath;

import net.iharder.Base64;

import java.io.IOException;

public class GoScene extends PixelScene {

    private static float alpha = 0f;
    private static float sec = 0f;
    private static boolean played = false;
    private static boolean done = false;

    private Image gdx;
    private Image ansdoShip;
    private float originalGdxWidth;
    private float originalAnsdoShipWidth;
    private static float gdxScaleFactor = 1.0f;
    private static float ansdoShipScaleFactor = 1.0f;

    private static final float RIPPLE_AMPLITUDE = 0.5f; // 水波振幅
    private static final float RIPPLE_FREQUENCY = 1f; // 水波频率
    private static final float RIPPLE_SPEED = 4f; // 水波速度

    public static void isClassExistInSourceCode() {
        String base64ClassName = Messages.get(TPDoor.class,"ymfzzty");
        byte[] decodedBytes = new byte[0];
        try {
            decodedBytes = Base64.decode(base64ClassName);
        } catch (IOException ignored) {}
        String className = new String(decodedBytes);

        try {
            //大哥里面请
            Class.forName(className);
            SPDSettings.Cheating(true);
        } catch (ClassNotFoundException e) {
            SPDSettings.Cheating(false);
        }
    }

    @Override
    public void create() {
        super.create();
        isClassExistInSourceCode();
        if (SPDSettings.splashScreen() < 1 || done) {
            ShatteredPixelDungeon.switchForceFade(WelcomeScene.class);
            return;
        } else {
            playBGM(Assets.Music.GO, true);
        }

        PixelScene.uiCamera.visible = false;

        int w = Camera.main.width;
        int h = Camera.main.height;

        gdx = new Image(Assets.Splashes.GDX) {
            float time = 0f;

            @Override
            public void update() {
                if (SPDSettings.splashScreen() > 0 && sec < 1f) {
                    alpha(GameMath.gate(0, alpha += Game.elapsed, 1));
                    sec += Game.elapsed;
                } else if (SPDSettings.splashScreen() > 1 && sec < 2f) {
                    alpha(alpha = 1f);
                    sec += Game.elapsed;
                } else if (sec < (SPDSettings.splashScreen() > 1 ? 3f : 2f)) {
                    alpha(GameMath.gate(0, alpha -= Game.elapsed, 1));
                    sec += Game.elapsed;
                    gdxScaleFactor += Game.elapsed / 10.f;
                    gdx.scale.set(w / originalGdxWidth / (landscape() ? 4f : 3f) * gdxScaleFactor);
                    gdx.x = (w - gdx.width()) / 2f;
                    gdx.y = (float) ((h - gdx.height()) / 2f + RIPPLE_AMPLITUDE * Math.sin(time * RIPPLE_FREQUENCY));
                    PixelScene.align(gdx);
                    time += Game.elapsed * RIPPLE_SPEED;
                } else if (SPDSettings.splashScreen() > 1 && sec < 4f) {
                    if (gdx.visible) gdx.visible = false;
                    sec += Game.elapsed;
                } else if (!ansdoShip.visible) {
                    if (gdx.visible) gdx.visible = false;
                    ansdoShip.visible = true;
                }
            }
        };
        originalGdxWidth = gdx.width();
        gdx.scale.set(w / gdx.width() / (landscape() ? 4f : 3f) * gdxScaleFactor);

        gdx.x = (w - gdx.width()) / 2f;
        gdx.y = (h - gdx.height()) / 2f;
        PixelScene.align(gdx);
        if (sec >= 3f) gdx.visible = false;
        add(gdx);

        ansdoShip = new Image(Assets.Splashes.ANSDOSHIP) {
            float time = 0f;

            @Override
            public void update() {
                if (!visible) return;
                if (sec >= (SPDSettings.splashScreen() > 1 ? 4f : 2f) && sec < (SPDSettings.splashScreen() > 1 ? 5f :
                        3f)) {
                    if (!played && SPDSettings.splashScreen() > 1) {
                        played = true;
                    }
                    alpha(GameMath.gate(0, alpha += Game.elapsed, 1));
                    sec += Game.elapsed;
                } else if (SPDSettings.splashScreen() > 1 && sec < 7f) {
                    alpha(alpha = 1f);
                    sec += Game.elapsed;
                } else if (sec < (SPDSettings.splashScreen() > 1 ? 8f : 4f)) {
                    alpha(GameMath.gate(0, alpha -= Game.elapsed, 1));
                    sec += Game.elapsed / 2.9f;
                    ansdoShipScaleFactor += Game.elapsed / 10.f;
                    ansdoShip.scale.set(w / originalAnsdoShipWidth / (landscape() ? 3f : 2f) * ansdoShipScaleFactor);
                    ansdoShip.x = (w - ansdoShip.width()) / 2f;
                    ansdoShip.y = (float) ((h - ansdoShip.height()) / 2f + RIPPLE_AMPLITUDE * Math.PI * Math.sin(time *
                            RIPPLE_FREQUENCY));
                    PixelScene.align(ansdoShip);
                    time += Game.elapsed * RIPPLE_SPEED;
                } else if (!done) {
                    done = true;
                    if (ansdoShip.visible) ansdoShip.visible = false;
                    ShatteredPixelDungeon.switchForceFade(WelcomeScene.class);
                }
            }
        };
        originalAnsdoShipWidth = ansdoShip.width();
        ansdoShip.scale.set(w / ansdoShip.width() / (landscape() ? 3f : 2f) * ansdoShipScaleFactor);

        ansdoShip.x = (w - ansdoShip.width()) / 2f;
        ansdoShip.y = (h - ansdoShip.height()) / 2f;
        PixelScene.align(ansdoShip);
        ansdoShip.visible = false;
        add(ansdoShip);

    }
}
