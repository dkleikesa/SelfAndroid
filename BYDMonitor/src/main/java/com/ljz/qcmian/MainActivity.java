package com.ljz.qcmian;

import android.Manifest;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.ljz.qcmian.utils.ThreadExecutor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class MainActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.M)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String[] permission = {Manifest.permission.READ_PHONE_STATE};
        this.requestPermissions(permission, 1);
        final TextView tv = (TextView) this.findViewById(R.id.tv);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {

                String str = Build.SERIAL;
                str += "\n";
                try {
//                    str += Build.getSerial();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                str += "\n";
                str += getSerialNumber();
                tv.setText(str);
                ThreadExecutor.post(new Runnable() {
                    @Override
                    public void run() {
                        Hello.main(null);
                    }
                });

            }
        });
////        if (MLog.ret()) {
        String A = "eddefefefe";
//        MLog.printlog(getSerialNumber());
//        TestClass.testLog(A);
////        }
        Log.e(A,(getSerialNumber()));
        Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg){

            }
        };
    }


    public String getSerialNumber() {
        // This field was deprecated in API level O. Use getSerial() instead.
        Object o = new Object();
        synchronized (o) {
            if (Build.VERSION.SDK_INT >= 26) {
                try {
                    Class<?> buildClazz = Class.forName("android.os.Build");
                    Method getMethod = buildClazz.getMethod("getSerial");
                    return (String) getMethod.invoke(null);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }
            if (Build.UNKNOWN.equalsIgnoreCase(Build.SERIAL)) {
                return "";
            }
        }
        return Build.SERIAL;
    }
}
