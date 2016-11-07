package com.example.matthewwatson.budget;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.RelativeLayout;

import com.davidstemmer.flow.plugin.screenplay.ScreenplayDispatcher;
import com.example.matthewwatson.budget.Model.TestPost;
import com.example.matthewwatson.budget.Network.RestClient;
import com.example.matthewwatson.budget.Network.UserStore;
import com.example.matthewwatson.budget.Stages.BudgetListStage;
import com.example.matthewwatson.budget.Stages.LoginStage;

import butterknife.Bind;
import butterknife.ButterKnife;
import flow.Flow;
import flow.History;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private String TAG = "MainActivity";
    private Flow flow;
    private ScreenplayDispatcher dispatcher;

    @Bind(R.id.container)
    RelativeLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this); //NOTHING WORKS WITHOUT THIS LINE

        flow = BudgetApplication.getMainFlow();
        dispatcher = new ScreenplayDispatcher(this, container); //controls UI, container is the views
        dispatcher.setUp(flow);//sets up based on what's in flow

//        testCalls();

        if(UserStore.getInstance().getToken() == null || UserStore.getInstance().getTokenExpiration() == null){
            History newHistory = History.single(new LoginStage());
            flow.setHistory(newHistory,Flow.Direction.REPLACE); //no visual transition
        }

    }

    @Override
    public void onBackPressed() { //overrides back button
        if (!flow.goBack()){
            flow.removeDispatcher(dispatcher);
            flow.setHistory(History.single(new BudgetListStage()),Flow.Direction.BACKWARD);
            super.onBackPressed();
        }
    }

    private void testCalls(){
        RestClient restClient = new RestClient();
        restClient.getApiService().getPost(1).enqueue(new Callback<TestPost>() { //1 is the id from ApiService.java
            @Override
            public void onResponse(Call<TestPost> call, Response<TestPost> response) {
                Log.d(TAG, "Get Post - Title: " + response.body().getTitle() + "\n" +
                "Body: " + response.body().getBody());
            }

            @Override
            public void onFailure(Call<TestPost> call, Throwable t) {
                Log.d(TAG, "Get Post Failed");

            }
        });

        TestPost testPost = new TestPost(1,"test post title", "test post body");
        restClient.getApiService().postPost(testPost).enqueue(new Callback<TestPost>() {
            @Override
            public void onResponse(Call<TestPost> call, Response<TestPost> response) {
                Log.d(TAG, "Post Post - Title: " + response.body().getTitle() + "\n" +
                        "Body: " + response.body().getBody());
            }

            @Override
            public void onFailure(Call<TestPost> call, Throwable t) {
                Log.d(TAG, "Post Post Failed");
            }
        });

        restClient.getApiService().getPosts().enqueue(new Callback<TestPost[]>() {
            @Override
            public void onResponse(Call<TestPost[]> call, Response<TestPost[]> response) {
                Log.d(TAG, "Retrieved: " + response.body().length+ " posts");
            }

            @Override
            public void onFailure(Call<TestPost[]> call, Throwable t) {
                Log.d(TAG,"failed");

            }
        });
    }
}
