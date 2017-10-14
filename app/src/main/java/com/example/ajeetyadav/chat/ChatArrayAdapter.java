package com.example.ajeetyadav.chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ajeet Yadav on 9/16/2017.
 */

class ChatArrayAdapter extends ArrayAdapter<ChatMessage> {

private TextView chatText,stime;
public List<ChatMessage> chatMessageList = new ArrayList<ChatMessage>();
private Context context;

@Override
public void add(ChatMessage object) {
        chatMessageList.add(object);
        super.add(object);
        }

public ChatArrayAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        this.context = context;
        }

public int getCount() {
        return this.chatMessageList.size();
        }

public ChatMessage getItem(int index) {
        return this.chatMessageList.get(index);
        }

public View getView(int position, View convertView, ViewGroup parent) {
        ChatMessage chatMessageObj = getItem(position);
        SimpleDateFormat sfd = new SimpleDateFormat("HH:mm a");
        String mTime=sfd.format(new Date(chatMessageObj.time));

        View row = convertView;
        LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (chatMessageObj.left) {
        row = inflater.inflate(R.layout.right, parent, false);
        }else{
        row = inflater.inflate(R.layout.left, parent, false);
        }
        chatText = (TextView) row.findViewById(R.id.msgr);
        // chatText.setText(chatMessageObj.message+" "+mTime);
        chatText.setText(chatMessageObj.message);

        //TextView sTime=(TextView)row.findViewById(R.id.setmssgTime);
        //sTime.setText(mTime);
        return row;
        }
        }