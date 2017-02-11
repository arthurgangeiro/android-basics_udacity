package br.com.arthurgrangeiro.saopaulo;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.lang.reflect.Array;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class BarsFragment extends Fragment {


    public BarsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Get the rootView to the fragment
        //View rootView = inflater.inflate(R.layout.tourist_attraction_list, container, false);
        View rootView = inflater.inflate(R.layout.tourist_attraction_list, container, false);

        // Create an array list of tourist attractions object
        ArrayList<TouristAttraction> touristAttractions = new ArrayList<TouristAttraction>();

        // Add elements to the array list
        touristAttractions.add(new TouristAttraction(R.drawable.bar_brahma, getString(R.string.bar_brahma), getString(R.string.bar_brahma_address)));
        touristAttractions.add(new TouristAttraction(R.drawable.bar_do_juarez, getString(R.string.bar_juarez), getString(R.string.bar_juarez_address)));
        touristAttractions.add(new TouristAttraction(R.drawable.bar_do_skye, getString(R.string.bar_skye), getString(R.string.bar_skye_address)));
        touristAttractions.add(new TouristAttraction(R.drawable.bar_frango, getString(R.string.bar_frango), getString(R.string.bar_frango_address)));
        touristAttractions.add(new TouristAttraction(R.drawable.bar_do_astor, getString(R.string.bar_astor), getString(R.string.bar_astor_address)));
        touristAttractions.add(new TouristAttraction(R.drawable.bar_do_luiz, getString(R.string.bar_do_luiz), getString(R.string.bar_do_luiz_address)));
        touristAttractions.add(new TouristAttraction(R.drawable.bar_do_frank, getString(R.string.bar_frank), getString(R.string.bar_frank_address)));

        // Create the adapter to the RecyclerView
        TouristAttractionRecyclerViewAdapter adapter = new TouristAttractionRecyclerViewAdapter(touristAttractions);

        //Get the RecyclerView element
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.list);

        //Populate the element using the adapter
        recyclerView.setAdapter(adapter);

        //Set the layout to the RecyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        // Inflate the layout for this fragment
        return rootView;
    }
}
