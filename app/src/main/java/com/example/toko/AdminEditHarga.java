package com.example.toko;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class AdminEditHarga extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_edit_harga);
        takeData(this);
    }

    public void takeData(Context context){
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
                    EditText editText = ((EditText)findViewById(R.id.hargaBox));
                    editText.setText(output.getString("harga_produk"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... v) {
                HashMap<String,String> params = new HashMap<>();

                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(ConfigURL.TakePriceBox, params);
                return res;
            }
        }

        checkToDB ae = new checkToDB(context);
        ae.execute();
    }

    public void updatePriceBox(View v){
        String harga = ((EditText)findViewById(R.id.hargaBox)).getText().toString();
        if(isNumeric(harga)){
            saveData(this);
        }
        else{
            Toast.makeText(this,"Mohon masukkan dalam bentuk numeric",Toast.LENGTH_LONG).show();
        }
    }


    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Integer.parseInt(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
    public void saveData(Context context){
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
                    if(output.getString("value").equalsIgnoreCase("1")){
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
                String harga = ((EditText)findViewById(R.id.hargaBox)).getText().toString();

                params.put("harga",harga);
                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(ConfigURL.SavePriceBox, params);
                return res;
            }
        }

        checkToDB ae = new checkToDB(context);
        ae.execute();
    }
}
