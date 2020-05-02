package com.example.toko;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;

public class AdminAturPesanan extends AppCompatActivity {
    LinearLayout listOrderOngoing;
    uiTemplate uiTemplate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_atur_pesanan);
        Fragment fragment = new aturPesananBiasa();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragmentMainActivity, fragment);
        ft.commit();
        uiTemplate = new uiTemplate(this);
    }

    public void onchangeFragment(View v){
        if(v == findViewById(R.id.usualFragmentList)) {
            Fragment fragment = new aturPesananBiasa();
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.fragmentMainActivity, fragment);
            ft.commit();
        }
        else if(v == findViewById(R.id.cateringFragmentList)) {
            Fragment fragment = new aturPesananCatering();
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.fragmentMainActivity, fragment);
            ft.commit();
        }
    }

    public void setListOrderOngoing(View view){
        this.listOrderOngoing = (LinearLayout)view;
    }

    public void checkMenu(Context context,boolean isCatering){
        class checkToDB extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;
            Context context;
            boolean isCatering;

            public checkToDB(Context context,boolean isCatering){
                this.context = context;
                this.isCatering = isCatering;
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
                        JSONArray category = product.getJSONArray(i);
                        LinearLayout linearLayout = uiTemplate.createLinearLayout(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT,10f,10f,true,false,5,5,5,5);
                        TextView judul = uiTemplate.createTextView(category.getJSONObject(0).getString("status"),R.font.pacifico);
                        judul.setTextSize(20f);
                        linearLayout.addView(judul);
                        for(int j = 0; j<category.length() ; j++){
                            final JSONObject detail = category.getJSONObject(j);
                            LinearLayout bigContainer =uiTemplate.createLinearLayout(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT,10f,10f,true,false,5,10,5,10);
                            TextView detailPesanan = uiTemplate.createTextView(detail.getString("detail_pesanan"),R.font.pacifico);
                            TextView tanggalPemesan = uiTemplate.createTextView(detail.getString("tanggal_order"),R.font.pacifico);
                            TextView namaPemesan = uiTemplate.createTextView(detail.getString("nama_pemesan"),R.font.pacifico);
                            TextView alamat = uiTemplate.createTextView(detail.getString("alamat"),R.font.pacifico);
                            bigContainer.addView(detailPesanan);
                            bigContainer.addView(tanggalPemesan);
                            bigContainer.addView(namaPemesan);
                            bigContainer.addView(alamat);
                            bigContainer.setBackgroundResource(R.drawable.round_edit_text_black);
                            linearLayout.addView(bigContainer);
                            bigContainer.setOnClickListener(new View.OnClickListener()
                            {
                                @Override
                                public void onClick(View v)
                                {
                                    Intent newActivity;
                                    newActivity = new Intent(context, AdminAturPesananUpdateStatus.class);
                                    newActivity.putExtra("order", (Serializable)changeJSONOBjectToMap(detail));
                                    startActivity(newActivity);
                                    finish();
                                }
                            });
                        }
                        listOrderOngoing.addView(linearLayout);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... v) {
                HashMap<String,String> params = new HashMap<>();

                RequestHandler rh = new RequestHandler();

                String res = null;
                if(isCatering)
                    res = rh.sendPostRequest(ConfigURL.TakePesananAllCatering, params);
                else
                    res = rh.sendPostRequest(ConfigURL.TakePesananAll, params);
                return res;
            }
        }

        checkToDB ae = new checkToDB(context,isCatering);
        ae.execute();
    }

    public HashMap<String,String> changeJSONOBjectToMap(JSONObject jsonObject) {
        HashMap<String,String> hashMap = new HashMap<>();
        Iterator<String> keysItr = jsonObject.keys();
        while (keysItr.hasNext()) {
            String key = keysItr.next();
            String value = null;
            try {
                value = jsonObject.getString(key);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            hashMap.put(key,value);
        }
        return hashMap;
    }
}
