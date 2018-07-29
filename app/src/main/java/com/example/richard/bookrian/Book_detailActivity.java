package com.example.richard.bookrian;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import static android.content.Context.MODE_PRIVATE;


/**
 * Created by richard on 7/5/17.
 */

public class Book_detailActivity extends Fragment {

    //Declare GUI var
    TextView l_book_name;
    TextView l_author_name;
    TextView l_availability;
    Button b_loan;
    Button b_return;
    TextView l_book_date;
    TextView l_library;

    //Declare var
    private static final String TAG = "Book_detailActivity";
    private FirebaseDatabase mDatabase;
    private DatabaseReference mBookRef;
    private DatabaseReference mUserRef;
    private DatabaseReference mInUserRef;
    private DatabaseReference mLoanRef;
    private String currentEmail;
    private Book book;
    private int mLibBookCount;
    private int mUserBookCount;
    private ValueEventListener mUserEventListener;
    private ValueEventListener mLoanEventListener;
    private String mCurrentDate;
    private String mLoanDate;
    private String mToday;
    private String mToday2;
    private boolean mBookloaned;
    View bookView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        bookView = inflater.inflate(R.layout.book_detail, container, false);

        //Initialize firebase
        final SharedPreferences mprefs = this.getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);

        //Retrive the shared obj
        Gson gson = new Gson();
        String json = mprefs.getString("booknew", "");
        final Book book = gson.fromJson(json, Book.class);

        //Refer to all view id
        l_book_name = (TextView) bookView.findViewById(R.id.l_book_name);
        l_author_name = (TextView) bookView.findViewById(R.id.l_book_author);
        l_availability = (TextView) bookView.findViewById(R.id.l_book_available);
        b_loan = (Button) bookView.findViewById(R.id.loan_button);
        b_return = (Button) bookView.findViewById(R.id.return_button);
        l_book_date = (TextView) bookView.findViewById(R.id.l_book_date);
        l_library = (TextView) bookView.findViewById(R.id.l_book_location);


        //SharedPreference Retrieve data.
        //Retrieve User name
        SharedPreferences prefs = this.getActivity().getSharedPreferences("userName", MODE_PRIVATE);
        String restoredText = prefs.getString("text", "No input");
        if (restoredText != null) {
            currentEmail = prefs.getString("account", "No name defined");//"No name defined" is the default value.
        }

        //Refer firebase
        mDatabase = FirebaseDatabase.getInstance();

        //Get Parcel. Current get current Book obj
        //book = getActivity().getIntent().getExtras().getParcelable("Result");

        //Refer to child node-Book.
        mBookRef = mDatabase.getReference("Book").child(book.getM_sName());

        //Refer to child node - User.
        mUserRef = mDatabase.getReference("User").child(currentEmail);

        mLoanRef = mUserRef.child("m_sLoan").child(book.getM_sName());

        //Set all label to show the book's detail
        l_book_name.setText(book.getM_sName());
        l_author_name.setText(book.getM_sAuthor());
        l_availability.setText(book.getM_sAvailable());
        l_library.setText(book.getM_sLibrary());


        //Availability of book. Enable/Disable the loan button
        if (book.getM_sAvailable().equals("yes")) {
            b_loan.setEnabled(true);
        }else {
            b_loan.setEnabled(false);
        }

        //If there is user did loan the book. Enable the return button
        b_return.setEnabled(true);


        //When the loan button is clicked.
        b_loan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Check book quantity and User allowed loan quota (maxinum 5 book)
                if (mLibBookCount != 0 && mUserBookCount > 0 ) {

                    //Book qty -1
                    //mLibBookCount = book.getM_sQuantity();
                    mBookRef.child("m_sQuantity").setValue((mLibBookCount - 1));

                    //User loan quota -1
                    mUserRef.child("m_sloanQtyAllow").setValue((mUserBookCount - 1));

                    //Refer to the book
                    mInUserRef = mUserRef.child("m_sLoan").child(book.getM_sName());

                    //Push the new loaned book to the user's firebase
                    mInUserRef.child("_id").setValue(book.get_id());
                    mInUserRef.child("m_sAvailable").setValue(book.getM_sAvailable());
                    mInUserRef.child("m_sAuthor").setValue(book.getM_sAuthor());
                    mInUserRef.child("m_sName").setValue(book.getM_sName());
                    mInUserRef.child("m_sLibrary").setValue(book.getM_sLibrary());

                    //Create a Date
                    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                    Date today = new Date();

                    //Calculate the time 5 days later
                    long add = today.getTime() + 432000000 ;
                    //Convert it to day yyy-mm-dd
                    today = new Date(add);
                    mToday = dateFormat.format(today);

                    //Store it in firebase
                    mInUserRef.child("m_sLoanDate").setValue(mToday);

                    //Disable the button
                    b_loan.setEnabled(false);

                    Toast.makeText(getActivity(), "You have loaned the book. Please obtain the book in our nearest library.", Toast.LENGTH_LONG).show();


                } else {
                    if(mUserBookCount <= 0) {
                        //Pop toast
                        Toast.makeText(getActivity(), "You have reached the maxinum loan of book. Please contact our support.", Toast.LENGTH_LONG).show();
                    }else if (mLibBookCount == 0){
                        //Pop toast
                        Toast.makeText(getActivity(), "The selected book is out of stock. Please come back later or contact our librarian", Toast.LENGTH_LONG).show();
                    }
                }

            }

        });

        b_return.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                //Book qty +1
                //mLibBookCount = book.getM_sQuantity();
                mBookRef.child("m_sQuantity").setValue((mLibBookCount + 1));

                //User loan quota +1
                mUserRef.child("m_sloanQtyAllow").setValue((mUserBookCount + 1));

                //Refer to the book
                mInUserRef = mUserRef.child("m_sLoan").child(book.getM_sName());
                mInUserRef.setValue(null);

                //Pop toast
                Toast.makeText(getActivity(), "You Have returned the book", Toast.LENGTH_LONG).show();

                //Disable the button
                b_return.setEnabled(false);

                //Toast
                Toast.makeText(getActivity(), "Thankyou. Please return the book to our nearest library in 24 hours", Toast.LENGTH_LONG).show();


            }
        });

        //return bookView;
        return bookView;
    }

    @Override
    public void onStart() {
        super.onStart();

        //Initialize firebase
        final SharedPreferences mprefs = this.getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);

        //Retrieve the sharedPreference
        Gson gson = new Gson();
        String json = mprefs.getString("booknew", "");
        final Book book = gson.fromJson(json, Book.class);



        // Add value event listener to the post
        // [START post_value_event_listener]
        DatabaseReference mUserLoanRef = mUserRef.child(book.getM_sName());
        mUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.child("m_sLoan").child(book.getM_sName()).exists() == true) {

                    //User can only borrow one same book at a time
                    //If it is not loaned, he cannot return
                    //If it is loanded, he cannot loan again
                    //Enable/Disalble the loan/return button according to situation
                    mBookloaned = true;
                    b_loan.setEnabled(false);
                    b_return.setEnabled(true);


                }else{

                    mBookloaned = false;
                    //If the user already loaned the book. The return button will be disabled
                    if (book.getM_sAvailable().equals("yes")){
                        b_loan.setEnabled(true);
                    }
                    //If the user did not loan the book. The return button will be disabled
                    b_return.setEnabled(false);
                }



                // Get User object and use the values to update the UI
                User newUser = dataSnapshot.getValue(User.class);
                mUserBookCount = newUser.getM_sloanQtyAllow();



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mBookRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Book newBook1 = dataSnapshot.getValue(Book.class);
                mLibBookCount = newBook1.getM_sQuantity();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // [START_EXCLUDE]
                Toast.makeText(getActivity(), "Failed to load post.",
                        Toast.LENGTH_SHORT).show();
                // [END_EXCLUDE]
            }
        });




        ValueEventListener loanListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Book newBook = dataSnapshot.getValue(Book.class);

                if (newBook != null) {
                    mLoanDate = newBook.getM_sLoanDate();
                }else{
                    //Set default date to today
                    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                    Date today2 = new Date();
                    mToday = dateFormat.format(today2);
                    mLoanDate = mToday;

                }


                try {

                    //Set date format
                    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                    Date today2 = new Date();
                    Date return_date = dateFormat.parse(mLoanDate);
                    //Get the time difference
                    long diff = return_date.getTime() - today2.getTime();
                    long days = diff/ (24*60*60*1000);
                    //convert it to days (String)
                    String s = Objects.toString(days,null);

                    //tmp, convert the s to int. To use for comparison below
                    int tmp = Integer.parseInt(s);

                    //Update  Date label
                    if(tmp > 0 ) {

                        l_book_date.setText(s + " days");

                    }else if (tmp<0) {
                        l_book_date.setText("The loan has overdued " + s + " day" );
                        //Set color to red if its over dued
                        l_book_date.setTextColor(Color.RED);
                    }else if (tmp == 0){
                        l_book_date.setText("Book is not loaned" );
                    }


                } catch (ParseException e) {
                    e.printStackTrace();
                }


                }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // [START_EXCLUDE]
                Toast.makeText(getActivity(), "Failed to load post.",
                        Toast.LENGTH_SHORT).show();
                // [END_EXCLUDE]
            }
            };
        mLoanRef.addValueEventListener(loanListener);
        // Keep copy of post listener so we can remove it when app stops
        mLoanEventListener = loanListener;


        };


    @Override
    public void onStop() {
        super.onStop();

        // Remove post value event listener
        if (mUserEventListener != null) {
            mUserRef.removeEventListener(mUserEventListener);
        }

        // Remove post value event listener
        if (mLoanEventListener != null) {
            mLoanRef.removeEventListener(mLoanEventListener);
        }



    }
}
