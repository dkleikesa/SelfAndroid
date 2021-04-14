package autonavi.jnitest;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.lang.reflect.Field;

import dalvik.system.DexClassLoader;

public class MyApplication extends Application {
    String TAG = "MyApplication";

    ClassLoader parentClassLoader;
    ClassLoader appClassLoader;
    DexClassLoader dexClassLoader;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        try {
            Log.e("lllLLL:","aaa");
//
            appClassLoader = MyApplication.class.getClassLoader();
            parentClassLoader = appClassLoader.getParent();
            File dexPath = new File("/sdcard/app-debug.apk");
            File tmpPath = new File(this.getFilesDir().getAbsolutePath(), "/testDexTmp/");
            dexClassLoader = new DexClassLoader(dexPath.getAbsolutePath(), tmpPath.getAbsolutePath(), tmpPath.getAbsolutePath(), parentClassLoader);

//            Class<Test> aClass = (Class<Test>) dexClassLoader.loadClass("autonavi.jnitest.Test");
//            Test ins = aClass.newInstance();
//            Toast.makeText(this, "abc" + ins.TTT, Toast.LENGTH_SHORT).show();
//
//            Log.e("lllLLL:","dddd");
//            Log.e("lllLLL:","abc" + ins.TTT);
//            setClassLoadProxy(appClassLoader, dexClassLoader);

            Toast.makeText(this, "dc" + new Test().TTT, Toast.LENGTH_SHORT).show();
            Log.e("lllLLL:","dc" + new Test().TTT);
        } catch (Exception e) {
            e.printStackTrace();
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

    public void reSetClassLoadProxy(ClassLoader oldLoader, ClassLoader newLoader) {
        try {
            Class clazz = Class.forName("java.lang.ClassLoader");
            Field parent = clazz.getDeclaredField("parent");
            parent.setAccessible(true);
            parent.set(newLoader, oldLoader.getParent());
            parent.set(oldLoader, null);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}
