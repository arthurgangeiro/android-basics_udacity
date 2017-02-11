package br.com.arthurgrangeiro.saopaulo;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by Arthur on 08/01/2017.
 */

public class TouristAttractionRecyclerViewAdapter extends RecyclerView.Adapter {

    private ArrayList<TouristAttraction> touristAttractions;

    public TouristAttractionRecyclerViewAdapter(ArrayList<TouristAttraction> touristAttractions) {
        this.touristAttractions = touristAttractions;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(MainActivity.context).inflate(R.layout.list_item, parent, false);
        TouristAttractionViewHolder holder = new TouristAttractionViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        TouristAttractionViewHolder viewHolder = (TouristAttractionViewHolder) holder;
        TouristAttraction touristAttraction = touristAttractions.get(position);
        viewHolder.imageView.setImageResource(touristAttraction.getPlaceImage());
        viewHolder.attractionName.setText(touristAttraction.getPlaceName());
        viewHolder.attractionAddress.setText(touristAttraction.getContactInfo());
    }

    @Override
    public int getItemCount() {
        return touristAttractions.size();
    }
}
