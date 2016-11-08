package com.example.nicholashall.myapplication;

import android.app.Application;

import com.example.nicholashall.myapplication.Stages.MapStage;

import flow.Flow;
import flow.History;

/**
 * Created by nicholashall on 11/7/16.
 */

public class PeopleMon extends Application {
    private static PeopleMon application;
    public final Flow mainFlow = new Flow(History.single(new MapStage()));

    public static final String API_BASE_URL =
            "https://efa-peoplemon-api.azurewebsites.net:443/";

    @Override
    public void onCreate() {
        super.onCreate();

        application=this;
    }

    public static PeopleMon getInstance(){
        return application;
    }

    public static Flow getMainFlow(){
        return getInstance().mainFlow;
    }
}