package com.example.raja.newsgateway;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by admin on 5/5/2017.
 */

public class MyFragment extends Fragment {
    ImageView articleImageIV;
    SimpleDateFormat src = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    SimpleDateFormat dest = new SimpleDateFormat("MMM dd,yyyy HH:mm");
    String url="";
    TextView titleTV;
    TextView authorTV;
    TextView descriptionTV;
    TextView publishedAtTv;
    TextView articleNumTV;

    public static final MyFragment newInstance(Story s,int articleNum,int totalPages){
        MyFragment f=new MyFragment();
        Bundle bdl=new Bundle(1);
        bdl.putParcelable("Story",(Parcelable)s);
        bdl.putString("ArticleNum",articleNum+"");
        bdl.putString("ArticleTotalNum",totalPages+"");
        f.setArguments(bdl);
        return f;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){

        View v = inflater.inflate(R.layout.myfragment_layout, container, false);

        Story story=getArguments().getParcelable("Story");
        url=story.getUrl();
        titleTV=(TextView) v.findViewById(R.id.title);
        authorTV=(TextView) v.findViewById(R.id.author);
        articleImageIV=(ImageView) v.findViewById(R.id.articleImage);
        descriptionTV=(TextView) v.findViewById(R.id.description);
        publishedAtTv=(TextView) v.findViewById(R.id.publishedAt);
        articleNumTV=(TextView) v.findViewById(R.id.articleNum);

        String titleStr=story.getTitle();
        String authorStr=story.getAuthor();
        if(authorStr.equals("null")){
            authorStr="";
        }
        String descriptionStr=story.getDescription();
        if(descriptionStr.equals("null")){
            descriptionStr="";
        }
        String tempStr= story.getPublishedAt();
        String publishedStr="";
        TimeZone tz=TimeZone.getTimeZone("America/Chicago");
        Calendar cal=Calendar.getInstance(tz);
        src.setCalendar(cal);
        try {
            cal.setTime(src.parse(tempStr));
            Date date=cal.getTime();
            publishedStr=dest.format(date);
        } catch (ParseException e) {
            publishedStr="";
            e.printStackTrace();
        }

        titleTV.setText(titleStr);
        authorTV.setText(authorStr);
        descriptionTV.setText(descriptionStr);
        publishedAtTv.setText(publishedStr);
        articleNumTV.setText(getArguments().getString("ArticleNum")+" of "+getArguments().getString("ArticleTotalNum"));

        if(!story.getUrlToImage().equals("")){
            photoLoad(story.getUrlToImage());
        }

        titleTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClicked(v);
            }
        });

        articleImageIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClicked(v);
            }
        });

        descriptionTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClicked(v);
            }
        });

        return v;

    }

    public void photoLoad(final String str){
        Picasso picasso=new Picasso.Builder(getActivity()).listener(new Picasso.Listener() {
            @Override
            public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                final String changedUrl=str.replace("http:","https:");

                picasso.load(changedUrl)
                        .error(R.drawable.missingimage)
                        .placeholder(R.drawable.missingimage)
                        .into(articleImageIV);
            }
        }).build();

        picasso.load(str)
                .error(R.drawable.missingimage)
                .placeholder(R.drawable.missingimage)
                .into(articleImageIV);
    }

    public void onClicked(View v){
        startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse(url)));
    }
}
