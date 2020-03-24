package com.example.toko;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class AdminAddEditMenuProduct extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_edit_menu_product);
    }

    public void addNewProduct(View view){
        if(view == findViewById(R.id.addMenuProductText)||view == findViewById(R.id.addMenuProductButton)) {
            Intent mainActivity = null;
            mainActivity = new Intent(this, AdminAddEditMenuProduct.class);
            startActivity(mainActivity);
        }
    }
}
