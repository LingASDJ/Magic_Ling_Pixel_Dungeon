package com.shatteredpixel.shatteredpixeldungeon.custom.utils;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.watabou.noosa.Game;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.DeviceCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;

public class CrashHandler implements Thread.UncaughtExceptionHandler {
    /** Debug Log tag*/
    public static final String TAG = "CrashHandler";
    /** 是否开启日志输出*/
    public static final boolean DEBUG = false;
    /** 崩溃日志目录*/
    public static final String CRASH_DIR = "crash_logs";
    /** 崩溃日志文件前缀*/
    private static final String CRASH_FILE_PREFIX = "crash_";
    /** ANR日志文件前缀 */
    private static final String ANR_FILE_PREFIX = "anr_";
    /** 崩溃日志文件后缀*/
    public static final String CRASH_FILE_EXTENSION = ".log";

    private static final long ANR_THRESHOLD_MS = 4000L;
    /** ANR检测轮询间隔 */
    private static final long ANR_CHECK_INTERVAL_MS = 500L;

    /** 系统默认的UncaughtException处理类 */
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    /** CrashHandler实例 */
    public static CrashHandler INSTANCE;
    /** 使用Properties来保存设备的信息和错误堆栈信息*/
    private Properties mDeviceCrashInfo = new Properties();
    private static final String VERSION_NAME = "versionName";
    private static final String VERSION_CODE = "versionCode";
    private static final String STACK_TRACE = "STACK_TRACE";

    //==================== ANR监控相关 ====================
    private HandlerThread anrMonitorThread;
    private Handler anrMonitorHandler;
    private volatile boolean mainThreadTick;
    private Thread mainUiThread;
    private final Runnable anrTickRunnable = new Runnable() {
        @Override
        public void run() {
            mainThreadTick = true;
        }
    };

    public boolean isFakeAnrTrace(String stack) {
        if (stack == null) return true;
        return stack.contains("SUSPECTED ANR")
                && stack.contains("nativePollOnce")
                && !stack.contains(".java:");
    }

