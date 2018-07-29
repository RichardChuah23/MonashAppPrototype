package com.example.richard.bookrian;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by richard on 5/5/17.
 */

public class BookListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Book> book;
    private ArrayList<Book> book_list;


    public BookListAdapter(Context context, ArrayList<Book> book) {
        this.context = context;
        this.book = book;
        this.book_list = new ArrayList<Book>();
        this.book_list.addAll(book);
    }

    public static class ViewHolder {
        TextView l_book_name;
        TextView l_book_author;
        ImageView iv_available;
    }


    @Override
    public int getCount() {
        return book.size();
    }

    @Override
    public Book getItem(int i) {
        return book.get(i);
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
            view = inflater.inflate(R.layout.list_book, null);

            // Setup ViewHolder and attach to view
            vh = new ViewHolder();
            vh.l_book_name = (TextView) view.findViewById(R.id.l_bookname);
            vh.l_book_author = (TextView) view.findViewById(R.id.l_bookauthor);
            vh.iv_available = (ImageView) view.findViewById(R.id.iv_available);
            view.setTag(vh);
        } else {
            // View has already been created, fetch our ViewHolder
            vh = (ViewHolder) view.getTag();
        }

        // Assign values to the TextViews using the Monster object

        vh.l_book_name.setText(book.get(i).getM_sName());
        vh.l_book_author.setText(book.get(i).getM_sAuthor());

        if(book.get(i).getM_sAvailable().equals("yes") ){
            vh.iv_available.setVisibility(View.VISIBLE);

        }else {
            vh.iv_available.setVisibility(View.INVISIBLE);
            System.out.println("TEST TEST" + book.get(i).getM_sAvailable());
        }

        return view;
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        book.clear();
        if (charText.length() == 0) {
            book.addAll(book_list);
            book_list.addAll(book);
        }
        else
        {
            for (Book wp : book_list)
            {
                if (wp.getM_sName().toLowerCase(Locale.getDefault()).contains(charText))
                {
                    book.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }


}