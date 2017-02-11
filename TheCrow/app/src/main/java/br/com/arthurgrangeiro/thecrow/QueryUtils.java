package br.com.arthurgrangeiro.thecrow;

import android.util.Log;
import android.widget.ArrayAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Arthur on 20/01/2017.
 */

public final class QueryUtils {

    private QueryUtils() {

    }

    public static ArrayList<Report> fetchReportData(String requestUrl) {
        //Create URL object
        URL url = createURL(requestUrl);

        // Perform a HTTP requesttothe URL and Receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e("Utils", "Erros closing input stream", e);
        }

        ArrayList<Report> reports = extractReports(jsonResponse);

        return reports;
    }

    /**
     * Return a list of Reports objects that has been buit up from
     * parsing a Json response.
     *
     * @param jsonResponse
     * @return
     */
    private static ArrayList<Report> extractReports(String jsonResponse) {

        ArrayList<Report> reports = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONObject jsonObjectResponse = jsonObject.getJSONObject("response");
            JSONArray jsonArrayResults = jsonObjectResponse.getJSONArray("results");

            for (int i = 0; i < jsonArrayResults.length(); i++) {
                JSONObject jsonItem = jsonArrayResults.getJSONObject(i);
                String sectionName = jsonItem.getString("sectionName");
                String webTitle = jsonItem.getString("webTitle");
                String date = jsonItem.getString("webPublicationDate");
                String webUrl = jsonItem.getString("webUrl");
                reports.add(new Report(sectionName, webTitle, date, webUrl));
            }
        } catch (JSONException e) {
            Log.e("Utils", "Problem parsing the report Json results", e);
        }
        return reports;
    }

    /**
     * Make a HTTP request to the given URL and returns a String as the response.
     *
     * @param url
     * @return
     * @throws IOException
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            //then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e("Utils", "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e("Utils", "Problem retrieving the report Json results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the InputStream into a String which contains the
     * whole JSON response from server.
     *
     * @param inputStream
     * @return
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Returns a new URL object from the given string URL
     *
     * @param stringUrl
     * @return
     */
    private static URL createURL(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e("Utils", "Error with creating URL", e);
        }
        return url;
    }
}
