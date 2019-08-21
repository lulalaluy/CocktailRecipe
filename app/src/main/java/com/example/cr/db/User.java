package com.example.cr.db;

import org.litepal.crud.DataSupport;

/**
 * project name：CocktailRecipe
 * className：
 * author：shuoyang
 * Date：2019-08-06 00:20
 */
public class User extends DataSupport {

    private int id;
    private String username;
    private String password;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
