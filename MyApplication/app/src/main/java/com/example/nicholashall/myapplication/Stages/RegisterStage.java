package com.example.nicholashall.myapplication.Stages;


import android.app.Application;

import com.example.nicholashall.myapplication.PeopleMon;
import com.example.nicholashall.myapplication.R;
import com.example.nicholashall.myapplication.Riggers.SlideRigger;

/**
 * Created by Nicholas.Hall on 10/31/16.
 */

public class RegisterStage extends IndexedStage {
    private final SlideRigger rigger;

    public RegisterStage(Application context){
        super(RegisterStage.class.getName());
        this.rigger = new SlideRigger(context);
    }
    @Override
    public int getLayoutId() {
        return R.layout.register_view;
    }

    @Override
    public Rigger getRigger() {
        return rigger;
    }

    public RegisterStage(){
        this(PeopleMon.getInstance());
    }
}
