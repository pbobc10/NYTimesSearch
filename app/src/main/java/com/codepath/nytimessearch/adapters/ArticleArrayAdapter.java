package com.codepath.nytimessearch.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.nytimessearch.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import com.codepath.nytimessearch.models.Article;

/**
 * Created by Owner on 7/27/2017.
 */

public class ArticleArrayAdapter extends ArrayAdapter<Article> {
    public ArticleArrayAdapter(Context context, List<Article> articles){
        super(context,android.R.layout.simple_list_item_1,articles);
    }

    ImageView imageView;
    TextView tvTitle;

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // get the data item for postion
        Article article = this.getItem(position);

        // check to see if existing view being reused
        // not using a recycled view -> inflate the layout
        if(convertView == null){
           convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_article_result,parent,false);
        }

        // find the image view
        imageView = (ImageView) convertView.findViewById(R.id.ivImage);

        //clear out recycled image from convertView from last time
        imageView.setImageResource(0);

        // find the tvTitle
        tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);

        tvTitle.setText(article.getHeadline());

        // populate the thumbnail image
        // remote download the image in the background

        String thumbnail = article.getThumbNail();

        if(!TextUtils.isEmpty(thumbnail)){
            Picasso.with(getContext()).load(thumbnail).placeholder(R.drawable.ic_image).into(imageView);
        }



        return convertView;
    }
}
