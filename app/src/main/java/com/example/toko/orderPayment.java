package com.example.toko;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class orderPayment extends AppCompatActivity {
    ArrayList<HashMap<String,String>> pesanan = new ArrayList<HashMap<String,String>>();
    Bundle bundle;
    JSONObject data_user;
    boolean isVa = false;
    int Total,userBalance = 0,hargaperbox;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundle = getIntent().getExtras();
        pesanan = (ArrayList<HashMap<String,String>>) bundle.getSerializable("order");
        try {
            data_user = new JSONObject(bundle.getString("user_data"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Total = bundle.getInt("total",0);
        hargaperbox = bundle.getInt("harga",0);
        takeData(this);
        setContentView(R.layout.activity_order_payment);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent newActivity = null;
        if(pesanan.get(0).get("tanggal_mulai") == null) {
            newActivity = new Intent(this, orderList.class);
        }else{
            newActivity = new Intent(this, cateringOrderList.class);
        }
        newActivity.putExtra("order",(Serializable) pesanan);
        newActivity.putExtra("user_data",bundle.getString("user_data"));
        startActivity(newActivity);
        finish();
    }


    public void onclickButton(View view){
        if(view == findViewById(R.id.buttonOrder)){
            RadioGroup radioGroup = findViewById(R.id.paymentMethod);
            if(radioGroup.getCheckedRadioButtonId()==R.id.paymentVirtual){
                //checkBalance
                if(userBalance>Total){
                    isVa = true;
                    saveData(this);
                }
                else {
                    Toast.makeText(this,"Saldo Kamu masih belum cukup",Toast.LENGTH_LONG).show();
                }
            }
            else{
                saveData(this);
            }
        }
    }

    public String changeArrayListToString(){
        String returnValue = "";
        for(int i = 0; i < pesanan.size() ; i++) {
            if(pesanan.get(0).get("tanggal_mulai")==null)
                returnValue += pesanan.get(i).get("nama_produk")+","+pesanan.get(i).get("total") + (i == pesanan.size()-1 ? "":"|");
            else{
                int perPaket = Integer.valueOf(pesanan.get(i).get("beda_hari")) * Integer.valueOf(pesanan.get(i).get("beda_hari")) * hargaperbox;
                returnValue += pesanan.get(i).get("nama_produk")+","+pesanan.get(i).get("total") + "," + perPaket + "," + pesanan.get(i).get("tanggal_mulai") + "," + pesanan.get(i).get("tanggal_akhir") + "," + pesanan.get(i).get("waktu") + (i == pesanan.size()-1 ? "":"|");
            }
        }
        return returnValue;
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

                try {
                    params.put("user_id",data_user.getString("user_id"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                params.put("total",String.valueOf(Total));
                params.put("pesanan",changeArrayListToString());
                params.put("isVA",isVa? "1":"0");
                RequestHandler rh = new RequestHandler();
                String res = "";
                if(pesanan.get(0).get("tanggal_mulai") != null){
                    res = rh.sendPostRequest(ConfigURL.SaveNewOrderCatering, params);
                }
                else{
                    rh.sendPostRequest(ConfigURL.SaveNewOrder, params);
                }
                return res;
            }
        }

        checkToDB ae = new checkToDB(context);
        ae.execute();
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
                    userBalance = output.getInt("balance");
                    Toast.makeText(context,output.getString("message"),Toast.LENGTH_LONG).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... v) {
                HashMap<String,String> params = new HashMap<>();
                try {
                    params.put("user_id",data_user.getString("user_id"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(ConfigURL.ValidateBalance, params);
                return res;
            }
        }

        checkToDB ae = new checkToDB(context);
        ae.execute();
    }
}
