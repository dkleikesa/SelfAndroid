package com.qcmian.happycolor.mission.circle;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.RelativeLayout;

import com.qcmian.happycolor.mission.MissionColorFactory;

public class CircleView extends View {

    private AnimatorSet animatorSet;
    private Bitmap bitmap;
    public CircleViewParam param;
    public boolean isShow = true;
    public int x;
    public int y;
    public Point touchEvent;
    public boolean animateX = true;

    public CircleView(Context context, CircleViewParam param) {
        super(context);
        if (param == null) {
            throw new RuntimeException("CircleViewParam is null");
        }
        this.param = param;
        int radius = param.circle.radius;
        int edgeWidth = (int) param.edgeWidth;


        int offset = radius;
        int left = (int) (param.positionX - offset);
        int right = (int) (param.positionX + offset);
        int top = (int) (param.positionY - offset);
        int bottom = (int) (param.positionY + offset);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(left, top, right, bottom);
        setLayoutParams(layoutParams);

        bitmap = Bitmap.createBitmap(radius * 2, radius * 2, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(param.circle.circleColor);
        canvas.drawCircle(radius, radius, radius, paint);

        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(edgeWidth);
        paint.setColor(param.circle.edgeColor);
        canvas.drawCircle(radius, radius, radius, paint);
        setBackground(new BitmapDrawable(bitmap));

        initDisappearAnimator(param.disappearDuration);
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

        animatorSet = new AnimatorSet();
        animatorSet.playTogether(animator1, animator2, animator3);
    }

    public void disAppear() {
        isShow = false;
        animatorSet.start();
    }

    public void addAnimatorListener(Animator.AnimatorListener listener) {
        animatorSet.addListener(listener);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(param.circle.radius * 2, param.circle.radius * 2);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        x = left + param.circle.radius;
        y = top + param.circle.radius;
    }


    public static class CircleViewParam {
        public float positionX;
        public float positionY;
        public float edgeWidth;
        public long disappearDuration;
        public MissionColorFactory.Circle circle;

    }
}
