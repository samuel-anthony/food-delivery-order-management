package com.example.toko;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class CateringReOrder extends AppCompatActivity {
    uiTemplate uiTemplate;
    LinearLayout listMenu;
    ArrayList<HashMap<String,String>> pesanan = new ArrayList<HashMap<String,String>>();
    ArrayList<Integer> idMenuMaster= new ArrayList<>();
    Bundle bundle;
    SimpleDateFormat dateFormat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catering_re_order);

        uiTemplate = new uiTemplate(this);
        listMenu = findViewById(R.id.listMenu);
        bundle = getIntent().getExtras();
        pesanan = (ArrayList<HashMap<String,String>>) bundle.getSerializable("order");
        dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        checkMenu(this);
    }


    @Override
    public void onBackPressed() {
        Intent newActivity = new Intent(this, cateringOrderList.class);
        newActivity.putExtra("order",(Serializable) pesanan);
        newActivity.putExtra("user_data",bundle.getString("user_data"));
        startActivity(newActivity);
        finish();

    }
    public void substractQuantity(View v){
        EditText editText = findViewById(R.id.quantityOrder);
        int value = Integer.parseInt(editText.getText().toString());
        if(value>1)
            --value;
        editText.setText(String.valueOf(value));
    }
    public void addQuantity(View v){
        EditText editText = findViewById(R.id.quantityOrder);
        int value = Integer.parseInt(editText.getText().toString());
        ++value;
        editText.setText(String.valueOf(value));
    }
    public void submitOrder(View v){
        TextView dateFrom = findViewById(R.id.dateCateringFrom);
        TextView dateTo = findViewById(R.id.dateCateringTo);
        TextView timePicker = findViewById(R.id.timeCatering);
        if(!dateFrom.getText().toString().isEmpty() && !dateTo.getText().toString().isEmpty() && !timePicker.getText().toString().isEmpty()){
            Date dateFrom2=null,dateTo2=null;
            try {
                dateFrom2 = dateFormat.parse(dateFrom.getText().toString());
                dateTo2 = dateFormat.parse(dateTo.getText().toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Date today = new Date();

            if(dateFrom2.compareTo(dateTo2) > 0){
                uiTemplate.createPopUpDialog("Error Input","DateTo should be later than DateFrom");
            }
            else if(today.compareTo(dateFrom2) > 0 || today.compareTo(dateFrom2)==0){
                uiTemplate.createPopUpDialog("Invalid value DateFrom","DateFrom should be later than today");
            }
            else{
                int totOrderPrevious = 0;
                String text ="";
                HashMap<String,String> map = new HashMap<>();
                for(int i =0; i < idMenuMaster.size(); i++){
                    RadioGroup radioGroup = findViewById(idMenuMaster.get(i));
                    int idDetail = radioGroup.getCheckedRadioButtonId();
                    RadioButton radioButton = findViewById(idDetail);
                    map.put(String.valueOf(idMenuMaster.get(i)),String.valueOf(idDetail));
                    text += radioButton.getText().toString() + ((i==idMenuMaster.size()-1) ? "" : "+");
                }


                EditText editText = findViewById(R.id.quantityOrder);
                totOrderPrevious = Integer.parseInt(editText.getText().toString());
                map.put("total",String.valueOf(totOrderPrevious));
                map.put("nama_produk",text);
                map.put("tanggal_mulai",dateFrom.getText().toString());
                map.put("tanggal_akhir",dateTo.getText().toString());
                map.put("waktu",timePicker.getText().toString());
                pesanan.add(map);


                Intent newActivity;
                newActivity = new Intent(this, orderList.class);
                newActivity.putExtra("order",(Serializable) pesanan);
                newActivity.putExtra("user_data",bundle.getString("user_data"));
                startActivity(newActivity);
            }
        }
        else{
            uiTemplate.createPopUpDialog("Missing Required field","Mohon isi semua kolom");
        }
    }
    public void checkMenu(Context context){
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
                        JSONArray category = product.getJSONArray(i);
                        LinearLayout linearLayout = uiTemplate.createLinearLayout(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT,10f,10f,true,false,5,5,5,5);
                        TextView judul = uiTemplate.createTextView(category.getJSONObject(0).getString("nama_produk"),R.font.pacifico);
                        judul.setTextSize(20f);
                        linearLayout.addView(judul);
                        RadioGroup radioGroup = uiTemplate.createRadioGroup(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT,i,10,0,5,0);
                        boolean ischecked = false;
                        for(int j = 0; j<category.length() ; j++){

                            JSONObject detail = category.getJSONObject(j);
                            RadioButton radioButton = uiTemplate.createRadioButton(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT,detail.getInt("id"),0,0,0,0,detail.getString("nama_produk_detail"));
                            if(detail.getBoolean("is_available")){
                                radioGroup.addView(radioButton);
                                if(!ischecked){
                                    radioButton.setChecked(true);
                                    ischecked = true;
                                }
                            }

                        }
                        linearLayout.addView(radioGroup);
                        listMenu.addView(linearLayout);
                        idMenuMaster.add(i);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... v) {
                HashMap<String,String> params = new HashMap<>();

                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(ConfigURL.TakeProductAll, params);
                return res;
            }
        }

        checkToDB ae = new checkToDB(context);
        ae.execute();
    }
}
