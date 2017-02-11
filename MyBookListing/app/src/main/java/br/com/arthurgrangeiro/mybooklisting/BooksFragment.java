package br.com.arthurgrangeiro.mybooklisting;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class BooksFragment extends Fragment {
    /**
     * Log tag for event logs in this class
     */
    private final String LOG_TAG = "BooksFragment";

    /**
     * String to get the jsonResponse from the MainActivity
     */
    private final String STRING_JSON_RESPONSE = "jsonResponse";

    /**
     * String to find the items inside an JSON Object
     */
    private final String STRING_JSON_ITEMS = "items";
    private final String STRING_JSON_TITLE = "title";
    private final String STRING_JSON_AUTHORS = "authors";
    private final String STRING_JSON_INFO_LINK = "infoLink";
    private final String STRING_JSON_VOLUME_INFO = "volumeInfo";

    /**
     * String used when the Google Books API has not an InfoLink
     */
    private final String GOOGLE_SEARCH_LINK = "https://google.com/search?q=";

    /**
     * Data to create a Book object if no books found
     */
    private final String GOOGLE_LINK = "https://google.com";
    ArrayList arrayList = new ArrayList();

    public BooksFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.book_list, container, false);

        ArrayList<Book> books;

        String jsonResponse = getArguments().getString(STRING_JSON_RESPONSE);

        books = extractItemsFromJson(jsonResponse);

        if(books.size()==0){
            books.add(new Book(GOOGLE_LINK, getString(R.string.book_not_found), arrayList));
        }

        BookAdapter bookAdapter = new BookAdapter(books);
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.list);

        recyclerView.setAdapter(bookAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        return rootView;
    }

    /**
     * Reads a stringJson and parse this to Book objects and put each one inside an ArrayList
     *
     * @param booksJSON
     * @return
     */
    private ArrayList<Book> extractItemsFromJson(String booksJSON) {

        //Lista a ser retonada
        ArrayList<Book> myBooks = new ArrayList();
        String bookTitle;
        ArrayList<String> arrayAuthors;
        String infoLink;

        try {
            // Get the root JSON, a JSONObject
            JSONObject baseJsonResponse = new JSONObject(booksJSON);

            //Get the JSONArray "items"
            JSONArray itemsArray = baseJsonResponse.getJSONArray(STRING_JSON_ITEMS);

            //Check if the itemsArray is null
            if (itemsArray != null) {
                //Read each itemsArrayPosition
                for (int i = 0; i < itemsArray.length(); i++) {
                    //Get the JsonObject who represents Volume item (Book)
                    JSONObject item = itemsArray.getJSONObject(i);

                    //Get the JsonObject who represents the information about the volume (Book)
                    JSONObject volumeInfo = item.getJSONObject(STRING_JSON_VOLUME_INFO);

                    //Get the book title
                    if (volumeInfo.has(STRING_JSON_TITLE)) {
                        bookTitle = volumeInfo.getString(STRING_JSON_TITLE);
                    } else {
                        bookTitle = getString(R.string.title_not_found);
                    }

                    //Get the authors name
                    JSONArray authorsJSON;
                    arrayAuthors = new ArrayList();
                    if (volumeInfo.has(STRING_JSON_AUTHORS)) {
                        authorsJSON = volumeInfo.getJSONArray(STRING_JSON_AUTHORS);
                        if (authorsJSON.length() > 0) {
                            for (int j = 0; j < authorsJSON.length(); j++) {
                                arrayAuthors.add(authorsJSON.getString(j));
                            }
                        }
                    } else {
                        arrayAuthors.add(getString(R.string.author_not_found));
                    }

                    // Get the link to know more about the book
                    if (volumeInfo.has(STRING_JSON_INFO_LINK)) {
                        infoLink = volumeInfo.getString(STRING_JSON_INFO_LINK);
                    } else {
                        infoLink = GOOGLE_SEARCH_LINK + bookTitle;
                    }

                    //Add the new Book to the myBooks ArrayList
                    myBooks.add(new Book(infoLink, bookTitle, arrayAuthors));
                }
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, getString(R.string.parse_json_error), e);
        }
        return myBooks;
    }
}
