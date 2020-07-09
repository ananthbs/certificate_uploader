package com.example.authentiation;

import com.google.firebase.database.Exclude;

public class foruploadpress {
    private String mname;
    private String mimageurl;
    private String mkey;

    public foruploadpress(){

    }

    public foruploadpress(String name,String imageurl){
       if(name.trim().equals("")){
           name= "no name";
       }
        mname= name;
        mimageurl = imageurl;
    }

    public String getname(){
        return mname;
    }

    public void setname(String name){
        mname=name;
    }

    public String getimageurl(){
        return mimageurl;
    }

    public void setimageurl(String imageurl){
         mimageurl = imageurl;
    }

    @Exclude
    public  String getkey(){
        return mkey;
    }
    @Exclude
    public void setkey(String key){
        mkey=key;
    }
}
