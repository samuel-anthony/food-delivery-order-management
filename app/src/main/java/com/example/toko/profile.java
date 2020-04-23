package com.example.toko;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;


public class profile extends Fragment {
    public profile() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        MainActivity mainActivity = (MainActivity) getActivity();
        JSONObject jsonObject = mainActivity.getDataUser();
        EditText phoneNumberUser = rootView.findViewById(R.id.phoneProfile);
        try {
            phoneNumberUser.setText(String.valueOf(jsonObject.getString("phone_number")));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return rootView;
    }

}
