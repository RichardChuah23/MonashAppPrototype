package com.example.richard.bookrian;

import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

/**
 * Created by richard on 4/5/17.
 */

public class BookScrollActivity extends Fragment implements ValueEventListener{

    public static final int ADD_BOOK_REQUEST = 1;

    static boolean calledAlready = false;

    private ListView book_list_view;
    private BookListAdapter bookadapter;
    private ArrayList<Book> book_add_list;
    private ArrayList<Book> book_add_list_filtered;


    SearchView search_edittext;
    private Dialog mSplashDialog;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseRef;
    View bookscrollView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        bookscrollView = inflater.inflate(R.layout.books, container, false);

        // Initialize the Person List with the values from our database
        book_add_list = new ArrayList<>();
        book_add_list_filtered = new ArrayList<>();

        //Initialize firebase
        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseRef = mDatabase.getReference("Book");


        //Initialize Shared pref
        final SharedPreferences mprefs = this.getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);



        //Get List View Reference
        book_list_view = (ListView) bookscrollView.findViewById(R.id.lv_book_list);
        search_edittext = (SearchView) bookscrollView.findViewById(R.id.searchEditText);

        search_edittext.setDrawingCacheBackgroundColor(Color.WHITE);



        //Create monster adapter, add some dummy data to it
        //Later need to use JSON to access it
        bookadapter = new BookListAdapter(getActivity(), book_add_list);
        book_list_view.setAdapter(bookadapter);



        //When item in the list is clicked
        book_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // Get selected Book
                Book result = (Book) book_list_view.getAdapter().getItem(i);


                SharedPreferences.Editor prefsEditor = mprefs.edit();
                Gson gson = new Gson();
                String json = gson.toJson(result);
                prefsEditor.putString("booknew", json);
                prefsEditor.commit();

                final FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, new Book_detailActivity()).commit();



            }
        });



        search_edittext.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return false;
            }


        });

        return bookscrollView;
    }

    @Override
    public void onStart() {
        super.onStart();

        if(mDatabaseRef != null)
            mDatabaseRef.addValueEventListener(this);

    }

    @Override
    public void onStop() {
        super.onStop();

        if(mDatabaseRef != null)
            mDatabaseRef.removeEventListener(this);
    }


    private void UpdateListCount() {
        // Get total size of person list & set title
        int numPeople = book_add_list.size();
        getActivity().setTitle("All Book: " + numPeople);
    }

    private void RefreshListView() {
        bookadapter.notifyDataSetChanged();
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {

        book_add_list.clear();

        //Show loading dialog
        final ProgressDialog dialog = ProgressDialog.show(getActivity(), "",
                "Loading. Please wait...", true);

        for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
            Book newBook = snapshot.getValue(Book.class);
            book_add_list.add(newBook);
            book_add_list_filtered.add(newBook);
        }

        RefreshListView();

        if (dialog != null) {
            dialog.dismiss();
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        book_add_list.clear();
        if (charText.length() == 0) {
            book_add_list.addAll(book_add_list_filtered);
        }
        else
        {
            for (Book wp : book_add_list_filtered)
            {
                if (wp.getM_sName().toLowerCase(Locale.getDefault()).contains(charText))
                {
                    book_add_list.add(wp);
                }
            }
        }
        bookadapter.notifyDataSetChanged();
    }






}

