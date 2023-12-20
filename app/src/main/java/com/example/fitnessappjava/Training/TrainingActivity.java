package com.example.fitnessappjava.Training;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.fitnessappjava.R;

public class TrainingActivity extends AppCompatActivity {

    Button buttonStartFirstTraining, buttonStartSecondTraining;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);

        buttonStartFirstTraining = findViewById(R.id.buttonStartFirstTraining);
        buttonStartSecondTraining = findViewById(R.id.buttonStartSecondTraining);

        buttonStartFirstTraining.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(TrainingActivity.this, FirstTrainingActivity.class);
                startActivity(intent);

            }
        });

        buttonStartSecondTraining.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(TrainingActivity.this, SecondTrainingActivity.class);
                startActivity(intent);

            }
        });

    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.training_menu, menu);
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        int id = item.getItemId();
//
//        if (id == R.id.id_privacy) {
//
//            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(""));
//            startActivity(intent);
//
//        }
//
//        if (id == R.id.id_term) {
//
//            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(""));
//            startActivity(intent);
//
//        }
//
//        if (id == R.id.id_rate) {
//
//        }
//
//        if (id == R.id.id_more) {
//
//            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(""));
//            startActivity(intent);
//
//        }
//
//        if (id == R.id.id_share) {
//
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    public void beforeAge18(View view) {

        Intent intent = new Intent(TrainingActivity.this, FirstTrainingActivity.class);
        startActivity(intent);

    }

    public void afterAge18(View view) {

        Intent intent = new Intent(TrainingActivity.this, SecondTrainingActivity.class);
        startActivity(intent);

    }

//    public void food(View view) {
//
//        Intent intent = new Intent(TrainingActivity.this, TipsActivity.class);
//        startActivity(intent);
//
//    }

}