package com.example.admin.knowyourgovernment;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {
    private ArrayList<Official> officialArrayList = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerAdapter mAdapter;
    private static final String TAG = "MyTag";
    private String currZipCode;
    private boolean locatorSet=false;
    List<Address> addressList=new ArrayList<>();

    private Locator locator;

    private CivicInfoDownloader civicInfoDownloader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        officialArrayList.add(new Official("President Of The United States", "Donald J.Trump", "Republican"));
//        officialArrayList.add(new Official("Vice-President Of The United States", "Mike Pence", "Republican"));
//        officialArrayList.add(new Official("United States Senate", "Tammy Duckworth", "Democratic"));
//        officialArrayList.add(new Official("United States Senate", "Richard J.Durbin", "Democratic"));
//        officialArrayList.add(new Official("Governor", "Bruce Rauner", "Republican"));
//        officialArrayList.add(new Official("Dummy Governor", "Dummy Bruce Rauner", "Republican"));

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
//        mAdapter = new RecyclerAdapter(officialArrayList, this);
//        recyclerView.setAdapter(mAdapter);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        locator=new Locator(this);

        boolean checkNtwk=checkNetwork();
        if(checkNtwk) {
            mAdapter = new RecyclerAdapter(officialArrayList, this);
            recyclerView.setAdapter(mAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            locator=new Locator(this);
            locatorSet=true;
        }
        else{
            noNetwork();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem){

        switch(menuItem.getItemId()) {
            case R.id.search:
                searchDialog();
                return true;
            case R.id.about:
                callAbout();
                return true;


            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }



    public void callAbout(){
        Intent intentA=new Intent(MainActivity.this,AboutActivity.class);
        startActivity(intentA);
    }

    public void searchDialog(){

        boolean checkNtwk=checkNetwork();
        if(!checkNtwk) {
            noNetwork();
        }
        else {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            final EditText editText = new EditText(this);
            editText.setGravity(Gravity.CENTER);
            builder.setView(editText);
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                    CivicInfoDownloader civicInfoDownloader = new CivicInfoDownloader(MainActivity.this);
                    civicInfoDownloader.execute(editText.getText().toString());


                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });
            builder.setMessage("Enter a City,State or a Zip Code");
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    @Override
    public void onClick(View v) {
        int pos = recyclerView.getChildLayoutPosition(v);
        Official official=officialArrayList.get(pos);
//        Toast.makeText(v.getContext(),"Short Click:"+official.getName(),Toast.LENGTH_SHORT).show();
        Intent intentOfficial=new Intent(MainActivity.this,PersonActivity.class);
        TextView zipTV=(TextView) findViewById(R.id.zip);
        intentOfficial.putExtra("Zipcode",zipTV.getText());
        intentOfficial.putExtra("Office",official.getOffice());
        intentOfficial.putExtra("Official_Name",official.getName());
        intentOfficial.putExtra("Party",official.getParty());
        intentOfficial.putExtra("Address",official.getAddress());
        intentOfficial.putExtra("Phone",official.getPhone());
        intentOfficial.putExtra("Email",official.getEmail());
        intentOfficial.putExtra("Website",official.getWebsite());
        intentOfficial.putExtra("GooglePlus",official.getGooglePlus());
        intentOfficial.putExtra("Facebook",official.getFacebook());
        intentOfficial.putExtra("Twitter",official.getTwitter());
        intentOfficial.putExtra("Youtube",official.getYoutube());
        intentOfficial.putExtra("Photo",official.getPhotoUrl());
        startActivity(intentOfficial);
    }

    @Override
    public boolean onLongClick(View v) {
        int pos = recyclerView.getChildLayoutPosition(v);
        Official official=officialArrayList.get(pos);
        Toast.makeText(v.getContext(),"Long Click:"+official.getName(),Toast.LENGTH_SHORT).show();
        return false;
    }


    //Location Functions

    public void setData(double lat, double lon) {
        //Log.d(TAG,"setData called.");
        String[] zip = doAddress(lat, lon);
        //Toast.makeText(this,"Zipcode:"+zip[1],Toast.LENGTH_SHORT).show();
        currZipCode=zip[1];
        //((TextView) findViewById(R.id.zip)).setText(zip[0]);

        //CALL CIVICINFODOWNLOADER WITH ZIPCODE DATA
        CivicInfoDownloader civicInfoDownloader=new CivicInfoDownloader(this);
        civicInfoDownloader.execute(currZipCode);
    }

    private String[] doAddress(double latitude, double longitude) {

        //Log.d(TAG, "doAddress: Lat: " + latitude + ", Lon: " + longitude);


        List<Address> addresses = null;
        for (int times = 0; times < 2; times++) {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            try {
                //Log.d(TAG, "doAddress: Getting address now");


                addresses = geocoder.getFromLocation(latitude, longitude, 1);
                //Log.d(TAG,"Address:"+addresses);
                String retStr[]=new String[2];
                retStr[0]=addresses.get(0).getAddressLine(1);
                retStr[1]=addresses.get(0).getPostalCode();

//                for (Address ad : addresses) {
//                    //Log.d(TAG, "doLocation: " + ad);
//
//                    sb.append("\nAddress\n\n");
//                    for (int i = 0; i < ad.getMaxAddressLineIndex(); i++) {
//                        //Log.d(TAG,"Address Line "+i+":"+ad.getAddressLine(i));
//                        sb.append("\t" + ad.getAddressLine(i) + "\n");
//                    }
//                   // sb.append("\t" + ad.getCountryName() + " (" + ad.getCountryCode() + ")\n");
//
//                }

                return retStr;
            } catch (IOException e) {
                //Log.d(TAG, "doAddress: " + e.getMessage());

            }
            Toast.makeText(this, "GeoCoder service is slow - please wait", Toast.LENGTH_SHORT).show();
        }
            Toast.makeText(this, "GeoCoder service timed out - please try again", Toast.LENGTH_LONG).show();
            return null;
    }

    public void noLocationAvailable() {
        Toast.makeText(this, "No location providers were available", Toast.LENGTH_LONG).show();
        recyclerView.setBackgroundColor(this.getResources().getColor(R.color.NoInternet));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        TextView tv=(TextView) findViewById(R.id.zip);
        tv.setText("No Data For Location");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //Log.d(TAG, "onRequestPermissionsResult: CALL: " + permissions.length);
        //Log.d(TAG, "onRequestPermissionsResult: PERM RESULT RECEIVED");

        if (requestCode == 5) {
            //Log.d(TAG, "onRequestPermissionsResult: permissions.length: " + permissions.length);
            for (int i = 0; i < permissions.length; i++) {
                if (permissions[i].equals(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        //Log.d(TAG, "onRequestPermissionsResult: HAS PERM");
                        locator.setUpLocationManager();
                        locator.determineLocation();
                    } else {
                        Toast.makeText(this, "Location permission was denied - cannot determine address", Toast.LENGTH_LONG).show();
                        //Log.d(TAG, "onRequestPermissionsResult: NO PERM");
                    }
                }
            }
        }
        //Log.d(TAG, "onRequestPermissionsResult: Exiting onRequestPermissionsResult");
    }


    @Override
    protected void onDestroy() {
        if(locatorSet){
        locator.shutdown();
        }
        super.onDestroy();
    }

    public void noData(String str){
        Toast.makeText(this,"No data found for address:"+str,Toast.LENGTH_SHORT).show();
    }
    public void civicUnavailable(){
        Toast.makeText(this,"Civic data unavailable",Toast.LENGTH_SHORT).show();
    }



    public void setOfficialList(Object[] objArr){
        String locStr=objArr[0].toString();
        officialArrayList.clear();
        mAdapter.notifyDataSetChanged();
        TextView zipTV=(TextView)findViewById(R.id.zip);
        if(locStr.equals("")){
            zipTV.setText("No data for location");
        }
        else{
            zipTV.setText(locStr);
            officialArrayList.addAll((ArrayList<Official>) objArr[1]);
            mAdapter = new RecyclerAdapter(officialArrayList, this);
            recyclerView.setAdapter(mAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            mAdapter.notifyDataSetChanged();

        }

    }

    public boolean checkNetwork(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()){
            return true;
        }
        else
            return false;
    }

    public void noNetwork(){

        recyclerView.setBackgroundColor(this.getResources().getColor(R.color.NoInternet));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        TextView tv=(TextView) findViewById(R.id.zip);
        tv.setText("No Data For Location");
        if(!officialArrayList.isEmpty()) {
            officialArrayList.clear();
            mAdapter.notifyDataSetChanged();
        }
        noInternetDialog();

    }


    public void noInternetDialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setMessage("Data cannot be accessed/loaded without an internet connection");
        builder.setTitle("No Network Connection");
        builder.create();
        builder.show();

    }

    @Override
    protected void onPause(){
        super.onPause();
    }


    @Override
    protected void onResume(){
        super.onResume();
    }

}
