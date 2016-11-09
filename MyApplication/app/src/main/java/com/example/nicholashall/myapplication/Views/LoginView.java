package com.example.nicholashall.myapplication.Views;


import android.content.Context;
import android.util.AttributeSet;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.nicholashall.myapplication.MainActivity;
import com.example.nicholashall.myapplication.Models.Authorization;
import com.example.nicholashall.myapplication.Network.RestClient;
import com.example.nicholashall.myapplication.Network.UserStore;
import com.example.nicholashall.myapplication.PeopleMon;
import com.example.nicholashall.myapplication.R;
import com.example.nicholashall.myapplication.Stages.MapStage;
import com.example.nicholashall.myapplication.Stages.RegisterStage;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import flow.Flow;
import flow.History;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Nicholas.Hall on 10/31/16.
 */

public class LoginView extends LinearLayout {
    private Context context;

    @Bind(R.id.username_field)
    EditText usernameField;

    @Bind(R.id.password_field)
    EditText passwordField;

    @Bind(R.id.login_button)
    Button loginButton;

    @Bind(R.id.register_button)
    Button registerButton;

    @Bind(R.id.spinner)
    ProgressBar spinner;

    public LoginView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);

//        maybe
        ((MainActivity)context).showMenuItem(false);

    }

    @OnClick(R.id.register_button)
    public void showRegisterView(){
        Flow flow = PeopleMon.getMainFlow();
        History newHistory = flow.getHistory().buildUpon()
                .push(new RegisterStage())
                .build();
        flow.setHistory(newHistory, Flow.Direction.FORWARD);
    }

    @OnClick(R.id.login_button)
    public void login(){
        InputMethodManager imm = (InputMethodManager)context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(usernameField.getWindowToken(),0);
        imm.hideSoftInputFromWindow(passwordField.getWindowToken(),0);

        String username = usernameField.getText().toString();
        String password = passwordField.getText().toString();
        String grantType ="password";

        if( username.isEmpty() || password.isEmpty()) {
            Toast.makeText(context, R.string.toast,
                    Toast.LENGTH_LONG).show();
        } else {
            loginButton.setEnabled(false);
            registerButton.setEnabled(false);
            spinner.setVisibility(GONE);

//            Authorization authorization = new Authorization(username, password,grantType);
            RestClient restClient = new RestClient();
            restClient.getApiService().login(username,password,grantType).enqueue(new Callback<Authorization>() {
                @Override
                public void onResponse(Call<Authorization> call, Response<Authorization> response) {
                    if (response.isSuccessful()){

                        Authorization authUser = response.body();

                        UserStore.getInstance().setToken(authUser.getToken());
                        UserStore.getInstance().setTokenExpiration(authUser.getExpires());

                        Flow flow = PeopleMon.getMainFlow();
                        History newHistory = History.single(new MapStage());
                        flow.setHistory(newHistory, Flow.Direction.REPLACE);

                    }else{
                        restView();
                        Toast.makeText(context, R.string.toast_login_failed + ": " + response.code(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Authorization> call, Throwable t) {
                    restView();
                    Toast.makeText(context, R.string.toast_login_failed, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void restView(){
        loginButton.setEnabled(true);
        registerButton.setEnabled(true);
        spinner.setVisibility(GONE);
    }
}
