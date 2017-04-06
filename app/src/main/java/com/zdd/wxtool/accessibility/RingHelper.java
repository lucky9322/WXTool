package com.zdd.wxtool.accessibility;

import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.view.accessibility.AccessibilityEvent;

import com.zdd.wxtool.manager.ContactPeopleManager;
import com.zdd.wxtool.model.ContactModel;
import com.zdd.wxtool.util.LogUtils;
import com.zdd.wxtool.util.Player;

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
    public void onAccessibilityEvent(Context context, AccessibilityEvent event) {
        int type = event.getEventType();
        switch (type) {
            case AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED:
                List<CharSequence> text = event.getText();
                if (!text.isEmpty()) {
                    LogUtils.i(TAG, "on receive message");
                    for (CharSequence itemMsg :
                            text) {
                        List<ContactModel> entities = ContactPeopleManager.getInstance().getMembersEntities();
                        for (int i = 0; i < entities.size(); i++) {

                            if (itemMsg.toString().toLowerCase().contains(entities.get(i).getUsername().toLowerCase())) {
                                Player player = new Player(context);
                                player.playUrl(entities.get(i).getRingUri());
                            }
                        }
                    }
                }
                break;
        }
    }
}
