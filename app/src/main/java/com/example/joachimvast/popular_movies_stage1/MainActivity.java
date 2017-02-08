package com.example.joachimvast.popular_movies_stage1;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MovieAdapter.itemClickListener{


    // Declare variables
    TextView mError;
    TextView mInstructions;
    RecyclerView mRecyclerView;
    MovieAdapter mAdapter;
    ArrayList<Movie> movielist = new ArrayList<>();
    CheckBox mPopular;
    CheckBox mTop;
    Button mSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Reference ID to each variable
        mError = (TextView) findViewById(R.id.tv_error_msg);
        mInstructions = (TextView) findViewById(R.id.tv_instruction);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_thumb);
        mSearch = (Button) findViewById(R.id.btn_search);
        mPopular = (CheckBox) findViewById(R.id.cb_sortbypopular);
        mTop = (CheckBox) findViewById(R.id.cb_sortbytop);

        // Create a LayoutManager for the RecyclerView
        GridLayoutManager manager = new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);

        // Set the LayoutManager for the RecyclerView object
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.hasFixedSize();

        // Make a new MovieAdapter object and set the adapter of the RecyclerView to that object
        mAdapter = new MovieAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
    }

    // Method for handling click on a thumbnail
    @Override
    public void onItemClick(int clickedItemIndex) {

        // Get the parent contex
        Context context = MainActivity.this;

        // Get the class of destination activity
        Class destinationActivity = Detailed.class;

        // Make a new intent
        Intent intent = new Intent(context, destinationActivity);

        // Get selected Movie Object to append data to our Intent so the ChildActivity can parse this into a TextView
        Movie selected = movielist.get(clickedItemIndex);
        intent.putExtra("image_path", selected.imagePath);
        intent.putExtra("title", selected.title);
        intent.putExtra("rating", selected.rating);
        intent.putExtra("release", selected.release);
        intent.putExtra("overview", selected.overview);

        // Start the activity using the intent
        startActivity(intent);
    }

    // Make a query, called when the Search button is clicked
    public void makeQuery(View v) {

        /*  Declare the variable that will be passed on in our call to the buildUrl function
            so we can determine whether to get the most popular or top rated movies
         */
        String sort = "";

        // If both checkboxes or none are checked, change the color of the text to red, providing feedback to the user
        if ((mTop.isChecked() && mPopular.isChecked()) || (!mTop.isChecked() && !mPopular.isChecked())) {
            mInstructions.setTextColor(Color.parseColor("#ff0000"));
        }
        else {

            // If statements to set the value of our sort value
            if (mPopular.isChecked()){
                sort = "popular";
            }
            if (mTop.isChecked()){
                sort = "top_rated";
            }

            // Set the visibility to GONE for unwanted views
            mPopular.setVisibility(View.GONE);
            mTop.setVisibility(View.GONE);
            mInstructions.setVisibility(View.GONE);
            mSearch.setVisibility(View.GONE);

            // Get our URL, pass on the sort variable
            URL apiUrl = NetworkUtils.buildUrl(sort);

            // Make a new MovieQueryTask Object and execute the task
            new MovieQueryTask().execute(apiUrl);
        }
    }

    // Display an error
    public void displayError() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mError.setVisibility(View.VISIBLE);
    }

    // Display our data
    public void showData() {
        mError.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }


    public class MovieQueryTask extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... params) {
            // Get the URL
            URL api = params[0];

            // Initiate results String
            String results = null;

            // try - catch to catch any IOExceptions
            try{
                // Set the value of the results String to the response from the HTTP request
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
                        Log.d("MyActivity",movie.imagePath);

                        // Add the Movie Object to our ArrayList<Movie> movielist
                        movielist.add(movie);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // Show the JSON data
                showData();

                // Set the list of our adapter
                mAdapter.setList(movielist);
            }
            else {

                // Display error
                displayError();
            }
        }
    }

}
