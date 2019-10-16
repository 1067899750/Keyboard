package com.example.guojin.keyboarddemo.my;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RelativeLayout;

import com.example.guojin.keyboarddemo.R;
import com.example.guojin.keyboarddemo.card.NumberKeyBordView;

public class NewActivity extends AppCompatActivity {
    private KeyEditText mEditText;
    private NumberKeyBoard mNumberKeyBoard;
    private RelativeLayout mRelativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);

        mEditText = findViewById(R.id.et_news);
        mRelativeLayout = findViewById(R.id.rl);

        mNumberKeyBoard = new NumberKeyBoard.Builder(this)
                .setEditText(mEditText)
                .setViewGroup(mRelativeLayout)
                .setKeyBoardType(NumberKeyBordView.PHONE_TYPE)
                .setTextLength(18)
                .setFocusable(true)
                .Build();


    }


    @Override
    public void onBackPressed() {
        if (mNumberKeyBoard.isShowKeyboard()){
            mNumberKeyBoard.hideKeyboard();
        } else {
            super.onBackPressed();
        }
    }
}














