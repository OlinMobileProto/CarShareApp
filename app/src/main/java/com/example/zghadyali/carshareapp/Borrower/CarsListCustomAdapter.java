package com.example.zghadyali.carshareapp.Borrower;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.zghadyali.carshareapp.Owner.OwnerActivity;
import com.example.zghadyali.carshareapp.R;
import com.example.zghadyali.carshareapp.SignUp.MainActivity;
import com.example.zghadyali.carshareapp.Volley.VolleyRequests;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class CarsListCustomAdapter extends BaseAdapter implements ListAdapter {
    //Needs borrower name (name), borrower profileID (profileID), list of cars they are allowed to borrow from (cars_ids)
    private String name, profileID;
    private JSONArray cars_ids;

    //List of car names that borrower can borrow from
    private ArrayList<String> list = new ArrayList<String>();
    private Context context;
    private BorrowerHome borrowerHome;

    //Calendar for alertdialog initializations
    private Calendar calendar;
    private int month, year, day, hour, minute;
    private int set_month, set_year, set_day, set_from_hour, set_from_minute, set_to_hour, set_to_minute;

    //Creating the new request initializations
    private JSONObject new_request;
    private String uniqueId;

    public CarsListCustomAdapter(String name, String profileID, JSONArray cars_ids, ArrayList<String> list, BorrowerHome borrowerHome,Context context) {
        this.name = name;
        this.profileID = profileID;
        this.cars_ids = cars_ids;
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

        //Gets the particular CarID that the borrower selects
        String carId = new String();
        try {
            carId = cars_ids.getString(position);
        } catch (JSONException e) {
            Log.e("Error", e.getMessage());
        }
        final String final_carId = carId;

        //Handle TextView and display string from your list
        TextView listItemText = (TextView)view.findViewById(R.id.list_item_string);

        // Displays the actual friend name instead of the id
        listItemText.setText(car);

        Button requestButton = (Button)view.findViewById(R.id.request_btn);

        requestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayAlertDialog(car, final_carId);
            }
        });

        return view;
    }

    public void displayAlertDialog(String car, String carId) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);

        View alertLayout = inflater.inflate(R.layout.make_request, null);

        final String final_carId = carId;
        final EditText date_request = (EditText) alertLayout.findViewById(R.id.Date_request);
        date_request.setInputType(InputType.TYPE_NULL);
        inputManager.hideSoftInputFromWindow(date_request.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        final EditText from_request = (EditText) alertLayout.findViewById(R.id.from_request);
        from_request.setInputType(InputType.TYPE_NULL);
        inputManager.hideSoftInputFromWindow(from_request.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        final EditText to_request = (EditText) alertLayout.findViewById(R.id.to_request);
        to_request.setInputType(InputType.TYPE_NULL);
        inputManager.hideSoftInputFromWindow(to_request.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        final EditText opt_message = (EditText) alertLayout.findViewById(R.id.message_opt);

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        date_request.setText((month + 1) + "/" + day + "/" + year);
        set_year = year;
        set_month = month;
        set_day = day;


        date_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int setYear, int setMonth, int setDay) {
                        if (setYear > year) {
                            date_request.setText((setMonth + 1) + "/" + setDay + "/" + setYear);
                            set_year = setYear;
                            set_month = setMonth;
                            set_day = setDay;
                        } else if (setYear == setYear && setMonth > month) {
                            date_request.setText((setMonth + 1) + "/" + setDay + "/" + setYear);
                            set_year = setYear;
                            set_month = setMonth;
                            set_day = setDay;
                        } else if (setYear == setYear && setMonth == month && setDay >= day) {
                            date_request.setText((setMonth + 1) + "/" + setDay + "/" + setYear);
                            set_year = setYear;
                            set_month = setMonth;
                            set_day = setDay;
                        } else {
                            Toast toast = Toast.makeText(context, "The date you chose is not valid", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }
                };
                DatePickerDialog mDatePickerDialog = new DatePickerDialog(context, myDateListener, year, month, day);
                mDatePickerDialog.getDatePicker().setMinDate(new Date().getTime()-1000);
                mDatePickerDialog.show();

            }
        });

        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);

        displayTime(from_request, hour, minute);
        set_from_hour = hour;
        set_from_minute = minute;

        from_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog.OnTimeSetListener mTimeListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int setHourOfDay, int setMinute) {
                        if (set_year > year) {
                            displayTime(from_request, setHourOfDay, setMinute);
                            set_from_hour = setHourOfDay;
                            set_from_minute = setMinute;
                        } else if (set_year == year && set_month > month) {
                            displayTime(from_request, setHourOfDay, setMinute);
                            set_from_hour = setHourOfDay;
                            set_from_minute = setMinute;
                        } else if (set_year == year && set_month == month && set_day > day) {
                            displayTime(from_request, setHourOfDay, setMinute);
                            set_from_hour = setHourOfDay;
                            set_from_minute = setMinute;
                        } else if (set_year == year && set_month == month && set_day == day && setHourOfDay > hour){
                            displayTime(from_request, setHourOfDay, setMinute);
                            set_from_hour = setHourOfDay;
                            set_from_minute = setMinute;
                        } else if (set_year == year && set_month == month && set_day == day && setHourOfDay == hour && setMinute > minute){
                            displayTime(from_request, setHourOfDay, setMinute);
                            set_from_hour = setHourOfDay;
                            set_from_minute = setMinute;
                        } else {
                            Toast toast = Toast.makeText(context, "The time you chose is not valid", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }
                };
                TimePickerDialog mTimePickerDialog = new TimePickerDialog(context, mTimeListener,  set_from_hour, set_from_minute, false);
                mTimePickerDialog.show();
            }
        });

        to_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog.OnTimeSetListener diffTimeListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int setHourOfDay, int setMinute) {
                        if (setHourOfDay > set_from_hour) {
                            displayTime(to_request, setHourOfDay, setMinute);
                            set_to_hour = setHourOfDay;
                            set_to_minute = setMinute;
                        } else if (setMinute > set_from_minute) {
                            displayTime(to_request, setHourOfDay, setMinute);
                            set_to_hour = setHourOfDay;
                            set_to_minute = setMinute;
                        } else {
                            Toast toast = Toast.makeText(context, "The time you chose is not valid", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }
                };
                TimePickerDialog diffTimePickerDialog = new TimePickerDialog(context, diffTimeListener, set_from_hour, set_from_minute, false);
                diffTimePickerDialog.show();
            }
        });

        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle("Request " + car);
        alert.setView(alertLayout);
        alert.setCancelable(true);
        alert.setNegativeButton("Cancel", null);


        alert.setPositiveButton("Request", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                //Create Unique ID
                uniqueId = UUID.randomUUID().toString();
                Log.d("unique", uniqueId);

                new_request = new JSONObject();
                String date = date_request.getText().toString();
                String from = set_from_hour + ":" + set_from_minute;
                String to = set_to_hour + ":" + set_to_minute;
                String message = opt_message.getText().toString();
                Log.d("DATE", date);

                try {
                    new_request.put("ownerId", final_carId);
                    new_request.put("requestId", uniqueId);
                    new_request.put("date", date);
                    new_request.put("startTime", from);
                    new_request.put("endTime", to);
                    new_request.put("borrowerName", name);
                    new_request.put("borrowerId", profileID);
                    if (message != null) {
                        new_request.put("optmessage", message);
                    } else {
                        new_request.put("optmessage", "");
                    }
                    new_request.put("approved", 0);
                } catch (JSONException e) {
                    Log.e("Error: ", e.getMessage());
                }
                Log.d("request_car_id", final_carId);
                Log.d("full request", new_request.toString());
                VolleyRequests handler = new VolleyRequests(context);
                handler.createrequest(final_carId, new_request);

            }
        });
        AlertDialog dialog = alert.create();
        dialog.show();
    }

    public void displayTime(EditText editText, int hourOfDay, int minute){
        if (hourOfDay < 12) {
            if (hourOfDay == 0){
                if (minute < 10){
                    editText.setText((hourOfDay + 12) + ":" + "0"+ minute + " AM");
                } else{
                    editText.setText((hourOfDay + 12) + ":" + minute + " AM");
                }
            } else {
                if (minute < 10){
                    editText.setText(hourOfDay + ":" + "0"+ minute + " AM");
                } else{
                    editText.setText(hourOfDay + ":" + minute + " AM");
                }
            }
        } else {
            if (hourOfDay == 12){
                if (minute < 10){
                    editText.setText(hourOfDay + ":" + "0"+ minute + " PM");
                } else{
                    editText.setText(hourOfDay + ":" + minute + " PM");
                }
            } else {
                if (minute < 10){
                    editText.setText((hourOfDay-12) + ":" + "0"+ minute + " PM");
                } else{
                    editText.setText((hourOfDay-12) + ":" + minute + " PM");
                }
            }
        }
    }


}