package autonavi.jnitest;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.TrafficStats;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import dalvik.system.DexClassLoader;


public class MainActivity extends AppCompatActivity {
    static volatile int num = 0;
    public static String TAG = "MMMainActivity";
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE",
            "android.permission.ACCESS_COARSE_LOCATION", Manifest.permission.RECORD_AUDIO};
    ClassLoader parentClassLoader;
    ClassLoader appClassLoader;
    DexClassLoader dexClassLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        checkPermission(this);
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.e("MainActivity", "onClick");
//                Snackbar.make(view, stringFromJNI(), Snackbar.LENGTH_SHORT)
//                        .setAction("Action", null).show();
//            }
//        });


        // Example of a call to a native method
////        TextView tv = (TextView) findViewById(R.id.sample_text);
//        FrameLayout webViewWrapper = (FrameLayout) findViewById(R.id.webview_wrapper);
//        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.MATCH_PARENT);
//        TextView tv = new TextView(this);
//        tv.setBackgroundColor(Color.WHITE);
//        tv.setMovementMethod(ScrollingMovementMethod.getInstance());
////        tv.setText(Html.fromHtml(html));
//
//
//        ScrollView scrollView = new ScrollView(this.getApplicationContext());
//        FrameLayout.LayoutParams svParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.MATCH_PARENT);
//        svParams.setMargins(50, 0, 10, 0);
//        scrollView.addView(tv, svParams);
//
//        webViewWrapper.addView(scrollView, layoutParams);
//        Log.e(TAG, "1 onCreate"+" taskID:" + this.getTaskId() + " " + this.toString());

        try {
            appClassLoader = MyApplication.class.getClassLoader();
            parentClassLoader = appClassLoader.getParent();
            File dexPath = new File("/sdcard/app-debug.apk");
            File tmpPath = new File(this.getFilesDir().getAbsolutePath(), "/testDexTmp/");
            dexClassLoader = new DexClassLoader(dexPath.getAbsolutePath(), tmpPath.getAbsolutePath(), null, parentClassLoader);

        } catch (Exception e) {
            e.printStackTrace();
        }

        Button b1 = (Button) findViewById(R.id.button3);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(v.getContext(), MyService.class);
                intent.putExtra("abc", "ddd");
                v.getContext().bindService(intent, new ServiceConnection() {
                    @Override
                    public void onServiceConnected(ComponentName name, IBinder service) {

                    }

                    @Override
                    public void onServiceDisconnected(ComponentName name) {

                    }
                }, Context.BIND_AUTO_CREATE);

//                setClassLoadProxy(appClassLoader, dexClassLoader);
                try {
                    Class cls = Class.forName("autonavi.jnitest.Test", false, appClassLoader);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                Log.e("TTT", "btn1 onClick");
                Toast.makeText(MainActivity.this, new Test().TTT, Toast.LENGTH_SHORT).show();
            }
        });
        b1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.e("TTT", "btn1 onLongClick");
                return false;
            }
        });

        Button b2 = (Button) findViewById(R.id.button4);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(v.getContext(), MyService.class);
                intent.putExtra("abc", "eee");
                v.getContext().bindService(intent, new ServiceConnection() {
                    @Override
                    public void onServiceConnected(ComponentName name, IBinder service) {

                    }

                    @Override
                    public void onServiceDisconnected(ComponentName name) {

                    }
                }, Context.BIND_AUTO_CREATE);

//                setClassLoadProxy(dexClassLoader, appClassLoader);
                Log.e("TTT", "btn2 onClick");
                Toast.makeText(MainActivity.this, new Test().TTT, Toast.LENGTH_SHORT).show();
            }
        });

        b2.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.e("TTT", "btn2 onLongClick");
                return false;
            }
        });


        RadioGroup mSwitchMapRadioGroup = (RadioGroup) findViewById(R.id.rg_switch_map);
        String provider = "amap";
        int index = 1;
        if (provider.equals("amap")) {
            index = 0;
        }
        ((RadioButton) mSwitchMapRadioGroup.getChildAt(index)).setChecked(true);
        mSwitchMapRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_map_amap) {
                    Log.i(TAG, " check amap");
                } else {
                    Log.i(TAG, " check tencent");
                }

            }
        });


        String QTAGUID_IFACE_STATS = "/proc/net/xt_qtaguid/iface_stat_fmt";
        String QTAGUID_UID_STATS = "/proc/net/xt_qtaguid/stats";

        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(QTAGUID_IFACE_STATS));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                Log.i("LJZ", line);
            }
            bufferedReader.close();

            bufferedReader = new BufferedReader(new FileReader(QTAGUID_UID_STATS));
            while ((line = bufferedReader.readLine()) != null) {
                Log.i("LJZZ", line);
            }
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

