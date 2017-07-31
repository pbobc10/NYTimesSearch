package com.codepath.nytimessearch.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.AdapterView;

import android.widget.GridView;
import android.widget.Toast;

import com.codepath.nytimessearch.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import com.codepath.nytimessearch.adapters.ArticleArrayAdapter;
import cz.msebera.android.httpclient.Header;
import com.codepath.nytimessearch.models.Article;

public class SearchActivity extends AppCompatActivity {

    GridView gvResults;
    ArrayList<Article> articles;
    ArticleArrayAdapter adapter;
    SharedPreferences mSettings;
    String searchItemQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setupViews();
        mSettings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    }

    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }
        public void setupViews (){
            gvResults = (GridView) findViewById(R.id.gvResults);
            articles = new ArrayList<>();
            adapter = new ArticleArrayAdapter(this,articles);
            gvResults.setAdapter(adapter);
            
            // hook up listener for grid click
            gvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // create a intent to display the article
                    Intent i = new Intent(getApplicationContext(),ArticleActivity.class);
                    // get the article to display
                    Article article = articles.get(position);
                    //pass in that article into intent
                    i.putExtra("article",article);
                    // launch the activity
                    startActivity(i);
                }
            });
        }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        ///getMenuInflater().inflate(R.menu.menu_search, menu);
        ///return true;

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        /////////////////////////////////////////////

        /////////////////////////////////////////////
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // perform query here
                searchItemQuery = query;
                onArticleSearch();
                Toast.makeText(getApplicationContext(),"the menu Item : "+searchItemQuery,Toast.LENGTH_LONG).show();
                // workaround to avoid issues with some emulators and keyboard devices firing twice if a keyboard enter is used
                // see https://code.google.com/p/android/issues/detail?id=24599
                searchView.clearFocus();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this,FilterActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onArticleSearch() {

        if (isNetworkAvailable()) {
            AsyncHttpClient client = new AsyncHttpClient();
            String url = "http://api.nytimes.com/svc/search/v2/articlesearch.json?";
            RequestParams params = new RequestParams();
            params.put("api-key", "45f9b92fe41b4f9899febe121af5d0ef");
            params.put("page", 0);
            params.put("q", searchItemQuery);
            params.put("begin_date", mSettings.getString("date", ""));
            params.put("sort", mSettings.getString("sort_order", ""));
            params.put("news_desk", mSettings.getString("news_desk", ""));

            client.get(url, params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    Log.d("DEBUG", response.toString());
                    JSONArray articleJsonResults = null;
                    try {
                        articleJsonResults = response.getJSONObject("response").getJSONArray("docs");
                        adapter.clear();
                        adapter.addAll(Article.fromJSONArray(articleJsonResults));
                        adapter.notifyDataSetChanged();
                        Log.d("DEBUG", articles.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        else{
                Toast.makeText(getApplicationContext(),"Network failures! Enable your Wifi or your Mobile Data Connection",Toast.LENGTH_LONG).show();
        }
    }
}
