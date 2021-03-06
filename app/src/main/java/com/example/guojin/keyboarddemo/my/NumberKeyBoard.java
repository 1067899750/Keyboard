package com.example.guojin.keyboarddemo.my;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.example.guojin.keyboarddemo.R;
import com.example.guojin.keyboarddemo.card.NumberKeyBoardView;
import com.example.guojin.keyboarddemo.utils.KeyBoardUtils;

import java.lang.reflect.Method;

/**
 * @author puyantao
 * @describe
 * @create 2019/10/16 9:14
 */
public class NumberKeyBoard {
    private Context mContext;
    private NumberKeyBoardView mMyKeyboardView;
    private RelativeLayout mKeyboardRl;
    private EditText mEditText;
    private View mKeyView;
    private ViewGroup mParent;
    private Window mWindow;
    /**
     * 允许输入数字的个数
     */
    private int mTextCount;
    /**
     * 键盘类型{@link NumberKeyBoardView#PHONE_TYPE}、{@link NumberKeyBoardView#CARD_TYPE}
     */
    private int mKeyBoardType;

    /**
     * 设置焦点，是否点击外部会消失
     */
    private boolean mFocusable = false;

    private boolean isTranslation = false;

    public NumberKeyBoard(Builder builder) {
        this.mContext = builder.buildContext;
        this.mEditText = builder.buildEditText;
        this.mParent = builder.buildViewGroup;
        this.mTextCount = builder.buildTextCount;
        this.mKeyBoardType = builder.buildKeyBoardType;
        this.mFocusable = builder.buildFocusable;
        initView();
        setListenerViewText();

    }

    public void initView() {
        //初始化键盘
        mKeyView = LayoutInflater.from(mContext).inflate(R.layout.number_key_board_view, null);
        mKeyView.setVisibility(View.GONE);
        mKeyView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        //把键盘添加到 window 上
        mWindow = scanForActivity(mContext).getWindow();
        mWindow.setGravity(Gravity.BOTTOM);
        mWindow.addContentView(mKeyView, new WindowManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        mKeyboardRl = mKeyView.findViewById(R.id.number_keyboard_rl);

        mMyKeyboardView = mKeyView.findViewById(R.id.number_keyboard_view);


        //自定义键盘光标可以自由移动 适用系统版本为android3.0以上
        if (android.os.Build.VERSION.SDK_INT <= 10) {
            mEditText.setInputType(InputType.TYPE_NULL);
        } else {
            scanForActivity(mContext).getWindow().setSoftInputMode(
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

        //设置限制输入个数
        if (mTextCount != 0) {
            mEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(mTextCount)});
        }

        if (mKeyBoardType != 0) {
            mMyKeyboardView.setKeyBoardType(mKeyBoardType);
        }

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
     * 设置监听变化的 EditText 试图
     */
    public void setListenerViewText() {
        mEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //防止该事件被执行两次
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (KeyBoardUtils.isSoftShowing(scanForActivity(mContext))) {
                        KeyBoardUtils.hideSoftKeyboard(scanForActivity(mContext), mEditText);
                    }
                    //点击按钮显示键盘
                    if (!isShowKeyboard()) {
                        showKeyboard();
                    }
                }
                return false;
            }
        });

        //点击其他子视图消失
        for (int i = 0; i < mParent.getChildCount(); i++) {
            if (!(mParent.getChildAt(i) instanceof KeyEditText)) {
                if (mParent.getChildAt(i) instanceof EditText) {
                    mParent.getChildAt(i).setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            //点击按钮显示键盘
                            if (event.getAction() == MotionEvent.ACTION_UP) {
                                if (isShowKeyboard()) {
                                    hideKeyboard();
                                }
                            }
                            return false;
                        }
                    });
                }
                mParent.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isShowKeyboard() && mFocusable) {
                            hideKeyboard();
                        }
                    }
                });
            }
        }

        mParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isShowKeyboard() && mFocusable) {
                    hideKeyboard();
                }
            }
        });

        //设置回调，并进行文本的插入与删除
        mMyKeyboardView.setOnKeyPressListener(new NumberKeyBoardView.OnKeyPressListener() {
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

    /**
     * 解决键盘遮挡问题
     */
    private void setKeyBoardHeight() {
        final int heightEd = mEditText.getBottom();

        DisplayMetrics outMetrics = new DisplayMetrics();
        scanForActivity(mContext).getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        final int heightPixels = outMetrics.heightPixels;

        mKeyboardRl.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
                if (isShowKeyboard()) {
                    int keyHeight = mKeyboardRl.getMeasuredHeight();
                    if (mParent.getMeasuredHeight() - heightEd < keyHeight) {
                        int scrollY = keyHeight - (mParent.getMeasuredHeight() - heightEd);
                        mParent.setTranslationY(-scrollY);
                        isTranslation = true;
                    }
                }

            }
        });

    }

    /**
     * 判断键盘是否显示
     *
     * @return
     */
    public boolean isShowKeyboard() {
        if (mKeyView.getVisibility() == View.VISIBLE) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 显示试图
     */
    public void showKeyboard() {
        setKeyBoardHeight();
        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.showanim);
        LayoutAnimationController controller = new LayoutAnimationController(animation);
        controller.setOrder(LayoutAnimationController.ORDER_NORMAL);
        mKeyView.startAnimation(animation);
        mKeyView.setVisibility(View.VISIBLE);
    }

    /**
     * 隐藏试图
     */
    public void hideKeyboard() {
        if (isTranslation) {
            mParent.setTranslationY(0);
        }
        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.dismissanim);
        LayoutAnimationController controller = new LayoutAnimationController(animation);
        controller.setOrder(LayoutAnimationController.ORDER_NORMAL);
        mKeyView.startAnimation(animation);
        mKeyView.setVisibility(View.GONE);
    }


    public static class Builder {
        private Context buildContext;
        private EditText buildEditText;
        private ViewGroup buildViewGroup;
        private int buildTextCount;
        /**
         * 键盘类型
         */
        private int buildKeyBoardType;
        private boolean buildFocusable;

        public Builder(Context context) {
            this.buildContext = context;
        }

        /**
         * 设置编辑试图
         *
         * @param editText
         * @return
         */
        public Builder setEditText(EditText editText) {
            this.buildEditText = editText;
            return this;
        }

        /**
         * 设置根试图, 用于点击取消键盘和{@link #buildFocusable} 连用
         *
         * @param viewGroup
         * @return
         */
        public Builder setViewGroup(ViewGroup viewGroup) {
            this.buildViewGroup = viewGroup;
            return this;
        }


        /**
         * 输入数字的个数
         *
         * @param count
         * @return
         */
        public Builder setTextLength(int count) {
            this.buildTextCount = count;
            return this;
        }

        /**
         * 设置键盘类型{@link NumberKeyBoardView#PHONE_TYPE}、{@link NumberKeyBoardView#CARD_TYPE}
         *
         * @return
         */
        public Builder setKeyBoardType(@NumberKeyBoardView.KeyBoardType int type) {
            this.buildKeyBoardType = type;
            return this;
        }

        /**
         * 设置焦点，是否点击外部会消失
         *
         * @param focusable true if the popup should grab focus, false otherwise.
         * @return
         */
        public Builder setFocusable(boolean focusable) {
            this.buildFocusable = focusable;
            return this;
        }

        /**
         * 构建 {@link NumberKeyBoard}
         *
         * @return
         */
        public NumberKeyBoard build() {
            return new NumberKeyBoard(this);
        }


    }


}
































