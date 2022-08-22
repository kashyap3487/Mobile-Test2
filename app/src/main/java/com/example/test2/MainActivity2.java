package com.example.test2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.test2.databinding.ActivityMain2Binding;
import com.example.test2.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class MainActivity2 extends AppCompatActivity {
    private DbHelper dbHelper;
    private List<SubClass> subcat ;
    private ListView categoryList;
    private ActivityMain2Binding binding;
    private Integer transferredId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        binding = ActivityMain2Binding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        categoryList = binding.categoriesLv1;
        dbHelper = new DbHelper(this);
        Intent intent = getIntent();
        String transferredName = intent.getExtras().getString("Category_name");
        transferredId = intent.getExtras().getInt("ID");
        this.setTitle(transferredName);
        Toast.makeText(this, ""+transferredName, Toast.LENGTH_SHORT).show();
        loadact2();
    }

    public void loadact2(){
        Cursor cursor = dbHelper.getSubCategory(transferredId);
        subcat=new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {

                subcat.add(
                        new SubClass(
                                cursor.getInt(0),
                                cursor.getString(1),
                                cursor.getInt(2)

                        )
                );
            } while (cursor.moveToNext());
            cursor.close();
            SubCatAdaptor adapter = new SubCatAdaptor(this,
                    R.layout.subcat_list,
                    subcat,
                    this);

            categoryList.setAdapter(adapter);
        }
    }

}