package com.example.guojin.keyboarddemo.card;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EdgeEffect;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.guojin.keyboarddemo.R;

/**
 * @author puyantao
 * @describe 身份证键盘 PopupWindow
 * @create 2019/10/14 15:15
 */
public class IdentityCardPopupWindow extends PopupWindow {
    private View mPopView;
    private IdentityCardKeyBordView mKeyView;
    private OnKeyPressListener mOnKeyPressListener;

    public IdentityCardPopupWindow(Context context) {
        super(context);
        initView(context);
    }

    public IdentityCardPopupWindow(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        mPopView = LayoutInflater.from(context).inflate(R.layout.identity_keyboard_pop, null);
        setContentView(mPopView);

        setContentView(mPopView);
        setTouchable(true);
        //设置焦点，是否点击外部会消失
        setFocusable(true);
        setBackgroundDrawable(new ColorDrawable());
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setAnimationStyle(R.style.PopWindowstyle);

        mKeyView = mPopView.findViewById(R.id.identity_card_view);

        //设置回调，并进行文本的插入与删除
        mKeyView.setOnKeyPressListener(new IdentityCardKeyBordView.OnKeyPressListener() {
            @Override
            public void onInertKey(String text) {
                if (mOnKeyPressListener != null){
                    mOnKeyPressListener.onInertKey(text);
                }
            }

            @Override
            public void onDeleteKey() {
                if (mOnKeyPressListener != null){
                    mOnKeyPressListener.onDeleteKey();
                }
            }
        });

    }

    /**
     *  设置位置
     * @param editText
     * @param view
     */
    public void showAtLocation(EditText editText, final View view){
        editText.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
//                int inputType = mEditText.getInputType();
//                mEditText.setInputType(InputType.TYPE_NULL);// 让系统键盘不弹出
                //点击按钮显示键盘
                showAtLocation(view, Gravity.BOTTOM, 0, 0);

//                mEditText.setInputType(inputType);
//                //设定光标位置
////                Selection.setSelection(mEditText.getText(), mEditText.getText().length());
//                mEditText.setSelection(mEditText.getText().length());
                return false;
            }
        });

    }

    /**
     * 按键点击监听器
     *
     * @param li
     */
    public void setOnKeyPressListener(OnKeyPressListener li) {
        mOnKeyPressListener = li;
    }


    /**
     * 回调接口
     */
    public interface OnKeyPressListener {
        /**
         * 添加数据回调
         *
         * @param text
         */
        void onInertKey(String text);

        /**
         * 删除数据回调
         */
        void onDeleteKey();

    }




}






































