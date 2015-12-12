package com.example.zghadyali.carshareapp.Borrower;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zghadyali.carshareapp.R;
import com.example.zghadyali.carshareapp.SignUp.MainActivity;

import java.util.ArrayList;

public class CarsListCustomAdapter extends BaseAdapter implements ListAdapter {
    private ArrayList<String> list = new ArrayList<String>();
    private Context context;
    private BorrowerHome borrowerHome;

    public CarsListCustomAdapter(ArrayList<String> list, BorrowerHome borrowerHome,Context context) {
        this.list = list;
        this.context = context;
        this.borrowerHome = borrowerHome;
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
            view = inflater.inflate(R.layout.cars_list, null);
        }
        final String car = list.get(position);

        //Handle TextView and display string from your list
        TextView listItemText = (TextView)view.findViewById(R.id.list_item_string);

        // Displays the actual friend name instead of the id
        listItemText.setText(car);

        Button requestButton = (Button)view.findViewById(R.id.request_btn);

        requestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayAlertDialog();
            }
        });

        return view;
    }

    public void displayAlertDialog() {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View alertLayout = inflater.inflate(R.layout.make_request, null);
        final EditText date_request = (EditText) alertLayout.findViewById(R.id.Date_request);
        final EditText from_request = (EditText) alertLayout.findViewById(R.id.from_request);
        final EditText to_request = (EditText) alertLayout.findViewById(R.id.to_request);
        final EditText opt_message = (EditText) alertLayout.findViewById(R.id.message_opt);


        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle("Send Request");
        alert.setView(alertLayout);
        alert.setCancelable(false);
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        alert.setPositiveButton("Send", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // code for matching password
//                try {
                String date = date_request.getText().toString();
                String from = from_request.getText().toString();
                String to = to_request.getText().toString();
                String message = opt_message.getText().toString();
//                } catch (Exception e) {
//                    Toast t =Toast.makeText(context, "Please enter quantity or enter 0 if none", 5000);
//                    t.show();
//                }

                Log.d("DATE", date);
            }
        });
        AlertDialog dialog = alert.create();
        dialog.show();
    }

}