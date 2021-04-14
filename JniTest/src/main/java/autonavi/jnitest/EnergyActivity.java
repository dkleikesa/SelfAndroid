package autonavi.jnitest;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.Choreographer;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.google.android.cameraview.CameraView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import autonavi.jnitest.render.TextureRender;
import biz.source_code.dsp.filter.FilterPassType;
import biz.source_code.dsp.filter.IirFilter;
import biz.source_code.dsp.filter.IirFilterCoefficients;
import biz.source_code.dsp.filter.IirFilterDesignExstrom;


public class EnergyActivity extends Activity {
    private static final String TAG = "EnergyActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_energy);
        initGLSurfaceView();
        initCamera();
        initPlaySound();
        initSurfaceView1();
        record();
        startNetwork();
        startLocation();


        for (int i = 0; i < 5; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        short[] buf = new short[dataLen1];
                        IirFilterCoefficients iirFilterCoefficients = IirFilterDesignExstrom.design(FilterPassType.bandpass, 3, 1700.0d / HZ, 2100.0d / HZ);
                        IirFilter iirFilter = new IirFilter(iirFilterCoefficients);
                        for (int i = 0; i < dataLen1; i++) {
                            buf[i] = (short) iirFilter.step(data1[i]);
                        }
                    }
                }
            }).start();
        }

    }


    private void startLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {

            @Override
            public void onLocationChanged(Location location) {

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        });

    }

    private void startNetwork() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                while (true) {
                    HttpURLConnection connection = null;
                    InputStream in = null;
                    try {
                        URL url = new URL("http://172.30.250.15/data");
                        connection = (HttpURLConnection) url.openConnection();
                        //设置请求方法
                        connection.setRequestMethod("GET");
                        //设置连接超时时间（毫秒）
                        connection.setConnectTimeout(5000);
                        //设置读取超时时间（毫秒）
                        connection.setReadTimeout(5000);

                        //返回输入流
                        in = connection.getInputStream();

                        //读取输入流
                        byte[] buf = new byte[10240];
                        int len;
                        int total = 0;
                        while ((len = in.read(buf)) != -1) {
                            total += len;
                        }
                        Log.d(TAG, "file size:" + total / 1024 / 1024.0);

                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (ProtocolException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (in != null) {
                            try {
                                in.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        if (connection != null) {//关闭连接
                            connection.disconnect();
                        }
                    }
                }
            }
        }).start();
    }


    CameraView cameraView;

    private void initCamera() {
        cameraView = findViewById(R.id.camera);
    }

    private void initGLSurfaceView() {
        GLSurfaceView glSurfaceView = findViewById(R.id.glsurfaceview);
        glSurfaceView.setEGLContextClientVersion(3);
        glSurfaceView.setRenderer(new TextureRender(BitmapFactory.decodeResource(getResources(), R.drawable.week1)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        cameraView.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        cameraView.stop();
    }


    int HZ = 48000;
    int duration = 1;
    short[] data = new short[HZ * duration];
    short[] data1 = new short[HZ * duration];
    int dataLen1 = HZ * duration;
    int dataLen;
    double rate = 30000;

    private void initPlaySound() {
        initSurfaceView();
        Choreographer.getInstance().postFrameCallback(frameCallback);
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    dataLen = sin(data, HZ, duration, 0b10101010);
                    play();
                    try {
                        Thread.sleep(duration * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

    }

    public static final int HEIGHT = Short.MAX_VALUE;

    public int sin(short[] wave, int hz, float duration, int content) {
        int len = (int) (duration * hz); // 一共多少数据
        int size = len / 8;
        int index = 0;

        for (int i = 0; i < 8; i++) {
            int b = content & 0x01;
            if (b == 1) {
                double rate = 1800.0d;
                double radio = 2.0 * Math.PI / hz * rate;
                double perWave = hz / rate;
                for (int j = 0; j < size; j++) {
                    wave[index] = (short) (HEIGHT * Math.sin(radio * (index % perWave)));
                    index++;
                }
            } else {
                double rate = 1900;
                double radio = 2.0 * Math.PI / hz * rate;
                double perWave = hz / rate;
                for (int j = 0; j < size; j++) {
                    wave[index] = (short) (HEIGHT * Math.sin(radio * (index % perWave)));
                    index++;
                }
            }
            content = content >> 1;
        }
        return len;
    }


    AudioRecord audioRecord;
    int bufferSize;

    private void record() {
        if (audioRecord == null) {
            bufferSize = HZ * duration;
            audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC,
                    HZ,
                    AudioFormat.CHANNEL_IN_MONO,
                    AudioFormat.ENCODING_PCM_16BIT, bufferSize);
            audioRecord.startRecording();
            data1 = new short[bufferSize];
        }
        new Thread(new Runnable() {
            @Override
            public void run() {

                while (true) {
                    dataLen1 = audioRecord.read(data1, 0, data1.length);
//                    filter();
                    write();
                }
            }
        }).start();

    }


    IirFilter iirFilter;

    void filter() {
        if (iirFilter == null) {
//            IirFilterCoefficients iirFilterCoefficients = IirFilterDesignExstrom.design(FilterPassType.bandpass, 10, ((double) (rate - 100)) / hz, ((double) (rate + 100)) / hz);
            IirFilterCoefficients iirFilterCoefficients = IirFilterDesignExstrom.design(FilterPassType.bandpass, 3, 1700.0d / HZ, 210.0d / HZ);
            iirFilter = new IirFilter(iirFilterCoefficients);
        }
        for (int i = 0; i < dataLen1; i++) {
            data1[i] = (short) iirFilter.step(data1[i]);
        }
    }

    void write() {
        try {
            File f = new File("/sdcard/ivi/sound.txt");
            if (f.exists()) {
                f.delete();
            }
            f.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(f);
            for (int i = 0; i < dataLen; i++) {
                fileOutputStream.write(String.valueOf(data1[i]).getBytes());
                fileOutputStream.write('\n');
            }
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    AudioTrack audioTrack;

    private void play() {
        if (audioTrack == null) {
            audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, HZ,
                    AudioFormat.CHANNEL_OUT_MONO,  //CHANNEL_OUT_STEREO
                    AudioFormat.ENCODING_PCM_16BIT,
                    dataLen,
                    AudioTrack.MODE_STREAM);
            audioTrack.setStereoVolume(1.0f, 1.0f);
            audioTrack.play();
        }
        Log.e(TAG, "play start");
        audioTrack.write(data, 0, dataLen);
    }


    public int sin1(short[] wave, int hz, float duration, int rate) {
        int len = (int) (duration * hz); // 一共多少数据
        double radio = 2.0 * Math.PI / hz * rate;
        double perWave = hz / rate;
        for (int i = 0; i < len; i++) {
            wave[i] = (short) (HEIGHT * Math.sin(radio * (i % perWave)));
        }
        return len;
    }


    SurfaceView surfaceView;
    private Path mPath;
    private Paint mPaint;
    private boolean isDrawing = false;
    int w, h;
    SurfaceHolder surfaceHolder;

    private void initSurfaceView() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(5);
        mPaint.setColor(Color.GREEN);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPath = new Path();

        surfaceView = findViewById(R.id.send);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                isDrawing = true;
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                w = width;
                h = height;
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                isDrawing = false;
            }
        });
    }

    SurfaceView surfaceView1;
    private Path mPath1;
    private Paint mPaint1;
    private boolean isDrawing1 = false;
    int w1, h1;
    SurfaceHolder surfaceHolder1;

    private void initSurfaceView1() {
        mPaint1 = new Paint();
        mPaint1.setAntiAlias(true);
        mPaint1.setStyle(Paint.Style.STROKE);
        mPaint1.setStrokeWidth(5);
        mPaint1.setColor(Color.BLUE);
        mPaint1.setStrokeJoin(Paint.Join.ROUND);
        mPath1 = new Path();

        surfaceView1 = findViewById(R.id.receive);
        surfaceHolder1 = surfaceView1.getHolder();
        surfaceHolder1.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                isDrawing1 = true;

            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                w1 = width;
                h1 = height;
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                isDrawing1 = false;
            }
        });
    }

    Choreographer.FrameCallback frameCallback = new Choreographer.FrameCallback() {
        @Override
        public void doFrame(long frameTimeNanos) {
            if (isDrawing) {
                if (w == 0 || h == 0) {
                    return;
                }
                mPath.reset();
                short max = Short.MIN_VALUE;
                short min = Short.MAX_VALUE;
                for (int i = 0; i < dataLen; i++) {
                    if (data[i] > max) {
                        max = data[i];
                    }
                    if (data[i] < min) {
                        min = data[i];
                    }
                }

//                Short max = Short.MAX_VALUE, min = Short.MIN_VALUE;
                boolean first = true;
                if (dataLen > 0) {
                    int size = dataLen;
                    float yRadio = ((float) h) / (max - min);
                    float xRadio = ((float) size) / w;
                    float offset = h / 2;
                    for (int i = 0; i < w; i++) {
                        float d = data[(int) (i * xRadio)];
                        float y = d * yRadio + offset;
                        if (first) {
                            mPath.moveTo(i, y);
                            first = false;
                            continue;
                        }

                        mPath.lineTo(i, y);
                    }
                }
                draw(surfaceHolder, mPaint, mPath);
            }

            if (isDrawing1) {
                if (w1 == 0 || h1 == 0) {
                    return;
                }
                mPath1.reset();

                Short max = Short.MIN_VALUE, min = Short.MAX_VALUE;
                for (int i = 0; i < dataLen1; i++) {
                    if (data[i] > max) {
                        max = data1[i];
                    }
                    if (data[i] < min) {
                        min = data1[i];
                    }
                }

                boolean first = true;
                if (dataLen > 0) {
                    int size = dataLen1;
                    float yRadio = ((float) h1) / (max - min);
                    float xRadio = ((float) size) / w1;
                    float offset = h1 / 2;
                    for (int i = 0; i < w1; i++) {
                        float d = data1[(int) (i * xRadio)];
                        float y = d * yRadio + offset;
                        if (first) {
                            mPath1.moveTo(i, y);
                            first = false;
                            continue;
                        }

                        mPath1.lineTo(i, y);
                    }
                }
                draw(surfaceHolder1, mPaint1, mPath1);
            }

            Choreographer.getInstance().postFrameCallback(frameCallback);
        }
    };

    private void draw(SurfaceHolder surfaceHolder, Paint paint, Path path) {
        Canvas canvas = null;
        //给画布加锁，防止线程安全，防止该内存区域被其他线程公用
        canvas = surfaceHolder.lockCanvas();
        if (null != canvas) {
            //清屏操作或者设置背景
            canvas.drawColor(Color.BLACK);
            canvas.drawPath(path, paint);
            //提交显示视图并解锁，防止长期占用此内存
            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

}
