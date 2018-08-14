package autonavi.jnitest;

import android.animation.ObjectAnimator;
import android.animation.StateListAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;


public class SurfaceViewActivity extends Activity {

    String TAG = MainActivity.TAG;
    Context mContext;
    MySurfaceView surfaceView;
    Button mButton;
    ImageView mImageView1;
    ImageView mImageView2;
    ImageView mImageView3;
    ImageView mImageView4;
    ImageView mImageView5;
    ImageView mImageView6;
    boolean isAnimateStart = false;

    public SurfaceViewActivity() {
        System.out.print("eee");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surfaceview);
        mContext = this;
        surfaceView = findViewById(R.id.surfaceview);
//        AnimationDrawable drawable = new AnimationDrawable();
//        int dur = 100;
//        BitmapDrawable bitmapDrawable1 = new BitmapDrawable(getResources(), BitmapFactory.decodeResource(getResources(), R.drawable.week1));
//        bitmapDrawable1.setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
//        drawable.addFrame(bitmapDrawable1, dur);
//        BitmapDrawable bitmapDrawable2 = new BitmapDrawable(getResources(), BitmapFactory.decodeResource(getResources(), R.drawable.week2));
//        bitmapDrawable2.setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
//        drawable.addFrame(bitmapDrawable2, dur);
//        BitmapDrawable bitmapDrawable3 = new BitmapDrawable(getResources(), BitmapFactory.decodeResource(getResources(), R.drawable.week3));
//        bitmapDrawable3.setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
//        drawable.addFrame(bitmapDrawable3, dur);
//        BitmapDrawable bitmapDrawable4 = new BitmapDrawable(getResources(), BitmapFactory.decodeResource(getResources(), R.drawable.week4));
//        bitmapDrawable4.setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
//        drawable.addFrame(bitmapDrawable4, dur);
//        BitmapDrawable bitmapDrawable5 = new BitmapDrawable(getResources(), BitmapFactory.decodeResource(getResources(), R.drawable.week5));
//        bitmapDrawable5.setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
//        drawable.addFrame(bitmapDrawable5, dur);
//        BitmapDrawable bitmapDrawable6 = new BitmapDrawable(getResources(), BitmapFactory.decodeResource(getResources(), R.drawable.week6));
//        bitmapDrawable6.setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
//        drawable.addFrame(bitmapDrawable6, dur);
//        BitmapDrawable bitmapDrawable7 = new BitmapDrawable(getResources(), BitmapFactory.decodeResource(getResources(), R.drawable.week7));
//        bitmapDrawable7.setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
//        drawable.addFrame(bitmapDrawable7, dur);
//        surfaceView.setBackground(drawable);
//        drawable.start();

