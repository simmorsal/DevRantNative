package com.simmorsal.devrantnative.volley;

import com.android.volley.VolleyError;

public interface OnVolleyWithErrorListener {
    void onResponse(String response);
    void onError(VolleyError volleyError, String response);
}
