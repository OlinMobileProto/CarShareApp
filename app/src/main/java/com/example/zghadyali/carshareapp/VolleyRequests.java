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

import org.json.JSONObject;

import java.util.UUID;

/**
 * Created by cynchen on 11/17/15.
 */
public class VolleyRequests {

    public RequestQueue queue;

    //Makes the volley request
    public VolleyRequests (Context context){
        queue = Volley.newRequestQueue(context);
    }

    public void makeownercar(String id_name, String ownername){
        String url = "http://52.33.226.47/cars";
        JSONObject CarInfo = new JSONObject();
        try{
            CarInfo.put("carId", id_name);
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

    public void doesuserexist(String facebook_id){
        String url = "http://52.33.226.47/person/" + facebook_id;
        JSONObject person = new JSONObject();
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                new JSONObject(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    Log.e("Error: ", error.getMessage());
                    }
                });
        queue.add(request);
    }

    public void getuser(final Callback callback,String facebook_id) {
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
                        Log.e("Error: ", error.getMessage());
                    }
                });
        queue.add(request);
    }
}
