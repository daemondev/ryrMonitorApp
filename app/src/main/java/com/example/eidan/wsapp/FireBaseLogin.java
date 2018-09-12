package com.example.eidan.wsapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;

//import com.daemondev.eidan.wsapp.R;

public class FireBaseLogin extends AppCompatActivity implements View.OnClickListener{

    TextView mTVUser, mTVPassword;
    Button mBtnLogin;
    private FirebaseAuth mAuth;
    private static final String TAG = "EmailPassword";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fire_base_login);

        mTVUser = (TextView) findViewById(R.id.email);
        mTVPassword = (TextView) findViewById(R.id.password);
        mBtnLogin = (Button) findViewById(R.id.email_sign_in_button);
        mAuth = FirebaseAuth.getInstance();

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FireBaseLogin.this, MainActivity.class);
                startActivity(intent);
                finish();
                setContentView(R.layout.activity_main);
                Toast.makeText(FireBaseLogin.this, "Test Click", Toast.LENGTH_LONG).show();
            }
        };
//        mBtnLogin.setOnClickListener(onClickListener);
        mBtnLogin.setOnClickListener(this);

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
//        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser user) {
//        hideProgressDialog();
        if (user != null) {
//            mStatusTextView.setText(getString(R.string.emailpassword_status_fmt, user.getEmail(), user.isEmailVerified()));
//            mDetailTextView.setText(getString(R.string.firebase_status_fmt, user.getUid()));

//            findViewById(R.id.email_password_buttons).setVisibility(View.GONE);
//            findViewById(R.id.email_password_fields).setVisibility(View.GONE);
//            findViewById(R.id.signed_in_buttons).setVisibility(View.VISIBLE);
//
//            findViewById(R.id.verify_email_button).setEnabled(!user.isEmailVerified());
        } else {
//            mStatusTextView.setText(R.string.signed_out);
//            mDetailTextView.setText(null);

//            findViewById(R.id.email_password_buttons).setVisibility(View.VISIBLE);
//            findViewById(R.id.email_password_fields).setVisibility(View.VISIBLE);
//            findViewById(R.id.signed_in_buttons).setVisibility(View.GONE);
        }
    }

    private void signIn(String email, String password){

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithEmail:failed", task.getException());
                            Toast.makeText(FireBaseLogin.this, R.string.auth_failed,
                                    Toast.LENGTH_SHORT).show();
                        }else{
                            Log.d(TAG, "User Login:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                            Intent intent = new Intent(FireBaseLogin.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                            setContentView(R.layout.activity_main);

                        }

                        // ...
                    }
                });



    }

    private boolean validateForm() {
        boolean valid = true;

        String email = mTVUser.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mTVUser.setError("Required.");
            valid = false;
        } else {
            mTVUser.setError(null);
        }

        String password = mTVPassword.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mTVPassword.setError("Required.");
            valid = false;
        } else {
            mTVPassword.setError(null);
        }

        return valid;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.email_create_account_button) {
            createAccount(mTVUser.getText().toString(), mTVPassword.getText().toString());
        } else if (i == R.id.email_sign_in_button) {
            signIn(mTVUser.getText().toString(), mTVPassword.getText().toString());
        }
        else if (i == R.id.sign_out_button) {
            signOut();
        }
//        else if (i == R.id.verify_email_button) {
//            sendEmailVerification();
//        }
    }

    private void signOut() {
        mAuth.signOut();
        updateUI(null);
    }
    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) {
            return;
        }

//        showProgressDialog();

        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(FireBaseLogin.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // [START_EXCLUDE]
//                        hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
        // [END create_user_with_email]
    }

}
