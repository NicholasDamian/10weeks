package com.example.nicholashall.myapplication.Stages;

import android.app.Application;

import com.example.nicholashall.myapplication.PeopleMon;
import com.example.nicholashall.myapplication.R;
import com.example.nicholashall.myapplication.Riggers.SlideRigger;

/**
 * Created by Nicholas.Hall on 10/31/16.
 */

public class LoginStage extends IndexedStage {
    private final SlideRigger rigger;

    public LoginStage(Application context){
        super(LoginStage.class.getName());
        this.rigger = new SlideRigger(context);
    }
    @Override
    public int getLayoutId() {
        return R.layout.login_view;
    }

    @Override
    public Rigger getRigger() {
        return rigger;
    }

    public LoginStage(){
        this(PeopleMon.getInstance());
    }
}
