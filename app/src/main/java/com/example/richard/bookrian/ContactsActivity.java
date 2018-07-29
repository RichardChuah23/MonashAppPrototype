package com.example.richard.bookrian;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.net.Uri;

/**
 * Created by richard on 4/5/17.
 */

//link clicking is retrieved from https://stackoverflow.com/questions/2201917/how-can-i-open-a-url-in-androids-web-browser-from-my-application

public class ContactsActivity extends Fragment {

    View contactView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contactView = inflater.inflate(R.layout.contact, container, false);

        TextView MonashWebsiteSub = (TextView) contactView.findViewById(R.id.MonashWebsiteSub);

        MonashWebsiteSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Create an intent. When clicked, browse the URL in browser
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.monash.edu"));
                startActivity(browserIntent);
            }

        });

        return contactView ;
    }
}
