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

import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignUpActivity extends AppCompatActivity {
    private static final String TAG = "SignUpActivity";

    @BindView(R.id.sign_up)
    Button sign_up;

    @BindView(R.id.email)
    TextInputEditText email;
    @BindView(R.id.username)
    TextInputEditText username;
    @BindView(R.id.password)
    TextInputEditText password;

    @BindView(R.id.layout_email)
    TextInputLayout layout_email;
    @BindView(R.id.layout_username)
    TextInputLayout layout_username;
    @BindView(R.id.layout_password)
    TextInputLayout layout_password;

    @BindView(R.id.log_in_link)
    TextView log_in_link;

    public void signUp() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignUpFailed();
            return;
        }

        sign_up.setEnabled(false);


        // TODO: Implement your own signUp logic here.

        ParseUser user = new ParseUser();

        user.setEmail(email.getText().toString().trim());
        user.setUsername(username.getText().toString().trim());
        user.setPassword(password.getText().toString().trim());

        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.i("Signup", "Successful");

                } else {
                    Toast.makeText(SignUpActivity.this,
                            e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        new Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignUpSuccess or onSignUpFailed
                        // depending on success
                        onSignUpSuccess();
                        // onSignUpFailed();
                    }
                }, 3000);
    }


    public void onSignUpSuccess() {
        sign_up.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignUpFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
        sign_up.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;


        if (email.getText().toString().trim().isEmpty() ||
                !android.util.Patterns.EMAIL_ADDRESS.matcher(email.getText().toString().trim()).matches()) {
            layout_email.setError("enter a valid email address");
            valid = false;
        } else {
            layout_email.setError(null);
        }

        if (username.getText().toString().trim().isEmpty() ||
                username.getText().toString().trim().length() < 3) {

            layout_username.setError("should be at least 3 characters");
            valid = false;
        } else {
            layout_password.setError(null);
        }


        if (password.getText().toString().trim().isEmpty() ||
                password.getText().toString().trim().length() < 4 ||
                password.getText().toString().trim().length() > 10) {

            layout_password.setError("should be between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            layout_password.setError(null);
        }

        return valid;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        ParseAnalytics.trackAppOpenedInBackground(getIntent());

        ButterKnife.bind(this);

        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                signUp();
            }
        });

        log_in_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
            }
        });

        password.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)
                    sign_up.callOnClick();
                return false;
            }
        });
    }
}
