package com.example.richard.bookrian;

import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by richard on 4/5/17.
 */

//The firebase authentication code is retrived from Tutorial solution.
    //The floating label code is retrived from http://www.androidhive.info/2015/09/android-material-design-floating-labels-for-edittext/

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    //Firebase variable
    private FirebaseAuth mAuth;
    private FirebaseAuthListener mAuthlistener ;
    private String currentMail;

    //UI elements
    private EditText i_emailText;
    private EditText i_passwordText;
    private Button loginButton;
    private Button registerButton;
    private String email;
    private String password;
    private ImageView app_logo;
    private Toolbar toolbar;
    private TextInputLayout inputLayoutEmail, inputLayoutPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        inputLayoutEmail = (TextInputLayout) findViewById(R.id.input_layout_email);
        inputLayoutPassword = (TextInputLayout) findViewById(R.id.input_layout_password);
        i_emailText = (EditText) findViewById(R.id.i_email);
        i_passwordText = (EditText) findViewById(R.id.i_password);
        loginButton = (Button) findViewById(R.id.loginButton);
        registerButton = (Button) findViewById(R.id.registerButton);
        app_logo = (ImageView) findViewById(R.id.img_signUpLogo);

        //Start listener for both button
        loginButton.setOnClickListener(this);
        registerButton.setOnClickListener(this);

        //Set up firebase initilization code
        mAuth = FirebaseAuth.getInstance();
        mAuthlistener = new FirebaseAuthListener();

        //Animation
        final Animation bubbleOut = AnimationUtils.loadAnimation(this, R.anim.bubble);
        app_logo.startAnimation(bubbleOut);

        //Listener for text change
        i_emailText.addTextChangedListener(new LoginActivity.MyTextWatcher(i_emailText));
        i_passwordText.addTextChangedListener(new LoginActivity.MyTextWatcher(i_passwordText));


    }

    @Override
    public void onStart() {
        super.onStart();

        if(mAuth != null)
            mAuth.addAuthStateListener(mAuthlistener);
    }

    @Override
    public void onStop() {
        super.onStop();

        if(mAuth != null)
            mAuth.removeAuthStateListener(mAuthlistener);
    }

    @Override
    public void onClick(View view) {
        email = i_emailText.getText().toString();
        password = i_passwordText.getText().toString();


        switch(view.getId()) {
            case R.id.loginButton:

                //Show loading dialog
                final ProgressDialog dialog = ProgressDialog.show(LoginActivity.this, "",
                        "Loading. Please wait...", true);

                //procced to login ONLY IF both input are not empty.
                if(email.equals("")||password.equals("")){

                    Toast.makeText(LoginActivity.this,
                            "Could not sign in. Please fill in email & password",
                            Toast.LENGTH_LONG).show();

                }else {

                    //Try login
                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            //check input. for display in floating label
                            submitForm();

                            //if Fail
                            if (!task.isSuccessful()) {
                                Toast.makeText(LoginActivity.this,
                                        "Could not sign in. Please check email & password",
                                        Toast.LENGTH_LONG).show();
                                //dimiss the dialog
                                if (dialog != null) {
                                    dialog.dismiss();
                                }
                            } else {
                                //Start new activity
                                Intent newIntent = new Intent(LoginActivity.this, NavigationDrawerActivity.class);
                                startActivity(newIntent);
                            }


                        }
                    });
                    break;

                }
                dialog.dismiss();
                break;
            case R.id.registerButton:

                //Navigate to Sign Up page
                Intent newIntent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(newIntent);

                break;
        }

    }

    private class FirebaseAuthListener implements FirebaseAuth.AuthStateListener{
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            FirebaseUser user = firebaseAuth.getCurrentUser();

            if(user != null) {

                //When user is logged in, create shared preference. To be used throughout the app
                Intent newIntent = new Intent(LoginActivity.this, NavigationDrawerActivity.class);
                //Split the email. exp, rcchu2@student.monash.edu become rcchu2. For better maintainance in Firebase
                currentMail = user.getEmail().split("@")[0];

                //Initialize shared preference
                SharedPreferences pref = getApplicationContext().getSharedPreferences("userName", 0);
                //update shared preference
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("email", user.getEmail());
                editor.putString("account", currentMail);
                editor.commit();

                //newIntent.putExtra("CurrentEmail",currentMail);
                startActivity(newIntent);
            }


        }

    }

    /**
     * Validating form
     */
    private void submitForm() {

        //Validate email and password format
        if (!validateEmail()) {
            return;
        }

        if (!validatePassword()) {
            return;
        }

    }



    private boolean validateEmail() {
        //get the email user key in
        String email = i_emailText.getText().toString().trim();

        //check email is empty or valid
        if (email.isEmpty() || !isValidEmail(email)) {
            //show floating
            inputLayoutEmail.setError(getString(R.string.err_msg_email));
            requestFocus(i_emailText);
            return false;
        } else {
            inputLayoutEmail.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatePassword() {
        //check password is empty or valid
        if (i_passwordText.getText().toString().trim().isEmpty()) {
            //update floating
            inputLayoutPassword.setError(getString(R.string.err_msg_password));
            requestFocus(i_passwordText);
            return false;
        } else {
            inputLayoutPassword.setErrorEnabled(false);
        }

        return true;
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                //Validate both input.
                case R.id.i_email:
                    validateEmail();
                    break;
                case R.id.i_password:
                    validatePassword();
                    break;
            }
        }
    }

}

