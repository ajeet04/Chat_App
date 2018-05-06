package com.example.ajeetyadav.chat;

/**
 * Created by Ajeet Yadav on 9/13/2017.
 */

public class mFriends {
    public String date;
   public  String thumb_image;
    public String name;
    public String type;
    public mFriends(){

    }
    public mFriends(String date,String name,String thumb_image,String type){
        this.date=date;
        this.name=name;
        this.thumb_image=thumb_image;
        this.type=type;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setName(String name) {
        this.name =name;
    }
    public String getName() {
        return name;
    }
    public void setThumb_image(String thumb_image) {
        this.thumb_image =thumb_image;
    }

    public String getThumb_image() {
        return thumb_image;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getType() {
        return type;
    }
}