//        try {
//            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("/sdcard/test"));
//            test t = new test(10, 5, "ab", "cd");
//            objectOutputStream.writeObject(t);
//
//            Log.i("ZZZ", t.toString());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                try {
                    Thread.sleep(10 * 1000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    public static class test implements Serializable {
        int a;
        static int b;
        static String c;
        transient static String e;

        public test(int a, int b, String c, String e) {
            this.a = a;
            this.b = b;
            this.c = c;
            this.e = e;
        }

        @Override
        public String toString() {
            return "a:" + a + " b:" + b + " c:" + c + " e:" + e;
        }
    }

    public void setClassLoadProxy(ClassLoader oldLoader, ClassLoader newLoader) {
        try {
            Class clazz = Class.forName("java.lang.ClassLoader");
            Field parent = clazz.getDeclaredField("parent");
            parent.setAccessible(true);
            parent.set(newLoader, parentClassLoader);
            parent.set(oldLoader, newLoader);

            ClassLoader loder = oldLoader;
            while (loder != null) {
                Log.e(TAG, loder.toString());
                loder = loder.getParent();
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    @Override
    protected void onStart() {
        Log.e(TAG, "1 onStart" + " taskID:" + this.getTaskId() + " " + this.toString());
        super.onStart();

    }

    @Override
    protected void onResume() {
        Log.e(TAG, "1 onResume" + " taskID:" + this.getTaskId() + " " + this.toString());
        super.onResume();

    }


    @Override
    protected void onPause() {
        Log.e(TAG, "1 onPause" + " taskID:" + this.getTaskId() + " " + this.toString());
        super.onPause();

    }

    @Override
    protected void onStop() {
        Log.e(TAG, "1 onStop" + " taskID:" + this.getTaskId() + " " + this.toString());
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        Log.e(TAG, "1 onDestroy" + " taskID:" + this.getTaskId() + " " + this.toString());
        super.onDestroy();

    }

    @Override
    protected void onRestart() {
        Log.e(TAG, "1 onRestart" + " taskID:" + this.getTaskId() + " " + this.toString());
        super.onRestart();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.


        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_main2activity) {

            Log.e("MainActivity", "onClick");
            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            intent.setClass(this, Main2Activity.class);
            this.getApplicationContext().startActivity(intent);
            Log.e(TAG, "dd:" + AbsTest.a);
            Log.e(TAG, "ee:" + TestA.a);
            AbsTest.a = "456";
            Log.e(TAG, "ff:" + TestB.a);
//            Intent serviceIntent = new Intent(MainActivity.this, MyService.class);
//            startService(serviceIntent);
        } else if (id == R.id.action_surfaceview) {
            Intent intent = new Intent();
            intent.setClass(this, SurfaceViewActivity.class);
            startActivity(intent);
        } else if (id == R.id.action_glsurfaceview) {
            Intent intent = new Intent();
            intent.setClass(this, GLSurfaceViewActivity.class);
            startActivity(intent);
        } else if (id == R.id.action_soundgenerate) {
            Intent intent = new Intent();
            intent.setClass(this, SoundGenerateActivity.class);
            startActivity(intent);
        } else if (id == R.id.action_enerty) {
            Intent intent = new Intent();
            intent.setClass(this, EnergyActivity.class);
            startActivity(intent);
        }


        return super.onOptionsItemSelected(item);
    }


    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
//    public native long getTotalStat(int type);

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
//        Log.e(TAG, "1 dispatchTouchEvent");
        return super.dispatchTouchEvent(ev);
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
