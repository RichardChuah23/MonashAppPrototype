package com.example.richard.bookrian;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import static android.content.Context.MODE_PRIVATE;

public class MainActivity extends Fragment {

    private static int SPLASH_TIME_OUT = 4000;
    static String calledAlready;
    Button b_accountbutton;
    Button b_librarybutton;
    Button b_booksbutton;
    Button b_contactbutton;
    TextView l_user;

    Toolbar toolbar;

    private String currentEmail;
    private String mCurUserName;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mUserRef;
    private ValueEventListener mUserEventListener;
    View homeView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeView = inflater.inflate(R.layout.activity_main, container, false);

        mDatabase = FirebaseDatabase.getInstance();



        //SharePreference for called Already.
        //setPersistance can only be Enabled once.
        SharedPreferences prefs1 = this.getActivity().getSharedPreferences("calledAlready", MODE_PRIVATE);
        String restoredText1 = prefs1.getString("text", "No input");

        if (restoredText1 != null) {
            calledAlready = prefs1.getString("calledAlready", "No name defined");//"No name defined" is the default value.
        }

        if (calledAlready == "no") {

            mDatabase.setPersistenceEnabled(true);
            calledAlready = "yes";

        }


        //Allow use of back button
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Animation fadein = AnimationUtils.loadAnimation(this.getActivity(), R.anim.demo);
        final Animation fadein2 = AnimationUtils.loadAnimation(this.getActivity(), R.anim.demo);
        final Animation fadein3 = AnimationUtils.loadAnimation(this.getActivity(), R.anim.demo);
        final Animation fadein4 = AnimationUtils.loadAnimation(this.getActivity(), R.anim.demo);

        b_accountbutton = (Button) homeView.findViewById(R.id.AccountButton);
        b_librarybutton = (Button) homeView.findViewById(R.id.LibraryBuutton);
        b_booksbutton = (Button) homeView.findViewById(R.id.BookButton);
        b_contactbutton = (Button) homeView.findViewById(R.id.ContactButton);

        b_accountbutton.setAlpha(0);
        b_librarybutton.setAlpha(0);
        b_booksbutton.setAlpha(0);
        b_contactbutton.setAlpha(0);

        //Animation
        b_booksbutton.startAnimation(fadein);
        b_booksbutton.setAlpha(1);

        //Delay
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                b_librarybutton.startAnimation(fadein2);
                b_librarybutton.setAlpha(1);
            }
        }, 100);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                b_accountbutton.startAnimation(fadein3);
                b_accountbutton.setAlpha(1);
            }
        }, 200);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                b_contactbutton.startAnimation(fadein4);
                b_contactbutton.setAlpha(1);
            }
        }, 300);


        //SharePreference
        SharedPreferences prefs = this.getActivity().getSharedPreferences("userName", MODE_PRIVATE);
        String restoredText = prefs.getString("text", "No input");
        if (restoredText != null) {
            currentEmail = prefs.getString("account", "No name defined");//"No name defined" is the default value.
        }

        //Refer to child node - User.
        mUserRef = mDatabase.getReference("User").child(currentEmail);


        /**
        //Set the toolbar to act as the Action Bar for this Activity
        //Make sure the toolbart exists in the activity and it is not null
        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        l_user = (TextView) toolbar.findViewById(R.id.l_toolbarUser);
        **/

        final FragmentManager fragmentManager = getFragmentManager();

        b_accountbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fragmentManager.beginTransaction().replace(R.id.content_frame, new AccountActivity()).commit();

            }

        });

        b_librarybutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.beginTransaction().replace(R.id.content_frame, new LibRecyclerActivity()).commit();

            }

        });

        b_booksbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //Intent newIntent = new Intent(getActivity().getApplicationContext(), BookScrollActivity.class);
                //startActivity(newIntent);

                fragmentManager.beginTransaction().replace(R.id.content_frame, new BookScrollActivity()).commit();

            }

        });

        b_contactbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.beginTransaction().replace(R.id.content_frame, new ContactsActivity()).commit();

            }

        });

        return homeView;


    }

   /**
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Inflate menu. Add items to the actionbar if it is present
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    **/


    @Override
    public void onStart() {
        super.onStart();

        // Add value event listener to the post
        // [START post_value_event_listener]
        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get User object and use the values to update the UI
                User newUser = dataSnapshot.getValue(User.class);
                mCurUserName = newUser.getM_sName();
                //l_user.setText( mCurUserName);

                //Initialize shared preference
                SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("LoanedSize", 0);
                //update shared preference
                SharedPreferences.Editor editor = pref.edit();
                editor.putInt("m_sLoanbooksize", (5-newUser.getM_sloanQtyAllow()));
                editor.commit();



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };


        mUserRef.addValueEventListener(userListener);
        // Keep copy of post listener so we can remove it when app stops
        mUserEventListener = userListener;

    }

    @Override
    public void onStop() {
        super.onStop();

        // Remove post value event listener
        if (mUserEventListener != null) {
            mUserRef.removeEventListener(mUserEventListener);
        }


    }

}


/**

 SharedPreferences http://stackoverflow.com/questions/23024831/android-shared-preferences-example
 Firebase Code https://firebase.google.com/docs/database/android/read-and-write
splash screen https://stackoverflow.com/questions/5486789/how-do-i-make-a-splash-screen
 ToolBar https://guides.codepath.com/android/Using-the-App-Toolbar
 Sign Up http://www.androidhive.info/2015/09/android-material-design-floating-labels-for-edittext/
 Link https://stackoverflow.com/questions/2201917/how-can-i-open-a-url-in-androids-web-browser-from-my-application
 cardview https://github.com/delaroy/CardDemo



 */