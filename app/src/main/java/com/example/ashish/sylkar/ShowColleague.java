package com.example.ashish.sylkar;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Ashish on 14-07-2017.
 */

public class ShowColleague extends ArrayAdapter<Users> {
    private Activity context;
    private List<Users> usersList;

    public ShowColleague(Context context, int resource, Activity context1) {
        super(context, resource);
        this.context = context1;
    }


    @Override
    public int getCount() {
        return 0;
    }

    @Override

    public long getItemId(int position) {
        return 0;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.list_layout,null,true);
        TextView textViewName = (TextView) listViewItem.findViewById(R.id.textViewName);
        TextView textViewJob = (TextView) listViewItem.findViewById(R.id.textViewJob);
        Users users = usersList.get(position);
        //textViewName.setText(users.getName());
        //textViewJob.setText(users.getJob());
        return super.getView(position, convertView, parent);
    }
}
