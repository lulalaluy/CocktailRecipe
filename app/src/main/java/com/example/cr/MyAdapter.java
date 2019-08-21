package com.example.cr;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cr.db.Favourite;
import com.example.cr.db.Favourite2;
import com.example.cr.db.Ingredient;
import com.example.cr.db.Rating;
import com.example.cr.db.Rating2;
import com.example.cr.db.Recipe;
import com.example.cr.db.User;


import org.json.JSONArray;
import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * project name：CocktailRecipe
 * className：
 * author：shuoyang
 * Date：2019-08-06 23:44
 */
public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Activity context;
    private List<Recipe> recipes;
    private double value  = 0;

    public MyAdapter(Activity context, List<Recipe> recipes){
        this.context = context;
        this.recipes = recipes;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

         TextView name_text;
         TextView type_text;
         LinearLayout content;
         Button eva_btn;
         Button favourite_btn;

        public MyViewHolder(View itemView) {
            super(itemView);
            name_text = itemView.findViewById(R.id.name_text);
            type_text = itemView.findViewById(R.id.type_text);
            content = itemView.findViewById(R.id.content);
            eva_btn = itemView.findViewById(R.id.eva_btn);
            favourite_btn = itemView.findViewById(R.id.favourite_btn);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.ingredient_item,parent,false);
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
                LinearLayout linearLayout = new LinearLayout(context);
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                Button button = new Button(context);
                button.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT));
                button.setText("rating");
                button.setTextColor(Color.parseColor("#ffffff"));
                button.setBackgroundResource(R.drawable.white_bg);
                button.setPadding(15,   0,15,0);
                final String name = array.getJSONObject(i).getString("name");
                if(DataSupport.where("cocktail_id= ? and user_id = ?",
                        String.valueOf(DataSupport.where("name= ? ",name).findFirst(Ingredient.class).getId()),user_id).find(Rating2.class).size()!=0)
                {
                    button.setText("rated");
                    button.setEnabled(false);
                }
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final String radioItems[] = new String[]{"0","1","2","3","4","5"};
                        AlertDialog.Builder radioDialog = new AlertDialog.Builder(context);
                        radioDialog.setTitle("rating");
                        radioDialog.setIcon(R.mipmap.ic_launcher_round);
                        radioDialog.setSingleChoiceItems(radioItems, 0, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(context,radioItems[which],Toast.LENGTH_SHORT).show();
                                value = Double.valueOf(radioItems[which]);
                            }
                        });

                        //setting button
                        radioDialog.setPositiveButton("OK"
                                , new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Rating2 rating2 = new Rating2();
                                        rating2.setUser_id(Integer.valueOf(user_id));
                                        Ingredient ingredient = DataSupport.where("name= ?",name).findFirst(Ingredient.class);
                                        rating2.setCocktail_id(ingredient.getId());
                                        System.out.println(recipes.get(position).getId());
                                        rating2.setRating(value);
                                        rating2.save();
                                        dialog.dismiss();
                                        notifyDataSetChanged();
                                    }
                                });
                        radioDialog.create().show();
                    }
                });
                Button button2 = new Button(context);
                button2.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT));
                button2.setText("preference");
                button2.setTextColor(Color.parseColor("#ffffff"));
                button2.setBackgroundResource(R.drawable.white_bg);
                button2.setPadding(15,0,15,0);
                if(DataSupport.where("cocktail_id= ? and user_id = ?",
                        String.valueOf(DataSupport.where("name= ? ",name).findFirst(Ingredient.class).getId()),user_id).find(Favourite2.class).size()!=0)
                {
                    button2.setText("chosen");
                    button2.setEnabled(false);
                }
                button2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final String radioItems[] = new String[]{"Favourite","Not Favourite"};
                        AlertDialog.Builder radioDialog = new AlertDialog.Builder(context);
                        radioDialog.setTitle("preference");
                        radioDialog.setIcon(R.mipmap.ic_launcher_round);
                        radioDialog.setSingleChoiceItems(radioItems, 0, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(context,radioItems[which],Toast.LENGTH_SHORT).show();
                                value=(which==0?1:0);
                            }
                        });
                        radioDialog.setPositiveButton("OK"
                                , new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        Favourite2 rating2 = new Favourite2();
                                        rating2.setUser_id(Integer.valueOf(user_id));
                                        Ingredient ingredient = DataSupport.where("name= ?",name).findFirst(Ingredient.class);
                                        rating2.setCocktail_id(ingredient.getId());
                                        System.out.println(recipes.get(position).getId());
                                        rating2.setRating(value);
                                        rating2.save();
                                        dialog.dismiss();
                                        notifyDataSetChanged();
                                    }
                                });
                        radioDialog.create().show();
                    }
                });
                linearLayout.addView(button);
                linearLayout.addView(button2);
                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                relativeLayout.addView(linearLayout,lp);
                vh.content.addView(relativeLayout);
                final String name2 = recipes.get(position).getName();
                if(DataSupport.where("cocktail_id= ? and user_id = ?",
                        String.valueOf(DataSupport.where("name= ?",name2).findFirst(Recipe.class).getId()),user_id).find(Rating.class).size()!=0)
                {
                    vh.eva_btn.setText("rated");
                    vh.eva_btn.setEnabled(false);
                }
                vh.eva_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final String radioItems[] = new String[]{"0","1","2","3","4","5"};
                        AlertDialog.Builder radioDialog = new AlertDialog.Builder(context);
                        radioDialog.setTitle("rating");
                        radioDialog.setIcon(R.mipmap.ic_launcher_round);
                        radioDialog.setSingleChoiceItems(radioItems, 0, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(context,radioItems[which],Toast.LENGTH_SHORT).show();
                                value = Double.valueOf(radioItems[which]);
                            }
                        });
                        radioDialog.setPositiveButton("OK"
                                , new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        Rating rating = new Rating();
                                        rating.setUser_id(Integer.valueOf(user_id));
                                        rating.setCocktail_id(recipes.get(position).getId());
                                        rating.setRating(value);
                                        rating.save();
                                        dialog.dismiss();
                                        notifyDataSetChanged();
                                    }
                                });
                        radioDialog.create().show();
                    }
                });
                if(DataSupport.where("cocktail_id= ? and user_id = ?",
                        String.valueOf(DataSupport.where("name= ?",name2).findFirst(Recipe.class).getId()),user_id).find(Favourite.class).size()!=0)
                {
                    vh.favourite_btn.setText("chosen");
                    vh.favourite_btn.setEnabled(false);
                }
                vh.favourite_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final String radioItems[] = new String[]{"Favourite","Not Favourite"};
                        AlertDialog.Builder radioDialog = new AlertDialog.Builder(context);
                        radioDialog.setTitle("preference");
                        radioDialog.setIcon(R.mipmap.ic_launcher_round);
                        radioDialog.setSingleChoiceItems(radioItems, 0, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(context,radioItems[which],Toast.LENGTH_SHORT).show();
                                value=(which==0?1:0);
                            }
                        });
                        //设置按钮
                        radioDialog.setPositiveButton("OK"
                                , new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        Favourite rating = new Favourite();
                                        rating.setUser_id(Integer.valueOf(user_id));
                                        rating.setCocktail_id(recipes.get(position).getId());
                                        System.out.println(recipes.get(position).getId());
                                        rating.setRating(value);
                                        rating.save();
                                        dialog.dismiss();
                                        notifyDataSetChanged();
                                    }
                                });
                        radioDialog.create().show();
                    }
                });
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
