package com.example.toko;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class adminMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);
    }

    public void changeMenu(View view) {
        Intent mainActivity = null;
        if(view == findViewById(R.id.buttonChangeMenuProduct)) {
            mainActivity = new Intent(this, AdminAddEditMenuProduct.class);
            startActivity(mainActivity);
        }
        else if(view == findViewById(R.id.buttonChangeMenuPrice)) {
            mainActivity = new Intent(this, AdminEditHarga.class);
            startActivity(mainActivity);
        }
    }
}
