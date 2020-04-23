package com.example.toko;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class AdminAddMenuProduct extends AppCompatActivity {
    uiTemplate uiTemplate;
    LinearLayout scrollViewListProduct;
    ArrayList<HashMap<String,String>> listProduct = new ArrayList<HashMap<String,String>>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_menu_product);
        uiTemplate = new uiTemplate(this);
        scrollViewListProduct = (LinearLayout)findViewById(R.id.scrollViewDaftarProduk);
        LinearLayout param1 = uiTemplate.createLinearLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT,12f,10f,false,false,0,10,0,0);
        EditText editTextNamaProduk = uiTemplate.createEditText(0,LinearLayout.LayoutParams.WRAP_CONTENT,4.5f,R.drawable.round_edit_text_black,R.font.pacifico,15f,R.string.AdminCustomMenuNameListProductDetailName,10,0,0,0);
        ImageView buttonRemove = uiTemplate.createImageViewOnLinear(R.drawable.ic_do_not_disturb_on_black_24dp,LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT, 10,0,0,0);
        buttonRemove.setVisibility(View.INVISIBLE);
        LinearLayout container = uiTemplate.createLinearLayout(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT,10f,10f,false,false,10,0,10,0);
        container.setBackgroundResource(R.drawable.round_edit_text_black);
        final TextView textView = uiTemplate.createTextViewWithoutMargins("Hide",R.font.pacifico);
        container.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(textView.getText().toString().equalsIgnoreCase("hide"))
                    textView.setText("Unhide");
                else
                    textView.setText("Hide");
            }
        });
        container.addView(textView);
        param1.addView(editTextNamaProduk);
        param1.addView(container);
        param1.addView(buttonRemove);
        scrollViewListProduct.addView(param1);
    }

    public void addMoreMenu(View view){
        LinearLayout param1 = uiTemplate.createLinearLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT,12f,10f,false,false,0,10,0,0);
        EditText editTextNamaProduk = uiTemplate.createEditText(0,LinearLayout.LayoutParams.WRAP_CONTENT,4.5f,R.drawable.round_edit_text_black,R.font.pacifico,15f,R.string.AdminCustomMenuNameListProductDetailName,10,0,0,0);
        LinearLayout container = uiTemplate.createLinearLayout(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT,10f,10f,false,false,10,0,10,0);
        container.setBackgroundResource(R.drawable.round_edit_text_black);
        final TextView textView = uiTemplate.createTextViewWithoutMargins("Hide",R.font.pacifico);
        container.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(textView.getText().toString().equalsIgnoreCase("hide"))
                    textView.setText("Unhide");
                else
                    textView.setText("Hide");
            }
        });
        container.addView(textView);
        ImageView buttonRemove = uiTemplate.createImageViewOnLinear(R.drawable.ic_do_not_disturb_on_black_24dp,LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT, 10,0,0,0);
        buttonRemove.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ((ViewGroup)v.getParent().getParent()).removeView((ViewGroup)v.getParent());
            }
        });
        param1.addView(editTextNamaProduk);
        param1.addView(container);
        param1.addView(buttonRemove);
        scrollViewListProduct.addView(param1);
    }

    public void buttonSave(View view){
        if(checkContent()){
            String namaKategori = ((EditText)findViewById(R.id.namaKategori)).getText().toString();
            if(namaKategori.isEmpty()){
                Toast.makeText(this,"Mohon masukan semua data",Toast.LENGTH_SHORT).show();
            }
            else{
                saveContent(this);
            }
        }
        else{
            Toast.makeText(this,"Mohon masukan semua data",Toast.LENGTH_SHORT).show();
        }
    }

    public boolean checkContent(){
        listProduct.clear();
        boolean is_no_error = true;
        for(int i = 0; i < scrollViewListProduct.getChildCount() ; i++){
            final View child = scrollViewListProduct.getChildAt(i);
            if(is_no_error){
                if (child instanceof LinearLayout) {
                    HashMap<String,String> map = new HashMap<>();
                    for(int j = 0; j <((LinearLayout) child).getChildCount();j++){
                        final View childOfChild = ((LinearLayout)child).getChildAt(j);
                        if(childOfChild instanceof EditText){
                            String content = ((EditText)childOfChild).getText().toString();
                            if(!content.isEmpty()){
                                map.put("nama_produk",content);
                            }
                            else{
                                is_no_error = false;
                                break;
                            }
                        }else if(childOfChild instanceof TextView){
                            map.put("is_available",((TextView)childOfChild).getText().toString().equalsIgnoreCase("hide") ? "1" : "0");
                        }
                    }
                    listProduct.add(map);
                }
            }
            else{
                break;
            }
        }
        return is_no_error;
    }

    public String convertArrayListToString(ArrayList<HashMap<String,String>> a){
        String returningString = "";
        for(int i = 0; i < a.size(); i++){
            HashMap obj = a.get(i);
            returningString += obj.get("nama_produk")+",";
            if(a.size()==i+1)
                returningString += obj.get("is_available");
            else
                returningString += obj.get("is_available")+";";
        }
        return returningString;
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
                String category = ((EditText)findViewById(R.id.namaKategori)).getText().toString();
                String listproduk = convertArrayListToString(listProduct);

                params.put("category",category);
                params.put("listproduk",listproduk);
                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(ConfigURL.SaveNewProduct, params);
                return res;
            }
        }

        checkToDB ae = new checkToDB(context);
        ae.execute();
    }
}
