package com.shatteredpixel.shatteredpixeldungeon.android;

import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.watabou.utils.PlatformSupport;

import java.io.File;

public class AndroidPlatformSupport extends PlatformSupport {
    @Override
    public void updateDisplaySize() {

    }

    @Override
    public void updateSystemUI() {

    }

    @Override
    public boolean connectedToUnmeteredNetwork() {
        return false;
    }

    @Override
    public boolean supportsVibration() {
        return false;
    }

    @Override
    public void setupFontGenerators(int pageSize, boolean systemFont) {

    }

    @Override
    protected FreeTypeFontGenerator getGeneratorForString(String input) {
        return null;
    }

    @Override
    public void updateGame(String url, UpdateCallback listener) {

    }

    @Override
    public void install(File file) {

    }

    public void refreshSafeInsets() {

    }
}
