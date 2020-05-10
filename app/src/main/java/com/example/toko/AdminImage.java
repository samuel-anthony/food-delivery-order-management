package com.example.toko;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class AdminImage extends Fragment {
    Bitmap bitmap = null;
    String id = "";
    HashMap<String,String> map = new HashMap<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_admin_image, container, false);
        AdminTopUpHistory myActivity = (AdminTopUpHistory) getActivity();
        id = myActivity.getTopUpHistoryId();
        takeData(myActivity,rootView,myActivity);

        return rootView;
    }



    public void takeData(Context context,View view, AdminTopUpHistory myActivity){
        class checkToDB extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;
            Context context;
            View rootView;
            AdminTopUpHistory myActivity;
            public checkToDB(Context context,View rootView,AdminTopUpHistory myActivity){
                this.context = context;
                this.rootView = rootView;
                this.myActivity = myActivity;
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
                JSONObject output = null;
                try {
                    output = new JSONObject(s);
                    map.put("id",output.getString("id"));
                    map.put("bukti",output.getString("bukti"));
                    map.put("user_id",output.getString("user_id"));
                    map.put("username",output.getString("username"));
                    map.put("nominal",output.getString("nominal"));
                    map.put("tanggal_request",output.getString("tanggal_request"));
                    byte[] imageBytes = Base64.decode(map.get("bukti"), Base64.DEFAULT);
                    bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                    ImageView uploadedPic = rootView.findViewById(R.id.uploadedPicture);
                    uploadedPic.setImageBitmap(bitmap);
                    myActivity.setBitmap(bitmap);
                    myActivity.setTextView(map);
                    myActivity.setTopup(map);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... v) {
                HashMap<String,String> params = new HashMap<>();
                RequestHandler rh = new RequestHandler();
                params.put("id",id);
                String res = rh.sendPostRequest(ConfigURL.TakeTopUpHistoryById, params);
                return res;
            }
        }

        checkToDB ae = new checkToDB(context,view,myActivity);
        ae.execute();
    }
}
