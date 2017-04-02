package com.zdd.wxtool.accessibility;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;

import com.zdd.wxtool.util.LogUtils;

/**
 * @CreateDate: 2017/4/2 下午8:30
 * @Author: lucky
 * @Description:
 * @Version: [v1.0]
 */

public class AccessibilityServiceHelp extends AccessibilityService {
    private static final String TAG = "AccessibilityServiceHel";

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        LogUtils.i(TAG,"on service connected");
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        LogUtils.i(TAG,"on accessibility event");
    }

    @Override
    public void onInterrupt() {
        LogUtils.i(TAG,"on interrupt");

    }
}
