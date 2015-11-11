package com.example.zghadyali.carshareapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MyCustomAdapter extends BaseAdapter implements ListAdapter {
    private ArrayList<String> list = new ArrayList<String>();
    private Context context;
    private loginFacebook loginfb;



    public MyCustomAdapter(ArrayList<String> list, loginFacebook loginfb,Context context) {
        this.list = list;
        this.context = context;
        this.loginfb = loginfb;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int pos) {
        return list.get(pos);
    }

    @Override
    public long getItemId(int pos) {
//        return list.get(pos).getId();
        return 0;
        //just return 0 if your list items do not have an Id variable.
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.friends_list, null);
        }

        //Handle TextView and display string from your list
        TextView listItemText = (TextView)view.findViewById(R.id.list_item_string);
        String name = list.get(position);
        listItemText.setText(name);

//        String thisID = ((MainActivity)context).getLoginfb().getIDAtPosition(position);
//        String thisID = loginfb.getIDAtPosition(position);
//        Log.d("ADAPTER thisID is ",thisID);

//        final boolean isApproved = loginfb.IDIsInApprovedList(thisID);
        final boolean isApproved = loginfb.PosIsApproved(position);


        //Handle buttons and add onClickListeners
//        Button deleteBtn = (Button)view.findViewById(R.id.delete_btn);
//        Button addBtn = (Button)view.findViewById(R.id.add_btn);
        Button addDelBtn = (Button)view.findViewById(R.id.add_del_btn);
        if (isApproved) {
            addDelBtn.setText("Remove");
        }
        else {
            addDelBtn.setText("Add");
        }

        addDelBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // TODO check if this is add or delete, do the action
                if (isApproved) {
                    // Remove from approved list
                    loginfb.removePosFromApprovedList(position);
                }
                else {
                    // Add to approved list
                    loginfb.addPosToApprovedList(position);
                }
                notifyDataSetChanged();
            }
        });

//        deleteBtn.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                //do something
//                list.remove(position); //or some other task
//                notifyDataSetChanged();
//            }
//        });
//        addBtn.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                //do something
//                notifyDataSetChanged();
//            }
//        });

        return view;
    }
}