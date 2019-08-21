package com.example.cr.db;

import org.litepal.crud.DataSupport;

/**
 * project name：CocktailRecipe
 * className：
 * author：shuoyang
 * Date：2019-08-05 21:03
 */
public class Recipe extends DataSupport {

    private int id;
    private String name;
    private String type;
    private String ingredients;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }



}
