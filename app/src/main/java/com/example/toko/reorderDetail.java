package com.example.toko;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class reorderDetail extends AppCompatActivity {
    HashMap<String,String> pesanan = new HashMap<>();

    ArrayList<HashMap<String,String>> pesananList = new ArrayList<HashMap<String,String>>();
    Bundle bundle;
    JSONObject data_user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reorder_detail);
        bundle = getIntent().getExtras();
        pesanan = (HashMap<String,String>) bundle.getSerializable("order");
        try {
            data_user = new JSONObject(bundle.getString("user_data"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        parseHashMapToUI();
    }

    public void onclickButton(View view){
        String listOrder[] = pesanan.get("detail_pesanan").split("\\|");
        for(int i = 0;i<listOrder.length ; i++){
            HashMap<String,String> map = new HashMap<>();
            String detailOrder[] = listOrder[i].split("\\,");
            map.put("total",detailOrder[1]);
            map.put("nama_produk",detailOrder[0]);
            pesananList.add(map);
        }
        Intent newActivity;
        newActivity = new Intent(this,reorderList.class);
        newActivity.putExtra("order",(Serializable)pesananList);
        newActivity.putExtra("user_data",bundle.getString("user_data"));
        startActivity(newActivity);
        finish();
    }


    public void parseHashMapToUI(){
        TextView textView1 = findViewById(R.id.namePemesan);
        TextView textView2 = findViewById(R.id.addressPemesan);
        TextView textView3 = findViewById(R.id.listOrderPemesan);
        TextView textView4 = findViewById(R.id.statusOrderPemesan);
        textView1.setText(pesanan.get("nama_pemesan"));
        textView2.setText(pesanan.get("alamat"));
        textView3.setText(pesanan.get("detail_pesanan"));
        textView4.setText(pesanan.get("status"));
    }
}
