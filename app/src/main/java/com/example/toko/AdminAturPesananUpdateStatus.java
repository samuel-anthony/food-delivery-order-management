package com.example.toko;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class AdminAturPesananUpdateStatus extends AppCompatActivity {
    HashMap<String,String> pesanan = new HashMap<>();
    Bundle bundle;
    boolean  isCatering;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_atur_pesanan_update_status);
        bundle = getIntent().getExtras();
        pesanan = (HashMap<String,String>) bundle.getSerializable("order");
        isCatering = bundle.getBoolean("isCatering");
        parseHashMapToUI();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent newActivity;
        newActivity = new Intent(this, AdminAturPesanan.class);
        startActivity(newActivity);
        finish();
    }

    public void onclickButton(View view){
        if(view == findViewById(R.id.buttonConfirm)){
            updateStatusConfirmReject(this,true);
        }
        else if(view == findViewById(R.id.buttonReject)){
            updateStatusConfirmReject(this,false);
        }
        else if(view == findViewById(R.id.buttonUpdateStatus)){
            updateStatusDatabase(this);
        }
    }


    public void parseHashMapToUI(){
        TextView textView1 = findViewById(R.id.namePemesan);
        TextView textView2 = findViewById(R.id.addressPemesan);
        TextView textView3 = findViewById(R.id.listOrderPemesan);
        TextView textView4 = findViewById(R.id.statusOrderPemesan);
        TextView textView5 = findViewById(R.id.deliveryTimePemesan);
        TextView textView6 = findViewById(R.id.metodeBayarPemesan);
        textView1.setText(pesanan.get("nama_pemesan"));
        textView2.setText(pesanan.get("alamat"));
        textView3.setText(pesanan.get("detail_pesanan"));
        textView4.setText(pesanan.get("status"));
        textView5.setText(isCatering ? pesanan.get("tanggal_start_order") + " - " + pesanan.get("tanggal_end_order") + "(" +pesanan.get("waktu_order_dikirim") + ")": "Immediate");
        textView6.setText(pesanan.get("pembayaran"));

        if(pesanan.get("status").equalsIgnoreCase("Confirmation Payment by Admin")){
            RelativeLayout somethingToHide = findViewById(R.id.layoutUpdateStatus);
            somethingToHide.setVisibility(View.INVISIBLE);
        }
        else if(!pesanan.get("status").equalsIgnoreCase("Payment Rejected")){
            RelativeLayout somethingToHide = findViewById(R.id.layoutConfirmReject);
            somethingToHide.setVisibility(View.INVISIBLE);
        }
        else{//pembyaran ditolak
            RelativeLayout somethingToHide = findViewById(R.id.layoutConfirmReject);
            somethingToHide.setVisibility(View.INVISIBLE);
            RelativeLayout somethingToHide2 = findViewById(R.id.layoutUpdateStatus);
            somethingToHide2.setVisibility(View.INVISIBLE);
        }

    }

    public void updateStatusDatabase(Context context){
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
                        Intent newActivity;
                        newActivity = new Intent(context, AdminAturPesanan.class);
                        startActivity(newActivity);
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
                params.put("id",pesanan.get("id"));
                params.put("status",pesanan.get("status"));
                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(ConfigURL.UpdateStatusPesanan, params);
                return res;
            }
        }

        checkToDB ae = new checkToDB(context);
        ae.execute();
    }

    public void updateStatusConfirmReject(Context context,boolean isApprove){
        class checkToDB extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;
            Context context;
            boolean isApprove;

            public checkToDB(Context context,boolean isApprove){
                this.context = context;
                this.isApprove = isApprove;
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
                        Intent newActivity;
                        newActivity = new Intent(context, AdminAturPesanan.class);
                        startActivity(newActivity);
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
                params.put("isApprove",isApprove? "1" : "0");
                params.put("id",pesanan.get("id"));
                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(ConfigURL.UpdateStatusPembayaranPesanan, params);
                return res;
            }
        }

        checkToDB ae = new checkToDB(context,isApprove);
        ae.execute();
    }
}
