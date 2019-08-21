package com.example.cr;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * project name：CocktailRecipe
 * className：
 * author：shuoyang
 * Date：2019-08-06 23:42
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private HomeFragment homeFragment;
    private GuessFragment guessFragment;
    private UserFragment userFragment;

    private RelativeLayout home_layout;
    private RelativeLayout guess_layout;
    private RelativeLayout user_layout;

    private ImageView home_image;
    private ImageView guess_image;
    private ImageView user_image;

    private TextView home_text;
    private TextView guess_text;
    private TextView user_text;

    private SharedPreferences sp;

    private int white = 0xFFFFFFFF;
    private int gray = 0xFF7597B3;

    private FragmentManager fManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        fManager = getSupportFragmentManager();
        initViews();

        Intent intent = getIntent();
        try {
            Context otherContext = createPackageContext("com.example.cr", CONTEXT_IGNORE_SECURITY);
            sp = otherContext.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String user_id = sp.getString("user_id","");
    }

    public void initViews(){
        home_layout = findViewById(R.id.home_layout);
        guess_layout = findViewById(R.id.guess_layout);
        user_layout = findViewById(R.id.user_layout);
        home_image = findViewById(R.id.home_image);
        guess_image = findViewById(R.id.guess_image);
        user_image = findViewById(R.id.user_image);
        home_text = findViewById(R.id.home_text);
        guess_text = findViewById(R.id.guess_text);
        user_text = findViewById(R.id.user_text);
        home_layout.setOnClickListener(this);
        guess_layout.setOnClickListener(this);
        user_layout.setOnClickListener(this);
        FragmentTransaction transaction=fManager.beginTransaction();
        clearChoice();
        hideFragments(transaction);
        home_image.setImageResource(R.drawable.home_pressed);
        if(homeFragment==null){
            homeFragment=new HomeFragment();
            transaction.add(R.id.content,homeFragment);
        }else{
            transaction.show(homeFragment);
        }
        transaction.commit();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.home_layout:
                setChoiceItem(0);
                break;
            case R.id.guess_layout:
                setChoiceItem(1);
                break;
            case R.id.user_layout:
                setChoiceItem(2);
                break;
            default:
                break;
        }
    }

    public void setChoiceItem(int index){
        FragmentTransaction transaction=fManager.beginTransaction();
        clearChoice();
        hideFragments(transaction);
        switch (index){
            case 0:
                home_image.setImageResource(R.drawable.home_pressed);

                if(homeFragment==null){
                    homeFragment=new HomeFragment();
                    transaction.add(R.id.content,homeFragment);
                }else{
                    transaction.show(homeFragment);
                }
                break;
            case 1:
                guess_image.setImageResource(R.drawable.guess_pressed);

                if(guessFragment==null){
                    guessFragment=new GuessFragment();
                    transaction.add(R.id.content,guessFragment);
                }else{
                    transaction.show(guessFragment);
                }
                break;
            case 2:
                user_image.setImageResource(R.drawable.user_pressed);

                if(userFragment==null){
                    userFragment=new UserFragment();
                    transaction.add(R.id.content,userFragment);
                }else{
                    transaction.show(userFragment);
                }
                break;
        }
        transaction.commit();
    }

    private void hideFragments(FragmentTransaction transaction){
        if(homeFragment!=null){
            transaction.hide(homeFragment);
        }
        if(guessFragment!=null){
            transaction.hide(guessFragment);
        }
        if(userFragment!=null){
            transaction.hide(userFragment);
        }
    }

    public void clearChoice(){
        home_image.setImageResource(R.drawable.home_normal);
        home_layout.setBackgroundColor(white);
        home_text.setTextColor(gray);
        guess_image.setImageResource(R.drawable.guess_normal);
        guess_layout.setBackgroundColor(white);
        guess_text.setTextColor(gray);
        user_image.setImageResource(R.drawable.user_normal);
        user_layout.setBackgroundColor(white);
        user_text.setTextColor(gray);
    }
}
