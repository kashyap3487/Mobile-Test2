package com.example.test2;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.test2.databinding.ActivityMainBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity{
    private ActivityMainBinding binding;
    private String category_name;
    private String note_name;
    private List<Categories> cat = new ArrayList<>();
    private ListView categoryList;
    private ListView subCatView;
    private  String notes;
    private static final String DB_NAME = "TodoList1";
    private DbHelper dbHelper;
    private SQLiteDatabase sqLiteDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        sqLiteDatabase = openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null);
        dbHelper = new DbHelper(this);
        categoryList = binding.mainLv;
        subCatView = findViewById(R.id.categories_lv1);
         FloatingActionButton add_note = binding.btnToDo;

        loadCat();
        add_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDialog();
            }
        });
    }

    private void createDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.dialogbox,null);
        builder.setView(view);
        Button btn = view.findViewById(R.id.btn_to_do);

        AlertDialog dialog = builder.create();

        dialog.show();
        EditText note = view.findViewById(R.id.dialogbox_note_edt);
        Spinner sp = view.findViewById(R.id.spinner_categories);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.categories, android.R.layout.simple_dropdown_item_1line);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        sp.setAdapter(adapter);
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category_name = parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        view.findViewById(R.id.btn_add_note).setOnClickListener(v -> {

            notes = note.getText().toString().trim();
            if(notes.isEmpty()){
                Toast.makeText(this, "Please Enter Notes", Toast.LENGTH_SHORT).show();
            }
            else if(category_name.equals("Please select a category")){
                Toast.makeText(this, "Please Select Category", Toast.LENGTH_SHORT).show();
            }
            else{
                Categories catigory = new Categories(category_name);


                if(dbHelper.addCategories(category_name)){
                    Toast.makeText(this, "Todo list added Successfully", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this, "Error Adding in Todo list", Toast.LENGTH_SHORT).show();
                }

                Categories cati = dbHelper.getCategory(category_name);
                if(dbHelper.addSubCategories(notes, cati.getId())){
                    Toast.makeText(this, "Your subcategory has been added", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this, "Error Adding  sub cat your Order", Toast.LENGTH_SHORT).show();
                }
                loadCat();
                dialog.dismiss();
            }
        });
    }
    public void loadCat() {
        cat=new ArrayList<>();
        Cursor cursor = dbHelper.getAllCategory();
        if (cursor.moveToFirst()) {
            do {
                cat.add(
                        new Categories(
                                cursor.getInt(0),
                                cursor.getString(1)

                        )
                );
            } while (cursor.moveToNext());
            cursor.close();
            CategoryAdapter adapter = new CategoryAdapter(this,
                    R.layout.categories_list,
                    cat,
                    MainActivity.this);

            adapter.notifyDataSetChanged();

            categoryList.setAdapter(adapter);
        }
    }


}