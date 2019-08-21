package com.example.cr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.cr.db.Favourite2;
import com.example.cr.db.Ingredient;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * project name：CocktailRecipe
 * className：
 * author：shuoyang
 * Date：2019-08-06 11:10
 */
public class GuideActivity extends AppCompatActivity {

    private TextView jump_btn;
    private TextView submit_btn;
    private SharedPreferences sp;
    private List<CheckBox> checkBoxList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        jump_btn = findViewById(R.id.jump_btn);
        submit_btn = findViewById(R.id.submit_btn);
        try {
            Context otherContext = createPackageContext("com.example.cr", CONTEXT_IGNORE_SECURITY);
            sp = otherContext.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        final String user_id =  sp.getString("user_id","");;
        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (CheckBox checkbox : checkBoxList) {
                    if (checkbox.isChecked()) {
                        Favourite2 rating = new Favourite2();
                        rating.setUser_id(Integer.valueOf(user_id));
                        System.out.println(checkbox.getText().toString()+"fsdsdfdsf   ");
                        rating.setCocktail_id(DataSupport.where("name = ?",checkbox.getText().toString()).findFirst(Ingredient.class).getId());
                        rating.setRating(1);
                        rating.save();
                    }
                }
                startActivity(new Intent(GuideActivity.this,Guide2Activity.class));
                finish();
            }
        });
        jump_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(GuideActivity.this,Guide2Activity.class));
                finish();
            }
        });
        List<Ingredient> recipes = DataSupport.findAll(Ingredient.class);
        Random random = new Random();
        List<Integer> numbers = new ArrayList<>();
        int count = 10;
        int current = 1;
        while(current<count){
            int x = random.nextInt(recipes.size());
            while (numbers.contains(Integer.valueOf(x))){
                x = random.nextInt(recipes.size());
            }
            numbers.add(x);
            current++;
            CheckBox checkBox = new CheckBox(this);
            checkBox.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT));
            checkBox.setText(recipes.get(x).getName());
            checkBox.setTextSize(30);
            checkBox.setTextColor(Color.parseColor("#ffffff"));
            checkBoxList.add(checkBox);
            ((ViewGroup)((ViewGroup)findViewById(android.R.id.content)).getChildAt(0)).addView(checkBox);
        }

    }
}
