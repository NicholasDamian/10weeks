package com.example.nicholashall.myapplication.Stages;

import android.app.Application;

import com.example.nicholashall.myapplication.PeopleMon;
import com.example.nicholashall.myapplication.R;
import com.example.nicholashall.myapplication.Riggers.SlideRigger;

/**
 * Created by nicholashall on 11/10/16.
 */

public class CaughtPeopleListStage extends IndexedStage{
    private final SlideRigger rigger;

    public CaughtPeopleListStage(Application context){
        super(MapStage.class.getName());
        this.rigger = new SlideRigger(context);
    }
    @Override
    public int getLayoutId() {
        return R.layout.caught_people_listview;
    }

    @Override
    public Rigger getRigger() {
        return rigger;
    }

    public CaughtPeopleListStage(){
        this(PeopleMon.getInstance());
    }
}
