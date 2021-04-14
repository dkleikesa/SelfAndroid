package com.qcmian.happycolor.mission;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.Point;
import android.os.Handler;
import android.os.Looper;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.qcmian.happycolor.R;
import com.qcmian.happycolor.mission.circle.CircleView;
import com.qcmian.happycolor.utils.TTSHelper;
import com.qcmian.happycolor.utils.Utils;

public class Mission {
    private long mDefaultDuration = 2000;
    CircleView[] mViews;
    Activity mActivity;
    RelativeLayout mLayout;
    int mAcitveNum = 0;
    int matchNum = 0;
    CircleView currentView;

    public static boolean animate = true;

    public Mission(Activity activity, RelativeLayout layout, MissionColorFactory.MissionColor missionColor) {
        if (activity == null) {
            throw new RuntimeException("activity is null");
        }
        if (layout == null) {
            throw new RuntimeException("root layout is null");
        }

        if (missionColor == null || missionColor.circles == null) {
            throw new RuntimeException("missionColor is null");
        }
        mActivity = activity;
        mLayout = layout;
        TTSHelper.loadAllSounds(missionColor);

        mLayout.removeAllViews();

        mAcitveNum = missionColor.circles.length;
        if (mAcitveNum <= 0) {
            throw new RuntimeException("no circle");
        }

        layout.setBackgroundColor(missionColor.backgroundColor);
        mViews = new CircleView[mAcitveNum * 2];
        for (int i = 0; i < mAcitveNum; i++) {
            Point p1 = getPosition(missionColor.circles[i].radius);
            CircleView v1 = createCircleView(activity, missionColor.circles[i], p1);
            v1.animateX = Math.random() > 0.5;
            mViews[i * 2] = v1;
            layout.addView(v1);
            v1.setOnTouchListener(mListener);

            Point p2 = getPosition(missionColor.circles[i].radius);
            while (Math.abs(p1.y - p2.y) < 2 * missionColor.circles[i].radius) {
                p2 = getPosition(missionColor.circles[i].radius);
            }
            CircleView v2 = createCircleView(activity, missionColor.circles[i], p2);
            v2.animateX = Math.random() > 0.5;
            mViews[i * 2 + 1] = v2;
            layout.addView(v2);
            v2.setOnTouchListener(mListener);
        }
        createAnimate();
        TTSHelper.speak("appear");
        TTSHelper.playBGM(R.raw.bgm);
//        TTSHelper.speakWithLoop("bgm");
//        ThreadExecutor.executor.scheduleAtFixedRate(new Runnable() {
//            @Override
//            public void run() {
//                TTSHelper.speak("bgm");
//            }
//        }, 0, 34, TimeUnit.SECONDS);
    }

    float lastValue = 0;

