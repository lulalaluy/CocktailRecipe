package com.example.cr;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cr.db.Favourite;
import com.example.cr.db.Rating;
import com.example.cr.db.Recipe;
import com.example.cr.db.User;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * project name：CocktailRecipe
 * className：
 * author：shuoyang
 * Date：2019-08-08 02:10
 */
public class GuessFragment extends Fragment {

    private RecyclerView recyclerview;
    private MyAdapter adapter;
    private List<Recipe> recipes = new ArrayList<>();
    private SharedPreferences sp;
    private List<Recipe> recipes1 = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_guess,container,false);
        recyclerview = view.findViewById(R.id.recyclerview);
        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        recipes = DataSupport.findAll(Recipe.class);
        try {
            Context otherContext = getActivity().createPackageContext("com.example.cr", getActivity().CONTEXT_IGNORE_SECURITY);
            sp = otherContext.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        adapter = new MyAdapter(getActivity(),recipes1);
        adapter.setHasStableIds(true);
        recyclerview.setAdapter(adapter);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        List<List<Integer>> list = new ArrayList<>(); // Evaluation matrix
        List<List<Integer>> list2 = new ArrayList<>(); // Joint matrix
        List<Integer> list3 = new ArrayList<>(); // The total number of people who like a recipe
        List<List<Double>> list4 = new ArrayList<>(); // Similar matrix
        List<User> users = DataSupport.findAll(User.class);
        for(User user:users){
            List<Integer> integers = new ArrayList<>();
            double acount = DataSupport.where("user_id = ?"
                    ,String.valueOf(user.getId())).average(Rating.class,"rating");
            for(int i=0;i<recipes.size();i++){
                List<Favourite> favourite = DataSupport.where("user_id = ? and cocktail_id = ?"
                        ,String.valueOf(user.getId()),String.valueOf(recipes.get(i).getId())).find(Favourite.class);
                if(favourite.size()==0){
                    List<Rating> rating = DataSupport.where("user_id = ? and cocktail_id = ?"
                            ,String.valueOf(user.getId()),String.valueOf(recipes.get(i).getId())).find(Rating.class);
                    if(rating.size()==0){
                        integers.add(0);
                    }else{
                        if(rating.get(0).getRating()<acount){
                            integers.add(0);
                        }else{
                            integers.add(1);
                        }
                    }
                }else{
                    integers.add(1);
                } // modify rating from 1-5 to 0/1
            }
            list.add(integers);
        }

        for(int i=0;i<recipes.size();i++){
            List<Integer> integers = new ArrayList<>();
            for(int j=0;j<recipes.size();j++){
                int c=0;
                for(List<Integer> listw:list){
                    if(listw.get(i)==1&&listw.get(j)==1){
                        c++;
                    }
                }
                integers.add(c);
            }
            list2.add(integers);
        }

        for(int j=0;j<recipes.size();j++){
            int c=0;
            for(List<Integer> listw:list){
                if(listw.get(j)==1){
                    c++;
                }
            }
            list3.add(c);
        }

        for(int i=0;i<recipes.size();i++){
            List<Double> integers = new ArrayList<>();
            for(int j=0;j<recipes.size();j++){
                if(list3.get(i)==0||list3.get(j)==0){
                    integers.add(Double.valueOf(0));
                }else {
                    integers.add(list2.get(i).get(j) / (Math.sqrt(list3.get(i) * list3.get(j))));
                }
            }
            list4.add(integers);
            String s = "";
            for(int m=0;m<integers.size();m++){
                s=s+String.valueOf(integers.get(m));
            }
            System.out.println(s);
            System.out.println("-------------------------------------------------------------");
        }

        System.out.println("sdfids"+sp.getString("user_id",""));
        List<Integer> judges = list.get(Integer.valueOf(sp.getString("user_id",""))-1);
        for(int i=0;i<judges.size();i++){
            if(judges.get(i)==0){

                for(int j=0;j<judges.size();j++){

                    if(judges.get(j)==1){
                        if(list4.get(i).get(j)>0){
                            recipes1.add(recipes.get(i));
                        }
                    }


                }


            }
        }
        adapter.notifyDataSetChanged();
    }
}