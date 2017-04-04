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

    private RingHelper mRingHelper;

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        LogUtils.i(TAG, "on service connected");

        mRingHelper = new RingHelper(this);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        LogUtils.i(TAG, "on accessibility event");
        if (event.getClassName() == null) {
            return;
        }
        String listenerPackageName = event.getPackageName().toString();
        if (listenerPackageName == "com.tencent.mm") {
            mRingHelper.onAccessibilityEvent(event);
        }
    }

    @Override
    public void onInterrupt() {
        LogUtils.i(TAG, "on interrupt");

    }
}
