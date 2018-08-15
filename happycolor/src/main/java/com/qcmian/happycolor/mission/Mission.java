package com.qcmian.happycolor.mission;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.qcmian.happycolor.Utils;
import com.qcmian.happycolor.mission.circle.CircleView;

import java.util.Random;

public class Mission {
    private float mDefaultRadius = 0.1f;
    private float mDefaultEdgeWidth = 0.0f;
    private long mDefaultDuration = 2000;
    CircleView[] mViews;
    Random random;
    Activity mActivity;
    RelativeLayout mLayout;
    int mAcitveNum = 0;

    public Mission(Activity activity, RelativeLayout layout, MissionColorFactory.MissionColor missionColor) {
        mActivity = activity;
        mLayout = layout;
        layout.setBackgroundColor(missionColor.backgroundColor);
        mViews = new CircleView[missionColor.CircleNum];
        random = new Random(System.currentTimeMillis());
        mAcitveNum = missionColor.CircleNum;
        for (int i = 0; i < missionColor.CircleNum; i++) {
            float x = getFloat();
            float y = getFloat();
            while (!viable(mViews, x, y)) {
                x = getFloat();
                y = getFloat();
            }
            mViews[i] = createCircleView(activity, x, y, missionColor.circleColors[i].circleColor, missionColor.circleColors[i].edgeColor);
            layout.addView(mViews[i]);

            mViews[i].setOnTouchListener(mListener);
        }

    }

    View.OnTouchListener mListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_MOVE:
                    moveCircleViewTo((CircleView) v, event);
                    check((CircleView) v);
                    break;
            }

            return true;
        }
    };

    private void check(CircleView view) {
        for (CircleView v : mViews) {
            if ((v == null)||(!v.isShow)) {
                continue;
            }

            if ((view.mParam.mCircleColor == v.mParam.mCircleColor) && (view != v)) {
                if (getDistance(view.x, view.y, v.x, v.y) < (v.mSideLength / 4)) {
                    v.disAppear();
                    view.disAppear();
                    mAcitveNum -= 2;
                    continue;
                }
            }
        }
        if (mAcitveNum <= 1) {
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
                for (CircleView v : mViews) {
                    if (v == null) {
                        continue;
                    }
                    mLayout.removeView(v);
                }
            }
        }, mDefaultDuration);
    }


    private float getDistance(float x1, float y1, float x2, float y2) {
        float dx = x1 - x2;
        float dy = y1 - y2;
        return (float) Math.sqrt(dx * dx + dy * dy);
    }

    private void moveCircleViewTo(CircleView v, MotionEvent event) {
        int x = (int) event.getRawX();
        int y = (int) event.getRawY();
        int offset = v.mSideLength / 2;
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) v.getLayoutParams();
        params.setMargins(x - offset, y - offset, x + offset, y + offset);
        v.setLayoutParams(params);
    }

    private boolean viable(CircleView[] views, float x, float y) {
        for (CircleView v : views) {
            if (v == null) {
                continue;
            }

            if (getDistance(x, y, v.mParam.mPositionX, v.mParam.mPositionY) < (mDefaultRadius * 2)) {
                return false;
            }
        }
        return true;
    }


    private float getFloat() {
        float value = random.nextFloat();
        float threshold = mDefaultRadius * 1.5f;
        return value * (1 - 2 * threshold) + threshold;
    }

    private CircleView createCircleView(Activity activity, float x, float y, int circleColor, int edgeColor) {
        int[] screenSize = Utils.getScreenWidth(activity);
        CircleView.CircleViewParam param = new CircleView.CircleViewParam();
        param.mPositionX = x;
        param.mPositionY = y;
        param.mRadius = mDefaultRadius;
        param.mCircleColor = circleColor;
        param.mEdgeWidth = mDefaultEdgeWidth;
        param.mEdgeColor = edgeColor;
        param.mDisappearDuration = mDefaultDuration;
        return new CircleView(activity, param, screenSize[0], screenSize[1]);
    }


}
