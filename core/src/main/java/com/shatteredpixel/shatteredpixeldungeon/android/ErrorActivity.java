package com.shatteredpixel.shatteredpixeldungeon.android;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.AsyncTask;
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

import cat.ereza.customactivityoncrash.CustomActivityOnCrash;
import cat.ereza.customactivityoncrash.config.CaocConfig;

public class ErrorActivity extends AppCompatActivity {

    private String errorMsg;
    private ProgressDialog progressDialog;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 显示加载提示
        showLoadingDialog();

        // 首先初始化 CrashHandler
        CrashHandler handler = CrashHandler.getInstance();
        handler.init();

        DynamicColors.applyToActivityIfAvailable(this, new DynamicColorsOptions.Builder().setPrecondition((activity, theme) -> true).build());
        BarUtils.transparentStatusBar(this);
        BarUtils.setStatusBarLightMode(this, true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            BarUtils.setNavBarColor(this, 0x00000000);
        }
        setContentView(R.layout.activity_error);

        // 获取错误信息
        errorMsg = CustomActivityOnCrash.getAllErrorDetailsFromIntent(this, getIntent());

        // 在后台线程中保存错误信息
        saveCrashToHandlerAsync(errorMsg);
    }

    private void showLoadingDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("正在保存错误日志...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private void hideLoadingDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    private void saveCrashToHandlerAsync(String errorMessage) {
        // 使用AsyncTask在后台保存错误信息
        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... voids) {
                try {
                    // 创建一个包含完整错误信息的异常
                    RuntimeException ex = new RuntimeException(errorMessage);

                    // 获取当前线程的堆栈
                    StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();

                    // 过滤掉不相关的堆栈信息
                    java.util.List<StackTraceElement> filtered = new java.util.ArrayList<>();
                    for (StackTraceElement element : stackTrace) {
                        if (!element.getClassName().contains("ErrorActivity") &&
                                !element.getClassName().contains("CustomActivityOnCrash")) {
                            filtered.add(element);
                        }
                    }

                    // 设置过滤后的堆栈
                    ex.setStackTrace(filtered.toArray(new StackTraceElement[0]));

                    // 保存到 CrashHandler，使用Android上下文
                    CrashHandler handler = CrashHandler.getInstance();
                    handler.saveCrashInfo(ErrorActivity.this, Thread.currentThread(), ex);

                    return true;
                } catch (Exception e) {
                    android.util.Log.e("ErrorActivity", "Failed to save crash info: " + e.getMessage());
                    e.printStackTrace();
                    return false;
                }
            }

            @Override
            protected void onPostExecute(Boolean success) {
                hideLoadingDialog();
                initializeUI();
                if (success) {
                    Toast.makeText(ErrorActivity.this, "错误日志已保存", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ErrorActivity.this, "保存错误日志失败", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }

    private void initializeUI() {
        final MaterialToolbar toolbar = findViewById(R.id.materialToolbar);
        setSupportActionBar(toolbar);
        final TextView textView = findViewById(R.id.error_info_text_view);
        textView.setText(errorMsg);

        final Button restartButton = findViewById(R.id.restart_button);
        restartButton.setOnClickListener(v -> {
            CaocConfig config = CustomActivityOnCrash.getConfigFromIntent(
                    getIntent()
            );
            if (config == null) {
                Snackbar.make(v, R.string.no_configuration_found, Snackbar.LENGTH_LONG).show();
                return;
            }
            CustomActivityOnCrash.restartApplication(ErrorActivity.this, config);
        });

        final Button copyButton = findViewById(R.id.copy_button);
        copyButton.setOnClickListener(v -> {
            final ClipboardManager systemService =
                    (ClipboardManager) this.getSystemService(CLIPBOARD_SERVICE);
            final ClipData mClipData = ClipData.newPlainText("Label", errorMsg);
            systemService.setPrimaryClip(mClipData);
            Snackbar.make(v, R.string.copy_complete, Snackbar.LENGTH_LONG).show();
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideLoadingDialog();
    }
}
