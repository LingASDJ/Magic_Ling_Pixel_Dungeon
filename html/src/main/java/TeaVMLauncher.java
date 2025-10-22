package com.shatteredpixel.shatteredpixeldungeon.html;

import com.badlogic.gdx.Files;
import com.watabou.noosa.Game;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.github.xpenatan.gdx.backends.teavm.TeaApplicationConfiguration;
import com.github.xpenatan.gdx.backends.teavm.TeaApplication;
import com.watabou.utils.FileUtils;

public class TeaVMLauncher {

    public static void main(String[] args) {
        TeaApplicationConfiguration config = new TeaApplicationConfiguration("canvas");
        config.width = 0;
        config.height = 0;
        config.useGL30 = true;
        config.preloadListener = assetLoader -> {
            try {
                assetLoader.loadScript("freetype.js");
            } catch (Exception e) {
                System.err.println("Error loading freetype.js: " + e.getMessage());
            }
        };

        initializeServices();
        FileUtils.setDefaultFileProperties(Files.FileType.Local, "");

        try {

            HtmlPlatformSupport platformSupport = new HtmlPlatformSupport();

            platformSupport.setupClickListener();

            new TeaApplication(new ShatteredPixelDungeon(platformSupport), config);

        } catch (Exception e) {
            System.err.println("Error launching TeaApplication: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void initializeServices() {
        Game.version = "3.0.2";
        Game.versionCode = 833;
    }
}
