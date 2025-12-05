package com.shatteredpixel.shatteredpixeldungeon.custom.utils;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.watabou.noosa.Game;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;

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
    /** 崩溃日志文件后缀*/
    public static final String CRASH_FILE_EXTENSION = ".log";
    /** 系统默认的UncaughtException处理类 */
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    /** CrashHandler实例 */
    public static CrashHandler INSTANCE;
    /** 使用Properties来保存设备的信息和错误堆栈信息*/
    private Properties mDeviceCrashInfo = new Properties();
    private static final String VERSION_NAME = "versionName";
    private static final String VERSION_CODE = "versionCode";
    private static final String STACK_TRACE = "STACK_TRACE";

    /** 保证只有一个CrashHandler实例 */
    private CrashHandler() {}

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
     */
    public void init() {
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
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
                Thread.sleep(3000);
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

        sb.append("=== CRASH REPORT ===\n");
        sb.append("Time: ").append(timestamp).append("\n");
        sb.append("Thread: ").append(thread.getName()).append(" (").append(thread.getId()).append(")\n");
        sb.append("Game Version: ").append(Game.version).append("\n");
        sb.append("Java Version: ").append(System.getProperty("java.version")).append("\n");
        sb.append("OS: ").append(System.getProperty("os.name")).append(" ").append(System.getProperty("os.version")).append("\n\n");

        sb.append("GameSeed: ").append(Dungeon.seed).append("\n");
        sb.append("Challenges: ").append(Dungeon.challenges).append("\n\n");

        sb.append("Exception Type: ").append(ex.getClass().getName()).append("\n");
        sb.append("Exception Message: ").append(ex.getMessage()).append("\n\n");

        sb.append("Stack Trace:\n");
        sb.append(getStackTrace(ex));

        // 添加原因异常
        Throwable cause = ex.getCause();
        while (cause != null) {
            sb.append("\nCaused by:\n");
            sb.append(cause.getClass().getName()).append(": ").append(cause.getMessage()).append("\n");
            sb.append(getStackTrace(cause));
            cause = cause.getCause();
        }

        sb.append("\n=== END REPORT ===\n");

        return sb.toString();
    }

//    private void saveCrashReport(String crashReport) {
//        try {
//            String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.getDefault()).format(new Date());
//            String fileName = CRASH_FILE_PREFIX + timestamp + CRASH_FILE_EXTENSION;
//            FileHandle crashFile = Gdx.files.local(CRASH_DIR).child(fileName);
//            crashFile.writeString(crashReport, false);
//
//            if (DEBUG) {
//                System.out.println("Crash report saved to: " + crashFile.path());
//            }
//        } catch (Exception e) {
//            System.err.println("Failed to save crash report: " + e.getMessage());
//        }
//    }

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

        if (Gdx.app.getType() == Application.ApplicationType.Android) {
            // 通过Gdx获取Android系统信息
            try {
                // 获取Android版本
                String version = String.valueOf(Gdx.app.getVersion());
                // 获取设备信息
                String model = Gdx.graphics.getDisplayMode().toString();

                sb.append("OS: Android ").append(version)
                        .append(" (").append(model).append(")\n");
            } catch (Exception e) {
                sb.append("OS: Android\n");
            }
        } else {
            sb.append("OS: ")
                    .append(System.getProperty("os.name"))
                    .append(" ")
                    .append(System.getProperty("os.version"))
                    .append("\n");
        }

        return sb.toString();
    }



    private String getStackTrace(Throwable ex) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        return sw.toString();
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
