package com.example.zghadyali.carshareapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ApprovedListAdapter extends BaseAdapter implements ListAdapter {
    private ArrayList<String> list = new ArrayList<String>();
    private Context context;
    private setALParent setAL;

    public ApprovedListAdapter(ArrayList<String> list, setALParent setAL, Context context) {
        this.list = list;
        this.context = context;
        this.setAL = setAL;
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
        final String thisID = list.get(position);

        //Handle TextView and display string from your list
        TextView listItemText = (TextView)view.findViewById(R.id.list_item_string);

        // Displays the actual friend name instead of the id
        listItemText.setText(setAL.getThisActivity().getFriendNameFromID(thisID));

        final boolean isApproved = setAL.IDIsApproved(thisID);

        //Handle buttons and add onClickListeners
        Button addDelBtn = (Button)view.findViewById(R.id.add_del_btn);
        if (isApproved) {
            addDelBtn.setText(R.string.remove_button);
        }
        else {
            addDelBtn.setText(R.string.add_button);
        }

        addDelBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (isApproved) {
                    // Remove from approved list
                    setAL.removeIDFromApprovedList(thisID);
                }
                else {
                    // Add to approved list
                    setAL.addIDToApprovedList(thisID);
                }
                notifyDataSetChanged();
            }
        });

        return view;
    }

    public void setNewList(ArrayList<String> newList) {
        this.list = newList;
        notifyDataSetChanged();
    }
}