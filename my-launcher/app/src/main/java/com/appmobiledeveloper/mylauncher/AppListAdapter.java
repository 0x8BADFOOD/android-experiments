package com.appmobiledeveloper.mylauncher;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AppListAdapter  extends BaseAdapter {

    Context mContext;
    LayoutInflater inflater;
    List<AppInfo> apps;
    ArrayList<AppInfo> arrayList = null;
    public AppListAdapter(Context context, List<AppInfo> appInfos) {
        mContext = context;
        this.apps = appInfos;
        inflater = LayoutInflater.from(mContext);
        arrayList  = new ArrayList<AppInfo>(appInfos);
        arrayList.addAll(apps);
    }

    @Override
    public int getCount() {
        return apps.size();
    }

    @Override
    public AppInfo getItem(int position) {
        return apps.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {


        if(view == null){
            view  = ((HomeActivity)mContext).getLayoutInflater().inflate(R.layout.list_item, null);
        }

        ImageView appIcon = (ImageView)view.findViewById(R.id.item_app_icon);
        appIcon.setImageDrawable(apps.get(position).image);

        TextView appLabel = (TextView)view.findViewById(R.id.item_app_label);
        appLabel.setText(apps.get(position).title);

        TextView appName = (TextView)view.findViewById(R.id.item_app_name);
        appName.setText(apps.get(position).packageName);

       view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // Send single item click data to SingleItemView Class
                Intent intent = new Intent(mContext, AppInfo.class);
                mContext.startActivity(intent);
            }
        });

        return view;
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        apps.clear();
        if (charText.length() == 0) {
            apps.addAll(arrayList);
        }
        else
        {
            for (AppInfo ai: arrayList)
            {
                if (ai.title.toString().toLowerCase(Locale.getDefault()).contains(charText))
                {
                    apps.add(ai);
                }
            }
        }
        notifyDataSetChanged();
    }
}
