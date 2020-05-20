package com.example.toko;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class reorderList extends AppCompatActivity {

    uiTemplate uiTemplate;
    ArrayList<HashMap<String,String>> pesanan = new ArrayList<HashMap<String,String>>();
    Bundle bundle;
    JSONObject data_user;
    LinearLayout listOrder;
    int harga,barang;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reorder_list);
        bundle = getIntent().getExtras();
        pesanan = (ArrayList<HashMap<String,String>>) bundle.getSerializable("order");
        try {
            data_user = new JSONObject(bundle.getString("user_data"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        uiTemplate = new uiTemplate(this);
        listOrder = findViewById(R.id.listOrder);
        changeArrayListToUI();
        takeData(this);
    }

    public void changeArrayListToUI(){
        for(int i = 0; i < pesanan.size() ; i++){
            LinearLayout container = uiTemplate.createLinearLayout(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT,10f,10f,false,false,5,0,5,0);
            LinearLayout judulContainer = uiTemplate.createLinearLayout(0,LinearLayout.LayoutParams.WRAP_CONTENT,7f,10f,false,false,0,0,0,0);
            TextView judulText  = uiTemplate.createTextView(pesanan.get(i).get("nama_produk"),R.font.pacifico);
            LinearLayout jumlah = uiTemplate.createLinearLayout(0,LinearLayout.LayoutParams.WRAP_CONTENT,1f,10f,false,false,0,0,0,0);
            TextView jumlahText = uiTemplate.createTextView(pesanan.get(i).get("total"),R.font.pacifico);

            judulContainer.addView(judulText);
            jumlah.addView(jumlahText);
            container.addView(judulContainer);
            container.addView(jumlah);
            listOrder.addView(container);
        }
    }
    public void changeMenu(View v){
        if(v==findViewById(R.id.buttonOrderMore)){
            Intent newActivity;
            newActivity = new Intent(this, orderEntry.class);
            newActivity.putExtra("order",(Serializable) pesanan);
            newActivity.putExtra("user_data",bundle.getString("user_data"));
            startActivity(newActivity);
            finish();
        }
        else{
            barang = 0;
            for(int i = 0; i < pesanan.size() ; i++) {
                barang += Integer.parseInt(pesanan.get(i).get("total"));
            }
            if(barang>0){
                Intent newActivity;
                newActivity = new Intent(this, orderPayment.class);
                newActivity.putExtra("order",(Serializable) pesanan);
                newActivity.putExtra("total",(int)(harga*barang));
                newActivity.putExtra("harga",(harga));
                newActivity.putExtra("user_data",bundle.getString("user_data"));
                startActivity(newActivity);
                finish();
            }
        }
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
                    harga = Integer.parseInt(output.getString("harga_produk"));
                    int total = 0;
                    for(int i = 0; i < pesanan.size() ; i++) {
                        total += Integer.parseInt(pesanan.get(i).get("total"));
                    }
                    TextView textViewJumlahOrder = findViewById(R.id.jumlahOrder);
                    textViewJumlahOrder.setText("Rp " + String.valueOf(harga*total));
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
}
