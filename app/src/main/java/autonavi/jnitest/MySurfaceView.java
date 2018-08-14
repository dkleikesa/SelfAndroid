package autonavi.jnitest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.graphics.drawable.shapes.Shape;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    SurfaceHolder mHolder;

    public MySurfaceView(Context context) {
        super(context);
        initView();
    }


    public MySurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public MySurfaceView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    void initView() {
        setZOrderOnTop(true);
        AnimationDrawable drawable = new AnimationDrawable();
        int dur = 500;
        BitmapDrawable bitmapDrawable1 = new BitmapDrawable(getResources(), BitmapFactory.decodeResource(getResources(), R.drawable.week1));
        bitmapDrawable1.setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        drawable.addFrame(bitmapDrawable1, dur);
        BitmapDrawable bitmapDrawable2 = new BitmapDrawable(getResources(), BitmapFactory.decodeResource(getResources(), R.drawable.week2));
        bitmapDrawable2.setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        drawable.addFrame(bitmapDrawable2, dur);
        BitmapDrawable bitmapDrawable3 = new BitmapDrawable(getResources(), BitmapFactory.decodeResource(getResources(), R.drawable.week3));
        bitmapDrawable3.setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        drawable.addFrame(bitmapDrawable3, dur);
        BitmapDrawable bitmapDrawable4 = new BitmapDrawable(getResources(), BitmapFactory.decodeResource(getResources(), R.drawable.week4));
        bitmapDrawable4.setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        drawable.addFrame(bitmapDrawable4, dur);
        BitmapDrawable bitmapDrawable5 = new BitmapDrawable(getResources(), BitmapFactory.decodeResource(getResources(), R.drawable.week5));
        bitmapDrawable5.setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        drawable.addFrame(bitmapDrawable5, dur);
        BitmapDrawable bitmapDrawable6 = new BitmapDrawable(getResources(), BitmapFactory.decodeResource(getResources(), R.drawable.week6));
        bitmapDrawable6.setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        drawable.addFrame(bitmapDrawable6, dur);
        BitmapDrawable bitmapDrawable7 = new BitmapDrawable(getResources(), BitmapFactory.decodeResource(getResources(), R.drawable.week7));
        bitmapDrawable7.setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        drawable.addFrame(bitmapDrawable7, dur);
//        RoundImageDrawable drawable = new RoundImageDrawable(R.drawable.week6);
//        drawable.setRound(50);
        setBackground(drawable);
        drawable.start();

//        setBackground(new ColorDrawable(Color.WHITE));

        mHolder = getHolder();
        mHolder.setFormat(PixelFormat.TRANSPARENT);
        mHolder.addCallback(this);
    }

    class RoundImageDrawable extends Drawable {
        private Paint mPaint;
        private Bitmap mBitmap;
        private RectF mRectF;
        private int mRound;

        public RoundImageDrawable(int bitmap) {
            this.mBitmap = BitmapFactory.decodeResource(getResources(), bitmap);
//            setBounds(0,0,mBitmap.getWidth(),mBitmap.getHeight());
            mPaint = new Paint();
            mPaint.setAntiAlias(true);
            BitmapShader shader = new BitmapShader(mBitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
            mPaint.setShader(shader);
        }

        /**
         * 初始化区域
         */
        @Override
        public void setBounds(int left, int top, int right, int bottom) {
            mRectF = new RectF(left, top, right, bottom);
            super.setBounds(left, top, right, bottom);
        }

        /**
         * 核心代码： 绘制圆角
         */
        @Override
        public void draw(Canvas canvas) {
            canvas.drawRoundRect(mRectF, mRound, mRound, mPaint);
        }

        /**
         * 暴露给外面设置圆角的大小
         *
         * @param round
         */
        public void setRound(int round) {
            this.mRound = round;
        }

        /**
         * getIntrinsicWidth、getIntrinsicHeight主要是为了在View使用wrap_content的时候，
         * 提供一下尺寸，默认为-1可不是我们希望的
         */
        @Override
        public int getIntrinsicHeight() {
            return mBitmap.getHeight();
        }

        @Override
        public int getIntrinsicWidth() {
            return mBitmap.getWidth();
        }

        /**
         * 根据画笔设定drawable的透明度
         */
        @Override
        public void setAlpha(int alpha) {
            mPaint.setAlpha(alpha);
        }

        /**
         * 根据画笔设定drawable的颜色过滤器
         */
        @Override
        public void setColorFilter(ColorFilter cf) {
            mPaint.setColorFilter(cf);
        }

        @Override
        public int getOpacity() {
            return PixelFormat.TRANSLUCENT;
        }

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
}
