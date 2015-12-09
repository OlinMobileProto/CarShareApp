package com.example.zghadyali.carshareapp;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by cynchen on 11/17/15.
 */
public class VolleyRequests {

    public RequestQueue queue;

    //Makes the volley request
    public VolleyRequests (Context context){
        queue = Volley.newRequestQueue(context, new OkHttpStack());
    }

    //Post and Patch requests:
    //Person-related requests:
    public void makeperson(String id_name, String ownername, String user_type){
        String url = "http://52.33.226.47/person";
        JSONObject PersonInfo = new JSONObject();
        try{
            PersonInfo.put("facebook_name", ownername);
        } catch (Exception e){
            Log.e("ERROR!", e.getMessage());
        }
        try{
            PersonInfo.put("facebook_id", id_name);
        } catch (Exception e){
            Log.e("ERROR!", e.getMessage());
        }
        try{
            PersonInfo.put("user_type", user_type);
        } catch (Exception e){
            Log.e("ERROR!", e.getMessage());
        }
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                PersonInfo,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error!", error.getMessage());
                    }
                });

        queue.add(request);

    }


    //BORROWER RELATED REQUESTS:
    public void makeborrower(String id_name, String borrower_name){
        String url = "http://52.33.226.47/borrowers";
        JSONObject BorrowerInfo = new JSONObject();
        try{
            BorrowerInfo.put("facebook_id", id_name);
        } catch (Exception e){
            Log.e("ERROR!", e.getMessage());
        }
        Log.d("Owner Name: ", borrower_name);
        try{
            BorrowerInfo.put("borrower_name", borrower_name);
        } catch (Exception e){
            Log.e("ERROR!", e.getMessage());
        }
        Log.d("JSONObject: ", BorrowerInfo.toString());
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                BorrowerInfo,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error!", error.getMessage());
                    }
                });

        queue.add(request);

    }

    public void addtocanborrow(String borrower_name, String owner_name){
        String url = "http://52.33.226.47/borrowers/" + borrower_name + "/canborrow";
        JSONObject my_approved = new JSONObject();
        JSONArray list_owners = new JSONArray();

        try{
            list_owners.put(owner_name);
            my_approved.put("users", list_owners);
        } catch (Exception e){
            Log.e("ERROR!", e.getMessage());
        }
        Log.d("JSONObject: ", my_approved.toString());
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                my_approved,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error!", error.getMessage());
                    }
                });

        queue.add(request);

    }

    //CAR RELATED REQUESTS:
    public void makeownercar(String id_name, String ownername){
        String url = "http://52.33.226.47/cars";
        JSONObject CarInfo = new JSONObject();
        try{
            CarInfo.put("facebook_id", id_name);
        } catch (Exception e){
            Log.e("ERROR!", e.getMessage());
        }
        Log.d("Owner Name: ", ownername);
        try{
            CarInfo.put("owner", ownername);
        } catch (Exception e){
            Log.e("ERROR!", e.getMessage());
        }
        Log.d("JSONObject: ", CarInfo.toString());
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                CarInfo,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error!", error.getMessage());
                    }
                });

        queue.add(request);

    }


    public void addtoapproved(String id_name, JSONArray approved_people){
        String url = "http://52.33.226.47/cars/" + id_name + "/approved";
        JSONObject approved_users = new JSONObject();

        try{
            approved_users.put("user", approved_people);
        } catch (Exception e){
            Log.e("ERROR!", e.getMessage());
        }
        Log.d("JSONObject: ", approved_users.toString());
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                approved_users,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error!", error.getMessage());
                    }
                });

        queue.add(request);

    }

    public void addcarinfo (String id_name, JSONObject owner_details){
        String url = "http://52.33.226.47/cars/" + id_name;

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.PATCH,
                url,
                owner_details,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                    }
                },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("Error!", error.getMessage());
                        }
                    });
        queue.add(request);
    }

    //CREATING AND PATCHING REQUESTS FOR CARS
    //Making a request for borrowing a car:
    public void createrequest (String id_name, JSONObject request_details){
        String url = "http://52.33.226.47/cars/" + id_name + "/requests";
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                request_details,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error!", error.getMessage());
                    }
                });
        queue.add(request);
    }
    //Parching request for borrowing a car:
    public void editrequest (String id_name, JSONObject edit_request_details){
        String url = "http://52.33.226.47/cars/" + id_name + "/requests";

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.PATCH,
                url,
                edit_request_details,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error!", error.getMessage());
                    }
                });
        queue.add(request);
    }

    //GET REQUESTS:
    public void getuser(final Callback callback, String facebook_id) {
        String url = "http://52.33.226.47/person/" + facebook_id;
        JSONObject person = new JSONObject();
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                new JSONObject(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        int user_status = 0;
                        try{
                            if (response.has("Error")){
                                user_status = 0;
                            }
                            else if (response.getString("user_type").equals("owner")){
                                user_status = 1;
                            }
                            else if (response.getString("user_type").equals("borrower")){
                                user_status = 2;
                            }
                        } catch (Exception e){
                            Log.e("Error:", e.getMessage());
                        }
                        callback.callback(user_status);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error!", error.getMessage());
                    }
                });
        queue.add(request);
    }

    public void getcarinfo (final callback_cars callback, String facebook_id) {
        String url = "http://52.33.226.47/cars/" + facebook_id;
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                new JSONObject(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONObject cars = new JSONObject();
                        try{
                            cars = response;
                        } catch (Exception e){
                            Log.e("Error:", e.getMessage());
                        }
                        callback.callback(cars);
                        Log.d("car response: ", cars.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error!", error.getMessage());
                    }
                });
        queue.add(request);
    }

    public void getborrowerinfo (final callback_cars callback, String facebook_id) {
        String url = "http://52.33.226.47/borrowers/" + facebook_id;
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                new JSONObject(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONObject borrower_info = new JSONObject();
                        try{
                            borrower_info = response;
                        } catch (Exception e){
                            Log.e("Error:", e.getMessage());
                        }
                        callback.callback(borrower_info);
                        Log.d("car response: ", borrower_info.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error!", error.getMessage());
                    }
                });
        queue.add(request);
    }
}
