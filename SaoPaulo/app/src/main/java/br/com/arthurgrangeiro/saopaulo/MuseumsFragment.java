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
public class MuseumsFragment extends Fragment {


    public MuseumsFragment() {
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
        touristAttractions.add(new TouristAttraction(R.drawable.museu_pinacoteca, getString(R.string.museum_pinacoteca), getString(R.string.museum_pinacoteca_address)));
        touristAttractions.add(new TouristAttraction(R.drawable.museu_mam, getString(R.string.museum_mam), getString(R.string.museum_mam_address)));
        touristAttractions.add(new TouristAttraction(R.drawable.museu_do_futebol, getString(R.string.museum_of_soccer), getString(R.string.museum_of_soccer_address)));
        touristAttractions.add(new TouristAttraction(R.drawable.museu_da_lingua_portuguesa, getString(R.string.museum_of_language), getString(R.string.museum_of_language_address)));
        touristAttractions.add(new TouristAttraction(R.drawable.museu_catavento, getString(R.string.museum_catavento), getString(R.string.museum_catavento_address)));
        touristAttractions.add(new TouristAttraction(R.drawable.museu_tomie_otake, getString(R.string.museum_tomie_otake), getString(R.string.museum_tomie_otake_address)));
        touristAttractions.add(new TouristAttraction(R.drawable.museu_masp, getString(R.string.museum_masp), getString(R.string.museum_masp_address)));

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
