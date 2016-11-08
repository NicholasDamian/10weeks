package com.example.nicholashall.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.RelativeLayout;

import com.davidstemmer.flow.plugin.screenplay.ScreenplayDispatcher;
import com.example.nicholashall.myapplication.Network.UserStore;
import com.example.nicholashall.myapplication.Stages.LoginStage;
import com.example.nicholashall.myapplication.Stages.MapStage;

import butterknife.Bind;
import butterknife.ButterKnife;
import flow.Flow;
import flow.History;

public class MainActivity extends AppCompatActivity {
    private String TAG = "MainActivity";
    private Flow flow;
    private ScreenplayDispatcher dispatcher;
    public Bundle savedInstanceState;

    @Bind(R.id.container)
    RelativeLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        maybe this
        this.savedInstanceState =savedInstanceState;


        ButterKnife.bind(this);

        flow = PeopleMon.getMainFlow();
        dispatcher = new ScreenplayDispatcher(this, container); //controls UI, container is the views
        dispatcher.setUp(flow);//sets up based on what's in flow

//        testCalls();

        if(UserStore.getInstance().getToken() == null || UserStore.getInstance().getTokenExpiration() == null){
            History newHistory = History.single(new LoginStage());
            flow.setHistory(newHistory,Flow.Direction.REPLACE); //no visual transition
        }

    }


    @Override
    public void onBackPressed(){
        if(!flow.goBack()){
            flow.removeDispatcher(dispatcher);
            flow.setHistory(History.single(new MapStage()), Flow.Direction.BACKWARD);
            super.onBackPressed();
        }
    }
}
