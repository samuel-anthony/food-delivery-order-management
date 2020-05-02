package com.example.toko;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;


public class aturPesananCatering extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_atur_pesnan_catering, container, false);
        AdminAturPesanan mainActivity = (AdminAturPesanan) getActivity();
        mainActivity.setListOrderOngoing((LinearLayout)rootView.findViewById(R.id.listOrderOngoing));
        mainActivity.checkMenu(rootView.getContext(),true);
        return rootView;
    }
}
