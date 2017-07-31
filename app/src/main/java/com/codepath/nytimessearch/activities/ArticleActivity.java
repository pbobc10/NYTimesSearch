package com.codepath.nytimessearch.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.codepath.nytimessearch.R;

import com.codepath.nytimessearch.models.Article;

public class ArticleActivity extends AppCompatActivity {
    ShareActionProvider miShare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
  //      getSupportActionBar().setDisplayUseLogoEnabled(true);


        Article article = (Article) getIntent().getSerializableExtra("article");
        WebView webView = (WebView) findViewById(R.id.wvArticle);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        webView.loadUrl(article.getWebUrl());


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_share, menu);

        MenuItem item = menu.findItem(R.id.menu_item_share);
        miShare = (ShareActionProvider) MenuItemCompat.getActionProvider(item);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_item_share) {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            // get reference to WebView
            WebView wvArticle = (WebView) findViewById(R.id.wvArticle);
            // pass in the URL currently being used by the WebView
            shareIntent.putExtra(Intent.EXTRA_TEXT, wvArticle.getUrl());
            startActivity(Intent.createChooser(shareIntent,"share link!"));

            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
