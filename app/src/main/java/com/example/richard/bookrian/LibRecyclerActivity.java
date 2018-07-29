package com.example.richard.bookrian;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by richard on 10/6/17.
 *
 * This code is retrieved and modified from https://github.com/delaroy/CardDemo
 */


public class LibRecyclerActivity extends Fragment {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;
    View ViewLibRecycle;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewLibRecycle = inflater.inflate(R.layout.lib_recycler, container, false);

        //Refer to the recycler View
        recyclerView =
                (RecyclerView) ViewLibRecycle.findViewById(R.id.recycler_view);

        //get the layout
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new RecyclerLibAdapter();
        recyclerView.setAdapter(adapter);

        return ViewLibRecycle;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }
}

