package com.shatteredpixel.shatteredpixeldungeon.android;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresPermission;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.backends.android.AndroidAudio;
import com.badlogic.gdx.backends.android.AsynchronousAndroidAudio;
import com.badlogic.gdx.graphics.g2d.freetype.FreeType;
import com.badlogic.gdx.utils.GdxNativesLoader;
import com.google.firebase.FirebaseApp;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.scenes.TexturePackScene;
import com.shatteredpixel.shatteredpixeldungeon.services.news.News;
import com.shatteredpixel.shatteredpixeldungeon.services.news.NewsImpl;
import com.shatteredpixel.shatteredpixeldungeon.update.UpdateImpl;
import com.shatteredpixel.shatteredpixeldungeon.update.Updates;
import com.watabou.noosa.Game;
import com.watabou.utils.FileUtils;

import cat.ereza.customactivityoncrash.config.CaocConfig;

public class AndroidLauncher extends AndroidApplication {

    public static AndroidApplication instance;

    private static AndroidPlatformSupport support;

    public static FirebaseAnalytics mFirebaseAnalyticsRecords;

    @RequiresPermission(allOf = {Manifest.permission.INTERNET, Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.WAKE_LOCK})
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 配置自定义崩溃处理
        CaocConfig.Builder.create()
                .backgroundMode(CaocConfig.BACKGROUND_MODE_SHOW_CUSTOM) //default: CaocConfig.BACKGROUND_MODE_SHOW_CUSTOM
                .minTimeBetweenCrashesMs(2000) //default: 3000
                .errorActivity(ErrorActivity.class) //default: null (default error activity)
                .apply();


        FirebaseApp.initializeApp(this);

        mFirebaseAnalyticsRecords = FirebaseAnalytics.getInstance(this);
        try {
            GdxNativesLoader.load();
            FreeType.initFreeType();
        } catch (Exception e) {
            AndroidMissingNativesHandler.error = e;
            Intent intent = new Intent(this, AndroidMissingNativesHandler.class);
            startActivity(intent);
            finish();
            return;
        }

        // there are some things we only need to set up on first launch
        if (instance == null) {

            instance = this;

            try {
                Game.version = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            } catch (PackageManager.NameNotFoundException e) {
                Game.version = "???";
            }
            try {
                Game.versionCode = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
            } catch (PackageManager.NameNotFoundException e) {
                Game.versionCode = 0;
            }

            if (NewsImpl.supportsNews()) {
                Updates.service = UpdateImpl.getUpdateService();
                News.service = NewsImpl.getNewsService();
            }

            FileUtils.setDefaultFileProperties(Files.FileType.Local, "");

            // grab preferences directly using our instance first
            // so that we don't need to rely on Gdx.app, which isn't initialized yet.
            // Note that we use a different prefs name on android for legacy purposes,
            // this is the default prefs filename given to an android app (.xml is automatically added to it)
            SPDSettings.set(instance.getPreferences("ShatteredPixelDungeon"));

        } else {
            instance = this;
        }

        // set desired orientation (if it exists) before initializing the app.
        if (SPDSettings.landscape() != null) {
            instance.setRequestedOrientation(SPDSettings.landscape() ?
                    ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE :
                    ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        }

        mFirebaseAnalyticsRecords.setAnalyticsCollectionEnabled(SPDSettings.firebaseRecords());

        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        config.depth = 0;

        config.useCompass = false;
        config.useAccelerometer = false;

        if (support == null) support = new AndroidPlatformSupport();
        else support.reloadGenerators();

        support.updateSystemUI();

        initialize(new ShatteredPixelDungeon(support), config);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == TexturePackScene.REQUEST_CODE_IMPORT_PACK) {
            TexturePackScene.handleActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public AndroidAudio createAudio(Context context, AndroidApplicationConfiguration config) {
        return new AsynchronousAndroidAudio(context, config);
    }

    @Override
    protected void onResume() {
        // prevents weird rare cases where the app is running twice
        if (instance != this) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                finishAndRemoveTask();
            } else {
                finish();
            }
        }
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        // do nothing, game should catch all back presses
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        support.updateSystemUI();
    }

    @Override
    public void onMultiWindowModeChanged(boolean isInMultiWindowMode) {
        super.onMultiWindowModeChanged(isInMultiWindowMode);
        support.updateSystemUI();
    }
}
