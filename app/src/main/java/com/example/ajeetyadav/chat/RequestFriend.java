package com.example.ajeetyadav.chat;

/**
 * Created by Ajeet Yadav on 9/16/2017.
 */

public class RequestFriend
{
    public String request_type;
    public String to;
    public String from;


    public String getRequest_type() {
        return request_type;
    }
    public void setRequest_type(String type){
        this.request_type=request_type;
    }


    public RequestFriend(){}
    public RequestFriend(String request_type,String to,String from)
    {


        this.to=to;
        this.from=from;
        this.request_type=request_type;
    }



    public void setTo(String to) {
        this.to =to;
    }
    public String getTo() {
        return to;
    }
    public void setFrom(String from) {
        this.from =from;
    }

    public String getFrom() {
        return from;
    }
}


