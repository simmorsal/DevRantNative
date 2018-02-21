package com.simmorsal.devrantnative.volley;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class VolleySingleton {
    static Context context;
    static VolleySingleton instance;
    RequestQueue requestQueue;

    public VolleySingleton(Context context)
    {
        VolleySingleton.context = context;
        requestQueue = GetRequestQueue();

    }

    public static synchronized VolleySingleton GetInstance(Context context)
    {
        if(instance == null)
        {
            instance = new VolleySingleton(context);
        }
        return  instance;
    }

    RequestQueue GetRequestQueue()
    {
        if(requestQueue == null)
        {
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return  requestQueue;
    }

    public <T> void AddToRequestQueue(Request<T> request)
    {
        GetRequestQueue().add(request);
    }

}
