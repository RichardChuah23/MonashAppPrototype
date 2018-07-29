package com.example.richard.bookrian;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;

/**
 * Created by richard on 5/5/17.
 */

public class LibraryListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Library> library;

    public LibraryListAdapter(Context context, ArrayList<Library> library) {
        this.context = context;
        this.library = library;
    }

    public static class ViewHolder {
        TextView l_library_name;
        TextView l_location;
    }


    @Override
    public int getCount() {
        return library.size();
    }

    @Override
    public Library getItem(int i) {
        return library.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;}


    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder vh;

        // Check if the view has been created for the row. If not, lets inflate it
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater)
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            // Reference list item layout here
            view = inflater.inflate(R.layout.list_library, null);

            // Setup ViewHolder and attach to view
            vh = new ViewHolder();
            vh.l_library_name = (TextView) view.findViewById(R.id.l_library_name);
            vh.l_location = (TextView) view.findViewById(R.id.l_location);
            view.setTag(vh);
        } else {
            // View has already been created, fetch our ViewHolder
            vh = (ViewHolder) view.getTag();
        }

        // Assign values to the TextViews using the Monster object
        vh.l_library_name.setText(library.get(i).getM_sName());
        vh.l_location.setText(library.get(i).getM_sLocation());

        return view;
    }


}
