package com.shatteredpixel.shatteredpixeldungeon.android;

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
    public void updateGame(String url, UpdateCallback listener) {

    }

    @Override
    public void install(File file) {

    }

    public void refreshSafeInsets() {

    }
}
