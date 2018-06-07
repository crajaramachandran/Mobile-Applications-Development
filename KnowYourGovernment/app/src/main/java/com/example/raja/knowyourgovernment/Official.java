package com.example.raja.knowyourgovernment;

/**
 * Created by admin on 3/26/2017.
 */

public class Official {

    private String office;
    private String name;
    private String party;
    private String address;
    private String phone;
    private String email;
    private String website;
    private String googlePlus;
    private String facebook;
    private String twitter;
    private String youtube;
    private String photoUrl;

    public Official(String office,String name,String party,String address,String phone,String email,String website,String googlePlus,String facebook,String twitter,String youtube,String photoUrl){
        this.office=office;
        this.name=name;
        this.party=party;
        this.address=address;
        this.phone=phone;
        this.email=email;
        this.website=website;
        this.googlePlus=googlePlus;
        this.facebook=facebook;
        this.twitter=twitter;
        this.youtube=youtube;
        this.photoUrl=photoUrl;

    }

    public String getOffice(){
        return office;
    }

    public String getName(){
        return name;
    }

    public String getParty(){
        return party;
    }

    public String getAddress(){
        return address;
    }

    public String getPhone(){
        return phone;
    }

    public String getEmail(){
        return email;
    }

    public String getWebsite(){
        return website;
    }

    public String getGooglePlus(){
        return googlePlus;
    }

    public String getFacebook(){
        return facebook;
    }

    public String getTwitter(){
        return twitter;
    }

    public String getYoutube(){
        return youtube;
    }

    public String getPhotoUrl(){
        return photoUrl;
    }


    @Override
    public String toString(){
        return office+" "+name+" "+party+" "+address+" "+phone+" "+email+" "+website+" "+googlePlus+" "+facebook+" "+twitter+" "+youtube+" "+photoUrl;
    }
}