        mImageView1 = findViewById(R.id.imgview1);
        final RotateAnimation rotateAnimation = new RotateAnimation(0, 360f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setInterpolator(new LinearInterpolator());
        rotateAnimation.setDuration(2000);
        rotateAnimation.setRepeatCount(-1);
        rotateAnimation.setFillAfter(true);
        rotateAnimation.setRepeatMode(Animation.RESTART);

        mImageView2 = findViewById(R.id.imgview2);
        final ScaleAnimation scaleAnimation1 = new ScaleAnimation(1f, 1.5f, 1f, 1.5f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation1.setInterpolator(new LinearInterpolator());
        scaleAnimation1.setDuration(2000);
        scaleAnimation1.setRepeatCount(-1);
        scaleAnimation1.setFillAfter(true);
        scaleAnimation1.setRepeatMode(Animation.REVERSE);
        final ScaleAnimation scaleAnimation2 = new ScaleAnimation(1.5f, 0.5f, 1.5f, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation2.setInterpolator(new LinearInterpolator());
        scaleAnimation2.setDuration(4000);
        scaleAnimation2.setRepeatCount(-1);
        scaleAnimation2.setFillAfter(true);
        scaleAnimation2.setRepeatMode(Animation.REVERSE);

        final ScaleAnimation scaleAnimation3 = new ScaleAnimation(0.5f, 1.5f, 0.5f, 1.5f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation3.setInterpolator(new LinearInterpolator());
        scaleAnimation3.setDuration(4000);
        scaleAnimation3.setRepeatCount(-1);
        scaleAnimation3.setFillAfter(true);
        scaleAnimation3.setRepeatMode(Animation.REVERSE);

        scaleAnimation1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                mImageView2.startAnimation(scaleAnimation2);

            }
        });

        scaleAnimation2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

                mImageView2.startAnimation(scaleAnimation3);
            }
        });
        scaleAnimation3.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

                mImageView2.startAnimation(scaleAnimation2);
            }
        });

        mImageView3 = findViewById(R.id.imgview3);
        final TranslateAnimation translateAnimation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 1,
                Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);
        translateAnimation.setInterpolator(new LinearInterpolator());
        translateAnimation.setDuration(2000);
        translateAnimation.setRepeatCount(-1);
        translateAnimation.setFillAfter(true);
        translateAnimation.setRepeatMode(Animation.REVERSE);

        mImageView4 = findViewById(R.id.imgview4);
        final ObjectAnimator rotateAnimator = ObjectAnimator.ofFloat(mImageView4, "rotation", 0f, 360f);
        rotateAnimator.setRepeatCount(ObjectAnimator.INFINITE);
        rotateAnimator.setStartDelay(0);
        rotateAnimator.setDuration(2000);
        rotateAnimator.setInterpolator(new LinearInterpolator());
        rotateAnimator.setRepeatMode(ObjectAnimator.RESTART);

        mImageView5 = findViewById(R.id.imgview5);
        final ObjectAnimator scaleAnimator = ObjectAnimator.ofFloat(mImageView5, "scaleX", 1f, 1.5f, 1f, 0.5f,1f);
        scaleAnimator.setRepeatCount(ObjectAnimator.INFINITE);
        scaleAnimator.setStartDelay(0);
        scaleAnimator.setDuration(8000);
        scaleAnimator.setInterpolator(new LinearInterpolator());
        scaleAnimator.setRepeatMode(ObjectAnimator.RESTART);
        mImageView5 = findViewById(R.id.imgview5);
        final ObjectAnimator scaleAnimator1 = ObjectAnimator.ofFloat(mImageView5, "scaleY", 1f, 1.5f, 1f, 0.5f,1f);
        scaleAnimator1.setRepeatCount(ObjectAnimator.INFINITE);
        scaleAnimator1.setStartDelay(0);
        scaleAnimator1.setDuration(8000);
        scaleAnimator1.setInterpolator(new LinearInterpolator());
        scaleAnimator1.setRepeatMode(ObjectAnimator.RESTART);

        mImageView6 = findViewById(R.id.imgview6);
        final ObjectAnimator translateAnimator = ObjectAnimator.ofFloat(mImageView6, "translationX", 0, mImageView6.getWidth(),0);
        translateAnimator.setRepeatCount(ObjectAnimator.INFINITE);
        translateAnimator.setStartDelay(0);
        translateAnimator.setDuration(2000);
        translateAnimator.setInterpolator(new LinearInterpolator());
        translateAnimator.setRepeatMode(ObjectAnimator.REVERSE);

        mButton = findViewById(R.id.btn);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Canvas canvas = surfaceView.mHolder.lockCanvas();
                Paint paint = new Paint();
                paint.setColor(Color.BLUE);
                canvas.drawRect(new Rect(10, 10, 100, 100), paint);
                surfaceView.mHolder.unlockCanvasAndPost(canvas);

                if (!isAnimateStart) {
                    mImageView1.startAnimation(rotateAnimation);
                    mImageView2.startAnimation(scaleAnimation1);
                    mImageView3.startAnimation(translateAnimation);
                    rotateAnimator.start();
                    scaleAnimator.start();
                    scaleAnimator1.start();
                    translateAnimator.setFloatValues(0,mImageView6.getWidth());
                    translateAnimator.start();
                    isAnimateStart = true;
                } else {
                    mImageView1.clearAnimation();
                    mImageView2.clearAnimation();
                    mImageView3.clearAnimation();
                    rotateAnimator.end();
                    scaleAnimator.end();
                    scaleAnimator1.end();
                    translateAnimator.reverse();
                    isAnimateStart = false;
                }
            }

        });


    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }


    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    protected void onRestart() {
        super.onRestart();

    }

}
