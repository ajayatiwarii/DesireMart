package com.example.ajaytiwari.parselogin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


/**
 * This fragment handles chat related UI which includes a list view for messages
 * and a message entry field with send button.
 */
public class Messages extends AppCompatActivity {
    public Button send;
    private TextView chatLine;
    private ListView listView;
    String senderId;
    ChatMessageAdapter adapter = null;
    List<ParseObject> profilesList;
    ChatMessageAdapter chatMessageAdapter;
    private List<String> items = new ArrayList<String>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        final Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        senderId = extras.getString("id");
        chatLine = (TextView) findViewById(R.id.txtChatLine);
        listView = (ListView) findViewById(android.R.id.list);
        final ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Message");
        query.whereEqualTo("recieverId", ParseUser.getCurrentUser().getObjectId());
        query.whereEqualTo("senderId", senderId);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, com.parse.ParseException e) {
                if (e == null) {
                    // The query was successful.
                    profilesList = objects;
                    chatMessageAdapter = new ChatMessageAdapter(getBaseContext(), android.R.id.text1,
                            profilesList);
                    listView.setAdapter(chatMessageAdapter);
                }

            }
        });
        send = (Button) findViewById(R.id.sendd);
        send.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        ParseObject parseObject = new ParseObject("Message");
                        parseObject.put("conversation", chatLine.getText().toString());
                        parseObject.put("senderId", ParseUser.getCurrentUser().getObjectId());
                        parseObject.put("recieverId", senderId);
                        parseObject.saveInBackground();
                        chatLine.setText("");
                        chatLine.clearFocus();
                    }
                });

    }

    public class ChatMessageAdapter extends ArrayAdapter<ParseObject> {
        List<ParseObject> messages = null;

        public ChatMessageAdapter(Context context, int textViewResourceId,
                                  List<ParseObject> items) {
            super(context, textViewResourceId, items);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater) getApplicationContext()
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(android.R.layout.simple_list_item_1, null);
            }
            String message = profilesList.get(position).getString("conversation");
            if (message != null && !message.isEmpty()) {
                TextView nameText = (TextView) v
                        .findViewById(android.R.id.text1);
                if (nameText != null) {
                    nameText.setText(message);
                    //if (message.startsWith("Me: ")) {
                    nameText.setTextAppearance(getContext(),
                            R.style.normalText);
                    ViewGroup.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
                    nameText.setLayoutParams(params);

                    nameText.setBackgroundResource(R.drawable.speech_bubble_orange);
                    nameText.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                }
            }
            return v;
        }
    }
}

