package com.example.test2;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import java.util.List;

public class CategoryAdapter extends ArrayAdapter {
    private List<Categories> categories_list;
    private Context context;
    private int layoutRes;
    private MainActivity act;
    private DbHelper dbHelper;
    private SQLiteDatabase sqLiteDatabase;
    private String req_category_name;


    public CategoryAdapter(@NonNull Context context, int resource, @NonNull List<Categories> categoryList, MainActivity mainActivity) {
        super(context, resource, categoryList);
        this.context = context;
        this.layoutRes = resource;
        this.categories_list = categoryList;
        this.act = mainActivity;
    }
    @Override
    public int getCount() {
        return categories_list.size();
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = convertView;
        if (v == null) v = inflater.inflate(layoutRes, null);
        Button btn = v.findViewById(R.id.list_btn);
        TextView btn_del = v.findViewById(R.id.btn_delete);
        Button btn_edit = v.findViewById(R.id.btn_edit);

        btn.setText(categories_list.get(position).getName());

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper = new DbHelper(act);
                String CategoryName = categories_list.get(position).getName();
                Categories cati = dbHelper.getCategory(CategoryName);
                Intent i;
                i = new Intent(act,MainActivity2.class);
                i.putExtra("ID", cati.getId());
                i.putExtra("Category_name", categories_list.get(position).getName());
                context.startActivity(i);
            }
        });

        btn_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper = new DbHelper(act);
                int cat_id = categories_list.get(position).getId();
                dbHelper.deleteCat(cat_id);
                categories_list.remove(position);
                notifyDataSetChanged();
                act.loadCat();
            }
        });
        return v;
    }
}
