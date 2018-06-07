package com.example.raja.knowyourgovernment;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by admin on 4/14/2017.
 */

public class PhotoActivity extends AppCompatActivity {
    ScrollView scrollView;
    ConstraintLayout constraintLayout;
    ImageView iv;
    TextView locationTV;
    TextView officeTV;
    TextView nameTV;
    String retIV;
    String retLoc;
    String retOffice;
    String retName;
    String retParty;
    View view;
    Intent intent;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_size_image);
        intent=getIntent();
        initView();
        callIntent();
        callParty();
        setText();

        if(!retIV.equals("")){
            photoLoad(retIV);
        }

    }

    public void photoLoad(final String str){
        Picasso picasso=new Picasso.Builder(this).listener(new Picasso.Listener() {
            @Override
            public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                final String changedUrl=str.replace("http:","https:");

                picasso.load(changedUrl)
                        .error(R.drawable.brokenimage)
                        .placeholder(R.drawable.placeholder)
                        .into(iv);
            }
        }).build();

        picasso.load(str)
                .error(R.drawable.brokenimage)
                .placeholder(R.drawable.placeholder)
                .into(iv);
    }

    public void callParty(){
        if(retParty.equals("Democratic")){
            scrollView.setBackgroundColor(view.getResources().getColor(R.color.DemocratColor));
            constraintLayout.setBackgroundColor(view.getResources().getColor(R.color.DemocratColor));
        }
        else if(retParty.equals("Republican")){
            scrollView.setBackgroundColor(view.getResources().getColor(R.color.RepublicColor));
            constraintLayout.setBackgroundColor(view.getResources().getColor(R.color.RepublicColor));

        }
    }

    public void callIntent(){

        retIV=intent.getStringExtra("Photo_Official");
        retOffice=intent.getStringExtra("Office");;
        retName=intent.getStringExtra("Official_Name");
        retLoc=intent.getStringExtra("Location");
        retParty=intent.getStringExtra("Party");
    }

    public void initView(){
        iv=(ImageView)findViewById(R.id.no_image_full);
        scrollView=(ScrollView)findViewById(R.id.scroll_full);
        constraintLayout=(ConstraintLayout)findViewById(R.id.constraint_full);
        view=scrollView.getRootView();
        officeTV=(TextView)findViewById(R.id.office_full);
        nameTV=(TextView)findViewById(R.id.official_name_full);
        locationTV=(TextView)findViewById(R.id.official_zip_full);
    }

    public void setText(){
        locationTV.setText(retLoc);
        officeTV.setText(retOffice);
        nameTV.setText(retName);
    }
}