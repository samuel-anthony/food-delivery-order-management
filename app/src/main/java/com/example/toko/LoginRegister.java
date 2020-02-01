package com.example.toko;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

public class LoginRegister extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Fragment fragment = new login();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragmentloginRegister, fragment);
        ft.commit();

        setContentView(R.layout.activity_login_register);
    }


    public void changeFragment(View view){
        if(view == findViewById(R.id.fragmentLoginToRegister)) {
            Fragment fragment = new register();
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.fragmentloginRegister, fragment);
            ft.commit();
        }else if(view == findViewById(R.id.fragmentRegisterToLogin)){
            Fragment fragment = new login();
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.fragmentloginRegister, fragment);
            ft.commit();
        }
        else if(view == findViewById(R.id.buttonLogin)){
            Intent mainMenu = new Intent(this,MainActivity.class);
            mainMenu.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(mainMenu);
            Toast.makeText(LoginRegister.this,"Login Successfully",Toast.LENGTH_SHORT).show();
        }
        else if(view == findViewById(R.id.buttonRegister)){
            //harusnya nanti ada validasinya
            Fragment fragment = new login();
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.fragmentloginRegister, fragment);
            ft.commit();
            Toast.makeText(LoginRegister.this,"Register Successfully",Toast.LENGTH_SHORT).show();
        }
    }

}
