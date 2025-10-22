package com.shatteredpixel.shatteredpixeldungeon.html;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.PixmapPacker;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.watabou.input.ControllerHandler;
import com.watabou.utils.PlatformSupport;

import org.teavm.jso.JSBody;

import java.io.File;
import java.util.HashMap;
import java.util.regex.Pattern;

public class HtmlPlatformSupport extends PlatformSupport {

    @Override
    public void updateDisplaySize() {
        if (Gdx.app == null || Gdx.graphics == null) {
            System.err.println("Gdx is not initialized yet. Skipping display size update.");
            return;
        }

        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    @Override
    public void updateSystemUI() {
        Gdx.app.postRunnable( new Runnable() {
            @Override
            public void run() {
                Object canvas = getCanvasElement("canvas");
                if (SPDSettings.fullscreen()) {
                    System.out.println("Requesting fullscreen.");
                    requestFullscreen("canvas");
                } else {
                    System.out.println("Exiting fullscreen.");
                    exitFullscreen();
                    setCanvasSize("canvas", SPDSettings.windowResolution().x, SPDSettings.windowResolution().y);
                }
            }
        });
    }

    @JSBody(script = ""
            + "document.body.addEventListener('click', function() {"
            + "    console.log('User clicked. Triggering fullscreen and audio...');"
            + "    document.getElementById('canvas').requestFullscreen();"
            + "    if (!window.audioContext) {"
            + "        window.audioContext = new (window.AudioContext || window.webkitAudioContext)();"
            + "    }"
            + "    window.audioContext.resume().then(() => {"
            + "        console.log('AudioContext resumed successfully.');"
            + "    }).catch((err) => {"
            + "        console.error('Failed to resume AudioContext:', err);"
            + "    });"
            + "}, { once: true });")
    public static native void setupClickListener();

    @JSBody(params = {"id"}, script = "document.getElementById(id).requestFullscreen();")
    private static native void requestFullscreen(String id);

    @JSBody(params = {"id"}, script = "return document.getElementById(id);")
    private static native Object getCanvasElement(String id);

    @JSBody(params = {}, script = "document.exitFullscreen();")
    private static native void exitFullscreen();

    @JSBody(params = {"id", "width", "height"}, script = "var canvas = document.getElementById(id); canvas.width = width; canvas.height = height;")
    private static native void setCanvasSize(String id, int width, int height);

    @Override
    public boolean connectedToUnmeteredNetwork() {
        return true;
    }

    @Override
    public boolean supportsVibration() {
        return ControllerHandler.vibrationSupported();
    }

    private static FreeTypeFontGenerator basicFontGenerator;

    @Override
    public void setupFontGenerators(int pageSize, boolean systemfont) {
        //don't bother doing anything if nothing has changed
        if (fonts != null && this.pageSize == pageSize && this.systemfont == systemfont) {
            return;
        }
        this.pageSize = pageSize;
        this.systemfont = systemfont;

        resetGenerators(false);
        fonts = new HashMap<>();

        basicFontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/pixel_font.ttf"));

        fonts.put(basicFontGenerator, new HashMap<>());

        packer = new PixmapPacker(pageSize, pageSize, Pixmap.Format.RGBA8888, 1, false);
    }

    @Override
    protected FreeTypeFontGenerator getGeneratorForString( String input ) {
        return basicFontGenerator;
    }

    private final Pattern regularsplitter = Pattern.compile("(?<=\n)|(?=\n)|(?<=_)|(?=_)|(?<=\\*\\*)|(?=\\*\\*)");

    private final Pattern regularsplitterMultiline = Pattern.compile("(?<= )|(?= )|(?<=\n)|(?=\n)|(?<=_)|(?=_)|(?<=\\*\\*)|(?=\\*\\*)");

    @Override
    public String[] splitforTextBlock(String text, boolean multiline) {
        if (multiline) {
            return regularsplitterMultiline.split(text);
        } else {
            return regularsplitter.split(text);
        }
    }

    @Override
    public void updateGame(String url, UpdateCallback listener) {

    }

    @Override
    public void install(File file) {

    }
}