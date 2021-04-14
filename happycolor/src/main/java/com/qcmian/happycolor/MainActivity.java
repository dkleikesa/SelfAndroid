package com.qcmian.happycolor;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qcmian.happycolor.mission.Mission;
import com.qcmian.happycolor.mission.MissionColorFactory;
import com.qcmian.happycolor.utils.TTSHelper;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    private static final boolean isFullScreen = true;

    RelativeLayout mRootLayout;
    int num = 4;

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

        new Mission(MainActivity.this, mRootLayout, MissionColorFactory.generate(2));

        findViewById(R.id.start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Mission(MainActivity.this, mRootLayout, MissionColorFactory.generate(num));

            }
        });

        findViewById(R.id.animate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TTSHelper.speak("click");
                Mission.animate = !Mission.animate;

            }
        });
        findViewById(R.id.one).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TTSHelper.speak("click");
                num = 1;
            }
        });
        findViewById(R.id.two).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TTSHelper.speak("click");
                num = 2;
            }
        });
        findViewById(R.id.four).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TTSHelper.speak("click");
                num = 4;
            }
        });
        findViewById(R.id.six).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TTSHelper.speak("click");
                num = 6;
            }
        });
        findViewById(R.id.eight).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TTSHelper.speak("click");
                num = 8;
            }
        });

        checkPermission(this);


    }

    private void checkPermission(Activity activity) {
        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.INTERNET,

                Manifest.permission.CAMERA
        };
        List<String> needRequest = new ArrayList<>();
        for (String permission : permissions) {
            int hasPermission = ContextCompat.checkSelfPermission(activity.getApplication(), permission);
            if (hasPermission != PackageManager.PERMISSION_GRANTED) {
                needRequest.add(permission);
            }
        }
        if (needRequest.size() <= 0) {
            return;
        }

        ActivityCompat.requestPermissions(activity, needRequest.toArray(new String[needRequest.size()]), 32);

    }

}
