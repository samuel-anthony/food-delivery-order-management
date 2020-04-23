package com.example.toko;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class AdminAddEditMenuCombo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_edit_menu_combo);
    }

    public void addNewCombo(View view){
        if(view == findViewById(R.id.addMenuProductText)||view == findViewById(R.id.addMenuProductButton)) {
            Intent mainActivity = null;
            mainActivity = new Intent(this, AdminAddMenuCombo.class);
            startActivity(mainActivity);
            finish();
        }
    }
}
