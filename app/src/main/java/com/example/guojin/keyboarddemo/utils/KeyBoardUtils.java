package com.example.guojin.keyboarddemo.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class KeyBoardUtils {


    /**
     * 判断键盘是否显示
     *
     * @param activity
     * @return
     */
    public static boolean isSoftShowing(Activity activity) {
        // 获取当前屏幕内容的高度
        int screenHeight = activity.getWindow().getDecorView().getHeight();
        // 获取View可见区域的bottom
        Rect rect = new Rect();
        // DecorView即为activity的顶级view
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        // 考虑到虚拟导航栏的情况（虚拟导航栏情况下：screenHeight = rect.bottom + 虚拟导航栏高度）
        // 选取screenHeight*2/3进行判断
        return screenHeight * 2 / 3 > rect.bottom;
    }

    /**
     * 设置遮挡键盘问题
     *
     * @param activity
     * @param height
     */
    public static void setPopuKeyBoardHeight(Activity activity, int height) {
        View decorView = activity.getWindow().getDecorView();
        Rect rect = new Rect();
        decorView.getWindowVisibleDisplayFrame(rect);
        int decHeight = rect.height() - height;
        rect.set(0, 0, rect.width(), decHeight);
        decorView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, rect.height()));
    }


    /**
     * 隐藏软键盘(有输入框)
     *
     * @param activity
     * @param mEditText
     */
    public static void hideSoftKeyboard(@NonNull Activity activity, @NonNull EditText mEditText) {
        View view = activity.getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputmanger = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }




}











