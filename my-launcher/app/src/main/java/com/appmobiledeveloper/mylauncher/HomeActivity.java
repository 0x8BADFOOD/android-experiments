package com.appmobiledeveloper.mylauncher;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HomeActivity extends Activity
        implements GestureDetector.OnGestureListener
{
    public static final String DEBUG_TAG = "HomeActivity";
    private PackageManager manager;
    private List<AppInfo> apps;
    private AppListAdapter adapter;
    private ListView list;
    private GestureDetectorCompat mDetector;

    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mDetector = new GestureDetectorCompat(this,this);

        textView = (TextView)findViewById(R.id.search);


        textView.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                String text = textView.getText().toString().toLowerCase(Locale.getDefault());
                Log.d(DEBUG_TAG,"afterTextChanged -> " + text);
                adapter.filter(text);
                if (text.equals("")){
                    list.setVisibility(View.GONE);
                }else{
                    list.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                // TODO Auto-generated method stub
            }
        });


        loadApps();
        loadListView2();
        list.setVisibility(View.GONE);
        textView.setVisibility(View.GONE);

//        addClickListener();


//        View myView = findViewById(R.id.activity_home);
//        myView.setOnTouchListener(new View.OnTouchListener() {
//            public boolean onTouch(View v, MotionEvent event) {
//                // ... Respond to touch events
//                int action = MotionEventCompat.getActionMasked(event);
//
//                switch(action) {
//                    case (MotionEvent.ACTION_DOWN) :
//                        Log.d(DEBUG_TAG,"Action was DOWN");
//                        return true;
//                    case (MotionEvent.ACTION_MOVE) :
//                        Log.d(DEBUG_TAG,"Action was MOVE");
//                        return true;
//                    case (MotionEvent.ACTION_UP) :
//                        Log.d(DEBUG_TAG,"Action was UP");
//                        return true;
//                    case (MotionEvent.ACTION_CANCEL) :
//                        Log.d(DEBUG_TAG,"Action was CANCEL");
//                        return true;
//                    case (MotionEvent.ACTION_OUTSIDE) :
//                        Log.d(DEBUG_TAG,"Movement occurred outside bounds " +
//                                "of current screen element");
//                        return true;
//                    default :
//                    return true;
//                }
//            }
//        });
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event){
//
//        int action = MotionEventCompat.getActionMasked(event);
//
//        switch(action) {
//            case (MotionEvent.ACTION_DOWN) :
//                Log.d(DEBUG_TAG,"Action was DOWN");
//                return true;
//            case (MotionEvent.ACTION_MOVE) :
//                Log.d(DEBUG_TAG,"Action was MOVE");
//                return true;
//            case (MotionEvent.ACTION_UP) :
//                Log.d(DEBUG_TAG,"Action was UP");
//                return true;
//            case (MotionEvent.ACTION_CANCEL) :
//                Log.d(DEBUG_TAG,"Action was CANCEL");
//                return true;
//            case (MotionEvent.ACTION_OUTSIDE) :
//                Log.d(DEBUG_TAG,"Movement occurred outside bounds " +
//                        "of current screen element");
//                return true;
//            default :
//                return super.onTouchEvent(event);
//        }
//    }

    public void openList(View v){
        Intent i = new Intent(this, AppListActivity.class);
        startActivity(i);
    }
    private void loadApps(){
        manager = getPackageManager();
        apps = new ArrayList<AppInfo >();

        Intent i = new Intent(Intent.ACTION_MAIN, null);
        i.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> availableActivities = manager.queryIntentActivities(i, 0);
        for(ResolveInfo ri:availableActivities){
            AppInfo app = new AppInfo();
            app.title= ri.loadLabel(manager);
            app.packageName= ri.activityInfo.packageName;
            app.image= ri.activityInfo.loadIcon(manager);
            apps.add(app);
        }
    }
    private void loadListView(){
        list = (ListView)findViewById(R.id.search_listview);

//        ArrayAdapter<AppInfo> adapter = new ArrayAdapter<AppInfo>(this,
//                R.layout.list_item,
//                apps) {
            ArrayAdapter<AppInfo> adapter = new ArrayAdapter<AppInfo>(this,
                    R.layout.list_item,
                    apps) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if(convertView == null){
                    convertView = getLayoutInflater().inflate(R.layout.list_item, null);
                }

                ImageView appIcon = (ImageView)convertView.findViewById(R.id.item_app_icon);
                appIcon.setImageDrawable(apps.get(position).image);

                TextView appLabel = (TextView)convertView.findViewById(R.id.item_app_label);
                appLabel.setText(apps.get(position).title);

                TextView appName = (TextView)convertView.findViewById(R.id.item_app_name);
                appName.setText(apps.get(position).packageName);

                return convertView;
            }
        };

        list.setAdapter(adapter);
    }
    private void loadListView2(){
        list = (ListView)findViewById(R.id.search_listview);

        adapter = new AppListAdapter(this,apps);
        list.setAdapter(adapter);


//        ArrayAdapter<AppInfo> adapter = new ArrayAdapter<AppInfo>(this,
//                R.layout.list_item,
//                apps) {

//        list.setAdapter(adapter);
    }
    private void addClickListener(){
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> av, View v, int pos,
                                    long id) {
                Intent i = manager.getLaunchIntentForPackage(apps.get(pos).packageName.toString());
                HomeActivity.this.startActivity(i);
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        this.mDetector.onTouchEvent(event);
        // Be sure to call the superclass implementation
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent event) {
        return true;
    }

    @Override
    public boolean onFling(MotionEvent event1, MotionEvent event2,
                           float velocityX, float velocityY) {
        return true;
    }

    @Override
    public void onLongPress(MotionEvent event) {
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                            float distanceY) {
        Log.d(DEBUG_TAG, "onScroll: " + e1.toString()+e2.toString());
        Toast.makeText(this, "OnScroll",
                Toast.LENGTH_LONG).show();
        textView.setVisibility(View.VISIBLE);
        textView.requestFocus();
        showKeyBoard();
        return true;
    }

    @Override
    public void onShowPress(MotionEvent event) {
    }

    @Override
    public boolean onSingleTapUp(MotionEvent event) {
        hideKeyBoard();
        textView.setVisibility(View.GONE);
        return true;
    }

    public void showKeyBoard(){
        InputMethodManager imm = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(textView, InputMethodManager.SHOW_IMPLICIT);
    }

    public void hideKeyBoard(){
        InputMethodManager imm = (InputMethodManager)
        getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(textView.getWindowToken(), 0);
    }
}
