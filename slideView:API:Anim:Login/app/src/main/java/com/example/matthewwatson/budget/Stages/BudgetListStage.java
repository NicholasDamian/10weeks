package com.example.matthewwatson.budget.Stages;

import android.app.Application;

import com.example.matthewwatson.budget.BudgetApplication;
import com.example.matthewwatson.budget.R;
import com.example.matthewwatson.budget.Riggers.SlideRigger;

/**
 * Created by Matthew.Watson on 10/31/16.
 */

public class BudgetListStage extends IndexedStage {
    private final SlideRigger rigger; //sets Rigger

    @Override
    public int getLayoutId() {
        return R.layout.budget_list_view;
    }

    @Override
    public Rigger getRigger() {
        return rigger;
    }

    public BudgetListStage(Application context){
        super(BudgetListStage.class.getName());
        this.rigger = new SlideRigger(context);
    }
    public BudgetListStage(){
        this(BudgetApplication.getInstance());
    }
}
