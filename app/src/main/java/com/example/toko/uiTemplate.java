package com.example.toko;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.support.v4.content.res.ResourcesCompat;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;



public class uiTemplate {
    Context context;
    public uiTemplate (Context context){
        this.context = context;
    }

    public LinearLayout createLinearLayout(int width, int height, float layoutWeight, float weightSum,boolean isVerticalLayout,boolean isRelativeCenter, int left, int top, int right, int bottom){
        LinearLayout container = new LinearLayout(context);
        LinearLayout.LayoutParams params;
        RelativeLayout.LayoutParams param2;
        if(isRelativeCenter) {
            param2 = new RelativeLayout.LayoutParams(width, height);
            param2.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
            container.setLayoutParams(param2);
        }
        else {
            if (width == 0 || height == 0) {
                params = new LinearLayout.LayoutParams(width, height, layoutWeight);
            } else {
                params = new LinearLayout.LayoutParams(width, height);
            }
            params.setMargins(left, top, right, bottom);
            container.setLayoutParams(params);
            container.setWeightSum(weightSum);
            if (isVerticalLayout)
                container.setOrientation(LinearLayout.VERTICAL);
        }
        return container;
    }

    public TextView createTextView(String textContent,int id){
        TextView textView = new TextView(context);
        textView.setText(textContent);
        if(id != -1){
            Typeface face = ResourcesCompat.getFont(context, id);
            textView.setTypeface(face);
        }
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        float density = context.getResources().getDisplayMetrics().density;
        int left = (int)(10 * density);
        int top = (int)(5 * density);
        int right = (int)(10 * density);
        int bottom = (int)(5 * density);
        layoutParams.setMargins(left, top, right, bottom);
        textView.setLayoutParams(layoutParams);

        return textView;
    }

    public TextView createTextViewWithoutMargins(String textContent,int id){
        TextView textView = new TextView(context);
        textView.setText(textContent);
        if(id != -1){
            Typeface face = ResourcesCompat.getFont(context, id);
            textView.setTypeface(face);
        }
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        float density = context.getResources().getDisplayMetrics().density;
        int left = (int)(10 * density);
        int top = (int)(0 * density);
        int right = (int)(15 * density);
        int bottom = (int)(0 * density);
        layoutParams.setMargins(left, top, right, bottom);
        textView.setLayoutParams(layoutParams);

        return textView;
    }

    public EditText createEditText(int width,int height,float layoutWeight,int backgroundId,int fontId,float textSize, int hint,int left, int top, int right, int bottom){
        EditText editText = new EditText(context);
        LinearLayout.LayoutParams layoutParams;
        float density = context.getResources().getDisplayMetrics().density;
        if(width==0 ||height ==0){
            layoutParams = new LinearLayout.LayoutParams(width,height,layoutWeight);
        }
        else{
            layoutParams = new LinearLayout.LayoutParams(width,height);
        }

        left = (int)(left * density);
        top = (int)(top * density);
        right = (int)(right * density);
        bottom = (int)(bottom * density);
        layoutParams.setMargins(left, top, right, bottom);
        editText.setLayoutParams(layoutParams);
        if(backgroundId!=-1) {
            editText.setBackgroundResource(backgroundId);
            int paddingDp = 10;
            int paddingPixel = (int)(paddingDp * density);

            editText.setPadding(paddingPixel,0,0,0);
        }
        if(fontId!=-1){
            Typeface face = ResourcesCompat.getFont(context, fontId);
            editText.setTypeface(face);
        }
        if(textSize != -1f)
            editText.setTextSize(textSize);
        if(hint !=-1){
            editText.setHint(hint);
        }
        return editText;
    }

    public ImageView createImageViewOnLinear(int drawableID, int width, int height,int left, int top, int right, int bottom){
        ImageView imageView = new ImageView(context);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width,height);
        layoutParams.setMargins(left, top, right, bottom);
        imageView.setLayoutParams(layoutParams);
        imageView.setImageResource(drawableID);
        return imageView;
    }
    public RadioGroup createRadioGroup(int width, int height,int id ,int left, int top, int right, int bottom){
        RadioGroup radioGroup = new RadioGroup(context);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width,height);
        layoutParams.setMargins(left, top, right, bottom);
        radioGroup.setLayoutParams(layoutParams);
        radioGroup.setId(id);

        return radioGroup;
    }

    public RadioButton createRadioButton(int width, int height, int id ,int left, int top, int right, int bottom,String content){
        RadioButton radioButton = new RadioButton(context);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width,height);
        layoutParams.setMargins(left, top, right, bottom);
        radioButton.setLayoutParams(layoutParams);
        radioButton.setId(id);
        radioButton.setText(content);
        return radioButton;
    }
}
