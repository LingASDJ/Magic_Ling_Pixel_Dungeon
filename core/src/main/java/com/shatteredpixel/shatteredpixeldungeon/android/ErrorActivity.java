package com.shatteredpixel.shatteredpixeldungeon.android;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.BarUtils;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.color.DynamicColors;
import com.google.android.material.color.DynamicColorsOptions;
import com.google.android.material.snackbar.Snackbar;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.CrashHandler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cat.ereza.customactivityoncrash.CustomActivityOnCrash;
import cat.ereza.customactivityoncrash.config.CaocConfig;

public class ErrorActivity extends AppCompatActivity {

    private String errorMsg;
    private final ExecutorService ioExecutor = Executors.newSingleThreadExecutor();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ✅第一步：立刻加载布局，优先渲染界面，消除延迟
        setContentView(R.layout.activity_error);

        // ✅主题、状态栏放在setContentView之后
        DynamicColors.applyToActivityIfAvailable(this,
                new DynamicColorsOptions.Builder().setPrecondition((activity, theme) -> true).build());
        BarUtils.transparentStatusBar(this);
        BarUtils.setStatusBarLightMode(this, true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            BarUtils.setNavBarColor(this, 0x00000000);
        }

        // 获取崩溃文本
        errorMsg = CustomActivityOnCrash.getAllErrorDetailsFromIntent(this, getIntent());

        // ✅UI立刻初始化，用户马上看到界面，不再等待IO保存
        initializeUI();

        // ✅IO保存放到后台线程，不阻塞UI，移除ProgressDialog
        saveCrashInBackground(errorMsg);
    }

    private void saveCrashInBackground(String errorMessage) {
        ioExecutor.execute(() -> {
            try {
                RuntimeException ex = new RuntimeException(errorMessage);
                java.util.List<StackTraceElement> filtered = new java.util.ArrayList<>();
                for (StackTraceElement element : Thread.currentThread().getStackTrace()) {
                    if (!element.getClassName().contains("ErrorActivity")
                            && !element.getClassName().contains("CustomActivityOnCrash")) {
                        filtered.add(element);
                    }
                }
                ex.setStackTrace(filtered.toArray(new StackTraceElement[0]));

                CrashHandler handler = CrashHandler.getInstance();
                handler.init();
                handler.saveCrashInfo(ErrorActivity.this, Thread.currentThread(), ex);

                runOnUiThread(() -> Toast.makeText(ErrorActivity.this,
                        R.string.log_saved, Toast.LENGTH_SHORT).show());
            } catch (Exception e) {
                android.util.Log.e("ErrorActivity", "Failed to save crash info", e);
                runOnUiThread(() -> Toast.makeText(ErrorActivity.this,
                        R.string.log_save_failed, Toast.LENGTH_SHORT).show());
            }
        });
    }

    private void initializeUI() {
        final MaterialToolbar toolbar = findViewById(R.id.materialToolbar);
        setSupportActionBar(toolbar);
        final TextView textView = findViewById(R.id.error_info_text_view);
        textView.setText(errorMsg);

        final Button restartButton = findViewById(R.id.restart_button);
        restartButton.setOnClickListener(v -> {
            CaocConfig config = CustomActivityOnCrash.getConfigFromIntent(getIntent());
            if (config == null) {
                Snackbar.make(v, R.string.no_configuration_found, Snackbar.LENGTH_LONG).show();
                return;
            }
            CustomActivityOnCrash.restartApplication(ErrorActivity.this, config);
        });

        final Button copyButton = findViewById(R.id.copy_button);
        copyButton.setOnClickListener(v -> {
            ClipboardManager systemService = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            ClipData mClipData = ClipData.newPlainText("crash_log", errorMsg);
            systemService.setPrimaryClip(mClipData);
            Snackbar.make(v, R.string.copy_complete, Snackbar.LENGTH_LONG).show();
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ioExecutor.shutdown();
    }
}
