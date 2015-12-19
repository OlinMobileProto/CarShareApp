package com.example.zghadyali.carshareapp.Borrower;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.zghadyali.carshareapp.R;
import com.example.zghadyali.carshareapp.Volley.VolleyRequests;
import com.example.zghadyali.carshareapp.Volley.callback_cars;
import com.example.zghadyali.carshareapp.Volley.callback_requests;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;

//home fragment for the borrower is where the user makes requests for each car that they can borrowg
public class BorrowerHome extends Fragment {

    /**
     * Citing this http://programmers.stackexchange.com/questions/143736/why-do-we-need-private-variables
     * You should be using getters and setters if you need to access these outside the class
     * and making these all private/protected
     */
    public Button now;
    public ListView carsListView;
    public ArrayAdapter carsAdapter;
    //This is only used in onCreateView, you can define it as a local variable there
    private SwipeRefreshLayout swipeRefreshLayout;
    private BorrowerActivity borrowerActivity;

    //Calendar for alertdialog initializations
    private Calendar calendar;
    private int month, year, day, hour, minute;
    private int set_month, set_year, set_day, set_from_hour, set_from_minute, set_to_hour, set_to_minute;

    //Default time
    //All these are unused, remove them
    private int month_now,year_now,day_now,hour_now,minute_now;
    private String starttime_now,endtime_now, date_now;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.borrower_home, container, false);

        borrowerActivity = (BorrowerActivity) getActivity();
        Log.d("BORROWER HOME STATUS: ", "you are now in the borrower home fragment");
        now = (Button) rootview.findViewById(R.id.now_button);
        now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayAlertDialog();
            }
        });

        carsListView = (ListView) rootview.findViewById(R.id.cars_list);
        carsAdapter = new ArrayAdapter<String>(getActivity(), R.layout.text_view, borrowerActivity.carsList);
        carsListView.setAdapter(carsAdapter);
        Log.d("carsList", borrowerActivity.carsList.toString());
        Log.d("carsIds", borrowerActivity.car_ids.toString());

        swipeRefreshLayout = (SwipeRefreshLayout) rootview.findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d("Swipe Refresh", "onRefresh called form SwipeRefreshLayout");
                swipeUpdate();
            }
        });

        Log.d("Length of borrow list", String.valueOf(borrowerActivity.len));
        /**
         * BorrowerActivity.len should be private and should have a getter in order to
         * access it
         */
        if (borrowerActivity.len == 0){
            TextView context = (TextView) rootview.findViewById(R.id.context);
            //This should be in Strings.xml
            context.setText("You are not approved to borrow any cars right now.");
            now.setVisibility(View.GONE);
        } else {
            //Remove this log
            Log.d("HERE", "jiu");
            carsListView = (ListView) rootview.findViewById(R.id.cars_list);
            //For viewability sake youll want to break this into a few lines
            CarsListCustomAdapter adapter = new CarsListCustomAdapter(
                    /**
                     * You called getActivity() earlier in this method and assigned it to a variable
                     * so why not reference that variable here instead of calling getActivity().
                     * While this is functional, all of these should also be private with getters
                     * because of the reason I posted before
                     */
                    ((BorrowerActivity) getActivity()).name,
                    ((BorrowerActivity) getActivity()).profileID,
                    ((BorrowerActivity) getActivity()).car_ids,
                    ((BorrowerActivity) getActivity()).carsList,
                    BorrowerHome.this, getActivity());
            carsListView.setAdapter(adapter);
        }

        return rootview;
    }


    //THis method is unused, you should remove it
    public void displayBeginTripDialog(String carId){
        LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        final View alertLayout = inflater.inflate(R.layout.begin_trip, null);

        final EditText parkLocation = (EditText) alertLayout.findViewById(R.id.parked_location);
        parkLocation.setInputType(InputType.TYPE_NULL);
        inputManager.hideSoftInputFromWindow(parkLocation.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        final EditText keyLocation = (EditText) alertLayout.findViewById(R.id.key_location);
        keyLocation.setInputType(InputType.TYPE_NULL);
        inputManager.hideSoftInputFromWindow(keyLocation.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        final VolleyRequests handler = new VolleyRequests(getActivity().getApplicationContext());
        handler.getcarinfo(new callback_cars() {
            @Override
            public void callback(JSONObject cars) {
                Log.d("JSON CARS", cars.toString());
                final JSONObject car = cars;
                try {
                    /**
                     * Rather than casting each of these you can call
                     * car.getString("parkedLocation")
                     */
                    final String parked = (String) car.get("parkedLocation");
                    final String keys = (String) car.get("keysLocation");
                    parkLocation.setText(parked);
                    keyLocation.setText(keys);
                } catch (JSONException e) {
                    Log.e("Error: ", e.getMessage());
                }
            }
        }, carId);

        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setTitle("Begin your trip ");
        alert.setView(alertLayout);
        alert.setCancelable(true);
        alert.setPositiveButton("Dismiss", null);
        AlertDialog dialog = alert.create();
        dialog.show();
    }

    private void swipeUpdate() {
        //SETTING DEFAULT DATE AND TIME
        /**
         * Dates are some of the worst designed things in Java, but instead of this
         * consider doing something like this
         * http://stackoverflow.com/questions/428918/how-can-i-increment-a-date-by-one-day-in-java
         * the SimpleDataFormat should really be used to generate the string values of dates
         */
        calendar = Calendar.getInstance();
        year_now = calendar.get(Calendar.YEAR);
        month_now = calendar.get(Calendar.MONTH);
        day_now = calendar.get(Calendar.DAY_OF_MONTH);

        hour_now = calendar.get(Calendar.HOUR_OF_DAY);
        minute_now = calendar.get(Calendar.MINUTE);

        date_now = month + "/" + day + "/" + year;
        starttime_now = hour + ":" + minute;
        endtime_now = (hour+1) + ":" + minute;

        borrowerActivity.updateCarList(date_now,starttime_now,endtime_now);
    }

    public void displayAlertDialog() {
        LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        View alertLayout = inflater.inflate(R.layout.make_request, null);

        final EditText date_request = (EditText) alertLayout.findViewById(R.id.Date_request);
        date_request.setInputType(InputType.TYPE_NULL);
        inputManager.hideSoftInputFromWindow(date_request.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        final EditText from_request = (EditText) alertLayout.findViewById(R.id.from_request);
        from_request.setInputType(InputType.TYPE_NULL);
        inputManager.hideSoftInputFromWindow(from_request.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        final EditText to_request = (EditText) alertLayout.findViewById(R.id.to_request);
        to_request.setInputType(InputType.TYPE_NULL);
        inputManager.hideSoftInputFromWindow(to_request.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        final LinearLayout optmessagetext = (LinearLayout) alertLayout.findViewById(R.id.optmessagesection);
        optmessagetext.setVisibility(View.GONE);

        //Same thing with the dates here
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        date_request.setText((month + 1) + "/" + day + "/" + year);
        set_year = year;
        set_month = month;
        set_day = day;


        date_request.setOnClickListener(new View.OnClickListener() {
            /**
             * Just the date stuff again with this. Dates are actually the dumbest thing
             * in java though
             */
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
                            //setYear will always equal setYear
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
                            Toast toast = Toast.makeText(getActivity(), "The date you chose is not valid", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }
                };
                DatePickerDialog mDatePickerDialog = new DatePickerDialog(getActivity(), myDateListener, year, month, day);
                //What is this for?
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
                            Toast toast = Toast.makeText(getActivity(), "The time you chose is not valid", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }
                };
                TimePickerDialog mTimePickerDialog = new TimePickerDialog(getActivity(), mTimeListener,  set_from_hour, set_from_minute, false);
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
                            Toast toast = Toast.makeText(getActivity(), "The time you chose is not valid", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }
                };
                TimePickerDialog diffTimePickerDialog = new TimePickerDialog(getActivity(), diffTimeListener, set_from_hour, set_from_minute, false);
                diffTimePickerDialog.show();
            }
        });

        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        //THis should be string.xml
        alert.setTitle("Set Time ");
        alert.setView(alertLayout);
        alert.setCancelable(true);
        alert.setNegativeButton("Cancel", null);


        alert.setPositiveButton("Set", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String date = date_request.getText().toString();
                //This can also be done with Dates
                final String from = set_from_hour + ":" + set_from_minute;
                final String to = set_to_hour + ":" + set_to_minute;

                final VolleyRequests handler = new VolleyRequests(getActivity().getApplicationContext());
                handler.getavailablecars(new callback_requests() {
                    @Override
                    public void callback(JSONArray cars) {
                        Log.d("available cars: ", (cars).toString());
                        borrowerActivity.updateCarList(date, from, to);
                        carsAdapter.notifyDataSetChanged();
                    }
                }, ((BorrowerActivity) getActivity()).profileID, date, from, to);

            }
        });
        AlertDialog dialog = alert.create();
        dialog.show();
    }

    /**
     * Using dates would make this method pretty much unnecessary
     */
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
