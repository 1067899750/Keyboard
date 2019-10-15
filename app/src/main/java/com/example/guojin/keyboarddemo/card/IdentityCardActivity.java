package com.example.guojin.keyboarddemo.card;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.guojin.keyboarddemo.R;

/**
 * @author puyantao
 * @description 身份证
 * @date 2019/10/14 11:39
 */
public class IdentityCardActivity extends AppCompatActivity {
    private EditText mEditText;
    private RelativeLayout mLinearLayout;
    private NumberKeyBoardPopupWindow mNumberKeyBoardPopupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identity_card);

        mEditText = findViewById(R.id.et);

        mLinearLayout = findViewById(R.id.ln);

        mNumberKeyBoardPopupWindow = new NumberKeyBoardPopupWindow.Builder(this)
                .setLocationView(mLinearLayout)
                .setBuildEditText(mEditText)
                .setTextCount(18)
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

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }


    @Override
    public void onBackPressed() {
        if (mNumberKeyBoardPopupWindow.isShowing()) {
            mNumberKeyBoardPopupWindow.dismiss();
        } else {
            super.onBackPressed();
        }
    }


}






















