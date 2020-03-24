package com.example.toko;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class adminMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);
    }

    public void changeMenu(View view) {
        if(view == findViewById(R.id.buttonChangeMenuProduct)) {
            Intent mainActivity = null;
            mainActivity = new Intent(this, AdminAddMenuProduct.class);
            startActivity(mainActivity);
        }
    }
}
