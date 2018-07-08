package com.alwaysbaked.instagram_parsehub;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseUser;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;

    @BindView(R.id.log_in)
    Button log_in;

    @BindView(R.id.username)
    TextInputEditText username;
    @BindView(R.id.password)
    TextInputEditText password;

    @BindView(R.id.layout_username)
    TextInputLayout layout_username;
    @BindView(R.id.layout_password)
    TextInputLayout layout_password;


    @BindView(R.id.sign_up_link)
    TextView sign_up_link;

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        log_in.setEnabled(false);

        // TODO: Implement your own authentication logic here.

        ParseUser.logInInBackground(username.getText().toString().trim(),
                password.getText().toString().trim(),
                new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if (user != null) {
                            Log.i("Login", "Successful");

                        } else {
                            Toast.makeText(LoginActivity.this,
                                    e.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });

        new Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        onLoginSuccess();
                        // onLoginFailed();
                    }
                }, 3000);
    }


    public void onLoginSuccess() {
        log_in.setEnabled(true);
        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
        log_in.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;


        if (username.getText().toString().trim().isEmpty() || username.getText().toString().trim().length() < 3) {
            layout_username.setError("incorrect username");
            valid = false;
        } else {
            username.setError(null);
        }

        if (password.getText().toString().trim().isEmpty() ||
                password.getText().toString().trim().length() < 4 ||
                password.getText().toString().trim().length() > 10) {

            layout_password.setError("incorrect password");
            valid = false;
        } else {
            layout_password.setError(null);
        }

        return valid;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        moveTaskToBack(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);

        ParseAnalytics.trackAppOpenedInBackground(getIntent());

        log_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });


        sign_up_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
                finish();
                overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
            }
        });

        password.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)
                    log_in.performClick();
                return false;
            }
        });

    }


}
