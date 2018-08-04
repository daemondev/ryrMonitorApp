package com.example.eidan.wsapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class Agent_Activity extends AppCompatActivity {

    TextView tv_callType, tv_phone, tv_callerID, tv_callRank;
    ImageView imgv_status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_);

        tv_callType = (TextView) findViewById(R.id.txt_agent_calltype);
        tv_phone = (TextView) findViewById(R.id.txt_agent_exten);
        tv_callerID = (TextView) findViewById(R.id.txt_agent_callerid);
        tv_callRank = (TextView) findViewById(R.id.txt_agent_callrank);
        imgv_status = (ImageView) findViewById(R.id.img_agent_state);

        Intent intent = getIntent();
        String _callType = intent.getExtras().getString("calltype");
        String _phone = intent.getExtras().getString("exten");
        String _callRank = intent.getExtras().getString("callrank");
        int _callerID = intent.getExtras().getInt("callerid");
        int _status = intent.getExtras().getInt("state");

        tv_callType.setText(_callType);
        tv_phone.setText(_phone);
        tv_callRank.setText(_callRank);
        tv_callerID.setText(""+_callerID);
        imgv_status.setImageResource(_status);

    }
}
