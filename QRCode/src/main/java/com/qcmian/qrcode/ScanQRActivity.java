package com.qcmian.qrcode

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import com.google.android.cameraview.CameraView;
import com.google.zxing.Binarizer;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.EncodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.PlanarYUVLuminanceSource;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.qcmian.qrcode.R;

import java.util.HashMap;
import java.util.Map;

import static android.graphics.Bitmap.Config.ARGB_8888;

public class ScanQRActivity extends AppCompatActivity {
    CameraView cameraView;
    ImageView cover;
    Rect codeRect;
    ImageView test;
    EditText addr;
    boolean hasResume = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initActionBar("扫码链接");
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
                codeRect = new Rect(cover.getMeasuredWidth() / 2 - 320, cover.getMeasuredHeight() / 2 - 320 - 150, cover.getMeasuredWidth() / 2 + 320, cover.getMeasuredHeight() / 2 + 320 - 150);
                canvas.drawRect(codeRect, paint);
                cover.setImageBitmap(bitmap);
            }
        });
    }

    Runnable work = new Runnable() {
        @Override
        public void run() {
            if (cameraView != null && hasResume) {
                cameraView.takePicture();
            }
            ThreadExecutor.postMain(work, 500);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        hasResume = true;
        cameraView.start();
        cameraView.addCallback(new CameraView.Callback() {
            @Override
            public void onPictureTaken(CameraView cameraView, final byte[] data) {
                super.onPictureTaken(cameraView, data);
                try {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                    int width = bitmap.getHeight();
                    int height = bitmap.getWidth();
                    float bi = (float) cover.getHeight() / cover.getWidth();
                    float sc = (float) cover.getWidth() / width;
                    int w, h;
                    if (sc * height > cover.getHeight()) {
                        w = width;
                        h = (int) (width * bi);
                    } else {
                        w = (int) (height / bi);
                        h = height;
                    }
                    Matrix matrix = new Matrix();
                    matrix.postRotate(90);
                    float scaleWidth = ((float) codeRect.width() / 2) / ((float) codeRect.width() / cover.getWidth() * w);
                    matrix.postScale(scaleWidth, scaleWidth);

                    bitmap = Bitmap.createBitmap(bitmap,
                            (int) (((float) codeRect.top / cover.getHeight()) * h),
                            (int) (width - (((float) (codeRect.left + codeRect.width()) / cover.getWidth()) * w)),
                            (int) ((float) codeRect.width() / cover.getWidth() * w),
                            (int) ((float) codeRect.height() / cover.getHeight() * h)
                            , matrix, true);

                    final Bitmap finalBitmap = bitmap;
                    ThreadExecutor.postMain(new Runnable() {
                        @Override
                        public void run() {
                            test.setImageBitmap(finalBitmap);
                        }
                    });

                    MultiFormatReader multiFormatReader = new MultiFormatReader();

                    int[] pixels = new int[bitmap.getWidth() * bitmap.getHeight()];
                    bitmap.getPixels(pixels, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
                    LuminanceSource source = new RGBLuminanceSource(bitmap.getWidth(), bitmap.getHeight(), pixels);
                    Binarizer binarizer = new HybridBinarizer(source);
                    BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer);

                    Map hints = new HashMap();
                    hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

                    Result result = multiFormatReader.decode(binaryBitmap, hints);

                    ThreadExecutor.postMain(new Runnable() {
                        @Override
                        public void run() {
                            addr.setText(result.getText());
                        }
                    });

                } catch (NotFoundException e) {
                    e.printStackTrace();
                }
            }
        });

        ThreadExecutor.postMain(work, 700);

    }


    @Override
    protected void onPause() {
        super.onPause();
        hasResume = false;
        ThreadExecutor.removeCallbacksMain(work);
        cameraView.stop();

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
