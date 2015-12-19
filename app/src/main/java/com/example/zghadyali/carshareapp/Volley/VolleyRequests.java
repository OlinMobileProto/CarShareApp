package com.example.zghadyali.carshareapp.Volley;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by cynchen on 11/17/15.
 *
 * ALL OF THE SERVER RELATED REQUESTS AS VOLLEY REQUESTS
 */
public class VolleyRequests {

    public RequestQueue queue;

    //Makes the volley request
    public VolleyRequests (Context context){
        queue = Volley.newRequestQueue(context, new OkHttpStack());
    }

    //Post and Patch requests:
    //Person-related requests:

    /**
     *
     *Create a person schema with name and user type
     */
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
                        //Do NOTHING WITH THE RESPONSE (NO RESPONSE AFTER POST)
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
    /**
     * Make a borrower with their name when they click on borrower
     */
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


    //CAR RELATED REQUESTS:

    /**
     *
     * @param id_name: facebook id of the owner
     * @param ownername: facebook name of the owner
     */
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

    /**
     *
     * @param id_name: facebook id of the car owner
     * @param approved_people: json array of the approved people
     */
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

    /**
     * REMOVE SOMEONE FROM A CAR OWNER"S APPROVED LIST
     */
    public void removefromapproved(String id_name, String borrower_name){
        String url = "http://52.33.226.47/cars/" + id_name + "/approved/" + borrower_name;

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.DELETE,
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
                        Log.e("Error!", error.getMessage());
                    }
                });

        queue.add(request);
    }

    /**
     * Patch request to add more info about the car
     */
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

    /**
     * Post request for a car
     * @param id_name: car owner's id name
     * @param request_details: the request schema info
     */
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

    //Patching request for borrowing a car:

    /**
     * Edit any key in the request schema
     * @param request_id: the request id
     */
    public void editrequest (String request_id, JSONObject edit_request_details){
        String url = "http://52.33.226.47/requests/" + request_id;
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
                        error.printStackTrace();
//                        Log.e("Error!", error.getMessage());
                    }
                });
        queue.add(request);
    }

    /**
     * PATCH ALL REQUESTS THAT HAVE EXPIRED ALREADY
     */
    public void editrequestalldone ( String borrowerId, String date, String endtime){
        String url = "http://52.33.226.47/requests_to_done/" + borrowerId + "/" + date + "/" + endtime;
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.PATCH,
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
                        error.printStackTrace();
//                        Log.e("Error!", error.getMessage());
                    }
                });
        queue.add(request);
    }

    //GET REQUESTS:

    /**
     * Get request for user info
     */
    public void getuser(final Callback callback, String facebook_id) {
        String url = "http://52.33.226.47/person/" + facebook_id;
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

    /**
     * Get request for car info
     */
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

    /**
     * Get borrower information
     */
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

    /**
     * GET ALL REQUESTS THAT THE OWNER IS CONNECTED TO
     */
    public void getownerRequests (final callback_requests callback, String ownerId) {
        String url = "http://52.33.226.47/requests_owner/" + ownerId;
        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                url,
                new JSONObject(),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        JSONArray requestInfo = new JSONArray();
                        try{
                            requestInfo = response;
                        } catch (Exception e){
                            Log.e("Error:", e.getMessage());
                        }
                        callback.callback(requestInfo);
                        Log.d("car response: ", requestInfo.toString());
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

    /**
     * GET ALL REQUESTS RELATED TO BORROWER
     * @param callback
     * @param borrowerId
     */
    public void getborrowerRequests (final callback_requests callback, String borrowerId) {
        String url = "http://52.33.226.47/requests_borrower/" + borrowerId;
        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                url,
                new JSONObject(),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        JSONArray requestInfo = new JSONArray();
                        try{
                            requestInfo = response;
                        } catch (Exception e){
                            Log.e("Error:", e.getMessage());
                        }
                        callback.callback(requestInfo);
                        Log.d("request response: ", requestInfo.toString());
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

    /**
     * Give borrower all cars that are available during a certain time
     */
    public void getavailablecars (final callback_requests callback, String borrowerId, String date, String starttime, String endtime) {
        String url = "http://52.33.226.47/requests_cars/" + borrowerId + "/"+date +"/"+ starttime+"/" + endtime;
        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                url,
                new JSONObject(),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        JSONArray requestInfo = new JSONArray();
                        try{
                            requestInfo = response;
                        } catch (Exception e){
                            Log.e("Error:", e.getMessage());
                        }
                        callback.callback(requestInfo);
                        Log.d("available car resp: ", requestInfo.toString());
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

    /**
     * Get all future/current requests
     */
    public void getfuturecurrentrequests (final callback_requests callback, String ownerId, String date) {
        String url = "http://52.33.226.47/requests_future/" + ownerId + "/"+date;
        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                url,
                new JSONObject(),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        JSONArray requestInfo = new JSONArray();
                        try{
                            requestInfo = response;
                        } catch (Exception e){
                            Log.e("Error:", e.getMessage());
                        }
                        callback.callback(requestInfo);
                        Log.d("available car resp: ", requestInfo.toString());
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