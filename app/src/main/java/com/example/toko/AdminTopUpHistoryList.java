package com.example.toko;

import androidx.appcompat.app.AppCompatActivity;

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

public class AdminTopUpHistoryList extends AppCompatActivity {
    LinearLayout listTopUpHistory;
    uiTemplate uiTemplate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_top_up_history_list);
        listTopUpHistory = findViewById(R.id.listTopUp);
        uiTemplate = new uiTemplate(this);
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
                    JSONArray product = output.getJSONArray("products");
                    LinearLayout linearLayout = uiTemplate.createLinearLayout(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT,10f,10f,true,false,5,5,5,5);
                    TextView judul = uiTemplate.createTextView("list top up",R.font.pacifico);
                    judul.setTextSize(20f);
                    linearLayout.addView(judul);
                    for(int j = 0; j<product.length() ; j++){
                        final JSONObject detail = product.getJSONObject(j);
                        LinearLayout bigContainer =uiTemplate.createLinearLayout(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT,10f,10f,true,false,5,10,5,10);
                        TextView username = uiTemplate.createTextView(detail.getString("username"),R.font.pacifico);
                        TextView nominal = uiTemplate.createTextView("Rp "+detail.getString("nominal"),R.font.pacifico);
                        TextView tanggal_request = uiTemplate.createTextView(detail.getString("tanggal_request"),R.font.pacifico);
                        bigContainer.addView(username);
                        bigContainer.addView(nominal);
                        bigContainer.addView(tanggal_request);
                        bigContainer.setBackgroundResource(R.drawable.round_edit_text_black);
                        linearLayout.addView(bigContainer);
                        bigContainer.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {
                                Intent newActivity;
                                newActivity = new Intent(context, AdminTopUpHistory.class);
                                try {
                                    newActivity.putExtra("id", detail.getString("id"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                startActivity(newActivity);
                                finish();
                            }
                        });
                    }
                    listTopUpHistory.addView(linearLayout);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... v) {
                HashMap<String,String> params = new HashMap<>();
                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(ConfigURL.TakeTopUpHistoryList, params);
                return res;
            }
        }

        checkToDB ae = new checkToDB(context);
        ae.execute();
    }
}
