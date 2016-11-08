package com.example.nicholashall.myapplication.Stages;

import android.app.Application;

import com.example.nicholashall.myapplication.PeopleMon;
import com.example.nicholashall.myapplication.R;
import com.example.nicholashall.myapplication.Riggers.SlideRigger;

/**
 * Created by nicholashall on 11/7/16.
 */

public class MapStage extends IndexedStage{
    private final SlideRigger rigger;

    public MapStage(Application context){
        super(MapStage.class.getName());
        this.rigger = new SlideRigger(context);
    }
    @Override
    public int getLayoutId() {
        return R.layout.map_view;
    }

    @Override
    public Rigger getRigger() {
        return rigger;
    }

    public MapStage(){
        this(PeopleMon.getInstance());
    }
}

