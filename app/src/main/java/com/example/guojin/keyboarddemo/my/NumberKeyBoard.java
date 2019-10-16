package com.example.guojin.keyboarddemo.my;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.inputmethodservice.KeyboardView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import com.example.guojin.keyboarddemo.R;
import com.example.guojin.keyboarddemo.card.NumberKeyBordView;

import java.lang.reflect.Method;

/**
 * @author puyantao
 * @describe
 * @create 2019/10/16 9:14
 */
public class NumberKeyBoard {
    private Context mContext;
    private NumberKeyBordView mMyKeyboardView;
    private EditText mEditText;
    private View mKeyView;
    private ViewGroup mParent;
    /**
     * 允许输入数字的个数
     */
    private int mTextCount;
    /**
     *  键盘类型{@link NumberKeyBordView#PHONE_TYPE}、{@link NumberKeyBordView#CARD_TYPE} （待接入）
     */
    private int mKeyBoardType;

    public NumberKeyBoard(Builder builder) {
        this.mContext = builder.buildContext;
        this.mEditText = builder.buildEditText;
        this.mParent = builder.buildViewGroup;
        this.mTextCount = builder.buildTextCount;
        this.mKeyBoardType = builder.buildKeyBoardType;
        initView();
        setListenerViewText();

    }

    public void initView() {
        //初始化键盘
        mKeyView = LayoutInflater.from(mContext).inflate(R.layout.number_key_board_view, null);
        mKeyView.setVisibility(View.GONE);
        mKeyView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        //把键盘添加到 window 上
        Window window = scanForActivity(mContext).getWindow();
        window.addContentView(mKeyView, new WindowManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.PopWindowstyle);

        try {
            mMyKeyboardView = mKeyView.findViewById(R.id.keyboard_view);
            mMyKeyboardView.setEnabled(true);
            mMyKeyboardView.setPreviewEnabled(false);
        } catch (Exception e) {
            Log.e("mMyKeyboardView", "mMyKeyboardView init failed!");
        }


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
     *
     */
    public void setListenerViewText() {
        mEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //点击按钮显示键盘
                showKeyboard();
                return false;
            }
        });

        mParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
            }
        });

        //设置回调，并进行文本的插入与删除
        mMyKeyboardView.setOnKeyPressListener(new NumberKeyBordView.OnKeyPressListener() {
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


    public boolean isShowKeyboard(){
        if (mKeyView.getVisibility() == View.VISIBLE){
            return true;
        } else {
            return false;
        }
    }

    /**
     *  显示试图
     */
    public void showKeyboard() {
//        Animation animation =  AnimationUtils.loadAnimation(mContext, R.anim.showanim);
//        LayoutAnimationController controller = new LayoutAnimationController(animation);
//        controller.setDelay(0.5f);
//        controller.setOrder(LayoutAnimationController.ORDER_NORMAL);
//        mKeyView.startAnimation(animation);
        mKeyView.setVisibility(View.VISIBLE);
    }

    /**
     *  隐藏试图
     */
    public void hideKeyboard() {
//        Animation animation =  AnimationUtils.loadAnimation(mContext, R.anim.dismissanim);
//        LayoutAnimationController controller = new LayoutAnimationController(animation);
//        controller.setDelay(0.5f);
//        controller.setOrder(LayoutAnimationController.ORDER_NORMAL);
//        mKeyView.startAnimation(animation);
        mKeyView.setVisibility(View.GONE);
    }


    public static class Builder{
        private Context buildContext;
        private EditText buildEditText;
        private ViewGroup buildViewGroup;
        private int buildTextCount;
        /**
         *  键盘类型
         */
        private int buildKeyBoardType;

        public Builder(Context context) {
            this.buildContext = context;
        }

        /**
         *  设置编辑试图
         * @param editText
         * @return
         */
        public Builder setEditText(EditText editText){
            this.buildEditText = editText;
            return this;
        }

        /**
         *  设置根试图
         * @param viewGroup
         * @return
         */
        public Builder setViewGroup(ViewGroup viewGroup){
            this.buildViewGroup = viewGroup;
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
         *  设置键盘类型
         * @return
         */
        public Builder setKeyBoardType(int type){
            this.buildKeyBoardType = type;
            return this;
        }

        /**
         *  构建 {@link NumberKeyBoard}
         * @return
         */
        public NumberKeyBoard Build(){
            return new NumberKeyBoard(this);
        }


    }


}
































