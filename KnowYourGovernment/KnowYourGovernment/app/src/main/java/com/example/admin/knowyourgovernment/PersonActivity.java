package com.example.admin.knowyourgovernment;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.text.util.Linkify;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
public class PersonActivity extends AppCompatActivity {
    private String recZipcode;
    TextView zipTV;
    TextView officeTV;
    TextView official_nameTV;
    TextView partyTV;
    TextView addressTV;
    TextView phoneTV;
    TextView emailTV;
    TextView websiteTV;
    ImageView googlePlusIV;
    ImageView facebookIV;
    ImageView twitterIV;
    ImageView youtubeIV;
    ImageView officialPhotoIV;
    ScrollView scrollView;
    ConstraintLayout constraintLayout;
    String retParty;
    String retPhone;
    String retAddress;
    String retEmail;
    String retWebsite;
    String retPhoto;
    String fb_id;
    String google_id;
    String twitter_id;
    String youtube_id;
    Intent intent;
    SpannableString spanAddress;

    View view;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.official_layout);
        intent=getIntent();
        initViews();
        callGetIntent();
        callSetText();
        callLinkify();
        callPartyColor();
        callSocialLinks();
        //photo check
        if(!retPhoto.equals("")){
            photoLoad(retPhoto);
        }

    }


    public void facebookClicked(View v){
        String FACEBOOK_URL="https://www.facebook.com/"+fb_id;
        String urlToUse;

        PackageManager packageManager=getPackageManager();
        try{
            int versionCode=packageManager.getPackageInfo("com.facebook.katana",0).versionCode;
            if(versionCode>=3002850){
                urlToUse="fb://facewebmodal/f?href="+FACEBOOK_URL;
            }
            else{
                urlToUse="fb://page/"+fb_id;
            }
        }catch(PackageManager.NameNotFoundException e){
            urlToUse=FACEBOOK_URL;
        }

        Intent facebookIntent=new Intent(Intent.ACTION_VIEW);
        facebookIntent.setData(Uri.parse(urlToUse));
        startActivity(facebookIntent);

    }

    public void twitterClicked(View v){
        Intent intent=null;
        String name=twitter_id;
        try{
            getPackageManager().getPackageInfo("com.twitter.android",0);
            intent=new Intent(Intent.ACTION_VIEW,Uri.parse("twitter://user?screen_name="+name));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        } catch (Exception e) {
            intent=new Intent(Intent.ACTION_VIEW,Uri.parse("https://twitter.com/"+name));
        }
        startActivity(intent);

    }

    public void googlePlusClicked(View v){
        String name=google_id;
        Intent intent=null;
        try{
            intent=new Intent(Intent.ACTION_VIEW);
            intent.setClassName("com.google.android.apps.plus","com.google.android.apps.plus.phone.UrlGatewayActivity");
            intent.putExtra("customAppUri",name);
            startActivity(intent);
        }
        catch(ActivityNotFoundException e){
            startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("https://plus.google.com/"+name)));
        }
    }

    public void youTubeClicked(View v){
        String name=youtube_id;
        Intent intent=null;
        try{
            intent=new Intent(Intent.ACTION_VIEW);
            intent.setPackage("com.google.android.youtube");
            intent.setData(Uri.parse("https://www.youtube.com/"+name));
            startActivity(intent);
        }
        catch(ActivityNotFoundException e){
            startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("https://www.youtube.com/"+name)));
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
                        .into(officialPhotoIV);
            }
        }).build();

        picasso.load(str)
                .error(R.drawable.brokenimage)
                .placeholder(R.drawable.placeholder)
                .into(officialPhotoIV);
    }


    public void onClicked(View v){
        Intent intent=new Intent(PersonActivity.this,PhotoActivity.class);
        intent.putExtra("Location",zipTV.getText());
        intent.putExtra("Office",officeTV.getText());
        intent.putExtra("Official_Name",official_nameTV.getText());
        intent.putExtra("Photo_Official",retPhoto);
        intent.putExtra("Party",retParty);
        startActivity(intent);

    }

    public void callLinkify(){
        if(!retAddress.equals("")){
            Linkify.addLinks(((TextView)findViewById(R.id.address)),Linkify.MAP_ADDRESSES);
        }
        if(!retPhone.equals("")){
            Linkify.addLinks(((TextView) findViewById(R.id.phone)), Linkify.PHONE_NUMBERS);
        }
        if(!retEmail.equals("")){
            Linkify.addLinks(((TextView) findViewById(R.id.email)), Linkify.EMAIL_ADDRESSES);
        }
        if(!retWebsite.equals("")){
            Linkify.addLinks(((TextView) findViewById(R.id.website)), Linkify.WEB_URLS);
        }
    }

    public void callPartyColor(){
        if(retParty.equals("Democratic")||retParty.equals("Democrat")){
            scrollView.setBackgroundColor(view.getResources().getColor(R.color.DemocratColor));
            constraintLayout.setBackgroundColor(view.getResources().getColor(R.color.DemocratColor));
        }
        else if(retParty.equals("Republican")){
            scrollView.setBackgroundColor(view.getResources().getColor(R.color.RepublicColor));
            constraintLayout.setBackgroundColor(view.getResources().getColor(R.color.RepublicColor));

        }
    }

    public void callSocialLinks(){

        fb_id=intent.getStringExtra("Facebook");
        google_id=intent.getStringExtra("GooglePlus");
        twitter_id=intent.getStringExtra("Twitter");
        youtube_id=intent.getStringExtra("Youtube");
        if(google_id.equals("")){
            googlePlusIV.setVisibility(View.INVISIBLE);
        }
        if(fb_id.equals("")){
            facebookIV.setVisibility(View.INVISIBLE);
        }
        if(twitter_id.equals("")){
            twitterIV.setVisibility(View.INVISIBLE);
        }
        if(youtube_id.equals("")){
            youtubeIV.setVisibility(View.INVISIBLE);
        }
    }

    public void callGetIntent(){
        recZipcode=intent.getStringExtra("Zipcode");
        retPhoto=intent.getStringExtra("Photo");
        retParty=intent.getStringExtra("Party");
        retPhone=intent.getStringExtra("Phone");
        retAddress=intent.getStringExtra("Address");
        retEmail=intent.getStringExtra("Email");
        retWebsite=intent.getStringExtra("Website");
    }

    public void callSetText(){
        zipTV.setText(recZipcode);
        officeTV.setText(intent.getStringExtra("Office"));
        official_nameTV.setText(intent.getStringExtra("Official_Name"));
        partyTV.setText("("+retParty+")");
        if(!retAddress.equals("No Data Provided")) {
            spanAddress = new SpannableString(retAddress);
            spanAddress.setSpan(new UnderlineSpan(),0,retAddress.length(),0);
            addressTV.setText(spanAddress);
        }
        else {
            addressTV.setText(retAddress);
        }
        phoneTV.setText(retPhone);
        emailTV.setText(retEmail);
        websiteTV.setText(retWebsite);
    }

    public void initViews(){
        zipTV=(TextView)findViewById(R.id.official_zip);
        officeTV=(TextView)findViewById(R.id.official_office);
        official_nameTV=(TextView)findViewById(R.id.official_name);
        partyTV=(TextView)findViewById(R.id.party);
        addressTV=(TextView)findViewById(R.id.address);
        phoneTV=(TextView)findViewById(R.id.phone);
        emailTV=(TextView)findViewById(R.id.email);
        websiteTV=(TextView)findViewById(R.id.website);
        googlePlusIV=(ImageView)findViewById(R.id.google);
        facebookIV=(ImageView)findViewById(R.id.fb);
        twitterIV=(ImageView)findViewById(R.id.twitter);
        youtubeIV=(ImageView)findViewById(R.id.youtube);
        officialPhotoIV=(ImageView)findViewById(R.id.no_image);
        scrollView=(ScrollView)findViewById(R.id.scroller);
        constraintLayout=(ConstraintLayout) findViewById(R.id.constraint);
        view=scrollView.getRootView();
    }

    public void callMap(View v){
        String mapAddress=addressTV.getText().toString();
        if(!mapAddress.equals("No Data Provided")){
        Uri mapUri = Uri.parse("geo:0,0?q=" + Uri.encode(mapAddress));
        Intent intent = new Intent(Intent.ACTION_VIEW, mapUri);
        intent.setPackage("com.google.android.apps.maps");

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            makeErrorAlert("No Application found that handles ACTION_VIEW (geo) intents");
        }
        }

    }

    private void makeErrorAlert(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(msg);
        builder.setTitle("No App Found");

        AlertDialog dialog = builder.create();
        dialog.show();
    }

}