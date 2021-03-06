package com.example.guojin.keyboarddemo.card;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.support.annotation.IntDef;
import android.util.AttributeSet;
import android.util.Log;

import com.example.guojin.keyboarddemo.R;
import com.example.guojin.keyboarddemo.utils.BitmapUtils;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

/**
 * @author puyantao
 * @describe
 * @create 2019/10/14 13:37
 */
public class NumberKeyBoardView extends KeyboardView implements KeyboardView.OnKeyboardActionListener {
    /**
     * 电话类型
     */
    public static final int PHONE_TYPE = 10;
    /**
     * 身份证类型
     */
    public static final int CARD_TYPE = 11;

    @IntDef({PHONE_TYPE, CARD_TYPE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface KeyBoardType {

    }

    /**
     * 用于区分左下角空白按键(暂时不用)
     */
    private int KEYCODE_EMPTY = -10;

    /**
     * 控制显示不同的数字
     */
    private int KEY_NUMBER_TYPE = -11;
    /**
     * 删除按键背景图片
     */
    private Drawable mDeleteKeyDrawable;
    private int mKeyboardBackground;
    /**
     * 加载键盘的类型
     */
    private int mKeyBoardType = -1;
    /**
     * 按键背景
     */
    private Drawable mKeyDrawable;
    private Drawable mKeyClickDrawable;
    private int mKeySize;
    private int mPaddingLeft;
    private int mPaddingRight;
    private int mPaddingTop;
    private int mPaddingBottom;
    private Keyboard.Key mKey;
    /**
     *  是否点击
     */
    private boolean isClick = false;

    /**
     *  是否长按
     */
    private boolean isLongClick = false;

    private OnKeyPressListener mOnKeyPressListener;


    public NumberKeyBoardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public NumberKeyBoardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }


    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ComputerKeyBoardView);
        mKeyDrawable = ta.getDrawable(R.styleable.ComputerKeyBoardView_keyBackgBackground);
        mKeyClickDrawable = ta.getDrawable(R.styleable.ComputerKeyBoardView_keyClickBackgBackground);
        //删除按键颜色
        mDeleteKeyDrawable = ta.getDrawable(R.styleable.ComputerKeyBoardView_deleteDrawable);
        //keyboard背景颜色
        mKeyboardBackground = ta.getColor(R.styleable.ComputerKeyBoardView_keyboardBackgBackground, Color.WHITE);
        mPaddingLeft = (int) ta.getDimension(R.styleable.ComputerKeyBoardView_leftPadding, 1);
        mPaddingRight = (int) ta.getDimension(R.styleable.ComputerKeyBoardView_rightPadding, 1);
        mPaddingTop = (int) ta.getDimension(R.styleable.ComputerKeyBoardView_topPadding, 1);
        mPaddingBottom = (int) ta.getDimension(R.styleable.ComputerKeyBoardView_bottomPadding, 1);
        mKeySize = (int) ta.getDimension(R.styleable.ComputerKeyBoardView_keyTextSize, 15);
        mKeyBoardType = ta.getInt(R.styleable.ComputerKeyBoardView_keyboardType, -1);
        ta.recycle();

        //获取xml中的按键布局
        Keyboard keyboard = new Keyboard(context, R.xml.num_key_borad_view);
        setKeyboard(keyboard);

        setEnabled(true);
        //禁止按键的浏览试图
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
                    drawKeyBackground(key, canvas);
                    //绘制删除键背景
                    drawDeleteBitmap(key, canvas);
                } else {
                    //数字键背景
                    drawKeyBackground(key, canvas);
                }

                if (key.label != null) {
                    drawText(key, canvas);
                }
            }
        }

        if (mKey != null) {
            if (isClick) {
                //点击时的背景
                drawKeyClickBackground(mKey, canvas);
                drawText(mKey, canvas);
            } else {
                //数字键背景
                drawKeyBackground(mKey, canvas);
                drawText(mKey, canvas);

            }

            if (isLongClick && !isClick){
                drawKeyClickBackground(mKey, canvas);
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
            drawDeleteBitmap(key, canvas);

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
            if (key.codes[0] == KEY_NUMBER_TYPE) {
                if (mKeyBoardType == PHONE_TYPE) {
                    canvas.drawText("-", rect.centerX(), baseline, paint);

                } else if (mKeyBoardType == CARD_TYPE) {
                    canvas.drawText("X", rect.centerX(), baseline, paint);

                }
            } else {
                canvas.drawText(key.label.toString(), rect.centerX(), baseline, paint);
            }
        }

    }


    /**
     * 绘制删除按键
     *
     * @param key
     * @param canvas
     */
    private void drawDeleteBitmap(Keyboard.Key key, Canvas canvas) {
        Bitmap bitmap1 = BitmapUtils.drawableToBitmap(mDeleteKeyDrawable);
        int width = bitmap1.getWidth() / 2;
        int height = bitmap1.getHeight() / 2;
        Rect rect = new Rect(key.x, key.y, key.x + key.width, key.y + key.height);
        canvas.drawBitmap(bitmap1, rect.centerX() - width, rect.centerY() - height, null);

    }

    /**
     * 数字键背景
     *
     * @param key
     * @param canvas
     */
    private void drawKeyBackground(Keyboard.Key key, Canvas canvas) {
        mKeyDrawable.setBounds(key.x, key.y, key.x + key.width, key.y + key.height);
        mKeyDrawable.draw(canvas);
    }

    /**
     * 点击时的背景
     *
     * @param key
     * @param canvas
     */
    private void drawKeyClickBackground(Keyboard.Key key, Canvas canvas) {
        mKeyClickDrawable.setBounds(key.x, key.y, key.x + key.width, key.y + key.height);
        mKeyClickDrawable.draw(canvas);
    }


    /**
     * 设置键盘类型
     *
     * @param type
     */
    public void setKeyBoardType(@KeyBoardType int type) {
        this.mKeyBoardType = type;
        invalidate();
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

        } else if (i == KEY_NUMBER_TYPE && mOnKeyPressListener != null) {
            if (mKeyBoardType == PHONE_TYPE) {
                mOnKeyPressListener.onInertKey("-");

            } else if (mKeyBoardType == CARD_TYPE) {
                mOnKeyPressListener.onInertKey("X");

            }
        } else {
            //添加数据回调
            if (mOnKeyPressListener != null) {
                mOnKeyPressListener.onInertKey(Character.toString((char) i));
            }
        }
    }

    @Override
    public void onPress(int i) {
        if (i == 0) {
            return;
        }
        Log.e("---> key : ", "onPress" + " : " + i);
        isClick = true;
        isLongClick = false;
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

















