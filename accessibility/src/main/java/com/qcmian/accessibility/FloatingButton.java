package com.qcmian.accessibility;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.PointF;
import android.net.Uri;
import android.os.Build;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

public class FloatingButton implements View.OnTouchListener {

    private WindowManager.LayoutParams mWindowParams;
    private WindowManager mWindowManager;
    public MyLayout mFloatingView;
    private Context mContext;

    private boolean isShowing = false;


    public FloatingButton(View view, Context context) {
        isShowing = false;
        mContext = context;
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        buildParams();
        mFloatingView = new MyLayout(context);
        mFloatingView.addView(view);
    }

    public  class MyLayout extends FrameLayout {

        public MyLayout(@NonNull Context context) {
            super(context);
        }

        public MyLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
            super(context, attrs);
        }

        public MyLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }

        public MyLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
        }

        @Override
        public boolean onInterceptTouchEvent(MotionEvent ev) {
            onTouch(this, ev);
            return super.onInterceptTouchEvent(ev);
        }
    }


    private void buildParams() {
        mWindowParams = new WindowManager.LayoutParams();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mWindowParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            mWindowParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        }

        mWindowParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
        mWindowParams.gravity = Gravity.LEFT | Gravity.TOP;
        mWindowParams.format = PixelFormat.TRANSPARENT;
        mWindowParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mWindowParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
    }

    private float mDiffX = 0;
    private float mDiffY = 0;
    private PointF mDownPoint;
    private PointF mCurrentPoint;
    private long mDownTime;

    @Override
    public boolean onTouch(View v, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDiffX = mWindowParams.x - motionEvent.getRawX();
                mDiffY = mWindowParams.y - motionEvent.getRawY();
                mCurrentPoint = mDownPoint = new PointF(motionEvent.getRawX(), motionEvent.getRawY());
                mDownTime = SystemClock.elapsedRealtime();
                v.postDelayed(mLongClickCheckRunnable, 1000);
                break;
            case MotionEvent.ACTION_MOVE:
                mCurrentPoint = new PointF(motionEvent.getRawX(), motionEvent.getRawY());
                windowX = mWindowParams.x = (int) (mDiffX + motionEvent.getRawX());
                windowY = mWindowParams.y = (int) (mDiffY + motionEvent.getRawY());
                mWindowManager.updateViewLayout(mFloatingView, mWindowParams);

                break;
            case MotionEvent.ACTION_UP:
                v.removeCallbacks(mLongClickCheckRunnable);
                if ((Math.abs(mCurrentPoint.x - mDownPoint.x) < 20) &&
                        (Math.abs(mCurrentPoint.y - mDownPoint.y) < 20) &&
                        (SystemClock.elapsedRealtime() - mDownTime) < 500) {
                    onClick();
                }
                break;
        }
        return true;
    }

    private void onClick() {

    }

    private void onLongClick() {

    }

    private Runnable mLongClickCheckRunnable = new Runnable() {
        @Override
        public void run() {
            if ((Math.abs(mCurrentPoint.x - mDownPoint.x) < 20) &&
                    (Math.abs(mCurrentPoint.y - mDownPoint.y) < 20) &&
                    (SystemClock.elapsedRealtime() - mDownTime) > 1000) {
                onLongClick();
            }
        }
    };


    private int windowX;
    private int windowY;

    public PointF getCenter() {
        WindowManager.LayoutParams params = (WindowManager.LayoutParams) mFloatingView.getLayoutParams();
        return new PointF(params.x + mFloatingView.getWidth() / 2, params.y + mFloatingView.getHeight() / 2);
    }

    public PointF getPosition() {
        WindowManager.LayoutParams params = (WindowManager.LayoutParams) mFloatingView.getLayoutParams();
        return new PointF(params.x, params.y);
    }

    public void setPosition(float x, float y) {
        mWindowParams.x = (int) x;
        mWindowParams.y = (int) y;
        mWindowManager.updateViewLayout(mFloatingView, mWindowParams);
    }


    public void show() {
        if (isShowing) {
            return;
        }
        try {
            if (!checkFloatingWindowPermission(mContext)) {
                requestSettingCanDrawOverlays(mContext);
            } else {
                mWindowManager.addView(mFloatingView, mWindowParams);
                isShowing = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void remove() {
        if (!isShowing) {
            return;
        }
        try {
            if (!checkFloatingWindowPermission(mContext)) {
                requestSettingCanDrawOverlays(mContext);
            } else {
                mWindowManager.removeView(mFloatingView);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            isShowing = false;
        }
    }

    private boolean checkFloatingWindowPermission(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(context)) {
                return false;
            }
        }
        return true;
    }

    private void requestSettingCanDrawOverlays(Context context) {
        int sdkInt = Build.VERSION.SDK_INT;
        if (sdkInt >= Build.VERSION_CODES.M) {//6.0以上
            Toast.makeText(context, "请打开显示悬浮窗开关!", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setData(Uri.parse("package:" + context.getPackageName()));
            ((Activity) context).startActivityForResult(intent, 'j');
        }
    }

    public boolean isShowing() {
        return isShowing;
    }


}
