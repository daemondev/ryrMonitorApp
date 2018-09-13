package com.example.eidan.wsapp;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.util.Log;
import android.os.Build;

import android.support.v7.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import android.provider.Settings.Secure;

public class MainActivity extends AppCompatActivity {
    public static WebSocketClient wsClient;
    EditText txtMessage;
    TextView txtHistory;
    List<Agent> lstAgent;
    String deviceID;
    RecyclerView myrv;
    RecyclerViewAdapter myAdapter;
    TextView tv_qty;

    Bundle b;

//    SharedPreferences preferences;
//    SharedPreferences.Editor editor;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        preferences = getSharedPreferences("dd_monitor", Context.MODE_PRIVATE);
//        editor = preferences.edit();

//        preferences = preferences = PreferenceManager.getDefaultSharedPreferences(this);;

        deviceID = Secure.getString(getApplicationContext().getContentResolver(), Secure.ANDROID_ID);
        tv_qty = findViewById(R.id.tv_qty);
        b = new Bundle();
        b.putInt("TALKING",R.drawable.call);
        b.putInt("IDLE",R.drawable.hangup);
        b.putInt("RINGING",R.drawable.ring2);

        lstAgent = new ArrayList<>();

        myrv = findViewById(R.id.recyclerview_agent_id);
        myAdapter = new RecyclerViewAdapter(this, lstAgent);
        myrv.setLayoutManager(new GridLayoutManager(this, 5));
//        myrv.setLayoutManager(new GridLayoutManager(this, Utility.calculateNoOfColumns(getApplicationContext())));
//        myrv.setLayoutManager(new GridLayoutManager(this, ColumnQty(this, R.id.recyclerview_agent_id)));
        myrv.setAdapter(myAdapter);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        cnxWebSocket();
//        txtMessage.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
//                boolean handled = false;
//                if(i == KeyEvent.KEYCODE_ENTER){
//                    //sendMessages();
//                    //txtHistory.setText(txtMessage.getText().toString() + "\n" + txtHistory.getText().toString());
//                    txtHistory.setText(String.format("%s %s %s",txtMessage.getText().toString(),"\n",txtHistory.getText().toString()));
//                    txtMessage.setText("");
//                    handled = true;
//
//                }
//                return handled;
//            }
//        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Update List", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });
    }

    Map<Integer,Integer> mmap = new HashMap<Integer, Integer>();

    void addItem(List<Agent> agents){
        for (Agent agent: agents){
            lstAgent.add(agent);
            mmap.put(agent.getCallerid(),lstAgent.indexOf(agent));
        }
        myAdapter.notifyDataSetChanged();
        tv_qty.setText("Total Agents: ["+lstAgent.size()+"]");
//        myAdapter.notifyItemInserted(lstAgent.size() - 1);
    }

    void updateState(List<Agent> agent){
        try {
            int index = mmap.get(agent.get(0).getCallerid());
            lstAgent.set(index, agent.get(0));
            myAdapter.notifyItemChanged(index);
        }catch (Exception e){
            addItem(agent);
        }
    }
    ///*
    private void cnxWebSocket(){
        URI uri;
        try {
            //uri = new URI("ws://192.168.3.107:8000/ws"); //URL DEV LOCALHOST
            uri = new URI("ws://ryr.progr.am/ws"); //URL PRODUCTION
        }catch (URISyntaxException e){
            e.printStackTrace();
            return;
        }

        wsClient = new WebSocketClient(uri) {
            @Override
            public void onOpen(ServerHandshake serverHandshake){
                Log.i("WebSocket", "Opened");
                //wsClient.send("Hello from " + Build.MANUFACTURER + " " + Build.MODEL);
                JSONObject payload = new JSONObject();
                try {
                    payload.put("event", "identify_android_device");
                    payload.put("data", Build.MANUFACTURER + "-" + deviceID +"-" + Build.MODEL);
                }catch (JSONException e){
                    e.printStackTrace();
                }
                Log.i("\n>>>>>>>WS-CONNECTION", String.format(">>>>>>\nSENDING TO SERVER: %s\n", payload.toString()));
                wsClient.send(payload.toString());
            }

            @Override
            public void onMessage(String message){
//                if(message.equals("xxx")) {
//                    Log.i("\n\n>>>XXX!\n\n", String.format("\n\n\n\n>>> %s \n\n\n\n", message));
//                    Agent_Activity.tv_callerID.setText("from MAIN>>>>>>");
//                    return;
//                }
                final String raw_message = message;
                JSONObject raw; JSONArray data;

                String event = "";

                List<Agent> agents = new ArrayList<>();
                try {
                    raw = new JSONObject(raw_message); Log.i(">>>> from server",  raw.toString());
                    event = raw.getString("event");
                    JSONObject agentDict;
                    if(event.equals("fillData")){
                        data = raw.getJSONArray("data");

                        for (int i=0; i<data.length();i++){
                            agentDict = (JSONObject) data.get(i);
                            agents.add(new Agent(agentDict.getString("calltype"),agentDict.getString("exten"),b.getInt(agentDict.getString("state")),agentDict.getInt("callerid")));
                        }
                    }else if(event.equals("updateState")) {
                        agentDict = raw.getJSONObject("data").getJSONObject("row");
                        agents.add(new Agent(agentDict.getString("calltype"), agentDict.getString("exten"), b.getInt(agentDict.getString("state")), agentDict.getInt("callerid")));
                    }else if(event.equals("paintChart")){
                        Log.i("\n>>>PAINTING CHART!!!: ", String.format("\n\n>>> %s \n\n", raw.toString()));
                        Agent_Activity.jsonArray = raw.getJSONArray("data");
                        Agent_Activity.thread.start();
                    }else{
                        Log.i(">>>\nNOT-PARSED: ", raw.toString());
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                final List<Agent> aa = agents; final String ee = event;

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        showAddItemDialog(MainActivity.this);
//                        createDialog();
//                        openDialog();

//                        addItem(agents);
                        if(ee.equals("updateState")){
                            updateState(aa);
                        }else{
                            addItem(aa);
                        }

                    }
                });
            }

            @Override
            public void onError(Exception e){
                Log.i("WebSocket", "Error" + e.getMessage());

            }

            @Override
            public void onClose(int i, String s, boolean b){
                Log.i("WebSocket", "Closed " + s);
            }
        };
        wsClient.connect();
    }
