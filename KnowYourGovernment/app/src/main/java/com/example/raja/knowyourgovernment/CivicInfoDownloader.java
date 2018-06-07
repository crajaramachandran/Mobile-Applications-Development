package com.example.raja.knowyourgovernment;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class CivicInfoDownloader extends AsyncTask<String,Void,String> {
    private String API_KEY="AIzaSyC0sTVcrv8d0SFelUPWmzGtiDDy0PR7kAM";
    private String apiURL="https://www.googleapis.com/civicinfo/v2/representatives";
    private String str="";
    private String TAG="CivicTAG";
    private MainActivity mainActivity;
    private String address_passed="";

    public CivicInfoDownloader(MainActivity ma){
        mainActivity=ma;
    }

    @Override
    protected void onPostExecute(String retString){

        if(retString.equals("")){
            mainActivity.civicUnavailable();
        }

        try {
            JSONObject obj=new JSONObject(str);

            String error=obj.optString("error");
            if(!error.equals(""))
            {
                mainActivity.noData(address_passed);
            }
            ArrayList<Official>  officialArrayList=new ArrayList<>();
            String location="";

            //Get the three JSON sections
            String normalizedStr=obj.getString("normalizedInput");
            String offices=obj.getString("offices");
            String officials=obj.getString("officials");
            JSONObject normalizedObj=new JSONObject(normalizedStr);
            JSONArray officeArr=new JSONArray(offices);
            JSONArray officialsArr=new JSONArray(officials);
            String official_name="";
            String party="";

            //Location field
            String cityStr=normalizedObj.optString("city");
            String stateStr=normalizedObj.optString("state");
            String zipStr=normalizedObj.optString("zip");
            if(cityStr.equals("")&&stateStr.equals("")){
                location=zipStr;
            }
            else if(cityStr.equals("")&&zipStr.equals("")){
                location=stateStr;
            }
            else if(stateStr.equals("")&&zipStr.equals("")){
                location=cityStr;
            }
            else if(cityStr.equals("")){
                location=stateStr+" "+zipStr;
            }
            else if(stateStr.equals("")){
                location=cityStr+","+zipStr;
            }
            else if(zipStr.equals("")){
                location=cityStr+","+stateStr;
            }
            else{
                location=cityStr+","+stateStr+" "+zipStr;
            }

            //offices
            int count=officeArr.length();
            Log.d(TAG,"count:"+count);
            //1.office name
            for(int i=0;i<count;i++){
                JSONObject arrObj=(JSONObject)officeArr.get(i);
                String office_name=arrObj.getString("name");
                String officialIndicesStr=arrObj.getString("officialIndices");
                JSONArray indicesArr=new JSONArray(officialIndicesStr);
                int indicesCnt=indicesArr.length();

                //2.official details
                for(int j=0;j<indicesCnt;j++){
                    int index=indicesArr.getInt(j);
                    JSONObject officialObj=(JSONObject)officialsArr.get(index);
                    //name and party
                    official_name=officialObj.getString("name");
                    party=officialObj.optString("party");
                    if(party.equals("")||party.equals(null)){
                        party="Unknown";
                    }
                    //address
                    String addressStr=officialObj.optString("address");
                    String retAddr="";
                    if(addressStr.equals(""))
                    {
                        retAddr="No Data Provided";
                    }
                    else {
                        JSONArray addressArr = new JSONArray(addressStr);
                        int addressCount = addressArr.length();
                        JSONObject addrObj = (JSONObject) addressArr.get(0);
                        String line="";
                        String line1=addrObj.optString("line1");
                        String line2=addrObj.optString("line2");
                        String line3=addrObj.optString("line3");
                        if(line2.equals("")&&line3.equals("")){
                            line=line1+","+"\n";
                        }
                        else if(line3.equals("")){
                            line=line1+","+line2+","+"\n";
                        }
                        String city = addrObj.optString("city");
                        String state = addrObj.optString("state");
                        String zip = addrObj.optString("zip");
                        retAddr = line + city + "," + state + " " + zip;
                    }

                    //Phone
                    String phoneStr=officialObj.optString("phones");
                    String retPhoneStr="";
                    if(phoneStr.equals("")){
                        retPhoneStr="No Data Provided";
                    }
                    else{
                        JSONArray phoneArr=new JSONArray(phoneStr);
                        retPhoneStr=(String) phoneArr.get(0);
                    }

                    //Email
                    String emailStr=officialObj.optString("emails");
                    String retEmailStr="";
                    if(emailStr.equals("")){
                        retEmailStr="No Data Provided";
                    }
                    else{
                        JSONArray emailArr=new JSONArray(emailStr);
                        retEmailStr=(String) emailArr.get(0);
                    }

                    //Website
                    String webStr=officialObj.optString("urls");
                    String retWebStr="";
                    if(webStr.equals("")){
                        retWebStr="No Data Provided";
                    }
                    else{
                        JSONArray webArr=new JSONArray(webStr);
                        retWebStr=(String) webArr.get(0);
                    }
                    String retYoutube="";
                    String retTwitter="";
                    String retFacebook="";
                    String retGooglePlus="";
                    //channels
                    String channels=officialObj.optString("channels");
                    if(!channels.equals("")){
                        JSONArray channelsArr=new JSONArray(channels);
                        int channelCount=channelsArr.length();
                        for(int ch=0;ch<channelCount;ch++){
                            JSONObject channelObj=(JSONObject) channelsArr.get(ch);
                            String type=channelObj.optString("type");
                            String id=channelObj.optString("id");
                            if(type.equals("GooglePlus")){
                                retGooglePlus=id;
                            }
                            else if(type.equals("Facebook")){
                                retFacebook=id;
                            }
                            else if(type.equals("Twitter")){
                                retTwitter=id;
                            }
                            else if(type.equals("YouTube")){
                                retYoutube=id;
                            }
                        }


                    }
                    //photo
                    String retPhotoStr=officialObj.optString("photoUrl");
                    officialArrayList.add(new Official(office_name,official_name,party,retAddr,retPhoneStr,retEmailStr,retWebStr,retGooglePlus,retFacebook,retTwitter,retYoutube,retPhotoStr));



                }

            }
            Object[] retArr=new Object[2];
            retArr[0]=location;
            retArr[1]=officialArrayList;

            mainActivity.setOfficialList(retArr);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected String doInBackground(String... params) {
        Log.d(TAG,"One");
        address_passed=params[0];
        String urlToUse="";
        Uri.Builder buildURL=Uri.parse(apiURL).buildUpon();
        buildURL.appendQueryParameter("key", API_KEY);
        buildURL.appendQueryParameter("address", address_passed);
        urlToUse = buildURL.build().toString();

        StringBuilder sb = new StringBuilder();
        try{
            Log.d(TAG,"Two");
            URL url = new URL(urlToUse);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader((new InputStreamReader(is)));
            Log.d(TAG,"Three");
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }

        }
        catch (Exception e) {
            e.printStackTrace();

        }
        str=sb.toString();



        return str;
    }

}
