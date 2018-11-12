package com.ljz.qcmian;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.ljz.qcmian.location.LocationService;
import com.ljz.qcmian.utils.Utils;


public class MainActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView tv = (TextView) this.findViewById(R.id.tv);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String str = Build.SERIAL;
                str += Utils.getDeviceID(MainActivity.this.getApplicationContext());
                tv.setText(str);
                Intent intent = new Intent(MainActivity.this,LocationService.class);
                MainActivity.this.startService(intent);

            }
        });
    }

}
