package com.example.eidan.wsapp;

/**
 * Created by Eidan on 29/07/2018.
 */

public class Agent {
    String calltype;
    String exten;
    String callrank;

    int state;
    int callerid;

    int totalcalls;
    int answeredcalls;
    int idlecalls;

    public String getCallrank() {
        return callrank;
    }

    public void setCallrank(String callrank) {
        this.callrank = callrank;
    }

    public int getTotalcalls() {
        return totalcalls;
    }

    public void setTotalcalls(int totalcalls) {
        this.totalcalls = totalcalls;
    }

    public int getAnsweredcalls() {
        return answeredcalls;
    }

    public void setAnsweredcalls(int answeredcalls) {
        this.answeredcalls = answeredcalls;
    }

    public int getIdlecalls() {
        return idlecalls;
    }

    public void setIdlecalls(int idlecalls) {
        this.idlecalls = idlecalls;
    }

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
