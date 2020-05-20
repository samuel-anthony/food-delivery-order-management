package com.example.toko;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import org.json.JSONException;
import org.json.JSONObject;


public class recentOrder extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_recent_order, container, false);
        MainActivity mainActivity = (MainActivity) getActivity();
        JSONObject jsonObject = mainActivity.getDataUser();
        mainActivity.setListOrder((LinearLayout)rootView.findViewById(R.id.listOrderOngoing));
        try {
            mainActivity.takeRecentOrder(rootView.getContext(),jsonObject.getString("user_id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return rootView;
    }
}
