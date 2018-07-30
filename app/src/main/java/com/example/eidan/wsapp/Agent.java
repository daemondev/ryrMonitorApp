package com.example.eidan.wsapp;

/**
 * Created by Eidan on 29/07/2018.
 */

public class Agent {
    String callType, phone;
    int status, callerID;

    public Agent() {
    }

    public Agent(String callType, String phone, int status, int callerID) {
        this.callType = callType;
        this.phone = phone;
        this.status = status;
        this.callerID = callerID;
    }

    public String getCallType() {
        return callType;
    }

    public void setCallType(String callType) {
        this.callType = callType;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getCallerID() {
        return callerID;
    }

    public void setCallerID(int callerID) {
        this.callerID = callerID;
    }
}
