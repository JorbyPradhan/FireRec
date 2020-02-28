package com.example.firebaserecycler;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by jb on 21/12/2018.
 */

public class ArtistViewHolder extends RecyclerView.ViewHolder {
   public ImageView img;
   public TextView Name,major,Year;
    public ArtistViewHolder(View item){
        super(item);
        img=(ImageView)item.findViewById(R.id.r_img);
        Name=(TextView)item.findViewById(R.id.r_name);
        major=(TextView)item.findViewById(R.id.r_major);
        Year=(TextView)item.findViewById(R.id.r_year);

    }
}
