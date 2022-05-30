package com.finproj.movieapp;

import android.content.Intent;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.finproj.movieapp.login.LoginActivity;

public class IntroScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //noinspection deprecation
        new Handler().postDelayed(() -> {
            startActivity(new Intent(IntroScreenActivity.this, LoginActivity.class));
            finish();
        }, 1500);
    }
}