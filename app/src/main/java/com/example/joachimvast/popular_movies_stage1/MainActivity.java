package com.example.joachimvast.popular_movies_stage1;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    // Create a TextView to display the list of movies from our API
    TextView mList;

    // Error message for feedback to user
    TextView mError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mList = (TextView) findViewById(R.id.tv_movielist);
        mError = (TextView) findViewById(R.id.tv_error_msg);

    }

    public void makeQuery() {
        URL apiUrl = NetworkUtils.buildUrl();
        new MovieQueryTask().execute(apiUrl);
    }

    public void displayError() {
        mList.setVisibility(View.INVISIBLE);
        mError.setVisibility(View.VISIBLE);
    }

    public void showData() {
        mError.setVisibility(View.INVISIBLE);
        mList.setVisibility(View.VISIBLE);
    }

    public class MovieQueryTask extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... params) {
            URL api = params[0];
            String result = null;
            String results = null;
            try{
                results = NetworkUtils.getResponseFromHttpUrl(api);
                JSONObject response = new JSONObject(results);
                JSONArray array = response.getJSONArray("results");
                result = array.getJSONObject(0).getString("original_title");
            } catch (IOException e){
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return result;
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
                mList.setText(results);
            }
            else {
                displayError();
            }
        }
    }

}
