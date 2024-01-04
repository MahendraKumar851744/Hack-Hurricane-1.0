package com.androidai.chatgpttextdetection;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

public class lay_loading extends AppCompatActivity {

    private TextView animatedTextView,loading;
    private String originalText;
    private String original = "Loading...";
    private int currentIndex = 0;
    private int currentIndex2 = 0;
    private Handler handler = new Handler(Looper.getMainLooper());
    private Handler handler2 = new Handler(Looper.getMainLooper());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lay_loading);
        Window window = getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        animatedTextView = findViewById(R.id.tv_loading);
        loading = findViewById(R.id.loading);
        originalText = "Real Text Classifier";
        animateText();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        if(Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) {
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if(Build.VERSION.SDK_INT >= 19) {
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decorView.setSystemUiVisibility(uiOptions);
        }
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
            }
        });
    }
    private void animateText() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (currentIndex <= originalText.length()) {
                    animatedTextView.setText(originalText.substring(0, currentIndex));
                    currentIndex++;
                    handler.postDelayed(this, 150);
                } else {
                    currentIndex2 = 0;
                    animateText2();
                }
            }
        }, 500);
    }

    private void animateText2() {
        handler2.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (currentIndex2 <= original.length()) {
                    loading.setText(original.substring(0, currentIndex2));
                    currentIndex2++;
                    handler2.postDelayed(this, 150);
                }else{
                    dismiss();
                }
            }
        }, 500);
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(null);
        handler2.removeCallbacks(null);
    }

    private void dismiss(){
        Intent i = new Intent(lay_loading.this,MainActivity.class);
        startActivity(i);
        finish();
    }



}