package com.example.richard.bookrian;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by richard on 11/6/17.
 */

public class ReferenceActivity extends Fragment {

    View refView;
    private TextView l_ref;
    private TextView l_aboutme;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        refView = inflater.inflate(R.layout.references, container, false);

        l_aboutme = (TextView) refView.findViewById(R.id.l_aboutme);
        l_ref = (TextView) refView.findViewById(R.id.l_ref);



        String aboutme = "Hi, I am Richard. A monash third year student in 2017. This is an application I developed for FIT3027-Assignment. It is my very first mobile application I have developed. Below are the resources I used to build this app. \n\n";

        String ref =

                "1.SharedPreferences \n" +
                "Retrieved From :\n"+
                "http://stackoverflow.com/questions/23024831/android-shared-preferences-example\n\n"+

                "2.Firebase \n" +
                "Retrieved From :\n"+
                "https://firebase.google.com/docs/database/android/read-and-write\n\n"+

                "3.Splash Screen \n" +
                "Retrieved From :\n"+
                "http://stackoverflow.com/questions/23024831/android-shared-preferences-example\n\n"+

                "4.ToolBar \n" +
                "Retrieved From :\n"+
                "https://guides.codepath.com/android/Using-the-App-Toolbar\n\n" +

                "5. Sign Up \n" +
                "Retrieved From :\n"+
                "http://www.androidhive.info/2015/09/android-material-design-floating-labels-for-edittext/\n\n" +

                "6.Clickable TextView (Link) \n" +
                "Retrieved From :\n"+
                "https://stackoverflow.com/questions/2201917/how-can-i-open-a-url-in-androids-web-browser-from-my-application\n\n"+

                "7. CardView \n" +
                "Retrieved From :\n"+
                "https://github.com/delaroy/CardDemo/\n\n" +

                        "8. Navigation Menu \n" +
                        "Retrieved From :\n"+
                        "http://robinsonprogramming.com/tuts/download.php?page=downloads_android_studio_$_NavigationDrawer.zip\n\n"




                ;


        l_ref.setText(ref);
        l_aboutme.setText(aboutme);

        return refView;
    }
}
