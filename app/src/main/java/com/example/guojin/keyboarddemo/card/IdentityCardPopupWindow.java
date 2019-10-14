package com.example.guojin.keyboarddemo.card;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.util.AttributeSet;
import android.util.Log;
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

import java.lang.ref.WeakReference;

/**
 * @author puyantao
 * @describe 身份证键盘 PopupWindow
 * @create 2019/10/14 15:15
 */
public class IdentityCardPopupWindow{
    private PopupWindow mPopupWindow;
    private View mPopView;
    private IdentityCardKeyBordView mKeyView;
    private WeakReference<Context> mWeakReference;
    private EditText mEditText;
    private View mLocationView;

    public IdentityCardPopupWindow(Builder builder) {
        this.mWeakReference = new WeakReference<>(builder.mContext);
        this.mEditText = builder.editText;
        this.mLocationView = builder.mView;
        initView();
    }

    private void initView() {
        mPopupWindow = new PopupWindow(mWeakReference.get());
        mPopView = LayoutInflater.from(mWeakReference.get()).inflate(R.layout.identity_keyboard_pop, null);
        mPopupWindow.setContentView(mPopView);

        mPopupWindow.setContentView(mPopView);
        mPopupWindow.setTouchable(true);
        //设置焦点，是否点击外部会消失
        mPopupWindow.setFocusable(true);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable());
        mPopupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        mPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setAnimationStyle(R.style.PopWindowstyle);

        mKeyView = mPopView.findViewById(R.id.identity_card_view);

        //设置位置
        showAtLocation(mEditText, mLocationView);
        //设置显示内容
        setListenerViewText(mEditText);

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
                mPopupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);

//                mEditText.setInputType(inputType);
//                //设定光标位置
////                Selection.setSelection(mEditText.getText(), mEditText.getText().length());
//                mEditText.setSelection(mEditText.getText().length());
                return false;
            }
        });

    }

    /**
     *  设置监听变化的 EditText 试图
     * @param editText
     */
    public void setListenerViewText(final EditText editText){
        //设置回调，并进行文本的插入与删除
        mKeyView.setOnKeyPressListener(new IdentityCardKeyBordView.OnKeyPressListener() {
            @Override
            public void onInertKey(String text) {
                int index = editText.getSelectionStart();
                Log.i("---> index : ", index + "");
                Editable editable = editText.getText();
                editable.insert(index, text);
            }

            @Override
            public void onDeleteKey() {
                int last = editText.getText().length();
                if (last > 0) {
                    //删除最后一位
                    int index = editText.getSelectionStart();
                    Log.i("---> index : ", index + "");
                    editText.getText().delete(index - 1, index);
                }
            }
        });

    }

    /**
     *  查看 PopupWindow 是否显示
     * @return
     */
    public boolean isShowing(){
        return mPopupWindow.isShowing();
    }

    /**
     *  隐藏 PopupWindow
     */
    public void dismiss(){
        mPopupWindow.dismiss();
    }



    public static class Builder{
        private Context mContext;
        private EditText editText;
        private View mView;

        public Builder(Context context) {
            this.mContext = context;
        }

        /**
         *  设置设置的 EditText 试图
         * @param editText
         * @return
         */
        public Builder setEditText(EditText editText){
            this.editText = editText;
            return this;
        }

        /**
         *  设置显示位置的试图
         * @param view
         * @return
         */
        public Builder setLocationView(View view){
            this.mView = view;
            return this;
        }

        public IdentityCardPopupWindow create(){
            return new IdentityCardPopupWindow(this);
        }

    }



}






































