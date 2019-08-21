package com.example.cr;

import android.app.Application;
import android.content.Context;
import android.content.res.AssetManager;

import com.example.cr.db.Ingredient;
import com.example.cr.db.Recipe;


import org.json.JSONArray;
import org.json.JSONObject;
import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * project name：CocktailRecipe
 * className：
 * author：shuoyang
 * Date：2019-08-07 16:11
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        LitePal.initialize(this);
        String json = getJson(this,"recipes.json");
        List<Recipe> recipes = DataSupport.findAll(Recipe.class);
        if(recipes.size()==0) {
            List<Ingredient> ingredients = new ArrayList<>();
            try {
                JSONArray array = new JSONArray(json);
                for (int i = 0; i < array.length(); i++) {
                    Recipe recipe = new Recipe();
                    JSONObject jsonObject = array.getJSONObject(i);
                    recipe.setName(jsonObject.getString("name"));
                    recipe.setType(jsonObject.getString("type"));
                    recipe.setIngredients(jsonObject.getString("ingredients"));
                    JSONArray ingredientArray = jsonObject.getJSONArray("ingredients");
                    for (int j = 0; j < ingredientArray.length(); j++) {
                        JSONObject ingredientJson = ingredientArray.getJSONObject(j);
                        Ingredient ingredient = new Ingredient();
                        ingredient.setName(ingredientJson.getString("name").replace(",", ""));
                        if (!ingredients.contains(ingredient)){
                            ingredient.save();
                        }
                    }
                    recipe.save();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public static String getJson(Context context, String fileName){
        StringBuilder stringBuilder = new StringBuilder();
        //Get the assets resource manager
        AssetManager assetManager = context.getAssets();
        //Read json file content with IO stream
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName),"utf-8"));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

}
