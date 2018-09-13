package com.example.eidan.wsapp;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.EntryXComparator;

import org.java_websocket.client.WebSocketClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Agent_Activity extends AppCompatActivity /*implements Runnable*/ {

    public static TextView tv_callerID; //, tv_callRank, tv_callType, tv_phone;
//    ImageView imgv_status;
//    WebView wb;

    LineChart mLineChart; PieChart mPieChart;
    public static Thread thread;
    public static JSONArray jsonArray;

    AgentTableDataAdapter da;
    SortableAgentTableView tblAgent;
    List<Agent> l;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_);

        tv_callerID = (TextView) findViewById(R.id.txt_agent_callerid);

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

        tblAgent = (SortableAgentTableView) findViewById(R.id.tblAgentCalls);
        l = new ArrayList<>();

//        l.add(new Agent("MoBiles", "987456321", R.drawable.call, 1900));
//        l.add(new Agent("MoBiles", "987456321", R.drawable.call, 1900));
//        l.add(new Agent("MoBiles", "987456321", R.drawable.call, 1900));
//        l.add(new Agent("MoBiles", "987456321", R.drawable.call, 1900));

//                tblAgent.setHeaderAdapter(new SimpleTableHeaderAdapter(this,"CALLTYPE", "EXTEN", "STATUS","CALLERID"));
        da = new AgentTableDataAdapter(this, l, tblAgent);
        tblAgent.setDataAdapter(da);

        Intent intent = getIntent();

        int _callerID = intent.getExtras().getInt("callerid");
        tv_callerID.setText(""+_callerID);
        Bundle extras = intent.getExtras();

        WebSocketClient mWSClient = MainActivity.wsClient;
        String message = "{\"event\":\"getChartData\",\"data\":"+_callerID+"}";

        mWSClient.send(message); //mWSClient.onMessage("xxx");

        thread = new Thread(){

            @Override
            public void run(){

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        paintLineChart();
                        paintPieChart();
                        paintDataTable();
                        thread.interrupt();
                    }
                });

            }

        };
    }

    public void paintDataTable(){
        if(jsonArray.length()>0) {
//            tblAgent = (SortableAgentTableView) findViewById(R.id.tblAgentCalls);
//            l = new ArrayList<>();
            try{
                JSONArray jsonTableData = (JSONArray) jsonArray.get(1);
                JSONObject item;
                for(int i=0;i<jsonTableData.length();i++){
                    item = (JSONObject) jsonTableData.get(i);
                    l.add(new Agent("MoBiles", "987456321", R.drawable.call, 1900));
                }
            }catch (JSONException e){
                e.printStackTrace();
            }


//            l.add(new Agent("MoBiles", "987456321", R.drawable.call, 1900));
//            l.add(new Agent("MoBiles", "987456321", R.drawable.call, 1900));
//            l.add(new Agent("MoBiles", "987456321", R.drawable.call, 1900));
//            l.add(new Agent("MoBiles", "987456321", R.drawable.call, 1900));
//
//            //<<<<<<<<        tblAgent.setHeaderAdapter(new SimpleTableHeaderAdapter(this,"CALLTYPE", "EXTEN", "STATUS","CALLERID"));
//            da = new AgentTableDataAdapter(this, l, tblAgent);
            da.notifyDataSetChanged();
//            tblAgent.setDataAdapter(da);
        }
    }


    public void paintPieChart(){
        if(jsonArray.length()>0){
            mPieChart = findViewById(R.id.thePieChart);
            mPieChart.setUsePercentValues(true);
            mPieChart.getDescription().setEnabled(false);
            mPieChart.setExtraOffsets(5, 10, 5, 5);

            mPieChart.setDragDecelerationFrictionCoef(0.99f);
            mPieChart.setDrawHoleEnabled(true);
            mPieChart.setHoleColor(Color.WHITE);
            mPieChart.setTransparentCircleRadius(60f);

            ArrayList<PieEntry> yValues = new ArrayList<>();
            try{
                JSONArray jsonChartData = (JSONArray) jsonArray.get(0);
                Log.i("\n\n>>> PIECHAR\n", String.format("PIECHART-DATA %s\n\n", jsonChartData.toString()));
                JSONObject item;
                for(int i=0; i< jsonChartData.length();i++){
                    item = (JSONObject) jsonChartData.get(i);
                    Log.i("PIE-ENTRY: ", String.format("\n>>>>> %s -> %d ---- %s",item.getString("state"), item.getInt("Q.CALLS"), item.toString()));
                    yValues.add(new PieEntry(item.getInt("Q.CALLS"), item.getString("state")));
                }
            }catch (JSONException e){
                e.printStackTrace();
                Log.i("\n\n>>> PIECHARt ERROR:\n", String.format("PIECHART-DATA ERROR -> %s\n\n", e.getMessage()));
            }

            Description description = new Description();
            description.setText("This is the CALL-STATUS stats");
            description.setTextSize(15);
            mPieChart.setDescription(description);
//            mPieChart.animateY(1000, Easing.EasingOption.EaseInOutCubic);

            PieDataSet pieDataSet = new PieDataSet(yValues, "STATE");
            pieDataSet.setSliceSpace(3f);
            pieDataSet.setSelectionShift(5f);
            pieDataSet.setColors(ColorTemplate.JOYFUL_COLORS);

            PieData pieData = new PieData((pieDataSet));
            pieData.setValueTextSize(10f);
            pieData.setValueTextColor(Color.YELLOW);
            mPieChart.setData(pieData);
            mPieChart.invalidate();
        }

    }

    public void paintLineChart(){
        if(jsonArray.length()>0){
            //chart = new LineChart(this);
            List<Entry> yValues = new ArrayList<>();
            Log.i("\n\nENTRY\n\n", String.format(">>>>> >>>>>>>>>\n"));
            try {

                JSONArray jsonChartData = (JSONArray) jsonArray.get(2);

                JSONObject item;
                for (int i=0; i<jsonChartData.length();i++){
                    item = (JSONObject) jsonChartData.get(i);
                    Log.i("ENTRY: ", String.format(">>>>> %s\n", item.toString()));
                    yValues.add(new Entry(item.getInt("hour"), item.getInt("qtyCalls") ));
                }
            }catch (JSONException e){
                e.printStackTrace();
            }


            Collections.sort(yValues, new EntryXComparator());
            LineDataSet set1 = new LineDataSet(yValues,"DataSet 1");
            set1.setFillAlpha(110);
            set1.setColor(Color.RED);
            set1.setLineWidth(3f);
            set1.setValueTextSize(10f);

            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);


            LineData data = new LineData(dataSets);
    //        data.notifyDataChanged();
            mLineChart = (LineChart) findViewById(R.id.theLineChart);

            mLineChart.setScaleEnabled(false);
            mLineChart.setDragEnabled(false);


            mLineChart.setData(data);
            mLineChart.invalidate();
        }else{
            Log.i("\n\nERROR:\n\n", String.format("\n\n>>>>>>>>>>>ARRAY IS EMPTY\n\n"));
        }
    }

//    @Override
//    public void run() {
//
//    }
}
