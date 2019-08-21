package com.example.cr;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.cr.db.Favourite;
import com.example.cr.db.Recipe;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;


/**
 * project name：CocktailRecipe
 * className：
 * author：shuoyang
 * Date：2019-08-06 00:23
 */
public class FavouriteActivity extends AppCompatActivity {

    private RecyclerView recyclerview;
    private MyAdapter2 adapter;
    private List<Recipe> recipes = new ArrayList<>();
    private TextView back_btn;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);
        back_btn = findViewById(R.id.back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        recyclerview = findViewById(R.id.rv);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        try {
            Context otherContext = createPackageContext("com.example.cr", CONTEXT_IGNORE_SECURITY);
            sp = otherContext.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Store all the recipes that the user likes
        List<Favourite> favourites = DataSupport.where("user_id = ?",sp.getString("user_id","")).find(Favourite.class);
        for(int i = 0; i< favourites.size(); i++){
            recipes.add(DataSupport.where(" id = ? ",String.valueOf(favourites.get(i).getCocktail_id())).findFirst(Recipe.class));
        }
        adapter = new MyAdapter2(this,recipes);
        adapter.setHasStableIds(true);
        recyclerview.setAdapter(adapter);
    }
}
