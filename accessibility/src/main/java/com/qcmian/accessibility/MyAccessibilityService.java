package com.qcmian.accessibility;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.content.SharedPreferences;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.PointF;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyAccessibilityService extends AccessibilityService {
    static final String TAG = "LLJJZZ";
    View mLayout;
    Model model = null;
    List<FloatingButton> floatingButtonList = new ArrayList<>();

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        Toast.makeText(this, "无障碍服务开启成功", Toast.LENGTH_LONG).show();
        Log.d(TAG, "onServiceConnected");

        LayoutInflater inflater = LayoutInflater.from(this);
        mLayout = inflater.inflate(R.layout.layout, null);
        FloatingButton floatingButton = new FloatingButton(mLayout, this);
        floatingButton.show();
        mLayout.findViewById(R.id.power).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                SharedPreferences sp = getSharedPreferences("MAIN_SP", MODE_PRIVATE);
                if (floatingButtonList.size() > 0) {
                    PositionModel positionModel = new PositionModel();
                    positionModel.map = new HashMap<>();
                    for (FloatingButton b : floatingButtonList) {
                        String key = ((TextView) b.mFloatingView.findViewById(R.id.num)).getText().toString();
                        positionModel.map.put(key, b.getPosition());
                    }
                    sp.edit().putString("positionModel", GSONProvider.get().toJson(positionModel)).commit();
                }
                Toast.makeText(v.getContext(), "位置保存", Toast.LENGTH_LONG).show();
                return true;
            }
        });
        mLayout.findViewById(R.id.power).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences sp = getSharedPreferences("MAIN_SP", MODE_PRIVATE);
                String str = sp.getString("model", "");
                if (!TextUtils.isEmpty(str)) {
                    model = GSONProvider.get().fromJson(str, Model.class);
                }
                str = sp.getString("positionModel", "");
                PositionModel positionModel = null;
                if (!TextUtils.isEmpty(str)) {
                    positionModel = GSONProvider.get().fromJson(str, PositionModel.class);
                }

                if (model == null || model.list == null || model.list.size() == 0) {
                    return;
                }
                if (model.index == null || model.index.size() <= 0) {
                    return;
                }
                if (floatingButtonList.size() > 0) {
                    for (FloatingButton b : floatingButtonList) {
                        b.remove();
                    }
                    floatingButtonList.clear();
                }
                for (int i : model.index) {
                    List<PointF> list = model.list.get(model.index.get(i)).list;
                    if (list == null || list.size() <= 0) {
                        continue;
                    }
                    LayoutInflater inflater = LayoutInflater.from(MyAccessibilityService.this);
                    View layout = inflater.inflate(R.layout.tag_btn_layout, null);
                    ((TextView) layout.findViewById(R.id.num)).setText(String.valueOf(i));
                    layout.findViewById(R.id.num).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int i = Integer.valueOf(((TextView) v.findViewById(R.id.num)).getText().toString());
                            List<PointF> list = model.list.get(model.index.get(i)).list;
                            int delay = 0;
                            for (final PointF pointF : list) {
                                mLayout.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        click(pointF.x, pointF.y);
                                    }
                                }, delay);
                                delay += 100;


                            }
                        }
                    });
                    FloatingButton floatingButton = new FloatingButton(layout, MyAccessibilityService.this);
                    floatingButtonList.add(floatingButton);
                    floatingButton.show();
                    if (positionModel != null && positionModel.map != null && positionModel.map.size() > 0) {
                        PointF pointF = positionModel.map.get(((TextView) layout.findViewById(R.id.num)).getText().toString());
                        if (pointF != null) {
                            floatingButton.setPosition(pointF.x, pointF.y);
                        }
                    }
                }
            }
        });
    }

    private void click(float x, float y) {
        final int DURATION = 1;
        Path clickPath = new Path();
        clickPath.moveTo(x, y);
        GestureDescription.StrokeDescription clickStroke =
                new GestureDescription.StrokeDescription(clickPath, 0, DURATION);
        GestureDescription.Builder clickBuilder = new GestureDescription.Builder();
        clickBuilder.addStroke(clickStroke);
        GestureDescription description = clickBuilder.build();
        boolean ret = dispatchGesture(description, null, null);
        Log.d(TAG, "click:" + ret + " " + x + " " + y);
    }


    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        Log.d(TAG, "onAccessibilityEvent：" + event);
    }

    @Override
    public void onInterrupt() {
        Log.d(TAG, "onInterrupt");
    }
}
