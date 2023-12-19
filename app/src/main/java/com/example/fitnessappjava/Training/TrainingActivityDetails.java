package com.example.fitnessappjava.Training;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.fitnessappjava.R;

public class TrainingActivityDetails extends AppCompatActivity {

    String buttonValue;
    Button startBtn;
    private CountDownTimer countDownTimer;
    TextView mTextView;
    private boolean mTimeRunning;
    private long mTimeLeftInMills;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training_details);

        Intent intent = getIntent();
        buttonValue = intent.getStringExtra("value");

        int intValue = Integer.valueOf(buttonValue);

        switch (intValue) {

            case 1:
                setContentView(R.layout.activity_bow);
                break;
            case 2:
                setContentView(R.layout.activity_bridge);
                break;
            case 3:
                setContentView(R.layout.activity_chair);
                break;
            case 4:
                setContentView(R.layout.activity_child);
                break;
            case 5:
                setContentView(R.layout.activity_cobbler);
                break;
            case 6:
                setContentView(R.layout.activity_cow);
                break;
            case 7:
                setContentView(R.layout.activity_playji);
                break;
            case 8:
                setContentView(R.layout.activity_pauseji);
                break;
            case 9:
                setContentView(R.layout.activity_plank);
                break;
            case 10:
                setContentView(R.layout.activity_crunches);
                break;
            case 11:
                setContentView(R.layout.activity_situp);
                break;
            case 12:
                setContentView(R.layout.activity_rotation);
                break;
            case 13:
                setContentView(R.layout.activity_twist);
                break;
            case 14:
                setContentView(R.layout.activity_windmill);
                break;
            case 15:
                setContentView(R.layout.activity_legup);
                break;

        }

        startBtn = findViewById(R.id.startButtonActivity);
        mTextView = findViewById(R.id.timeActivity);

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mTimeRunning) {

                    stopTimer();

                } else {

                    startTimer();

                }

            }
        });

    }

    private void stopTimer() {

        countDownTimer.cancel();
        mTimeRunning = false;
        startBtn.setText("START");

    }

    private void startTimer() {

        final CharSequence first_value = mTextView.getText();
        String first_num = first_value.toString();
        String second_num = first_num.substring(0, 2);
        String third_num = first_num.substring(3, 5);

        final int number = Integer.valueOf(second_num) * 60 + Integer.valueOf(third_num);
        mTimeLeftInMills = number * 1000;

        countDownTimer = new CountDownTimer(mTimeLeftInMills, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                mTimeLeftInMills = millisUntilFinished;
                updateTimer();

            }

            @Override
            public void onFinish() {

                int newValue = Integer.valueOf(buttonValue) + 1;
                if (newValue <= 7) {

                    Intent intent = new Intent(TrainingActivityDetails.this, TrainingActivityDetails.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.putExtra("value", String.valueOf(newValue));
                    startActivity(intent);

                } else {

                    newValue = 1;
                    Intent intent = new Intent(TrainingActivityDetails.this, TrainingActivityDetails.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.putExtra("value", String.valueOf(newValue));
                    startActivity(intent);

                }

            }
        }.start();
        startBtn.setText("Pause");
        mTimeRunning = true;

    }

    private void updateTimer() {

        int minutes = (int) mTimeLeftInMills / 60000;
        int seconds = (int) mTimeLeftInMills % 60000 / 1000;

        String timeLeftText = "";
        if (minutes < 10) {
            timeLeftText = "0";
        }
        timeLeftText = timeLeftText + minutes + ":";
        if (seconds < 10) {
            timeLeftText += "0";
        }
        timeLeftText += seconds;
        mTextView.setText(timeLeftText);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}