package com.example.toko;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class adminMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        final android.os.Handler handler = new android.os.Handler();
        final int delay = 60000; //milliseconds

        handler.postDelayed(new Runnable(){
            public void run(){
                //do something
                takeUpdateStatus(adminMainActivity.this);
                handler.postDelayed(this, delay);
            }
        }, delay);
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
        else if(view == findViewById(R.id.buttonChangeHour)) {
            mainActivity = new Intent(this, AdminEditHour.class);
            startActivity(mainActivity);
        }
        else if(view == findViewById(R.id.buttonListOrder)) {
            mainActivity = new Intent(this, AdminAturPesanan.class);
            startActivity(mainActivity);
        }
        else if(view == findViewById(R.id.buttonListTopUp)) {
            mainActivity = new Intent(this, AdminTopUpHistoryList.class);
            startActivity(mainActivity);
        }
    }

    public void takeUpdateStatus(Context context){
        class checkToDB extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;
            Context context;
            public checkToDB(Context context){
                this.context = context;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(context,"menyimpan data...","Please wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                try {
                    JSONObject output = new JSONObject(s);
                    JSONArray product = output.getJSONArray("products");
                    for(int i = 0; i<product.length() ; i++){

                        new AlertDialog.Builder(context)
                                .setTitle("Reminder")
                                .setMessage("Your order : "+ output.getString("") + " will delivered on " + output.getString(""))
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                }).show();

                    }

                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... v) {
                HashMap<String,String> params = new HashMap<>();
                RequestHandler rh = new RequestHandler();

                String res = null;
                res = rh.sendPostRequest(ConfigURL.TakePesananByIdForCateringAdmin, params);
                return res;
            }
        }

        checkToDB ae = new checkToDB(context);
        ae.execute();
    }

}
