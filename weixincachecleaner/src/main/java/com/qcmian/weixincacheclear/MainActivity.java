package com.qcmian.weixincacheclear;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class MainActivity extends AppCompatActivity {


    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    // We want at least 2 threads and at most 4 threads in the core pool,
    // preferring to have 1 less than the CPU count to avoid saturating
    // the CPU with background work
    private static final int CORE_POOL_SIZE = CPU_COUNT * 2;
    private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2;
    private static final int KEEP_ALIVE_SECONDS = 30;

    private static final ThreadFactory sThreadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);

        public Thread newThread(Runnable r) {
            return new Thread(r, "WeixinCache #" + mCount.getAndIncrement());
        }
    };

    private static final BlockingQueue<Runnable> sPoolWorkQueue =
            new LinkedBlockingQueue<Runnable>(256);

    /**
     * An {@link Executor} that can be used to execute tasks in parallel.
     */
    public static final Executor THREAD_POOL_EXECUTOR;

    static {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_SECONDS, TimeUnit.SECONDS,
                sPoolWorkQueue, sThreadFactory);
        threadPoolExecutor.allowCoreThreadTimeOut(true);
        THREAD_POOL_EXECUTOR = threadPoolExecutor;
    }


    RecyclerView mRecyclerView;
    TextView textView;
    MyAdapter myAdapter = new MyAdapter();

    List<ItemBean> mList = new CopyOnWriteArrayList<>();
    Map<String, List<ItemBean>> mMd5Map = new ConcurrentHashMap<>();

    List<ItemBean> mNullList = new CopyOnWriteArrayList<>();


    List<String> mSelectList = new ArrayList<>();


    private final AtomicInteger mTaskCount = new AtomicInteger(0);
    private final AtomicInteger mFileCount = new AtomicInteger(0);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.textView);
        findViewById(R.id.scan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startScan(v.getContext());
                startCheckLoadingFile();
            }
        });
        findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int size = 0;
                for (String md5 : mSelectList) {
                    List<ItemBean> list = mMd5Map.get(md5);
                    if (list == null) {
                        continue;
                    }
                    for (ItemBean bean : list) {
                        new File(bean.filePath).delete();
                        mList.remove(bean);
                    }
                    mMd5Map.remove(md5);
                    size += list.size();
                }


                textView.setText("不重复图片:" + mMd5Map.size());
                List<ItemBean> list = new ArrayList<>(mMd5Map.size());
                for (Map.Entry<String, List<ItemBean>> entry : mMd5Map.entrySet()) {
                    ItemBean bean = entry.getValue().get(0);
                    bean.num = entry.getValue().size();
                    list.add(bean);
                }
                Collections.sort(list, new Comparator<ItemBean>() {
                    @Override
                    public int compare(ItemBean o1, ItemBean o2) {
                        return Integer.compare(o2.num, o1.num);
                    }
                });
                myAdapter.updateData(list, true);

                Toast.makeText(v.getContext(), "共删除文件：" + size, Toast.LENGTH_SHORT).show();
                textView.setText("共删除文件：" + size);
                mSelectList.clear();
            }
        });

        findViewById(R.id.delete_null).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mNullList != null && mNullList.size() > 0) {
                    int i = 0;
                    for (ItemBean bean : mNullList) {
                        new File(bean.filePath).delete();
                        mNullList.remove(bean);
                        i++;
                    }
                    Toast.makeText(v.getContext(), "共删除空文件：" + i, Toast.LENGTH_SHORT).show();
                    textView.setText("共删除空文件：" + i);
                }
            }
        });
        findViewById(R.id.md5_sort).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText("不重复图片:" + mMd5Map.size());
                List<ItemBean> list = new ArrayList<>(mMd5Map.size());
                for (Map.Entry<String, List<ItemBean>> entry : mMd5Map.entrySet()) {
                    ItemBean bean = entry.getValue().get(0);
                    bean.num = entry.getValue().size();
                    list.add(bean);
                }
                Collections.sort(list, new Comparator<ItemBean>() {
                    @Override
                    public int compare(ItemBean o1, ItemBean o2) {
                        return Integer.compare(o2.num, o1.num);
                    }
                });
                myAdapter.updateData(list, true);
            }
        });
        findViewById(R.id.origin_sort).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText("共发现文件" + mList.size());
                myAdapter.updateData(mList, false);
            }
        });


        myAdapter.onItemClickListener = new MyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, String position) {
                textView.setText("共发现文件" + mMd5Map.get(position).size());
                myAdapter.updateData(mMd5Map.get(position), false);
            }

            @Override
            public void onCheckedChanged(View view, boolean isChecked, String md5) {
                Toast.makeText(view.getContext(), "选择包含文件:" + mMd5Map.get(md5).size(), Toast.LENGTH_SHORT).show();
                if (isChecked) {
                    if (!mSelectList.contains(md5)) {
                        mSelectList.add(md5);
                    }
                } else {
                    if (mSelectList.contains(md5)) {
                        mSelectList.remove(md5);
                    }
                }
            }
        };
        mRecyclerView = findViewById(R.id.container);
        mRecyclerView.setAdapter(myAdapter);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        checkPermission(this);

    }

    void startCheckLoadingFile() {
        mRecyclerView.postDelayed(checkFileRunnable, 100);
    }

    Runnable checkFileRunnable = new Runnable() {
        @Override
        public void run() {
            textView.setText("共发现文件" + mList.size());
            if ((mTaskCount.get() > 0)) {
                mRecyclerView.postDelayed(this, 100);
            } else {
                startFillMD5();
                startCheckLoadingMD5();
            }
        }
    };

    void startCheckLoadingMD5() {
        mRecyclerView.postDelayed(checkMd5Runnable, 100);
    }

    Runnable checkMd5Runnable = new Runnable() {
        @Override
        public void run() {
            textView.setText("正在加载md5：" + mFileCount.get() + " NULL文件个数:" + mNullList.size());
            if ((mTaskCount.get() > 0)) {
                mRecyclerView.postDelayed(this, 100);
            } else {
                myAdapter.updateData(mList, false);
            }
        }
    };


    List<ItemBean> startScan(Context context) {
        File path = null;
        File path2 = new File("/sdcard/tencent/MicroMsg");
        File path1 = new File("/sdcard/tencent/micromsg");
        int length = 32;
        if ((!path2.exists()) || (!path2.isDirectory())) {
            Toast.makeText(context, "文件路径不存在(/sdcard/tencent/MicroMsg)", Toast.LENGTH_SHORT).show();
        } else {
            path = path2;
        }

        if ((!path1.exists()) || (!path1.isDirectory())) {
            Toast.makeText(context, "文件路径不存在(/sdcard/tencent/micromsg)", Toast.LENGTH_SHORT).show();
        } else {
            path = path1;
        }
        if (path == null) {
            return null;
        }

        List<File> pathList = new ArrayList<>();

        File[] files = path.listFiles();
        if (files == null || files.length <= 0) {
            Toast.makeText(context, "文件夹为空(/sdcard/tencent/micromsg)", Toast.LENGTH_SHORT).show();
            return null;
        }
        for (File f : files) {
            if (f.getName().length() >= length) {
                pathList.add(f);
            }
        }
        if (pathList.size() <= 0) {
            Toast.makeText(context, "未找到缓存路径(/sdcard/tencent/micromsg)", Toast.LENGTH_SHORT).show();
            return null;
        }

        mTaskCount.set(0);
        mMd5Map.clear();
        mList.clear();
        mNullList.clear();
        for (File f : pathList) {
            f = new File(f, "image2");
            if (!f.isDirectory()) {
                continue;
            }
            File[] files1 = f.listFiles();
            for (File f1 : files1) {
                if (!f1.isDirectory()) {
                    continue;
                }
                int val1 = -1;
                try {
                    val1 = Integer.valueOf(f1.getName(), 16);
                } catch (Exception e) {
                    val1 = -1;
                }
                if (val1 >= 0 && val1 <= 0xff) {
                    mTaskCount.incrementAndGet();
                    THREAD_POOL_EXECUTOR.execute(new MyRunnable(f1));

                }

            }
        }
        return mList;
    }

    public class MyRunnable implements Runnable {
        File mF;

        public MyRunnable(File f) {
            mF = f;
        }

        @Override
        public void run() {
            File[] files2 = mF.listFiles();
            for (File f2 : files2) {
                if (!f2.isDirectory()) {
                    continue;
                }

                int val2 = -1;
                try {
                    val2 = Integer.valueOf(f2.getName(), 16);
                } catch (Exception e) {
                    val2 = -1;
                }
                if (val2 >= 0 && val2 <= 0xff) {
                    File[] files3 = f2.listFiles();
                    for (File f3 : files3) {
                        if (f3.isDirectory() || f3.getName().equals(".nomedia")) {
                            continue;
                        }
                        ItemBean bean = new ItemBean();
                        bean.filePath = f3.getAbsolutePath();
//                        bean.size = f3.length();
//                        bean.time = f3.lastModified();
//                        bean.md5 = bytesToHexString(getFileMD5(f3));
                        mList.add(bean);
//                        if (!mMd5Map.containsKey(bean.md5)) {
//                            List<ItemBean> t = new ArrayList<>();
//                            t.add(bean);
//                            mMd5Map.put(bean.md5, t);
//                        } else {
//                            List<ItemBean> t = mMd5Map.get(bean.md5);
//                            t.add(bean);
//                        }
                    }
                }

            }
            mTaskCount.decrementAndGet();
        }
    }

    void startFillMD5() {
        if (mList.size() <= 0) {
            return;
        }
        mTaskCount.set(0);
        mFileCount.set(0);
        mNullList.clear();
        if (mList.size() <= MAXIMUM_POOL_SIZE) {
            mTaskCount.incrementAndGet();
            THREAD_POOL_EXECUTOR.execute(new Md5Runnable(0, mList.size()));
            return;
        }

        int per = mList.size() / MAXIMUM_POOL_SIZE;
        for (int i = 0; i < MAXIMUM_POOL_SIZE; i++) {
            mTaskCount.incrementAndGet();
            if (i != (MAXIMUM_POOL_SIZE - 1)) {
                THREAD_POOL_EXECUTOR.execute(new Md5Runnable(i * per, per));
            } else {
                THREAD_POOL_EXECUTOR.execute(new Md5Runnable(i * per, mList.size() - i * per));
            }
        }

    }

    public class Md5Runnable implements Runnable {
        int start;
        int len;

        public Md5Runnable(int start, int len) {
            this.start = start;
            this.len = len;
        }

        @Override
        public void run() {
            for (int i = 0; i < len; i++) {
                ItemBean bean = mList.get(start + i);
                bean.md5 = bytesToHexString(getFileMD5(new File(bean.filePath)));
                if (TextUtils.isEmpty(bean.md5)) {
                    mNullList.add(bean);
                } else if (!mMd5Map.containsKey(bean.md5)) {
                    List<ItemBean> t = new ArrayList<>();
                    t.add(bean);
                    mMd5Map.put(bean.md5, t);
                } else {
                    List<ItemBean> t = mMd5Map.get(bean.md5);
                    t.add(bean);
                }
                mFileCount.incrementAndGet();
            }
            mTaskCount.decrementAndGet();
        }
    }


    public static byte[] getFileMD5(File file) {
        if (!file.isFile() || file.length() <= 0) {
            return null;
        }
        MessageDigest digest = null;
        FileInputStream in = null;
        byte buffer[] = new byte[1024];
        int len;
        try {
            digest = MessageDigest.getInstance("MD5");
            in = new FileInputStream(file);
            while ((len = in.read(buffer, 0, 1024)) != -1) {
                digest.update(buffer, 0, len);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return digest.digest();
    }

    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }


    private void checkPermission(Activity activity) {
        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.ACCESS_WIFI_STATE
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
