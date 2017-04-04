package com.zdd.wxtool.accessibility;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;

import com.zdd.wxtool.util.LogUtils;

import java.util.List;

/**
 * @CreateDate: 2017/4/2 下午10:05
 * @Author: lucky
 * @Description:
 * @Version: [v1.0]
 */

public class RingHelper {
    private static final String TAG = "RingHelper";

    private AccessibilityService mAccessibilityService;

    public RingHelper(AccessibilityService accessibilityService) {
        mAccessibilityService = accessibilityService;
    }

    /**
     * 铃声控制回掉方法
     *
     * @param event
     */
    public void onAccessibilityEvent(AccessibilityEvent event) {
        int type = event.getEventType();
        switch (type) {
            case AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED:
                List<CharSequence> text = event.getText();
                if (!text.isEmpty()) {
                    for (CharSequence itemMsg :
                            text) {
                        if (itemMsg.toString().contains("Lucky")) {
                            LogUtils.i(TAG, "on receive lucky message");
                        }
                    }
                }
                break;
        }
    }
}
