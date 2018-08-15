package com.qcmian.happycolor.mission.circle;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.RelativeLayout;

public class CircleView extends View {

    private AnimatorSet mAnimatorSet;
    public int mSideLength;
    private Bitmap mBitmap;
    public CircleViewParam mParam;
    public boolean isShow = true;
    public int x;
    public int y;


    public CircleView(Context context, CircleViewParam param, int screenWidth, int screenHeight) {
        super(context);
        if (param == null) {
            throw new RuntimeException("CircleViewParam is null");
        }
        mParam = param;

        int tag = screenWidth >= screenHeight ? screenHeight : screenWidth;
        int radius = (int) (tag * param.mRadius);
        int edgeWidth = (int) (tag * param.mEdgeWidth);
        mSideLength = (radius + edgeWidth) * 2;

        int offset = (radius + edgeWidth);
        int left = (int) (screenWidth * param.mPositionX - offset);
        int right = (int) (screenWidth * param.mPositionX + offset);
        int top = (int) (screenHeight * param.mPositionY - offset);
        int bottom = (int) (screenHeight * param.mPositionY + offset);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(left, top, right, bottom);
        setLayoutParams(layoutParams);

        int x = mSideLength / 2;
        int y = mSideLength / 2;
        mBitmap = Bitmap.createBitmap(mSideLength, mSideLength, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(mBitmap);
        Paint paint = new Paint();
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(param.mCircleColor);
        canvas.drawCircle(x, y, radius, paint);

        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(edgeWidth);
        paint.setColor(param.mEdgeColor);
        canvas.drawCircle(x, y, radius + edgeWidth / 2, paint);
        setBackground(new BitmapDrawable(mBitmap));

        initDisappearAnimator(param.mDisappearDuration);
    }

    private void initDisappearAnimator(long duration) {
        final ObjectAnimator animator1 = ObjectAnimator.ofFloat(this, "scaleX", 1f, 2f);
        animator1.setStartDelay(0);
        animator1.setDuration(duration);
        animator1.setInterpolator(new LinearInterpolator());
        final ObjectAnimator animator2 = ObjectAnimator.ofFloat(this, "scaleY", 1f, 2f);
        animator2.setStartDelay(0);
        animator2.setDuration(duration);
        animator2.setInterpolator(new LinearInterpolator());

        final ObjectAnimator animator3 = ObjectAnimator.ofFloat(this, "alpha", 1f, 0f);
        animator3.setStartDelay(0);
        animator3.setDuration(duration);
        animator3.setInterpolator(new LinearInterpolator());

        mAnimatorSet = new AnimatorSet();
        mAnimatorSet.playTogether(animator1, animator2, animator3);
    }

    public void disAppear() {
        isShow = false;
        mAnimatorSet.start();
    }

    public void addAnimatorListener(Animator.AnimatorListener listener) {
        mAnimatorSet.addListener(listener);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(mSideLength, mSideLength);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        x = left + mSideLength / 2;
        y = top + mSideLength / 2;
    }


    public static class CircleViewParam {
        public float mPositionX;
        public float mPositionY;
        public float mRadius;
        public float mEdgeWidth;
        public int mCircleColor;
        public int mEdgeColor;
        public long mAppearDuration;
        public long mDisappearDuration;

    }
}
