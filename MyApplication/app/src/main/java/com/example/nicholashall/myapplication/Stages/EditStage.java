package com.example.nicholashall.myapplication.Stages;

import android.app.Application;

import com.davidstemmer.screenplay.stage.Stage;
import com.example.nicholashall.myapplication.PeopleMon;
import com.example.nicholashall.myapplication.R;
import com.example.nicholashall.myapplication.Riggers.SlideRigger;

/**
 * Created by nicholashall on 11/9/16.
 */

public class EditStage extends IndexedStage {
    private final SlideRigger rigger;

    public EditStage (Application context){
        super(LoginStage.class.getName());
        this.rigger = new SlideRigger(context);
    }
    @Override
    public int getLayoutId() {
        return R.layout.edit_view;
    }

    @Override
    public Stage.Rigger getRigger() {
        return rigger;
    }

    public EditStage() {
        this(PeopleMon.getInstance());
    }
}

