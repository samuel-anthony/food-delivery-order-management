package com.example.toko;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class LoginRegister extends AppCompatActivity {
    private android.content.SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;
    public String current_login_id;
    public static final String login_id = "login_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login_register);

        mPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        mEditor = mPreferences.edit();
        loadData();
        if (!current_login_id.equalsIgnoreCase("")) {
            takeUser(this);
        }
        else{
            Fragment fragment = new login();
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.fragmentloginRegister, fragment);
            ft.commit();

        }
    }

    public void loadData(){
        current_login_id = mPreferences.getString(login_id,"");
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
//            Intent mainMenu = new Intent(this,MainActivity.class);
//            mainMenu.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            startActivity(mainMenu);
//            Toast.makeText(LoginRegister.this,"Login Successfully",Toast.LENGTH_SHORT).show();
            checkLogin(this);
        }
        else if(view == findViewById(R.id.buttonRegister)){
            //harusnya nanti ada validasinya
            String usernameString = ((EditText)findViewById(R.id.usernameRegister)).getText().toString();
            String phoneString = ((EditText)findViewById(R.id.phoneRegister)).getText().toString();
            String passwordString = ((EditText)findViewById(R.id.passwordRegister)).getText().toString();
            if(!usernameString.isEmpty() && !phoneString.isEmpty() && !passwordString.isEmpty())
                checkRegister(this);
            else
                Toast.makeText(LoginRegister.this,"Please fill all the blank!",Toast.LENGTH_SHORT).show();
        }
    }



    public void checkLogin(Context context){
        class checkLoginToDB extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;
            Context context;

            public checkLoginToDB(Context context){
                this.context = context;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(context,"Try To Login...","Please wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                try {
                    JSONObject output = new JSONObject(s);
                    if(output.getString("value").equalsIgnoreCase("1")){
                        Intent mainActivity = null;
                        if(output.getString("is_user").equalsIgnoreCase("1")){
                            mainActivity = new Intent(context, MainActivity.class);
                            mainActivity.putExtra("user_data",s);
                            mainActivity.putExtra("username",((EditText)findViewById(R.id.usernameLogin)).getText().toString());
                            mEditor.putString(login_id,((EditText)findViewById(R.id.usernameLogin)).getText().toString());
                            mEditor.commit();
                        }
                        else{
                            mainActivity = new Intent(context,adminMainActivity.class);
                        }
                        startActivity(mainActivity);
                        finish();
                    }
                    Toast.makeText(context,output.getString("message"),Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... v) {
                HashMap<String,String> params = new HashMap<>();
                String username = ((EditText)findViewById(R.id.usernameLogin)).getText().toString();
                String password = ((EditText)findViewById(R.id.passwordLogin)).getText().toString();

                params.put("username",username);
                params.put("password",password);

                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(ConfigURL.Login, params);
                return res;
            }
        }

        checkLoginToDB ae = new checkLoginToDB(context);
        ae.execute();
    }

    public void checkRegister(Context context){
        class checkLoginToDB extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;
            Context context;

            public checkLoginToDB(Context context){
                this.context = context;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(context,"Try To Create An Account...","Please wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                try {
                    JSONObject output = new JSONObject(s);
                    if(output.getString("value").equalsIgnoreCase("1")){
                        Fragment fragment = new login();
                        FragmentManager fm = getSupportFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        ft.replace(R.id.fragmentloginRegister, fragment);
                        ft.commit();
                    }
                    Toast.makeText(context,output.getString("message"),Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... v) {
                HashMap<String,String> params = new HashMap<>();
                String username = ((EditText)findViewById(R.id.usernameRegister)).getText().toString();
                String phone = ((EditText)findViewById(R.id.phoneRegister)).getText().toString();
                String password = ((EditText)findViewById(R.id.passwordRegister)).getText().toString();

                params.put("username",username);
                params.put("phone_number",phone);
                params.put("password",password);

                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(ConfigURL.Register, params);
                return res;
            }
        }

        checkLoginToDB ae = new checkLoginToDB(context);
        ae.execute();
    }


    public void takeUser(Context context){
        class checkLoginToDB extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;
            Context context;

            public checkLoginToDB(Context context){
                this.context = context;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(context,"Mencoba Login...","Please wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                try {
                    JSONObject output = new JSONObject(s);
                    if(output.getString("value").equalsIgnoreCase("1")){
                        Intent mainActivity = null;
                        if(output.getString("is_user").equalsIgnoreCase("1")){
                            mainActivity = new Intent(context, MainActivity.class);
                            mainActivity.putExtra("user_data",s);
                            mainActivity.putExtra("username",current_login_id);
                        }
                        else{
                            mainActivity = new Intent(context,adminMainActivity.class);
                        }
                        startActivity(mainActivity);
                        finish();
                    }
                    Toast.makeText(context,output.getString("message"),Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... v) {
                HashMap<String,String> params = new HashMap<>();

                params.put("username",current_login_id);

                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(ConfigURL.LoginByPreference, params);
                return res;
            }
        }

        checkLoginToDB ae = new checkLoginToDB(context);
        ae.execute();
    }
}
