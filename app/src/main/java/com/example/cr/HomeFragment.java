package com.example.cr;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cr.db.Recipe;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * project name：CocktailRecipe
 * className：
 * author：shuoyang
 * Date：2019-08-06 14:23
 */
public class HomeFragment extends Fragment {

    private RecyclerView recyclerview;
    private MyAdapter adapter;
    private List<Recipe> recipes = new ArrayList<>();
    private Button find_btn;
    private TextView info_text;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,container,false);
        recyclerview = view.findViewById(R.id.recyclerview);
        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        recipes = DataSupport.findAll(Recipe.class);
        adapter = new MyAdapter(getActivity(),recipes);
        adapter.setHasStableIds(true);
        recyclerview.setAdapter(adapter);
        find_btn = view.findViewById(R.id.find_btn);
        info_text = view.findViewById(R.id.info_text);
        find_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recipes.clear();
                recipes.addAll(DataSupport.where("name like '%"+info_text.getText().toString()+"%' ").find(Recipe.class));
                adapter.notifyDataSetChanged();
            }
        });
        return view;
    }




}