    void createAnimate() {
        final int width = Utils.getScreenSize()[0];
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setDuration(6000);
        valueAnimator.setRepeatMode(ValueAnimator.REVERSE);
        valueAnimator.setRepeatCount(-1);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {


                float value = (float) animation.getAnimatedValue();
                if (animate) {
                    for (CircleView view : mViews) {
                        if (view == currentView || !view.isShow) {
                            continue;
                        }
                        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view.getLayoutParams();
                        int total = width - view.param.circle.radius * 2;
                        int diff = (int) Math.abs((value - lastValue) * total);
                        int left;
                        if (view.animateX) {
                            left = params.leftMargin + diff;
                        } else {
                            left = params.leftMargin - diff;
                        }
                        if (left >= total) {
                            view.animateX = false;
                        }
                        if (left <= 0) {
                            view.animateX = true;
                        }
                        params.setMargins(left, params.topMargin, 0, 0);
                        view.setLayoutParams(params);
                    }
                }
                lastValue = value;
            }
        });
        valueAnimator.start();
    }

    View.OnTouchListener mListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    CircleView circleView = (CircleView) v;
                    currentView = circleView;
                    circleView.bringToFront();
                    circleView.touchEvent = new Point((int) event.getRawX(), (int) event.getRawY());
                    TTSHelper.speak(circleView.param.circle.name);
                case MotionEvent.ACTION_MOVE:
                    moveCircleViewTo((CircleView) v, event);
                    check((CircleView) v);
                    break;
                case MotionEvent.ACTION_UP:
                    if (currentView.isShow) {
                        matchNum = 0;
                    }
                    currentView = null;
                    break;
            }

            return true;
        }
    };


    private void check(CircleView view) {
        for (CircleView v : mViews) {
            if ((v == null) || (!v.isShow)) {
                continue;
            }

            if ((view.param.circle.circleColor == v.param.circle.circleColor) && (view != v)) {
                if (getDistance(view.x, view.y, v.x, v.y) < ((v.param.circle.radius + view.param.circle.radius) / 8)) {

                    v.disAppear();
                    view.disAppear();
                    v.setOnTouchListener(null);
                    view.setOnTouchListener(null);
                    matchNum++;
                    mAcitveNum--;

                    if (matchNum > 6) {
                        matchNum = 6;
                    }
                    String sound = "match" + matchNum;
                    TTSHelper.speak(sound);
                    break;
                }
            }
        }
        if (mAcitveNum < 1) {
            finish();
        }
    }

    Handler handler = new Handler(Looper.getMainLooper());

    boolean isFinishing = false;

    void finish() {
        if (isFinishing) {
            return;
        }
        isFinishing = true;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(mActivity, "pass", Toast.LENGTH_SHORT).show();
                mLayout.removeAllViews();
            }
        }, mDefaultDuration);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                TTSHelper.speak("胜利");
            }
        }, mDefaultDuration / 2);

    }


    private float getDistance(float x1, float y1, float x2, float y2) {
        float dx = x1 - x2;
        float dy = y1 - y2;
        return (float) Math.sqrt(dx * dx + dy * dy);
    }

    private void moveCircleViewTo(CircleView v, MotionEvent event) {
        int xOffset = (int) (event.getRawX() - v.touchEvent.x);
        int yOffset = (int) (event.getRawY() - v.touchEvent.y);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) v.getLayoutParams();
        int targetX = params.leftMargin + xOffset;
        int targetY = params.topMargin + yOffset;
        if (targetX < 0) {
            targetX = 0;
        }
        if (targetY < 0) {
            targetY = 0;
        }
        int[] screenSize = Utils.getScreenSize();
        if (targetX > (screenSize[0] - 2 * v.param.circle.radius)) {
            targetX = (screenSize[0] - 2 * (int) v.param.circle.radius);
        }
        if (targetY > (screenSize[1] - 2 * (int) v.param.circle.radius)) {
            targetY = (screenSize[1] - 2 * (int) v.param.circle.radius);
        }
        params.setMargins(targetX, targetY, 0, 0);
        v.setLayoutParams(params);
        v.touchEvent = new Point((int) event.getRawX(), (int) event.getRawY());
    }

    private boolean viable(CircleView[] views, float x, float y, float radius) {
        for (CircleView v : views) {
            if (v == null) {
                continue;
            }
            if (getDistance(x, y, v.param.positionX, v.param.positionY) <= (radius + v.param.circle.radius)) {
                return false;
            }
        }
        return true;
    }


    private int getPosition(int radius, int length) {
        double value = Math.random();
        double threshold = radius * 1.5;
        return (int) (value * (length - 2 * threshold) + threshold);
    }

    private Point getPosition(int radius) {
        int[] screenSize = Utils.getScreenSize();
        int x = getPosition(radius, screenSize[0]);
        int y = getPosition(radius, screenSize[1]);
        while (!viable(mViews, x, y, radius)) {
            x = getPosition(radius, screenSize[0]);
            y = getPosition(radius, screenSize[1]);
        }
        return new Point(x, y);
    }

    private CircleView createCircleView(Activity activity, MissionColorFactory.Circle circle, Point point) {

        CircleView.CircleViewParam param = new CircleView.CircleViewParam();
        param.circle = circle;
        param.edgeWidth = 1;
        param.disappearDuration = mDefaultDuration;
        param.positionX = point.x;
        param.positionY = point.y;

        return new CircleView(activity, param);
    }


}
