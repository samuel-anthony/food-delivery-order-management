package com.example.toko;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Period;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{
    View datePickerView;
    View timePickeView;
    SimpleDateFormat dateFormat;
    LinearLayout listMenu;
    Bundle bundle;
    JSONObject data_user;
    ArrayList<HashMap<String,String>> pesanan = new ArrayList<HashMap<String,String>>();
    ArrayList<Integer> idMenuMaster= new ArrayList<>();
    uiTemplate uiTemplate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fragment fragment = new HalamanUtama();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragmentMainActivity, fragment);
        ft.commit();
        bundle = getIntent().getExtras();
        try {
            data_user = new JSONObject(bundle.getString("user_data"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        uiTemplate = new uiTemplate(this);
        dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        setContentView(R.layout.activity_main);
    }

    public void onchangeMenu(View v){
        Intent newActivity;
        if(v==findViewById(R.id.buttonOrder)){
            newActivity = new Intent(this, orderEntry.class);
            ArrayList<HashMap<String,String>> pesanan = new ArrayList<HashMap<String,String>>();
            newActivity.putExtra("order",(Serializable) pesanan);
            newActivity.putExtra("user_data",bundle.getString("user_data"));
            startActivity(newActivity);
        }
        else if (v==findViewById(R.id.logoTopUp)){
            newActivity = new Intent(this, topUpEntry.class);
            ArrayList<HashMap<String,String>> pesanan = new ArrayList<HashMap<String,String>>();
            newActivity.putExtra("order",(Serializable) pesanan);
            newActivity.putExtra("user_data",bundle.getString("user_data"));
            startActivity(newActivity);
        }
    }
    public void onchangeFragment(View v) throws JSONException {
        if(v == findViewById(R.id.homeMenu)) {
            Fragment fragment = new HalamanUtama();
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.fragmentMainActivity, fragment);
            ft.commit();
        }
        else if(v == findViewById(R.id.profileMenu)) {
            Fragment fragment = new profile();
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.fragmentMainActivity, fragment);
            ft.commit();
        }
        else if(v == findViewById(R.id.cateringMenu)) {
            Fragment fragment = new cateringList();
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.fragmentMainActivity, fragment);
            ft.commit();
        }
        else if(v == findViewById(R.id.cateringFragmentEntry)) {
            Fragment fragment = new cateringEntry();
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.fragmentMainActivity, fragment);
            ft.commit();
        }
        else if(v == findViewById(R.id.cateringFragmentList)) {
            Fragment fragment = new cateringList();
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.fragmentMainActivity, fragment);
            ft.commit();
        }
    }


    public void showDatePicker(View view){
        String selectedDate = ((TextView)view).getText().toString();
        DialogFragment datePicker = new DatePickerFragment(true,false,selectedDate);
        datePicker.show(getSupportFragmentManager(), "Date Picker");
        datePickerView = view;
    }

    public void showTimePicker(View view){
        DialogFragment timePicker = new TimePickerFragment(view);
        timePicker.show(getSupportFragmentManager(), "Date Picker");
        timePickeView = view;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        String currentDateString = dateFormat.format(calendar.getTime());
        TextView a = (TextView) datePickerView;
        a.setText(currentDateString);

    }

    public JSONObject getDataUser(){
        return this.data_user;
    }

    public void setListMenu(LinearLayout view){
        listMenu = view;
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

                boolean flag = false;
                int index = 0;
                for(int i = 0; i < pesanan.size(); i++){
                    int totSame =0;
                    for(int j =0; j < idMenuMaster.size(); j++) {
                        if (pesanan.get(i).get(String.valueOf(idMenuMaster.get(j))).equalsIgnoreCase(map.get(String.valueOf(idMenuMaster.get(j))))) {
                            totSame++;
                        }
                        if(totSame==idMenuMaster.size()){
                            totOrderPrevious = Integer.parseInt(pesanan.get(i).get("total"));
                            flag = true;
                            index = i;
                        }
                    }
                    if(flag)
                        break;
                }
                EditText editText = findViewById(R.id.quantityOrder);
                totOrderPrevious = Integer.parseInt(editText.getText().toString());
                map.put("total",String.valueOf(totOrderPrevious));
                map.put("nama_produk",text);
                map.put("tanggal_mulai",dateFrom.getText().toString());
                map.put("tanggal_akhir",dateTo.getText().toString());
                map.put("waktu",timePicker.getText().toString());

                Date firstDate = null,secondDate = null;
                try {
                    firstDate = dateFormat.parse(dateFrom.getText().toString());
                    secondDate = dateFormat.parse(dateTo.getText().toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                long diffInMillies = Math.abs(secondDate.getTime() - firstDate.getTime());
                long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS)+1;
                map.put("beda_hari", String.valueOf(diff));
                if(flag)
                    pesanan.remove(index);
                pesanan.add(map);


                Intent newActivity;
                newActivity = new Intent(this, cateringOrderList.class);
                newActivity.putExtra("order",(Serializable) pesanan);
                newActivity.putExtra("user_data",bundle.getString("user_data"));
                startActivity(newActivity);
            }        }
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
                    idMenuMaster= new ArrayList<>();
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
