package com.example.guojin.keyboarddemo;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.util.AttributeSet;

import java.util.List;
import java.util.PropertyResourceBundle;

public class NumKeyView extends KeyboardView implements KeyboardView.OnKeyboardActionListener {
    //用于区分左下角空白按键,(要与xml里设置的数值相同)
    private int KEYCODE_EMPTY = -10;

    private Drawable mDeleteKeyDrawable;   //删除按键背景图片
    private int mKryboardBackgroud;
    private Drawable mKryDrawable;   //按键背景
    private int mPaddingLeft;
    private int mPaddingRight;
    private int mPaddingTop;
    private int mPaddingBottom;

    public NumKeyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public NumKeyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.NumKeyView);
        mKryDrawable = ta.getDrawable(R.styleable.NumKeyView_keyBackgBackground);
        mDeleteKeyDrawable = ta.getDrawable(R.styleable.NumKeyView_deleteDrawable); //删除按键颜色
        mKryboardBackgroud = ta.getColor(R.styleable.NumKeyView_keyboardBackgBackground, Color.WHITE); //keyboard背景颜色
        mPaddingLeft = (int) ta.getDimension(R.styleable.NumKeyView_leftPadding, 0);
        mPaddingRight = (int) ta.getDimension(R.styleable.NumKeyView_rightPadding, 0);
        mPaddingTop = (int) ta.getDimension(R.styleable.NumKeyView_topPadding, 0);
        mPaddingBottom = (int) ta.getDimension(R.styleable.NumKeyView_bottomPadding, 0);
        ta.recycle();

        //获取xml中的按键布局
        Keyboard keyboard = new Keyboard(context, R.xml.numkeyview);
        setKeyboard(keyboard);

        setEnabled(true);
        setPreviewEnabled(false);
        setOnKeyboardActionListener(this);
    }


    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(mKryboardBackgroud);

        drawKeyboardBorder(canvas);

        Keyboard keyboard = getKeyboard();
        if (keyboard == null) return;
        List<Keyboard.Key> keys = keyboard.getKeys();
        if (keys != null && keys.size() > 0) {
            Paint paint = new Paint();
            paint.setTextAlign(Paint.Align.CENTER);
            Typeface font = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD);
            paint.setTypeface(font);
            paint.setAntiAlias(true);

            for (Keyboard.Key key : keys) {
                if (key.codes[0] == Keyboard.KEYCODE_DELETE) {
                    //绘制删除键背景
                    drawKeyBackGround(key, canvas);
                    //绘制按键图片
                    drawkeyDelete(key, canvas);
                } else {
                    drawKeyBackGround(key, canvas);
                }

                if (key.label != null) {
                    if (key.codes[0] == Keyboard.KEYCODE_DELETE) {

                    } else {
                        paint.setColor(getContext().getResources().getColor(R.color.c000000));
                        paint.setTextSize(sp2px(25));
                    }
                    Rect rect = new Rect(key.x, key.y, key.x + key.width, key.y + key.height);
                    Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
                    int baseline = (rect.bottom + rect.top - fontMetrics.bottom - fontMetrics.top) / 2;
                    // 下面这行是实现水平居中，drawText对应改为传入targetRect.centerX()
                    paint.setTextAlign(Paint.Align.CENTER);
                    canvas.drawText(key.label.toString(), rect.centerX(), baseline, paint);
                }
            }
        }
    }

    //绘制边框
    private void drawKeyboardBorder(Canvas canvas) {

    }

    //数字键
    private void drawKeyBackGround(Keyboard.Key key, Canvas canvas) {
        mKryDrawable.setBounds(key.x, key.y, key.x + key.width, key.y + key.height);
        mKryDrawable.draw(canvas);
    }

    //删除键
    private void drawkeyDelete(Keyboard.Key key, Canvas canvas) {
        int drawWidth = key.width;
        int drawHeight = key.height;
        drawWidth = drawWidth / 2;
        drawHeight = drawHeight / 2;
        int widthInterval = (key.width - drawWidth) / 2;
        int heightInterval = (key.height - drawHeight) / 2;

        mDeleteKeyDrawable.setBounds(key.x + widthInterval, key.y + heightInterval,
                key.x + widthInterval + drawWidth, key.y + heightInterval + drawHeight);
        mDeleteKeyDrawable.draw(canvas);
    }


    //回调接口
    public interface OnKeyPressListener {
        //添加数据回调
        void onInertKey(String text);

        //删除数据回调
        void onDeleteKey();

        void onClearKey();
    }

    private OnKeyPressListener mOnkeyPressListener;

    public void setOnKeyPressListener(OnKeyPressListener li) {
        mOnkeyPressListener = li;
    }

    @Override
    public void onKey(int i, int[] ints) {
        if (i == Keyboard.KEYCODE_DELETE && mOnkeyPressListener != null) {
            //删除数据回调
            mOnkeyPressListener.onDeleteKey();

        } else if (i == Keyboard.KEYCODE_CANCEL && mOnkeyPressListener != null) {
            //清除数据
            mOnkeyPressListener.onClearKey();

        } else if (i != KEYCODE_EMPTY) {
            //添加数据回调
            mOnkeyPressListener.onInertKey(Character.toString((char) i));
        }
    }

    @Override
    public void onPress(int i) {

    }

    @Override
    public void onRelease(int i) {

    }


    @Override
    public void onText(CharSequence charSequence) {

    }

    @Override
    public void swipeRight() {
        super.swipeRight();
    }

    @Override
    public void swipeDown() {
        super.swipeDown();
    }

    @Override
    public void swipeLeft() {
        super.swipeLeft();
    }

    @Override
    public void swipeUp() {
        super.swipeUp();
    }


    /*****************************************************************/

    private int sp2px(float spValue) {
        final float fontScale = getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }


}

