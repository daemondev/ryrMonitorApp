package com.example.eidan.wsapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;

import android.util.Log;
import android.os.Build;

public class MainActivity extends AppCompatActivity {
    private WebSocketClient wsClient;
    EditText txtMessage;
    TextView txtHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        txtMessage = (EditText) findViewById(R.id.txtMessage);
        txtHistory = (TextView) findViewById(R.id.txtHistory);
        cnxWebSocket();
        txtMessage.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                boolean handled = false;
                if(i == KeyEvent.KEYCODE_ENTER){
                    //sendMessages();
                    //txtHistory.setText(txtMessage.getText().toString() + "\n" + txtHistory.getText().toString());
                    txtHistory.setText(String.format("%s %s %s",txtMessage.getText().toString(),"\n",txtHistory.getText().toString()));
                    txtMessage.setText("");
                    handled = true;

                }
                return handled;
            }
        });



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    ///*
    private void cnxWebSocket(){
        URI uri;
        try {
            uri = new URI("ws://192.168.3.107:8000/ws");
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
                    payload.put("data", "Hello from " + Build.MANUFACTURER + " " + Build.MODEL);
                }catch (JSONException e){
                    e.printStackTrace();
                }
                wsClient.send(payload.toString());
            }

            @Override
            public void onMessage(String s){
                final String message = s;
                JSONObject json = null;
                String aux = "";

                String name = "";
                String theMessage = "";
                try {
                    json = new JSONObject(message);
                    Log.i(">>>> from server",  json.toString());
                    //aux = json.getString("data").toString();
                    //json = new JSONObject(aux);

                    //name = json.getString("event").toString();
                    //theMessage = json.getString("data").toString();

                    name = json.getString("event").toString();
                    theMessage = json.getString("data").toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //final String auxMessage = aux;
                final String auxMessage = "EVENT: " + name + " - DATA: " + theMessage;

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("DEBUG", auxMessage + " - " + message);
                        TextView textView = (TextView) findViewById(R.id.txtHistory);

                        //textView.setText(textView.getText() + "\n" + auxMessage);
                        textView.setText(auxMessage + "\n" + textView.getText());
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
