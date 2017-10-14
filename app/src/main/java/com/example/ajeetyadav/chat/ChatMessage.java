package com.example.ajeetyadav.chat;

/**
 * Created by Ajeet Yadav on 9/16/2017.
 */
public class ChatMessage {
    public boolean left;
    public String message;
    public long time;


    public ChatMessage(boolean left, String message, long time) {
        super();
        this.left = left;
        this.message = message;
        this.time = time;
    }
}