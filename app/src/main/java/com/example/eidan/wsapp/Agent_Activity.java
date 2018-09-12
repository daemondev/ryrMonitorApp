package com.example.eidan.wsapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import org.java_websocket.client.WebSocketClient;

import java.util.ArrayList;
import java.util.List;

public class Agent_Activity extends AppCompatActivity {

    public static TextView tv_callerID; //, tv_callRank, tv_callType, tv_phone;
//    ImageView imgv_status;
//    WebView wb;

    LineChart mChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_);

//        tv_callType = (TextView) findViewById(R.id.txt_agent_calltype);
//        tv_phone = (TextView) findViewById(R.id.txt_agent_exten);
        tv_callerID = (TextView) findViewById(R.id.txt_agent_callerid);
//        tv_callRank = (TextView) findViewById(R.id.txt_agent_callrank);
//        imgv_status = (ImageView) findViewById(R.id.img_agent_state);

        /*
        <WebView
            android:id="@+id/wb"
            android:layout_width="match_parent"
            android:layout_height="match_parent">
        </WebView>

        wb = (WebView) findViewById(R.id.wb);
        String html = "<table><tr><td>ID</td><td>NOMBRE</td><td>DIRECCIÓN</td><td>TELÉFONO</td></tr><tr><td>19000</td><td>Richar</td><td>SJM Pamplona</td><td>123456789</td></tr></table>";
        wb.loadDataWithBaseURL(null, html,"text/html","utf-8",null);
        */

//        sun.net.www.http.HttpClient client = new sun.net.www.http.HttpClient();
        final SortableAgentTableView tblAgent =  (SortableAgentTableView) findViewById(R.id.tbl);
        List<Agent> l = new ArrayList<>();
        l.add(new Agent("MoBiles", "987456321", R.drawable.call,1900));
        l.add(new Agent("MoBiles", "987456321", R.drawable.call,1900));
        l.add(new Agent("MoBiles", "987456321", R.drawable.call,1900));
        l.add(new Agent("MoBiles", "987456321", R.drawable.call,1900));

//        tblAgent.setHeaderAdapter(new SimpleTableHeaderAdapter(this,"CALLTYPE", "EXTEN", "STATUS","CALLERID"));
        final AgentTableDataAdapter da = new AgentTableDataAdapter(this, l, tblAgent);
        tblAgent.setDataAdapter(da);

        paintChart();

        Intent intent = getIntent();
//        String _callType = intent.getExtras().getString("calltype");
//        String _phone = intent.getExtras().getString("exten");
//        String _callRank = intent.getExtras().getString("callrank");
        int _callerID = intent.getExtras().getInt("callerid");
//        int _status = intent.getExtras().getInt("state");

//        tv_callType.setText(_callType);
//        tv_phone.setText(_phone);
//        tv_callRank.setText(_callRank);
        tv_callerID.setText(""+_callerID);
        Bundle extras = intent.getExtras();
//        WebSocketClient mWSClient = extras.get(MainActivity.wsClient);
        WebSocketClient mWSClient = MainActivity.wsClient;
        String message = "{\"event\":\"getChartData\",\"data\":"+_callerID+"}";

        mWSClient.send(message);
        mWSClient.onMessage("xxx");

//        imgv_status.setImageResource(_status);

    }

    public void triggerPaint(){
        paintChart();
    }

    public void paintChart(){
//        **********************************************************
        //chart = new LineChart(this);
        List<Entry> entries = new ArrayList<Entry>();
//
        for(int i=0;i< 5;i++){
            entries.add(new Entry(5, 6));
        }

        LineDataSet ds = new LineDataSet(entries,"Label");

        LineData data = new LineData(ds);
        mChart = (LineChart) findViewById(R.id.theChart);
        mChart.setData(data);
        mChart.invalidate();
    }
}
