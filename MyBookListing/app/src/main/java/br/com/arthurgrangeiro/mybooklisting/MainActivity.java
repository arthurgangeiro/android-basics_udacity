package br.com.arthurgrangeiro.mybooklisting;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

public class MainActivity extends AppCompatActivity {
    /**
     * Log tag for event logs in this class
     */
    private final String LOG_TAG = MainActivity.class.getSimpleName();
    /**
     * Query for search on the google books API
     */
    private final String GOOGLE_BOOKS_REQUEST_URL =
            "https://www.googleapis.com/books/v1/volumes?q=";

    /**
     * This value is used to hide/show the LinearLayout empty
     */
    private static boolean showIsEmpty;

    private final String EMPTY_BOOLEAN = "zero";
    /**
     * This view appears for the user before his first search
     */
    private LinearLayout empty;

    /**
     * This is an user input to compose the search query
     */
    private String searchTerm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Get an ActionBar object and call hide method
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        //Get the Linear with the id empty_list and put this in empty variable
        empty = (LinearLayout) findViewById(R.id.empty_list);

        //Check if savedInstaceState is null, if not, we can show or hide the empty LinearLayout
        if (savedInstanceState != null) {
            boolean zero = savedInstanceState.getBoolean(EMPTY_BOOLEAN);
            if (zero == true) {
                empty.setVisibility(View.GONE);
            }
        }



        //Get the search tearm from the user
        EditText mySearch = (EditText) findViewById(R.id.search_term);

        //Set the listener for when the user make an search
        mySearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if(isConectionAvailable()) {
                        searchTerm = String.valueOf(v.getText());
                        BooksAsyncTask booksAsyncTask = new BooksAsyncTask();
                        booksAsyncTask.execute();
                        MainActivity.showIsEmpty = true;
                        empty.setVisibility(View.GONE);
                    }else{
                        Toast toast = Toast.makeText(v.getContext(), getString(R.string.no_internet), Toast.LENGTH_LONG);
                        toast.show();
                    }
                    return true;
                }
                return false;
            }
        });

    }

    private boolean isConectionAvailable() {
        ConnectivityManager cm =
                (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        return isConnected;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (showIsEmpty == true) {
            empty.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(EMPTY_BOOLEAN, this.showIsEmpty);
    }

    /**
     * Get the BookAsyncTask return and call the BooksFragment using the BookAsyncTask return as a
     * parameter
     *
     * @param jsonResponse
     */
    private void updateUi(String jsonResponse) {

        //Create an Bundle object to send data for the BooksFragment
        Bundle bundle = new Bundle();
        bundle.putString("jsonResponse", jsonResponse);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        BooksFragment fragment = new BooksFragment();
        fragment.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragm, fragment);
        fragmentTransaction.addToBackStack(null);
        //fragmentTransaction.add(R.id.fragm, fragment);
        fragmentTransaction.commit();
    }

    private class BooksAsyncTask extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... params) {

            //Build an string using the GOOGLE_BOOKS_REQUEST_URL and searchTerm
            StringBuilder searchURL = new StringBuilder();
            searchURL.append(GOOGLE_BOOKS_REQUEST_URL);
            String[] myTerms = searchTerm.trim().split(" ");
            for (int i = 0; i < myTerms.length; i++) {
                searchURL.append(myTerms[i]);
                if (i < (myTerms.length - 1)) {
                    searchURL.append("+");
                }
            }

            // Create URL object
            URL url = createUrl(searchURL.toString());

            // Perform HTTP request to the URL and receive a JSON response back
            String jsonResponse = "";
            try {
                jsonResponse = makeHttpRequest(url);
            } catch (IOException e) {
                Log.e(LOG_TAG, "Problem receiving the books JSON results", e);
            }

            // Return the {@link Event} object as the result fo the {@link TsunamiAsyncTask}
            return jsonResponse;
        }

        @Override
        protected void onPostExecute(String jsonResponse) {
            updateUi(jsonResponse);
        }


        /**
         * Returns new URL object from the given string URL.
         */
        private URL createUrl(String stringUrl) {
            URL url = null;
            try {
                url = new URL(stringUrl);
            } catch (MalformedURLException exception) {
                Log.e(LOG_TAG, "Error with creating URL", exception);
                return null;
            }
            return url;
        }

        /**
         * Make an HTTP request to the given URL and return a String as the response.
         */
        private String makeHttpRequest(URL url) throws IOException {
            String jsonResponse = "";
            if (url != null) {
                HttpURLConnection urlConnection = null;
                InputStream inputStream = null;
                try {
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.setReadTimeout(10000 /* milliseconds */);
                    urlConnection.setConnectTimeout(15000 /* milliseconds */);
                    urlConnection.connect();
                    if (urlConnection.getResponseCode() == 200) {
                        inputStream = urlConnection.getInputStream();
                        jsonResponse = readFromStream(inputStream);
                    } else {
                        Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
                    }

                } catch (IOException e) {
                    // TODO: Handle the exception
                    Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results. ", e);
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                    if (inputStream != null) {
                        // function must handle java.io.IOException here
                        inputStream.close();
                    }
                }
            }
            return jsonResponse;
        }

        /**
         * Convert the {@link InputStream} into a String which contains the
         * whole JSON response from the server.
         */
        private String readFromStream(InputStream inputStream) throws IOException {
            StringBuilder output = new StringBuilder();
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String line = reader.readLine();
                while (line != null) {
                    output.append(line);
                    line = reader.readLine();
                }
            }
            return output.toString();
        }
    }
}



