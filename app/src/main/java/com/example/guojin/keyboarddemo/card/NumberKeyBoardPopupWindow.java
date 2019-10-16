package com.example.guojin.keyboarddemo.card;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.ViewConfigurationCompat;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.PopupWindow;

import com.example.guojin.keyboarddemo.R;
import com.example.guojin.keyboarddemo.my.NumberKeyBoard;
import com.example.guojin.keyboarddemo.utils.KeyBoardUtils;

import java.lang.ref.WeakReference;
import java.lang.reflect.Method;

/**
 * @author puyantao
 * @describe 身份证键盘 PopupWindow
 * @create 2019/10/14 15:15
 */
public class NumberKeyBoardPopupWindow extends PopupWindow {
    private View mPopView;
    private NumberKeyBordView mKeyView;
    private WeakReference<Context> mWeakReference;
    private EditText mEditText;
    private View mLocationView;
    /**
     * 允许输入数字的个数
     */
    private int mTextCount;
    /**
     * 触摸 X 的位置
     */
    private float eventX;
    /**
     * 触摸 Y 的位置
     */
    private float eventY;
    /**
     * 键盘类型{@link NumberKeyBordView#PHONE_TYPE}、{@link NumberKeyBordView#CARD_TYPE}
     */
    private int mKeyBoardType;

    public NumberKeyBoardPopupWindow(Builder builder) {
        this(builder.buildContext);
        this.mWeakReference = new WeakReference<>(builder.buildContext);
        this.mEditText = builder.buildEditText;
        this.mLocationView = builder.buildView;
        this.mTextCount = builder.buildTextCount;
        this.mKeyBoardType = builder.buildKeyBoardType;
        initView();
    }

    public NumberKeyBoardPopupWindow(Context context) {
        super(context);
    }

    private void initView() {
        mPopView = LayoutInflater.from(mWeakReference.get()).inflate(R.layout.identity_keyboard_pop, null);
        setContentView(mPopView);

        setTouchable(true);
        //设置焦点，是否点击外部会消失
        setFocusable(true);
        setBackgroundDrawable(new ColorDrawable());
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setAnimationStyle(R.style.PopWindowstyle);

        mKeyView = mPopView.findViewById(R.id.identity_card_view);

        //设置限制输入个数
        if (mTextCount != 0) {
            mEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(mTextCount)});
        }

        if (mKeyBoardType != 0){
            mKeyView.setKeyboardType(mKeyBoardType);
        }


        //自定义键盘光标可以自由移动 适用系统版本为android3.0以上
        if (android.os.Build.VERSION.SDK_INT <= 10) {
            mEditText.setInputType(InputType.TYPE_NULL);
        } else {
            scanForActivity(mWeakReference.get()).getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            try {
                Class<EditText> cls = EditText.class;
                Method setShowSoftInputOnFocus;
                setShowSoftInputOnFocus = cls.getMethod(
                        "setShowSoftInputOnFocus", boolean.class);
                setShowSoftInputOnFocus.setAccessible(true);
                setShowSoftInputOnFocus.invoke(mEditText, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //约定触摸消失位置
        setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                eventX = event.getRawX();
                eventY = event.getRawY();
                int[] location = getLocation(mEditText);
                if (location[0] < eventX && eventX < location[0] + mEditText.getRight()
                        && location[1] < eventY && eventY < location[1] + mEditText.getBottom()) {
                    mEditText.requestFocus();
                    return false;
                }
                return false;
            }
        });

        //设置位置
        showAtLocation();
        //设置显示内容
        setListenerViewText();


    }


    /**
     * 强制转换Activity
     *
     * @param cont
     * @return
     */
    private Activity scanForActivity(Context cont) {
        if (cont instanceof Activity) {
            return (Activity) cont;
        } else if (cont instanceof ContextWrapper) {
            return scanForActivity(((ContextWrapper) cont).getBaseContext());
        } else {
            return (Activity) cont;
        }
    }


    /**
     * View 相对于屏幕的宽，高
     *
     * @param v
     * @return
     */
    public int[] getLocation(View v) {
        int[] location = new int[2];
        v.getLocationOnScreen(location);
        return location;
    }


    /**
     * 设置位置
     */
    public void showAtLocation() {
        mEditText.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //点击按钮显示键盘
                showAtLocation(mLocationView, Gravity.BOTTOM, 0, 0);
                return false;
            }
        });

    }


    /**
     * 获取键盘试图
     *
     * @return
     */
    public NumberKeyBordView getKeyView() {
        return mKeyView;
    }

    /**
     * 设置监听变化的 EditText 试图
     */
    public void setListenerViewText() {
        //设置回调，并进行文本的插入与删除
        mKeyView.setOnKeyPressListener(new NumberKeyBordView.OnKeyPressListener() {
            @Override
            public void onInertKey(String text) {
                int index = mEditText.getSelectionStart();
                Editable editable = mEditText.getText();
                editable.insert(index, text);
            }

            @Override
            public void onDeleteKey() {
                int last = mEditText.getText().length();
                if (last > 0) {
                    //删除最后一位
                    int index = mEditText.getSelectionStart();
                    if (index > 0) {
                        mEditText.getText().delete(index - 1, index);
                    }
                }
            }
        });

    }


    @Override
    public void dismiss() {
        super.dismiss();
    }

    public static class Builder {
        private Context buildContext;
        private EditText buildEditText;
        private View buildView;
        private int buildTextCount;
        /**
         * 键盘类型
         */
        private int buildKeyBoardType;

        public Builder(Context context) {
            this.buildContext = context;
        }

        /**
         * 设置设置的 EditText 试图
         *
         * @param buildEditText
         * @return
         */
        public Builder setBuildEditText(EditText buildEditText) {
            this.buildEditText = buildEditText;
            return this;
        }

        /**
         * 设置显示位置的试图
         *
         * @param view
         * @return
         */
        public Builder setLocationView(View view) {
            this.buildView = view;
            return this;
        }

        /**
         * 输入数字的个数
         *
         * @param count
         * @return
         */
        public Builder setTextCount(int count) {
            this.buildTextCount = count;
            return this;
        }

        /**
         * 设置键盘类型{@link NumberKeyBordView#PHONE_TYPE}、{@link NumberKeyBordView#CARD_TYPE}
         *
         * @return
         */
        public Builder setKeyBoardType(@NumberKeyBordView.KeyBoardType int type) {
            this.buildKeyBoardType = type;
            return this;
        }


        public NumberKeyBoardPopupWindow create() {
            return new NumberKeyBoardPopupWindow(this);
        }

    }


}






































