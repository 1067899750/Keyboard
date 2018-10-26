package com.example.guojin.keyboarddemo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 *
 *
 *   Description
 *   Author puyantao
 *   Email 1067899750@qq.com
 *   Date 2018-10-26 16:40
 *
 *
 *                        .::::.
 *                     .::::::::.
 *                    :::::::::::
 *                 ..:::::::::::'
 *               '::::::::::::'
 *                 .::::::::::
 *            '::::::::::::::..
 *               ..::::::::::::.
 *             ``::::::::::::::::  <- touch me
 *               ::::``:::::::::'        .:::.
 *             ::::'   ':::::'       .::::::::.
 *           .::::'      ::::     .:::::::'::::.
 *            .:::'       :::::  .:::::::::' ':::::.
 *          .::'        :::::.:::::::::'      ':::::.
 *         .::'         ::::::::::::::'         ``::::.
 *      ...:::           ::::::::::::'              ``::.
 *     ```` ':.          ':::::::::'                  ::::..
 *                        '.:::::'                    ':'````..
 *
*/
public class MainActivity extends AppCompatActivity {


    private EditText mEditText;
    private NumKeyView mKeyView;
    private LinearLayout mLinearlayout;
    private PopupWindow mPop;
    private View mPopView;
    private RelativeLayout mRelativeLayout;

    private Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mPop.dismiss();
            mKeyView.setVisibility(View.VISIBLE);
            ;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        mEditText = findViewById(R.id.et);
        mLinearlayout = findViewById(R.id.ln);


        mPop = new PopupWindow();
        mPopView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.keyboard_pop, null);
        mPop.setContentView(mPopView);
        mPop.setTouchable(true);
        mPop.setFocusable(true);
        mPop.setBackgroundDrawable(new ColorDrawable());
        mPop.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        mPop.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        mPop.setAnimationStyle(R.style.PopWindowstyle);
        mKeyView = mPopView.findViewById(R.id.keyboardview);
        mRelativeLayout = mPopView.findViewById(R.id.iv_hide);
        mRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPop.dismiss();
            }
        });


        mEditText.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                int inputType = mEditText.getInputType();
                mEditText.setInputType(InputType.TYPE_NULL);// 让系统键盘不弹出
                //点击按钮显示键盘
                mPop.showAtLocation(mLinearlayout, Gravity.BOTTOM, 0, 0);

                mEditText.setInputType(inputType);
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

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });


        //设置回调，并进行文本的插入与删除
        mKeyView.setOnKeyPressListener(new NumKeyView.OnKeyPressListener() {
            @Override
            public void onInertKey(String text) {
                mEditText.append(text);
            }

            @Override
            public void onDeleteKey() {
                int last = mEditText.getText().length();
                if (last > 0) {
                    //删除最后一位
                    mEditText.getText().delete(last - 1, last);
                }
            }

            @Override
            public void onClearKey() {
                mEditText.getText().clear();
            }
        });
    }


}
