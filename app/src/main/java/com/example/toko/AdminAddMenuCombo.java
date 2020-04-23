package com.example.toko;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class AdminAddMenuCombo extends AppCompatActivity {

    uiTemplate uiTemplate;
    ArrayList<HashMap<String,String>> listProduct = new ArrayList<HashMap<String,String>>();
    ArrayList<HashMap<String,String>> listProductWillBeSaved = new ArrayList<HashMap<String,String>>();
    LinearLayout scrollViewListProduct, getScrollViewListProductWillBeSaved;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_menu_combo);
        scrollViewListProduct = findViewById(R.id.scrollViewDaftarProduk);
        getScrollViewListProductWillBeSaved = findViewById(R.id.scrollViewDaftarSaved);
        uiTemplate = new uiTemplate(this);
        takeCategory(this);
    }

    public void refresh(HashMap<String,String> map,boolean removeFromlistProduct){
        if(removeFromlistProduct){
            for(int i = 0; i < listProduct.size(); i++){
                if(map.equals(listProduct.get(i))){
                    listProduct.remove(i);
                    break;
                }
            }
            listProductWillBeSaved.add(map);
        }else{
            for(int i = 0; i < listProductWillBeSaved.size(); i++){
                if(map.equals(listProductWillBeSaved.get(i))){
                    listProductWillBeSaved.remove(i);
                    break;
                }
            }
            listProduct.add(map);
        }
        scrollViewListProduct.removeAllViews();
        getScrollViewListProductWillBeSaved.removeAllViews();
        for(int i = 0; i < listProduct.size() ; i++){
            LinearLayout container = uiTemplate.createLinearLayout(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT,10f,10f,false,false,10,5,10,5);
            container.setBackgroundResource(R.drawable.round_edit_text_black);
            TextView textView = uiTemplate.createTextView(listProduct.get(i).get("nama_produk"),R.font.pacifico);
            final int index = i;
            container.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    refresh(listProduct.get(index),true);
                }
            });
            container.addView(textView);
            scrollViewListProduct.addView(container);
        }
        for(int i = 0; i < listProductWillBeSaved.size() ; i++){

            LinearLayout container = uiTemplate.createLinearLayout(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT,10f,10f,false,false,10,5,10,5);
            container.setBackgroundResource(R.drawable.round_edit_text_black);
            TextView textView = uiTemplate.createTextView(listProductWillBeSaved.get(i).get("nama_produk"),R.font.pacifico);
            final int index = i;
            container.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    refresh(listProductWillBeSaved.get(index),false);
                }
            });
            container.addView(textView);
            getScrollViewListProductWillBeSaved.addView(container);
        }
    }

    public void takeCategory(Context context){
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
                    JSONArray resultList = output.getJSONArray("categories");
                    for(int i = 0; i<resultList.length() ; i++){
                        JSONObject jo = resultList.getJSONObject(i);
                        final HashMap<String,String> map = new HashMap<>();
                        final String master_id = jo.getString("id");
                        final String category_name = jo.getString("nama_produk");
                        map.put("id",master_id);
                        map.put("nama_produk",category_name);
                        LinearLayout container = uiTemplate.createLinearLayout(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT,10f,10f,false,false,10,5,10,5);
                        container.setBackgroundResource(R.drawable.round_edit_text_black);
                        TextView textView = uiTemplate.createTextView(jo.getString("nama_produk"),R.font.pacifico);
                        container.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {
                                refresh(map,true);
                            }
                        });
                        container.addView(textView);
                        scrollViewListProduct.addView(container);
                        listProduct.add(map);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... v) {
                HashMap<String,String> params = new HashMap<>();

                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(ConfigURL.TakeProductCategories, params);
                return res;
            }
        }

        checkToDB ae = new checkToDB(context);
        ae.execute();
    }



    public void buttonSave(View view){
        if(listProductWillBeSaved.size()>0){
            String namaPaket = ((EditText)findViewById(R.id.namaPaket)).getText().toString();
            if(namaPaket.isEmpty()){
                Toast.makeText(this,"Mohon masukan semua data",Toast.LENGTH_SHORT).show();
            }
            else{
                saveContent(this);
            }
        }
        else{
            Toast.makeText(this,"Mohon pilih paling tidak 1 menu",Toast.LENGTH_SHORT).show();
        }
    }



    public String convertArrayListToString(ArrayList<HashMap<String,String>> a){
        String returningString = "";
        for(int i = 0; i < a.size(); i++){
            HashMap obj = a.get(i);
            if(a.size()==i+1)
                returningString += obj.get("id");
            else
                returningString += obj.get("id")+";";
        }
        return returningString;
    }

    public void saveContent(Context context){
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
                String paket = ((EditText)findViewById(R.id.namaPaket)).getText().toString();
                String listproduk = convertArrayListToString(listProductWillBeSaved);

                params.put("paket",paket);
                params.put("listproduk",listproduk);
                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(ConfigURL.SaveNewCombo, params);
                return res;
            }
        }

        checkToDB ae = new checkToDB(context);
        ae.execute();
    }
}
