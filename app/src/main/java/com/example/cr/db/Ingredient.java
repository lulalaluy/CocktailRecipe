package com.example.cr.db;

import org.litepal.crud.DataSupport;

/**
 * project name：CocktailRecipe
 * className：
 * author：shuoyang
 * Date：2019-08-05 20:50
 */
public class Ingredient extends DataSupport {

    private int id;
    private String name;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean equals(Object obj)
    {
        if(!(obj instanceof Ingredient))
            return false;

        Ingredient p= (Ingredient)obj;
        return this.name.equals(p.getName());
    }

}
