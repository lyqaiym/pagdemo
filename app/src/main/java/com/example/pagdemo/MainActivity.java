package com.example.pagdemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.animation.Animator;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.airbnb.lottie.LottieAnimationView;

import org.libpag.PAGFile;
import org.libpag.PAGView;

public class MainActivity extends AppCompatActivity {
    String TAG = "MainActivityTAG";
    ConstraintLayout constraintLayoutPag;
    ConstraintLayout constraintLayoutLottie;
    PAGView pagView;
    LottieAnimationView lottieAnimationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        constraintLayoutPag = findViewById(R.id.cl_test);
        constraintLayoutLottie = findViewById(R.id.cl_test2);
        pagView = findViewById(R.id.pv_test);
        lottieAnimationView = findViewById(R.id.lav_test);
        findViewById(R.id.bt_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pagView.play();
                lottieAnimationView.playAnimation();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
//                        finish(); // 这个也会崩溃，调用到 onVisibilityAggregated
                        ViewGroup group = (ViewGroup) constraintLayoutPag.getParent();
                        if (group != null) {
                            group.removeView(constraintLayoutPag);// 崩溃在 dispatchDetachedFromWindow
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    group.addView(constraintLayoutPag);
                                }
                            }, 200);
                        }
                        ViewGroup group2 = (ViewGroup) constraintLayoutLottie.getParent();
                        if (group2 != null) {
                            group2.removeView(constraintLayoutLottie);
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    group2.addView(constraintLayoutLottie);
                                }
                            }, 200);
                        }
                    }
                }, 200);
            }
        });
        PAGFile pagFile = PAGFile.Load(getAssets(), "particle_video.pag");
        pagView.setComposition(pagFile);
        pagView.addListener(new PAGView.PAGViewListener() {
            @Override
            public void onAnimationStart(PAGView view) {
                Log.d(TAG, "onAnimationStart");
            }

            @Override
            public void onAnimationEnd(PAGView view) {
                Log.d(TAG, "pag:onAnimationEnd", new Exception());
                constraintLayoutPag.removeView(view);
            }

            @Override
            public void onAnimationCancel(PAGView view) {
                Log.d(TAG, "onAnimationCancel");
            }

            @Override
            public void onAnimationRepeat(PAGView view) {
                Log.d(TAG, "onAnimationCancel");
            }
        });
//        LottieTask<LottieComposition> task = LottieCompositionFactory.fromAsset(this, "Lottie Logo 1.json");
        lottieAnimationView.setAnimation("Lottie Logo 1.json");
        lottieAnimationView.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                Log.d(TAG, "lottie:onAnimationStart");
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                Log.d(TAG, "lottie:onAnimationEnd", new Exception());
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }
}