package com.example.toko;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{
    View datePickerView, phoneProfileView,addressProfileView;
    View timePickeView;
    SimpleDateFormat dateFormat;
    LinearLayout listMenu,listOrder;
    Bundle bundle;
    JSONObject data_user;
    ArrayList<HashMap<String,String>> pesanan = new ArrayList<HashMap<String,String>>();
    ArrayList<Integer> idMenuMaster= new ArrayList<>();
    uiTemplate uiTemplate;
    String menu ="",usernameString;
    ArrayList<HashMap<String,String>> pesananList = new ArrayList<HashMap<String,String>>();
    private android.content.SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;
    boolean isDefaultTimeCustom = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fragment fragment = new HalamanUtama();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragmentMainActivity, fragment);
        ft.commit();
        bundle = getIntent().getExtras();
        usernameString = bundle.getString("username");
        try {
            data_user = new JSONObject(bundle.getString("user_data"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        uiTemplate = new uiTemplate(this);
        dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        mPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        mEditor = mPreferences.edit();
        setContentView(R.layout.activity_main);

        final android.os.Handler handler = new android.os.Handler();
        final int delay = 60000; //milliseconds

        handler.postDelayed(new Runnable(){
            public void run(){
                //do something
                try {
                    takeUpdateStatus(MainActivity.this,data_user.getString("user_id"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                handler.postDelayed(this, delay);
            }
        }, delay);
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
            newActivity.putExtra("username",usernameString);
            startActivity(newActivity);
        }
        else if(v == findViewById(R.id.buttonLogOut)){
            mEditor.putString("login_id","");
            mEditor.commit();
            newActivity = new Intent(this, LoginRegister.class);
            startActivity(newActivity);
            finish();
        }
        else if(v == findViewById(R.id.buttonOrderPrime)){
            newActivity = new Intent(this, reorderList.class);
            HashMap<String,String> map = new HashMap<>();
            pesananList =new ArrayList<HashMap<String,String>>();
            map.put("nama_produk",menu);
            map.put("total",((EditText)findViewById(R.id.quantityOrder)).getText().toString());
            pesananList.add(map);
            newActivity.putExtra("order",(Serializable)pesananList);
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
        } else if(v == findViewById(R.id.activityMenu)) {
            Fragment fragment = new recentOrder();
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.fragmentMainActivity, fragment);
            ft.commit();
        } else if(v == findViewById(R.id.primeMenu)) {
            Fragment fragment = new primeEntry();
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.fragmentMainActivity, fragment);
            ft.commit();
        }
    }

    public void setPhoneProfileView(View phoneProfileView){
        this.phoneProfileView = phoneProfileView;
    }

    public void setAddressProfileView(View addressProfileView){
        this.addressProfileView = addressProfileView;
    }

    public void buttonUpdateProfile(View view){
        updateProfile(this);
    }
    public void showDatePicker(View view){
        String selectedDate = ((TextView)view).getText().toString();
        DialogFragment datePicker = new DatePickerFragment(true,false,selectedDate);
        datePicker.show(getSupportFragmentManager(), "Date Picker");
        datePickerView = view;
    }

    public void showTimePicker(View view){
        if(isDefaultTimeCustom){
            DialogFragment timePicker = new TimePickerFragment(view);
            timePicker.show(getSupportFragmentManager(), "Date Picker");
            timePickeView = view;
        }
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


    public String getUsernameString(){
        return this.usernameString;
    }

    public JSONObject getDataUser(){
        return this.data_user;
    }

    public void setListMenu(LinearLayout view){
        listMenu = view;
    }


    public void setListOrder(LinearLayout view){
        listOrder = view;
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
            uiTemplate.createPopUpDialog("Missing Required field","Please Fill All The Blank!");
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
                    isDefaultTimeCustom = false;
                    JSONObject timeDefault = output.getJSONObject("response");
                    final ArrayList<String> arrayListProject = new ArrayList<String>();
                    arrayListProject.add("morning");
                    arrayListProject.add("afternoon");
                    arrayListProject.add("evening");
                    arrayListProject.add("custom");
                    final ArrayList<String> arrayListTime = new ArrayList<String>();
                    arrayListTime.add(timeDefault.getString("morning"));
                    arrayListTime.add(timeDefault.getString("afternoon"));
                    arrayListTime.add(timeDefault.getString("evening"));
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context,android.R.layout.simple_selectable_list_item, arrayListProject);
                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    Spinner spinnerProject = findViewById(R.id.defaultTime);
                    spinnerProject.setAdapter(arrayAdapter);
                    spinnerProject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            isDefaultTimeCustom = position < 3 ? false : true;
                            if(!isDefaultTimeCustom)
                                ((TextView)findViewById(R.id.timeCatering)).setText(arrayListTime.get(position));
                        }
                        @Override
                        public void onNothingSelected(AdapterView <?> parent) {
                            Toast.makeText(parent.getContext(), "Nothing Selected: ",    Toast.LENGTH_LONG).show();
                        }
                    });
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



    public void takeRecentOrder(Context context,String user_id){
        class checkToDB extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;
            Context context;
            String user_id;
            public checkToDB(Context context,String user_id){
                this.context = context;
                this.user_id = user_id;
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
                                    newActivity = new Intent(context, reorderDetail.class);
                                    newActivity.putExtra("order", (Serializable)changeJSONOBjectToMap(detail));
                                    newActivity.putExtra("user_data",bundle.getString("user_data"));
                                    startActivity(newActivity);
                                }
                            });
                        }
                        listOrder.addView(linearLayout);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... v) {
                HashMap<String,String> params = new HashMap<>();
                params.put("user_id",user_id);
                RequestHandler rh = new RequestHandler();

                String res = null;
                res = rh.sendPostRequest(ConfigURL.TakePesananByIdList, params);
                return res;
            }
        }

        checkToDB ae = new checkToDB(context,user_id);
        ae.execute();
    }


    public void takeUpdateStatus(Context context,String user_id){
        class checkToDB extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;
            Context context;
            String user_id;
            public checkToDB(Context context,String user_id){
                this.context = context;
                this.user_id = user_id;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(context,"Saving Data...","Please wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                try {
                    JSONObject output = new JSONObject(s);
                    if(output.getString("value").equalsIgnoreCase("1")){
                        new AlertDialog.Builder(context)
                                .setTitle("Reminder")
                                .setMessage("Your order : "+ output.getString("") + " will delivered on " + output.getString(""))
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                }).show();
                    }
                    Toast.makeText(context,output.getString("message"),Toast.LENGTH_LONG).show();
                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... v) {
                HashMap<String,String> params = new HashMap<>();
                params.put("user_id",user_id);
                RequestHandler rh = new RequestHandler();

                String res = null;
                res = rh.sendPostRequest(ConfigURL.TakePesananByIdForCatering, params);
                return res;
            }
        }

        checkToDB ae = new checkToDB(context,user_id);
        ae.execute();
    }

    public void updateProfile(Context context){
        class checkToDB extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;
            Context context;
            public checkToDB(Context context){
                this.context = context;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(context,"Saving Data...","Please wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                try {
                    JSONObject output = new JSONObject(s);
                    if(output.getString("value").equalsIgnoreCase("1")){

                        String phoneProfile = ((EditText)(phoneProfileView)).getText().toString();
                        String addressProfile = ((EditText)(addressProfileView)).getText().toString();
                        data_user.put("phone_number",phoneProfile);
                        data_user.put("address",addressProfile);

                        Fragment fragment = new HalamanUtama();
                        FragmentManager fm = getSupportFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        ft.replace(R.id.fragmentMainActivity, fragment);
                        ft.commit();
                    }
                    Toast.makeText(context,output.getString("message"),Toast.LENGTH_LONG).show();
                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... v) {
                HashMap<String,String> params = new HashMap<>();
                String phoneProfile = ((EditText)(phoneProfileView)).getText().toString();
                String addressProfile = ((EditText)(addressProfileView)).getText().toString();
                String user_id = null;
                try {
                    user_id = data_user.getString("user_id");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                params.put("user_id",user_id);
                params.put("phone_no",phoneProfile);
                params.put("address",addressProfile);
                RequestHandler rh = new RequestHandler();

                String res = null;
                res = rh.sendPostRequest(ConfigURL.UpdateProfileUser, params);
                return res;
            }
        }

        checkToDB ae = new checkToDB(context);
        ae.execute();
    }

    public void takePrimeOrder(Context context){
        class checkToDB extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;
            Context context;
            public checkToDB(Context context){
                this.context = context;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(context,"Saving Data...","Please wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                try {
                    JSONObject output = new JSONObject(s);
                    JSONArray product = output.getJSONArray("products");
                    menu = "";
                    for(int i = 0; i<product.length() ; i++){
                        JSONObject detil = product.getJSONObject(i);
                        LinearLayout linearLayout = uiTemplate.createLinearLayout(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT,10f,10f,true,false,5,5,5,5);
                        TextView judul = uiTemplate.createTextView(detil.getString("nama_produk"),R.font.pacifico);
                        judul.setTextSize(15f);
                        linearLayout.addView(judul);
                        listOrder.addView(linearLayout);
                        menu += detil.getString("nama_produk") + (i==product.length()-1 ? "":"+");
                    }
                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... v) {
                HashMap<String,String> params = new HashMap<>();
                RequestHandler rh = new RequestHandler();

                String res = null;
                res = rh.sendPostRequest(ConfigURL.TakePesananForPrime, params);
                return res;
            }
        }

        checkToDB ae = new checkToDB(context );
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
