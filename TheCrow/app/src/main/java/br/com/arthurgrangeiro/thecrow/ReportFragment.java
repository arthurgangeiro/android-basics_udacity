package br.com.arthurgrangeiro.thecrow;


import android.app.Fragment;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ReportFragment extends Fragment
        implements LoaderCallbacks<List<Report>> {


    /**
     * URL for news data from The Guardian dataset
     */
    private static final String THE_GUARDIAN_BASE_REQUEST_URL =
            "http://content.guardianapis.com/search?q=";
    private static final String REQUEST_API_KEY = "&api-key=test";
    private static final String REQUEST_API_AND = "%20AND%20";

    /**
     * Constant value for the report loader id. We can choose any integer.
     */
    private static final int REPORT_LOADER_ID = 1;

    /**
     * ReportAdapter for the list
     */
    private ReportAdapter mAdapter;

    /**
     * TextView that is displayed when the list is empty.
     */
    private TextView mEmptyStateTextView;

    private String searchText = "";

    private View rootView;

    public ReportFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.reports_list_fragment, container, false);

        String userSearch = getArguments().getString("userSearch");

        if (userSearch != null) {
            searchText = userSearch;
        }

        ListView reportListView = (ListView) rootView.findViewById(R.id.list_frag);

        mEmptyStateTextView = (TextView) rootView.findViewById(R.id.empty_view_frag);
        reportListView.setEmptyView(mEmptyStateTextView);

        // Creates a new adater
        mAdapter = new ReportAdapter(rootView.getContext(), new ArrayList<Report>());

        //Set the adater on the ListView
        reportListView.setAdapter(mAdapter);

        reportListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Report currentReport = mAdapter.getItem(position);
                Uri reportUri = Uri.parse(currentReport.getWebURL());
                Intent webSiteIntent = new Intent(Intent.ACTION_VIEW, reportUri);
                startActivity(webSiteIntent);
            }
        });

        ConnectivityManager connectivityManager = (ConnectivityManager)
                rootView.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(REPORT_LOADER_ID, null, this);

        } else {
            View loadingIndicator = rootView.findViewById(R.id.loading_indicator_frag);
            loadingIndicator.setVisibility(View.GONE);

            mEmptyStateTextView.setText(getString(R.string.no_internet_connection));
        }

        return rootView;
    }

    @Override
    public Loader<List<Report>> onCreateLoader(int id, Bundle args) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(THE_GUARDIAN_BASE_REQUEST_URL);

        String userQuery[] = searchText.trim().split(" ");
        for (int i = 0; i < userQuery.length; i++) {
            stringBuilder.append(userQuery[i]);
            if (i < userQuery.length - 1) {
                stringBuilder.append(REQUEST_API_AND);
            }
        }
        stringBuilder.append(REQUEST_API_KEY);
        return new ReportLoader(rootView.getContext(), stringBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<Report>> loader, List<Report> data) {

        View loadingIndicator = rootView.findViewById(R.id.loading_indicator_frag);
        loadingIndicator.setVisibility(View.GONE);

        mEmptyStateTextView.setText(getString(R.string.no_reports));

        mAdapter.clear();

        if (data != null & !data.isEmpty()) {
            mAdapter.addAll(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Report>> loader) {
        mAdapter.clear();
    }
}

