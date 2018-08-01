package com.example.eidan.wsapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
import java.util.List;

import android.util.Log;
import android.os.Build;

import android.support.v7.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import android.provider.Settings.Secure;

public class MainActivity extends AppCompatActivity {
    private WebSocketClient wsClient;
    EditText txtMessage;
    TextView txtHistory;
    List<Agent> lstAgent;
    String deviceID;
    RecyclerView myrv;
    RecyclerViewAdapter myAdapter;

    Bundle b;


    @SuppressLint("HardwareIds")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        deviceID = Secure.getString(getApplicationContext().getContentResolver(), Secure.ANDROID_ID);

        b = new Bundle();
        b.putInt("TALKING",R.drawable.call);
        b.putInt("IDLE",R.drawable.hangup);
        b.putInt("RINGING",R.drawable.ring);

        lstAgent = new ArrayList<>();

        myrv = findViewById(R.id.recyclerview_agent_id);
        myAdapter = new RecyclerViewAdapter(this, lstAgent);
        myrv.setLayoutManager(new GridLayoutManager(this, 3));
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

    void addItem(List<Agent> agents){
        for (Agent agent: agents){
            lstAgent.add(agent);
        }
        myAdapter.notifyDataSetChanged();
//        myAdapter.notifyItemInserted(lstAgent.size() - 1);
    }

    ///*
    private void cnxWebSocket(){
        URI uri;
        try {
//            uri = new URI("ws://192.168.3.107:8000/ws");
            uri = new URI("ws://ryr.progr.am/ws");
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
                    payload.put("event", "listFiles");
                    payload.put("data", "Hello from " + Build.MANUFACTURER + " - " + deviceID +" " + Build.MODEL);
                }catch (JSONException e){
                    e.printStackTrace();
                }
                Log.i(">>>>>>>\nWS-CONNECTION", String.format(">>>>>>\nSENDING TO SERVER: %s\n", payload.toString()));
                wsClient.send(payload.toString());
            }

            @Override
            public void onMessage(String message){
                final String raw_message = message;
                JSONObject raw; JSONArray data;

                String event = "";

                List<Agent> agents = new ArrayList<>();
                try {
                    raw = new JSONObject(raw_message); Log.i(">>>> from server",  raw.toString());
                    event = raw.getString("event");
                    data = raw.getJSONArray("data");

                    if(event.equals("fillData")){
                        JSONObject agentDict;
                        for (int i=0; i<data.length();i++){
                            agentDict = (JSONObject) data.get(i);
                            agents.add(new Agent(agentDict.getString("calltype"),agentDict.getString("exten"),b.getInt(agentDict.getString("state")),agentDict.getInt("callerid")));
                        }
                    }else {
                        Log.i(">>>\nNOT-PARSED: ", raw.toString());
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                final List<Agent> aa = agents;

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        showAddItemDialog(MainActivity.this);
//                        createDialog();
//                        openDialog();
                        addItem(aa);
//                        addItem(agents);


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
