package com.example.toko;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class AdminAddEditMenuProduct extends AppCompatActivity {

    LinearLayout scrollViewCategory;
    uiTemplate uiTemplate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_edit_menu_product);
        scrollViewCategory = findViewById(R.id.scrollViewDaftarKategori);
        uiTemplate = new uiTemplate(this);
        takeCategory(this);
    }

    public void addNewProduct(View view){
        if(view == findViewById(R.id.addMenuProductText)||view == findViewById(R.id.addMenuProductButton)) {
            Intent mainActivity = null;
            mainActivity = new Intent(this, AdminAddMenuProduct.class);
            startActivity(mainActivity);
            finish();
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
                        final String master_id = jo.getString("id");
                        final String category_name = jo.getString("nama_produk");
                        LinearLayout container = uiTemplate.createLinearLayout(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT,10f,10f,false,false,10,5,10,5);
                        container.setBackgroundResource(R.drawable.round_edit_text_black);
                        TextView textView = uiTemplate.createTextView(jo.getString("nama_produk"),R.font.pacifico);
                        container.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {
                                Intent editActivity = new Intent(context, AdminEditMenuProduct.class);
                                editActivity.putExtra("master_id",master_id);
                                editActivity.putExtra("category_name",category_name);
                                startActivity(editActivity);
                            }
                        });
                        container.addView(textView);
                        scrollViewCategory.addView(container);

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
}
