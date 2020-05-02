package com.example.toko;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class AdminEditHour extends AppCompatActivity {

    View timePickeView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_edit_hour);
        takeData(this);
    }

    public void showTimePicker(View view){
        DialogFragment timePicker = new TimePickerFragment(view);
        timePicker.show(getSupportFragmentManager(), "Date Picker");
        timePickeView = view;
    }

    public void updateJam(View view){
        saveData(this);
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
                    TextView textView = findViewById(R.id.jamPagi);
                    textView.setText(output.getString("jam_pagi"));
                    TextView textView1 = findViewById(R.id.jamSiang);
                    textView1.setText(output.getString("jam_siang"));
                    TextView textView2 = findViewById(R.id.jamMalam);
                    textView2.setText(output.getString("jam_malam"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... v) {
                HashMap<String,String> params = new HashMap<>();

                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(ConfigURL.TakeHourDefault, params);
                return res;
            }
        }

        checkToDB ae = new checkToDB(context);
        ae.execute();
    }

    public void saveData(Context context){
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
                String pagi = ((TextView)findViewById(R.id.jamPagi)).getText().toString();
                String siang = ((TextView)findViewById(R.id.jamSiang)).getText().toString();
                String malam = ((TextView)findViewById(R.id.jamMalam)).getText().toString();
                params.put("jam_pagi",pagi);
                params.put("jam_siang",siang);
                params.put("jam_malam",malam);
                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(ConfigURL.SaveHourDefault, params);
                return res;
            }
        }

        checkToDB ae = new checkToDB(context);
        ae.execute();
    }
}
