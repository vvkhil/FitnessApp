package com.example.fitnessappjava.Training;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fitnessappjava.R;
import com.example.fitnessappjava.SignInUpProfile.MainActivity;

public class SplashScreen extends AppCompatActivity {


    private ImageView imageViewAppSplash;
    private TextView textViewAppName;
    private Animation up, down;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        imageViewAppSplash = findViewById(R.id.appSplash);
        up = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.up);
        imageViewAppSplash.setAnimation(up);

        textViewAppName = findViewById(R.id.appName);
        down = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.down);
        textViewAppName.setAnimation(down);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();

            }
        }, 3500);

    }
}