package com.example.guojin.keyboarddemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.guojin.keyboarddemo.card.IdentityCardActivity;
import com.example.guojin.keyboarddemo.computer.ComputerKeyBoardActivity;

/**
 * @author puyantao
 * @description
 * @date 2019/10/14 11:39
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        startActivity(new Intent(MainActivity.this, IdentityCardActivity.class));

        initEvent();

    }

    /**
     * 点击事件
     */
    private void initEvent() {
        findViewById(R.id.btn_computer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ComputerKeyBoardActivity.class));
            }
        });


        findViewById(R.id.btn_card).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, IdentityCardActivity.class));
            }
        });

    }
}
