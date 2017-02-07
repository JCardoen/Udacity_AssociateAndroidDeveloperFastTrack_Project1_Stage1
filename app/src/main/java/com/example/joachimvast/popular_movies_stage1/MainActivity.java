package com.example.joachimvast.popular_movies_stage1;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    // Error message for feedback to user
    TextView mError;
    RecyclerView mRecyclerView;
    MovieAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mError = (TextView) findViewById(R.id.tv_error_msg);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_thumb);

        GridLayoutManager manager = new GridLayoutManager(this, GridLayoutManager.DEFAULT_SPAN_COUNT, GridLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.hasFixedSize();

        mAdapter = new MovieAdapter();
        mRecyclerView.setAdapter(mAdapter);
    }

    public void makeQuery() {
        URL apiUrl = NetworkUtils.buildUrl();
        new MovieQueryTask().execute(apiUrl);
    }

    public void displayError() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mError.setVisibility(View.VISIBLE);
    }

    public void showData() {
        mError.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    public class MovieQueryTask extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... params) {
            URL api = params[0];
            String results = null;
            try{
                results = NetworkUtils.getResponseFromHttpUrl(api);
            } catch (IOException e){
                e.printStackTrace();
            }

            return results;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String results) {
            // If the results from our HTTP request are not null, display the data
            if (results != null && !results.equals("")){
                ArrayList<Movie> movielist = new ArrayList();
                // Parse our JSONString
                try {
                    // Make an object of our JSON String
                    JSONObject object = new JSONObject(results);

                    // Make an array of our JSON Object
                    JSONArray array = object.getJSONArray("results");

                    // Iterate over each JSONObject and add them to our ArrayList<Movie> variable
                    for (int i = 0; i < array.length() ; i++){

                        // Create a movie object with the index of the array
                        Movie movie = new Movie(array.getJSONObject(i));

                        // Add the Movie Object to our ArrayList<Movie> movielist
                        movielist.add(movie);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                showData();
                mAdapter.setList(movielist);
            }
            else {
                displayError();
            }
        }
    }

}
