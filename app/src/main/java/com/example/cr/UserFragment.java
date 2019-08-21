package com.example.cr;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.cr.db.User;

import org.litepal.crud.DataSupport;

/**
 * project name：CocktailRecipe
 * className：
 * author：shuoyang
 * Date：2019-08-06 03:16
 */
public class UserFragment extends Fragment implements View.OnClickListener{

    private RelativeLayout password_layout;
    private RelativeLayout name_layout;
    private RelativeLayout favourite_layout;
    private RelativeLayout eva_layout;
    private RelativeLayout quit_layout;
    private SharedPreferences sp;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user,container,false);
        password_layout = view.findViewById(R.id.password_layout);
        name_layout = view.findViewById(R.id.name_layout);
        favourite_layout = view.findViewById(R.id.favourite_layout);
        eva_layout = view.findViewById(R.id.eva_layout);
        quit_layout = view.findViewById(R.id.quit_layout);
        password_layout.setOnClickListener(this);
        name_layout.setOnClickListener(this);
        favourite_layout.setOnClickListener(this);
        eva_layout.setOnClickListener(this);
        quit_layout.setOnClickListener(this);
        return view;
    }



    @Override
    public void onClick(View view) {
        try {
            Context otherContext = getActivity().createPackageContext("com.example.cr", getActivity().CONTEXT_IGNORE_SECURITY);
            sp = otherContext.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        final String user_id = sp.getString("user_id","");
        final LayoutInflater factory = LayoutInflater.from(getActivity());
        switch (view.getId()){
            case R.id.password_layout:
                final View view2 = factory.inflate(R.layout.editbox_layout, null);
                final EditText edit=view2.findViewById(R.id.editText);
                edit.setInputType(InputType.TYPE_CLASS_TEXT| InputType.TYPE_TEXT_VARIATION_PASSWORD);
                AlertDialog dialog = new AlertDialog.Builder(getActivity())
                        .setTitle("change password")
                        .setView(view2)
                        .setPositiveButton("OK",
                                new android.content.DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        ContentValues values = new ContentValues();
                                        values.put("password", edit.getText().toString());
                                        DataSupport.update(User.class, values,Integer.valueOf(user_id) );

                                    }
                                }).setNegativeButton("CANCLE", null).create();
                WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
                params.width = 200;
                params.height = 200 ;
                dialog.show();
                break;
            case R.id.name_layout:
                final View view3 = factory.inflate(R.layout.editbox_layout, null);
                final EditText edit2=view3.findViewById(R.id.editText);
                AlertDialog dialog2 = new AlertDialog.Builder(getActivity())
                        .setTitle("change username")
                        .setView(view3)
                        .setPositiveButton("OK",
                                new android.content.DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        ContentValues values = new ContentValues();
                                        values.put("name", edit2.getText().toString());
                                        DataSupport.update(User.class, values,Integer.valueOf(user_id) );
                                    }
                                }).setNegativeButton("CANCLE", null).create();
                WindowManager.LayoutParams params2= dialog2.getWindow().getAttributes();
                params2.width = 200;
                params2.height = 200 ;
                dialog2.show();
                break;
            case R.id.favourite_layout:
                startActivity(new Intent(getActivity(), FavouriteActivity.class));
                break;
            case R.id.eva_layout:
                startActivity(new Intent(getActivity(), RatingActivity.class));
                break;
            case R.id.quit_layout:
                final AlertDialog.Builder normalDialog =  new AlertDialog.Builder(getActivity());
                normalDialog.setTitle("MESSAGE");
                normalDialog.setMessage("Want to quit?");
                normalDialog.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                getActivity().finish();
                            }
                        });
                normalDialog.setNegativeButton("CANCLE",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                normalDialog.show();
                break;
        }
    }
}