package br.com.arthurgrangeiro.saopaulo;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Arthur on 08/01/2017.
 */

public class TouristAttractionViewHolder extends RecyclerView.ViewHolder {

    final ImageView imageView;
    final TextView attractionName;
    final TextView attractionAddress;

    public TouristAttractionViewHolder(View itemView) {
        super(itemView);
        imageView = (ImageView) itemView.findViewById(R.id.tourist_image);
        attractionName = (TextView) itemView.findViewById(R.id.tourist_attraction_name);
        attractionAddress = (TextView) itemView.findViewById(R.id.tourist_attraction_address);
    }
}
