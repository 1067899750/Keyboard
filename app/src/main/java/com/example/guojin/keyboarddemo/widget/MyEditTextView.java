package com.example.guojin.keyboarddemo.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.EditText;
/**
 *
 * @description
 * @author puyantao
 * @date 2019/10/14 11:31
 */

@SuppressLint("AppCompatCustomView")
public class MyEditTextView extends EditText {
    private int mLastPos = 0;
    private int mCurPos = 0;
    private float startY;
    private float startX;
    private float selectionStart;
    private float secleX;

    private EditTextSelectChange editTextSelectChange;


    public MyEditTextView(Context context) {
        super(context);
        init();

    }

    public MyEditTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyEditTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                startX = event.getX();
                startY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float evX = event.getX();
                float evY = event.getY();
//                setX(event.getRawX() - startX);
//                setY(event.getRawY() - getHeight() - startY);
                break;

            case MotionEvent.ACTION_UP:

                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        secleX = new Paint().measureText("s");
    }

    @Override
    protected void onSelectionChanged(int selStart, int selEnd) {
        super.onSelectionChanged(selStart, selEnd);
        if (this.editTextSelectChange != null) {
            mCurPos = selEnd;
            editTextSelectChange.change(mLastPos, mCurPos);
            mLastPos = mCurPos;
        }
    }

    public void setEditTextSelectChange(EditTextSelectChange editTextSelectChange) {
        this.editTextSelectChange = editTextSelectChange;
    }


    /**
     * 编辑框光标改变监听接口
     */
    public interface EditTextSelectChange {

        void change(int lastPos, int curPos);
    }

}












