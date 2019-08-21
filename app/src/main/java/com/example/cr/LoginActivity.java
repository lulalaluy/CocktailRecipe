package com.example.cr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cr.db.User;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * project name：CocktailRecipe
 * className：
 * author：shuoyang
 * Date：2019-08-06 21:31
 */
public class LoginActivity extends AppCompatActivity {

    private Button login_btn;
    private Button reg_btn;
    private TextView username_text;
    private TextView password_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login_btn = findViewById(R.id.login_btn);
        reg_btn = findViewById(R.id.reg_btn);
        username_text = findViewById(R.id.username_text);
        password_text = findViewById(R.id.password_text);
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(username_text.getText())||TextUtils.isEmpty(password_text.getText())){
                    Toast.makeText(LoginActivity.this,"Account or password cannot be empty!",Toast.LENGTH_SHORT).show();
                    return;
                }
                List<User> users = DataSupport.where("username=?",username_text.getText().toString()).find(User.class);
                if(users.size()==0){
                    Toast.makeText(LoginActivity.this,"The account does not exist, please register!",Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    List<User> loginUser = DataSupport.where
                            ("username=? and password=?",username_text.getText().toString(),password_text.getText().toString()).find(User.class);
                    if(loginUser.size()!=0){
                        SharedPreferences sp=LoginActivity.this.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
                        SharedPreferences.Editor edit=sp.edit();
                        edit.putString("user_id",String.valueOf(loginUser.get(0).getId()));
                        edit.commit();
                        startActivity(new Intent(LoginActivity.this,GuideActivity.class));
                    }else{
                        Toast.makeText(LoginActivity.this,"The account password is incorrect!",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        reg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,RegActivity.class));
            }
        });
    }
}
