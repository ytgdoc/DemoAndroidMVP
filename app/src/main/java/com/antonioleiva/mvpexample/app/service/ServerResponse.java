package com.antonioleiva.mvpexample.app.service;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Dori on 3/10/2016.
 */

public class ServerResponse implements Serializable {

    @SerializedName("message")
    private String message;

    @SerializedName("statusCode")
    private int statusCode;

    public ServerResponse(String message, int statusCode){
        this.message = message;
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}
