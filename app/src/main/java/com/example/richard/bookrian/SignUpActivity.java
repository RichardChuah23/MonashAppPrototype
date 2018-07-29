package com.example.richard.bookrian;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


//The firebase authentication code is retrived from Tutorial solution.
//The floating label code is retrived from http://www.androidhive.info/2015/09/android-material-design-floating-labels-for-edittext/

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {


    //Decalre variable
    private Toolbar toolbar;
    private EditText inputName, inputEmail, inputPassword,inputPassword2;
    private TextInputLayout inputLayoutName, inputLayoutEmail, inputLayoutPassword, inputLayoutPassword2;
    private Button b_signUp;
    static String calledAlready ;
    private String currentEmail;
    private String mName;
    private String mEmail;
    private String mPassword;
    private String mPassword2;
    private String currentMail;
    private TextView l_clickHere;

    //Declare firebase variable
    private FirebaseAuth mAuth;
    private FirebaseAuthListener mAuthlistener ;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mUserRef;
    private ValueEventListener mUserEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        //Create tool bar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Refer to all view
        inputLayoutName = (TextInputLayout) findViewById(R.id.input_layout_name);
        inputLayoutEmail = (TextInputLayout) findViewById(R.id.input_layout_email);
        inputLayoutPassword = (TextInputLayout) findViewById(R.id.input_layout_password);
        inputLayoutPassword2 = (TextInputLayout) findViewById(R.id.input_layout_password2);
        inputName = (EditText) findViewById(R.id.input_name);
        inputEmail = (EditText) findViewById(R.id.input_email);
        inputPassword = (EditText) findViewById(R.id.input_password);
        inputPassword2 = (EditText) findViewById(R.id.input_password2);
        b_signUp = (Button) findViewById(R.id.btn_signup);
        l_clickHere = (TextView) findViewById(R.id.l_clickhere);

        //add textchange listener for floating label
        inputName.addTextChangedListener(new MyTextWatcher(inputName));
        inputEmail.addTextChangedListener(new MyTextWatcher(inputEmail));
        inputPassword.addTextChangedListener(new MyTextWatcher(inputPassword));
        inputPassword2.addTextChangedListener(new MyTextWatcher(inputLayoutPassword2));


        //add listner for signup button and back to login page textview
        l_clickHere.setOnClickListener(this);
        b_signUp.setOnClickListener(this);

        //Setup firebase
        mDatabase = FirebaseDatabase.getInstance();

        //ensure the setPersistenceEnable will not be called twice.
        if (calledAlready == "no") {
            mDatabase.setPersistenceEnabled(true);
            calledAlready = "yes";

        }

        //update called already to shared preference
        SharedPreferences pref = getApplicationContext().getSharedPreferences("calledAlready", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("calledAlready", calledAlready);
        editor.commit();

        //Set up firebase initilization code
        mAuth = FirebaseAuth.getInstance();
        mAuthlistener = new SignUpActivity.FirebaseAuthListener();

    }

    private class FirebaseAuthListener implements FirebaseAuth.AuthStateListener{
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            FirebaseUser user = firebaseAuth.getCurrentUser();


            if(user != null) {


            }
        }

    }

    @Override
    public void onClick(View view) {

        //Retrieve the input
        mName = inputName.getText().toString();
        mEmail = inputEmail.getText().toString();
        mPassword = inputPassword.getText().toString();
        mPassword2 = inputPassword2.getText().toString();


        switch(view.getId()) {
            case R.id.btn_signup:
                submitForm();
                //Show loading dialog
                final ProgressDialog dialog = ProgressDialog.show(SignUpActivity.this, "",
                        "Loading. Please wait...", true);
                //attempt create account only if password = confirm password and all input is not empty
                if (mPassword.equals(mPassword2) && mEmail.equals("")==false && mPassword.equals("") ==false ){



                    mAuth.createUserWithEmailAndPassword(mEmail, mPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            //If failed to create account
                            if(!task.isSuccessful()) {
                                Toast.makeText(SignUpActivity.this,
                                        "Could not register account. Please enter a valid email",
                                        Toast.LENGTH_LONG).show();
                                        dialog.dismiss();

                            }else{

                                //show toast
                                Toast.makeText(getApplicationContext(), "Thank You!", Toast.LENGTH_SHORT).show();

                                //split the email.
                                currentMail = mEmail.split("@")[0];

                                //Refer to child node - User.
                                mUserRef = mDatabase.getReference("User").child(currentMail);

                                //Push data to firebase
                                mUserRef.child("m_sName").setValue(inputName.getText().toString());
                                mUserRef.child("m_sEmail").setValue(inputEmail.getText().toString());
                                mUserRef.child("m_sloanQtyAllow").setValue(5);

                                //create intent and start activity
                                Intent newIntent = new Intent(SignUpActivity.this, NavigationDrawerActivity.class);

                                //update shared preference
                                SharedPreferences pref = getApplicationContext().getSharedPreferences("userName", 0);
                                SharedPreferences.Editor editor = pref.edit();
                                editor.putString("email", inputEmail.getText().toString());
                                editor.putString("account", currentMail);
                                editor.commit();

                                //newIntent.putExtra("CurrentEmail",currentMail);
                                startActivity(newIntent);
                            }

                        }
                    });

                }else {
                    Toast.makeText(SignUpActivity.this,
                            "Could not register account. Please confirm the input are correct",
                            Toast.LENGTH_LONG).show();
                    dialog.dismiss();

                }
                break;

            case R.id.l_clickhere:

                //click on the text view, navigate back to login
                Intent newIntent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(newIntent);
                break;

        }


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


    /**
     * Validating form
     */
    private void submitForm() {
        if (!validateName()) {
            return;
        }

        if (!validateEmail()) {
            return;
        }

        if (!validatePassword()) {
            return;
        }

        if (!validatePassword2()) {
            return;
        }

    }

    private boolean validateName() {
        //check if the input is empty
        if (inputName.getText().toString().trim().isEmpty()) {
            inputLayoutName.setError(getString(R.string.err_msg_name));
            requestFocus(inputName);
            return false;
        } else {
            inputLayoutName.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateEmail() {
        String email = inputEmail.getText().toString().trim();
        //check empty and invalid email
        if (email.isEmpty() || !isValidEmail(email)) {
            inputLayoutEmail.setError(getString(R.string.err_msg_email));
            requestFocus(inputEmail);
            return false;
        } else {
            inputLayoutEmail.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatePassword() {
        //check empty password
        if (inputPassword.getText().toString().trim().isEmpty()) {
            //update floating label
            inputLayoutPassword.setError(getString(R.string.err_msg_password));
            requestFocus(inputPassword);
            return false;
        } else {
            inputLayoutPassword.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatePassword2() {
        if (inputPassword2.getText().toString().trim().isEmpty()) {

            inputLayoutPassword2.setError(getString(R.string.err_msg_password));
            requestFocus(inputPassword2);
            return false;
        } else {
            inputLayoutPassword2.setErrorEnabled(false);
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

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2 ) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.input_name:
                    validateName();
                    break;
                case R.id.input_email:
                    validateEmail();
                    break;
                case R.id.input_password:
                    validatePassword();
                    break;

                case R.id.input_password2:
                    validatePassword2();
                    break;
            }
        }
    }



}