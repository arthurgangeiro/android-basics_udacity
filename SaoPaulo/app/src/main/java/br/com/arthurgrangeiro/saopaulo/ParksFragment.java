package br.com.arthurgrangeiro.saopaulo;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ParksFragment extends Fragment {


    public ParksFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Get the rootView to the fragment
        View rootView = inflater.inflate(R.layout.tourist_attraction_list, container, false);

        // Create an array list of tourist attractions object
        ArrayList<TouristAttraction> touristAttractions = new ArrayList<TouristAttraction>();

        // Add elements to the array list
        touristAttractions.add(new TouristAttraction(R.drawable.parque_do_ibirapuera, getString(R.string.park_ibirapuera), getString(R.string.park_ibirapuera_address)));
        touristAttractions.add(new TouristAttraction(R.drawable.parque_burle_marx, getString(R.string.park_burle_marx), getString(R.string.park_burle_marx_address)));
        touristAttractions.add(new TouristAttraction(R.drawable.parque_villa_lobos, getString(R.string.park_villa_lobos), getString(R.string.park_villa_lobos_address)));
        touristAttractions.add(new TouristAttraction(R.drawable.parque_da_agua_branca, getString(R.string.park_agua_branca), getString(R.string.park_agua_branca_address)));
        touristAttractions.add(new TouristAttraction(R.drawable.parque_buenos_aires, getString(R.string.park_buenos_aires), getString(R.string.park_buenos_aires_address)));
        touristAttractions.add(new TouristAttraction(R.drawable.parque_da_luz, getString(R.string.park_of_light), getString(R.string.park_of_light_address)));
        touristAttractions.add(new TouristAttraction(R.drawable.parque_da_juventude, getString(R.string.park_of_youth), getString(R.string.park_of_youth_address)));

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
