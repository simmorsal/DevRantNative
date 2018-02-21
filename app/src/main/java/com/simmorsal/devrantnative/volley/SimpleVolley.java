package com.simmorsal.devrantnative.volley;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class SimpleVolley {

    public SimpleVolley() {

    }

    /**
     * @param context
     * @param params          can be null
     * @param url
     * @param method
     * @return
     */
    public SimpleVolley callVolley(final Context context, final Map<String, String> params, String url, int method) {
        Log.i("url", url); //TODO COMMENT THIS
        StringRequest stringRequest = new StringRequest(method, url, new Response.Listener<String>() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onResponse(String response) {
                if (mOnVolleyListener != null)
                    mOnVolleyListener.onResponse(response);
                if (mOnVolleyWithErrorListener != null)
                    mOnVolleyWithErrorListener.onResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                String errorText = null;
                if (error.networkResponse != null && error.networkResponse.data != null)
                    errorText = new String(error.networkResponse.data);

                if (errorText != null) {
                    Log.i("VOLLEY ERROR", errorText);
                }

                Toast.makeText(context, "Please try again", Toast.LENGTH_SHORT).show();
                if (mOnVolleyWithErrorListener != null) {
                    mOnVolleyWithErrorListener.onError(error, errorText);
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                if (params != null)
                    return params;
                else
                    return new HashMap<String, String>();
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        VolleySingleton.GetInstance(context).AddToRequestQueue(stringRequest);

        return this;
    }


    OnVolleyListener mOnVolleyListener;

    public void setOnVolleyListener(OnVolleyListener l) {
        mOnVolleyListener = l;
    }

    OnVolleyWithErrorListener mOnVolleyWithErrorListener;

    public void setmOnVolleyWithErrorListener(OnVolleyWithErrorListener l) {
        mOnVolleyWithErrorListener = l;
    }
}



