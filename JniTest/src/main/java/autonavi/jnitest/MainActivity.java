package autonavi.jnitest;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
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

import java.io.File;
import java.lang.reflect.Field;

import dalvik.system.DexClassLoader;


public class MainActivity extends AppCompatActivity {
    static volatile int num = 0;
    public static String TAG = "MMMainActivity";
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE",
            "android.permission.ACCESS_COARSE_LOCATION"};
    ClassLoader parentClassLoader;
    ClassLoader appClassLoader;
    DexClassLoader dexClassLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(this,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                setClassLoadProxy(appClassLoader, dexClassLoader);

                Log.e("TTT", "btn1 onClick");
                Toast.makeText(MainActivity.this, new Test().TTT, Toast.LENGTH_SHORT).show();
            }
        });

        Button b2 = (Button) findViewById(R.id.button4);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setClassLoadProxy(dexClassLoader, appClassLoader);
                Log.e("TTT", "btn2 onClick");
                Toast.makeText(MainActivity.this, new Test().TTT, Toast.LENGTH_SHORT).show();
            }
        });


        RadioGroup mSwitchMapRadioGroup = (RadioGroup) findViewById(R.id.rg_switch_map);
        String provider = "amap";
        int index = 1;
        if(provider.equals("amap")){
            index = 0;
        }
        ((RadioButton)mSwitchMapRadioGroup.getChildAt(index)).setChecked(true);
        mSwitchMapRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.rb_map_amap){
                    Log.i(TAG, " check amap");
                }else {
                    Log.i(TAG, " check tencent");
                }

            }
        });
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
        Log.e("TTT", "activity " + event.actionToString(event.getAction()));
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
            this.getApplicationContext().startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
//    public native String stringFromJNI();

    // Used to load the 'native-lib' library on application startup.
//    static {
//        System.loadLibrary("native-lib");
//    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
//        Log.e(TAG, "1 dispatchTouchEvent");
        return super.dispatchTouchEvent(ev);
    }
}
