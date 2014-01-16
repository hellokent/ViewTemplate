package com.example.viewtemplate;

import android.content.res.AssetManager;
import android.text.TextUtils;
import android.util.Log;
import com.example.viewtemplate.xml.nodes.Item;
import com.example.viewtemplate.xml.nodes.LogConfig;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * print log utils
 * Created by chenyang.coder@gmail.com on 13-8-18 下午3:07.
 * Finished on 13-8-19 上午1:58.
 */
public final class L {
	static final int STACK_DEPTH = 4;
    static final ArrayList<LogConfigure> LOG_CONFIG = new ArrayList<LogConfigure>();
    static boolean sInited = false;

    static {
        final AssetManager assetManager = Utils.getApp().getAssets();
        InputStream inputStream = null;
        final String logConfigFileName = Utils.App.getMetaString(Config.LOG_FILE_TAG);
        if (!TextUtils.isEmpty(logConfigFileName)){
            try {
                inputStream = assetManager.open(Utils.App.getMetaString(Config.LOG_FILE_TAG));
                LogConfig logConfig = Utils.Xml.parseStream(inputStream, LogConfig.class);
                if (logConfig != null){
                    for (Item item : logConfig.items){
                        LOG_CONFIG.add(new LogConfigure(item));
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (inputStream != null){
                    try {
                        inputStream.close();
                    } catch (IOException ignored) {}
                }
            }
        }
        sInited = true;
    }

	private L() {
	}

    /**
     * 这是一个代码异味(code smell)。它可以用于开发过程中追踪bug，但不要提交到你的版本控制系统
     */
	public static void v(final String msg, final Object... args) {
		log(Log.VERBOSE, null, msg, args);
	}

    /**
     * 把一切东西都记录在这里。这在debug过程中最常用到。我主张在进入生产阶段前减少debug语句的数量，
     * 只留下最有意义的部分，在调试(troubleshooting)的时候激活。
     */
	public static void d(final String msg, final Object... args) {
		log(Log.DEBUG, null, msg, args);
	}

    /**
     *  用户行为(user-driven)和系统的特定行为(例如计划任务…)
     */
	public static void i(final String msg, final Object... args) {
		log(Log.INFO, null, msg, args);
	}

    /**
     * 录在这个级别的事件都有可能成为一个error。
     * 例如，一次调用数据库使用的时间超过了预设时间，或者内存缓存即将到达容量上限。
     * 这可以让你适当地发出警报，或者在调试时更好地理解系统在failure之前做了些什么
     */
	public static void w(final String msg, final Object... args) {
		log(Log.WARN, null, msg, args);
	}

    /**
     * 把每一个错误条件都记录在这。例如API调用返回了错误，或是内部错误条件
     */
	public static void e(final String msg, final Object... args) {
		log(Log.ERROR, null, msg, args);
	}

	public static void exception(final Throwable throwable) {
		log(Log.WARN, throwable, "");
	}

	private static void log(int level, Throwable throwable, String msg, Object... arg) {
        Log.println(level, "log", String.format(msg, arg));
		if (!BuildConfig.DEBUG || !sInited) {
			return;
		}

        try{
            final String className = Thread.currentThread().getStackTrace()[STACK_DEPTH].getClassName();
            LogConfigure logConfig = null;
            for(LogConfigure config : LOG_CONFIG){
                if (config.checkClassName(className)){
                    logConfig = config;
                    break;
                }
            }

            if (logConfig == null || level > logConfig.mMaxLevel ){
                return;
            }

            if (throwable == null){
                Log.println(level, logConfig.mTag, String.format(msg, arg));
            } else {
                Log.println(level, logConfig.mTag, Log.getStackTraceString(throwable));
            }

        }catch (Throwable t){
            t.printStackTrace();
            if (BuildConfig.DEBUG){
                throw new RuntimeException(t);
            }
        }
	}

    private static class LogConfigure {
        String mPackageName;
        boolean mEnable;
        int mMaxLevel;
        String mTag;

        private LogConfigure(final String packageName, final boolean enable, final int maxLevel, final String tag) {
            mPackageName = packageName;
            mEnable = enable;
            mMaxLevel = maxLevel;
            mTag = tag;
        }

        private LogConfigure(Item item) {
            this(item.packageName,
                    item.tag.isEnable(),
                    item.tag.getLevel(),
                    item.tag.getValue().trim());
        }

        public boolean checkClassName(final String className){
            return mEnable && className.startsWith(mPackageName);
        }

        @Override
        public String toString() {
            return "LogInfomation{" +
                    "packageName='" + mPackageName + '\'' +
                    ", enable=" + mEnable +
                    ", maxLevel=" + mMaxLevel +
                    ", tag='" + mTag + '\'' +
                    '}';
        }
    }

}
