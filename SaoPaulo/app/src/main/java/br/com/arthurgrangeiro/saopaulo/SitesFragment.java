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
public class SitesFragment extends Fragment {


    public SitesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //cGet the rootView to the fragment
        View rootView = inflater.inflate(R.layout.tourist_attraction_list, container, false);

        // Create an array list of tourist attractions object
        ArrayList<TouristAttraction> touristAttractions = new ArrayList<TouristAttraction>();

        // Add elements to the array list
        touristAttractions.add(new TouristAttraction(R.drawable.lugar_catedral_da_se, getString(R.string.site_cathedral_of_se), getString(R.string.site_cathedral_of_se_address)));
        touristAttractions.add(new TouristAttraction(R.drawable.lugar_pateo_do_colegio, getString(R.string.site_pateo_do_colegio), getString(R.string.site_pateo_do_colegio_address)));
        touristAttractions.add(new TouristAttraction(R.drawable.lugar_bolsa_de_valores_de_sp, getString(R.string.site_bolsa_de_valores), getString(R.string.site_bolsa_de_valores_address)));
        touristAttractions.add(new TouristAttraction(R.drawable.lugar_teatro_municipal, getString(R.string.site_teatro_municipal), getString(R.string.site_teatro_municipal_address)));
        touristAttractions.add(new TouristAttraction(R.drawable.lugar_basilica_sao_pedro, getString(R.string.site_basilica_bento), getString(R.string.site_basilica_bento_address)));
        touristAttractions.add(new TouristAttraction(R.drawable.lugar_ccbb, getString(R.string.site_ccbb), getString(R.string.site_ccbb_address)));
        touristAttractions.add(new TouristAttraction(R.drawable.lugar_edificio_martinelli, getString(R.string.site_edificio_martinelli), getString(R.string.site_edificio_martinelli_address)));

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
