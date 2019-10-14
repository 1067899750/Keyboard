package com.example.guojin.keyboarddemo.card;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.util.AttributeSet;
import android.util.Log;

import com.example.guojin.keyboarddemo.R;


import java.util.List;

/**
 * @author puyantao
 * @describe
 * @create 2019/10/14 13:37
 */
public class IdentityCardKeyBordView extends KeyboardView implements KeyboardView.OnKeyboardActionListener {
    /**
     * 用于区分左下角空白按键,(要与xml里设置的数值相同)
     */
    private int KEYCODE_EMPTY = -10;
    /**
     * 删除按键背景图片
     */
    private Drawable mDeleteKeyDrawable;
    private int mKeyboardBackground;
    /**
     * 按键背景
     */
    private Drawable mKryDrawable;
    private Drawable mKryClickDrawable;
    private int mKeySize;
    private int mPaddingLeft;
    private int mPaddingRight;
    private int mPaddingTop;
    private int mPaddingBottom;
    private Keyboard.Key mKey;
    private boolean isClick = false;

    private OnKeyPressListener mOnKeyPressListener;


    public IdentityCardKeyBordView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public IdentityCardKeyBordView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }


    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ComputerKeyBordView);
        mKryDrawable = ta.getDrawable(R.styleable.ComputerKeyBordView_keyBackgBackground);
        mKryClickDrawable = ta.getDrawable(R.styleable.ComputerKeyBordView_keyClickBackgBackground);
        //删除按键颜色
        mDeleteKeyDrawable = ta.getDrawable(R.styleable.ComputerKeyBordView_deleteDrawable);
        //keyboard背景颜色
        mKeyboardBackground = ta.getColor(R.styleable.ComputerKeyBordView_keyboardBackgBackground, Color.WHITE);
        mPaddingLeft = (int) ta.getDimension(R.styleable.ComputerKeyBordView_leftPadding, 1);
        mPaddingRight = (int) ta.getDimension(R.styleable.ComputerKeyBordView_rightPadding, 1);
        mPaddingTop = (int) ta.getDimension(R.styleable.ComputerKeyBordView_topPadding, 1);
        mPaddingBottom = (int) ta.getDimension(R.styleable.ComputerKeyBordView_bottomPadding, 1);
        mKeySize = (int) ta.getDimension(R.styleable.ComputerKeyBordView_keyTextSize, 15);
        ta.recycle();

        //获取xml中的按键布局
        Keyboard keyboard = new Keyboard(context, R.xml.identity_card_numkey_view);
        setKeyboard(keyboard);

        setEnabled(true);
        setPreviewEnabled(false);
        setOnKeyboardActionListener(this);

    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //背景
        canvas.drawColor(mKeyboardBackground);

        drawKeyboardBorder(canvas);

        Keyboard keyboard = getKeyboard();
        if (keyboard == null) {
            return;
        }
        List<Keyboard.Key> keys = keyboard.getKeys();
        if (keys != null && keys.size() > 0) {

            for (Keyboard.Key key : keys) {
                if (key.codes[0] == Keyboard.KEYCODE_DELETE) {
                    //数字键背景
                    drawKeyBackGround(key, canvas);
                    //绘制删除键背景
                    drawKeyDelete(key, canvas);
                } else {
                    //数字键背景
                    drawKeyBackGround(key, canvas);
                }

                if (key.label != null) {
                    drawText(key, canvas);
                }
            }
        }

        if (mKey != null) {
            if (isClick) {
                //点击时的背景
                drawKeyClickBackGround(mKey, canvas);
                drawText(mKey, canvas);
            } else {
                //数字键背景
                drawKeyBackGround(mKey, canvas);
                drawText(mKey, canvas);

            }
        }


    }

    /**
     * 绘制边框
     *
     * @param canvas
     */
    private void drawKeyboardBorder(Canvas canvas) {

    }


    /**
     * 绘制按键文字
     *
     * @param key
     * @param canvas
     */
    private void drawText(Keyboard.Key key, Canvas canvas) {
        //删除按键
        if (key.codes[0] == Keyboard.KEYCODE_DELETE) {
            drawKeyDelete(key, canvas);
//            drawableToBitmap(key, canvas);
        } else {
            Paint paint = new Paint();
            paint.setTextAlign(Paint.Align.CENTER);
            //设置字体样式
            Typeface font = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL);
            paint.setTypeface(font);
            paint.setAntiAlias(true);
            paint.setColor(getContext().getResources().getColor(R.color.c000000));
            paint.setTextSize(mKeySize);

            Rect rect = new Rect(key.x, key.y, key.x + key.width, key.y + key.height);
            Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
            int baseline = (rect.bottom + rect.top - fontMetrics.bottom - fontMetrics.top) / 2;
            // 下面这行是实现水平居中，drawText对应改为传入targetRect.centerX()
            paint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText(key.label.toString(), rect.centerX(), baseline, paint);
        }

    }

    /**
     * 数字键背景
     *
     * @param key
     * @param canvas
     */
    private void drawKeyBackGround(Keyboard.Key key, Canvas canvas) {
        mKryDrawable.setBounds(key.x, key.y, key.x + key.width, key.y + key.height);
        mKryDrawable.draw(canvas);
    }

    /**
     * 点击时的背景
     *
     * @param key
     * @param canvas
     */
    private void drawKeyClickBackGround(Keyboard.Key key, Canvas canvas) {
        mKryClickDrawable.setBounds(key.x, key.y, key.x + key.width, key.y + key.height);
        mKryClickDrawable.draw(canvas);
    }


    /**
     * 删除键图片
     *
     * @param key
     * @param canvas
     */
    private void drawKeyDelete(Keyboard.Key key, Canvas canvas) {
        int drawWidth = key.width;
        int drawHeight = key.height;
        drawWidth = drawWidth / 2;
        drawHeight = drawHeight / 2;
        int widthInterval = (key.width - drawWidth) / 2;
        int heightInterval = (key.height - drawHeight) / 2;

        Bitmap bitmap = Bitmap.createBitmap(
                mDeleteKeyDrawable.getIntrinsicWidth(),
                mDeleteKeyDrawable.getIntrinsicHeight(),
                mDeleteKeyDrawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                        : Bitmap.Config.RGB_565);

        Rect mTopSrcRect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        Rect mTopDestRect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        canvas.drawBitmap(bitmap, mTopSrcRect, mTopDestRect, null);

        mDeleteKeyDrawable.setBounds(key.x + widthInterval, key.y + heightInterval,
                key.x + widthInterval + drawWidth, key.y + heightInterval + drawHeight);
        mDeleteKeyDrawable.draw(canvas);




    }


    public void drawableToBitmap(Keyboard.Key key, Canvas canvas) {
        int drawWidth = key.width;
        int drawHeight = key.height;
        drawWidth = drawWidth / 2;
        drawHeight = drawHeight / 2;
        int widthInterval = (key.width - drawWidth) / 2;
        int heightInterval = (key.height - drawHeight) / 2;
        Bitmap bitmap = Bitmap.createBitmap(
                mDeleteKeyDrawable.getIntrinsicWidth(),
                mDeleteKeyDrawable.getIntrinsicHeight(),
                mDeleteKeyDrawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                        : Bitmap.Config.RGB_565);

        Canvas d = new Canvas(bitmap);
        mDeleteKeyDrawable.setBounds(key.x + widthInterval, key.y + heightInterval,
                key.x + widthInterval + drawWidth, key.y + heightInterval + drawHeight);
        mDeleteKeyDrawable.draw(d);

        Rect mTopSrcRect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        Rect mTopDestRect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        canvas.drawBitmap(bitmap, mTopSrcRect, mTopDestRect, null);

    }

    /**
     * 按键点击监听器
     *
     * @param li
     */
    public void setOnKeyPressListener(OnKeyPressListener li) {
        mOnKeyPressListener = li;
    }

    @Override
    public void onKey(int i, int[] ints) {
        if (i == 0) {
            return;
        }
        Log.e("---> key : ", "onKey");
        if (i == Keyboard.KEYCODE_DELETE && mOnKeyPressListener != null) {
            //删除数据回调
            mOnKeyPressListener.onDeleteKey();

        } else if (i != KEYCODE_EMPTY) {
            //添加数据回调
            mOnKeyPressListener.onInertKey(Character.toString((char) i));
        }
    }

    @Override
    public void onPress(int i) {
        if (i == 0) {
            return;
        }
        Log.e("---> key : ", "onPress" + " : " + i);
        isClick = true;
        setKeyBackground(i);
    }


    @Override
    public void onRelease(int i) {
        if (i == 0) {
            return;
        }
        Log.e("---> key : ", "onRelease" + " : " + i);
        isClick = false;
        setKeyBackground(i);
    }


    @Override
    public void onText(CharSequence charSequence) {
        Log.e("---> key : ", "onText" + ":" + charSequence.toString());
    }

    @Override
    public void swipeRight() {
        super.swipeRight();
        Log.e("---> key : ", "swipeRight");
    }

    @Override
    public void swipeDown() {
        super.swipeDown();
        Log.e("---> key : ", "swipeDown");
    }

    @Override
    public void swipeLeft() {
        super.swipeLeft();
        Log.e("---> key : ", "swipeLeft");
    }

    @Override
    public void swipeUp() {
        super.swipeUp();
        Log.e("---> key : ", "swipeUp");
    }


    /**
     * 回调接口
     */
    public interface OnKeyPressListener {
        /**
         * 添加数据回调
         *
         * @param text
         */
        void onInertKey(String text);

        /**
         * 删除数据回调
         */
        void onDeleteKey();

    }


    /*****************************************************************/

    private int sp2px(float spValue) {
        final float fontScale = getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    private void setKeyBackground(int i) {
        Keyboard keyboard = getKeyboard();
        if (keyboard == null) {
            return;
        }
        List<Keyboard.Key> keys = keyboard.getKeys();
        for (int j = 0; j < keys.size(); j++) {
            Keyboard.Key key = keys.get(j);
            if (key.codes[0] == i) {
                mKey = keys.get(j);
                break;
            }
        }
        invalidate();
    }


}

















