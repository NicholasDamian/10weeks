package com.example.matthewwatson.budget.Stages;

import android.app.Application;

import com.example.matthewwatson.budget.BudgetApplication;
import com.example.matthewwatson.budget.R;
import com.example.matthewwatson.budget.Riggers.SlideRigger;

/**
 * Created by Matthew.Watson on 10/31/16.
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
        this(BudgetApplication.getInstance());
    }
}
