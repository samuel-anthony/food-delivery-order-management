package com.example.toko;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
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

public class AdminTopUpHistory extends AppCompatActivity {

    String imageString = "";
    FrameLayout fragmentPicture;
    Bitmap bitmap = null;
    Uri imageUri;
    TextView uploadPictureStat;
    private static final int PICK_IMAGE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_top_up_history);
        fragmentPicture = findViewById(R.id.fragmentImage);
        fragmentPicture.setVisibility(View.INVISIBLE);
        Fragment fragment = new image();
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

    public void hideUploadedPicture(View view){
        fragmentPicture.setVisibility(View.INVISIBLE);
    }
}
