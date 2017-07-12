package apps.mohanad.com.boookfinder;

import android.text.TextUtils;
import android.util.Log;

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
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Created by mohanad on 03/07/17.
 */

public class QueryUtils {

    /**
     * Tag for the log messages
     */
    public static final String LOG_TAG = QueryUtils.class.getSimpleName();

    /**
     * Query the url and return an {@link Book} object to represent a list of books.
     */
    public static ArrayList<Book> fetchListOfBookData(String requestUrl) {
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        Log.v("son", jsonResponse);
        // Extract relevant fields from the JSON response and create an {@link Book} object
        ArrayList<Book> earthquake = extractFeatureFromJson(jsonResponse);

        // Return the {@link Book}
        return earthquake;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }

    /**
     * make a network request for a given url
     *
     * @param url : the earthquake url
     * @return jsonString
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        //create http url connection
        HttpURLConnection urlConnection = null;
        //create inputsrtream
        InputStream inputStream = null;
        try {
            //open connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Accept", "application/json");
            //set the request method to GET
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            //get the json inputstram
            inputStream = urlConnection.getInputStream();
            //convert inputstream to jsonString
            jsonResponse = readFromStream(inputStream);
        } catch (IOException e) {
            Log.e(LOG_TAG, " error in make http request");
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
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
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

    /**
     * Return an {@link Book} object by parsing out information
     * about the a list of books from the input BookJSON string.
     */
    private static ArrayList<Book> extractFeatureFromJson(String BookJSON) {
        //the result list of books
        ArrayList<Book> resultList = new ArrayList<>();

        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(BookJSON)) {
            return resultList;
        }


        try {
            //get the base json
            JSONObject baseJsonResponse = new JSONObject(BookJSON);

            if (baseJsonResponse.has("items")) {
                // get the json arrays of books
                JSONArray resultsArray = baseJsonResponse.getJSONArray("items");
                //iterate throw books and add to list
                for (int i = 0; i < resultsArray.length(); ++i) {
                    //get the ith json recipes
                    JSONObject book = resultsArray.getJSONObject(i);

                    String id = "", jsonUrl = "", title = "";

                    if (book.has("id")) {
                        //extract the recipes data
                        id = book.getString("id");
                    }

                    if (book.has("selfLink")) {
                        //getting the book url
                        jsonUrl = book.getString("selfLink");
                    }

                    StringBuilder bookAuthors = new StringBuilder();
                    String imageUrl = "";
                    float rating = 0.0f;

                    if (book.has("volumeInfo")) {
                        JSONObject volumeInfo = book.getJSONObject("volumeInfo");
                        if (volumeInfo.has("title")) {
                            title = volumeInfo.getString("title");
                        }
                        if (volumeInfo.has("authors")) {
                            JSONArray authorsArray = volumeInfo.getJSONArray("authors");
                            for (int j = 0; j < authorsArray.length(); ++j) {
                                String author = authorsArray.getString(j);
                                bookAuthors.append(author + " , ");
                            }
                        }

                        if (volumeInfo.has("imageLinks")) {
                            JSONObject imagJsonObject = volumeInfo.getJSONObject("imageLinks");
                            if (imagJsonObject.has("thumbnail")) {
                                //getting the book image link
                                imageUrl = imagJsonObject.getString("thumbnail");
                            }
                        }

                        if (volumeInfo.has("averageRating")) {
                            //getting the book averageRating
                            rating = (float) volumeInfo.getDouble("averageRating");
                        }
                    }

                    //create a new recipe object and add to the list
                    resultList.add(new Book(id, title, bookAuthors.toString(), imageUrl, rating, jsonUrl));
                }

                //return the list
                return resultList;
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the Book JSON results", e);
        }
        return resultList;
    }
}