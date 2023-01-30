package com.example.pagdemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import org.libpag.PAGFile;
import org.libpag.PAGView;

public class MainActivity extends AppCompatActivity {
    String TAG = "MainActivityTAG";
    ConstraintLayout constraintLayout;
    PAGView pagView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        constraintLayout = findViewById(R.id.cl_test);
        pagView = findViewById(R.id.pv_test);
        findViewById(R.id.bt_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pagView.play();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
//                        finish(); // 这个也会崩溃，调用到 onVisibilityAggregated
                        ViewGroup group = (ViewGroup) constraintLayout.getParent();
                        if (group != null) {
                            group.removeView(constraintLayout);// 崩溃在 dispatchDetachedFromWindow
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
                Log.d(TAG, "onAnimationEnd", new Exception());
                constraintLayout.removeView(view);
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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }
}