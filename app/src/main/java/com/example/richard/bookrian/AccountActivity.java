package com.example.richard.bookrian;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by richard on 21/5/17.
 */
public class AccountActivity extends Fragment {

    //Variable for GUI
    TextView l_name;
    TextView l_email;
    ListView lv_listview;
    TextView l_quota;

    //Variables
    static boolean calledAlready = false;


    //Variable for Firebase
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseRef;
    private DatabaseReference mUserbaseRef;
    private DatabaseReference mLoanbaseRef;
    private ValueEventListener mUserListener;
    private ValueEventListener mLoanListener;

    //Variable for ViewList
    private ListView book_list_view;
    private BookListAdapter bookadapter;
    private ArrayList<Book> book_add_list;
    private String currentEmail;
    View accountView ;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Assignment a variable for the view
        accountView = inflater.inflate(R.layout.account, container, false);

        //Refer to all View's id
        l_name = (TextView) accountView.findViewById(R.id.l_name);
        l_email = (TextView) accountView.findViewById(R.id.l_email);
        l_quota = (TextView) accountView.findViewById(R.id.l_qtyallowed);

        //Initialize Shared Pref for clicked book
        final SharedPreferences mprefs = this.getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);


        //Get data from SharedPreference
        SharedPreferences prefs = this.getActivity().getSharedPreferences("userName", Context.MODE_PRIVATE);
        String restoredText = prefs.getString("text", "No input");
        if (restoredText != null) {
            currentEmail = prefs.getString("account", "No name defined");//"No name defined" is the default value.
        }

        // Initialize Database
        mDatabase = FirebaseDatabase.getInstance();
        //Refer to user
        mDatabaseRef = mDatabase.getReference("User");

        //Refer to the user's database
        mUserbaseRef = mDatabaseRef.child(currentEmail);
        //Refer to Loan
        mLoanbaseRef = mUserbaseRef.child("m_sLoan");

        //List View
        book_add_list = new ArrayList<>();
        lv_listview = (ListView)accountView.findViewById(R.id.lv_book) ;

        //Set adapter. show it on GUI
        bookadapter = new BookListAdapter(getActivity(), book_add_list);
        lv_listview.setAdapter(bookadapter);

        //Clicking on the items in the ListView
        lv_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // Get the selected Library.(An obj instance)
                Book result = (Book) lv_listview.getAdapter().getItem(i);

                //Use sharepreferences to share the created obj. Use it in Book_detailActivity
                SharedPreferences.Editor prefsEditor = mprefs.edit();
                Gson gson = new Gson();
                String json = gson.toJson(result);
                prefsEditor.putString("booknew", json);
                prefsEditor.commit();

                //Create a fragment manager
                final FragmentManager fragmentManager = getFragmentManager();

                //Navigate to the fragment
                fragmentManager.beginTransaction().replace(R.id.content_frame, new Book_detailActivity()).commit();

            }
        });


        return accountView;

    }


    @Override
    public void onStart() {
        super.onStart();
        //Value listener for firebase
        ValueEventListener userListener = new ValueEventListener() {

            //Syncronize each time the data changes
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                User user = dataSnapshot.getValue(User.class);
                //Update label
                l_name.setText(user.getM_sName());
                l_email.setText(user.getM_sEmail());
                l_quota.setText(Integer.toString(user.getM_sloanQtyAllow()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        };

        //Add listener
        mUserbaseRef.addValueEventListener(userListener);
        //Assign to mUserListener to remove it on onStop()
        mUserListener = userListener;

        ValueEventListener loanListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                book_add_list.clear();

                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Book newBook = snapshot.getValue(Book.class);
                    book_add_list.add(newBook);

                }


                RefreshListView();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        };

        //Add listener
        mLoanbaseRef.addValueEventListener(loanListener);
        //Assign to mLoanListener to remove it on onStop()
        mLoanListener = loanListener;
    }

    @Override
    public void onStop() {
        super.onStop();

        // Remove User value event listener
        if (mUserbaseRef != null) {
            mUserbaseRef.removeEventListener(mUserListener);
        }

        // Remove Loan value event listener
        if (mLoanbaseRef != null) {
            mLoanbaseRef.removeEventListener(mLoanListener);
        }

    }

    private void RefreshListView() {
        bookadapter.notifyDataSetChanged();
    }

}

