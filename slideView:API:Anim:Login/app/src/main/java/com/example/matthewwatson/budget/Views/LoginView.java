package com.example.matthewwatson.budget.Views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.example.matthewwatson.budget.BudgetApplication;
import com.example.matthewwatson.budget.R;
import com.example.matthewwatson.budget.Stages.RegisterStage;

import butterknife.ButterKnife;
import butterknife.OnClick;
import flow.Flow;
import flow.History;

/**
 * Created by Matthew.Watson on 10/31/16.
 */

public class LoginView extends LinearLayout {
    Context context;

    public LoginView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    @Override
    protected void onFinishInflate() { //inflates our container
        super.onFinishInflate();
        ButterKnife.bind(this); //because of the onclick

    }
    @OnClick(R.id.login_register_button)
    public void showRegisterView(){
        Flow flow = BudgetApplication.getMainFlow();
        History newHistory = flow.getHistory().buildUpon()
                .push(new RegisterStage())
                .build();
        flow.setHistory(newHistory,Flow.Direction.FORWARD);

    }
}
