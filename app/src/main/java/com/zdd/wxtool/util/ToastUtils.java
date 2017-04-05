package com.zdd.wxtool.util;

import android.content.Context;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.zdd.wxtool.R;

/**
 * @CreateDate: 2017/4/5 上午11:33
 * @Author: lucky
 * @Description:
 * @Version: [v1.0]
 */

public class ToastUtils {
    public static int LENGTH_SHORT = Toast.LENGTH_SHORT;
    public static int LENGTH_LONG = Toast.LENGTH_LONG;

    private static Toast toast;
    private static Handler handler = new Handler();

    private static View layout;
    private static TextView message;

    private static Runnable run = new Runnable() {
        public void run() {
            toast.cancel();
        }
    };

    private static void inflaterLayout(Context context) {
        LayoutInflater inflate = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layout = inflate.inflate(R.layout.toast_layout, null);
        message = (TextView) layout.findViewById(R.id.toast_content);
    }

    public static Toast makeText(Context context, CharSequence text, int duration) {

        if (null == layout) {
            inflaterLayout(context);
        }
        message.setText(text);

        handler.removeCallbacks(run);
        switch (duration) {
            case Toast.LENGTH_SHORT:
                duration = 1000;
                break;
            case Toast.LENGTH_LONG:
                duration = 3000;
                break;
            default:
                break;
        }
        if (null != toast) {
            toast.setView(layout);
            toast.setDuration(duration);
        } else {
            toast = new Toast(context);
            toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 60);
            toast.setDuration(duration);
            toast.setView(layout);
        }


        handler.postDelayed(run, duration);
        return toast;
    }

    public static Toast makeText(Context context, int resId, int duration) {
        return makeText(context, context.getResources().getText(resId), duration);
    }
}
