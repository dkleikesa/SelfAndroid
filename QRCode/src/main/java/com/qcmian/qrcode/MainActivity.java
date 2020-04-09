package com.meituan.android.mrnmap.test;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.react.bridge.UiThreadUtil;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.meituan.android.mrnmap.test.zxing.camera.CameraManager;
import com.meituan.android.mrnmap.test.zxing.decoding.CaptureActivityHandler;
import com.meituan.android.mrnmap.test.zxing.decoding.InactivityTimer;
import com.meituan.qcs.android.lbs.debugkit.utils.ThreadExecutor;

import java.io.IOException;
import java.util.Vector;

import static android.graphics.Bitmap.Config.ARGB_8888;

public class ScanQRActivity extends AppCompatActivity implements SurfaceHolder.Callback {
    SurfaceView cameraView;
    ImageView cover;
    Rect codeRect;
    ImageView test;
    EditText addr;
    boolean hasResume = false;

    private InactivityTimer inactivityTimer;
    private boolean hasSurface;
    private CaptureActivityHandler handler;
    private Vector<BarcodeFormat> decodeFormats;
    private String characterSet;
    private boolean vibrate;
    public static final String INTENT_EXTRA_KEY_QR_SCAN = "result";
    private int needResult = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qrcode);
        initActionBar("扫码链接");
        CameraManager.init(getApplication());
        hasSurface = false;
        inactivityTimer = new InactivityTimer(this);
        cameraView = findViewById(R.id.camera);
        test = findViewById(R.id.test);
        cover = findViewById(R.id.cover);
        addr = findViewById(R.id.addr);
        cover.post(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = Bitmap.createBitmap(cover.getMeasuredWidth(), cover.getMeasuredHeight(), ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                Paint paint = new Paint();
                paint.setColor(Color.argb(150, 0, 0, 0));
                canvas.drawRect(0, 0, cover.getMeasuredWidth(), cover.getMeasuredHeight(), paint);
                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
                paint.setFlags(Paint.ANTI_ALIAS_FLAG);
                paint.setColor(Color.TRANSPARENT);
                canvas.drawRect(CameraManager.get().getFramingRect(), paint);
                cover.setImageBitmap(bitmap);
            }
        });
        findViewById(R.id.go).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), addr.getText().toString(), Toast.LENGTH_SHORT).show();
                String str = addr.getText().toString();
                if (TextUtils.isEmpty(str)) {
                    return;
                }

                final String finalParameter = parseResult();
                ThreadExecutor.postThreadPool(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            MRNUtils.jumpByUrl(ScanQRActivity.this, finalParameter, null);
                        } catch (IOException e) {
                            e.printStackTrace();
                            UiThreadUtil.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(ScanQRActivity.this, "网络错误，请确定在同一网段", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

        Intent intent = getIntent();
        if (intent != null) {
            needResult = intent.getIntExtra("needResult", 0);
        }
    }

    private String parseResult() {
        String str = addr.getText().toString();
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        String parameter = str;
        if (str.startsWith("http")) {
            Uri uri = Uri.parse(str);
            parameter = uri.getQueryParameter("h");
        }
        return parameter;

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null) {
            needResult = intent.getIntExtra("needResult", 0);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        SurfaceHolder surfaceHolder = cameraView.getHolder();
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        decodeFormats = null;
        characterSet = null;

        vibrate = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        CameraManager.get().closeDriver();
    }

    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        super.onDestroy();
    }

    /**
     * Handler scan result
     *
     * @param result
     * @param barcode
     */
    public void handleDecode(Result result, Bitmap barcode) {
        inactivityTimer.onActivity();
        playBeepSoundAndVibrate();
        String resultString = result.getText();
        //FIXME
        if (TextUtils.isEmpty(resultString)) {
            Toast.makeText(ScanQRActivity.this, "Scan failed!", Toast.LENGTH_SHORT).show();
        } else {
            addr.setText(resultString);
            if (needResult == 1) {
                Intent intent = new Intent();
                intent.putExtra("result_url", parseResult());
                setResult(Activity.RESULT_OK, intent);
                ScanQRActivity.this.finish();

            }
        }
        test.setImageBitmap(barcode);
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder, cover);
        } catch (IOException ioe) {
            return;
        } catch (RuntimeException e) {
            return;
        }
        if (handler == null) {
            handler = new CaptureActivityHandler(this, decodeFormats,
                    characterSet);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;

    }

    public Handler getHandler() {
        return handler;
    }


    private static final long VIBRATE_DURATION = 200L;

    private void playBeepSoundAndVibrate() {
        if (vibrate) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }


    protected void initActionBar(String title) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setCustomView(R.layout.actionbar);//设置自定义的布局：actionbar_custom
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); //Enable自定义的View
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setDisplayShowHomeEnabled(false);
            actionBar.setDisplayShowTitleEnabled(false);

            //title
            if (!TextUtils.isEmpty(title)) {
                TextView tv = actionBar.getCustomView().findViewById(R.id.tv_action_bar_title);
                tv.setText(title);
            }
            ImageView back = actionBar.getCustomView().findViewById(R.id.back);
            back.setVisibility(View.VISIBLE);
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ScanQRActivity.this.finish();
                }
            });
        }
    }
}
