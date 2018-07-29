package com.example.richard.bookrian;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by richard on 10/6/17.
 */

public class RecyclerLibAdapter extends RecyclerView.Adapter<RecyclerLibAdapter.ViewHolder>  {

    private String[] titles = {"Hargrave Andrew Library",
            "Sir Louis Mantheson Library",
            "Monash Caufiled Library",
            "Monash Peninsula Library",
};

    private String[] details = {"Clayton Campus",
            "Clayton Campus", "Caufield Campus",
            "Peninsula Campus"};

    private String[] numbers = {"(03)99625054",
            "(03)93355154", "(03)97855054",
            "(03)99865054"};
    private String[] add1 = {"Monash University",
            "Monash University", "Monash University",
            "Monash University"};
    private String[] add2 = {"13 College Walk",
            " 40 Exhibition Walk", "900 Dandenong Rd",
            "Building L, McMahons Road"};
    private String[] add3 = {"Clayton VIC 3800",
            "Clayton VIC 380", "Caulfield East VIC 3145",
            "Frankston VIC 3199"};

    private int[] images = {R.drawable.mg_hargrave,
            R.drawable.mg_mantheson ,
            R.drawable.mg_caufiled,
            R.drawable.mg_peninsula };

    class ViewHolder extends RecyclerView.ViewHolder{

        public int currentItem;
        public ImageView itemImage;
        public TextView itemTitle;
        public TextView itemDetail;

        public ViewHolder(View itemView) {
            super(itemView);
            itemImage = (ImageView)itemView.findViewById(R.id.item_image);
            itemTitle = (TextView)itemView.findViewById(R.id.item_title);
            itemDetail =
                    (TextView)itemView.findViewById(R.id.item_detail);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    int position = getAdapterPosition();


                    Library curlib = new Library(position,titles[position], details[position], numbers[position], add1[position], add2[position], add3[position
                            ]);

                    Intent newIntent = new Intent().setClass(v.getContext(), Library_detailActivity.class);
                    newIntent.putExtra("Library",curlib);
                    v.getContext().startActivity(newIntent);
                }
            });
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_layout, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }



    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.itemTitle.setText(titles[i]);
        viewHolder.itemDetail.setText(details[i]);
        viewHolder.itemImage.setImageResource(images[i]);
    }

    @Override
    public int getItemCount() {
        return titles.length;
    }


}
