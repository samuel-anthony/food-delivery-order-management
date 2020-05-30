package com.example.toko;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class AdminTopUpHistory extends AppCompatActivity {

    String imageString = "";
    FrameLayout fragmentPicture;
    Bitmap bitmap = null;
    Uri imageUri;
    TextView uploadPictureStat;
    Bundle bundle;
    String topUpHistoryId;
    private static final int PICK_IMAGE = 1;

    HashMap<String,String> topup = new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_top_up_history);
        bundle = getIntent().getExtras();
        topUpHistoryId = bundle.getString("id");
        fragmentPicture = findViewById(R.id.fragmentImage);
        fragmentPicture.setVisibility(View.INVISIBLE);
        Fragment fragment = new AdminImage();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragmentImage, fragment);
        ft.commit();
    }

    public void showUploadedPicture(View view){
        if(bitmap != null){
            fragmentPicture.setVisibility(View.VISIBLE);
            Toast.makeText(this,"Tap anywhere to dismiss",Toast.LENGTH_LONG).show();
        }
    }

    public void setBitmap(Bitmap bitmap){
        this.bitmap = bitmap;
    }

    public String getTopUpHistoryId(){
        return topUpHistoryId;
    }

    public void setTextView(HashMap<String,String> map){
        TextView nama = findViewById(R.id.namePemesan);
        TextView alamat = findViewById(R.id.addressPemesan);

        nama.setText(map.get("username"));
        alamat.setText(map.get("nominal"));
    }

    public void setTopup(HashMap<String,String> topup){
        this.topup = topup;
    }

    public void hideUploadedPicture(View view){
        fragmentPicture.setVisibility(View.INVISIBLE);
    }

    public void onclickButton(View view){
        if(view == findViewById(R.id.buttonConfirm)){
            updateStatusConfirmReject(this,true);
        }
        else if(view == findViewById(R.id.buttonReject)){
            updateStatusConfirmReject(this,false);
        }
    }


    public void updateStatusConfirmReject(Context context, boolean isApprove){
        class checkToDB extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;
            Context context;
            boolean isApprove;

            public checkToDB(Context context,boolean isApprove){
                this.context = context;
                this.isApprove = isApprove;
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
                        Intent mainActivity;
                        mainActivity = new Intent(context, AdminTopUpHistoryList.class);
                        startActivity(mainActivity);
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
                params.put("isApproved",isApprove? "1" : "0");
                params.put("id",topup.get("id"));
                params.put("user_id",topup.get("user_id"));
                params.put("nominal",topup.get("nominal"));
                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(ConfigURL.UpdateStatusTopUpHistory, params);
                return res;
            }
        }

        checkToDB ae = new checkToDB(context,isApprove);
        ae.execute();
    }
}