//*/

//    call -> showAddItemDialog(MainActivity.this);
    private void showAddItemDialog(Context c) {
        final EditText taskEditText = new EditText(c);
        AlertDialog dialog = new AlertDialog.Builder(c)
                .setTitle("Add a new task")
                .setMessage("What do you want to do next?")
                .setView(taskEditText)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String task = String.valueOf(taskEditText.getText());
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }

    private void createDialog(){

        final String[] option = {"Add" , "View" , "Select" , "Delete"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.select_dialog_item,option);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select option");
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        final  AlertDialog a = builder.create();
        a.show();
    }

    public void openDialog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure, You wanted to make decision");
                alertDialogBuilder.setPositiveButton("yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                Toast.makeText(MainActivity.this,"You clicked yes button",Toast.LENGTH_LONG).show();
                            }
                        });

        alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void sendMessage(View view){
        //txtHistory.setText(txtMessage.getText().toString() + "\n" + txtHistory.getText().toString());
        //{"0":"new message","1":{"name":"GEANNCARLO, VILELA","message":"ffffffffff"}}
        //{"0":"new message","1":{"name":"ANDROID","message":"Android Message"}
        //String message = "{\"0\":\"new message\", \"1\":\" + txtMessage.getText().toString() + \"}";

        String message = "{\"0\":\"new message\",\"1\":{\"name\":\"" + "ANDROID" + "\",\"message\":\"" + txtMessage.getText().toString() + "\"}}";

        JSONObject payload = new JSONObject();
        try {
            payload.put("event", "listFiles");
            payload.put("data", message);
        }catch (JSONException e){
            e.printStackTrace();
        }


        wsClient.send(payload.toString());
        txtMessage.setText("");
    }
}

class Utility {
    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int noOfColumns = (int) (dpWidth / 180);
        return noOfColumns;
    }
}

class ColumnQty {
    private int width, height, remaining;
    private DisplayMetrics displayMetrics;

    public ColumnQty(Context context, int viewId) {

        View view = View.inflate(context, viewId, null);
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        width = view.getMeasuredWidth();
        height = view.getMeasuredHeight();
        displayMetrics = context.getResources().getDisplayMetrics();
    }
    public int calculateNoOfColumns() {

        int numberOfColumns = displayMetrics.widthPixels / width;
        remaining = displayMetrics.widthPixels - (numberOfColumns * width);
//        System.out.println("\nRemaining\t" + remaining + "\nNumber Of Columns\t" + numberOfColumns);
        if (remaining / (2 * numberOfColumns) < 15) {
            numberOfColumns--;
            remaining = displayMetrics.widthPixels - (numberOfColumns * width);
        }
        return numberOfColumns;
    }
    public int calculateSpacing() {

        int numberOfColumns = calculateNoOfColumns();
//        System.out.println("\nNumber Of Columns\t"+ numberOfColumns+"\nRemaining Space\t"+remaining+"\nSpacing\t"+remaining/(2*numberOfColumns)+"\nWidth\t"+width+"\nHeight\t"+height+"\nDisplay DPI\t"+displayMetrics.densityDpi+"\nDisplay Metrics Width\t"+displayMetrics.widthPixels);
        return remaining / (2 * numberOfColumns);
    }
}