package com.zdd.wxtool.application;

import org.litepal.LitePal;
import org.litepal.LitePalApplication;

/**
 * @CreateDate: 2017/4/2 下午9:01
 * @Author: lucky
 * @Description:
 * @Version: [v1.0]
 */

public class App extends LitePalApplication {
    private static final String TAG = "App";

    private static App instance = null;

    public static App getInstance() {
        if (null == instance) {
            synchronized (App.class) {
                if (null == instance) {
                    instance = new App();
                }
            }
        }
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LitePal.initialize(this);
    }
}
