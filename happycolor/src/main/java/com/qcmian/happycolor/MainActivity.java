package com.qcmian.happycolor;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qcmian.happycolor.mission.Mission;
import com.qcmian.happycolor.mission.MissionColorFactory;

public class MainActivity extends Activity {
    private static final boolean isFullScreen = true;

    RelativeLayout mRootLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (isFullScreen) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        setContentView(R.layout.activity_main);

        mRootLayout = findViewById(R.id.root_layout);

        new Mission(this, mRootLayout, MissionColorFactory.missionColors[0]);


        mRootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mRootLayout.addView(view);
            }
        });
        TextView tv = findViewById(R.id.score);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Mission(MainActivity.this, mRootLayout, MissionColorFactory.missionColors[0]);
            }
        });

    }


}
