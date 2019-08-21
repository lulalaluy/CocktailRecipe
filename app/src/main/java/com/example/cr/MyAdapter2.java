package com.example.cr;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cr.db.Recipe;

import org.json.JSONArray;

import java.util.List;

/**
 * project name：CocktailRecipe
 * className：
 * author：shuoyang
 * Date：2019-08-08 14:20
 */
public class MyAdapter2 extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Activity context;
    private List<Recipe> recipes;
    private double value  = 0;

    public MyAdapter2(Activity context, List<Recipe> recipes){
        this.context = context;
        this.recipes = recipes;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

         TextView name_text;
         TextView type_text;
         LinearLayout content;

        public MyViewHolder(View itemView) {
            super(itemView);
            name_text = itemView.findViewById(R.id.name_text);
            type_text = itemView.findViewById(R.id.type_text);
            content = itemView.findViewById(R.id.content);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.ingredient_item2,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        MyViewHolder vh = (MyViewHolder)holder;
        vh.content.removeAllViews();
        vh.name_text.setText(recipes.get(position).getName());
        vh.type_text.setText(recipes.get(position).getType());
        SharedPreferences sp = null;
        try{
            try {
                Context otherContext = context.createPackageContext("com.example.cr", context.CONTEXT_IGNORE_SECURITY);
                sp = otherContext.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
            } catch (Exception e) {
                e.printStackTrace();
            }
            final String user_id = sp.getString("user_id","");
            final JSONArray array = new JSONArray(recipes.get(position).getIngredients());
            for(int i=0;i<array.length();i++){
                RelativeLayout relativeLayout = new RelativeLayout(context);
                RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                lp2.setMargins(0,15,0,0);
                relativeLayout.setLayoutParams(lp2);
                relativeLayout.setGravity(RelativeLayout.CENTER_VERTICAL);
                TextView textView = new TextView(context);
                textView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                textView.setText(array.getJSONObject(i).getString("quantity")+" "+array.getJSONObject(i).getString("name"));
                textView.setTextColor(Color.parseColor("#000000"));
                textView.setTextSize(18);
                textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                relativeLayout.addView(textView);
                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                vh.content.addView(relativeLayout);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @Override
    public int getItemCount() {
        return recipes.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


}
