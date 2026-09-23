package com.shatteredpixel.shatteredpixeldungeon.android;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.ScrollView;
import android.widget.TextView;
import android.window.OnBackInvokedCallback;
import android.window.OnBackInvokedDispatcher;

import androidx.annotation.RequiresPermission;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.view.ContextThemeWrapper;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
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
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.CrashHandler;
import com.shatteredpixel.shatteredpixeldungeon.scenes.BackupSaveScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CrashReportScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.TexturePackScene;
import com.shatteredpixel.shatteredpixeldungeon.services.news.News;
import com.shatteredpixel.shatteredpixeldungeon.services.news.NewsImpl;
import com.shatteredpixel.shatteredpixeldungeon.ui.Button;
import com.shatteredpixel.shatteredpixeldungeon.update.UpdateImpl;
import com.shatteredpixel.shatteredpixeldungeon.update.Updates;
import com.watabou.input.KeyEvent;
import com.watabou.noosa.Game;
import com.watabou.utils.FileUtils;

import java.io.File;

import cat.ereza.customactivityoncrash.CustomActivityOnCrash;
import cat.ereza.customactivityoncrash.config.CaocConfig;

public class AndroidLauncher extends AndroidApplication {

    public static AndroidApplication instance;

    private static AndroidPlatformSupport support;

    public static FirebaseAnalytics mFirebaseAnalyticsRecords;

    @RequiresPermission(allOf = {Manifest.permission.INTERNET, Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.WAKE_LOCK})
    @SuppressLint({"SetTextI18n", "RestrictedApi"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 配置自定义崩溃处理
        CaocConfig.Builder.create()
                .backgroundMode(CaocConfig.BACKGROUND_MODE_SHOW_CUSTOM) //default: CaocConfig.BACKGROUND_MODE_SHOW_CUSTOM
                .minTimeBetweenCrashesMs(2000) //default: 3000
                .errorActivity(ErrorActivity.class) //default: null (default error activity)
                .apply();
        CustomActivityOnCrash.install(this);

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
        CrashHandler.getInstance().startAnrMonitor();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == TexturePackScene.REQUEST_CODE_IMPORT_PACK) {
            TexturePackScene.handleActivityResult(requestCode, resultCode, data);
        }

        Uri uri = data != null ? data.getData() : null;
        if(requestCode == 9001){
            BackupSaveScene.handleSAFSaveResult(this, resultCode, uri);
        } else if(requestCode == 9002) {
            BackupSaveScene.handleSAFImportResult(this, resultCode, uri);
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
        CrashHandler.getInstance().setAppInForeground(true);
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
    protected void onDestroy() {
        CrashHandler.getInstance().stopAnrMonitor();
        super.onDestroy();
    }


    @Override protected void onPause()  {
        super.onPause();  CrashHandler.getInstance().setAppInForeground(false);
    }


    /**
     * 显示原生崩溃详情对话框。
     *
     * @param crashText    崩溃文本内容
     * @param logFileName  当前这条日志的文件名（仅文件名，相对 crash_logs 目录，可为 null）
     */
    public static void showNativeCrashDialog(final String crashText, final String logFileName) {
        if (!(instance instanceof AndroidLauncher)) {
            return;
        }
        final AndroidLauncher launcher = (AndroidLauncher) instance;

        launcher.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final Context dialogContext = new ContextThemeWrapper(launcher,
                        com.google.android.material.R.style.Theme_MaterialComponents_DayNight_Dialog_Alert);

                ScrollView scrollView = new ScrollView(dialogContext);
                scrollView.setPadding(dp2px(16), dp2px(8), dp2px(16), dp2px(8));
                scrollView.setFillViewport(true);

                TextView textView = new TextView(dialogContext);
                textView.setText(crashText);
                textView.setTextSize(10);
                textView.setTypeface(android.graphics.Typeface.MONOSPACE);
                textView.setMaxLines(Integer.MAX_VALUE);
                textView.setVerticalScrollBarEnabled(true);
                scrollView.addView(textView);

                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(dialogContext);
                builder.setTitle(dialogContext.getString(R.string.crash_dialog_title));
                builder.setView(scrollView);
                builder.setCancelable(true);

                // 先持有dialog引用，onClick可以拿到AlertDialog实例
                final AlertDialog alertDialog = builder.create();

                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE,
                        dialogContext.getString(R.string.crash_dialog_copy),
                        (dialog, which) -> {
                            ClipboardManager cm = (ClipboardManager) launcher.getSystemService(Context.CLIPBOARD_SERVICE);
                            cm.setPrimaryClip(ClipData.newPlainText("crash_log", crashText));
                            View decorView = alertDialog.getWindow() != null ? alertDialog.getWindow().getDecorView() : null;
                            if (decorView != null) {
                                Snackbar snackbar = Snackbar.make(decorView,
                                        dialogContext.getString(R.string.crash_copy_success),
                                        Snackbar.LENGTH_SHORT);
                                snackbar.setBackgroundTint(0xFF323232);
                                snackbar.setTextColor(0xFFFFFFFF);
                                snackbar.show();
                            }
                        });

                alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL,
                        dialogContext.getString(R.string.crash_dialog_delete),
                        (dialog, which) -> {
                            deleteCrashLog(launcher, logFileName);
                            // 删除后回到游戏线程，重建崩溃报告列表（与桌面端删除行为一致）
                            if (Gdx.app != null) {
                                Gdx.app.postRunnable(() -> {
                                    if (ShatteredPixelDungeon.scene() instanceof CrashReportScene) {
                                        ShatteredPixelDungeon.switchNoFade(CrashReportScene.class);
                                    }
                                });
                            }
                        });

                alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE,
                        dialogContext.getString(R.string.crash_dialog_close),
                        (dialog, which) -> {});

                alertDialog.show();
            }
        });
    }

    /**
     * 删除指定的一条崩溃/ANR日志（crash_logs 目录下的单个 .log 文件）。
     * 仅接受纯文件名，带路径分隔符的一律忽略，防止路径穿越。
     */
    private static void deleteCrashLog(Context context, String logFileName) {
        try {
            if (logFileName == null || logFileName.isEmpty()) {
                return;
            }
            // 防御：只允许纯文件名，且必须以 .log 结尾
            if (logFileName.contains("/") || logFileName.contains("\\")
                    || !logFileName.endsWith(CrashHandler.CRASH_FILE_EXTENSION)) {
                return;
            }
            File crashDir = new File(context.getFilesDir(), CrashHandler.CRASH_DIR);
            File target = new File(crashDir, logFileName);
            if (target.isFile()) {
                //noinspection ResultOfMethodCallIgnored
                target.delete();
            }
        } catch (Exception ignored) {
            // 删除失败不影响崩溃对话框本身
        }
    }

    private static int dp2px(int dp){
        if(instance == null) return dp;
        float density = instance.getResources().getDisplayMetrics().density;
        return (int)(dp * density + 0.5f);
    }



    @Override
    public void onMultiWindowModeChanged(boolean isInMultiWindowMode) {
        super.onMultiWindowModeChanged(isInMultiWindowMode);
        support.updateSystemUI();
    }
}
