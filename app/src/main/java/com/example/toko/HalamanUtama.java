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
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class HalamanUtama extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_halaman_utama, container, false);
        MainActivity mainActivity = (MainActivity) getActivity();
        JSONObject jsonObject = mainActivity.getDataUser();
        TextView phoneNumberUser = rootView.findViewById(R.id.userBalance);
        TextView usernameField = rootView.findViewById(R.id.greetingUsername);
        usernameField.setText(mainActivity.getUsernameString());
        try {
            phoneNumberUser.setText(String.valueOf(jsonObject.getString("balance")));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return rootView;
    }

    public void changeFragment(View view){
        Fragment fragment = new register();
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragmentMainActivity, fragment);
        ft.commit();
    }
}
