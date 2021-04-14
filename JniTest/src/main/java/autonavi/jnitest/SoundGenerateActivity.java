package autonavi.jnitest;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.view.Choreographer;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import biz.source_code.dsp.filter.FilterPassType;
import biz.source_code.dsp.filter.IirFilter;
import biz.source_code.dsp.filter.IirFilterCoefficients;
import biz.source_code.dsp.filter.IirFilterDesignExstrom;


public class SoundGenerateActivity extends Activity {
    private static final String TAG = "SoundGenerateActivity";

    int hz = 48000;
    int duration = 1;
    short[] data = new short[hz * duration];
    short[] data1 = new short[hz * duration];
    int dataLen;
    double rate = 30000;

    boolean bb = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound);
        initSurfaceView();
        initSurfaceView1();


        findViewById(R.id.btn_generate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bb) {
                    dataLen = sin(data, hz, duration, 0b10101010);

                    Choreographer.getInstance().postFrameCallback(frameCallback);
                    play();
                    bb = false;
                } else {
                    bb = true;
                    dataLen = 0;
                    Choreographer.getInstance().postFrameCallback(frameCallback);

                }
            }
        });

        findViewById(R.id.btn_receive).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRecord) {
                    isRecord = false;
                } else {
                    isRecord = true;
                    record();
                }
            }
        });


    }

    AudioRecord audioRecord;
    boolean isRecord = false;
    int bufferSize;

    private void record() {
        if (audioRecord == null) {
//            bufferSize = AudioRecord.getMinBufferSize(hz, AudioFormat.CHANNEL_IN_MONO,
//                    AudioFormat.ENCODING_PCM_8BIT);
            bufferSize = hz * duration;
            audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC,
                    hz,
                    AudioFormat.CHANNEL_IN_MONO,
                    AudioFormat.ENCODING_PCM_16BIT, bufferSize);
            audioRecord.startRecording();
            data = new short[bufferSize];
        }

        new Thread(new Runnable() {
            @Override
            public void run() {

                while (isRecord) {
                    dataLen = audioRecord.read(data, 0, data.length);
                    filter();
                    write();

                    audioTrack.write(data1, 0, dataLen);

                    if (AudioRecord.ERROR_INVALID_OPERATION != dataLen) {
                        surfaceView.post(new Runnable() {
                            @Override
                            public void run() {
                                Choreographer.getInstance().postFrameCallback(frameCallback);
                            }
                        });

                    }
                }
            }
        }).start();

    }


    IirFilter iirFilter;

    void filter() {
        if (iirFilter == null) {
//            IirFilterCoefficients iirFilterCoefficients = IirFilterDesignExstrom.design(FilterPassType.bandpass, 10, ((double) (rate - 100)) / hz, ((double) (rate + 100)) / hz);
            IirFilterCoefficients iirFilterCoefficients = IirFilterDesignExstrom.design(FilterPassType.bandpass, 3, 17000.0d / hz, 21000.0d / hz);
            iirFilter = new IirFilter(iirFilterCoefficients);
        }
        for (int i = 0; i < dataLen; i++) {
            data1[i] = (short) iirFilter.step(data[i]);
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
                fileOutputStream.write(String.valueOf(data[i]).getBytes());
                fileOutputStream.write('\n');
            }
//            fileOutputStream.write(data);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    AudioTrack audioTrack;

    private void play() {
        if (audioTrack == null) {
            audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, hz,
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


    public static final int HEIGHT = Short.MAX_VALUE;

    /**
     * @param wave     数据缓存
     * @param hz       采样率
     * @param duration 生成的数据时长（秒）
     * @param rate     声音的频率
     * @return
     */
    public int sin(short[] wave, int hz, float duration, int content) {
        int len = (int) (duration * hz); // 一共多少数据
        int size = len / 8;
        int index = 0;

        for (int i = 0; i < 8; i++) {
            int b = content & 0x01;
            if (b == 1) {
                double rate = 18000.0d;
                double radio = 2.0 * Math.PI / hz * rate;
                double perWave = hz / rate;
                for (int j = 0; j < size; j++) {
                    data[index] = (short) (HEIGHT * Math.sin(radio * (index % perWave)));
                    index++;
                }
            } else {
                rate = 19000;
                double radio = 2.0 * Math.PI / hz * rate;
                double perWave = hz / rate;
                for (int j = 0; j < size; j++) {
                    data[index] = (short) (HEIGHT * Math.sin(radio * (index % perWave)));
                    index++;
                }
            }
            content = content >> 1;
        }


//        double perWave = hz / rate;
//        for (int i = 0; i < len; i++) {
//            wave[i] = (short) (HEIGHT * Math.sin(radio * (i % perWave)));
//        }
        return len;
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

    public int modulate(int content) {
        int size = dataLen / 8;
        int index = 0;
        short offset = Short.MAX_VALUE / 2;
        for (int i = 0; i < 8; i++) {
            int b = content & 0x01;
            if (b == 1) {
                for (int j = 0; j < size; j++) {
//                    data[index] = (short) (data[index] / 2 + offset);
                    index++;
                }

            } else {
                for (int j = 0; j < size; j++) {
                    data[index] = (short) (data[index] / 3);
                    index++;
                }
            }

            content = content >> 1;
        }
        return 0;
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

        surfaceView = findViewById(R.id.surfaceview);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                isDrawing = true;
                w = surfaceView.getWidth();
                h = surfaceView.getHeight();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

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

        surfaceView1 = findViewById(R.id.surfaceview1);
        surfaceHolder1 = surfaceView1.getHolder();
        surfaceHolder1.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                isDrawing1 = true;
                w1 = surfaceView.getWidth();
                h1 = surfaceView.getHeight();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                isDrawing = false;
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

                Short max = Byte.MAX_VALUE, min = Byte.MIN_VALUE;
                boolean first = true;
                if (dataLen > 0) {
                    int size = dataLen;
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
