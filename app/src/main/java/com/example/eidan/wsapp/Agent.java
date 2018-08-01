package com.example.eidan.wsapp;

/**
 * Created by Eidan on 29/07/2018.
 */

public class Agent {
    String calltype, exten;
    int state, callerid;

    public Agent() {
    }

    public Agent(String calltype, String exten, int state, int callerid) {
        this.calltype = calltype;
        this.exten = exten;
        this.state = state;
        this.callerid = callerid;
    }

    public String getCalltype() {
        return calltype;
    }

    public void setCalltype(String calltype) {
        this.calltype = calltype;
    }

    public String getExten() {
        return exten;
    }

    public void setExten(String exten) {
        this.exten = exten;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getCallerid() {
        return callerid;
    }

    public void setCallerid(int callerid) {
        this.callerid = callerid;
    }
}
