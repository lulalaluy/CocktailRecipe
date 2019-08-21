package com.example.cr;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cr.db.User;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * project name：CocktailRecipe
 * className：
 * author：shuoyang
 * Date：2019-08-07 20:44
 */
public class RegActivity extends AppCompatActivity {

    private EditText username_text;
    private EditText password_text;
    private EditText name_text;

    private Button back_btn;
    private Button reg_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);
        username_text = findViewById(R.id.username_text);
        password_text = findViewById(R.id.password_text);
        name_text = findViewById(R.id.name_text);
        back_btn = findViewById(R.id.back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        reg_btn = findViewById(R.id.reg_btn);
        reg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(TextUtils.isEmpty(username_text.getText())||TextUtils.isEmpty(password_text.getText())||TextUtils.isEmpty(name_text.getText())){
                    Toast.makeText(RegActivity.this,"Account or password or nickname cannot be empty!",Toast.LENGTH_SHORT).show();
                    return;
                }

                List<User> users = DataSupport.where("username=?",username_text.getText().toString()).find(User.class);
                if(users.size()!=0){
                    Toast.makeText(RegActivity.this,"The account already exists, please change！",Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    User user = new User();
                    user.setName(name_text.getText().toString());
                    user.setUsername(username_text.getText().toString());
                    user.setPassword(password_text.getText().toString());
                    user.save();
                    Toast.makeText(RegActivity.this,"success！",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }


}
