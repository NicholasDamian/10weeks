package com.example.nicholashall.myapplication.Stages;

import android.app.Application;

import com.davidstemmer.screenplay.stage.Stage;
import com.example.nicholashall.myapplication.PeopleMon;
import com.example.nicholashall.myapplication.R;
import com.example.nicholashall.myapplication.Riggers.SlideRigger;

/**
 * Created by nicholashall on 11/11/16.
 */

public class NearbyPeopleStage extends IndexedStage{
    private final SlideRigger rigger;

    public NearbyPeopleStage(Application context){
        super(MapStage.class.getName());
        this.rigger = new SlideRigger(context);
    }
    @Override
    public int getLayoutId() {
        return R.layout.nearby_people_listview;
    }

    @Override
    public Stage.Rigger getRigger() {
        return rigger;
    }

    public NearbyPeopleStage(){
        this(PeopleMon.getInstance());
    }
}
