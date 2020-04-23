package com.example.toko;

import android.content.Intent;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    Bundle bundle;
    JSONObject data_user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fragment fragment = new HalamanUtama();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragmentMainActivity, fragment);
        ft.commit();
        bundle = getIntent().getExtras();
        try {
            data_user = new JSONObject(bundle.getString("user_data"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        setContentView(R.layout.activity_main);
    }

    public void onchangeMenu(View v){
        Intent newActivity;
        if(v==findViewById(R.id.buttonOrder)){
            newActivity = new Intent(this, orderEntry.class);
            ArrayList<HashMap<String,String>> pesanan = new ArrayList<HashMap<String,String>>();
            newActivity.putExtra("order",(Serializable) pesanan);
            newActivity.putExtra("user_data",bundle.getString("user_data"));
            startActivity(newActivity);
        }
    }
    public void onchangeFragment(View v) throws JSONException {
        if(v == findViewById(R.id.homeMenu)) {
            Fragment fragment = new HalamanUtama();
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.fragmentMainActivity, fragment);
            ft.commit();
        }
        else if(v == findViewById(R.id.profileMenu)) {
            Fragment fragment = new profile();
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.fragmentMainActivity, fragment);
            ft.commit();

        }
    }

    public JSONObject getDataUser(){
        return this.data_user;
    }
}
