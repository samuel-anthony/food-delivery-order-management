package com.example.toko;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class topUpEntry extends AppCompatActivity {
    String imageString = "";
    Uri imageUri;
    Bitmap bitmap = null;
    TextView uploadPictureStat;
    private static final int PICK_IMAGE = 1;

    Bundle bundle;
    JSONObject data_user;
    FrameLayout fragmentPicture;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_up_entry);
        uploadPictureStat = findViewById(R.id.uploadedPictureStatus);
        fragmentPicture = findViewById(R.id.fragmentImage);
        fragmentPicture.setVisibility(View.INVISIBLE);
        Fragment fragment = new image();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragmentImage, fragment);
        ft.commit();
        bundle = getIntent().getExtras();
        try {
            data_user = new JSONObject(bundle.getString("user_data"));
            ((TextView)findViewById(R.id.userBalance)).setText(data_user.getString("balance"));
            ((TextView)findViewById(R.id.greetingUsername)).setText(bundle.getString("username"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void showUploadFragment(View view){
        Intent gallery = new Intent();
        gallery.setType("image/*");
        gallery.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(gallery,"Select Picture"),PICK_IMAGE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE && resultCode == RESULT_OK){
            imageUri = data.getData();
            try{
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imageUri);
                ImageView uploadedPic = findViewById(R.id.uploadedPicture);
                uploadedPic.setImageBitmap(bitmap);
                uploadPictureStat.setText("picture selected, click to view");
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] imageBytes = baos.toByteArray();
                imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    public void showUploadedPicture(View view){
        if(bitmap != null){
            fragmentPicture.setVisibility(View.VISIBLE);
            Toast.makeText(this,"Tap anywhere to dismiss",Toast.LENGTH_LONG).show();
        }
    }

    public void hideUploadedPicture(View view){
        fragmentPicture.setVisibility(View.INVISIBLE);
    }

    public void confirmTopUp(View view){
        EditText editText = findViewById(R.id.nominalTopUp);
        if(!editText.getText().toString().isEmpty() && Integer.valueOf(editText.getText().toString()) > 0 && !imageString.isEmpty()){
            saveContent(this);
        }
        else{
            Toast.makeText(this,"mohon isi nominal dengan benar dan upload gambar sebagai bukti",Toast.LENGTH_LONG).show();
        }
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
                String nominalTopUp = ((EditText)findViewById(R.id.nominalTopUp)).getText().toString();

                try {
                    params.put("user_id",data_user.getString("user_id"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                params.put("nominal",nominalTopUp);
                params.put("imageString",imageString);
                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(ConfigURL.SaveNewTopUp, params);
                return res;
            }
        }

        checkToDB ae = new checkToDB(context);
        ae.execute();
    }
}
