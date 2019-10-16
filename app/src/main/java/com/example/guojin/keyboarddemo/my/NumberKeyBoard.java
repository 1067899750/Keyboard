package com.example.guojin.keyboarddemo.my;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.text.Editable;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
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

    public NumberKeyBoard(Builder builder) {
        this.mContext = builder.buildContext;
        this.mEditText = builder.buildEditText;
        this.mKeyView = builder.buildKeyView;
        this.mParent = builder.buildViewGroup;
        initView();
        setListenerViewText();

    }

    public void initView() {
        try {
            mMyKeyboardView = mParent.findViewById(R.id.keyboard_view);
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


    /**
     *  显示试图
     */
    public void showKeyboard() {
        int visibility = mMyKeyboardView.getVisibility();
        if (visibility == View.GONE || visibility == View.INVISIBLE) {
            mMyKeyboardView.setVisibility(View.VISIBLE);
        }
        mKeyView.setVisibility(View.VISIBLE);
    }

    /**
     *  隐藏试图
     */
    public void hideKeyboard() {
        int visibility = mMyKeyboardView.getVisibility();
        if (visibility == View.VISIBLE) {
            mMyKeyboardView.setVisibility(View.INVISIBLE);
        }
        mKeyView.setVisibility(View.GONE);
    }



    public static class Builder{
        private Context buildContext;
        private EditText buildEditText;
        private ViewGroup buildViewGroup;
        private View buildKeyView;


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
         *  设置键盘根试图
         * @param view
         * @return
         */
        public Builder setKeyView(View view){
            this.buildKeyView = view;
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
































