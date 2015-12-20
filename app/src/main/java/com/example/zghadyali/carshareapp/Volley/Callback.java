package com.example.zghadyali.carshareapp.Volley;

/**
 * Created by faridaghadyali on 11/19/15.
 * get user_status integer
 */

/**
 * This callback, callback_cars and ,callback_requests are all essentially the same
 * This could be a cool opportunity to use generics. While they aren't required, it
 * makes your code cleaner and it can be really helpful for some situations.
 * HEres a good tutorial http://www.tutorialspoint.com/java/java_generics.htm
 */
public interface Callback {
    void callback(Integer user_status);
}
