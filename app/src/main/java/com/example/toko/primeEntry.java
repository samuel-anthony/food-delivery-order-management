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


public class primeEntry extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_prime_entry, container, false);
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.setListOrder((LinearLayout)rootView.findViewById(R.id.primeListMenu));
        mainActivity.takePrimeOrder(rootView.getContext());
        return rootView;
    }
}
