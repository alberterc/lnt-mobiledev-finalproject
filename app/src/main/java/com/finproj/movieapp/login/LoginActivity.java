package com.finproj.movieapp.login;

import android.content.Intent;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.finproj.movieapp.MainMenuActivity;
import com.finproj.movieapp.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.shobhitpuri.custombuttons.GoogleSignInButton;

public class LoginActivity extends AppCompatActivity {

    private GoogleSignInClient gsc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button exitButton = findViewById(R.id.exit_button);
        exitButton.setOnClickListener(v -> onBackPressed());

        GoogleSignInButton logInButton = findViewById(R.id.login_button);
        logInButton.setOnClickListener(view -> {
            createLogInRequest();
            logIn();
        });
    }

    private void logIn() {
        Intent logInIntent = gsc.getSignInIntent();
        //noinspection deprecation
        startActivityForResult(logInIntent, 100);
    }

    private void createLogInRequest() {
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso
        gsc = GoogleSignIn.getClient(this, gso);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                task.getResult(ApiException.class);
                changeToMainMenuActivity();
            } catch (ApiException e) {
                Toast.makeText(this, "Log In Failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void changeToMainMenuActivity() {
        Intent intent = new Intent(getApplicationContext(), MainMenuActivity.class);
        startActivity(intent);
        finish();
    }
}