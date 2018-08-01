package com.example.eidan.wsapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class Agent_Activity extends AppCompatActivity {

    TextView callType, phone, callerID;
    ImageView status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_);

        callType = (TextView) findViewById(R.id.txt_agent_calltype);
        phone = (TextView) findViewById(R.id.txt_agent_exten);
        callerID = (TextView) findViewById(R.id.txt_agent_callerid);
        status = (ImageView) findViewById(R.id.img_agent_state);

        Intent intent = getIntent();
        String _callType = intent.getExtras().getString("calltype");
        String _phone = intent.getExtras().getString("exten");
        String _callerID = intent.getExtras().getString("callerid");
        int _status = intent.getExtras().getInt("state");

        callType.setText(_callType);
        phone.setText(_phone);
        callerID.setText(_callerID);
        status.setImageResource(_status);

    }
}
