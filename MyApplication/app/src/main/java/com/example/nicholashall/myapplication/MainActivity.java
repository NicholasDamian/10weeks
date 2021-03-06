package com.example.nicholashall.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.davidstemmer.flow.plugin.screenplay.ScreenplayDispatcher;
import com.example.nicholashall.myapplication.Models.Account;
import com.example.nicholashall.myapplication.Models.ImageLoadedEvent;
import com.example.nicholashall.myapplication.Network.RestClient;
import com.example.nicholashall.myapplication.Network.UserStore;
import com.example.nicholashall.myapplication.Stages.EditStage;
import com.example.nicholashall.myapplication.Stages.LoginStage;
import com.example.nicholashall.myapplication.Stages.MapStage;

import org.greenrobot.eventbus.EventBus;

import java.io.ByteArrayOutputStream;

import butterknife.Bind;
import butterknife.ButterKnife;
import flow.Flow;
import flow.History;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

//    public static String yellow;


    private Context context;
    private Flow flow;
    private ScreenplayDispatcher dispatcher;

    public Bundle savedInstanceState;

//    this look at it
    private static int RESULT_LOAD_IMAGE = 1;

    @Bind(R.id.container)
    RelativeLayout container;

    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        maybe this
        this.savedInstanceState = savedInstanceState;

        ButterKnife.bind(this);

        flow = PeopleMon.getMainFlow();
        dispatcher = new ScreenplayDispatcher(this, container); //controls UI, container is the views
        dispatcher.setUp(flow);//sets up based on what's in flow

        if(UserStore.getInstance().getToken() == null || UserStore.getInstance().getTokenExpiration() == null){
            History newHistory = History.single(new LoginStage());
            flow.setHistory(newHistory,Flow.Direction.REPLACE); //no visual transition
        }

        if (Build.VERSION.SDK_INT >= 23) {
            if (!(ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) ==
                    PackageManager.PERMISSION_GRANTED)) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
            if (!(ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_GRANTED)) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }

            if ((ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }

            if ((ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }


        }

    }

    @Override
    public void onBackPressed(){
        if(!flow.goBack()){
            flow.removeDispatcher(dispatcher);
            flow.setHistory(History.single(new MapStage()), Flow.Direction.BACKWARD);
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_logout, menu);
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //This pinpoints where the app goes after selecting the edit profile item
            case R.id.edit_profile:
                Flow flow = PeopleMon.getMainFlow();
                History newHistory = flow.getHistory().buildUpon()
                        //Pushes our screen to the corresponding view that contains edit profile information
                        .push(new EditStage())
                        .build();
                flow.setHistory(newHistory, Flow.Direction.FORWARD);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void showMenuItem(boolean show) {
        if ( menu != null){
            menu.findItem(R.id.edit_profile).setVisible(show);
            menu.findItem(R.id.logout).setVisible(show);
        }
    }

    public void getImage(){
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try{

            if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null){

                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA };
                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String imageString = cursor.getString(columnIndex);
                cursor.close();

                Log.i("yellow",imageString);
                //Convert to Bitmap Array




                Bitmap bm = BitmapFactory.decodeFile(imageString);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
                byte[] b = baos.toByteArray();

                //Take the bitmap Array and e
                // encode it to Base64
                String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);


                Log.d("***BASE64****", encodedImage);
                makeApiCallForProfile(encodedImage);

                //Make API Call to Send Base64 to Server

                EventBus.getDefault().post(new ImageLoadedEvent(imageString));
            }else{
                Toast.makeText(this,"Error Retrieving Image1", Toast.LENGTH_LONG).show();

            }

        } catch (Exception e){
            Toast.makeText(this,"Error Retrieving Image", Toast.LENGTH_LONG).show();
        }
    }

    private void makeApiCallForProfile(String imageString){

        Account acct = new Account(imageString, null);
        RestClient restClient = new RestClient();
        restClient.getApiService().postUserInfo(acct).enqueue(new Callback<Void>() {

            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                // Is the server response between 200 to 299
                if (response.isSuccessful()){

                }else{
                    Toast.makeText(context,"Get User Info Failed" + ": " + response.code(), Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context,"Get User Info Failed", Toast.LENGTH_LONG).show();
            }
        });
    }
}
