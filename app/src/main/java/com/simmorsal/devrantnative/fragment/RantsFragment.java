package com.simmorsal.devrantnative.fragment;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.simmorsal.devrantnative.R;
import com.simmorsal.devrantnative.adapter.RantAdapter;
import com.simmorsal.devrantnative.animations.RantLoadingAnimation;
import com.simmorsal.devrantnative.model.RantModel;
import com.simmorsal.devrantnative.utils.ConstValues;
import com.simmorsal.devrantnative.utils.Tools;
import com.simmorsal.devrantnative.volley.OnVolleyWithErrorListener;
import com.simmorsal.devrantnative.volley.SimpleVolley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;


public class RantsFragment extends Fragment {

    public RantsFragment() {}

    RantLoadingAnimation rantLoadingAnimation;
    View view;
    RecyclerView rv;

    List<RantModel> data;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_rants, container, false);

        initializer();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            rantLoadingAnimation = new RantLoadingAnimation((RelativeLayout) view.findViewById(R.id.relContainer));
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    getRants();
                }
            }, 0000);
        } else
            getRants();

        return view;
    }

    private void initializer() {
        rv = view.findViewById(R.id.rv);
    }

    private void getRants() {
        String url = ConstValues.RANTS;
        new SimpleVolley()
                .callVolley(getActivity(), null, url, Request.Method.GET)
                .setmOnVolleyWithErrorListener(new OnVolleyWithErrorListener() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getBoolean("success")){
                                data = RantModel.Import(jsonObject.getJSONArray("rants"));
                                runRv();
                                if (rantLoadingAnimation != null)
                                    rantLoadingAnimation.stop();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(VolleyError volleyError, String response) {
                        if (rantLoadingAnimation != null)
                            rantLoadingAnimation.stop();
                    }
                });
    }

    private void runRv() {
        RantAdapter adapter = new RantAdapter(data, getActivity());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rv.setAdapter(adapter);
        rv.setLayoutManager(layoutManager);
        adapter.notifyDataSetChanged();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            rv.setAlpha(0);
            rv.setTranslationY(Tools.dpToPx(40));
            rv.animate().alpha(1).translationY(0).setDuration(300).withLayer();
        }
    }
}













