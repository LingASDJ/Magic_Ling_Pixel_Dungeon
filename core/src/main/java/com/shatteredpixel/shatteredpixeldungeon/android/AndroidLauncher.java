package com.shatteredpixel.shatteredpixeldungeon.android;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.ViewConfiguration;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.window.OnBackInvokedCallback;
import android.window.OnBackInvokedDispatcher;

import androidx.annotation.RequiresPermission;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationBase;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.backends.android.AndroidAudio;
import com.badlogic.gdx.backends.android.AndroidGraphics;
import com.badlogic.gdx.backends.android.AsynchronousAndroidAudio;
import com.badlogic.gdx.backends.android.DefaultAndroidInput;
import com.badlogic.gdx.backends.android.surfaceview.FillResolutionStrategy;
import com.badlogic.gdx.backends.android.surfaceview.GLSurfaceView20;
import com.badlogic.gdx.backends.android.surfaceview.ResolutionStrategy;
import com.badlogic.gdx.graphics.g2d.freetype.FreeType;
import com.badlogic.gdx.utils.GdxNativesLoader;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.google.firebase.FirebaseApp;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.scenes.TexturePackScene;
import com.shatteredpixel.shatteredpixeldungeon.services.news.News;
import com.shatteredpixel.shatteredpixeldungeon.services.news.NewsImpl;
import com.shatteredpixel.shatteredpixeldungeon.ui.Button;
import com.shatteredpixel.shatteredpixeldungeon.update.UpdateImpl;
import com.shatteredpixel.shatteredpixeldungeon.update.Updates;
import com.watabou.input.KeyEvent;
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

        //Shattered still overrides the back gesture behaviour, but we need to do it in a new way
        // (API added in Android 13, functionality enforced in Android 16)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            //we post this to a runnable so that it's delayed and overrides
            // default GDX back handling, which only sends a key down event
            runnables.add(new Runnable() {
                @Override
                public void run() {
                    getOnBackInvokedDispatcher().registerOnBackInvokedCallback(OnBackInvokedDispatcher.PRIORITY_DEFAULT, new OnBackInvokedCallback() {
                        @Override
                        public void onBackInvoked() {
                            KeyEvent.addKeyEvent(new KeyEvent(Input.Keys.BACK, true));
                            KeyEvent.addKeyEvent(new KeyEvent(Input.Keys.BACK, false));
                        }
                    });
                }
            });
        }

        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        config.depth = 0;

        //we manage this ourselves
        config.useImmersiveMode = false;

        config.useCompass = false;
        config.useAccelerometer = false;

        if (support == null) support = new AndroidPlatformSupport();
        else                 support.reloadGenerators();

        support.updateSystemUI();

        Button.longClick = ViewConfiguration.getLongPressTimeout()/1000f;

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
    protected AndroidGraphics createGraphics(AndroidApplicationConfiguration config) {
        return new AndroidGraphics(this, config,
                config.resolutionStrategy == null ? new FillResolutionStrategy() : config.resolutionStrategy) {
            @Override
            protected GLSurfaceView20 createGLSurfaceView(AndroidApplicationBase application, ResolutionStrategy resolutionStrategy) {
                if (!checkGL20()) throw new GdxRuntimeException("libGDX requires OpenGL ES 2.0");

                GLSurfaceView.EGLConfigChooser configChooser = getEglConfigChooser();
                GLSurfaceView20 view = new GLSurfaceView20(application.getContext(), resolutionStrategy, config.useGL30 ? 3 : 2) {
                    @Override
                    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
                        if (outAttrs != null) {
                            outAttrs.imeOptions = outAttrs.imeOptions | EditorInfo.IME_FLAG_NO_EXTRACT_UI;
                            if (onscreenKeyboardType == Input.OnscreenKeyboardType.Default) {
                                // The trick is to omit InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD here
                                outAttrs.inputType = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS;
                            } else {
                                outAttrs.inputType = DefaultAndroidInput.getAndroidInputType(onscreenKeyboardType, true);
                            }
                        }

                        // Delegate to super class without outAttrs to modify
                        return super.onCreateInputConnection(null);
                    }
                };

                if (configChooser != null)
                    view.setEGLConfigChooser(configChooser);
                else
                    view.setEGLConfigChooser(config.r, config.g, config.b, config.a, config.depth, config.stencil);

                view.setRenderer(this);
                return view;
            }
        };
    }

    @Override
    public void onMultiWindowModeChanged(boolean isInMultiWindowMode) {
        super.onMultiWindowModeChanged(isInMultiWindowMode);
        support.updateSystemUI();
    }
}
