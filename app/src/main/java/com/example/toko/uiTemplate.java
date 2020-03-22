package com.example.toko;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.support.v4.content.res.ResourcesCompat;
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
        return textView;
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
