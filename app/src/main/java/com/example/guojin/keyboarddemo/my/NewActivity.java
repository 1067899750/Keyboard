package com.example.guojin.keyboarddemo.my;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.example.guojin.keyboarddemo.R;

public class NewActivity extends AppCompatActivity {
    private EditText mEditText;
    private View mView;
    private NumberKeyBoard mKeyboardUtil;
    private RelativeLayout mRelativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);

        mEditText = findViewById(R.id.et_news);
        mView = findViewById(R.id.key_board_view);
        mRelativeLayout = findViewById(R.id.rl);

        mKeyboardUtil = new NumberKeyBoard.Builder(this)
                .setEditText(mEditText)
                .setKeyView(mView)
                .setViewGroup(mRelativeLayout)
                .Build();


    }






}