    private final Runnable anrDetectRunnable = new Runnable() {
        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                //关键修复：Gdx.app为空就终止监控循环，避免NPE
                if (Gdx.app == null) {
                    break;
                }

                boolean tickBefore = mainThreadTick;
                mainThreadTick = false;
                //往libgdx主线程post一个空任务
                Gdx.app.postRunnable(anrTickRunnable);

                try {
                    Thread.sleep(ANR_THRESHOLD_MS);
                } catch (InterruptedException e) {
                    break;
                }

                if (!mainThreadTick && tickBefore) {
                    // 主线程在阈值时间没有执行任务 → 疑似ANR
                    captureSuspectedANR();
                }

                try {
                    Thread.sleep(ANR_CHECK_INTERVAL_MS);
                } catch (InterruptedException e) {
                    break;
                }
            }
        }
    };


    /** 保证只有一个CrashHandler实例 */
    private CrashHandler() {}

    /**
     * 公共方法：保存崩溃信息（不依赖Gdx）
     * @param thread 发生崩溃的线程
     * @param ex 崩溃异常
     * @param context Android上下文
     */
    public void saveCrashInfo(Context context, Thread thread, Throwable ex) {
        try {
            // 创建崩溃报告
            String crashReport = generateCrashReport(thread, ex);

            // 使用Android的文件系统保存
            saveCrashReportAndroid(context, crashReport);

            // 输出到控制台
            if (DEBUG) {
                System.out.println("Crash report saved successfully");
            }
        } catch (Exception e) {
            System.err.println("Failed to save crash info: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 使用Android文件系统保存崩溃报告
     */
    private void saveCrashReportAndroid(Context context, String crashReport) {
        try {
            // 获取应用私有目录
            File crashDir = new File(context.getFilesDir(), CRASH_DIR);
            if (!crashDir.exists()) {
                crashDir.mkdirs();
            }

            // 创建崩溃报告文件
            String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.getDefault()).format(new Date());
            String fileName = CRASH_FILE_PREFIX + timestamp + CRASH_FILE_EXTENSION;
            File crashFile = new File(crashDir, fileName);

            // 写入文件
            try (FileOutputStream fos = new FileOutputStream(crashFile);
                 OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8")) {
                osw.write(crashReport);
                osw.flush();
            }

            if (DEBUG) {
                System.out.println("Crash report saved to: " + crashFile.getAbsolutePath());
            }
        } catch (Exception e) {
            System.err.println("Failed to save crash report: " + e.getMessage());
            // 即使保存失败，也要输出到控制台
            System.err.println(crashReport);
        }
    }

    /** 获取CrashHandler实例 ,单例模式*/
    public static CrashHandler getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CrashHandler();
        }
        return INSTANCE;
    }

    /**
     * 初始化,获取系统默认的UncaughtException处理器,
     * 设置该CrashHandler为程序的默认处理器
     * 同时开启ANR监控
     */
    public void init() {
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
        startAnrMonitor();
    }

    /** 启动ANR卡顿监控 */
    public void startAnrMonitor(){
        if(!DeviceCompat.isAndroid()) return;
        if(anrMonitorThread != null) return;
        if(Gdx.app == null) return;

        mainUiThread = Looper.getMainLooper().getThread();
        anrMonitorThread = new HandlerThread("AnrMonitorThread");
        anrMonitorThread.start();
        anrMonitorHandler = new Handler(anrMonitorThread.getLooper());
        anrMonitorHandler.post(anrDetectRunnable);
    }

    /** 停止ANR监控，销毁时调用 */
    public void stopAnrMonitor(){
        if(anrMonitorThread != null){
            anrMonitorThread.interrupt();
            anrMonitorThread.quit();
            anrMonitorThread = null;
        }
        if(anrMonitorHandler != null){
            anrMonitorHandler.removeCallbacks(anrDetectRunnable);
            anrMonitorHandler = null;
        }
    }

    /** 捕获疑似ANR，抓取主线程堆栈，写日志 */
    private void captureSuspectedANR(){
        if(Gdx.app == null) return;
        try {
            StringBuilder sb = new StringBuilder();
            String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
            sb.append("Time: ").append(timestamp).append("\n");
            sb.append("===== SUSPECTED ANR (Main thread blocked) =====\n");
            sb.append("Threshold: ").append(ANR_THRESHOLD_MS).append("ms\n");
            sb.append("\n--- MAIN THREAD STACK TRACE ---\n");

            //打印主线程堆栈
            StackTraceElement[] stack = mainUiThread.getStackTrace();
            for (StackTraceElement element : stack) {
                sb.append("\tat ").append(element.toString()).append("\n");
            }

            sb.append("\n");
            sb.append(getSystemInfo());
            sb.append("\n===== END SUSPECTED ANR =====\n");

            //保存ANR日志
            String timeFile = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.getDefault()).format(new Date());
            String fileName = ANR_FILE_PREFIX + timeFile + CRASH_FILE_EXTENSION;
            FileHandle crashFile = Gdx.files.local(CRASH_DIR).child(fileName);
            crashFile.writeString(sb.toString(), false);

            System.err.println(sb);
        }catch (Exception e){
            System.err.println("captureSuspectedANR failed");
            e.printStackTrace();
        }
    }

    private void ensureCrashDirExists() {
        if (Gdx.files != null) {
            FileHandle crashDir = Gdx.files.local(CRASH_DIR);
            if (!crashDir.exists()) {
                crashDir.mkdirs();
            }
        }
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        try {
            // 确保目录存在
            ensureCrashDirExists();

            // 创建崩溃报告
            String crashReport = generateCrashReport(thread, ex);

            // 保存崩溃报告
            saveCrashReport(crashReport);

            // 检查是否是致命错误
            if (isFatalError(ex)) {
                handleFatalError(ex);
            }

            // 输出到控制台
            System.err.println(crashReport);

        } catch (Exception e) {
            System.err.println("Failed to handle crash: " + e.getMessage());
            e.printStackTrace();
        }

        // 调用默认处理器或退出
        if (!handleException(ex) && mDefaultHandler != null) {
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            try {
                Thread.sleep(0);
            } catch (InterruptedException e) {
                System.err.println("Error while waiting to exit: " + e.getMessage());
            }
            System.exit(1);
        }
    }

    private void saveCrashReport(String crashReport) {
        try {
            if (Gdx.files == null) {
                // 如果Gdx.files不可用，输出到控制台
                System.err.println("Gdx.files not available, printing crash report to console:");
                System.err.println(crashReport);
                return;
            }

            ensureCrashDirExists();

            String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.getDefault()).format(new Date());
            String fileName = CRASH_FILE_PREFIX + timestamp + CRASH_FILE_EXTENSION;
            FileHandle crashFile = Gdx.files.local(CRASH_DIR).child(fileName);
            crashFile.writeString(crashReport, false);

            if (DEBUG) {
                System.out.println("Crash report saved to: " + crashFile.path());
            }
        } catch (Exception e) {
            System.err.println("Failed to save crash report: " + e.getMessage());
            // 即使保存失败，也要输出到控制台
            System.err.println(crashReport);
        }
    }

    private String generateCrashReport(Thread thread, Throwable ex) {
        StringBuilder sb = new StringBuilder();
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        sb.append("Time: ").append(timestamp).append("\n");
        sb.append("=== CRASH REPORT ===\n");

        sb.append(getStackTrace(ex));

        sb.append("\n=== END REPORT ===\n");

        return sb.toString();
    }

    private boolean isFatalError(Throwable ex) {
        return ex instanceof InternalError ||
                ex instanceof OutOfMemoryError ||
                ex instanceof StackOverflowError ||
                (ex.getCause() != null && isFatalError(ex.getCause()));
    }

    private void handleFatalError(Throwable ex) {
        try {
            String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.getDefault()).format(new Date());
            String fileName = "fatal_" + timestamp + CRASH_FILE_EXTENSION;
            FileHandle crashFile = Gdx.files.local(CRASH_DIR).child(fileName);

            StringBuilder sb = new StringBuilder();
            sb.append("=== FATAL ERROR REPORT ===\n");
            sb.append("Time: ").append(timestamp).append("\n");
            sb.append("Error Type: ").append(ex.getClass().getName()).append("\n");
            sb.append("Error Message: ").append(ex.getMessage()).append("\n\n");
            sb.append("System Information:\n");
            sb.append("Java Version: ").append(System.getProperty("java.version")).append("\n");
            sb.append("JVM Name: ").append(System.getProperty("java.vm.name")).append("\n");
            sb.append(getSystemInfo());
            sb.append("Available Processors: ").append(Runtime.getRuntime().availableProcessors()).append("\n");
            sb.append("Max Memory: ").append(Runtime.getRuntime().maxMemory() / (1024 * 1024)).append(" MB\n");
            sb.append("Total Memory: ").append(Runtime.getRuntime().totalMemory() / (1024 * 1024)).append(" MB\n");
            sb.append("Free Memory: ").append(Runtime.getRuntime().freeMemory() / (1024 * 1024)).append(" MB\n");
            sb.append("\n=== END REPORT ===\n");

            crashFile.writeString(sb.toString(), false);

            if (DEBUG) {
                System.out.println("Fatal error report saved to: " + crashFile.path());
            }
        } catch (Exception e) {
            System.err.println("Failed to save fatal error report: " + e.getMessage());
        }
    }

    private String getSystemInfo() {
        StringBuilder sb = new StringBuilder();

        if (DeviceCompat.isAndroid()) {
            try {
                // 通过反射获取Android设备名称
                Class<?> buildClass = Class.forName("android.os.Build");
                String model = (String) buildClass.getField("MODEL").get(null);
                String version = (String) buildClass.getField("RELEASE").get(null);

                sb.append("OS: Android ").append(version)
                        .append(" (").append(model).append(")\n\n");
            } catch (Exception e) {
                // 如果反射获取失败，使用基础信息
                try {
                    String version = String.valueOf(Gdx.app.getVersion());
                    sb.append("OS: Android ").append(version).append("\n\n");
                } catch (Exception ex) {
                    sb.append("OS: Android\n\n");
                }
            }
        } else {
            sb.append("OS: ")
                    .append(System.getProperty("os.name"))
                    .append(" ")
                    .append(System.getProperty("os.version"))
                    .append("\n\n");
        }

        return sb.toString();
    }

    private String getStackTrace(Throwable ex) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        String stackTrace = sw.toString();
        // 移除第一行的异常类型信息
        if (stackTrace.contains(":")) {
            stackTrace = stackTrace.substring(stackTrace.indexOf(":") + 1).trim();
        }
        return stackTrace;
    }

    private boolean handleException(Throwable ex) {
        if (ex == null) {
            if (DEBUG) {
                System.out.println(TAG + " handleException --- ex==null");
            }
            return true;
        }

        final String msg = ex.getLocalizedMessage();
        if(msg == null) {
            return false;
        }

        Gdx.app.error(TAG, "Program error: " + msg);
        collectCrashDeviceInfo();
        return true;
    }

    public void sendPreviousReportsToServer() {
        sendCrashReportsToServer();
    }

    private void sendCrashReportsToServer() {
        FileHandle crashDir = Gdx.files.local(CRASH_DIR);
        if (!crashDir.exists()) return;

        FileHandle[] files = crashDir.list();
        if (files == null) return;

        for (FileHandle file : files) {
            if (file.name().endsWith(CRASH_FILE_EXTENSION)) {
                postReport(file);
                // 不立即删除文件，而是标记为已发送
                file.moveTo(crashDir.child("sent_" + file.name()));
            }
        }
    }

    private void postReport(FileHandle file) {
        // TODO 实现发送崩溃报告到服务器
        if (DEBUG) {
            System.out.println("Sending crash report: " + file.name());
        }
    }

    public void collectCrashDeviceInfo() {
        try {
            mDeviceCrashInfo.put(VERSION_NAME, Game.version);
            mDeviceCrashInfo.put(VERSION_CODE, "1");
        } catch (Exception e) {
            if (DEBUG) {
                System.err.println("Error while collect package info: " + e.getMessage());
            }
        }

        mDeviceCrashInfo.put("OS", System.getProperty("os.name"));
        mDeviceCrashInfo.put("OS_VERSION", System.getProperty("os.version"));
        mDeviceCrashInfo.put("OS_ARCH", System.getProperty("os.arch"));
        mDeviceCrashInfo.put("JAVA_VERSION", System.getProperty("java.version"));
        mDeviceCrashInfo.put("JAVA_VENDOR", System.getProperty("java.vendor"));

        if (Gdx.graphics != null) {
            mDeviceCrashInfo.put("OPENGL_VERSION", Gdx.graphics.getGLVersion().getRendererString());
            mDeviceCrashInfo.put("DISPLAY_MODE", Gdx.graphics.getDisplayMode().toString());
        }
    }

    public static class ExceptionStrings implements Bundlable {
        public String fileName;
        public String stackTrace;
        public String message;

        public ExceptionStrings() {}

        public ExceptionStrings(String fileName, String stackTrace, String message) {
            this.fileName = fileName;
            this.stackTrace = stackTrace;
            this.message = message;
        }

        @Override
        public void restoreFromBundle(Bundle bundle) {
            fileName = bundle.getString("fileName");
            stackTrace = bundle.getString("stackTrace");
            message = bundle.getString("message");
        }

        @Override
        public void storeInBundle(Bundle bundle) {
            bundle.put("fileName", fileName);
            bundle.put("stackTrace", stackTrace);
            bundle.put("message", message);
        }
    }
}
