package com.appmobiledeveloper.mylauncher;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class HomeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    public void openList(View v){
        Intent i = new Intent(this, AppListActivity.class);
        startActivity(i);
    }
}
