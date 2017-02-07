package com.example.joachimvast.popular_movies_stage1;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

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
                showData();
                mAdapter.set
            }
            else {
                displayError();
            }
        }
    }

}
