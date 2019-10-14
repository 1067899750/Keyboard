package com.example.guojin.keyboarddemo.card;

import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.guojin.keyboarddemo.R;
import com.example.guojin.keyboarddemo.computer.ComputerKeyBordView;

import java.lang.reflect.Method;

/**
 *
 * @description 身份证
 * @author puyantao
 * @date 2019/10/14 11:39
 */
public class IdentityCardActivity extends AppCompatActivity {
    private EditText mEditText;
    private IdentityCardKeyBordView mKeyView;
    private LinearLayout mLinearLayout;
    private PopupWindow mPop;
    private View mPopView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identity_card);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        mEditText = findViewById(R.id.et);

        mLinearLayout = findViewById(R.id.ln);

        mPop = new PopupWindow();
        mPopView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.identity_keyboard_pop, null);
        mPop.setContentView(mPopView);
        mPop.setTouchable(true);
        //设置焦点，是否点击外部会消失
        mPop.setFocusable(true);
        mPop.setBackgroundDrawable(new ColorDrawable());
        mPop.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        mPop.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        mPop.setAnimationStyle(R.style.PopWindowstyle);

        mKeyView = mPopView.findViewById(R.id.identity_card_view);


        //自定义键盘光标可以自由移动 适用系统版本为android3.0以上
        if (android.os.Build.VERSION.SDK_INT <= 10) {
            mEditText.setInputType(InputType.TYPE_NULL);
        } else {
            getWindow().setSoftInputMode(
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



        mEditText.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
//                int inputType = mEditText.getInputType();
//                mEditText.setInputType(InputType.TYPE_NULL);// 让系统键盘不弹出
                //点击按钮显示键盘
                mPop.showAtLocation(mLinearLayout, Gravity.BOTTOM, 0, 0);

//                mEditText.setInputType(inputType);
//                //设定光标位置
////                Selection.setSelection(mEditText.getText(), mEditText.getText().length());
//                mEditText.setSelection(mEditText.getText().length());
                return false;
            }
        });



        //输入结束点击键盘确认键执行的 方法
        mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                return false;
            }
        });

        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.i("---> : ", "beforeTextChanged");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.i("---> : ", "onTextChanged" + start + ":" + before + ":" + count);

            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.i("---> : ", "afterTextChanged");

            }
        });


        //设置回调，并进行文本的插入与删除
        mKeyView.setOnKeyPressListener(new IdentityCardKeyBordView.OnKeyPressListener() {
            @Override
            public void onInertKey(String text) {
                int index = mEditText.getSelectionStart();
                Log.i("---> index : ", index + "");
//                mEditText.append(text);
                Editable editable = mEditText.getText();
                editable.insert(index, text);
            }

            @Override
            public void onDeleteKey() {
                int last = mEditText.getText().length();
                if (last > 0) {
                    //删除最后一位
                    int index = mEditText.getSelectionStart();
                    Log.i("---> index : ", index + "");
                    mEditText.getText().delete(index - 1, index);
                }
            }
        });

    }



    @Override
    public void onBackPressed() {
        if(mPop.isShowing()){
            mPop.dismiss();
        } else {
            super.onBackPressed();
        }
    }



}






















