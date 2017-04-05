package com.zdd.wxtool.util;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Context;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityManager;

import java.util.List;

/**
 * @CreateDate: 2017/4/5 下午6:43
 * @Author: lucky
 * @Description: 检测当前 插件服务是否开启
 * @Version: [v1.0]
 */

public class CommonUtils {
    private static final String TAG = "CommonUtils";

    /**
     *
     * @param mContext
     * @return
     */
    public static boolean isActivateAccessSeervice(Context mContext) {
        int accessibilityEnabled = 0;
        final String service = "com.zdd.wxtool/com.zdd.wxtool.accessibility.AccessibilityServiceHelp";
//        final String service = getPackageName() + "/" + YOURAccessibilityService.class.getCanonicalName();
        try {
            accessibilityEnabled = Settings.Secure.getInt(
                    mContext.getApplicationContext().getContentResolver(),
                    android.provider.Settings.Secure.ACCESSIBILITY_ENABLED);
            Log.v(TAG, "accessibilityEnabled = " + accessibilityEnabled);
        } catch (Settings.SettingNotFoundException e) {
            Log.e(TAG, "Error finding setting, default accessibility to not found: "
                    + e.getMessage());
        }
        TextUtils.SimpleStringSplitter mStringColonSplitter = new TextUtils.SimpleStringSplitter(':');

        if (accessibilityEnabled == 1) {
            Log.v(TAG, "***ACCESSIBILITY IS ENABLED*** -----------------");
            String settingValue = Settings.Secure.getString(
                    mContext.getApplicationContext().getContentResolver(),
                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (settingValue != null) {
                mStringColonSplitter.setString(settingValue);
                while (mStringColonSplitter.hasNext()) {
                    String accessibilityService = mStringColonSplitter.next();

                    Log.v(TAG, "-------------- > accessibilityService :: " + accessibilityService + " " + service);
                    if (accessibilityService.equalsIgnoreCase(service)) {
                        Log.v(TAG, "We've found the correct setting - accessibility is switched on!");
                        return true;
                    }
                }
            }
        } else {
            Log.v(TAG, "***ACCESSIBILITY IS DISABLED***");
        }

        return false;
    }

    /**
     * Check当前辅助服务是否启用
     *
     * @param
     * @return 是否启用
     */
    public static boolean checkAccessibilityEnabled(Context context) {
        final String serviceName = "com.zdd.wxtool/.accessibility.AccessibilityServiceHelp";

        List<AccessibilityServiceInfo> accessibilityServices =
                ((AccessibilityManager) context.getSystemService(Context.ACCESSIBILITY_SERVICE)).
                        getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_GENERIC);
        for (AccessibilityServiceInfo info : accessibilityServices) {
            if (info.getId().equals(serviceName)) {
                return true;
            }
        }
        return false;
    }
}
