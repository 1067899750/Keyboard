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
 * @author puyantao
 * @description 身份证
 * @date 2019/10/14 11:39
 */
public class IdentityCardActivity extends AppCompatActivity {
    private EditText mEditText;
    private LinearLayout mLinearLayout;
    private IdentityCardPopupWindow mIdentityCardPopupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identity_card);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        mEditText = findViewById(R.id.et);

        mLinearLayout = findViewById(R.id.ln);

        mIdentityCardPopupWindow = new IdentityCardPopupWindow.Builder(this)
                .setLocationView(mLinearLayout)
                .setEditText(mEditText)
                .create();

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


    }


    @Override
    public void onBackPressed() {
        if (mIdentityCardPopupWindow.isShowing()) {
            mIdentityCardPopupWindow.dismiss();
        } else {
            super.onBackPressed();
        }
    }


}






















