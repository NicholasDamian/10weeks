package com.example.nicholashall.myapplication.Views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Patterns;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.nicholashall.myapplication.Models.Account;
import com.example.nicholashall.myapplication.Network.RestClient;
import com.example.nicholashall.myapplication.PeopleMon;
import com.example.nicholashall.myapplication.R;
import com.example.nicholashall.myapplication.Stages.LoginStage;

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

public class RegisterView extends LinearLayout {
    private Context context;

    @Bind(R.id.username_field)
    EditText usernameField;

    @Bind(R.id.email_field)
    EditText emailField;

    @Bind(R.id.password_field)
    EditText passwordField;

    @Bind(R.id.confirm_field)
    EditText confirmField;

    @Bind(R.id.register_button)
    Button registerButton;

    @Bind(R.id.spinner)
    ProgressBar spinner;


    public RegisterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
    }

    @OnClick(R.id.register_button)
    public void register(){
        InputMethodManager imm = (InputMethodManager)context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(usernameField.getWindowToken(),0);
        imm.hideSoftInputFromWindow(emailField.getWindowToken(),0);
        imm.hideSoftInputFromWindow(passwordField.getWindowToken(),0);
        imm.hideSoftInputFromWindow(confirmField.getWindowToken(),0);

        String username = usernameField.getText().toString();
        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();
        String confirm = confirmField.getText().toString();
        String picture ="picture";
        String apiKey ="iOSandroid301november2016";

        if(username.isEmpty() || email.isEmpty() || password.isEmpty() ||confirm.isEmpty()){
            Toast.makeText(context, R.string.fill_all_fields_out, Toast.LENGTH_SHORT).show();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(context, R.string.toast_valid_email, Toast.LENGTH_SHORT).show();
        } else if  (!password.equals(confirm)) {
            Toast.makeText(context, R.string.password_dont_match, Toast.LENGTH_SHORT).show();
        } else {
            registerButton.setEnabled(false);
            spinner.setVisibility(VISIBLE);

            Account account = new Account(email, username,picture,apiKey,password);
            final RestClient restClient = new RestClient();
            restClient.getApiService().register(account).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {

                        Flow flow = PeopleMon.getMainFlow();
                        History newHistory = History.single(new LoginStage());
                        flow.setHistory(newHistory, Flow.Direction.BACKWARD);

                    } else {
                        resetView();
                        Toast.makeText(context, R.string.registration_failed, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    resetView();
                    Toast.makeText(context,"Its really broken", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void resetView(){
        registerButton.setEnabled(true);
        spinner.setVisibility(GONE);
    }


}
